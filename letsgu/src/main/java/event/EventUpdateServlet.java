package event;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet("/letsgu/event/update")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5)
public class EventUpdateServlet extends HttpServlet {

	private static final String UPLOAD_DIR = "upload";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 로그인 세션 확인
		Users loginUser = getLoginUser(req);

		if (loginUser == null) {
			resp.sendRedirect(req.getContextPath() + "/letsgu/login");
			return;
		}

		int eventId = Integer.parseInt(req.getParameter("eventId"));

		EventService e_service = new EventService();
		CategoryService c_service = new CategoryService();

		Event event = e_service.getEventById(eventId);
		List<Category> categoryList = c_service.getCategoryList();
		List<String> regionList = e_service.getRegionList();

		req.setAttribute("event", event);
		req.setAttribute("categoryList", categoryList);
		req.setAttribute("regionList", regionList);

		req.getRequestDispatcher("/WEB-INF/views/event/eventUpdate.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");

//		int userId = (int) req.getSession().getAttribute("userId");  세션 테스트용
		// 로그인 세션 확인
		Users loginUser = getLoginUser(req);

		if (loginUser == null) {
			resp.sendRedirect(req.getContextPath() + "/letsgu/login");
			return;
		}
		
		int userId = loginUser.getUser_id();

		// 이미지 저장 위치 설정
		String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		System.out.println("파일 저장 경로: " + uploadPath);

		int eventId = Integer.parseInt(req.getParameter("eventId"));
		int categoryId = Integer.parseInt(req.getParameter("category_id"));
		String title = req.getParameter("title");
		String region = req.getParameter("region");
		String eventDateStr = req.getParameter("event_date");
		int capacity = Integer.parseInt(req.getParameter("capacity"));
		String description = req.getParameter("description");

		Date eventDate = null;
		if (eventDateStr != null && !eventDateStr.isEmpty()) {
			eventDate = Date.valueOf(eventDateStr);
		}

		// 업로드 파일 이미지 변경 (기본은 기존 이미지 유지)
		String oldUploadImg = req.getParameter("oldUploadImg");
		String uploadImg = oldUploadImg;

		Part filePart = req.getPart("uploadImg"); // form의 name 속성
		String newFileName = extractFileName(filePart);

		if (newFileName != null && !newFileName.isEmpty()) {
			File file = new File(uploadPath, newFileName);
			Files.copy(filePart.getInputStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			uploadImg = newFileName; // DB에는 새 파일명 저장
		}

		Event event = new Event();
		event.setEventId(eventId);
		event.setAuthorId(userId); // 본인 글 확인용
		event.setCategoryId(categoryId);
		event.setTitle(title);
		event.setRegion(region);
		event.setEventDate(eventDate);
		event.setCapacity(capacity);
		event.setDescription(description);
		event.setUploadImg(uploadImg);

		EventService service = new EventService();
		boolean result = service.modifyEvent(event);

		if (result) {
			resp.sendRedirect(req.getContextPath() + "/letsgu/event/eventdetail?eventId=" + eventId);
		}

	}

	// 세션 검사
	private Users getLoginUser(HttpServletRequest req) {
		HttpSession session = req.getSession(false);

		if (session != null) {
			return (Users) session.getAttribute("loginId");

		}
		return null;
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
}
