package event2;

public class JoinService {
	JoinDAO dao = new JoinDAO();
	
	public boolean insertJoin(int event_id, int user_id) {
		boolean result = dao.addJoin(event_id, user_id);
		return result;
	}
	
	public boolean removeJoin(int event_id, int user_id) {
		boolean result = dao.deleteJoin(event_id, user_id);
		return result;
	}
	
	public int getJoinCount(int eventId) {
		int result = dao.getJoinCount(eventId);
		return result;
	}
}
