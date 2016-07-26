package com.viettelperu.qos.ws.wsdl.client;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.viettelperu.qos.ws.wsdl.util.WsConstants;
import com.viettelperu.qos.ws.wsdl.vascm.CheckSubExistsActive;
import com.viettelperu.qos.ws.wsdl.vascm.CheckSubExistsActiveResponse;
import com.viettelperu.qos.ws.wsdl.vascm.ViewSubscriberByIsdnResponse;
import com.viettelperu.qos.ws.wsdl.vascm.WsRequest;
import com.viettelperu.qos.ws.wsdl.weather.Forecast;
import com.viettelperu.qos.ws.wsdl.weather.ForecastReturn;
import com.viettelperu.qos.ws.wsdl.weather.GetCityForecastByZIPResponse;
import com.viettelperu.qos.ws.wsdl.weather.Temp;

public class VasCmClient extends WebServiceGatewaySupport {

	private static final Logger log = LoggerFactory.getLogger(VasCmClient.class);

	public VasCmClient() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.viettelperu.qos.ws.wsdl.vascm");
		
		super.setDefaultUri(WsConstants.WS_VASCM_ENDPOINT);
		super.setMarshaller(marshaller);
		super.setUnmarshaller(marshaller);
	}
	
	public CheckSubExistsActiveResponse checkSubExistsActive(String wsUsername, String wsPassword, String isdn) {
		log.debug("START method checkSubExistsActive, isdn: " + isdn);
		CheckSubExistsActive subExistsActive = new CheckSubExistsActive();
		WsRequest wsRequest = new WsRequest();
		wsRequest.setIsdn(isdn);
		subExistsActive.setWsRequest(wsRequest);
		subExistsActive.setWsUsername(wsUsername);
		subExistsActive.setWsPassword(wsPassword);
		
		CheckSubExistsActiveResponse response = (CheckSubExistsActiveResponse) getWebServiceTemplate()
				.marshalSendAndReceive(
						this.getDefaultUri(),
						subExistsActive,
						new SoapActionCallback(this.getDefaultUri() + "/CheckSubExistsActive"));

		return response;
	}

	public ViewSubscriberByIsdnResponse viewSubscriberByIsdn(String wsUsername, String wsPassword, String isdn) {
		log.debug("START method viewSubscriberByIsdn, isdn: " + isdn);
		CheckSubExistsActive subExistsActive = new CheckSubExistsActive();
		WsRequest wsRequest = new WsRequest();
		wsRequest.setIsdn(isdn);
		subExistsActive.setWsRequest(wsRequest);
		subExistsActive.setWsUsername(wsUsername);
		subExistsActive.setWsPassword(wsPassword);
		
		ViewSubscriberByIsdnResponse response = (ViewSubscriberByIsdnResponse) getWebServiceTemplate()
				.marshalSendAndReceive(
						this.getDefaultUri(),
						subExistsActive,
						new SoapActionCallback(this.getDefaultUri() + "/ViewSubscriberByIsdn"));

		return response;
	}
	
	public void printResponse(GetCityForecastByZIPResponse response) {

		ForecastReturn forecastReturn = response.getGetCityForecastByZIPResult();

		if (forecastReturn.isSuccess()) {
			log.info("Forecast for " + forecastReturn.getCity() + ", " + forecastReturn.getState());

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			for (Forecast forecast : forecastReturn.getForecastResult().getForecast()) {

				Temp temperature = forecast.getTemperatures();

				log.info(String.format("%s %s %s°-%s°", format.format(forecast.getDate().toGregorianCalendar().getTime()),
						forecast.getDesciption(), temperature.getMorningLow(), temperature.getDaytimeHigh()));
				log.info("");
			}
		} else {
			log.info("No forecast received");
		}
	}

}