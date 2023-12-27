package org.ilearn.springcloud;


import org.ilearn.irule.CustomizedRobinRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
@EnableEurekaClient
@RibbonClient(name = "CLOUD-PAYMENT-SERVICE",configuration = CustomizedRobinRule.class)
public class OrderApplication {
    public static void main( String[] args ) {
        SpringApplication.run(OrderApplication.class,args);
    }
}
