package com.livk.support;

import com.livk.domain.User;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

import java.util.Date;

/**
 * <p>
 * CsvItemProcessor
 * </p>
 *
 * @author livk
 * @date 2021/12/27
 */
public class CsvItemProcessor extends ValidatingItemProcessor<User> {
    @Override
    public User process(User item) throws ValidationException {
        super.process(item);
        if (item.getSex().equals("男")) {
            item.setSex("1");
        } else {
            item.setSex("2");
        }
        item.setStatus(1);
        item.setCreateTime(new Date());
        return item;
    }
}
