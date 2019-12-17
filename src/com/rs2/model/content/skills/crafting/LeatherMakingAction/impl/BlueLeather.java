package com.rs2.model.content.skills.crafting.LeatherMakingAction.impl;

import java.util.HashMap;

import com.rs2.model.content.skills.crafting.LeatherMakingAction.LeatherMaking;
import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 21/12/11 Time: 19:48 To change
 * this template use File | Settings | File Templates.
 */
public class BlueLeather extends LeatherMaking {

	public BlueLeather(final Player player, final int used, final int used2, final int result, final int amount, final int manualAmount, final int level, final double experience) {
		super(player, used, used2, result, amount, manualAmount, level, experience);
	}

	public static BlueLeather create(final Player player, int buttonId, int manualAmount) {
		final BlueLeatherMake blueLeatherMake = BlueLeatherMake.forId(buttonId);
		if (blueLeatherMake == null || (blueLeatherMake.getAmount() == 0 && manualAmount == 0))
			return null;
		return new BlueLeather(player, blueLeatherMake.getUsed(), blueLeatherMake.getUsed2(), blueLeatherMake.getResult(), blueLeatherMake.getAmount(), manualAmount, blueLeatherMake.getLevel(), blueLeatherMake.getExperience());
	}

	public static enum BlueLeatherMake {
		VAMB(8889, 2505, 1, 2487, 1, 66, 70), VAMB5(8888, 2505, 1, 2487, 5, 66, 70), VAMB10(8887, 2505, 1, 2487, 10, 66, 70), VAMBX(8886, 2505, 1, 2487, 0, 66, 70), CHAPS(8893, 2505, 2, 2493, 1, 68, 140), CHAPS5(8892, 2505, 2, 2493, 5, 68, 140), CHAPS10(8891, 2505, 2, 2493, 10, 68, 140), CHAPSX(8890, 2505, 2, 2493, 0, 68, 140), BODY(8897, 2505, 3, 2499, 1, 71, 210), BODY5(8896, 2505, 3, 2499, 5, 71, 210), BODY10(8895, 2505, 3, 2499, 10, 71, 210), BODYX(8894, 2505, 3, 2499, 0, 71, 210);

		private int buttonId;
		private int used;
		private int used2;
		private int result;
		private int amount;
		private int level;
		private double experience;

		public static HashMap<Integer, BlueLeatherMake> blueLeatherItems = new HashMap<Integer, BlueLeatherMake>();

		public static BlueLeatherMake forId(int id) {
			if (blueLeatherItems == null) {
				return null;
			}
			return blueLeatherItems.get(id);
		}

		static {
			for (BlueLeatherMake data : BlueLeatherMake.values()) {
				blueLeatherItems.put(data.buttonId, data);
			}
		}

		private BlueLeatherMake(int buttonId, int used, int used2, int result, int amount, int level, double experience) {
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
