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
 * @date 2022/5/12
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
