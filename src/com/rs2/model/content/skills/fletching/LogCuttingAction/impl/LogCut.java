package com.rs2.model.content.skills.fletching.LogCuttingAction.impl;

import java.util.HashMap;

import com.rs2.model.content.skills.fletching.LogCuttingAction.LogCutting;
import com.rs2.model.players.Player;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 22/12/11 Time: 14:20 To change
 * this template use File | Settings | File Templates.
 * 
 */
public class LogCut extends LogCutting {

	public LogCut(final Player player, final int used, final int result, final int level, final double experience, final int amount, final int manualAmount) {
		super(player, used, result, level, experience, amount, manualAmount);
	}

	public static LogCut create(final Player player, int buttonId, int manualAmount) {
		final LogCutData logCutData = LogCutData.forId(buttonId);
		if (logCutData == null || (logCutData.getAmount() == 0 && manualAmount == 0))
			return null;
		return new LogCut(player, logCutData.getUsed(), logCutData.getResult(), logCutData.getLevel(), logCutData.getExperience(), logCutData.getAmount(), manualAmount);
	}

	public static enum LogCutData {
		SHAFT(8889, 1511, 52, 1, 1, 5), SHAFT5(8888, 1511, 52, 5, 1, 5), SHAFT10(8887, 1511, 52, 10, 1, 5), SHAFTX(8886, 1511, 52, 0, 1, 5), SHORTBOW(8893, 1511, 50, 1, 5, 5), SHORTBOW5(8892, 1511, 50, 5, 5, 5), SHORTBOW10(8891, 1511, 50, 10, 5, 5), SHORTBOWX(8890, 1511, 50, 0, 5, 5), LONGBOW(8897, 1511, 48, 1, 10, 10), LONGBOW5(8896, 1511, 48, 5, 10, 10), LONGBOW10(8895, 1511, 48, 10, 10, 10), LONGBOWX(8894, 1511, 48, 0, 10, 10);

		private int buttonId;
		private int used;
		private int result;
		private int amount;
		private int level;
		private double experience;

		public static HashMap<Integer, LogCutData> logCutItems = new HashMap<Integer, LogCutData>();

		public static LogCutData forId(int id) {
			return logCutItems.get(id);
		}

		static {
			for (LogCutData data : LogCutData.values()) {
				logCutItems.put(data.buttonId, data);
			}
		}

		private LogCutData(int buttonId, int used, int result, int amount, int level, double experience) {
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