package dto;

public class LeaveCommentDTO {

	private String guest;
	private Integer apartmentId;
	private String text;
	private Integer rating;
	
	public LeaveCommentDTO() {
		super();
	}
	public LeaveCommentDTO(String guest, Integer apartmentId, String text, Integer rating) {
		super();
		this.guest = guest;
		this.apartmentId = apartmentId;
		this.text = text;
		this.rating = rating;
	}
	
	public String getGuest() {
		return guest;
	}
	public void setGuest(String guest) {
		this.guest = guest;
	}
	public Integer getApartmentId() {
		return apartmentId;
	}
	public void setApartmentId(Integer apartmentId) {
		this.apartmentId = apartmentId;
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
}
