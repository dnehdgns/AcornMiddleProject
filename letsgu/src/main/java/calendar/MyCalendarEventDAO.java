package calendar;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;


public class MyCalendarEventDAO {

    // === DB 연결 정보 (환경에 맞게 수정) ===
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String URL    = "jdbc:oracle:thin:@localhost:1521:testdb";
    private static final String USER   = "scott";
    private static final String PASS   = "tiger";

    static {
        try { Class.forName(DRIVER); }
        catch (ClassNotFoundException e) { throw new RuntimeException(e); }
    }

    // 서블릿/코드에서 호출하는 이름과 맞춤
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    private static final String TYPE_BOOKMARK      = "bookmark";
    private static final String TYPE_PARTICIPATION = "participation";

    /**
     * 날짜별 이벤트 개수 조회 (YYYY-MM-DD -> count)
     */
    public Map<String, Integer> findMyCounts(String userId, LocalDate start, LocalDate end, String type) {
        if (userId == null || start == null || end == null || type == null) {
            throw new IllegalArgumentException("userId/start/end/type는 필수입니다.");
        }

        final String sqlBookmark =
            "SELECT TO_CHAR(e.event_date, 'YYYY-MM-DD') AS d, COUNT(*) AS cnt \n" +
            "FROM BOOKMARK b \n" +
            "JOIN EVENT e ON e.event_id = b.event_id \n" +
            "WHERE b.u_id = ? \n" +
            "  AND TRUNC(e.event_date) BETWEEN ? AND ? \n" +
            "GROUP BY TO_CHAR(e.event_date, 'YYYY-MM-DD') \n" +
            "ORDER BY d";

        final String sqlParticipation =
            "SELECT TO_CHAR(e.event_date, 'YYYY-MM-DD') AS d, COUNT(*) AS cnt \n" +
            "FROM PARTICIPATION p \n" +
            "JOIN EVENT e ON e.event_id = p.event_id \n" +
            "WHERE p.u_id = ? \n" +
            "  AND TRUNC(e.event_date) BETWEEN ? AND ? \n" +
            "GROUP BY TO_CHAR(e.event_date, 'YYYY-MM-DD') \n" +
            "ORDER BY d";

        String sql = TYPE_BOOKMARK.equalsIgnoreCase(type) ? sqlBookmark : sqlParticipation;

        Map<String, Integer> map = new LinkedHashMap<>();
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, userId);
            pst.setDate(2, Date.valueOf(start));
            pst.setDate(3, Date.valueOf(end));

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getString("d"), rs.getInt("cnt"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 해당 날짜의 이벤트 리스트 조회
     * 반환: List<Map<String,Object>>
     *  - 키: eventId, title, eventDate(String:YYYY-MM-DD), region, capacity(Long|null), status, description, authorId
     */
    public List<Map<String, Object>> findMyListByDate(String userId, LocalDate date, String type) {
        if (userId == null || date == null || type == null) {
            throw new IllegalArgumentException("userId/date/type는 필수입니다.");
        }

        final String sqlBookmark =
            "SELECT e.event_id, e.title, e.event_date, e.region, e.capacity, e.status, e.description, e.author_id \n" +
            "FROM BOOKMARK b \n" +
            "JOIN EVENT e ON e.event_id = b.event_id \n" +
            "WHERE b.u_id = ? \n" +
            "  AND TRUNC(e.event_date) = ? \n" +
            "ORDER BY e.event_date, e.event_id";

        final String sqlParticipation =
            "SELECT e.event_id, e.title, e.event_date, e.region, e.capacity, e.status, e.description, e.author_id \n" +
            "FROM PARTICIPATION p \n" +
            "JOIN EVENT e ON e.event_id = p.event_id \n" +
            "WHERE p.u_id = ? \n" +
            "  AND TRUNC(e.event_date) = ? \n" +
            "ORDER BY e.event_date, e.event_id";

        String sql = TYPE_BOOKMARK.equalsIgnoreCase(type) ? sqlBookmark : sqlParticipation;

        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, userId);
            pst.setDate(2, Date.valueOf(date));

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("eventId",   rs.getLong("event_id"));
                    row.put("title",     rs.getString("title"));

                    Date d = rs.getDate("event_date");
                    row.put("eventDate", (d != null) ? d.toLocalDate().toString() : "");

                    row.put("region",    rs.getString("region"));

                    long cap = rs.getLong("capacity");
                    row.put("capacity",  rs.wasNull() ? null : cap); // null 허용

                    row.put("status",    rs.getString("status"));
                    row.put("description", rs.getString("description"));
                    row.put("authorId",  rs.getString("author_id"));

                    list.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
