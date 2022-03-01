package top.lcywings.pony.dao.ledger;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.lcywings.pony.domain.ledger.Type;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/2/28 13:51
 **/
@Repository
public interface TypeDao extends BaseMapper<Type> {

    /**
     * 查询类型列表
     *
     * @param name 名称
     * @param page 分页
     * @return 类型列表
     * @author majuehao
     * @date 2022/2/28 14:26
     **/
    List<Type> findListByNamePage(@Param("name") String name, @Param("page") Page<Type> page);

    /**
     * 根据名称查类型
     *
     * @param name 类型名称
     * @return 类型实体
     * @author majuehao
     * @date 2022/2/28 14:34
     **/
    Type findOneByName(@Param("name") String name);
}