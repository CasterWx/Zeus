package com.antzuhl.zeus.core.mobile;

import com.antzuhl.zeus.core.common.ZeusException;
import com.antzuhl.zeus.core.context.RequestContext;
import com.antzuhl.zeus.core.filters.ZeusFilter;

public class MobileAddTimeStamp extends ZeusFilter {

	private final static int TIMESTAMP_EXPIRED_SECS = 600;

	@Override
	public String filterType() {
		return "post";
	}

	@Override
	public boolean shouldFilter() {		
		return true;
	}
	@Override
	public int filterOrder() {
		return 30;

	}
	@Override
	public Object run() throws ZeusException {
		RequestContext.getCurrentContext().addZeusResponseHeader("X-S2G-TIMESTAMP", String.valueOf(System.currentTimeMillis()/1000));
		return true;
	}
}

