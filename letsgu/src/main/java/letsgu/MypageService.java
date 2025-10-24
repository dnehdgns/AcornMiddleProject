package project;

public class MypageService {

	//private final UserDAO dao = new UserDAO();
	private final UserDAO dao;
	
	public MypageService(UserDAO dao) {
		this.dao=dao;
	}
	public User login(String id) {

		return dao.findLoginId(id);

	}
}