package hr.fer.zemris.java.p11.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PrviServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html; charset=utf-8");
		resp.setStatus(HttpServletResponse.SC_OK);
		
		resp.getWriter().write("<html>\n");
		resp.getWriter().write("  <body>\n");
		resp.getWriter().write("    <h1>Naša prva stranica!</h1>\n");
		resp.getWriter().write("    <p>Ovo je naša prva dinamički generirana stranica iz servleta!!!</p>\n");
		resp.getWriter().write("  </body>\n");
		resp.getWriter().write("</html>\n");
		
		
		
		resp.getWriter().flush();
	}
	
}
