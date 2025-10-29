package user;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet("/letsgu/update")
@MultipartConfig(maxFileSize = 10 * 1024 * 1024) // 10MB
public class UpdateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserDAO dao = new UserDAO();
    private final UpdateService service = new UpdateService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ✅ 세션에서 USER_ID 가져오기
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("USER_ID");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/letsgu/login");
            return;
        }

        // ✅ 현재 사용자 정보 조회 → update.jsp 폼 프리필
        try {
            Users user = dao.findByUserId(userId);
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/letsgu/login");
                return;
            }
            request.setAttribute("user", user);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "프로필 정보를 불러오는 중 오류가 발생했습니다.");
        }

        request.getRequestDispatcher("/WEB-INF/views/login/update.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // ✅ 세션에서 USER_ID 확인
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("USER_ID");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/letsgu/login");
            return;
        }

        // ✅ 폼 데이터 받기
        String username = request.getParameter("username");
        String email    = request.getParameter("email");
        String gender   = request.getParameter("gender");
        String agegroup = request.getParameter("age_group");

        // ✅ 파일 업로드 처리
        Part filePart = request.getPart("upload_img");
        String fileName = null;

        if (filePart != null && filePart.getSize() > 0) {
            fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

            String uploadPath = getServletContext().getRealPath("/uploads");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            filePart.write(uploadPath + File.separator + fileName);
        }

        // ✅ DTO 구성 (PK 기준)
        Users user = new Users();
        user.setUserId(userId);      // WHERE USER_ID = ?
        user.setName(username);
        user.setEmail(email);
        user.setGender(gender);
        user.setAgegroup(agegroup);
        user.setUploadimg(fileName); // null이면 DAO에서 NVL로 기존값 유지

        // ✅ 서비스 호출
        boolean ok;
        try {
            ok = service.updateProfile(user); // 내부에서 DAO.updateProfile 호출
        } catch (Exception e) {
            e.printStackTrace();
            ok = false;
        }

        // ✅ 결과 처리
        if (ok) {
            response.sendRedirect(request.getContextPath() + "/letsgu/mypage?updated=1");
        } else {
            request.setAttribute("error", "프로필 업데이트에 실패했습니다.");
            request.setAttribute("user", dao.findByUserId(userId)); // 기존 값 유지
            request.getRequestDispatcher("/WEB-INF/views/login/update.jsp").forward(request, response);
        }
    }
}
