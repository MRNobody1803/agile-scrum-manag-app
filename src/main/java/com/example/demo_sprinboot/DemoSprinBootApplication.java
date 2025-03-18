package com.example.demo_sprinboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.example.demo_sprinboot",
        "com.example.demo_sprinboot.mappers"
})
public class DemoSprinBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSprinBootApplication.class, args);
    }

}
