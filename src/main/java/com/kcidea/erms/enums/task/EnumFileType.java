package com.kcidea.erms.enums.task;


/**
 * @author majuehao
 * @version 1.0
 * @date 2021/12/27 8:32
 **/
public enum EnumFileType {

    /**
     * csv
     */
    csv(1000, "csv"),

    /**
     * txt
     */
    txt(2000, "txt"),

    /**
     * xlsx
     */
    xlsx(3000, "xlsx"),

    /**
     * xls
     */
    xls(4000, "xls");


    int value;
    String name;

    EnumFileType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    /**
     * 根据值获取对应的枚举
     *
     * @param type 传入的值
     * @return 枚举
     */
    public static EnumFileType getFileType(int type) {
        for (EnumFileType enumFileType : EnumFileType.values()) {
            if (enumFileType.value == type) {
                return enumFileType;
            }
        }
        return null;
    }

    /**
     * 根据值获取对应的枚举
     *
     * @param typeName 传入的值
     * @return 枚举
     */
    public static EnumFileType getFileType(String typeName) {
        for (EnumFileType enumFileType : EnumFileType.values()) {
            if (enumFileType.name.equals(typeName)) {
                return enumFileType;
            }
        }
        return null;
    }

}
