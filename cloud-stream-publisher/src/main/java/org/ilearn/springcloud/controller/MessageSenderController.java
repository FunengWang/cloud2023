package org.ilearn.springcloud.controller;

import org.ilearn.springcloud.service.MessageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageSenderController {
    @Autowired
    private MessageProvider messageProvider;

    @GetMapping("/message/send")
    public void sendMessage(){
        messageProvider.send();
    }
}
