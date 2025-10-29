package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/letsgu/signup")
public class SignupServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // ✅ JSP 경로는 처음 버전 기준으로 복구
        request.getRequestDispatcher("/WEB-INF/views/login/signup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ✅ 인코딩 설정
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // ✅ 폼 파라미터 수집
        String loginId = request.getParameter("login_id");
        String pw = request.getParameter("password");
        String name = request.getParameter("username");
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");
        String agegroup = request.getParameter("age_group");

        // ✅ DTO 생성
        Users user = new Users();
        user.setId(loginId);
        user.setPw(pw);
        user.setName(name);
        user.setGender(gender);
        user.setEmail(email);
        user.setAgegroup(agegroup);

        // ✅ 서비스 호출
        SignupService service = new SignupService();
        System.out.println("[SERVLET] 회원가입 요청: " + loginId);

        boolean success = service.signUp(user);
        System.out.println("[SERVLET] 회원가입 결과: " + success);

        // ✅ 결과 처리 (alert + 리디렉트)
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
