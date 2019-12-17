package com.rs2.model.players;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.content.skills.Tools.Tool;
import com.rs2.model.objects.GameObject;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class MiscActions {

	
	static void mineWolfMountain(final Player player, final int object, final int obX, final int obY){
		final Tool pickaxe = Tools.getTool(player, Skill.MINING);
		if (pickaxe == null) {
			player.getActionSender().sendMessage("You do not have a pickaxe that you can use.");
			return;
		}
		if (!SkillHandler.hasRequiredLevel(player, Skill.MINING, 50, "mine here")) {
			return;
		}
		final int task = player.getTask();
		player.resetAnimation();
		player.tempMiscInt = 0;
		player.getUpdateFlags().sendAnimation(pickaxe.getAnimation());
		player.getActionSender().sendSound(432, 1, 0);
		player.setSkilling(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task)) {
					container.stop();
					return;
				}
				boolean finished = false;
				final GameObject p = ObjectHandler.getInstance().getObject(obX, obY, player.getPosition().getZ());
				if (p != null && p.getDef().getId() != object) {
					container.stop();
					return;
				}
                container.setTick(3);
                player.getUpdateFlags().sendAnimation(pickaxe.getAnimation());
                player.getActionSender().sendSound(432, 1, 0);
				if (player.tempMiscInt == 1) {
					int face = SkillHandler.getFace(object, obX, obY, player.getPosition().getZ());
					int type = SkillHandler.getType(object, obX, obY, player.getPosition().getZ());
					new GameObject(Constants.EMPTY_OBJECT, obX, obY, player.getPosition().getZ(), face, type == 11 ? 11 : 10, object, 1);
					player.getActionSender().walkTo(player.getPosition().getX() < 2840 ? 3 : -3, 0, true);
					container.stop();
					finished = true;
				}
				player.tempMiscInt++;
			}
			@Override
			public void stop() {
				player.getUpdateFlags().sendAnimation(-1);
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 3);
	}
	
	
}
