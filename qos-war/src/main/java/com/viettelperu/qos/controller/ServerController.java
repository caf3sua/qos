package com.viettelperu.qos.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viettelperu.qos.framework.api.APIResponse;
import com.viettelperu.qos.framework.controller.BaseController;
import com.viettelperu.qos.model.dto.ServerDTO;
import com.viettelperu.qos.service.CategoryService;

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
    private CategoryService categoryService;

    /**
     * Method to get the sub categories for a parent category by given name
     * GET
     *
     * @param parentCatName
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getAllServerInfo", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse getAllServerInfo() throws Exception {
        //Category category = categoryService.findByCategoryName(parentCatName);
        List<ServerDTO> serverInfos = new ArrayList<>();
        ServerDTO s1 = new ServerDTO(0l, "[PE] Hanoi Local - 1 Gb/s - Bitel", "127.0.0.1", 0, "online", "VN");
        ServerDTO s2 = new ServerDTO(1l, "[PE] Hanoi Itsol - 1 Gb/s - Bitel", "118.70.74.174", 1, "online", "VN");
        ServerDTO s3 = new ServerDTO(2l, "[PE] Lima - 1 Gb/s - Bitel", "192.168.1.3", 2, "online", "PE");
        ServerDTO s4 = new ServerDTO(3l, "[PE] London - 1 Gb/s - Bitel", "192.168.1.4", 3, "offline", "GB");
        serverInfos.add(s1);
        serverInfos.add(s2);
        serverInfos.add(s3);
        serverInfos.add(s4);

        return APIResponse.toOkResponse(serverInfos);
    }
    
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