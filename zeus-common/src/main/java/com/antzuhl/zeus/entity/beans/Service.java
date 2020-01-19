package com.antzuhl.zeus.entity.beans;

public class Service {
    private Integer id ;
    private Integer namespaceId;
    private String name;
    private String status;
    private String info;

    public Service() {
    }

    public Service(Integer id, Integer namespaceId, String name, String status, String info) {
        this.id = id;
        this.namespaceId = namespaceId;
        this.name = name;
        this.status = status;
        this.info = info;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", namespaceId='" + namespaceId + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", info='" + info + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
