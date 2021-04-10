package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import beans.Comment;

public class CommentDAO {

	private String contextPath;
	
	public CommentDAO() {
		super();
	}

	public CommentDAO(String contextPath) {
		this.contextPath = contextPath;
	}
	
	public List<Comment> findByApartment(Integer id){
		List<Comment> allComments = load(contextPath);
		List<Comment> comments = new ArrayList<Comment>();
		for(int i = 0; i < allComments.size(); i++) {
			if(allComments.get(i).getApartment() == id) {
				comments.add(allComments.get(i));
			}
		}
		return comments;
	}
	
	public Comment findById(Integer id){
		List<Comment> comments = load(contextPath);
		for(Comment comment: comments) {
			if(comment.getId() == id) {
				return comment;
			}
		}
		return null;
	}
	
	public List<Comment> findAll(){
		return load(contextPath);
	}
	
	public Integer getLastId(){
		List<Comment> comments = load(contextPath);
		return comments.size();
	}
	
	public void disapproveOne(Integer id) throws IOException {
		List<Comment> comments = load(contextPath);
		for(Comment comment: comments) {
			if(comment.getId() == id) {
				comment.setApproved(false);
				update(comments, contextPath);
				break;
			}
		}
	}
	
	public void disapproveByApartment(Integer apartmentId) throws IOException {
		List<Comment> comments = load(contextPath);
		for(Comment comment: comments) {
			if(comment.getApartment() == apartmentId) {
				comment.setApproved(false);
			}
		}
		update(comments, contextPath);
	}
	
	public void disapproveAll() throws IOException {
		List<Comment> comments = load(contextPath);
		for(Comment comment: comments) {
			comment.setApproved(false);	
		}
		update(comments, contextPath);
	}
	
	private List<Comment> load(String contextPath) {
		BufferedReader in = null;
		List<Comment> comments = new ArrayList<Comment>();
		
		try {
			File file = new File(contextPath + "/comments.txt");
			in = new BufferedReader(new FileReader(file));
			String line;
			
			while((line = in.readLine()) != null) {
				line = line.trim();
				if(line.equals("") || line.indexOf('#') == 0) {
					continue;
				}

				String[] lines = line.split(";");
				
				Integer id = Integer.parseInt(lines[0]);
				String guest = lines[1];
				Integer apartment = Integer.parseInt(lines[2]);
				String text = lines[3];
				Integer rating = Integer.parseInt(lines[4]);
				boolean deleted = Boolean.parseBoolean(lines[5]);
					
					
				Comment comment = new Comment(id, guest, apartment, text, rating, deleted);
				comments.add(comment);
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
		return comments;
	}
	
	public void save(Comment comment, String contextPath) throws IOException {
		Files.write(Paths.get(contextPath + "/comments.txt"), comment.forFile().getBytes(), StandardOpenOption.APPEND);
	}
	
	public void update(List<Comment> comments, String contextPath) throws IOException {
		String fileContent = "";
		for(Comment comment: comments) {
			fileContent += comment.forFile();
		}
		Files.write(Paths.get(contextPath + "/comments.txt"), fileContent.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
	}
	
	public void update(Comment comment, String contextPath) throws IOException {
		List<Comment> comments = load(contextPath);
		String file = "";
		for(Comment c: comments) {
			if(c.getId() == comment.getId()) {
				c.setApproved(comment.isApproved());
			}
			file += c.forFile();
		}
		Files.write(Paths.get(contextPath, "/comments.txt"), file.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
	}
}
