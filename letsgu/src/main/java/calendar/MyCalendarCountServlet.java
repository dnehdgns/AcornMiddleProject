package calendar;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebServlet("/letsgu/mypage/cal/count")
public class MyCalendarCountServlet extends HttpServlet {
  private final MyCalendarEventDAO dao = new MyCalendarEventDAO();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("UTF-8");
    resp.setContentType("application/json; charset=UTF-8");

    Integer userId = (Integer) req.getSession().getAttribute("loginUserId");
    if (userId == null) { resp.setStatus(401); resp.getWriter().write("{\"error\":\"unauthorized\"}"); return; }

    String start = req.getParameter("start");
    String end   = req.getParameter("end");
    String source= req.getParameter("source"); // participation | bookmark
    if (start==null || end==null || source==null) {
      resp.setStatus(400); resp.getWriter().write("{\"error\":\"start,end,source required\"}"); return;
    }

    Map<String,Integer> counts = dao.countsByRangeForUser(userId, start, end, source);

    StringBuilder json = new StringBuilder("{\"counts\":{");
    boolean first = true;
    for (Map.Entry<String,Integer> e: counts.entrySet()) {
      if(!first) json.append(',');
      json.append('"').append(e.getKey()).append("\":").append(e.getValue());
      first = false;
    }
    json.append("}}");
    resp.getWriter().write(json.toString());
  }
}