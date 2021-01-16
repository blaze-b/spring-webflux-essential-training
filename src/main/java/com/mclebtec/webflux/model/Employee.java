package com.mclebtec.webflux.model;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;

@Data
@Document
@Scope(scopeName = "request", proxyMode = TARGET_CLASS)
public class Employee {
    @Id
    int id;
    @Field(value = "first_name")
    String firstName;
    @Field(value = "last_name")
    String lastName;
    @Field(value = "email")
    String email;

}
