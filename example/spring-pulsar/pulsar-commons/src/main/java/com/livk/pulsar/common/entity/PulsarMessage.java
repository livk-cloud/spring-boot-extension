package com.livk.pulsar.common.entity;

import com.livk.commons.jackson.JacksonUtils;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * PulsarMessage
 * </p>
 *
 * @author livk
 */
@Data
public class PulsarMessage<T> implements Serializable {

    private String msgId;

    private LocalDateTime sendTime;

    private T data;

    public String toJson() {
        return JacksonUtils.writeValueAsString(this);
    }
}
