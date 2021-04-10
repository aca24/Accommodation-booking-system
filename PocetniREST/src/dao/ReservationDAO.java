package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import beans.Reservation;
import enums.ReservationStatus;

public class ReservationDAO {

	//private Map<Integer, Reservation> reservations = new HashMap<>();
	private String contextPath;

	public ReservationDAO() {
		super();
	}

	public ReservationDAO(String contextPath) {
		this.contextPath = contextPath;
	}
	
	public List<Reservation> findByApartment(Integer apartmentID){
		List<Reservation> allReservations = load(contextPath);
		List<Reservation> reservations = new ArrayList<Reservation>();
		for(Reservation r: allReservations) {
			if(r.getApartmentId().equals(apartmentID)) {
				reservations.add(r);
			}
		}
		return reservations;
	}
	
	public List<Reservation> findByGuest(String guest){
		List<Reservation> allReservations = load(contextPath);
		List<Reservation> reservations = new ArrayList<Reservation>();
		for(Reservation r: allReservations) {
			if(r.getGuest().equals(guest)) {
				reservations.add(r);
			}
		}
		return reservations;
	}
	
	public Reservation findById(Integer id) {
		List<Reservation> reservations = load(contextPath);
		for(Reservation r: reservations) {
			if(r.getId().equals(id)) {
				return r;
			}
		}
		return null;
	}
	
	public Integer getLastId(){
		List<Reservation> reservations = load(contextPath);
		return reservations.size();
	}
	
	public List<Reservation> findAll(){
		return load(contextPath);
	}
	
	private List<Reservation> load(String contextPath) {
		BufferedReader in = null;
		List<Reservation> reservations = new ArrayList<Reservation>();
		
		try {
			File file = new File(contextPath + "/reservations.txt");
			in = new BufferedReader(new FileReader(file));
			String line;
			
			while((line = in.readLine()) != null) {
				line = line.trim();
				if(line.equals("") || line.indexOf('#') == 0) {
					continue;
				}
				
				String[] lines = line.split(";");
				
				Integer id = Integer.parseInt(lines[0]);
				Integer apartmentId = Integer.parseInt(lines[1]);
				String guest = lines[2];
					
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				Date date = formatter.parse(lines[3]);
					
				Integer numberOfStays = Integer.parseInt(lines[4]);
				Double price = Double.parseDouble(lines[5]);
				String message = lines[6];
				String statusString = lines[7];
				ReservationStatus status = Enum.valueOf(ReservationStatus.class, statusString);
					
				Reservation reservation = new Reservation(id, apartmentId, date, numberOfStays, price, message, guest, status);
				reservations.add(reservation);
				
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
		
		return reservations;
	}
	
	public void save(Reservation reservation, String contextPath) throws IOException{
		Files.write(Paths.get(contextPath + "/reservations.txt"), reservation.forFile().getBytes(), StandardOpenOption.APPEND);
	}
	
	public void update(Reservation reservation, String contextPath) throws IOException {
		List<Reservation> reservations = load(contextPath);
		String file = "";
		for(Reservation r: reservations) {
			if(r.getId() == reservation.getId()) {
				r.setStatus(reservation.getStatus());
			}
			file += r.forFile();
		}
		Files.write(Paths.get(contextPath, "/reservations.txt"), file.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
	}
}
