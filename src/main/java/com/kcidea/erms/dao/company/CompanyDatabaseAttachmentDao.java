package com.kcidea.erms.dao.company;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.company.CompanyDatabaseAttachment;
import com.kcidea.erms.model.database.AttachmentDontCheckModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/1/6 15:19
 **/
public interface CompanyDatabaseAttachmentDao extends BaseMapper<CompanyDatabaseAttachment> {

    /**
     * 根据学校id、对应的主键的id、类型，查询文件列表
     *
     * @param sid  学校id
     * @param id   对应的主键的id
     * @param type 类型
     * @return 附件列表
     * @author majuehao
     * @date 2021/12/24 14:33
     **/
    List<AttachmentDontCheckModel> findFileListBySidUniqueIdType(@Param("sid") Long sid,
                                                                 @Param("id") Long id,
                                                                 @Param("type") int type);

    /**
     * 根据学校id、对应的主键的id、类型，查询文件列表
     *
     * @param sid  学校id
     * @param id   对应的主键的id
     * @param type 类型
     * @return 附件列表
     * @author majuehao
     * @date 2021/12/24 14:33
     **/
    List<CompanyDatabaseAttachment> findListBySidUniqueIdType(@Param("sid") Long sid,
                                                              @Param("id") Long id,
                                                              @Param("type") int type);

    /**
     * 根据学校id、对应的主键的id、类型，删除附件列表
     *
     * @param sid  学校id
     * @param id   对应的主键的id
     * @param type 类型
     * @author majuehao
     * @date 2021/12/24 14:33
     **/
    void deleteBySidUniqueIdType(@Param("sid") Long sid,
                                 @Param("id") Long id,
                                 @Param("type") int type);
}