package com.antzuhl.zeus.config;


import com.antzuhl.zeus.annotation.AnnotationImport;
import com.antzuhl.zeus.entity.request.ServiceRequest;
import com.antzuhl.zeus.entity.response.ServiceInfoResponce;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerConfig implements ApplicationListener<WebServerInitializedEvent> {
    public int getServerPort() {
        return serverPort;
    }

    public static int serverPort;

    public String getUrl() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "http://" + address.getHostAddress() + ":" + this.serverPort;
    }

    public String getHost() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address.getHostAddress();
    }

    private static Logger logger = LoggerFactory.getLogger(ServerConfig.class);

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        serverPort = event.getWebServer().getPort();
        // register
        RestTemplate restTemplate = new RestTemplate();
        ServiceRequest request = new ServiceRequest();
        request.setServiceName(AnnotationImport.getApplicationServerName());
        request.setPort(ServerConfig.serverPort);
        request.setComment(AnnotationImport.getApplicationServerComment());
        ServiceInfoResponce responce = restTemplate.postForObject("http://" + AnnotationImport.getApplicationServerAddr() + ":7777/zeus-server/service/registory", request, ServiceInfoResponce.class);
        logger.info("Service Register {}", responce);
    }
}

