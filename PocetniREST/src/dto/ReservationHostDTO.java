package dto;

import enums.ReservationStatus;

public class ReservationHostDTO {

	private Integer id;
	private String apartmentName;
	private String guest;
	private String date;
	private Integer numberOfStays;
	private Double price;
	private String message;
	private ReservationStatus status;
	
	public ReservationHostDTO() {
		super();
	}

	public ReservationHostDTO(Integer id, String apartmentName, String guest, String date, Integer numberOfStays,
			Double price, String message, ReservationStatus status) {
		super();
		this.id = id;
		this.apartmentName = apartmentName;
		this.guest = guest;
		this.date = date;
		this.numberOfStays = numberOfStays;
		this.price = price;
		this.message = message;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getApartmentName() {
		return apartmentName;
	}

	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
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

	public ReservationStatus getStatus() {
		return status;
	}

	public void setStatus(ReservationStatus status) {
		this.status = status;
	}
}
