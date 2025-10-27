package event1;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import user.Users;

@WebServlet("/letsgu/admin/list")
public class AdminServlet extends HttpServlet {

	private EventService service = new EventService();

	// 관리자 인증 메서드(중복코드)
	private boolean isAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		// 세션 확인
		Users loginUser = (Users) req.getSession().getAttribute("LOGIN_ID");

		// 관리자 여부 확인 ( url 직접 접근 시 접근 제한 )
		if (loginUser == null || !"ADMIN".equals(loginUser.getRule())) {
			resp.setContentType("text/html; charset=UTF-8");
			resp.getWriter().println("<script>alert('관리자 전용 페이지입니다.'); history.back();</script>");
			return false;
		}
		return true;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		//관리자 인증
		if(!isAdmin(req, resp)) return;

		service.updateEventStatus(); // 비활성화 이벤트 갱신
		List<Event> inactvieList = service.getInactiveEventList();    //비활성화 목록

		req.setAttribute("inactiveList", inactvieList);

		req.getRequestDispatcher("/WEB-INF/views/event/adminPage.jsp").forward(req, resp);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");
		
		//관리자 인증
		if (!isAdmin(req, resp)) return; 

		boolean result = service.deleteInactiveEvent();

		if (result) {
			System.out.println("[비활성화 이벤트 삭제]");
			resp.sendRedirect(req.getContextPath() + "/letsgu/admin/list");

		}

	}

}
