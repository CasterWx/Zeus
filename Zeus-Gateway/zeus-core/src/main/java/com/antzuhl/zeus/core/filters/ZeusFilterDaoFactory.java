package com.antzuhl.zeus.core.filters;

import com.antzuhl.zeus.core.common.Constants;
import com.antzuhl.zeus.core.common.IZeusFilterDao;
import com.google.common.collect.Maps;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;

import java.util.concurrent.ConcurrentMap;

public class ZeusFilterDaoFactory {
    private static final DynamicStringProperty daoType = DynamicPropertyFactory.getInstance().getStringProperty(Constants.ZEUS_FILTER_DAO_TYPE, "jdbc");
    
    private static ConcurrentMap<String, IZeusFilterDao> daoCache = Maps.newConcurrentMap();

    private ZeusFilterDaoFactory(){
    	
    }
    
    public static IZeusFilterDao getZeusFilterDao(){
		IZeusFilterDao dao = daoCache.get(daoType.get());
    	
    	if(dao != null){
    		return dao;
    	}
    	
    	if("jdbc".equalsIgnoreCase(daoType.get())){
    		dao = new JDBCZeusFilterDaoBuilder().build();
    	}else if("http".equalsIgnoreCase(daoType.get())){
    		dao =  new HttpZeusFilterDaoBuilder().build();
    	}else{
    		dao =  new HttpZeusFilterDaoBuilder().build();
    	}
    	
    	daoCache.putIfAbsent(daoType.get(), dao);
    	
    	return dao;
    }
    
    public static String getCurrentType(){
    	return daoType.get();
    }
    
}
