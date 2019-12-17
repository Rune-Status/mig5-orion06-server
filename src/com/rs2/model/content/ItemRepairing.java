package com.rs2.model.content;

import com.rs2.model.players.Player;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.content.skills.Tools.Tool;
import com.rs2.model.content.dialogue.Dialogues;

public class ItemRepairing {

	private Player player;
	
	private int[] repairers = {519,//bob @lumbridge
							594//nurmof @dwarven mine
							};
	
	public ItemRepairing(Player player) {
		this.player = player;
	}
	
	public boolean handleItemOnNpc(int npcId, int itemId){
		int repairingNpc = -1;
		for(int i : repairers){
			if(npcId == i){
				repairingNpc = i;
				break;
			}
		}
		Tool broken = Tools.getBrokenTool(player, itemId);
		if(repairingNpc != -1 && broken != null){
			player.tempMiscInt = itemId;
			player.tempMiscInt2 = repairingNpc;
			Dialogues.startDialogue(player, 10088);
			return true;
		}
		return false;
	}
	
}
