package com.antzuhl.zeus.config;

import com.alibaba.fastjson.JSON;
import com.antzuhl.zeus.beans.AuditLog;
import com.antzuhl.zeus.beans.Service;
import com.antzuhl.zeus.entity.ServiceInfo;
import com.antzuhl.zeus.entity.response.LocalAuditLog;
import com.antzuhl.zeus.registory.AuditLogRegistory;
import com.antzuhl.zeus.registory.ServiceRegistory;
import com.antzuhl.zeus.result.GenericJsonResult;
import com.antzuhl.zeus.result.HResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ZeusApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 确认service存活的Task池
     */
    public static ScheduledExecutorService keepaliveService = Executors.newScheduledThreadPool(1);
    /**
     * 服务列表，服务启动时注册到此
     */
    public static ConcurrentHashMap<Long, Service> serviceMap = new ConcurrentHashMap<>();
    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

    private RestTemplate restTemplate;
    private static Logger logger = LoggerFactory.getLogger(ZeusApplicationListener.class);

    @Autowired
    ServiceRegistory serviceRegistory;

    @Autowired
    AuditLogRegistory auditLogRegistory;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logger.info("ZeusApplicationListener init...");
        List<Service> list = (List<Service>) serviceRegistory.findAll();
        for (Service service : list) {
            // 启动时所有service均不可用
            service.setLiving(0);
            serviceRegistory.save(service);
            serviceMap.put(service.getId(), service);
        }
        requestFactory.setConnectTimeout(1000);
        requestFactory.setConnectionRequestTimeout(1000);
        requestFactory.setReadTimeout(1000);
        restTemplate = new RestTemplate(requestFactory);
        ZeusApplicationListener.keepaliveService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                logger.info("Server Map for-each starting...(size:{})", serviceMap.size());
                serviceMap.forEach((key, value) -> {
                    logger.info("{} : {}", key, value);
                    if (value.getLiving() == 1) {
                        logger.info("RestTemplate start request {}", "http://" + value.getServiceAddr() + ":" + value.getPort() + "/zeus/service/ping");
                        GenericJsonResult<ServiceInfo> result = null;
                        try {
                            result = restTemplate.postForObject("http://" + value.getServiceAddr() + ":" + value.getPort() + "/zeus/service/ping",
                                    null, GenericJsonResult.class);
                        } catch (Exception e) {
                            logger.info(e.getMessage());
                        }
                        if (result == null || result.getCode() != HResult.R_ALIVE.getCode()) {
                            logger.warn("Server Down {} {}:{}!!!", value.getServiceName(), value.getServiceAddr(), value.getPort());
                            Service tmp = serviceMap.get(key);
                            tmp.setLiving(0);
                            serviceMap.put(key, tmp);
                            serviceRegistory.save(value);
                        } else {
                            logger.info("Server ping {} {}", result.getCode(), result.getMessage());
                        }
                    }
                });
                logger.info("Server Map for-each end...");
            }
        }, 0, 5, TimeUnit.SECONDS);
        ZeusApplicationListener.keepaliveService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                serviceMap.forEach((key, value) -> {
                    logger.info("logstash task {} : {}", key, value);
                    if (value.getLiving() == 1) {
                        String result = null;
                        try {
                            logger.info("Request Log moudle {}", "http://" + value.getServiceAddr() + ":" + value.getPort() + "/zeus/auditlog/getNoAccessAuditLogs");
                            result = restTemplate.postForObject("http://" + value.getServiceAddr() + ":" + value.getPort() + "/zeus/auditlog/getNoAccessAuditLogs", null, String.class);
                        } catch (Exception e) {
                            logger.info("Log Stash error {}", e.getMessage());
                        }
                        if (!StringUtils.isEmpty(result)) {
                            List<LocalAuditLog> logList = JSON.parseArray(result, LocalAuditLog.class);
                            logger.info("get Log {}", logList);
                            logList.forEach(log->{
                                AuditLog auditLog = new AuditLog();
                                auditLog.setLogId(log.getId());
                                auditLog.setUseTime(log.getUseTime());
                                auditLog.setCreateTime(log.getCreateTime());
                                auditLog.setMethod(log.getMethod());
                                auditLog.setPath(log.getPath());
                                auditLog.setStatus(log.getStatus());
                                auditLog.setModifyTime(log.getModifyTime());
                                auditLogRegistory.save(auditLog);
                            });
                        }
                    }
                });
            }
        }, 0, 10, TimeUnit.SECONDS);
    }
}
