package com.rs2.model.content.skills.runecrafting;

import com.rs2.Constants;
import com.rs2.model.content.questing.QuestDefinition;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;
import com.rs2.util.Misc;

public class MixingRunes {

	public final static int BindingNeck = 5521;
	
	public enum MixingRunesData {
		MIST_RUNES_AIR(1438, 556, 2480, 4695, 6, 8.5), MIST_RUNES_WATER(1444, 555, 2478, 4695, 6, 8), DUST_RUNES_AIR(1438, 556, 2481, 4696, 10, 9), DUST_RUNES_EARTH(1440, 557, 2478, 4696, 10, 8.3), MUD_RUNES_WATER(1444, 555, 2481, 4698, 13, 9.5), MUD_RUNES_EARTH(1440, 557, 2480, 4698, 13, 9.3), SMOKE_RUNES_AIR(1438, 556, 2482, 4697, 15, 9.5), SMOKE_RUNES_FIRE(1442, 554, 2478, 4697, 15, 8.5), STEAM_RUNES_WATER(1444, 555, 2482, 4694, 19, 10), STEAM_RUNES_FIRE(1442, 554, 2480, 4694, 19, 9.3), LAVA_RUNES_EARTH(1440, 557, 2482, 4699, 23, 10.5), LAVA_RUNES_FIRE(1442, 554, 2481, 4699, 23, 10), ;
		private int talismanId;
		private int elementalRuneId;
		private int altarId;
		private int combinedRune;
		private int levelRequired;
		private double experienceReceived;

		public static MixingRunesData forId(int itemUsed, int objectId) {
			for (MixingRunesData mixingRunesData : MixingRunesData.values()) {
				if (itemUsed == mixingRunesData.talismanId && objectId == mixingRunesData.altarId) {
					return mixingRunesData;
				}
			}
			return null;
		}
		MixingRunesData(int talismanId, int elementalRuneId, int altarId, int combinedRune, int levelRequired, double experienceReceived) {
			this.talismanId = talismanId;
			this.elementalRuneId = elementalRuneId;
			this.altarId = altarId;
			this.combinedRune = combinedRune;
			this.levelRequired = levelRequired;
			this.experienceReceived = experienceReceived;
		}

		public int getTalismanId() {
			return talismanId;
		}

		public int getElementalRuneId() {
			return elementalRuneId;
		}

		public int getCombinedRune() {
			return combinedRune;
		}

		public int getLevelRequired() {
			return levelRequired;
		}

		public double getExperienceReceived() {
			return experienceReceived;
		}
	}

	public static boolean combineRunes(Player player, int itemUsed, int objectId) {
		MixingRunesData mixingRunesData = MixingRunesData.forId(itemUsed, objectId);
		if (mixingRunesData == null) {
			return false;
		}
		if (!Constants.RUNECRAFTING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if(player.questStage[14] != 1){
			QuestDefinition questDef = QuestDefinition.forId(14);
			String questName = questDef.getName();
			player.getActionSender().sendMessage("You need to complete "+questName+" to do this.");
			return true;
		}
		if (!player.getInventory().playerHasItem(mixingRunesData.getTalismanId())) {
			player.getActionSender().sendMessage("You need a " + ItemManager.getInstance().getItemName(mixingRunesData.getTalismanId()).toLowerCase() + " to do this.");
			return true;
		}
		if (!player.getInventory().playerHasItem(mixingRunesData.getElementalRuneId()) || !player.getInventory().playerHasItem(Runecrafting.PureEss)) {
			player.getActionSender().sendMessage("You need pure essences to do this.");
			return true;
		}
		if (player.getSkill().getPlayerLevel(Skill.RUNECRAFTING) < mixingRunesData.getLevelRequired()) {
			player.getActionSender().sendMessage("You need a runecrafting level of " + mixingRunesData.getLevelRequired() + " to do this.");
			return true;
		}
		player.getInventory().removeItem(new Item(mixingRunesData.getTalismanId(), 1));
		player.getActionSender().sendMessage("You attempt to bind the Runes.");
		player.getActionSender().sendSound(481, 1, 0);
		player.getUpdateFlags().sendAnimation(791);
		player.getUpdateFlags().sendHighGraphic(186);
		int essAmount = player.getInventory().getItemAmount(Runecrafting.PureEss);
		int runeAmount = player.getInventory().getItemAmount(mixingRunesData.getElementalRuneId());
		int combineTimes = essAmount > runeAmount ? runeAmount : essAmount;
		int successfulCombines = 0;
		if(player.getEquipment().getId(Constants.AMULET) == BindingNeck){
			successfulCombines = combineTimes;
		} else {
			for(int i = 0; i < combineTimes; i++){
				if(Misc.random(1) == 0)
					successfulCombines++;
			}
		}
		player.getInventory().removeItem(new Item(mixingRunesData.getElementalRuneId(), combineTimes));
		player.getInventory().removeItem(new Item(Runecrafting.PureEss, combineTimes));
		player.getInventory().addItem(new Item(mixingRunesData.getCombinedRune(), successfulCombines));
		player.getSkill().addExp(Skill.RUNECRAFTING, mixingRunesData.getExperienceReceived() * successfulCombines);
		if (player.getEquipment().getId(Constants.AMULET) == BindingNeck) {
			player.setBindingNeckCharge(player.getBindingNeckCharge() - 1);
		}
		if (player.getBindingNeckCharge() <= 0) {
			player.setBindingNeckCharge(15);
			player.getEquipment().replaceEquipment(BindingNeck, Constants.AMULET);

			player.getActionSender().sendMessage("Your binding necklace crumble into dust.");
		}
		return true;
	}
}
