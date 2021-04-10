package beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import enums.ReservationStatus;

public class Reservation implements Serializable{

	private static final long serialVersionUID = -6933367596488171376L;

	private Integer id;
	private Integer apartmentId;
	private Date date;
	private Integer numberOfStays;
	private Double price;
	private String message;
	private String guest;
	private ReservationStatus status;
	
	public Reservation() {
		super();
	}
	public Reservation(Integer id, Integer apartmentId, Date date, Integer numberOfStays, Double price, String message,
			String guest, ReservationStatus status) {
		super();
		this.id = id;
		this.apartmentId = apartmentId;
		this.date = date;
		this.numberOfStays = numberOfStays;
		this.price = price;
		this.message = message;
		this.guest = guest;
		this.status = status;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getApartmentId() {
		return apartmentId;
	}
	public void setApartmentId(Integer apartmentId) {
		this.apartmentId = apartmentId;
	}
	public String getFormatedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return sdf.format(date);
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
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
	public String getGuest() {
		return guest;
	}
	public void setGuest(String guest) {
		this.guest = guest;
	}
	public ReservationStatus getStatus() {
		return status;
	}
	public void setStatus(ReservationStatus status) {
		this.status = status;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String forFile() {
		return this.getId() + ";" + this.getApartmentId() + ";" + 
				this.getGuest() + ";" + this.getFormatedDate() + ";" + 
				this.getNumberOfStays() + ";" + this.getPrice() + ";" + this.getMessage() + ";" +
				this.getStatus().name() + "\r\n";
	}
}
