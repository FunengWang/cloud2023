package org.ilearn.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ConfigClientController {
    @Value("${config.profile.version}")
    private String profile;

    @GetMapping("/profile/version")
    public String getProfile(){
        return profile;
    }
}
