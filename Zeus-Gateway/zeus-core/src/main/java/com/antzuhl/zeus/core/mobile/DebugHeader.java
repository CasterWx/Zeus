package com.antzuhl.zeus.core.mobile;

import com.antzuhl.zeus.core.context.RequestContext;
import com.antzuhl.zeus.core.filters.ZeusFilter;
import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.util.Pair;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class DebugHeader extends ZeusFilter {
    static final DynamicBooleanProperty INCLUDE_DEBUG_HEADER =
            DynamicPropertyFactory.getInstance().getBooleanProperty("zeus.include.debug.header", true);

    static final DynamicBooleanProperty INCLUDE_ROUTE_URL_HEADER =
            DynamicPropertyFactory.getInstance().getBooleanProperty("zeus.include-route-url-header", false);

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        //request was routed to a zeus gateway server, so don't send response headers
        return INCLUDE_DEBUG_HEADER.get();
    }

    @Override
    public Object run() {
        addStandardResponseHeaders(RequestContext.getCurrentContext().getRequest(), RequestContext.getCurrentContext().getResponse());
        return null;
    }

    public void addStandardResponseHeaders(HttpServletRequest req, HttpServletResponse res) {

        RequestContext context = RequestContext.getCurrentContext();
        List<Pair<String, String>> headers = context.getZeusResponseHeaders();
        headers.add(new Pair("X_ZEUS", "mobile_gateway"));
        // TODO, get zeus instance id
        headers.add(new Pair("X_ZEUS_INSTANCE", "unknown"));
        headers.add(new Pair("CONNECTION", "KEEP_ALIVE"));
        headers.add(new Pair("X_ZEUS_FILTER_EXECUTION_STATUS", context.getFilterExecutionSummary().toString()));
        headers.add(new Pair("X_ORIGINATING_URL", getOriginatingURL()));
        if (INCLUDE_ROUTE_URL_HEADER.get()) {
            String routeUrl = context.getRouteUrl().toString();
            if (routeUrl != null && !routeUrl.isEmpty()) {
                headers.add(new Pair("x-zeus-route-url", routeUrl));
            }
        }

        //Support CROS
        headers.add(new Pair("Access-Control-Allow-Origin", "*"));
        headers.add(new Pair("Access-Control-Allow-Headers","Content-Type, Accept"));

        headers.add(new Pair("x-zeus-remote-call-cost", String.valueOf(RequestContext.getCurrentContext().get("remoteCallCost"))));

        if (!context.errorHandled() && context.getResponseStatusCode() >= 400) {
            headers.add(new Pair("X_ZEUS_ERROR_CAUSE", "Error from Origin"));

        }


    }

    String getOriginatingURL() {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();

        String protocol = request.getHeader("X_FORWARDED_PROTO");
        if (protocol == null) protocol = "http";
        String host = request.getHeader("HOST");
        String uri = request.getRequestURI();
        String URL = "${protocol}://${host}${uri}";
        if (request.getQueryString() != null) {
            URL += "?${request.getQueryString()}";
        }
        return URL;
    }

  
}

