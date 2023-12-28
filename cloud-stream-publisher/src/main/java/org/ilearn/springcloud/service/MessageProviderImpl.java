package org.ilearn.springcloud.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.util.UUID;

@EnableBinding(Source.class)
@Slf4j
public class MessageProviderImpl implements MessageProvider{
    @Autowired
    private MessageChannel output;

    @Override
    public void send() {
        String uuid = UUID.randomUUID().toString();
        Message<String> message = MessageBuilder.withPayload(uuid).build();
        output.send(message);
        log.info("*********** Sent message : "+uuid);
    }
}
