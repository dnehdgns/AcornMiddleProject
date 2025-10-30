package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/letsgu/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private LoginService service;

    @Override
    public void init() throws ServletException {
        this.service = new LoginService(new UserDAO());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	request.getRequestDispatcher("/WEB-INF/views/login/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // 폼 파라미터
        String loginId = nvl(request.getParameter("login_id"), request.getParameter("LOGIN_ID"));
        String pw      = nvl(request.getParameter("password"), request.getParameter("PASSWORD"));

        if (isEmpty(loginId) || isEmpty(pw)) {
            response.getWriter().println("<script>alert('아이디/비밀번호를 입력해 주세요.');history.back();</script>");
            return;
        }

        // DB 로그인 검증 (Users 반환)
        Users user = service.login(loginId, pw);

        if (user == null) {
            response.getWriter().println("<script>alert('아이디 또는 비밀번호가 올바르지 않습니다.');history.back();</script>");
            return;
        }

     // 로그인 성공 시 (user != null 일 때)
        HttpSession old = request.getSession(false);
        if (old != null) old.invalidate(); // 기존 세션 삭제

        HttpSession session = request.getSession(true);

        // 글쓰기 서블릿이 기대하는 형태로 맞춰줌
        session.setAttribute("LOGIN_ID", user);  // Users 객체
        session.setAttribute("USER_ID", user.getUserId());
        session.setAttribute("RULE", user.getRule());
        session.setMaxInactiveInterval(60 * 30); // 30분 유지
        
    	String checkEvent = request.getParameter("eventId");
    	System.out.println(checkEvent);
    	if(checkEvent != null && checkEvent != "") {
	    	int eventId = Integer.parseInt(checkEvent);
	    	if(eventId>0) {
	    		response.sendRedirect(request.getContextPath() + "/letsgu/event/eventdetail?eventId=" + eventId);
	    	}else {
	    		response.sendRedirect(request.getContextPath() + "/letsgu/main");
	    	}
    	}else {
    		response.sendRedirect(request.getContextPath() + "/letsgu/main");
    	}
    }

    // 유틸
    private String nvl(String a, String b) {
        return (a != null && !a.isEmpty()) ? a : b;
    }
    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
