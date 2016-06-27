package com.kok.utils;

public class PrinterUtil {

	private PrinterUtil(){
		
	}
	/**
	 * 打印信息
	 * 
	 * @param message
	 *            要打印的信息
	 */
	public static void log(String tag,String message) {
		System.out.println("["+tag+"] : "+message);
	}

}
