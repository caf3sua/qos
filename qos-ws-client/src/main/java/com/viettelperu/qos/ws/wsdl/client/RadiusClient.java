package com.viettelperu.qos.ws.wsdl.client;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.viettelperu.qos.ws.wsdl.radius.GetMSISDN;
import com.viettelperu.qos.ws.wsdl.radius.GetMSISDNResponse;
import com.viettelperu.qos.ws.wsdl.util.WsConstants;
import com.viettelperu.qos.ws.wsdl.weather.Forecast;
import com.viettelperu.qos.ws.wsdl.weather.ForecastReturn;
import com.viettelperu.qos.ws.wsdl.weather.GetCityForecastByZIPResponse;
import com.viettelperu.qos.ws.wsdl.weather.Temp;

public class RadiusClient extends WebServiceGatewaySupport {

	private static final Logger log = LoggerFactory.getLogger(RadiusClient.class);
	
	public RadiusClient() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.viettelperu.qos.ws.wsdl.radius");
		
		super.setDefaultUri(WsConstants.WS_RADIUS_ENDPOINT);
		super.setMarshaller(marshaller);
		super.setUnmarshaller(marshaller);
	}

	public GetMSISDNResponse getMSISDN(String username, String password, String ip) {

		GetMSISDN request = new GetMSISDN();
		request.setUsername(username);
		request.setPassword(password);
		request.setIp(ip);

		log.info("Requesting getMSISDN for username:" + username);

		GetMSISDNResponse response = (GetMSISDNResponse) getWebServiceTemplate()
				.marshalSendAndReceive(
						this.getDefaultUri(),
						request,
						new SoapActionCallback(this.getDefaultUri() + "/GetMSISDN"));

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