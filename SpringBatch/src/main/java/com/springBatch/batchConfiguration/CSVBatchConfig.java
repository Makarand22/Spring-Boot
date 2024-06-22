package com.springBatch.batchConfiguration;

import com.springBatch.entity.Customer;
import com.springBatch.processor.CustomerProcessor;
import com.springBatch.repository.CustomerRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class CSVBatchConfig {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

//    @Autowired
//    private StepBuilderFactory stepBuilderFactory;
//
//    @Autowired
//    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public FlatFileItemReader<Customer> customerReader() {
        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
        itemReader.setName("csv-reader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<Customer> lineMapper() {
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");

        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    public CustomerProcessor createProcessor() {
        return new CustomerProcessor();
    }

    @Bean
    public RepositoryItemWriter<Customer> customerWriter() {
        RepositoryItemWriter<Customer> repositoryItemWriter = new RepositoryItemWriter<>();
        repositoryItemWriter.setRepository(customerRepository);
        repositoryItemWriter.setMethodName("save");
        return repositoryItemWriter;
    }

    // deprecated
//    @Bean
//    public Step step() {
//        return stepBuilderFactory.get("step-1").<Customer, Customer>chunk(10)
//                .reader(customerReader())
//                .processor(createProcessor())
//                .writer(customerWriter())
//                .build();
//    }


    @Bean
    public Step step1() {
        return new StepBuilder("csv-step", jobRepository)
                .<Customer, Customer>chunk(10, transactionManager)
                .reader(customerReader())
                .processor(createProcessor())
                .writer(customerWriter())
                .build();
    }

    @Bean
    public Job job() {
        return new JobBuilder("customer-job", jobRepository)
                .start(step1())
                .build();
    }
// Deprecated
//    @Bean
//    public Job job() {
//        return jobBuilderFactory.get("customer-job")
//                .incrementer(new RunIdIncrementer())
//                .flow(step())
//                .end()
//                .build();
//    }
}
