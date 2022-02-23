package top.lcywings.pony.model.common;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/29
 **/
@Data
@Accessors(chain = true)
public class TaskParamModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学校id
     */
    private Long sid;

    /**
     * 年份
     */
    private Integer vYear;

    /**
     * 是否覆盖数据
     */
    private Integer coverFlag;
}
