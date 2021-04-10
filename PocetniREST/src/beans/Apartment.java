package beans;

import java.io.Serializable;
import java.util.List;

import enums.ApartmentStatus;
import enums.ApartmentType;

public class Apartment implements Serializable{

	private static final long serialVersionUID = 5296110538208788005L;
	
	private Integer id;
	private String name;
	private ApartmentType type;
	private Integer numberOfRooms;
	private Integer numberOfGuests;
	private Integer locationId;
	private String hostUsername;
	private Double price;
	private Integer checkInTime; // inicijalno 2pm
	private Integer checkOutTime; // inicijalno 10am
	private ApartmentStatus status; // aktivno/neaktivno
	private List<Integer> amenities; // id-jevi amenitia
	private boolean deleted;
	
	public Apartment() {
		super();
		this.deleted = false;
	}	
	public Apartment(Integer id, String name, ApartmentType type, Integer numberOfRooms, 
					Integer numberOfGuests, Integer locationId, String hostUsername, 
					Double price, Integer checkInTime, Integer checkOutTime, ApartmentStatus status, List<Integer> amenities, boolean deleted) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.numberOfRooms = numberOfRooms;
		this.numberOfGuests = numberOfGuests;
		this.locationId = locationId;
		this.hostUsername = hostUsername;
		this.price = price;
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
		this.status = status;
		this.amenities = amenities;
		this.deleted = deleted;
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
	public ApartmentType getType() {
		return type;
	}
	public void setType(ApartmentType type) {
		this.type = type;
	}
	public Integer getNumberOfRooms() {
		return numberOfRooms;
	}
	public void setNumberOfRooms(Integer numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}
	public Integer getNumberOfGuests() {
		return numberOfGuests;
	}
	public void setNumberOfGuests(Integer numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}
	public Integer getLocationId() {
		return locationId;
	}
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	public String getHostUsername() {
		return hostUsername;
	}
	public void setHostUsername(String hostUsername) {
		this.hostUsername = hostUsername;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getCheckInTime() {
		return checkInTime;
	}
	public void setCheckInTime(Integer checkInTime) {
		this.checkInTime = checkInTime;
	}
	public Integer getCheckOutTime() {
		return checkOutTime;
	}
	public void setCheckOutTime(Integer checkOutTime) {
		this.checkOutTime = checkOutTime;
	}
	public ApartmentStatus getStatus() {
		return status;
	}
	public void setStatus(ApartmentStatus status) {
		this.status = status;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public List<Integer> getAmenities() {
		return amenities;
	}
	public void setAmenities(List<Integer> amenities) {
		this.amenities = amenities;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	private String amenitiesToString(List<Integer> amenities) {
		if(!amenities.isEmpty()) {
			String a = "";
			for(Integer id: amenities) {
				a += id + ",";
			}
			
			return a.substring(0, a.length() - 1);
		}
		return "";
	}
	
	public String forFile() {
		return this.getId() + ";" + this.getName() + ";" + 
				this.getType() + ";" + this.getNumberOfRooms() + ";" + 
				this.getNumberOfGuests() + ";" + this.getLocationId()
				+ ";" + this.getHostUsername() + ";" + this.getPrice()
				+ ";" + this.getCheckInTime() + ";" + this.getCheckOutTime()
				+ ";" + this.getStatus() + ";" + amenitiesToString(this.getAmenities())
				+ ";" + this.isDeleted() + "\r\n";
	}
}
