package dto;

import java.util.List;

import enums.ApartmentStatus;
import enums.ApartmentType;

public class ApartmentUpdateDTO {

	private String name;
	private ApartmentType type;
	private ApartmentStatus status;
	private String city;
	private String street;
	private Integer number;
	private String postalCode;
	private Double price;
	private Integer numberOfRooms;
	private Integer numberOfGuests;
	private Integer checkInTime;
	private Integer checkOutTime;
	private List<Integer> amenities;
	private List<String> dates;
	
	public ApartmentUpdateDTO() {
		super();
	}
	public ApartmentUpdateDTO(String name, ApartmentType type, ApartmentStatus status, String city, String street, Integer number, String postalCode,
			Double price, Integer numberOfRooms, Integer numberOfGuests, Integer checkInTime, Integer checkOutTime,
			List<Integer> amenities, List<String> dates) {
		super();
		this.name = name;
		this.type = type;
		this.status = status;
		this.city = city;
		this.street = street;
		this.number = number;
		this.postalCode = postalCode;
		this.price = price;
		this.numberOfRooms = numberOfRooms;
		this.numberOfGuests = numberOfGuests;
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
		this.amenities = amenities;
		this.dates = dates;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ApartmentType getType() {
		return type;
	}
	public void setType(ApartmentType type) {
		this.type = type;
	}
	public ApartmentStatus getStatus() {
		return status;
	}
	public void setStatus(ApartmentStatus status) {
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
	public Integer getCheckInTime() {
		return checkInTime;
	}
	public void setCheckInTime(Integer checkInTime) {
		this.checkInTime = checkInTime;
	}
	public Integer getCheckOutTime() {
		return checkOutTime;
	}
	public void setCheckOutTime(Integer checkOutTime) {
		this.checkOutTime = checkOutTime;
	}
	public List<Integer> getAmenities() {
		return amenities;
	}
	public void setAmenities(List<Integer> amenities) {
		this.amenities = amenities;
	}
	public List<String> getDates() {
		return dates;
	}
	public void setDates(List<String> dates) {
		this.dates = dates;
	}
}
