package com.rs2.model.content.skills.crafting;

import java.util.HashMap;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class GemCutting {
	public static int CHISEL = 1755;

	public static boolean handleCutting(final Player player, int selectedItemId, int usedItemId, final int slot) {
		if (!(selectedItemId == CHISEL || usedItemId == CHISEL))
			return false;
		final int itemId = selectedItemId != CHISEL ? selectedItemId : usedItemId;
		final gemData gem = gemData.forId(itemId);
		if (gem != null) {
			if (!Constants.CRAFTING_ENABLED) {
				player.getActionSender().sendMessage("This skill is currently disabled.");
				return true;
			}
			if (!player.getInventory().getItemContainer().contains(CHISEL)) {
				return true;
			} else if (player.getSkill().getLevel()[Skill.CRAFTING] < gem.getLevel()) {
				player.getDialogue().sendStatement("You need a crafting level of " + gem.getLevel() + " to cut this gem.");// wrong
				return true;
			}
			player.getUpdateFlags().sendAnimation(gem.getAnimation());
			player.getActionSender().sendSound(464, 1, 0);
			if(player.getInventory().playerHasItem(CHISEL) && player.getInventory().playerHasItem(itemId)){
				player.getInventory().removeItem(new Item(itemId, 1));
				player.getInventory().addItem(new Item(gem.getGemId(), 1));
				player.getSkill().addExp(Skill.CRAFTING, gem.getExperience());
			}
			return true;
		}
        return false;
	}

	public static enum gemData {// uncut, gem, level, anim, experience
		SAPPHIRE(1623, 1607, 20, 888, 50), EMERALD(1621, 1605, 27, 889, 67), RUBY(1619, 1603, 34, 887, 85), DIAMOND(1617, 1601, 43, 886, 107.5), DRAGONSTONE(1631, 1615, 55, 885, 137.5), ONYX(6571, 6573, 67, 885, 168),
		OPAL(1625, 1609, 1, 891, 15), JADE(1627, 1611, 13, 890, 20), RED_TOPAZ(1629, 1613, 16, 887, 25);

		private short uncutId;
		private short gemId;
		private byte level;
		private short animId;
		private double experience;

		public static HashMap<Integer, gemData> craftinggems = new HashMap<Integer, gemData>();

		public static gemData forId(int id) {
			return craftinggems.get(id);
		}

		static {
			for (gemData c : gemData.values()) {
				craftinggems.put(c.getId(), c);
			}
		}

		private gemData(int uncutId, int gemId, int level, int animId, double experience) {
			this.uncutId = (short) uncutId;
			this.gemId = (short) gemId;
			this.level = (byte) level;
			this.animId = (short) animId;
			this.experience = experience;
		}

		public int getId() {
			return uncutId;
		}

		public int getGemId() {
			return gemId;
		}

		public int getLevel() {
			return level;
		}

		public int getAnimation() {
			return animId;
		}

		public double getExperience() {
			return experience;
		}
	}
}
