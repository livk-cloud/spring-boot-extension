package com.livk.batch.support;

import com.livk.batch.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.lang.NonNull;

import java.util.Date;

/**
 * <p>
 * CsvItemProcessor
 * </p>
 *
 * @author livk
 * @date 2021/12/27
 */
@RequiredArgsConstructor
public class CsvItemProcessor implements ItemProcessor<User, User> {

    private final Validator<? super User> validator;

    @Override
    public User process(@NonNull User item) throws ValidationException {
        try {
            validator.validate(item);
            if (item.getSex().equals("男")) {
                item.setSex("1");
            } else {
                item.setSex("2");
            }
            item.setStatus(1);
            item.setCreateTime(new Date());
            item.setUpdateTime(new Date());
            return item;
        } catch (ValidationException e) {
            return null;
        }
    }

}
