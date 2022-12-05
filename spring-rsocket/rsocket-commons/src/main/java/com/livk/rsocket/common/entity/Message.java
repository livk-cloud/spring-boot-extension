package com.livk.rsocket.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * <p>
 * Message
 * </p>
 *
 * @author livk
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String from;

    private String to;

    private long index;

    private long created = Instant.now().getEpochSecond();

    public Message(String from, String to) {
        this(from, to, 0);
    }

    public Message(String from, String to, long index) {
        this.from = from;
        this.to = to;
        this.index = index;
    }

}
