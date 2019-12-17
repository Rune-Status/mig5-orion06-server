package com.rs2.model.content.skills.fletching.LogCuttingAction.impl;

import java.util.HashMap;

import com.rs2.model.content.skills.fletching.LogCuttingAction.LogCutting;
import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 22/12/11 Time: 14:21 To change
 * this template use File | Settings | File Templates.
 */
public class WillowCut extends LogCutting {

	public WillowCut(final Player player, final int used, final int result, final int level, final double experience, final int amount, final int manualAmount) {
		super(player, used, result, level, experience, amount, manualAmount);
	}

	public static WillowCut create(final Player player, int buttonId, int manualAmount) {
		final WillowCutData willowCutData = WillowCutData.forId(buttonId);
		if (willowCutData == null || (willowCutData.getAmount() == 0 && manualAmount == 0))
			return null;
		return new WillowCut(player, willowCutData.getUsed(), willowCutData.getResult(), willowCutData.getLevel(), willowCutData.getExperience(), willowCutData.getAmount(), manualAmount);
	}

	public static enum WillowCutData {
		SHORTBOW(8874, 1519, 60, 1, 35, 33.3), SHORTBOW5(8873, 1519, 60, 5, 35, 33.3), SHORTBOW10(8872, 1519, 60, 10, 35, 33.3), SHORTBOWX(8871, 1519, 60, 0, 35, 33.3), LONGBOW(8878, 1519, 58, 1, 40, 41.5), LONGBOW5(8877, 1519, 58, 5, 40, 41.5), LONGBOW10(8876, 1519, 58, 10, 40, 41.5), LONGBOWX(8875, 1519, 58, 0, 40, 41.5);

		private int buttonId;
		private int used;
		private int result;
		private int amount;
		private int level;
		private double experience;

		public static HashMap<Integer, WillowCutData> willowCutItems = new HashMap<Integer, WillowCutData>();

		public static WillowCutData forId(int id) {
			return willowCutItems.get(id);
		}

		static {
			for (WillowCutData data : WillowCutData.values()) {
				willowCutItems.put(data.buttonId, data);
			}
		}

		private WillowCutData(int buttonId, int used, int result, int amount, int level, double experience) {
			this.buttonId = buttonId;
			this.used = used;
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
