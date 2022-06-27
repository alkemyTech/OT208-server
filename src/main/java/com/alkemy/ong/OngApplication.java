package com.alkemy.ong;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "ONG API by Alkemy 208 Java Team", version = "1.0.0"))
@SpringBootApplication
public class OngApplication {

    public static void main(String[] args) {
        SpringApplication.run(OngApplication.class, args);
    }

}
