package org.compass.mseventmanagerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "org.compass.mseventmanagerapi.client")
@SpringBootApplication
public class MsEventManagerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsEventManagerApiApplication.class, args);
    }

}
