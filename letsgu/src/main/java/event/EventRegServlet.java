package event;

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

@WebServlet("/letsgu/event/reg")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5)
public class EventRegServlet extends HttpServlet {

	private static final String UPLOAD_DIR = "upload";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		//로그인 세션 확인
		Users loginUser = getLoginUser(req);

		if (loginUser == null) {
			resp.sendRedirect(req.getContextPath() + "/letsgu/login");
			return;
		}

		
	    CategoryService service = new CategoryService();
        List<Category> categoryList = service.getCategoryList();
        List<String> regionList = Arrays.asList("강남구", "마포구", "서초구", "종로구", "용산구", "은평구");    //임시

        String categoryParam = req.getParameter("category_id");
        
        req.setAttribute("categoryList", categoryList);
        req.setAttribute("regionList", regionList);
        req.setAttribute("selectedCategory", categoryParam);

		req.getRequestDispatcher("/WEB-INF/views/event/eventReg.jsp").forward(req, resp);
	}

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");

		//로그인 세션 확인(로그인 풀림 방지)
		Users loginUser = getLoginUser(req);

		if (loginUser == null) {
			resp.sendRedirect(req.getContextPath() + "/letsgu/login");
			return;
		}

		// 로그인한 경우
		int authorId = loginUser.getUser_id();

		// 업로드 폴더 설정 (webapp/upload)
		String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		System.out.println("파일 저장 경로: " + uploadPath);

		// 파일 처리
		Part filePart = req.getPart("uploadImg");         // form의 name 속성
		String fileName = extractFileName(filePart);
		if (fileName != null && !fileName.isEmpty()) {
			File file = new File(uploadPath, fileName);
			Files.copy(filePart.getInputStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}

		
		int categoryId = Integer.parseInt(req.getParameter("category_id"));
		String title = req.getParameter("title");
		String region = req.getParameter("region");
		String eventDateStr = req.getParameter("event_date");
		String capacityStr = req.getParameter("capacity");
		String description = req.getParameter("description");
		String uploadImg = fileName;

		Date eventDate = null;
		if (eventDateStr != null && !eventDateStr.isEmpty()) {
			eventDate = Date.valueOf(eventDateStr);
		}
		
		int capacity = 0;
		if (capacityStr != null && !capacityStr.isEmpty()) {
		    capacity = Integer.parseInt(capacityStr);
		}
		
		Event event = new Event(authorId, categoryId, title, region, eventDate, capacity, description, uploadImg);
		
		EventService service = new EventService();	
		boolean result = service.regEvent(event);
		

		if (result) {
			resp.sendRedirect(req.getContextPath() + "/letsgu/event/list");
		} else {
			
			CategoryService c_service = new CategoryService();
            List<Category> categoryList = c_service.getCategoryList();
            List<String> regionList = Arrays.asList("강남구", "마포구", "서초구", "종로구", "용산구", "은평구");

            req.setAttribute("categoryList", categoryList);
            req.setAttribute("regionList", regionList);
            
			req.getRequestDispatcher("/WEB-INF/views/event/eventReg.jsp").forward(req, resp);
		}
	}
	
	//세션 검사
	private Users getLoginUser(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		
		if(session != null) {
			return (Users) session.getAttribute("loginId");
			
		}
		return null;
	}

	//파일 처리
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
