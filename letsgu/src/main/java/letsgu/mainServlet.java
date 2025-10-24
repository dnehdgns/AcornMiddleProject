package project;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/letsgu/main")
public class mainServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	request.getRequestDispatcher("/WEB-INF/views/main.jsp").forward(request, response);
	
	HttpSession session = request.getSession(false);
    if (session == null) {
        response.sendRedirect(request.getContextPath() + "/letsgu/login");
        return;
    }
	
	}
}