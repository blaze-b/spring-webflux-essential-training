package com.mclebtec.webflux.service;

import com.mclebtec.webflux.dao.EmployeeRepository;
import com.mclebtec.webflux.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class EmployeeService implements IEmployeeService {

    @Autowired
    EmployeeRepository employeeRepo;

    public void create(Employee e) throws Exception {
        employeeRepo.save(e).subscribe();
    }

    @Override
    public void create(InputStream inputStream) throws Exception {
        List<Employee> employees = parseCSVFile(inputStream);
        employeeRepo.saveAll(employees).subscribe();
    }

    @Override
    public Mono<Employee> findById(Integer id) {
        return employeeRepo.findById(id);
    }

    @Override
    public Flux<Employee> findByName(String name) {
        return employeeRepo.findByName(name);
    }

    @Override
    public Flux<Employee> findAll() {
        return employeeRepo.findAll();
    }

    @Override
    public Mono<Employee> update(Employee e) {
        return employeeRepo.save(e);
    }

    @Override
    public Mono<Void> delete(Integer id) {
        return employeeRepo.deleteById(id);
    }

    private List<Employee> parseCSVFile(InputStream inputStream) throws Exception {
        final List<Employee> cars = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split(";");
                    final Employee employee = new Employee();
                    employee.setId(Integer.parseInt(data[0]));
                    employee.setFirstName(data[1]);
                    employee.setLastName(data[2]);
                    employee.setEmail(data[3]);
                    cars.add(employee);
                }
                return cars;
            }
        } catch (final IOException e) {
            log.error("Failed to parse CSV file = {}", e.getMessage());
            throw new Exception("Failed to parse CSV file {}", e);
        }
    }
}