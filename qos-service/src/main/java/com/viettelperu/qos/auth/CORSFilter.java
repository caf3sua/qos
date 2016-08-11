package com.viettelperu.qos.auth;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * If required. not used here.
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter {

//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) res;
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        if (request.getMethod() != "OPTIONS") {
//            chain.doFilter(req, res);
//        } else {
//        }
//    }

    @Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
			ServletException {
		HttpServletResponse httpResp = (HttpServletResponse) resp;
		HttpServletRequest httpReq = (HttpServletRequest) req;

		httpResp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS");
		httpResp.setHeader("Access-Control-Allow-Origin", "*");
		if (httpReq.getMethod().equalsIgnoreCase("OPTIONS")) {
			httpResp.setHeader("Access-Control-Allow-Headers",
					httpReq.getHeader("Access-Control-Request-Headers"));
		}
		chain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
