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
	public boolean isLiked(int userId, int eventId) {
	    String sql = "SELECT COUNT(*) FROM LIKE_INFO WHERE user_id = ? AND event_id = ? AND like_type = 'LIKE'";
	    try (Connection conn = dbcon();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, userId);
	        stmt.setInt(2, eventId);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	public boolean isdisLiked(int userId, int eventId) {
	    String sql = "SELECT COUNT(*) FROM LIKE_INFO WHERE user_id = ? AND event_id = ? AND like_type = 'DISLIKE'";
	    try (Connection conn = dbcon();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, userId);
	        stmt.setInt(2, eventId);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
    public boolean addLike(int event_id, int user_id) {
        String sql = "INSERT INTO LIKE_INFO (event_id, user_id, like_type) VALUES (?, ?, 'LIKE')";
        try (Connection conn = dbcon();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, event_id);
            stmt.setInt(2, user_id);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;
        }
    }
    
    public boolean addDisLike(int event_id, int user_id) {
        String sql = "INSERT INTO LIKE_INFO (event_id, user_id, like_type) VALUES (?, ?, 'DISLIKE')";
        try (Connection conn = dbcon();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, event_id);
            stmt.setInt(2, user_id);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deleteLike(int eventid, int userid) {
        String sql = "DELETE FROM LIKE_INFO WHERE user_id = ? AND event_id = ? AND like_type = 'LIKE'";
        try (Connection conn = dbcon();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userid);
            stmt.setInt(2, eventid);
            conn.setAutoCommit(true);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;
        }
    }
    public boolean deleteDisLike(int eventid, int userid) {
        String sql = "DELETE FROM LIKE_INFO WHERE user_id = ? AND event_id = ? AND like_type = 'DISLIKE'";
        try (Connection conn = dbcon();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userid);
            stmt.setInt(2, eventid);
            conn.setAutoCommit(true);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    public int getLikeCount(int eventId) {
        String sql = "SELECT COUNT(*) FROM LIKE_INFO WHERE event_id = ? AND like_type = 'LIKE'";
        try (Connection conn = dbcon();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int getDisLikeCount(int eventId) {
        String sql = "SELECT COUNT(*) FROM LIKE_INFO WHERE event_id = ? AND like_type = 'DISLIKE'";
        try (Connection conn = dbcon();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public boolean updateLikeType(int userId, int eventId, String newType) {
        String sql = "UPDATE LIKE_INFO SET like_type = ? WHERE user_id = ? AND event_id = ?";
        try (Connection conn = dbcon();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newType); // 'LIKE' 또는 'DISLIKE'
            pstmt.setInt(2, userId);
            pstmt.setInt(3, eventId);

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
