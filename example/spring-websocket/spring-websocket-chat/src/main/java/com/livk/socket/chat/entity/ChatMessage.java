package com.livk.socket.chat.entity;

import lombok.Data;

/**
 * @author livk
 */
@Data
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}
