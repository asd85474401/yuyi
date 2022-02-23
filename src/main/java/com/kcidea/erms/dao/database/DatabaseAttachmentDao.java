package com.kcidea.erms.dao.database;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.database.DatabaseAttachment;
import com.kcidea.erms.model.database.AttachmentModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/24 14:25
 **/
public interface DatabaseAttachmentDao extends BaseMapper<DatabaseAttachment> {

    /**
     * 根据学校id、对应的主键的id、类型，查询附件列表
     *
     * @param sid  学校id
     * @param id   对应的主键的id
     * @param type 类型
     * @return 附件列表
     * @author majuehao
     * @date 2021/12/24 14:33
     **/
    List<DatabaseAttachment> findListBySidUniqueIdType(@Param("sid") Long sid,
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
    List<AttachmentModel> findNamePathListBySidUniqueIdType(@Param("sid") Long sid,
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