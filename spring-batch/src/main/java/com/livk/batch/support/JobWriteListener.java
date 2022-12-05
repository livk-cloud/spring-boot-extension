package com.livk.batch.support;

import com.livk.batch.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.lang.Nullable;

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
    public void beforeWrite(@Nullable Chunk<? extends User> items) {
        log.info("spring batch start write,data:{}", items);
    }

    @Override
    public void afterWrite(@Nullable Chunk<? extends User> items) {
        log.info("spring batch is write,data:{}", items);
    }

    @Override
    public void onWriteError(@Nullable Exception e, @Nullable Chunk<? extends User> items) {
        log.error("spring batch write an error occurred ,message:{} data:{}", e.getMessage(), items, e);
    }
}
