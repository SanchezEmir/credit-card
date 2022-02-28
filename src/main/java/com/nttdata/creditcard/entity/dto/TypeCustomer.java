package com.nttdata.creditcard.entity.dto;

import com.nttdata.creditcard.entity.SubType;
import com.nttdata.creditcard.entity.enums.ETypeCustomer;

import lombok.Data;

@Data
public class TypeCustomer {
  
  private String id;
  
  private ETypeCustomer value;
  
  private SubType subType;

}
