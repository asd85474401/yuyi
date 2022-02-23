package com.kcidea.erms.dao.company;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.company.CompanyDatabaseContactPeople;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/1/5 13:38
 **/
public interface CompanyDatabaseContactPeopleDao extends BaseMapper<CompanyDatabaseContactPeople> {

    /**
     * 根据学校id、数据库id、联系人类型，查询联系人列表
     *
     * @param sid         学校id
     * @param did         数据库id
     * @param contactType 联系人类型
     * @return 联系人列表
     * @author majuehao
     * @date 2022/1/5 18:26
     **/
    List<CompanyDatabaseContactPeople> findListBySidDidContactType(@Param("sid") Long sid,
                                                                   @Param("did") Long did,
                                                                   @Param("contactType") Integer contactType);

    /**
     * 根据学校id、数据库id、联系人类型、联系人id，查询联系人
     *
     * @param sid       学校id
     * @param did       数据库id
     * @param type      联系人类型
     * @param contactId 联系人id
     * @return 联系人
     * @author majuehao
     * @date 2022/1/5 18:26
     **/
    CompanyDatabaseContactPeople findOneBySidDidContactTypeContactId(@Param("sid") Long sid,
                                                                     @Param("did") Long did,
                                                                     @Param("type") int type,
                                                                     @Param("contactId") Long contactId);

    /**
     * 根据学校id、数据库id、联系人类型，查询联系人数量
     *
     * @param sid         学校id
     * @param did         数据库id
     * @param contactType 联系人类型
     * @return 联系人数量
     * @author majuehao
     * @date 2022/1/5 18:26
     **/
    int findCountBySidDidContactType(@Param("sid") Long sid,
                                     @Param("did") Long did,
                                     @Param("contactType") int contactType);

    /**
     * 根据学校id、数据库id、联系人类型、联系人id，删除联系人
     *
     * @param sid         学校id
     * @param did         数据库id
     * @param contactType 联系人类型
     * @param contactId   联系人id
     * @author majuehao
     * @date 2022/1/6 16:25
     **/
    void deleteBySidDidContactTypeContactId(@Param("sid") Long sid,
                                            @Param("did") Long did,
                                            @Param("contactType") int contactType,
                                            @Param("contactId") Long contactId);

    /**
     * 根据学校id、数据库id、联系人类型、电话号码、id，查询电话号码数量
     *
     * @param sid         学校id
     * @param did         数据库id
     * @param contactType 联系人类型
     * @param phone       联系人id
     * @return 电话号码数量
     * @author majuehao
     * @date 2022/1/19 13:10
     **/
    int findCountBySidDidContactTypePhone(@Param("sid") Long sid, @Param("did") Long did,
                                          @Param("contactType") int contactType,
                                          @Param("phone") String phone);

}