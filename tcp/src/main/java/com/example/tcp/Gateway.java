package com.example.tcp;

import org.springframework.integration.annotation.MessagingGateway;

/**
 * Created by kdo on 16. 8. 30.
 */
@MessagingGateway(defaultRequestChannel = "toTcp")
public interface Gateway {
    String sendSimple(String param);
}
