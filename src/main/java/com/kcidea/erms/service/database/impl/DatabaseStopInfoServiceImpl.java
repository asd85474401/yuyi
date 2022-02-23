package com.kcidea.erms.service.database.impl;

import com.alibaba.excel.EasyExcel;
import com.google.common.collect.Lists;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.common.util.BeanListCopyUtils;
import com.kcidea.erms.common.util.ConvertUtil;
import com.kcidea.erms.common.util.DateTimeUtil;
import com.kcidea.erms.common.util.DownloadUtil;
import com.kcidea.erms.dao.database.DatabaseAttachmentDao;
import com.kcidea.erms.dao.database.DatabaseStopInfoDao;
import com.kcidea.erms.dao.ers.SchoolDatabaseListDao;
import com.kcidea.erms.domain.database.DatabaseAttachment;
import com.kcidea.erms.domain.database.DatabaseStopInfo;
import com.kcidea.erms.domain.ers.SchoolDatabaseList;
import com.kcidea.erms.enums.common.EnumTrueFalse;
import com.kcidea.erms.enums.database.EnumAttachmentType;
import com.kcidea.erms.enums.fund.EnumOrderType;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.database.AttachmentModel;
import com.kcidea.erms.model.database.DatabasePropertyModel;
import com.kcidea.erms.model.database.AttachmentDontCheckModel;
import com.kcidea.erms.model.database.stopinfo.DatabaseStopInfoModel;
import com.kcidea.erms.service.common.BaseService;
import com.kcidea.erms.service.database.DatabaseStopInfoService;
import com.kcidea.erms.service.database.SchoolDataBaseListService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/24
 **/
@Service
public class DatabaseStopInfoServiceImpl extends BaseService implements DatabaseStopInfoService {

    @Resource
    private DatabaseStopInfoDao databaseStopInfoDao;

    @Resource
    private DatabaseAttachmentDao databaseAttachmentDao;

    @Resource
    private SchoolDatabaseListDao schoolDatabaseListDao;

    @Resource
    private SchoolDataBaseListService schoolDataBaseListService;

    @Value("${my-config.temp-path}")
    private String TEMP_PATH;

    @Value("${my-config.file-url}")
    private String FILE_URL;


    /**
     * 查询数据库停订列表
     *
     * @param sid        学校id
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
    @Override
    public PageResult<DatabaseStopInfoModel> findDatabaseStopList(Long sid, Integer vYear, Long did, Long languageId, Long type,
                                                                  Integer pageNum, Integer pageSize) {
        // 校验参数
        super.checkSidDid(sid, did);
        super.checkPageParam(pageNum, pageSize);

        // 先查询停订表查询符合条件的数据
        List<DatabaseStopInfoModel> list = databaseStopInfoDao.findListBySidYearDid(sid, vYear, did);

        if (!languageId.equals(Constant.ALL_LONG_VALUE) || !type.equals(Constant.ALL_LONG_VALUE)) {
            //查询符合语种、资源类型的数据库id，筛选
            Set<Long> didSet = super.getDidSetBySidPropertyIdLanguageId(sid, type, languageId);
            list = list.stream().filter(s -> didSet.contains(s.getDId())).collect(Collectors.toList());
        }

        int total = list.size();

        // 分页
        list = list.stream().skip(Math.min(pageNum * pageSize - pageSize, total))
                .limit(pageSize).collect(Collectors.toList());

        // 查询数据库名称
        Map<Long, String> dbIdNameMap = super.getDidNameMapBySid(sid);

        // 订购年份
        String orderYears;

        // 填充数据
        for (DatabaseStopInfoModel model : list) {
            // 数据库名
            model.setDatabaseName(dbIdNameMap.getOrDefault(model.getDId(), ""));

            // 语种 资源类型
            DatabasePropertyModel databasePropertyModel = super.findDatabaseProperty(sid, model.getDId());
            model.setTypes(databasePropertyModel.getProperties());
            model.setLanguage(databasePropertyModel.getLanguage());

            // 订购年份
            orderYears = DateTimeUtil.getYearRangeByYearList(schoolDatabaseListDao.findYearListBySidDid(sid, model.getDId()));
            model.setOrderYears(orderYears);

            // 附件
            List<AttachmentModel> attachments = databaseAttachmentDao.findNamePathListBySidUniqueIdType(sid,
                    model.getId(), EnumAttachmentType.停订附件.getValue());
            model.setAttachmentModelList(BeanListCopyUtils.copyListProperties(attachments, AttachmentDontCheckModel::new));
        }

        PageResult<DatabaseStopInfoModel> result = new PageResult<>();
        result.success(list, total);
        return result;
    }

    /**
     * 回显编辑停订数据库
     *
     * @param sid            学校id
     * @param databaseStopId 停订数据库id
     * @return 停订数据库详情
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @Override
    public DatabaseStopInfoModel findOneById(Long sid, Long databaseStopId) {
        DatabaseStopInfo info = databaseStopInfoDao.findOneBySidDatabaseStopId(sid, databaseStopId);
        if (info == null) {
            return new DatabaseStopInfoModel();
        }
        DatabaseStopInfoModel model = new DatabaseStopInfoModel();
        BeanUtils.copyProperties(info, model);


        Long did = info.getDId();

        // 数据库名
        model.setDatabaseName(super.findDatabaseName(sid, did));

        // 语种 资源类型
        DatabasePropertyModel databasePropertyModel = super.findDatabaseProperty(sid, did);
        model.setTypes(databasePropertyModel.getProperties());
        model.setLanguage(databasePropertyModel.getLanguage());

        // 订购年份
        String orderYears = DateTimeUtil.getYearRangeByYearList(schoolDatabaseListDao.
                findYearListBySidDid(sid, did));
        model.setOrderYears(orderYears);

        // 附件
        List<AttachmentModel> attachments = databaseAttachmentDao.findNamePathListBySidUniqueIdType(
                sid, did, EnumAttachmentType.停订附件.getValue());
        model.setAttachmentModelList(BeanListCopyUtils.copyListProperties(attachments, AttachmentDontCheckModel::new));

        return model;
    }

    /**
     * 新增或编辑停订数据库
     *
     * @param sid                   学校id
     * @param userId                学校id
     * @param databaseStopInfoModel 停订数据库详情
     * @return 新增或编辑的结果
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @Override
    public String addOrUpdateDatabaseStopInfo(Long sid, Long userId, DatabaseStopInfoModel databaseStopInfoModel) {
        // 校验参数
        checkSidUserId(sid, userId);
        if (databaseStopInfoModel == null) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        // 判断是新增还是编辑
        boolean insertFlag = false;
        if (databaseStopInfoModel.getId() == null) {
            insertFlag = true;
        }

        // 年份
        Integer vYear = databaseStopInfoModel.getVyear();
        // 数据库id
        Long did = databaseStopInfoModel.getDId();
        // 停订时间
        LocalDate stopDay = databaseStopInfoModel.getStopDay();
        // 停订说明
        String remark = databaseStopInfoModel.getRemark();
        // 附件列表
        List<AttachmentDontCheckModel> attachmentModelList = databaseStopInfoModel.getAttachmentModelList();

        DatabaseStopInfo databaseStopInfo;

        if (insertFlag) {
            // 判断是否已经添加停订说明
            if (databaseStopInfoDao.findCountBySidYearDid(sid, vYear, did) > 0) {
                throw new CustomException("该数据库" + vYear + "年已经添加停订说明");
            }
            databaseStopInfo = new DatabaseStopInfo().create(sid, did, vYear, stopDay, remark, userId,
                    LocalDateTime.now());
            databaseStopInfoDao.insert(databaseStopInfo);
        } else {
            databaseStopInfo = databaseStopInfoDao.findOneBySidDatabaseStopId(sid, databaseStopInfoModel.getId());
            if (databaseStopInfo == null) {
                throw new CustomException(Vm.NO_DATA);
            }
            BeanUtils.copyProperties(databaseStopInfoModel, databaseStopInfo);
            databaseStopInfo.setSId(sid);
            databaseStopInfo.setUpdatedBy(userId);
            databaseStopInfo.setUpdatedTime(LocalDateTime.now());
            databaseStopInfoDao.updateById(databaseStopInfo);
            // 删除已有附件
            databaseAttachmentDao.deleteBySidUniqueIdType(sid, databaseStopInfoModel.getId(),
                    EnumAttachmentType.停订附件.getValue());
        }

        // 添加附件
        if (!CollectionUtils.isEmpty(attachmentModelList)) {
            for (AttachmentDontCheckModel attachmentModel : attachmentModelList) {
                DatabaseAttachment databaseAttachment = new DatabaseAttachment().create(
                        sid, databaseStopInfo.getId(), EnumAttachmentType.停订附件.getValue(),
                        attachmentModel.getFileName(), attachmentModel.getFilePath(), userId,
                        LocalDateTime.now());
                databaseAttachmentDao.insert(databaseAttachment);
            }
        }

        // 级联修改ERS学校数据库表
        SchoolDatabaseList schoolDatabaseList = schoolDataBaseListService.findOneBySidYearDid(sid, vYear, did);
        // 如果没有查询到，说明是新增的数据库,需要向ERS学校数据库表中插入一条停订的数据
        if (schoolDatabaseList == null) {
            schoolDatabaseList = new SchoolDatabaseList().create(did, "", sid, EnumOrderType.停订数据库.getValue(),
                    vYear, EnumTrueFalse.是.getValue(), did, userId, LocalDateTime.now());
            schoolDataBaseListService.addOrUpdateDataStopInfo(schoolDatabaseList, true);
        } else {
            schoolDatabaseList.setOrderType(EnumOrderType.停订数据库.getValue());
            schoolDatabaseList.setUpdatedBy(userId);
            schoolDatabaseList.setUpdatedTime(LocalDateTime.now());
            schoolDataBaseListService.addOrUpdateDataStopInfo(schoolDatabaseList, false);
        }

        return insertFlag ? Vm.INSERT_SUCCESS : Vm.UPDATE_SUCCESS;
    }

    /**
     * 查询停订的数据库下拉列表
     *
     * @param sid   学校id
     * @param vYear 停订年份
     * @return 数据库集合
     * @author majuehao
     * @date 2021/11/24 14:02
     */
    @Override
    public List<IdNameModel> findDatabaseStopSelectList(Long sid, Integer vYear) {
        // 查询停订表查询符合条件的数据
        List<DatabaseStopInfoModel> list = databaseStopInfoDao.findListBySidYearDid(sid, vYear, null);

        List<IdNameModel> idNameModels =
                Lists.newArrayList(new IdNameModel().create(Constant.ALL_LONG_VALUE, Constant.ALL_STRING_VALUE));

        // 查询数据库名称
        Map<Long, String> dbIdNameMap = super.getDidNameMapBySid(sid);
        // 填充数据
        for (DatabaseStopInfoModel model : list) {
            idNameModels.add(new IdNameModel().create(model.getDId(),
                    dbIdNameMap.get(model.getDId()) == null ? "" : dbIdNameMap.get(model.getDId())));
        }
        return idNameModels;
    }


    /**
     * 删除停订数据库详情
     *
     * @param sid            学校id
     * @param databaseStopId 停订数据库id
     * @return 删除的结果
     * @author majuehao
     * @date 2021/11/22 11:03
     **/
    @Override
    public String deleteDatabaseStopInfo(Long sid, Long databaseStopId) {

        // 校验信息
        super.checkSid(sid);

        // 查询停订数据库详情
        DatabaseStopInfo databaseStopInfo = databaseStopInfoDao.findOneBySidDatabaseStopId(sid, databaseStopId);

        // 删除停订数据库详情
        databaseStopInfoDao.deleteBySidDatabaseStopId(sid, databaseStopId);

        // 删除学校数据库表数据
        if (databaseStopInfo != null) {
            schoolDatabaseListDao.deleteBySidYearDid(sid, databaseStopInfo.getVyear(), databaseStopInfo.getDId());
        }

        return Vm.DELETE_SUCCESS;

    }

    /**
     * 导出数据库停订列表
     *
     * @param sid        学校id
     * @param vYear      订购年份
     * @param did        数据库id
     * @param languageId 语种id
     * @param type       资源类型
     * @return 导出的文件
     * @author majuehao
     * @date 2021/11/22 10:04
     **/
    @Override
    public ResponseEntity<byte[]> exportDatabaseStopList(Long sid, Integer vYear, Long did, Long languageId, Long type) {
        // 校验参数
        super.checkSidDid(sid, did);

        // 先查询停订表查询符合条件的数据
        List<DatabaseStopInfoModel> list = databaseStopInfoDao.findListBySidYearDid(sid, vYear, did);

        if (!languageId.equals(Constant.ALL_LONG_VALUE) || !type.equals(Constant.ALL_LONG_VALUE)) {
            //查询符合语种、资源类型的数据库id，筛选
            Set<Long> didSet = super.getDidSetBySidPropertyIdLanguageId(sid, type, languageId);
            list = list.stream().filter(s -> didSet.contains(s.getDId())).collect(Collectors.toList());
        }

        // 导出集合
        List<DatabaseStopInfoModel> exportList = Lists.newArrayList();

        // 查询数据库名称
        Map<Long, String> dbIdNameMap = super.getDidNameMapBySid(sid);

        // 订购年份
        String orderYears;

        // 填充数据
        for (DatabaseStopInfoModel model : list) {
            DatabaseStopInfoModel exportModel = new DatabaseStopInfoModel();
            // 数据库名
            exportModel.setDatabaseName(dbIdNameMap.getOrDefault(model.getDId(), ""));

            // 语种 资源类型
            DatabasePropertyModel databasePropertyModel = super.findDatabaseProperty(sid, model.getDId());
            exportModel.setTypes(databasePropertyModel.getProperties());
            exportModel.setLanguage(databasePropertyModel.getLanguage());

            // 订购年份
            orderYears = DateTimeUtil.getYearRangeByYearList(schoolDatabaseListDao.findYearListBySidDid(sid, model.getDId()));
            exportModel.setOrderYears(orderYears);

            // 附件
            List<AttachmentModel> attachments = databaseAttachmentDao.findNamePathListBySidUniqueIdType(sid, model.getId(),
                    EnumAttachmentType.停订附件.getValue());
            model.setAttachmentModelList(BeanListCopyUtils.copyListProperties(attachments, AttachmentDontCheckModel::new));

            // 附件路径
            StringBuilder attachListBuilder = new StringBuilder();
            for (AttachmentModel attachment : attachments) {
                String filePath = attachment.getFilePath();
                attachListBuilder.append(Constant.SplitChar.LINE_FEED_CHAR).append(FILE_URL).append(filePath);

            }
            exportModel.setAttachmentUrl(attachListBuilder.toString());

            // 停订日期
            exportModel.setStopDayStr(DateTimeUtil.localDateToString(model.getStopDay(), Constant.Pattern.DATE));

            // 停订原因
            exportModel.setRemark(model.getRemark());
            exportList.add(exportModel);
        }

        // 文件名
        String fileName = ConvertUtil.objToString(vYear).concat("年")
                .concat(Constant.SplitChar.LINE_CHAR).concat("停订数据库列表导出")
                .concat(Constant.Suffix.XLSX_WITH_POINT);

        // 返回导出
        String filePath = TEMP_PATH + System.currentTimeMillis();
        EasyExcel.write(filePath, DatabaseStopInfoModel.class).sheet("停订数据库列表").doWrite(exportList);
        return DownloadUtil.getResponseEntityCanDeleteFile(filePath, fileName, EnumTrueFalse.是.getValue());
    }
}
