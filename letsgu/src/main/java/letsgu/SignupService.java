package project;

public class SignupService {

	UserDAO dao = new UserDAO();
    // ✅ 회원가입 
    public boolean signUp(User user) {
    	int result = 0;
    	
        try {
        	System.out.println("[SERVICE] DAO.insert 호출 직전");
        	result = dao.insert(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
        System.out.println("[SERVICE] DAO.insert 결과 = " + result);
        // ✅ 클래스명(X) → 객체 변수(user)
        boolean success = result > 0;

        System.out.println("[Service] signUp 결과 = " + success);
        return success;
    }
}
