package com.viettelperu.qos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viettelperu.qos.framework.api.APIResponse;
import com.viettelperu.qos.framework.controller.BaseController;

/**
 * Server creation and get APIs
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
@Controller
@RequestMapping("server")
public class ServerController extends BaseController {
    private static Logger LOG = LoggerFactory.getLogger(ServerController.class);

    /**
     * Method to get the category by given id
     * GET
     *
     * @param catId
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/ping", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody
    ResponseEntity ping() throws Exception {
    	// Generate the http headers with the file properties
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");

        APIResponse resObj = new APIResponse();
        resObj.setCode(200);
        resObj.setResult("OK");
        return new ResponseEntity<>(resObj, headers, HttpStatus.OK);
    }
}