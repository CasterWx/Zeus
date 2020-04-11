package com.antzuhl.zeus.event;

import com.alibaba.fastjson.JSONObject;
import com.antzuhl.zeus.annotation.AnnotationImport;
import com.antzuhl.zeus.entity.Service;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskApplicationListener implements ApplicationListener<WebServerInitializedEvent> {

    /**
     * 确认service存活的Task池
     */
    public static ScheduledExecutorService keepaliveService = Executors.newScheduledThreadPool(1);
    /**
     * 服务列表，服务启动时注册到此
     */
    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

    private RestTemplate restTemplate;
    private static Logger logger = LoggerFactory.getLogger(TaskApplicationListener.class);

    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        logger.info("TaskApplicationListener init...");
        requestFactory.setConnectTimeout(1000);
        requestFactory.setConnectionRequestTimeout(1000);
        requestFactory.setReadTimeout(1000);
        restTemplate = new RestTemplate(requestFactory);
        keepaliveService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    String result = restTemplate.postForObject("http://" + AnnotationImport.getApplicationServerAddr() + ":7777" + "/zeus-server/service/queryService",
                            null, String.class);
                    if (!StringUtils.isEmpty(result)) {
                        List<Service> list = JSONObject.parseArray(result, Service.class);
                        for (Service service : list) {
                            ServiceDiscovery.getInstance().put(service.getServiceName(), service);
                        }
                    }
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

}
