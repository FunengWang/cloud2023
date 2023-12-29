# Introduction
I wrote down this note during learning Spring Cloud. The whole project contains 17 modules. It may look awful. Actually, you don't need to run them all. You only need to run combination of several moudles to demonstrate one Spring Cloud Module.

## Preperation
The project is based on JDK1.8 and built on Maven, uses Mysql(5.7+) and Rabbit MQ.
Make sure you use dependencies with the same versions as below. Using other versions may cause unexpected error.
| Dependency | Version |
| ---------- | ------- |
| spring-cloud-dependencies | Hoxton.SR1 |
| spring-boot-dependencies | 2.2.2.RELEASE |

One step left before you moving on, run `mvn clean` and `mvn install` for module cloud-api-commons, which contains only two entities used in other modules. I avoid code repetition in this way.
```Java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {
    private Integer code;
    private String message;
    private T data;
}
```
```Java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {
    private Long id;
    private String serial;
}

```
# Spring Cloud Components
> Spring Cloud provides tools for developers to quickly build some of the common patterns in distributed systems (e.g. configuration management, service discovery, circuit breakers, intelligent routing, micro-proxy, control bus, short lived microservices and contract testing).

Spring Cloud is a huge community. Many companies (Netflix, Alibaba) made contributions for this community, then Spring Cloud adopted these technology as memebers in Spring Cloud community. While some old technology is not continuted, then new technology emerged.

To implement service discovery, you have four options：Netflix Eureka, Zookeeper, Consul and Alibaba Nacos. So which option should we choose and which do I use in this project? I made a list to break it down.


1. Service Discovery
   - **Netflix Eureka**  <sub>not continuted</sub>
   - Zookeeper
   - Consul
   - _Alibaba Nacos_ <sub>a good option to replace Eureka </sub>
2. Service Call
   - **Netflix Ribbon**
   - LoadBalancer <sub>this one intends to replace Ribbon gradually</sub>
   - ~~Netflix Feign~~ <sub>not continuted,not recommend to use</sub>
   - **OpenFeign** <sub>a good option to replace Feign</sub>
3. Service Fallback and Circuite breaker
   - **Netflix Hsytrix** <sub>not continuted</sub>
   - Resilience4j
   - _Alibaba Sentinel_ <sub>a good option to replace Hystrix</sub>
4. Gateway
   - ~~Netflix Zuul~~ <sub>be not continuted</sub>
   - ~~Netflix Zuul2~~ <sub>not release now,still in the development progress</sub>
   - **Spring Cloud Gateway**
5. Configuration Management
   - **Spring Cloud Config** <sub>not continuted</sub>
   - _Alibaba Nacos_ <sub>a good option to replace Bus</sub>
6. Bus
   - **Spring Cloud Bus** <sub>not continuted</sub>
   - _Alibaba Nacos_ <sub>a good option to replace Bus</sub>

The bold items are used in this project. You may notice that Spring Cloud Alibaba seems a good option. Yes, it is indeedly. Unfortunately, this project doesn't involve Spring Cloud Alibaba.


#  Netflix Eureka
You only need to run involved modules. Run servers first then clients.

## Eureka Server 
There are two eureka server instances, each of them point to each other. Two instances work together, one crashes, another still can work.

| Module Name               | port |
|---------------------------| ------------- |
| cloud-eureka-server       |  7001 |
| cloud-eureka-servr-backup | 7002 |

`EurekaServerApplication.java`
``` Java
@SpringBootApplication
@EnableEurekaServer //add this annotation to indicate a eureka server insatnce
public class EurekaServerApplication { 
    public static void main( String[] args ) {
        SpringApplication.run(EurekaServerApplication.class,args);
    }
}
```
`application.yml`
``` yml
eureka:
  instance:
    hostname: eureka7001.com
  client:
    register-with-eureka: false #indicates eureka server doesn't need register itself
    fetch-registry: false #indicates eureka server doesn't need register itself
    service-url:
      defaultZone: http://eureka7002.com:7002/eureka/ #pointed to another eureka insatnces

```

## Eureka Client
| Module Name | Service Provider | Service Consumer | port |
|-------------|-----------|------------------|------|
| cloud-project-order         |           | Y                | 80   |
| cloud-project-payment | Y         |                  | 8001 |

```java
@SpringBootApplication
@EnableEurekaClient //add this annotation to indicate a eureka client
@RibbonClient(name = "CLOUD-PAYMENT-SERVICE",configuration = CustomizedRobinRule.class)
//ignore @RibbonClient, we will discuss this annotation later
public class OrderApplication {
    public static void main( String[] args ) {
        SpringApplication.run(OrderApplication.class,args);
    }
}
```
``` yml
spring:
  application:
    name: cloud-order-service #indicates the service name

eureka:
  instance:
#    instance-id: order80
    prefer-ip-address: true
  client:
    register-with-eureka: true #register this service in eureka server
    fetch-registry: true #register this service in eureka server
    service-url: #eureka server cluster has two instances
      defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/

```
visit localhost:7001 or localhost:7002 via browser
![img.png](docs/imgs/eureka-server.png)
Modify property `eureka.instance.insatnce-id` will impact how to display in eureka server dashboard, the default display pattern is `${hostname}:${spring.application.name}:${server.port}`

# Netflix Ribbon 
Before moving on, make sure you have already run eureka servers. Run these modules as below.

| Module Name                  | Service Provider/Consumer | service name          | port |
|------------------------------|---------------------------|-----------------------|------|
| cloud-project-order          | Service Consumer          | cloud-order-service   | 80   |
| cloud-project-payment        | Service Provider          | cloud-payment-service | 8001 |
| cloud-project-payment-backup | Service Provider          | cloud-payment-service | 8002 |

**Notice: same microservice applications should have same service name.**

![img.png](docs/imgs/ribbon-eureka-server.png)
## Service Provider
cloud-payment-service is a simple application, expose several web endpoints, query data from DB then return.
There are two cloud-payment-service instances running.

`PaymentApplication.java`
```java
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient //now microservice applications can know each other
public class PaymentApplication {
    public static void main( String[] args ) {
        SpringApplication.run(PaymentApplication.class,args);
    }
}
```
`application.yml`

```yml
server:
  port: 8001
spring:
  application:
    name: cloud-payment-service
  datasource: #build a mysql datasource
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: org.gjt.mm.mysql.Driver
    url: jdbc:mysql://localhost:3306/db2023?useUnicode=true&characterEncoding=utf-8&useSSL=false
    username: root
    password: root
mybatis: #mybatis mapper setting
  config-location: classpath:mybatis-config.xml
  mapperLocation: classpath:mapper/*.xml
  type-aliases-package: org.ilearn.springcloud.entities
```
create a table in Mysql, and insert a few sample data
```sql
CREATE TABLE `payment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `serial` varchar(200) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb3;
```
`PaymentController.java`

autowired a `DiscoveryClient` into controller, now we can know other services and instances via discoveryClient.Try to call this Get Request.
```java
@RestController
@RequestMapping("/payment")
@Slf4j
public class PaymentController {
    @Autowired
    private DiscoveryClient discoveryClient;
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
```

## Service Consumer
We are going to let order-service to call payment-service. To implement this, we combine Ribbon and RestTemplate.
One consumer instance, two provider instances, default call rule is Round-Robbin.

`OrderApplication.java`
```java
@SpringBootApplication
@EnableEurekaClient
@RibbonClient(name = "CLOUD-PAYMENT-SERVICE",configuration = CustomizedRobinRule.class)
/*name indicates service provider's name, 
configuration indicates to override default robbin rule,
if you don't want to override default robbin rule, don't get a value to configuration key*/
public class OrderApplication {
    public static void main( String[] args ) {
        SpringApplication.run(OrderApplication.class,args);
    }
}
```
`ApplicationContextConfig.java`
```java
@Configuration
public class ApplicationContextConfig {
    @Bean
    @LoadBalanced
    //@LoadBalanced indicates to open Load Balance function, default rule is Round-Robbin
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
```
`CustomizedRobbinRule.java`
```java
@Configuration
public class CustomizedRobinRule {
    @Bean
    public IRule myRule(){
        //default rule is Round-robbin, here switch to random rule
        return new RandomRule();
    }
}
```
#  OpenFeign
Robbin has already implemented Load Balancing. Why do we still need OpenFeign? Let's look back on Robbin.
The drawback is hard coding. With OpenFeign, we can implement Load Balancing more elegantly.

`OrderController.java`
```java
@RestController
@Slf4j
@RequestMapping("/consumer")
public class OrderController {
    //hard code here
    private static final String PAYMENT_SERVICE = "http://CLOUD-PAYMENT-SERVICE";
    @Autowired
    private RestTemplate restTemplate;
    
    @GetMapping("/payment/get/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") Long id){
        return restTemplate.getForObject(PAYMENT_SERVICE +"/payment/get/"+id,CommonResult.class);
    }
}
```

| Module Name                  | Service Provider/Consumer | service name          | port |
|------------------------------|---------------------------|-----------------------|------|
| cloud-project-order-feign    | Service Consumer          | cloud-order-service   | 80   |
| cloud-project-payment        | Service Provider          | cloud-payment-service | 8001 |
| cloud-project-payment-backup | Service Provider          | cloud-payment-service | 8002 |

**Notice: Do not run cloud-project-order-feign and cloud-project-order together.**

## Service Provider
No extra config on provider side.

## Service Consumer





# Spring Cloud Netflix Hystrix
# Spring Cloud Gateway
# Spring Cloud Config
## Config Server
## Config Client
# Spring Cloud Stream
## Message Publisher
## Message Receiver



  
