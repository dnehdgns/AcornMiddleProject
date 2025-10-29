package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/letsgu/main")
public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ✅ 로그인 시점에 세션이 이미 생성되었기 때문에 false 필요 없음
        HttpSession session = request.getSession();

        // ✅ USER_ID(PK)와 RULE(권한) 가져오기
        Integer userId = (Integer) session.getAttribute("USER_ID");
        String rule = (String) session.getAttribute("RULE");

        // ✅ 로그인 여부 판별
        boolean isLoggedIn = (userId != null);

        // ✅ JSP에서 사용될 값 전달
        request.setAttribute("IS_LOGGED_IN", isLoggedIn);
        request.setAttribute("USER_ID", userId);
        request.setAttribute("RULE", rule);

        // ✅ 메인 JSP로 이동
        request.getRequestDispatcher("/WEB-INF/views/login/main.jsp").forward(request, response);
    }
}
