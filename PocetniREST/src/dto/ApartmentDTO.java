package dto;

import enums.ApartmentStatus;
import enums.ApartmentType;

public class ApartmentDTO {

	private Integer id;
	private String name;
	private ApartmentType type;
	private ApartmentStatus status;
	private Integer numberOfRooms;
	private Integer numberOfGuests;
	private String city;
	private Double price;
	private Integer reviews;
	private Double rating;
	
	public ApartmentDTO() {
		super();
	}
	public ApartmentDTO(Integer id, String name, ApartmentType type, ApartmentStatus status, Integer numberOfRooms, Integer numberOfGuests,
			String city, Double price, Integer reviews, Double rating) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.status = status;
		this.numberOfRooms = numberOfRooms;
		this.numberOfGuests = numberOfGuests;
		this.city = city;
		this.price = price;
		this.reviews = reviews;
		this.rating = rating;
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getReviews() {
		return reviews;
	}
	public void setReviews(Integer reviews) {
		this.reviews = reviews;
	}
	public Double getRating() {
		return rating;
	}
	public void setRating(Double rating) {
		this.rating = rating;
	}
}
