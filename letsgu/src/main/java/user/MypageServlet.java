package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/letsgu/mypage")
public class MypageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserDAO dao = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ✅ 1) 로그인 세션 가져오기
        HttpSession session = request.getSession(); // false 필요 없음 (이미 로그인 시 생성됨)
        Integer userId = (Integer) session.getAttribute("USER_ID");
        String rule = (String) session.getAttribute("RULE");

        // ✅ 2) 로그인 여부 확인
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/letsgu/login");
            return;
        }

        System.out.println("[Mypage] 현재 로그인된 USER_ID: " + userId + ", RULE: " + rule);

        // ✅ 3) 회원 정보 조회 (userId로 검색)
        Users user = null;
        try {
            user = dao.findByUserId(userId); // ← ★ DAO 메서드 수정 필요 (findLoginId → findByUserId)
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "마이페이지 정보를 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            return;
        }

        // ✅ 4) DB에 유저 없음 → 재로그인 유도
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/letsgu/login");
            return;
        }

        // ✅ 5) JSP로 전달
        request.setAttribute("user", user);
        request.setAttribute("RULE", rule);

        // ✅ 6) 페이지 이동
        request.getRequestDispatcher("/WEB-INF/views/login/mypage.jsp").forward(request, response);
    }
}
