package com.github.jessyZu.dz;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by zhoulai on 16/9/24.
 */
public class ApplicationContextAwareBean implements ApplicationContextAware {
    public static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CONTEXT = applicationContext;
    }
}