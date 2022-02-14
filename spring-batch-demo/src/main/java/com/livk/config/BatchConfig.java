package com.livk.config;

import com.livk.domain.User;
import com.livk.support.CsvBeanValidator;
import com.livk.support.CsvItemProcessor;
import com.livk.support.JobCompletionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.validator.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.FileNotFoundException;

/**
 * <p>
 * BatchConfig
 * </p>
 *
 * @author livk
 * @date 2021/12/27
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public ItemReader<User> reader() {
        var reader = new FlatFileItemReader<User>();
        reader.setLinesToSkip(1);
        reader.setResource(new ClassPathResource("data.csv"));
        reader.setLineMapper(new DefaultLineMapper<>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                // 配置了四行文件
                setNames("userName", "sex", "age", "address");
                // 配置列于列之间的间隔符,会通过间隔符对每一行进行切分
                setDelimiter("\t");
            }});

            // 设置要映射的实体类属性
            setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                setTargetType(User.class);
            }});
        }});
        return reader;
    }

    @Bean
    public ItemProcessor<User, User> processor() {
        var processor = new CsvItemProcessor();
        processor.setValidator(csvBeanValidator());
        return processor;
    }

    @Bean
    public ItemWriter<User> writer(DataSource dataSource) {
        var writer = new JdbcBatchItemWriter<User>();
        writer.setDataSource(dataSource);
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("insert into sys_user(user_name, sex, age, address, status, create_time) values (:userName, :sex, :age, :address, :status, :createTime)");
        return writer;
    }

    @Bean
    public Step csvStep(StepBuilderFactory stepBuilderFactory,
                        DataSource dataSource) {
        return stepBuilderFactory.get("csvStep")
                .<User, User>chunk(5)
                .reader(reader())
                .processor(processor())
                .writer(writer(dataSource))
                .faultTolerant()
                // 设定一个我们允许的这个step可以跳过的异常数量，假如我们设定为3，则当这个step运行时，只要出现的异常数目不超过3，整个step都不会fail。注意，若不设定skipLimit，则其默认值是0
                .skipLimit(3)
                // 指定我们可以跳过的异常，因为有些异常的出现，我们是可以忽略的
                .skip(Exception.class)
                // 出现这个异常我们不想跳过，因此这种异常出现一次时，计数器就会加一，直到达到上限
                .noSkip(FileNotFoundException.class)
                .build();
    }

    @Bean
    public Job csvJob(JobBuilderFactory jobBuilderFactory,
                      Step step,
                      JobCompletionListener listener) {
        return jobBuilderFactory.get("csvJob")
                .start(step)
                .listener(listener)
                .build();
    }

    @Bean
    public Validator<User> csvBeanValidator() {
        return new CsvBeanValidator<>();
    }
}
