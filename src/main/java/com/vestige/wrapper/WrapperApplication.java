package com.vestige.wrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.vestige.soapapi.service.VestigeSoapService;
import com.vestige.soapapi.service.VestigeSoapTrainingService;
import com.vestige.soapapi.service.impl.VestigeSoapServiceImpl;
import com.vestige.soapapi.service.impl.VestigeSoapTrainingServiceImpl;

/***
 * 
 * @author sohan.maurya
 *
 */
@SpringBootApplication
public class WrapperApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(WrapperApplication.class, args);
	}
																													
	@Bean(name = "restTemplate")
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean(name = "vestigeSoapService")
	public VestigeSoapService getVestigeSoapService() {
		return new VestigeSoapServiceImpl();
	}
	
	@Bean(name = "VestigeSoapTrainingService")
	public VestigeSoapTrainingService getVestigeSoapTrainingService() {
		return new VestigeSoapTrainingServiceImpl();
	}

}
