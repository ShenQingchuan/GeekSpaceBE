package com.rpzjava.sqbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan("com.rpzjava.sqbe.entities")
@SpringBootApplication
public class SqbeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SqbeApplication.class, args);
    }

}
