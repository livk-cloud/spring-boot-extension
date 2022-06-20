package com.livk.batch.support;

import com.livk.batch.domain.User;
import lombok.Setter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import javax.annotation.Nonnull;
import java.util.Date;

/**
 * <p>
 * CsvItemProcessor
 * </p>
 *
 * @author livk
 * @date 2021/12/27
 */
@Setter
public class CsvItemProcessor implements ItemProcessor<User, User> {

	private Validator<? super User> validator;

	@Override
	public User process(@Nonnull User item) throws ValidationException {
		try {
			validator.validate(item);
			if (item.getSex().equals("男")) {
				item.setSex("1");
			} else {
				item.setSex("2");
			}
			item.setStatus(1);
			item.setCreateTime(new Date());
			return item;
		} catch (ValidationException e) {
			return null;
		}
	}

}
