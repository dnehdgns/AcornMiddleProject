package event2;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import user.Users;

@WebServlet("/letsgu/event/bookmark")
public class BookMarkServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    resp.setContentType("text/plain");
	    resp.getWriter().write("북마크 서블릿이 정상적으로 연결되었습니다.");
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 로그인 사용자 확인
//        HttpSession session = req.getSession();
//        Users user = (Users) session.getAttribute("user");
//        if (user == null) {
//            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }
        
		Users user = getLoginUser(req);
		req.setAttribute("loginUser", user);

        // 2. 요청 파라미터 처리
        BufferedReader reader = req.getReader();
        JsonReader jsonReader = new JsonReader(reader);
        jsonReader.setLenient(true); 
        JsonObject json = JsonParser.parseReader(jsonReader).getAsJsonObject();

        int eventId = json.get("postId").getAsInt();
        String action = json.get("action").getAsString();

        // 3. DAO 호출
        BookMarkService bookService = new BookMarkService();
        boolean success = false;
        if ("add".equals(action)) {
            success = bookService.insertBookMark(eventId,user.getUserId());
        } else if ("remove".equals(action)) {
            success = bookService.deleteBookMark(eventId,user.getUserId());
        }

        // 4. 응답 반환
        resp.setContentType("application/json");
        resp.getWriter().write("{\"success\":" + success + "}");

	}
	
	private Users getLoginUser(HttpServletRequest req) {
		HttpSession session = req.getSession(false);

		if (session != null) {
			return (Users) session.getAttribute("LOGIN_ID");

		}
		return null;
	}
}
