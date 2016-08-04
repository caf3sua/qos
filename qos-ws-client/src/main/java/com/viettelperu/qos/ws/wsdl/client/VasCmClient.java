/*
 * Copyright (c) 2016 Bitel Peru and/or its affiliates. All rights reserved.
 */
package com.viettelperu.qos.ws.wsdl.client;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.viettelperu.qos.ws.wsdl.util.WsConstants;
import com.viettelperu.qos.ws.wsdl.vascm.CheckSubExistsActive;
import com.viettelperu.qos.ws.wsdl.vascm.CheckSubExistsActiveResponse;
import com.viettelperu.qos.ws.wsdl.vascm.Customer;
import com.viettelperu.qos.ws.wsdl.vascm.ViewSubscriberByIsdnResponse;
import com.viettelperu.qos.ws.wsdl.vascm.WsRequest;
import com.viettelperu.qos.ws.wsdl.vascm.WsResponse;

/**
 * VasCm web service.
 * 
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 *
 */
public class VasCmClient extends WebServiceGatewaySupport {

	/** Log instance */
	private static final Logger log = LoggerFactory.getLogger(VasCmClient.class);

	/**
	 * Constructor method
	 * 
	 * Set default UIR, context path and mar/unmarshaller object
	 */
	public VasCmClient() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.viettelperu.qos.ws.wsdl.vascm");
		
		super.setDefaultUri(WsConstants.WS_VASCM_ENDPOINT);
		super.setMarshaller(marshaller);
		super.setUnmarshaller(marshaller);
	}
	
	/**
	 * Check exist phone number
	 * 
	 * @param wsUsername username to access Web service
	 * @param wsPassword password to access Web service
	 * @param isdn Phonenumber need check (split by ,)
	 * @return CheckSubExistsActiveResponse with code as below
	 * 	 	0						Fail
	 * 		1						Success
	 * 		RC_AUTHENTICATE_FAIL	Can not access/Fail authentication
	 */
	public CheckSubExistsActiveResponse checkSubExistsActive(String wsUsername, String wsPassword, String isdn) {
		log.info(String.format("Start VASCM_checkSubExistsActive: Param[wsUsername=%s,wsPassword=%s,isdn=%s]", wsUsername, wsPassword, isdn));
		
		// Set request
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

		log.info(String.format("End VASCM_checkSubExistsActive: result=[%s]", printCheckSubExistsActiveResponse(response)));
		return response;
	}

	/**
	 * Get information of phone number
	 * 
	 * @param wsUsername username to access Web service
	 * @param wsPassword password to access Web service
	 * @param isdn Phonenumber need check
	 * @return ViewSubscriberByIsdnResponse
	 */
	public ViewSubscriberByIsdnResponse viewSubscriberByIsdn(String wsUsername, String wsPassword, String isdn) {
		log.info(String.format("Start VASCM_viewSubscriberByIsdn: Param[wsUsername=%s,wsPassword=%s,isdn=%s]", wsUsername, wsPassword, isdn));
		
		// Set request
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

		log.info(String.format("End VASCM_viewSubscriberByIsdn: result=[%s]", printViewSubscriberByIsdnResponse(response)));
		return response;
	}
	
	/**
	 * Print response of GetMSISDN as example
	 * 
	 * @param response
   	 * <ns2:checkSubExistsActiveResponse xmlns:ns2="http://ws.register.cm.viettel.com/">
     *    <return>
     *       <code>1</code>
     *       <message/>
     *       <returnValue>,930192778,930205723,930421822,930429591,930630555,930660999,931999895</returnValue>
     *    </return>
     * </ns2:checkSubExistsActiveResponse>
	 * @return
	 */
	public String printCheckSubExistsActiveResponse(CheckSubExistsActiveResponse response) {

		WsResponse resultResponse = response.getReturn();

		if (null != resultResponse) {
			return String.format("code=%s,message=%s,desc=%s", resultResponse.getCode(), resultResponse.getMessage(), resultResponse.getReturnValue());
		} else {
			return "No data received";
		}
	}

	/**
	 * Print response of GetMSISDN as example
	 * 
	 * @param response
   	 * <ns2:viewSubscriberByIsdnResponse xmlns:ns2="http://ws.register.cm.viettel.com/">
     *    <return>
     *       <code>1</code>
     *       <lstWSSubscriber>
     *          <custId>7079706</custId>
     *          <firstName>Thinhdd41</firstName>
     *          <isdn>931999896</isdn>
     *          <lastName>null null</lastName>
     *          <serviceType>1</serviceType>
     *          <startDatetime>16/04/2015</startDatetime>
     *          <status>2</status>
     *          <telecomService>M</telecomService>
     *       </lstWSSubscriber>
     *       <message/>
     *    </return>
     * </ns2:viewSubscriberByIsdnResponse>
	 * @return
	 */
	public String printViewSubscriberByIsdnResponse(ViewSubscriberByIsdnResponse response) {

		WsResponse resultResponse = response.getReturn();

		if (null != resultResponse) {
			List<Customer> lstCustomer = resultResponse.getLstCustomer();
			String code = resultResponse.getCode();
			
			StringBuffer buffer = new StringBuffer();
			buffer.append("code=" + code);
			buffer.append("\n");
			
			for (Customer customer : lstCustomer) {
				String customOut = String.format("custId=%s,firstName=%s,mobileNo=%s,lastName=%s,busType=%s,startDatetime=%s,status=%d,taxType=%s" 
						, customer.getCustId(), customer.getName(), customer.getMobileNo()
						, customer.getFullName(), customer.getBusType(), customer.getAddedDate()
						, customer.getStatus(), customer.getTaxType());
				buffer.append("{" + customOut + "}");
				buffer.append("\n");
			}
			String out = buffer.toString();
			// Remove last \n char
			return out.substring(0, out.length() - 2);
		} else {
			return "No data received";
		}
	}
}