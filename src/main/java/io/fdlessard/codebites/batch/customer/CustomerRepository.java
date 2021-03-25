package io.fdlessard.codebites.batch.customer;

import io.fdlessard.codebites.batch.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
