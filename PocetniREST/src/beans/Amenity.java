package beans;

import java.io.Serializable;

public class Amenity implements Serializable{

	private static final long serialVersionUID = -7156683173724906143L;

	private Integer id;
	private String name;
	private boolean deleted;
	
	public Amenity() {
		super();
	}
	public Amenity(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public String forFile() {
		return this.getId() + ";" + this.getName() + ";" + this.isDeleted()  + "\r\n";
	}
}
