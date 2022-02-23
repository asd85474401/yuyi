package com.kcidea.erms.dao.feedback;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcidea.erms.domain.feedback.QuestionFeedbackType;
import com.kcidea.erms.model.common.IdNameModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/27 9:54
 **/
public interface QuestionFeedbackTypeDao extends BaseMapper<QuestionFeedbackType> {

    /**
     * 根据学校id、类型id，查询名字
     *
     * @param sid    学校id
     * @param typeId 类型id
     * @return 名字
     * @author majuehao
     * @date 2021/12/27 10:00
     */
    String findNameBySidTypeId(@Param("sid") Long sid, @Param("typeId") Long typeId);

    /**
     * 根据学校id、名字，查询类型id
     *
     * @param sid      学校id
     * @param typeName 类型id
     * @return 类型id
     * @author majuehao
     * @date 2021/12/27 10:00
     */
    Long findTypeIdBySidName(@Param("sid") Long sid, @Param("typeName") String typeName);

    /**
     * 根据学校id,查询名字列表
     *
     * @param sid 学校id
     * @return Id名字列表
     * @author majuehao
     * @date 2021/12/27 10:24
     **/
    List<IdNameModel> findIdNameListBySid(@Param("sid") Long sid);
}