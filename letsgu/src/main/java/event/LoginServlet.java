package event;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/letsgu/login")
public class LoginServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.getRequestDispatcher("/WEB-INF/views/event/login.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("utf-8");
		
		int userId = Integer.parseInt(req.getParameter("user_id"));
		
		if (userId < 1 || userId > 5) {
            resp.getWriter().println("<script>alert('존재하지 않는 사용자입니다.'); history.back();</script>");
            return;
        }
		
		Users user = new Users();
		user.setUser_id(userId);
        user.setUsername("테스트유저" + userId);

        HttpSession session = req.getSession();
        session.setAttribute("loginId", user);

        System.out.println("로그인 성공: user_id=" + userId);
        resp.sendRedirect(req.getContextPath() + "/letsgu/event/reg");
	}
}
