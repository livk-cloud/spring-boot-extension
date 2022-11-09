package com.livk.ck.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>
 * User
 * </p>
 *
 * @author livk
 * @date 2022/4/24
 */
@Data
@Accessors(chain = true)
public class User {

    private Integer id;

    private String appId;

    private String version;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date regTime;

}
