package com.example;

import com.example.tcp.ClientConfig;
import com.example.tcp.Gateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.integration.ip.util.TestingUtilities;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by kdo on 16. 8. 30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ClientConfig.class, TcpNotNeedServerTest.ServerConfig.class})
public class TcpNotNeedServerTest {
    @Autowired
    Gateway gateway;

    @Autowired
    AbstractServerConnectionFactory crLfServer;

    @Before
    public void setup() {
        TestingUtilities.waitListening(this.crLfServer, 10000L);
    }

    @Test
    public void testTcp() {
        String result = gateway.sendSimple("test");
        assertThat(result, is("TEST"));
    }

    @EnableIntegration
    @IntegrationComponentScan
    @Configuration
    public static class ServerConfig {

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
            return new TcpNetServerConnectionFactory(9876);
        }
    }
}
