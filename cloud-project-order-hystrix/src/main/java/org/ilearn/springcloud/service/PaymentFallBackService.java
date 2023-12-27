package org.ilearn.springcloud.service;

import org.springframework.stereotype.Component;

@Component
public class PaymentFallBackService implements PaymentHystrixService{
    @Override
    public String ok(Integer id) {
        return "FallBackService: Current service encountered an unexpected exception or the related services are hectic. Please invoke later.";
    }

    @Override
    public String timeout(Integer id) {
        return "FallBackService : Current service encountered an unexpected exception or the related services are hectic. Please invoke later.";
    }
}
