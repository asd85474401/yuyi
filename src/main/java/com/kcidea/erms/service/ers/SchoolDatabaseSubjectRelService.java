package com.kcidea.erms.service.ers;

import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/29
 **/
public interface SchoolDatabaseSubjectRelService {

    /**
     * 修改数据库学科覆盖
     *
     * @param sid         学校id
     * @param did         数据库id
     * @param subjectList 学科id集合
     * @param userId      操作人id
     * @author yeweiwei
     * @date 2021/11/29 13:27
     */
    void updateDatabaseSubjectRel(Long sid, Long did, List<Long> subjectList, Long userId);
}
