package event1;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/letsgu/event/list")
public class EventListServlet extends HttpServlet {

	private EventService e_service = new EventService();
	private CategoryService c_service = new CategoryService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");

		// 상태 갱신(마감일, 비추천)
		e_service.updateEventStatus();

		// 요청 파라미터 처리
		int currentPage = 1;
		int pageSize = 5;
		int grpSize = 5;

		if (req.getParameter("p") != null) {
			currentPage = Integer.parseInt(req.getParameter("p"));
		}

		String categoryParam = req.getParameter("category");
		String regionParam = req.getParameter("region");
		String fileName = req.getParameter("fileName");

		// 조회 데이터
		List<Event> eventList;
		List<String> regionList = e_service.getRegionList();
		List<Category> categoryList = c_service.getCategoryList();

		// 필터 조건별 이벤트 조회
		if (isValid(regionParam) && !"전체".equals(regionParam)) { // 지역 선택된 경우

			if (isValid(categoryParam)) {
				// 특정 지역 + 특정 카테고리
				int categoryId = Integer.parseInt(categoryParam);
				eventList = e_service.getEventListByRegionAndCategory(regionParam, categoryId);
			} else {
				// 특정 지역 + 전체 카테고리
				eventList = e_service.getEventListByRegion(regionParam);
			}

		} else { // 지역 '전체'인 경우
			if (isValid(categoryParam)) {
				// 전체 지역 + 특정 카테고리
				int categoryId = Integer.parseInt(categoryParam);
				eventList = e_service.getEventListByCategory(categoryId);
			} else {
				// 전체 지역 + 전체 카테고리
				eventList = e_service.getEventList(currentPage, pageSize);
			}
		}

		// 페이징 처리
		int totalRecords = e_service.getTotalCount();
		PageHandler page = new PageHandler(pageSize, grpSize, totalRecords, currentPage);
		System.out.println("[ 전체 이벤트 수 ] : " + totalRecords);

		//데이터 심기
		req.setAttribute("regionList", regionList);
		req.setAttribute("categoryList", categoryList);
		req.setAttribute("eventList", eventList);
		req.setAttribute("page", page);
		req.setAttribute("fileName", fileName);

		req.getRequestDispatcher("/WEB-INF/views/event/eventList.jsp").forward(req, resp);

	}

	// 문자열 유효성 검사
	private boolean isValid(String param) {
		return param != null && !param.trim().isEmpty();
	}
	
	
}
