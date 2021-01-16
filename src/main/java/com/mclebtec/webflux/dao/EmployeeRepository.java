package com.mclebtec.webflux.dao;

import com.mclebtec.webflux.model.Employee;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;


public interface EmployeeRepository extends ReactiveMongoRepository<Employee, Integer> {
    @Query("{ 'first_name': ?0 }")
    Flux<Employee> findByName(final String name);
}