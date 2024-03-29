package com.rs2.model.players;

import java.util.ArrayList;

import com.rs2.Constants;
import com.rs2.model.players.Player.TradeStage;
import com.rs2.model.players.container.inventory.Inventory;
import com.rs2.model.players.item.Item;
import com.rs2.util.Logger;
import com.rs2.util.Misc;
import com.rs2.util.NameUtil;

public class TradeManager {

	public static void refresh(Player player, Player otherPlayer) {
		player.getActionSender().sendUpdateItems(3322, player.getInventory().getItemContainer().toArray());
		otherPlayer.getActionSender().sendUpdateItems(3322, otherPlayer.getInventory().getItemContainer().toArray());
		player.getActionSender().sendUpdateItems(3415, player.getTrade().toArray());
		player.getActionSender().sendUpdateItems(3416, otherPlayer.getTrade().toArray());
		otherPlayer.getActionSender().sendUpdateItems(3415, otherPlayer.getTrade().toArray());
		otherPlayer.getActionSender().sendUpdateItems(3416, player.getTrade().toArray());
		player.getActionSender().sendString("Trading With: " + otherPlayer.getUsername(), 3417);
		otherPlayer.getActionSender().sendString("Trading With: " + player.getUsername(), 3417);
	}

	public static void handleTradeRequest(Player player, Player otherPlayer) {
		if(player.questStage[0] != 1)
			return;
		if (!Constants.ADMINS_CAN_INTERACT && (player.getStaffRights() >= 2 || otherPlayer.getStaffRights() >= 2)) {
			player.getActionSender().sendMessage("You are not allowed to trade this player.");
			return;
	    }
		if (otherPlayer.getLastPersonTraded() != player) {
			player.getActionSender().sendMessage("Sending trade offer...");
			otherPlayer.getActionSender().sendMessage("" + NameUtil.uppercaseFirstLetter(player.getUsername()) + ":tradereq:");
			player.setTradeStage(TradeStage.SEND_REQUEST);
			player.setLastPersonTraded(otherPlayer);
		} else {
			player.setTradeStage(TradeStage.SEND_REQUEST_ACCEPT);
			otherPlayer.setTradeStage(TradeStage.SEND_REQUEST_ACCEPT);
			sendTrade(player, otherPlayer);
			player.setLastPersonTraded(null);
			otherPlayer.setLastPersonTraded(null);
		}
	}

	private static void sendTrade(Player player, Player otherPlayer) {
		player.getTrade().clear();
		otherPlayer.getTrade().clear();
		player.getActionSender().sendString("Trading With: " + otherPlayer.getUsername() + " has " + otherPlayer.getInventory().getItemContainer().freeSlots() + " free inventory slots.", 3417);
		otherPlayer.getActionSender().sendString("Trading With: " + player.getUsername() + " has " + player.getInventory().getItemContainer().freeSlots() + " free inventory slots.", 3417);
		player.getActionSender().sendString("", 3431);
		otherPlayer.getActionSender().sendString("", 3431);
		refresh(player, otherPlayer);
		player.setTradingEntity(otherPlayer);
        otherPlayer.setTradingEntity(player);
        player.getActionSender().sendInterface(3323, 3321);
		otherPlayer.getActionSender().sendInterface(3323, 3321);
	}

	public static void declineTrade(Player player) {
		if (player.getTradingEntity() == null) {
			return;
		}
		Player otherPlayer = (Player) player.getTradingEntity();
		otherPlayer.getActionSender().sendMessage("Other player has declined the trade.");
		player.getActionSender().removeInterfaces();
        otherPlayer.getActionSender().removeInterfaces();
		player.setTradeStage(TradeStage.WAITING);
        otherPlayer.setTradeStage(TradeStage.WAITING);
        giveBackItems(otherPlayer);
        giveBackItems(player);
		player.setTradingEntity(null);
        otherPlayer.setTradingEntity(null);
        player.save();
        otherPlayer.save();
	}

	public static void giveBackItems(Player player) {
		for (int i = 0; i < Inventory.SIZE; i++) {
			if (player.getTrade().get(i) != null) {
				Item item = player.getTrade().get(i);
				if (item != null) {
					player.getTrade().remove(item);
					player.getInventory().addItem(item);
				}
			}
		}
        player.getTrade().clear();
	}

	public static void offerItem(Player player, int slot, int tradeItem, int amount) {
		Player otherPlayer = (Player) player.getTradingEntity();
		if (player.getTradeStage().equals(TradeStage.SECOND_TRADE_WINDOW)) {
			return;
		}
		if (tradeItem == -1 || otherPlayer == null) {
			return;
		}
		Item inv = player.getInventory().getItemContainer().get(slot);
		int invAmount = player.getInventory().getItemContainer().getCount(tradeItem);
		if (inv == null || inv.getId() != tradeItem || !inv.validItem()) {
			return;
		}
		if (inv.getId() <= 0 || !inv.validItem() || amount < 1) {
			return;
		}
		if (new Item(tradeItem).getDefinition().isUntradable()) {
			player.getActionSender().sendMessage("You cannot trade that item.");
			return;
		}
		if (invAmount > amount) {
			invAmount = amount;
		}
		if (inv.getDefinition().isStackable()) {
			if (!player.getInventory().removeItemSlot(new Item(tradeItem, invAmount), slot)) {
				player.getInventory().removeItem(new Item(tradeItem, invAmount));
			}
		} else {
			for (int i = 0; i < invAmount; i++) {
				player.getInventory().removeItem(new Item(tradeItem, 1));
			}
		}
		int tradeAmount = player.getTrade().getCount(tradeItem);
		if (tradeAmount > 0 && inv.getDefinition().isStackable()) {
			player.getTrade().set(player.getTrade().getSlotById(inv.getId()), new Item(tradeItem, tradeAmount + invAmount));
		} else {
			player.getTrade().add(new Item(inv.getId(), invAmount));
		}
		refresh(player, otherPlayer);
		player.setTradeStage(TradeStage.SEND_REQUEST_ACCEPT);
		otherPlayer.setTradeStage(TradeStage.SEND_REQUEST_ACCEPT);
		player.getActionSender().sendString("", 3431);
		otherPlayer.getActionSender().sendString("", 3431);
	}

	public static void removeTradeItem(Player player, int slot, int tradeItem, int amount) {
		Player otherPlayer = (Player) player.getTradingEntity();
		if (player.getTradeStage().equals(TradeStage.SECOND_TRADE_WINDOW)) {
			return;
		}
		if (tradeItem == -1 || otherPlayer == null) {
			return;
		}
		Item itemOnScreen = player.getTrade().get(slot);
		int itemOnScreenAmount = player.getTrade().getCount(tradeItem);
		if (itemOnScreen == null || itemOnScreen.getId() != tradeItem || amount < 1) {
			return;
		}
		if (itemOnScreenAmount > amount) {
			itemOnScreenAmount = amount;
		}
		int remove = player.getTrade().remove(new Item(tradeItem, itemOnScreenAmount), slot);
		player.getInventory().addItem(new Item(itemOnScreen.getId(), remove));
		refresh(player, otherPlayer);
		player.setTradeStage(TradeStage.SEND_REQUEST_ACCEPT);
		otherPlayer.setTradeStage(TradeStage.SEND_REQUEST_ACCEPT);
		player.getActionSender().sendString("", 3431);
		otherPlayer.getActionSender().sendString("", 3431);
	}

	public static void acceptStageOne(Player player) {
		Player otherPlayer = (Player) player.getTradingEntity();
		player.setTradeStage(TradeStage.ACCEPT);
		if (!otherPlayer.getTradeStage().equals(TradeStage.ACCEPT)) {
			player.getActionSender().sendString("Waiting for other player...", 3431);
			otherPlayer.getActionSender().sendString("Other player accepted.", 3431);
		} else {
			int tradeItems = 0;
			for (Item item : player.getTrade().getItems()) {
				if (item != null && (!item.getDefinition().isStackable() || !otherPlayer.getInventory().playerHasItem(item.getId()))) {
					tradeItems++;
				}
			}
			if (otherPlayer.getInventory().getItemContainer().freeSlots() < tradeItems) {
				player.getActionSender().sendString("Other player doesn't have enough inventory space for this trade.", 3431);
				otherPlayer.getActionSender().sendString("You don't have enough inventory space for this trade.", 3431);
				return;
			}
			tradeItems = 0;
			for (Item item : otherPlayer.getTrade().getItems()) {
				if (item != null && (!item.getDefinition().isStackable() || !player.getInventory().playerHasItem(item.getId()))) {
					tradeItems++;
				}
			}
			if (player.getInventory().getItemContainer().freeSlots() < tradeItems) {
				otherPlayer.getActionSender().sendString("Other player doesn't have enough inventory space for this trade.", 3431);
				player.getActionSender().sendString("You don't have enough inventory space for this trade.", 3431);
				return;
			}
			refresh(player, otherPlayer);
			player.getActionSender().sendInterface(3443, 3213);
			otherPlayer.getActionSender().sendInterface(3443, 3213);
			player.setTradeStage(TradeStage.SECOND_TRADE_WINDOW);
			otherPlayer.setTradeStage(TradeStage.SECOND_TRADE_WINDOW);
			player.getActionSender().sendString("Are you sure you want to accept this trade?", 3535);
			otherPlayer.getActionSender().sendString("Are you sure you want to accept this trade?", 3535);
			sendSecondScreen(player);
			sendSecondScreen(otherPlayer);
		}
	}

	public static void acceptStageTwo(Player player) {
		if (!player.getTradeStage().equals(TradeStage.SECOND_TRADE_WINDOW)) {
			return;
		}
		Player otherPlayer = (Player) player.getTradingEntity();
		player.setTradeStage(TradeStage.ACCEPT);
		if (!otherPlayer.getTradeStage().equals(TradeStage.ACCEPT)) {
			player.getActionSender().sendString("Waiting for other player...", 3535);
			otherPlayer.getActionSender().sendString("Other player accepted.", 3535);
		} else {
			Item[] playerItems = new Item[Inventory.SIZE];
			for (int i = 0; i < Inventory.SIZE; i++) {
				Item newItems = player.getTrade().get(i);
				if (newItems == null) {
					continue;
				}
				playerItems[i] = newItems;
				player.getTrade().remove(newItems);
				otherPlayer.getInventory().addItem(newItems);
			}
			Item[] otherPlayerItems = new Item[Inventory.SIZE];
			for (int i = 0; i < Inventory.SIZE; i++) {
				Item newItems = otherPlayer.getTrade().get(i);
				if (newItems == null) {
					continue;
				}
				otherPlayerItems[i] = newItems;
				otherPlayer.getTrade().remove(newItems);
				player.getInventory().addItem(newItems);
			}
			player.setTradeStage(TradeStage.WAITING);
			otherPlayer.setTradeStage(TradeStage.WAITING);
			player.getActionSender().sendMessage("You accept the trade.");
			otherPlayer.getActionSender().sendMessage("You accept the trade.");
			if(Constants.LOG_TRADES){
				String[] tradeInfo = new String[100];
				tradeInfo[0] = "NEW TRADE";
				tradeInfo[1] = System.currentTimeMillis()+"�"+player.getUsername()+"�"+player.getPosition().getX()+"�"+player.getPosition().getY()+"�"+player.getPosition().getZ()+"�"+otherPlayer.getUsername()+"�"+otherPlayer.getPosition().getX()+"�"+otherPlayer.getPosition().getY()+"�"+otherPlayer.getPosition().getZ();
				tradeInfo[2] = "Player 1";
				int c1 = 1;
				for (int i = 0; i < Inventory.SIZE; i++) {
					if(playerItems[i] != null){
						tradeInfo[2+c1] = playerItems[i].getId()+"�"+playerItems[i].getCount();
						c1++;
					}
				}
				tradeInfo[2+c1] = "Player 2";
				int c2 = 1;
				for (int i = 0; i < Inventory.SIZE; i++) {
					if(otherPlayerItems[i] != null){
						tradeInfo[2+c1+c2] = otherPlayerItems[i].getId()+"�"+otherPlayerItems[i].getCount();
						c2++;
					}
				}
				tradeInfo[2+c1+c2] = "END";
				Logger.logEvent(tradeInfo, "trades");
			}	
			player.getActionSender().removeInterfaces();
			otherPlayer.getActionSender().removeInterfaces();
			player.setTradingEntity(null);
	        otherPlayer.setTradingEntity(null);
	        player.save();
	        otherPlayer.save();
		}
	}

	private static void sendSecondScreen(Player player) {
		Player otherPlayer = (Player) player.getTradingEntity();
		StringBuilder trade = new StringBuilder();
		boolean empty = true;
		ArrayList<Item> listOfItems1 = new ArrayList<Item>();
		for (int i = 0; i < Inventory.SIZE; i++) {
			Item item = player.getTrade().get(i);
			if (item != null) {
				int idx = -1;
				for (int j = 0; j < listOfItems1.size(); j++) {
					if(listOfItems1.get(j).getId() == item.getId()){
						idx = j;
						break;
					}
				}
				if(idx == -1)
					listOfItems1.add(new Item(item.getId(), item.getCount()));
				else{
					int count = listOfItems1.get(idx).getCount()+item.getCount();
					listOfItems1.set(idx, new Item(item.getId(), count));
				}
			}
		}
		for (Item item : listOfItems1) {
		//for (int i = 0; i < Inventory.SIZE; i++) {
			//Item item = player.getTrade().get(i);
			String prefix = "";
			if (item != null) {
				empty = false;
				if (item.getCount() >= 1000 && item.getCount() < 1000000) {
					prefix = "@cya@" + item.getCount() / 1000 + "K @whi@(" + Misc.format(item.getCount()) + ")";
				} else if (item.getCount() >= 1000000) {
					prefix = "@gre@" + item.getCount() / 1000000 + " million @whi@(" + Misc.format(item.getCount()) + ")";
				} else {
					prefix = "" + item.getCount();
				}
				trade.append(item.getDefinition().getName());
				trade.append(" x ");
				trade.append(prefix);
				trade.append("\\n");
			}
		}
		if (empty) {
			trade.append("Absolutely nothing!");
		}
		player.getActionSender().sendString(trade.toString(), 3557);
		trade = new StringBuilder();
		empty = true;
		ArrayList<Item> listOfItems2 = new ArrayList<Item>();
		for (int i = 0; i < Inventory.SIZE; i++) {
			Item item = otherPlayer.getTrade().get(i);
			if (item != null) {
				int idx = -1;
				for (int j = 0; j < listOfItems2.size(); j++) {
					if(listOfItems2.get(j).getId() == item.getId()){
						idx = j;
						break;
					}
				}
				if(idx == -1)
					listOfItems2.add(new Item(item.getId(), item.getCount()));
				else{
					int count = listOfItems2.get(idx).getCount()+item.getCount();
					listOfItems2.set(idx, new Item(item.getId(), count));
				}
			}
		}
		if (otherPlayer == null) {
			return;
		}
		for (Item item : listOfItems2) {
		//for (int i = 0; i < Inventory.SIZE; i++) {
			//Item item = otherPlayer.getTrade().get(i);
			String prefix = "";
			if (item != null) {
				empty = false;
				if (item.getCount() >= 1000 && item.getCount() < 1000000) {
					prefix = "@cya@" + item.getCount() / 1000 + "K @whi@(" + Misc.format(item.getCount()) + ")";
				} else if (item.getCount() >= 1000000) {
					prefix = "@gre@" + item.getCount() / 1000000 + " million @whi@(" + Misc.format(item.getCount()) + ")";
				} else {
					prefix = "" + item.getCount();
				}
				trade.append(item.getDefinition().getName());
				trade.append(" x ");
				trade.append(prefix);
				trade.append("\\n");
			}
		}
		if (empty) {
			trade.append("Absolutely nothing!");
		}
		player.getActionSender().sendString(trade.toString(), 3558);
	}

}
