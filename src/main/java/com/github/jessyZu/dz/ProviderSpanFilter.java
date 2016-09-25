package com.github.jessyZu.dz;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import org.springframework.cloud.sleuth.*;
import org.springframework.cloud.sleuth.sampler.NeverSampler;

import java.util.Map;

/**
 * Created by zhoulai on 16/9/25.
 */

@Activate(group = {Constants.PROVIDER}, order = -9000)
public class ProviderSpanFilter implements Filter {

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        boolean isTraceDubbo = false;
        Tracer tracer = null;
        SpanExtractor spanExtractor = null;
        SpanInjector spanInjector = null;
        SpanReporter spanReporter = null;
        Span span = null;
        try {
            Map<String, String> attachments = RpcContext.getContext().getAttachments();
            tracer = ApplicationContextAwareBean.CONTEXT.getBean(Tracer.class);
            spanExtractor = ApplicationContextAwareBean.CONTEXT.getBean(DubboSpanExtractor.class);
            spanInjector = ApplicationContextAwareBean.CONTEXT.getBean(DubboSpanInjector.class);
            spanReporter = ApplicationContextAwareBean.CONTEXT.getBean(SpanReporter.class);

            isTraceDubbo = (tracer != null && spanExtractor != null && spanInjector != null && spanReporter != null);
            if (isTraceDubbo) {
                String spanName = invoker.getUrl().getParameter("interface") + ":" + invocation.getMethodName() + ":" + invoker.getUrl().getParameter("version") + "(" + invoker.getUrl().getHost() + ")";
                Span parent = spanExtractor
                        .joinTrace(RpcContext.getContext());
                boolean skip = Span.SPAN_NOT_SAMPLED.equals(attachments.get(Span.SAMPLED_NAME));
                if (parent != null) {
                    span = tracer.createSpan(spanName, parent);
                    if (parent.isRemote()) {
                        parent.logEvent(Span.SERVER_RECV);
                    }
                } else {
                    if (skip) {
                        span = tracer.createSpan(spanName, NeverSampler.INSTANCE);
                    } else {
                        span = tracer.createSpan(spanName);
                    }
                    span.logEvent(Span.SERVER_RECV);
                }

                spanInjector.inject(span, RpcContext.getContext());
            }
            Result result = invoker.invoke(invocation);
            return result;


        } finally {
            if (isTraceDubbo && span != null) {
                if (span.hasSavedSpan()) {
                    Span parent = span.getSavedSpan();
                    if (parent.isRemote()) {
                        parent.logEvent(Span.SERVER_SEND);
                        parent.stop();
                        spanReporter.report(parent);
                    }
                } else {
                    span.logEvent(Span.SERVER_SEND);
                }
                tracer.close(span);
            }
        }

    }

}
