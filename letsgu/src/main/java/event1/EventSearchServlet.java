package event1;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/letsgu/event/search")
public class EventSearchServlet extends HttpServlet {

	private EventService e_service = new EventService();
	private CategoryService c_service = new CategoryService();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");

		String keyword = req.getParameter("keyword");

		//공백 검색 시
		if (keyword == null || keyword.trim().isEmpty()) {
			resp.sendRedirect(req.getContextPath() + "/letsgu/event/list");
			return;
		}
		
		List<Event> eventList = e_service.searchEventByTitle(keyword);     //제목 검색
		List<String> regionList = e_service.getRegionList();                            //지역 + 카테고리 리스트
		List<Category> categoryList = c_service.getCategoryList();
		
		
		req.setAttribute("eventList", eventList);
		req.setAttribute("keyword", keyword.trim());
		req.setAttribute("regionList", regionList);
		req.setAttribute("categoryList", categoryList);
		
		req.getRequestDispatcher("/WEB-INF/views/event/eventList.jsp").forward(req, resp);

	}
}
