package com.livk.commons.spring.context;

import com.livk.commons.bean.util.ClassUtils;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * The type Auto import selector.
 *
 * @author livk
 */
class AutoImportSelector extends AbstractImportSelector<AutoImport> {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Set<String> names = new HashSet<>();
        if (importingClassMetadata == null || annotationClass == null) {
            return new String[0];
        }
        for (String annotationType : importingClassMetadata.getAnnotationTypes()) {
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                Class<?> type = ClassUtils.forName(annotationType, classLoader);
                if (type.isAnnotation() && type.isAnnotationPresent(annotationClass)) {
                    names.addAll(ImportCandidates.load(type, classLoader).getCandidates());
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return StringUtils.toStringArray(names);
    }
}
