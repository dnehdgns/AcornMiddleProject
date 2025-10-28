package calendar;

import java.sql.*;
import java.util.*;

//캘린더 DAO 
public class MainCalendarEventDAO {

    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String URL    = "jdbc:oracle:thin:@localhost:1521:testdb";
    private static final String USER   = "scott";
    private static final String PASS   = "tiger";

    //드라이버 로드
    static {
        try { Class.forName(DRIVER); } 
        catch (ClassNotFoundException e) { throw new RuntimeException(e); }
    }
    //DB연결
    private Connection dbcon() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // 일자별 개수 집계 YYYY-MM-DD ~ YYYY-MM-DD
    public Map<String,Integer> countsByRange(String startYmd, String endYmd) {
      
    	String sql =
            "SELECT TO_CHAR(e.event_date, 'YYYY-MM-DD') AS d, COUNT(*) AS cnt " +
            "FROM event e " +
            "WHERE TO_CHAR(e.event_date, 'YYYY-MM-DD') BETWEEN ? AND ? " +
            "GROUP BY TO_CHAR(e.event_date, 'YYYY-MM-DD')";

        Map<String,Integer> map = new LinkedHashMap<>();
        
        try (Connection con = dbcon();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, startYmd);
            ps.setString(2, endYmd);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getString("d"), rs.getInt("cnt"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
    
    //날짜별 목록 YYYY-MM-DD 조회 
    public List<Event> findEventsByDate(String ymd) {
        String sql =
            "SELECT e.*, c.category_name " +
            "FROM event e " +
            "JOIN category c ON e.category_id = c.category_id " +
            "WHERE TO_CHAR(e.event_date, 'YYYY-MM-DD') = ? " +
            "ORDER BY e.created_at DESC";

        List<Event> list = new ArrayList<>();
        
        try (Connection con = dbcon();
             PreparedStatement ps = con.prepareStatement(sql)){

        	ps.setString(1, ymd);
            
        	try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                	
                    Event ev = new Event();
                    
                    ev.setEventId(rs.getInt("event_id"));
                    ev.setAuthorId(rs.getInt("author_id"));
                    ev.setCategoryId(rs.getInt("category_id"));
                    ev.setTitle(rs.getString("title"));
                    ev.setRegion(rs.getString("region"));
                    ev.setEventDate(rs.getDate("event_date"));
                    ev.setCapacity(rs.getInt("capacity"));
                    ev.setDescription(rs.getString("description"));
                    ev.setStatus(rs.getString("status"));
                    ev.setCreatedAt(rs.getDate("created_at"));
                    ev.setUploadImg(rs.getString("upload_img"));
                    ev.setCategoryName(rs.getString("category_name"));
                  
                    list.add(ev);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
}