package event1;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import user.Users;

@WebServlet("/letsgu/event/eventdetail")
public class EventDetailServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");

		// 세션 확인
		Users loginId = getLoginUser(req);
		req.setAttribute("loginUser", loginId);

		int eventId = Integer.parseInt(req.getParameter("eventId"));

		EventService service = new EventService();
		Event event = service.getEventById(eventId);

		String keyword = req.getParameter("keyword");


		req.setAttribute("event", event);
		req.setAttribute("keyword", keyword);

		req.getRequestDispatcher("/WEB-INF/views/event/eventdetail.jsp").forward(req, resp);
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
