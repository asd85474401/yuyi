package com.kcidea.erms.service.ers;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/16
 **/
public interface SchoolService {
    /**
     * 根据学校id查询学校开始年份
     *
     * @param sId 学校id
     * @return 学校开始年份
     * @author yeweiwei
     * @date 2021/11/16 11:19
     */
    Integer findStartYearBySid(Long sId);
}
