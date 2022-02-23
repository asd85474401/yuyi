package com.kcidea.erms.service.common;

import com.kcidea.erms.model.common.DropDownModel;
import com.kcidea.erms.model.common.IdNameModel;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/01/12
 **/
public interface SelectListService {

    /**
     * 根据学校id查询数据库属性下拉集合
     *
     * @return 数据库属性下拉集合
     * @author yeweiwei
     * @date 2021/11/25 11:09
     */
    List<IdNameModel> findPropertySelectList();

    /**
     * 查询代理商集合
     *
     * @param sid 学校id
     * @return 代理商集合
     * @author yeweiwei
     * @date 2021/11/24 19:47
     */
    List<IdNameModel> findAgentSelectList(Long sid);

    /**
     * 查询数据商集合
     *
     * @param sid 学校id
     * @return 数据商集合
     * @author yeweiwei
     * @date 2021/11/24 19:53
     */
    List<IdNameModel> findCompanySelectList(Long sid);

    /**
     * 查询学科覆盖集合
     *
     * @return 学科覆盖集合
     * @author yeweiwei
     * @date 2021/11/24 19:23
     */
    List<DropDownModel> findSubjectSelectList();

    /**
     * 查询数据商填写的类型
     *
     * @return 数据商填写的类型
     * @author huxubin
     * @date 2022/1/14 14:38
     */
    List<IdNameModel> findTableTypeSelect();

    /**
     * 根据学校id查询地区的下拉选择内容
     *
     * @param sid 学校id
     * @return 地区的下拉选择内容
     * @author huxubin
     * @date 2022/1/6 14:33
     */
    List<String> findAreaSelectList(Long sid);

    /**
     * 查询数据库性质的下拉选择内容
     *
     * @param sid 学校id
     * @return 数据库性质的下拉选择内容
     * @author huxubin
     * @date 2022/1/6 14:45
     */
    List<String> findNatureTypeSelectList(Long sid);

    /**
     * 查询数据商填写审核的数据库下拉
     *
     * @param sid 学校id
     * @return 数据商填写的数据库下拉
     * @author huxubin
     * @date 2022/1/14 14:46
     */
    List<IdNameModel> findCompanyWriteCheckDataBaseSelect(Long sid);

    /**
     * 查询审核的下拉菜单
     *
     * @param checkUser 是否审核人员
     * @return 查询审核的下拉菜单
     * @author huxubin
     * @date 2022/1/14 15:03
     */
    List<IdNameModel> findCheckStateSelectList(boolean checkUser);

    /**
     * 查询数据库信息管理的下拉选择内容
     *
     * @param sid 学校id
     * @return 数据库的下拉选择内容
     * @author majuehao
     * @date 2022/1/12 9:52
     **/
    List<IdNameModel> findLevelTotalDatabaseSelectList(Long sid);

    /**
     * 查询反馈处理的数据库下拉选择内容
     *
     * @param sid 学校id
     * @return 数据库的下拉选择内容
     * @author majuehao
     * @date 2022/1/17 14:38
     **/
    List<IdNameModel> findFeedbackDatabaseSelectList(Long sid);

    /**
     * 查询通用的数据库下拉内容
     *
     * @param sid    学校id
     * @param addAll 是否添加全部标签
     * @return 数据库的下拉选择内容
     * @author majuehao
     * @date 2022/1/17 14:38
     **/
    List<IdNameModel> findGenericDatabaseSelectList(Long sid, boolean addAll);

    /**
     * 查询评估时新增的数据库下拉
     * @param sid 学校id
     * @param vYear 年份
     * @return 数据库的下拉选择内容
     * @author huxubin
     * @date 2022/1/19 15:09
     */
    List<IdNameModel> findEvaluationAddSelectList(Long sid, Integer vYear);
}
