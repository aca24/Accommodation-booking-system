package dto;

import java.util.List;

public class ApartmentCreateDTO {

	private String name;
	private String type;
	private String status;
	private String city;
	private String street;
	private Integer number;
	private String postalCode;
	private String longitude;
	private String latitude;
	private Double price;
	private Integer numberOfRooms;
	private Integer numberOfGuests;
	private String checkInTime;
	private String checkOutTime;
	private List<Integer> amenities;
	private String dates;
	private String startDate;
	private String endDate;
	
	public ApartmentCreateDTO() {
		super();
	}
	public ApartmentCreateDTO(String name, String type, String status, String city, String street, Integer number, String postalCode,
			String longitude, String latitude, Double price, Integer numberOfRooms, Integer numberOfGuests,
			String checkInTime, String checkOutTime, List<Integer> amenities, String dates, String startDate, String endDate) {
		super();
		this.name = name;
		this.type = type;
		this.status = status;
		this.city = city;
		this.street = street;
		this.number = number;
		this.postalCode = postalCode;
		this.longitude = longitude;
		this.latitude = latitude;
		this.price = price;
		this.numberOfRooms = numberOfRooms;
		this.numberOfGuests = numberOfGuests;
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
		this.amenities = amenities;
		this.dates = dates;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getNumberOfRooms() {
		return numberOfRooms;
	}
	public void setNumberOfRooms(Integer numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}
	public Integer getNumberOfGuests() {
		return numberOfGuests;
	}
	public void setNumberOfGuests(Integer numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}
	public String getCheckInTime() {
		return checkInTime;
	}
	public void setCheckInTime(String checkInTime) {
		this.checkInTime = checkInTime;
	}
	public String getCheckOutTime() {
		return checkOutTime;
	}
	public void setCheckOutTime(String checkOutTime) {
		this.checkOutTime = checkOutTime;
	}
	public List<Integer> getAmenities() {
		return amenities;
	}
	public void setAmenities(List<Integer> amenities) {
		this.amenities = amenities;
	}
	public String getDates() {
		return dates;
	}
	public void setDates(String dates) {
		this.dates = dates;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
