package user;

public class UpdateService {

	private final UserDAO dao = new UserDAO();
	
	public boolean updateProfile(Users u) {
	    int affected = dao.updateProfile(u); // int 반환 받음
	    return affected > 0;  // ✅ 1 이상이면 성공
	    
	   
	}
}