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
 */
@Data
public class Message<T> implements Serializable {

    private String msgId;

    private LocalDateTime sendTime;

    private String msg;

    private T data;

}
