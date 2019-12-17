package com.rs2.model.content;

import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.skills.magic.SpellBook;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class OperateItem {

	public static void operate(Player player, int slot){
		Item item = player.getEquipment().getItemContainer().get(player.getSlot());
		if (item == null)
			return;
		player.setClickItem(item.getId());
		switch(item.getId()){
		 case 2552 : // ring of duelling
	        case 2554 :
	        case 2556 :
	        case 2558 :
	        case 2560 :
	        case 2562 :
	        case 2564 :
	        case 2566 :
	        	player.setStatedInterface("operate");
        		Dialogues.startDialogue(player, 10004);
	        	break;
            case 1712 : // glory
            case 1710 :
            case 1708 :
            case 1706 :
            	player.setStatedInterface("operate");
        		Dialogues.startDialogue(player, 10003);
            	break;
            case 3853 ://games necklace
        	case 3855 :
        	case 3857 :
        	case 3859 :
        	case 3861 :
        	case 3863 :
        	case 3865 :
        	case 3867 :
        		player.setStatedInterface("operate");
        		Dialogues.startDialogue(player, 10002);
            	break;
            	
        	case 8118 ://book of the dead
        		if(player.getMagicBookType() != SpellBook.NECROMANCY){
        			player.tempMagicBookType = player.getMagicBookType();
        			player.getActionSender().sendSidebarInterface(6, 19104);
        			player.setMagicBookType(SpellBook.NECROMANCY);
        			player.getActionSender().sendFrame106(6);
        		}else{
        			player.setMagicBookType(player.tempMagicBookType);
        			if(player.getMagicBookType() == SpellBook.MODERN)
        				player.getActionSender().sendSidebarInterface(6, 1151);
        			if(player.getMagicBookType() == SpellBook.ANCIENT)
        				player.getActionSender().sendSidebarInterface(6, 12855);
        			player.getActionSender().sendFrame106(6);
        		}
            	break;	
		}
	}
	
}
