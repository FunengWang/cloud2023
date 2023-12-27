package org.ilearn.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.ilearn.springcloud.entities.CommonResult;
import org.ilearn.springcloud.entities.Payment;
import org.ilearn.springcloud.service.PaymentFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/consumer")
public class OrderFeignController {
    @Autowired
    private PaymentFeignService paymentFeignService;

    @GetMapping("/payment/get/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") Long id){
        return paymentFeignService.getPaymentById(id);
    }

    @GetMapping("/payment/feign/timeout")
    public String paymentTimeout(){
        //default timeout is 1 second, exceed 1 second throw 'feign.RetryableException: Read timed out ......'
        return paymentFeignService.paymentTimeout();
    }
}
