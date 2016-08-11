package com.viettelperu.qos.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.viettelperu.qos.framework.data.JPAEntity;


/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "QOS_SERVER", indexes = {  @Index(name="name_idx", columnList = "name", unique = true),
					@Index(name="ipAddress_idx", columnList = "ipAddress", unique = true)})
public class Server extends JPAEntity<Long> implements Serializable {
	private static final long serialVersionUID = 8454862946814331767L;

	private String name;
	private String ipAddress;
	private Integer priority;
	private String description;
	private String status;
	private String countryCode;
	private String url;

    @Column
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Column
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Column
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Column
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
