package event2;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CommentDAO {
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
	
	public ArrayList<Comment> GetComment(int eventId){
		Connection con = dbcon();
		String sql = "select * from comments where event_id = ? order by CREATED_AT DESC";
		ArrayList<Comment> list = new ArrayList<>();
		
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, eventId);
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				int comment_id = rs.getInt("COMMENT_ID");
				int event_id = rs.getInt("EVENT_ID");
				String user_id = rs.getString("USER_ID");
				String content = rs.getString("USER_TEXT"); 
				Date create_time = rs.getDate("CREATED_AT");
				
				Comment comment = new Comment(comment_id,event_id,user_id,content,create_time);
				list.add(comment);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	public int AddComment(Comment comment) {
		Connection con = dbcon();
		String sql = "insert into comments(COMMENT_ID, EVENT_ID,USER_ID,USER_TEXT) values(comment_seq.NEXTVAL,?,?,?)";
		PreparedStatement pst = null;
		int result = 0;
		
		try {
			pst = con.prepareStatement(sql);
			pst.setInt(1, comment.getEventId());
			pst.setString(2, comment.getUserId());
			pst.setString(3, comment.getContent());
			
			result = pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public int RemoveComment(int commentId) {
		Connection con = dbcon();
		int result = 0;
		
		String sql = "delete from comments where comment_id = ?";
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, commentId);
			
			result= pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
