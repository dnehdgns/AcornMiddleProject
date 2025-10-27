package event2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LikeDAO {
	String driver = "oracle.jdbc.driver.OracleDriver" ;
	String url="jdbc:oracle:thin:@localhost:1521:testdb";
	String user="scott";
	String password="tiger";
  
	public Connection dbcon(){	 
		Connection con =null;
		try {
			Class.forName(driver);
			con  =DriverManager.getConnection(url, user, password);
			if( con != null) System.out.println("ok");
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return con;
	}
	public boolean hasAlreadyLiked(int eventId, int userId, String type) throws SQLException {
        String sql = "SELECT COUNT(*) FROM LIKE_INFO WHERE EVENT_ID = ? AND USER_ID = ? AND TYPE = ?";
        try (Connection con = dbcon(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, eventId); pst.setInt(2, userId); pst.setString(3, type);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public void insertLike(int eventId, int userId, String type) throws SQLException {
        String sql = "INSERT INTO LIKE_INFO (EVENT_ID, USER_ID, TYPE, CREATED_AT) VALUES (?, ?, ?, SYSDATE)";
        try (Connection con = dbcon(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, eventId); pst.setInt(2, userId); pst.setString(3, type);
            pst.executeUpdate();
        }
    }

    public void deleteLike(int eventId, int userId, String type) throws SQLException {
        String sql = "DELETE FROM LIKE_INFO WHERE EVENT_ID = ? AND USER_ID = ? AND TYPE = ?";
        try (Connection con = dbcon(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, eventId); pst.setInt(2, userId); pst.setString(3, type);
            pst.executeUpdate();
        }
    }

    public Map<String, Integer> getLikeCounts(int eventId) throws SQLException {
        String sql = "SELECT TYPE, COUNT(*) FROM LIKE_INFO WHERE EVENT_ID = ? GROUP BY TYPE";
        Map<String, Integer> counts = new HashMap<>();
        try (Connection con = dbcon(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, eventId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    counts.put(rs.getString("TYPE"), rs.getInt(2));
                }
            }
        }
        counts.putIfAbsent("like", 0);
        counts.putIfAbsent("dislike", 0);
        return counts;
    }

	
	
}
