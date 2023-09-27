/*
Lucas Fahie 
Marton Bone  
Web PDF Turn-in
.tex File Maker

Purpose: This object will take three strings as values: the user's ID,
the name of the file for the algorithm, and the name of the file for 
the code. It will take all of them, pass it through TexMaker, create
the file, and compile it into a .pdf file.
*/

// All imports needed
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.*;

// Main class
public class ReturnToSender
{
	// Initializing global variables
	String id, algoName, codeName;

	/* Constructor for program
	   @param id, algoName, codeName
	*/
	ReturnToSender(String id, String algoName, String codeName)
	{
		this.id = id;
		this.algoName = algoName;
		this.codeName = codeName;
	}

	/* This method creates and compiles the file, getting it ready to send out
	   @return true if code compiles successfully, false if something 
	   Goes wrong
	*/ 
	public boolean createFile()
	{
		// Inside try block if anything goes wrong
		try
		{
			String nameSanitized = id.replaceAll("[^a-zA-Z]","");
			// Turn file names into File objects
			File algorithm = new File("/server/data/" + nameSanitized + "/" + algoName);
			File sourceCode = new File("/server/data/" + nameSanitized + "/" + codeName);

			// Initialize TexMaker and make full .tex file
			TexMaker tMaker = new TexMaker(id,algorithm,sourceCode);
			tMaker.makePdfFile();

			// Create Process object to execute conversion command
			Process process = Runtime.getRuntime().exec("pdflatex --output-directory=/server/data/" + nameSanitized + " \"/server/data/" + nameSanitized + "/assignment.tex\"");
System.out.println("command executed");
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			
			File logFile = new File("/server/log.txt");
    		PrintWriter logWriter = new PrintWriter(new FileWriter(logFile, true));

            // Get the input stream and read the output
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(
									new InputStreamReader(inputStream));

			// Initialize string
            String line = "";
System.out.println("starting logging");
			// Print out output of command
            while ((line = reader.readLine()) != null) 
			{
                logWriter.println(line);
            }
			logWriter.close();
			stdInput.close();
			stdError.close();
			reader.close();
System.out.println("waiting for process");
            // Wait for the process to finish
            process.waitFor();

			// Return true, everything worked
			return true;
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
			return false;
		}
	}
}
