/***************
Lucas Fahie
Web PDF turn-in 
.tex File Maker

Purpose: This program will create the .tex file that will later be
compiled into a .pdf file and mailed to the user
****************/

// All imports needed
import java.io.*;
import java.util.Scanner;

// Main class
public class TexMaker
{
	/* Variables:
	ID - User-provided ID to identify the User
	algorithm - Algorithm user provided for the assignment
	source - User-provided code for their assignment
	*/
	String id;
	File algorithm;
	File source;
	String texString = "";

	/*
	constructor
	@Param id, algorithm, source
	*/
	public TexMaker(String id, File algorithm, File source)
	{
		this.id = id;
		this.algorithm = algorithm;
		this.source = source;
	}

	/* 
	makeFile takes the user input and inserts them into the
	template that is already inside of the server.
	*/
	public void makePdfFile() throws IOException
	{
		// Inside try block incase anything happens
		try
		{
			// Fetches files for templates and file to be written to
			File header1 = new File("/server/template/header1.tex");
			File header2 = new File("/server/template/header2.tex");
			File header3 = new File("/server/template/header3.tex");
			File endDocu = new File("/server/template/endDocu.tex");
			File texFile = new File("/server/data/" + id.replaceAll("[^a-zA-Z]","") + "/assignment.tex");

			// Create new FileWriter for program to write to
			FileWriter texFileWriter = new FileWriter(texFile, false);

			// If the file has already been created, replace it with ""
			if (!(texFile.createNewFile()))
			{
				texFileWriter.write("");
			}

			// Add all information to string
			add(header1, false);
			add(id, true);
			add("}", false);
			add(header2, false);
			add("\n\\begin{verbatim}\n", false);
			add(algorithm, true);
			add("\n\\end{verbatim}\n", false);
			add(header3, false);
			add("\n\\begin{verbatim}\n", false);
			add(source, true);
			add("\n\\end{verbatim}\n", false);
			add(endDocu, false);

			// Write the user-inputted content combined with the template
			// to the file
			System.out.print(texString);
			texFileWriter.write(texString);

			texFileWriter.close();
		}
		// If an IOException happens, display in output
		catch(IOException e)
		{
			File log = new File("/server/log.txt");
			PrintWriter logwriter = new PrintWriter(new FileWriter(log, true));
			logwriter.println("Error with input/output: " + e.getMessage());
			logwriter.close();
		}
		// If any other error occurs, display in output
		catch(Exception e)
		{
			File log = new File("/server/log.txt");
			PrintWriter logwriter = new PrintWriter(new FileWriter(log, true));
			logwriter.println("Something went wrong.");
			logwriter.close();
		}
	}
	
	/*
	toString converts a file into a string to make it viable to add 
	to the template
	@param file
	@return string version of file
	*/
	public String toString(File file) throws IOException
	{
		// Inside try block incase anything goes wrong
		try
		{
			// New file reader and creates a scanner to add lines
			// to the string
			Scanner scanner = new Scanner(file);
			String result = "";
			while(scanner.hasNextLine())
			{
				result += "\n";
				result += scanner.nextLine();
				
			}
			scanner.close();
			// Return result
			return result;
		}

		// If any exception happens, display to server and return 
		// a blank string
		catch(Exception e)
		{
			File log = new File("/server/log.txt");
			PrintWriter logwriter = new PrintWriter(new FileWriter(log, true));
			logwriter.println(e);
			return "";
		}
	}

	/*
	First constructor for add method - Takes a file, converts it into a
	string and calls the add method that takes a string.
	@param file
	*/
	public void add(File file, boolean escape) throws IOException
	{
		this.add(toString(file), escape);
	}

	/*
	Adds a string to the user-provided/template string
	*/
	public void add(String content, boolean escape)
	{
		if (escape)
		{
			content = content.replace("\\end{verbatim}", "\\textbackslash{}end\\{verbatim\\}");
		/*
		content = content.replace("\\", "\\textbackslash");
		content = content.replace("{", "\\{");
		content = content.replace("}", "\\}");
		content = content.replace("$", "\\$");
		content = content.replace("&", "\\&");
		content = content.replace("#", "\\#");
		content = content.replace("^", "\\^{}");
		content = content.replace("_", "\\_");
		content = content.replace("~", "\\~{}");
		content = content.replace("%", "\\%");

		content = content.replace("<", "\\textless{}");
		content = content.replace(">", "\\textgreater{}");
		content = content.replace("|", "\\textbar{}");

		content = content.replace("\"", "\\textquotedbl{}");
		content = content.replace("'", "\\textquotesingle{}");
		content = content.replace("`", "\\textasciigrave{}");
		*/
		}
		texString += content;
	}
}