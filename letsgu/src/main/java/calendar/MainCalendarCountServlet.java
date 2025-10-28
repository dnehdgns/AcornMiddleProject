package calendar;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

// DB에서 이벤트 개수 집계 JSON 반환
@WebServlet("/letsgu/cal/event/count")
public class MainCalendarCountServlet extends HttpServlet {

	
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String URL    = "jdbc:oracle:thin:@localhost:1521/testdb";
    private static final String USER   = "scott";
    private static final String PW     = "tiger";
    

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        
    	req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        //1.기간 파라미터 받기
        String start = req.getParameter("start"); 
        String end   = req.getParameter("end");  
     
        if (start == null || end == null) {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"start/end (YYYY-MM-DD) required\"}");
            return;
        }

        //2.SQL 실행 
        String sql =
            "SELECT TO_CHAR(event_date,'YYYY-MM-DD') AS d, COUNT(*) AS cnt " +
            "FROM event " +
            "WHERE event_date >= TO_DATE(?, 'YYYY-MM-DD') " +
            "  AND event_date <  TO_DATE(?, 'YYYY-MM-DD') " +
            "GROUP BY TO_CHAR(event_date,'YYYY-MM-DD')";

        //결과 Map(key,value)
        Map<String, Integer> counts = new LinkedHashMap<>();
        
        //3.JDBC 
        try {
            // 드라이버 로드
            Class.forName(DRIVER);

            //DB 접속 실행
            try (Connection con = DriverManager.getConnection(URL, USER, PW);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, start);
                ps.setString(2, end);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        counts.put(rs.getString("d"), rs.getInt("cnt"));
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            resp.getWriter().write("{\"error\":\"Oracle Driver not found. Put ojdbc8.jar in WEB-INF/lib\"}");
            return;
        } catch (SQLException e) {
            resp.getWriter().write("{\"error\":\"" + "\"}");
            return;
        }

        //4. JSON 문자열 생성
        StringBuilder json = new StringBuilder("{\"counts\":{");
     
        boolean first = true;
       
        for (Map.Entry<String, Integer> it : counts.entrySet()) {
            if (!first) json.append(',');
            json.append('"').append(it.getKey()).append("\":").append(it.getValue());
            first = false;
        }
        json.append("}}");

        //5. JSON 응답
        resp.getWriter().write(json.toString());
    }

}