package org.ilearn.springcloud.Message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Sink.class)
@Slf4j
public class MessageListener {
    @Value("${server.port}")
    private String port;
    @StreamListener(Sink.INPUT)
    public void receive(Message<String> message){
        log.info("Message Recevier from port "+port+" recevied mesage : "+message);
    }
}
