package com.antzuhl.zeus.aop;

import com.antzuhl.zeus.annotation.FlowMonitor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Aspect
@Component
@Slf4j
public class FlowMonitorAcpect {

    private Logger logger = LoggerFactory.getLogger(FlowMonitorAcpect.class);

    @Pointcut("@annotation(com.antzuhl.zeus.annotation.FlowMonitor)")
    public void annotationPoinCut(){}

    @After("annotationPoinCut()")
    public void after(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String classPath = joinPoint.getTarget().getClass().getName();
        logger.info("flowMonitor Api ==> {}", classPath);
        FlowMonitor flowMonitor = signature.getMethod().getAnnotation(FlowMonitor.class);
        List<Date> dates = new ArrayList<>();
        flowMonitor.name();
    }

}
