package com.example.tcp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.tcp.TcpOutboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetClientConnectionFactory;
import org.springframework.messaging.MessageHandler;

/**
 * 클라이언트 설정
 * Created by kdo on 16. 8. 30.
 */
@EnableIntegration
@IntegrationComponentScan
@Configuration
public class ClientConfig {

    /**
     * 서버 서비스 포트
     */
    private final int port = 9876;

    @Bean
    public AbstractClientConnectionFactory connectionFactory() {
        return new TcpNetClientConnectionFactory("localhost", this.port);
    }

    @Bean
    @ServiceActivator(inputChannel = "toTcp")
    public MessageHandler tcpOutGate(AbstractClientConnectionFactory connectionFactory) {
        TcpOutboundGateway gate = new TcpOutboundGateway();
        gate.setConnectionFactory(connectionFactory);
        gate.setOutputChannelName("resultToString");
        return gate;
    }

    @Transformer(inputChannel = "resultToString")
    public String resultToString(byte[] bytes) {
        return new String(bytes);
    }
}
