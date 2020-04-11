package com.antzuhl.zeus.core.mobile;

import com.antzuhl.zeus.core.context.RequestContext;
import com.antzuhl.zeus.core.filters.ZeusFilter;
import com.antzuhl.zeus.core.monitoring.StatManager;

public class Stats extends ZeusFilter {
    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 20000;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        int status = ctx.getResponseStatusCode();
        StatManager sm = StatManager.getManager();
        sm.collectRequestStats(ctx.getRequest());
        sm.collectRouteStatusStats(ctx.getRouteName(), status);
        return null;
    }
}
