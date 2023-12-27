package org.ilearn.springcloud.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
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
}
