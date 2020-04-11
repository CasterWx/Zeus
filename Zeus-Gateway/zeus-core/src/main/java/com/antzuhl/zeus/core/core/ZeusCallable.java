package com.antzuhl.zeus.core.core;

import com.antzuhl.zeus.core.common.ZeusException;
import com.antzuhl.zeus.core.context.RequestContext;
import com.dianping.cat.Cat;
import com.dianping.cat.Cat.Context;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.internal.DefaultMessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.concurrent.Callable;

public class ZeusCallable implements Callable {

	private static Logger LOGGER = LoggerFactory.getLogger(ZeusCallable.class);

	private AsyncContext ctx;
	private ZeusRunner zeusRunner;
	private Context catCtx;
	private HttpServletRequest request;

	public ZeusCallable(Context catContext, AsyncContext asyncContext, ZeusRunner zeusRunner,
			HttpServletRequest request) {
		this.ctx = asyncContext;
		this.zeusRunner = zeusRunner;
		this.catCtx = catContext;
		this.request = request;
	}

	@Override
	public Object call() throws Exception {
		Cat.logRemoteCallServer(catCtx);
		RequestContext.getCurrentContext().unset();
		Transaction tran = ((DefaultMessageProducer) Cat.getProducer()).newTransaction("ZeusCallable",
				request.getRequestURL().toString());
		RequestContext zeusContext = RequestContext.getCurrentContext();
		long start = System.currentTimeMillis();
		try {
			service(ctx.getRequest(), ctx.getResponse());
			tran.setStatus(Transaction.SUCCESS);
		} catch (Throwable t) {
			LOGGER.error("ZeusCallable execute error.", t);
			Cat.logError(t);
			tran.setStatus(t);
		} finally {
			try {
				reportStat(zeusContext, start);
			} catch (Throwable t) {
				Cat.logError("ZeusCallable collect stats error.", t);
			}
			try {
				ctx.complete();
			} catch (Throwable t) {
				Cat.logError("AsyncContext complete error.", t);
			}
			zeusContext.unset();

			tran.complete();
		}
		return null;
	}

	private void service(ServletRequest req, ServletResponse res) {
		try {

			init((HttpServletRequest) req, (HttpServletResponse) res);

			// marks this request as having passed through the "Zeus engine", as
			// opposed to servlets
			// explicitly bound in web.xml, for which requests will not have the
			// same data attached
			RequestContext.getCurrentContext().setZeusEngineRan();

			try {
				preRoute();
			} catch (ZeusException e) {
				error(e);
				postRoute();
				return;
			}
			try {
				route();
			} catch (ZeusException e) {
				error(e);
				postRoute();
				return;
			}
			try {
				postRoute();
			} catch (ZeusException e) {
				error(e);
				return;
			}

		} catch (Throwable e) {
			error(new ZeusException(e, 500, "UNHANDLED_EXCEPTION_" + e.getClass().getName()));
		}
	}

	/**
	 * executes "post" ZeusFilters
	 *
	 * @throws ZeusException
	 */
	private void postRoute() throws ZeusException {
		Transaction tran = Cat.getProducer().newTransaction("ZeusCallable", "postRoute");
		try {
			zeusRunner.postRoute();
			tran.setStatus(Transaction.SUCCESS);
		} catch (Throwable e) {
			tran.setStatus(e);
			throw e;
		} finally {
			tran.complete();
		}
	}

	/**
	 * executes "route" filters
	 *
	 * @throws ZeusException
	 */
	private void route() throws ZeusException {
		Transaction tran = Cat.getProducer().newTransaction("ZeusCallable", "route");
		try {
			zeusRunner.route();
			tran.setStatus(Transaction.SUCCESS);
		} catch (Throwable e) {
			tran.setStatus(e);
			throw e;
		} finally {
			tran.complete();
		}
	}

	/**
	 * executes "pre" filters
	 *
	 * @throws ZeusException
	 */
	private void preRoute() throws ZeusException {
		Transaction tran = Cat.getProducer().newTransaction("ZeusCallable", "preRoute");
		try {
			zeusRunner.preRoute();
			tran.setStatus(Transaction.SUCCESS);
		} catch (Throwable e) {
			tran.setStatus(e);
			throw e;
		} finally {
			tran.complete();
		}
	}

	/**
	 * initializes request
	 *
	 * @param servletRequest
	 * @param servletResponse
	 */
	private void init(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
		zeusRunner.init(servletRequest, servletResponse);
	}

	/**
	 * sets error context info and executes "error" filters
	 *
	 * @param e
	 */
	private void error(ZeusException e) {
		Transaction tran = Cat.getProducer().newTransaction("ZeusCallable", "errorRoute");
		try {
			RequestContext.getCurrentContext().setThrowable(e);
			zeusRunner.error();
			tran.setStatus(Transaction.SUCCESS);
		} catch (Throwable t) {
			Cat.logError(t);
		} finally {
			tran.complete();
			Cat.logError(e);
		}
	}

	private void reportStat(RequestContext zeusContext, long start) {

		long remoteServiceCost = 0l;
		Object remoteCallCost = zeusContext.get("remoteCallCost");
		if (remoteCallCost != null) {
			try {
				remoteServiceCost = Long.parseLong(remoteCallCost.toString());
			} catch (Exception ignore) {
			}
		}

		long replyClientCost = 0l;
		Object sendResponseCost = zeusContext.get("sendResponseCost");
		if (sendResponseCost != null) {
			try {
				replyClientCost = Long.parseLong(sendResponseCost.toString());
			} catch (Exception ignore) {
			}
		}

		long replyClientReadCost = 0L;
		Object sendResponseReadCost = zeusContext.get("sendResponseCost:read");
		if (sendResponseReadCost != null) {
			try {
				replyClientReadCost = Long.parseLong(sendResponseReadCost.toString());
			} catch (Exception ignore) {
			}
		}

		long replyClientWriteCost = 0L;
		Object sendResponseWriteCost = zeusContext.get("sendResponseCost:write");
		if (sendResponseWriteCost != null) {
			try {
				replyClientWriteCost = Long.parseLong(sendResponseWriteCost.toString());
			} catch (Exception ignore) {
			}
		}

		if (zeusContext.sendZeusResponse()) {
			URL routeUrl = zeusContext.getRouteUrl();
			if (routeUrl == null) {
				LOGGER.warn("Unknown Route: [ {" + zeusContext.getRequest().getRequestURL() + "} ]");
			}
		}

		// TODO report metrics
	}
}
