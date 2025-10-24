package event;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/letsgu/admin/list")
public class AdminServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		EventService service = new EventService();
		List<Event> inactvieList = service.getInactiveEventList();
		
		req.setAttribute("inactiveList", inactvieList);
		
		req.getRequestDispatcher("/WEB-INF/views/event/adminPage.jsp").forward(req, resp);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("utf-8");
		
		EventService service = new EventService();
		boolean result = service.deleteInactiveEvent();
		
		if(result) {
			resp.sendRedirect(req.getContextPath() + "/letsgu/admin/list");
			
		}
		
	}

}
