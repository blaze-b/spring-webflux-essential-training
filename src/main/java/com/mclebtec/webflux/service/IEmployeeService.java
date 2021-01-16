package com.mclebtec.webflux.service;


import com.mclebtec.webflux.model.Employee;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;

public interface IEmployeeService {
    void create(Employee e) throws Exception;

    Mono<Employee> findById(Integer id);

    Flux<Employee> findByName(String name);

    Flux<Employee> findAll();

    Mono<Employee> update(Employee e);

    Mono<Void> delete(Integer id);

    void create(InputStream inputStream) throws Exception;

}