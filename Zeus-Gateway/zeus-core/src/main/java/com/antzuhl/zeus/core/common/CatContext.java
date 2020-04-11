package com.antzuhl.zeus.core.common;

import com.dianping.cat.Cat;

import java.util.HashMap;
import java.util.Map;

public class CatContext implements Cat.Context {
	private Map<String, String> properties = new HashMap<String, String>();

	@Override
	public void addProperty(String key, String value) {
		this.properties.put(key, value);
	}

	@Override
	public String getProperty(String key) {
		return this.properties.get(key);
	}

}
