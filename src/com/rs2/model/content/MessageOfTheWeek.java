package com.rs2.model.content;

import java.io.File;
import java.io.FileOutputStream;

import com.rs2.Server;
import com.rs2.util.FileOperations;
import com.rs2.util.Misc;
import com.rs2.util.Packer;
import com.rs2.util.Stream;
import com.rs2.util.Time;

public class MessageOfTheWeek {
	
	//main interface: 15244
	static int VIRUS_STYLE = 5993;
	static int DRAMA_STYLE = 15767;
	static int GOOD_BAD_PASSWORD_STYLE = 15774;
	static int PASSWORD_SAFETY_STYLE = 15791;
	static int ITEM_SCAM_STYLE = 15801;
	static int SECURITY_STYLE = 15812;
	static int CHRISTMAS_STYLE = 15819;
	static int QUESTION_STYLE = 17511;

	int style;
	String[] message;
	String title;
	
	MessageOfTheWeek(int style, boolean isDidYouKnow, String...message){
		this.style = style;
		this.message = message;
		if(isDidYouKnow)
			this.title = "Did you know?";
		else
			this.title = "Message of the week";
	}
	
	static MessageOfTheWeek messagelist[] = {
		//Default rs messages
		new MessageOfTheWeek(GOOD_BAD_PASSWORD_STYLE, false, "Your password is only as safe as your computer.", "Install anti-virus software!"),
		new MessageOfTheWeek(VIRUS_STYLE, false, "Out of date anti-virus software is useless.", "Update it often and run regular scans!"),
		
		//Custom messages
		new MessageOfTheWeek(ITEM_SCAM_STYLE, true, "There isn't such thing as 'rare black lobster'", "It's a scam."),
		new MessageOfTheWeek(QUESTION_STYLE, true, "There is currently over 20 quests for you to", "complete in-game."),
		new MessageOfTheWeek(QUESTION_STYLE, true, "At level 5 in a skill, you have already reached", "xp rate 1.1 in that skill."),
		new MessageOfTheWeek(QUESTION_STYLE, true, "The server launched 18.4.2014 14:00 GMT +2", "and has been online since that."),
	};
	
	static MessageOfTheWeek hweenMessage = new MessageOfTheWeek(DRAMA_STYLE, false, "Halloween is here! Check the forums for", "info about the H'ween event!");//Custom message
	static MessageOfTheWeek xmasMessage = new MessageOfTheWeek(CHRISTMAS_STYLE, false, "OSRSPK staff wishes you a Merry Christmas", "and a Happy New Year!");//Default rs message
	static MessageOfTheWeek easterMessage = new MessageOfTheWeek(DRAMA_STYLE, false, "Easter is here! Check the forums for", "info about the Easter event!");//Custom message
	
	public static MessageOfTheWeek getNewMessageOfTheWeek(int i){
		if(Server.isHween)
			return hweenMessage;
		if(Server.isXmass)
			return xmasMessage;
		if(Server.isEaster)
			return easterMessage;
		return messagelist[i];
	}
	
	public static void saveMessageOfTheWeek(){
		File f = new File("./data/messageOfTheWeek.dat");
		f.delete();
		try {
			Packer stream2 = new Packer(new FileOutputStream("./data/messageOfTheWeek.dat"));
			stream2.writeLong(System.currentTimeMillis());
			stream2.writeUnsignedByte(Server.messageOfTheWeek);
			stream2.close();
		} catch (Exception e) {}
	}
	
	public static void loadMessageOfTheWeek() {
		long lastTime = 0;
		long currentTime = System.currentTimeMillis();
		if(FileOperations.FileExists("./data/messageOfTheWeek.dat")){
			try {
				byte abyte2[] = FileOperations.ReadFile("./data/messageOfTheWeek.dat");
				Stream stream2 = new Stream(abyte2);
				lastTime = stream2.readQWord();
				Server.messageOfTheWeek = stream2.readUnsignedByte();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        if(Server.isHween || Server.isXmass || Server.isEaster){
        	Server.messageOfTheWeek = 250;
        	saveMessageOfTheWeek();
        }
        else if((Time.daysBetween(lastTime, currentTime) > 0 && Time.isStartOfWeek(currentTime)) || Server.messageOfTheWeek == 250 || lastTime == 0){
        	Server.messageOfTheWeek = Misc.random_(messagelist.length);
        	System.out.println("New message of the week.");
        	saveMessageOfTheWeek();
        }
	}
	
	public int getStyle() {
		return style;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String[] getMessage() {
		return message;
	}
	
}
