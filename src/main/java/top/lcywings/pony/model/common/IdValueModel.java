package top.lcywings.pony.model.common;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author majuehao
 * @version 1.0
 * @date 2021/11/18 16:02
 **/
@Data
public class IdValueModel  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 值
     */
    private BigDecimal value;
}
