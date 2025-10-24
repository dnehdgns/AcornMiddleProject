package project;

public class User {
	private String id;
	private String pw;
	private String name;
	private String email;
	private String gender;
	private String agegroup;
	public User(String id, String pw, String name, String email, String gender, String agegroup) {
		super();
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.agegroup = agegroup;
		
	}
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public String getId() {
		return id;
	}
	public String getPw() {
		return pw;
	}
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public String getGender() {
		return gender;
	}
	public String getAgegroup() {
		return agegroup;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setEmail(String mail) {
		this.email = mail;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public void setAgegroup(String agegroup) {
		this.agegroup = agegroup;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", pw=" + pw + ", name=" + name + ", email=" + email + ", gender=" + gender
				+ ", agegroup=" + agegroup + "]";
	}
	
	
	
}
