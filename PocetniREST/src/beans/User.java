package beans;

import java.io.Serializable;

import enums.UserRole;

public class User implements Serializable {
	
	private static final long serialVersionUID = 1949307267283963703L;
	
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String gender;
	private UserRole role;
	private Boolean blocked;
	
	public User() {
		super();
		this.blocked = false;
	}
	
	public User(String username, String password, String firstName,String lastName,
			String gender, UserRole role, Boolean blocked) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.role = role;
		this.blocked = blocked;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public UserRole getRole() {
		return role;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Boolean isBlocked() {
		return blocked;
	}
	
	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", firstName=" + firstName + ", lastName="
				+ lastName + ", gender=" + gender + ", role=" + role + "]";
	}
	
	@Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if(obj instanceof User)
        {
            User temp = (User) obj;
            if(this.username == temp.username && this.password== temp.password && this.firstName == temp.firstName && this.lastName == temp.lastName
            		&& this.gender == temp.gender && this.role == temp.role)
                return true;
        }
        return false;

    }
	
	public String forFile() {
		return this.getUsername() + ";" + this.getPassword() + ";" + 
				this.getFirstName() + ";" + this.getLastName() + ";" + 
				this.getGender() + ";" + this.getRole().name() + ";" + this.isBlocked() + "\r\n";
	}
	
	

}
