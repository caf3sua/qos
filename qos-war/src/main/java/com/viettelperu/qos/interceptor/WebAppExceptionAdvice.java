package com.viettelperu.qos.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viettelperu.qos.framework.api.APIResponse;

/**
 * Exception Handler Controller Advice to catch all controller exceptions and respond gracefully to
 * the caller
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
@ControllerAdvice
public class WebAppExceptionAdvice {
    private static Logger LOG = LoggerFactory.getLogger(WebAppExceptionAdvice.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public APIResponse handleAnyException(Exception e) {
        LOG.error(e.getMessage());
        e.printStackTrace();
        return APIResponse.toErrorResponse(e.getMessage());
    }
}
