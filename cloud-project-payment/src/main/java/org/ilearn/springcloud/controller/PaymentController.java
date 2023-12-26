package org.ilearn.springcloud.controller;

import org.ilearn.springcloud.entities.CommonResult;
import org.ilearn.springcloud.entities.Payment;
import org.ilearn.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@Slf4j
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Value(("${server.port}"))
    private String serverPort;

    @Autowired
    private DiscoveryClient discoveryClient;

    @PostMapping("/create")
    public CommonResult<Integer> create(@RequestBody Payment payment){
        int result = paymentService.create(payment);
        log.info("insert a record :"+result);

        if(result>0){
            return new CommonResult<Integer>(200,"insert succeed. Server port:"+ serverPort,result);
        }else {
            return new CommonResult<Integer>(444,"insert fail. Server port:"+serverPort,result);
        }
    }

    @GetMapping("/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
        Payment payment   = paymentService.getPaymentById(id);
        log.info("query result: "+payment);

        if(payment!=null){
            return new CommonResult<Payment>(200,"query succeed.Server port:"+serverPort,payment);
        }else {
            return new CommonResult<Payment>(444,"Not find record with ID: "+id+"server port: "+serverPort,null);
        }
    }

    @GetMapping("/discovery")
    public Object discovery() {
        List<String> services = discoveryClient.getServices();
        for (String service: services){
            log.info("*******element: "+service);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance:instances){
            log.info(instance.getServiceId()+"\t"+instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
        }
        return discoveryClient;
    }
}
