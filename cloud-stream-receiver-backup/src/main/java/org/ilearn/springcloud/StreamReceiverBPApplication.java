package org.ilearn.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class StreamReceiverBPApplication {
    public static void main( String[] args ) {
        SpringApplication.run(StreamReceiverBPApplication.class,args);
    }
}
