package com.antzuhl.zeus.core.mobile;

import com.antzuhl.zeus.core.context.RequestContext;
import com.antzuhl.zeus.core.filters.ZeusFilter;
import com.antzuhl.zeus.core.util.Debug;
import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicPropertyFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class DebugRequest extends ZeusFilter {
    private static final String ZEUS_BODY_DEBUG_DISABLE = "zeus.body.debug.disable";
    private static final String ZEUS_HEADER_DEBUG_DISABLE = "zeus.header.debug.disable";
    static final DynamicBooleanProperty BODY_DEBUG_DISABLED =
            DynamicPropertyFactory.getInstance().getBooleanProperty(ZEUS_BODY_DEBUG_DISABLE, false);
    static final DynamicBooleanProperty HEADER_DEBUG_DISABLED =
            DynamicPropertyFactory.getInstance().getBooleanProperty(ZEUS_HEADER_DEBUG_DISABLE, true);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
    	return -10;
    }

    @Override
    public boolean shouldFilter() {
    	return Debug.debugRequest();
    }

    @Override
    public Object run() {
        HttpServletRequest req = RequestContext.getCurrentContext().getRequest();

        Debug.addRequestDebug("REQUEST:: " + req.getScheme() + " " + req.getRemoteAddr() + ":" + req.getRemotePort());
        Debug.addRequestDebug("REQUEST:: > " + req.getMethod() + " " + req.getRequestURI() + " " + req.getProtocol());

        Enumeration<String> headerIt = req.getHeaderNames();
        while (headerIt.hasMoreElements()) {
            String name = (String) headerIt.nextElement();
            String value = req.getHeader(name);
            Debug.addRequestDebug("REQUEST:: > " + name + ":" + value);
        }

//        final RequestContext ctx = RequestContext.getCurrentContext();
//        if (!ctx.isChunkedRequestBody() && !BODY_DEBUG_DISABLED.get()) {
//            InputStream inp = ctx.getRequest().getInputStream();
//            String body = null;
//            if (inp != null) {
//                body = inp.getText();
//                Debug.addRequestDebug("REQUEST:: > " + body);
//            }
//        }
        return null;
    }

   
}
