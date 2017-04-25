import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import com.neviarch.CentralProcessingUnit;
import com.neviarch.Compiler;

public class Program
{
	public static final String INPUT_NAME = "runtime.nevl";
	public static final String OUTPUT_NAME = "runtime.nev";
	
	public static void main(String[] args)
	{
		for (String arg : args) System.out.println(arg);
		
		if (args.length == 0)
		{
			if (!readCode()) return;
			if (!compile(INPUT_NAME, OUTPUT_NAME)) return;
			
			args = new String[] { "execute", OUTPUT_NAME };
		}
		
		if (args[0].equals("compile"))
		{
			if (args.length == 2)
				compile(args[1]);
			
			else if (args.length == 3)
				compile(args[1], args[2]);
			
			return;
		}
		else if (args[0].equals("execute"))
		{
			execute(args[1]);
			return;
		}
		
		System.out.println("Usage: compile file.nevl [file.nev] OR execute file.nev");
	}
	
	public static boolean readCode()
	{
		String code = null;
		
		try (Scanner in = new Scanner(System.in))
		{
			StringBuilder codeBuilder = new StringBuilder();
			
			while (in.hasNextLine())
			{
				String line = in.nextLine();
				if (line.equals("")) break;
				codeBuilder.append(line).append('\n');
			}
			
			code = codeBuilder.toString();
		}
		
		File input = new File(INPUT_NAME);
		
		try {
			input.createNewFile();
		}
		catch (IOException e) {
			System.out.println("Could not create runtime file " + INPUT_NAME + ".");
			return false;
		}
		
		try (PrintWriter writer = new PrintWriter(input)) {
			writer.write(code.toString());
		}
		catch (FileNotFoundException fnfe) {
			System.out.println("Could not find runtime file " + INPUT_NAME + " just created.");
			return false;
		}
		
		return true;
	}
	
	public static boolean compile(String codeName, String compiledName)
	{
		Compiler compiler = null;
		
		try {
			InputStream inStream = new FileInputStream(codeName);
			OutputStream outStream = new FileOutputStream(compiledName);
			
			compiler = new Compiler(inStream);
			compiler.compile().writeTo(outStream);
			return true;
		}
		catch (FileNotFoundException fnfe) {
			System.out.println("The file " + codeName + " does not exist.");
		}
		catch (IOException fnfe) {
			System.out.println("Could not write output file " + compiledName + ".");
		}
		catch (Exception e) {
			System.out.println("Compilation error at instruction " + compiler.getLinesCompiledCount() + ".");
			System.out.println(e.getMessage());
		}
		
		return false;
	}
	
	public static boolean compile(String codeName) {
		return compile(codeName, codeName.substring(0, codeName.length() - 1));
	}
	
	public static void execute(String fileName)
	{
		byte[] program;
		
		try {
			program = Files.readAllBytes(Paths.get(fileName));
		}
		catch (IOException e) {
			System.out.println(fileName + " does not exist or there is no permission to read it.");
			return;
		}
		
		new CentralProcessingUnit().run(program);
	}
}
