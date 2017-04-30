package com.neviarch;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class IO
{
	private Scanner in;
	private PrintStream out;
	
	public IO(InputStream in, PrintStream out)
	{
		this.in = new Scanner(in);
		this.out = out;
	}
	
	public int read() {
		return this.in.nextInt();
	}
	
	public void write(int value) {
		this.out.println(value);
	}
}
