package dto;

import java.util.Date;
import java.util.List;

import beans.Amenity;
import enums.ApartmentType;

public class ApartmentProfileDTO {
	
	private Integer id;
	private String name;
	private AddressDTO address;
	private Double longitude;
	private Double latitude;
	private Double rating;
	private Integer reviews;
	// information
	private ApartmentType type;
	private Integer numberOfRooms;
	private Integer numberOfGuests;
	private Double price;
	private Integer checkInTime;
	private Integer checkOutTime;
	// available dates
	private List<Date> availableDates;
	// comments
	private List<CommentDTO> comments;
	
	// amenities
	private List<Amenity> amenities;

	public ApartmentProfileDTO() {
		super();
	}
	public ApartmentProfileDTO(Integer id, String name, AddressDTO address, Double longitude, Double latitude, Double rating, Integer reviews,
			ApartmentType type, Integer numberOfRooms, Integer numberOfGuests, Double price, Integer checkInTime,
			Integer checkOutTime, List<Date> availableDates, List<CommentDTO> comments, List<Amenity> amenities) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.longitude = longitude;
		this.latitude = latitude;
		this.rating = rating;
		this.reviews = reviews;
		this.type = type;
		this.numberOfRooms = numberOfRooms;
		this.numberOfGuests = numberOfGuests;
		this.price = price;
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
		this.availableDates = availableDates;
		this.comments = comments;
		this.amenities = amenities;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AddressDTO getAddress() {
		return address;
	}
	public void setAddress(AddressDTO address) {
		this.address = address;
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
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
	public Integer getReviews() {
		return reviews;
	}
	public void setReviews(Integer reviews) {
		this.reviews = reviews;
	}
	public ApartmentType getType() {
		return type;
	}
	public void setType(ApartmentType type) {
		this.type = type;
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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
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
	public List<Date> getAvailableDates() {
		return availableDates;
	}
	public void setAvailableDates(List<Date> availableDates) {
		this.availableDates = availableDates;
	}
	public List<CommentDTO> getComments() {
		return comments;
	}
	public void setComments(List<CommentDTO> comments) {
		this.comments = comments;
	}
	public List<Amenity> getAmenities() {
		return amenities;
	}
	public void setAmenities(List<Amenity> amenities) {
		this.amenities = amenities;
	}
}
