package calendar;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/letsgu/mypage/event/listByDate")
public class MyCalendarListByDateServlet extends HttpServlet {
  private final MyCalendarEventDAO dao = new MyCalendarEventDAO();
  private static final SimpleDateFormat FMT = new SimpleDateFormat("yyyy-MM-dd");

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("UTF-8");
    resp.setContentType("application/json; charset=UTF-8");

    Integer userId = (Integer) req.getSession().getAttribute("loginUserId");
    if (userId == null) { resp.setStatus(401); resp.getWriter().write("{\"error\":\"unauthorized\"}"); return; }

    String ymd = req.getParameter("date");
    String source = req.getParameter("source"); // participation | bookmark
    if (ymd==null || ymd.length()!=10 || source==null) {
      resp.setStatus(400); resp.getWriter().write("{\"error\":\"date(YYYY-MM-DD), source required\"}"); return;
    }

    List<Event> list = dao.findUserEventsByDate(userId, ymd, source);

    StringBuilder json = new StringBuilder();
    json.append("{\"items\":[");
    for (int i=0;i<list.size();i++){
      if(i>0) json.append(',');
      Event e = list.get(i);
      String title = safe(e.getTitle());
      String region= safe(e.getRegion());
      String desc  = safe(e.getDescription());
      String status= safe(e.getStatus());
      String date  = (e.getEventDate()==null)? ymd : FMT.format(e.getEventDate());

      json.append('{')
          .append("\"eventId\":").append(e.getEventId()).append(',')
          .append("\"authorId\":").append(e.getAuthorId()).append(',')
          .append("\"categoryId\":").append(e.getCategoryId()).append(',')
          .append("\"title\":").append(title.isEmpty() ? "\"제목 없음\"" : "\"" + esc(title) + "\"").append(',')
          .append("\"region\":").append(region.isEmpty()? "null" : "\"" + esc(region)+"\"").append(',')
          .append("\"eventDate\":").append(date==null? "null" : "\"" + esc(date) + "\"").append(',')
          .append("\"capacity\":").append(e.getCapacity()).append(',')
          .append("\"description\":").append(desc.isEmpty()? "null" : "\"" + esc(desc) + "\"").append(',')
          .append("\"status\":").append(status.isEmpty()? "null" : "\"" + esc(status) + "\"")
          .append('}');
    }
    json.append("]}");
    resp.getWriter().write(json.toString());
  }

  private static String safe(String s){ return s==null? "":s; }
  private static String esc(String s){
    StringBuilder sb=new StringBuilder(s.length()+16);
    for(int i=0;i<s.length();i++){
      char c=s.charAt(i);
      switch(c){
        case '\"': sb.append("\\\""); break;
        case '\\': sb.append("\\\\"); break;
        case '\b': sb.append("\\b");  break;
        case '\f': sb.append("\\f");  break;
        case '\n': sb.append("\\n");  break;
        case '\r': sb.append("\\r");  break;
        case '\t': sb.append("\\t");  break;
        default:
          if(c<0x20) sb.append(String.format("\\u%04x",(int)c)); else sb.append(c);
      }
    }
    return sb.toString();
  }
}