package user;

public class SignupService {

    private final UserDAO dao = new UserDAO();

    /** ✅ 회원가입 서비스 */
    public boolean signUp(Users user) {
        System.out.println("[SERVICE] 회원가입 요청: " + user.getId());

        // 1) 아이디 중복 확인
        if (dao.existsByLoginId(user.getId())) {
            System.out.println("[SERVICE] 이미 존재하는 아이디입니다: " + user.getId());
            return false;
        }

        // 2) DAO insert 실행
        int result = 0;
        try {
            result = dao.insert(user);
            System.out.println("[SERVICE] DAO.insert 결과 = " + result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[SERVICE] 예외 발생: " + e.getMessage());
            return false;
        }

        // 3) 성공 여부 반환
        boolean success = (result > 0);
        System.out.println("[SERVICE] 회원가입 결과 = " + success);
        return success;
    }

    /** ✅ 아이디 중복 검사 (직접 호출용, 선택 사항) */
    public boolean existsByLoginId(String id) {
        if (id == null || id.trim().isEmpty()) return false;
        return dao.existsByLoginId(id.trim());
    }
}
