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
import java.util.StringTokenizer;

import beans.Location;

public class LocationDAO {
	
	private String contextPath;
	
	public LocationDAO() {
		super();
	}

	public LocationDAO(String contextPath) {
		this.contextPath = contextPath;
	}
	
	public Location findById(Integer id){
		List<Location> locations = load(contextPath);
		for(Location location: locations) {
			if(location.getId() == id) {
				return location;
			}
		}
		return null;
	}
	
	public Integer getLastId() {
		List<Location> locations = load(contextPath);
		return locations.size();
	}
	
	public List<Location> findAll(){
		return load(contextPath);
	}
	
	private List<Location> load(String contextPath) {
		BufferedReader in = null;
		List<Location> locations = new ArrayList<Location>();
		
		try {
			File file = new File(contextPath + "/locations.txt");
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			
			while((line = in.readLine()) != null) {
				line = line.trim();
				if(line.equals("") || line.indexOf('#') == 0) {
					continue;
				}
				st = new StringTokenizer(line, ";");
				while(st.hasMoreTokens()) {
					Integer id = Integer.parseInt(st.nextToken().trim());
					Integer address = Integer.parseInt(st.nextToken().trim());
					Double longitude = Double.parseDouble(st.nextToken().trim());
					Double latitude = Double.parseDouble(st.nextToken().trim());
					
					Location location = new Location(id, longitude, latitude, address);
					locations.add(location);
				}
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
		return locations;
	}
	
	public void save(Location location, String contextPath) throws IOException{
		Files.write(Paths.get(contextPath + "/locations.txt"), location.forFile().getBytes(), StandardOpenOption.APPEND);
	}

	public Location findByAddressId(Integer id){
		List<Location> locations = load(contextPath);
		for(Location location: locations) {
			if(location.getAddressId() == id) {
				return location;
			}
		}
		return null;
	}
}
