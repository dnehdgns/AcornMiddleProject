package event2;

import java.util.ArrayList;

public class CommentService {
	
	CommentDAO dao = new CommentDAO();

	public boolean AddComment(int userId, int eventId, String content) {
		return false;
	}
	
	public boolean DeleteComment(int commentId, int userId) {
		return false;
	}
	
	public ArrayList<Comment>GetComment(int eventId){
		return null;
	}
}
