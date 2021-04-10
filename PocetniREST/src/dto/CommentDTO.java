package dto;

public class CommentDTO {

	private Integer id;
	private String guest;
	private String text;
	private Integer rating;
	
	public CommentDTO() {
		super();
	}
	public CommentDTO(Integer id, String guest, String text, Integer rating) {
		super();
		this.id = id;
		this.guest = guest;
		this.text = text;
		this.rating = rating;
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
