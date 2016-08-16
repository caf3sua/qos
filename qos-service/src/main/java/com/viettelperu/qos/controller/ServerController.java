package com.viettelperu.qos.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viettelperu.qos.framework.api.APIResponse;
import com.viettelperu.qos.framework.controller.BaseController;
import com.viettelperu.qos.model.dto.DataDTO;
import com.viettelperu.qos.util.EncryptionUtil;

/**
 * Server creation and get APIs
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
@Controller
@RequestMapping("server")
public class ServerController extends BaseController {
    private static Logger LOG = LoggerFactory.getLogger(ServerController.class);

    @Autowired
    private Environment env;
    
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
    
    /**
     * Method to get the category by given id
     * GET
     *
     * @param catId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/encrypt", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    APIResponse encrypt(@RequestBody DataDTO data) throws Exception {
    	LOG.debug("START encrypt for data:" + data);
    	// Check key
    	
    	String key = env.getProperty("secret_key");
    	if (!StringUtils.equals(data.getKey(), key)) {
    		return APIResponse.toErrorResponse("Missing or not compare key");
    	}
    	String dataEncrypt = EncryptionUtil.encrypt(data.getData());
    	
        return APIResponse.toOkResponse(dataEncrypt);
    }
}