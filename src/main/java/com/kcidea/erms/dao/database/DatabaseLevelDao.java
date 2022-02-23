package com.kcidea.erms.dao.database;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.database.DatabaseLevel;
import com.kcidea.erms.model.database.DidTotalIdModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/1/12 9:16
 **/
public interface DatabaseLevelDao extends BaseMapper<DatabaseLevel> {

    /**
     * 根据学校id,数据库id，查总库对应的子库数量
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 总库对应的子库数量
     * @author majuehao
     * @date 2021/10/22 14:02
     */
    int findSonCountListBySidDid(@Param("sid") Long sid, @Param("did") Long did);

    /**
     * 根据学校id,数据库id，查总库对应的子库列表
     *
     * @param sid      学校id
     * @param totalDid 数据库id
     * @return 总库对应的子库列表
     * @author majuehao
     * @date 2021/10/22 14:02
     */
    Set<Long> findSonListBySidTotalDid(@Param("sid") Long sid, @Param("totalDid") Long totalDid);

    /**
     * 根据学校id，查子库总库对应表
     *
     * @param sid 学校id
     * @return 子库总库对应表
     * @author majuehao
     * @date 2022/1/12 13:59
     **/
    List<DidTotalIdModel> findDidTotalIdListBySid(@Param("sid") Long sid);

    /**
     * 根据学校id，查数据库id列表
     *
     * @param sid 学校id
     * @return 数据库列表
     * @author majuehao
     * @date 2022/1/12 13:59
     **/
    Set<Long> findDidSetBySid(@Param("sid") Long sid);

    /**
     * 根据学校id，数据库id，删除数据库（如果是总库还会删除相关子库）
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库列表
     * @author majuehao
     * @date 2022/1/12 13:59
     **/
    int deleteBySidDid(@Param("sid") Long sid, @Param("did") Long did);


    /**
     * 根据学校id，查询数据库id列表
     *
     * @param sid 学校id
     * @return 数据库id列表
     * @author majuehao
     * @date 2022/1/12 10:38
     **/
    List<Long> findLevelTotalDatabaseSelectList(@Param("sid") Long sid);

    /**
     * 根据学校id，查出所有层级关系
     *
     * @param sid 学校id
     * @return 所有层级关系
     * @author majuehao
     * @date 2022/1/18 20:17
     **/
    List<DatabaseLevel> findListBySid(@Param("sid") Long sid);

    /**
     * 根据学校id、数据库id，查出层级详情
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 层级详情
     * @author majuehao
     * @date 2022/1/18 20:26
     **/
    DatabaseLevel findOneBySidDid(@Param("sid") Long sid, @Param("did") Long did);
}