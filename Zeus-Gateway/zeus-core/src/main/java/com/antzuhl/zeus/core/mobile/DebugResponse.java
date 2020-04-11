package com.antzuhl.zeus.core.mobile;

import com.antzuhl.zeus.core.context.RequestContext;
import com.antzuhl.zeus.core.filters.ZeusFilter;
import com.antzuhl.zeus.core.util.Debug;
import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DebugResponse extends ZeusFilter {
	private static final Logger LOGGER = LoggerFactory.getLogger(DebugResponse.class);
    private static final String ZEUS_BODY_DEBUG_DISABLE = "zeus.body.debug.disable";
    private static final String ZEUS_HEADER_DEBUG_DISABLE = "zeus.header.debug.disable";
    static final DynamicBooleanProperty BODY_DEBUG_DISABLED =
            DynamicPropertyFactory.getInstance().getBooleanProperty(ZEUS_BODY_DEBUG_DISABLE, false);
    static final DynamicBooleanProperty HEADER_DEBUG_DISABLED =
            DynamicPropertyFactory.getInstance().getBooleanProperty(ZEUS_HEADER_DEBUG_DISABLE, true);

	@Override
	public String filterType() {
		return "post";
	}

	@Override
	public int filterOrder() {
		return 1000;
	}

	@Override
	public boolean shouldFilter() {
		return Debug.debugRequest();
	}

	@Override
	public Object run() {
		List<Pair<String, String>> headers = RequestContext.getCurrentContext().getZeusResponseHeaders();
		for (Pair<String, String> it : headers) {
			Debug.addRequestDebug("OUTBOUND: <  " + it.first() + ":" + it.second());
		}
		List<Pair<String, String>> headers1 = RequestContext.getCurrentContext().getOriginResponseHeaders();
		for (Pair<String, String> it : headers1) {
			Debug.addRequestDebug("OUTBOUND: <  " + it.first() + ":" + it.second());
		}
		dumpRoutingDebug();
		dumpRequestDebug();
		return null;
	}

	public void dumpRequestDebug() {
		@SuppressWarnings("unchecked")
		List<String> rd = (List<String>) RequestContext.getCurrentContext().get("requestDebug");
		if(rd != null){
		StringBuilder b = new StringBuilder("");
		for (String it : rd) {
			b.append("REQUEST_DEBUG::"+it+"\n");
		}
		LOGGER.info(b.toString());
		}
	}

	public void dumpRoutingDebug() {
		@SuppressWarnings("unchecked")
		List<String> rd = (List<String>) RequestContext.getCurrentContext().get("routingDebug");
		if(rd != null){
			StringBuilder b = new StringBuilder("");
			for (String it : rd) {
				b.append("ZEUS_DEBUG::"+it+"\n");
			}
			LOGGER.info(b.toString());
		}
	}
}
