package com.kcidea.erms.service.company;

import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.domain.database.DatabaseAccessUrl;
import com.kcidea.erms.domain.database.DatabaseLevel;
import com.kcidea.erms.model.company.AccessUrlCheckModel;
import com.kcidea.erms.model.company.CompanyWriteCheckModel;
import com.kcidea.erms.model.company.ContactPeopleCheckModel;
import com.kcidea.erms.model.company.DataBaseInfoCheckModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/01/14
 **/
public interface CompanyWriteCheckService {

    /**
     * 查询数据商填写信息审核列表
     *
     * @param sid       学校id
     * @param did       数据库
     * @param tableType 类型
     * @param state     审核状态
     * @param pageNum   页码
     * @param pageSize  每页数量
     * @return 列表
     * @author majuehao
     * @date 2021/10/18 10:27
     **/
    PageResult<CompanyWriteCheckModel> findCompanyWriteCheckList(Long sid, Long did, Integer tableType, Integer state,
                                                                 Integer pageNum, Integer pageSize);

    /**
     * 审核数据商填写信息
     *
     * @param sid    学校id
     * @param userId 用户id
     * @param id     填写信息的id
     * @param state  审核状态
     * @param remark 审核说明
     * @return 修改的结果
     * @author majuehao
     * @date 2021/12/21 9:49
     **/
    String updateCompanyWriteCheck(Long sid, Long userId, Long id, Integer state, String remark);

    /**
     * 添加层级信息,校验是否真的审核通过
     *
     * @param sid       学校id
     * @param totalDid  总库id
     * @param sonDidSet 子库id集合
     * @param userId    用户id
     * @param now       时间
     * @author majuehao
     * @date 2022/1/18 17:17
     **/
    void addDatabaseLevelAndVerify(Long sid, Long totalDid, Set<Long> sonDidSet, Long userId, LocalDateTime now);

    /**
     * 判断总库能否导入
     *
     * @param didLevelMap          数据库层级map
     * @param totalDid             总库id
     * @param totalDidSonDidSetMap 总库对应的子库map
     * @author majuehao
     * @date 2022/1/25 15:17
     **/
    void checkTotalDatabaseLevel(Map<Long, DatabaseLevel> didLevelMap, Long totalDid,
                                 Map<Long, Set<Long>> totalDidSonDidSetMap);

    /**
     * 判断子库能否导入
     *
     * @param sid                  学校id
     * @param sonDid               子库id
     * @param totalDidSonDidSetMap 总库对应的子库map
     * @param didLevelMap          数据库层级map
     * @param totalDid             总库id
     * @param userId               用户id
     * @author majuehao
     * @date 2022/1/25 15:13
     **/
    void checkSonDatabaseLevel(Long sid, Long sonDid, Map<Long, Set<Long>> totalDidSonDidSetMap,
                               Map<Long, DatabaseLevel> didLevelMap,
                               Long totalDid, Long userId);


    /**
     * 查询数据商填写-》访问信息-》审核回显
     *
     * @param sid 学校id
     * @param id  审核记录的id
     * @return 数据商填写-》访问信息-》审核回显
     * @author huxubin
     * @date 2022/1/18 11:06
     */
    AccessUrlCheckModel findAccessUrlCheck(Long sid, Long id);

    /**
     * 查询数据商填写-》库商信息(联系人)-》审核回显
     *
     * @param sid 学校id
     * @param id  审核记录的id
     * @return 数据商填写-》库商信息(联系人)-》审核回显
     * @author huxubin
     * @date 2022/1/18 14:39
     */
    ContactPeopleCheckModel findContactPeopleCheck(Long sid, Long id);

    /**
     * 查询数据商填写-》基本信息-》审核回显
     *
     * @param sid 学校id
     * @param id  审核记录的id
     * @return 数据商填写-》基本信息-》审核回显
     * @author huxubin
     * @date 2022/1/18 15:47
     */
    DataBaseInfoCheckModel findBaseInfoCheck(Long sid, Long id);

    /**
     * 根据学校id和审核查询审核的状态
     *
     * @param sid 学校id
     * @param id  审核id
     * @return 审核的状态
     * @author huxubin
     * @date 2022/1/19 9:38
     */
    Integer findCompanyWriteState(Long sid, Long id);

    /**
     * 根据学校查询访问链接，按did分组
     *
     * @param sid 学校id
     * @return 访问链接
     * @author majuehao
     * @date 2022/1/24 16:28
     **/
    Map<Long, List<DatabaseAccessUrl>> findSchoolAccessUrlMap(Long sid);
}
