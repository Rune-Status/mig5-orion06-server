package com.rs2.model.content.skills.runecrafting;

import com.rs2.Constants;
import com.rs2.model.content.questing.QuestDefinition;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.runecrafting.RunecraftAltars.Altar;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class Tiaras {

	public static boolean bindTiara(Player player, int itemId, int objectId) {
		final Altar altar = RunecraftAltars.getAltarByAltarId(itemId, objectId);
		if (altar == null) {
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
		if (player.getInventory().playerHasItem(5525)) {
			player.getInventory().removeItem(new Item(5525, 1));
			player.getInventory().removeItem(new Item(altar.getTalisman(), 1));
			player.getInventory().addItem(new Item(altar.getTiara(), 1));
			player.getSkill().addExp(Skill.RUNECRAFTING, altar.getTiaraXp());
			player.getActionSender().sendMessage("You bind the power of the talisman into the tiara.");
		}
		return true;
	}

	public static void handleTiara(Player player, int id) {
		//System.out.println(id);
		int[][] tiaras = {{5527, 1}, {5529, 2}, {5531, 4}, {5535, 8}, {5537, 16}, {5533, 32}, {5539, 64}, {5543, 512}, {5541, 256}, {5545, 128}, {5547, 1024}, {5551, 2048}, {5549, 4096}};
		for (int[] t : tiaras) {
			if (t[0] == id) {
				player.getActionSender().sendConfig(491, t[1]);
				//System.out.println(t[1]);
				return;
			}
		}
		player.getActionSender().sendConfig(491, 0);
	}

}
