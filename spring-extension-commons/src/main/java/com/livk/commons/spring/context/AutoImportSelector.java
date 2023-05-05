/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.commons.spring.context;

import com.livk.commons.bean.util.ClassUtils;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * The type Auto import selector.
 *
 * @author livk
 */
class AutoImportSelector extends AbstractImportSelector<AutoImport> {

    @NonNull
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Set<String> names = new HashSet<>();
        if (importingClassMetadata == null || annotationClass == null) {
            return new String[0];
        }
        for (String annotationType : importingClassMetadata.getAnnotationTypes()) {
            try {
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
