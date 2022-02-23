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
public class DataBaseInfoCheckModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 子库下拉筛选的数据
     */
    private List<IdNameModel> childSelectList;

    /**
     * 总库信息
     */
    private TotalDatabaseInfoModel totalInfo;

    /**
     * 子库信息
     */
    private List<TotalDatabaseInfoModel> childInfo;

    public DataBaseInfoCheckModel create(List<IdNameModel> childSelectList, TotalDatabaseInfoModel totalInfo,
                                         List<TotalDatabaseInfoModel> childInfo) {
        this.childSelectList = childSelectList;
        this.totalInfo = totalInfo;
        this.childInfo = childInfo;
        return this;
    }
}
