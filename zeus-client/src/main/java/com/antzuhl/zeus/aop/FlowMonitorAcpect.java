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
        if (flowMonitor != null) {
            Long call = 0L;
            if (flowApiCache.findFlowKey(classPath)) {
                call = flowApiCache.findCache(classPath).getCall();
            }
            call ++;
            FlowApiData flowApiData = new FlowApiData(flowMonitor.name(),classPath,call);
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
