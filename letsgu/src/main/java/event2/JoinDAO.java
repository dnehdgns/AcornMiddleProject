package event2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JoinDAO {
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
	
	public boolean isJoined(int userId, int eventId) {
	    String sql = "SELECT COUNT(*) FROM PARTICIPATION WHERE user_id = ? AND event_id = ?";
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

    public boolean addJoin(int event_id, int user_id) {
        String sql = "INSERT INTO PARTICIPATION (event_id, user_id) VALUES (?, ?)";
        try (Connection conn = dbcon();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, event_id);
            stmt.setInt(2, user_id);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deleteJoin(int eventid, int userid) {
        String sql = "DELETE FROM PARTICIPATION WHERE user_id = ? AND event_id = ?";
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

    public int getJoinCount(int eventId) {
        String sql = "SELECT COUNT(*) FROM PARTICIPATION WHERE event_id = ?";
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
}
