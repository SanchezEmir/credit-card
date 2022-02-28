package com.nttdata.creditcard.service;

import com.nttdata.creditcard.entity.CreditCard;
import com.nttdata.creditcard.entity.dto.Customer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICreditCardService {

  Mono<CreditCard> findById(String id);

  Flux<CreditCard> findAll();

  Mono<CreditCard> create(CreditCard t);

  Mono<CreditCard> update(CreditCard t);

  Mono<Boolean> delete(String t);

  Mono<Customer> findCustomerById(String id);

  Mono<Customer> findCustomerByNumber(String number);

  Flux<CreditCard> findCreditCardByCustomer(String id);

}
