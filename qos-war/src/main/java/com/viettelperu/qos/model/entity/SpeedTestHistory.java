package com.viettelperu.qos.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.viettelperu.qos.framework.data.JPAEntity;


/**
 * Speed Test History Entity
 *
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
@Entity
@Table(name = "QOS_SPEEDTEST_HISTORY", indexes = {  @Index(name="userName_idx", columnList = "user_name"),
        @Index(name="serverName_idx", columnList = "server_name")})
public class SpeedTestHistory extends JPAEntity<Long> {
	// 1: Android, 2: iOs, 3:website
	public enum ApplicationType {
		Unknow,
		Android,
		iOS,
		Website
    }
	
	public enum NetworkTechnology {
		CELL_3G,
		CELL_4G,
		WIFI,
		ETHERNET,
		CELL_2G,
		CELL,
		NONE,
		UNKNOWN
    }

	public enum SignalStrength {
		dB,
		RSSI,
		RSRP
    }

	private ApplicationType applicationType;
	private String serverName;
	private String isp;
	private Date startTime;
	private Date endTime;
	private String ipAddress;
	/** Username login or Phone number */
	private String userName;
	private String departmentZone;
	private String province;
	private String district;
	private String address;
	// Luu du lieu o dang bit per second
	private Double downloadSpeed;
	private Double maxDownloadSpeed;
	private Double uploadSpeed;
	private Double maxUploadSpeed;
	private Double packageLoss;
	private Double latency;
	private Double minLatency;
	private Double variationLatency;
	private Double latitudes;
	private Double longitudes;
	/** a Mobile Country Code. This code identifies the country. For example, in China MCC is equal to 460, in USA - 310, Hungary - 216, Belorussia - 257. */
	private Double mcc;
	/** a Mobile Network Code. This code identifies the mobile operator. The detailed table with MCC and MNC codes is available here. */
	private Double mnc;
	private NetworkTechnology networkTechnology;
	/** CellID (CID) — is a generally unique number used to identify each Base transceiver station (BTS) or sector of a BTS within a Location area code. */
	private String cid;
	/** LAC - Location Area Code is a unique number of current location area. A location area is a set of base stations that are grouped together to optimize signalling. */
	private String lac;
	private SignalStrength signalStrengthUnit;
	private Double signalStrength;
	// Extend
	private String ispCountryCode;
	private String serverCountryCode;
	private String network;
	
	private String rnc;
	private String snr;
	private String psc;
	private String ecno;
	private String imsi;
	private String imei;
	private String brand;
	private String deviceModel;
	/**  The Absolute Radio Frequency Channel Number */
	private String arfcn;
	private String rscp;
	private String ecdno;
	
	@Column
    public ApplicationType getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(ApplicationType applicationType) {
		this.applicationType = applicationType;
	}

    @Column(name="server_name")
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Column
	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	@Column
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

    @Column(name="user_name")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column
	public String getDepartmentZone() {
		return departmentZone;
	}

	public void setDepartmentZone(String departmentZone) {
		this.departmentZone = departmentZone;
	}

	@Column
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column
	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@Column
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column
	public Double getDownloadSpeed() {
		return downloadSpeed;
	}

	public void setDownloadSpeed(Double downloadSpeed) {
		this.downloadSpeed = downloadSpeed;
	}

	@Column
	public Double getMaxDownloadSpeed() {
		return maxDownloadSpeed;
	}

	public void setMaxDownloadSpeed(Double maxDownloadSpeed) {
		this.maxDownloadSpeed = maxDownloadSpeed;
	}

	@Column
	public Double getUploadSpeed() {
		return uploadSpeed;
	}

	public void setUploadSpeed(Double uploadSpeed) {
		this.uploadSpeed = uploadSpeed;
	}

	@Column
	public Double getMaxUploadSpeed() {
		return maxUploadSpeed;
	}

	public void setMaxUploadSpeed(Double maxUploadSpeed) {
		this.maxUploadSpeed = maxUploadSpeed;
	}

    @Column
	public Double getPackageLoss() {
		return packageLoss;
	}

	public void setPackageLoss(Double packageLoss) {
		this.packageLoss = packageLoss;
	}

	@Column
	public Double getLatency() {
		return latency;
	}

	public void setLatency(Double latency) {
		this.latency = latency;
	}

	@Column
	public Double getMinLatency() {
		return minLatency;
	}

	public void setMinLatency(Double minLatency) {
		this.minLatency = minLatency;
	}

    @Column
	public Double getVariationLatency() {
		return variationLatency;
	}

	public void setVariationLatency(Double variationLatency) {
		this.variationLatency = variationLatency;
	}

	@Column
	public Double getLatitudes() {
		return latitudes;
	}

	public void setLatitudes(Double latitudes) {
		this.latitudes = latitudes;
	}

	@Column
	public Double getLongitudes() {
		return longitudes;
	}

	public void setLongitudes(Double longitudes) {
		this.longitudes = longitudes;
	}

	@Column
	public Double getMcc() {
		return mcc;
	}

	public void setMcc(Double mcc) {
		this.mcc = mcc;
	}

	@Column
	public Double getMnc() {
		return mnc;
	}

	public void setMnc(Double mnc) {
		this.mnc = mnc;
	}

	@Column
	public NetworkTechnology getNetworkTechnology() {
		return networkTechnology;
	}

	public void setNetworkTechnology(NetworkTechnology networkTechnology) {
		this.networkTechnology = networkTechnology;
	}

	@Column
	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

    @Column
	public String getLac() {
		return lac;
	}

	public void setLac(String lac) {
		this.lac = lac;
	}


    @Override
    public String toString() {
        return "SpeedTestHistory{" +
                "id='" + id + '\'' +
                ", userName=" + userName +
                '}';
    }

    @Column
	public String getIspCountryCode() {
		return ispCountryCode;
	}

	public void setIspCountryCode(String ispCountryCode) {
		this.ispCountryCode = ispCountryCode;
	}

	@Column
	public String getServerCountryCode() {
		return serverCountryCode;
	}

	public void setServerCountryCode(String serverCountryCode) {
		this.serverCountryCode = serverCountryCode;
	}

	/**
	 * @return the signalStrengthUnit
	 */
	@Column
	public SignalStrength getSignalStrengthUnit() {
		return signalStrengthUnit;
	}

	/**
	 * @param signalStrengthUnit the signalStrengthUnit to set
	 */
	public void setSignalStrengthUnit(SignalStrength signalStrengthUnit) {
		this.signalStrengthUnit = signalStrengthUnit;
	}

	/**
	 * @return the signalStrength
	 */
	@Column
	public Double getSignalStrength() {
		return signalStrength;
	}

	/**
	 * @param signalStrength the signalStrength to set
	 */
	public void setSignalStrength(Double signalStrength) {
		this.signalStrength = signalStrength;
	}

	@Column
	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	@Column
	public String getRnc() {
		return rnc;
	}

	@Column
	public String getSnr() {
		return snr;
	}

	@Column
	public String getPsc() {
		return psc;
	}

	@Column
	public String getEcno() {
		return ecno;
	}

	@Column
	public String getImsi() {
		return imsi;
	}

	@Column
	public String getImei() {
		return imei;
	}

	@Column
	public String getBrand() {
		return brand;
	}

	@Column
	public String getDeviceModel() {
		return deviceModel;
	}

	@Column
	public String getArfcn() {
		return arfcn;
	}

	/**
	 * @param rnc the rnc to set
	 */
	public void setRnc(String rnc) {
		this.rnc = rnc;
	}

	/**
	 * @param snr the snr to set
	 */
	public void setSnr(String snr) {
		this.snr = snr;
	}

	/**
	 * @param psc the psc to set
	 */
	public void setPsc(String psc) {
		this.psc = psc;
	}

	/**
	 * @param ecno the ecno to set
	 */
	public void setEcno(String ecno) {
		this.ecno = ecno;
	}

	/**
	 * @param imsi the imsi to set
	 */
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	/**
	 * @param imei the imei to set
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}

	/**
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	/**
	 * @param deviceModel the deviceModel to set
	 */
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	/**
	 * @param arfcn the arfcn to set
	 */
	public void setArfcn(String arfcn) {
		this.arfcn = arfcn;
	}

	@Column
	public String getRscp() {
		return rscp;
	}

	public void setRscp(String rscp) {
		this.rscp = rscp;
	}

	@Column
	public String getEcdno() {
		return ecdno;
	}

	public void setEcdno(String ecdno) {
		this.ecdno = ecdno;
	}
}
