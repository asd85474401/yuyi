package com.kcidea.erms.controller.database;

import com.kcidea.erms.aop.ActionRights;
import com.kcidea.erms.common.result.MultipleResult;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.result.Result;
import com.kcidea.erms.controller.common.BaseController;
import com.kcidea.erms.enums.menu.EnumMenu;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.common.FileModel;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.database.buyplan.DatabaseBuyListModel;
import com.kcidea.erms.service.common.UploadService;
import com.kcidea.erms.service.database.DatabaseBuyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/12/7
 **/
@Slf4j
@RestController
@RequestMapping("/databaseBuy")
public class DatabaseBuyController extends BaseController {

    @Resource
    private DatabaseBuyService databaseBuyService;

    @Resource
    private UploadService uploadService;

    /**
     * 查询数据库采购列表
     *
     * @param vYear      年份
     * @param did        数据库id
     * @param language   语种
     * @param type       资源类型
     * @param natureType 数据库性质
     * @param buyType    采购类型
     * @param subjectId  学科覆盖id
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @return 数据库采购列表
     * @author yeweiwei
     * @date 2021/12/7 19:59
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库列表采购管理)
    @GetMapping(value = "/findBuyList")
    public PageResult<DatabaseBuyListModel> findBuyList(
            @RequestParam(value = "vYear") Integer vYear,
            @RequestParam(value = "did", required = false) Long did,
            @RequestParam(value = "language", defaultValue = "999") Long language,
            @RequestParam(value = "type", defaultValue = "999") Long type,
            @RequestParam(value = "natureType", defaultValue = "999") Integer natureType,
            @RequestParam(value = "buyType", defaultValue = "999") Integer buyType,
            @RequestParam(value = "subjectId", required = false) Long subjectId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        super.saveInfoLog();
        super.checkYear(vYear);
        return databaseBuyService.findBuyList(getSid(), vYear, did, language, type, natureType, buyType, subjectId, pageNum, pageSize);
    }

    /**
     * 根据年份获得数据库下拉集合
     *
     * @param vYear 年份
     * @return 数据库下拉集合
     * @author yeweiwei
     * @date 2021/12/8 13:43
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库列表采购管理)
    @GetMapping(value = "/findDatabaseSelectList")
    public MultipleResult<IdNameModel> findDatabaseSelectList(@RequestParam(value = "vYear") Integer vYear) {
        super.saveInfoLog();
        super.checkYear(vYear);
        return new MultipleResult<IdNameModel>().success(databaseBuyService.findDatabaseSelectList(getSid(), vYear));
    }


    /**
     * 上传数据库订购列表文件
     *
     * @param file 表格
     * @return 文件信息
     * @author yeweiwei
     * @date 2021/12/8 15:57
     */
    @ActionRights(userAction = EnumUserAction.导入, menu = EnumMenu.数据库列表采购管理)
    @PostMapping(value = "/uploadBuyExcelFile")
    public Result<FileModel> uploadBuyExcelFile(@RequestParam MultipartFile file) {
        super.saveInfoLog();
        return new Result<FileModel>().success(uploadService.uploadExcelFile(file));
    }
}
