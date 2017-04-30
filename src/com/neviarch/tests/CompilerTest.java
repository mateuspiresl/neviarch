package com.neviarch.tests;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

import com.neviarch.compiler.Compiler;

public class CompilerTest
{
	@Test
	public void test() throws Exception
	{
		Assert.assertArrayEquals(array(0, 0, 0, 4), compile("LOAD aX 0x4"));
		Assert.assertArrayEquals(array(0x10, 0x40, 1, 0xE), compile("StoRE BX 0x10E"));
		Assert.assertArrayEquals(array(0x20, 0x40, 1, 0xE), compile("SET BX 0x10E"));
		Assert.assertArrayEquals(array(0, 0, 0, 4, 0x10, 0x40, 1, 0xE, 0x20, 0x40, 1, 0xE),
				compile("LOAD aX 0x4\n StoRE BX 0x10E\n SET BX 0x10E"));
		
		Assert.assertArrayEquals(array(0x30, 1, 0, 0), compile("add ax bx"));
		Assert.assertArrayEquals(array(0x40, 0x83, 0, 0), compile("SUB CX dX"));
		Assert.assertArrayEquals(array(0x51, 0x87, 0, 0), compile("MULt Gx HX"));
		Assert.assertArrayEquals(array(0x61, 5, 0, 0), compile("DIV eX fx"));
		Assert.assertArrayEquals(array(0x30, 1, 0, 0, 0x40, 0x83, 0, 0, 0x51, 0x87, 0, 0, 0x61, 5, 0, 0), 
				compile("add ax bx\n SUB CX dX\n MULt Gx HX\n DIV eX fx"));
		
		Assert.assertArrayEquals(array(
					0, 0, 0, 4, 0x10, 0x40, 1, 0xE, 0x20, 0x40, 1, 0xE,
					0x30, 1, 0, 0, 0x40, 0x83, 0, 0, 0x51, 0x87, 0, 0, 0x61, 5, 0, 0),
				compile("LOAD aX 0x4\n StoRE BX 0x10E\n SET BX 0x10E\n add ax bx\n SUB CX dX\n MULt Gx HX\n DIV eX fx"));
		
		Assert.assertArrayEquals(array(0x70, 0, 0, 0xB), compile("JuMP 0xB"));
		Assert.assertArrayEquals(array(0x81, 0x82, 0xF, 0x2F), compile("jumpeq gX cx 0xF2F"));
		Assert.assertArrayEquals(array(0x91, 0x82, 0xF, 0x2F), compile("jumpgr gX cx 0xF2F"));
		Assert.assertArrayEquals(array(0xA0, 1, 0, 0xB), compile("JuMPgrEQ ax bx 0xB"));
		Assert.assertArrayEquals(array(0x70, 0, 0, 0xB, 0x81, 0x82, 0xF, 0x2F, 0x91, 0x82, 0xF, 0x2F, 0xA0, 1, 0, 0xB),
				compile("JuMP 0xB\n jumpeq gX cx 0xF2F\n jumpgr Gx cx 0xF2F\n JuMPgrEQ ax bx 0xB"));
		
		Assert.assertArrayEquals(array(0xB0, 0, 0, 0), compile("IN ax"));
		Assert.assertArrayEquals(array(0xC0, 0x40, 0, 0), compile("out BX"));
		Assert.assertArrayEquals(array(0xD0, 1, 0, 0), compile("loadreg ax bx"));
		Assert.assertArrayEquals(array(0xE0, 0x42, 0, 0), compile("storereg BX cx"));
		Assert.assertArrayEquals(array(0xF0, 0x83, 0, 0), compile("setreg cX Dx"));
		Assert.assertArrayEquals(array(0xB0, 0, 0, 0, 0xC0, 0x40, 0, 0, 0xD0, 1, 0, 0, 0xE0, 0x42, 0, 0, 0xF0, 0x83, 0, 0),
				compile("IN ax\n out BX\n loadreg ax bx\n storereg BX cx\n setreg cX Dx"));
		
		Assert.assertArrayEquals(array(
				0x70, 0, 0, 0xB, 0x81, 0x82, 0xF, 0x2F, 0x91, 0x82, 0xF, 0x2F, 0xA0, 1, 0, 0xB,
				0xB0, 0, 0, 0, 0xC0, 0x40, 0, 0, 0xD0, 1, 0, 0, 0xE0, 0x42, 0, 0, 0xF0, 0x83, 0, 0),
			compile("JuMP 0xB\n jumpeq gX cx 0xF2F\n jumpgr Gx cx 0xF2F\n JuMPgrEQ ax bx 0xB\n "
					+ "IN ax\n out BX\n loadreg ax bx\n storereg BX cx\n setreg cX Dx"));
		
		Assert.assertArrayEquals(array(
					0, 0, 0, 4, 0x10, 0x40, 1, 0xE, 0x20, 0x40, 1, 0xE,
					0x30, 1, 0, 0, 0x40, 0x83, 0, 0, 0x51, 0x87, 0, 0, 0x61, 5, 0, 0,
					0x70, 0, 0, 0xB, 0x81, 0x82, 0xF, 0x2F, 0x91, 0x82, 0xF, 0x2F, 0xA0, 1, 0, 0xB,
					0xB0, 0, 0, 0, 0xC0, 0x40, 0, 0, 0xD0, 1, 0, 0, 0xE0, 0x42, 0, 0, 0xF0, 0x83, 0, 0),
				compile("LOAD aX 0x4\n StoRE BX 0x10E\n SET BX 0x10E\n add ax bx\n SUB CX dX\n MULt Gx HX\n DIV eX fx\n "
						+ "JuMP 0xB\n jumpeq gX cx 0xF2F\n jumpgr Gx cx 0xF2F\n JuMPgrEQ ax bx 0xB\n "
						+ "IN ax\n out BX\n loadreg ax bx\n storereg BX cx\n setreg cX Dx"));
	}
	
	@Test
	public void dereferece()
	{
		Assert.assertArrayEquals(array(0xD0, 0x42, 0, 0), compile("LOAD bx (cx)"));
		Assert.assertArrayEquals(array(0xE0, 0xC4, 0, 0), compile("STORE dx (ex)"));
		Assert.assertArrayEquals(array(0xF1, 0x46, 0, 0), compile("SET fx (gx)"));
	}
	
	@Test
	public void label()
	{
		String code;
		byte[] compiled;
		
		code = "label:\n load ax 0x0\n jump label\n set ax 0x0";
		compiled = array(0, 0, 0, 0, 0x70, 0, 0, 0, 0x20, 0, 0, 0);
		Assert.assertArrayEquals(compiled, compile(code));
		
		code = "load ax 0x0\n label:\n\n\n jumpeq ax bx label\n set ax 0x0";
		compiled = array(0, 0, 0, 0, 0x80, 1, 0, 1, 0x20, 0, 0, 0);
		Assert.assertArrayEquals(compiled, compile(code));
	}
	
	@Test
	public void comment()
	{
		String code;
		byte[] compiled;
		
		code = "#   comment\n\n label: #c comment\n load ax 0x0\n # comment\n\n jump label #comment comment\n set ax 0x0\n #comment";
		compiled = array(0, 0, 0, 0, 0x70, 0, 0, 0, 0x20, 0, 0, 0);
		Assert.assertArrayEquals(compiled, compile(code));
	}
	
	private static byte[] array(int... data)
	{
		byte[] bytes = new byte[data.length];
		int index = 0;
		
		for (int value : data)
			bytes[index++] = (byte) value;
		
		return bytes;
	}
	
	private static byte[] compile(String code) {
		return new Compiler(toStream(code)).compile().getBytes();
	}
	
	private static InputStream toStream(String code) {
		return new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
	}
}
