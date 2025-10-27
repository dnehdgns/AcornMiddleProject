package user;

public class Users {
	private int userId; // PK
	private String id;
	private String pw;
	private String name;
	private String email;
	private String gender;
	private String agegroup;
	private String rule;

	public Users() {
		// TODO Auto-generated constructor stub
	}

	public Users(int userId, String id, String pw, String name, String email, String gender, String agegroup) {
		super();
		this.userId = userId;
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.agegroup = agegroup;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAgegroup() {
		return agegroup;
	}

	public void setAgegroup(String agegroup) {
		this.agegroup = agegroup;
	}
	
	public String getRule() {
	    return rule;
	}

	public void setRule(String rule) {
	    this.rule = rule;
	}

	@Override
	public String toString() {
		return "Users [userId=" + userId + ", id=" + id + ", pw=" + pw + ", name=" + name + ", email=" + email
				+ ", gender=" + gender + ", agegroup=" + agegroup + ", rule= " + rule + "]";
	}

}
