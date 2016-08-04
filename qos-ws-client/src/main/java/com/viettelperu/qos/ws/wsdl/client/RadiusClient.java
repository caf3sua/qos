/*
 * Copyright (c) 2016 Bitel Peru and/or its affiliates. All rights reserved.
 */
package com.viettelperu.qos.ws.wsdl.client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.viettelperu.qos.ws.wsdl.radius.GetMSISDN;
import com.viettelperu.qos.ws.wsdl.radius.GetMSISDNResponse;
import com.viettelperu.qos.ws.wsdl.radius.ResultResponse;
import com.viettelperu.qos.ws.wsdl.util.WsConstants;

/**
 * Radius web service.
 * 
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 *
 */
public class RadiusClient extends WebServiceGatewaySupport {

	/** Log instance */
	private static final Logger log = LoggerFactory.getLogger(RadiusClient.class);
	
	/**
	 * Constructor method
	 * 
	 * Set default UIR, context path and mar/unmarshaller object
	 */
	public RadiusClient() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.viettelperu.qos.ws.wsdl.radius");
		
		super.setDefaultUri(WsConstants.WS_RADIUS_ENDPOINT);
		super.setMarshaller(marshaller);
		super.setUnmarshaller(marshaller);
	}

	/**
	 * Get ISDN (phone number) by IP Address
	 * 
	 * @param wsUsername username to access Web service
	 * @param wsPassword password to access Web service
	 * @param ip IP Address to lookup ISDN
	 * @return GetMSISDNResponse with code as below
	 * 		0	Success
	 * 		1	ErrorCode ISDN not found from IP
	 * 		2	Can not access/Fail authentication
	 * 		3	Wrong IP format
	 * 		4	Error when lookup IP pool (tecnical code)
	 * 		100	ErrorCode undefined.
	 */
	public GetMSISDNResponse getMSISDN(String wsUsername, String wsPassword, String ip) {
		log.info(String.format("Start RadiusGW_getMSISDN: Param[wsUsername=%s,wsPassword=%s,ip=%s]", wsUsername, wsPassword, ip));
		
		GetMSISDN request = new GetMSISDN();
		request.setUsername(wsUsername);
		request.setPassword(wsPassword);
		request.setIp(ip);
		
		GetMSISDNResponse response = (GetMSISDNResponse) getWebServiceTemplate()
				.marshalSendAndReceive(
						this.getDefaultUri(),
						request,
						new SoapActionCallback(this.getDefaultUri() + "/GetMSISDN"));

		log.info(String.format("End RadiusGW_getMSISDN: result=[%s]", printGetMSISDNResponse(response)));
		return response;
	}

	/**
	 * Print response of GetMSISDN as example
	 * 
	 * @param response
   	 * <getMSISDNResponse xmlns="http://viettel.com/xsd">
     *   <return>
     *       <code>0</code>
     *       <desc>931999896</desc>
     *    </return>
     *  </getMSISDNResponse>
	 * @return
	 */
	public String printGetMSISDNResponse(GetMSISDNResponse response) {

		ResultResponse resultResponse = response.getReturn();

		if (null != resultResponse) {
			return String.format("code=%s,desc=%s", response.getReturn().getCode(), response.getReturn().getDesc());
		} else {
			return "No data received";
		}
	}

}