package com.rs2.model.content.questing.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.questing.SpecialEvent;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class Xmass14 extends SpecialEvent{
	
	public Xmass14(int id, int type) {
		super(id, type);
	}
	
	public void initialize(){
		System.out.println("Xmass 14 event started! (1st xmass event) [CUSTOM]");
		spawnNpc(QuestConstants.SANTA, new Position(3209,3435,0), 1);
		spawnNpc(QuestConstants.ELF_VARROCK_XMASS14, new Position(3237,3401,0), 1);
		spawnNpc(QuestConstants.ELF_LUMBRIDGE_XMASS14, new Position(3228,3209,0), 1);
		spawnNpc(QuestConstants.ELF_FALADOR_XMASS14, new Position(3042,3344,0), 1);
		spawnNpc(QuestConstants.GHOST_XMASS14, new Position(3018,3180,0), 2);
	}

	public void eventCompleted(final Player player) {
		player.holidayEventStage[this.getId()] = 1;
		player.getActionSender().sendMessage("Congratulations! Xmass event Complete!");
		player.getInventory().addItemOrDrop(new Item(QuestConstants.NOTED_FINE_CLOTH, 5));
		player.getInventory().addItemOrDrop(new Item(QuestConstants.XP_LAMP, 3));
		player.getInventory().addItemOrDrop(new Item(QuestConstants.YOYO, 1));
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 9565 && objectX == 3018 && objectY == 3182){//Ghost cell door
			player.getActionSender().sendMessage("It is locked.");
			return true;
		}
		return false;
	}
	
	public boolean getQuestDrop(final Player killer, int npcId, Position deathPos, int stage){
		if(stage < 2)
			return false;
		if(killer.hasItem(QuestConstants.GHOSTS_PRESENT))
			return false;
		if(npcId == QuestConstants.IMP){
			if((killer.holidayEventStage[this.getId()] & Misc.getActualValue(7)) != 0 && (killer.holidayEventStage[this.getId()] & Misc.getActualValue(9)) == 0){
				if(Misc.random_(5) == 0){
					GroundItem drop = new GroundItem(new Item(QuestConstants.GHOSTS_PRESENT, 1), killer, deathPos);
					GroundItemManager.getManager().dropItem(drop);
					killer.getDialogue().sendPlayerChat("That is probably the present I'm looking for.", Dialogues.CONTENT);
					killer.getDialogue().endDialogue();
					return true;
				} else {
					killer.getDialogue().sendPlayerChat("Looks like this imp didn't have the present.",
														"Maybe its on the next one.", Dialogues.CONTENT);
					killer.getDialogue().endDialogue();
					return true;
				}
			}
		}
		return false;
	}

	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(stage == 1)
			return false;
		if(npcId == QuestConstants.SANTA){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Could you please help me?",Dialogues.SAD);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("What's the matter?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Three of my elves have been gone missing,",
													"could you help me find them?",Dialogues.SAD);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendOption("Sure, I'll go look for them.",
													"Maybe later.");
					return true;
				}
				if(chatId == 5){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Sure, I'll go look for them.", Dialogues.CONTENT);
						eventStarted(player);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}//end of stage 0
			if(stage == 2){
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Thank you so much!", Dialogues.SLIGHTLY_SAD);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 2
			int returned = 0;
			if((player.holidayEventStage[this.getId()] & Misc.getActualValue(4)) != 0)
				returned++;
			if((player.holidayEventStage[this.getId()] & Misc.getActualValue(6)) != 0)
				returned++;
			if((player.holidayEventStage[this.getId()] & Misc.getActualValue(9)) != 0)
				returned++;
			if(returned == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("All three elves are still missing.", Dialogues.SLIGHTLY_SAD);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(returned == 1){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Two elves are still missing.", Dialogues.SLIGHTLY_SAD);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(returned == 2){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("One elf is still missing.", Dialogues.SLIGHTLY_SAD);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(returned == 3){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("All elves are back!",
													"Thank you for your help!", Dialogues.HAPPY);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Oh, almost forgot. Here is your",
													"present "+player.getUsername()+".", Dialogues.HAPPY);
					return true;
				}
				if(chatId == 3){
					Npc npc = Npc.getNpcById(QuestConstants.SANTA);
	            	npc.getUpdateFlags().sendAnimation(861);
	            	npc.getUpdateFlags().setForceChatMessage("Ho Ho Ho!");
					player.getDialogue().endDialogue();
					eventCompleted(player);
					return false;
				}
			}
		}//end of santa
		if(npcId == QuestConstants.ELF_VARROCK_XMASS14){
			if(stage < 2)
				return false;
			if((player.holidayEventStage[this.getId()] & Misc.getActualValue(3)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hey, I'm assuming you are one of Santa's",
														"elves.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("What do you want?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Could you return to Santa, he is very worried.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Not yet, First I need to find boots.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("Boots? But aren't you already wearing boots?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Yes, but I meant new pair of leather boots for",
													"Santa, his boots are so worn out.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("Maybe I could bring you some boots?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("If it's not too much to ask.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendPlayerChat("Not at all, I'll be back soon.", Dialogues.CONTENT);
					player.holidayEventStage[this.getId()] += Misc.getActualValue(3);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage
			if((player.holidayEventStage[this.getId()] & Misc.getActualValue(4)) == 0 && (player.holidayEventStage[this.getId()] & Misc.getActualValue(3)) != 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Did you bring the boots?", Dialogues.CONTENT);
					if(player.getInventory().playerHasItem(QuestConstants.LEATHER_BOOTS))
						player.getDialogue().setNextChatId(3);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("Not yet, sorry.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Yes I did! Here you go.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					if(player.getInventory().playerHasItem(QuestConstants.LEATHER_BOOTS)){
						player.getDialogue().sendNpcChat("Just what I was looking for, thanks.", Dialogues.CONTENT);
						player.holidayEventStage[this.getId()] += Misc.getActualValue(4);
						player.getInventory().removeItem(new Item(QuestConstants.LEATHER_BOOTS, 1));
						player.getDialogue().endDialogue();
						return true;
					}
				}
			}//end of stage
		}//end of varrock elf
		if(npcId == QuestConstants.ELF_LUMBRIDGE_XMASS14){
			if(stage < 2)
				return false;
			if((player.holidayEventStage[this.getId()] & Misc.getActualValue(5)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hey, I'm assuming you are one of Santa's",
														"elves.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("What do you want?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Could you return to Santa, he is very worried.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("But I need a cake for a party...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("Cake? I can make you a cake.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("You can? That would be great!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("I'll be back soon.", Dialogues.CONTENT);
					player.holidayEventStage[this.getId()] += Misc.getActualValue(5);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage
			if((player.holidayEventStage[this.getId()] & Misc.getActualValue(6)) == 0 && (player.holidayEventStage[this.getId()] & Misc.getActualValue(5)) != 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Did you bring the cake?", Dialogues.CONTENT);
					if(player.getInventory().playerHasItem(QuestConstants.CAKE))
						player.getDialogue().setNextChatId(3);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("Not yet, sorry.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Yes I did! Here you go.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					if(player.getInventory().playerHasItem(QuestConstants.CAKE)){
						player.getDialogue().sendNpcChat("Looks delicious, thanks.", Dialogues.CONTENT);
						player.holidayEventStage[this.getId()] += Misc.getActualValue(6);
						player.getInventory().removeItem(new Item(QuestConstants.CAKE, 1));
						player.getDialogue().endDialogue();
						return true;
					}
				}
			}//end of stage
		}//end of lumbridge elf
		if(npcId == QuestConstants.ELF_FALADOR_XMASS14){
			if(stage < 2)
				return false;
			if((player.holidayEventStage[this.getId()] & Misc.getActualValue(7)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hey, I'm assuming you are one of Santa's",
														"elves.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("What do you want?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Could you return to Santa, he is very worried.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("...But the imps stole a present!",
													"I must get it back!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("Imps, huh? I can go get it for you.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("If its not too much of a trouble.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("Not at all, I'll be back soon.", Dialogues.CONTENT);
					player.holidayEventStage[this.getId()] += Misc.getActualValue(7);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage
			if((player.holidayEventStage[this.getId()] & Misc.getActualValue(8)) == 0 && (player.holidayEventStage[this.getId()] & Misc.getActualValue(7)) != 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Did you find the present?", Dialogues.CONTENT);
					if(player.getInventory().playerHasItem(QuestConstants.GHOSTS_PRESENT))
						player.getDialogue().setNextChatId(3);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("Not yet, sorry.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Yes I did! Here you go.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Yeah, this is it. Maybe you could deliver",
													"it for me to make sure its not lost again.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("Sure, where do I deliver it to?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("As far as I know Ghost is in jail in",
													"Port Sarim, so go there to look for him.", Dialogues.CONTENT);
					player.holidayEventStage[this.getId()] += Misc.getActualValue(8);
					return true;
				}
			}//end of stage
			if((player.holidayEventStage[this.getId()] & Misc.getActualValue(9)) == 0 && (player.holidayEventStage[this.getId()] & Misc.getActualValue(8)) != 0){
				if(player.hasItem(QuestConstants.GHOSTS_PRESENT))
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I lost the present!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Maybe the imps have it again.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage
			if((player.holidayEventStage[this.getId()] & Misc.getActualValue(9)) != 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I have delivered the present now.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Good, then I'm ready to return to Santa.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage
		}//end of falador elf
		if(npcId == QuestConstants.GHOST_XMASS14){
			if(stage < 2)
				return false;
			if((player.holidayEventStage[this.getId()] & Misc.getActualValue(9)) == 0 && (player.holidayEventStage[this.getId()] & Misc.getActualValue(8)) != 0){
				if(!player.getInventory().playerHasItem(QuestConstants.GHOSTS_PRESENT))
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hey, are you Ghost?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Yes I am, why do you ask?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("There was some problems on delivering your",
														"Christmas present, but anyway here you go...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Oh, thanks. Didn't think I was going to",
													"get one this year!", Dialogues.HAPPY);
					player.holidayEventStage[this.getId()] += Misc.getActualValue(9);
					player.getInventory().removeItem(new Item(QuestConstants.GHOSTS_PRESENT, 1));
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage
		}//end of ghost
		return false;
	}
	
}
