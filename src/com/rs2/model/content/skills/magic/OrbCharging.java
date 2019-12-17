package com.rs2.model.content.skills.magic;

import com.rs2.Constants;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class OrbCharging {

	public static boolean chargeOrb(final Player player, int buttonId, final int amount) {
		int amount_ = amount;
		if(buttonId == 2799)
			amount_ = 1;
		if(buttonId == 2798)
			amount_ = 5;
		if(buttonId == 1747)
			amount_ = player.getInventory().getItemAmount(567);
		if (amount_ <= 0)
			return false;
		if (amount_ >= 28)
			amount_ = 28;
		final int realAmount = amount_-1;
		if (player.getStatedInterface() == "orb charge") {
		    player.getActionSender().removeInterfaces();
		    player.tempMagicSkill.initialize_continuousSpell();
			player.setSkilling(new CycleEvent() {
				int amnt = realAmount;
				@Override
				public void execute(CycleEventContainer container) {
					if (amnt == 0 || !player.tempMagicSkill.initialize_continuousSpell()) {
						container.stop();
						return;
					}
					amnt--;
				}
				@Override
				public void stop() {
					player.resetAnimation();
				}
			});
			CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 6);
			return true;
		}
		return false;
	}
	
}
