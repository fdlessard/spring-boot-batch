package io.fdlessard.codebites.batch.jobs;


import io.fdlessard.codebites.batch.customer.Customer;
import io.fdlessard.codebites.batch.modified.ModifiedCustomer;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class JobsConfiguration {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Autowired
  private ItemProcessor<Customer, ModifiedCustomer> customerItemProcessor;

  @Bean
  public Job customerJob(
      JdbcCursorItemReader<Customer> customerItemReader,
      JpaItemWriter<ModifiedCustomer> modifiedCustomerItemWriter,
      @Qualifier("chainedTransactionManager") PlatformTransactionManager chainedTransactionManager
  ) {

    Step step1 = stepBuilderFactory.get("step-1")
        .transactionManager(chainedTransactionManager)
        .<Customer, ModifiedCustomer>chunk(10)
        .reader(customerItemReader)
        .processor(customerItemProcessor)
        .writer(modifiedCustomerItemWriter)
        .build();

    return jobBuilderFactory.get("customer-data-loader-job")
        .start(step1)
        .build();
  }

/*
  @Bean
  public FlatFileItemReader<Customer> flatFileItemReader(
      @Value("${inputFile}") Resource inputFile) {

    FlatFileItemReader<Customer> customerFlatFileItemReader = new FlatFileItemReader<>();
    customerFlatFileItemReader.setName("CSV-READER");
    customerFlatFileItemReader.setLinesToSkip(1);
    customerFlatFileItemReader.setResource(inputFile);
    customerFlatFileItemReader.setLineMapper(lineMapper());

    return customerFlatFileItemReader;
  }

  @Bean
  public LineMapper<Customer> lineMapper() {

    DefaultLineMapper<Customer> customerLineMapper = new DefaultLineMapper<>();
    DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
    delimitedLineTokenizer.setDelimiter(",");
    delimitedLineTokenizer.setStrict(false);
    delimitedLineTokenizer.setNames("id", "version", "company", "first_name", "last_name");

    customerLineMapper.setLineTokenizer(delimitedLineTokenizer);
    BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
    fieldSetMapper.setTargetType(Customer.class);
    customerLineMapper.setFieldSetMapper(fieldSetMapper);

    return customerLineMapper;
  }*/

  @Bean
  public JdbcCursorItemReader<Customer> customerItemReader(
      @Qualifier("customerDataSource") DataSource customerDataSource
  ) {

    return new JdbcCursorItemReaderBuilder<Customer>()
        .dataSource(customerDataSource)
        .name("customerReader")
        .sql("select * from cust1.customer")
        .rowMapper(new CustomerRowMapper())
        .build();

  }

  @Bean
  public JpaItemWriter<ModifiedCustomer> modifiedCustomerItemWriter(
      @Qualifier("modifiedCustomerEntityManagerFactory") EntityManagerFactory modifiedCustomerEntityManagerFactory
  ) {

    JpaItemWriter<ModifiedCustomer> modifiedCustomerItemWriter = new JpaItemWriter<>();
    modifiedCustomerItemWriter.setEntityManagerFactory(modifiedCustomerEntityManagerFactory);

    return modifiedCustomerItemWriter;
  }

  @Bean(name = "chainedTransactionManager")
  PlatformTransactionManager chainTransactionManager(
          @Qualifier("customerTransactionManager") PlatformTransactionManager customerTransactionManager,
          @Qualifier("modifiedCustomerTransactionManager") PlatformTransactionManager modifiedCustomerTransactionManager
  ) {
    ChainedTransactionManager chainedTransactionManager =
            new ChainedTransactionManager(
                    customerTransactionManager, modifiedCustomerTransactionManager
            );

    return chainedTransactionManager;
  }

}
