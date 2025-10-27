package event1;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import user.Users;

@WebServlet("/letsgu/event/delete")
public class EventDeleteServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");

		// 로그인 세션 확인
		Users loginUser = getLoginUser(req);

		if (loginUser == null) {
			resp.sendRedirect(req.getContextPath() + "/letsgu/login");
			return;
		}
		
		int userId = loginUser.getUserId();

		int eventId = Integer.parseInt(req.getParameter("eventId"));

		EventService service = new EventService();
		boolean result = service.deleteEvent(eventId, userId);

		if (result) {
			resp.sendRedirect(req.getContextPath() + "/letsgu/event/list");
		}
	}

	// 세션 검사
	private Users getLoginUser(HttpServletRequest req) {
		HttpSession session = req.getSession(false);

		if (session != null) {
			return (Users) session.getAttribute("LOGIN_ID");

		}
		return null;
	}

}
