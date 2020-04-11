package com.antzuhl.zeus.servlet;

import com.antzuhl.zeus.core.common.ZeusException;
import com.antzuhl.zeus.core.context.RequestContext;
import com.antzuhl.zeus.core.core.ZeusRunner;
import com.dianping.cat.Cat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SyncZeusServlet extends HttpServlet {
	
	private static final long serialVersionUID = -7314825620092836092L;

	private static Logger LOGGER = LoggerFactory.getLogger(SyncZeusServlet.class);
	
    private ZeusRunner zeusRunner = new ZeusRunner();

    @Override
    public void service(javax.servlet.ServletRequest req, javax.servlet.ServletResponse res) throws javax.servlet.ServletException, java.io.IOException {
        try {


            init((HttpServletRequest) req, (HttpServletResponse) res);

            // marks this request as having passed through the "Zeus engine", as opposed to servlets
            // explicitly bound in web.xml, for which requests will not have the same data attached
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
        } finally {
        	RequestContext.getCurrentContext().unset();
        }
    }

    /**
     * executes "post" ZeusFilters
     *
     * @throws ZeusException
     */
    void postRoute() throws ZeusException {
    	zeusRunner.postRoute();
    }

    /**
     * executes "route" filters
     *
     * @throws ZeusException
     */
    void route() throws ZeusException {
    	zeusRunner.route();
    }

    /**
     * executes "pre" filters
     *
     * @throws ZeusException
     */
    void preRoute() throws ZeusException {
    	zeusRunner.preRoute();
    }

    /**
     * initializes request
     *
     * @param servletRequest
     * @param servletResponse
     */
    void init(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
    	zeusRunner.init(servletRequest, servletResponse);
    }

	/**
	 * sets error context info and executes "error" filters
	 *
	 * @param e
	 * @throws ZeusException
	 */
	void error(ZeusException e) {
		try {
			RequestContext.getCurrentContext().setThrowable(e);
			zeusRunner.error();
		} catch (Throwable t) {
			Cat.logError(t);
			LOGGER.error(e.getMessage(), e);
		}finally{
			Cat.logError(e);
		}
	}

}
