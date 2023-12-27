package org.ilearn.springcloud.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.ilearn.springcloud.service.PaymentHystrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/consumer")
@DefaultProperties(defaultFallback = "globalFallback")
public class OrderHystrixController {

    @Autowired
    private PaymentHystrixService paymentHystrixService;

    @GetMapping("/payment/hystrix/ok/{id}")
    @HystrixCommand
    public String ok(@PathVariable("id")Integer id){
//        int age = 12/0;
        return paymentHystrixService.ok(id);
    }

    @GetMapping("/payment/hystrix/timeout/{id}")
    @HystrixCommand(fallbackMethod = "timeoutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "1500")
    })
//    @HystrixCommand
    public String timeout(@PathVariable("id")Integer id){
//        int age = 12/0;
        return paymentHystrixService.timeout(id);
    }

    public String timeoutHandler(Integer id) {
        return "Current service encountered an unexpected exception or the related services are hectic. Please invoke later.";
    }

    public String globalFallback(){
        return "Current service encountered an unexpected exception or the related services are hectic. Please invoke later.";
    }
}
