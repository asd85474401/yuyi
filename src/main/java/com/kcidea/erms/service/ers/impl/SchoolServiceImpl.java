package com.kcidea.erms.service.ers.impl;

import com.kcidea.erms.common.constant.Vm;
import com.kcidea.erms.common.exception.CustomException;
import com.kcidea.erms.dao.ers.SchoolDao;
import com.kcidea.erms.service.common.BaseService;
import com.kcidea.erms.service.ers.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/16
 **/
@Slf4j
@Service
public class SchoolServiceImpl extends BaseService implements SchoolService {

    @Resource
    private SchoolDao schoolDao;

    /**
     * 根据学校id查询学校开始年份
     *
     * @param sId 学校id
     * @return 学校开始年份
     * @author yeweiwei
     * @date 2021/11/16 11:19
     */
    @Override
    public Integer findStartYearBySid(Long sId) {
        super.checkSid(sId);
        Integer startYear = schoolDao.findStartYearById(sId);
        if (null == startYear) {
            throw new CustomException(Vm.SYSTEM_ERROR_PLEASE_REFRESH_AND_RETRY);
        }
        return startYear;
    }
}
