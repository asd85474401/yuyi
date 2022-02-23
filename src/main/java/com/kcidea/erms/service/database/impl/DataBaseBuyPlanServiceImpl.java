package com.kcidea.erms.service.database.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.result.Result;
import com.kcidea.erms.common.util.ConvertUtil;
import com.kcidea.erms.common.util.DownloadUtil;
import com.kcidea.erms.dao.database.DatabaseBaseInfoDao;
import com.kcidea.erms.dao.database.DatabaseBuyPlanDao;
import com.kcidea.erms.dao.database.DatabaseBuyPlanRelDao;
import com.kcidea.erms.dao.database.DatabaseEvaluationDao;
import com.kcidea.erms.domain.database.DatabaseBaseInfo;
import com.kcidea.erms.domain.database.DatabaseBuyPlan;
import com.kcidea.erms.domain.database.DatabaseBuyPlanRel;
import com.kcidea.erms.domain.ers.Agent;
import com.kcidea.erms.domain.ers.Company;
import com.kcidea.erms.enums.common.EnumTrueFalse;
import com.kcidea.erms.enums.database.EnumCheckState;
import com.kcidea.erms.enums.database.EnumEvaluationResult;
import com.kcidea.erms.enums.fund.EnumOrderType;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.database.buyplan.*;
import com.kcidea.erms.model.database.evaluation.DatabaseEvaluationInfoModel;
import com.kcidea.erms.model.database.DatabasePropertyModel;
import com.kcidea.erms.service.common.BaseService;
import com.kcidea.erms.service.database.DataBaseBuyPlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/06
 **/
@Service
public class DataBaseBuyPlanServiceImpl extends BaseService implements DataBaseBuyPlanService {

    @Resource
    private DatabaseBuyPlanDao databaseBuyPlanDao;

    @Resource
    private DatabaseBuyPlanRelDao databaseBuyPlanRelDao;

    @Resource
    private DatabaseEvaluationDao databaseEvaluationDao;

    @Resource
    private DatabaseBaseInfoDao databaseBaseInfoDao;

    @Value("${my-config.temp-path}")
    private String TEMP_PATH;


    /**
     * 采购计划列表
     *
     * @param sid       学校id
     * @param vYear     年份
     * @param title     采购计划名称
     * @param state     审核状态
     * @param checkFlag 审核标识
     * @param pageNum   页码
     * @param pageSize  每页数量
     * @return 采购计划列表
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @Override
    public PageResult<DatabaseBuyListModel> findBuyPlanList(Long sid, Integer vYear, String title, Integer state,
                                                            boolean checkFlag, Integer pageNum, Integer pageSize) {
        // 校验参数
        checkSid(sid);
        checkPageParam(pageNum, pageSize);

        Page<DatabaseBuyListModel> page = new Page<>(pageNum, pageSize);

        // 查询数据
        List<DatabaseBuyListModel> list =
                databaseBuyPlanDao.findListByYearTitleStateSidCheckFlagPage(vYear, title, state, sid, checkFlag, page);

        // 组装数据
        for (DatabaseBuyListModel model : list) {
            DatabaseBuyListModel databaseBuyPlanModel =
                    databaseBuyPlanRelDao.findDataBasePriceCountBySidPlanId(sid, model.getId());
            // 数据库数量
            model.setDatabaseCount(databaseBuyPlanModel.getDatabaseCount());
            // 数据库总价
            model.setMoney(super.moneyIntoTenThousand(databaseBuyPlanModel.getMoney()));
            // 审核状态名
            EnumCheckState checkState = EnumCheckState.getCheckState(model.getState());
            model.setStateName(checkState == null ? "" : checkState.getName());
        }

        // 分页返回
        PageResult<DatabaseBuyListModel> result = new PageResult<>();
        return result.success(list, page.getTotal());
    }

    /**
     * 年度采购计划数据库列表
     *
     * @param sid      学校id
     * @param planId   采购计划id
     * @param did      数据库id
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 采购计划列表
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @Override
    public PageResult<DatabaseBuyListModel> findBuyPlanDataBaseList(Long sid, Long planId, Long did,
                                                                    Integer pageNum, Integer pageSize) {
        // 校验参数
        checkSid(sid);
        checkPageParam(pageNum, pageSize);

        Page<DatabaseBuyListModel> page = new Page<>(pageNum, pageSize);

        // 查询数据
        List<DatabaseBuyListModel> list = databaseBuyPlanRelDao.findListBySidPlanIdDidPage(sid, planId, did, page);

        // 数据库名称
        Map<Long, String> dbIdNameMap = super.getDidNameMapBySid(sid);

        // 组装数据
        for (DatabaseBuyListModel model : list) {
            // 数据库名称
            model.setName(dbIdNameMap.get(model.getDid()));
            // 数据库价格
            model.setPrice(super.moneyIntoTenThousand(model.getPrice()));
        }

        // 分页返回
        PageResult<DatabaseBuyListModel> result = new PageResult<>();
        return result.success(list, page.getTotal());
    }

    /**
     * 新增/编辑采购数据库
     *
     * @param sid    学校id
     * @param model  采购数据库详情
     * @param userId 操作人id
     * @return 新增/编辑的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @Override
    public String addOrUpdateDatabaseBuyPlanRel(Long sid, DatabaseBuyPlanRelInfoModel model, Long userId) {
        // 校验参数
        checkSidUserId(sid, userId);
        checkDid(model.getDId());

        boolean insertFlag = false;
        // 没有采购计划关系id则视为新增
        if (model.getId() == null) {
            insertFlag = true;
        }

        // 校验是否有重复数据库
        if (databaseBuyPlanRelDao.findCountBySidDidPlanIdPlanRelId(sid, model.getDId(), model.getPlanId(), model.getId()) != 0) {
            throw new CustomException("同一个采购计划中不允许出现重复的数据库");
        }

        if (insertFlag) {
            // 新增的情况
            DatabaseBuyPlanRel databaseBuyPlanRel = new DatabaseBuyPlanRel();
            // 复制参数，采购计划id，数据库id，预算金额，采购原因
            BeanUtils.copyProperties(model, databaseBuyPlanRel);
            databaseBuyPlanRel.setSId(sid);
            databaseBuyPlanRel.setCreatedBy(userId);
            databaseBuyPlanRel.setCreatedTime(LocalDateTime.now());
            databaseBuyPlanRelDao.insert(databaseBuyPlanRel);
            return Vm.INSERT_SUCCESS;
        } else {
            // 编辑的情况
            // 查询采购计划关系
            DatabaseBuyPlanRel databaseBuyPlanRel = databaseBuyPlanRelDao.findOneBySidPlanRelId(sid, model.getId());
            if (databaseBuyPlanRel != null) {
                // 查询采购计划
                Integer state = databaseBuyPlanDao.findStateBySidPlanId(sid, databaseBuyPlanRel.getPlanId());
                if (state == EnumCheckState.待审核.getValue()) {
                    throw new CustomException("审核期间将无法编辑");
                }
                // 复制参数，采购计划id，数据库id，预算金额，采购原因
                BeanUtils.copyProperties(model, databaseBuyPlanRel);
                // 编辑数据
                databaseBuyPlanRel.setUpdatedBy(userId);
                databaseBuyPlanRel.setUpdatedTime(LocalDateTime.now());
                databaseBuyPlanRelDao.updateById(databaseBuyPlanRel);
                return Vm.UPDATE_SUCCESS;
            } else {
                throw new CustomException(Vm.NO_DATA);
            }
        }
    }

    /**
     * 回显采购数据库
     *
     * @param sid       学校id
     * @param planRelId 采购关系id
     * @return 采购数据库
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @Override
    public DatabaseBuyPlanRelInfoModel findDatabaseBuyPlanRelOne(Long sid, Long planRelId) {
        // 校验参数
        super.checkSid(sid);

        // 查询采购计划关系
        DatabaseBuyPlanRel databaseBuyPlanRel = databaseBuyPlanRelDao.findOneBySidPlanRelId(sid, planRelId);

        if (databaseBuyPlanRel == null) {
            throw new CustomException(Vm.NO_DATA);
        }

        // 复制参数
        DatabaseBuyPlanRelInfoModel model = new DatabaseBuyPlanRelInfoModel();
        BeanUtils.copyProperties(databaseBuyPlanRel, model);
        return model;
    }

    /**
     * 新增/编辑采购计划
     *
     * @param sid    学校id
     * @param planId 采购计划id
     * @param title  采购计划标题
     * @param vYear  年份
     * @param userId 操作人id
     * @return 新增/编辑的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @Override
    public Result<IdNameModel> addOrUpdateDatabaseBuyPlan(Long sid, Long planId, String title, Integer vYear, Long userId) {
        // 校验参数
        checkSidUserId(sid, userId);

        // 返回对象
        Result<IdNameModel> result = new Result<>();

        boolean insertFlag = false;
        // 没有采购计划id则视为新增
        if (planId == null) {
            insertFlag = true;
        }

        if (databaseBuyPlanDao.findCountBySidTitleYearPlanId(sid, title, vYear, planId) != 0) {
            throw new CustomException("不允许出现重复名称的采购计划");
        }

        if (insertFlag) {
            // 新增的情况
            DatabaseBuyPlan databaseBuyPlan = new DatabaseBuyPlan();
            // 采购计划名称
            databaseBuyPlan.setTitle(title);
            // 审核状态默认值
            databaseBuyPlan.setState(EnumTrueFalse.否.getValue());
            // 学校id
            databaseBuyPlan.setSId(sid);
            // 年份
            databaseBuyPlan.setVyear(vYear);
            databaseBuyPlan.setCreatedBy(userId);
            databaseBuyPlan.setCreatedTime(LocalDateTime.now());
            databaseBuyPlanDao.insert(databaseBuyPlan);
            // 成功插入并返回
            result.successMsg(Vm.INSERT_SUCCESS);
            result.success(new IdNameModel().create(databaseBuyPlan.getId(), databaseBuyPlan.getTitle()));
            return result;
        } else {
            // 编辑的情况
            // 查询采购计划
            DatabaseBuyPlan databaseBuyPlan = databaseBuyPlanDao.findOneBySidPlanId(sid, planId);
            if (databaseBuyPlan != null) {
                if (databaseBuyPlan.getState().equals(EnumCheckState.待审核.getValue())) {
                    throw new CustomException("审核期间将无法编辑");
                }
                // 有更改才更新
                if (!databaseBuyPlan.getTitle().equals(title)) {
                    // 采购计划名称
                    databaseBuyPlan.setTitle(title);
                    databaseBuyPlan.setUpdatedBy(userId);
                    databaseBuyPlan.setUpdatedTime(LocalDateTime.now());
                    databaseBuyPlanDao.updateById(databaseBuyPlan);
                }
                result.successMsg(Vm.UPDATE_SUCCESS);
                result.success(new IdNameModel().create(databaseBuyPlan.getId(), databaseBuyPlan.getTitle()));
                return result;
            } else {
                throw new CustomException(Vm.ERROR_PARAMS);
            }
        }

    }

    /**
     * 回显采购计划
     *
     * @param sid    学校id
     * @param planId 采购计划id
     * @return 采购计划
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @Override
    public String findDatabaseBuyPlanOne(Long sid, Long planId) {
        // 校验参数
        super.checkSid(sid);

        // 查询采购计划
        DatabaseBuyPlan databaseBuyPlan = databaseBuyPlanDao.findOneBySidPlanId(sid, planId);
        if (databaseBuyPlan == null) {
            throw new CustomException(Vm.NO_DATA);
        }
        return databaseBuyPlan.getTitle();
    }

    /**
     * 删除采购计划
     *
     * @param sid    学校id
     * @param planId 采购计划id
     * @return 删除的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String deleteDatabaseBuyPlan(Long sid, Long planId) {
        // 校验参数
        checkSid(sid);
        databaseBuyPlanDao.deleteBySidPlanId(sid, planId);
        databaseBuyPlanRelDao.deleteBySidPlanId(sid, planId);
        return Vm.DELETE_SUCCESS;
    }

    /**
     * 删除采购数据库
     *
     * @param sid       学校id
     * @param planRelId 采购数据库详情
     * @return 删除的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @Override
    public String deleteDatabaseBuyPlanRel(Long sid, Long planRelId) {
        // 校验参数
        checkSid(sid);
        databaseBuyPlanRelDao.deleteBySidPlanRelId(sid, planRelId);
        return Vm.DELETE_SUCCESS;
    }

    /**
     * 新增数据库采购计划 ==》获取数据库下拉框
     *
     * @param sid   学校id
     * @param vYear 年份
     * @return 数据库下拉框
     * @author majuehao
     * @date 2021/12/7 14:23
     **/
    @Override
    public List<IdNameModel> findBuyPlanRelDataBaseDropDownList(Long sid, Integer vYear) {
        // 校验参数
        checkSid(sid);

        // 返回参数
        List<IdNameModel> list = Lists.newArrayList();

        // 查询评估通过的数据库
        Set<Long> didSet = databaseEvaluationDao.findListBySidDidYearResultType(sid, Constant.ALL_LONG_VALUE,
                vYear, Constant.ALL_INT_VALUE, EnumEvaluationResult.通过.getValue())
                .stream().map(DatabaseEvaluationInfoModel::getDid).collect(Collectors.toSet());

        // 获取名称
        Map<Long, String> idNameMap = super.getDidNameMapBySid(sid);
        for (Long did : didSet) {
            list.add(new IdNameModel().create(did, idNameMap.get(did) != null ? idNameMap.get(did) : ""));
        }

        return list;
    }

    /**
     * 数据库采购列表 ==》获取数据库下拉框
     *
     * @param sid    学校id
     * @param planId 采购计划id
     * @return 数据库下拉框
     * @author majuehao
     * @date 2021/12/7 14:23
     **/
    @Override
    public List<IdNameModel> findBuyPlanDataBaseDropDownList(Long sid, Long planId) {
        // 校验参数
        checkSid(sid);

        // 返回集合
        List<IdNameModel> list = Lists.newArrayList();

        // 查询数据
        List<DatabaseBuyListModel> databaseBuyPlanDetailModelList =
                databaseBuyPlanRelDao.findListBySidPlanIdDidPage(sid, planId, Constant.ALL_LONG_VALUE, null);

        // 查询数据库名称
        Map<Long, String> dbIdNameMap = super.getDidNameMapBySid(sid);

        // 组装数据
        for (DatabaseBuyListModel model : databaseBuyPlanDetailModelList) {
            list.add(new IdNameModel().create(model.getDid(), dbIdNameMap.get(model.getDid())));
        }

        return list;
    }

    /**
     * 采购计划提交审核
     *
     * @param sid    学校id
     * @param planId 采购计划id
     * @param userId 操作人id
     * @return 修改的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @Override
    public String updateDatabaseBuyPlanState(Long sid, Long planId, Long userId) {
        // 校验参数
        checkSidUserId(sid, userId);

        // 查询采购计划
        DatabaseBuyPlan databaseBuyPlan = databaseBuyPlanDao.findOneBySidPlanId(sid, planId);

        if (databaseBuyPlan == null) {
            throw new CustomException(Vm.NO_DATA);
        }

        // 只有未提交和审核未通过才可以提交审核
        if (databaseBuyPlan.getState() != EnumCheckState.未提交.getValue()
                && databaseBuyPlan.getState() != EnumCheckState.审核未通过.getValue()) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }
        databaseBuyPlan.setState(EnumCheckState.待审核.getValue());
        databaseBuyPlan.setRemark("");
        databaseBuyPlan.setUpdatedBy(userId);
        databaseBuyPlan.setUpdatedTime(LocalDateTime.now());
        databaseBuyPlanDao.updateById(databaseBuyPlan);
        return Vm.UPDATE_SUCCESS;
    }

    /**
     * 审核采购计划
     *
     * @param sid    学校id
     * @param planId 采购计划id
     * @param state  审核状态
     * @param remark 审核说明
     * @param userId 操作人id
     * @return 修改的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @Override
    public String updateDataBaseBuyPlanCheck(Long sid, Long planId, Integer state, String remark, Long userId) {
        // 校验参数
        super.checkSidUserId(sid, userId);

        // 查询采购计划
        DatabaseBuyPlan databaseBuyPlan = databaseBuyPlanDao.findOneBySidPlanId(sid, planId);

        if (databaseBuyPlan == null) {
            throw new CustomException(Vm.NO_DATA);
        }

        // 校验审核状态
        Integer beforeState = databaseBuyPlan.getState();
        super.checkCheckState(beforeState, state);

        databaseBuyPlan.setState(state);
        databaseBuyPlan.setRemark(remark);
        databaseBuyPlan.setUpdatedBy(userId);
        databaseBuyPlan.setUpdatedTime(LocalDateTime.now());
        databaseBuyPlanDao.updateById(databaseBuyPlan);
        return Vm.UPDATE_SUCCESS;
    }

    /**
     * 年度采购计划审核导出
     *
     * @param sid    学校id
     * @param planId 采购计划id
     * @param vYear  年份
     * @return 数据库下拉框
     * @author majuehao
     * @date 2021/12/7 14:23
     **/
    @Override
    public ResponseEntity<byte[]> exportBuyPlanDataBaseList(Long sid, Long planId, Integer vYear) {
        // 校验参数
        super.checkSid(sid);
        // 返回集合
        List<DatabaseBuyListModel> list = Lists.newArrayList();

        // 查询数据
        List<DatabaseBuyListModel> databaseBuyPlanDetailModelList
                = databaseBuyPlanRelDao.findListBySidPlanIdDidPage(sid, planId, null, null);

        // 预计价格、采购原因
        Map<Long, DatabaseBuyListModel> didInfoMap = Maps.newLinkedHashMap();
        // 数据库
        Map<Long, String> didNameMap = Maps.newHashMap();
        // 出版商
        Map<Long, Company> companyMap = Maps.newHashMap();
        // 代理商
        Map<Long, Agent> agentMap = Maps.newHashMap();
        // 文件名
        String fileName = "";

        // 有数据才进行查询以及收束数据
        if (databaseBuyPlanDetailModelList != null) {
            // 将预计价格、采购原因收束成集合方便组装数据
            didInfoMap = databaseBuyPlanDetailModelList.stream()
                    .collect(Collectors.toMap(DatabaseBuyListModel::getDid, Function.identity(), (n1, n2) -> n1,
                            LinkedHashMap::new));
            // 查询数据库名
            didNameMap = super.getDidNameMapBySid(sid);

            // 查询采购计划名称
            fileName = databaseBuyPlanDao.findOneBySidPlanId(sid, planId).getTitle();
            fileName = fileName.concat(Constant.SplitChar.LINE_CHAR).concat(ConvertUtil.objToString(vYear)).concat("年")
                    .concat(Constant.SplitChar.LINE_CHAR).concat("采购计划导出").concat(Constant.Suffix.XLSX_WITH_POINT);
        }

        // 组装数据
        for (Long did : didInfoMap.keySet()) {
            DatabaseBuyListModel model = new DatabaseBuyListModel();
            // 预计金额(人民币/万元)
            model.setPriceStr(super.moneyIntoTenThousand(didInfoMap.get(did).getPrice()).toPlainString());
            // 采购原因
            model.setRemark(didInfoMap.get(did).getRemark());
            // 数据库名称
            model.setName(didNameMap.get(did));

            DatabasePropertyModel databaseProperty = super.findDatabaseProperty(sid, did);
            if (databaseProperty != null) {
                // 语种
                model.setLanguage(databaseProperty.getLanguage());
                // 资源类型
                model.setType(databaseProperty.getProperties());
            }

            DatabaseBaseInfo databaseEvaluationInfo = databaseBaseInfoDao.findOneBySidDid(sid, did);
            if (databaseEvaluationInfo != null) {
                // 出版商
                model.setCompany(super.findCompanyName(databaseEvaluationInfo.getCompanyId(), companyMap));
                // 代理商
                model.setAgent(super.findAgentName(databaseEvaluationInfo.getAgentId(), agentMap));
                // 是否全文
                EnumTrueFalse enumTrueFalse = EnumTrueFalse.getTrueFalse(databaseEvaluationInfo.getFulltextFlag());
                model.setFullTextFlag(enumTrueFalse == null ? "" : enumTrueFalse.getName());
            }

            Integer state = databaseBuyPlanDao.findStateBySidPlanId(sid, planId);
            if (state != null) {
                // 审核结果
                EnumCheckState checkState = EnumCheckState.getCheckState(state);
                model.setCheckResult(checkState == null ? "" : checkState.getName());
            }

            Integer orderType = databaseEvaluationDao.findOrderTypeBySidDidYear(sid, did, vYear);
            if (orderType != null) {
                // 订购类型
                EnumOrderType enumOrderType = EnumOrderType.getEnumOrderType(orderType);
                model.setOrderType(enumOrderType == null ? "" : enumOrderType.getName());
            }

            // 添加返回
            list.add(model);
        }

        // 返回导出
        String filePath = TEMP_PATH + System.currentTimeMillis();
        EasyExcel.write(filePath, DatabaseBuyListModel.class).sheet("年度采购计划").doWrite(list);
        return DownloadUtil.getResponseEntityCanDeleteFile(filePath, fileName, EnumTrueFalse.是.getValue());

    }
}
