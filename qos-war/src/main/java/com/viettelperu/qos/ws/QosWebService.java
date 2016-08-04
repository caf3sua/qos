package com.viettelperu.qos.ws;

import com.viettelperu.qos.ws.wsdl.radius.GetMSISDNResponse;
import com.viettelperu.qos.ws.wsdl.vascm.CheckSubExistsActiveResponse;
import com.viettelperu.qos.ws.wsdl.vascm.ViewSubscriberByIsdnResponse;
import com.viettelperu.qos.ws.wsdl.weather.GetCityForecastByZIPResponse;

public interface QosWebService {

    public void hello();
    
    public GetCityForecastByZIPResponse getCityForecastByZip(String zipCode);
    
    /**
     * Get phone number from IP Address
     * 
     * @param username
     * @param password
     * @param ip
     * @return
     */
    public GetMSISDNResponse getMSISDN(String username, String password, String ip);
    
    /**
     * Check exist of phone number
     * @param wsUsername
     * @param wsPassword
     * @param isdn
     * @return
     */
    public CheckSubExistsActiveResponse checkSubExistsActive(String wsUsername, String wsPassword, String isdn);
    
    /**
     * View information of user by phone number
     * @param wsUsername
     * @param wsPassword
     * @param isdn
     * @return
     */
    public ViewSubscriberByIsdnResponse viewSubscriberByIsdn(String wsUsername, String wsPassword, String isdn);
}
