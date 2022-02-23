package com.kcidea.erms.dao.database;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.database.DatabaseBuyInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/12/7
 **/
public interface DatabaseBuyInfoDao extends BaseMapper<DatabaseBuyInfo> {

    /**
     * 根据学校id，数据库id，查询数据库采购详情
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 采购详情
     * @author majuehao
     * @date 2021/12/9 10:53
     **/
    DatabaseBuyInfo findOneBySidDid(@Param("sid") Long sid, @Param("did") Long did);
}
