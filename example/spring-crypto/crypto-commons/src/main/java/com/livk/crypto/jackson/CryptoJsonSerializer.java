package com.livk.crypto.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.livk.crypto.CryptoType;
import com.livk.crypto.annotation.CryptoEncrypt;
import com.livk.crypto.parse.CryptoFormatter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.format.Printer;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

/**
 * @author livk
 */
@NoArgsConstructor
@AllArgsConstructor
public class CryptoJsonSerializer extends JsonSerializer<Object> implements ContextualSerializer {

    private CryptoFormatter<Object> formatter;

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String text = formatter.print(value, Locale.CHINA);
        CryptoType type = formatter.type();
        gen.writeString(type.wrapper(text));
    }

    @SuppressWarnings("unchecked")
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        JavaType javaType = property.getType();
        CryptoEncrypt encrypt = Optional.ofNullable(property.getAnnotation((CryptoEncrypt.class)))
                .orElse(property.getContextAnnotation(CryptoEncrypt.class));
        Printer<?> printer = getPrinter(javaType.getRawClass(), encrypt.value());
        if (printer != null) {
            return new CryptoJsonSerializer((CryptoFormatter<Object>) printer);
        }
        return prov.findValueSerializer(javaType, property);
    }

    private CryptoFormatter<?> getPrinter(Class<?> rawClass, CryptoType type) {
        for (CryptoFormatter<?> formatter : CryptoFormatter.fromContext().get(rawClass)) {
            if (formatter.type().equals(type)) {
                return formatter;
            }
        }
        return null;
    }
}
