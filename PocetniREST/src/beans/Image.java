package beans;

public class Image {

	private Integer id;
	private Integer apartmentId;
	private String imageURL;
	private Boolean deleted;
	
	public Image() {
		super();
	}
	public Image(Integer id, Integer apartmentId, String imageURL, Boolean deleted) {
		super();
		this.id = id;
		this.apartmentId = apartmentId;
		this.imageURL = imageURL;
		this.deleted = deleted;
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
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
	public String forFile() {
		return this.getId() + ";" + this.getApartmentId() + ";" + this.getImageURL() 
				+ ";" + this.getDeleted()+ "\r\n";
	}
}
