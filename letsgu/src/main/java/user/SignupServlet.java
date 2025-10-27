package user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.net.httpserver.HttpServer;

@WebServlet("/letsgu/signup")
public class SignupServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getRequestDispatcher("/WEB-INF/views/login/signup.jsp").forward(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		// 파마미터 값 받기
		String loginId = request.getParameter("login_id");
		String pw = request.getParameter("password");
		String name = request.getParameter("username");
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		String agegroup = request.getParameter("age_group");

		Users user = new Users();
		user.setId(loginId); // 또는 setLoginId(...)
		user.setPw(pw); // 나중에 BCrypt로 해시 권장
		user.setName(name);
		user.setGender(gender); // DB가 NOT NULL이면 폼 추가 or 기본값 처리
		user.setEmail(email);
		user.setAgegroup(agegroup);

		// ✅ Service 호출

		SignupService service = new SignupService();

		System.out.println("[SERVLET] doPost 진입");
		System.out.printf("[SERVLET] params: id=%s, pw=%s, name=%s, email=%s%n", request.getParameter("login_id"),
				request.getParameter("password"), request.getParameter("name"), request.getParameter("email"));

		boolean success = service.signUp(user);
		System.out.println("[Servlet] 회원가입 결과: " + success);

//		if (success) {
//			response.sendRedirect(request.getContextPath() + "/letsgu/login");
//		} else {
//			request.getRequestDispatcher("/WEB-INF/views/login/signup.jsp").forward(request, response);
//		}
		
		if (success) {
            response.getWriter().println("<script>");
            response.getWriter().println("alert('회원가입이 완료되었습니다. 로그인해주세요!');");
            response.getWriter().println("location.href='" + request.getContextPath() + "/letsgu/login';");
            response.getWriter().println("</script>");
        } else {
            response.getWriter().println("<script>");
            response.getWriter().println("alert('이미 존재하는 아이디이거나 회원가입에 실패했습니다.');");
            response.getWriter().println("history.back();");
            response.getWriter().println("</script>");
        }

	}
}
