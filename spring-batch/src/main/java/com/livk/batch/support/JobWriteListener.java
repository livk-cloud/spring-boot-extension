package com.livk.batch.support;

import com.livk.batch.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

/**
 * <p>
 * JobWriteListener
 * </p>
 *
 * @author livk
 * @date 2022/12/2
 */
@Slf4j
public class JobWriteListener implements ItemWriteListener<User> {

    @Override
    public void beforeWrite(Chunk<? extends User> items) {
        log.info("spring batch start write,data:{}", items);
    }

    @Override
    public void afterWrite(Chunk<? extends User> items) {
        log.info("spring batch is write,data:{}", items);
    }

    @Override
    public void onWriteError(Exception e, Chunk<? extends User> items) {
        log.error("spring batch write an error occurred ,message:{} data:{}", e.getMessage(), items, e);
    }
}
