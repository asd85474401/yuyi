package com.kcidea.erms.controller.database;

import com.kcidea.erms.aop.ActionRights;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.result.MultipleResult;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.result.Result;
import com.kcidea.erms.controller.common.BaseController;
import com.kcidea.erms.domain.database.DatabaseContactPeople;
import com.kcidea.erms.enums.common.EnumTrueFalse;
import com.kcidea.erms.enums.menu.EnumMenu;
import com.kcidea.erms.enums.user.EnumUserAction;
import com.kcidea.erms.model.common.FileModel;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.company.AccessUrlInfoModel;
import com.kcidea.erms.model.company.ContactPeopleInfoModel;
import com.kcidea.erms.model.database.detail.DataBaseTitleDetailModel;
import com.kcidea.erms.model.database.detail.DatabaseInfoModel;
import com.kcidea.erms.model.database.info.DatabaseInfoInsertModel;
import com.kcidea.erms.service.common.SelectListService;
import com.kcidea.erms.service.common.UploadService;
import com.kcidea.erms.service.database.DataBaseDetailService;
import com.kcidea.erms.service.database.DataBaseInfoService;
import com.kcidea.erms.service.database.DataBaseSortUrlService;
import com.kcidea.erms.service.ers.DatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author huxubin
 * @version 1.0
 * @date 2022/1/5
 **/
@Slf4j
@RestController
@RequestMapping("/databaseInfo")
public class DataBaseInfoController extends BaseController {

    @Resource
    private DataBaseInfoService dataBaseInfoService;

    @Resource
    private DatabaseService databaseService;

    @Resource
    private DataBaseSortUrlService dataBaseSortUrlService;

    @Resource
    private UploadService uploadService;

    @Resource
    private DataBaseDetailService dataBaseDetailService;

    @Resource
    private SelectListService selectListService;

    /**
     * 查询数据库列表
     *
     * @param did          数据库
     * @param languageId   语种
     * @param area         所在地区
     * @param natureType   数据库性质
     * @param fulltextFlag 是否全文
     * @param propertyId   资源类型
     * @param subjectId    学科id
     * @param pageNum      页码
     * @param pageSize     每页数量
     * @return 数据库列表
     * @author majuehao
     * @date 2021/10/18 10:27
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库列表)
    @GetMapping(value = "/findDatabaseList")
    public PageResult<DataBaseTitleDetailModel> findDatabaseList(
            @RequestParam(value = "did", required = false) Long did,
            @RequestParam(value = "languageId") Long languageId,
            @RequestParam(value = "area", required = false) String area,
            @RequestParam(value = "natureType", required = false) String natureType,
            @RequestParam(value = "fulltextFlag") Integer fulltextFlag,
            @RequestParam(value = "propertyId") Long propertyId,
            @RequestParam(value = "subjectId") Long subjectId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        super.saveInfoLog();
        return dataBaseInfoService.findDatabaseList(getSid(), did, languageId, area, natureType, fulltextFlag,
                propertyId, subjectId, EnumTrueFalse.是.getValue(), pageNum, pageSize);
    }

    /**
     * 查询子库列表
     *
     * @param totalDid     主库数据库id
     * @param languageId   语种
     * @param area         所在地区
     * @param natureType   数据库性质
     * @param fulltextFlag 是否全文
     * @param propertyId   资源类型
     * @param subjectId    学科id
     * @return 数据库列表
     * @author majuehao
     * @date 2021/10/18 10:27
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库列表)
    @GetMapping(value = "/findSonDatabaseList")
    public MultipleResult<DataBaseTitleDetailModel> findSonDatabaseList(
            @RequestParam(value = "totalDid") Long totalDid,
            @RequestParam(value = "languageId") Long languageId,
            @RequestParam(value = "area", required = false) String area,
            @RequestParam(value = "natureType", required = false) String natureType,
            @RequestParam(value = "fulltextFlag") Integer fulltextFlag,
            @RequestParam(value = "propertyId") Long propertyId,
            @RequestParam(value = "subjectId") Long subjectId) {
        super.saveInfoLog();
        return new MultipleResult<DataBaseTitleDetailModel>().success(dataBaseInfoService.findSonDatabaseList(getSid(),
                totalDid, languageId, area, natureType, fulltextFlag, propertyId, subjectId));
    }

    /**
     * 导出数据库列表
     *
     * @param did          数据库
     * @param languageId   语种
     * @param area         所在地区
     * @param natureType   数据库性质
     * @param fulltextFlag 是否全文
     * @param propertyId   资源类型
     * @param subjectId    学科id
     * @return 数据库列表
     * @author majuehao
     * @date 2021/10/18 10:27
     **/
    @ActionRights(userAction = EnumUserAction.导出, menu = EnumMenu.数据库列表)
    @GetMapping(value = "/exportDatabaseList")
    public ResponseEntity<byte[]> exportDatabaseList(
            @RequestParam(value = "did", required = false) Long did,
            @RequestParam(value = "languageId") Long languageId,
            @RequestParam(value = "area", required = false) String area,
            @RequestParam(value = "natureType", required = false) String natureType,
            @RequestParam(value = "fulltextFlag") Integer fulltextFlag,
            @RequestParam(value = "propertyId") Long propertyId,
            @RequestParam(value = "subjectId") Long subjectId) {
        super.saveInfoLog();
        return dataBaseInfoService.exportDatabaseList(getSid(), did, languageId, area, natureType, fulltextFlag,
                propertyId, subjectId);
    }

    /**
     * 查询数据库的下拉选择内容
     *
     * @return 数据库的下拉选择内容
     * @author majuehao
     * @date 2022/1/12 9:52
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库列表)
    @GetMapping(value = "/findDatabaseSelectList")
    public MultipleResult<IdNameModel> findDatabaseSelectList() {
        super.saveInfoLog();
        List<IdNameModel> list = selectListService.findLevelTotalDatabaseSelectList(getSid());
        return new MultipleResult<IdNameModel>().success(list);
    }

    /**
     * 查询地区的下拉选择内容
     *
     * @return 地区的下拉选择内容
     * @author huxubin
     * @date 2022/1/6 14:31
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库列表)
    @GetMapping(value = "/findAreaSelectList")
    public MultipleResult<String> findAreaSelectList() {
        super.saveInfoLog();
        List<String> areaList = selectListService.findAreaSelectList(getSid());
        return new MultipleResult<String>().success(areaList);
    }

    /**
     * 查询数据库性质的下拉选择内容
     *
     * @return 数据库性质的下拉选择内容
     * @author huxubin
     * @date 2022/1/6 14:31
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库列表)
    @GetMapping(value = "/findNatureTypeSelectList")
    public MultipleResult<String> findNatureTypeSelectList() {
        super.saveInfoLog();
        List<String> areaList = selectListService.findNatureTypeSelectList(getSid());
        return new MultipleResult<String>().success(areaList);
    }

    /**
     * 删除数据库
     *
     * @param did 数据库id
     * @return 删除的结果
     * @author majuehao
     * @date 2022/1/13 9:53
     **/
    @ActionRights(userAction = EnumUserAction.删除, menu = EnumMenu.数据库列表)
    @RequestMapping(value = "/deleteDatabase", method = RequestMethod.DELETE)
    public Result<String> deleteDatabase(@RequestParam(value = "did") Long did) {
        super.saveInfoLog();
        return new Result<String>().successMsg(dataBaseInfoService.deleteDatabase(getSid(), did));
    }

    /**
     * 新增数据库层级
     *
     * @param did       数据库id
     * @param totalFlag 总库标识
     * @param totalDid  总库id
     * @return 新增的结果
     * @author majuehao
     * @date 2022/1/13 9:53
     **/
    @ActionRights(userAction = EnumUserAction.新增, menu = EnumMenu.数据库列表)
    @PostMapping(value = "/addDatabaseLevel")
    public Result<String> addDatabaseLevel(@RequestParam(value = "did") Long did,
                                           @RequestParam(value = "totalFlag") Integer totalFlag,
                                           @RequestParam(value = "totalDid", required = false) Long totalDid) {
        super.saveInfoLog();
        return new Result<String>().successMsg(dataBaseInfoService.addDatabaseLevel(getSid(), getUserId(), did,
                totalFlag, totalDid));
    }

    /**
     * 联想查询数据库
     *
     * @param search 检索条件
     * @return 数据库集合
     * @author majuehao
     * @date 2021/11/26 9:29
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库列表)
    @RequestMapping(value = "/findDatabaseListBySearch", method = RequestMethod.GET)
    public MultipleResult<IdNameModel> findDatabaseListBySearch(@RequestParam(value = "search") String search) {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(databaseService.findDatabaseListBySearch(getSid(), search));
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
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库列表)
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
     * 新增数据库信息
     *
     * @param databaseInfo 数据库信息
     * @return 新增的结果
     * @author yeweiwei
     * @date 2021/11/25 10:59
     */
    @ActionRights(userAction = EnumUserAction.新增, menu = EnumMenu.数据库列表)
    @PostMapping(value = "/addDatabase")
    public Result<IdNameModel> addDatabase(@RequestBody @Valid DatabaseInfoModel databaseInfo) {
        super.saveInfoLog();
        Result<IdNameModel> result = new Result<>();
        result.success(databaseService.addDatabase(getSid(), databaseInfo, getUserId()));
        result.setMsg(Vm.INSERT_SUCCESS);
        return result;
    }

    /**
     * 查询数据库的短链接
     *
     * @param did 数据库id
     * @return 数据库的短链接
     * @author huxubin
     * @date 2022/1/13 13:50
     */
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库列表)
    @GetMapping(value = "/findDataBaseSortUrl")
    public Result<String> findDataBaseSortUrl(@RequestParam(value = "did") Long did) {
        super.saveInfoLog();
        return new Result<String>().success(dataBaseSortUrlService.findApiKey(getSid(), did));
    }

    /**
     * 刷新数据库的短链接
     *
     * @param did 数据库id
     * @return 数据库的短链接
     * @author huxubin
     * @date 2022/1/13 13:50
     */
    @ActionRights(userAction = EnumUserAction.修改, menu = EnumMenu.数据库列表)
    @PostMapping(value = "/updateDataBaseSortUrl")
    public Result<String> updateDataBaseSortUrl(@RequestParam(value = "did") Long did) {
        super.saveInfoLog();
        return new Result<String>().success(dataBaseSortUrlService.updateApiKey(getSid(), did));
    }

    /**
     * 编辑数据库
     *
     * @param databaseInfoInsertModel 数据库信息详情
     * @return 更新的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @ActionRights(userAction = EnumUserAction.修改, menu = EnumMenu.数据库列表)
    @PostMapping(value = "/updateDatabase")
    public Result<String> updateDatabase(@RequestBody @Valid DatabaseInfoInsertModel databaseInfoInsertModel) {
        super.saveInfoLog();
        return new Result<String>().successMsg(dataBaseInfoService.updateDatabase(getSid(), getUserId(),
                databaseInfoInsertModel));
    }

    /**
     * 回显编辑数据库
     *
     * @param did 数据库id
     * @return 基本信息
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库列表)
    @GetMapping(value = "/findOneTotalDatabaseInfo")
    public Result<DatabaseInfoInsertModel> findOneTotalDatabaseInfo(@RequestParam(value = "did") Long did) {
        super.saveInfoLog();
        return new Result<DatabaseInfoInsertModel>()
                .success(dataBaseInfoService.findOneTotalDatabaseInfo(getSid(), did));
    }

    /**
     * 上传使用指南
     *
     * @param file 文件
     * @return 文件信息
     * @author majuehao
     * @date 2021/12/21 13:31
     **/
    @ActionRights(userAction = EnumUserAction.新增, menu = EnumMenu.数据库列表)
    @PostMapping(value = "/uploadUseGuideFile")
    public Result<FileModel> uploadUseGuideFile(@RequestParam MultipartFile file) {
        super.saveInfoLog();
        return new Result<FileModel>().success(uploadService.uploadFile(file, Constant
                .MAX_EVALUATION_ATTACH_FILE_SIZE));
    }

    /**
     * 访问信息 -> 访问信息列表
     *
     * @param did 数据库id
     * @return 访问信息列表
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库列表)
    @GetMapping(value = "/findAccessUrlList")
    public MultipleResult<AccessUrlInfoModel> findAccessUrlList(@RequestParam(value = "did") Long did) {
        super.saveInfoLog();
        return new MultipleResult<AccessUrlInfoModel>()
                .success(dataBaseDetailService.findAccessDetailModelList(getSid(), did));
    }

    /**
     * 访问信息 -> 回显编辑访问信息
     *
     * @param did      数据库id
     * @param accessId 访问id
     * @return 访问信息
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库列表)
    @GetMapping(value = "/findOneAccessUrl")
    public Result<AccessUrlInfoModel> findOneAccessUrl(@RequestParam(value = "did") Long did,
                                                       @RequestParam(value = "accessId") Long accessId) {
        super.saveInfoLog();
        return new Result<AccessUrlInfoModel>().success(dataBaseInfoService.findOneAccessUrl(getSid(), did, accessId));
    }

    /**
     * 访问信息 -> 删除访问信息
     *
     * @param did      数据库id
     * @param accessId 访问id
     * @return 删除的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @ActionRights(userAction = EnumUserAction.删除, menu = EnumMenu.数据库列表)
    @RequestMapping(value = "/deleteAccessUrl", method = RequestMethod.DELETE)
    public Result<String> deleteAccessUrl(@RequestParam(value = "did") Long did,
                                          @RequestParam(value = "accessId") Long accessId) {
        super.saveInfoLog();
        return new Result<String>().successMsg(dataBaseInfoService.deleteAccessUrl(getSid(), did, accessId));
    }

    /**
     * 访问信息 -> 新增访问信息
     *
     * @param databaseAccessUrlInfoModel 访问信息
     * @return 新增的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @ActionRights(userAction = EnumUserAction.新增, menu = EnumMenu.数据库列表)
    @PostMapping(value = "/addAccessUrl")
    public Result<String> addOrUpdateAccessUrl(@RequestBody @Valid AccessUrlInfoModel databaseAccessUrlInfoModel) {
        super.saveInfoLog();
        return new Result<String>().successMsg(
                dataBaseInfoService.addAccessUrl(getSid(), getUserId(), databaseAccessUrlInfoModel));
    }

    /**
     * 访问信息 -> 编辑访问信息
     *
     * @param databaseAccessUrlInfoModel 访问信息
     * @return 编辑的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @ActionRights(userAction = EnumUserAction.修改, menu = EnumMenu.数据库列表)
    @PostMapping(value = "/updateAccessUrl")
    public Result<String> updateAccessUrl(@RequestBody @Valid AccessUrlInfoModel databaseAccessUrlInfoModel) {
        super.saveInfoLog();
        return new Result<String>().successMsg(
                dataBaseInfoService.updateAccessUrl(getSid(), getUserId(), databaseAccessUrlInfoModel));
    }

    /**
     * 库商信息 -> 联系人列表
     *
     * @param did         数据库id
     * @param contactType 联系人类型
     * @return 联系人列表
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库列表)
    @GetMapping(value = "/findCompanyContactPeople")
    public MultipleResult<ContactPeopleInfoModel> findCompanyContactPeople(
            @RequestParam(value = "did") Long did,
            @RequestParam(value = "contactType") Integer contactType) {
        super.saveInfoLog();
        return new MultipleResult<ContactPeopleInfoModel>().success(
                dataBaseDetailService.findDatabaseContactPeopleList(getSid(), did, contactType));
    }

    /**
     * 库商信息 -> 回显编辑联系人
     *
     * @param did         数据库id
     * @param contactId   联系人id
     * @param contactType 联系人类型
     * @return 联系人
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @ActionRights(userAction = EnumUserAction.查询, menu = EnumMenu.数据库列表)
    @GetMapping(value = "/findOneContactPeople")
    public Result<DatabaseContactPeople> findOneContactPeople(@RequestParam(value = "did") Long did,
                                                              @RequestParam(value = "contactId") Long contactId,
                                                              @RequestParam(value = "contactType") Integer contactType) {
        super.saveInfoLog();
        return new Result<DatabaseContactPeople>().success(
                dataBaseInfoService.findOneContactPeople(getSid(), did, contactId, contactType));
    }

    /**
     * 库商信息 -> 新增联系人
     *
     * @param contactPeopleInfoModel 联系人详情
     * @return 新增的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @ActionRights(userAction = EnumUserAction.新增, menu = EnumMenu.数据库列表)
    @PostMapping(value = "/addContactPeople")
    public Result<String> addContactPeople(@RequestBody @Valid ContactPeopleInfoModel contactPeopleInfoModel) {
        super.saveInfoLog();
        return new Result<String>().successMsg(
                dataBaseInfoService.addContactPeople(getSid(), contactPeopleInfoModel));
    }

    /**
     * 库商信息 -> 编辑联系人
     *
     * @param contactPeopleInfoModel 联系人详情
     * @return 编辑的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @ActionRights(userAction = EnumUserAction.修改, menu = EnumMenu.数据库列表)
    @PostMapping(value = "/updateContactPeople")
    public Result<String> updateContactPeople(@RequestBody @Valid ContactPeopleInfoModel contactPeopleInfoModel) {
        super.saveInfoLog();
        return new Result<String>().successMsg(
                dataBaseInfoService.updateContactPeople(getSid(), contactPeopleInfoModel));
    }

    /**
     * 库商信息 -> 删除联系人
     *
     * @param did         数据库id
     * @param contactId   联系人id
     * @param contactType 联系人类型
     * @return 删除的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @ActionRights(userAction = EnumUserAction.删除, menu = EnumMenu.数据库列表)
    @RequestMapping(value = "/deleteCompanyContactPeople", method = RequestMethod.DELETE)
    public Result<String> deleteContactPeople(@RequestParam(value = "did") Long did,
                                              @RequestParam(value = "contactId") Long contactId,
                                              @RequestParam(value = "contactType") Integer contactType) {
        super.saveInfoLog();
        return new Result<String>().successMsg(
                dataBaseInfoService.deleteContactPeople(getSid(), did, contactId, contactType));
    }

    /**
     * 上传数据库导入文件
     *
     * @param file 表格
     * @return 文件信息
     * @author majuehao
     * @date 2021/12/21 13:31
     **/
    @ActionRights(userAction = EnumUserAction.新增, menu = EnumMenu.数据库列表)
    @PostMapping(value = "/uploadDatabaseInfoExcelFile")
    public Result<FileModel> uploadDatabaseInfoExcelFile(@RequestParam MultipartFile file) {
        super.saveInfoLog();
        return new Result<FileModel>().success(uploadService.uploadFile(file, Constant.MAX_EVALUATION_ATTACH_FILE_SIZE));
    }


}
