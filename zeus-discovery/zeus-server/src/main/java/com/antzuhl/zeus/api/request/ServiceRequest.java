package com.antzuhl.zeus.api.request;

import com.antzuhl.zeus.beans.Service;
import lombok.Data;

@Data
public class ServiceRequest {
    private Long serviceId;
    private String serviceName;
    private String serviceAddr;
    private Integer port;
    private String comment;

    public Service toData() {
        Service service = new Service();
        service.setId(null);
        service.setServiceName(getServiceName());
        service.setServiceAddr(getServiceAddr() + ":" + port);
        service.setComment(getComment());
        return service;
    }

}
