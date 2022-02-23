package com.kcidea.erms.dao.database;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.database.DatabaseContactPeople;
import com.kcidea.erms.model.company.ContactPeopleInfoModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/30 19:49
 **/
public interface DatabaseContactPeopleDao extends BaseMapper<DatabaseContactPeople> {

    /**
     * 查询数据库相应类型联系人信息
     *
     * @param sid  学校id
     * @param did  数据库id
     * @param type 类型：0=供应商联系人 1=代理商联系人
     * @return 联系人信息
     * @author majuehao
     * @date 2021/11/30 14:40
     **/
    List<ContactPeopleInfoModel> findListBySidDidType(@Param("sid") Long sid,
                                                      @Param("did") Long did,
                                                      @Param("type") int type);

    /**
     * 根据学校id、数据库id、联系人类型、联系人id，查询联系人
     *
     * @param sid       学校id
     * @param did       数据库id
     * @param type      类型：0=供应商联系人 1=代理商联系人
     * @param contactId 联系人id
     * @return 联系人
     * @author majuehao
     * @date 2022/1/5 18:26
     **/
    DatabaseContactPeople findOneBySidDidContactTypeContactId(@Param("sid") Long sid,
                                                              @Param("did") Long did,
                                                              @Param("type") int type,
                                                              @Param("contactId") Long contactId);

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
     * 根据学校id、数据库id、联系人类型、电话号码，查询电话号码数量
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
                                          @Param("contactType") int contactType, @Param("phone") String phone);

    /**
     * 根据学校id查询联系人列表
     *
     * @param sid 学校id
     * @return 联系人列表
     * @author majuehao
     * @date 2022/1/24 16:45
     **/
    List<DatabaseContactPeople> findListBySid(Long sid);
}
