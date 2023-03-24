package com.livk.commons.jackson.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.livk.commons.jackson.annotation.NumberJsonFormat;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

/**
 * The type Number json serializer.
 *
 * @author livk
 */
@AllArgsConstructor
@NoArgsConstructor
public class NumberJsonSerializer extends JsonSerializer<Number> implements ContextualSerializer {

    private static final List<Class<?>> SUPPORT_PRIMITIVE_CLASS = List.of(
            byte.class, short.class, int.class, long.class,
            float.class, double.class);

    private String format;

    @Override
    public void serialize(Number value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(new DecimalFormat(format).format(value));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        Class<?> rawClass = property.getType().getRawClass();
        NumberJsonFormat jsonFormat = Optional.ofNullable(property.getAnnotation((NumberJsonFormat.class)))
                .orElse(property.getContextAnnotation(NumberJsonFormat.class));
        if (Number.class.isAssignableFrom(rawClass) || this.simpleTypeSupport(jsonFormat.simpleTypeSupport(), rawClass)) {
            return new NumberJsonSerializer(jsonFormat.pattern());
        }
        return prov.findValueSerializer(property.getType(), property);
    }

    private boolean simpleTypeSupport(boolean support, Class<?> rawClass) {
        return support && rawClass.isPrimitive() && SUPPORT_PRIMITIVE_CLASS.contains(rawClass);
    }
}
