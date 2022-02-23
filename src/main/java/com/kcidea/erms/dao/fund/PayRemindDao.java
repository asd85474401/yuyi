package com.kcidea.erms.dao.fund;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.fund.PayRemind;
import org.apache.ibatis.annotations.Param;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/17
 **/
public interface PayRemindDao extends BaseMapper<PayRemind> {
    /**
     * 根据学校id查询付款到期提醒
     *
     * @param sid 学校id
     * @return 付款到期提醒
     * @author yeweiwei
     * @date 2021/11/17 9:47
     */
    PayRemind findOneBySid(@Param("sid") Long sid);
}
