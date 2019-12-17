package com.rs2.model.content.skills.crafting.LeatherMakingAction.impl;

import java.util.HashMap;

import com.rs2.model.content.skills.crafting.LeatherMakingAction.LeatherMaking;
import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 21/12/11 Time: 19:48 To change
 * this template use File | Settings | File Templates.
 */
public class NormalLeather extends LeatherMaking {

	public NormalLeather(final Player player, final int used, final int used2, final int result, final int amount, final int manualAmount, final int level, final double experience) {
		super(player, used, used2, result, amount, manualAmount, level, experience);
	}

	public static NormalLeather create(final Player player, int buttonId, int manualAmount) {
		final NormalLeatherMake normalLeatherMake = NormalLeatherMake.forId(buttonId);
		if (normalLeatherMake == null || (normalLeatherMake.getAmount() == 0 && manualAmount == 0))
			return null;
		return new NormalLeather(player, normalLeatherMake.getUsed(), normalLeatherMake.getUsed2(), normalLeatherMake.getResult(), normalLeatherMake.getAmount(), manualAmount, normalLeatherMake.getLevel(), normalLeatherMake.getExperience());
	}

	public static enum NormalLeatherMake {
		ARMOUR(8635, 1741, 1, 1129, 1, 14, 25), ARMOUR5(8634, 1741, 1, 1129, 5, 14, 25), ARMOUR10(8633, 1741, 1, 1129, 10, 14, 25), GLOVES(8638, 1741, 1, 1059, 1, 1, 13.8), GLOVES5(8637, 1741, 1, 1059, 5, 1, 13.8), GLOVES10(8636, 1741, 1, 1059, 10, 1, 13.8), BOOTS(8641, 1741, 1, 1061, 1, 7, 16.25), BOOTS5(8640, 1741, 1, 1061, 5, 7, 16.25), BOOTS10(8639, 1741, 1, 1061, 10, 7, 16.25), VAMB(8644, 1741, 1, 1063, 1, 11, 22), VAMB5(8643, 1741, 1, 1063, 5, 11, 22), VAMB10(8642, 1741, 1, 1063, 10, 11, 22), CHAPS(8647, 1741, 1, 1095, 1, 18, 27), CHAPS5(8646, 1741, 1, 1095, 5, 18, 27), CHAPS10(8645, 1741, 1, 1095, 10, 18, 27), COIF(8650, 1741, 1, 1169, 1, 38, 37), COIF5(8649, 1741, 1, 1169, 5, 38, 37), COIF10(8648, 1741, 1, 1169, 10, 38, 37), COWL(8653, 1741, 1, 1167, 1, 9, 18.5), COWL5(8652, 1741, 1, 1167, 5, 9, 18.5), COWL10(8651, 1741, 1, 1167, 10, 9, 18.5);

		private int buttonId;
		private int used;
		private int used2;
		private int result;
		private int amount;
		private int level;
		private double experience;

		public static HashMap<Integer, NormalLeatherMake> normalLeatherItems = new HashMap<Integer, NormalLeatherMake>();

		public static NormalLeatherMake forId(int id) {
			if (normalLeatherItems == null) {
				return null;
			}
			return normalLeatherItems.get(id);
		}

		static {
			for (NormalLeatherMake data : NormalLeatherMake.values()) {
				normalLeatherItems.put(data.buttonId, data);
			}
		}

		private NormalLeatherMake(int buttonId, int used, int used2, int result, int amount, int level, double experience) {
			this.buttonId = buttonId;
			this.used = used;
			this.used2 = used2;
			this.result = result;
			this.amount = amount;
			this.level = level;
			this.experience = experience;
		}

		public int getButtonId() {
			return buttonId;
		}

		public int getUsed() {
			return used;
		}

		public int getUsed2() {
			return used2;
		}

		public int getResult() {
			return result;
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

}
