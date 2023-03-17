package com.livk.crypto.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.livk.crypto.CryptoType;
import com.livk.crypto.annotation.AnnoEncrypt;
import com.livk.crypto.parse.CryptoFormatter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.format.Printer;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

/**
 * @author livk
 */
@JsonComponent
@AllArgsConstructor
@NoArgsConstructor
public class CryptoJsonSerializer extends JsonSerializer<Object> implements ContextualSerializer {

    private Printer<Object> printer;

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String text = printer.print(value, Locale.CHINA);
        gen.writeString(text);
    }

    @SuppressWarnings("unchecked")
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        JavaType javaType = property.getType();
        AnnoEncrypt encrypt = Optional.ofNullable(property.getAnnotation((AnnoEncrypt.class)))
                .orElse(property.getContextAnnotation(AnnoEncrypt.class));
        Printer<?> printer = getPrinter(javaType.getRawClass(), encrypt.value());
        if (printer != null) {
            return new CryptoJsonSerializer((Printer<Object>) printer);
        }
        return prov.findValueSerializer(property.getType(), property);
    }

    private Printer<?> getPrinter(Class<?> rawClass, CryptoType type) {
        for (CryptoFormatter<?> formatter : CryptoFormatter.fromContext().get(rawClass)) {
            if (formatter.type().equals(type)) {
                return formatter;
            }
        }
        return null;
    }
}
