package scripts.pre

import com.netflix.config.DynamicBooleanProperty
import com.netflix.config.DynamicPropertyFactory
import com.netflix.config.DynamicStringProperty
import com.antzuhl.zeus.core.context.RequestContext
import com.antzuhl.zeus.core.filters.ZeusFilter

class DebugModeSetter extends ZeusFilter {

    static final DynamicBooleanProperty couldSetDebug =
            DynamicPropertyFactory.getInstance().getBooleanProperty("zeus.could.set.debug", true);
    static final DynamicBooleanProperty debugRequest =
            DynamicPropertyFactory.getInstance().getBooleanProperty("zeus.debug.request", false);
    static final DynamicStringProperty debugParameter =
            DynamicPropertyFactory.getInstance().getStringProperty("zeus.debug.parameter", "debugRequest");

    @Override
    String filterType() {
        return 'pre'
    }

    @Override
    int filterOrder() {
        return -100;
    }

    boolean shouldFilter() {
        if (!couldSetDebug.get()) {
            return false
        }
        if ("true".equals(RequestContext.currentContext.getRequest().getParameter(debugParameter.get()))) return true;
        return debugRequest.get();
    }

    Object run() {
        RequestContext.getCurrentContext().setDebugRequest(true)
        RequestContext.getCurrentContext().setDebugRouting(true)
        return null;
    }
}



