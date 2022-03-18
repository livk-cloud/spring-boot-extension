package com.livk.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * BaseEntity
 * </p>
 *
 * @author livk
 * @date 2022/2/14
 */
@Data
public class BaseEntity<T> implements Serializable {

    protected T createTime;

    protected T updateTime;
}
