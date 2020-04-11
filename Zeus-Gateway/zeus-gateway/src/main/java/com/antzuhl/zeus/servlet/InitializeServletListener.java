package com.antzuhl.zeus.servlet;

import com.antzuhl.zeus.core.common.Constants;
import com.antzuhl.zeus.core.core.FilterFileManager;
import com.antzuhl.zeus.core.core.FilterLoader;
import com.antzuhl.zeus.core.core.LogConfigurator;
import com.antzuhl.zeus.core.filters.ZeusFilterPoller;
import com.antzuhl.zeus.core.groovy.GroovyCompiler;
import com.antzuhl.zeus.core.groovy.GroovyFileFilter;
import com.antzuhl.zeus.core.monitoring.*;
import com.antzuhl.zeus.core.util.IPUtil;
import com.antzuhl.zeus.tool.InfoBoard;
import com.netflix.appinfo.*;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryManager;
import com.netflix.servo.util.ThreadCpuStats;
import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InitializeServletListener implements ServletContextListener {

	private Logger LOGGER = LoggerFactory.getLogger(InitializeServletListener.class);
	private String appName = null;

	private LogConfigurator logConfigurator;
	private InfoBoard internalsServer;
	
	public InitializeServletListener() {
//		System.setProperty(Constants.DEPLOY_ENVIRONMENT, "test");
//		System.setProperty(Constants.DEPLOYMENT_APPLICATION_ID, "mobile_zeus");
//		System.setProperty(Constants.DEPLOY_CONFIG_URL, "http://localhost:8080/configfiles/mobile_zeus/default/application");
		String applicationID = ConfigurationManager.getConfigInstance().getString(Constants.DEPLOYMENT_APPLICATION_ID);
		if (StringUtils.isEmpty(applicationID)) {
			LOGGER.warn("Using default config!");
			ConfigurationManager.getConfigInstance().setProperty(Constants.DEPLOYMENT_APPLICATION_ID, "mobile_zeus");
		}
		
		System.setProperty(DynamicPropertyFactory.ENABLE_JMX, "true");
		
        loadConfiguration();
        configLog();
        registerEureka();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
        try {
        	initInfoBoard();
            initMonitor();
            initZeus();
            updateInstanceStatusToEureka();
        } catch (Exception e) {
        	LOGGER.error("Error while initializing zeus gateway.", e);
        	throw new RuntimeException(e);
        }
	}
	
	private void updateInstanceStatusToEureka() {
		DynamicBooleanProperty eurekaEnabled = DynamicPropertyFactory.getInstance().getBooleanProperty("eureka.enabled",
				true);
		if (!eurekaEnabled.get()) return;
        ApplicationInfoManager.getInstance().setInstanceStatus(InstanceInfo.InstanceStatus.UP);
	}
	
	private void initInfoBoard() {
    	internalsServer = new InfoBoard(appName,ConfigurationManager.getConfigInstance().getInt("server.internals.port", 8077));
        internalsServer.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		FilterFileManager.shutdown();
	}

	private void initMonitor() {
		LOGGER.info("Registering Servo Monitor");
		MonitorRegistry.getInstance().setPublisher(new ServoMonitor());

		LOGGER.info("Starting Poller");
		MetricPoller.startPoller();

		LOGGER.info("Registering Servo Tracer");
		TracerFactory.initialize(new Tracer());

		LOGGER.info("Registering Servo Counter");
		CounterFactory.initialize(new Counter());

		LOGGER.info("Starting CPU stats");
		final ThreadCpuStats stats = ThreadCpuStats.getInstance();
		stats.start();

	}
	
    private void initZeus() throws Exception {
        LOGGER.info("Starting Groovy Filter file manager");
        final AbstractConfiguration config = ConfigurationManager.getConfigInstance();
        final String preFiltersPath = config.getString(Constants.ZEUS_FILTER_PRE_PATH);
        final String postFiltersPath = config.getString(Constants.ZEUS_FILTER_POST_PATH);
        final String routeFiltersPath = config.getString(Constants.ZEUS_FILTER_ROUTE_PATH);
        final String errorFiltersPath = config.getString(Constants.ZEUS_FILTER_ERROR_PATH);
        final String customPath = config.getString(Constants.ZEUS_FILTER_CUSTOM_PATH);

        //load local filter files
        FilterLoader.getInstance().setCompiler(new GroovyCompiler());
        FilterFileManager.setFilenameFilter(new GroovyFileFilter());
        if (customPath == null) {
            FilterFileManager.init(5, preFiltersPath, postFiltersPath, routeFiltersPath, errorFiltersPath);
        } else {
            FilterFileManager.init(5, preFiltersPath, postFiltersPath, routeFiltersPath, errorFiltersPath, customPath);
        }
        //load filters in DB
		startZeusFilterPoller();
        LOGGER.info("Groovy Filter file manager started");
    }

    private void startZeusFilterPoller() {
        ZeusFilterPoller.start();
        LOGGER.info("ZeusFilterPoller Started.");
    }
    
	private void loadConfiguration() {
		appName = ConfigurationManager.getDeploymentContext().getApplicationId();

		// Loading properties via archaius.
		if (null != appName) {
			try {
				LOGGER.info(String.format("Loading application properties with app id: %s and environment: %s", appName,
						ConfigurationManager.getDeploymentContext().getDeploymentEnvironment()));
				ConfigurationManager.loadCascadedPropertiesFromResources(appName);
			} catch (IOException e) {
				LOGGER.error(String.format(
						"Failed to load properties for application id: %s and environment: %s. This is ok, if you do not have application level properties.",
						appName, ConfigurationManager.getDeploymentContext().getDeploymentEnvironment()), e);
			}
		} else {
			LOGGER.warn(
					"Application identifier not defined, skipping application level properties loading. You must set a property 'archaius.deployment.applicationId' to be able to load application level properties.");
		}

	}

	private void configLog() {
		logConfigurator = new LogConfigurator(appName,ConfigurationManager.getDeploymentContext().getDeploymentEnvironment());
		logConfigurator.config();
	}

	private void registerEureka() {
		DynamicBooleanProperty eurekaEnabled = DynamicPropertyFactory.getInstance().getBooleanProperty("eureka.enabled",
				true);
		if (!eurekaEnabled.get())
			return;

		EurekaInstanceConfig eurekaInstanceConfig = new PropertiesInstanceConfig() {
		};
        ConfigurationManager.getConfigInstance().setProperty("eureka.statusPageUrl","http://"+ getTurbineInstance());

		DiscoveryManager.getInstance().initComponent(eurekaInstanceConfig, new DefaultEurekaClientConfig());

		final DynamicStringProperty serverStatus = DynamicPropertyFactory.getInstance()
				.getStringProperty("server." + IPUtil.getLocalIP() + ".status", "up");
		DiscoveryManager.getInstance().getDiscoveryClient().registerHealthCheckCallback(new HealthCheckCallback() {
			@Override
			public boolean isHealthy() {
				return serverStatus.get().toLowerCase().equals("up");
			}
		});

		String version = String.valueOf(System.currentTimeMillis());
		String group = ConfigurationManager.getConfigInstance().getString("server.group", "default");
		String dataCenter = ConfigurationManager.getConfigInstance().getString("server.data-center", "default");

		Map<String, String> metadata = new HashMap<String, String>();
		metadata.put("version", version);
		metadata.put("group", group);
		metadata.put("dataCenter", dataCenter);

		String turbineInstance = getTurbineInstance();
		if (turbineInstance != null) {
			metadata.put("turbine.instance", turbineInstance);
		}

		ApplicationInfoManager.getInstance().registerAppMetadata(metadata);
	}

	public String getTurbineInstance() {
		String instance = null;
		String ip = IPUtil.getLocalIP();
		if (ip != null) {
			instance = ip + ":" + ConfigurationManager.getConfigInstance().getString("server.internals.port", "8077");
		} else {
			LOGGER.warn("Can't build turbine instance as can't fetch the ip.");
		}
		return instance;
	}
}

