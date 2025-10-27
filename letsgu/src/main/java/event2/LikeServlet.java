package event2;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/letsgu/event/like")
public class LikeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        int eventId = Integer.parseInt(req.getParameter("event_id"));
        String type = req.getParameter("type");

        Object uidObj = req.getSession().getAttribute("userid");
        if (uidObj == null) {
            resp.getWriter().write("{\"message\":\"로그인이 필요합니다.\"}");
            return;
        }
        int userId = (int) uidObj;

        LikeDAO dao = new LikeDAO();
        String message;
        Map<String, Integer> counts;

        try {
            if (dao.hasAlreadyLiked(eventId, userId, type)) {
                dao.deleteLike(eventId, userId, type);
                message = type + " 취소됨!";
            } else {
                dao.insertLike(eventId, userId, type);
                message = type + " 처리 완료!";
            }

            counts = dao.getLikeCounts(eventId);
            resp.getWriter().write("{\"message\":\"" + message + "\",\"likeCount\":" + counts.get("like") + ",\"dislikeCount\":" + counts.get("dislike") + "}");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.getWriter().write("{\"message\":\"서버 오류 발생\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int eventId = Integer.parseInt(req.getParameter("event_id"));
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        LikeDAO dao = new LikeDAO();
        try {
            Map<String, Integer> counts = dao.getLikeCounts(eventId);
            resp.getWriter().write("{\"likeCount\":" + counts.get("like") + ",\"dislikeCount\":" + counts.get("dislike") + "}");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.getWriter().write("{\"likeCount\":0,\"dislikeCount\":0}");
        }
    }
}