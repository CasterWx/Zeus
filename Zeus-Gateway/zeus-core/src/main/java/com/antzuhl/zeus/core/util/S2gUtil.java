package com.antzuhl.zeus.core.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

public class S2gUtil {
	public static boolean contains(String[] srcTags, String[] dstTags) {
		if (dstTags == null || dstTags.length == 0)
			return true;

		if (srcTags == null || srcTags.length == 0)
			return false;

		List<String> srcTagList = Arrays.asList(srcTags);
		for (String t : dstTags) {
			if (!srcTagList.contains(t)) {
				return false;
			}
		}
		return true;
	}
	public static String trimEnd(String data,char trim){
		data=StringUtils.trim(data);		
		if (!StringUtils.isEmpty(data)&& (data.toCharArray()[data.length() - 1]) == trim) {
			data = data.substring(0, data.length() - 1);
		}
		return data;
	}
	public static String getServerletUrl(HttpServletRequest request) {
		//String url= request.getServletPath();
	    String uri = request.getRequestURI();
	    String contextPath = request.getContextPath();
	    if (StringUtils.length(contextPath) > 0) {
	        uri = StringUtils.substring(uri, contextPath.length());
	    }
	    if(StringUtils.length(uri) > 0){
	    	uri=uri.substring(1, StringUtils.length(uri));
	    }
	    String query= request.getQueryString();	
		if(!StringUtils.isEmpty(query)){
			uri+="?"+query;
		}
		return uri;
	}
	public static String[] getRoutes(HttpServletRequest request) {		
		return getServerletUrl(request).split("/");
	}
}
