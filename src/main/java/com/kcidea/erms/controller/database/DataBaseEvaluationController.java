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
import com.kcidea.erms.model.database.evaluation.DatabaseEvaluationInfoModel;
import com.kcidea.erms.model.database.evaluation.EvaluationModel;
import com.kcidea.erms.service.common.SelectListService;
import com.kcidea.erms.service.common.UploadService;
import com.kcidea.erms.service.database.DatabaseEvaluationService;
import com.kcidea.erms.service.ers.DatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/24
 **/
@Slf4j
@RestController
@RequestMapping("/databaseEvaluation")
public class DataBaseEvaluationController extends BaseController {

    @Resource
    private DatabaseEvaluationService databaseEvaluationService;

    @Resource
    private DatabaseService databaseService;

    @Resource
    private UploadService uploadService;

    @Resource
    private SelectListService selectListService;

    /**
     * 查询有评估的数据库集合
     *
     * @return 数据库集合
     * @author yeweiwei
     * @date 2021/11/24 14:02
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库评估管理)
    @GetMapping(value = "/findDatabaseSelectList")
    public MultipleResult<IdNameModel> findDatabaseSelectList(@RequestParam(value = "vYear") Integer vYear) {
        super.saveInfoLog();
        super.checkYear(vYear);
        return new MultipleResult<IdNameModel>().success(databaseEvaluationService.findDatabaseSelectList(getSid(),
                vYear));
    }

    /**
     * 查询评估列表
     *
     * @param vYear      年份
     * @param did        数据库id
     * @param language   语种
     * @param type       资源类型
     * @param resultType 评估结果
     * @param pageNum    页码
     * @param pageSize   每页数量
     * @return 评估列表
     * @author yeweiwei
     * @date 2021/11/24 15:25
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库评估管理)
    @GetMapping(value = "/findDatabaseEvaluationPage")
    public PageResult<DatabaseEvaluationInfoModel> findDatabaseEvaluationPage(
            @RequestParam(value = "vYear") Integer vYear,
            @RequestParam(value = "did", required = false) Long did,
            @RequestParam(value = "language", defaultValue = "999") Long language,
            @RequestParam(value = "fulltextFlag", defaultValue = "999") Integer fulltextFlag,
            @RequestParam(value = "type", defaultValue = "999") Long type,
            @RequestParam(value = "resultType", defaultValue = "999") Integer resultType,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        super.saveInfoLog();
        super.checkYear(vYear);
        return databaseEvaluationService.findDatabaseEvaluationPage(getSid(), vYear, did,
                language, fulltextFlag, type, resultType, pageNum, pageSize);
    }

    /**
     * 删除数据库评估
     *
     * @param id 评估id
     * @return 删除的结果
     * @author yeweiwei
     * @date 2021/11/24 19:02
     */
    @ActionRights(userAction = EnumUserAction.删除, menu = EnumMenu.数据库评估管理)
    @DeleteMapping(value = "/deleteDatabaseEvaluation")
    public Result<String> deleteDatabaseEvaluation(@RequestParam(value = "id") Long id) {
        super.saveInfoLog();
        return new Result<String>().successMsg(databaseEvaluationService.deleteDatabaseEvaluation(id, getSid()));
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
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库评估管理)
    @GetMapping(value = "/findFullDatabaseSelectList")
    public PageResult<IdNameModel> findFullDatabaseSelectList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "letter", required = false) String letter,
            @RequestParam(value = "propertyId", required = false, defaultValue = "999") Long propertyId,
            @RequestParam(value = "languageId", required = false, defaultValue = "999") Long languageId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        super.saveInfoLog();
        return databaseService.findDatabaseSelectList(getSid(), name, letter, propertyId, languageId, pageNum,
                pageSize);
    }

    /**
     * 查询数据库信息
     *
     * @return 数据库信息
     * @author yeweiwei
     * @date 2021/11/25 10:59
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库评估管理)
    @GetMapping(value = "/findDatabaseEvaluationInfo")
    public Result<DatabaseEvaluationInfoModel> findDatabaseEvaluationInfo(@RequestParam(value = "did") Long did) {
        super.saveInfoLog();
        return new Result<DatabaseEvaluationInfoModel>()
                .success(databaseEvaluationService.findDatabaseEvaluationInfo(getSid(), did));
    }

    /**
     * 新增数据库信息
     *
     * @param databaseInfo 数据库信息
     * @return 新增的结果
     * @author yeweiwei
     * @date 2021/11/25 10:59
     */
    @ActionRights(userAction = EnumUserAction.新增, menu = EnumMenu.数据库评估管理)
    @PostMapping(value = "/addDatabase")
    public Result<IdNameModel> addDatabase(@RequestBody @Valid DatabaseInfoModel databaseInfo) {
        super.saveInfoLog();
        Result<IdNameModel> result = new Result<>();
        result.success(databaseService.addDatabase(getSid(), databaseInfo, getUserId()));
        result.setMsg(Vm.INSERT_SUCCESS);
        return result;
    }

//    /**
//     * 新增数据库评估
//     *
//     * @param model 数据库评估信息
//     * @return 新增的结果
//     * @author yeweiwei
//     * @date 2021/11/26 8:42
//     */
//    @ActionRights(userAction = EnumUserAction.新增, menu = EnumMenu.数据库评估管理)
//    @PostMapping(value = "/addDatabaseEvaluation")
//    public Result<String> addDatabaseEvaluation(@RequestBody @Valid DatabaseEvaluationInfoModel model) {
//        super.saveInfoLog();
//        return new Result<String>().successMsg(
//                databaseEvaluationService.addDatabaseEvaluation(model, getSid(), getUserId()));
//    }

    /**
     * 新增评估的数据库
     *
     * @param did   数据库id
     * @param vYear 年份
     * @return 新增的结果
     * @author huxubin
     * @date 2022/1/20 10:39
     */
    @ActionRights(userAction = EnumUserAction.新增, menu = EnumMenu.数据库评估管理)
    @PostMapping(value = "/addDatabaseEvaluation")
    public Result<String> addDatabaseEvaluation(@RequestParam(value = "did") Long did,
                                                @RequestParam(value = "vYear") Integer vYear) {
        super.saveInfoLog();
        return new Result<String>().successMsg(
                databaseEvaluationService.addEvaluationDatabase(getSid(), getUserId(),did,vYear));
    }

    /**
     * 联想查询数据库
     *
     * @param search 检索条件
     * @return 数据库集合
     * @author majuehao
     * @date 2021/11/26 9:29
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库评估管理)
    @RequestMapping(value = "/findDatabaseListBySearch", method = RequestMethod.GET)
    public MultipleResult<IdNameModel> findDatabaseListBySearch(@RequestParam(value = "search") String search) {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(databaseService.findDatabaseListBySearch(getSid(), search));
    }

    /**
     * 编辑回显
     *
     * @param id 数据库评估id
     * @return 数据库评估详情
     * @author yeweiwei
     * @date 2021/11/29 14:19
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库评估管理)
    @GetMapping(value = "/findOneDatabaseEvaluation")
    public Result<DatabaseEvaluationInfoModel> findOneDatabaseEvaluation(@RequestParam(value = "id") Long id) {
        super.saveInfoLog();
        return new Result<DatabaseEvaluationInfoModel>().success(
                databaseEvaluationService.findOneDatabaseEvaluation(id, getSid(), getUserId()));
    }

    /**
     * 修改数据库评估
     *
     * @param model 数据库评估信息
     * @return 修改的结果
     * @author yeweiwei
     * @date 2021/11/26 8:42
     */
    @ActionRights(userAction = EnumUserAction.修改, menu = EnumMenu.数据库评估管理)
    @PostMapping(value = "/updateDatabaseEvaluation")
    public Result<String> updateDatabaseEvaluation(@RequestBody @Valid DatabaseEvaluationInfoModel model) {
        super.saveInfoLog();
        return new Result<String>().successMsg(
                databaseEvaluationService.updateDatabaseEvaluation(model, getSid(), getUserId()));
    }

    /**
     * 上传导入评估文件
     *
     * @param file 表格
     * @return 文件信息
     * @author yeweiwei
     * @date 2021/11/26 16:08
     */
    @LoginCheck(action = EnumLoginAction.Normal)
    @PostMapping(value = "/uploadEvaluationExcelFile")
    public Result<FileModel> uploadEvaluationExcelFile(@RequestParam MultipartFile file) {
        super.saveInfoLog();
        return new Result<FileModel>().success(uploadService.uploadExcelFile(file));
    }

    /**
     * 导出评估列表
     *
     * @param vYear      年份
     * @param did        数据库id
     * @param language   语种
     * @param type       资源类型
     * @param resultType 评估结果
     * @return 导出文件
     * @author yeweiwei
     * @date 2021/11/29 14:40
     */
    @ActionRights(userAction = EnumUserAction.导出, menu = EnumMenu.数据库评估管理)
    @GetMapping(value = "/exportEvaluationList")
    public ResponseEntity<byte[]> exportEvaluationList(
            @RequestParam(value = "vYear") Integer vYear,
            @RequestParam(value = "did", required = false) Long did,
            @RequestParam(value = "language", defaultValue = "999") Long language,
            @RequestParam(value = "fulltextFlag", defaultValue = "999") Integer fulltextFlag,
            @RequestParam(value = "type", defaultValue = "999") Long type,
            @RequestParam(value = "resultType", defaultValue = "999") Integer resultType) {
        super.saveInfoLog();
        checkYear(vYear);
        return databaseEvaluationService
                .exportEvaluationList(getSid(), vYear, did, language, fulltextFlag, type, resultType);
    }

    /**
     * 上传评估附件
     *
     * @param file 文件
     * @return 文件信息
     * @author yeweiwei
     * @date 2021/12/1 13:10
     */
    @LoginCheck(action = EnumLoginAction.Normal)
    @PostMapping(value = "/uploadEvaluationAttachFile")
    public Result<FileModel> uploadEvaluationAttachFile(@RequestParam MultipartFile file) {
        super.saveInfoLog();
        return new Result<FileModel>().success(uploadService.uploadFile(file,
                Constant.MAX_EVALUATION_ATTACH_FILE_SIZE));
    }

    /**
     * 评估信息回显
     *
     * @param evaluationId 评估id
     * @return 评估信息
     * @author yeweiwei
     * @date 2021/12/1 13:08
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库评估管理)
    @GetMapping(value = "/findOneEvaluation")
    public Result<EvaluationModel> findOneEvaluation(@RequestParam(value = "evaluationId") Long evaluationId) {
        super.saveInfoLog();
        return new Result<EvaluationModel>().success(databaseEvaluationService.findOneEvaluation(getSid(),
                evaluationId));
    }

    /**
     * 评估
     *
     * @param evaluationModel 评估信息
     * @return 评估结果
     * @author yeweiwei
     * @date 2021/12/1 13:08
     */
    @ActionRights(userAction = EnumUserAction.修改, menu = EnumMenu.数据库评估管理)
    @PostMapping(value = "/evaluateDatabase")
    public Result<String> evaluateDatabase(@RequestBody @Valid EvaluationModel evaluationModel) {
        super.saveInfoLog();
        return new Result<String>().successMsg(
                databaseEvaluationService.evaluateDatabase(getSid(), getUserId(), evaluationModel));
    }

    /**
     * 查询评估新增的数据库下拉
     *
     * @param vYear 评估id
     * @return 查询评估新增的数据库下拉
     * @author yeweiwei
     * @date 2021/12/1 13:08
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库评估管理)
    @GetMapping(value = "/findAddSelectList")
    public MultipleResult<IdNameModel> findAddSelectList(@RequestParam(value = "vYear") Integer vYear) {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(selectListService.findEvaluationAddSelectList(getSid(),
                vYear));
    }
}
