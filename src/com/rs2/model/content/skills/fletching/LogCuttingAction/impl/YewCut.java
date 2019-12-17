package com.rs2.model.content.skills.fletching.LogCuttingAction.impl;

import java.util.HashMap;

import com.rs2.model.content.skills.fletching.LogCuttingAction.LogCutting;
import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 22/12/11 Time: 14:21 To change
 * this template use File | Settings | File Templates.
 */
public class YewCut extends LogCutting {

	public YewCut(final Player player, final int used, final int result, final int level, final double experience, final int amount, final int manualAmount) {
		super(player, used, result, level, experience, amount, manualAmount);
	}

	public static YewCut create(final Player player, int buttonId, int manualAmount) {
		final YewCutData yewCutData = YewCutData.forId(buttonId);
		if (yewCutData == null || (yewCutData.getAmount() == 0 && manualAmount == 0))
			return null;
		return new YewCut(player, yewCutData.getUsed(), yewCutData.getResult(), yewCutData.getLevel(), yewCutData.getExperience(), yewCutData.getAmount(), manualAmount);
	}

	public static enum YewCutData {
		SHORTBOW(8874, 1515, 68, 1, 65, 68.5), SHORTBOW5(8873, 1515, 68, 5, 65, 68.5), SHORTBOW10(8872, 1515, 68, 10, 65, 68.5), SHORTBOWX(8871, 1515, 68, 0, 65, 68.5), LONGBOW(8878, 1515, 66, 1, 70, 75), LONGBOW5(8877, 1515, 66, 5, 70, 75), LONGBOW10(8876, 1515, 66, 10, 70, 75), LONGBOWX(8875, 1515, 66, 0, 70, 75);

		private int buttonId;
		private int used;
		private int result;
		private int amount;
		private int level;
		private double experience;

		public static HashMap<Integer, YewCutData> yewCutItems = new HashMap<Integer, YewCutData>();

		public static YewCutData forId(int id) {
			return yewCutItems.get(id);
		}

		static {
			for (YewCutData data : YewCutData.values()) {
				yewCutItems.put(data.buttonId, data);
			}
		}

		private YewCutData(int buttonId, int used, int result, int amount, int level, double experience) {
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
