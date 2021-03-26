package io.fdlessard.codebites.batch.jobs;

import io.fdlessard.codebites.batch.customer.Customer;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class CustomerRowMapper implements RowMapper<Customer> {

  public static final String ID_COLUMN = "id";
  public static final String ID_VERSION = "version";
  public static final String FIRST_NAME_COLUMN = "firstname";
  public static final String LAST_NAME_COLUMN = "lastname";
  public static final String COMPANY_COLUMN = "company";

  public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {

    return Customer.builder()
        .id(rs.getLong(ID_COLUMN))
        .version(rs.getInt(ID_VERSION))
        .firstName(rs.getString(FIRST_NAME_COLUMN))
        .lastName(rs.getString(LAST_NAME_COLUMN))
        .company(rs.getString(COMPANY_COLUMN))
        .build();
  }
}
