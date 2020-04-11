package com.antzuhl.zeus.api;

import com.antzuhl.zeus.annotation.AnnotationImport;
import com.antzuhl.zeus.entity.ServiceInfo;
import com.antzuhl.zeus.result.GenericJsonResult;
import com.antzuhl.zeus.result.HResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/zeus/service")
public class ServiceAliveController {

    @RequestMapping("/ping")
    public GenericJsonResult<ServiceInfo> ping() {
        GenericJsonResult<ServiceInfo> result = new GenericJsonResult<>();
        result.setCode(HResult.R_ALIVE);
        ServiceInfo info = new ServiceInfo();
        info.setServiceName(AnnotationImport.getApplicationServerName());
        info.setServiceAddr(AnnotationImport.getApplicationServerAddr());
        info.setComment(AnnotationImport.getApplicationServerComment());
        info.setLiving(1);
        result.setMessage(HResult.valueOfString(HResult.R_ALIVE.getCode()));
        result.setData(info);
        return result;
    }
}
