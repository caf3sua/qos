package com.viettelperu.qos.ws.wsdl.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class LogbackInterceptor implements ClientInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(LogbackInterceptor.class);

	public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
		OutputStream out = new ByteArrayOutputStream();
		try {
			logger.debug("Sent request by sample [" + messageContext.getRequest() + "]");
			if (messageContext.getRequest() instanceof SaajSoapMessage) {
				((SaajSoapMessage) messageContext.getRequest()).writeTo(out);
				logger.debug("Sent request by sample [" + formatXml(out.toString()) + "]");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
		OutputStream out = new ByteArrayOutputStream();
		try {
			logger.debug("Received response by sample [" + messageContext.getResponse() + "] for request ["
					+ messageContext.getRequest() + "]");
			if (messageContext.getResponse() instanceof SaajSoapMessage) {
				((SaajSoapMessage) messageContext.getResponse()).writeTo(out);
				logger.debug("Received response [" + formatXml(out.toString()) + "]");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
		return false;
	}

	public void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException {
	}

	public String formatXml(String xml) {
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(parseXml(xml));
			transformer.transform(source, result);
			return result.getWriter().toString();
		} catch (Exception e) {
			return xml;
		}
	}

	private Document parseXml(String in) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(in));
			return db.parse(is);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}