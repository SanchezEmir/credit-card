package com.nttdata.creditcard.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nttdata.creditcard.entity.CreditCard;
import com.nttdata.creditcard.entity.dto.Customer;
import com.nttdata.creditcard.repository.ICreditCardRepository;
import com.nttdata.creditcard.service.ICreditCardService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CreditCardServiceImpl implements ICreditCardService {

  private final WebClient webClient;
  private final ReactiveCircuitBreaker reactiveCircuitBreaker;

  @Value("${config.base.gatewey}")
  private String url;

  public CreditCardServiceImpl(
      ReactiveResilience4JCircuitBreakerFactory circuitBreakerFactory) {
    
    this.webClient = WebClient.builder()
        .baseUrl(this.url).build();
    
    this.reactiveCircuitBreaker = circuitBreakerFactory.create("customer");
  }

  @Autowired
  ICreditCardRepository repo;

  @Override
  public Mono<CreditCard> findById(String id) {
    return repo.findById(id);
  }

  @Override
  public Flux<CreditCard> findAll() {
    return repo.findAll();
  }

  @Override
  public Mono<CreditCard> create(CreditCard t) {
    return repo.save(t);
  }

  @Override
  public Mono<CreditCard> update(CreditCard t) {
    return repo.save(t);
  }

  @Override
  public Mono<Boolean> delete(String t) {
    return repo.findById(t)
        .flatMap(credit -> repo.delete(credit).then(Mono.just(Boolean.TRUE)))
        .defaultIfEmpty(Boolean.FALSE);
  }

  @Override
  public Mono<Customer> findCustomerById(String id) {
    log.info("buscando Customer por Id");
    return reactiveCircuitBreaker.run(
        webClient.get().uri(this.url + "/customer/find/{id}", id).accept(MediaType.APPLICATION_JSON)
            .retrieve().bodyToMono(Customer.class),
        throwable -> {
          return this.getDefaultCustomer();
        });
  }

  @Override
  public Mono<Customer> findCustomerByNumber(String number) {
    log.info("buscando Customer por documentNumber");
    return reactiveCircuitBreaker.run(webClient.get()
        .uri(this.url + "/customer/documentNumber/{number}", number).accept(MediaType.APPLICATION_JSON).retrieve()
        .bodyToMono(Customer.class), throwable -> {
          return this.getDefaultCustomer();
        });
  }

  @Override
  public Flux<CreditCard> findCreditCardByCustomer(String id) {
    return repo.findByCustomerId(id);
  }

  public Mono<Customer> getDefaultCustomer() {
    log.info("no encontro customer");
    Mono<Customer> customer = Mono.just(new Customer());
    return customer;
  }

}
