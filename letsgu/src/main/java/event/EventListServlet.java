package event;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/letsgu/event/list")
public class EventListServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");

		int currentPage = 1;
		int pageSize = 5;
		int grpSize = 5;

		if (req.getParameter("p") != null) {
			currentPage = Integer.parseInt(req.getParameter("p"));
		}

		String categoryParam = req.getParameter("category");
		String regionParam = req.getParameter("region");
		String fileName = req.getParameter("fileName");

		EventService e_service = new EventService();
		CategoryService c_service = new CategoryService();

		List<Event> eventList = null;
		List<Category> categoryList = null;
		List<String> regionList = null;

		// 페이징 처리
		int totalRecords = e_service.getTotalCount();
		System.out.println("전체 이벤트 데이터 수 : " + totalRecords);
		PageHandler page = new PageHandler(pageSize, grpSize, totalRecords, currentPage);

		// 전체 or 지역 선택 + 전체 or 카테고리 선택
		if (regionParam != null && !regionParam.isEmpty() && !"전체".equals(regionParam)) { // 지역 선택된 경우
			String region = regionParam;

			if (categoryParam != null && !categoryParam.isEmpty()) {
				// 특정 지역 + 특정 카테고리
				int categoryId = Integer.parseInt(categoryParam);
				eventList = e_service.getEventListByRegionAndCategory(region, categoryId);
			} else {
				// 특정 지역 + 전체 카테고리
				eventList = e_service.getEventListByRegion(region);
			}

		} else { // 지역 '전체'인 경우
			if (categoryParam != null && !categoryParam.isEmpty()) {
				// 전체 지역 + 특정 카테고리
				int categoryId = Integer.parseInt(categoryParam);
				eventList = e_service.getEventListByCategory(categoryId);
			} else {
				// 전체 지역 + 전체 카테고리
				eventList = e_service.getEventList(currentPage, pageSize);
			}
		}

		regionList = e_service.getRegionList();
		categoryList = c_service.getCategoryList();

		req.setAttribute("regionList", regionList);
		req.setAttribute("categoryList", categoryList);
		req.setAttribute("eventList", eventList);
		req.setAttribute("page", page);
		req.setAttribute("fileName", fileName);

		req.getRequestDispatcher("/WEB-INF/views/event/eventList.jsp").forward(req, resp);

	}
}
