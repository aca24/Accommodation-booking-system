package sorters;

import java.util.Comparator;

import dto.ApartmentDTO;

public class ApartmentPriceSorter implements Comparator<ApartmentDTO>{
	
	private String direction;
	
	public ApartmentPriceSorter(String direction) {
		this.direction = direction;
	}

	@Override
	public int compare(ApartmentDTO o1, ApartmentDTO o2) {
		if(direction.equals("asc")) {
			return o2.getPrice().compareTo(o1.getPrice());
		}
		else {
			return o1.getPrice().compareTo(o2.getPrice());
		}		
	}
}
