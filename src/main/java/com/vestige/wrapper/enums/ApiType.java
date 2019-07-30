package com.vestige.wrapper.enums;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

public enum ApiType {
	REST("REST"), SOAP("SOAP");

	private String apiName;

	private ApiType(String apiName) {
		this.apiName = apiName;
	}

	private static Map<String, ApiType> map = new HashMap<>();

	static {
		for (ApiType apiType : ApiType.values()) {
			 map.put(apiType.getApiName(), apiType);
		}
	}

	public String getApiName() {
		return apiName;
	}

	public static ApiType getApiType(String apiName){
		return StringUtils.isEmpty(apiName) ? null : map.get(apiName.toUpperCase());
	}
}
