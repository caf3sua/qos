package com.viettelperu.qos.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
    	LOG.info("getAllServerInfo");
//    	List<Server> servers = serverService.findAll();
        List<ServerDTO> servers = new ArrayList<>();
        ServerDTO s1 = new ServerDTO(0l, "[PE] Hanoi Local - 1 Gb/s - Bitel", "localhost"
        		, 0, "online", "VN", "http://localhost:8080/qos-service");
        ServerDTO s2 = new ServerDTO(1l, "[PE] Hanoi Itsol - 1 Gb/s - Bitel", "118.71.224.225"
        		, 1, "online", "VN", "http://118.71.224.225:8080/qos");
        ServerDTO s3 = new ServerDTO(2l, "[PE] Lima - 1 Gb/s - Bitel", "192.168.1.3"
        		, 2, "offline", "PE", "http://192.168.1.3");
        servers.add(s1);
        servers.add(s2);
        servers.add(s3);
        
        return APIResponse.toOkResponse(servers);
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
    
    /**
     * Method to get the category by given id
     * GET
     *
     * @param catId
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/ipinfo", method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody
    ResponseEntity getIpInfo(HttpServletRequest request) throws Exception {
    	// Generate the http headers with the file properties
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");

        // Get ip info
        String remoteAddr = request.getRemoteAddr();
        if (remoteAddr.equals("0:0:0:0:0:0:0:1")) {
            InetAddress localip = java.net.InetAddress.getLocalHost();
            remoteAddr = localip.getHostAddress();
        }
        
        String ipInfo = sendIpApi(remoteAddr);
        
        APIResponse resObj = new APIResponse();
        resObj.setCode(200);
        resObj.setResult(ipInfo);
        resObj.setExtraResult(remoteAddr);
        return new ResponseEntity<>(resObj, headers, HttpStatus.OK);
    }
    
    private String sendIpApi(String ipAddress) throws IOException {
    	LOG.debug("Get Ip Info: " + "http://ip-api.com/json/" + ipAddress);
    	
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://ip-api.com/json/" + ipAddress);
//		httpGet.addHeader("User-Agent", USER_AGENT);
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				httpResponse.getEntity().getContent()));

		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = reader.readLine()) != null) {
			response.append(inputLine);
		}
		reader.close();

		// print result
		httpClient.close();
		
		return response.toString();
	}
}