package com.antzuhl.zeus.filter;

import com.antzuhl.zeus.annotation.AnnotationImport;
import com.antzuhl.zeus.beans.AuditLog;
import com.antzuhl.zeus.dao.MapperFactory;
import com.antzuhl.zeus.framework.SqlResourceFactory;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class AuditLogInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = LoggerFactory.getLogger(AuditLogInterceptor.class);

    private SqlResourceFactory sqlResourceFactory = new SqlResourceFactory();
    private SqlSession sqlSession;

    /**
     * Router白名单
     */
    private List<String> filterRouters = Arrays.asList(
            "/zeus/auditlog/getAuditLogs", "/zeus/service/ping",
            "/favicon.ico", "/error", "/zeus/auditlog/getNoAccessAuditLogs",
            "/zeus/auditlog/ruleOutAutidLogs"
    );


    /**
     * Request
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (containZeusRouter(request.getRequestURI())) {
            return true;
        }
        String applicationName = AnnotationImport.getApplicationServerName();
//        logger.info("Get Environment Application Name : {}", applicationName);
        String randomId = applicationName + "-" + new Date().getTime();
        logger.info("Gener Random Audit Id : {}", randomId);
        AuditLog auditLog = new AuditLog();
        auditLog.setId(randomId);
        auditLog.setMethod(request.getMethod());
        auditLog.setPath(request.getRequestURI());
        auditLog.setCreateTime(new Date());
        auditLog.setModifyTime(new Date());
        auditLog.setStatus(null);
        auditLog.setUseTime(null);
        auditLog.setLiving(1);
        MapperFactory.auditLogMapper.insertSelective(auditLog);
        request.setAttribute("auditLogId", auditLog.getId());
        return true;
    }

    /**
     * Response
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (containZeusRouter(request.getRequestURI())) {
            return;
        }
        String auditLogId = (String) request.getAttribute("auditLogId");

        AuditLog log = MapperFactory.auditLogMapper.selectByPrimaryKey(auditLogId);
        log.setStatus(response.getStatus());
        log.setModifyTime(new Date());
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(log.getModifyTime().getTime() - log.getCreateTime().getTime());
        logger.info("Request {} use Time {}分{}秒{}微秒", log.getPath(), c.get(Calendar.MINUTE), c.get(Calendar.SECOND), c.get(Calendar.MILLISECOND));
        String useTime = c.get(Calendar.MINUTE) + "分" + c.get(Calendar.SECOND) + "秒" + c.get(Calendar.MILLISECOND) + "微秒";
        log.setUseTime(useTime);
        MapperFactory.auditLogMapper.updateByPrimaryKey(log);
    }

    /**
     * 过滤请求
     */
    private boolean containZeusRouter(String path) {
        if (filterRouters.contains(path)) {
            return true;
        }
        return false;
    }
}
