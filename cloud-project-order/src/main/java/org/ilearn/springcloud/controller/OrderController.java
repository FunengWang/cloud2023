package org.ilearn.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.ilearn.springcloud.entities.CommonResult;
import org.ilearn.springcloud.entities.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@Slf4j
@RequestMapping("/consumer")
public class OrderController {
    private static final String PAYMENT_SERVICE = "http://CLOUD-PAYMENT-SERVICE";
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/payment/create")
    public CommonResult<Integer> create(Payment payment){
        return restTemplate.postForObject(PAYMENT_SERVICE +"/payment/create",payment,CommonResult.class);
    }

    @GetMapping("/payment/get/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") Long id){
        return restTemplate.getForObject(PAYMENT_SERVICE +"/payment/get/"+id,CommonResult.class);
    }
}
