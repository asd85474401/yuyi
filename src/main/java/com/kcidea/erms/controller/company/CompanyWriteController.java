package com.kcidea.erms.controller.company;

import com.kcidea.erms.aop.LoginCheck;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.result.MultipleResult;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.result.Result;
import com.kcidea.erms.controller.common.BaseController;
import com.kcidea.erms.domain.company.CompanyDatabaseContactPeople;
import com.kcidea.erms.enums.user.EnumLoginAction;
import com.kcidea.erms.model.common.DropDownModel;
import com.kcidea.erms.model.common.FileModel;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.company.*;
import com.kcidea.erms.model.database.detail.DataBaseTitleDetailModel;
import com.kcidea.erms.model.database.detail.DatabaseInfoJsonModel;
import com.kcidea.erms.model.database.detail.DatabaseInfoModel;
import com.kcidea.erms.service.common.SelectListService;
import com.kcidea.erms.service.common.UploadService;
import com.kcidea.erms.service.company.CompanyWriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 数据商填写
 *
 * @author huxubin
 * @version 1.0
 * @date 2021/12/7
 **/
@Slf4j
@RestController
@RequestMapping("/companyWrite")
public class CompanyWriteController extends BaseController {

    @Resource
    private CompanyWriteService companyWriteService;

    @Resource
    private UploadService uploadService;

    @Resource
    private SelectListService selectListService;

    /**
     * 基本信息 -> 填写基本信息
     *
     * @param totalDatabaseInfoModel 基本信息详情
     * @return 添加或更新的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @PostMapping(value = "/addTotalDatabaseInfo")
    public Result<String> addDatabaseInfo(@RequestBody @Valid TotalDatabaseInfoModel totalDatabaseInfoModel) {
        super.saveInfoLog();
        return new Result<String>().successMsg(companyWriteService.addOrUpdateDatabaseInfo(totalDatabaseInfoModel));
    }

    /**
     * 获取数据库名称
     *
     * @param apiKey apiKey
     * @return 数据库名称
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @GetMapping(value = "/findDatabaseName")
    public Result<IdNameModel> findDatabaseName(@RequestParam(value = "apiKey") String apiKey) {
        super.saveInfoLog();
        return new Result<IdNameModel>().success(companyWriteService.findDatabaseIdName(apiKey));
    }

    /**
     * 基本信息 -> 子库列表
     *
     * @param apiKey apiKey
     * @param did    子库id
     * @return 访问信息列表
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @GetMapping(value = "/findSonDatabaseList")
    public MultipleResult<DataBaseTitleDetailModel> findSonDatabaseList(
            @RequestParam(value = "apiKey") String apiKey,
            @RequestParam(value = "did", defaultValue = "999") Long did) {
        super.saveInfoLog();
        return new MultipleResult<DataBaseTitleDetailModel>().success(
                companyWriteService.findSonDatabaseList(apiKey, did));
    }

    /**
     * 基本信息 -> 回显编辑基本信息
     *
     * @param apiKey apiKey
     * @return 基本信息
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @GetMapping(value = "/findOneTotalDatabaseInfo")
    public Result<TotalDatabaseInfoModel> findOneTotalDatabaseInfo(@RequestParam String apiKey) {
        super.saveInfoLog();
        return new Result<TotalDatabaseInfoModel>().success(companyWriteService.findOneTotalDatabaseInfo(apiKey));
    }

    /**
     * 基本信息 -> 删除基本信息
     *
     * @param apiKey   apiKey
     * @param totalDid 总库id
     * @return 删除的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @RequestMapping(value = "/deleteDatabaseInfo", method = RequestMethod.DELETE)
    public Result<String> deleteDatabaseInfo(@RequestParam(value = "apiKey") String apiKey,
                                             @RequestParam(value = "totalDid") Long totalDid) {
        super.saveInfoLog();
        return new Result<String>().successMsg(companyWriteService.deleteDatabaseInfo(apiKey, totalDid));
    }

    /**
     * 库商信息 -> 供应商联系人列表
     *
     * @param apiKey apiKey
     * @return 供应商联系人列表
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @GetMapping(value = "/findCompanyContactPeople")
    public MultipleResult<ContactPeopleInfoModel> findCompanyContactPeople(@RequestParam(value = "apiKey") String apiKey) {
        super.saveInfoLog();
        return new MultipleResult<ContactPeopleInfoModel>().success(
                companyWriteService.findDatabaseContactPeople(apiKey, Constant.CompanyWrite.COMPANY_CONTACT));
    }

    /**
     * 库商信息 -> 代理商联系人列表
     *
     * @param apiKey apiKey
     * @return 代理商联系人列表
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @GetMapping(value = "/findAgentContactPeople")
    public MultipleResult<ContactPeopleInfoModel> findAgentContactPeople(@RequestParam(value = "apiKey") String apiKey) {
        super.saveInfoLog();
        return new MultipleResult<ContactPeopleInfoModel>().success(
                companyWriteService.findDatabaseContactPeople(apiKey, Constant.CompanyWrite.AGENT_CONTACT));
    }

    /**
     * 库商信息 -> 回显编辑供应商联系人
     *
     * @param apiKey    apiKey
     * @param contactId 联系人id
     * @return 供应商联系人
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @GetMapping(value = "/findOneCompanyContactPeople")
    public Result<CompanyDatabaseContactPeople> findOneCompanyContactPeople(
            @RequestParam(value = "apiKey") String apiKey,
            @RequestParam(value = "contactId") Long contactId) {
        super.saveInfoLog();
        return new Result<CompanyDatabaseContactPeople>().success(
                companyWriteService.findOneDatabaseContactPeople(apiKey, contactId, Constant.CompanyWrite.COMPANY_CONTACT));
    }

    /**
     * 库商信息 -> 回显编辑代理商联系人
     *
     * @param apiKey    apiKey
     * @param contactId 联系人id
     * @return 代理商联系人
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @GetMapping(value = "/findOneAgentContactPeople")
    public Result<CompanyDatabaseContactPeople> findOneAgentContactPeople(
            @RequestParam(value = "apiKey") String apiKey,
            @RequestParam(value = "contactId") Long contactId) {
        super.saveInfoLog();
        return new Result<CompanyDatabaseContactPeople>().success(
                companyWriteService.findOneDatabaseContactPeople(apiKey, contactId, Constant.CompanyWrite.AGENT_CONTACT));
    }

    /**
     * 库商信息 -> 删除供应商联系人
     *
     * @param apiKey    apiKey
     * @param contactId 联系人id
     * @return 删除的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @RequestMapping(value = "/deleteCompanyContactPeople", method = RequestMethod.DELETE)
    public Result<String> deleteCompanyContactPeople(
            @RequestParam(value = "apiKey") String apiKey,
            @RequestParam(value = "contactId") Long contactId) {
        super.saveInfoLog();
        return new Result<String>().successMsg(
                companyWriteService.deleteContactPeople(apiKey, contactId, Constant.CompanyWrite.COMPANY_CONTACT));
    }

    /**
     * 库商信息 -> 删除代理商联系人
     *
     * @param apiKey    apiKey
     * @param contactId 联系人id
     * @return 删除的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @RequestMapping(value = "/deleteAgentContactPeople", method = RequestMethod.DELETE)
    public Result<String> deleteAgentContactPeople(
            @RequestParam(value = "apiKey") String apiKey,
            @RequestParam(value = "contactId") Long contactId) {
        super.saveInfoLog();
        return new Result<String>().successMsg(
                companyWriteService.deleteContactPeople(apiKey, contactId, Constant.CompanyWrite.AGENT_CONTACT));
    }

    /**
     * 库商信息 -> 新增或编辑供应商联系人
     *
     * @param contactPeopleInfoModel 供应商联系人详情
     * @return 新增或编辑的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @PostMapping(value = "/addOrUpdateCompanyContactPeople")
    public Result<String> addOrUpdateCompanyContactPeople(@RequestBody @Valid ContactPeopleInfoModel
                                                                  contactPeopleInfoModel) {
        super.saveInfoLog();
        return new Result<String>().successMsg(
                companyWriteService.addOrUpdateContactPeople(contactPeopleInfoModel, Constant.CompanyWrite.COMPANY_CONTACT));
    }

    /**
     * 库商信息 -> 新增或编辑代理商联系人
     *
     * @param contactPeopleInfoModel 供应商联系人详情
     * @return 新增或编辑的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @PostMapping(value = "/addOrUpdateAgentContactPeople")
    public Result<String> addOrUpdateAgentContactPeople(@RequestBody @Valid ContactPeopleInfoModel
                                                                contactPeopleInfoModel) {
        super.saveInfoLog();
        return new Result<String>().successMsg(
                companyWriteService.addOrUpdateContactPeople(contactPeopleInfoModel, Constant.CompanyWrite.AGENT_CONTACT));
    }

    /**
     * 上传使用指南
     *
     * @param file 文件
     * @return 文件信息
     * @author majuehao
     * @date 2021/12/21 13:31
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @PostMapping(value = "/uploadUseGuideFile")
    public Result<FileModel> uploadUseGuideFile(@RequestParam MultipartFile file) {
        super.saveInfoLog();
        return new Result<FileModel>().success(uploadService.uploadFile(file, Constant
                .MAX_EVALUATION_ATTACH_FILE_SIZE));
    }

    /**
     * 查询学科覆盖下拉
     *
     * @return 学科覆盖集合
     * @author majuehao
     * @date 2022/1/6 14:35
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @GetMapping(value = "/findSubjectSelectList")
    public MultipleResult<DropDownModel> findSubjectSelectList() {
        super.saveInfoLog();
        return new MultipleResult<DropDownModel>().success(selectListService.findSubjectSelectList());
    }

    /**
     * 查询资源类型下拉
     *
     * @return 资源类型集合
     * @author majuehao
     * @date 2022/1/6 14:35
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @GetMapping(value = "/findTypeSelectList")
    public MultipleResult<IdNameModel> findTypeSelectList() {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(selectListService.findPropertySelectList());
    }

    /**
     * 查询供应商下拉
     *
     * @param apiKey apiKey
     * @return 供应商集合
     * @author majuehao
     * @date 2022/1/6 14:35
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @GetMapping(value = "/findCompanySelectList")
    public MultipleResult<String> findCompanySelectList(@RequestParam(value = "apiKey") String apiKey) {
        super.saveInfoLog();
        return new MultipleResult<String>().success(companyWriteService.findCompanySelectList(apiKey));
    }

    /**
     * 查询代理商下拉
     *
     * @param apiKey apiKey
     * @return 代理商集合
     * @author majuehao
     * @date 2022/1/6 14:35
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @GetMapping(value = "/findAgentSelectList")
    public MultipleResult<String> findAgentSelectList(@RequestParam(value = "apiKey") String apiKey) {
        super.saveInfoLog();
        return new MultipleResult<String>().success(companyWriteService.findAgentSelectList(apiKey));
    }

    /**
     * 访问信息 -> 总库访问信息列表
     *
     * @param apiKey apiKey
     * @return 访问信息列表
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @GetMapping(value = "/findAccessUrlList")
    public MultipleResult<AccessUrlInfoModel> findAccessUrlList(@RequestParam(value = "apiKey") String apiKey) {
        super.saveInfoLog();
        return new MultipleResult<AccessUrlInfoModel>().success(companyWriteService.findAccessUrlList(apiKey));
    }

    /**
     * 访问信息 -> 子库访问信息列表
     *
     * @param apiKey apiKey
     * @param did    数据库id
     * @return 访问信息列表
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @GetMapping(value = "/findSonAccessUrlList")
    public MultipleResult<AccessUrlInfoModel> findSonAccessUrlList(@RequestParam(value = "apiKey") String apiKey,
                                                                   @RequestParam(value = "did", defaultValue = "999") Long did) {
        super.saveInfoLog();
        return new MultipleResult<AccessUrlInfoModel>().success(companyWriteService.findSonAccessUrlList(apiKey, did));
    }

    /**
     * 访问信息 -> 回显编辑访问信息
     *
     * @param apiKey   apiKey
     * @param accessId 访问id
     * @return 访问信息
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @GetMapping(value = "/findOneAccessUrl")
    public Result<AccessUrlInfoModel> findOneAccessUrl(@RequestParam(value = "apiKey") String apiKey,
                                                       @RequestParam(value = "accessId") Long accessId) {
        super.saveInfoLog();
        return new Result<AccessUrlInfoModel>().success(companyWriteService.findOneAccessUrl(apiKey, accessId));
    }

    /**
     * 访问信息 -> 新增或编辑访问信息
     *
     * @param accessUrlInfoModel 访问信息
     * @return 新增或编辑的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @PostMapping(value = "/addOrUpdateAccessUrl")
    public Result<String> addOrUpdateAccessUrl(@RequestBody @Valid AccessUrlInfoModel accessUrlInfoModel) {
        super.saveInfoLog();
        return new Result<String>().successMsg(companyWriteService.addOrUpdateAccessUrl(accessUrlInfoModel));
    }

    /**
     * 访问信息 -> 删除访问信息
     *
     * @param apiKey   apiKey
     * @param accessId 访问id
     * @return 删除的结果
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @RequestMapping(value = "/deleteAccessUrl", method = RequestMethod.DELETE)
    public Result<String> deleteAccessUrl(@RequestParam(value = "apiKey") String apiKey,
                                          @RequestParam(value = "accessId") Long accessId) {
        super.saveInfoLog();
        return new Result<String>().successMsg(companyWriteService.deleteAccessUrl(apiKey, accessId));
    }

    /**
     * 获取子库的apiKey
     *
     * @param apiKey apiKey
     * @param did    子库id
     * @return 子库的apiKey
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @GetMapping(value = "/findSonApiKey")
    public Result<String> findSonApiKey(@RequestParam(value = "apiKey") String apiKey,
                                        @RequestParam(value = "did") Long did) {
        super.saveInfoLog();
        return new Result<String>().success(companyWriteService.findSonApiKey(apiKey, did));
    }

    /**
     * 获取子库的apiKey列表
     *
     * @param apiKey apiKey
     * @return 子库的apiKey列表
     * @author majuehao
     * @date 2022/1/5 15:26
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @GetMapping(value = "/findSonApiKeyList")
    public MultipleResult<IdNameModel> findSonApiKeyList(@RequestParam(value = "apiKey") String apiKey) {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(companyWriteService.findSonApiKeyList(apiKey));
    }

    /**
     * 基本信息 -> 查询数据库集合
     *
     * @param apiKey     apiKey
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
    @LoginCheck(action = EnumLoginAction.Skip)
    @GetMapping(value = "/findFullDatabaseSelectList")
    public PageResult<IdNameModel> findFullDatabaseSelectList(
            @RequestParam(value = "apiKey") String apiKey,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "letter", required = false) String letter,
            @RequestParam(value = "propertyId", required = false, defaultValue = "999") Long propertyId,
            @RequestParam(value = "languageId", required = false, defaultValue = "999") Long languageId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        super.saveInfoLog();
        return companyWriteService.findDatabaseSelectList(apiKey, name, letter, propertyId, languageId, pageNum, pageSize);
    }

    /**
     * 基本信息 -> 联想查询数据库
     *
     * @param apiKey apiKey
     * @param search 检索条件
     * @return 数据库集合
     * @author majuehao
     * @date 2021/11/26 9:29
     **/
    @LoginCheck(action = EnumLoginAction.Skip)
    @RequestMapping(value = "/findDatabaseListBySearch", method = RequestMethod.GET)
    public MultipleResult<IdNameModel> findDatabaseListBySearch(@RequestParam(value = "apiKey") String apiKey,
                                                                @RequestParam(value = "search") String search) {
        super.saveInfoLog();
        return new MultipleResult<IdNameModel>().success(companyWriteService.findDatabaseListBySearch(apiKey, search));
    }

    /**
     * 基本信息 -> 新增数据库信息
     *
     * @param jsonModel 数据库信息
     * @return 新增的结果
     * @author yeweiwei
     * @date 2021/11/25 10:59
     */
    @LoginCheck(action = EnumLoginAction.Skip)
    @PostMapping(value = "/addDatabase")
    public Result<IdNameModel> addDatabase(@RequestBody @Valid DatabaseInfoJsonModel jsonModel) {
        super.saveInfoLog();
        Result<IdNameModel> result = new Result<>();
        if (jsonModel == null) {
            return result.error(Vm.ERROR_PARAMS);
        }
        DatabaseInfoModel databaseInfoModel = jsonModel.getDatabaseInfo();
        String apiKey = jsonModel.getApiKey();
        result.success(companyWriteService.addDatabase(apiKey, databaseInfoModel));
        result.setMsg(Vm.INSERT_SUCCESS);
        return result;
    }

    /**
     * 获取标签的审核状态
     *
     * @param apiKey    apiKey
     * @param tableType 标签类型
     * @return 审核状态
     * @author majuehao
     * @date 2021/11/25 10:59
     */
    @LoginCheck(action = EnumLoginAction.Skip)
    @GetMapping(value = "/findCheckState")
    public Result<TableCheckState> findCheckState(@RequestParam(value = "apiKey") String apiKey,
                                                  @RequestParam(value = "tableType") Integer tableType) {
        super.saveInfoLog();
        return new Result<TableCheckState>().success(companyWriteService.findCheckState(apiKey, tableType));
    }

    /**
     * 提交审核
     *
     * @param apiKey    apiKey
     * @param tableType 标签类型
     * @return 更新结果
     * @author majuehao
     * @date 2021/11/25 10:59
     */
    @LoginCheck(action = EnumLoginAction.Skip)
    @RequestMapping(value = "/updateCheckState", method = RequestMethod.POST)
    public Result<String> updateCheckState(@RequestParam(value = "apiKey") String apiKey,
                                           @RequestParam(value = "tableType") Integer tableType) {
        super.saveInfoLog();
        return new Result<String>().successMsg(companyWriteService.updateCheckState(apiKey, tableType));
    }

    /**
     * 返回修改
     *
     * @param apiKey    apiKey
     * @param tableType 标签类型
     * @return 更新结果
     * @author majuehao
     * @date 2021/11/25 10:59
     */
    @LoginCheck(action = EnumLoginAction.Skip)
    @RequestMapping(value = "/updateCheckStateReturnModify", method = RequestMethod.POST)
    public Result<String> updateCheckStateReturnModify(@RequestParam(value = "apiKey") String apiKey,
                                                       @RequestParam(value = "tableType") Integer tableType) {
        super.saveInfoLog();
        return new Result<String>().successMsg(companyWriteService.updateCheckStateReturnModify(apiKey, tableType));
    }

}
