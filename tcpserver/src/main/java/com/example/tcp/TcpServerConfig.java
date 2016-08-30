package com.example.tcp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.messaging.MessageChannel;

/**
 * Created by kdo on 16. 8. 30.
 */
@EnableIntegration
@IntegrationComponentScan
@Configuration
public class TcpServerConfig {
    private int port = 9876;

    @Bean
    public TcpInboundGateway tcpInGate(AbstractServerConnectionFactory connectionFactory) {
        TcpInboundGateway inGate = new TcpInboundGateway();
        inGate.setConnectionFactory(connectionFactory);
        inGate.setRequestChannel(fromTcp());
        return inGate;
    }

    @Bean
    public MessageChannel fromTcp() {
        return new DirectChannel();
    }

    @MessageEndpoint
    public static class Echo {

        @Transformer(inputChannel = "fromTcp", outputChannel = "toEcho")
        public String convert(byte[] bytes) {
            return new String(bytes);
        }

        @ServiceActivator(inputChannel = "toEcho")
        public String upCase(String in) {
            return in.toUpperCase();
        }

        @Transformer(inputChannel = "resultToString")
        public String convertResult(byte[] bytes) {
            return new String(bytes);
        }

    }

    @Bean
    public AbstractServerConnectionFactory serverCF() {
        return new TcpNetServerConnectionFactory(this.port);
    }
}
