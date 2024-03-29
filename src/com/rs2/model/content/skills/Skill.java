package com.rs2.model.content.skills;

import com.rs2.Constants;
import com.rs2.model.content.skills.prayer.Prayer;
import com.rs2.model.players.Player;

public class Skill {

	private Player player;

	public static final int SKILL_COUNT = 22;
	public static final double MAXIMUM_EXP = 200000000;

	private int[] level = new int[SKILL_COUNT];
	private double[] exp = new double[SKILL_COUNT];

	public int skillRenewalTimer = 100;
	public int hitpointRenewalTimer = 100;
    
    private static final int[] EXPERIENCE_BY_LEVEL;
    
    static {
        EXPERIENCE_BY_LEVEL = new int[100];
        int points = 0, output = 0;
        for (int lvl = 1; lvl <= 99; lvl++) {
            points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            output = (int) Math.floor(points / 4);
            EXPERIENCE_BY_LEVEL[lvl] = output;
        }
    }

	public static final String[] SKILL_NAME = {"Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting"};

	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2, HITPOINTS = 3, RANGED = 4, PRAYER = 5, MAGIC = 6, COOKING = 7, WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11, CRAFTING = 12, SMITHING = 13, MINING = 14, HERBLORE = 15, AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19, RUNECRAFTING = 20;

	private long lastAction = -10000;
	private long lastAction2 = -10000;

	public Skill(Player player) {
		this.player = player;
		for (int i = 0; i < level.length; i++) {
			if (i == 3) {
				level[i] = 10;
				exp[i] = 1154;
			} else {
				level[i] = 1;
				exp[i] = 0;
			}
		}
	}

	public void skillTick() {
		if (skillRenewalTimer <= 0) {
			for (int i = 0; i < SKILL_COUNT; i++) {
				if (level[i] != getLevelForXP(getExp()[i])) {
					if (level[i] > getLevelForXP(getExp()[i])) {
						level[i] -= 1;
					} else if (i != 5) {
						level[i] += 1;
					}
					refresh(i);
				}
			}
			skillRenewalTimer = 100;
			return;
		}
		if (skillRenewalTimer == 50) {
			if (player.getSpecialAmount() < 100) {
				player.setSpecialAmount(player.getSpecialAmount() + 10);
				player.updateSpecialBar();
			}
			if (player.getIsUsingPrayer()[Prayer.RAPID_RESTORE]) {
				for (int i = 0; i < SKILL_COUNT; i++) {
					if (level[i] != getLevelForXP(getExp()[i])) {
						if (level[i] > getLevelForXP(getExp()[i])) {
							level[i] -= 1;
						} else if (i != 5) {
							level[i] += 1;
						}
						refresh(i);
					}
				}
			}
			if (player.getIsUsingPrayer()[Prayer.RAPID_HEAL]) {
				if (level[3] != getLevelForXP(getExp()[3])) {
					if (level[3] < getLevelForXP(getExp()[3])) {
						level[3] += 1;
					}
					refresh(3);
				}
			}
		}
		skillRenewalTimer--;
	}

	public int[][] CHAT_INTERFACES = {{ATTACK, 6247, 0, 0}, {DEFENCE, 6253, 0, 0}, {STRENGTH, 6206, 0, 0}, {HITPOINTS, 6216, 0, 0}, {RANGED, 4443, 5453, 6114}, {PRAYER, 6242, 0, 0}, {MAGIC, 6211, 0, 0}, {COOKING, 6226, 0, 0}, {WOODCUTTING, 4272, 0, 0}, {FLETCHING, 6231, 0, 0}, {FISHING, 6258, 0, 0}, {FIREMAKING, 4282, 0, 0}, {CRAFTING, 6263, 0, 0}, {SMITHING, 6221, 0, 0}, {MINING, 4416, 4417, 4438}, {HERBLORE, 6237, 0, 0}, {AGILITY, 4277, 0, 0}, {THIEVING, 4261, 4263, 4264}, {SLAYER, 12122, 0, 0}, {FARMING, 4887, 4889, 4890},
			{RUNECRAFTING, 4267, 0, 0},};

	public void sendSkillsOnLogin() {
		refresh();
	}

	public void refresh() {
		for (int i = 0; i < level.length; i++) {
			player.getActionSender().sendSkill(i, level[i], exp[i]);
		}
		player.setCombatLevel(calculateCombatLevel());
		player.setAppearanceUpdateRequired(true);
	}

	public void refresh(int skill) {
		player.getActionSender().sendSkill(skill, level[skill], exp[skill]);
		player.setCombatLevel(calculateCombatLevel());
		player.setAppearanceUpdateRequired(true);
	}

	public int getPlayerLevel(int skill) {
		return getLevelForXP(exp[skill]);
	}

	public int getLevelForXP(double exp) {
		for (int lvl = 1; lvl <= 99; lvl++) {
			if (EXPERIENCE_BY_LEVEL[lvl] > exp) {
				return lvl;
			}
		}
		return 99;
	}

	public int getXPForLevel(int level) {    
        if (level >= EXPERIENCE_BY_LEVEL.length)
            return Integer.MAX_VALUE;
		return EXPERIENCE_BY_LEVEL[level];
	}

	public int getTotalLevel() {
		int total = 0;
		for (int i = 0; i < SKILL_NAME.length; i++) {
			total += getPlayerLevel(i);
		}
		return total;
	}

	public int getTotalXp() {
		int total = 0;
		for (int i = 0; i < SKILL_NAME.length; i++) {
			total += getExp()[i];
		}
		return total;
	}
	
	public int getAddedXp(int skill, double xp){
        //xp *= Constants.EXP_RATE;
//		if(Constants.EASY_AEON){
		if (skill <= 6)
			xp *= Constants.COMBAT_XP;
		else
			xp *= Constants.SKILLING_XP;
//		} else {
//			xp *= getXpRate(skill);
//		}
//		if (player.getEnchantingChamber().isInEnchantingChamber() || player.getAlchemistPlayground().isInAlchemistPlayGround() || player.getCreatureGraveyard().isInCreatureGraveyard() || player.getTelekineticTheatre().isInTelekineticTheatre())
//			xp *= 0.75;
		int iXp = (int)xp;
		return iXp;
	}
	
	double mod = 1.02;
	
//	public double getXpRate(int skill){
//		double rate;
//		int lvl = getLevelForXP(exp[skill]);
//		if(lvl == 1)
//			rate = 1;
//		else{
//			double sub1 = Math.pow(mod, lvl);
//			double sub21 = Math.pow(lvl, 2);
//			double sub2 = sub21/3300;
//			rate = (sub1 + sub2);
//		}
//		return rate;
//	}
	
//	public double getXpRateForLvl(int lvl){
//		double rate;
//		if(lvl == 1)
//			rate = 1;
//		else{
//			double sub1 = Math.pow(mod, lvl);
//			double sub21 = Math.pow(lvl, 2);
//			double sub2 = sub21/3300;
//			rate = (sub1 + sub2);
//		}
//		return rate;
//	}
//	
//	public double getAverageXpRate(int skill){
//		double avg = 0;
//		int max = getLevelForXP(exp[skill]);
//		for(int i = 1; i <= max; i++){
//			avg += getXpRateForLvl(i);
//		}
//		return (avg/max);
//	}
//	
//	public double getAverageXpRateForLvl(int lvl){
//		double avg = 0;
//		int max = lvl;
//		for(int i = 1; i <= max; i++){
//			avg += getXpRateForLvl(i);
//		}
//		return (avg/max);
//	}

	public void addExp(int skill, double xp) {
		if (getLevel()[skill] >= 3 && player.questStage[0] != 1)
			return;
		if (xp <= 0) {
			return;
		}
		int oldLevel = getLevelForXP(exp[skill]);
        //xp *= Constants.EXP_RATE;
//		if(Constants.EASY_AEON){
		if (skill <= 6)
			xp *= Constants.COMBAT_XP;
		else
			xp *= Constants.SKILLING_XP;
//		} else {
//			xp *= getXpRate(skill);
//		}
		if (player.getEnchantingChamber().isInEnchantingChamber() || player.getAlchemistPlayground().isInAlchemistPlayGround() || player.getCreatureGraveyard().isInCreatureGraveyard() || player.getTelekineticTheatre().isInTelekineticTheatre())
			exp[skill] += xp * 0.75;
		else
			exp[skill] += xp;
		if (exp[skill] > MAXIMUM_EXP) {
			exp[skill] = MAXIMUM_EXP;
		}
		int newLevel = getLevelForXP(exp[skill]);
		int levelDiff = newLevel - oldLevel;
		if (levelDiff > 0) {
			level[skill] += levelDiff;
            if (skill > Skill.ATTACK && skill <= Skill.MAGIC) {
                player.setCombatLevel(calculateCombatLevel());
            }
            player.getUpdateFlags().sendGraphic(199);
			sendLevelUpMessage(skill);
		    player.setAppearanceUpdateRequired(true);
		}
		refresh(skill);
	}
	
	public void addQuestExp(int skill, double xp) {
		if (xp <= 0) {
			return;
		}
		int oldLevel = getLevelForXP(exp[skill]);
		if(Constants.EASY_AEON){
			xp *= Constants.QUEST_EXP_RATE;
		} else {
			xp *= 1.0;
		}
		exp[skill] += xp;
		if (exp[skill] > MAXIMUM_EXP) {
			exp[skill] = MAXIMUM_EXP;
		}
		int newLevel = getLevelForXP(exp[skill]);
		int levelDiff = newLevel - oldLevel;
		if (levelDiff > 0) {
			level[skill] += levelDiff;
            if (skill > Skill.ATTACK && skill <= Skill.MAGIC) {
                player.setCombatLevel(calculateCombatLevel());
            }
            player.getUpdateFlags().sendGraphic(199);
			sendLevelUpMessage(skill);
		    player.setAppearanceUpdateRequired(true);
		}
		refresh(skill);
	}
	
	private void sendLevelUpMessage(int skill) {
		int[][] data = {{0, 6248, 6249, 6247, 219, 233, 200, 400}, // ATTACK
				{1, 6254, 6255, 6253, 216, 210, 256, 256}, // DEFENCE
				{2, 6207, 6208, 6206, 208, 235, 220, 220}, // STRENGTH
				{3, 6217, 6218, 6216, 222, 212, 256, 200}, // HITPOINTS
				{4, 5453, 6114, 4443, 202, 224, 256, 256}, // RANGED
				{5, 6243, 6244, 6242, 211, 207, 256, 256}, // PRAYER
				{6, 6212, 6213, 6211, 215, 230, 256, 256}, // MAGIC
				{7, 6227, 6228, 6226, 217, 196, 180, 256}, // COOKING
				{8, 4273, 4274, 4272, 220, 209, 220, 256}, // WOODCUTTING
				{9, 6232, 6233, 6231, 205, 229, 256, 256}, // FLETCHING
				{10, 6259, 6260, 6258, 213, 226, 256, 256}, // FISHING
				{11, 4283, 4284, 4282, 126, 199, 256, 256}, // FIREMAKING
				{12, 6264, 6265, 6263, 214, 228, 300, 220}, // CRAFTING
				{13, 6222, 6223, 6221, 195, 206, 256, 256}, // SMITHING
				{14, 4417, 4438, 4416, 227, 223, 256, 256}, // MINING
				{15, 6238, 6239, 6237, 249, 218, 256, 256}, // HERBLORE
				{16, 4278, 4279, 4277, 232, 231, 256, 256}, // AGILITY
				{17, 4263, 4264, 4261, 236, 201, 200, 256}, // THIEVING
				{18, 12123, 12124, 12122, 275, 276, 380, 380}, // SLAYER
				{19, 313, 312, 310, 422, 424, 320, 175}, // FARMING
				{20, 4268, 4269, 4267, 194, 200, 320, 320}, // RUNECRAFTING
		};
		String[] name = {"Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting",};
		if (skill == data[skill][0]) {

			player.getActionSender().sendString("@dbl@Congratulations, you just advanced a " + name[skill] + " level!", data[skill][1]);
			player.getActionSender().sendString("Your " + name[skill] + " level is now " + getPlayerLevel(skill) + ".", data[skill][2]);
			player.getActionSender().sendMessage("You've just advanced a " + name[skill] + " level! You have reached level " + getPlayerLevel(skill) + ".");
			int i = (getPlayerLevel(skill) >= 50 ? 1 : 0);
			player.getActionSender().sendQuickSong(data[skill][4+i], data[skill][6+i]);
			if (skill == 19) {
				player.getActionSender().moveInterfaceComponent(311, 0, 30);
				player.getActionSender().sendItemOnInterface(311, 200, 5340);
			}
			/*if (skill == 19) {
				//player.getActionSender().sendItemOnInterface(12173, 200, 5340);
				// player.getActionSender().sendString("Check the Farming skill guide for information.",
				// 4890);
			}*/
			if (getLevelForXP(getExp()[skill]) == 99) {
				player.getActionSender().sendMessage("Well done! You've achieved the highest possible level in this skill!");
			}
			player.getActionSender().sendChatInterface(data[skill][3]);
			player.getDialogue().endDialogue();
		}
		// c.getCombat().resetPlayerAttack();
		player.setAppearanceUpdateRequired(true);
		player.getActionSender().sendString("Total Lvl: " + getTotalLevel(), 3984);
	}

	public int calculateCombatLevel() {
		final int attack = getLevelForXP(exp[ATTACK]);
		final int defence = getLevelForXP(exp[DEFENCE]);
		final int strength = getLevelForXP(exp[STRENGTH]);
		final int hp = getLevelForXP(exp[HITPOINTS]);
		final int prayer = getLevelForXP(exp[PRAYER]);
		final int ranged = getLevelForXP(exp[RANGED]);
		final int magic = getLevelForXP(exp[MAGIC]);
		double level = defence + hp + (prayer / 2);
		double magiclvl = (level + (1.3 * (1.5 * magic))) / 4;
		double rangelvl = (level + (1.3 * (1.5 * ranged))) / 4;
		double meleelvl = (level + (1.3 * (attack + strength))) / 4;
		if (meleelvl >= rangelvl && meleelvl >= magiclvl) {
			return (int) meleelvl;
		} else if (rangelvl >= meleelvl && rangelvl >= magiclvl) {
			return (int) rangelvl;
		} else {
			return (int) magiclvl;
		}
	}

	public void setLevel(int[] level) {
		this.level = level;
	}

	public int[] getLevel() {
		return level;
	}

	public void setExp(double[] exp) {
		this.exp = exp;
	}

	public double[] getExp() {
		return exp;
	}

	public void setSkillLevel(int skillId, int skillLevel) {
		level[skillId] = skillLevel;
	}

	public boolean canDoAction(int timer) {
		if (System.currentTimeMillis() >= lastAction) {
			lastAction = System.currentTimeMillis() + timer;
			return true;
		}
		return false;
	}

	public boolean canDoAction2(int timer) {
		if (System.currentTimeMillis() >= lastAction2) {
			lastAction2 = System.currentTimeMillis() + timer;
			return true;
		}
		return false;
	}

}
