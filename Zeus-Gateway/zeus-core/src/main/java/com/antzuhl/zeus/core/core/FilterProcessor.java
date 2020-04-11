package com.antzuhl.zeus.core.core;

import com.antzuhl.zeus.core.common.ExecutionStatus;
import com.antzuhl.zeus.core.common.IFilterUsageNotifier;
import com.antzuhl.zeus.core.common.ZeusException;
import com.antzuhl.zeus.core.common.ZeusFilterResult;
import com.antzuhl.zeus.core.context.RequestContext;
import com.antzuhl.zeus.core.filters.ZeusFilter;
import com.antzuhl.zeus.core.util.Debug;
import com.netflix.servo.monitor.DynamicCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FilterProcessor {
	private static final Logger LOGGER = LoggerFactory.getLogger(FilterProcessor.class);

	private static FilterProcessor instance = new FilterProcessor();
	
    private IFilterUsageNotifier usageNotifier;

	protected FilterProcessor() {
		usageNotifier = new BasicFilterUsageNotifier();
	}

	/**
	 * @return the singleton FilterProcessor
	 */
	public static FilterProcessor getInstance() {
		return instance;
	}

	/**
	 * sets a singleton processor in case of a need to override default behavior
	 *
	 * @param processor
	 */
	public static void setProcessor(FilterProcessor processor) {
		instance = processor;
	}
	
    /**
     * Override the default filter usage notification impl.
     *
     * @param notifier
     */
    public void setFilterUsageNotifier(IFilterUsageNotifier notifier) {
        this.usageNotifier = notifier;
    }


	/**
	 * runs "post" filters which are called after "route" filters.
	 * ZeusExceptions from ZeusFilters are thrown. Any other Throwables are
	 * caught and a ZeusException is thrown out with a 500 status code
	 *
	 * @throws ZeusException
	 */
	public void postRoute() throws ZeusException {
		try {
			runFilters("post");
		} catch (Throwable e) {
			if (e instanceof ZeusException) {
				throw (ZeusException) e;
			}
			throw new ZeusException(e, 500, "UNCAUGHT_EXCEPTION_IN_POST_FILTER_" + e.getClass().getName());
		}

	}

	/**
	 * runs all "error" filters. These are called only if an exception occurs.
	 * Exceptions from this are swallowed and logged so as not to bubble up.
	 * @throws ZeusException
	 */
	public void error() throws ZeusException {
		try {
			runFilters("error");
		} catch (Throwable e) {
			if (e instanceof ZeusException) {
				throw (ZeusException) e;
			}
			throw new ZeusException(e, 500, "UNCAUGHT_EXCEPTION_IN_POST_FILTER_" + e.getClass().getName());
		}
	}

	/**
	 * Runs all "route" filters. These filters route calls to an origin.
	 *
	 * @throws ZeusException
	 *             if an exception occurs.
	 */
	public void route() throws ZeusException {
		try {
			runFilters("route");
		} catch (Throwable e) {
			if (e instanceof ZeusException) {
				throw (ZeusException) e;
			}
			throw new ZeusException(e, 500, "UNCAUGHT_EXCEPTION_IN_ROUTE_FILTER_" + e.getClass().getName());
		}
	}

	/**
	 * runs all "pre" filters. These filters are run before routing to the
	 * orgin.
	 *
	 * @throws ZeusException
	 */
	public void preRoute() throws ZeusException {
		try {
			runFilters("pre");
		} catch (Throwable e) {
			if (e instanceof ZeusException) {
				throw (ZeusException) e;
			}
			throw new ZeusException(e, 500, "UNCAUGHT_EXCEPTION_IN_PRE_FILTER_" + e.getClass().getName());
		}
	}

	/**
	 * runs all filters of the filterType sType/ Use this method within filters
	 * to run custom filters by type
	 *
	 * @param sType
	 *            the filterType.
	 * @return
	 * @throws Throwable
	 *             throws up an arbitrary exception
	 */
	public Object runFilters(String sType) throws Throwable {
		if (RequestContext.getCurrentContext().debugRouting()) {
			Debug.addRoutingDebug("Invoking {" + sType + "} type filters");
		}
		boolean bResult = false;
		List<ZeusFilter> list = FilterLoader.getInstance().getFiltersByType(sType);
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				ZeusFilter zeusFilter = list.get(i);
				Object result = processZeusFilter(zeusFilter);
				if (result != null && result instanceof Boolean) {
					bResult |= ((Boolean) result);
				}
			}
		}
		return bResult;
	}

	/**
	 * Processes an individual ZeusFilter. This method adds Debug information.
	 * Any uncaught Thowables are caught by this method and converted to a
	 * ZeusException with a 500 status code.
	 *
	 * @param filter
	 * @return the return value for that filter
	 * @throws ZeusException
	 */
	public Object processZeusFilter(ZeusFilter filter) throws ZeusException {
		RequestContext ctx = RequestContext.getCurrentContext();
		boolean bDebug = ctx.debugRouting();
		long execTime = 0;
		String filterName = "";

		try {
			long ltime = System.currentTimeMillis();
			filterName = filter.getClass().getSimpleName();

			RequestContext copy = null;
			Object o = null;
			Throwable t = null;

			if (bDebug) {
				Debug.addRoutingDebug("Filter " + filter.filterType() + " " + filter.filterOrder() + " " + filterName);
				copy = ctx.copy();
			}

			ZeusFilterResult result = filter.runFilter();
			ExecutionStatus s = result.getStatus();
			execTime = System.currentTimeMillis() - ltime;

			switch (s) {
				case FAILED:
                    t = result.getException();
                    ctx.addFilterExecutionSummary(filterName, ExecutionStatus.FAILED.name(), execTime);
                    break;					
				case SUCCESS:
                    o = result.getResult();
                    ctx.addFilterExecutionSummary(filterName, ExecutionStatus.SUCCESS.name(), execTime);
                    if (bDebug) {
                        Debug.addRoutingDebug("Filter {" + filterName + " TYPE:" + filter.filterType() + " ORDER:" + filter.filterOrder() + "} Execution time = " + execTime + "ms");
                        Debug.compareContextState(filterName, copy);
                    }
					break;
				default:
					break;
			}
            if (t != null) throw t;
            
            usageNotifier.notify(filter, s);
            
            return o;
		} catch (Throwable e) {
            if (bDebug) {
                Debug.addRoutingDebug("Running Filter failed " + filterName + " type:" + filter.filterType() + " order:" + filter.filterOrder() + " " + e.getMessage());
            }
            
            usageNotifier.notify(filter, ExecutionStatus.FAILED);
            if (e instanceof ZeusException) {
                throw (ZeusException) e;
            } else {
				ZeusException ex = new ZeusException(e, "Filter threw Exception", 500, filter.filterType() + ":" + filterName);
                ctx.addFilterExecutionSummary(filterName, ExecutionStatus.FAILED.name(), execTime);
                throw ex;
            }
		}

	}
	

    /**
     * Publishes a counter metric for each filter on each use.
     */
    public static class BasicFilterUsageNotifier implements IFilterUsageNotifier {
        private static final String METRIC_PREFIX = "zeus.filter-";

        @Override
        public void notify(ZeusFilter filter, ExecutionStatus status) {
            DynamicCounter.increment(METRIC_PREFIX + filter.getClass().getSimpleName(), "status", status.name(), "filtertype", filter.filterType());
        }
    }

}

