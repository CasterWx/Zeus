package com.antzuhl.zeus.beans;

import com.antzuhl.zeus.api.response.LocalAuditLog;

import java.io.Serializable;
import java.util.Date;

public class AuditLog implements Serializable {
    private String id;
    private Date createTime;
    private Date modifyTime;
    private String method;
    private String path;
    private Integer living;
    private String useTime;
    private Integer status;

    private static final long serialVersionUID = 1L;

    public AuditLog() {
    }

    public LocalAuditLog toData() {
        LocalAuditLog localAuditLog = new LocalAuditLog();
        localAuditLog.setId(getId());
        localAuditLog.setCreateTime(getCreateTime());
        localAuditLog.setMethod(getMethod());
        localAuditLog.setModifyTime(getModifyTime());
        localAuditLog.setPath(getPath());
        localAuditLog.setLiving(getLiving());
        localAuditLog.setUseTime(getUseTime());
        localAuditLog.setStatus(getStatus());
        return localAuditLog;
    }

    public AuditLog(String i, Date date, Date date1, String s, String s1, int living, int i1) {
        setId(i);
        setCreateTime(date);
        setModifyTime(date1);
        setMethod(s);
        setPath(s1);
        setLiving(living);
        setStatus(i1);
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public void setLiving(Integer living) {
        this.living = living;
    }

    public Integer getLiving() {
        return living;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
