//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.07.13 at 08:24:16 PM ICT 
//


package com.viettelperu.qos.ws.wsdl.vascm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for checkSubExistsActive complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="checkSubExistsActive"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="wsRequest" type="{http://ws.register.cm.viettel.com/}wsRequest" minOccurs="0"/&gt;
 *         &lt;element name="wsUsername" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="wsPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "checkSubExistsActive", propOrder = {
    "wsRequest",
    "wsUsername",
    "wsPassword"
})
public class CheckSubExistsActive {

    protected WsRequest wsRequest;
    protected String wsUsername;
    protected String wsPassword;

    /**
     * Gets the value of the wsRequest property.
     * 
     * @return
     *     possible object is
     *     {@link WsRequest }
     *     
     */
    public WsRequest getWsRequest() {
        return wsRequest;
    }

    /**
     * Sets the value of the wsRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link WsRequest }
     *     
     */
    public void setWsRequest(WsRequest value) {
        this.wsRequest = value;
    }

    /**
     * Gets the value of the wsUsername property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWsUsername() {
        return wsUsername;
    }

    /**
     * Sets the value of the wsUsername property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWsUsername(String value) {
        this.wsUsername = value;
    }

    /**
     * Gets the value of the wsPassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWsPassword() {
        return wsPassword;
    }

    /**
     * Sets the value of the wsPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWsPassword(String value) {
        this.wsPassword = value;
    }

}
