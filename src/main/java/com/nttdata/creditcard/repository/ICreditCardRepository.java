package com.nttdata.creditcard.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.nttdata.creditcard.entity.CreditCard;

import reactor.core.publisher.Flux;

public interface ICreditCardRepository extends ReactiveMongoRepository<CreditCard, String> {
  
  Flux<CreditCard> findByCustomerId(String id);

}
