package com.livk.proto.amqp.converter;

import com.livk.proto.User;
import com.livk.proto.UserConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.lang.NonNull;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class UserAmqpProtobufMessageConverter extends AbstractMessageConverter {

    private final MessageConverter defaultMessageConverter;

    private final UserConverter userConverter = UserConverter.INSTANCE;

    @NonNull
    @Override
    protected Message createMessage(@NonNull Object object, @NonNull MessageProperties messageProperties) {
        if (object instanceof User user) {
            byte[] byteArray = userConverter.convert(user);
            return new Message(byteArray, messageProperties);
        }
        return defaultMessageConverter.toMessage(object, messageProperties);
    }

    @NonNull
    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        return userConverter.convert(message.getBody());
    }
}
