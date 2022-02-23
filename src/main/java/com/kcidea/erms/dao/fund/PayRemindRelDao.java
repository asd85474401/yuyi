package com.kcidea.erms.dao.fund;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.fund.PayRemindRel;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/17
 **/
public interface PayRemindRelDao extends BaseMapper<PayRemindRel> {
    /**
     * 根据学校id查询付款到期需要提醒的用户id
     *
     * @param sid         学校id
     * @param disableFlag 禁用标识
     * @return 用户id集合
     * @author yeweiwei
     * @date 2021/11/17 9:52
     */
    Set<Long> findUserIdSetBySidDisableFlag(@Param("sid") Long sid, @Param("disableFlag") int disableFlag);

    /**
     * 根据学校id删除付款到期需要提醒的用户
     *
     * @param sid 学校id
     * @author yeweiwei
     * @date 2021/11/17 10:09
     */
    void deleteBySid(@Param("sid") Long sid);

    /**
     * 根据学校id和用户id，查询支付提醒关系详情
     *
     * @param sid    学校id
     * @param userId 用户id
     * @return 支付提醒关系详情
     * @author majuehao
     * @date 2021/11/22 16:35
     **/
    PayRemindRel findOneByUserIdSid(@Param("sid") Long sid, @Param("userId") Long userId);


    /**
     * 根据学校id和用户id，查询支付提醒关系的数量
     *
     * @param sid    学校id
     * @param userId 用户id
     * @return 支付提醒关系详情
     * @author majuehao
     * @date 2021/11/22 16:35
     **/
    int findCountByUserIdSid(@Param("sid")Long sid,  @Param("userId")Long userId);
}
