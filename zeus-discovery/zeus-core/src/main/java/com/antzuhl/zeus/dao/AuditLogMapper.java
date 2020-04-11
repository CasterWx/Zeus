package com.antzuhl.zeus.dao;

import com.antzuhl.zeus.beans.AuditLog;

import java.util.List;

public interface AuditLogMapper {
    int deleteByPrimaryKey(String id);

    int insert(AuditLog record);

    int insertSelective(AuditLog record);

    AuditLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AuditLog record);

    int updateByPrimaryKey(AuditLog record);

    List<AuditLog> queryAll();

    List<AuditLog> getNoAccessAuditLogs();

    int ruleOut(String id);

    int createTable();
}
