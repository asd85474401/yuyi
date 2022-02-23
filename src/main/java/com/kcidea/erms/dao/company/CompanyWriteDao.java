package com.kcidea.erms.dao.company;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.company.CompanyWrite;
import org.apache.ibatis.annotations.Param;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/1/5 16:15
 **/
public interface CompanyWriteDao extends BaseMapper<CompanyWrite> {

    /**
     * 根据学校id、数据库id、表格类型，查询供应商填写审核信息
     *
     * @param sid       学校id
     * @param did       数据库id
     * @param tableType 表格类型
     * @return 供应商填写审核信息详情
     * @author majuehao
     * @date 2022/1/5 16:45
     **/
    CompanyWrite findOneBySidDidTableType(@Param("sid") Long sid, @Param("did") Long did,
                                          @Param("tableType") int tableType);

    /**
     * 根据学校id、数据库id、表格类型，删除供应商填写审核信息
     *
     * @param sid       学校id
     * @param did       数据库id
     * @param tableType 表格类型
     * @author majuehao
     * @date 2022/1/5 16:45
     **/
    void deleteBySidDidTableType(@Param("sid") Long sid, @Param("did") Long did,
                                 @Param("tableType") int tableType);

    /**
     * 根据学校id、数据库id、表格类型，查询审核状态
     *
     * @param sid       学校id
     * @param did       数据库id
     * @param tableType 表格类型
     * @return 审核状态
     * @author majuehao
     * @date 2022/1/5 16:45
     **/
    Integer findStateBySidDidTableType(@Param("sid") Long sid, @Param("did") Long did,
                                       @Param("tableType") int tableType);
}
