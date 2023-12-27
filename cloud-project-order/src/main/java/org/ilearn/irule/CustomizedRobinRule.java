package org.ilearn.irule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomizedRobinRule {
    @Bean
    public IRule myRule(){
        //default rule is Round-robbin
        return new RandomRule();
    }
}
