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
import com.neviarch.ControlUnit;
import com.neviarch.Memory;
import com.neviarch.Register;
import com.neviarch.Registers;

public class Program
{
	public static final String INPUT_NAME = "C:/Mateus/runtime.nevl";
	public static final String OUTPUT_NAME = "C:/Mateus/runtime.nev";
	public static final String ERROR_MESSAGE = "Usage: compile file.nevl [file.nev] OR execute file.nev";
	
	public static void main(String[] args)
	{
		if (args.length == 0)
		{
			if (!readCode())
				System.out.println(ERROR_MESSAGE);
			
			else if (compile(INPUT_NAME, OUTPUT_NAME))
				execute(OUTPUT_NAME);
		}
		else if (args[0].equals("compile"))
		{
			if (args.length == 2)
				compile(args[1]);
			
			else if (args.length == 3)
				compile(args[1], args[2]);
			
			else if (readCode())
				compile(INPUT_NAME, OUTPUT_NAME);
			
			else System.out.println(ERROR_MESSAGE);
		}
		else if (args[0].equals("execute"))
		{
			if (args.length == 2)
				execute(args[1]);
			
			else if (args.length == 3 && args[2].equals("--log"))
			{
				ControlUnit.LOG = true;
				execute(args[1]);
			}
			
			else System.out.println(ERROR_MESSAGE);
		}
		else System.out.println(ERROR_MESSAGE);
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
		
		if (!input.exists()) try {
			input.createNewFile();
		}
		catch (IOException e) {
			System.out.println("Could not create runtime file " + INPUT_NAME + ".");
			e.printStackTrace();
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
	
	public static boolean compile(String codeFileName, String compiledFileName)
	{
		Compiler compiler = null;
		
		try {
			try (InputStream inStream = new FileInputStream(codeFileName))
			{
				compiler = new Compiler(inStream);
				
				System.out.println("Starting compilation of " + codeFileName + ".");
				compiler.compile();
			}
			catch (FileNotFoundException fnfe) {
				System.out.println("The file " + codeFileName + " does not exist.");
				return false;
			}
			
			System.out.println("Compiled " + compiler.getLinesCompiledCount() + " lines of code.");
			
			File outFile = new File(compiledFileName);
			if (outFile.exists() && !outFile.delete()) System.out.println("Error when trying to delete file " + outFile + ".");
			
			if (!outFile.createNewFile())
			{
				System.out.println("Error when trying to create file " + outFile + ".");
				return false;
			}
			
			byte[] program = compiler.getBytes();
			System.out.println("Program size: " + program.length);
			
			try (OutputStream outStream = new FileOutputStream(outFile, false)) {
				outStream.write(program);
			}
			
			if (outFile.exists())
			{
				System.out.println("Writing executable file " + compiledFileName + " done.");
				return true;
			}
			else {
				System.out.println("Unknown error when creating executable file " + compiledFileName + ".");
			}
		}
		catch (IOException ioe) {
			System.out.println("Could not write output file " + compiledFileName + ".");
			System.out.println("Error description: " + ioe.getMessage());
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
		
		System.out.println("Executing " + fileName + "...\n");
		CentralProcessingUnit cpu = new CentralProcessingUnit().run(program);
		
		if (!ControlUnit.LOG)
		{
			Registers registers = cpu.getRegisters();
			Memory memory = cpu.getMemory();
			
			System.out.print("REG [ ");
			for (int i = 0; i < 8; i++)
				System.out.print(registers.get(Register.fromCode(i)) + " ");
			System.out.println("]");
			
			System.out.print("MEM [ ");
			for (int i = 0; i < 8; i++) {
				registers.set(Register.MAR, i);
				memory.fetch();
				System.out.print(registers.get(Register.MBR) + " ");
			}
			System.out.println("]");
			
			System.out.println();
		}
	}
}
