package com.github.jessyZu.dz.api;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zhoulai on 16/9/25.
 */
public class Service1Impl implements Service1 {
    @Autowired
    Service2 service2;

    public void hi() {
        System.out.println("Service1.hi");
        service2.hi();
    }
}
