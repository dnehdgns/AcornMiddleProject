package calendar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import event1.Event;

//DB에서 날짜별 이벤트 리스트 집계 JSON 반환
@WebServlet("/letsgu/event/listByDate")
public class MainCalendarListByDateServlet extends HttpServlet {
	
    private final MainCalendarEventDAO dao = new MainCalendarEventDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        //1.날짜 파라미터 받기
        String ymd = req.getParameter("date");
        
        if (ymd == null || ymd.trim().length() != 10) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"missing or invalid 'date' (YYYY-MM-DD)\"}");
            return;
        }

        //2.DAO 날자 이벤트 목록 조회
        List<Event> list = dao.findEventsByDate(ymd.trim());

        //3. JSON 문자열 생성
        StringBuilder json = new StringBuilder();
        
        json.append("{\"items\":[");
       
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) json.append(',');
            Event e = list.get(i);
            
            String title   = safe(e.getTitle());
            String region  = safe(e.getRegion());
            String desc    = safe(e.getDescription());
            String status  = safe(e.getStatus());
            String eventDate = (e.getEventDate() == null) ? ymd : fmt.format(e.getEventDate());

            json.append('{')
                .append("\"eventId\":").append(e.getEventId()).append(',')
                .append("\"authorId\":").append(e.getAuthorId()).append(',')
                .append("\"categoryId\":").append(e.getCategoryId()).append(',')
                .append("\"title\":").append(title.isEmpty() ? "\"제목 없음\"" : "\"" + esc(title) + "\"").append(',')
                .append("\"region\":").append(region.isEmpty() ? "null" : "\"" + esc(region) + "\"").append(',')
                .append("\"eventDate\":").append(eventDate == null ? "null" : "\"" + esc(eventDate) + "\"").append(',')
                .append("\"capacity\":").append(e.getCapacity()).append(',')
                .append("\"description\":").append(desc.isEmpty() ? "null" : "\"" + esc(desc) + "\"").append(',')
                .append("\"status\":").append(status.isEmpty() ? "null" : "\"" + esc(status) + "\"")
                .append('}');
        }

        json.append("]}");

        //4.JSON 응답
        resp.getWriter().write(json.toString());
    }

    //null 방지
    private static String safe(String s) { return s == null ? "" : s; }
   
    //특수문자 이스케이프 처리
    private static String esc(String s) {
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b");  break;
                case '\f': sb.append("\\f");  break;
                case '\n': sb.append("\\n");  break;
                case '\r': sb.append("\\r");  break;
                case '\t': sb.append("\\t");  break;
                default:
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int)c));
                    else sb.append(c);
            }
        }
        return sb.toString();
    }
}