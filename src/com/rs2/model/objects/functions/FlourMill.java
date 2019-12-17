package com.rs2.model.objects.functions;

import com.rs2.GlobalVariables;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class FlourMill {

	public static int FLOUR_BIN_CONFIG = 695;
	
	public static void putFlourInHopper(Player player) {
		int amount = player.configValue[FLOUR_BIN_CONFIG] + player.getGrainInHopper();
		if (amount > 30) {
			player.getActionSender().sendMessage("The hopper or bin is already full.");
			return;
		}
		if (!player.getInventory().playerHasItem(1947)) {
			return;
		}
		player.getInventory().removeItem(new Item(1947, 1));
		player.getUpdateFlags().sendAnimation(832);
		player.setGrainInHopper(player.getGrainInHopper() + 1);
		player.getActionSender().sendMessage("You place the grain into the hopper.");
		if (player.getGrainInHopper() < 30) {
			player.getActionSender().sendMessage("The hopper is now holding " + player.getGrainInHopper() + " pieces of grain.");
		} else {
			player.getActionSender().sendMessage("The hopper is now full with 30 pieces of grain.");
		}
	}

	public static void operateHopper(Player player) {
		if (player.getGrainInHopper() < 1) {
			player.getActionSender().sendMessage("There is no grain in the hopper.");
			return;
		}
		if (player.configValue[FLOUR_BIN_CONFIG] > 29) {
			player.getActionSender().sendMessage("The grain bin is already full.");
			return;
		}
		int amount = player.configValue[FLOUR_BIN_CONFIG] + player.getGrainInHopper();
		player.getActionSender().sendConfig(FLOUR_BIN_CONFIG, amount);
		player.configValue[FLOUR_BIN_CONFIG] = amount;
		player.setGrainInHopper(0);
		player.getUpdateFlags().sendAnimation(832);
		player.getActionSender().sendMessage("The grain in the hopper slides down the chute.");
	}

	public static void takeFromBin(Player player) {
		if (player.configValue[FLOUR_BIN_CONFIG] < 1) {
			player.getActionSender().sendMessage("There is no grain in the bin.");
			return;
		}
		if (!player.getInventory().playerHasItem(1931)) {
			player.getActionSender().sendMessage("You need an empty pot in order to take flour from here.");
			return;
		}
		if(player.configValue[FLOUR_BIN_CONFIG] > 30)
			return;
		player.getUpdateFlags().sendAnimation(832);
		player.getInventory().removeItem(new Item(1931, 1));
		player.getInventory().addItem(new Item(1933, 1));
		player.configValue[FLOUR_BIN_CONFIG] -= 1;
		player.getActionSender().sendConfig(FLOUR_BIN_CONFIG, player.configValue[FLOUR_BIN_CONFIG]);
		player.getActionSender().sendMessage("You take some flour from the bin.");
		if (player.configValue[FLOUR_BIN_CONFIG] > 0) {
			player.getActionSender().sendMessage("There is enough grain left in the bin to fill " + player.configValue[FLOUR_BIN_CONFIG] + " pot" + (player.configValue[FLOUR_BIN_CONFIG] > 1 ? "s" : "") + ".");
		} else {
			player.getActionSender().sendMessage("The grain bin is now empty.");
		}
	}
}
