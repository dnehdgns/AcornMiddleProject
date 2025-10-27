package user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/letsgu/login")
public class LoginServlet extends HttpServlet {

	// inti 초기화 함수 필드 생성
	private LoginService service;
	
	@Override
	public void init () throws ServletException {
		this.service=  new LoginService(new UserDAO());
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getRequestDispatcher("/WEB-INF/views/login/login.jsp").forward(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 요청/응답 인코딩
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		// 파라미터
		String loginid = request.getParameter("login_id");
		String pw = request.getParameter("password");
		
		System.out.println("입력값 확인 => ID: " + loginid + ", PW: " + pw);

		// 서비스 호출
		Users loginUser = service.login(loginid, pw);

		if (loginUser != null) {
			HttpSession session = request.getSession();
			session.setAttribute("LOGIN_ID", loginUser); // 로그인 성공 시 세션 저장
			//session.setMaxInactiveInterval(30 * 60);
			response.sendRedirect(request.getContextPath() + "/letsgu/main");
			
		} else {
//			request.setAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
			response.getWriter().println("<script>");
            response.getWriter().println("alert('아이디 또는 비밀번호가 올바르지 않습니다.');");
            response.getWriter().println("history.back();");
            response.getWriter().println("</script>");
//			request.getRequestDispatcher("/WEB-INF/views/login/signup.jsp").forward(request, response);
		}
	}
}