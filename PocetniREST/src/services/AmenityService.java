package services;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Amenity;
import beans.Apartment;
import beans.User;
import dao.AmenityDAO;
import dao.ApartmentDAO;
import dto.AmenityDTO;
import enums.UserRole;

@Path("amenities")
public class AmenityService {

	@Context
	ServletContext context;
	
	@GET
	@Path("") // radi
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAll(){
		AmenityDAO amenityDao = new AmenityDAO(context.getRealPath(""));
		return Response.status(Response.Status.OK).entity(amenityDao.findAll()).build();
	}
	
	@GET
	@Path("/{id}") // radi
	@Produces(MediaType.APPLICATION_JSON)
	public Amenity findById(@PathParam("id") Integer id) {
		AmenityDAO amenityDao = new AmenityDAO(context.getRealPath(""));
		return amenityDao.findById(id);
	}
	
	@POST
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(@Context HttpServletRequest request, AmenityDTO amenityDTO, @QueryParam("id") Integer id) {
		
		User user = (User)request.getSession().getAttribute("user");
		if(user == null || !user.getRole().equals(UserRole.ADMIN)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		AmenityDAO amenityDao = new AmenityDAO(context.getRealPath(""));
		Integer amenityId = id == -1 ? amenityDao.getLastId() + 1 : id;
		Amenity a = new Amenity(amenityId, amenityDTO.getName());
		
		try {
			if(id == -1)
				amenityDao.save(a, context.getRealPath(""));
			else
				amenityDao.update(a, context.getRealPath(""));
		} catch (IOException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
		return Response.status(Response.Status.OK).build();
	}
	
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response delete(@Context HttpServletRequest request, @PathParam("id") Integer id) {
		
		User user = (User)request.getSession().getAttribute("user");
		if(user == null || !user.getRole().equals(UserRole.ADMIN)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		
		AmenityDAO amenityDao = new AmenityDAO(context.getRealPath(""));
		Amenity amenity = amenityDao.findById(id);
		amenity.setDeleted(true);
		
		try {
			amenityDao.update(amenity, context.getRealPath(""));
		} catch (IOException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured, please try again later.").build();
		}
		
		// sad je potrebno obrisati iz apartmana
		ApartmentDAO apartmentDao = new ApartmentDAO(context.getRealPath(""));
		List<Apartment> apartments = apartmentDao.find();
		
		for(Apartment a: apartments) {
			List<Integer> amenities = a.getAmenities();
			if(amenities.contains(id)) {
				amenities.remove(id);
			}
			
			a.setAmenities(amenities);
			
			try {
				apartmentDao.update(a, context.getRealPath(""));
			} catch (IOException e) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured, please try again later.").build();
			}
		}
		
		return Response.status(Response.Status.OK).build();
	}
}
