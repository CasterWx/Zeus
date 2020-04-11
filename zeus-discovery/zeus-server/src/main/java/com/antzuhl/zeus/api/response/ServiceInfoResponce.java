package com.antzuhl.zeus.api.response;

import lombok.Data;

@Data
public class ServiceInfoResponce {
    private Long id;
    private String serviceName;
    private String serviceAddr;
    private Integer living;
    private String comment;
}
