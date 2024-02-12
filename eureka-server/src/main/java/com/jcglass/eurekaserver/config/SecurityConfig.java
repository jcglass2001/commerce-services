package com.jcglass.eurekaserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SecurityConfig {
    @Value("${eureka.username}")
    private String eurekaUsername;
    @Value("${eureka.password}")
    private String eurekaPassword;


}
