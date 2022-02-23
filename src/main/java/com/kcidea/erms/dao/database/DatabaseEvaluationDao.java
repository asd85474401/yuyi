package com.kcidea.erms.dao.database;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.database.DatabaseEvaluation;
import com.kcidea.erms.model.database.evaluation.DatabaseEvaluationInfoModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/24
 **/
public interface DatabaseEvaluationDao extends BaseMapper<DatabaseEvaluation> {

    /**
     * 根据学校id、数据库id、年份、订购类型、评估结果查询
     *
     * @param sid          学校id
     * @param did          数据库id
     * @param vYear        年份
     * @param fulltextFlag 全文标识
     * @param resultType   评估结果
     * @return 数据库评估集合
     * @author yeweiwei
     * @date 2021/11/24 16:17
     */
    List<DatabaseEvaluationInfoModel> findListBySidDidYearResultType(@Param("sid") Long sid,
                                                                     @Param("did") Long did,
                                                                     @Param("vYear") Integer vYear,
                                                                     @Param("fulltextFlag") Integer fulltextFlag,
                                                                     @Param("resultType") Integer resultType);

    /**
     * 根据id和学校id删除
     *
     * @param id  id
     * @param sid 学校id
     * @author yeweiwei
     * @date 2021/11/24 19:01
     */
    void deleteByIdSid(@Param("id") Long id, @Param("sid") Long sid);

    /**
     * 根据与学校id、数据库id、年份查询数据库评估数量
     *
     * @param sid   学校id
     * @param did   数据库id
     * @param vYear 年份
     * @return 数量
     * @author yeweiwei
     * @date 2021/11/26 14:04
     */
    int findCountBySidDidYear(@Param("sid") Long sid, @Param("did") Long did, @Param("vYear") Integer vYear);

    /**
     * 根据id查询数据库评估详情
     *
     * @param id 数据库评估id
     * @return 数据库评估详情
     * @author yeweiwei
     * @date 2021/11/29 14:22
     */
    DatabaseEvaluation findOneById(@Param("id") Long id);

    /**
     * 根据学校id、数据库id、年份查询评估id
     *
     * @param sid   学校id
     * @param did   数据库id
     * @param vYear 年份
     * @return 评估id
     * @author yeweiwei
     * @date 2021/11/30 11:17
     */
    Long findIdBySidDidYear(@Param("sid") Long sid, @Param("did") Long did, @Param("vYear") Integer vYear);


    /**
     * 根据学校id、数据库id,查询历年评估记录
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 历年评估记录
     * @author majuehao
     * @date 2021/11/30 14:40
     **/
    List<DatabaseEvaluationInfoModel> findListBySidDid(@Param("sid") Long sid, @Param("did") Long did);

    /**
     * 根据学校id和年份查询学校的数据库评估集合
     *
     * @param sid   学校id
     * @param vYear 年份
     * @return 数据库评估集合
     * @author yeweiwei
     * @date 2021/11/30 18:06
     */
    List<DatabaseEvaluation> findListBySidYear(@Param("sid") Long sid, @Param("vYear") Integer vYear);

    /**
     * 根据学校id、年份查询有评估的数据库id集合
     *
     * @param sid   学校id
     * @param vYear 年份
     * @return 数据库id集合
     * @author yeweiwei
     * @date 2021/11/30 18:08
     */
    Set<Long> findDidSetBySidYear(@Param("sid") Long sid, @Param("vYear") Integer vYear);

    /**
     * 根据学校id、数据库id、年份，查询订购状态
     *
     * @param sid   学校id
     * @param did   数据库id
     * @param vYear 年份
     * @return 订购状态
     * @author majuehao
     * @date 2021/12/8 14:54
     **/
    Integer findOrderTypeBySidDidYear(@Param("sid") Long sid,
                                      @Param("did") Long did,
                                      @Param("vYear") Integer vYear);
}
