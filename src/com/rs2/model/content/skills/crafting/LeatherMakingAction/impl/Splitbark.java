package com.rs2.model.content.skills.crafting.LeatherMakingAction.impl;

import java.util.HashMap;

import com.rs2.model.content.skills.crafting.LeatherMakingAction.LeatherMaking;
import com.rs2.model.content.skills.crafting.LeatherMakingAction.SplitbarkMaking;
import com.rs2.model.players.Player;

public class Splitbark extends SplitbarkMaking {

	public Splitbark(final Player player, final int result, final int amount, final int manualAmount, final int req1, final int req2, final int req3) {
		super(player, result, amount, manualAmount, req1, req2, req3);
	}

	public static Splitbark create(final Player player, int buttonId, int manualAmount) {
		final SplitbarkMake splitbarkMake = SplitbarkMake.forId(buttonId);
		if (splitbarkMake == null || (splitbarkMake.getAmount() == 0 && manualAmount == 0))
			return null;
		return new Splitbark(player, splitbarkMake.getResult(), splitbarkMake.getAmount(), manualAmount, splitbarkMake.getReq1(), splitbarkMake.getReq1(), splitbarkMake.getReq3());
	}

	public static enum SplitbarkMake {
		HELM(8949, 3385, 1, 2, 2, 6000), HELM5(8948, 3385, 5, 2, 2, 6000), HELM10(8947, 3385, 10, 2, 2, 6000), HELMX(8946, 3385, 0, 2, 2, 6000), BODY(8953, 3387, 1, 4, 4, 37000), BODY5(8952, 3387, 5, 4, 4, 37000), BODY10(8951, 3387, 10, 4, 4, 37000), BODYX(8950, 3387, 0, 4, 4, 37000), LEGS(8957, 3389, 1, 3, 3, 32000), LEGS5(8956, 3389, 5, 3, 3, 32000), LEGS10(8955, 3389, 10, 3, 3, 32000), LEGSX(8954, 3389, 0, 3, 3, 32000), GAUNTLETS(8961, 3391, 1, 1, 1, 1000), GAUNTLETS5(8960, 3391, 5, 1, 1, 1000), GAUNTLETS10(8959, 3391, 10, 1, 1, 1000), GAUNTLETSX(8958, 3391, 0, 1, 1, 1000), BOOTS(8965, 3393, 1, 1, 1, 1000), BOOTS5(8964, 3393, 5, 1, 1, 1000), BOOTS10(8963, 3393, 10, 1, 1, 1000), BOOTSX(8962, 3393, 0, 1, 1, 1000);

		private int buttonId;
		private int result;
		private int amount;
		private int req1;
		private int req2;
		private int req3;

		public static HashMap<Integer, SplitbarkMake> splitbarkItems = new HashMap<Integer, SplitbarkMake>();

		public static SplitbarkMake forId(int id) {
			if (splitbarkItems == null) {
				return null;
			}
			return splitbarkItems.get(id);
		}

		static {
			for (SplitbarkMake data : SplitbarkMake.values()) {
				splitbarkItems.put(data.buttonId, data);
			}
		}

		private SplitbarkMake(int buttonId, int result, int amount, int req1, int req2, int req3) {
			this.buttonId = buttonId;
			this.result = result;
			this.amount = amount;
			this.req1 = req1;
			this.req2 = req2;
			this.req3 = req3;
		}

		public int getButtonId() {
			return buttonId;
		}

		public int getResult() {
			return result;
		}
		
		public int getReq1() {
			return req1;
		}
		
		public int getReq2() {
			return req2;
		}
		
		public int getReq3() {
			return req3;
		}

		public int getAmount() {
			return amount;
		}

	}

}
