package scripts.post

import com.antzuhl.zeus.core.context.RequestContext
import com.antzuhl.zeus.core.filters.ZeusFilter
import com.antzuhl.zeus.core.monitoring.StatManager

class Stats extends ZeusFilter {
    @Override
    String filterType() {
        return "post"
    }

    @Override
    int filterOrder() {
        return 20000
    }

    @Override
    boolean shouldFilter() {
        return true
    }

    @Override
    Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        int status = ctx.getResponseStatusCode();
        StatManager sm = StatManager.manager
        sm.collectRequestStats(ctx.getRequest());
        sm.collectRouteStatusStats(ctx.routeName, status);
    }

}
