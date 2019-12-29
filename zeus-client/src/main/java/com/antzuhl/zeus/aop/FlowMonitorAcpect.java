package com.antzuhl.zeus.aop;

import com.antzuhl.zeus.bean.FlowApiData;
import com.antzuhl.zeus.cache.FlowApiCache;
import com.antzuhl.zeus.server.FlowMonitor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Aspect
@Component
public class FlowMonitorAcpect {

    private FlowApiCache flowApiCache = new FlowApiCache();

    @Pointcut("@annotation(com.antzuhl.zeus.server.FlowMonitor)")
    public void annotationPoinCut(){}

    @After("annotationPoinCut()")
    public void after(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String classPath = joinPoint.getTarget().getClass().getName();
        FlowMonitor flowMonitor = signature.getMethod().getAnnotation(FlowMonitor.class);
        List<Date> dates = new ArrayList<>();
        FlowApiData flowApiData = null;
        if (flowMonitor != null) {
            Long call = 0L;
            if (flowApiCache.findFlowKey(classPath)) {
                flowApiData = flowApiCache.findCache(classPath);
                call = flowApiData.getCall();
                dates = flowApiData.getDates();
            }
            dates.add(new Date());
            call ++;
            flowApiData = new FlowApiData(flowMonitor.name(),classPath,dates,call);
            flowApiCache.addCache(flowApiData);
            flowApiCache.addFlowKey(classPath);
        }
        List<FlowApiData> list = flowApiCache.getAllCache();
        for (FlowApiData s : list
             ) {
            System.out.println(s.toString());
        }
    }

}
