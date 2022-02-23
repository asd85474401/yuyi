package com.kcidea.erms.service.ers.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.dao.ers.*;
import com.kcidea.erms.domain.ers.*;
import com.kcidea.erms.enums.ers.EnumCompanyType;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.database.DatabasePropertyInfoModel;
import com.kcidea.erms.model.database.detail.DatabaseInfoModel;
import com.kcidea.erms.service.common.BaseService;
import com.kcidea.erms.service.ers.DatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.Collator;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/25
 **/
@Slf4j
@Service
public class DatabaseServiceImpl extends BaseService implements DatabaseService {

    @Resource
    private VdatabaseDao vdatabaseDao;

    @Resource
    private VdatabasePropertyRelDao vdatabasePropertyRelDao;

    @Resource
    private SchoolDatabaseAliasDao schoolDatabaseAliasDao;

    @Resource
    private CompanyDao companyDao;

    @Resource
    private AgentDao agentDao;

    /**
     * 查询学校自定义和系统默认的数据库集合
     *
     * @param sid        学校id
     * @param name       数据库名称
     * @param letter     首字母
     * @param propertyId 数据库属性
     * @param languageId 数据库语种
     * @param pageNum    页码
     * @param pageSize   每页数量
     * @return 数据库集合
     * @author yeweiwei
     * @date 2021/11/25 16:19
     */
    @Override
    public PageResult<IdNameModel> findDatabaseSelectList(Long sid, String name, String letter, Long propertyId,
                                                          Long languageId, Integer pageNum, Integer pageSize) {
        // 校验参数
        super.checkPageParam(pageNum, pageSize);
        super.checkSid(sid);

        // 查询符合名称、首字母数据，优先别名
        List<IdNameModel> list = vdatabaseDao.findListBySidNameLetter(sid, name, letter);

        if (!languageId.equals(Constant.ALL_LONG_VALUE) || !propertyId.equals(Constant.ALL_LONG_VALUE)) {
            // 查询符合数据库属性、数据库语种数据，优先学校沙箱数据库
            Set<Long> didSet = super.getDidSetBySidPropertyIdLanguageId(sid, propertyId, languageId);
            // 筛选
            list = list.stream().filter(s -> didSet.contains(s.getId())).collect(Collectors.toList());
        }

        // 排序
        Comparator<Object> comparator = Collator.getInstance(Locale.CHINA);
        list.sort((x, y) -> comparator.compare(x.getName(), y.getName()));

        // 分页返回
        int total = list.size();
        list = list.stream().skip(Math.min(pageNum * pageSize - pageSize, total))
                .limit(pageSize).collect(Collectors.toList());
        PageResult<IdNameModel> result = new PageResult<>();
        result.success(list, total);

        return result;
    }

    /**
     * 查询数据库信息
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库信息
     * @author yeweiwei
     * @date 2021/11/25 11:04
     */
    @Override
    public DatabaseInfoModel findDatabaseInfo(Long sid, Long did) {
        super.checkSidDid(sid, did);

        DatabaseInfoModel infoModel = vdatabaseDao.findOneBySidDid(sid, did);
        if (null == infoModel) {
            throw new CustomException(Vm.NO_DATA);
        }

        //查询数据库属性和语种
        List<DatabasePropertyInfoModel> propertyInfoList = vdatabasePropertyRelDao.findListBySidDid(sid, did);
        //如果有学校自定义的属性，就使用学校自定义的
        if (propertyInfoList.stream().anyMatch(s -> s.getSid() != null && s.getSid().equals(sid))) {
            propertyInfoList = propertyInfoList.stream()
                    .filter(s -> s.getSid() != null && s.getSid().equals(sid)).collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(propertyInfoList)) {
            infoModel.setPropertyIdList(propertyInfoList.stream().map(DatabasePropertyInfoModel::getPropertyId)
                    .distinct().collect(Collectors.toList()));
            infoModel.setProperties(propertyInfoList.stream().map(DatabasePropertyInfoModel::getPropertyName)
                    .collect(Collectors.joining(Constant.SplitChar.SEMICOLON_CHAR)));
            infoModel.setLanguageId(propertyInfoList.get(0).getLanguageId());
            infoModel.setPaperFlag(propertyInfoList.get(0).getPaperFlag());
        }

        return infoModel;
    }

    /**
     * 新增数据库
     *
     * @param sid          学校id
     * @param databaseInfo 数据库信息
     * @param userId       操作人id
     * @return 数据库信息
     * @author yeweiwei
     * @date 2021/11/25 14:53
     */
    @Override
    public IdNameModel addDatabase(Long sid, DatabaseInfoModel databaseInfo, Long userId) {
        //查询别名是否重复
        SchoolDatabaseAlias alias = schoolDatabaseAliasDao.findOneBySidName(sid, databaseInfo.getName());
        if (null != alias) {
            return new IdNameModel().create(alias.getDId(), alias.getDName(), false);
        }

        //查询名称是否重复
        Vdatabase vdatabase = vdatabaseDao.findOneBySidName(sid, databaseInfo.getName());
        if (null != vdatabase) {
            return new IdNameModel().create(vdatabase.getId(), vdatabase.getName(), false);
        }

        LocalDateTime now = LocalDateTime.now();

        //新增数据库
        Vdatabase database = new Vdatabase().create(databaseInfo.getName(), sid, userId, now);
        vdatabaseDao.insert(database);

        // 新增数据库属性关系
        Long databaseId = database.getId();
        this.addDatabasePropertyRel(sid, databaseInfo.getPropertyIdList(), databaseInfo.getLanguageId(),
                databaseInfo.getPaperFlag(), userId, databaseId);

        return new IdNameModel().create(databaseId, databaseInfo.getName(), true);
    }

    /**
     * 新增数据库属性关系
     *
     * @param sid            学校id
     * @param propertyIdList 属性id集合
     * @param languageId     语种id
     * @param paperFlag      纸本标识
     * @param userId         操作人id
     * @param did            数据库id
     * @author yeweiwei
     * @date 2021/11/26 10:55
     */
    private void addDatabasePropertyRel(Long sid, List<Long> propertyIdList, Long languageId,
                                        Integer paperFlag, Long userId, Long did) {
        LocalDateTime now = LocalDateTime.now();

        //新增属性
        for (Long propertyId : propertyIdList) {
            VdatabasePropertyRel vdatabasePropertyRel = new VdatabasePropertyRel().create(sid, did,
                    languageId, propertyId, paperFlag, userId, now);
            vdatabasePropertyRelDao.insert(vdatabasePropertyRel);
        }
    }

    /**
     * 更新数据库
     *
     * @param sid          学校id
     * @param databaseInfo 数据库信息
     * @param userId       操作人id
     * @return 更新结果
     * @author yeweiwei
     * @date 2021/11/26 9:47
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @DS("slave_ers")
    public String updateDatabase(Long sid, DatabaseInfoModel databaseInfo, Long userId) {
        super.checkSidUserId(sid, userId);

        Long did = databaseInfo.getDid();
        if (null == did) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        //数据库名称
        String name = databaseInfo.getName();

        // 更新数据库别名数据库
        this.updateDatabaseAlias(sid, did, userId, name);

        List<Long> propertyIdList = databaseInfo.getPropertyIdList();

        //查询原来的属性
        List<DatabasePropertyInfoModel> oldPropertyList = vdatabasePropertyRelDao.findListBySidDid(sid, did);
        //如果有学校自定义的属性
        if (oldPropertyList.stream().anyMatch(s -> s.getSid() != null && s.getSid().equals(sid))) {
            vdatabasePropertyRelDao.deleteBySidDid(sid, did);
        }
        this.addDatabasePropertyRel(sid, propertyIdList,
                databaseInfo.getLanguageId(), databaseInfo.getPaperFlag(), userId, did);

        return Vm.UPDATE_SUCCESS;
    }

    /**
     * 更新数据库别名
     *
     * @param sid    学校id
     * @param did    数据库id
     * @param userId 用户id
     * @param name   别名
     * @author majuehao
     * @date 2022/1/18 12:45
     **/
    private void updateDatabaseAlias(Long sid, Long did, Long userId, String name) {
        //查询别名是否重复
        SchoolDatabaseAlias existAlias = schoolDatabaseAliasDao.findOneBySidName(sid, name);
        if (null != existAlias && !existAlias.getDId().equals(did)) {
            throw new CustomException(Vm.EXIST_NAME);
        }

        //查询名称是否重复
        Vdatabase existDatabase = vdatabaseDao.findOneBySidName(sid, name);
        if (null != existDatabase && !existDatabase.getId().equals(did)) {
            throw new CustomException(Vm.EXIST_NAME);
        }

        // 更新别名
        SchoolDatabaseAlias alias = schoolDatabaseAliasDao.findOneBySidDid(sid, did);
        if (alias == null) {
            alias = new SchoolDatabaseAlias()
                    .create(sid, did, name, null, null, userId, LocalDateTime.now());
            schoolDatabaseAliasDao.insert(alias);
        } else {
            alias.setDName(name);
            alias.setUpdatedBy(userId);
            alias.setUpdatedTime(LocalDateTime.now());
            schoolDatabaseAliasDao.updateById(alias);
        }
    }

    /**
     * 新增数据商
     *
     * @param sid         学校id
     * @param userId      操作人id
     * @param companyName 数据商名称
     * @param addFlag     新增标识
     * @author yeweiwei
     * @date 2021/12/2 14:07
     */
    @Override
    @DS("slave_ers")
    @Transactional(rollbackFor = Exception.class)
    public Long findCompanyId(Long sid, Long userId, String companyName, boolean addFlag) {

        // 判断名字是否为空
        if (Strings.isNullOrEmpty(companyName)) {
            return null;
        }

        companyName = companyName.trim();

        Long companyId = null;

        //判断名称是否重复
        Company company = companyDao.findBySidName(sid, companyName);
        if (company == null) {
            if (addFlag) {
                companyId = this.addCompany(sid, userId, companyName).getId();
            }
        } else {
            companyId = company.getId();
        }
        return companyId;
    }

    /**
     * 新增供应商
     *
     * @param sid         学校id
     * @param userId      用户id
     * @param companyName 供应商名字
     * @return 供应商详情
     * @author majuehao
     * @date 2022/1/19 20:07
     **/
    private Company addCompany(Long sid, Long userId, String companyName) {
        Company company = new Company();
        company.create(sid, EnumCompanyType.自定义.getValue(), companyName, userId, LocalDateTime.now());
        companyDao.insert(company);
        return company;
    }

    /**
     * 新增代理商
     *
     * @param sid       学校id
     * @param userId    操作人id
     * @param agentName 代理商名称
     * @param addFlag   新增标识
     * @return 代理商id
     * @author yeweiwei
     * @date 2021/12/2 14:07
     */
    @Override
    @DS("slave_ers")
    @Transactional(rollbackFor = Exception.class)
    public Long findAgentId(Long sid, Long userId, String agentName, boolean addFlag) {

        // 判断名字是否为空
        if (Strings.isNullOrEmpty(agentName)) {
            return null;
        }

        agentName = agentName.trim();

        Long agentId = null;

        //判断名称是否重复
        Agent agent = agentDao.findBySidName(sid, agentName);
        if (agent == null) {
            if (addFlag) {
                agentId = this.addAgent(sid, userId, agentName).getId();
            }
        } else {
            agentId = agent.getId();
        }
        return agentId;
    }

    /**
     * 新增代理商
     *
     * @param sid       学校id
     * @param userId    用户id
     * @param agentName 代理商名字
     * @return 代理商详情
     * @author majuehao
     * @date 2022/1/19 20:07
     **/
    private Agent addAgent(Long sid, Long userId, String agentName) {
        Agent agent = new Agent();
        agent.create(sid, agentName, null, userId, LocalDateTime.now());
        agentDao.insert(agent);
        return agent;
    }

    /**
     * 联想查询数据库
     *
     * @param sid    学校id
     * @param search 检索条件
     * @return 数据库集合
     * @author majuehao
     * @date 2021/11/26 9:29
     **/
    @Override
    public List<IdNameModel> findDatabaseListBySearch(Long sid, String search) {
        super.checkSid(sid);
        if (Strings.isNullOrEmpty(search)) {
            throw new CustomException("很抱歉，请输入检索条件搜索");
        }
        search = search.trim();

        List<IdNameModel> idNameModels = vdatabaseDao.findListBySidNameLetter(sid, search, null);
        List<IdNameModel> result = Lists.newArrayList();

        // 剔除不符合条件的数据库名
        for (IdNameModel model : idNameModels) {
            if (model.getName().substring(0, search.length()).equalsIgnoreCase(search)) {
                result.add(model);
            }
        }
        return result;
    }
}
