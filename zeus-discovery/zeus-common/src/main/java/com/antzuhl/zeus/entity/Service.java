package com.antzuhl.zeus.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Service {

    private Long id;
    private String serviceName;
    private String serviceAddr;
    private Integer port;
    private Integer living;
    private String comment;
}
