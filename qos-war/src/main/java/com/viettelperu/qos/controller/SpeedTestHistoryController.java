package com.viettelperu.qos.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viettelperu.qos.framework.api.APIResponse;
import com.viettelperu.qos.framework.controller.BaseController;
import com.viettelperu.qos.framework.exception.NotFoundException;
import com.viettelperu.qos.model.dto.SpeedTestHistoryDTO;
import com.viettelperu.qos.model.entity.SpeedTestHistory;
import com.viettelperu.qos.model.entity.SpeedTestHistory.ApplicationType;
import com.viettelperu.qos.model.entity.SpeedTestHistory.NetworkTechnology;
import com.viettelperu.qos.model.entity.SpeedTestHistory.SignalStrength;
import com.viettelperu.qos.service.SpeedTestHistoryService;

/**
 * Speed Test History creation and get APIs
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
@Controller
@RequestMapping("history")
public class SpeedTestHistoryController extends BaseController {
    private static Logger LOG = LoggerFactory.getLogger(SpeedTestHistoryController.class);

    @Autowired
    private SpeedTestHistoryService speedTestHistoryService;

    /**
     * Method to handle creation of the category by extracting the categoryInfo json from
     * POST body expected in the format - {"name":"cat1", "priority":2, "parent":"pCat"}
     *
     * @param categoryDTO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST, headers = {JSON_API_CONTENT_HEADER})
    public @ResponseBody
    APIResponse createHistory(@RequestBody SpeedTestHistoryDTO dto) throws Exception {
    	Validate.notNull(dto, "SpeedTestHistoryDTO is null");
    	
    	SpeedTestHistory history = new SpeedTestHistory();
    	convertDTOintoEntity(dto, history);
    	
    	// Insert to database
    	SpeedTestHistory result = speedTestHistoryService.insert(history);
    	
        return APIResponse.toOkResponse(result);
    }

    /**
     * Method to get the category by given id
     * GET
     *
     * @param catId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse getHistoryById(@PathVariable Long id) throws Exception {
    	LOG.debug("START getHistoryById for id:" + id);
    	SpeedTestHistory history = speedTestHistoryService.findById(id);
    	
        return APIResponse.toOkResponse(history);
    }

    /**
     * Method to get the category by given id
     * DELETE
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public @ResponseBody
    APIResponse deleteHistoryById(@PathVariable Long id) throws Exception {
    	LOG.debug("START deleteHistoryById for id:" + id);
    	Validate.notNull(id, "ID is blank");
    	
    	boolean result = speedTestHistoryService.deleteById(id);
    	if (!result) {
    		return APIResponse.toErrorResponse("History with ID:" + id + " not found.");
    	}
    	
        return APIResponse.toOkResponse("Delete success");
    }
    
    /**
     * Method to get the category by given name
     * GET
     *
     * @param catName
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getByUsername/{username}", method = RequestMethod.GET)
    public @ResponseBody
    APIResponse getByUsername(@PathVariable String username) throws Exception {
    	LOG.debug("START getByUsername for username:" + username);
    	
    	List<SpeedTestHistory> history = speedTestHistoryService.findByUsername(username);
    	if (null == history || history.size() == 0) {
    		return APIResponse.toOkResponse(history, HttpStatus.NOT_FOUND.value());
    	}
    	
    	// Convert to DTO
    	Mapper mapper = new DozerBeanMapper();
    	List<SpeedTestHistoryDTO> historyDTO = new ArrayList<>();
    	for (SpeedTestHistory speedTestHistory : history) {
    		SpeedTestHistoryDTO item = mapper.map(speedTestHistory, SpeedTestHistoryDTO.class);
    		historyDTO.add(item);
		}
    	
        return APIResponse.toOkResponse(historyDTO);
    }
    
    /**
     * Method to get the category by given name
     * GET
     *
     * @param catName
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/search}", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse search(@RequestBody SpeedTestHistoryDTO speedTestHistoryDTO) throws Exception {
    	throw new NotFoundException("History for search criteria");
    }
    
    /**
     * Convert from DTO to Entity
     * @param dto
     * @param entity
     */
    private void convertDTOintoEntity(SpeedTestHistoryDTO dto, SpeedTestHistory entity) {
    	entity.setApplicationType(ApplicationType.valueOf(dto.getApplicationType()));
    	entity.setServerName(dto.getServerName());
    	entity.setIsp(dto.getIsp());
    	entity.setStartTime(dto.getStartTime());
    	entity.setEndTime(dto.getEndTime());
    	entity.setIpAddress(dto.getIpAddress());
    	entity.setUserName(dto.getUserName());
    	entity.setDepartmentZone(dto.getDepartmentZone());
    	entity.setProvince(dto.getProvince());
    	entity.setDistrict(dto.getDistrict());
    	entity.setAddress(dto.getAddress());
    	entity.setDownloadSpeed(dto.getDownloadSpeed());
    	entity.setMaxDownloadSpeed(dto.getMaxDownloadSpeed());
    	entity.setUploadSpeed(dto.getUploadSpeed());
    	entity.setMaxUploadSpeed(dto.getMaxUploadSpeed());
    	entity.setPackageLoss(dto.getPackageLoss());
    	entity.setLatency(dto.getLatency());
    	entity.setMinLatency(dto.getMinLatency());
    	entity.setVariationLatency(dto.getVariationLatency());
    	entity.setLatitudes(dto.getLatitudes());
    	entity.setLongitudes(dto.getLongitudes());
    	entity.setMcc(dto.getMcc());
    	entity.setMnc(dto.getMnc());
    	entity.setNetworkTechnology(NetworkTechnology.valueOf(dto.getNetworkTechnology()));
    	entity.setCid(dto.getCid());
    	entity.setLac(dto.getLac());
    	entity.setSignalStrengthUnit(SignalStrength.valueOf(dto.getSignalStrengthUnit()));
    	entity.setSignalStrength(dto.getSignalStrength());
    	// Extend
    	entity.setIspCountryCode(dto.getIspCountryCode());
    	entity.setServerCountryCode(dto.getServerCountryCode());
    	entity.setNetwork(dto.getNetwork());
    	
    	entity.setRnc(dto.getRnc());
    	entity.setSnr(dto.getSnr());
    	entity.setPsc(dto.getPsc());
    	entity.setEcno(dto.getEcno());
    	entity.setImsi(dto.getImsi());
    	entity.setImei(dto.getImei());
    	entity.setBrand(dto.getBrand());
    	entity.setDeviceModel(dto.getDeviceModel());
    	/**  The Absolute Radio Frequency Channel Number */
    	entity.setArfcn(dto.getArfcn());
    	entity.setRscp(dto.getRscp());
    	entity.setEcdno(dto.getEcdno());
    }
}