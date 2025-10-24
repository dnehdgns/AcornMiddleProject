package event;

public class Users {

	private int user_id;
	private String username;

	
	public Users() {
	}

	public Users(int user_id, String username) {
		this.user_id = user_id;
		this.username = username;
	}

	
	@Override
	public String toString() {
		return "Users [user_id=" + user_id + ", username=" + username + "]";
	}

	
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
