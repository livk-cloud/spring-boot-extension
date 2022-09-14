package com.livk.batch.support;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.lang.NonNull;

import java.util.Set;


/**
 * <p>
 * CsvBeanValidator
 * </p>
 *
 * @author livk
 * @date 2021/12/27
 */
public class CsvBeanValidator<T> implements org.springframework.batch.item.validator.Validator<T> {

    private final Validator validator;

    public CsvBeanValidator() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            this.validator = validatorFactory.usingContext().getValidator();
        }
    }

    @Override
    public void validate(@NonNull T value) throws ValidationException {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(value);
        if (!constraintViolations.isEmpty()) {
            StringBuilder message = new StringBuilder();
            constraintViolations
                    .forEach(constraintViolation -> message.append(constraintViolation.getMessage()).append("\n"));
            throw new ValidationException(message.toString());
        }
    }

}
