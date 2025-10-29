package event1;

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

import user.Users;

@WebServlet("/letsgu/event/update")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5)
public class EventUpdateServlet extends HttpServlet {

	private static final String UPLOAD_DIR = "upload";

	private EventService e_service = new EventService();
	private CategoryService c_service = new CategoryService();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 로그인 세션 확인
		Users loginUser = getLoginUser(req);
		if (loginUser == null) {
			resp.sendRedirect(req.getContextPath() + "/letsgu/login");
			return;
		}

		
		int eventId = Integer.parseInt(req.getParameter("eventId"));
		Event event = e_service.getEventById(eventId);
		
		String selectedParam = req.getParameter("category_id");
		int selectedCategory;
		if (isValid(selectedParam)) {
			selectedCategory = Integer.parseInt(selectedParam);
		} else {
			selectedCategory = event.getCategoryId(); // 기본값: 기존 이벤트의 카테고리
		}
		
		
		List<Category> categoryList = c_service.getCategoryList();
		List<String> regionList = e_service.getRegionList();

		req.setAttribute("event", event);
		req.setAttribute("categoryList", categoryList);
		req.setAttribute("regionList", regionList);
		req.setAttribute("selectedCategory", selectedCategory);

		req.getRequestDispatcher("/WEB-INF/views/event/eventUpdate.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");

		// 로그인 세션 확인
		Users loginUser = getLoginUser(req);
		if (loginUser == null) {
			resp.sendRedirect(req.getContextPath() + "/letsgu/login");
			return;
		}
		
		int userId = loginUser.getUserId();

		//파라미터 추출
		int eventId = Integer.parseInt(req.getParameter("eventId"));
		int categoryId = Integer.parseInt(req.getParameter("category_id"));
		String title = req.getParameter("title");
		String region = req.getParameter("region");
		String eventDateStr = req.getParameter("event_date");
		int capacity = Integer.parseInt(req.getParameter("capacity"));
		String description = req.getParameter("description");
		String oldUploadImg = req.getParameter("oldUploadImg");
		
		Date eventDate = (isValid(eventDateStr)) ? Date.valueOf(eventDateStr) : null;
		
		//파일 업로드
		String uploadImg = handleFileUpload(req, oldUploadImg);


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

		boolean result = e_service.modifyEvent(event);

		if (result) {
			resp.sendRedirect(req.getContextPath() + "/letsgu/event/eventdetail?eventId=" + eventId);
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
	
	//문자열 유효성 검사
	private boolean isValid(String value) {
		return value != null && !value.trim().isEmpty();
	}
	
	//파일 업로드 처리
	private String handleFileUpload(HttpServletRequest req, String oldUploadImg) throws IOException, ServletException {
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
		return oldUploadImg; // 새 파일 없으면 기존 유지
	}

	// 파일명 추출
	private String extractFileName(Part part) {
		if (part == null) return null;
		String header = part.getHeader("content-disposition");
		for (String token : header.split(";")) {
			if (token.trim().startsWith("filename")) {
				String fileName = token.substring(token.indexOf('=') + 2, token.length() - 1);
				return fileName.replace("\\", "/").substring(fileName.lastIndexOf("/") + 1);
			}
		}
		return null;
	}
}
