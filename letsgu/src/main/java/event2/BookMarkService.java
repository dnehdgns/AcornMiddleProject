package event2;


public class BookMarkService {
	BookMarkDAO dao = new BookMarkDAO();
	
	public boolean checkBookMarked(int userId, int eventId) {
		boolean result = dao.isBookmarked(userId, eventId);
		
		return result;
	}
	
	public boolean insertBookMark(int event_id, int user_id) {
		boolean result = dao.addBookmark(event_id,user_id);
		
		return result;
	}
	
	public boolean deleteBookMark(int event_id, int user_id) {
		boolean result = dao.removeBookmark(event_id,user_id);
		
		return result;
	}
}
