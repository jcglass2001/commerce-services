package com.jcglass.inventory;

import com.jcglass.inventory.model.Inventory;
import com.jcglass.inventory.repo.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class,args);
    }

    @Bean
    public CommandLineRunner LoadData(InventoryRepository inventoryRepository){
        return args -> {


        };
    }
}
