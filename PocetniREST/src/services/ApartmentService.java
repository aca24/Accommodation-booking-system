package services;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Address;
import beans.Amenity;
import beans.Apartment;
import beans.Comment;
import beans.DatesForRent;
import beans.Location;
import beans.Reservation;
import beans.User;
import dao.AddressDAO;
import dao.AmenityDAO;
import dao.ApartmentDAO;
import dao.CommentDAO;
import dao.DatesForRentDAO;
import dao.LocationDAO;
import dao.ReservationDAO;
import dto.AddressDTO;
import dto.ApartmentCreateDTO;
import dto.ApartmentDTO;
import dto.ApartmentFilterDTO;
import dto.ApartmentProfileDTO;
import dto.ApartmentUpdateDTO;
import dto.CommentDTO;
import enums.ApartmentStatus;
import enums.ApartmentType;
import enums.ReservationStatus;
import enums.UserRole;
import sorters.ApartmentPriceSorter;

@Path("apartments")
public class ApartmentService {
	
	@Context
	ServletContext context;
	
	@GET
	@Path("") 
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAll(@Context HttpServletRequest request){
		
		User user = (User)request.getSession().getAttribute("user");
		if(user == null || !user.getRole().equals(UserRole.ADMIN)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		ApartmentDAO apartmentDao = new ApartmentDAO(context.getRealPath(""));
		List<Apartment> apartments = apartmentDao.findAll();
		
		List<ApartmentDTO> apartmentsDTO = createDTO(apartments);
		
		return Response.status(Response.Status.OK).entity(apartmentsDTO).build();
	}
	
	@GET
	@Path("/findByName/{apartmentName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findIdByName(@Context HttpServletRequest request, @PathParam("apartmentName") String apartmentName) {
		User user = (User)request.getSession().getAttribute("user");
		if(!user.getRole().equals(UserRole.ADMIN)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		ApartmentDAO apartmentDAO = new ApartmentDAO(context.getRealPath(""));
		Integer apartmentId = apartmentDAO.findIdByName(apartmentName);
		if (apartmentId == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		return Response.status(Response.Status.OK).entity(apartmentId).build();
	}
	
	@GET
	@Path("/findByNameHost/{apartmentName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByNameHost(@Context HttpServletRequest request, @PathParam("apartmentName") String apartmentName) {
		User user = (User)request.getSession().getAttribute("user");
		if(!user.getRole().equals(UserRole.HOST)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		String username = user.getUsername();
		ApartmentDAO apartmentDAO = new ApartmentDAO(context.getRealPath(""));
		Integer apartmentId = apartmentDAO.findIdHostByName(apartmentName, username);
		if (apartmentId == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		return Response.status(Response.Status.OK).entity(apartmentId).build();
	}
	
	@GET
	@Path("/findByNameActive/{apartmentName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByNameActive(@Context HttpServletRequest request, @PathParam("apartmentName") String apartmentName) {
		ApartmentDAO apartmentDAO = new ApartmentDAO(context.getRealPath(""));
		Integer apartmentId = apartmentDAO.findIdActiveByName(apartmentName);
		if (apartmentId == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		return Response.status(Response.Status.OK).entity(apartmentId).build();
	}
	
	@GET
	@Path("searchById/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchById(@Context HttpServletRequest request, @PathParam("id") Integer id) {
		ApartmentDAO apartmentDAO = new ApartmentDAO(context.getRealPath(""));
		Apartment apartment = apartmentDAO.findById(id);
		List<Apartment> apartments = new ArrayList<Apartment>();
		apartments.add(apartment);
		List<ApartmentDTO> apartmentsDTO = createDTO(apartments);
		
		return Response.status(Response.Status.OK).entity(apartmentsDTO).build();
	}
	
	@GET
	@Path("/apartmentsActive")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findActive() {
				
		ApartmentDAO apartmentDao = new ApartmentDAO(context.getRealPath(""));
		List<Apartment> apartments = apartmentDao.findActive();
		
		List<ApartmentDTO> apartmentsDTO = createDTO(apartments);
		
		return Response.status(Response.Status.OK).entity(apartmentsDTO).build();
	}
	
	@GET
	@Path("/hostApartmentsInactive")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findInactiveByHost(@Context HttpServletRequest request) {
				
		User user = (User)request.getSession().getAttribute("user");
		if(user == null || !user.getRole().equals(UserRole.HOST)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		String username = user.getUsername();
		ApartmentDAO apartmentDao = new ApartmentDAO(context.getRealPath(""));
		List<Apartment> apartments = apartmentDao.findInactiveByHost(username);
		
		List<ApartmentDTO> apartmentsDTO = createDTO(apartments);
		
		return Response.status(Response.Status.OK).entity(apartmentsDTO).build();
	}
	
	@GET
	@Path("/hostApartmentsActive")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findActiveByHost(@Context HttpServletRequest request) {
				
		User user = (User)request.getSession().getAttribute("user");
		if(user == null || !user.getRole().equals(UserRole.HOST)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		String username = user.getUsername();
		ApartmentDAO apartmentDao = new ApartmentDAO(context.getRealPath(""));
		List<Apartment> apartments = apartmentDao.findActiveByHost(username);
		
		List<ApartmentDTO> apartmentsDTO = createDTO(apartments);
		
		return Response.status(Response.Status.OK).entity(apartmentsDTO).build();
	}
	
	@PUT
	@Path("/setActive/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setActive(@Context HttpServletRequest request, @PathParam("id") Integer id) {
		User user = (User)request.getSession().getAttribute("user");
		if(user == null || !user.getRole().equals(UserRole.ADMIN)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		ApartmentDAO apartmentDAO = new ApartmentDAO(context.getRealPath(""));
		Apartment apartment = apartmentDAO.findById(id);
		apartment.setStatus(ApartmentStatus.ACTIVE);
		try {
			apartmentDAO.update(apartment, context.getRealPath(""));
		} catch (IOException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.status(Response.Status.OK).build();
	}
	
	@POST
	@Path("/filter/{status}") // radi
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response filter(@Context HttpServletRequest request, ApartmentFilterDTO filterDTO, @PathParam("status") String status, @QueryParam("sort") String sort) {
		
		User user = (User)request.getSession().getAttribute("user");
		
		LocationDAO locationDao = new LocationDAO(context.getRealPath(""));
		AddressDAO addressDao = new AddressDAO(context.getRealPath(""));
		
		List<Apartment> allApartments = getApartments(user, status);
		List<ApartmentDTO> apartments = new ArrayList<ApartmentDTO>();
		List<String> amenitiesString = filterDTO.getAmenities();
		
		for(Apartment a: allApartments) { // prolazimo kroz sve apartmane koje filtriramo
			List<Amenity> amenities = new ArrayList<Amenity>();
			Location location = locationDao.findById(a.getLocationId());
			Address address = addressDao.findById(location.getAddressId());
			
			if(filterDTO.getCity().equals(address.getCity()) || filterDTO.getCity().isEmpty()) {
				if(filterDTO.getType().equals(a.getType().toString()) || filterDTO.getType().isEmpty()) {
					List<Integer> nor = filterDTO.getNumberOfRooms();
					if(nor.contains(a.getNumberOfRooms()) || nor.isEmpty() || (nor.contains(5) && a.getNumberOfRooms() >= 5)) {
						List<Integer> nog = filterDTO.getNumberOfGuests();
						if(nog.contains(a.getNumberOfGuests()) || nog.isEmpty() || (nog.contains(5) && a.getNumberOfGuests() >= 5)) {
							CommentDAO commentDao = new CommentDAO(context.getRealPath(""));
							List<Comment> comments = commentDao.findByApartment(a.getId());
							double rating = 0;
							int sum = 0;
							int i;
							int reviews = comments.size();
							for(i = 0; i < reviews; i++) {
								sum += comments.get(i).getRating();
							}
							rating = i != 0 ? sum/i : 0;
							if(Integer.parseInt(filterDTO.getRating()) <= rating) { // rating je u najgorem slucaju 1 sto znaci da svaki apartman ulazi u rezultat
								if(Integer.parseInt(filterDTO.getMinPrice()) <= a.getPrice() || filterDTO.getMinPrice().equals("-1")) { // ako je vrednost -1 to znaci da cena nije setovana
									if(Integer.parseInt(filterDTO.getMaxPrice()) >= a.getPrice() || filterDTO.getMaxPrice().equals("-1")) {
										AmenityDAO amenityDao = new AmenityDAO(context.getRealPath(""));
										List<Integer> amenitiesIDs = a.getAmenities(); // uzimamo sve amenitije koje ima i-ti apartman
										for(Integer id: amenitiesIDs) {
											amenities.add(amenityDao.findById(id)); // pronalazimo ih u bazi i smestamo u listu
										}
										int provera = 0;
										for(String filterAm: amenitiesString) { // prolazimo kroz listu zadatih amenitija
											for(Amenity amenity: amenities) { // i kroz listu koje nas apartman ima
												if(amenity.getName().equals(filterAm)) {
													provera++;
												}
											}
										}
										if(provera == amenitiesString.size()) { // nas apartman ima sve zadate amenitije
											boolean available = true;
											if(!filterDTO.getStartDate().equals("")) {
												List<Date> availableDates = findAvailableDates(a);
												
												SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
												Date startDate;
												Date endDate;
												try {
													startDate = sdf.parse(filterDTO.getStartDate());
													endDate = sdf.parse(filterDTO.getEndDate());
												} catch (ParseException e) {
													return Response.status(Response.Status.BAD_REQUEST).build();
												}
												
												List<Date> filterDates = new ArrayList<Date>();
												while(!startDate.after(endDate)) {
													filterDates.add(startDate);
													Calendar c = Calendar.getInstance();
													c.setTime(startDate);
													c.add(Calendar.DATE, 1);
													startDate = c.getTime();
												}
												
												for(Date filterDate: filterDates) {
													if(!availableDates.contains(filterDate)) {
														available = false;
													}
												}
											}
											
											if(available) {
												ApartmentDTO apartmentDTO = new ApartmentDTO(a.getId(),a.getName(), a.getType(), a.getStatus(), a.getNumberOfRooms(), a.getNumberOfGuests(), address.getCity(), a.getPrice(), reviews, rating);
												apartments.add(apartmentDTO);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		if(!sort.equals("none")) {
			apartments.sort(new ApartmentPriceSorter(sort));
		}
		
		return Response.status(Response.Status.OK).entity(apartments).build();
	}
	
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(@Context HttpServletRequest request, @PathParam("id") Integer id) {
		
		User user = (User)request.getSession().getAttribute("user");
		if(user == null || user.getRole().equals(UserRole.GUEST)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		ApartmentDAO apartmentDao = new ApartmentDAO(context.getRealPath(""));
		Apartment apartment = apartmentDao.findById(id);
		
		if(apartment == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		apartment.setDeleted(true);
		try {
			apartmentDao.update(apartment, context.getRealPath(""));
		} catch (IOException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
		return Response.status(Response.Status.OK).entity("Apartment is deleted.").build();
	}
	
	@GET
	@Path("/update/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findForUpdate(@Context HttpServletRequest request, @PathParam("id") Integer id) {
		
		User user = (User)request.getSession().getAttribute("user");
		if(user == null || user.getRole().equals(UserRole.GUEST)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		ApartmentDAO apartmentDao = new ApartmentDAO(context.getRealPath(""));
		Apartment a = apartmentDao.findById(id);
		
		if(a == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		// address
		LocationDAO locationDao = new LocationDAO(context.getRealPath(""));
		Location location = locationDao.findById(a.getLocationId());
		AddressDAO addressDao = new AddressDAO(context.getRealPath(""));
		Address address = addressDao.findById(location.getAddressId());
		
		// Dates for rent
		DatesForRentDAO dfrDao = new DatesForRentDAO(context.getRealPath(""));
		DatesForRent dfr = dfrDao.findByApartmentId(a.getId());
		List<String> dates = new ArrayList<String>();
		if(dfr != null) {
			dates = dfr.datesToListString(dfr.getDates());
		}
		
		ApartmentUpdateDTO apartmentDTO = new ApartmentUpdateDTO(a.getName(), a.getType(), a.getStatus(), address.getCity(), address.getStreet(), address.getNumber(), address.getPostalCode(), a.getPrice(), a.getNumberOfRooms(), a.getNumberOfGuests(), a.getCheckInTime(), a.getCheckOutTime(), a.getAmenities(), dates);
		
		return Response.status(Response.Status.OK).entity(apartmentDTO).build();
	}
	
	@POST
	@Path("create") // create/update
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createNew(@Context HttpServletRequest request, ApartmentCreateDTO a, @QueryParam("id") Integer updateId) {
		
		User user = (User)request.getSession().getAttribute("user");
		
		ApartmentDAO apartmentDao = new ApartmentDAO(context.getRealPath(""));
		Integer apartmentId = updateId == -1 ? apartmentDao.getLastId() + 1 : updateId;
		
		// kreiranje adrese
		AddressDAO addressDao = new AddressDAO(context.getRealPath(""));
		LocationDAO locationDao = new LocationDAO(context.getRealPath(""));
		
		Address address = new Address(null, a.getStreet(), a.getNumber(), a.getCity(), a.getPostalCode());
		Location location;
		Integer id = addressDao.exists(address);
		if(id != null) { // adresa vec postoji pa je necemo upisivati u fajl
			location = locationDao.findByAddressId(id);
		}
		else { // moramo upisati adresu, a zatim lokaciju
			int addressId = addressDao.getLastId() + 1;
			address.setId(addressId);
			int locationId = locationDao.getLastId() + 1;
			location = new Location(locationId, Double.parseDouble(a.getLatitude()), Double.parseDouble(a.getLongitude()), addressId);
			try {
				addressDao.save(address, context.getRealPath(""));
				locationDao.save(location, context.getRealPath(""));
			} catch (IOException e) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured, please try again later.").build();
			}
		}
		ApartmentType type = Enum.valueOf(ApartmentType.class, a.getType());
		ApartmentStatus status = Enum.valueOf(ApartmentStatus.class, a.getStatus());
		String[] checkInTokens = a.getCheckInTime().split(":");
		Integer checkIn = Integer.parseInt(checkInTokens[0]);
		String[] checkOutTokens = a.getCheckOutTime().split(":");
		Integer checkOut = Integer.parseInt(checkOutTokens[0]);
		Apartment apartment = new Apartment(apartmentId, a.getName(), type, a.getNumberOfRooms(), a.getNumberOfGuests(), location.getId(), user.getUsername(), a.getPrice(), checkIn, checkOut, status, a.getAmenities(), false);
		try {
			if(updateId == -1)
				apartmentDao.save(apartment, context.getRealPath(""));
			else
				apartmentDao.update(apartment, context.getRealPath(""));
		} catch (IOException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured, please try again later.").build();
		}
		
		// Upisivanje dostupnih datuma
		DatesForRentDAO datesForRentDao = new DatesForRentDAO(context.getRealPath(""));
		Integer datesForRentId = updateId == -1 ? datesForRentDao.getLastId() + 1 : datesForRentDao.findByApartmentId(apartment.getId()).getId();
		List<Date> datesForRent = new ArrayList<Date>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		String separateDates = a.getDates();
		String start = a.getStartDate();
		String end = a.getEndDate();
		if(start != "") {
			Date startDate;
			Date endDate;
			try {
				startDate = sdf.parse(start);
				endDate = sdf.parse(end);
			} catch (ParseException e1) {
				return Response.status(Response.Status.BAD_REQUEST).entity("An error occured, please try again later.").build();
			}
		
			while(!startDate.after(endDate)) {
				datesForRent.add(startDate);
				Calendar c = Calendar.getInstance();
				c.setTime(startDate);
				c.add(Calendar.DATE, 1);
				startDate = c.getTime();
			}
		}
		
		if(!separateDates.isEmpty()) {
			String[] dates = separateDates.split(",");
			
			for(int i = 0; i < dates.length; i++) {
				try {
					Date d = sdf.parse(dates[i]);
					if(!datesForRent.contains(d)) {
						datesForRent.add(d);
					}		
				} catch (ParseException e) {
					return Response.status(Response.Status.BAD_REQUEST).entity("An error occured, please try again later.").build();
				}
			}
		}
			
		DatesForRent dfr = new DatesForRent(datesForRentId, apartment.getId(), datesForRent);
			
		try {
			if(updateId == -1)
				datesForRentDao.save(dfr, context.getRealPath(""));
			else
				datesForRentDao.update(dfr, context.getRealPath(""));
		} catch (IOException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured, please try again later.").build();
		}
		
		new File(context.getRealPath("") + "/img/apartment" + apartment.getId()).mkdir();

		return Response.status(Response.Status.OK).entity(apartment.getId()).build();
	}
	
	@GET
	@Path("/{id}") // Apartment Profile
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") Integer id) {
		ApartmentDAO apartmentDao = new ApartmentDAO(context.getRealPath(""));
		
		
		Apartment a = apartmentDao.findById(id);
		if(a == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		// address
		LocationDAO locationDao = new LocationDAO(context.getRealPath(""));
		Location location = locationDao.findById(a.getLocationId());
		AddressDAO addressDao = new AddressDAO(context.getRealPath(""));
		Address address = addressDao.findById(location.getAddressId());
		AddressDTO addressDTO = new AddressDTO(address.getCity(), address.getStreet(), address.getNumber(), location.getLongitude(), location.getLatitude());
		
		// comments, rating
		CommentDAO commentDao = new CommentDAO(context.getRealPath(""));
		List<Comment> comments = commentDao.findByApartment(a.getId());
		List<CommentDTO> commentsDTO = new ArrayList<CommentDTO>();
		
		double rating = 0;
		int sum = 0;
		int i;
		int reviews = comments.size();
		for(i = 0; i < reviews; i++) {
			Comment c = comments.get(i);
			if(c.isApproved()) {
				CommentDTO commentDTO = new CommentDTO(c.getId(), c.getGuest(), c.getText(), c.getRating());
				commentsDTO.add(commentDTO);
			}
			sum += comments.get(i).getRating();
		}
		rating = i != 0 ? sum/i : 0;
		Collections.reverse(commentsDTO);
		// available dates
		List<Date> availableDates = findAvailableDates(a);
		
		// amenities
		AmenityDAO amenityDao = new AmenityDAO(context.getRealPath(""));
		List<Amenity> amenities = new ArrayList<Amenity>();
		for(Integer amenityId: a.getAmenities()) {
			Amenity amenity = amenityDao.findById(amenityId);
			amenities.add(amenity);
		}
	
		ApartmentProfileDTO apartmentDTO = new ApartmentProfileDTO(a.getId(), a.getName(), addressDTO, location.getLongitude(), location.getLatitude(), rating, reviews, a.getType(), a.getNumberOfRooms(), a.getNumberOfGuests(), a.getPrice(), a.getCheckInTime(), a.getCheckOutTime(), availableDates, commentsDTO, amenities);
				
		return Response.status(Response.Status.OK).entity(apartmentDTO).build();
	}
	
	private List<ApartmentDTO> createDTO(List<Apartment> apartments) {
		
		CommentDAO commentDao = new CommentDAO(context.getRealPath(""));
		LocationDAO locationDao = new LocationDAO(context.getRealPath(""));
		AddressDAO addressDao = new AddressDAO(context.getRealPath(""));

		List<ApartmentDTO> apartmentsDTO = new ArrayList<ApartmentDTO>();
		for(Apartment a: apartments) {
			List<Comment> comments = commentDao.findByApartment(a.getId());
			double rating = 0;
			int sum = 0;
			int i;
			int reviews = comments.size();
			for(i = 0; i < reviews; i++) {
				sum += comments.get(i).getRating();
			}
			rating = i != 0 ? sum/i : 0;
			
			Location location = locationDao.findById(a.getLocationId());
			Address address = addressDao.findById(location.getAddressId());
			ApartmentDTO apartmentDTO = new ApartmentDTO(a.getId(),a.getName(), a.getType(), a.getStatus(), a.getNumberOfRooms(), a.getNumberOfGuests(), address.getCity(), a.getPrice(), reviews, rating);
			apartmentsDTO.add(apartmentDTO);
		}
		return apartmentsDTO;
	}
		
	private List<Apartment> getApartments(User user,String status){
		ApartmentDAO apartmentDao = new ApartmentDAO(context.getRealPath(""));
		List<Apartment> apartments = new ArrayList<Apartment>();
		
		if(user != null && user.getRole().equals(UserRole.HOST)) { // ako je domacin vracamo samo njegove apartmane
			apartments = apartmentDao.findActiveByHost(user.getUsername());
		}
		else if(user != null && user.getRole().equals(UserRole.ADMIN)){
			apartments = apartmentDao.findAll();
		}
		else {
			apartments = apartmentDao.findActive();
		}
		
		return apartments;
	}
	
	private List<Date> findAvailableDates(Apartment apartment) {
		DatesForRentDAO dfrDao = new DatesForRentDAO(context.getRealPath(""));
		ReservationDAO reservationDao = new ReservationDAO(context.getRealPath(""));
		
		DatesForRent dfr = dfrDao.findByApartmentId(apartment.getId());
		List<Date> datesForRent = new ArrayList<Date>();
		List<Date> rentedDates = new ArrayList<Date>(); // lista izdatih datuma
		List<Date> availableDates = new ArrayList<Date>(); // lista dostupnih datuma
		if(dfr != null) {
			datesForRent = dfr.getDates(); // datumi za izdavanje
			
			List<Reservation> reservations = reservationDao.findByApartment(apartment.getId());
			for(Reservation r: reservations) {
				if(r.getStatus().equals(ReservationStatus.ACCEPTED) || r.getStatus().equals(ReservationStatus.CREATED)) {
					Date startDate = r.getDate(); // ako je rezervacija kreirana ili prihvacenja dodajemo u listu izdatih datuma
					rentedDates.add(startDate);
					for(int j = 1; j < r.getNumberOfStays(); j++) { // pocetni datum plus onoliko dana koliko ima nocenja
						Calendar c = Calendar.getInstance();
						c.setTime(startDate);
						c.add(Calendar.DATE, 1);
						startDate = c.getTime();
						rentedDates.add(startDate);
					}
						
				}
			}
		}
		
		for(Date forRent: datesForRent) {
			if(!rentedDates.contains(forRent) && !forRent.before(new Date())) {
				availableDates.add(forRent);
			}
		}
		
		return availableDates;
	}
}
