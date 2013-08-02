package com.want.utils;

import java.io.*;



public class Logs {
	private static String text = "";
	public static void write(String log) throws IOException{
		text = text + "\n" + log;
		File file = new File("logs/logs.txt");
		FileWriter w = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(w);
		PrintWriter wr = new PrintWriter(bw);
		wr.append(text);
		wr.close();
		bw.close();
	}
	
	
	
}
