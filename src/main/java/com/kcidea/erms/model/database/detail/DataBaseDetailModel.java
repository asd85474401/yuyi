package com.kcidea.erms.model.database.detail;

import com.kcidea.erms.model.company.AccessUrlInfoModel;
import com.kcidea.erms.model.company.ContactPeopleInfoModel;
import com.kcidea.erms.model.database.info.DatabaseInfoInsertModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/01
 **/
@Data
public class DataBaseDetailModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 基本信息
     */
    DatabaseInfoInsertModel dataBaseBaseDetailModel;

    /**
     * 访问信息
     */
    List<AccessUrlInfoModel> accessDetailModelList;

    /**
     * 供应商联系人
     */
    List<ContactPeopleInfoModel> companyContactList;

    /**
     * 代理商联系人
     */
    List<ContactPeopleInfoModel> agentContactList;

    /**
     * 使用统计获取
     */
    DatabaseUsingStatisticsModel usingStatisticsModel;

    /**
     * 代理商联系人
     */
    DatabaseSuShiModel suShiModel;

    /**
     * 内容介绍
     */
    String remark;
}
