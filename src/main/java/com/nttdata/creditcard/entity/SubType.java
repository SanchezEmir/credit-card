package com.nttdata.creditcard.entity;

import com.nttdata.creditcard.entity.enums.ESubType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubType {
  
  private String id;
  
  private ESubType value;

}
