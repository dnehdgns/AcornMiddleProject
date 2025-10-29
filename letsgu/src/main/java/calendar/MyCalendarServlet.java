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
			
//	    user_id =1 테스트
			HttpSession session = req.getSession();
		    session.setAttribute("user_id", "1");	
			
			
			
//	    HttpSession session = req.getSession(false);
//
//		String userId = null;
//	    if (session != null) {
//	            Object v = session.getAttribute("user_id");
//	            if (v != null) userId = String.valueOf(v);
//	        }

	    // 기본 탭 (bookmark)
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
