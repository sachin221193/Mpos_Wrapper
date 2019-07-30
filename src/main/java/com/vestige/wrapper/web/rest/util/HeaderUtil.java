package com.vestige.wrapper.web.rest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

public class HeaderUtil {

	private static final Logger log = LoggerFactory.getLogger(HeaderUtil.class);

	private static final String APPLICATION_NAME = "wrapperApp";

	private HeaderUtil() {
	}

	public static HttpHeaders createFailureAlert(String entityName, String errorKey, String message) {

		log.error("Entity processing failed, {}", message);
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-" + APPLICATION_NAME + "-error", "error." + errorKey);
		headers.add("X-" + APPLICATION_NAME + "-params", entityName);
		return headers;
	}

}
