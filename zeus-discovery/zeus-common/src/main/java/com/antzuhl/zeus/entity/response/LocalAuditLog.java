package com.antzuhl.zeus.entity.response;

import java.io.Serializable;
import java.util.Date;

public class LocalAuditLog implements Serializable {
    private String id;
    private String logFrom;
    private Date createTime;
    private Date modifyTime;
    private String method;
    private String path;
    private Integer living;
    private String useTime;
    private Integer status;

    private static final long serialVersionUID = 1L;

    public LocalAuditLog() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogFrom() {
        return logFrom;
    }

    public void setLogFrom(String logFrom) {
        this.logFrom = logFrom;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method == null ? null : method.trim();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public Integer getLiving() {
        return living;
    }

    public void setLiving(Integer living) {
        this.living = living;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }
}
