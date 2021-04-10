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

import beans.Image;

public class ImageDAO {

	private String contextPath;
	
	public ImageDAO() {
		super();
	}
	
	public ImageDAO(String contextPath) {
		this.contextPath = contextPath;
	}
	
	public List<Image> findAll(){
		return load(contextPath);
	}
	
	public List<Image> findByApartment(Integer id) {
		List<Image> allImages = load(contextPath);
		List<Image> images = new ArrayList<Image>();
		for(Image img: allImages) {
			if(img.getApartmentId() == id && img.getDeleted() != true) {
				images.add(img);
			}
		}
		return images;
	}
	
	public Image findById(Integer id) {
		List<Image> images = load(contextPath);
		for(Image img: images) {
			if(img.getId() == id) {
				return img;
			}
		}
		return null;
	}
	
	public Integer getLastId() {
		List<Image> images = load(contextPath);
		return images.size();
	}
	
	private List<Image> load(String contextPath) {
		BufferedReader in = null;
		List<Image> images = new ArrayList<Image>();
		
		try {
			File file = new File(contextPath + "/images.txt");
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
					Integer apartmentId = Integer.parseInt(st.nextToken().trim());
					String imageURL = st.nextToken().trim();
					Boolean deleted = Boolean.parseBoolean(st.nextToken().trim());
					
					Image img = new Image(id, apartmentId, imageURL, deleted);
					images.add(img);
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
		return images;
	}
	
	public void save(Image img, String contextPath) throws IOException{
		Files.write(Paths.get(contextPath + "/images.txt"), img.forFile().getBytes(), StandardOpenOption.APPEND);
	}
	
	public void update(Image img, String contextPath) throws IOException {
		List<Image> images = load(contextPath);
		String file = "";
		for(Image i: images) {
			if(i.getId() == img.getId()) {
				i.setImageURL(img.getImageURL());
				i.setDeleted(img.getDeleted());
			}
			file += i.forFile();
		}
		Files.write(Paths.get(contextPath, "/images.txt"), file.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
	}
}
