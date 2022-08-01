package com.livk.excel.batch.config;

import com.livk.excel.batch.entity.Info;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
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

    @Bean
    public ItemWriter<List<Info>> writer(SqlSessionFactory sqlSessionFactory) {
        var writer = new MyBatisBatchItemWriter<List<Info>>();
        writer.setStatementId("com.livk.excel.mapper.InfoMapper.saveBatch");
        writer.setSqlSessionFactory(sqlSessionFactory);
        return writer;
    }
}
