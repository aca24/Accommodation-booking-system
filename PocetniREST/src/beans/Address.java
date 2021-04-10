package beans;

import java.io.Serializable;

public class Address implements Serializable{
	
	private static final long serialVersionUID = 5585183168382612182L;

	private Integer id;
	private String street;
	private Integer number;
	private String city;
	private String postalCode;
	
	public Address() {
		super();
	}
	public Address(Integer id, String street, Integer number, String city, String postalCode) {
		super();
		this.id = id;
		this.street = street;
		this.number = number;
		this.city = city;
		this.postalCode = postalCode;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
		
	public String forFile() {
		return this.getId() + ";" + this.getStreet() + ";" + this.getNumber() 
				+ ";" + this.getCity() + ";" + this.getPostalCode() + "\r\n";
	}
}
