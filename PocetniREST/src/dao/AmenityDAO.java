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

import beans.Amenity;

public class AmenityDAO {

	private String contextPath;
	
	public AmenityDAO() {
		super();
	}

	public AmenityDAO(String contextPath) {
		this.contextPath = contextPath;
	}
	
	public Amenity findById(Integer id){
		List<Amenity> amenities = load(contextPath);
		for(Amenity amenity: amenities) {
			if(amenity.getId() == id) {
				return amenity;
			}
		}
		return null;
	}
	
	public Amenity findByName(String name){
		List<Amenity> amenities = load(contextPath);
		for(Amenity amenity: amenities) {
			if(amenity.getName().equals(name)) {
				return amenity;
			}
		}
		return null;
	}
	
	public List<Amenity> findAll(){
		List<Amenity> allAmenities = load(contextPath);
		List<Amenity> amenities = new ArrayList<Amenity>();
		for(Amenity amenity: allAmenities) {
			if(!amenity.isDeleted()) {
				amenities.add(amenity);
			}
		}
		return amenities;
	}
	
	public Integer getLastId() {
		List<Amenity> amenities = load(contextPath);
		return amenities.size();
	}
	
	private List<Amenity> load(String contextPath) {
		BufferedReader in = null;
		List<Amenity> amenities = new ArrayList<Amenity>();
		
		try {
			File file = new File(contextPath + "/amenities.txt");
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
					String name = st.nextToken().trim();
					boolean deleted = Boolean.parseBoolean(st.nextToken().trim());
					
					Amenity amenity = new Amenity(id, name);
					amenity.setDeleted(deleted);
					amenities.add(amenity);
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
		return amenities;
	}
	
	public void save(Amenity amenity, String contextPath) throws IOException{
		Files.write(Paths.get(contextPath + "/amenities.txt"), amenity.forFile().getBytes(), StandardOpenOption.APPEND);
	}
	
	public void update(Amenity amenity, String contextPath) throws IOException {
		List<Amenity> amenities = load(contextPath);
		String file = "";
		for(Amenity a: amenities) {
			if(a.getId() == amenity.getId()) {
				a.setName(amenity.getName());
				a.setDeleted(amenity.isDeleted());
			}
			file += a.forFile();
		}
		Files.write(Paths.get(contextPath, "/amenities.txt"), file.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
	}
}
