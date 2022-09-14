package com.livk.excel.batch.config;

import com.livk.excel.batch.entity.Info;
import com.livk.excel.batch.support.MyBatisBatchItemWriter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * <p>
 * BatchConfig
 * </p>
 *
 * @author livk
 * @date 2022/7/19
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {

//    @Bean
//    public ItemWriter<List<Info>> writer(SqlSessionFactory sqlSessionFactory) {
//        MyBatisBatchItemWriter<List<Info>> writer = new MyBatisBatchItemWriter<>();
//        writer.setStatementId("com.livk.excel.mapper.InfoMapper.saveBatch");
//        writer.setSqlSessionFactory(sqlSessionFactory);
//        return writer;
//    }

    @Bean
    public ItemWriter<List<Info>> writer(SqlSessionFactory sqlSessionFactory) {
        return new MyBatisBatchItemWriter<>(sqlSessionFactory, "com.livk.excel.batch.mapper.InfoMapper.saveBatch");
    }
}
