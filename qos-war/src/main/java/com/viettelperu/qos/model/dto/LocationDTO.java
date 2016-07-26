package com.viettelperu.qos.model.dto;

/**
 * @author Nam, Nguyen Hoai <namnh@itsol.vn>
 */
public class LocationDTO {
//	Latitude: 12° 2' 26.49'' S   -12.0406920
//	Longitude: 77° 1' 52.59'' W   -77.0312760
//  address: Jirón Viru, Distrito de Lima, Peru
//	Elevation:	152.61 m
	
    double latitude;
    double longitude;
    String latitudeDisplay;
    String longitudeDisplay;
    String elevation;
    String address;
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getLatitudeDisplay() {
		return latitudeDisplay;
	}
	public void setLatitudeDisplay(String latitudeDisplay) {
		this.latitudeDisplay = latitudeDisplay;
	}
	public String getLongitudeDisplay() {
		return longitudeDisplay;
	}
	public void setLongitudeDisplay(String longitudeDisplay) {
		this.longitudeDisplay = longitudeDisplay;
	}
	public String getElevation() {
		return elevation;
	}
	public void setElevation(String elevation) {
		this.elevation = elevation;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
    
}
