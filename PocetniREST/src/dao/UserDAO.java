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

import beans.User;
import enums.UserRole;

public class UserDAO {
	
	private String contextPath;
	
	public UserDAO() {
		super();
	}

	public UserDAO(String contextPath) {
		this.contextPath = contextPath;
	}
	
	public User find(String username, String password) {
		List<User> users = load(contextPath);
		for(User user: users) {
			if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
				return user;
			}
		}
		return null;
	}
	
	public User findByUsername(String username){
		List<User> users = load(contextPath);
		for(User user: users) {
			if(user.getUsername().equals(username)) {
				return user;
			}
		}
		return null;
	}
	
	public List<User> findAll() {
		return load(contextPath);
	}
	
	private List<User> load(String contextPath) {
		BufferedReader in = null;
		List<User> users = new ArrayList<User>();
		
		try {
			File file = new File(contextPath + "/users.txt");
			in = new BufferedReader(new FileReader(file));
			String line;
			
			while((line = in.readLine()) != null) {
				line = line.trim();
				if(line.equals("") || line.indexOf('#') == 0) {
					continue;
				}
				
				String[] lines = line.split(";");
				
				String username = lines[0];
				String password = lines[1];
				String firstName = lines[2];
				String lastName = lines[3];
				String gender = lines[4];
					
				String stringRole = lines[5];
				UserRole role = Enum.valueOf(UserRole.class, stringRole);
				Boolean deleted = Boolean.parseBoolean(lines[6]);
				
				User user = new User(username, password, firstName, lastName, gender, role, deleted);	
				users.add(user);
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
		
		return users;
	}
	
	public void save(User user, String contextPath) throws IOException{
		Files.write(Paths.get(contextPath + "/users.txt"), user.forFile().getBytes(), StandardOpenOption.APPEND);
	}
	
	public void update(User user, String contextPath) throws IOException {
		List<User> users = load(contextPath);
		String file = "";
		for(User u: users) {
			if(u.getUsername().equals(user.getUsername())) {
				u.setFirstName(user.getFirstName());
				u.setLastName(user.getLastName());
				u.setPassword(user.getPassword());
				u.setGender(user.getGender());
				u.setBlocked(user.isBlocked());
			}
			file += u.forFile();
		}
		Files.write(Paths.get(contextPath, "/users.txt"), file.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
	}

}
