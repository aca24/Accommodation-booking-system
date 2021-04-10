package services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
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

import org.glassfish.jersey.media.multipart.FormDataParam;

import beans.Image;
import dao.ImageDAO;
import dto.ImageDTO;

@Path("images")
public class ImageService {

	@Context
	ServletContext context;
	
	@GET
	@Path("") 
	@Produces(MediaType.APPLICATION_JSON)
	public List<Image> findAll(){
		ImageDAO imgDao = new ImageDAO(context.getRealPath(""));
		return imgDao.findAll();
	}
	
	@GET
	@Path("/{id}") 
	@Produces(MediaType.APPLICATION_JSON)
	public Image findById(@PathParam("id") Integer id){
		ImageDAO imgDao = new ImageDAO(context.getRealPath(""));
		return imgDao.findById(id);
	}
	
	@GET
	@Path("/apartment/{id}") 
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByApartment(@PathParam("id") Integer id){
		ImageDAO imgDao = new ImageDAO(context.getRealPath(""));
		List<Image> images =  imgDao.findByApartment(id);
		
		List<ImageDTO> imagesDTO = new ArrayList<ImageDTO>();
		
		for(Image i: images) {
			ImageDTO imageDTO = new ImageDTO(i.getId(), i.getImageURL());
			imagesDTO.add(imageDTO);
		}
		
		return Response.status(Response.Status.OK).entity(imagesDTO).build();
	}
	
	@POST
	@Path("/upload/{imgName}/{apartmentId}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public Response uploadImage(@FormDataParam("file") InputStream uploadedInputStream, @PathParam("imgName") String imgName, @PathParam("apartmentId") Integer apartmentId, @QueryParam("update") boolean update)  {
		
		String imageURL = "/img/apartment" + apartmentId + "/" + imgName + ".jpg";
		ImageDAO imgDao = new ImageDAO(context.getRealPath(""));
		 Image img = new Image(imgDao.getLastId() + 1, apartmentId, imageURL, false);
		 
		
		if(update) {
			System.out.println("update");
			boolean exists = false;
			List<Image> images = imgDao.findByApartment(apartmentId);
			for(Image i: images) {
				System.out.println(i.getImageURL());
				System.out.println(i.getId());
				String url = i.getImageURL();
				String[] tokens = url.split("/");
				if(tokens[3].equals(imgName + ".jpg") && !i.getDeleted()) {
					System.out.println("exists");
					exists = true;
					break;
				}
			}
			
			try {
				java.nio.file.Files.copy(uploadedInputStream, new File(context.getRealPath("") + imageURL).toPath(), StandardCopyOption.REPLACE_EXISTING);
				if(!exists) {
					imgDao.save(img, context.getRealPath(""));
				}
					
			} catch (IOException e) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured, please try again later.").build();
			}
		}
		else {
			try {
				java.nio.file.Files.copy(uploadedInputStream, new File(context.getRealPath("") + imageURL).toPath(), StandardCopyOption.REPLACE_EXISTING);
				imgDao.save(img, context.getRealPath(""));
			} catch (IOException e) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured, please try again later.").build();
			}
		}
		
		
		return Response.status(Response.Status.OK).build();	
	}
	
	@DELETE
	@Path("/{imgName}/{apartmentId}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response delete(@PathParam("imgName") String imgName, @PathParam("apartmentId") Integer apartmentId) {
		
		ImageDAO imgDao = new ImageDAO(context.getRealPath(""));
		List<Image> images = imgDao.findByApartment(apartmentId);
		
		for(Image img: images) {
			String url = img.getImageURL();
			String[] tokens = url.split("/");
			if(tokens[3].equals(imgName + ".jpg")) {
				img.setDeleted(true);
				try {
					imgDao.update(img, context.getRealPath(""));
				} catch (IOException e) {
					return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occured, please try again later.").build();
				}
			}
		}
		
		return Response.status(Response.Status.OK).build();	
	}
	
}
