package com.rs2.model.content.skills.fletching;

import java.util.HashMap;
import java.util.Map;

import com.rs2.Constants;
import com.rs2.model.content.skills.Menus;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class BoltTipsMaking {

	public enum BoltData {

		OYSTER_PEARL(411, 46, 6, 41, 3), OYSTER_PEARLS(413, 46, 24, 41, 9), OPAL(1609, 45, 12, 11, 1.5);

		private int item1;
		private int item2;
		private int amount;
		private int level;
		private double experience;

		@SuppressWarnings("unused")
		private static Map<String, BoltData> arrow = new HashMap<String, BoltData>();

		public static BoltData forId(int itemUsed) {
			for (BoltData arrowData : BoltData.values()) {
				if (arrowData.item1 == itemUsed)
					return arrowData;
			}
			return null;
		}

		private BoltData(int item1, int item2, int amount, int level, double experience) {
			this.item1 = item1;
			this.item2 = item2;
			this.amount = amount;
			this.level = level;
			this.experience = experience;
		}

		public int getItem1() {
			return item1;
		}

		public int getItem2() {
			return item2;
		}

		public int getAmount() {
			return amount;
		}

		public int getLevel() {
			return level;
		}

		public double getExperience() {
			return experience;
		}
	}
	
	public static boolean handleItemOnItem(Player player, int itemUsed, int usedWith) {
		if (itemUsed == 1755 || usedWith == 1755) {
			int item2 = -1;
			if (itemUsed == 411 || usedWith == 411) {//normal pearl
				item2 = 411;
			} else if (itemUsed == 413 || usedWith == 413) {//3 pearls
				item2 = 413;
			}
			else if (itemUsed == 1609 || usedWith == 1609) {//opal
				item2 = 1609;
			}
			if(item2 == -1)
				return false;
			final BoltData arrowData = BoltData.forId(item2);
			if (arrowData == null)
				return false;
			if (!Constants.FLETCHING_ENABLED) {
				player.getActionSender().sendMessage("This skill is currently disabled.");
				return false;
			}
			if (player.getSkill().getLevel()[Skill.FLETCHING] < arrowData.getLevel()) {
				player.getDialogue().sendStatement("You need a fletching level of " + arrowData.getLevel() + " to do this");
				return true;
			}
			player.getInventory().removeItem(new Item(arrowData.getItem1(), 1));
			player.getInventory().addItem(new Item(arrowData.getItem2(), arrowData.getAmount()));
			player.getSkill().addExp(Skill.FLETCHING, arrowData.getExperience());
			return true;
		}
		return false;
	}

}
