package com.nttdata.creditcard.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.creditcard.entity.CreditCard;
import com.nttdata.creditcard.service.ICreditCardService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/creditcard")
public class CreditCardController {

  @Autowired
  ICreditCardService service;

  @GetMapping("list")
  public Flux<CreditCard> findAll() {
    return service.findAll();
  }

  @GetMapping("/find/{id}")
  public Mono<CreditCard> findById(@PathVariable String id) {
    return service.findById(id);
  }

  @GetMapping("/findCreditCards/{id}")
  public Flux<CreditCard> findCreditCardByCustomer(@PathVariable String id) {
    return service.findCreditCardByCustomer(id);
  }

  @PostMapping("/create")
  public Mono<ResponseEntity<CreditCard>> create(
      @RequestBody CreditCard creditCard) {
    return service
        .findCustomerByNumber(creditCard.getCustomer().getDocumentNumber())
        .flatMap(c -> {
          creditCard.setCustomer(c);
          creditCard.setCreateAt(LocalDateTime.now());
          return service.create(creditCard);
        })
        .map(savedCustomer -> new ResponseEntity<>(savedCustomer,
            HttpStatus.CREATED))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PutMapping("/update")
  public Mono<ResponseEntity<CreditCard>> update(
      @RequestBody CreditCard creditCard) {
    log.info("buscando tarjeta de credito");
    return service.findById(creditCard.getId())
        .flatMap(
            cc -> service.findCustomerById(creditCard.getCustomer().getId())
                .flatMap(customer -> {
                  creditCard.setCustomer(customer);
                  log.info("tarjeta de credito actualizada");
                  return service.update(creditCard);
                }))
        .map(cc -> new ResponseEntity<>(cc, HttpStatus.CREATED))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/delete/{id}")
  public Mono<ResponseEntity<String>> delete(@PathVariable String id) {
    return service.delete(id).filter(deleteCustomer -> deleteCustomer)
        .map(deleteCustomer -> new ResponseEntity<>("CreditCard Deleted",
            HttpStatus.ACCEPTED))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

}
