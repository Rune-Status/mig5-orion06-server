package com.rs2.model.content.questing.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.questing.SpecialEvent;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;
import com.rs2.util.clip.Region;

public class Birthday15 extends SpecialEvent{
	
	public Birthday15(int id, int type) {
		super(id, type);
	}
	
	public int hatsDropped = 0;
	
	public void initialize(){
		System.out.println("Birthday 15 event started! (1st birthday event) [CUSTOM]");
		final Tick timer1 = new Tick(500) {
            @Override
            public void execute() {
            	hatsDropped++;
            	for(int i = 0; i < 6; i++){
            		int phat2Drop = (QuestConstants.RED_PARTYHAT + (i*2));
            		int x = 2145+Misc.random_(1695);
            		int y = 2560+Misc.random_(1410);
            		boolean surface = Misc.random_(2) == 0;
            		if(!surface){
            			x = 2300+Misc.random_(1390);
                		y = 9085+Misc.random_(1280);
            		}
            		boolean clippedTile = false;
            		if(Region.getClipping(x, y, 0) != 0)
            		clippedTile = true;
					while (clippedTile){
						x = 2145+Misc.random_(1695);
	            		y = 2560+Misc.random_(1410);
	            		surface = Misc.random_(2) == 0;
	            		if(!surface){
	            			x = 2300+Misc.random_(1390);
	                		y = 9085+Misc.random_(1280);
	            		}
						if(Region.getClipping(x, y, 0) != 0)
							clippedTile = true;
						else
							clippedTile = false;
					}
            		Position pos2Drop = new Position(x,y,0);
            		GroundItem drop = new GroundItem(new Item(phat2Drop, 1), pos2Drop, false);
					GroundItemManager.getManager().dropItem(drop);
					//System.out.println("Phat spawned: "+phat2Drop+" "+pos2Drop);
            	}
            	System.out.println("Total phat sets dropped: "+hatsDropped);
            }
		};
        World.getTickManager().submit(timer1);
	}
	
}
