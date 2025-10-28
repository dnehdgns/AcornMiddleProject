package calendar;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

	@WebServlet("/letsgu/myCalendar")
	public class MyCalendarServlet  extends HttpServlet{
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			

//	    HttpSession session = req.getSession(false);
//	    String userId = (session != null) ? (String) session.getAttribute("userId") : null;
//	    if (userId == null) {
//	        resp.sendRedirect(req.getContextPath() + "/letsgu/login");
//	        return;
//	     }

	    // 기본 탭 (bookmark | participation)
	    String type = req.getParameter("type");
	    if (!"participation".equalsIgnoreCase(type)) {
	          type = "bookmark";
	    }
	    req.setAttribute("mycalType", type);

	    // 상세 페이지 URL 
	    req.setAttribute("eventDetailUrl", req.getContextPath() + "/letsgu/event/eventdetail");	
			
		req.getRequestDispatcher("/WEB-INF/views/calendar/MyCalendar.jsp").forward(req, resp);
		}
	}
