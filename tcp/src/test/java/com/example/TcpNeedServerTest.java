package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by kdo on 16. 8. 30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Config.class})
public class TcpNeedServerTest {

    @Autowired
    Config.Gateway gateway;

    @Test
    public void testSendSimple() {
        String result = gateway.sendSimple("test");
        assertThat(result, is("TEST"));
    }
}
