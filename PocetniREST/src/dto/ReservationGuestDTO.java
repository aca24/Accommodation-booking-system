package dto;

import enums.ReservationStatus;

public class ReservationGuestDTO {
	
	private Integer id;
	private String apartmentName;
	private String date;
	private Integer numberOfStays;
	private Double price;
	private ReservationStatus status;
	
	public ReservationGuestDTO() {
		super();
	}

	public ReservationGuestDTO(Integer id, String apartmentName, String date, Integer numberOfStays, Double price,
			ReservationStatus status) {
		super();
		this.id = id;
		this.apartmentName = apartmentName;
		this.date = date;
		this.numberOfStays = numberOfStays;
		this.price = price;
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

	public ReservationStatus getStatus() {
		return status;
	}

	public void setStatus(ReservationStatus status) {
		this.status = status;
	}
	
	
}
