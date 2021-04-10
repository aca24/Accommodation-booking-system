package dto;

import java.util.Date;
import java.util.List;

public class ReservationDTO {

	private Integer apartmentId;
	private String guest;
	private String date;
	private Integer numberOfStays;
	private Double price;
	private String message;
	private List<Date> availableDates;
	
	public ReservationDTO() {
		super();
	}
	public ReservationDTO(Integer apartmentId, String guest, String date, Integer numberOfStays, Double price,
			String message, List<Date> availableDates) {
		super();
		this.apartmentId = apartmentId;
		this.guest = guest;
		this.date = date;
		this.numberOfStays = numberOfStays;
		this.price = price;
		this.message = message;
		this.availableDates = availableDates;
	}
	
	public Integer getApartmentId() {
		return apartmentId;
	}
	public void setApartmentId(Integer apartmentId) {
		this.apartmentId = apartmentId;
	}
	public String getGuest() {
		return guest;
	}
	public void setGuest(String guest) {
		this.guest = guest;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getNumberOfStays() {
		return numberOfStays;
	}
	public void setNumberOfStays(Integer numberOfStays) {
		this.numberOfStays = numberOfStays;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<Date> getAvailableDates() {
		return availableDates;
	}
	public void setAvailableDates(List<Date> availableDates) {
		this.availableDates = availableDates;
	}
}
