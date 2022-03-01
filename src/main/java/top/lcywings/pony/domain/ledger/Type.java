package top.lcywings.pony.domain.ledger;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author majuehao
 * @version 1.0
 * @date 2022/2/28 13:51
 **/
@Data
public class Type implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    public Type create(String name, String createdBy, LocalDateTime createdTime) {
        this.name = name;
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        return this;
    }

    public Type update(String name, String updatedBy, LocalDateTime updatedTime) {
        this.name = name;
        this.updatedBy = updatedBy;
        this.updatedTime = updatedTime;
        return this;
    }

    private static final long serialVersionUID = 1L;
}