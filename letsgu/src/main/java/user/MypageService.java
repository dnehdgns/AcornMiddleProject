package user;

public class MypageService {

	//private final UserDAO dao = new UserDAO();
	private final UserDAO dao;
	
	public MypageService(UserDAO dao) {
		this.dao=dao;
	}
	public Users login(String id) {

		return dao.findLoginId(id);

	}
}