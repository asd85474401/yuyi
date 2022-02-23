package com.kcidea.erms.service.fund.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.util.ConvertUtil;
import com.kcidea.erms.common.util.DownloadUtil;
import com.kcidea.erms.dao.fund.ContractPayDao;
import com.kcidea.erms.dao.fund.SchoolBudgetDao;
import com.kcidea.erms.dao.fund.SchoolBudgetRelDao;
import com.kcidea.erms.domain.fund.SchoolBudget;
import com.kcidea.erms.domain.fund.SchoolBudgetRel;
import com.kcidea.erms.enums.common.EnumTrueFalse;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.common.IdValueModel;
import com.kcidea.erms.model.fund.SchoolBudgetExportModel;
import com.kcidea.erms.model.fund.SchoolBudgetInfoModel;
import com.kcidea.erms.model.fund.SchoolBudgetModel;
import com.kcidea.erms.model.fund.YearBudgetModel;
import com.kcidea.erms.model.user.UserManageModel;
import com.kcidea.erms.service.common.BaseService;
import com.kcidea.erms.service.fund.SchoolBudgetService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/15
 **/
@Service
public class SchoolBudgetServiceImpl extends BaseService implements SchoolBudgetService {

    @Resource
    private SchoolBudgetDao schoolBudgetDao;

    @Resource
    private SchoolBudgetRelDao schoolBudgetRelDao;

    @Value("${my-config.temp-path}")
    private String TEMP_PATH;

    @Resource
    private ContractPayDao contractPayDao;

    /**
     * 分页查询预算列表
     *
     * @param sid        学校id
     * @param budgetName 预算来源
     * @param vYear      年份
     * @param pageNum    页码
     * @param pageSize   每页数量
     * @return 预算列表
     * @author majuehao
     * @date 2021/11/15 14:35
     **/
    @Override
    public PageResult<SchoolBudgetModel> findBudgetListByNameYearSidPage(Long sid, String budgetName, Integer vYear,
                                                                         Integer pageNum, Integer pageSize) {
        // 校验分页参数
        super.checkPageParam(pageNum, pageSize);

        // 校验参数
        super.checkSid(sid);

        Page<UserManageModel> page = new Page<>(pageNum, pageSize);

        // 查询预算数据
        List<SchoolBudgetModel> list = schoolBudgetRelDao.findBudgetListByNameYearSidPage(budgetName, vYear, sid, page);

        // 遍历查询已支付、未支付、剩余金额
        for (SchoolBudgetModel model : list) {
            BigDecimal paidPrice = getPaidPrice(sid, model.getBudgetId(), vYear);
            BigDecimal unpaidPrice = getUnPaidPrice(sid, model.getBudgetId(), vYear);
            BigDecimal surplusPrice = model.getTotalPrice().subtract(paidPrice.add(unpaidPrice));

            model.setPaidPrice(paidPrice);
            model.setUnpaidPrice(unpaidPrice);
            model.setSurplusPrice(surplusPrice);
        }

        PageResult<SchoolBudgetModel> result = new PageResult<>();
        return result.success(list, page.getTotal());
    }

    /**
     * 获取当年已支付金额
     *
     * @param budgetId 预算id
     * @param vYear    年份
     * @return 已支付金额
     * @author majuehao
     * @date 2021/11/18 10:54
     **/
    private BigDecimal getPaidPrice(Long sid, Long budgetId, Integer vYear) {
        BigDecimal paidPrice = contractPayDao.findSumBySidBudgetIdvYearPayFlag(sid, budgetId, vYear, EnumTrueFalse.是.getValue());
        return ConvertUtil.objToDec(paidPrice);
    }

    /**
     * 获取当年待支付金额
     *
     * @param budgetId 预算id
     * @param vYear    年份
     * @return 待支付金额
     * @author majuehao
     * @date 2021/11/18 10:54
     **/
    private BigDecimal getUnPaidPrice(Long sid, Long budgetId, Integer vYear) {
        BigDecimal unPaidPrice = contractPayDao.findSumBySidBudgetIdvYearPayFlag(sid, budgetId, vYear, EnumTrueFalse.否.getValue());
        return ConvertUtil.objToDec(unPaidPrice);
    }

    /**
     * 新增学校预算来源
     *
     * @param sid                   学校id
     * @param schoolBudgetInfoModel 学校预算来源信息
     * @param adminId               操作人id
     * @return 新增的结果
     * @author majuehao
     * @date 2021/11/15 16:15
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String addSchoolBudget(Long sid, SchoolBudgetInfoModel schoolBudgetInfoModel, Long adminId) {
        // 校验参数
        super.checkSid(sid);

        if (schoolBudgetDao.findCountBySidName(sid, schoolBudgetInfoModel.getName()) > 0) {
            throw new CustomException("预算来源重复，操作失败");
        }

        // 插入预算来源信息
        SchoolBudget schoolBudget = new SchoolBudget();
        schoolBudget.create(sid, schoolBudgetInfoModel.getName(), adminId, LocalDateTime.now());
        schoolBudgetDao.insert(schoolBudget);

        // 循环插入历年预算金额信息
        List<YearBudgetModel> list = schoolBudgetInfoModel.getTotalPriceList();
        for (YearBudgetModel model : list) {

            // 申报预算金额
            BigDecimal declarePrice = model.getDeclarePrice();

            // 批复预算金额
            BigDecimal totalPrice = model.getPrice();

            // 预算年份
            Integer vYear = model.getVYear();

            // 校验预算金额
            this.checkPrice(declarePrice, totalPrice, vYear);

            schoolBudgetRelDao.insert(new SchoolBudgetRel().create(sid, schoolBudget.getId(), model.getVYear(),
                    ConvertUtil.objToDec(declarePrice), ConvertUtil.objToDec(totalPrice),
                    adminId, LocalDateTime.now()));

        }
        return Vm.INSERT_SUCCESS;
    }

    /**
     * 编辑预算回显
     *
     * @param sid         学校
     * @param budgetRelId 预算id
     * @return 预算信息
     * @author majuehao
     * @date 2021/11/15 16:15
     **/
    @Override
    public SchoolBudgetInfoModel findOneByBudgetRelId(Long sid, Long budgetRelId) {
        SchoolBudgetRel model = schoolBudgetRelDao.findOneBySidId(sid, budgetRelId);
        if (model == null) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }
        YearBudgetModel yearBudgetModel = new YearBudgetModel();
        yearBudgetModel.create(model.getVyear(), model.getDeclarePrice(), model.getTotalPrice());

        SchoolBudget schoolBudget = schoolBudgetDao.selectById(model.getBudgetId());
        if (schoolBudget == null) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        return new SchoolBudgetInfoModel().create(budgetRelId, model.getBudgetId(), schoolBudget.getName(),
                Lists.newArrayList(yearBudgetModel));
    }

    /**
     * 编辑预算
     *
     * @param sid                   学校id
     * @param adminId               操作人id
     * @param schoolBudgetInfoModel 预算详情
     * @return 编辑的结果
     * @author majuehao
     * @date 2021/11/15 16:15
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String updateBudget(Long sid, Long adminId, SchoolBudgetInfoModel schoolBudgetInfoModel) {
        if (schoolBudgetInfoModel == null) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        // 预算名称
        String name = schoolBudgetInfoModel.getName();

        // 关系id
        Long budgetRelId = schoolBudgetInfoModel.getBudgetRelId();

        // 年度预算列表
        List<YearBudgetModel> totalPriceList = schoolBudgetInfoModel.getTotalPriceList();

        if (Strings.isNullOrEmpty(name)) {
            throw new CustomException(Vm.BUDGET_NAME_NOT_BLANK);
        }

        if (CollectionUtils.isEmpty(totalPriceList)) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        // 申报预算金额
        BigDecimal declarePrice = ConvertUtil.objToDec(totalPriceList.get(0).getDeclarePrice());

        // 批复预算金额
        BigDecimal totalPrice = ConvertUtil.objToDec(totalPriceList.get(0).getPrice());

        // 预算年份
        Integer vYear = totalPriceList.get(0).getVYear();

        // 校验预算金额
        this.checkPrice(declarePrice, totalPrice, vYear);

        // 判断更新的数据是否存在
        SchoolBudgetRel schoolBudgetRel = schoolBudgetRelDao.findOneBySidBudgetRelId(sid, budgetRelId);
        if (schoolBudgetRel == null) {
            throw new CustomException(Vm.NO_DATA);
        }

        // 计算支付和未支付的金额之和
        BigDecimal actualPrice = contractPayDao.findSumBySidBudgetIdvYearPayFlag(sid,
                schoolBudgetRel.getBudgetId(), schoolBudgetRel.getVyear(), null);
        // 判断预算是否小于需要的金额
        if (actualPrice.compareTo(totalPrice) > 0) {
            throw new CustomException("检测到您输入的预算总金额低于实际支付的金额，请重新输入");
        }

        // 查询数据
        SchoolBudget schoolBudget = schoolBudgetDao.selectById(schoolBudgetRel.getBudgetId());

        // 判断是否重名
        if (!Objects.equals(name, schoolBudget.getName())) {
            if (schoolBudgetDao.findCountBySidName(sid, name) > 0) {
                throw new CustomException("预算来源重复，操作失败");
            }

            // 如果预算名字进行更改了，更改数据预算表数据
            schoolBudget.update(schoolBudget.getId(), sid, name, adminId, LocalDateTime.now());
            schoolBudgetDao.updateById(schoolBudget);
        }

        // 如果总金额或者申报预算金额进行更改了，更改数据预算关系表数据
        if (totalPrice.compareTo(schoolBudgetRel.getTotalPrice()) != 0 ||
                declarePrice.compareTo(schoolBudgetRel.getDeclarePrice()) != 0) {
            schoolBudgetRel.update(schoolBudgetRel.getId(), sid, schoolBudgetRel.getBudgetId(),
                    schoolBudgetRel.getVyear(), declarePrice, totalPrice, adminId, LocalDateTime.now());
            schoolBudgetRelDao.updateById(schoolBudgetRel);
        }

        return Vm.UPDATE_SUCCESS;
    }

    /**
     * 校验预算金额
     *
     * @param declarePrice 申报预算金额
     * @param totalPrice   批复预算金额
     * @author majuehao
     * @date 2022/2/14 13:25
     **/
    private void checkPrice(BigDecimal declarePrice, BigDecimal totalPrice, Integer vYear) {
        if (vYear == null) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        declarePrice = ConvertUtil.objToDec(declarePrice);
        totalPrice = ConvertUtil.objToDec(totalPrice);

        if (declarePrice.compareTo(totalPrice) < 0) {
            throw new CustomException(vYear + "年的申报预算金额不得小于批复预算金额");
        }
    }


    /**
     * 删除预算
     *
     * @param sid         学校id
     * @param budgetRelId 预算id
     * @return 删除的结果
     * @author majuehao
     * @date 2021/11/15 16:15
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String deleteBudget(Long sid, Long budgetRelId) {
        // 校验参数
        super.checkSid(sid);

        // 根据预算id查询来源
        SchoolBudgetRel schoolBudgetRel = schoolBudgetRelDao.findOneBySidBudgetRelId(sid, budgetRelId);

        // 判断是否有支付记录
        if (contractPayDao.findCountBySidBudgetId(sid, schoolBudgetRel.getBudgetId()) != 0) {
            throw new CustomException("检测到此预算已经有绑定的支付记录，请您先解除绑定再进行删除");
        }

        // 删除预算来源
        schoolBudgetDao.deleteById(schoolBudgetRel.getBudgetId());
        // 删除该来源所有年份的预算
        schoolBudgetRelDao.deleteByBudgetId(schoolBudgetRel.getBudgetId());

        return Vm.DELETE_SUCCESS;
    }

    /**
     * 导出预算列表
     *
     * @param sid        学校id
     * @param budgetName 预算来源
     * @param vYear      年份
     * @return 预算列表
     * @author majuehao
     * @date 2021/11/15 14:35
     **/
    @Override
    public ResponseEntity<byte[]> exportBudgetList(Long sid, String budgetName, Integer vYear) {
        // 校验参数
        super.checkSid(sid);

        // 查询数据
        List<SchoolBudgetExportModel> list = schoolBudgetRelDao.findBudgetListByNameYearSid(budgetName, vYear, sid);

        // 查询所有已支付、未支付金额
        Map<Long, BigDecimal> paidPriceMap = contractPayDao.findSumBySidYearPayFlag(sid, vYear, EnumTrueFalse.是.getValue())
                .stream().collect(Collectors.toMap(IdValueModel::getId, IdValueModel::getValue));
        Map<Long, BigDecimal> unpaidPriceMap = contractPayDao.findSumBySidYearPayFlag(sid, vYear, EnumTrueFalse.否.getValue())
                .stream().collect(Collectors.toMap(IdValueModel::getId, IdValueModel::getValue));

        // 声明对象
        BigDecimal totalPrice;
        BigDecimal paidPrice;
        BigDecimal unpaidPrice;
        BigDecimal surplusPrice;
        BigDecimal declarePrice;

        // 遍历查询已支付、未支付、剩余金额
        for (SchoolBudgetExportModel model : list) {
            totalPrice = ConvertUtil.objToDec(model.getTotalPrice(), BigDecimal.ZERO);
            paidPrice = paidPriceMap.get(model.getBudgetId()) != null ? paidPriceMap.get(model.getBudgetId()) : BigDecimal.ZERO;
            unpaidPrice = unpaidPriceMap.get(model.getBudgetId()) != null ? unpaidPriceMap.get(model.getBudgetId()) : BigDecimal.ZERO;
            surplusPrice = totalPrice.subtract(paidPrice.add(unpaidPrice));
            declarePrice = ConvertUtil.objToDec(model.getDeclarePrice(), BigDecimal.ZERO);

            // 转换为万元为单位
            model.setTotalPrice(super.moneyIntoTenThousand(totalPrice).toPlainString());
            model.setPaidPrice(super.moneyIntoTenThousand(paidPrice).toPlainString());
            model.setUnpaidPrice(super.moneyIntoTenThousand(unpaidPrice).toPlainString());
            model.setSurplusPrice(super.moneyIntoTenThousand(surplusPrice).toPlainString());
            model.setDeclarePrice(super.moneyIntoTenThousand(declarePrice).toPlainString());
        }

        // 返回导出
        String filePath = TEMP_PATH + System.currentTimeMillis();
        EasyExcel.write(filePath, SchoolBudgetExportModel.class).sheet("预算来源列表").doWrite(list);
        return DownloadUtil.getResponseEntityCanDeleteFile(filePath,
                ConvertUtil.objToString(vYear).concat("年预算来源列表").concat(Constant.Suffix.XLSX_WITH_POINT),
                EnumTrueFalse.是.getValue());
    }

    /**
     * 根据学校id获取预算来源下拉集合
     *
     * @param sid 学校id
     * @return 预算来源下拉集合
     * @author yeweiwei
     * @date 2021/11/16 13:41
     */
    @Override
    public List<IdNameModel> findBudgetSelectListBySid(Long sid) {
        // 校验参数
        super.checkSid(sid);
        List<IdNameModel> list = Lists.newArrayList(new IdNameModel().create(999L, "全部"));
        list.addAll(schoolBudgetDao.findSelectListBySid(sid));
        return list;
    }
}
