package io.fdlessard.codebites.batch.modified;

import java.util.List;

import io.fdlessard.codebites.batch.modified.ModifiedCustomer;
import io.fdlessard.codebites.batch.modified.ModifiedCustomerRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModifiedCustomerItemWriter implements ItemWriter<ModifiedCustomer> {

  @Autowired
  private ModifiedCustomerRepository modifiedCustomerRepository;

  @Override
  public void write(List<? extends ModifiedCustomer> modifiedCustomers) throws Exception {
    modifiedCustomerRepository.saveAll(modifiedCustomers);
  }
}