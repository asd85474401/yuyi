package com.kcidea.erms.service.common.impl;

import com.google.common.collect.Lists;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.dao.company.CompanyWriteCheckDao;
import com.kcidea.erms.dao.database.DatabaseBaseInfoDao;
import com.kcidea.erms.dao.database.DatabaseEvaluationDao;
import com.kcidea.erms.dao.database.DatabaseLevelDao;
import com.kcidea.erms.dao.ers.*;
import com.kcidea.erms.dao.feedback.QuestionFeedbackDao;
import com.kcidea.erms.domain.database.DatabaseEvaluation;
import com.kcidea.erms.enums.database.EnumArea;
import com.kcidea.erms.enums.database.EnumCheckState;
import com.kcidea.erms.enums.database.EnumNatureType;
import com.kcidea.erms.enums.database.EnumTableType;
import com.kcidea.erms.enums.subject.EnumSubjectCategoryType;
import com.kcidea.erms.enums.subject.EnumSubjectLevel;
import com.kcidea.erms.model.common.DropDownModel;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.service.common.BaseService;
import com.kcidea.erms.service.common.SelectListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/01/12
 **/
@Slf4j
@Service
public class SelectListServiceImpl extends BaseService implements SelectListService {

    @Resource
    private VdatabasePropertyDao vdatabasePropertyDao;

    @Resource
    private SubjectDao subjectDao;

    @Resource
    private AgentDao agentDao;

    @Resource
    private CompanyDao companyDao;

    @Resource
    private DatabaseBaseInfoDao databaseBaseInfoDao;

    @Resource
    private DatabaseLevelDao databaseLevelDao;

    @Resource
    private CompanyWriteCheckDao companyWriteCheckDao;

    @Resource
    private QuestionFeedbackDao questionFeedbackDao;

    @Resource
    private SchoolDatabaseListDao schoolDatabaseListDao;

    @Resource
    private DatabaseEvaluationDao databaseEvaluationDao;

    /**
     * 根据学校id查询数据库属性下拉集合
     *
     * @return 数据库属性下拉集合
     * @author yeweiwei
     * @date 2021/11/25 11:09
     */
    @Override
    public List<IdNameModel> findPropertySelectList() {
        return vdatabasePropertyDao.findListBySid(0L);
    }

    /**
     * 查询代理商集合
     *
     * @param sid 学校id
     * @return 代理商集合
     * @author yeweiwei
     * @date 2021/11/24 19:47
     */
    @Override
    public List<IdNameModel> findAgentSelectList(Long sid) {
        super.checkSid(sid);
        return agentDao.findListBySid(sid);
    }

    /**
     * 查询数据商集合
     *
     * @param sid 学校id
     * @return 数据商集合
     * @author yeweiwei
     * @date 2021/11/24 19:53
     */
    @Override
    public List<IdNameModel> findCompanySelectList(Long sid) {
        super.checkSid(sid);
        return companyDao.findListBySid(sid);
    }

    /**
     * 查询学科覆盖集合
     *
     * @return 学科覆盖集合
     * @author yeweiwei
     * @date 2021/11/24 19:23
     */
    @Override
    public List<DropDownModel> findSubjectSelectList() {
        List<DropDownModel> list = Lists.newArrayList();
        DropDownModel dropDownModel = new DropDownModel().setId(Constant.Subject.COMPREHENSIVE_SUBJECT_LONG)
                .setName(Constant.Subject.COMPREHENSIVE_SUBJECT_STRING).setSonList(null).setParentId(0L).setTreeLevel(0);
        list.add(dropDownModel);
        list.addAll(subjectDao.findListByCategoryTypeTreeLevel(
                EnumSubjectCategoryType.教育部学科.getValue(), EnumSubjectLevel.一级学科.getValue()));
        return DropDownModel.buildTree(list, 0L);
    }

    /**
     * 查询数据商填写的类型
     *
     * @return 数据商填写的类型
     * @author huxubin
     * @date 2022/1/14 14:38
     */
    @Override
    public List<IdNameModel> findTableTypeSelect() {
        List<IdNameModel> list = Lists.newArrayList();
        list.add(new IdNameModel().create(Constant.ALL_LONG_VALUE, Constant.ALL_STRING_VALUE));
        for (EnumTableType tableType : EnumTableType.values()) {
            list.add(new IdNameModel().create(tableType.getLongValue(), tableType.getName()));
        }
        return list;
    }

    /**
     * 根据学校id查询地区的下拉选择内容
     *
     * @param sid 学校id
     * @return 地区的下拉选择内容
     * @author huxubin
     * @date 2022/1/6 14:33
     */
    @Override
    public List<String> findAreaSelectList(Long sid) {
        super.checkSid(sid);
        //数据库地区对应的下拉
        List<String> areaList = Lists.newArrayList();

        //先把系统默认的添加进去
        List<String> defaultAreaList = EnumArea.getAreaList();
        areaList.addAll(defaultAreaList);

        //查询学校已有的数据库地区
        List<String> schoolAreaList = databaseBaseInfoDao.findAreaListBySid(sid);
        for (String area : schoolAreaList) {
            //把默认的排除掉，添加剩下自定义的
            if (!defaultAreaList.contains(area)) {
                areaList.add(area);
            }
        }
        return areaList;
    }

    /**
     * 查询数据库性质的下拉选择内容
     *
     * @param sid 学校id
     * @return 数据库性质的下拉选择内容
     * @author huxubin
     * @date 2022/1/6 14:45
     */
    @Override
    public List<String> findNatureTypeSelectList(Long sid) {
        super.checkSid(sid);
        //数据库性质对应的下拉
        List<String> natureTypeList = Lists.newArrayList();

        //先把系统默认的添加进去
        List<String> defaultTypeList = EnumNatureType.getTypeList();
        natureTypeList.addAll(defaultTypeList);

        //查询学校已有的数据库性质
        List<String> schoolNatureTypeList = databaseBaseInfoDao.findNatureTypeListBySid(sid);
        for (String natureType : schoolNatureTypeList) {
            //把默认的排除掉，添加剩下自定义的
            if (!defaultTypeList.contains(natureType.toUpperCase())) {
                natureTypeList.add(natureType.toUpperCase());
            }
        }
        return natureTypeList;
    }

    /**
     * 查询数据商填写审核的数据库下拉
     *
     * @param sid 学校id
     * @return 数据商填写的数据库下拉
     * @author huxubin
     * @date 2022/1/14 14:46
     */
    @Override
    public List<IdNameModel> findCompanyWriteCheckDataBaseSelect(Long sid) {
        //有审核记录的数据库id
        List<Long> didList = companyWriteCheckDao.findDidListBySid(sid);
        return this.findDataBaseIdNameBySelect(sid, didList, true);
    }

    /**
     * 查询审核的下拉菜单
     *
     * @param checkUser 是否审核人员
     * @return 查询审核的下拉菜单
     * @author huxubin
     * @date 2022/1/14 15:03
     */
    @Override
    public List<IdNameModel> findCheckStateSelectList(boolean checkUser) {
        List<IdNameModel> resultList = Lists.newArrayList();
        resultList.add(new IdNameModel().create(Constant.ALL_LONG_VALUE, Constant.ALL_STRING_VALUE));
        for (EnumCheckState checkState : EnumCheckState.values()) {
            if (checkUser && checkState == EnumCheckState.未提交) {
                continue;
            }
            resultList.add(new IdNameModel().create(checkState.getLongValue(), checkState.getName()));
        }
        return resultList;
    }

    /**
     * 查询数据库的下拉选择内容
     *
     * @param sid 学校id
     * @return 数据库的下拉选择内容
     * @author majuehao
     * @date 2022/1/12 9:52
     **/
    @Override
    public List<IdNameModel> findLevelTotalDatabaseSelectList(Long sid) {
        List<Long> list = databaseLevelDao.findLevelTotalDatabaseSelectList(sid);
        return this.findDataBaseIdNameBySelect(sid, list, false);
    }

    /**
     * 查询反馈处理的数据库下拉选择内容
     *
     * @param sid 学校id
     * @return 数据库的下拉选择内容
     * @author majuehao
     * @date 2022/1/17 14:38
     **/
    @Override
    public List<IdNameModel> findFeedbackDatabaseSelectList(Long sid) {
        super.checkSid(sid);
        List<Long> list = questionFeedbackDao.findDidListBySid(sid);
        return this.findDataBaseIdNameBySelect(sid, list, true);
    }

    /**
     * 查询通用的数据库下拉内容
     *
     * @param sid 学校id
     * @return 数据库的下拉选择内容
     * @author majuehao
     * @date 2022/1/17 14:38
     **/
    @Override
    public List<IdNameModel> findGenericDatabaseSelectList(Long sid, boolean addAll) {
        Set<Long> didSet = schoolDatabaseListDao.findOrderDidSetBySidYear(sid, null);
        return this.findDataBaseIdNameBySelect(sid, Lists.newArrayList(didSet), addAll);
    }

    /**
     * 查询评估时新增的数据库下拉
     *
     * @param sid   学校id
     * @param vYear 年份
     * @return 数据库的下拉选择内容
     * @author huxubin
     * @date 2022/1/19 15:09
     */
    @Override
    public List<IdNameModel> findEvaluationAddSelectList(Long sid, Integer vYear) {
        //查询学校有的数据库
        Set<Long> didSet = databaseLevelDao.findDidSetBySid(sid);
        //查询可选择的数据库
        List<IdNameModel> list = this.findDataBaseIdNameBySelect(sid, Lists.newArrayList(didSet), false);
        //查询这个年份已有的评估数据库
        Set<Long> evaluationDidSet = databaseEvaluationDao.findDidSetBySidYear(sid,vYear);
        for(IdNameModel model:list){
            //如果这个年份已经有了，则设置成禁用
            if(evaluationDidSet.contains(model.getId())){
                model.setFlag(false);
            }else {
                model.setFlag(true);
            }
        }
        return list;
    }

    /**
     * 根据要求组装数据库下拉的数据
     *
     * @param sid     学校id
     * @param didList 需要查询的数据库id
     * @param addAll  是否添加全部标签
     * @return 查询的数据库下拉返回结果
     * @author huxubin
     * @date 2022/1/14 14:54
     */
    private List<IdNameModel> findDataBaseIdNameBySelect(Long sid, List<Long> didList, boolean addAll) {
        //查询数据库名称
        Map<Long, IdNameModel> dataBaseNameMap = super.findDatabaseNameMap(sid);

        //返回的结果
        List<IdNameModel> resultList = Lists.newArrayList();
        if (addAll) {
            resultList.add(new IdNameModel().create(Constant.ALL_LONG_VALUE, Constant.ALL_STRING_VALUE));
        }
        for (Long did : didList) {
            if (dataBaseNameMap.containsKey(did)) {
                resultList.add(dataBaseNameMap.get(did));
            }
        }
        return resultList;
    }


}
