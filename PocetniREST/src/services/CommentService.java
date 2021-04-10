package services;

import java.io.IOException;
import java.util.ArrayList;
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
import beans.Comment;
import beans.Reservation;
import beans.User;
import dao.ApartmentDAO;
import dao.CommentDAO;
import dao.ReservationDAO;
import dto.CommentReviewDTO;
import dto.LeaveCommentDTO;
import enums.ReservationStatus;
import enums.UserRole;



@Path("comments")
public class CommentService {

	@Context
	ServletContext context;
	
	@GET
	@Path("/getAll")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(@Context HttpServletRequest request) {
		User user = (User)request.getSession().getAttribute("user");
		if(!user.getRole().equals(UserRole.ADMIN)) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Please, sign in to leave a comment.").build();
		}
		CommentDAO commentDAO = new CommentDAO(context.getRealPath(""));
		ApartmentDAO apartmentDAO = new ApartmentDAO(context.getRealPath(""));
		List<Comment> allComments = commentDAO.findAll();
		List<CommentReviewDTO> comments = new ArrayList<CommentReviewDTO>();
		
		for (Comment comment : allComments) {
			Apartment apartment = apartmentDAO.findById(comment.getApartment());
			String apartmentName = apartment.getName();
			CommentReviewDTO commentReviewDTO = new CommentReviewDTO(comment.getId(), 
					comment.getGuest(), apartmentName, comment.getText(), comment.getRating(),
					comment.isApproved());
			
			comments.add(commentReviewDTO);
		}

		return Response.status(Response.Status.OK).entity(comments).build(); 		
	}
	
	
	@GET
	@Path("/getMyComments")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMyComments(@Context HttpServletRequest request) {
		User user = (User)request.getSession().getAttribute("user");
		if(!user.getRole().equals(UserRole.HOST)) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Please, sign in to leave a comment.").build();
		}
		ApartmentDAO apartmentDAO = new ApartmentDAO(context.getRealPath(""));
		CommentDAO commentDAO = new CommentDAO(context.getRealPath(""));
		List<Apartment> apartments = apartmentDAO.findByHost(user.getUsername());
		List<Comment> allComments = new ArrayList<Comment>();
		for (Apartment apartment : apartments) {
			allComments.addAll(commentDAO.findByApartment(apartment.getId()));
		}
		
		List<CommentReviewDTO> comments = new ArrayList<CommentReviewDTO>();
		
		for (Comment comment : allComments) {
			Apartment apartment = apartmentDAO.findById(comment.getApartment());
			String apartmentName = apartment.getName();
			CommentReviewDTO commentReviewDTO = new CommentReviewDTO(comment.getId(), 
					comment.getGuest(), apartmentName, comment.getText(), comment.getRating(),
					comment.isApproved());
			
			comments.add(commentReviewDTO);
		}
		
		return Response.status(Response.Status.OK).entity(comments).build(); 
	}
	
	@PUT
	@Path("/approve/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response approveComment(@Context HttpServletRequest request, @PathParam("id") Integer id) {
		User user = (User)request.getSession().getAttribute("user");
		if(!user.getRole().equals(UserRole.HOST)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		CommentDAO commentDAO = new CommentDAO(context.getRealPath(""));
		Comment comment = commentDAO.findById(id);
		if (!comment.isApproved()) {
			comment.setApproved(true);
		}
		try {
			commentDAO.update(comment, context.getRealPath(""));
		} catch (IOException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured, please try again later.").build();
		}
		return Response.status(Response.Status.OK).build();
	}
	
	@PUT
	@Path("/disprove/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response disproveComment(@Context HttpServletRequest request, @PathParam("id") Integer id) {
		User user = (User)request.getSession().getAttribute("user");
		if(!user.getRole().equals(UserRole.HOST)) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		CommentDAO commentDAO = new CommentDAO(context.getRealPath(""));
		Comment comment = commentDAO.findById(id);
		if (comment.isApproved()) {
			comment.setApproved(false);
		}
		try {
			commentDAO.update(comment, context.getRealPath(""));
		} catch (IOException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured, please try again later.").build();
		}
		return Response.status(Response.Status.OK).build();
	}
	
	@POST
	@Path("/comment")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response leaveComment(@Context HttpServletRequest request, LeaveCommentDTO commentDTO) {
		User user = (User)request.getSession().getAttribute("user");
		if(user == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Please, sign in to leave a comment.").build();
		}

		if(commentDTO.getRating() == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("You must give a rating.").build();
		}
	
		ReservationDAO reservationDao = new ReservationDAO(context.getRealPath(""));
		List<Reservation> reservations = reservationDao.findByGuest(user.getUsername());
		
		boolean exists = false;
		for(Reservation r: reservations) {
			if(r.getStatus().equals(ReservationStatus.FINISHED) || r.getStatus().equals(ReservationStatus.REJECTED)) {
				if(r.getApartmentId() == commentDTO.getApartmentId()) { // postoji rezervacija za zadati apartman koja ima status Rejected ili Finished
					exists = true;
					break;
				}
			}
		}
		
		if(!exists) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You can leave a comment only when your reservation is finished or rejected.").build();
		}
		
		CommentDAO commentDao = new CommentDAO(context.getRealPath(""));
		Integer id = commentDao.getLastId();
		Comment comment = new Comment(++id, commentDTO.getGuest(), commentDTO.getApartmentId(), commentDTO.getText(), commentDTO.getRating(), false);
		
		try {
			commentDao.save(comment, context.getRealPath(""));
		} catch (IOException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured, please try again later.").build();
		}
		return Response.status(Response.Status.OK).entity("Thank you for your review, your comment will show once a host approves it.").build(); 
	}
	
}
