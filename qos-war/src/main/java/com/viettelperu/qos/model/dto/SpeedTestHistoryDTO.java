package com.viettelperu.qos.model.dto;

import java.util.Date;

/**
 *
 * {"name":"cat1", "priority":2, "parent":"pCat"}
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public class SpeedTestHistoryDTO {
	Long id;
	String applicationType;
	String serverName;
	String ips;
	Date startTime;
	Date endTime;
	Double duration;
	String ipAddress;
	String userName;
	String departmentZone;
	String province;
	String district;
	String address;
	Double downloadSpeed;
	Double maxDownloadSpeed;
	Double uploadSpeed;
	Double maxUploadSpeed;
	Double packageLoss;
	Double latency;
	Double minLatency;
	Double variationLatency;
	Double latitudes;
	Double longitudes;
	Double mcc;
	Double mnc;
	String networkTechnology;
	String cid;
	String lac;
	String signalStrengthUnit;
	Double signalStrength;
	// Extend
	String ipsCountryCode;
	String serverCountryCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getIps() {
		return ips;
	}

	public void setIps(String ips) {
		this.ips = ips;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDepartmentZone() {
		return departmentZone;
	}

	public void setDepartmentZone(String departmentZone) {
		this.departmentZone = departmentZone;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getDownloadSpeed() {
		return downloadSpeed;
	}

	public void setDownloadSpeed(Double downloadSpeed) {
		this.downloadSpeed = downloadSpeed;
	}

	public Double getMaxDownloadSpeed() {
		return maxDownloadSpeed;
	}

	public void setMaxDownloadSpeed(Double maxDownloadSpeed) {
		this.maxDownloadSpeed = maxDownloadSpeed;
	}

	public Double getUploadSpeed() {
		return uploadSpeed;
	}

	public void setUploadSpeed(Double uploadSpeed) {
		this.uploadSpeed = uploadSpeed;
	}

	public Double getMaxUploadSpeed() {
		return maxUploadSpeed;
	}

	public void setMaxUploadSpeed(Double maxUploadSpeed) {
		this.maxUploadSpeed = maxUploadSpeed;
	}

	public Double getPackageLoss() {
		return packageLoss;
	}

	public void setPackageLoss(Double packageLoss) {
		this.packageLoss = packageLoss;
	}

	public Double getLatency() {
		return latency;
	}

	public void setLatency(Double latency) {
		this.latency = latency;
	}

	public Double getMinLatency() {
		return minLatency;
	}

	public void setMinLatency(Double minLatency) {
		this.minLatency = minLatency;
	}

	public Double getVariationLatency() {
		return variationLatency;
	}

	public void setVariationLatency(Double variationLatency) {
		this.variationLatency = variationLatency;
	}

	public Double getLatitudes() {
		return latitudes;
	}

	public void setLatitudes(Double latitudes) {
		this.latitudes = latitudes;
	}

	public Double getLongitudes() {
		return longitudes;
	}

	public void setLongitudes(Double longitudes) {
		this.longitudes = longitudes;
	}

	public Double getMcc() {
		return mcc;
	}

	public void setMcc(Double mcc) {
		this.mcc = mcc;
	}

	public Double getMnc() {
		return mnc;
	}

	public void setMnc(Double mnc) {
		this.mnc = mnc;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getLac() {
		return lac;
	}

	public void setLac(String lac) {
		this.lac = lac;
	}
	
	public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	public String getNetworkTechnology() {
		return networkTechnology;
	}

	public void setNetworkTechnology(String networkTechnology) {
		this.networkTechnology = networkTechnology;
	}

	public String getIpsCountryCode() {
		return ipsCountryCode;
	}

	public void setIpsCountryCode(String ipsCountryCode) {
		this.ipsCountryCode = ipsCountryCode;
	}

	public String getServerCountryCode() {
		return serverCountryCode;
	}

	public void setServerCountryCode(String serverCountryCode) {
		this.serverCountryCode = serverCountryCode;
	}

	/**
	 * @return the signalStrengthUnit
	 */
	public String getSignalStrengthUnit() {
		return signalStrengthUnit;
	}

	/**
	 * @param signalStrengthUnit the signalStrengthUnit to set
	 */
	public void setSignalStrengthUnit(String signalStrengthUnit) {
		this.signalStrengthUnit = signalStrengthUnit;
	}

	/**
	 * @return the signalStrength
	 */
	public Double getSignalStrength() {
		return signalStrength;
	}

	/**
	 * @param signalStrength the signalStrength to set
	 */
	public void setSignalStrength(Double signalStrength) {
		this.signalStrength = signalStrength;
	}

	@Override
    public String toString() {
        return "SpeedTestHistoryDTO{" +
                "id='" + id + '\'' +
                '}';
    }
}
