package com.antzuhl.zeus.entity.request;

import com.antzuhl.zeus.entity.ServiceInfo;
import lombok.Data;

@Data
public class ServiceRequest {
    private Long serviceId;
    private String serviceName;
    private String serviceAddr;
    private Integer port;
    private String comment;

    public ServiceInfo toData() {
        ServiceInfo service = new ServiceInfo();
        service.setServiceId(null);
        service.setServiceAddr(getServiceAddr());
        service.setPort(getPort());
        service.setComment(getComment());
        return service;
    }


    public boolean isVaild() {
        return serviceAddr != null;
    }
}
