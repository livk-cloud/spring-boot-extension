package com.livk.commons.jackson.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.livk.commons.jackson.support.annotation.NumberJsonFormat;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Optional;

/**
 * @author livk
 */
@JsonComponent
@AllArgsConstructor
@NoArgsConstructor
public class NumberJsonSerializer extends JsonSerializer<Number> implements ContextualSerializer {

    private String format;

    @Override
    public void serialize(Number value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(new DecimalFormat(format).format(value));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        JavaType javaType = property.getType();
        NumberJsonFormat jsonFormat = Optional.ofNullable(property.getAnnotation((NumberJsonFormat.class)))
                .orElse(property.getContextAnnotation(NumberJsonFormat.class));
        if (Number.class.isAssignableFrom(javaType.getRawClass()) || this.simpleTypeSupport(jsonFormat.simpleTypeSupport(), javaType)) {
            return new NumberJsonSerializer(jsonFormat.pattern());
        }
        return prov.findValueSerializer(property.getType(), property);
    }

    private boolean simpleTypeSupport(boolean support, JavaType javaType) {
        return support && javaType instanceof SimpleType;
    }
}
