package com.viettelperu.qos.servlet;

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */	
public class QosServletContextListener implements ServletContextListener{
	private static Logger LOG = LoggerFactory.getLogger(QosServletContextListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("QosServletContextListener destroyed");
	}

    //Run this before web application is started
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		LOG.info("Start QosServletContextListener");
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext());
		ComboPooledDataSource dataSource = (ComboPooledDataSource) context.getBean("dataSource");
		
		try {
			dataSource.getConnection();
		} catch (SQLException e) {
			LOG.error("Can not create connection database", e);
			throw new RuntimeException(e.getMessage());
		}
	}
}
