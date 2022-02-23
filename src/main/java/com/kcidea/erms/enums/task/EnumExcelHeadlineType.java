package com.kcidea.erms.enums.task;

import com.google.common.collect.Lists;
import com.kcidea.erms.common.exception.CustomException;

import java.util.List;
import java.util.Map;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/7/29
 **/
public enum EnumExcelHeadlineType {

    /**
     * 数据库信息导入
     */
    DatabaseInfoImport(1, "数据库信息导入", 1, Lists.newArrayList("数据库", "语种",
            "纸电", "所在地区", "是否全文", "资源类型", "数据库性质")),

    /**
     * 用户反馈处理列表导入
     */
    UserFeedbackListImportTaskImpl(2, "用户反馈处理列表导入", 1, Lists.newArrayList(
            "反馈人", "用户身份", "所属单位", "联系邮箱", "联系电话", "反馈时间", "反馈类型", "数据库", "反馈标题",
            "反馈内容", "回复人", "回复时间", "回复内容")),

    ;

    final int value;
    final String name;
    final int headlineNum;
    final List<String> headlines;

    EnumExcelHeadlineType(int value, String name, int headlineNum, List<String> headlines) {
        this.value = value;
        this.name = name;
        this.headlineNum = headlineNum;
        this.headlines = headlines;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public int getHeadlineNum() {
        return headlineNum;
    }

    public List<String> getHeadlines() {
        return headlines;
    }

    /**
     * 检查map是否包含枚举对应的所有表头
     *
     * @param enumExcelHeadlineType 文件表头枚举
     * @param headlineMap           读出来的表头map
     * @author yeweiwei
     * @date 2021/7/29 14:20
     */
    public static void checkHeadlines(EnumExcelHeadlineType enumExcelHeadlineType, Map<Integer, String> headlineMap) {
        //需要的表头集合
        List<String> headlines = enumExcelHeadlineType.getHeadlines();
        for (String headline : headlines) {
            if (!headlineMap.containsValue(headline)) {
                throw new CustomException("文件表头缺失:[".concat(headline).concat("]字段，请按照模板文件进行检查！"));
            }
        }
    }
}
