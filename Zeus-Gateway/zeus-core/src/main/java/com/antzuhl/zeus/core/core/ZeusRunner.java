package com.antzuhl.zeus.core.core;

import com.antzuhl.zeus.core.common.HttpServletRequestWrapper;
import com.antzuhl.zeus.core.common.HttpServletResponseWrapper;
import com.antzuhl.zeus.core.common.ZeusException;
import com.antzuhl.zeus.core.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class initializes servlet requests and responses into the RequestContext
 * and wraps the FilterProcessor calls to preRoute(), route(), postRoute(), and
 * error() methods
 *
 */
public class ZeusRunner {

	/**
	 * Creates a new <code>ZeusRunner</code> instance.
	 */
	public ZeusRunner() {
	}

	/**
	 * sets HttpServlet request and HttpResponse
	 *
	 * @param servletRequest
	 * @param servletResponse
	 */
	public void init(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
		RequestContext.getCurrentContext().setRequest(new HttpServletRequestWrapper(servletRequest));
		RequestContext.getCurrentContext().setResponse(new HttpServletResponseWrapper(servletResponse));
	}

	/**
	 * executes "pre" filterType ZeusFilters
	 *
	 * @throws com.antzuhl.zeus.core.common.ZeusException
	 */
	public void preRoute() throws ZeusException {
		FilterProcessor.getInstance().preRoute();
	}

	/**
	 * executes "route" filterType ZeusFilters
	 *
	 * @throws ZeusException
	 */
	public void route() throws ZeusException {
		FilterProcessor.getInstance().route();
	}

	/**
	 * executes "post" filterType ZeusFilters
	 *
	 * @throws ZeusException
	 */
	public void postRoute() throws ZeusException {
		FilterProcessor.getInstance().postRoute();
	}

	/**
	 * executes "error" filterType ZeusFilters
	 * @throws ZeusException
	 */
	public void error() throws ZeusException {
		FilterProcessor.getInstance().error();
	}

}
