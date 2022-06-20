package com.livk.batch.domain;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * User
 * </p>
 *
 * @author livk
 * @date 2021/12/27
 */
@Data
public class User {

    private String userName;

	private String sex;

	private Integer age;

	private String address;

	private Integer status;

	private Date createTime;

}
