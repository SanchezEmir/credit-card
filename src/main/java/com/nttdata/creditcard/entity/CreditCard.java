package com.nttdata.creditcard.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.nttdata.creditcard.entity.dto.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Document(collection =  "CreditCard")
@AllArgsConstructor
@NoArgsConstructor
public class CreditCard {
  
  @Id
  private String id;
  
  private String cardNumber;
  
  private Customer customer;
  
  private Double limitCredit;
  
  private LocalDate expiration;
  
  private LocalDateTime createAt;

}
