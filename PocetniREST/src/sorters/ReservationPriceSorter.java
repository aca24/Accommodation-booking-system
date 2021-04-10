package sorters;

import java.util.Comparator;

import beans.Reservation;

public class ReservationPriceSorter implements Comparator<Reservation> {

	private String direction;
	
	public ReservationPriceSorter(String direction) {
		this.direction = direction;
	}
	
	@Override
	public int compare(Reservation o1, Reservation o2) {
		if(direction.equals("asc")) {
			return o2.getPrice().compareTo(o1.getPrice());
		}
		else {
			return o1.getPrice().compareTo(o2.getPrice());
		}
	}

}
