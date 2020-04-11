package com.antzuhl.zeus.core.filters;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.antzuhl.zeus.core.common.Constants;
import com.antzuhl.zeus.core.common.FilterInfo;
import com.antzuhl.zeus.core.common.IZeusFilterDao;
import com.google.common.collect.Lists;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HttpZeusFilterDao implements IZeusFilterDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpZeusFilterDao.class);
	
    private DynamicStringProperty filterRepository = DynamicPropertyFactory.getInstance().getStringProperty(Constants.ZEUS_FILTER_REPO, "http://localhost:8081/filters");
    private String applicationName;
    private CloseableHttpClient httpclient;
	public HttpZeusFilterDao(String applicationName){
		this.applicationName = applicationName;
		this.httpclient = newClient(); 
	}
	
	@Override
	public List<FilterInfo> getAllCanaryFilters() throws Exception {
		List<FilterInfo> list = Lists.newArrayList();
		
		HttpGet method = new HttpGet(filterRepository.get()+"/canary/"+applicationName);
		
		CloseableHttpResponse response = httpclient.execute(method);
		
		String body = EntityUtils.toString(response.getEntity());
		JSONArray jsonArray = JSON.parseArray(body);
		
		for(int i=0;i<jsonArray.size();i++){
			JSONObject json = jsonArray.getJSONObject(i);
			
			FilterInfo filter = JSON.toJavaObject(json, FilterInfo.class);
			
			filter.setFilterCode(new String(Base64.decodeBase64(filter.getFilterCode().getBytes("utf-8")),"utf-8"));
			list.add(filter);
		}
		return list;
	}


	
	@Override
	public List<FilterInfo> getAllActiveFilters() throws Exception {
		
		List<FilterInfo> list = Lists.newArrayList();
		HttpGet method = new HttpGet(filterRepository.get()+"/active/"+applicationName);
		
		CloseableHttpResponse response = httpclient.execute(method);
		
		String body = EntityUtils.toString(response.getEntity());
		JSONArray jsonArray = JSON.parseArray(body);
		
		for(int i=0;i<jsonArray.size();i++){
			JSONObject json = jsonArray.getJSONObject(i);
			
			FilterInfo filter = JSON.toJavaObject(json, FilterInfo.class);
			
			filter.setFilterCode(new String(Base64.decodeBase64(filter.getFilterCode().getBytes("utf-8")),"utf-8"));
			list.add(filter);
		}
		return list;
	}

	@Override
	public List<String> getAllFilterIds() throws Exception {
		return null;
	}

	@Override
	public List<FilterInfo> getZeusFilters(String filterId) throws Exception {
		return null;
	}

	@Override
	public FilterInfo getFilter(String filterId, int revision) throws Exception {
		return null;
	}

	@Override
	public FilterInfo getLatestFilter(String filterId) throws Exception {
		return null;
	}

	@Override
	public FilterInfo getActiveFilter(String filterId) throws Exception {
		return null;
	}



	@Override
	public FilterInfo canaryFilter(String filterId, int revision) throws Exception {
		return null;
	}

	@Override
	public FilterInfo activateFilter(String filterId, int revision) throws Exception {
		return null;
	}

	@Override
	public FilterInfo deactivateFilter(String filterId, int revision) throws Exception {
		return null;
	}

	@Override
	public FilterInfo addFilter(String filterCode, String filterType, String filterName,
			String filterDisablePropertyName, String filterOrder) throws Exception {
		return null;
	}

	@Override
	public String getFilterIdsRaw(String index) {
		return null;
	}

	@Override
	public List<String> getFilterIdsIndex(String index) {
		return null;
	}

	@Override
	public void close() {
		
	}
	
	
	private CloseableHttpClient newClient() {
		RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(0, false);
		RedirectStrategy redirectStrategy = new RedirectStrategy() {
			@Override
			public boolean isRedirected(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) {
				return false;
			}

			@Override
			public HttpUriRequest getRedirect(HttpRequest httpRequest, HttpResponse httpResponse,
					HttpContext httpContext) {
				return null;
			}
		};
		CloseableHttpClient httpclient = HttpClients.custom().disableContentCompression()
				.setConnectionManager(newConnectionManager()).setDefaultRequestConfig(config)
				.setRetryHandler(retryHandler).setRedirectStrategy(redirectStrategy).disableCookieManagement().build();
		return httpclient;
	}
	
	private HttpClientConnectionManager newConnectionManager() {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(10);
		cm.setDefaultMaxPerRoute(5);
		return cm;
	}


}

