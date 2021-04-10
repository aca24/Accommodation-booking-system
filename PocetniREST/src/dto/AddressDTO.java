package dto;

public class AddressDTO {

	private String city;
	private String street;
	private Integer number;
	private Double longitude;
	private Double latitude;
	
	public AddressDTO() {
		super();
	}
	public AddressDTO(String city, String street, Integer number, Double longitude, Double latitude) {
		super();
		this.city = city;
		this.street = street;
		this.number = number;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
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
}
