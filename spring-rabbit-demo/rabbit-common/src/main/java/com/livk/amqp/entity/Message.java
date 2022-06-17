package com.livk.amqp.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * Message
 * </p>
 *
 * @author livk
 * @date 2022/4/14
 */
@Data
public class Message<T> implements Serializable {

    private String msgId;

    private LocalDateTime sendTime;

    private String msg;

    private T data;

}
