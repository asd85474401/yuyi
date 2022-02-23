package top.lcywings.pony.model.common;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author huxubin
 * @version 1.0
 * @date 2021/8/3
 **/
@Data
@Accessors(chain = true)
public class IdNameModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * apiKey
     */
    private String apiKey;

    /**
     * 标识符
     */
    private boolean flag;

    public IdNameModel create(Long id, String name) {
        this.id = id;
        this.name = name;
        return this;
    }

    public IdNameModel create(Long id, String name, boolean flag) {
        this.id = id;
        this.name = name;
        this.flag = flag;

        return this;
    }
}
