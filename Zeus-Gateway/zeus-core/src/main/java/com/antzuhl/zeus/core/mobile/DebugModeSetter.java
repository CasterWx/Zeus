package com.antzuhl.zeus.core.mobile;

import com.antzuhl.zeus.core.common.Constants;
import com.antzuhl.zeus.core.context.RequestContext;
import com.antzuhl.zeus.core.filters.ZeusFilter;
import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

public class DebugModeSetter extends ZeusFilter {

    static final DynamicBooleanProperty couldSetDebug =
            DynamicPropertyFactory.getInstance().getBooleanProperty("zeus.could.set.debug", true);
    static final DynamicBooleanProperty debugRequest =
            DynamicPropertyFactory.getInstance().getBooleanProperty(Constants.ZEUS_DEBUG_REQUEST, true);
    static final DynamicStringProperty debugParameter =
            DynamicPropertyFactory.getInstance().getStringProperty(Constants.ZEUS_DEBUG_PARAMETER, "debugRequest");

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return -100;
    }

    public boolean shouldFilter() {
        if (!couldSetDebug.get()) {
            return false;
        }
        System.out.println(RequestContext.getCurrentContext().getRequest().getParameter(debugParameter.get()));
        if ("true".equals(RequestContext.getCurrentContext().getRequest().getParameter(debugParameter.get()))) {
        	
        	return true;
        }
        return debugRequest.get();
    }

    public Object run() {
        RequestContext.getCurrentContext().setDebugRequest(true);
        RequestContext.getCurrentContext().setDebugRouting(true);
        return null;
    }
}



