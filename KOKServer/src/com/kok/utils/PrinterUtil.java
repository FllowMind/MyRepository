package com.kok.utils;

public class PrinterUtil {

	private PrinterUtil(){
		
	}
	/**
	 * ��ӡ��Ϣ
	 * 
	 * @param message
	 *            Ҫ��ӡ����Ϣ
	 */
	public static void log(String tag,String message) {
		System.out.println("["+tag+"] : "+message);
	}

}
