package project;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.net.httpserver.HttpServer;

@WebServlet("/letsgu/mypage")
public class MypageServlet extends HttpServlet {

	private final UserDAO dao = new UserDAO();
	 
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// 1) 세션 안전하게 얻기 (새로 만들지 않음)
	    HttpSession session = request.getSession(false);
	    if (session == null) {
	        response.sendRedirect(request.getContextPath() + "/letsgu/login");
	        return;
	    }
	    
	    // 2) 로그인 아이디 가져오기
	    String loginId = (String) session.getAttribute("LOGIN_ID");
	    if (loginId == null || loginId.isEmpty()) {
	        response.sendRedirect(request.getContextPath() + "/letsgu/login");
	        return;
	    }
	    
	    
	 // 3) DAO/Service로 회원정보 조회
	    // 메서드 시그니처 예: public User findByLoginId(String loginId)
	    User user = dao.findLoginId(loginId);
	    if (user == null) {
	        // 아이디는 세션에 있는데 DB에 없을 때의 방어 로직
	        response.sendRedirect(request.getContextPath() + "/letsgu/login");
	        return;
	    }

	    // 4) request에 담아 JSP로 forward
	    request.setAttribute("user", user);
	    request.getRequestDispatcher("/WEB-INF/views/mypage.jsp").forward(request, response);
	       
	    
	    
	}
}
