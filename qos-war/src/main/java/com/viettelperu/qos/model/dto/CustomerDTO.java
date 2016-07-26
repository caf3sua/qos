package com.viettelperu.qos.model.dto;

import java.util.Date;

/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public class CustomerDTO {
	String code;
    Long custId;
    String firstName;
    String lastName;
    Long serviceType;
    Date startDatetime;
    Long Status;
    String message;
    String telecomService;
    String isdn;
    
	/**
	 * @return the isdn
	 */
	public String getIsdn() {
		return isdn;
	}
	/**
	 * @param isdn the isdn to set
	 */
	public void setIsdn(String isdn) {
		this.isdn = isdn;
	}
	/**
	 * @return the telecomService
	 */
	public String getTelecomService() {
		return telecomService;
	}
	/**
	 * @param telecomService the telecomService to set
	 */
	public void setTelecomService(String telecomService) {
		this.telecomService = telecomService;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the custId
	 */
	public Long getCustId() {
		return custId;
	}
	/**
	 * @param custId the custId to set
	 */
	public void setCustId(Long custId) {
		this.custId = custId;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the serviceType
	 */
	public Long getServiceType() {
		return serviceType;
	}
	/**
	 * @param serviceType the serviceType to set
	 */
	public void setServiceType(Long serviceType) {
		this.serviceType = serviceType;
	}
	/**
	 * @return the startDatetime
	 */
	public Date getStartDatetime() {
		return startDatetime;
	}
	/**
	 * @param startDatetime the startDatetime to set
	 */
	public void setStartDatetime(Date startDatetime) {
		this.startDatetime = startDatetime;
	}
	/**
	 * @return the status
	 */
	public Long getStatus() {
		return Status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Long status) {
		Status = status;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
    
    
}
