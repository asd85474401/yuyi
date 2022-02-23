package com.kcidea.erms.dao.ers;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.ers.Agent;
import com.kcidea.erms.model.common.IdNameModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/24
 **/
@DS("slave_ers")
public interface AgentDao extends BaseMapper<Agent> {
    /**
     * 根据学校id查询代理商
     *
     * @param sid 学校id
     * @return 代理商集合
     * @author yeweiwei
     * @date 2021/11/24 19:52
     */
    List<IdNameModel> findListBySid(@Param("sid") Long sid);

    /**
     * 根据学校id、名称查询代理商
     *
     * @param sid  学校id
     * @param name 名称
     * @return 代理商
     * @author yeweiwei
     * @date 2021/12/2 15:55
     */
    Agent findBySidName(@Param("sid") Long sid, @Param("name") String name);
}
