package atj;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Fahrenheit
 */
@WebServlet("/Fahrenheit")
public class Fahrenheit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Double value1 = Double.parseDouble(request.getParameter("value1"));
		switch(request.getParameter("operation"))
		{
		case "F-C" :
			response.getWriter().println(value1 + "   Fahrenheita to   " + (5*(value1-32))/9 + "    w skali Celsjusza  " );
			break;
		case "C-F" :
			response.getWriter().println(value1 + "   Celsjusza to   " + (32+(1.8*value1)) + "    w skali Fahrenheita  " );
			break;
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
