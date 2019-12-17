package com.rs2.model.content.skills.crafting.LeatherMakingAction;

import com.rs2.Constants;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.crafting.LeatherMakingHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public abstract class SplitbarkMaking {

	public static final int FINE_CLOTH = 3470;
	public static final int BARK = 3239;
	public static final int COINS = 995;

	protected Player player;

	protected int result;

	protected int amount;
	
	protected int req1;
	protected int req2;
	protected int req3;

	protected int manualAmount;

	protected SplitbarkMaking(Player player, int result, int amount, int manualAmount, int req1, int req2, int req3) {
		this.player = player;
		this.result = result;
		this.manualAmount = manualAmount;
		this.amount = amount;
		this.req1 = req1;
		this.req2 = req2;
		this.req3 = req3;
	}

	public boolean makeSplitbarkAction() {
		int actionAmount = amount != 0 ? amount : manualAmount;
		if (!player.getInventory().playerHasItem(new Item(FINE_CLOTH, actionAmount*req1))) {
			player.getDialogue().sendStatement("You need "+actionAmount*req1+" " + new Item(FINE_CLOTH).getDefinition().getName().toLowerCase() + " to do this.");
			return true;
		}
		if (!player.getInventory().playerHasItem(new Item(BARK, actionAmount*req2))) {
			player.getDialogue().sendStatement("You need "+actionAmount*req2+" " + new Item(BARK).getDefinition().getName().toLowerCase() + " to do this.");
			return true;
		}
		if (!player.getInventory().playerHasItem(new Item(COINS, actionAmount*req3))) {
			player.getDialogue().sendStatement("You need "+actionAmount*req3+" " + new Item(COINS).getDefinition().getName().toLowerCase() + " to do this.");
			return true;
		}
		player.getInventory().removeItem(new Item(FINE_CLOTH, actionAmount*req1));
		player.getInventory().removeItem(new Item(BARK, actionAmount*req2));
		player.getInventory().removeItem(new Item(COINS, actionAmount*req3));
		player.getInventory().addItem(new Item(result, actionAmount));
		Dialogues.sendDialogue(player, 1263, 6, 0);
		player.getActionSender().sendString("What would you like to make?", 8966);//resets a string to default value
		return true;
	}

}
