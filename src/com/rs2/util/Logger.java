package com.rs2.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	
	public static void logMainDataFolder(String line, String file) {
		String filePath = "./data/"+file+".txt";
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath, true));
			try {
				out.write(line);
				out.newLine();
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void logEvent(String line, String file) {
		String filePath = "./data/logs/"+file+".txt";
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath, true));
			try {
				out.write(line);
				out.newLine();
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void logEvent(String[] line, String file) {
		String filePath = "./data/logs/"+file+".txt";
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filePath, true));
			try {
				for(int i = 0; i < line.length; i++){
					if(line[i] == null)
						break;
					out.write(line[i]);
					out.newLine();
				}
			} finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
