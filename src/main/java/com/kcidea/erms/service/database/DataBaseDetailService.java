package com.kcidea.erms.service.database;

import com.kcidea.erms.model.company.AccessUrlInfoModel;
import com.kcidea.erms.model.company.ContactPeopleInfoModel;
import com.kcidea.erms.model.database.detail.DataBaseDetailModel;
import com.kcidea.erms.model.database.detail.DataBaseTitleDetailModel;
import com.kcidea.erms.model.database.evaluation.DatabaseEvaluationInfoModel;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/30 14:04
 **/
public interface DataBaseDetailService {

    /**
     * 查询数据库标题栏信息
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库标题栏信息
     * @author majuehao
     * @date 2021/11/30 14:40
     **/
    DataBaseTitleDetailModel findDataBaseTitleDetail(Long sid, Long did);

    /**
     * 查询数据库历年评估记录
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 历年评估记录
     * @author majuehao
     * @date 2021/11/30 14:40
     **/
    List<DatabaseEvaluationInfoModel> findDataBaseEvaluationList(Long sid, Long did);

    /**
     * 查询数据库信息
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库数据库信息
     * @author majuehao
     * @date 2021/11/30 14:40
     **/
    DataBaseDetailModel findDataBaseDetail(Long sid, Long did);

    /**
     * 查询数据库访问信息列表
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库访问信息列表
     * @author majuehao
     * @date 2021/11/30 14:40
     **/
    List<AccessUrlInfoModel> findAccessDetailModelList(Long sid, Long did);

    /**
     * 查询数据库联系人信息
     *
     * @param sid        学校id
     * @param did        数据库id
     * @param contactType 联系人类型
     * @return 数据库联系人信息
     * @author majuehao
     * @date 2021/11/30 14:40
     **/
    List<ContactPeopleInfoModel> findDatabaseContactPeopleList(Long sid, Long did, Integer contactType);

}
