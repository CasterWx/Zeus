package com.antzuhl.zeus.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.antzuhl.zeus.api.request.ServiceRequest;
import com.antzuhl.zeus.api.response.ServiceInfoResponce;
import com.antzuhl.zeus.beans.Service;
import com.antzuhl.zeus.config.ZeusApplicationListener;
import com.antzuhl.zeus.registory.ServiceRegistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/zeus-server/service")
public class ServiceController {
    @Autowired
    private ServiceRegistory serviceRegistory;

    private static Logger logger = LoggerFactory.getLogger(ServiceController.class);

    /**
     * 查询所有服务
     */
    @RequestMapping(value = "/queryService")
    public String queryAll() {
        List<Service> result = (List<Service>) serviceRegistory.findAll();
        return JSON.toJSON(result).toString();
    }

    /**
     * 注册一个服务
     */
    @RequestMapping(value = "/registory")
    public ServiceInfoResponce registory(@RequestBody ServiceRequest serviceRequest, HttpServletRequest request) {
        ServiceInfoResponce responce = new ServiceInfoResponce();
        if (serviceRequest == null) {
            return null;
        }
        Service service = serviceRequest.toData();
        service.setLiving(1);
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        service.setPort(serviceRequest.getPort());
        service.setServiceAddr(ip);

        Service result = serviceRegistory.findByServiceName(service.getServiceName());
        if (result != null) {
            service.setId(result.getId());
        }
        service = serviceRegistory.save(service);
        // service注册成功，注册
        ZeusApplicationListener.serviceMap.put(service.getId(), service);

        responce.setId(service.getId());

        responce.setServiceName(service.getServiceName());
        responce.setComment(service.getComment());
        responce.setLiving(service.getLiving());
        return responce;
    }


    /**
     * 测试接口
     */
    @RequestMapping(value = "/test")
    public Service test() {
        Service service = new Service();
        service.setServiceAddr("localhost");
        service.setComment("你好");
        service.setLiving(1);
        service = serviceRegistory.save(service);
        return service;
    }
}
