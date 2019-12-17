package com.rs2.model.content.skills.mining;

import com.rs2.model.content.DailyTask;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.content.skills.Tools.Tool;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class MineEssence {

	public static void startMiningEss(final Player player) {
		final Tool pickaxe = Tools.getTool(player, Skill.MINING);
		if (pickaxe == null) {
			player.getActionSender().sendMessage("You do not have a pickaxe that you can use.");
			return;
		}
		if (player.getInventory().getItemContainer().freeSlots() <= 0) {
			player.getActionSender().sendMessage("Not enough space in your inventory.");
			player.getActionSender().sendSound(1878, 1, 0);
			return;
		}
		final int task = player.getTask();
		final int ess = player.getSkill().getPlayerLevel(Skill.MINING) < 30 ? 1436 : 7936;
		final int anim = pickaxe.getAnimation();
		player.getActionSender().sendMessage("You swing your pick at the rock.");
		player.getActionSender().sendSound(432, 1, 0);
		player.getUpdateFlags().sendAnimation(anim);
		player.setSkilling(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task)) {
					container.stop();
					return;
				}
				player.getActionSender().sendSound(432, 1, 0);
				player.getUpdateFlags().sendAnimation(anim);
				if (!player.getInventory().addItem(new Item(ess, 1))) {
					//player.getActionSender().sendMessage("Not enough space in your inventory.");
					container.stop();
				} else {
					player.getSkill().addExp(Skill.MINING, 5);
					DailyTask task = player.getDailyTask();
					if(task.getTaskTarget() == 1436 && player.dailyTaskAmount < task.getReqAmount()){
						player.dailyTaskAmount++;
						if(player.dailyTaskAmount == task.getReqAmount())
							player.getActionSender().sendMessage("Daily task completed! Talk to Skill Master to claim your reward.");
					}
				}
				if (player.getInventory().getItemContainer().freeSlots() <= 0) {
					player.getActionSender().sendMessage("Not enough space in your inventory.");
					player.getActionSender().sendSound(1878, 1, 0);
					player.getUpdateFlags().sendAnimation(-1);
					container.stop();
					return;
				}
			}
			@Override
			public void stop() {
				player.resetAnimation();
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), pickaxe.getEssSpeed());
	}

}
