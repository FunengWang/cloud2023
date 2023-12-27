package org.ilearn.springcloud.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "CLOUD-PAYMENT-SERVICE-HYSTRIX",fallback = PaymentFallBackService.class)
public interface PaymentHystrixService {
    @GetMapping("/payment/hystrix/ok/{id}")
     String ok(@PathVariable("id")Integer id);

    @GetMapping("/payment/hystrix/timeout/{id}")
     String timeout(@PathVariable("id")Integer id);
}
