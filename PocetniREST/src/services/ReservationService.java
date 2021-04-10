package services;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Apartment;
import beans.DatesForRent;
import beans.Reservation;
import beans.User;
import dao.ApartmentDAO;
import dao.DatesForRentDAO;
import dao.ReservationDAO;
import dto.ReservationAdminDTO;
import dto.ReservationDTO;
import dto.ReservationGuestDTO;
import dto.ReservationHostDTO;
import enums.ReservationStatus;
import enums.UserRole;
import sorters.ReservationPriceSorter;


@Path("reservations")
public class ReservationService {

	@Context
	ServletContext context;
	
	@GET
	@Path("/getAll")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(@Context HttpServletRequest request) {
		User user = (User)request.getSession().getAttribute("user");
		if(!user.getRole().equals(UserRole.ADMIN)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		ReservationDAO reservationDAO = new ReservationDAO(context.getRealPath(""));
		List<Reservation> reservations = reservationDAO.findAll();
		List<ReservationAdminDTO> reservationsDTO = new ArrayList<ReservationAdminDTO>();
		ApartmentDAO apartmentDAO = new ApartmentDAO(context.getRealPath(""));
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		for (Reservation reservation : reservations) {
			Apartment apartment = apartmentDAO.findById(reservation.getApartmentId());
			String apartmentName = apartment.getName();
			String host = apartment.getHostUsername();
			String date = sdf.format(reservation.getDate());
			
			ReservationAdminDTO reservationAdminDTO = new ReservationAdminDTO(reservation.getId(), apartmentName, host, reservation.getGuest(), date, reservation.getNumberOfStays(), reservation.getPrice(), reservation.getStatus());
			reservationsDTO.add(reservationAdminDTO);
		}
		return Response.status(Response.Status.OK).entity(reservationsDTO).build();
	}
	
	@GET
	@Path("/getReservationsGuest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getReservationsByGuest(@Context HttpServletRequest request) {
		User user = (User)request.getSession().getAttribute("user");
		if(user == null || !user.getRole().equals(UserRole.GUEST)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		String username = user.getUsername();
		ReservationDAO reservationDAO = new ReservationDAO(context.getRealPath(""));
		List<Reservation> reservations = reservationDAO.findByGuest(username);
		List<ReservationGuestDTO> myReservations = new ArrayList<ReservationGuestDTO>();
		ApartmentDAO apartmentDAO = new ApartmentDAO(context.getRealPath(""));
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		for (Reservation reservation : reservations) {
			Apartment apartment = apartmentDAO.findById(reservation.getApartmentId());
			String apartmentName = apartment.getName();
			String date = sdf.format(reservation.getDate());

			ReservationGuestDTO reservationGuestDTO = new ReservationGuestDTO(reservation.getId(), apartmentName, date,
					reservation.getNumberOfStays(), reservation.getPrice(), reservation.getStatus());
			myReservations.add(reservationGuestDTO);
		}
		
		return Response.status(Response.Status.OK).entity(myReservations).build();

	}
	
	@GET
	@Path("/getReservationsHost")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getReservationsByHost(@Context HttpServletRequest request) {
		User user = (User)request.getSession().getAttribute("user");
		if(user == null || !user.getRole().equals(UserRole.HOST)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		String username = user.getUsername();
		ApartmentDAO apartmentDAO = new ApartmentDAO(context.getRealPath(""));
		ReservationDAO reservationDAO = new ReservationDAO(context.getRealPath(""));
		List<Apartment> apartments = apartmentDAO.findByHost(username);
		List<Reservation> allReservations = new ArrayList<Reservation>();
		
		for (Apartment a : apartments) {
			List<Reservation> reservations = reservationDAO.findByApartment(a.getId());
			allReservations.addAll(reservations);
		}
		
		List<ReservationHostDTO> myReservations = new ArrayList<ReservationHostDTO>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		for (Reservation reservation : allReservations) {
			Apartment apartment = apartmentDAO.findById(reservation.getApartmentId());
			String apartmentName = apartment.getName();
			String date = sdf.format(reservation.getDate());
			
			ReservationHostDTO reservationHostDTO = new ReservationHostDTO(reservation.getId(), apartmentName, 
					reservation.getGuest(), date, reservation.getNumberOfStays(), 
					reservation.getPrice(), reservation.getMessage(), reservation.getStatus());
			myReservations.add(reservationHostDTO);
		}
		
		return Response.status(Response.Status.OK).entity(myReservations).build();
	}
	
	@PUT
	@Path("/cancel/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cancelReservation(@Context HttpServletRequest request, @PathParam("id") Integer id) {
		User user = (User)request.getSession().getAttribute("user");
		if(!user.getRole().equals(UserRole.GUEST)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		} 
		ReservationDAO reservationDAO = new ReservationDAO(context.getRealPath(""));
		DatesForRentDAO datesForRentDAO = new DatesForRentDAO(context.getRealPath(""));
		Reservation reservation = reservationDAO.findById(id);
		reservation.setStatus(ReservationStatus.CANCELED);
		try {
			DatesForRent datesForRent = datesForRentDAO.findByApartmentId(reservation.getApartmentId());
			List<Date> dates = datesForRent.getDates();
			List<Date> newDates = new ArrayList<Date>();
			Date date = reservation.getDate();
			newDates.add(date);
			for(int i = 1; i < reservation.getNumberOfStays(); i++) {
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				c.add(Calendar.DATE, 1);
				date = c.getTime();
				newDates.add(date);
			}
			dates.addAll(newDates);
			datesForRentDAO.update(datesForRent, context.getRealPath(""));
			reservationDAO.update(reservation, context.getRealPath(""));
		} catch (IOException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured, please try again later.").build();
		}
		return Response.status(Response.Status.OK).entity(reservation).build();
	}
	
	@PUT
	@Path("/reject/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response rejectReservation(@Context HttpServletRequest request, @PathParam("id") Integer id) {
		User user = (User)request.getSession().getAttribute("user");
		if(!user.getRole().equals(UserRole.HOST)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		ReservationDAO reservationDAO = new ReservationDAO(context.getRealPath(""));
		DatesForRentDAO datesForRentDAO = new DatesForRentDAO(context.getRealPath(""));
		Reservation reservation = reservationDAO.findById(id);
		reservation.setStatus(ReservationStatus.REJECTED);
		try {
			DatesForRent datesForRent = datesForRentDAO.findByApartmentId(reservation.getApartmentId());
			List<Date> dates = datesForRent.getDates();
			List<Date> newDates = new ArrayList<Date>();
			Date date = reservation.getDate();
			newDates.add(date);
			for(int i = 1; i < reservation.getNumberOfStays(); i++) {
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				c.add(Calendar.DATE, 1);
				date = c.getTime();
				newDates.add(date);
			}
			dates.addAll(newDates);
			datesForRentDAO.update(datesForRent, context.getRealPath(""));
			reservationDAO.update(reservation, context.getRealPath(""));
		} catch (IOException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured, please try again later.").build();
		}
		
		return Response.status(Response.Status.OK).entity(reservation).build();
	}
	
	@PUT
	@Path("/accept/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response acceptReservation(@Context HttpServletRequest request, @PathParam("id") Integer id) {
		User user = (User)request.getSession().getAttribute("user");
		if(!user.getRole().equals(UserRole.HOST)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		ReservationDAO reservationDAO = new ReservationDAO(context.getRealPath(""));
		Reservation reservation = reservationDAO.findById(id);
		reservation.setStatus(ReservationStatus.ACCEPTED);
		try {
			reservationDAO.update(reservation, context.getRealPath(""));
		} catch (IOException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured, please try again later.").build();
		}
		return Response.status(Response.Status.OK).entity(reservation).build();
	}
	
	@PUT
	@Path("/finish/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response finishReservation(@Context HttpServletRequest request, @PathParam("id") Integer id) {
		User user = (User)request.getSession().getAttribute("user");
		if(!user.getRole().equals(UserRole.HOST)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		ReservationDAO reservationDAO = new ReservationDAO(context.getRealPath(""));
		Reservation reservation = reservationDAO.findById(id);
		reservation.setStatus(ReservationStatus.FINISHED);
		try {
			reservationDAO.update(reservation, context.getRealPath(""));
		} catch (IOException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured, please try again later.").build();
		}
		return Response.status(Response.Status.OK).entity(reservation).build();
	}
	
	
	@POST
	@Path("/reserve")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createReservation(@Context HttpServletRequest request, ReservationDTO reservationDTO) {
		
		User user = (User)request.getSession().getAttribute("user");
		if(user == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Please, sign in to create a reservation.").build();
		}
		
		if(reservationDTO.getDate().isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Please, choose a date.").build();
		}
		
		Integer numberOfStays = reservationDTO.getNumberOfStays();
		if(numberOfStays == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Please, enter number of stays.").build();
		}
		
		List<Date> availableDates = reservationDTO.getAvailableDates();
		List<Date> reservedDates = new ArrayList<Date>();
		
		
		ReservationDAO reservationDao = new ReservationDAO(context.getRealPath(""));
		Integer lastId = reservationDao.getLastId();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date date;
		try {
			date = sdf.parse(reservationDTO.getDate());
		} catch (ParseException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("An error occured, please try again later.").build();
		}
		
		reservedDates.add(date);
		for(int i = 1; i < numberOfStays; i++) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, 1);
			date = c.getTime();
			reservedDates.add(date);
		}
		for(Date d: reservedDates) {
			if(!availableDates.contains(d)) {
				return Response.status(Response.Status.BAD_REQUEST).entity("Please, change number of stays to match available dates.").build();
			}
		}
		
		availableDates.removeAll(reservedDates);
		
		//racunanje cene
		Double priceByDay = reservationDTO.getPrice();
		Double price = 0.0;
		for (int i = 0; i < numberOfStays; i++) {
			price += priceByDay;
		}
		
		Reservation reservation = new Reservation(++lastId, reservationDTO.getApartmentId(), reservedDates.get(0), numberOfStays, price, reservationDTO.getMessage(), reservationDTO.getGuest(), ReservationStatus.CREATED);
				
		try {
			reservationDao.save(reservation, context.getRealPath(""));
			availableDates.removeAll(reservedDates);		
			DatesForRentDAO datesForRentDAO = new DatesForRentDAO(context.getRealPath(""));
			DatesForRent datesForRent = datesForRentDAO.findByApartmentId(reservation.getApartmentId());
			datesForRent.setDates(availableDates);
			datesForRentDAO.update(datesForRent, context.getRealPath(""));
		} catch (IOException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured, please try again later.").build();
		}
		
		return Response.status(Response.Status.OK).entity("Reservation is created.").build();
	}
	
	@POST
	@Path("/sorterHost/{sort}") 
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response sortHost(@Context HttpServletRequest request, @PathParam("sort") String sort) {
		User user = (User)request.getSession().getAttribute("user");
		if(user == null || !user.getRole().equals(UserRole.HOST)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		String username = user.getUsername();
		ApartmentDAO apartmentDAO = new ApartmentDAO(context.getRealPath(""));
		ReservationDAO reservationDAO = new ReservationDAO(context.getRealPath(""));
		List<Apartment> apartments = apartmentDAO.findByHost(username);
		List<Reservation> allReservations = new ArrayList<Reservation>();
		
		for (Apartment a : apartments) {
			List<Reservation> reservations = reservationDAO.findByApartment(a.getId());
			allReservations.addAll(reservations);
		}
		
		allReservations.sort(new ReservationPriceSorter(sort));
		
		List<ReservationHostDTO> myReservations = new ArrayList<ReservationHostDTO>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		for (Reservation reservation : allReservations) {
			Apartment apartment = apartmentDAO.findById(reservation.getApartmentId());
			String apartmentName = apartment.getName();
			String date = sdf.format(reservation.getDate());
			
			ReservationHostDTO reservationHostDTO = new ReservationHostDTO(reservation.getId(), apartmentName, 
					reservation.getGuest(), date, reservation.getNumberOfStays(), 
					reservation.getPrice(), reservation.getMessage(), reservation.getStatus());
			myReservations.add(reservationHostDTO);
		}
		
		return Response.status(Response.Status.OK).entity(myReservations).build();
	}
	
	@POST
	@Path("/sorterGuest/{sort}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sortGuest(@Context HttpServletRequest request, @PathParam("sort") String sort) {
		User user = (User)request.getSession().getAttribute("user");
		if(user == null || !user.getRole().equals(UserRole.GUEST)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		String username = user.getUsername();
		ReservationDAO reservationDAO = new ReservationDAO(context.getRealPath(""));
		List<Reservation> reservations = reservationDAO.findByGuest(username);
		List<ReservationGuestDTO> myReservations = new ArrayList<ReservationGuestDTO>();
		ApartmentDAO apartmentDAO = new ApartmentDAO(context.getRealPath(""));
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		reservations.sort(new ReservationPriceSorter(sort));
		
		for (Reservation reservation : reservations) {
			Apartment apartment = apartmentDAO.findById(reservation.getApartmentId());
			String apartmentName = apartment.getName();
			String date = sdf.format(reservation.getDate());

			ReservationGuestDTO reservationGuestDTO = new ReservationGuestDTO(reservation.getId(), apartmentName, date,
					reservation.getNumberOfStays(), reservation.getPrice(), reservation.getStatus());
			myReservations.add(reservationGuestDTO);
		}
		
		return Response.status(Response.Status.OK).entity(myReservations).build();

	}
	
	@POST
	@Path("/sorterAdmin/{sort}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sortAdmin(@Context HttpServletRequest request, @PathParam("sort") String sort) {
		User user = (User)request.getSession().getAttribute("user");
		if(!user.getRole().equals(UserRole.ADMIN)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		ReservationDAO reservationDAO = new ReservationDAO(context.getRealPath(""));
		List<Reservation> reservations = reservationDAO.findAll();
		List<ReservationAdminDTO> reservationsDTO = new ArrayList<ReservationAdminDTO>();
		ApartmentDAO apartmentDAO = new ApartmentDAO(context.getRealPath(""));
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		reservations.sort(new ReservationPriceSorter(sort));
		
		for (Reservation reservation : reservations) {
			Apartment apartment = apartmentDAO.findById(reservation.getApartmentId());
			String apartmentName = apartment.getName();
			String host = apartment.getHostUsername();
			String date = sdf.format(reservation.getDate());
			
			ReservationAdminDTO reservationAdminDTO = new ReservationAdminDTO(reservation.getId(), apartmentName, host, reservation.getGuest(), date, reservation.getNumberOfStays(), reservation.getPrice(), reservation.getStatus());
			reservationsDTO.add(reservationAdminDTO);
		}
		return Response.status(Response.Status.OK).entity(reservationsDTO).build();
	}
	
}
