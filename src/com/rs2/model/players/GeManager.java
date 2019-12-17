package com.rs2.model.players;

import java.util.ArrayList;

import com.rs2.Constants;
import com.rs2.model.content.BankPin;
import com.rs2.model.content.GeOffer;
import com.rs2.model.players.Player.BankOptions;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class GeManager {
	
	public static final int GE_MAIN_INTERFACE = 19018;
	public static final int GE_BUY_INTERFACE = 18890;
	public static final int GE_SELL_INTERFACE = 18939;
	public static final int GE_COLLECT_INTERFACE = 18984;

    public static void openGe(Player player) {
    	clearTempValues(player);
    	updateOfferBoxes(player);
    	player.getActionSender().sendInterface(GE_MAIN_INTERFACE);
    }
    
    public static void updateCollectStatusScreen(Player player) {
    	double p1 = player.geItemsTraded[player.geOfferBox_temp];
		double p2 = player.geOfferItemAmount[player.geOfferBox_temp];
		double p3 = 100*(p1/p2);
		int progress = (int) p3;
    	boolean isCompleted = progress == 100;
    	boolean isCancelled = player.geIsCanceled[player.geOfferBox_temp];
		int child = 18998;
		player.getActionSender().sendString("for a total price of "+Misc.format(player.geCashMoved[player.geOfferBox_temp])+" coins.", child+4);
		String s = ((isCompleted || isCancelled) ? "" : "have");
		String s2 = ((isCompleted || isCancelled) ? "" : " so far");
		if(player.geOfferType[player.geOfferBox_temp]){
			player.getActionSender().sendString("You "+s+" sold a total of "+Misc.format(player.geItemsTraded[player.geOfferBox_temp])+""+s2, child+3);
		} else {
			player.getActionSender().sendString("You "+s+" bought a total of "+Misc.format(player.geItemsTraded[player.geOfferBox_temp])+""+s2, child+3);
		}
		if(isCancelled)
			progress = 250;
		if(isCancelled || isCompleted)
			player.getActionSender().sendFrame171(1, 19016);//hide cancel
		else
			player.getActionSender().sendFrame171(0, 19016);//show cancel
		player.getActionSender().sendGeProgressBar(19011, progress);
		Item[] collectItems = null;
		Item item1 = null;
		Item item2 = null;
    	if(player.geOfferType[player.geOfferBox_temp]){//sell
    		item1 = new Item(995, player.geCollectItem2Receive[player.geOfferBox_temp]);
    		item2 = new Item(player.geOfferItemId[player.geOfferBox_temp], player.geCollectOtherItem[player.geOfferBox_temp]);
    	} else {
    		item1 = new Item(player.geOfferItemId[player.geOfferBox_temp], player.geCollectItem2Receive[player.geOfferBox_temp]);
    		item2 = new Item(995, player.geCollectOtherItem[player.geOfferBox_temp]);
    	}
    	if(item1.getCount() <= 0)
			item1 = null;
		if(item2.getCount() <= 0)
			item2 = null;
		collectItems = new Item[]{item1, item2};
		player.getActionSender().sendUpdateItems(19006, collectItems);
    }
    
    public static void viewOffer(Player player) {
    	double p1 = player.geItemsTraded[player.geOfferBox_temp];
		double p2 = player.geOfferItemAmount[player.geOfferBox_temp];
		double p3 = 100*(p1/p2);
		int progress = (int) p3;
    	//int progress = 100*(player.geItemsTraded[player.geOfferBox_temp]/player.geOfferItemAmount[player.geOfferBox_temp]);
    	boolean isCompleted = progress == 100;
    	boolean isCancelled = player.geIsCanceled[player.geOfferBox_temp];
    	player.geOfferItemId_temp = player.geOfferItemId[player.geOfferBox_temp];
		player.geOfferItemAmount_temp = player.geOfferItemAmount[player.geOfferBox_temp];
		player.geOfferPrice_temp = player.geOfferPrice[player.geOfferBox_temp];
		player.getActionSender().sendString(""+Misc.format(getGuidePrice(player.geOfferItemId[player.geOfferBox_temp])), 18997);
		player.getActionSender().sendGeItemSprite(19008, player.geOfferItemId[player.geOfferBox_temp]);
		int child = 18998;
		player.getActionSender().sendString(""+Misc.format(player.geOfferItemAmount_temp), child);
		player.getActionSender().sendString(Misc.format(player.geOfferPrice_temp)+" coins", child+1);
		player.getActionSender().sendString(Misc.format(player.geOfferPrice_temp*player.geOfferItemAmount_temp)+" coins", child+2);
		player.getActionSender().sendString("for a total price of "+Misc.format(player.geCashMoved[player.geOfferBox_temp])+" coins.", child+4);
		String s = ((isCompleted || isCancelled) ? "" : "have");
		String s2 = ((isCompleted || isCancelled) ? "" : " so far");
		if(player.geOfferType[player.geOfferBox_temp]){
			player.getActionSender().sendFrame171(0, 19012);//show sell
			player.getActionSender().sendFrame171(1, 19014);//hide buy
			player.getActionSender().sendString("Sell offer", 18992);
			player.getActionSender().sendString("You "+s+" sold a total of "+Misc.format(player.geItemsTraded[player.geOfferBox_temp])+""+s2, child+3);
		} else {
			player.getActionSender().sendFrame171(1, 19012);//hide sell
			player.getActionSender().sendFrame171(0, 19014);//show buy
			player.getActionSender().sendString("Buy offer", 18992);
			player.getActionSender().sendString("You "+s+" bought a total of "+Misc.format(player.geItemsTraded[player.geOfferBox_temp])+""+s2, child+3);
		}
		if(isCancelled)
			progress = 250;
		if(isCancelled || isCompleted)
			player.getActionSender().sendFrame171(1, 19016);//hide cancel
		else
			player.getActionSender().sendFrame171(0, 19016);//show cancel
		player.getActionSender().sendGeProgressBar(19011, progress);
		Item[] collectItems = null;
		Item item1 = null;
		Item item2 = null;
    	if(player.geOfferType[player.geOfferBox_temp]){//sell
    		item1 = new Item(995, player.geCollectItem2Receive[player.geOfferBox_temp]);
    		item2 = new Item(player.geOfferItemId[player.geOfferBox_temp], player.geCollectOtherItem[player.geOfferBox_temp]);
    	} else {
    		item1 = new Item(player.geOfferItemId[player.geOfferBox_temp], player.geCollectItem2Receive[player.geOfferBox_temp]);
    		item2 = new Item(995, player.geCollectOtherItem[player.geOfferBox_temp]);
    	}
    	if(item1.getCount() <= 0)
			item1 = null;
		if(item2.getCount() <= 0)
			item2 = null;
		collectItems = new Item[]{item1, item2};
		player.getActionSender().sendUpdateItems(19006, collectItems);
    	player.getActionSender().sendInterface(GE_COLLECT_INTERFACE);
    	if(item1 == null && item2 == null && (isCompleted || isCancelled)){
			resetGeBox(player);
		}
    }
    
    public static void cancelOffer(Player player){
    	int progress = 100*(player.geItemsTraded[player.geOfferBox_temp]/player.geOfferItemAmount[player.geOfferBox_temp]);
    	boolean isCompleted = progress == 100;
    	boolean isCancelled = player.geIsCanceled[player.geOfferBox_temp];
    	if(isCancelled || isCompleted)
    		return;
    	GeOffer.cancelOffer(player, player.geOfferBox_temp);
    	player.geIsCanceled[player.geOfferBox_temp] = true;
    	player.getActionSender().sendFrame171(1, 19016);//hide cancel
    	Item[] collectItems = null;
    	if(player.geOfferType[player.geOfferBox_temp]){//sell
    		Item item1 = new Item(995, player.geCollectItem2Receive[player.geOfferBox_temp]);
    		Item item2 = new Item(player.geOfferItemId[player.geOfferBox_temp], (player.geOfferItemAmount[player.geOfferBox_temp]-player.geItemsTraded[player.geOfferBox_temp]));
    		player.geCollectOtherItem[player.geOfferBox_temp] = item2.getCount();
    		if(item1.getCount() <= 0)
    			item1 = null;
    		if(item2.getCount() <= 0)
    			item2 = null;
    		collectItems = new Item[]{item1, item2};
    	} else {
    		Item item1 = new Item(player.geOfferItemId[player.geOfferBox_temp], player.geCollectItem2Receive[player.geOfferBox_temp]);
    		Item item2 = new Item(995, ((player.geOfferItemAmount[player.geOfferBox_temp]*player.geOfferPrice[player.geOfferBox_temp])-player.geCashMoved[player.geOfferBox_temp]));
    		player.geCollectOtherItem[player.geOfferBox_temp] = item2.getCount();
    		if(item1.getCount() <= 0)
    			item1 = null;
    		if(item2.getCount() <= 0)
    			item2 = null;
    		collectItems = new Item[]{item1, item2};
    	}
    	player.getActionSender().sendGeProgressBar(19011, 250);
		player.getActionSender().sendUpdateItems(19006, collectItems);
		updateCollectStatusScreen(player);
    }
    
    public static void collectItem(Player player, int slot, int itemId, int itemCount) {
    	double p1 = player.geItemsTraded[player.geOfferBox_temp];
		double p2 = player.geOfferItemAmount[player.geOfferBox_temp];
		double p3 = 100*(p1/p2);
		int progress = (int) p3;
		Item item = new Item(itemId);
		boolean isNoteable = item.getDefinition().isNoteable();
		int notedid = item.getDefinition().getNotedId();
		boolean isCompleted = progress == 100;
    	boolean isCancelled = player.geIsCanceled[player.geOfferBox_temp];
		int item2Collect = 0;
		int amount2Collect = 0;
		if(slot == 0){
			if(player.geOfferType[player.geOfferBox_temp])
				item2Collect = 995;
			else
				item2Collect = player.geOfferItemId[player.geOfferBox_temp];
			amount2Collect = player.geCollectItem2Receive[player.geOfferBox_temp];
		}else{
			if(player.geOfferType[player.geOfferBox_temp]){
				item2Collect = player.geOfferItemId[player.geOfferBox_temp];
			}else
				item2Collect = 995;
			amount2Collect = player.geCollectOtherItem[player.geOfferBox_temp];
		}
		if (itemCount < 1 || itemId < 0 || item2Collect != item.getId() || !item.validItem()) {
			return;
		}
		if (itemId > Constants.MAX_ITEM_ID) {
			player.getActionSender().sendMessage("This item is not supported yet.");
			return;
		}
		int itemId_collected = (isNoteable ? notedid : itemId);
		Item collectedItem = new Item(itemId_collected, amount2Collect);
		if(!player.getInventory().canAddItem(collectedItem))
			return;
		if(slot == 0)
			player.geCollectItem2Receive[player.geOfferBox_temp] = 0;
		else
			player.geCollectOtherItem[player.geOfferBox_temp] = 0;
		Item[] collectItems = null;
		Item item1 = null;
		Item item2 = null;
    	if(player.geOfferType[player.geOfferBox_temp]){//sell
    		item1 = new Item(995, player.geCollectItem2Receive[player.geOfferBox_temp]);
    		item2 = new Item(player.geOfferItemId[player.geOfferBox_temp], player.geCollectOtherItem[player.geOfferBox_temp]);
    	} else {
    		item1 = new Item(player.geOfferItemId[player.geOfferBox_temp], player.geCollectItem2Receive[player.geOfferBox_temp]);
    		item2 = new Item(995, player.geCollectOtherItem[player.geOfferBox_temp]);
    	}
    	if(item1.getCount() <= 0)
			item1 = null;
		if(item2.getCount() <= 0)
			item2 = null;
		collectItems = new Item[]{item1, item2};
		player.getActionSender().sendUpdateItems(19006, collectItems);
		player.getInventory().addItem(collectedItem);
		if(item1 == null && item2 == null && (isCompleted || isCancelled)){
			resetGeBox(player);
		}
	}
    
    public static void notifyBox(Player player, int boxId){
    	String itemName = new Item(player.geOfferItemId[boxId], 1).getDefinition().getName();
    	String s = "buying";
    	if(player.geOfferType[boxId])
    		s = "selling";
    	player.getActionSender().sendMessage("Grand Exchange: Finished "+s+" "+Misc.format(player.geOfferItemAmount[boxId])+" x "+itemName+".");
    	/*if(player.geOfferType[boxId]){//sell
    		player.getActionSender().sendMessage("Finished selling "+player.geOfferItemAmount[boxId]+" "+itemName+".");
    	} else {
    		player.getActionSender().sendMessage("Finished buying "+player.geOfferItemAmount[boxId]+" "+itemName+".");
    	}*/
    	player.geNotifyProgress[boxId] = false;
    }
    
    public static void resetGeBox(Player player){
    	player.geOfferType[player.geOfferBox_temp] = true;
    	player.geOfferItemId[player.geOfferBox_temp] = -1;
    	player.geOfferItemAmount[player.geOfferBox_temp] = 0;
    	player.geOfferPrice[player.geOfferBox_temp] = 0;
    	player.geIsCanceled[player.geOfferBox_temp] = false;
    	player.geItemsTraded[player.geOfferBox_temp] = 0;
    	player.geCashMoved[player.geOfferBox_temp] = 0;
    	player.geCollectItem2Receive[player.geOfferBox_temp] = 0;
    	player.geCollectOtherItem[player.geOfferBox_temp] = 0;
    	openGe(player);
    }
    
    public static void resetGeBox(Player player, int box){
    	if(box < 0 || box > 5)
    		return;
    	player.geOfferType[box] = true;
    	player.geOfferItemId[box] = -1;
    	player.geOfferItemAmount[box] = 0;
    	player.geOfferPrice[box] = 0;
    	player.geIsCanceled[box] = false;
    	player.geItemsTraded[box] = 0;
    	player.geCashMoved[box] = 0;
    	player.geCollectItem2Receive[box] = 0;
    	player.geCollectOtherItem[box] = 0;
    	openGe(player);
    }

    public static boolean geButtons(Player player, int buttonId){
		switch(buttonId){
		
			case 19024://buy buttons
			case 19027:
			case 19030:
			case 19033:
			case 19036:
			case 19039:
				player.geOfferBox_temp = (buttonId-19024)/3;
				player.getActionSender().sendInterface(GE_BUY_INTERFACE);
				return true;
				
			case 19025://sell buttons
			case 19028:
			case 19031:
			case 19034:
			case 19037:
			case 19040:
				player.geOfferBox_temp = (buttonId-19025)/3;
				Item[] inventory = player.getInventory().getItemContainer().toArray();
				player.getActionSender().sendUpdateItems(19102, inventory);
				player.getActionSender().sendInterface(GE_SELL_INTERFACE, 19101);
				return true;
			
			case 18907://back to main buttons
			case 18956:
			case 18990:
				openGe(player);
				return true;
				
			case 19042://view offer
			case 19051:
			case 19060:
			case 19069:
			case 19078:
			case 19087:
				player.geOfferBox_temp = (buttonId-19042)/9;
				viewOffer(player);
				return true;
				
			case 18908:
			case 18957:
				updateItemAmount(player, -1);
				return true;
			
			case 18909:
			case 18898:
			case 18958:
			case 18947:
				updateItemAmount(player, +1);
				return true;
				
			case 18899:
			case 18948:	
				updateItemAmount(player, +10);
				return true;
			
			case 18900:
			case 18949:	
				updateItemAmount(player, +100);
				return true;
				
			case 18901:
				updateItemAmount(player, +1000);
				return true;
				
			case 18950:
				updateItemAmount(player, "all");
				return true;	
				
			case 18902:
			case 18951:	
				player.getActionSender().openXInterface(18900);
				return true;
				
			case 18910:
			case 18959:
				updatePrice(player, -1);
				return true;
			
			case 18911:
			case 18960:	
				updatePrice(player, +1);
				return true;
				
			case 18903:
			case 18952:
				updatePrice(player, "-5%");
				return true;
			
			case 18906:
			case 18955:	
				updatePrice(player, "+5%");
				return true;
				
			case 18904:
			case 18953:	
				updatePrice(player, "guide");
				return true;
				
			case 18905:
			case 18954:
				player.getActionSender().openXInterface(18901);
				return true;
				
			case 18896://accept offer, buy
			case 18945://accept offer, sell
				acceptOffer(player);
				return true;
				
			case 19017://cancel
				cancelOffer(player);
				return true;
				
		}
		return false;
	}
    
    public static void updateOfferBoxes(Player player){
    	for(int box = 0; box < 6; box++){
    		if(player.geOfferItemAmount[box] == 0){
    			player.getActionSender().sendString("Empty", 19095+box);
    			
    			player.getActionSender().sendFrame171(0, 19023+(box*3));//show buy/sell
    			player.getActionSender().sendFrame171(1, 19041+(box*9));//hide status
    			
    			player.getActionSender().sendUpdateItem(19044+(box*9), -1, 0);
				player.getActionSender().sendString("Item name", 19045+(box*9));
				player.getActionSender().sendString("0 coins", 19046+(box*9));
				player.getActionSender().sendGeProgressBar(19049+(box*9), 0);
    		} else {
    			player.getActionSender().sendString(player.geOfferType[box] ? "Sell" : "Buy", 19095+box);
    			//player.getActionSender().sendUpdateItems(19044+(box*9), new Item[]{new Item(player.geOfferItemId[box], player.geOfferItemAmount[box])});
    			player.getActionSender().sendUpdateItem(19044+(box*9), player.geOfferItemId[box], player.geOfferItemAmount[box]);
    			player.getActionSender().sendString(new Item(player.geOfferItemId[box], 1).getDefinition().getName(), 19045+(box*9));
				player.getActionSender().sendString(Misc.format(player.geOfferPrice[box])+" coins", 19046+(box*9));
				double p1 = player.geItemsTraded[box];
				double p2 = player.geOfferItemAmount[box];
				double p3 = 100*(p1/p2);
				int progress = (int) p3;
				//int progress = 100*(player.geItemsTraded[box]/player.geOfferItemAmount[box]);
				boolean isCancelled = player.geIsCanceled[box];
				if(isCancelled)
					progress = 250;
				player.getActionSender().sendGeProgressBar(19049+(box*9), progress);
				
				player.getActionSender().sendFrame171(1, 19023+(box*3));//hide buy/sell
    			player.getActionSender().sendFrame171(0, 19041+(box*9));//show status
    		}
    	}
    }
    
    public static void acceptOffer(Player player){
    	if(player.geOfferItemId_temp < 0)
    		return;
    	if(player.geOfferItemAmount_temp < 0)
    		return;
    	if(player.geOfferBox_temp < 0 || player.geOfferBox_temp > 5)
    		return;
    	if(player.geOfferPrice_temp*player.geOfferItemAmount_temp < 0)
    		return;
    	boolean isSell = true;
    	if(player.getInterface() == GE_BUY_INTERFACE)
    		isSell = false;
    	if(!isSell){
    		int cashNeeded = player.geOfferPrice_temp*player.geOfferItemAmount_temp;
    		if(!player.getInventory().playerHasItem(995, cashNeeded)){
    			player.getActionSender().sendMessage("Not enough coins!");
    			return;
    		}
    		player.getInventory().removeItem(new Item(995, cashNeeded));
    	} else {
    		int itemsNeeded = player.geOfferItemAmount_temp;
    		Item item = new Item(player.geOfferItemId_temp, 1);
    		//int itemAmount = player.getInventory().getItemContainer().getCount(player.geOfferItemId_temp);
    		int notedAmount = 0;
    		int notedid = -1;
    		boolean isNoteable = item.getDefinition().isNoteable();
    		if(isNoteable){
    			notedid = item.getDefinition().getNotedId();
    			notedAmount = player.getInventory().getItemContainer().getCount(notedid);
    		}
    		if(notedAmount >= itemsNeeded){
    			player.getInventory().removeItem(new Item(notedid, itemsNeeded));
    		} else {
    			if(notedAmount > 0)
    				player.getInventory().removeItem(new Item(notedid, notedAmount));
    			player.getInventory().removeItem(new Item(player.geOfferItemId_temp, itemsNeeded-notedAmount));
    		}
    	}
    	player.geOfferType[player.geOfferBox_temp] = isSell;
    	player.geOfferItemId[player.geOfferBox_temp] = player.geOfferItemId_temp;
    	player.geOfferItemAmount[player.geOfferBox_temp] = player.geOfferItemAmount_temp;
    	player.geOfferPrice[player.geOfferBox_temp] = player.geOfferPrice_temp;
    	//System.out.println("Processed accept offer.");
    	new GeOffer(player.getUsername(), player.geOfferBox_temp, isSell, player.geOfferItemId_temp, player.geOfferItemAmount_temp, player.geOfferItemAmount_temp, player.geOfferPrice_temp);
    	openGe(player);
    }
    
    public static int getGuidePrice(int id){
    	int price = GeOffer.CompletedOffer.getGuidePriceForItem(id);
    	if(price == -1){
    		Item item = new Item(id, 1);
        	price = item.getDefinition().getPrice();
    	}
    	return price;
    }
    
    public static void clearTempValues(Player player){
    	player.geOfferItemId_temp = -1;
    	player.geOfferItemAmount_temp = 0;
    	player.geOfferPrice_temp = 0;
    	player.geOfferBox_temp = 0;
    }
    
    public static void updateItemAmountX(Player player, int modify){
    	player.geOfferItemAmount_temp = modify;
    	if(player.geOfferItemAmount_temp < 1)
    		player.geOfferItemAmount_temp = 1;
    	if(player.getInterface() == GE_SELL_INTERFACE){
    		int maxAmount = player.getInventory().getItemContainer().getCount(player.geOfferItemId_temp);
    		Item item = new Item(player.geOfferItemId_temp, 1);
    		boolean isNoteable = item.getDefinition().isNoteable();
    		if(isNoteable){
    			int notedid = item.getDefinition().getNotedId();
    			maxAmount += player.getInventory().getItemContainer().getCount(notedid);
    		}
    		if(player.geOfferItemAmount_temp >= maxAmount){
    			player.geOfferItemAmount_temp = maxAmount;
    		}
    	}
    	updateStrings(player);
    }
    
    public static void updatePriceX(Player player, int modify){
    	player.geOfferPrice_temp = modify;
    	if(player.geOfferPrice_temp < 0)
    		player.geOfferPrice_temp = 0;
    	updateStrings(player);
    }
    
    public static void updateItemAmount(Player player, int modify){
    	player.geOfferItemAmount_temp += modify;
    	if(player.geOfferItemAmount_temp < 1)
    		player.geOfferItemAmount_temp = 1;
    	if(player.getInterface() == GE_SELL_INTERFACE){
    		int maxAmount = player.getInventory().getItemContainer().getCount(player.geOfferItemId_temp);
    		Item item = new Item(player.geOfferItemId_temp, 1);
    		boolean isNoteable = item.getDefinition().isNoteable();
    		if(isNoteable){
    			int notedid = item.getDefinition().getNotedId();
    			maxAmount += player.getInventory().getItemContainer().getCount(notedid);
    		}
    		if(player.geOfferItemAmount_temp >= maxAmount){
    			player.geOfferItemAmount_temp = maxAmount;
    		}
    	}
    	updateStrings(player);
    }
    
    public static void updateItemAmount(Player player, String s){
    	if(s.equals("all")){
    		player.geOfferItemAmount_temp = player.getInventory().getItemContainer().getCount(player.geOfferItemId_temp);
    		Item item = new Item(player.geOfferItemId_temp, 1);
    		boolean isNoteable = item.getDefinition().isNoteable();
    		if(isNoteable){
    			int notedid = item.getDefinition().getNotedId();
    			player.geOfferItemAmount_temp += player.getInventory().getItemContainer().getCount(notedid);
    		}
    	}
    	updateStrings(player);
    }
    
    public static void updatePrice(Player player, int modify){
    	player.geOfferPrice_temp += modify;
    	if(player.geOfferPrice_temp < 0)
    		player.geOfferPrice_temp = 0;
    	updateStrings(player);
    }
    
    public static void updatePrice(Player player, String modify){
    	if(modify.equals("guide"))
    		player.geOfferPrice_temp = getGuidePrice(player.geOfferItemId_temp);
    	if(modify.equals("+5%")){
    		double a = player.geOfferPrice_temp;
    		double b = a*1.05;
    		int ev = (int) b;
    		player.geOfferPrice_temp = ev;
    	}
    	if(modify.equals("-5%")){
    		double a = player.geOfferPrice_temp;
    		double b = a*0.95;
    		int ev = (int) b;
    		player.geOfferPrice_temp = ev;
    	}
    	if(player.geOfferPrice_temp < 0)
    		player.geOfferPrice_temp = 0;
    	updateStrings(player);
    }
    
    public static void updateStrings(Player player){
    	int child = 18920;
    	if(player.getInterface() == GE_SELL_INTERFACE)
    		child = 18969;
		player.getActionSender().sendString(""+Misc.format(player.geOfferItemAmount_temp), child);
		player.getActionSender().sendString(Misc.format(player.geOfferPrice_temp)+" coins", child+1);
		player.getActionSender().sendString(Misc.format(player.geOfferPrice_temp*player.geOfferItemAmount_temp)+" coins", child+2);
    }
    
    public static void sellItem(Player player, int slot, int itemId, int itemAmount) {
		Item inventoryItem = player.getInventory().getItemContainer().get(slot);
		if (inventoryItem == null || inventoryItem.getId() != itemId || !inventoryItem.validItem()) {
			return;
		}
		if(inventoryItem.getDefinition().isUntradable())
			return;
		int amount = player.getInventory().getItemContainer().getCount(itemId);
		boolean isNote = inventoryItem.getDefinition().isNoted();
		if (inventoryItem.getDefinition().getId() > Constants.MAX_ITEM_ID) {
			player.getActionSender().sendMessage("This item is not supported yet.");
			return;
		}
		int transferId = isNote ? inventoryItem.getDefinition().getNormalId() : inventoryItem.getDefinition().getId();
		if (amount > itemAmount) {
			amount = itemAmount;
		}
		if(transferId == 995)//can't sell money!
			return;
		
		player.geOfferItemId_temp = transferId;
		player.geOfferItemAmount_temp = inventoryItem.getCount();
		player.geOfferPrice_temp = getGuidePrice(transferId);
		player.getActionSender().sendString(""+Misc.format(player.geOfferPrice_temp), 18968);
		player.getActionSender().sendGeItemSprite(18983, transferId);
		updateStrings(player);
	}
    
}
