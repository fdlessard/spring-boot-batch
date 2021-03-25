package io.fdlessard.codebites.batch.configurations;


import io.fdlessard.codebites.batch.customer.Customer;
import io.fdlessard.codebites.batch.modified.ModifiedCustomer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfiguration {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Autowired
  private ItemReader<Customer> customerItemReader;

  @Autowired
  private ItemWriter<ModifiedCustomer> modifiedCustomerItemWriter;

  @Autowired
  private ItemProcessor<Customer, ModifiedCustomer> customerItemProcessor;


  @Bean
  public Job customerJob() {

    Step step1 = stepBuilderFactory.get("step-1")
        .<Customer, ModifiedCustomer>chunk(10)
        .reader(customerItemReader)
        .processor(customerItemProcessor)
        .writer(modifiedCustomerItemWriter)
        .build();

    return jobBuilderFactory.get("customer-data-loader-job")
        .start(step1)
        .build();
  }

  @Bean
  public FlatFileItemReader<Customer> flatFileItemReader(@Value("${inputFile}") Resource inputFile) {

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
  }

}
