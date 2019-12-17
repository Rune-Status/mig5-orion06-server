package com.rs2.model.content;

import com.rs2.Constants;
import com.rs2.model.World;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;
import com.rs2.util.Area;
import com.rs2.util.Misc;

public class SmokeDungeon {
	
	public static boolean checkAreas(final Player player){
		if(player.tempAreaEffectInt == 1235)
			return true;
		if(player.inSmokeDungeon()){
			final Tick timer1 = new Tick(20) {
	            @Override
	            public void execute() {
	            	if(!player.isLoggedIn()){
	            		stop();
	            		return;
	            	}
	            	if(player.inSmokeDungeon()){
	            		if(player.getEquipment().getId(Constants.HAT) != 4164 && player.getCurrentHp() > 1){
	            			player.hit((player.getCurrentHp()-20) < 1 ? (player.getCurrentHp()-1) : 20, HitType.NORMAL);
	            			player.getActionSender().sendMessage("You should wear a facemask to protect yourself from the smoke.");
						}
	            	}else{
	            		player.tempAreaEffectInt = -1;
	            		stop();
	            	}
	            }
			};
	        World.getTickManager().submit(timer1);
	        player.tempAreaEffectInt = 1235;
			return true;
		}
		return false;
	}
	
}
