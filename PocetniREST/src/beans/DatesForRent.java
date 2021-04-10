package beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatesForRent {

	private Integer id;
	private Integer apartmentId;
	private List<Date> dates;
	
	public DatesForRent() {
		super();
	}
	public DatesForRent(Integer id, Integer apartmentId, List<Date> dates) {
		super();
		this.id = id;
		this.apartmentId = apartmentId;
		this.dates = dates;
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
	public List<Date> getDates() {
		return dates;
	}
	public void setDates(List<Date> dates) {
		this.dates = dates;
	}
	
	private String datesToString(List<Date> dates) {
		if(!dates.isEmpty()) {
			String s = "";
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			for(Date d: dates) {
				s += sdf.format(d) + ",";
			}
			
			return s.substring(0, s.length() - 1);
		}
		return "";
	}
	
	public List<String> datesToListString(List<Date> dates){
		if(!dates.isEmpty()) {
			List<String> s = new ArrayList<String>();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			for(Date d: dates) {
				s.add(sdf.format(d));
			}
			
			return s;
		}
		return null;
	}
	
	public String forFile() {
		return this.getId() + ";" + this.getApartmentId() + ";" + 
				datesToString(this.getDates()) + "\r\n";
	}
}
