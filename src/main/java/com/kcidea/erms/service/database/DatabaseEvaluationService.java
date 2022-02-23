package com.kcidea.erms.service.database;

import com.kcidea.erms.common.result.PageResult;
import com.kcidea.erms.model.common.IdNameModel;
import com.kcidea.erms.model.database.AttachmentModel;
import com.kcidea.erms.model.database.evaluation.DatabaseEvaluationInfoModel;
import com.kcidea.erms.model.database.evaluation.EvaluationModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/24
 **/
public interface DatabaseEvaluationService {
    /**
     * 查询学校有评估的数据库下拉框集合
     *
     * @param sid   学校id
     * @param vYear 年份
     * @return 数据库下拉集合
     * @author yeweiwei
     * @date 2021/11/24 14:21
     */
    List<IdNameModel> findDatabaseSelectList(Long sid, Integer vYear);

    /**
     * 查询评估列表
     *
     * @param sid          学校id
     * @param vYear        年份
     * @param did          数据库id
     * @param language     语种
     * @param fulltextFlag 全文标识
     * @param type         资源类型
     * @param resultType   评估结果
     * @param pageNum      页码
     * @param pageSize     每页数量
     * @return 评估列表
     * @author yeweiwei
     * @date 2021/11/24 15:26
     */
    PageResult<DatabaseEvaluationInfoModel> findDatabaseEvaluationPage(Long sid, Integer vYear, Long did, Long language,
                                                                       Integer fulltextFlag, Long type, Integer resultType,
                                                                       Integer pageNum, Integer pageSize);

    /**
     * 删除数据库评估
     *
     * @param id  数据库评估id
     * @param sid 学校id
     * @return 删除的结果
     * @author yeweiwei
     * @date 2021/11/24 18:46
     */
    String deleteDatabaseEvaluation(Long id, Long sid);

    /**
     * 新增数据库评估
     *
     * @param model  数据库评估
     * @param sid    学校id
     * @param userId 操作人id
     * @return 添加的结果
     * @author yeweiwei
     * @date 2021/11/26 8:43
     */
    String addDatabaseEvaluation(DatabaseEvaluationInfoModel model, Long sid, Long userId);

    /**
     * 修改数据库评估
     *
     * @param model  数据库评估
     * @param sid    学校id
     * @param userId 操作人id
     * @return 修改的结果
     * @author yeweiwei
     * @date 2021/11/26 13:44
     */
    String updateDatabaseEvaluation(DatabaseEvaluationInfoModel model, Long sid, Long userId);

    /**
     * 编辑回显
     *
     * @param id     数据库评估id
     * @param sid    学校id
     * @param userId 操作人id
     * @return 数据库评估信息
     * @author yeweiwei
     * @date 2021/11/29 14:20
     */
    DatabaseEvaluationInfoModel findOneDatabaseEvaluation(Long id, Long sid, Long userId);

    /**
     * 导出评估列表
     *
     * @param sid          学校id
     * @param vYear        年份
     * @param did          数据库id
     * @param language     语种
     * @param fulltextFlag 全文标识
     * @param type         资源类型
     * @param resultType   评估结果
     * @return 导出文件
     * @author yeweiwei
     * @date 2021/11/29 14:41
     */
    ResponseEntity<byte[]> exportEvaluationList(Long sid, Integer vYear, Long did, Long language, Integer fulltextFlag,
                                                Long type, Integer resultType);

    /**
     * 数据库评估
     *
     * @param sid             学校id
     * @param userId          操作人id
     * @param evaluationModel 评估内容
     * @return 评估结果
     * @author yeweiwei
     * @date 2021/12/1 11:07
     */
    String evaluateDatabase(Long sid, Long userId, EvaluationModel evaluationModel);

    /**
     * 更新附件表
     *
     * @param sid        学校id
     * @param uniqueId   表id
     * @param type       附件类型
     * @param attachList 要更新的附件列表
     * @param userId     用户id
     * @author majuehao
     * @date 2022/1/14 13:04
     **/
     void updateAttachment(Long sid, Long uniqueId, Integer type, List<AttachmentModel> attachList, Long userId);

    /**
     * 回显评估内容
     *
     * @param sid          学校id
     * @param evaluationId 评估id
     * @return 评估内容
     * @author yeweiwei
     * @date 2021/12/1 13:44
     */
    EvaluationModel findOneEvaluation(Long sid, Long evaluationId);

    /**
     * 根据学校id、数据库id查询数据库评估信息
     *
     * @param sid 学校id
     * @param did 数据库id
     * @return 数据库评估信息
     * @author yeweiwei
     * @date 2021/12/8 13:54
     */
    DatabaseEvaluationInfoModel findDatabaseEvaluationInfo(Long sid, Long did);

    /**
     * 新增评估的数据库
     * @param sid 学校id
     * @param userId 用户id
     * @param did 数据库id
     * @param vYear 年份
     * @return 新增的结果
     * @author huxubin
     * @date 2022/1/20 10:40
     */
    String addEvaluationDatabase(Long sid, Long userId, Long did, Integer vYear);
}
