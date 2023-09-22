package me.Vark123.EpicClans.ClanSystem.LogSystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.Vark123.EpicClans.FileManager;

public class ClanLogger {

	private static final SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private File logFile;

	
	public ClanLogger(String clanId) {
		this(new File(FileManager.getClanDir(), clanId));
	}
	
	public ClanLogger(File clanDir) {
		if(!clanDir.exists())
			clanDir.mkdir();
		
		File logDir = new File(clanDir, "logs");
		if(!logDir.exists())
			logDir.mkdir();
		
		Date date = new Date();
		SimpleDateFormat filenameDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		logFile = new File(logDir, filenameDateFormat.format(date)+".log");
		if(!logFile.exists())
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void logMessage(String msg) {
		try {
			final BufferedWriter out = new BufferedWriter(new FileWriter(logFile, true));
			out.write("["+logDateFormat.format(new Date())+"] "+msg+"\n");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
