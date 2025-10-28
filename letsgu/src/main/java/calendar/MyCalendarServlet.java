package calendar;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

	@WebServlet("/letsgu/MyCalendar")
	public class MyCalendarServlet  extends HttpServlet{
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			
		    Integer userId = (Integer) req.getSession().getAttribute("loginUserId");
		    if (userId == null) {
		      resp.sendRedirect(req.getContextPath() + "/login"); // 로그인 페이지로
		      return;
		    }
		    
			req.getRequestDispatcher("/WEB-INF/views/calendar/MyCalendar.jsp").forward(req, resp);
		}
	}
