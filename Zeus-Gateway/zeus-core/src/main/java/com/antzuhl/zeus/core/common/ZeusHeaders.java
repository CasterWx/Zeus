package com.antzuhl.zeus.core.common;

/**
 * HTTP Headers
 */
public class ZeusHeaders {
    public static final String TRANSFER_ENCODING = "transfer-encoding";
    public static final String CHUNKED = "chunked";
    public static final String CONTENT_ENCODING = "content-encoding";
    public static final String CONTENT_LENGTH = "content-length";
    public static final String ACCEPT_ENCODING = "accept-encoding";
    public static final String CONNECTION = "connection";
    public static final String KEEP_ALIVE = "keep-alive";
    public static final String HOST = "host";
    public static final String X_FORWARDED_PROTO = "x-forwarded-proto";
    public static final String X_FORWARDED_FOR = "x-forwarded-for";

    public static final String X_ZEUS = "x-zeus";
    public static final String X_ZEUS_INSTANCE = "x-zeus-instance";
    public static final String X_ORIGINATING_URL = "x-originating-url";
    public static final String X_ZEUS_ERROR_CAUSE = "x-zeus-error-cause";
    public static final String X_ZEUS_CLIENT_HOST = "x-zeus-client-host";
    public static final String X_ZEUS_CLIENT_PROTO = "x-zeus-client-proto";
    public static final String X_ZEUS_SURGICAL_FILTER = "x-zeus-surgical-filter";
    public static final String X_ZEUS_FILTER_EXECUTION_STATUS = "x-zeus-filter-executions";
    public static final String X_ZEUS_REQUEST_TOPLEVEL_ID = "x-zeus-request.toplevel.uuid";
}