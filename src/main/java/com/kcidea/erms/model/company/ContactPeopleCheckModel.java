package com.kcidea.erms.model.company;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2022/1/18
 **/
@Data
public class ContactPeopleCheckModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 供应商联系人
     */
    private List<ContactPeopleInfoModel> companyPeopleList;

    /**
     * 代理商联系人
     */
    private List<ContactPeopleInfoModel> agentPeopleList;

    public ContactPeopleCheckModel create(List<ContactPeopleInfoModel> companyPeopleList,
                                          List<ContactPeopleInfoModel> agentPeopleList) {
        this.companyPeopleList = companyPeopleList;
        this.agentPeopleList = agentPeopleList;
        return this;
    }
}
