package com.kcidea.erms.dao.company;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kcidea.erms.domain.company.CompanyWriteCheck;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/1/14 9:43
 **/
public interface CompanyWriteCheckDao extends BaseMapper<CompanyWriteCheck> {

    /**
     * 根据学校id、数据库id、表格类型、审核状态，查询列表
     *
     * @param sid       学校id
     * @param did       数据库id
     * @param tableType 表格类型
     * @param state     审核状态
     * @param checkPage 分页
     * @return 列表数据
     * @author majuehao
     * @date 2022/1/14 10:20
     **/
    List<CompanyWriteCheck> findListBySidDidTableTypeStatePage(@Param("sid") Long sid,
                                                               @Param("did") Long did,
                                                               @Param("tableType") Integer tableType,
                                                               @Param("state") Integer state,
                                                               @Param("checkPage") Page<CompanyWriteCheck> checkPage);

    /**
     * 查询有审核记录的数据库id
     *
     * @param sid 学校id
     * @return 有审核记录的数据库id
     * @author huxubin
     * @date 2022/1/14 14:52
     */
    List<Long> findDidListBySid(@Param("sid") Long sid);

    /**
     * 根据学校id和主键id查询
     *
     * @param sid 学校id
     * @param id  主键id
     * @return 查询的结果
     * @author huxubin
     * @date 2022/1/18 11:07
     */
    CompanyWriteCheck findOneBySidId(@Param("sid") Long sid, @Param("id") Long id);

    /**
     * 跟据学校id、数据库id、表格类型、审核状态，查询数量
     *
     * @param sid       学校id
     * @param did       数据库id
     * @param tableType 表格类型
     * @param state     审核状态
     * @return 查询数量
     * @author majuehao
     * @date 2022/1/20 10:12
     **/
    int findCountSidDidTableTypeState(@Param("sid") Long sid,
                                      @Param("did") Long did,
                                      @Param("tableType") Integer tableType,
                                      @Param("state") Integer state);
}