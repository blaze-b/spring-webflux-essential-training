package com.mclebtec.webflux.controller;


import com.mclebtec.webflux.model.Employee;
import com.mclebtec.webflux.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Base64;

@Slf4j
@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void create(@RequestBody Employee e) throws Exception {
        log.info("Inside the method to create the employee details");
        employeeService.create(e);
    }

    @PostMapping(value = "/create/bulk", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void create(@RequestPart("files") Flux<FilePart> files) {
        log.info("Inside the method to do the bulk upload with file = {}", files);
        files
                .flatMap(this::readBase64Content)
                .filter(m -> {
                    log.info("Print details = {}", m);
                    return false;
                });
    }

    private Mono<String> readBase64Content(FilePart filePart) {
        log.info("Inside the method to do the bulk upload with file = {}", filePart.filename());
        Mono<String> stringMono = filePart.content().flatMap(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            String content = Base64.getEncoder().encodeToString(bytes);
            return Mono.just(content);
        }).last();
        log.info("Details = {}", stringMono);
        return stringMono;
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<Mono<Employee>> findById(@PathVariable("id") Integer id) {
        Mono<Employee> e = employeeService.findById(id);
        HttpStatus status = e != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<Mono<Employee>>(e, status);
    }

    @GetMapping(value = "/name/{name}")
    @ResponseBody
    public Flux<Employee> findByName(@PathVariable("name") String name) {
        return employeeService.findByName(name);
    }

    @GetMapping(value = "/list", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<Employee> findAll() {
        return employeeService.findAll();
    }

    @PutMapping(value = "/update")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Employee> update(@RequestBody Employee e) {
        return employeeService.update(e);
    }

    @DeleteMapping(value = "/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Integer id) {
        employeeService.delete(id).subscribe();
    }

}
