package com.viettelperu.qos.ws.wsdl.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.viettelperu.qos.ws.wsdl.client.WeatherClient;
import com.viettelperu.qos.ws.wsdl.weather.GetCityForecastByZIPResponse;

public class MainClient {
	private static final Logger log = LoggerFactory.getLogger(WeatherClient.class);
	
	public static void main(String[] args) {
		
		
		String zipCode = "94304";
		log.debug("START method main, zipcodes: " + zipCode);
		
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.viettelperu.qos.ws.wsdl.weather");
		
		WeatherClient weatherClient = new WeatherClient();
		weatherClient.setDefaultUri("http://ws.cdyne.com/WeatherWS");
		weatherClient.setMarshaller(marshaller);
		weatherClient.setUnmarshaller(marshaller);
		
		GetCityForecastByZIPResponse response = weatherClient.getCityForecastByZip(zipCode);
		System.out.println("response" + response);
		
		log.debug("END method main");
	}
}
