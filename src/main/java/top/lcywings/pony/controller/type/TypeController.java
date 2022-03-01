package top.lcywings.pony.controller.type;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.lcywings.pony.common.result.MultipleResult;
import top.lcywings.pony.common.result.Result;
import top.lcywings.pony.controller.common.BaseController;
import top.lcywings.pony.domain.ledger.Type;
import top.lcywings.pony.service.type.TypeService;

import javax.annotation.Resource;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/02/28
 **/
@Slf4j
@RestController
@RequestMapping("/type")
public class TypeController extends BaseController {

    @Resource
    private TypeService typeService;

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
    @GetMapping(value = "/findTypeList")
    public MultipleResult<Type> findTypeList(@RequestParam(value = "name") String name,
                                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        this.saveInfoLog();
        return new MultipleResult<Type>().success(typeService.findTypeList(name, pageNum, pageSize));
    }

    /**
     * 新增类型
     *
     * @param name 类型名称
     * @return 新增的结果
     * @author majuehao
     * @date 2022/2/28 14:34
     **/
    @PostMapping(value = "/addType")
    public Result<String> addType(@RequestParam(value = "name") String name) {
        super.saveInfoLog();
        return new Result<String>().successMsg(typeService.addType(name));
    }

    /**
     * 编辑类型
     *
     * @param name 类型名称
     * @return 编辑的结果
     * @author majuehao
     * @date 2022/2/28 14:34
     **/
    @PostMapping(value = "/updateType")
    public Result<String> updateType(@RequestParam(value = "id") Long id,
                                     @RequestParam(value = "name") String name) {
        super.saveInfoLog();
        return new Result<String>().successMsg(typeService.updateType(id, name));
    }

    /**
     * 删除类型
     *
     * @param id 类型id
     * @return 删除的结果
     * @author majuehao
     * @date 2022/2/28 15:05
     **/
    @RequestMapping(value = "/deleteType", method = RequestMethod.DELETE)
    public Result<String> deleteType(@RequestParam(value = "id") Long id) {
        super.saveInfoLog();
        return new Result<String>().successMsg(typeService.deleteType(id));
    }
}