import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.*;

public class Confirmation extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Cookie ck[] = request.getCookies();
	if (ck == null)
	{
		response.sendRedirect("/");
		return;
	}
	response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    File file = new File("/server/thankyou.html");
    BufferedReader br = new BufferedReader(new FileReader(file));
    for (String line; (line = br.readLine()) != null;) {
        out.println(line);
    }
    br.close();
	}
}