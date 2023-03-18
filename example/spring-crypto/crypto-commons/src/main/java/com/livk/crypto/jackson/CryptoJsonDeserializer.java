package com.livk.crypto.jackson;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.livk.crypto.CryptoType;
import com.livk.crypto.parse.CryptoFormatter;
import lombok.NoArgsConstructor;
import org.springframework.format.Parser;

import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;

/**
 * @author livk
 */
@NoArgsConstructor
public class CryptoJsonDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {

    private JavaType javaType;

    private JsonDeserializer<Object> defaultJsonDeserializer;

    @Override
    public Object deserialize(JsonParser p, DeserializationContext context) throws IOException {
        String text = context.readTree(p).textValue();
        CryptoType type = CryptoType.match(text);
        Parser<?> parser = getParser(type);
        if (parser == null) {
            return defaultJsonDeserializer.deserialize(p, context);
        }
        try {
            return parser.parse(type.unwrap(text), Locale.CHINA);
        } catch (ParseException e) {
            throw new JsonParseException(p, e.getMessage());
        }
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext context, BeanProperty property) throws JsonMappingException {
        javaType = property.getType();
        defaultJsonDeserializer = context.findContextualValueDeserializer(javaType, property);
        return this;
    }

    private Parser<?> getParser(CryptoType type) {
        for (CryptoFormatter<?> formatter : CryptoFormatter.fromContext().get(javaType.getRawClass())) {
            if (formatter.type().equals(type)) {
                return formatter;
            }
        }
        return null;
    }
}
