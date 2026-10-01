package com.ecommerce.fornecedores_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FornecedoresServiceApplication {
    static void main(String[] args) {
        SpringApplication.run(FornecedoresServiceApplication.class, args);
    }
}