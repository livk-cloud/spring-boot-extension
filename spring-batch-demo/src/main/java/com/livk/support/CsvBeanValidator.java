package com.livk.support;


import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import javax.validation.Validation;

/**
 * <p>
 * CsvBeanValidator
 * </p>
 *
 * @author livk
 * @date 2021/12/27
 */
public class CsvBeanValidator<T> implements Validator<T> {

    public CsvBeanValidator() {
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.usingContext().getValidator();
    }

    private final javax.validation.Validator validator;

    @Override
    public void validate(T value) throws ValidationException {
        var constraintViolations = validator.validate(value);
        if (!constraintViolations.isEmpty()) {
            var message = new StringBuilder();
            constraintViolations.forEach(constraintViolation ->
                    message.append(constraintViolation.getMessage()).append("\n"));
            throw new ValidationException(message.toString());
        }
    }
}
