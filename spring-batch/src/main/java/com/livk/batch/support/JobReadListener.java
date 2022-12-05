package com.livk.batch.support;

import com.livk.batch.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.lang.Nullable;

/**
 * <p>
 * JobReadListener
 * </p>
 *
 * @author livk
 */
@Slf4j
public class JobReadListener implements ItemReadListener<User> {

    @Override
    public void beforeRead() {
        log.info("spring batch start read");
    }

    @Override
    public void afterRead(@Nullable User item) {
        log.info("spring batch is read, data:{}", item);
    }

    @Override
    public void onReadError(Exception ex) {
        log.error("spring batch read an error occurred ,message:{}", ex.getMessage(), ex);
    }
}
