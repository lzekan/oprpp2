package hr.fer.zemris.vicevi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="ViceviServlet", urlPatterns="/")
public class ViceviServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static class Vic{
		private String vic;
		private int glasovi;
		
		public Vic(String vic) {
			this.vic = vic;
			this.glasovi = 0;
		}
		
		public String getVic() {
			return this.vic;
		}
		
		public int getGlasovi() {
			return this.glasovi;
		}
		
		public void updateGlasovi() {
			this.glasovi++;
		}
		
		public String toString() {
			return this.vic;
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html; charset=utf-8");
		resp.setStatus(HttpServletResponse.SC_OK);
		
		List<Vic> vicevi = new ArrayList<>();
		vicevi.add(new Vic("vic 1"));
		vicevi.add(new Vic("vic 2"));
		vicevi.add(new Vic("vic 3"));

		req.setAttribute("vicevi", vicevi);
		
		req.getRequestDispatcher("/WEB-INF/pages/vicevi.jsp").forward(req, resp);
	}

}
