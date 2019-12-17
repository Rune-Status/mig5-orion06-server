package com.rs2.model.content;

import com.rs2.Constants;
import com.rs2.model.World;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;

public class DesertHeat {
	
	static int[] desertRegions = new int[]{
			12589,
			12844,
			12845,
			12846,
			12848,
			12847,
			13100,
			13101,
			13102,
			13356,
			13357,
			13359,
			13360,
			13614,
			13615,
			13616,
			13617,
			13872,
			13873,	
	};
	
	static boolean inDesert(final Player player){
		int region_ = Misc.coords2Region(player.getPosition().getX(), player.getPosition().getY());
		for(int reg : desertRegions){
			if(reg == region_){
				return true;
			}
		}
		return false;
	}

	public static boolean checkAreas(final Player player){
		if(player.tempAreaEffectInt2 == 1234)
			return true;
		if(inDesert(player)){
			final Tick timer1 = new Tick(150) {
	            @Override
	            public void execute() {
	            	if(!player.isLoggedIn()){
	            		stop();
	            		return;
	            	}
	            	if(inDesert(player)){
	            		int item = 0;
						if(player.getInventory().playerHasItem(1829))
							item = 1829;
						else if(player.getInventory().playerHasItem(1827))
							item = 1827;
						else if(player.getInventory().playerHasItem(1825))
							item = 1825;
						else if(player.getInventory().playerHasItem(1823))
							item = 1823;
						if(item != 0){
							player.getActionSender().sendMessage("You take a drink of water.");
							player.getUpdateFlags().sendAnimation(829);
							player.getInventory().removeItem(new Item(item, 1));
							player.getInventory().addItemOrDrop(new Item(item+2, 1));
						} else {
							player.hit(1+Misc.random(9), HitType.NORMAL);
							player.getActionSender().sendMessage("You should get a waterskin for any travelling in the desert.");
							player.getActionSender().sendMessage("You start dying of thirst while you're in the desert.");
						}
						this.setTickDelay(150);
						if(player.getEquipment().getId(Constants.CHEST) == 1833)
							this.setTickDelay(this.getTickDelay()+20);
						if(player.getEquipment().getId(Constants.LEGS) == 1835)
							this.setTickDelay(this.getTickDelay()+20);
						if(player.getEquipment().getId(Constants.FEET) == 1837)
							this.setTickDelay(this.getTickDelay()+10);
	            	}else{
	            		player.tempAreaEffectInt2 = -1;
	            		stop();
	            	}
	            }
			};
	        World.getTickManager().submit(timer1);
	        player.tempAreaEffectInt2 = 1234;
			return true;
		}
		return false;
	}
	
}
