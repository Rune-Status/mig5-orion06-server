package com.rs2.model.content.skills.crafting;

import java.util.HashMap;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class StaffCrafting {
	
	public static int BATTLESTAFF = 1391;
	
	public static boolean handleCrafting(final Player player, int selectedItemId, int usedItemId, final int slot) {
		if (!(selectedItemId == BATTLESTAFF || usedItemId == BATTLESTAFF))
			return false;
		final int itemId = selectedItemId != BATTLESTAFF ? selectedItemId : usedItemId;
		final staffData staff = staffData.forId(itemId);
		if (staff != null) {
			if (!Constants.CRAFTING_ENABLED) {
				player.getActionSender().sendMessage("This skill is currently disabled.");
				return true;
			}
			if (!player.getInventory().getItemContainer().contains(BATTLESTAFF)) {
				return true;
			} else if (player.getSkill().getLevel()[Skill.CRAFTING] < staff.getLevel()) {
				player.getDialogue().sendStatement("You need a crafting level of " + staff.getLevel() + " to craft this staff.");// wrong
				return true;
			}
			if(player.getInventory().playerHasItem(BATTLESTAFF) && player.getInventory().playerHasItem(staff.getOrb())){
				player.getInventory().removeItem(new Item(BATTLESTAFF, 1));
				player.getInventory().removeItem(new Item(staff.getOrb(), 1));
				player.getInventory().addItem(new Item(staff.getBattlestaff(), 1));
				player.getSkill().addExp(Skill.CRAFTING, staff.getExperience());
			}
			return true;
		}
        return false;
	}
	
	public static enum staffData {// orb, battlestaff, lvl, xp
		AIR(573, 1397, 66, 137.5), WATER(571, 1395, 54, 100), EARTH(575, 1399, 58, 112.5), FIRE(569, 1393, 62, 125);

		private int orb;
		private int battlestaff;
		private byte level;
		private double experience;

		public static HashMap<Integer, staffData> craftingStaff = new HashMap<Integer, staffData>();

		public static staffData forId(int id) {
			return craftingStaff.get(id);
		}

		static {
			for (staffData c : staffData.values()) {
				craftingStaff.put(c.getOrb(), c);
			}
		}

		private staffData(int orb, int battlestaff, int level, double experience) {
			this.orb = orb;
			this.battlestaff = battlestaff;
			this.level = (byte) level;
			this.experience = experience;
		}

		public int getOrb() {
			return orb;
		}

		public int getBattlestaff() {
			return battlestaff;
		}

		public int getLevel() {
			return level;
		}

		public double getExperience() {
			return experience;
		}
	}
	
}
