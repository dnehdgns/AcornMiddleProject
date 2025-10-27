package user;

public class LoginService {

	//private final UserDAO dao = new UserDAO();
	private final UserDAO dao;
	
	public LoginService(UserDAO dao) {
		this.dao=dao;
	}
	
	//로그인 유저 정보
	public Users login(String loginId, String password) {
        return dao.findByLogin(loginId, password);
    }
	
	
}