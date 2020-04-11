package com.antzuhl.zeus.core.hystrix;

import com.antzuhl.zeus.core.common.ZeusException;
import com.google.common.collect.Lists;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.netflix.client.AbstractLoadBalancerAwareClient;
import com.netflix.client.ClientFactory;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.netflix.discovery.DiscoveryManager;
import com.netflix.discovery.shared.Application;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public abstract class RibbonZeusCommon extends HystrixCommand<HttpResponse> {

	protected String commandGroup;
	protected String commandKey;
	protected String serviceName;
	protected HttpRequest request;
	protected AbstractLoadBalancerAwareClient client;
	
	public RibbonZeusCommon(AbstractLoadBalancerAwareClient client,HttpRequest request,String serviceName, String commandGroup, String commandKey,Setter setter) {
		super(setter);
		this.commandKey = commandKey;
		this.commandGroup = commandGroup;
		this.serviceName = serviceName;
		this.request = request;
		this.client = client;
	}

	protected RestClient getRestClient() throws ZeusException {
		Application application = DiscoveryManager.getInstance().getDiscoveryClient().getApplication(serviceName);
		if (application == null) {
			throw new ZeusException( "Service-NotFoud",HttpServletResponse.SC_NOT_FOUND, serviceName + "服务未找到");
		}
		
		List<DiscoveryEnabledServer> instances = Lists.newArrayList();
		for (InstanceInfo info : application.getInstances()) {
			if (info.getStatus() == InstanceStatus.UP) {
				instances.add(new DiscoveryEnabledServer(info, false, false));
			}
		}

		RestClient client = (RestClient) ClientFactory.getNamedClient(serviceName);
		ZoneAwareLoadBalancer loadbalancer = (ZoneAwareLoadBalancer) client.getLoadBalancer();
		
//		//loadbalancer.setServersList(instances);		
//		IRule rule = new RandomRule();
//		int ruleLoad = ZeusCommandHelper.getLoadBalanceRule(commandGroup, commandKey);
//		if (ruleLoad == 2) {
//			rule = new ClientConfigEnabledRoundRobinRule();
//		} else if (ruleLoad == 3) {
//			rule=new AvailabilityFilteringRule();
//		} else if (ruleLoad == 3) {
//			rule=new ZoneAvoidanceRule();
//		} else if (ruleLoad == 4) {
//			rule=new RetryRule();
//		} else if (ruleLoad == 5) {
//			rule=new RoundRobinRule();
//		}else if (ruleLoad == 6) {
//			rule=new ResponseTimeWeightedRule();
//		}else if (ruleLoad == 7) {
//			rule=new WeightedResponseTimeRule();
//		}
//		loadbalancer.setRule(rule);
//		client.setLoadBalancer(loadbalancer);
		return client;
	}
	
	@Override
	protected HttpResponse run() throws Exception {    	
		
		HttpResponse response = (HttpResponse) client.executeWithLoadBalancer(request);
		return response;
	}
	
}
