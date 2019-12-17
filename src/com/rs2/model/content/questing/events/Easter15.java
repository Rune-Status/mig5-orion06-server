package com.rs2.model.content.questing.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.questing.SpecialEvent;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;
import com.rs2.util.clip.Region;

public class Easter15 extends SpecialEvent{
	
	public Easter15(int id, int type) {
		super(id, type);
	}
	
	public int eggsDropped = 0;
	
	boolean playerHasAtleastOneItem(final Player player){
		if(!player.getInventory().playerHasItem(QuestConstants.EASTER_EGG_1) && !player.getInventory().playerHasItem(QuestConstants.EASTER_EGG_2) &&
				!player.getInventory().playerHasItem(QuestConstants.EASTER_EGG_3) && !player.getInventory().playerHasItem(QuestConstants.EASTER_EGG_4) &&
				!player.getInventory().playerHasItem(QuestConstants.EASTER_EGG_5) && !player.getInventory().playerHasItem(QuestConstants.EASTER_EGG_6)){
			return false;
		}
		return true;
	}
	
	boolean playerHasAllItems(final Player player){
		if(player.getInventory().playerHasItem(QuestConstants.EASTER_EGG_1) && player.getInventory().playerHasItem(QuestConstants.EASTER_EGG_2) &&
				player.getInventory().playerHasItem(QuestConstants.EASTER_EGG_3) && player.getInventory().playerHasItem(QuestConstants.EASTER_EGG_4) &&
				player.getInventory().playerHasItem(QuestConstants.EASTER_EGG_5) && player.getInventory().playerHasItem(QuestConstants.EASTER_EGG_6)){
			return true;
		}
		return false;
	}
	
	public void initialize(){
		System.out.println("Easter 15 event started! (1st easter event) [CUSTOM]");
		spawnNpc(QuestConstants.EASTER_BUNNY, new Position(2965,3296,0), 1);
		final Tick timer1 = new Tick(500) {
            @Override
            public void execute() {
            	eggsDropped++;
            	for(int i = 0; i < 6; i++){
            		int egg2Drop = (QuestConstants.EASTER_EGG_1 + i);
            		int x = 2945+Misc.random_(350);
            		int y = 3145+Misc.random_(370);
            		boolean clippedTile = false;
            		if(Region.getClipping(x, y, 0) != 0)
            		clippedTile = true;
					while (clippedTile){
						x = 2945+Misc.random_(350);
						y = 3145+Misc.random_(370);
						if(Region.getClipping(x, y, 0) != 0)
							clippedTile = true;
						else
							clippedTile = false;
					}
            		Position pos2Drop = new Position(x,y,0);
            		GroundItem drop = new GroundItem(new Item(egg2Drop, 1), pos2Drop, false);
					GroundItemManager.getManager().dropItem(drop);
					//System.out.println("Egg spawned: "+egg2Drop+" "+pos2Drop);
            	}
            	System.out.println("Total egg sets dropped: "+eggsDropped);
            }
		};
        World.getTickManager().submit(timer1);
	}

	public void eventCompleted(final Player player) {
		player.holidayEventStage[this.getId()] = 1;
		player.getActionSender().sendMessage("Congratulations! Easter event Complete!");
		player.getInventory().addItemOrDrop(new Item(QuestConstants.NOTED_FINE_CLOTH, 5));
		player.getInventory().addItemOrDrop(new Item(QuestConstants.XP_LAMP, 3));
		player.getInventory().addItemOrDrop(new Item(QuestConstants.EASTER_EGG, 1));
	}

	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		/*if(stage == 1)
			return false;*/
		if(npcId == QuestConstants.EASTER_BUNNY){
			if(stage == 0){
				if(!playerHasAtleastOneItem(player)){
					if(chatId == 1){
						player.getDialogue().sendNpcChat("I'm hiding eggs all over the world.",
														"Have fun hunting them!",Dialogues.HAPPY);
						return true;
					}
					if(chatId == 2){
						player.getDialogue().sendNpcChat("If you manage to find all six, come back here",
														"and show me.", Dialogues.HAPPY);
						player.getDialogue().endDialogue();
						return true;
					}
				} else {
					if(chatId == 1){
						player.getDialogue().sendNpcChat("Have you found all six eggs yet?",Dialogues.HAPPY);
						if(playerHasAllItems(player))
							player.getDialogue().setNextChatId(3);
						return true;
					}
					if(chatId == 2){
						player.getDialogue().sendPlayerChat("No, not yet.",Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}
					if(chatId == 3){
						player.getDialogue().sendPlayerChat("Yes, I have them right here!", Dialogues.HAPPY);
						return true;
					}
					if(chatId == 4){
						player.getDialogue().sendNpcChat("Wonderful. Here's something I give to everyone",
														"who manages to get the task done.", Dialogues.HAPPY);
						return true;
					}
					if(chatId == 5){
						if(playerHasAllItems(player)){
							player.getDialogue().sendStatement("You hand over the eggs and get some stuff",
															"in return.");
							player.getInventory().removeItem(new Item(QuestConstants.EASTER_EGG_1, 1));
							player.getInventory().removeItem(new Item(QuestConstants.EASTER_EGG_2, 1));
							player.getInventory().removeItem(new Item(QuestConstants.EASTER_EGG_3, 1));
							player.getInventory().removeItem(new Item(QuestConstants.EASTER_EGG_4, 1));
							player.getInventory().removeItem(new Item(QuestConstants.EASTER_EGG_5, 1));
							player.getInventory().removeItem(new Item(QuestConstants.EASTER_EGG_6, 1));
							eventCompleted(player);
							return true;
						}
					}
				}
			}//end of stage 0
			if(stage == 1){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Have you found all six eggs yet?",Dialogues.HAPPY);
					if(playerHasAllItems(player))
						player.getDialogue().setNextChatId(3);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("No, not yet.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Yes, I have them right here!", Dialogues.HAPPY);
					return true;
				}
				if(chatId == 4){
					if(playerHasAllItems(player)){
						player.getDialogue().sendStatement("You hand over the eggs and get an easter egg",
															"in return from the bunny.");
						player.getInventory().removeItem(new Item(QuestConstants.EASTER_EGG_1, 1));
						player.getInventory().removeItem(new Item(QuestConstants.EASTER_EGG_2, 1));
						player.getInventory().removeItem(new Item(QuestConstants.EASTER_EGG_3, 1));
						player.getInventory().removeItem(new Item(QuestConstants.EASTER_EGG_4, 1));
						player.getInventory().removeItem(new Item(QuestConstants.EASTER_EGG_5, 1));
						player.getInventory().removeItem(new Item(QuestConstants.EASTER_EGG_6, 1));
						player.getInventory().addItemOrDrop(new Item(QuestConstants.EASTER_EGG, 1));
						player.getDialogue().endDialogue();
						return true;
					}
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("You can keep hunting the eggs and I'll",
													"give one of my easter eggs for each complete set",
													"you bring to me.", Dialogues.HAPPY);
					return true;
				}
			}//end of stage1
		}//end of easter bunny
		return false;
	}
	
}
