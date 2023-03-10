package com.livk.example.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * User
 * </p>
 *
 * @author livk
 */
@Data
@Accessors(chain = true)
public class User {

    private Integer id;

    private String username;

    private String password;

    private Integer type;

    private Date createTime;

}
