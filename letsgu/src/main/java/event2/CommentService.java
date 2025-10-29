package event2;

import java.util.ArrayList;

public class CommentService {
	
	CommentDAO dao = new CommentDAO();

	public int insertComment(Comment comment) {
		int result = dao.AddComment(comment);
		return result;
	}
	
	public int removeComment(int commentId) {
		int result = dao.RemoveComment(commentId);
		return result;
	}
	
	public ArrayList<Comment>GetComment(int eventId){
		ArrayList<Comment> list = new ArrayList<Comment>();
		list = dao.GetComment(eventId);
		
		return list;
	}
}
