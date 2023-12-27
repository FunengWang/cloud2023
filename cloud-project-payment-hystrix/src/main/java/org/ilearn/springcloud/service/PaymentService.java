package org.ilearn.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@DefaultProperties(defaultFallback = "globalFallBack")
public class PaymentService {
    /**
     * Mock a simple healthy service
     * @param id - Integer
     * @return String
     */
    public String ok(Integer id) {
        return "Ok service returns the result： "+id;
    }

    /**
     * Mock a time-consuming service
     * @param id - Integer
     * @return String
     */
    @HystrixCommand(fallbackMethod = "timeoutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000")
    })
    public String timeout(Integer id) {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        int age = 2/0;
        return "Timeout service returns the result : "+id;
    }

    public String timeoutHandler(Integer id) {
        return "Payment service is going through an extreme high volume of request or encountered an unexpected exception. Please invoke later.";
    }

    //=====================Circuit Breaker
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60")
    })
    public String paymentCircuitBreaker(Integer id){
        if(id<0){
            throw new RuntimeException("id must be grater than 0.");
        }
        String serialNo = IdUtil.simpleUUID();
        return "Invoke succeed, serial Number is "+serialNo;
    }

    public String globalFallBack(){
        return "Current service is going through an extreme high volume of request or encountered an unexpected exception. Please invoke later.";
    }
}
