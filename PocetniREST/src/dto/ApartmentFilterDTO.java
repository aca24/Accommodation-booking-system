package dto;

import java.util.List;


public class ApartmentFilterDTO {

	private String city;
	private String type;
	private List<Integer> numberOfRooms;
	private List<Integer> numberOfGuests;
	private String minPrice;
	private String maxPrice;
	private String rating;
	private List<String> amenities;
	private String startDate;
	private String endDate;
	
	public ApartmentFilterDTO() {
		super();
	}
	public ApartmentFilterDTO(String city, String type, List<Integer> numberOfRooms,
			List<Integer> numberOfGuests, String minPrice, String maxPrice, String rating, List<String> amenities, String startDate, String endDate) {
		super();
		this.city = city;
		this.type = type;
		this.numberOfRooms = numberOfRooms;
		this.numberOfGuests = numberOfGuests;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.rating = rating;
		this.amenities = amenities;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Integer> getNumberOfRooms() {
		return numberOfRooms;
	}
	public void setNumberOfRooms(List<Integer> numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}
	public List<Integer> getNumberOfGuests() {
		return numberOfGuests;
	}
	public void setNumberOfGuests(List<Integer> numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}
	public String getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}
	public String getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public List<String> getAmenities() {
		return amenities;
	}
	public void setAmenities(List<String> amenities) {
		this.amenities = amenities;
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
	@Override
	public String toString() {
		return "ApartmentFilterDTO [city=" + city + ", type=" + type + ", numberOfRooms=" + numberOfRooms
				+ ", numberOfGuests=" + numberOfGuests + ", minPrice=" + minPrice + ", maxPrice=" + maxPrice
				+ ", rating=" + rating + ", amenities=" + amenities + "]";
	}
}
