package com.livk.crypto.jackson;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.livk.crypto.CryptoType;
import com.livk.crypto.annotation.AnnoDecrypt;
import com.livk.crypto.parse.CryptoFormatter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.format.Parser;

import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

/**
 * @author livk
 */
@JsonComponent
@AllArgsConstructor
@NoArgsConstructor
public class CryptoJsonDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {

    private Parser<Object> parser;

    @Override
    public Object deserialize(JsonParser p, DeserializationContext context) throws IOException {
        String text = context.readTree(p).textValue();
        try {
            return parser.parse(text, Locale.CHINA);
        } catch (ParseException e) {
            throw new JsonParseException(p, e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext context, BeanProperty property) throws JsonMappingException {
        JavaType javaType = property.getType();
        AnnoDecrypt decrypt = Optional.ofNullable(property.getAnnotation((AnnoDecrypt.class)))
                .orElse(property.getContextAnnotation(AnnoDecrypt.class));
        Parser<?> parser = getParser(javaType.getRawClass(), decrypt.value());
        if (parser != null) {
            return new CryptoJsonDeserializer((Parser<Object>) parser);
        }
        return context.findContextualValueDeserializer(javaType, property);
    }

    private Parser<?> getParser(Class<?> rawClass, CryptoType type) {
        for (CryptoFormatter<?> formatter : CryptoFormatter.fromContext().get(rawClass)) {
            if (formatter.type().equals(type)) {
                return formatter;
            }
        }
        return null;
    }
}
