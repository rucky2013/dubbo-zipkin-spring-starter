package com.github.jessyZu.dz;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.RpcContext;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.sleuth.SpanExtractor;
import org.springframework.cloud.sleuth.SpanInjector;
import org.springframework.cloud.sleuth.autoconfig.TraceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

/**
 * Created by zhoulai on 16/9/24.
 */

@Configuration
@ConditionalOnProperty(value = "dubbo.trace.enabled", matchIfMissing = true)
@ConditionalOnClass(Filter.class)
@AutoConfigureAfter(TraceAutoConfiguration.class)
public class TraceDubboAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public ApplicationContextAwareBean applicationContextAwareBean() {
        return new ApplicationContextAwareBean();
    }


    @Bean
    public SpanInjector<RpcContext> dubboSpanInjector() {
        return new DubboSpanInjector();
    }


    @Bean
    public SpanExtractor<RpcContext> dubboSpanExtractor(Random random) {
        return new DubboSpanExtractor(random);
    }
}
