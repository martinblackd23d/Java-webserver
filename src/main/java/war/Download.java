import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.*;
import jakarta.servlet.annotation.MultipartConfig;

public class Download extends HttpServlet {
	private final int SIZE = 1024;

  public void init()
  {
      //do nothing
  }
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/pdf");
	response.setHeader("Content-Disposition", "attachment; filename=assignment.pdf");
	String nameSanitized = null;
	Cookie ck[] = request.getCookies();
	if (ck == null)
	{
		response.sendRedirect("");
		return;
	}
	for(int i = 0; i < ck.length; i++)
	{
		if(ck[i].getName().equals("name"))
		{
			nameSanitized = ck[i].getValue();
		}
	}
	Cookie cookie = new Cookie("name", "");
	cookie.setMaxAge(0);
	response.addCookie(cookie);
	if (nameSanitized == null)
	{
		return;
	}

    try {
		File file = new File("/server/data/" + nameSanitized + "/assignment.pdf");
		InputStream in = new FileInputStream(file);
		OutputStream out = response.getOutputStream();
		byte[] buffer = new byte[SIZE];

		int numBytesRead;
		while ((numBytesRead = in.read(buffer)) > 0) {
			out.write(buffer, 0, numBytesRead);
		}
		in.close();
		out.close();
		deleteFiles(nameSanitized);
		}
	catch(Exception e)
		{
			// Display what went wrong and return false
			File file = new File("/server/log.txt");
			try
			{
				PrintWriter logWriter = new PrintWriter(new FileWriter(file, true));
				logWriter.println(e);
				logWriter.close();
			}
			catch(Exception f)
			{
				System.out.print(f);
			}
			System.out.print(e);
  }
  }

  public void destroy()
  {
      //do nothing
  }

  public void deleteFiles(String nameSanitized) throws IOException {
	try {
	File file = new File("/server/data/" + nameSanitized + "/assignment.pdf");
	file.delete();
	file = new File("/server/data/" + nameSanitized + "/assignment.tex");
	file.delete();
	file = new File("/server/data/" + nameSanitized + "/algorithm.txt");
	file.delete();
	file = new File("/server/data/" + nameSanitized + "/program.txt");
	file.delete();
	file = new File("/server/data/" + nameSanitized + "/assignment.aux");
	file.delete();
	file = new File("/server/data/" + nameSanitized + "/assignment.log");
	file.delete();
	file = new File("/server/data/" + nameSanitized);
	file.delete();
	}
	catch (Exception e) {
			File log = new File("/server/log.txt");
			PrintWriter logwriter = new PrintWriter(new FileWriter(log, true));
			logwriter.println("Error with deletion: " + e.getMessage());
			logwriter.close();
	}
  }
}