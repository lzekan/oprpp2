package hr.fer.zemris.java.p11.servleti;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="kv", urlPatterns={"/racunaj-kvadrate"})
public class KvadratiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static class BrojINjegovKvadrat {
		int broj;
		int kvadrat;
		
		public BrojINjegovKvadrat(int broj, int kvadrat) {
			super();
			this.broj = broj;
			this.kvadrat = kvadrat;
		}
		
		public int getBroj() {
			return broj;
		}
		
		public int getKvadrat() {
			return kvadrat;
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer a = 0, b = 20;
		
		try {
			a = Integer.valueOf(req.getParameter("a"));
		} catch(Exception ignorable) {
		}
		
		try {
			b = Integer.valueOf(req.getParameter("b"));
		} catch(Exception ignorable) {
		}
		
		if(a > b) {
			Integer tmp = a;
			a = b;
			b = tmp;
		}
		
		List<BrojINjegovKvadrat> rezultati = new ArrayList<>();
		for(int i = a; i <= b; i++) {
			rezultati.add(new BrojINjegovKvadrat(i, i*i));
		}
		
		req.setAttribute("rezultati", rezultati);
		
		req
			.getRequestDispatcher("/WEB-INF/pages/kvadrati.jsp").forward(req, resp);
	}
	
	
	
	
	
	
	
	
	
}
