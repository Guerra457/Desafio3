package org.compass.msticketmanagerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "org.compass.msticketmanagerapi.client")
@SpringBootApplication
public class MsTicketManagerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsTicketManagerApiApplication.class, args);
    }

}
