package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import beans.Apartment;
import enums.ApartmentStatus;
import enums.ApartmentType;

public class ApartmentDAO {
	
	private String contextPath;

	public ApartmentDAO() {
		super();
	}

	public ApartmentDAO(String contextPath) {
		this.contextPath = contextPath;
	}
	
	public List<Apartment> findByHost(String host){
		List<Apartment> allApartments = load(contextPath);
		List<Apartment> apartments = new ArrayList<Apartment>();
		for(Apartment a: allApartments) {
			if(a.getHostUsername().equals(host)) {
				apartments.add(a);
			}
		}
		return apartments;
	}
	
	public List<Apartment> findActiveByHost(String host){
		List<Apartment> allApartments = load(contextPath);
		List<Apartment> apartments = new ArrayList<Apartment>();
		for(Apartment a: allApartments) {
			if(a.getHostUsername().equals(host) && a.getStatus().equals(ApartmentStatus.ACTIVE) && !a.isDeleted()) {
				apartments.add(a);
			}
		}
		return apartments;
	}
	
	public List<Apartment> findInactiveByHost(String host){
		List<Apartment> allApartments = load(contextPath);
		List<Apartment> apartments = new ArrayList<Apartment>();
		for(Apartment a: allApartments) {
			if(a.getHostUsername().equals(host) && a.getStatus().equals(ApartmentStatus.INACTIVE)) {
				apartments.add(a);
			}
		}
		return apartments;
	}
	
	public List<Apartment> findByName(List<Apartment> allApartments, String name){
		List<Apartment> apartments = new ArrayList<Apartment>();
		for(Apartment a: allApartments) {
			if(a.getName().toLowerCase().contains(name.toLowerCase())) {
				apartments.add(a);
			}
		}
		return apartments;
	}
	
	public Apartment findById(Integer id){
		List<Apartment> apartments = load(contextPath);
		for(Apartment a: apartments) {
			if(a.getId().equals(id)) {
				return a;
			}
		}
		return null;
	}
	
	public Integer findIdByName(String name) {
		List<Apartment> apartments = load(contextPath);
		for(Apartment a: apartments) {
			if (a.getName().toLowerCase().equals(name.toLowerCase())) {
				return a.getId();
			}
		}
		return null;
	}
	
	public Integer findIdHostByName(String name, String username) {
		List<Apartment> apartments = load(contextPath);
		for(Apartment a: apartments) {
			if (a.getName().toLowerCase().equals(name.toLowerCase()) && a.getHostUsername().equals(username) && a.getStatus().equals(ApartmentStatus.ACTIVE)) {
				return a.getId();
			}
		}
		return null;
	}
	
	public Integer findIdActiveByName(String name) {
		List<Apartment> apartments = load(contextPath);
		for(Apartment a: apartments) {
			if (a.getName().toLowerCase().equals(name.toLowerCase()) && a.getStatus().equals(ApartmentStatus.ACTIVE)) {
				return a.getId();
			}
		}
		return null;
	}
	
	public List<Apartment> findActive(){
		List<Apartment> allApartments = load(contextPath);
		List<Apartment> apartments = new ArrayList<Apartment>();
		for(Apartment a: allApartments) {
			if(a.getStatus().equals(ApartmentStatus.ACTIVE) && !a.isDeleted()) {
				apartments.add(a);
			}
		}
		return apartments;
	}
	
	public List<Apartment> find(){
		return load(contextPath);
	}
	
	public List<Apartment> findAll(){
		List<Apartment> allApartments = load(contextPath);
		List<Apartment> apartments = new ArrayList<Apartment>();
		for(Apartment a: allApartments) {
			if(!a.isDeleted()) {
				apartments.add(a);
			}
		}
		return apartments;
	}
	
	public Integer getLastId() {
		List<Apartment> allApartments = load(contextPath);
		return allApartments.size();
	}
	
	private List<Apartment> load(String contextPath) {
		BufferedReader in = null;
		List<Apartment> apartments = new ArrayList<Apartment>();
		
		try {
			File file = new File(contextPath + "/apartments.txt");
			in = new BufferedReader(new FileReader(file));
			String line;
			
			while((line = in.readLine()) != null) {
				line = line.trim();
				if(line.equals("") || line.indexOf('#') == 0) {
					continue;
				}
				
				String[] lines = line.split(";");
				
				Integer id = Integer.parseInt(lines[0]);	
				String name = lines[1];
				String stringType = lines[2];
				ApartmentType type = Enum.valueOf(ApartmentType.class, stringType);
				Integer numberOfRooms = Integer.parseInt(lines[3]);
				Integer numberOfGuests = Integer.parseInt(lines[4]);
					
				Integer locationId = Integer.parseInt(lines[5]);
				String hostUsername = lines[6];
					
				Double price = Double.parseDouble(lines[7]);
				Integer checkInTime = Integer.parseInt(lines[8]);
				Integer checkOutTime = Integer.parseInt(lines[9]);
				String statusString = lines[10];
				ApartmentStatus status = Enum.valueOf(ApartmentStatus.class, statusString);
				String amenitiesString = lines[11];
				List<Integer> amenities = new ArrayList<Integer>();
				if(amenitiesString.length() != 0) {
					String[] amenitiesArray = amenitiesString.split(",");
					for(int i = 0; i < amenitiesArray.length; i++) {
						amenities.add(Integer.parseInt(amenitiesArray[i]));
					}
				}
				
				
				boolean deleted = Boolean.parseBoolean(lines[12]);
					
				Apartment apartment = new Apartment(id, name, type, numberOfRooms, numberOfGuests, locationId, hostUsername, price, checkInTime, checkOutTime, status, amenities, deleted);
				apartments.add(apartment);					
					
				
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				}
				catch (Exception e) {}
			}
		}
		return apartments;
	}
	
	public void save(Apartment apartment, String contextPath) throws IOException{
		Files.write(Paths.get(contextPath + "/apartments.txt"), apartment.forFile().getBytes(), StandardOpenOption.APPEND);
	}
	
	public void update(Apartment apartment, String contextPath) throws IOException {
		List<Apartment> apartments = load(contextPath);
		String file = "";
		for(Apartment a: apartments) {
			if(a.getId() == apartment.getId()) {
				a.setName(apartment.getName());
				a.setType(apartment.getType());
				a.setNumberOfRooms(apartment.getNumberOfRooms());
				a.setNumberOfGuests(apartment.getNumberOfGuests());
				a.setLocationId(apartment.getLocationId());
				a.setHostUsername(apartment.getHostUsername());
				a.setPrice(apartment.getPrice());
				a.setCheckInTime(apartment.getCheckInTime());
				a.setCheckOutTime(apartment.getCheckOutTime());
				a.setStatus(apartment.getStatus());
				a.setAmenities(apartment.getAmenities());
				a.setDeleted(apartment.isDeleted());
			}
			file += a.forFile();
		}
		Files.write(Paths.get(contextPath, "/apartments.txt"), file.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
	}

}
