package io.fdlessard.codebites.batch.jobs;

import io.fdlessard.codebites.batch.customer.Customer;
import io.fdlessard.codebites.batch.modified.ModifiedCustomer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class CustomerTransactionItemProcessor implements ItemProcessor<Customer, ModifiedCustomer> {

  @Override
  public ModifiedCustomer process(Customer customer) throws Exception {

    return ModifiedCustomer.builder()
        .fullName(customer.getFirstName() + " " + customer.getLastName())
        .company(customer.getCompany())
        .build();
  }

}
