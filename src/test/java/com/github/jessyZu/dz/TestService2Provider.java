package com.github.jessyZu.dz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.application.name=TestService2Provider"})
@ContextConfiguration(locations = {"classpath:spring/dubbo-demo-service2-provider.xml"}, classes = TestApp.class)
public class TestService2Provider {


    @Test
    public void test() throws Exception {
        System.in.read();
    }

}
