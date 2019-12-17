package com.rs2.model.content.skills.farming;

import java.util.HashMap;

import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class Sacks {
	
	private static final int EMPTY_SACK = 5418;
	private static final int EMPTY_BASKET = 5376;
	private static final int FULL_SACK = 10;
	private static final int FULL_BASKET = 5;
	
	public static boolean fillSack(Player player, int selectedItemId, int usedItemId) {
		int itemId = -1;
		int sackId = 1;
		if (sackData.forId(selectedItemId) != null){
			itemId = selectedItemId;
			sackId = usedItemId;
		}else if(sackData.forId(usedItemId) != null){
			itemId = usedItemId;
			sackId = selectedItemId;
		}
		if(itemId == -1 || new Item(sackId, 1).getDefinition().isNoted())
			return false;
		final sackData sack = sackData.forId(itemId);
		if (sack != null) {
			if(!(sackId == (sack.isSack() ? EMPTY_SACK : EMPTY_BASKET) || (sackId >= sack.getSackId() && sackId <= (sack.getSackId()+(sack.isSack() ? 16 : 6)))))
				return false;
			int sackSpace = (sack.isSack() ? FULL_SACK : FULL_BASKET);
			if(sackId != (sack.isSack() ? EMPTY_SACK : EMPTY_BASKET))
				sackSpace = (sack.isSack() ? 9 : 4)-((sackId-sack.getSackId())/2);
			int amount = player.getInventory().getItemAmount(itemId);
			int removedAmount = -1;
			int newSack = -1;
			if(sackSpace >= amount){
				if(sackId != (sack.isSack() ? EMPTY_SACK : EMPTY_BASKET))
					newSack = sackId+(2*amount);
				else
					newSack = sack.getSackId()+(2*(amount-1));
				removedAmount = amount;
			} else {
				newSack = sack.getSackId()+(sack.isSack() ? 18 : 8);
				removedAmount = sackSpace;
			}
			player.getInventory().removeItem(new Item(itemId, removedAmount));
			player.getInventory().removeItem(new Item(sackId, 1));
			player.getInventory().addItem(new Item(newSack, 1));
			return true;
		}
		return false;
	}

	public static sackData getSack(int id) {
        for(sackData sack : sackData.values()){
        	int min = sack.getSackId();
        	int max = sack.getSackId()+(sack.isSack ? 18 : 8);
        	if(id >= min && id <= max)
        		return sack;
        }
        return null;
	}
	
	public static boolean takeOneFromSack(Player player, int itemId){
		if(itemId == -1 || new Item(itemId, 1).getDefinition().isNoted())
			return false;
		final sackData sack = getSack(itemId);
		if(sack != null){
			Item item2Add = new Item(sack.getStoredItem(), 1);
			boolean addEmpty = false;
			if(itemId == sack.getSackId())
				addEmpty = true;
			if (player.getInventory().canAddItem(item2Add)){
				player.getInventory().addItem(item2Add);
				player.getInventory().removeItem(new Item(itemId, 1));
				if(addEmpty){
					player.getInventory().addItem(new Item((sack.isSack() ? EMPTY_SACK : EMPTY_BASKET), 1));
				}else{
					player.getInventory().addItem(new Item(itemId-2, 1));
				}
				return true;
			}
		}
		return false;
	}
	
	public static enum sackData {
		POTATOS(5420, 1942, true), ONIONS(5440, 1957, true), CABBAGES(5460, 1965, true), APPLES(5378, 1955, false), ORANGES(5388, 2108, false), STRAWBERRIES(5398, 5504, false), BANANAS(5408, 1963, false), TOMATOS(5960, 1982, false);

		private int sackId;
		private int storedItem;
		
		private boolean isSack;

		public static HashMap<Integer, sackData> fillingSack = new HashMap<Integer, sackData>();

		public static sackData forId(int id) {
			return fillingSack.get(id);
		}

		static {
			for (sackData c : sackData.values()) {
				fillingSack.put(c.getStoredItem(), c);
			}
		}

		private sackData(int sackId, int storedItem, boolean isSack) {
			this.sackId = sackId;
			this.storedItem = storedItem;
			this.isSack = isSack;
		}

		public int getSackId() {
			return sackId;
		}

		public int getStoredItem() {
			return storedItem;
		}

		public boolean isSack() {
			return isSack;
		}

	}
	
}
