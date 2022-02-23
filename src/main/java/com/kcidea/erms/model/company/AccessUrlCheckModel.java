package com.kcidea.erms.model.company;

import com.kcidea.erms.model.common.IdNameModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2022/1/18
 **/
@Data
public class AccessUrlCheckModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 子库下拉列表
     */
    private List<IdNameModel> childSelectList;

    /**
     * 总库的访问信息
     */
    private List<AccessUrlInfoModel> totalUrl;

    /**
     * 子库的访问信息
     */
    private List<AccessUrlInfoModel> childUrlList;

    public AccessUrlCheckModel create(List<IdNameModel> childSelectList, List<AccessUrlInfoModel> totalUrl,
                                      List<AccessUrlInfoModel> childUrlList) {
        this.childSelectList = childSelectList;
        this.totalUrl = totalUrl;
        this.childUrlList = childUrlList;
        return this;
    }
}
