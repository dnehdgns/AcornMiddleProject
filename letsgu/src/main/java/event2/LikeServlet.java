package event2;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import user.Users;

@WebServlet("/letsgu/event/like")
public class LikeServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Users user = getLoginUser(req);
		req.setAttribute("loginUser", user);

        // 2. 요청 파라미터 처리
        BufferedReader reader = req.getReader();
        JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
        int eventId = json.get("postId").getAsInt();
        String action = json.get("action").getAsString();

        // 3. DAO 호출
        LikeService likeService = new LikeService();
        boolean success = false;
        JsonObject result = new JsonObject();
        LikeDAO daoLike = new LikeDAO();
		boolean liked = daoLike.isLiked(user.getUserId(), eventId);
		boolean disliked = daoLike.isdisLiked(user.getUserId(), eventId);
      
        
        if ("like".equals(action)) {
        	if(disliked) {
        		success = likeService.updateLikeType(user.getUserId(), eventId, "LIKE");
        	}else {
        		success = likeService.insertLike(eventId,user.getUserId());
        	}
        } else if ("dislike".equals(action)) {
        	if(liked) {
        		success = likeService.updateLikeType(user.getUserId(), eventId, "DISLIKE");
        	}else {
        		success = likeService.insertDisLike(eventId,user.getUserId());
        	}
        } else if ("removelike".equals(action)) {
        	success = likeService.removeLike(eventId, user.getUserId());
        } else if ("removedislike".equals(action)) {
        	success = likeService.removeDisLike(eventId, user.getUserId());
        }
        
        
        int likeCnt = likeService.getLikeCount(eventId);
        int dislikeCnt = likeService.getDisLikeCount(eventId);
        result.addProperty("success", success);
        result.addProperty("likeCount", likeCnt); 
        result.addProperty("dislikeCount", dislikeCnt);

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