package calendar;

import java.sql.*;
import java.util.*;

public class MyCalendarEventDAO {
    private static final String DRIVER="oracle.jdbc.driver.OracleDriver";
    private static final String URL   ="jdbc:oracle:thin:@localhost:1521:testdb";
    private static final String USER  ="scott";
    private static final String PASS  ="tiger";
    static { try { Class.forName(DRIVER);} catch (ClassNotFoundException e){ throw new RuntimeException(e);} }
    private Connection con() throws SQLException { return DriverManager.getConnection(URL, USER, PASS); }

    // participation | bookmark
    public Map<String,Integer> countsByRangeForUser(int userId, String start, String end, String source){
        String joinTable = source.equalsIgnoreCase("bookmark") ? "bookmark" : "participation";
        String userCol   = source.equalsIgnoreCase("bookmark") ? "u_id" : "u_id";
        String sql =
            "SELECT TO_CHAR(e.event_date,'YYYY-MM-DD') d, COUNT(*) cnt " +
            "FROM event e JOIN "+joinTable+" j ON e.event_id = j.event_id " +
            "WHERE j."+userCol+" = ? " +
            "AND e.event_date >= TO_DATE(?, 'YYYY-MM-DD') " +
            "AND e.event_date <  TO_DATE(?, 'YYYY-MM-DD') " +
            "GROUP BY TO_CHAR(e.event_date,'YYYY-MM-DD')";
    
        Map<String,Integer> map = new LinkedHashMap<>();
       
        try(Connection c = con(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, userId);
            ps.setString(2, start);
            ps.setString(3, end);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    map.put(rs.getString("d"), rs.getInt("cnt"));
                }
            }
        }catch(SQLException e){ e.printStackTrace(); }
        return map;
    }

    public List<Event> findUserEventsByDate(int userId, String ymd, String source){
        String joinTable = source.equalsIgnoreCase("bookmark") ? "bookmark" : "participation";
        String userCol   = source.equalsIgnoreCase("bookmark") ? "u_id" : "u_id";

        String sql =
            "SELECT e.* FROM event e " +
            "JOIN "+joinTable+" j ON e.event_id = j.event_id " +
            "WHERE j."+userCol+" = ? " +
            "AND TO_CHAR(e.event_date,'YYYY-MM-DD') = ? " +
            "ORDER BY e.created_at DESC";
        List<Event> list = new ArrayList<>();
        try(Connection c = con(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, userId);
            ps.setString(2, ymd);
         
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
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
                    list.add(ev);
                }
            }
        }catch(SQLException e){ e.printStackTrace(); }
        return list;
    }
}