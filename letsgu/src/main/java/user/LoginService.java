package user;

/**
 * 
 */
public class LoginService {
    private final UserDAO dao;
    
    public LoginService(UserDAO dao) { 
    	this.dao = dao; 
    	}

    public Users login(String loginId, String password) {
    	return dao.findByLogin(loginId, password);   
    	}

}