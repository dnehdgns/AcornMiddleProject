package calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet({"/letsgu/myCalendar/count", "/mypage/cal/count"})
public class MyCalendarCountServlet extends HttpServlet {

    private static final String DEFAULT_TYPE = "bookmark";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 0) 인코딩/캐시 방지 (탭 전환/달 이동 시 이전 응답 캐싱으로 꼬이는 문제 예방)
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        resp.setHeader("Pragma", "no-cache");

        // 1) 로그인 확인
        HttpSession session = req.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;
        if (userId == null || userId.trim().isEmpty()) {
            sendJson(resp, 401, "{\"counts\":{}}");
            return;
        }

        // 2) 파라미터 파싱
        String startStr = req.getParameter("start"); // YYYY-MM-DD
        String endStr   = req.getParameter("end");   // YYYY-MM-DD
        String type     = sanitizeType(req.getParameter("type"));

        LocalDate start, end;
        try {
            if (startStr == null || endStr == null) {
                throw new IllegalArgumentException("start/end required");
            }
            start = LocalDate.parse(startStr);
            end   = LocalDate.parse(endStr);
            // 역전 방지: start > end 이면 스왑
            if (start.isAfter(end)) {
                LocalDate tmp = start; start = end; end = tmp;
            }
        } catch (Exception e) {
            // 잘못된 날짜 포맷 등
            sendJson(resp, 400, "{\"counts\":{}}");
            return;
        }

        // 3) DAO 호출
        Map<String, Integer> counts = new LinkedHashMap<>();
        try {
            MyCalendarEventDAO dao = new MyCalendarEventDAO(); // 같은 package면 import 불필요
            counts = dao.findMyCounts(userId, start, end, type); // {"YYYY-MM-DD": N}
            if (counts == null) counts = new LinkedHashMap<>();
        } catch (Exception e) {
            e.printStackTrace();
            sendJson(resp, 500, "{\"counts\":{}}");
            return;
        }

        // 4) JSON 직렬화
        StringBuilder json = new StringBuilder("{\"counts\":{");
        boolean first = true;
        for (Map.Entry<String,Integer> it : counts.entrySet()) {
            if (!first) json.append(',');
            json.append('"').append(esc(it.getKey())).append("\":").append(it.getValue());
            first = false;
        }
        json.append("}}");

        sendJson(resp, 200, json.toString());
    }

    // --- 유틸 ---

    private static String sanitizeType(String type) {
        if (type == null) return DEFAULT_TYPE;
        String t = type.trim().toLowerCase();
        // 허용값만 통과
        if (!"participation".equals(t) && !"bookmark".equals(t)) {
            t = DEFAULT_TYPE;
        }
        return t;
    }

    private static void sendJson(HttpServletResponse resp, int status, String body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json; charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.write(body);
        }
    }

    private static String esc(String s) {
        if (s == null) return "";
        // 날짜 키(YYYY-MM-DD)라도 안전하게 이스케이프
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
