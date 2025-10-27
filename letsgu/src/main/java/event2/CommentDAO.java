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
		String sql = "select * from comments where event_id = ?";
		ArrayList<Comment> list = new ArrayList<>();
		
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				int comment_id = rs.getInt("COMMENT_ID");
				int event_id = rs.getInt("EVENT_ID");
				int user_id = rs.getInt("USER_ID");
				String content = rs.getString("CONTENT"); 
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
		String sql = "insert into comments(EVENT_ID,USER_ID,CONTENT) values(?,?,?)";
		PreparedStatement pst = null;
		int result = 0;
		
		try {
			pst = con.prepareStatement(sql);
			pst.setInt(1, comment.getEvent_id());
			pst.setInt(2, comment.getUser_id());
			pst.setString(3, comment.getContent());
			
			result = pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public int DeleteComment(int userId) {
		Connection con = dbcon();
		int result = 0;
		
		String sql = "delete from comment where user_id = ?";
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, userId);
			
			result= pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
