package com.rs2.model.content.questing;
import java.util.Calendar;

import com.rs2.Server;
import com.rs2.model.content.questing.events.Birthday15;
import com.rs2.model.content.questing.events.Easter15;
import com.rs2.model.content.questing.events.Hween14;
import com.rs2.model.content.questing.events.Hween15;
import com.rs2.model.content.questing.events.MissingEvent;
import com.rs2.model.content.questing.events.Xmass14;
import com.rs2.model.content.questing.events.Xmass15;

public class SpecialEventDefinition {
	
	public static int EVENT_COUNT = 0;
	static int NORMAL = 0;
	static int HWEEN = 1;
	static int XMASS = 2;
	static int EASTER = 3;
	
	static SpecialEvent eventlist[] = {
		new MissingEvent(-1),
		//new Hween14(0, HWEEN),
		//new Xmass14(1, XMASS),
		new Easter15(2, EASTER),
		//new Birthday15(-1, NORMAL),
		new Hween15(-1, HWEEN),
		new Xmass15(-1, XMASS),
	};
	
	public static SpecialEvent getEvent(int id){
		for (int i2 = 0; i2 < eventlist.length; i2++) {
			int eventId = eventlist[i2].getId();
			if(id == eventId && eventlist[i2].isInitialised())
				return eventlist[i2];
		}
		return eventlist[0];
	}
	
	public static void initSpecialEvents(){
		EVENT_COUNT = eventlist.length;
		for (int i2 = 0; i2 < eventlist.length; i2++) {
			if(eventlist[i2].getType() == NORMAL){
				eventlist[i2].initialize();
				eventlist[i2].setInitialised(true);
			}
			if(eventlist[i2].getType() == HWEEN && Server.isHween){
				eventlist[i2].initialize();
				eventlist[i2].setInitialised(true);
			}
			if(eventlist[i2].getType() == XMASS && Server.isXmass){
				eventlist[i2].initialize();
				eventlist[i2].setInitialised(true);
			}
			if(eventlist[i2].getType() == EASTER && Server.isEaster){
				eventlist[i2].initialize();
				eventlist[i2].setInitialised(true);
			}
		}
	}
	
}
