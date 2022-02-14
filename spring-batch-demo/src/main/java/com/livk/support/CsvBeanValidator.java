package com.livk.support;

import jakarta.validation.Validation;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

/**
 * <p>
 * CsvBeanValidator
 * </p>
 *
 * @author livk
 * @date 2021/12/27
 */
public class CsvBeanValidator<T> implements Validator<T> {

    public CsvBeanValidator(){
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.usingContext().getValidator();
    }

    private final jakarta.validation.Validator validator;

    @Override
    public void validate(T value) throws ValidationException {
        var constraintViolations = validator.validate(value);
        if (constraintViolations.size() > 0) {
            var message = new StringBuilder();
            constraintViolations.forEach(constraintViolation ->
                    message.append(constraintViolation.getMessage()).append("\n"));
            throw new ValidationException(message.toString());
        }
    }
}
