package com.livk.crypto.parse;

import com.livk.crypto.CryptoType;
import com.livk.crypto.annotation.AnnoDecrypt;
import com.livk.crypto.exception.FormatterNotFountException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author livk
 */
public class SpringAnnotationFormatterFactory implements AnnotationFormatterFactory<AnnoDecrypt> {

    private final Map<Class<?>, List<CryptoFormatter<?>>> map;

    public SpringAnnotationFormatterFactory(ObjectProvider<CryptoFormatter<?>> cryptoFormatters) {
        map = cryptoFormatters.orderedStream()
                .collect(Collectors.groupingBy(CryptoFormatter::supportClass));
    }

    @Override
    public Set<Class<?>> getFieldTypes() {
        return map.keySet();
    }

    @Override
    public Printer<?> getPrinter(AnnoDecrypt annotation, Class<?> fieldType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Parser<?> getParser(AnnoDecrypt annotation, Class<?> fieldType) {
        return (text, locale) -> {
            CryptoType type = CryptoType.match(text);
            for (CryptoFormatter<?> parser : map.get(fieldType)) {
                if (type.equals(parser.type())) {
                    return parser.parse(type.unwrap(text), locale);
                }
            }
            throw new FormatterNotFountException("fieldType:" + fieldType + " Parser NotFount!");
        };
    }
}
