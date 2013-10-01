package com.want.utils;

import java.io.*;



public class Logs {
	
	public static void write(String log) throws IOException{

		File file = new File("logs/logs.txt");
		FileWriter w = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(w);
		PrintWriter wr = new PrintWriter(bw);
		wr.append(log);
		wr.close();
		bw.close();
	}
	
	
	
}
