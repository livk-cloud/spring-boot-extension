package com.livk.r2dbc.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * <p>
 * User
 * </p>
 *
 * @author livk
 */
@Table("\"user\"")
public record User(@Id Long id, String username, String password) {

}
