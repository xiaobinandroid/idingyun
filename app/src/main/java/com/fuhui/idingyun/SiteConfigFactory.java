package com.fuhui.idingyun;

import java.util.HashMap;
import java.util.Map;

public class SiteConfigFactory {
	
	
	private static final Map<String, String> map=new HashMap<String, String>();

	
	public static String setParam(String key, String value){
		return map.put(key, value);
	}
	
	public static String getValue(String key){
		return map.get(key);
	}
	
	
	
}
