package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.tcp.TcpOutboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetClientConnectionFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * Created by kdo on 16. 8. 30.
 */
@EnableIntegration
@IntegrationComponentScan
@Configuration
public class Config {

    private int port = 9876;

    @MessagingGateway(defaultRequestChannel = "toTcp")
    public interface Gateway {
        String sendSimple(String param);
    }

    @Bean
    @ServiceActivator(inputChannel = "toTcp")
    public MessageHandler tcpOutGate(AbstractClientConnectionFactory connectionFactory) {
        TcpOutboundGateway gate = new TcpOutboundGateway();
        gate.setConnectionFactory(connectionFactory);
        gate.setOutputChannelName("resultToString");
        return gate;
    }

    @Bean
    public MessageChannel fromTcp() {
        return new DirectChannel();
    }

    @Bean
    public AbstractClientConnectionFactory clientCF() {
        return new TcpNetClientConnectionFactory("localhost", this.port);
    }

    @Transformer(inputChannel = "resultToString")
    public String convertResult(byte[] bytes) {
        return new String(bytes);
    }
}
