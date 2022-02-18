package com.livk.common.redis.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * LivkMessage
 * </p>
 *
 * @author livk
 * @date 2021/11/26
 */
@Data
@Accessors(chain = true)
public class LivkMessage<T> {

    public static final String CHANNEL = "livk-topic";

    private static final long serialVersionUID = 8632296967087444509L;

    /**
     * 公告信息id
     */
    private Long id;

    /**
     * 公告内容
     */
    private String msg;

    private T data;

    public static <T> LivkMessage<T> of(){
        return new LivkMessage<>();
    }
}
