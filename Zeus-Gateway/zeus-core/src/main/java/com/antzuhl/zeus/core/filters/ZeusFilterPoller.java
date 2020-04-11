package com.antzuhl.zeus.core.filters;

import com.antzuhl.zeus.core.common.Constants;
import com.antzuhl.zeus.core.common.FilterInfo;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.google.common.collect.Maps;
import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicLongProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ZeusFilterPoller {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZeusFilterPoller.class);

	private Map<String, FilterInfo> runningFilters = Maps.newHashMap();

	private DynamicBooleanProperty pollerEnabled = DynamicPropertyFactory.getInstance()
			.getBooleanProperty(Constants.ZEUS_FILTER_POLLER_ENABLED, true);

	private DynamicLongProperty pollerInterval = DynamicPropertyFactory.getInstance()
			.getLongProperty(Constants.ZEUS_FILTER_POLLER_INTERVAL, 30000);

	private DynamicBooleanProperty active = DynamicPropertyFactory.getInstance()
			.getBooleanProperty(Constants.ZEUS_USE_ACTIVE_FILTERS, true);
	private DynamicBooleanProperty canary = DynamicPropertyFactory.getInstance()
			.getBooleanProperty(Constants.ZEUS_USE_CANARY_FILTERS, false);

	private DynamicStringProperty preFiltersPath = DynamicPropertyFactory.getInstance()
			.getStringProperty(Constants.ZEUS_FILTER_PRE_PATH, null);
	private DynamicStringProperty routeFiltersPath = DynamicPropertyFactory.getInstance()
			.getStringProperty(Constants.ZEUS_FILTER_ROUTE_PATH, null);
	private DynamicStringProperty postFiltersPath = DynamicPropertyFactory.getInstance()
			.getStringProperty(Constants.ZEUS_FILTER_POST_PATH, null);
	private DynamicStringProperty errorFiltersPath = DynamicPropertyFactory.getInstance()
			.getStringProperty(Constants.ZEUS_FILTER_ERROR_PATH, null);
	private DynamicStringProperty customFiltersPath = DynamicPropertyFactory.getInstance()
			.getStringProperty(Constants.ZEUS_FILTER_CUSTOM_PATH, null);

	private static ZeusFilterPoller instance = null;

	private volatile boolean running = true;

	private Thread checherThread = new Thread("ZeusFilterPoller") {

		public void run() {
			while (running) {
				try {
					if (!pollerEnabled.get())
						continue;
					if (canary.get()) {
						Transaction tran = Cat.getProducer().newTransaction("FilterPoller", "canary-"+ZeusFilterDaoFactory.getCurrentType());
						
						try{
							Map<String, FilterInfo> filterSet = Maps.newHashMap();
	
							List<FilterInfo> activeScripts = ZeusFilterDaoFactory.getZeusFilterDao().getAllActiveFilters();
	
							if (!activeScripts.isEmpty()) {
								for (FilterInfo filterInfo : activeScripts) {
									filterSet.put(filterInfo.getFilterId(), filterInfo);
								}
							}
	
							List<FilterInfo> canaryScripts = ZeusFilterDaoFactory.getZeusFilterDao().getAllCanaryFilters();
							if (!canaryScripts.isEmpty()) {
								for (FilterInfo filterInfo : canaryScripts) {
									filterSet.put(filterInfo.getFilterId(), filterInfo);
								}
							}
	
							for (FilterInfo filterInfo : filterSet.values()) {
								doFilterCheck(filterInfo);
							}
							tran.setStatus(Transaction.SUCCESS);
						}catch(Throwable t){
							tran.setStatus(t);
							Cat.logError(t);
						}finally{
							tran.complete();
						}
					} else if (active.get()) {
						Transaction tran = Cat.getProducer().newTransaction("FilterPoller", "active-"+ZeusFilterDaoFactory.getCurrentType());
						
						try{
							List<FilterInfo> newFilters = ZeusFilterDaoFactory.getZeusFilterDao().getAllActiveFilters();
							
							tran.setStatus(Transaction.SUCCESS);
							if (newFilters.isEmpty())
								continue;
							for (FilterInfo newFilter : newFilters) {
								doFilterCheck(newFilter);
							}
						}catch(Throwable t){
							tran.setStatus(t);
							Cat.logError(t);
						}finally{
							tran.complete();
						}
					}
				} catch (Throwable t) {
					LOGGER.error("ZeusFilterPoller run error!", t);
				} finally {
					try {
						sleep(pollerInterval.get());
					} catch (InterruptedException e) {
						LOGGER.error("ZeusFilterPoller sleep error!", e);
					}
				}
			}
		}
	};
	
	private ZeusFilterPoller(){

		this.checherThread.start();
	}
	
	
	public static void start(){
		if(instance == null){
			synchronized(ZeusFilterPoller.class){
				if(instance == null){
					instance = new ZeusFilterPoller() ;
				}
			}
		}
	}
	
	public static ZeusFilterPoller getInstance(){
		return instance;
	}

	public void stop(){
		this.running = false;
	}
	private void doFilterCheck(FilterInfo newFilter) throws IOException {
		FilterInfo existFilter = runningFilters.get(newFilter.getFilterId());
		if (existFilter == null || !existFilter.equals(newFilter)) {
			LOGGER.info("adding filter to disk" + newFilter.toString());
			writeFilterToDisk(newFilter);
			runningFilters.put(newFilter.getFilterId(), newFilter);
		}
	}

	private void writeFilterToDisk(FilterInfo newFilter) throws IOException {
		String filterType = newFilter.getFilterType();

		String path = preFiltersPath.get();
		if (filterType.equals("post")) {
			path = postFiltersPath.get();
		} else if (filterType.equals("route")) {
			path = routeFiltersPath.get();
		} else if (filterType.equals("error")) {
			path = errorFiltersPath.get();
		} else if (!filterType.equals("pre") && customFiltersPath.get() != null) {
			path = customFiltersPath.get();
		}

		File f = new File(path, newFilter.getFilterName() + ".groovy");
		FileWriter file = new FileWriter(f);
		BufferedWriter out = new BufferedWriter(file);
		out.write(newFilter.getFilterCode());
		out.close();
		file.close();
		LOGGER.info("filter written " + f.getPath());
	}
}
