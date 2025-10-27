package event1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import user.Users;

@WebServlet("/letsgu/event/reg")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5)
public class EventRegServlet extends HttpServlet {

	private static final String UPLOAD_DIR = "upload";
	private CategoryService c_service = new CategoryService();
	private EventService e_service = new EventService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 로그인 세션 확인
		Users loginUser = getLoginUser(req);

		if (loginUser == null) {
			resp.sendRedirect(req.getContextPath() + "/letsgu/login");
			return;
		}

		List<Category> categoryList = c_service.getCategoryList();
		List<String> regionList = Arrays.asList("강남구", "마포구", "서초구", "종로구", "용산구", "은평구"); // 임시 (나중에 지역 결정)


		req.setAttribute("categoryList", categoryList);
		req.setAttribute("regionList", regionList);
		req.setAttribute("selectedCategory", req.getParameter("category_id"));

		req.getRequestDispatcher("/WEB-INF/views/event/eventReg.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");

		// 로그인 세션 확인(로그인 풀림 방지)
		Users loginUser = getLoginUser(req);

		if (loginUser == null) {
			resp.sendRedirect(req.getContextPath() + "/letsgu/login");
			return;
		}

		// 로그인한 경우
		int authorId = loginUser.getUserId();
		
		//파일 업로드 (새 글 등록 - 기존 이미지 없음)
		String uploadImg = handleFileUpload(req, null);

	
		//값 가져오기
		int categoryId = Integer.parseInt(req.getParameter("category_id"));
		String title = req.getParameter("title");
		String region = req.getParameter("region");
		String eventDateStr = req.getParameter("event_date");
		String capacityStr = req.getParameter("capacity");
		String description = req.getParameter("description");
		

		Date eventDate = null;
		if (isValid(eventDateStr)) {
			eventDate = Date.valueOf(eventDateStr);
		}

		int capacity = 0;
		if (isValid(capacityStr)) {
			capacity = Integer.parseInt(capacityStr);
		}

		// 모델
		Event event = new Event(authorId, categoryId, title, region, eventDate, capacity, description, uploadImg);

		//등록처리
		boolean result = e_service.regEvent(event);

		if (result) {
			System.out.println("[이벤트 등록 성공]");
			resp.sendRedirect(req.getContextPath() + "/letsgu/event/list");
		} else {
			System.out.println("[이벤트 등록 실패]");
			req.setAttribute("categoryList", c_service.getCategoryList());
			req.setAttribute("regionList", Arrays.asList("강남구", "마포구", "서초구", "종로구", "용산구", "은평구"));   //지역 임시 설정

			req.getRequestDispatcher("/WEB-INF/views/event/eventReg.jsp").forward(req, resp);
		}
	}

	// 세션 검사
	private Users getLoginUser(HttpServletRequest req) {
		HttpSession session = req.getSession(false);

		if (session != null) {
			return (Users) session.getAttribute("LOGIN_ID");

		}
		return null;
	}
	
	// 파일 업로드 처리
		private String handleFileUpload(HttpServletRequest req, String oldUploadImg)
				throws IOException, ServletException {

			String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
			File uploadDir = new File(uploadPath);
			if (!uploadDir.exists()) uploadDir.mkdirs();

			Part filePart = req.getPart("uploadImg");
			String newFileName = extractFileName(filePart);

			if (isValid(newFileName)) {
				File newFile = new File(uploadPath, newFileName);
				Files.copy(filePart.getInputStream(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				System.out.println("[파일 업로드] 저장 완료: " + newFileName);
				return newFileName;
			}
			return oldUploadImg; // 새 글 등록이므로 null 반환
		}

	// 파일 처리
	private String extractFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		for (String token : contentDisp.split(";")) {
			if (token.trim().startsWith("filename")) {
				return token.substring(token.indexOf('=') + 2, token.length() - 1);
			}
		}
		return null;
	}
	
	//문자열 유효성 검사
	private boolean isValid(String str) {
		return str != null && !str.trim().isEmpty();
	}
}
