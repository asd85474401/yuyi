package top.lcywings.pony.service.type;

import top.lcywings.pony.domain.ledger.Type;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/02/28
 **/
public interface TypeService {

    /**
     * 查询所有类型
     *
     * @param name     类型名称
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 类型列表
     * @author majuehao
     * @date 2022/2/28 13:59
     **/
    List<Type> findTypeList(String name, Integer pageNum, Integer pageSize);

    /**
     * 新增类型
     *
     * @param name 类型名称
     * @return 新增的结果
     * @author majuehao
     * @date 2022/2/28 14:34
     **/
    String addType(String name);

    /**
     * 编辑类型
     *
     * @param id   类型id
     * @param name 类型名称
     * @return 编辑的结果
     * @author majuehao
     * @date 2022/2/28 14:34
     **/
    String updateType(Long id, String name);

    /**
     * 删除类型
     *
     * @param id 类型id
     * @return 删除的结果
     * @author majuehao
     * @date 2022/2/28 15:05
     **/
    String deleteType(Long id);
}
