package com.rs2.model.npcs.drop;

import com.rs2.model.players.item.Item;

public class NPCItemDrop {
	
	private int id;
	private int amount;
	private int minVal;
	private int maxVal;
	private int items[];
	private int amounts[];
	
	public NPCItemDrop(int id, int amount, int minVal, int maxVal, int items[], int amounts[]) {
		this.id = id;
		this.amount = amount;
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.items = items;
		this.amounts = amounts;
	}
	
	public int getId() {
		return id;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public int getMinVal() {
		return minVal;
	}
	
	public int getMaxVal() {
		return maxVal;
	}
	
	public int[] getItems() {
		if(items == null)
			return new int[0];
		return items;
	}
	
	public int[] getAmounts() {
		if(amounts == null)
			return new int[0];
		return amounts;
	}
	
}
