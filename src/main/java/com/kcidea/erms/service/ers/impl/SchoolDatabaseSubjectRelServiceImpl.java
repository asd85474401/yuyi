package com.kcidea.erms.service.ers.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.kcidea.erms.common.constant.Constant;
import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.dao.ers.SchoolDatabaseSubjectRelDao;
import com.kcidea.erms.dao.ers.SubjectDao;
import com.kcidea.erms.domain.ers.SchoolDatabaseSubjectRel;
import com.kcidea.erms.domain.ers.Subject;
import com.kcidea.erms.service.ers.SchoolDatabaseSubjectRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/29
 **/
@Slf4j
@Service
public class SchoolDatabaseSubjectRelServiceImpl implements SchoolDatabaseSubjectRelService {
    @Resource
    private SchoolDatabaseSubjectRelDao schoolDatabaseSubjectRelDao;

    @Resource
    private SubjectDao subjectDao;

    /**
     * 修改数据库学科覆盖
     *
     * @param sid         学校id
     * @param did         数据库id
     * @param subjectList 学科id集合
     * @param userId      操作人id
     * @author yeweiwei
     * @date 2021/11/29 13:27
     */
    @Override
    @DS("slave_ers")
    @Transactional(rollbackFor = Exception.class)
    public void updateDatabaseSubjectRel(Long sid, Long did, List<Long> subjectList, Long userId) {
        if (null == sid || null == did || null == userId) {
            throw new CustomException(Vm.ERROR_PARAMS);
        }

        if (CollectionUtils.isEmpty(subjectList)) {
            return;
        }

        // 时间
        LocalDateTime now = LocalDateTime.now();

        // 学科
        Subject subject;

        // 门类学科id
        Long subjectCategoryId;

        schoolDatabaseSubjectRelDao.deleteBySidDid(sid, did);
        for (Long subjectId : subjectList) {
            SchoolDatabaseSubjectRel schoolDatabaseSubjectRel = new SchoolDatabaseSubjectRel();

            // 查询学科，综合学科无需查询
            if (subjectId.equals(Constant.Subject.COMPREHENSIVE_SUBJECT_LONG)) {
                subject = new Subject().createComprehensive();
            } else {
                subject = subjectDao.selectById(subjectId);
            }

            if (subject == null) {
                continue;
            }

            // 如果是门类学科，那不存储一级学科
            subjectCategoryId = subject.getSubjectCategory();

            // 更新学科
            schoolDatabaseSubjectRel.create(sid, did, subjectId, subjectCategoryId, userId, now);
            schoolDatabaseSubjectRelDao.insert(schoolDatabaseSubjectRel);
        }
    }
}
