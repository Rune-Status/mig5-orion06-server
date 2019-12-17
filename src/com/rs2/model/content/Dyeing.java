package com.rs2.model.content;

import java.util.HashMap;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 21/12/11 Time: 13:53 To change
 * this template use File | Settings | File Templates.
 */
public class Dyeing {
	public static int RED_DYE = 1763;
	public static int YELLOW_DYE = 1765;
	public static int BLUE_DYE = 1767;
	public static int ORANGE_DYE = 1769;
	public static int GREEN_DYE = 1771;
	public static int PURPLE_DYE = 1773;
	
	public static boolean mixDyes(final Player player, int item1, int item2) {
		if((item1 == RED_DYE && item2 == YELLOW_DYE) || (item1 == YELLOW_DYE && item2 == RED_DYE)){
			player.getInventory().removeItem(new Item(item1, 1));
    		player.getInventory().removeItem(new Item(item2, 1));
    		player.getInventory().addItem(new Item(ORANGE_DYE, 1));
			player.getActionSender().sendMessage("You mix the two dyes and make an orange one.");
			return true;
		}
		if((item1 == BLUE_DYE && item2 == YELLOW_DYE) || (item1 == YELLOW_DYE && item2 == BLUE_DYE)){
			player.getInventory().removeItem(new Item(item1, 1));
    		player.getInventory().removeItem(new Item(item2, 1));
    		player.getInventory().addItem(new Item(GREEN_DYE, 1));
			player.getActionSender().sendMessage("You mix the two dyes and make a green one.");
			return true;
		}
		if((item1 == BLUE_DYE && item2 == RED_DYE) || (item1 == RED_DYE && item2 == BLUE_DYE)){
			player.getInventory().removeItem(new Item(item1, 1));
    		player.getInventory().removeItem(new Item(item2, 1));
    		player.getInventory().addItem(new Item(PURPLE_DYE, 1));
			player.getActionSender().sendMessage("You mix the two dyes and make a purple one.");
			return true;
		}
		return false;
	}
	
	public static boolean perform(final Player player, int selectedItemId, int usedItemId) {
		if(mixDyes(player, selectedItemId, usedItemId))
			return true;
		return false;
	}

}
