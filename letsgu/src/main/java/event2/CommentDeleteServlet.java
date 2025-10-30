package event2;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/letsgu/event/commentdel")
public class CommentDeleteServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("utf-8");
		
		int eventid = Integer.parseInt(req.getParameter("eventId"));
		int commentId = Integer.parseInt(req.getParameter("commentId"));
        
        CommentService commentService = new CommentService();
        int result = commentService.removeComment(commentId);
        
        if(result>0) {
        	resp.sendRedirect(req.getContextPath() + "/letsgu/event/eventdetail?eventId=" + eventid);
        }
	}
}
