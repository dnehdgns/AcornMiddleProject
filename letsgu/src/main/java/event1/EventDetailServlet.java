package event1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import event2.BookMarkDAO;
import event2.BookMarkService;
import event2.Comment;
import event2.CommentDAO;
import event2.CommentService;
import event2.JoinDAO;
import event2.LikeDAO;
import user.Users;

@WebServlet("/letsgu/event/eventdetail")
public class EventDetailServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");

		// 세션 확인
		Users loginId = getLoginUser(req);
		req.setAttribute("loginUser", loginId);

		int eventId = Integer.parseInt(req.getParameter("eventId"));

		EventService service = new EventService();
		Event event = service.getEventById(eventId);

		String keyword = req.getParameter("keyword");


		req.setAttribute("event", event);
		

		BookMarkService bookService = new BookMarkService();
		JoinDAO daoJoin = new JoinDAO();
		LikeDAO daoLike = new LikeDAO();
		CommentService commentService = new CommentService();
		
		ArrayList<Comment> list = commentService.GetComment(eventId);
		
		req.setAttribute("commentList", list);
		
		if(loginId != null) {
			boolean bookmarked = bookService.checkBookMarked(loginId.getUserId(), eventId);
			boolean joined = daoJoin.isJoined(loginId.getUserId(), eventId);
			boolean liked = daoLike.isLiked(loginId.getUserId(), eventId);
			boolean disliked = daoLike.isdisLiked(loginId.getUserId(), eventId);
			
			int joinCnt = daoJoin.getJoinCount(eventId);
			int likeCnt = daoLike.getLikeCount(eventId);
			int dislikeCnt = daoLike.getDisLikeCount(eventId);
			
			req.setAttribute("joinCount", joinCnt);
			req.setAttribute("likeCount", likeCnt);
			req.setAttribute("dislikeCount", dislikeCnt);
			
			req.setAttribute("bookmarked", bookmarked);
			req.setAttribute("joined", joined);
			req.setAttribute("liked", liked);
			req.setAttribute("disliked", disliked);
		}


		req.setAttribute("keyword", keyword);

		req.getRequestDispatcher("/WEB-INF/views/event/eventdetail.jsp").forward(req, resp);
	}

	// 세션 검사
	private Users getLoginUser(HttpServletRequest req) {
		HttpSession session = req.getSession(false);

		if (session != null) {
			return (Users) session.getAttribute("LOGIN_ID");

		}
		return null;
	}
}
