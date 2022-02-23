package com.kcidea.erms.service.database.impl;

import com.google.common.collect.Maps;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.dao.database.DatabaseBuyDao;
import com.kcidea.erms.dao.ers.SchoolDatabaseSubjectRelDao;
import com.kcidea.erms.dao.ers.SubjectDao;
import com.kcidea.erms.enums.database.EnumBuyType;
import com.kcidea.erms.enums.database.EnumCheckState;
import com.kcidea.erms.enums.database.EnumNatureType;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.database.buyplan.DatabaseBuyListModel;
import com.kcidea.erms.model.database.DatabasePropertyModel;
import com.kcidea.erms.model.subject.SubjectAndCategoryModel;
import com.kcidea.erms.service.common.BaseService;
import com.kcidea.erms.service.database.DatabaseBuyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/12/7
 **/
@Slf4j
@Service
public class DatabaseBuyServiceImpl extends BaseService implements DatabaseBuyService {

    @Resource
    private DatabaseBuyDao databaseBuyDao;

    @Resource
    private SubjectDao subjectDao;

    @Resource
    private SchoolDatabaseSubjectRelDao schoolDatabaseSubjectRelDao;


    /**
     * 查询数据库采购列表
     *
     * @param sid        学校id
     * @param vYear      年份
     * @param did        数据库id
     * @param language   语种
     * @param type       资源类型
     * @param natureType 数据库性质
     * @param buyType    采购类型
     * @param subjectId  学科id
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @return 数据库采购列表
     * @author yeweiwei
     * @date 2021/12/7 20:01
     */
    @Override
    public PageResult<DatabaseBuyListModel> findBuyList(Long sid, Integer vYear, Long did, Long language, Long type,
                                                        Integer natureType, Integer buyType, Long subjectId,
                                                        Integer pageNum, Integer pageSize) {
        super.checkSid(sid);
        super.checkPageParam(pageNum, pageSize);
        List<DatabaseBuyListModel> databaseBuyList =
                databaseBuyDao.findListBySidDidYearNatureBuyType(sid, did, vYear, natureType, buyType);

        if (!language.equals(Constant.ALL_LONG_VALUE) || !type.equals(Constant.ALL_LONG_VALUE)) {
            // 查询符合条件的数据库id集合 筛选
            Set<Long> didSet = super.getDidSetBySidPropertyIdLanguageId(sid, type, language);
            databaseBuyList = databaseBuyList.stream().filter(s -> didSet.contains(s.getDid())).collect(Collectors.toList());
        }

        if (null != subjectId) {
            Set<Long> didSet = super.getDidSetBySidSubject(sid, subjectId);
            databaseBuyList = databaseBuyList.stream().filter(s -> didSet.contains(s.getDid())).collect(Collectors.toList());
        }

        // 分页数据
        int total = databaseBuyList.size();
        databaseBuyList = databaseBuyList.stream().skip(Math.min(pageNum * pageSize - pageSize, total))
                .limit(pageSize).collect(Collectors.toList());

        //填充数据
        this.handleDatabaseBuyList(sid, databaseBuyList);

        PageResult<DatabaseBuyListModel> result = new PageResult<>();
        result.success(databaseBuyList, total);
        return result;
    }

    /**
     * 填充数据库采购列表数据
     *
     * @param sid             学校id
     * @param databaseBuyList 数据库采购列表
     * @author yeweiwei
     * @date 2021/12/8 13:14
     */
    private void handleDatabaseBuyList(Long sid, List<DatabaseBuyListModel> databaseBuyList) {
        // 查询数据库名称
        Map<Long, String> dbIdNameMap = super.getDidNameMapBySid(sid);
        // 数据库学科覆盖
        Map<Long, String> subjectIdNameMap = Maps.newHashMap();
        // 构造数据信息
        for (DatabaseBuyListModel model : databaseBuyList) {
            Long dId = model.getDid();
            model.setDName(dbIdNameMap.getOrDefault(dId, ""));

            //数据库语种属性
            DatabasePropertyModel databaseProperty = super.findDatabaseProperty(sid, dId);
            model.setLanguage(databaseProperty.getLanguage());
            model.setProperties(databaseProperty.getProperties());

            //学科覆盖
            SubjectAndCategoryModel subjectAndCategoryModel = super.findDatabaseSubjects(sid, dId);
            String databaseSubjects = subjectAndCategoryModel == null ? "" : subjectAndCategoryModel.getSubject();
            model.setSubjects(databaseSubjects);

            //枚举对应的string
            EnumBuyType enumBuyType = EnumBuyType.getEnumBuyType(model.getBuyType());
            model.setBuyTypeStr(enumBuyType == null ? "" : enumBuyType.getName());
            EnumNatureType enumNatureType = EnumNatureType.getEnumNatureType(model.getNatureType());
            model.setNatureTypeStr(enumNatureType == null ? "" : enumNatureType.getName());
            EnumCheckState checkState = EnumCheckState.getCheckState(model.getState());
            model.setStateStr(checkState == null ? "" : checkState.getName());

            //价格改为万元单位
            BigDecimal price = model.getPrice();
            model.setPriceStr(price.divide(Constant.Price.TEN_THOUSAND, 4, BigDecimal.ROUND_HALF_UP)
                    .stripTrailingZeros().toPlainString());
        }
    }

    /**
     * 查询在学校订购列表中的数据库id名称集合
     *
     * @param sid   学校id
     * @param vYear 年份
     * @return 数据库id名称集合
     * @author yeweiwei
     * @date 2021/12/8 13:31
     */
    @Override
    public List<IdNameModel> findDatabaseSelectList(Long sid, Integer vYear) {
        super.checkSid(sid);

        //根据sid查询数据库id集合
        Set<Long> didSet = databaseBuyDao.findDidSetBySidYear(sid, vYear);

        return super.findDatabaseIdNameList(didSet, sid);
    }
}
