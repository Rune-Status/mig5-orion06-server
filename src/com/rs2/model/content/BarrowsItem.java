package com.rs2.model.content;

import com.rs2.Constants;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class BarrowsItem {
	
	
	private static int[] repairers = {519,//bob @lumbridge
							};
	
	public final static int TIMER = 4500;//was 5625
	
	final static int HELM_REPAIR = 60000;
	final static int WEP_REPAIR = 100000;
	final static int BODY_REPAIR = 90000;
	final static int LEGS_REPAIR = 80000;
	
	public static boolean handleItemOnNpc(Player player, int npcId, Item item){
		int repairingNpc = -1;
		for(int i : repairers){
			if(npcId == i){
				repairingNpc = i;
				break;
			}
		}
		if(repairingNpc != -1 && repairPrice(item) != -1){
			player.tempMiscItem = item;
			player.tempMiscInt2 = repairingNpc;
			Dialogues.startDialogue(player, 10089);
			return true;
		}
		return false;
	}
	
	public static boolean repairItem(Player player, Item item) {
		int price = repairPrice(item);
		if (!player.getInventory().playerHasItem(new Item(995, price)) && price > 0) {
        	player.getActionSender().sendMessage("You don't have enough coins to fix that.");
        	return false;
        }
		player.getInventory().removeItem(new Item(995, price));
        player.getInventory().removeItemSlot(item, player.getSlot());
        player.getInventory().addItem(new Item(fixedItem(item), 1));
        return true;
	}
	
	int fixed;
	int wear100;
	
	public BarrowsItem(int fixed, int wear100){
		this.fixed = fixed;
		this.wear100 = wear100;
	}
	
	private int getFixed(){
		return this.fixed;
	}
	
	private int getWear100(){
		return this.wear100;
	}
	
	public int getWear0(){
		return this.wear100+4;
	}
	
	public static int fixedItem(Item item){
		BarrowsItem bi = getBarrowsItem(item);
		if(bi == null)
			return -1;
		return bi.getFixed();
	}
	
	public static boolean isDegradedItem(int id){
		Item item = new Item(id, 1);
		if(repairPrice(item) != -1)
			return true;
		return false;
	}
	
	public static int repairPrice(Item item){
		int slot = item.getDefinition().getSlot();
		int state = -1;
		BarrowsItem bi = getBarrowsItem(item);
		if(bi == null)
			return -1;
		state = item.getId()-bi.getWear100();
		double a = item.getTimer() < 0 ? 0 : TIMER-item.getTimer();
		double b = TIMER;
		double div1 = 1;
		double div2 = a/b;
		if(slot == Constants.HAT){
			div1 = HELM_REPAIR/4;
		}else if(slot == Constants.WEAPON){
			div1 = WEP_REPAIR/4;
		}else if(slot == Constants.CHEST){
			div1 = BODY_REPAIR/4;
		}else if(slot == Constants.LEGS){	
			div1 = LEGS_REPAIR/4;
		}
		return (int) ((div1*state)+(div1*div2));
	}
	
	public static BarrowsItem getBarrowsItem(Item item){
		if(item == null)
			return null;
		int itemId = item.getId();
		int slot = item.getDefinition().getSlot();
		if(slot == Constants.HAT){
			return getItem(itemId, 0);
		}else if(slot == Constants.WEAPON){
			return getItem(itemId, 1);
		}else if(slot == Constants.CHEST){
			return getItem(itemId, 2);
		}else if(slot == Constants.LEGS){	
			return getItem(itemId, 3);
		}
		return null;
	}
	
	private static BarrowsItem getItem(int itemId, int slot){
		for(int i = 0; i < barrowsItems.length; i++)
		if(itemId >= barrowsItems[i][slot].getWear100() && itemId <= barrowsItems[i][slot].getWear0()){
			return barrowsItems[i][slot];
		}
		return null;
	}
			
	public static BarrowsItem[][] barrowsItems = {
			{//ahrim
			new BarrowsItem(4708, 4856),//helm
			new BarrowsItem(4710, 4862),//wep
			new BarrowsItem(4712, 4868),//body
			new BarrowsItem(4714, 4874)},//legs
			
			{//dharok
			new BarrowsItem(4716, 4880),//helm
			new BarrowsItem(4718, 4886),//wep
			new BarrowsItem(4720, 4892),//body
			new BarrowsItem(4722, 4898)},//legs
			
			{//guthan
			new BarrowsItem(4724, 4904),//helm
			new BarrowsItem(4726, 4910),//wep
			new BarrowsItem(4728, 4916),//body
			new BarrowsItem(4730, 4922)},//legs
			
			{//karil
			new BarrowsItem(4732, 4928),//helm
			new BarrowsItem(4734, 4934),//wep
			new BarrowsItem(4736, 4940),//body
			new BarrowsItem(4738, 4946)},//legs
			
			{//torag
			new BarrowsItem(4745, 4952),//helm
			new BarrowsItem(4747, 4958),//wep
			new BarrowsItem(4749, 4964),//body
			new BarrowsItem(4751, 4970)},//legs
			
			{//verac
			new BarrowsItem(4753, 4976),//helm
			new BarrowsItem(4755, 4982),//wep
			new BarrowsItem(4757, 4988),//body
			new BarrowsItem(4759, 4994)},//legs
			
	};
}
