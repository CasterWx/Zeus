package com.antzuhl.zeus.entity;

import java.io.Serializable;

public class ServiceInfo implements Serializable {
    private Integer serviceId;
    private String serviceName;
    private String serviceAddr;
    private Integer port;
    private Integer living;
    private String comment;

    private static final long serialVersionUID = 1L;

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getPort() {
        return port;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName == null ? null : serviceName.trim();
    }

    public String getServiceAddr() {
        return serviceAddr;
    }

    public void setServiceAddr(String serviceAddr) {
        this.serviceAddr = serviceAddr == null ? null : serviceAddr.trim();
    }

    public Integer getLiving() {
        return living;
    }

    public void setLiving(Integer living) {
        this.living = living;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    @Override
    public String toString() {
        return "ServiceInfo{" +
                "serviceId=" + serviceId +
                ", serviceName='" + serviceName + '\'' +
                ", serviceAddr='" + serviceAddr + '\'' +
                ", port=" + port +
                ", living=" + living +
                ", comment='" + comment + '\'' +
                '}';
    }
}