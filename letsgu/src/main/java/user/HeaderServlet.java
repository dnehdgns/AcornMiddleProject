package user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/letsgu/header")
public class HeaderServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	       // ✅ 로그인 시점에 세션이 이미 생성되었기 때문에 false 필요 없음
        HttpSession session = req.getSession();

        // ✅ USER_ID(PK)와 RULE(권한) 가져오기
        Integer userId = (Integer) session.getAttribute("USER_ID");
        String rule = (String) session.getAttribute("RULE");
        String eventId = req.getParameter("eventId");
        req.setAttribute("eventId", eventId);


        // ✅ 로그인 여부 판별
        boolean isLoggedIn = (userId != null);

        // ✅ JSP에서 사용될 값 전달
        req.setAttribute("IS_LOGGED_IN", isLoggedIn);
        req.setAttribute("USER_ID", userId);
        req.setAttribute("RULE", rule);
		
		req.getRequestDispatcher("/WEB-INF/views/common/header.jsp").forward(req, resp);
	}
}
