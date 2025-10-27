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
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.getRequestDispatcher("/WEB-INF/views/comment.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("utf-8");
		
		
		int eventid = Integer.parseInt(req.getParameter("eventid"));
		int userid = Integer.parseInt(req.getParameter("userid"));
		String content = req.getParameter("content");
		
        Comment comment = new Comment();
        comment.setEvent_id(eventid);
        comment.setUser_id(userid);
        comment.setContent(content);
        
        CommentDAO dao = new CommentDAO();
        int result = dao.AddComment(comment);
        
        ArrayList<Comment> list = dao.GetComment(eventid);
        
        req.setAttribute("list", list);
        
        if (result > 0) {
            req.getRequestDispatcher("/WEB-INF/views/comment/commentTest.jsp").forward(req, resp);
        } else {
        	req.getRequestDispatcher("/WEB-INF/views/comment/commentTest2.jsp").forward(req, resp);
        }
	}
}
