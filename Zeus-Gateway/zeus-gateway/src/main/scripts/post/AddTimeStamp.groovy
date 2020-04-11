package scripts.post

import com.antzuhl.zeus.core.common.ZeusException;
import com.antzuhl.zeus.core.context.RequestContext;
import com.antzuhl.zeus.core.filters.ZeusFilter;

public class AddTimeStamp extends ZeusFilter {

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
		return 1;

	}
	@Override
	public Object run() throws ZeusException {
		long time = System.currentTimeMillis()/1000;
		RequestContext.getCurrentContext().addZeusResponseHeader("X-S2G-TIMESTAMP", String.valueOf(time));
		return true;
	}
}
