package com.neviarch.tests.compiler;

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
		Assert.assertArrayEquals(array(0x00, 0x04), compile("READ aX 0x4"));
		Assert.assertArrayEquals(array(0x25, 0x0E), compile("LoAd BX 0x10E"));
		Assert.assertArrayEquals(array(0x00, 0x04, 0x25, 0x0E), compile("READ aX 0x4 LoAd BX 0x10E"));
		
		Assert.assertArrayEquals(array(0x40, 0x80), compile("add ax bx"));
		Assert.assertArrayEquals(array(0x69, 0x80), compile("SUB CX dX"));
		Assert.assertArrayEquals(array(0x40, 0x80, 0x69, 0x80), compile("add ax bx SUB CX dX"));
		
		Assert.assertArrayEquals(array(0x00, 0x04, 0x25, 0x0E, 0x40, 0x80, 0x69, 0x80),
				compile("READ aX 0x4 LoAd BX 0x10E add ax bx SUB CX dX"));
		
		Assert.assertArrayEquals(array(0x92, 0x80), compile("DIV eX fx"));
		Assert.assertArrayEquals(array(0xBB, 0x80), compile("MULt Gx HX"));
		Assert.assertArrayEquals(array(0x92, 0x80, 0xBB, 0x80), compile("DIV eX fx MULt Gx HX"));
		
		Assert.assertArrayEquals(array(0xC0, 0x0B), compile("JuMP 0xB"));
		Assert.assertArrayEquals(array(0xE8, 0x2F), compile("jumpcond cx 0x2F"));
		Assert.assertArrayEquals(array(0xC0, 0x0B, 0xE8, 0x2F), compile("JuMP 0xB jumpcond cx 0x2F"));
		
		Assert.assertArrayEquals(array(0x92, 0x80, 0xBB, 0x80, 0xC0, 0x0B, 0xE8, 0x2F),
				compile("DIV eX fx MULt Gx HX JuMP 0xB jumpcond cx 0x2F"));
		
		Assert.assertArrayEquals(array(0x00, 0x04, 0x25, 0x0E, 0x40, 0x80, 0x69, 0x80, 0x92, 0x80, 0xBB, 0x80, 0xC0, 0x0B, 0xE8, 0x2F),
				compile("READ aX 0x4 LoAd BX 0x10E add ax bx SUB CX dX DIV eX fx MULt Gx HX JuMP 0xB jumpcond cx 0x2F"));
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
