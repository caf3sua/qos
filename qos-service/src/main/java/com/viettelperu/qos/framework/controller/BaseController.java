package com.viettelperu.qos.framework.controller;

import java.io.IOException;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

/**
 * All controllers in spring should extend this controller so as to have
 * centralize control for doing any sort of common functionality.
 * e.g. extracting data from post request body
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public abstract class BaseController {
    protected static final String JSON_API_CONTENT_HEADER = "Content-type=application/json";

    public String extractPostRequestBody(HttpServletRequest request) throws IOException {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            Scanner s = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
        return "";
    }

    public JSONObject parseJSON(String object) {
        return new JSONObject(object);
    }
}
