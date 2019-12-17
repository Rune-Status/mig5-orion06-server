package com.rs2.util.requirement;

import java.util.ArrayList;

import com.rs2.model.Entity;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 *
 */
public abstract class InventoryRequirement extends ExecutableRequirement {

	private int itemId, itemAmount;
	private ArrayList<Item> items = new ArrayList<Item>();

	public InventoryRequirement(int itemId, int itemAmount) {
		this.itemId = itemId;
		this.itemAmount = itemAmount;
	}
	
	public void updateReq(ArrayList<Item> array) {
		this.items = array;
	}

	@Override
	public void execute(Entity entity) {
		if (!entity.isPlayer())
			return;
		Player player = (Player) entity;
		if(items.size() == 0)
			player.getInventory().removeItem(new Item(itemId, itemAmount));
		else{
			for(Item item : items){
				player.getInventory().removeItem(item);
			}
		}
	}

	@Override
	boolean meetsRequirement(Entity entity) {
		if (!entity.isPlayer())
			return true;
		Player player = (Player) entity;
		if(items.size() == 0)
			return player.getInventory().getItemAmount(itemId) >= itemAmount;
		else{
			for(Item item : items){
				if(player.getInventory().getItemAmount(item.getId()) < item.getCount())
					return false;
			}
			return true;
		}	
	}
}
