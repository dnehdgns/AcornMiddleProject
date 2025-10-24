package event;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/letsgu/event/eventdetail")
public class EventDetailServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");
//		req.getSession().setAttribute("userId", 2);     세션 테스트용
		
		Users loginId = getLoginUser(req);
		req.setAttribute("loginId", loginId);

		int eventId = Integer.parseInt(req.getParameter("eventId"));

		EventService e_service = new EventService();
		Event event = e_service.getEventById(eventId);

		req.setAttribute("event", event);

		req.getRequestDispatcher("/WEB-INF/views/event/eventdetail.jsp").forward(req, resp);
	}

	// 세션 검사
	private Users getLoginUser(HttpServletRequest req) {
		HttpSession session = req.getSession(false);

		if (session != null) {
			return (Users) session.getAttribute("loginId");

		}
		return null;
	}
}
