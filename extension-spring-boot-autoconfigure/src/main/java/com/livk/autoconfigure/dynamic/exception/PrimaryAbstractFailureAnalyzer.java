package com.livk.autoconfigure.dynamic.exception;

import com.livk.auto.service.annotation.SpringFactories;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalyzer;

/**
 * @author livk
 */
@SpringFactories(FailureAnalyzer.class)
public class PrimaryAbstractFailureAnalyzer extends AbstractFailureAnalyzer<PrimaryNotFountException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, PrimaryNotFountException cause) {

        return new FailureAnalysis(cause.getMessage(), "请添加 spring.dynamic.primary", cause);
    }

}
