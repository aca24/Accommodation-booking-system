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
import java.util.StringTokenizer;

import beans.Address;

public class AddressDAO {

	private String contextPath;
	
	public AddressDAO() {
		super();
	}

	public AddressDAO(String contextPath) {
		this.contextPath = contextPath;
	}

	public Integer exists(Address address) {
		List<Address> addresses = load(contextPath);
		for(Address a: addresses) {
			if(a.getCity().equals(address.getCity()) && a.getStreet().equals(address.getStreet()) && 
			a.getNumber().equals(address.getNumber()) && a.getPostalCode().equals(address.getPostalCode())) {
				return a.getId(); // vracamo onu adresu pomocu koje pronalazimo lokaciju
			}
		}
		return null;
	}

	public Integer getLastId() {
		List<Address> addresses = load(contextPath);
		return addresses.size();
	}
	
	public Address findById(Integer id){
		List<Address> addresses = load(contextPath);
		for(Address address: addresses) {
			if(address.getId() == id) {
				return address;
			}
		}
		return null;
	}
	
	private List<Address> load(String contextPath) {
		BufferedReader in = null;
		List<Address> addresses = new ArrayList<Address>();
		
		try {
			File file = new File(contextPath + "/addresses.txt");
			in = new BufferedReader(new FileReader(file));
			String line;
			StringTokenizer st;
			
			while((line = in.readLine()) != null) {
				line = line.trim();
				if(line.equals("") || line.indexOf('#') == 0) {
					continue;
				}
				st = new StringTokenizer(line, ";");
				while(st.hasMoreTokens()) {
					Integer id = Integer.parseInt(st.nextToken().trim());
					String street = st.nextToken().trim();
					Integer number = Integer.parseInt(st.nextToken().trim());
					String city = st.nextToken().trim();
					String postalCode = st.nextToken().trim();
					
					Address address = new Address(id, street, number, city, postalCode);
					addresses.add(address);
				}
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
		return addresses;
	}
	
	public void save(Address address, String contextPath) throws IOException{
		Files.write(Paths.get(contextPath + "/addresses.txt"), address.forFile().getBytes(), StandardOpenOption.APPEND);
	}
	
	public void update(Address address, String contextPath) throws IOException {
		List<Address> addresses = load(contextPath);
		String file = "";
		for(Address a: addresses) {
			if(a.getId() == address.getId()) {
				a.setCity(address.getCity());
				a.setStreet(address.getStreet());
				a.setNumber(address.getNumber());
				a.setPostalCode(address.getPostalCode());
			}
			file += a.forFile();
		}
		Files.write(Paths.get(contextPath, "/addresses.txt"), file.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
	}

	
	
}
