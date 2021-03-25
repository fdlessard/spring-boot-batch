package io.fdlessard.codebites.batch.modified;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.fdlessard.codebites.batch.commons.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Table(name = "modified_customer", schema = "cust2")
@Entity
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifiedCustomer extends BaseEntity {

  @NotBlank(message = "fullName name cannot be blank")
  @Size(min = 2, message = "fullName must have more thant 2 characters")
  private String fullName;

  @NotBlank(message = "company name cannot be blank")
  @Size(min = 2, message = "company must have more thant 2 characters")
  private String company;

}