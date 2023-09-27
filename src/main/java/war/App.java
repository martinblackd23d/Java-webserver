import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.*;
import jakarta.servlet.annotation.MultipartConfig;

@MultipartConfig(
  fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
  maxFileSize = 1024 * 1024 * 10, // 10 MB
  maxRequestSize = 1024 * 1024 * 100 // 100 MB
)

public class App extends HttpServlet {
  public void init()
  {
      //do nothing
  }
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    File file = new File("/server/index.html");
    BufferedReader br = new BufferedReader(new FileReader(file));
    for (String line; (line = br.readLine()) != null;) {
        out.println(line);
    }
    System.out.println("Hello World");
    br.close();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String name = request.getParameter("name");
    String nameSanitized = name.replaceAll("[^a-zA-Z]","");
    File logFile = new File("/server/log.txt");
    PrintWriter logWriter = new PrintWriter(new FileWriter(logFile, true));
    logWriter.println("Name: " + name);
    deleteFiles(nameSanitized);
    new File("/server/data/" + nameSanitized).mkdirs();
    try {
    for (Part part : request.getParts()) {
      if (part.getName().equals("algorithm")) {
        part.write("/server/data/" + nameSanitized + "/algorithm.txt");
      }
      else if (part.getName().equals("program")) {
        part.write("/server/data/" + nameSanitized + "/program.txt");
      }
      ReturnToSender rts = new ReturnToSender(name, "algorithm.txt", "program.txt");
      rts.createFile();
    }
    }
    catch(Exception e)
    {
      logWriter.println(e);
    }
System.out.println("redirecting to confirmation");
    logWriter.close();
    Cookie cookie = new Cookie("name", nameSanitized);
    cookie.setMaxAge(60*60);
    response.addCookie(cookie);
    response.sendRedirect("confirmation");
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
      catch(Exception e)
      {
      File log = new File("/server/log.txt");
			PrintWriter logwriter = new PrintWriter(new FileWriter(log, true));
			logwriter.println("Error with deletion" + e.getMessage());
			logwriter.close();
  }
}
}