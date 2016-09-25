package com.github.jessyZu.dz;

import com.github.jessyZu.dz.api.Service1;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.application.name=TestService1Consumer"})
@ContextConfiguration(locations = {"classpath:spring/dubbo-demo-service1-consumer.xml"}, classes = TestApp.class)
public class TestService1Consumer {

    @Autowired
    Service1 service1;

    @Test
    public void test() throws IOException {
        service1.hi();
        System.in.read();


    }

}
