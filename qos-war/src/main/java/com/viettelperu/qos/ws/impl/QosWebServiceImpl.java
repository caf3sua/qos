package com.viettelperu.qos.ws.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.viettelperu.qos.ws.QosWebService;
import com.viettelperu.qos.ws.wsdl.client.RadiusClient;
import com.viettelperu.qos.ws.wsdl.client.VasCmClient;
import com.viettelperu.qos.ws.wsdl.client.WeatherClient;
import com.viettelperu.qos.ws.wsdl.radius.GetMSISDNResponse;
import com.viettelperu.qos.ws.wsdl.vascm.CheckSubExistsActiveResponse;
import com.viettelperu.qos.ws.wsdl.vascm.ViewSubscriberByIsdnResponse;
import com.viettelperu.qos.ws.wsdl.weather.GetCityForecastByZIPResponse;

@Component
public class QosWebServiceImpl implements QosWebService {
	private static final Logger log = LoggerFactory.getLogger(QosWebServiceImpl.class);
	
	private WeatherClient weatherClient;
	private RadiusClient radiusClient;
	private VasCmClient vasCmClient;
	
	public QosWebServiceImpl() {
		// Init weather client
		weatherClient = new WeatherClient();
		
		// Init radius client
		radiusClient = new RadiusClient();
		
		// Init VasCm client
		vasCmClient = new VasCmClient();
	}
	
    public void hello() {
    	System.out.println("QosWebServiceImpl test");
    }

	@Override
	public GetCityForecastByZIPResponse getCityForecastByZip(String zipCode) {
		log.debug("START method getCityForecastByZip, zipcodes: " + zipCode);
		
		GetCityForecastByZIPResponse response = weatherClient.getCityForecastByZip(zipCode);
		System.out.println("response" + response);
		
		log.debug("END method getCityForecastByZip");
		return response;
	}

	@Override
	public GetMSISDNResponse getMSISDN(String username, String password, String ip) {
		log.debug("START method getMSISDN, username: " + username);
		GetMSISDNResponse responseSoap = radiusClient.getMSISDN(username, password, ip);
		System.out.println(responseSoap);
		log.debug("END method getMSISDN");
		return responseSoap;
	}

	@Override
	public CheckSubExistsActiveResponse checkSubExistsActive(String wsUsername, String wsPassword, String isdn) {
		log.debug("START method checkSubExistsActive, isdn: " + isdn);
		CheckSubExistsActiveResponse responseSoap = vasCmClient.checkSubExistsActive(wsUsername, wsPassword, isdn);
		System.out.println(responseSoap);
		log.debug("END method checkSubExistsActive");
		return responseSoap;
	}

	@Override
	public ViewSubscriberByIsdnResponse viewSubscriberByIsdn(String wsUsername, String wsPassword, String isdn) {
		log.debug("START method viewSubscriberByIsdn, isdn: " + isdn);
		ViewSubscriberByIsdnResponse responseSoap = vasCmClient.viewSubscriberByIsdn(wsUsername, wsPassword, isdn);
		System.out.println(responseSoap);
		log.debug("END method viewSubscriberByIsdn");
		return responseSoap;
	}
}
