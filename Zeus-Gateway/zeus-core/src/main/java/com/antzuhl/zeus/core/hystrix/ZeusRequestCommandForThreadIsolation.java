package com.antzuhl.zeus.core.hystrix;

import com.antzuhl.zeus.core.common.CatContext;
import com.antzuhl.zeus.core.common.Constants;
import com.dianping.cat.Cat;
import com.dianping.cat.Cat.Context;
import com.netflix.hystrix.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

public class ZeusRequestCommandForThreadIsolation extends HystrixCommand<HttpResponse> {

    HttpClient httpclient;
    HttpUriRequest httpUriRequest;
    HttpContext httpContext;

    public ZeusRequestCommandForThreadIsolation(HttpClient httpclient, HttpUriRequest httpUriRequest, String commandGroup, String commandKey) {
        this(httpclient, httpUriRequest, null,commandGroup, commandKey);
    }
    public ZeusRequestCommandForThreadIsolation(HttpClient httpclient, HttpUriRequest httpUriRequest, HttpContext httpContext, String commandGroup, String commandKey) {
        this(httpclient, httpUriRequest, httpContext,commandGroup, commandKey, ZeusCommandHelper.getThreadPoolKey(commandGroup,commandKey));
    }

    public ZeusRequestCommandForThreadIsolation(HttpClient httpclient, HttpUriRequest httpUriRequest, HttpContext httpContext, String commandGroup, String commandKey, String threadPoolKey) {

        super(Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey(commandGroup))
                .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(threadPoolKey))
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                )
        );
        this.httpclient = httpclient;
        this.httpUriRequest = httpUriRequest;
        this.httpContext = httpContext;
    }

    @Override
    protected HttpResponse run() throws Exception {
        try {
            return forward();
        } catch (IOException e) {
            throw e;
        }
    }

    HttpResponse forward() throws IOException {
    	Context ctx = new CatContext();
    	Cat.logRemoteCallClient(ctx);
    	httpUriRequest.addHeader(Constants.CAT_ROOT_MESSAGE_ID, ctx.getProperty(Context.ROOT));
    	httpUriRequest.addHeader(Constants.CAT_PARENT_MESSAGE_ID, ctx.getProperty(Context.PARENT));
    	httpUriRequest.addHeader(Constants.CAT_CHILD_MESSAGE_ID, ctx.getProperty(Context.CHILD));
        return httpclient.execute(httpUriRequest, httpContext);
    }
}

