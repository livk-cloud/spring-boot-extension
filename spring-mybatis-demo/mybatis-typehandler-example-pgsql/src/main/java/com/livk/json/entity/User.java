package com.livk.json.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * <p>
 * User
 * </p>
 *
 * @author livk
 * @date 2022/5/26
 */
@Data
@TableName("\"user\"")
public class User {

	private Long id;

	private String username;

	private String password;

	private JsonNode des;

}
