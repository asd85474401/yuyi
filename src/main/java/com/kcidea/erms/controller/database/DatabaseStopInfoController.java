package com.kcidea.erms.controller.database;

import com.kcidea.erms.aop.ActionRights;
import com.kcidea.erms.aop.LoginCheck;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.result.MultipleResult;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.result.Result;
import com.kcidea.erms.controller.common.BaseController;
import com.kcidea.erms.enums.menu.EnumMenu;
import com.kcidea.erms.enums.user.EnumLoginAction;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.common.FileModel;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.database.detail.DatabaseInfoModel;
import com.kcidea.erms.model.database.stopinfo.DatabaseStopInfoModel;
import com.kcidea.erms.service.common.UploadService;
import com.kcidea.erms.service.database.DatabaseStopInfoService;
import com.kcidea.erms.service.ers.DatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/24
 **/
@Slf4j
@RestController
@RequestMapping("/databaseStopInfo")
public class DatabaseStopInfoController extends BaseController {

    @Resource
    private DatabaseStopInfoService databaseStopInfoService;

    @Resource
    private UploadService uploadService;

    @Resource
    private DatabaseService databaseService;

    /**
     * 查询数据库停订列表
     *
     * @param vYear      订购年份
     * @param did        数据库id
     * @param languageId 语种id
     * @param type       资源类型
     * @param pageNum    页码
     * @param pageSize   每页数量
     * @return 数据库停订列表
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.停订数据库管理)
    @GetMapping(value = "/findDatabaseStopList")
    public PageResult<DatabaseStopInfoModel> findDatabaseStopList(
            @RequestParam(value = "vYear") Integer vYear,
            @RequestParam(value = "did", defaultValue = "999") Long did,
            @RequestParam(value = "languageId", defaultValue = "999") Long languageId,
            @RequestParam(value = "type", defaultValue = "999") Long type,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        super.saveInfoLog();
        super.checkYear(vYear);
        return databaseStopInfoService.findDatabaseStopList(getSid(), vYear, did, languageId, type, pageNum, pageSize);
    }

    /**
     * 上传停订附件
     *
     * @param file 表格
     * @return 文件信息
     * @author majuehao
     * @date 2021/12/21 13:31
     **/
    @LoginCheck(action = EnumLoginAction.Normal)
    @PostMapping(value = "/uploadDatabaseStopInfoExcelFile")
    public Result<FileModel> uploadDatabaseStopInfoExcelFile(@RequestParam MultipartFile file) {
        super.saveInfoLog();
        return new Result<FileModel>().success(uploadService.uploadFile(file, Constant.MAX_EVALUATION_ATTACH_FILE_SIZE));
    }

    /**
     * 新增停订数据库
     *
     * @param databaseStopInfoModel 停订数据库详情
     * @return 新增的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.新增, menu = EnumMenu.停订数据库管理)
    @PostMapping(value = "/addDatabaseStopInfo")
    public Result<String> addDatabaseStopInfo(@RequestBody @Valid DatabaseStopInfoModel databaseStopInfoModel) {
        super.saveInfoLog();
        super.checkYear(databaseStopInfoModel.getVyear());
        return new Result<String>().successMsg(databaseStopInfoService.
                addOrUpdateDatabaseStopInfo(getSid(), getUserId(), databaseStopInfoModel));
    }

    /**
     * 编辑停订数据库
     *
     * @param databaseStopInfoModel 停订数据库详情
     * @return 编辑的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.修改, menu = EnumMenu.停订数据库管理)
    @PostMapping(value = "/updateDatabaseStopInfo")
    public Result<String> updateDatabaseStopInfo(@RequestBody @Valid DatabaseStopInfoModel databaseStopInfoModel) {
        super.saveInfoLog();
        super.checkYear(databaseStopInfoModel.getVyear());
        return new Result<String>().successMsg(databaseStopInfoService.
                addOrUpdateDatabaseStopInfo(getSid(), getUserId(), databaseStopInfoModel));
    }

    /**
     * 回显编辑停订数据库
     *
     * @param databaseStopId 停订数据库id
     * @return 停订数据库详情
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.停订数据库管理)
    @GetMapping(value = "/findOneById")
    public Result<DatabaseStopInfoModel> findOneById(@RequestParam(value = "databaseStopId") Long databaseStopId) {
        super.saveInfoLog();
        return new Result<DatabaseStopInfoModel>()
                .success(databaseStopInfoService.findOneById(getSid(), databaseStopId));
    }

    /**
     * 查询学校自定义和系统默认的数据库集合
     *
     * @param name       数据库名称
     * @param letter     首字母
     * @param propertyId 数据库属性
     * @param languageId 数据库语种
     * @param pageNum    页码
     * @param pageSize   每页数量
     * @return 数据库集合
     * @author yeweiwei
     * @date 2021/11/25 16:16
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.停订数据库管理)
    @GetMapping(value = "/findFullDatabaseSelectList")
    public PageResult<IdNameModel> findFullDatabaseSelectList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "letter", required = false) String letter,
            @RequestParam(value = "propertyId", required = false, defaultValue = "999") Long propertyId,
            @RequestParam(value = "languageId", required = false, defaultValue = "999") Long languageId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        super.saveInfoLog();
        return databaseService.findDatabaseSelectList(getSid(), name, letter, propertyId, languageId, pageNum, pageSize);
    }

    /**
     * 新增数据库信息
     *
     * @param databaseInfo 数据库信息
     * @return 新增的结果
     * @author yeweiwei
     * @date 2021/11/25 10:59
     */
    @ActionRights(userAction = EnumUserAction.新增, menu = EnumMenu.停订数据库管理)
    @PostMapping(value = "/addDatabase")
    public Result<IdNameModel> addDatabase(@RequestBody @Valid DatabaseInfoModel databaseInfo) {
        super.saveInfoLog();
        Result<IdNameModel> result = new Result<>();
        result.success(databaseService.addDatabase(getSid(), databaseInfo, getUserId()));
        result.setMsg(Vm.INSERT_SUCCESS);
        return result;
    }

    /**
     * 联想查询数据库
     *
     * @param search 检索条件
     * @return 数据库集合
     * @author majuehao
     * @date 2021/11/26 9:29
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.停订数据库管理)
    @GetMapping(value = "/findDatabaseListBySearch")
    public MultipleResult<IdNameModel> findDatabaseListBySearch(@RequestParam(value = "search") String search) {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(databaseService.findDatabaseListBySearch(getSid(), search));
    }

    /**
     * 查询停订的数据库下拉列表
     *
     * @param vYear 停订年份
     * @return 数据库集合
     * @author majuehao
     * @date 2021/11/24 14:02
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.停订数据库管理)
    @GetMapping(value = "/findDatabaseStopSelectList")
    public MultipleResult<IdNameModel> findDatabaseStopSelectList(@RequestParam(value = "vYear") Integer vYear) {
        super.saveInfoLog();
        super.checkYear(vYear);
        return new MultipleResult<IdNameModel>()
                .success(databaseStopInfoService.findDatabaseStopSelectList(getSid(), vYear));
    }

    /**
     * 删除停订数据库详情
     *
     * @param databaseStopId 停订数据库id
     * @return 删除的结果
     * @author majuehao
     * @date 2021/11/22 11:03
     **/
    @ActionRights(userAction = EnumUserAction.删除, menu = EnumMenu.停订数据库管理)
    @RequestMapping(value = "/deleteDatabaseStopInfo", method = RequestMethod.DELETE)
    public Result<String> deleteDatabaseStopInfo(@RequestParam(value = "databaseStopId") Long databaseStopId) {
        super.saveInfoLog();
        return new Result<String>().successMsg(databaseStopInfoService.deleteDatabaseStopInfo(getSid(), databaseStopId));
    }

    /**
     * 导出数据库停订列表
     *
     * @param vYear      订购年份
     * @param did        数据库id
     * @param languageId 语种id
     * @param type       资源类型
     * @return 导出的文件
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @ActionRights(userAction = EnumUserAction.导出, menu = EnumMenu.停订数据库管理)
    @GetMapping(value = "/exportDatabaseStopList")
    public ResponseEntity<byte[]> exportDatabaseStopList(
            @RequestParam(value = "vYear") Integer vYear,
            @RequestParam(value = "did", defaultValue = "999") Long did,
            @RequestParam(value = "languageId", defaultValue = "999") Long languageId,
            @RequestParam(value = "type", defaultValue = "999") Long type) {
        super.saveInfoLog();
        super.checkYear(vYear);
        return databaseStopInfoService.exportDatabaseStopList(getSid(), vYear, did, languageId, type);
    }
}
