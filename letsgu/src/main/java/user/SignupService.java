package user;

public class SignupService {

	UserDAO dao = new UserDAO();
    // ✅ 회원가입 
    public boolean signUp(Users user) {
    	
    	//아이디 중복 확인
    	 if (dao.existsByLoginId(user.getId())) {
             System.out.println("[SERVICE] 이미 존재하는 아이디입니다: " + user.getId());
             return false;
         }

         int result = dao.insert(user);
         System.out.println("[SERVICE] DAO.insert 결과 = " + result);

         boolean success = result > 0;
         System.out.println("[SERVICE] signUp 결과 = " + success);

         return success;
    }
}
