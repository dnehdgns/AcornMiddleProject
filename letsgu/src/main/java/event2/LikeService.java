package event2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LikeService {
	LikeDAO dao = new LikeDAO();
	
	public boolean checkLiked(int userId, int eventId) {
		boolean result = dao.isLiked(userId, eventId);
		return result;
	}
	public boolean isdisLiked(int userId, int eventId) {
		boolean result = dao.isdisLiked(userId, eventId);
		return result;
	}
    public boolean insertLike(int event_id, int user_id) {
		boolean result = dao.addLike(event_id, user_id);
		return result;
    }
    
    public boolean insertDisLike(int event_id, int user_id) {
    	boolean result = dao.addDisLike(event_id, user_id);
    	return result;
    }

    public boolean removeLike(int eventid, int userid) {
    	boolean result = dao.deleteLike(eventid, userid);
    	return result;
    }
    public boolean removeDisLike(int eventid, int userid) {
    	boolean result = dao.deleteDisLike(eventid, userid);
    	return result;
    }

    public int getLikeCount(int eventId) {
    	int result = dao.getLikeCount(eventId);
    	return result;
    }
    public int getDisLikeCount(int eventId) {
    	int result = dao.getDisLikeCount(eventId);
    	return result;
    }
    public boolean updateLikeType(int userId, int eventId, String newType) {
    	boolean result = dao.updateLikeType(userId, eventId, newType);
    	return result;
    }
}
