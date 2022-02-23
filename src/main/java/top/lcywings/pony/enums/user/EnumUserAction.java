package top.lcywings.pony.enums.user;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/11/9
 **/
public enum EnumUserAction {

    /**
     * 显示
     */
    显示(1, "显示","showFlag"),

    /**
     * 查询
     */
    查询(2, "查询","selectFlag"),

    /**
     * 新增
     */
    新增(3, "新增","insertFlag"),

    /**
     * 修改
     */
    修改(4, "修改","updateFlag"),

    /**
     * 删除
     */
    删除(5, "删除","deleteFlag"),

    /**
     * 导入
     */
    导入(6, "导入","importFlag"),

    /**
     * 导出
     */
    导出(7, "导出","exportFlag"),

    /**
     * 其他
     */
    其他(8, "其他","otherFlag");

    int value;
    String name;
    String operationFlag;

    EnumUserAction(int value, String name, String operationFlag) {
        this.value = value;
        this.name = name;
        this.operationFlag = operationFlag;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperationFlag() {
        return operationFlag;
    }

    public EnumUserAction setOperationFlag(String operationFlag) {
        this.operationFlag = operationFlag;
        return this;
    }

    /**
     * 根据值获取对应的枚举
     *
     * @param type 传入的值
     * @return 枚举
     */
    public static EnumUserAction getUserAction(int type) {
        for (EnumUserAction userRightsType : EnumUserAction.values()) {
            if (userRightsType.value == type) {
                return userRightsType;
            }
        }
        return EnumUserAction.显示;
    }
}
