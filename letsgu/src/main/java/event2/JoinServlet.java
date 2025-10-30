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

import user.Users;

@WebServlet("/letsgu/event/join")
public class JoinServlet extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Users user = getLoginUser(req);
		req.setAttribute("loginUser", user);

		
		System.out.println(user);
        // 2. 요청 파라미터 처리
        BufferedReader reader = req.getReader();
        JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
        int eventId = json.get("postId").getAsInt();
        String action = json.get("action").getAsString();

        // 3. DAO 호출
        JoinService joinService = new JoinService();
        boolean success = false;
        JsonObject result = new JsonObject();
      
        
        if ("add".equals(action)) {
            success = joinService.insertJoin(eventId,user.getUserId());
            
        } else if ("remove".equals(action)) {
            success = joinService.removeJoin(eventId,user.getUserId());
        }
        
        
        int joinCnt = joinService.getJoinCount(eventId);
        result.addProperty("success", success);
        result.addProperty("joinCount", joinCnt); // ✅ 실시간 참여 인원 포함

        // 4. 응답 반환
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(result.toString()); // ✅ 여기만 바꾸면 돼요!
	}
	
	private Users getLoginUser(HttpServletRequest req) {
		HttpSession session = req.getSession(false);

		if (session != null) {
			return (Users) session.getAttribute("LOGIN_ID");

		}
		return null;
	}
}
