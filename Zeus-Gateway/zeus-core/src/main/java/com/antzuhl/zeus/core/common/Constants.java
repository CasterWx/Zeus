package com.antzuhl.zeus.core.common;

public class Constants {

	// config items
	public static final String APPLICATION_NAME = "zeus-gateway";
	public static final String DEPLOYMENT_APPLICATION_ID = "archaius.deployment.applicationId";
	public static final String DEPLOY_CONFIG_URL = "archaius.configurationSource.additionalUrls";
	public static final String DEPLOY_ENVIRONMENT = "archaius.deployment.environment";

	public static final String DATA_SOURCE_CLASS_NAME = "zeus.data-source.class-name";
	public static final String DATA_SOURCE_URL = "zeus.data-source.url";
	public static final String DATA_SOURCE_USER = "zeus.data-source.user";
	public static final String DATA_SOURCE_PASSWORD = "zeus.data-source.password";
	public static final String DATA_SOURCE_MIN_POOL_SIZE = "zeus.data-source.min-pool-size";
	public static final String DATA_SOURCE_MAX_POOL_SIZE = "zeus.data-source.max-pool-size";
	public static final String DATA_SOURCE_CONNECT_TIMEOUT = "zeus.data-source.connection-timeout";
	public static final String DATA_SOURCE_IDLE_TIMEOUT = "zeus.data-source.idle-timeout";
	public static final String DATA_SOURCE_MAX_LIFETIME = "zeus.data-source.max-lifetime";

	public static final String FILTER_TABLE_NAME = "zeus.filter.table.name";
	public static final String ZEUS_FILTER_DAO_TYPE = "zeus.filter.dao.type";
	public static final String ZEUS_FILTER_REPO = "zeus.filter.repository";

	public static final String ZEUS_FILTER_ADMIN_ENABLED = "zeus.filter.admin.enabled";

	public static final String ZEUS_FILTER_POLLER_ENABLED = "zeus.filter.poller.enabled";
	public static final String ZEUS_FILTER_POLLER_INTERVAL = "zeus.filter.poller.interval";

	public static final String ZEUS_USE_ACTIVE_FILTERS = "zeus.use.active.filters";
	public static final String ZEUS_USE_CANARY_FILTERS = "zeus.use.canary.filters";

	public static final String ZEUS_FILTER_PRE_PATH = "zeus.filter.pre.path";
	public static final String ZEUS_FILTER_ROUTE_PATH = "zeus.filter.route.path";
	public static final String ZEUS_FILTER_POST_PATH = "zeus.filter.post.path";
	public static final String ZEUS_FILTER_ERROR_PATH = "zeus.filter.error.path";
	public static final String ZEUS_FILTER_CUSTOM_PATH = "zeus.filter.custom.path";

	public static final String ZEUS_SERVLET_ASYNC_TIMEOUT = "zeus.servlet.async.timeout";
	public static final String ZEUS_THREADPOOL_CODE_SIZE = "zeus.thread-pool.core-size";
	public static final String ZEUS_THREADPOOL_MAX_SIZE = "zeus.thread-pool.maximum-size";
	public static final String ZEUS_THREADPOOL_ALIVE_TIME = "zeus.thread-pool.alive-time";

	public static final String ZEUS_INITIAL_STREAM_BUFFER_SIZE = "zeus.initial-stream-buffer-size";
	public static final String ZEUS_SET_CONTENT_LENGTH = "zeus.set-content-length";

	public static final String ZEUS_CLIENT_MAX_CONNECTIONS = "zeus.client.max.connections";
	public static final String ZEUS_CLIENT_ROUTE_MAX_CONNECTIONS = "zeus.client.route.max.connections";

	public static final String DEFAULT_GROUP = "default-group";
	public static final String DEFAULT_NAME = "default-name";
//	public static final String DEFAULT_DOMAIN = "default-domain";

//	public static final String ZEUS_ROUTE_POLLER_ENABLED = "zeus.route.poller.enabled";
//	public static final String ZEUS_ROUTE_POLLER_INTERVAL = "zeus.route.poller.interval";
//	public static final String ZEUS_ROUTE_POLLER_URL = "zeus.route.poller.url";
	// constants
	public static final String CAT_CHILD_MESSAGE_ID = "X-CAT-CHILD-ID";
	public static final String CAT_PARENT_MESSAGE_ID = "X-CAT-PARENT-ID";
	public static final String CAT_ROOT_MESSAGE_ID = "X-CAT-ROOT-ID";
	public static final String CAT_S2G_APP = "X-S2G-CAT-APP";
	public static final String HTTP_ERROR_CODE_HEADER = "X-S2G-CODE";
	public static final String HTTP_ERROR_MESSAGE_HEADER = "X-S2G-MESSAGE";
	public static final String HTTP_S2G_DOMAIN = "X-S2G-DOMAIN";
	public static final String HTTP_S2G_SERVICEID = "X-S2G-SERVICE";
	
//	public static final String GAETEWAY_DATA = "GatewayData";
	
	
    public static final String ZEUS_DEBUG_REQUEST = "zeus.debug.request";
    public static final String ZEUS_DEBUG_PARAMETER = "zeus.debug.parameter";
}

