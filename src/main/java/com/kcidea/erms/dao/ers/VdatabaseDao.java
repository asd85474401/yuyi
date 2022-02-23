package com.kcidea.erms.dao.ers;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.ers.Vdatabase;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.database.detail.DatabaseInfoModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/16
 **/
@DS("slave_ers")
public interface VdatabaseDao extends BaseMapper<Vdatabase> {
    /**
     * 根据学校id查询数据库名称，优先使用别名
     *
     * @param sid 学校id
     * @return 数据库id、名称集合
     * @author yeweiwei
     * @date 2021/11/16 14:50
     */
    List<IdNameModel> findDidNameListBySid(@Param("sid") Long sid);


    /**
     * 根据学校id、数据库id，查询数据库名称，优先使用别名
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库id、名称集合
     * @author yeweiwei
     * @date 2021/11/16 14:50
     */
    IdNameModel findDidNameListBySidDid(@Param("sid") Long sid, @Param("did") Long did);

    /**
     * 根据学校id、数据库id查询数据库信息
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库信息
     * @author yeweiwei
     * @date 2021/11/25 14:07
     */
    DatabaseInfoModel findOneBySidDid(@Param("sid") Long sid, @Param("did") Long did);

    /**
     * 根据学校id、数据库名称查询数据库
     *
     * @param sid  学校id
     * @param name 名称
     * @return 数据库
     * @author yeweiwei
     * @date 2021/11/25 15:44
     */
    Vdatabase findOneBySidName(@Param("sid") Long sid, @Param("name") String name);

    /**
     * 查询符合学校id、名称、首字母数据库
     *
     * @param sid    学校id
     * @param name   名称
     * @param letter 首字母
     * @return 数据库id名称集合
     * @author majuehao
     * @date 2021/11/25 19:52
     **/
    List<IdNameModel> findListBySidNameLetter(@Param("sid") Long sid,
                                              @Param("name") String name,
                                              @Param("letter") String letter);

    /**
     * 根据学校id查询数据库id、名称集合，不使用别名
     *
     * @param sid 学校id
     * @return 数据库id名称集合
     * @author yeweiwei
     * @date 2021/11/29 16:56
     */
    List<IdNameModel> findIdNameListBySid(@Param("sid") Long sid);

    /**
     * 根据学校id、数据库id查询数据库名称
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库名称
     * @author yeweiwei
     * @date 2021/12/6 17:01
     */
    String findNameBySidDid(@Param("sid") Long sid, @Param("did") Long did);
}
