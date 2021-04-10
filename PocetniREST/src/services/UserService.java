package services;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import beans.Apartment;
import beans.Reservation;
import beans.User;
import dao.ApartmentDAO;
import dao.ReservationDAO;
import dao.UserDAO;
import dto.UserDTO;
import dto.UserRegisterDTO;
import dto.UserCurrentDTO;
import dto.UserLoginDTO;
import dto.UserMyProfileDTO;
import enums.UserRole;


@Path("/users")
public class UserService {
	
	@Context
	ServletContext context;
	
	
	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public User findByUsername(@PathParam("username") String username) {
		UserDAO userDao = new UserDAO(context.getRealPath(""));
		return userDao.findByUsername(username);
	}
	
	@GET
	@Path("/role")
	@Produces(MediaType.TEXT_PLAIN)
	public Response userRole(@Context HttpServletRequest request) {
		User user = (User)request.getSession().getAttribute("user");
		if(user != null) {
			return Response.status(Response.Status.OK).entity(user.getRole().toString()).build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	@GET
	@Path("/currentUser") 
	@Produces(MediaType.APPLICATION_JSON)
	public Response currentUser(@Context HttpServletRequest request, @CookieParam("userIdentificationKey") Cookie cookie) {
		User user = (User) request.getSession().getAttribute("user");
		
		if(user != null) {
			UserCurrentDTO userDTO = new UserCurrentDTO(user.getUsername(), user.getFirstName(), user.getLastName(), user.getGender());
			return Response.status(Response.Status.OK).entity(userDTO).build();
		}
		if(cookie != null && !cookie.getValue().equals("-1")) {
			UserDAO userDao = new UserDAO(context.getRealPath(""));
			user = userDao.findByUsername(cookie.getValue());
			UserCurrentDTO userDTO = new UserCurrentDTO(user.getUsername(), user.getFirstName(), user.getLastName(), user.getGender());
			request.getSession().setAttribute("user", user);
			return Response.status(Response.Status.OK).entity(userDTO).build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
	
	//REGISTRACIJA
	
	@POST
	@Path("/register/{role}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response signup(UserRegisterDTO userDTO, @PathParam("role") String role){
		UserDAO userDao = new UserDAO(context.getRealPath(""));
		User user = userDao.findByUsername(userDTO.getUsername());
		
		if(user != null) { // proveravamo username i na frontendu i na backendu
			return Response.status(Response.Status.CONFLICT).entity("This username is already taken.").build();
		}
		if(!userDTO.getPassword().equals(userDTO.getConfirmPassword())) { // proveravamo password i na frontendu i na backendu
			return Response.status(Response.Status.CONFLICT).entity("Passwords do not match.").build();
		}
		
		user = new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getGender(), UserRole.valueOf(role), false);
		try {
			userDao.save(user, context.getRealPath(""));
		} catch (IOException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured, please try again later.").build();
		}
		return Response.status(Response.Status.OK).build();
	}
	
	
	//PRIJAVLJIVANJE
	
	@POST
	@Path("") 
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(UserLoginDTO userDTO, @Context HttpServletRequest request) {
		UserDAO userDao = new UserDAO(context.getRealPath(""));
		User loggedUser = userDao.find(userDTO.getUsername(), userDTO.getPassword());
		
		if(loggedUser == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid username and/or password").build();
		}
		
		if(loggedUser.isBlocked()) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Your account is blocked.").build();
		}
		
		
		if(userDTO.getRememberMe()) {
			Cookie cookie = new Cookie("userIdentificationKey", loggedUser.getUsername(), "/PocetniREST/rest", "localhost");
			NewCookie newCookie = new NewCookie(cookie, "", 365*24*60*60, false);
			request.getSession().setAttribute("user", loggedUser);
			return Response.status(Response.Status.OK).cookie(newCookie).entity(loggedUser.getRole()).build();
		}
		else {
			request.getSession().setAttribute("user", loggedUser);
			return Response.status(Response.Status.OK).entity(loggedUser.getRole()).build();
		}
	}
	
	//ODJAVLJIVANJE
	
	@GET
	@Path("/logout") 
	@Produces(MediaType.TEXT_PLAIN)
	public Response logout(@Context HttpServletRequest request, @CookieParam("userIdentificationKey") Cookie cookie) throws ServletException {
		User user = (User) request.getSession().getAttribute("user");
		if(user != null) {	
			if(cookie != null) {
				request.getSession().invalidate();
				return Response.status(Response.Status.OK).header("Set-Cookie", "userIdentificationKey=-1; path=/PocetniREST/rest; expires=Thu, 01 Jan 1970 00:00:00 GMT").build();
			}
			request.getSession().invalidate();
		}
		return Response.status(Response.Status.OK).build();
	}
	
	//IZMENA LICNIH PODATAKA
	
	@POST
	@Path("/myProfile") 
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response myProfile(UserMyProfileDTO userDTO, @Context HttpServletRequest request){
		UserDAO userDao = new UserDAO(context.getRealPath(""));
		User user = userDao.findByUsername(userDTO.getUsername());
		
		if(user == null) { // Ako ne postoji user (username je promenjen na frontendu)
			return Response.status(Response.Status.BAD_REQUEST).entity("An error occured, please try again later.").build();
		}
		
		if(userDTO.getOldPassword() != "") {
			if(userDTO.getNewPassword() != "") {
				if(userDTO.getConfirmPassword() != "") {
					if(!userDTO.getOldPassword().equals(user.getPassword())) { // proveravamo staru lozinku da li se poklapa sa lozinkom iz baze
						return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid old password.").build();
					}
					if(!userDTO.getNewPassword().equals(userDTO.getConfirmPassword())) {
						return Response.status(Response.Status.CONFLICT).entity("Passwords do not match.").build();
					}
					user = new User(userDTO.getUsername(), userDTO.getNewPassword(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getGender(), UserRole.GUEST, false);
				}
				else {
					user = new User(user.getUsername(), user.getPassword(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getGender(), UserRole.GUEST, false);
				}
			}
			else {
				user = new User(user.getUsername(), user.getPassword(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getGender(), UserRole.GUEST, false);
			}
		}
		else {
			user = new User(user.getUsername(), user.getPassword(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getGender(), UserRole.GUEST, false);
		}
		
		try {
			request.getSession().setAttribute("user", user);
			userDao.update(user, context.getRealPath(""));
		} catch (IOException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured, please try again later.").build();
		}
		return Response.status(Response.Status.OK).entity("The changes have been saved.").build();
	}
	
	@GET
	@Path("/adminSearch")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findAll(@Context HttpServletRequest request) {
		User user = (User)request.getSession().getAttribute("user");
		if(user == null || !user.getRole().equals(UserRole.ADMIN)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		UserDAO userDao = new UserDAO(context.getRealPath(""));
		List<User> allUsers = userDao.findAll();
		List<UserDTO> usersDto = new ArrayList<UserDTO>();
		for (User userIt : allUsers) {
			UserDTO userDto = new UserDTO(userIt.getUsername(), userIt.getFirstName(), userIt.getLastName(), userIt.getGender(), userIt.getRole(), userIt.isBlocked());
			usersDto.add(userDto);
		}

		return Response.status(Response.Status.OK).entity(usersDto).build();
	}
	
	@GET
	@Path("/hostSearch")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findByHost(@Context HttpServletRequest request) {
		User user = (User)request.getSession().getAttribute("user");
		if(user == null || !user.getRole().equals(UserRole.HOST)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		List<User> users = new ArrayList<User>();
		UserDAO userDAO = new UserDAO(context.getRealPath(""));
		ApartmentDAO apartmentDAO = new ApartmentDAO(context.getRealPath(""));
		ReservationDAO reservationDAO = new ReservationDAO(context.getRealPath(""));
		List<Apartment> apartmentsByHost = apartmentDAO.findByHost(user.getUsername());
		List<String> usernames = new ArrayList<String>();
		for (Apartment apartment : apartmentsByHost) {
			List<Reservation> reservations = reservationDAO.findByApartment(apartment.getId());
			for (Reservation reservation : reservations) {
				User guestUser = userDAO.findByUsername(reservation.getGuest());
				if (!usernames.contains(guestUser.getUsername())) {
					usernames.add(guestUser.getUsername());
				}
			}
		}
		for (String username : usernames) {
			User user1 = userDAO.findByUsername(username);
			users.add(user1);
		}
				
		List<UserDTO> usersDto = new ArrayList<UserDTO>();
		for (User userIt : users) {
			UserDTO userDto = new UserDTO(userIt.getUsername(), userIt.getFirstName(), userIt.getLastName(), userIt.getGender(), userIt.getRole(), userIt.isBlocked());
			if (!usersDto.contains(userDto)) {
				usersDto.add(userDto);
			}
		}
		
		return Response.status(Response.Status.OK).entity(usersDto).build();
	}
	
}
