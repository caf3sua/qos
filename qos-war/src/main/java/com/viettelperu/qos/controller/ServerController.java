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
import com.viettelperu.qos.service.ServerService;

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
    private ServerService serverService;

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
    	//List<Server> serverInfos = serverService.findAll();
        List<ServerDTO> serverInfos = new ArrayList<>();
        ServerDTO s1 = new ServerDTO(0l, "[PE] Hanoi Local - 1 Gb/s - Bitel", "localhost"
        		, 0, "online", "VN", "http://localhost:8080/qos");
        ServerDTO s2 = new ServerDTO(1l, "[PE] Hanoi Itsol - 1 Gb/s - Bitel", "118.71.224.225"
        		, 1, "online", "VN", "http://118.71.224.225:8080/qos");
        ServerDTO s3 = new ServerDTO(2l, "[PE] Lima - 1 Gb/s - Bitel", "192.168.1.3"
        		, 2, "online", "PE", "http://192.168.1.3");
        ServerDTO s4 = new ServerDTO(3l, "[PE] London - 1 Gb/s - Bitel", "192.168.1.4"
        		, 3, "offline", "GB", "http://192.168.1.4");
        ServerDTO s5 = new ServerDTO(4l, "[PE] TPBank - 1 Gb/s", "103.232.56.17"
        		, 4, "online", "PE", "http://103.232.56.17:8080/qos");
        serverInfos.add(s1);
        serverInfos.add(s2);
        serverInfos.add(s3);
        serverInfos.add(s4);
        serverInfos.add(s5);

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