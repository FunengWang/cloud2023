package org.ilearn.springcloud.controller;

import org.ilearn.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@Slf4j
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Value(("${server.port}"))
    private String serverPort;

    @GetMapping("/hystrix/ok/{id}")
    public String ok(@PathVariable("id")Integer id){
        String result = paymentService.ok(id);
        log.info("******result"+result);
        return result;
    }

    @GetMapping("/hystrix/timeout/{id}")
    public String timeout(@PathVariable("id")Integer id){
        String result = paymentService.timeout(id);
        log.info("******result"+result);
        return result;
    }

    @GetMapping("/hystrix/breaker/{id}")
    public String circuitBreaker(@PathVariable("id")Integer id){
        return paymentService.paymentCircuitBreaker(id);
    }

}
