package org.ilearn.springcloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OrderFeignApplication {
    public static void main( String[] args ) {
        SpringApplication.run(OrderFeignApplication.class,args);
    }
}
