package com.rs2.model.content.skills.runecrafting;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.questing.QuestDefinition;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.content.skills.runecrafting.RunecraftAltars.Altar;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Runecrafting class
 */

public class Runecrafting {

	public final static int RuneEss = 1436;
	public final static int PureEss = 7936;
	public final Position[] RUNE_ESSENCE_LOCATIONS = {new Position(2899, 4818, 0), new Position(2910, 4832, 0), new Position(2898, 4844, 0), new Position(2921, 4845, 0), new Position(2924, 4818, 0),};

	public enum Runes {
		FIRE(554, 14, 7, 2482, 35), WATER(555, 5, 6, 2480, 19), AIR(556, 1, 5, 2478, 11), EARTH(557, 9, 6.5, 2481, 26), MIND(558, 2, 5.5, 2479, 14), BODY(559, 20, 7.5, 2483, 46), DEATH(560, 65, 10, 2488, -1), NATURE(561, 44, 9, 2486, 91), CHAOS(562, 35, 8.5, 2487, 74), LAW(563, 54, 9.5, 2485, -1), COSMIC(564, 27, 8, 2484, 59), BLOOD(565, 77, 10.5, 2490, -1), SOUL(566, 90, 11, 2489, -1);

		private int id;
		private int level;
		private double xp;
		private int altar;
		private int multiplier;

		private Runes(int id, int level, double xp, int altar, int multiplier) {
			this.id = id;
			this.level = level;
			this.xp = xp;
			this.altar = altar;
			this.multiplier = multiplier;
		}

		public int getId() {
			return id;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}

		public int getAltar() {
			return altar;
		}

		public int getMultiplier() {
			return multiplier;
		}

	}

	public static void craftRunes(Player player, Runes rune) {
		if (!Constants.RUNECRAFTING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if(player.questStage[14] != 1){
			QuestDefinition questDef = QuestDefinition.forId(14);
			String questName = questDef.getName();
			player.getActionSender().sendMessage("You need to complete "+questName+" to do this.");
			return;
		}
		if (!SkillHandler.hasRequiredLevel(player, Skill.RUNECRAFTING, rune.getLevel(), "craft this rune")) {
			return;
		}
		int runeAmount = player.getInventory().getItemAmount(RuneEss);
		int pureAmount = player.getInventory().getItemAmount(PureEss);
		double multiplier = rune.getMultiplier() < 0 ? 0 : player.getSkill().getPlayerLevel(Skill.RUNECRAFTING) / rune.getMultiplier();
		int realMultiplier = (int) Math.floor(multiplier) + 1;
		if (rune.getId() >= 560) {
			if (pureAmount < 1) {
				player.getActionSender().sendMessage("You need pure essence to make these kinds of runes.");
				return;
			}
			player.getInventory().removeItem(new Item(PureEss, pureAmount));
			player.getInventory().addItem(new Item(rune.getId(), pureAmount * realMultiplier));
			player.getSkill().addExp(Skill.RUNECRAFTING, rune.getXp() * pureAmount);
		} else {
			if (pureAmount < 1 && runeAmount < 1) {
				player.getActionSender().sendMessage("You need rune or pure essence to make these kinds of runes.");
				return;
			}
			for (int p = 0; p < 28; p++) {
				player.getInventory().removeItem(new Item(RuneEss, runeAmount));
				player.getInventory().removeItem(new Item(PureEss, pureAmount));
			}
			player.getInventory().addItem(new Item(rune.getId(), runeAmount * realMultiplier));
			player.getInventory().addItem(new Item(rune.getId(), pureAmount * realMultiplier));
			player.getSkill().addExp(Skill.RUNECRAFTING, rune.getXp() * runeAmount);
			player.getSkill().addExp(Skill.RUNECRAFTING, rune.getXp() * pureAmount);
		}
		player.getActionSender().sendSound(481, 1, 0);
		player.getUpdateFlags().sendAnimation(791);
		player.getUpdateFlags().sendHighGraphic(186);
	}

	public static void teleportRunecraft(final Player player, final Npc npc) {
		if(player.questStage[14] != 1){
			QuestDefinition questDef = QuestDefinition.forId(14);
			String questName = questDef.getName();
			player.getActionSender().sendMessage("You need to complete "+questName+" to do this.");
		}else{
			player.setRunecraftNpc(npc.getNpcId());
			npc.teleportPlayerRunecraft(player, 2911, 4832, 0, "Senventior disthine molenko!");
		}
	}

	public static boolean clickTalisman(Player player, int itemId) {
		final Altar altar = RunecraftAltars.getAltarByTalismanId(itemId);
		if (altar == null) {
			return false;
		}
		locate(player, altar.getRuinPosition().getX(), altar.getRuinPosition().getY());
		return true;
	}

	public static void locate(Player player, int xPos, int yPos) {
		String X = "";
		String Y = "";
		if (player.getPosition().getX() >= xPos) {
			X = "west";
		}
		if (player.getPosition().getY() > yPos) {
			Y = "South";
		}
		if (player.getPosition().getX() < xPos) {
			X = "east";
		}
		if (player.getPosition().getY() <= yPos) {
			Y = "North";
		}
		player.getActionSender().sendMessage("You feel a slight pull towards " + Y + "-" + X + "...");
	}

}