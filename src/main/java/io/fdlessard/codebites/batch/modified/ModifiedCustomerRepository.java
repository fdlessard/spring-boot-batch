package io.fdlessard.codebites.batch.modified;

import io.fdlessard.codebites.batch.modified.ModifiedCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModifiedCustomerRepository extends JpaRepository<ModifiedCustomer, Long> {

}
