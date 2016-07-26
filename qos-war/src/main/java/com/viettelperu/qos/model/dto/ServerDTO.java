package com.viettelperu.qos.model.dto;

/**
 * {"name":"job1", "metadataJsonString":"{}", "callbackUrl":"", "catId":1}
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public class ServerDTO {
	Long serverId;
    String name;
    String ipAddress;
    Integer priority;
    String description;
    String status;
    String countryCode;

	public ServerDTO(Long serverId, String name, String ipAddress, Integer priority, String status, String countryCode) {
		super();
		this.serverId = serverId;
		this.name = name;
		this.ipAddress = ipAddress;
		this.priority = priority;
		this.status = status;
		this.countryCode = countryCode;
	}
    
    public ServerDTO(Long serverId, String name, String ipAddress, String description) {
		super();
		this.serverId = serverId;
		this.name = name;
		this.ipAddress = ipAddress;
		this.description = description;
	}
    
    /**
	 * @return the priority
	 */
	public Integer getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
    
    public ServerDTO() {
	}

	/**
	 * @return the serverId
	 */
	public Long getServerId() {
		return serverId;
	}

	/**
	 * @param serverId the serverId to set
	 */
	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}



	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}



	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}



	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}



	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}



	@Override
    public String toString() {
        return "ServerDTO{" +
                "name='" + name + '\'' +
                ", id='" + serverId + '\'' +
                '}';
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

    public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
}
