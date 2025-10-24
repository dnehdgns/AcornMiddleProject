package project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
	

	public class UserDAO {
		  
		//오라클 경로
		  String driver = "oracle.jdbc.driver.OracleDriver" ;
		  String url="jdbc:oracle:thin:@localhost:1521:testdb";
		  String user="project";
		  String password="tiger";	 
		  
		  //경로코드
		  public Connection getConnection() {
			  Connection con = null;
			  try {
				Class.forName(driver);
				  con = DriverManager.getConnection(url,user,password);
				  System.out.println("[DB] 연결 성공!");   // 연결 성공 시 출력
				  
				  
				  if(con != null) System.out.println("ok");
			} catch (ClassNotFoundException e) {
				System.out.println("[DB] 드라이버 로딩 실패: " + e.getMessage());
				e.printStackTrace();
				
				
			} catch (SQLException e) {
		        System.out.println("[DB] 연결 실패: " + e.getMessage());  // 실패 시 에러 메시지 출력
				e.printStackTrace();
			}
			  return con;
		  }
	
		
		  //회원가입
		  public int insert(User user) {
			    System.out.println("[DAO] insert 진입");
		  String sql ="INSERT INTO USERS (LOGIN_ID, PASSWORD, USERNAME, EMAIL, GENDER, AGE_GROUP) VALUES (?, ?, ?, ?, ?, ?)";
		  Connection con =null;
		  PreparedStatement pst = null;
		  int result =0;
		  
		  try {
				 con = getConnection();
				 pst = con.prepareStatement(sql);
				  
				  pst.setString(1, user.getId());
				  pst.setString(2, user.getPw());
				  pst.setString(3, user.getName());
				  pst.setString(4, user.getEmail());
				  pst.setString(5, user.getGender());
				  pst.setString(6, user.getAgegroup());
				// ⭐ 쿼리 실행 및 결과 저장 (수정 필요)
				  result = pst.executeUpdate(); 
	              
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
	            try { if (pst != null) pst.close(); } catch (SQLException e) {e.printStackTrace();}
	            try { if (con != null) con.close(); } catch (SQLException e) {e.printStackTrace();}
	        }
			  
			  return result;
			  
		  }
		  
		  
		 //아이디조회
		  public boolean existsByLoginId(String id, String pw) {
			    String sql = "SELECT 1 FROM USERS WHERE login_id=? and password=?";
						    
			    Connection con = null;
		        PreparedStatement pst = null;
		        ResultSet rs = null;
		        boolean result=false;
		       
		        try {
		        	con=getConnection();
			        pst=con.prepareStatement(sql);
			        
			        pst.setString(1, id);
			        pst.setString(2, pw);
			        rs=pst.executeQuery();
			        if(rs.next()) {
			        	return true;
			        }else {
			        	return false;
			        }
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
			      try { if (rs != null) rs.close(); } catch (SQLException e) {e.printStackTrace();}
			      try { if (pst != null) pst.close(); } catch (SQLException e) {e.printStackTrace();}
			      try { if (con != null) con.close(); } catch (SQLException e) {e.printStackTrace();}
			     }
			    return result;
			  }
		  
		 

		  
		  		//회원 상세조회(마이페이지)
		  		public User findLoginId(String Id) {
		  			String sql="SELECT 	USER_ID, USERNAME, EMAIL, GENDER, AGE_GROUP FROM USERS WHERE LOGIN_ID=?";
		  			Connection con=null;
		  			PreparedStatement pst = null;
		  			ResultSet rs =null;
		  			User u = null;
		  			 
		  			try {
		  				con=getConnection();
			  			pst=con.prepareStatement(sql);
			  			pst.setString(1, Id);
			  			rs=pst.executeQuery();
			  			
			  			
			  			if(rs.next()) {
			  				
			  			u =new User();
			  			u.setId(rs.getString("user_id"));
		                u.setName(rs.getString("username"));
		                u.setEmail(rs.getString("email"));
		                u.setGender(rs.getString("gender"));
		                u.setAgegroup(rs.getString("age_group"));
//			  			System.out.print(u);
		                
			  			}
					} catch (Exception e) {
						e.printStackTrace();
					}finally {
				        try { if (rs != null) rs.close(); } catch (Exception e) {e.printStackTrace();}
				        try { if (pst != null) pst.close(); } catch (Exception e) {e.printStackTrace();}
				        try { if (con != null) con.close(); } catch (Exception e) {e.printStackTrace();}
				    }
		  			return u;
		  		
		  
		  		}
	}
