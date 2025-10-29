package calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;

@WebServlet({"/letsgu/myCalendar/list", "/mypage/cal/list"})
public class MyCalendarListByDateServlet extends HttpServlet {

    private static final String DEFAULT_TYPE = "bookmark";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 인코딩/캐시 방지
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        resp.setHeader("Pragma", "no-cache");

        // 1. 로그인 확인
        HttpSession session = req.getSession(false);
        String userId = null;
        if (session != null) {
            Object v = session.getAttribute("user_id");
            if (v != null) userId = String.valueOf(v);
        }

        // 2. 파라미터
        String dateStr = req.getParameter("date"); 
        String type    = sanitizeType(req.getParameter("type"));

        LocalDate date;
        try {
            if (dateStr == null || dateStr.trim().isEmpty()) {
                throw new IllegalArgumentException("date required");
            }
            date = LocalDate.parse(dateStr);
        } catch (Exception e) {
            sendJson(resp, 400, "{\"items\":[]}");
            return;
        }
        
        // 3. userId 없으면 빈 결과
        if (userId == null || userId.trim().isEmpty()) {
            sendJson(resp, 200, "{\"items\":[]}");
            return;
        }

        // 4.DAO 호출
        List<Map<String,Object>> items;
        try {
            MyCalendarEventDAO dao = new MyCalendarEventDAO();
            items = dao.findMyListByDate(userId, date, type);
            if (items == null) items = Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            sendJson(resp, 500, "{\"items\":[]}");
            return;
        }

        // 5. JSON 직렬화
        StringBuilder sb = new StringBuilder("{\"items\":[");
        boolean first = true;
        for (Map<String,Object> row : items) {
            if (!first) sb.append(',');
            sb.append('{');

            sb.append("\"eventId\":").append(n2(row.get("eventId"))).append(',');
            sb.append("\"title\":\"").append(esc(s2(row.get("title")))).append("\",");
            sb.append("\"eventDate\":\"").append(esc(date2(row.get("eventDate")))).append("\",");
            sb.append("\"region\":\"").append(esc(s2(row.get("region")))).append("\",");
            sb.append("\"capacity\":").append(n2(row.get("capacity"))).append(',');
            sb.append("\"status\":\"").append(esc(s2(row.get("status")))).append("\",");
            sb.append("\"description\":\"").append(esc(s2(row.get("description")))).append("\",");
            sb.append("\"authorId\":\"").append(esc(s2(row.get("authorId")))).append("\"");
            sb.append('}');
            first = false;
        }
        sb.append("]}");

        sendJson(resp, 200, sb.toString());
    }

    // --- 유틸 ---

    private static String sanitizeType(String type) {
        if (type == null) return DEFAULT_TYPE;
        String t = type.trim().toLowerCase();
        return ("participation".equals(t) || "bookmark".equals(t)) ? t : DEFAULT_TYPE;
    }

    private static void sendJson(HttpServletResponse resp, int status, String body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json; charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) { out.write(body); }
    }

    private static String s2(Object o) { return (o == null) ? "" : String.valueOf(o); }

    private static String date2(Object o) {
        if (o == null) return "";
        if (o instanceof java.sql.Date) return ((java.sql.Date)o).toLocalDate().toString();
        if (o instanceof java.util.Date) {
            return new java.sql.Date(((java.util.Date)o).getTime()).toLocalDate().toString();
        }
        return String.valueOf(o); 
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private static String n2(Object o) {
        if (o == null) return "null";
        if (o instanceof Number) return String.valueOf(((Number)o).longValue());
        try { return String.valueOf(Long.parseLong(String.valueOf(o))); }
        catch (Exception ignore) { return "null"; }
    }
}
