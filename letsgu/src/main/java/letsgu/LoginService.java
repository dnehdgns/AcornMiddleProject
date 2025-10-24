package project;

public class LoginService {

	//private final UserDAO dao = new UserDAO();
	private final UserDAO dao;
	
	public LoginService(UserDAO dao) {
		this.dao=dao;
	}
	public boolean login(String id, String pw) {

		return dao.existsByLoginId(id, pw);

	}
}