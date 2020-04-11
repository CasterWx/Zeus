package com.antzuhl.zeus.api;

import com.alibaba.fastjson.JSON;
import com.antzuhl.zeus.api.response.LocalAuditLog;
import com.antzuhl.zeus.beans.AuditLog;
import com.antzuhl.zeus.dao.MapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/zeus/auditlog")
public class ClientAuditLogController {

    /**
     * 获取本client收集到的日志
     * 交由server做统一处理
     */
    @RequestMapping("/getAuditLogs")
    public List<LocalAuditLog> getAllLog() {
        List<AuditLog> result = MapperFactory.auditLogMapper.queryAll();
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        List<LocalAuditLog> list = result.stream().map(AuditLog::toData).collect(Collectors.toList());
        return list;
    }
    private static Logger logger = LoggerFactory.getLogger(ClientAuditLogController.class);
    @RequestMapping("/getNoAccessAuditLogs")
    public String getNoAccessAuditLogs() {
        logger.info("getNoAccessAuditLogs Api");
        List<LocalAuditLog> list = new ArrayList<>();
        List<AuditLog> result = MapperFactory.auditLogMapper.getNoAccessAuditLogs();
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        list = result.stream().map(AuditLog::toData).collect(Collectors.toList());

        result.forEach(auditLog -> MapperFactory.auditLogMapper.ruleOut(auditLog.getId()));
        logger.info("Send Log {}", JSON.toJSON(list).toString());
        return JSON.toJSON(list).toString();
    }

}
