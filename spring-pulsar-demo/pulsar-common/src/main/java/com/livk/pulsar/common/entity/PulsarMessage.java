package com.livk.pulsar.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * PulsarMessage
 * </p>
 *
 * @author livk
 * @date 2022/4/27
 */
@Data
public class PulsarMessage<T> implements Serializable {
    private String msgId;

    private LocalDateTime sendTime;

    private String msg;

    private T data;
}
