package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // 오라클 경로
    String driver = "oracle.jdbc.driver.OracleDriver";
    String url    = "jdbc:oracle:thin:@localhost:1521:testdb";
    String user   = "scott";
    String password = "tiger";

    // DB 연결
    public Connection getConnection() {
        Connection con = null;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            System.out.println("[DB] 연결 성공!");
            if (con != null) System.out.println("ok");
        } catch (ClassNotFoundException e) {
            System.out.println("[DB] 드라이버 로딩 실패: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("[DB] 연결 실패: " + e.getMessage());
            e.printStackTrace();
        }
        return con;
    }

    /* ==============================
       회원가입 (시퀀스 + 컬럼 정합)
       - 2차 코드의 스키마: user_id(PK, SEQ), login_id, password, username, email, gender, age_group
       - 평문 비밀번호 저장(해시 전제 아님) — 이후 해시 도입 시 여기서 교체
       ============================== */
 // UserDAO 내부에 넣을 것
    public int insert(Users u) {
        System.out.println("[DAO] insert 진입");

        // RULE까지 포함(테이블 제약 회피용) — u.getRule()이 null이면 'USER'로 저장
        String sql =
        		  "INSERT INTO USERS (" +
        		  "  user_id, login_id, password, username, email, gender, age_group, rule, upload_img" +
        		  ") VALUES (" +
        		  "  users_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, NVL(?, 'USER'), NVL(?, 'default.png')" +
        		  ")";

        Connection con = null;
        PreparedStatement pst = null;
        int result = 0;

        try {
            con = getConnection();
            pst = con.prepareStatement(sql);

            // 1:1 매핑 (두 번째 버전 DTO 기준)
            pst.setString(1, u.getId());
            pst.setString(2, u.getPw());
            pst.setString(3, u.getName());
            pst.setString(4, u.getEmail());
            pst.setString(5, u.getGender());
            pst.setString(6, u.getAgegroup());
            pst.setString(7, u.getRule());
            pst.setString(8, u.getUploadimg()); // null이면 'default.png' 저장


            result = pst.executeUpdate();
            System.out.println("[DAO] insert 결과 row=" + result);

        } catch (SQLException e) {
            // 에러 원인 즉시 파악 가능하도록 강화 로그
            System.out.println("[DAO][INSERT][ERROR] SQLState=" + e.getSQLState()
                    + ", ErrorCode=" + e.getErrorCode()
                    + ", Message=" + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (pst != null) pst.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return result;
    }


    /* ==============================
       아이디 중복 확인
       ============================== */
    public boolean existsByLoginId(String loginId) {
        String sql = "SELECT 1 FROM USERS WHERE login_id = ?";

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean result = false;

        try {
            con = getConnection();
            pst = con.prepareStatement(sql);
            pst.setString(1, loginId);
            rs = pst.executeQuery();
            result = rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs  != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (pst != null) pst.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return result;
    }

    /* ==============================
       로그인 (user_id + password 매칭)
       - rule도 조회하여 세션 권한 처리에 활용
       ============================== */
    public Users findByLogin(String loginId, String password) {
        String sql = "SELECT * FROM USERS WHERE LOGIN_ID = ? AND PASSWORD = ?";

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Users u = null;

        try {
            con = getConnection();
            pst = con.prepareStatement(sql);

            pst.setString(1, loginId);
            pst.setString(2, password);

            rs = pst.executeQuery();

            if (rs.next()) {
                u = new Users();
                u.setUserId(rs.getInt("USER_ID"));       // PK
                u.setId(rs.getString("LOGIN_ID"));       // 아이디
                u.setPw(rs.getString("PASSWORD"));       // 비밀번호
                u.setName(rs.getString("USERNAME"));
                u.setEmail(rs.getString("EMAIL"));
                u.setGender(rs.getString("GENDER"));
                u.setAgegroup(rs.getString("AGE_GROUP"));
                u.setRule(rs.getString("RULE"));
                u.setUploadimg(rs.getString("UPLOAD_IMG"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (pst != null) pst.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return u;
    }

    /* ==============================
       회원 상세조회 (마이페이지)
       - 1차 코드의 확장 필드(UPLOAD_IMG, RULE)까지 포함
       ============================== */
    public Users findByUserId(int userId) {
        String sql = "SELECT USER_ID, LOGIN_ID, USERNAME, EMAIL, GENDER, AGE_GROUP, RULE, UPLOAD_IMG "
                   + "FROM USERS WHERE USER_ID = ?";

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Users u = null;

        try {
            con = getConnection();
            pst = con.prepareStatement(sql);
            pst.setInt(1, userId);
            rs = pst.executeQuery();

            if (rs.next()) {
                u = new Users();
                u.setUserId(rs.getInt("USER_ID"));
                u.setId(rs.getString("LOGIN_ID"));
                u.setName(rs.getString("USERNAME"));
                u.setEmail(rs.getString("EMAIL"));
                u.setGender(rs.getString("GENDER"));
                u.setAgegroup(rs.getString("AGE_GROUP"));
                u.setRule(rs.getString("RULE"));
                u.setUploadimg(rs.getString("UPLOAD_IMG"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (pst != null) pst.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return u;
    }
    /* ==============================
       프로필 업데이트 (NVL 패턴 유지)
       - null/빈문자면 기존 값 유지
       - WHERE는 login_id 기준
       - 1차 코드 기능 보존: upload_img 포함
       ============================== */
    public int updateProfile(Users u) {
        String sql = "UPDATE USERS SET "
                   + "USERNAME   = NVL(?, USERNAME), "
                   + "EMAIL      = NVL(?, EMAIL), "
                   + "UPLOAD_IMG = NVL(?, UPLOAD_IMG), "
                   + "AGE_GROUP  = NVL(?, AGE_GROUP), "
                   + "GENDER     = NVL(?, GENDER) "
                   + "WHERE USER_ID = ?";  // ✅ 변경됨!

        Connection con = null;
        PreparedStatement pst = null;
        int updated = 0;

        try {
            con = getConnection();
            pst = con.prepareStatement(sql);

            pst.setString(1, u.getName());
            pst.setString(2, u.getEmail());
            pst.setString(3, u.getUploadimg());
            pst.setString(4, u.getAgegroup());
            pst.setString(5, u.getGender());
            pst.setInt(6, u.getUserId()); // ✅ PK 기준

            updated = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (pst != null) pst.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (Exception e) { e.printStackTrace(); }
        }
        return updated;
    }


    // 공백/NULL 안전한 바인딩
    private static void setNullableString(PreparedStatement pst, int idx, String v) throws SQLException {
        if (v == null || v.trim().isEmpty()) pst.setNull(idx, java.sql.Types.VARCHAR);
        else pst.setString(idx, v.trim());
    }
}
