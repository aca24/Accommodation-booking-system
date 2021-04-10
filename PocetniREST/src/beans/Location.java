package beans;

import java.io.Serializable;

public class Location implements Serializable{

	private static final long serialVersionUID = 4466578101475381664L;
	
	private Integer id;
	private Double longitude;
	private Double latitude;
	private Integer addressId;
	
	public Location() {
		super();
	}
	public Location(Integer id, Double longitude, Double latitude, Integer addressId) {
		super();
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
		this.addressId = addressId;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Integer getAddressId() {
		return addressId;
	}
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}
	
	public String forFile() {
		return this.getId() + ";" + this.getAddressId() + ";" + this.getLongitude() 
				+ ";" + this.getLatitude() + "\r\n";
	}
	
}
