package event2;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/letsgu/event/commentadd")
public class CommentAddServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("utf-8");
		
		
		int eventid = Integer.parseInt(req.getParameter("eventId"));
		String userid = req.getParameter("userId");
		String content = req.getParameter("content");
		
        Comment comment = new Comment();
        comment.setEventId(eventid);
        comment.setUserId(userid);
        comment.setContent(content);
        
        CommentService commentService = new CommentService();
        int result = commentService.insertComment(comment);
        
        if(result>0) {
        	resp.sendRedirect(req.getContextPath() + "/letsgu/event/eventdetail?eventId=" + eventid);
        }
        
	}
}
