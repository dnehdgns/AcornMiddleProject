package event1;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

	// 학원컴 : String url = "jdbc:oracle:thin:@localhost:1521:testdb";
	// 노트북 : String url = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
	// DB 연결
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:testdb";
	String user = "scott";
	String password = "tiger";

	// db 연결
	public Connection dbcon() {
		Connection con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			if (con != null)
				System.out.println("ok");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}

	// 키워드 검색 (이벤트 제목)
	public List<Event> findEventByTitle(String keyword) {
		Connection con = dbcon();

		String sql = "SELECT e.*, c.category_name  " + "FROM event e  "
				+ "JOIN category c ON e.category_id = c.category_id  " + "WHERE INSTR(e.title, ?) > 0  "
				+ "ORDER BY e.created_at DESC ";

		List<Event> searchList = new ArrayList<Event>();

		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, keyword);
			rs = pst.executeQuery();

			while (rs.next()) {
				Event event = new Event();
				event.setEventId(rs.getInt("event_id"));
				event.setAuthorId(rs.getInt("author_id"));
				event.setCategoryId(rs.getInt("category_id"));
				event.setCategoryName(rs.getString("category_name"));
				event.setTitle(rs.getString("title"));
				event.setRegion(rs.getString("region"));
				event.setEventDate(rs.getDate("event_date"));
				event.setCapacity(rs.getInt("capacity"));
				event.setDescription(rs.getString("description"));
				event.setStatus(rs.getString("status"));
				event.setCreatedAt(rs.getDate("created_at"));
				event.setUploadImg(rs.getString("upload_img"));
				searchList.add(event);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return searchList;
	}

	// 지역 목록 조회
	public List<String> findAllRegion() {

		Connection con = dbcon();

		String sql = "SELECT DISTINCT region " + "FROM event " + "WHERE region IS NOT NULL " + "ORDER BY region ";

		List<String> regionList = new ArrayList<String>();

		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();

			while (rs.next()) {
				String region = rs.getString("region");
				regionList.add(region);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return regionList;
	}

	// 이벤트 조회 (전체 지역 + 전체 카테고리 - 페이징 포함)
	public List<Event> findAllEvents(int currentPage, int pageSize) {

		int grpStart = (currentPage - 1) * pageSize + 1;
		int grpEnd = currentPage * pageSize;

		Connection con = dbcon();

		String sql = "SELECT * " + "FROM ( " + "    SELECT ROWNUM num, inner_table.* " + "    FROM ( "
				+ "        SELECT e.*, c.category_name " + "        FROM category c "
				+ "        JOIN event e ON c.category_id = e.category_id " + "        WHERE e.status = 'ACTIVE' "
				+ "        ORDER BY e.created_at DESC, event_id DESC " + "    ) inner_table " + " ) "
				+ " WHERE num BETWEEN ? AND ? " + " ORDER BY event_id DESC ";

		List<Event> allList = new ArrayList<Event>();

		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = con.prepareStatement(sql);

			pst.setInt(1, grpStart);
			pst.setInt(2, grpEnd);

			rs = pst.executeQuery();

			while (rs.next()) {
				Event event = new Event();
				event.setEventId(rs.getInt("event_id"));
				event.setAuthorId(rs.getInt("author_id"));
				event.setCategoryId(rs.getInt("category_id"));
				event.setTitle(rs.getString("title"));
				event.setRegion(rs.getString("region"));
				event.setEventDate(rs.getDate("event_date"));
				event.setCapacity(rs.getInt("capacity"));
				event.setDescription(rs.getString("description"));
				event.setStatus(rs.getString("status"));
				event.setCreatedAt(rs.getDate("created_at"));
				event.setUploadImg(rs.getString("upload_img"));
				event.setCategoryName(rs.getString("category_name"));

				allList.add(event);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return allList;

	}

	// 이벤트 조회 (전체 지역 + 특정 카테고리)
	public List<Event> findEventsByCategory(int category_id) {
		Connection con = dbcon();

		String sql = "SELECT e.*, c.category_name " + "FROM category c "
				+ "JOIN event e ON c.category_id = e.category_id " + "WHERE e.status = 'ACTIVE' AND e.category_id = ? "
				+ "ORDER BY e.created_at DESC";

		List<Event> categoryList = new ArrayList<Event>();

		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = con.prepareStatement(sql);
			pst.setInt(1, category_id);

			rs = pst.executeQuery();

			while (rs.next()) {
				Event event = new Event();
				event.setEventId(rs.getInt("event_id"));
				event.setAuthorId(rs.getInt("author_id"));
				event.setCategoryId(rs.getInt("category_id"));
				event.setTitle(rs.getString("title"));
				event.setRegion(rs.getString("region"));
				event.setEventDate(rs.getDate("event_date"));
				event.setCapacity(rs.getInt("capacity"));
				event.setDescription(rs.getString("description"));
				event.setStatus(rs.getString("status"));
				event.setCreatedAt(rs.getDate("created_at"));
				event.setUploadImg(rs.getString("upload_img"));
				event.setCategoryName(rs.getString("category_name"));

				categoryList.add(event);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return categoryList;

	}

	// 이벤트 조회 (특정 지역 + 전체 카테고리)
	public List<Event> findEventsByRegion(String region) {
		Connection con = dbcon();

		String sql = "SELECT e.*, c.category_name " + "FROM category c "
				+ "JOIN event e ON c.category_id = e.category_id " + "WHERE e.status = 'ACTIVE' AND e.region = ? "
				+ "ORDER BY e.created_at DESC";

		List<Event> regionList = new ArrayList<Event>();

		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, region);

			rs = pst.executeQuery();

			while (rs.next()) {
				Event event = new Event();
				event.setEventId(rs.getInt("event_id"));
				event.setAuthorId(rs.getInt("author_id"));
				event.setCategoryId(rs.getInt("category_id"));
				event.setTitle(rs.getString("title"));
				event.setRegion(rs.getString("region"));
				event.setEventDate(rs.getDate("event_date"));
				event.setCapacity(rs.getInt("capacity"));
				event.setDescription(rs.getString("description"));
				event.setStatus(rs.getString("status"));
				event.setCreatedAt(rs.getDate("created_at"));
				event.setUploadImg(rs.getString("upload_img"));
				event.setCategoryName(rs.getString("category_name"));

				regionList.add(event);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return regionList;
	}

	// 이벤트 조회 (특정 지역 + 특정 카테고리)
	public List<Event> findEventsByRegionAndCategory(String region, int categoryId) {
		Connection con = dbcon();

		String sql = "SELECT e.*, c.category_name " + "FROM category c "
				+ "JOIN event e ON c.category_id = e.category_id "
				+ "WHERE e.status = 'ACTIVE' AND e.region = ?  AND e.category_id = ? " + "ORDER BY e.created_at DESC";

		List<Event> regCatList = new ArrayList<Event>();

		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, region);
			pst.setInt(2, categoryId);

			rs = pst.executeQuery();

			while (rs.next()) {
				Event event = new Event();
				event.setEventId(rs.getInt("event_id"));
				event.setAuthorId(rs.getInt("author_id"));
				event.setCategoryId(rs.getInt("category_id"));
				event.setTitle(rs.getString("title"));
				event.setRegion(rs.getString("region"));
				event.setEventDate(rs.getDate("event_date"));
				event.setCapacity(rs.getInt("capacity"));
				event.setDescription(rs.getString("description"));
				event.setStatus(rs.getString("status"));
				event.setCreatedAt(rs.getDate("created_at"));
				event.setUploadImg(rs.getString("upload_img"));
				event.setCategoryName(rs.getString("category_name"));

				regCatList.add(event);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return regCatList;
	}

	// 총 레코드 수 조회(페이징 처리)
	public int countAll() {
		Connection con = dbcon();

		String sql = "select count(*) from event";

		PreparedStatement pst = null;
		ResultSet rs = null;
		int resultCnt = 0;

		try {
			pst = con.prepareStatement(sql);

			rs = pst.executeQuery();

			if (rs.next()) {
				resultCnt = rs.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultCnt;
	}

	// 이벤트 등록
	public int insertEvent(Event event) {

		Connection con = dbcon();

		String sql = "INSERT INTO event(event_id, author_id, category_id, title, region, event_date, capacity, description, upload_img) "
				+ "VALUES (event_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";

		PreparedStatement pst = null;

		int result = 0;

		try {
			pst = con.prepareStatement(sql);

			pst.setInt(1, event.getAuthorId());
			pst.setInt(2, event.getCategoryId());
			pst.setString(3, event.getTitle());
			pst.setString(4, event.getRegion());
			pst.setDate(5, event.getEventDate());
			pst.setInt(6, event.getCapacity());
			pst.setString(7, event.getDescription());
			pst.setString(8, event.getUploadImg());

			result = pst.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

	// 이벤트 상세보기
	public Event findEventById(int eventId) {
		Connection con = dbcon();

		String sql = "SELECT e.*, u.username AS author_name, c.category_name " + "FROM event e "
				+ "JOIN users u ON e.author_id = u.user_id " + "JOIN category c ON e.category_id = c.category_id "
				+ "WHERE e.event_id = ?";

		PreparedStatement pst = null;
		ResultSet rs = null;
		Event event = null;

		try {
			pst = con.prepareStatement(sql);
			pst.setInt(1, eventId);
			rs = pst.executeQuery();

			if (rs.next()) {
				event = new Event();
				event.setEventId(rs.getInt("event_id"));
				event.setAuthorId(rs.getInt("author_id"));
				event.setCategoryId(rs.getInt("category_id"));
				event.setTitle(rs.getString("title"));
				event.setRegion(rs.getString("region"));
				event.setEventDate(rs.getDate("event_date"));
				event.setCapacity(rs.getInt("capacity"));
				event.setDescription(rs.getString("description"));
				event.setStatus(rs.getString("status"));
				event.setCreatedAt(rs.getDate("created_at"));
				event.setUploadImg(rs.getString("upload_img"));
				event.setCategoryName(rs.getString("category_name"));
				event.setAuthorName(rs.getString("author_name"));

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return event;
	}

	// 이벤트 수정(본인 글만)
	public int updateEvent(Event event) {
		Connection con = dbcon();

		String sql = "UPDATE event " + "SET category_id = ?, " + "    title = ?, " + "    region = ?, "
				+ "    event_date = ?, " + "    capacity = ?, " + "    description = ?, " + "    upload_img = ? "
				+ "WHERE event_id = ? AND author_id = ?";

		PreparedStatement pst = null;
		int result = 0;

		try {
			pst = con.prepareStatement(sql);

			pst.setInt(1, event.getCategoryId());
			pst.setString(2, event.getTitle());
			pst.setString(3, event.getRegion());
			pst.setDate(4, event.getEventDate());
			pst.setInt(5, event.getCapacity());
			pst.setString(6, event.getDescription());
			pst.setString(7, event.getUploadImg());
			pst.setInt(8, event.getEventId());
			pst.setInt(9, event.getAuthorId());

			result = pst.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// 이벤트 삭제(본인 글만)
	public int deleteEvent(int eventId, int authorId) {
		Connection con = dbcon();

		String sql = "DELETE FROM event WHERE event_id = ? AND author_id = ?";

		PreparedStatement pst = null;
		int result = 0;

		try {
			pst = con.prepareStatement(sql);

			pst.setInt(1, eventId);
			pst.setInt(2, authorId);

			result = pst.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// 비활성화 처리
	// 모집기간 경과 시 update
	public void updateExpiredEvent() {

		Connection con = dbcon();

		String sql = "UPDATE event SET status = 'INACTIVE'"
				+ "WHERE TRUNC(event_date) < TRUNC(SYSDATE) AND status = 'ACTIVE'";

		PreparedStatement pst = null;

		try {
			pst = con.prepareStatement(sql);
			int result = pst.executeUpdate();
			System.out.println("[만료 이벤트 비활성화 처리] : " + result + "건 업데이트");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 비추천수 10회 이상 시 update
	public void updateDislikeEvent() {
		Connection con = dbcon();

		String sql = "UPDATE event " + "SET status = 'INACTIVE' " + "WHERE status = 'ACTIVE' " + "AND event_id IN ( "
				+ "    SELECT event_id " + "    FROM like_info " + "    WHERE type = 'DISLIKE' "
				+ "    GROUP BY event_id " + "    HAVING COUNT(*) >= 10 " + ")";

		PreparedStatement pst = null;

		try {
			pst = con.prepareStatement(sql);
			int result = pst.executeUpdate();
			System.out.println("[비추천 10회 이상 이벤트 비활성화 처리] : " + result + "건 업데이트");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 비활성화된 이벤트 조회(관리자용)
	public List<Event> findInactiveEventList() {
		Connection con = dbcon();

		String sql = "SELECT * " + "FROM event " + "WHERE status = 'INACTIVE' ORDER BY created_at DESC";

		List<Event> inactiveList = new ArrayList<Event>();

		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();

			while (rs.next()) {
				Event event = new Event();
				event.setEventId(rs.getInt("event_id"));
				event.setTitle(rs.getString("title"));
				event.setRegion(rs.getString("region"));
				event.setEventDate(rs.getDate("event_date"));
				event.setDescription(rs.getString("description"));
				event.setStatus(rs.getString("status"));
				event.setCreatedAt(rs.getDate("created_at"));
				event.setUploadImg(rs.getString("upload_img"));

				inactiveList.add(event);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inactiveList;
	}

	// 비활성화 이벤트 전체 삭제(관리자용)
	public int deleteInactive() {
		Connection con = dbcon();

		String sql = "DELETE FROM event WHERE status = 'INACTIVE'";

		PreparedStatement pst = null;
		int result = 0;

		try {
			pst = con.prepareStatement(sql);
			result = pst.executeUpdate();
			System.out.println("[비활성화 이벤트 전체 삭제(관리자)] : " + result + "건 삭제");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	// 인기순 정렬 이벤트 조회
	public List<Event> findEventsByPopularity() {
		Connection con = dbcon();

		String sql = "SELECT * FROM ( "
				+ " SELECT e.event_id, e.title, e.description, e.region, e.upload_img, e.created_at, c.category_name, "
				+ " NVL(SUM(CASE WHEN l.type = 'LIKE' THEN 1 ELSE 0 END), 0) AS like_count, "
				+ " NVL(SUM(CASE WHEN l.type = 'DISLIKE' THEN 1 ELSE 0 END), 0) AS dislike_count, "
				+ " (NVL(SUM(CASE WHEN l.type = 'LIKE' THEN 1 ELSE 0 END), 0) "
				+ "  - NVL(SUM(CASE WHEN l.type = 'DISLIKE' THEN 1 ELSE 0 END), 0)) AS popularity " + " FROM event e "
				+ "  JOIN category c ON e.category_id = c.category_id "
				+ " LEFT JOIN like_info l ON e.event_id = l.event_id " + " WHERE e.status = 'ACTIVE' "
				+ " GROUP BY e.event_id, e.title, e.description, e.region, e.upload_img, e.created_at, c.category_name  "
				+ " HAVING SUM(CASE WHEN l.type IS NOT NULL THEN 1 ELSE 0 END) > 0 "
				+ " ORDER BY popularity DESC, e.created_at DESC " + ") WHERE ROWNUM <= 5";

		List<Event> sortList = new ArrayList<Event>();

		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();

			while (rs.next()) {
				Event event = new Event();
				event.setEventId(rs.getInt("event_id"));
				event.setTitle(rs.getString("title"));
				event.setDescription(rs.getString("description"));
				event.setRegion(rs.getString("region"));
				event.setCreatedAt(rs.getDate("created_at"));
				event.setUploadImg(rs.getString("upload_img"));
				event.setCategoryName(rs.getString("category_name"));
				event.setLikeCount(rs.getInt("like_count"));
				event.setDislikeCount(rs.getInt("dislike_count"));
				event.setPopularity(rs.getInt("popularity"));
				sortList.add(event);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sortList;

	}

}
