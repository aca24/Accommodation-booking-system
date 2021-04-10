package dto;

public class ImageDTO {
	
	private Integer id;
	private String imageURL;
	
	public ImageDTO() {
		super();
	}
	public ImageDTO(Integer id, String imageURL) {
		super();
		this.id = id;
		this.imageURL = imageURL;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
}
