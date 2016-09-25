package com.github.jessyZu.dz;


import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanInjector;
import org.springframework.cloud.sleuth.Tracer;

import java.util.Map;

/**
 * Created by zhoulai on 16/9/21.
 */
@Activate(group = {Constants.CONSUMER}, order = -9000)
public class ConsumerSpanFilter implements Filter {


    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        boolean isTraceDubbo = false;
        Tracer tracer = null;
        SpanInjector spanInjector = null;
        try {
            Map<String, String> attachments = RpcContext.getContext().getAttachments();
            tracer = ApplicationContextAwareBean.CONTEXT.getBean(Tracer.class);
            spanInjector = ApplicationContextAwareBean.CONTEXT.getBean(DubboSpanInjector.class);
            isTraceDubbo = (tracer != null && spanInjector != null);
            if (isTraceDubbo) {
                String spanName = invoker.getUrl().getParameter("interface") + ":" + invocation.getMethodName() + ":" + invoker.getUrl().getParameter("version") + "(" + invoker.getUrl().getHost() + ")";
                Span newSpan = tracer.createSpan(spanName);
                spanInjector.inject(newSpan, RpcContext.getContext());
                newSpan.logEvent(Span.CLIENT_SEND);

            }

            Result result = invoker.invoke(invocation);
            return result;

        } finally {
            if (isTraceDubbo) {
                if (tracer.isTracing()) {
                    tracer.getCurrentSpan().logEvent(Span.CLIENT_RECV);
                    tracer.close(tracer.getCurrentSpan());
                }

            }
        }
    }

}
