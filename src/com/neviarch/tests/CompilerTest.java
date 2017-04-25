package com.neviarch.tests;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

import com.neviarch.Compiler;

public class CompilerTest
{
	@Test
	public void test() throws Exception
	{
		Assert.assertArrayEquals(array(0, 0, 0, 4), compile("LOAD aX 0x4"));
		Assert.assertArrayEquals(array(0x10, 0x40, 1, 0xE), compile("StoRE BX 0x10E"));
		Assert.assertArrayEquals(array(0x20, 0x40, 1, 0xE), compile("SET BX 0x10E"));
		Assert.assertArrayEquals(array(0, 0, 0, 4, 0x10, 0x40, 1, 0xE, 0x20, 0x40, 1, 0xE),
				compile("LOAD aX 0x4 StoRE BX 0x10E SET BX 0x10E"));
		
		Assert.assertArrayEquals(array(0x30, 1, 0, 0), compile("add ax bx"));
		Assert.assertArrayEquals(array(0x40, 0x83, 0, 0), compile("SUB CX dX"));
		Assert.assertArrayEquals(array(0x51, 0x87, 0, 0), compile("MULt Gx HX"));
		Assert.assertArrayEquals(array(0x61, 5, 0, 0), compile("DIV eX fx"));
		Assert.assertArrayEquals(array(0x30, 1, 0, 0, 0x40, 0x83, 0, 0, 0x51, 0x87, 0, 0, 0x61, 5, 0, 0), 
				compile("add ax bx SUB CX dX MULt Gx HX DIV eX fx"));
		
		Assert.assertArrayEquals(array(
					0, 0, 0, 4, 0x10, 0x40, 1, 0xE, 0x20, 0x40, 1, 0xE,
					0x30, 1, 0, 0, 0x40, 0x83, 0, 0, 0x51, 0x87, 0, 0, 0x61, 5, 0, 0),
				compile("LOAD aX 0x4 StoRE BX 0x10E SET BX 0x10E add ax bx SUB CX dX MULt Gx HX DIV eX fx"));
		
		Assert.assertArrayEquals(array(0x70, 0, 0, 0xB), compile("JuMP 0xB"));
		Assert.assertArrayEquals(array(0x81, 0x82, 0xF, 0x2F), compile("jumpeq gX cx 0xF2F"));
		Assert.assertArrayEquals(array(0x91, 0x82, 0xF, 0x2F), compile("jumpgr gX cx 0xF2F"));
		Assert.assertArrayEquals(array(0xA0, 1, 0, 0xB), compile("JuMPgrEQ ax bx 0xB"));
		Assert.assertArrayEquals(array(0x70, 0, 0, 0xB, 0x81, 0x82, 0xF, 0x2F, 0x91, 0x82, 0xF, 0x2F, 0xA0, 1, 0, 0xB),
				compile("JuMP 0xB jumpeq gX cx 0xF2F jumpgr Gx cx 0xF2F JuMPgrEQ ax bx 0xB"));
		
		Assert.assertArrayEquals(array(
					0, 0, 0, 4, 0x10, 0x40, 1, 0xE, 0x20, 0x40, 1, 0xE,
					0x30, 1, 0, 0, 0x40, 0x83, 0, 0, 0x51, 0x87, 0, 0, 0x61, 5, 0, 0,
					0x70, 0, 0, 0xB, 0x81, 0x82, 0xF, 0x2F, 0x91, 0x82, 0xF, 0x2F, 0xA0, 1, 0, 0xB),
				compile("LOAD aX 0x4 StoRE BX 0x10E SET BX 0x10E add ax bx SUB CX dX MULt Gx HX DIV eX fx "
						+ "JuMP 0xB jumpeq gX cx 0xF2F jumpgr Gx cx 0xF2F JuMPgrEQ ax bx 0xB"));
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
