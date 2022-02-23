package com.kcidea.erms.model.database.detail;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * @author yeweiwei
 * @version 1.0
 * @date 2021/11/25
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class DatabaseInfoJsonModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Valid
    private DatabaseInfoModel databaseInfo;

    private String apiKey;
}
