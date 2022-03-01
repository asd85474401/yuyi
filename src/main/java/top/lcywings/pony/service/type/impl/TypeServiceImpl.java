package top.lcywings.pony.service.type.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.lcywings.pony.common.constant.Vm;
import top.lcywings.pony.common.exception.CustomException;
import top.lcywings.pony.dao.ledger.TypeDao;
import top.lcywings.pony.domain.ledger.Type;
import top.lcywings.pony.service.common.BaseService;
import top.lcywings.pony.service.type.TypeService;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/02/28
 **/
@Service
@Slf4j
public class TypeServiceImpl extends BaseService implements TypeService {

    @Resource
    private TypeDao typeDao;

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
    @Override
    public List<Type> findTypeList(String name, Integer pageNum, Integer pageSize) {
        // 校验分页
        super.checkPage(pageNum, pageSize);

        // 分页
        Page<Type> page = new Page<>(pageNum, pageSize);

        // 查询数据并返回
        return typeDao.findListByNamePage(name, page);
    }

    /**
     * 新增类型
     *
     * @param name 类型名称
     * @return 新增的结果
     * @author majuehao
     * @date 2022/2/28 14:34
     **/
    @Override
    public String addType(String name) {
        // 校验参数
        if (Strings.isNullOrEmpty(name)) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        // 查重
        Type type = typeDao.findOneByName(name);
        if (type != null) {
            throw new CustomException(Vm.EXIST_NAME);
        }

        // 新增
        typeDao.insert(new Type().create(name, null, LocalDateTime.now()));

        return Vm.INSERT_SUCCESS;
    }

    /**
     * 编辑类型
     *
     * @param id   类型id
     * @param name 类型名称
     * @return 编辑的结果
     * @author majuehao
     * @date 2022/2/28 14:34
     **/
    @Override
    public String updateType(Long id, String name) {
        // 校验参数
        if (id == null || Strings.isNullOrEmpty(name)) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        Type type = typeDao.selectById(id);
        if (type == null) {
            throw new CustomException(Vm.EXIST_NAME);
        }

        if (!Objects.equals(name, type.getName())) {
            // 查重
            if (typeDao.findOneByName(name) != null) {
                throw new CustomException(Vm.EXIST_NAME);
            }
            type.update(name, null, LocalDateTime.now());
        }

        return Vm.UPDATE_SUCCESS;
    }

    /**
     * 删除类型
     *
     * @param id 类型id
     * @return 删除的结果
     * @author majuehao
     * @date 2022/2/28 15:05
     **/
    @Override
    public String deleteType(Long id) {
        typeDao.deleteById(id);
        return Vm.DELETE_SUCCESS;
    }
}
