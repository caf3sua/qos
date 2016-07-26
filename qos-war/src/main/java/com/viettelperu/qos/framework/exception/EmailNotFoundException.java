package com.viettelperu.qos.framework.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public class EmailNotFoundException extends AuthenticationException {
	private static final long serialVersionUID = 1L;

	public EmailNotFoundException(String message) {
        super(message);
    }
}
