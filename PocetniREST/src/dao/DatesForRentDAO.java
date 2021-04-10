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

import beans.DatesForRent;

public class DatesForRentDAO {
	
	private String contextPath;
	
	public DatesForRentDAO() {
		super();
	}
	
	public DatesForRentDAO(String contextPath) {
		this.contextPath = contextPath;
	}
	
	public DatesForRent findByApartmentId(Integer id) {
		List<DatesForRent> datesForRent = load(contextPath);
		for(DatesForRent rd: datesForRent) {
			if(rd.getApartmentId() == id) {
				return rd;
			}
		}
		return null;
	}
	
	public Integer getLastId() {
		List<DatesForRent> dates = load(contextPath);
		return dates.size();
	}
	
	public List<DatesForRent> findAll() {
		return load(contextPath);
	}
	
	private List<DatesForRent> load(String contextPath) {
		BufferedReader in = null;
		List<DatesForRent> datesForRent = new ArrayList<DatesForRent>();
		
		try {
			File file = new File(contextPath + "/datesForRent.txt");
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
					
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				List<Date> dates = new ArrayList<Date>();
				
				if(lines.length > 2) {
					String d = lines[2];
					String[] tokens = d.split(",");
					
					
					for(int i = 0; i < tokens.length; i++) {
						Date date = formatter.parse(tokens[i]);
						dates.add(date);
					}
				}
				
				DatesForRent rd = new DatesForRent(id, apartmentId, dates);
				datesForRent.add(rd);
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
		return datesForRent;
	}
	
	public void save(DatesForRent dates, String contextPath) throws IOException{
		Files.write(Paths.get(contextPath + "/datesForRent.txt"), dates.forFile().getBytes(), StandardOpenOption.APPEND);
	}
	
	public void update(DatesForRent dates, String contextPath) throws IOException {
		List<DatesForRent> allDates = load(contextPath);
		String file = "";
		for(DatesForRent d: allDates) {
			if(d.getId() == dates.getId()) {
				d.setDates(dates.getDates());
			}
			file += d.forFile();
		}
		Files.write(Paths.get(contextPath, "/datesForRent.txt"), file.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
	}

}
