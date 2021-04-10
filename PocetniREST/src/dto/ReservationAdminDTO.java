package dto;

import enums.ReservationStatus;

public class ReservationAdminDTO {
	
	private Integer id;
	private String apartmentName;
	private String host;
	private String guest;
	private String date;
	private Integer numberOfStays;
	private Double price;
	private ReservationStatus status;
	
	public ReservationAdminDTO() {
		super();
	}

	public ReservationAdminDTO(Integer id, String apartmentName, String host, String guest, String date,
			Integer numberOfStays, Double price, ReservationStatus status) {
		super();
		this.id = id;
		this.apartmentName = apartmentName;
		this.host = host;
		this.guest = guest;
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

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
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

	public ReservationStatus getStatus() {
		return status;
	}

	public void setStatus(ReservationStatus status) {
		this.status = status;
	}

}
