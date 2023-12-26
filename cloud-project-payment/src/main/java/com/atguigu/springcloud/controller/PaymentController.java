package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payment/create")
    public CommonResult<Integer> create(@RequestBody Payment payment){
        int result = paymentService.create(payment);
        log.info("insert a record :"+result);

        if(result>0){
            return new CommonResult<Integer>(200,"insert succeed.",result);
        }else {
            return new CommonResult<Integer>(444,"insert fail.",result);
        }
    }

    @GetMapping("/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
        Payment payment   = paymentService.getPaymentById(id);
        log.info("query result: "+payment);

        if(payment!=null){
            return new CommonResult<Payment>(200,"query succeed.",payment);
        }else {
            return new CommonResult<Payment>(444,"Not find record with ID: "+id,null);
        }
    }
}
