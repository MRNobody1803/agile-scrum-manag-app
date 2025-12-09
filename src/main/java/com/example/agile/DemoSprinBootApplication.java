package com.example.agile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.example.agile",
        "com.example.agile.mappers"
})
public class DemoSprinBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSprinBootApplication.class, args);
    }

}
