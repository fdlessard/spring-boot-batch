package io.fdlessard.codebites.batch.customer;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.fdlessard.codebites.batch.commons.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Table(name = "customer", schema = "cust1")
@Entity
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends BaseEntity {

  @NotBlank(message = "lastName name cannot be blank")
  @Size(min = 2, message = "lastName must have more thant 2 characters")
  private String lastName;

  @NotBlank(message = "firstName name cannot be blank")
  @Size(min = 2, message = "firstName must have more thant 2 characters")
  private String firstName;

  @NotBlank(message = "company name cannot be blank")
  @Size(min = 2, message = "company must have more thant 2 characters")
  private String company;

}