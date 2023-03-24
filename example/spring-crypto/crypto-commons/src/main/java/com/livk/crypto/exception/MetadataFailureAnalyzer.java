package com.livk.crypto.exception;

import com.livk.auto.service.annotation.SpringFactories;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalyzer;

/**
 * @author livk
 */
@SpringFactories(FailureAnalyzer.class)
public class MetadataFailureAnalyzer extends AbstractFailureAnalyzer<MetadataIllegalException> {
    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, MetadataIllegalException cause) {
        return new FailureAnalysis(cause.getMessage(), cause.getAction(), cause);
    }
}
