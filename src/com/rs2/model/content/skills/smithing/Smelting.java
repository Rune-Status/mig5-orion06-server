package com.rs2.model.content.skills.smithing;

import com.rs2.Constants;
import com.rs2.model.content.DailyTask;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class Smelting {

	private static final int[][][] smeltBars = {{{2349, 1, 6}, {436, 1}, {438, 1}}, {{2351, 15, 13}, {440, 1}}, {{2355, 20, 14}, {442, 1}}, {{2353, 30, 18}, {440, 1}, {453, 2}}, {{2893, 20, 8}, {2892, 1}, {453, 4}}, {{2357, 40, 23}, {444, 1}}, {{2365, 40, 23}, {446, 1}}, {{2359, 50, 30}, {447, 1}, {453, 4}}, {{2361, 70, 38}, {449, 1}, {453, 6}}, {{2363, 85, 50}, {451, 1}, {453, 8}}};

	public static final int[][] smeltButtons = {{3987, 2349, 1}, {3986, 2349, 5}, {2807, 2349, 10}, {2414, 2349, 28}, {3991, 2351, 1}, {3990, 2351, 5}, {3989, 2351, 10}, {3988, 2351, 28}, {3995, 2355, 1}, {3994, 2355, 5}, {3993, 2355, 10}, {3992, 2355, 28}, {3999, 2353, 1}, {3998, 2353, 5}, {3997, 2353, 10}, {3996, 2353, 28}, {4003, 2357, 1}, {4002, 2357, 5}, {4001, 2357, 10}, {4000, 2357, 28}, {7441, 2359, 1}, {7440, 2359, 5}, {6397, 2359, 10}, {4158, 2359, 28}, {7446, 2361, 1}, {7444, 2361, 5}, {7443, 2361, 10}, {7442, 2361, 28}, {7450, 2363, 1}, {7449, 2363, 5}, {7448, 2363, 10}, {7447, 2363, 28}};

	public static void oreOnFurnace(Player player, int itemUsed) {
		for (int[][] smeltBar : smeltBars) {
			if (itemUsed == smeltBar[1][0] || smeltBar.length > 2 && itemUsed == smeltBar[2][0]) {
				player.setOldItem(smeltBar[0][0]);
				smeltBar(player, 1);
				break;
			}
		}
	}

	public static void smeltInterface(Player player) {
		if (!Constants.SMITHING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		player.getActionSender().sendItemOnInterface(2405, 150, 2349);
		player.getActionSender().sendItemOnInterface(2406, 150, 2351);
		player.getActionSender().sendItemOnInterface(2407, 150, 2355);
		player.getActionSender().sendItemOnInterface(2409, 150, 2353);
		player.getActionSender().sendItemOnInterface(2410, 150, 2357);
		player.getActionSender().sendItemOnInterface(2411, 150, 2359);
		player.getActionSender().sendItemOnInterface(2412, 150, 2361);
		player.getActionSender().sendItemOnInterface(2413, 150, 2363);
		player.getActionSender().sendChatInterface(2400);
	}

	public static boolean handleSmelting(final Player player, final int buttonId ,final int amount) {
		for (int[] element : smeltButtons) {
			if (buttonId == element[0]) {
				player.setOldItem(element[1]);
                if (element[2] == 28 && amount < 1){
                    player.getActionSender().openXInterface(buttonId);
				    return true;
                }
				smeltBar(player, amount < 1 ? element[2] : amount);
				return true;
			}
		}
		return false;
	}

	public static boolean smeltBar(final Player player, final int amount) {
		player.getActionSender().removeInterfaces();
		final int bar = player.getOldItem();
		for (int id = 0; id < smeltBars.length; id++) {
			if (bar == smeltBars[id][0][0]) {
				if (!Constants.SMITHING_ENABLED) {
					player.getActionSender().sendMessage("This skill is currently disabled.");
					return false;
				}
				final int level = smeltBars[id][0][1];
				final int xp = smeltBars[id][0][2];
				final int ore1 = smeltBars[id][1][0];
				final int ore1Amount = smeltBars[id][1][1];
				int ore2 = 0;
				int ore2Amount = 0;
				if (smeltBars[id].length > 2) {
					ore2 = smeltBars[id][2][0];
					ore2Amount = smeltBars[id][2][1];
				}
				if (!SkillHandler.hasRequiredLevel(player, Skill.SMITHING, level, "smelt this bar")) {
					return true;
				}
				final int task = player.getTask();
				final Item firstOre = new Item(ore1, ore1Amount);
				final Item secondOre = new Item(ore2, ore2Amount);
				final Item finalBar = new Item(bar);
				final boolean needSecondOre = ore2 > 0;
				if (!player.getInventory().playerHasItem(firstOre) || (needSecondOre && !player.getInventory().playerHasItem(secondOre))) {
					player.getActionSender().sendMessage("You have run out of ore to smith!");
					return true;
				}
				if (player.questStage[0] != 1) {
					player.getDialogue().sendStatement("You smelt the " + firstOre.getDefinition().getName().toLowerCase() + " " + (needSecondOre ? "and " + secondOre.getDefinition().getName().toLowerCase() + " together " : "") + "in the furnace.");
					player.setClickId(0);
				} else {
					if(finalBar.getId() == 2365)
						player.getActionSender().sendMessage("You place a lump of gold in the furnace.");
					else
						player.getActionSender().sendMessage("You smelt the " + firstOre.getDefinition().getName().toLowerCase() + " " + (needSecondOre ? "and " + secondOre.getDefinition().getName().toLowerCase() + " together " : "") + "in the furnace.");
				}
				player.getUpdateFlags().sendAnimation(899);
				player.getActionSender().sendSound(469, 1, 0);
				player.setSkilling(new CycleEvent() {
					int count = amount;
					@Override
					public void execute(CycleEventContainer b) {
						if (!player.checkTask(task)) {
							b.stop();
							return;
						}
						if (!player.getInventory().playerHasItem(firstOre) || (needSecondOre && !player.getInventory().playerHasItem(secondOre))) {
							player.getActionSender().sendMessage("You have run out of ore to smith!");
							b.stop();
							return;
						}
						player.getInventory().removeItem(firstOre);
						if (needSecondOre) {
							player.getInventory().removeItem(secondOre);
						}
						if (bar == 2351 && Misc.random(100) > 50 && player.getEquipment().getId(Constants.RING) != 2568) {
							player.getActionSender().sendMessage("You unsuccessfuly smelt the iron ore.");
						} else {
							if (bar == 2351 && player.getEquipment().getId(Constants.RING) == 2568) {
								player.setRingOfForgingLife(player.getRingOfForgingLife() - 1);
								if (player.getRingOfForgingLife() < 1) {
									player.getEquipment().removeAmount(Constants.RING, 1);
									player.getActionSender().sendMessage("Your ring of forging shatters!");
									player.setRingOfForgingLife(140);
								}
							}
							player.getInventory().addItem(finalBar);
							DailyTask task = player.getDailyTask();
							if(task.getTaskTarget() == finalBar.getId() && player.dailyTaskAmount < task.getReqAmount()){
								player.dailyTaskAmount++;
								if(player.dailyTaskAmount == task.getReqAmount())
									task.completed(player);
							}
							if(finalBar.getId() == 2357 && player.getEquipment().getId(Constants.HANDS) == 776)
								player.getSkill().addExp(Skill.SMITHING, 56.2);
							else
								player.getSkill().addExp(Skill.SMITHING, xp);
							if (player.questStage[0] != 1) {
								if (player.questStage[0] == 35){
									player.setNextTutorialStage();
								}
								player.setClickId(0);
								player.getDialogue().sendStatement("You retrieve a " + finalBar.getDefinition().getName().toLowerCase() + " from the furnace.");
								player.getQuestHandler().setTutorialStage();
							} else {
								if(finalBar.getId() == 2365)
									player.getActionSender().sendMessage("You retrieve a bar of gold from the furnace.");
								else
									player.getActionSender().sendMessage("You retrieve a " + finalBar.getDefinition().getName().toLowerCase() + " from the furnace.");
							    if(count > 1)
							    	player.getActionSender().sendMessage("You smelt the " + firstOre.getDefinition().getName().toLowerCase() + " " + (needSecondOre ? "and " + secondOre.getDefinition().getName().toLowerCase() + " together " : "") + "in the furnace.");
                            }
						}
						count--;
						if (count < 1) {
							b.stop();
							return;
						}
						if (!player.getInventory().playerHasItem(firstOre) || (needSecondOre && !player.getInventory().playerHasItem(secondOre))) {
							player.getActionSender().sendMessage("You have run out of ore to smith!");
							b.stop();
							return;
						}
						player.getActionSender().sendSound(469, 1, 0);
						player.getUpdateFlags().sendAnimation(899);
					}
					@Override
					public void stop() {
						player.getTask();
						player.resetAnimation();
					}
				});
				CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 4);
				return true;
			}
		}
		return false;
	}
}
