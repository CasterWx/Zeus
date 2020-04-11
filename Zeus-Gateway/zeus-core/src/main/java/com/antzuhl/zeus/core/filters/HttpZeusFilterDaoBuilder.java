package com.antzuhl.zeus.core.filters;

import com.antzuhl.zeus.core.common.Constants;
import com.antzuhl.zeus.core.common.IZeusFilterDao;
import com.antzuhl.zeus.core.common.IZeusFilterDaoBuilder;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

public class HttpZeusFilterDaoBuilder implements IZeusFilterDaoBuilder {

	private static final DynamicStringProperty appName = DynamicPropertyFactory.getInstance()
			.getStringProperty(Constants.DEPLOYMENT_APPLICATION_ID, Constants.APPLICATION_NAME);

	public HttpZeusFilterDaoBuilder() {

	}

	@Override
	public IZeusFilterDao build() {
		return new HttpZeusFilterDao(appName.get());

	}

}
