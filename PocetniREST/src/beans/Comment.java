package beans;

import java.io.Serializable;

public class Comment implements Serializable{

	private static final long serialVersionUID = -964071664751294847L;

	private Integer id;
	private String guest;
	private Integer apartment;
	private String text;
	private Integer rating;
	private boolean approved;
	
	public Comment() {
		super();
		this.approved = false;
	}
	public Comment(Integer id, String guest, Integer apartment, String text, Integer rating, boolean approved) {
		super();
		this.id = id;
		this.guest = guest;
		this.apartment = apartment;
		this.text = text;
		this.rating = rating;
		this.approved = approved;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getGuest() {
		return guest;
	}
	public void setGuest(String guest) {
		this.guest = guest;
	}
	public Integer getApartment() {
		return apartment;
	}
	public void setApartment(Integer apartment) {
		this.apartment = apartment;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String forFile() {
		return this.getId() + ";" + this.getGuest() + ";" + this.getApartment()
				+ ";" + this.getText() + ";" + this.getRating() + ";" + 
				this.isApproved() + "\r\n";
	}
}
