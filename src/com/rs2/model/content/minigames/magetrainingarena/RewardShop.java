package com.rs2.model.content.minigames.magetrainingarena;

import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;

public class RewardShop {

	private final static int SHOP_INTERFACE = 15944;
	
	public static void openShop(Player player){
		updateStrings(player);
		Item[] shopItems = new Item[rewardItems.length];
		for(int i = 0; i < rewardItems.length; i++){
			shopItems[i] = new Item(rewardItems[i].getId(), 100);
		}
		player.getActionSender().sendUpdateItems(15948, shopItems);
		player.getActionSender().sendInterface(SHOP_INTERFACE);
	}
	
	private static void updateStrings(Player player){
		player.getActionSender().sendString(""+player.getTelekineticTheatre().telekineticPizzazPoint, 15955);
		player.getActionSender().sendString(""+player.getEnchantingChamber().enchantmentPizzazPoint, 15956);
		player.getActionSender().sendString(""+player.getAlchemistPlayground().alchemistPizzazPoint, 15957);
		player.getActionSender().sendString(""+player.getCreatureGraveyard().graveyardPizzazPoints, 15958);
	}

	public static void getPrice(Player player, int slot){
		player.getActionSender().sendMessage(ItemManager.getInstance().getItemName(rewardItems[slot].getId()) + " costs "+rewardItems[slot].getTelekineticCost()+" Telekinetic, "+rewardItems[slot].getAlchemistCost()+" Alchemist,");
		player.getActionSender().sendMessage(rewardItems[slot].getEnchantmentCost()+" Enchantment and "+rewardItems[slot].getGraveyardCost()+" Graveyard Pizazz Points.");
	}
	
	public static void buyItem(Player player, int slot){
		RewardItem ritem = rewardItems[slot];
		if(ritem.getId() == 6926 && player.bonesToPeachUnlocked){
			player.getActionSender().sendMessage("You have already bought this item!");
			return;
		}
		if(ritem.getId() == 6910 && !player.getInventory().playerHasItem(6908)){//apprentice wand
			player.getActionSender().sendMessage("You need "+ItemManager.getInstance().getItemName(6908)+" to buy this item!");
			return;
		}
		if(ritem.getId() == 6912 && !player.getInventory().playerHasItem(6910)){//teacher wand
			player.getActionSender().sendMessage("You need "+ItemManager.getInstance().getItemName(6910)+" to buy this item!");
			return;
		}
		if(ritem.getId() == 6914 && !player.getInventory().playerHasItem(6912)){//master wand
			player.getActionSender().sendMessage("You need "+ItemManager.getInstance().getItemName(6912)+" to buy this item!");
			return;
		}
		if(ritem.getTelekineticCost() > player.getTelekineticTheatre().telekineticPizzazPoint ||
			ritem.getEnchantmentCost() > player.getEnchantingChamber().enchantmentPizzazPoint ||
			ritem.getAlchemistCost() > player.getAlchemistPlayground().alchemistPizzazPoint ||
			ritem.getGraveyardCost() > player.getCreatureGraveyard().graveyardPizzazPoints){
			player.getActionSender().sendMessage("You don't have enough points to buy this!");
			return;
		}
		//succesfull buy below
		if(ritem.getId() == 6926){
			player.bonesToPeachUnlocked = true;
			player.getActionSender().sendMessage("You have unlocked 'bones to peaches' spell.");
			spendPoints(player, ritem);
		} else if (player.getInventory().canAddItem(new Item(ritem.getId(), 1))){
			if(ritem.getId() == 6910)
				player.getInventory().removeItem(new Item(6908, 1));
			else if(ritem.getId() == 6912)
				player.getInventory().removeItem(new Item(6910, 1));
			else if(ritem.getId() == 6914)
				player.getInventory().removeItem(new Item(6912, 1));
			player.getInventory().addItem(new Item(ritem.getId(), 1));
			spendPoints(player, ritem);
		}
	}
	
	private static void spendPoints(Player player, RewardItem ritem){
		player.getTelekineticTheatre().telekineticPizzazPoint -= ritem.getTelekineticCost();
		player.getEnchantingChamber().enchantmentPizzazPoint -= ritem.getEnchantmentCost();
		player.getAlchemistPlayground().alchemistPizzazPoint -= ritem.getAlchemistCost();
		player.getCreatureGraveyard().graveyardPizzazPoints -= ritem.getGraveyardCost();
		updateStrings(player);
	}
	
	public static class RewardItem{
		
		int id;
		int[] cost;
		
		public RewardItem(int id, int[] cost){
			this.id = id;
			this.cost = cost;
		}
		
		public int getId(){
			return id;
		}
		
		public int getTelekineticCost(){
			return cost[0];
		}
		
		public int getAlchemistCost(){
			return cost[1];
		}
		
		public int getEnchantmentCost(){
			return cost[2];
		}
		
		public int getGraveyardCost(){
			return cost[3];
		}

	}
	
	public static RewardItem[] rewardItems = {
		new RewardItem(6908, new int[]{30, 30, 300, 30}),//beginner wand
		new RewardItem(6910, new int[]{60, 60, 600, 60}),//apprentice wand
		new RewardItem(6912, new int[]{150, 200, 1500, 150}),//teacher wand
		new RewardItem(6914, new int[]{240, 240, 2400, 240}),//master wand
		new RewardItem(6916, new int[]{400, 450, 4000, 400}),//infinity top
		new RewardItem(6918, new int[]{350, 400, 3000, 350}),//infinity hat
		new RewardItem(6920, new int[]{120, 120, 1200, 120}),//infinity boots
		new RewardItem(6922, new int[]{175, 225, 1500, 175}),//infinity gloves
		new RewardItem(6924, new int[]{450, 500, 5000, 450}),//infinity bottoms
		new RewardItem(6889, new int[]{500, 550, 6000, 500}),//mages book
		new RewardItem(6926, new int[]{200, 300, 2000, 200}),//bones to peaches spell
		new RewardItem(4695, new int[]{1, 1, 15, 1}),//mist rune
		new RewardItem(4696, new int[]{1, 1, 15, 1}),//dust rune
		new RewardItem(4698, new int[]{1, 1, 15, 1}),//mud rune
		new RewardItem(4697, new int[]{1, 1, 15, 1}),//smoke rune
		new RewardItem(4696, new int[]{1, 1, 15, 1}),//steam rune
		new RewardItem(4699, new int[]{1, 1, 15, 1}),//lava rune
		new RewardItem(564, new int[]{0, 0, 5, 0}),//cosmic rune
		new RewardItem(562, new int[]{0, 0, 5, 0}),//chaos rune
		new RewardItem(561, new int[]{0, 1, 0, 1}),//nature rune
		new RewardItem(560, new int[]{2, 1, 20, 1}),//death rune
		new RewardItem(563, new int[]{2, 0, 0, 0}),//law rune
		new RewardItem(566, new int[]{2, 2, 25, 2}),//soul rune
		new RewardItem(565, new int[]{2, 2, 25, 2}),//blood rune
	};
	
}