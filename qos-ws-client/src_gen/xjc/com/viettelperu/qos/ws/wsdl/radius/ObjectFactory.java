//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.01 at 10:46:27 PM ICT 
//


package com.viettelperu.qos.ws.wsdl.radius;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.viettelperu.qos.ws.wsdl.radius package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ResultResponse_QNAME = new QName("http://viettel.com/xsd", "ResultResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.viettelperu.qos.ws.wsdl.radius
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetMSISDN }
     * 
     */
    public GetMSISDN createGetMSISDN() {
        return new GetMSISDN();
    }

    /**
     * Create an instance of {@link GetMSISDNResponse }
     * 
     */
    public GetMSISDNResponse createGetMSISDNResponse() {
        return new GetMSISDNResponse();
    }

    /**
     * Create an instance of {@link ResultResponse }
     * 
     */
    public ResultResponse createResultResponse() {
        return new ResultResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://viettel.com/xsd", name = "ResultResponse")
    public JAXBElement<ResultResponse> createResultResponse(ResultResponse value) {
        return new JAXBElement<ResultResponse>(_ResultResponse_QNAME, ResultResponse.class, null, value);
    }

}