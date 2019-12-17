package com.rs2.model.content.questing.quests;

import com.rs2.Constants;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class BlackKnightFortress extends Quest {
	
	final int rewardQP = 3;
	
	public BlackKnightFortress(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to Sir Amik Varze at the",
							"White Knights' Castle in Falador.",
							(player.getQuestPoints() >= 12 ? "@str@" : "")+"I have a total of at least 12 Quest Points",
							"I would have an advantage if I could fight Level 33 Knights",
							"and if I had a smithing level of 26."};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should find and sabotage Black Knights' secret weapon,",
							"which can be found somewhere in the Black Knights'",
							"fortress. I will need bronze med helm and iron chainbody to",
							"disguise myself."};
			return text;	
		}
		if(stage == 3){
			String text[] = {"It seems that I could sabotage the potion by putting",
							"ordinary cabbage in there. Just need to find a way",
							"to do that."};
			return text;	
		}
		if(stage == 4){
			String text[] = {"I should now return to Sir Amik Varze to finish",
							"this quest."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "3 Quest Points", "2500 Coins"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("3 Quest Points", 12150);
		player.getActionSender().sendString("2500 Coins", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getInventory().addItemOrDrop(new Item(QuestConstants.COINS, 2500));
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.CABBAGE);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if((objectId == 73 && objectX == 3007 && objectY == 3516) || (objectId == 74 && objectX == 3007 && objectY == 3515)){//double door
			player.getActionSender().sendMessage("It is locked.");
			return true;
		}
		if(objectId == 2342 && objectX == 3025 && objectY == 3507){//grill
			if(stage == 2){
				Dialogues.sendDialogue(player, QuestConstants.BLACK_KNIGHT, 200, 0);
				return true;
			}
		}
		if(objectId == 2340 && objectX == 3028 && objectY == 3510){//door to witch
			player.getActionSender().sendMessage("It is locked.");
			return true;
		}
		if(objectId == 2341 && objectX == 3016 && objectY == 3517){//push wall #1
			player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
			player.getActionSender().walkTo(0, player.getPosition().getY() < 3517 ? 1 : -1, true);
			player.getActionSender().sendMessage("You push against the wall. You find a secret passage.");
			return true;
		}
		if(objectId == 2341 && objectX == 3030 && objectY == 3510){//push wall #2
			player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 1);
			player.getActionSender().walkTo(0, player.getPosition().getY() < 3510 ? 1 : -1, true);
			player.getActionSender().sendMessage("You push against the wall. You find a secret passage.");
			return true;
		}
		if(objectId == 2339 && objectX == 3025 && objectY == 3511){//door
			player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 1);
			player.getActionSender().walkTo(player.getPosition().getX() < 3026 ? 1 : -1, 0, true);
			return true;
		}
		if(objectId == 2337 && objectX == 3016 && objectY == 3514){//door
			if(player.getPosition().getY() < 3515){
				if(player.getEquipment().getId(Constants.HAT) == QuestConstants.BRONZE_MED_HELM && player.getEquipment().getId(Constants.CHEST) == QuestConstants.IRON_CHAINBODY && stage != 0){
					player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
					player.getActionSender().walkTo(0, player.getPosition().getY() < 3515 ? 1 : -1, true);
				} else {
					player.getDialogue().setLastNpcTalk(QuestConstants.FORTRESS_GUARD);
					player.getDialogue().sendNpcChat("Hey! You are not allowed in there!",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
				}
				return true;
			}
			player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
			player.getActionSender().walkTo(0, player.getPosition().getY() < 3515 ? 1 : -1, true);
			return true;
		}
		if(objectId == 2338 && objectX == 3020 && objectY == 3515){//door
			if(player.getPosition().getX() < 3020){
				Dialogues.sendDialogue(player, QuestConstants.FORTRESS_GUARD, 100, 0);
				return true;
			}
			player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
			player.getActionSender().walkTo(player.getPosition().getX() < 3020 ? 1 : -1, 0, true);
			return true;
		}
		return false;
	}
	
	public boolean useQuestItemOnObject(final Player player, int itemId, int objectId, final int stage){
		if(itemId == QuestConstants.CABBAGE && objectId == 2336){//hole
			if(stage == 3){
				player.getInventory().removeItem(new Item(QuestConstants.CABBAGE, 1));
				player.questStage[this.getId()] = 4;
				Dialogues.sendDialogue(player, QuestConstants.WITCH, 100, 0);
			}else{
				player.getActionSender().sendMessage("You have no reason to do this.");
			}
			return true;
		}
		return false;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.SIR_AMIK_VARZE){
			if(stage == 0){
				if(player.getQuestPoints() < 12)
					return false;
				if(chatId == 1){
					player.getDialogue().sendNpcChat("I am the leader of the White Knights of Falador. Why",
													"do you seek my audience?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("I seek a quest!",
													"I don't, I'm just looking around.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I seek a quest.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Well, I need some spy work doing but it's quite",
													"dangerous. It will involve going into the Black Knights'",
													"fortress.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendOption("I laugh in the face of danger!",
													"I go and cower in a corner at the first sign of danger!");
					return true;
				}
				if(chatId == 6){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I laugh in the face of danger!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(7);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Well that's good. Don't get too overconfident though.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("You've come along at just the right time actually. All of",
													"my knights are already known to the Black Knights.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Subtlety isn't exactly our strong point.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendPlayerChat("Can't you just take your White Knights' armour off?",
														"They wouldn't recognise you then!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("I am afraid our charter prevents us using espionage in",
													"any form, that is the domain of the Temple Knights.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendPlayerChat("Temple Knights? Who are they?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("That information is classified. I am forbidden to share it",
													"with outsiders.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendPlayerChat("So... what do you need doing?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendNpcChat("Well, the Black Knights have started making strange",
													"threats to us; demanding large amounts of money and",
													"land, and threatening to invade Falador if we don't pay",
													"them.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("Now, NORMALLY this wouldn't be a problem...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendNpcChat("But they claim to have a powerful new secret weapon.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendNpcChat("Your mission, should you decide to accept it, is to",
													"infiltrate their fortress, find out what their secret",
													"weapon is, and then sabotage it.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 19){
					player.getDialogue().sendNpcChat("You will be well paid.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 20){
					player.getDialogue().sendOption("Ok, I'll do my best.",
													"No, I'm not ready to do that.");
					return true;
				}
				if(chatId == 21){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Ok, I'll do my best.", Dialogues.CONTENT);
						questStarted(player);
						player.getDialogue().endDialogue();
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}//end of stage 0
			if(stage == 4){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I have ruined the Black Knights' invincibility potion.",
														"That should put a stop to your problem and an end to",
														"their little schemes.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Yes, we have just received a message from the Black",
													"Knights saying they withdraw their demands, which",
													"would seem to confirm your story.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Now I believe there was some talk of a cash reward...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Absolutely right. Please accept this reward.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendStatement("Sir Amik hands you 2500 coins.");
					return true;
				}
				if(chatId == 6){
					player.getDialogue().endDialogue();
					questCompleted(player);
					return true;
				}
			}//end of stage 4
		}//end of of sir amik varze
		if(npcId == QuestConstants.FORTRESS_GUARD){
			if(chatId == 100){
				player.getDialogue().sendNpcChat("I wouldn't go in there if I were you. Those Black",
												"Knights are in an important meeting. They said they'd",
												"kill anyone who went in there!",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 101){
				player.getDialogue().sendOption("Okay, I won't.",
												"I don't care. I'm going in anyway.");
				return true;
			}
			if(chatId == 102){
				if(optionId == 2){
					player.getDialogue().sendPlayerChat("I don't care. I'm going in anyway.", Dialogues.CONTENT);
					return true;
				}else{
					player.getDialogue().endDialogue();
					return false;
				}
			}
			if(chatId == 103){
				player.getActionSender().walkThroughDoor(2338, 3020, 3515, 0);
				player.getActionSender().walkTo(player.getPosition().getX() < 3020 ? 1 : -1, 0, true);
				player.getDialogue().endDialogue();
				return false;
			}
		}//end of fortress guard
		if(npcId == QuestConstants.WITCH || npcId == QuestConstants.BLACK_KNIGHT || npcId == QuestConstants.GRELDO){
			if(chatId == 200){
				player.getDialogue().setLastNpcTalk(QuestConstants.BLACK_KNIGHT);
				player.getDialogue().sendNpcChat("So... how's the secret weapon coming along?",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 201){
				player.getDialogue().setLastNpcTalk(QuestConstants.WITCH);
				player.getDialogue().sendNpcChat("The invincibility potion is almost ready...",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 202){
				player.getDialogue().setLastNpcTalk(QuestConstants.WITCH);
				player.getDialogue().sendNpcChat("It's taken me FIVE YEARS, but it's almost ready.",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 203){
				player.getDialogue().setLastNpcTalk(QuestConstants.WITCH);
				player.getDialogue().sendNpcChat("Greldo the Goblin here is just going to fetch the last",
												"ingredient for me.",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 204){
				player.getDialogue().setLastNpcTalk(QuestConstants.WITCH);
				player.getDialogue().sendNpcChat("It's a specially grown cabbage grown by my cousin",
												"Helda who lives in Draynor Manor.",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 205){
				player.getDialogue().setLastNpcTalk(QuestConstants.WITCH);
				player.getDialogue().sendNpcChat("The soil there is slightly magical and it gives the",
												"cabbages slight magical properties....",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 206){
				player.getDialogue().setLastNpcTalk(QuestConstants.WITCH);
				player.getDialogue().sendNpcChat("...not to mention the trees!",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 207){
				player.getDialogue().setLastNpcTalk(QuestConstants.WITCH);
				player.getDialogue().sendNpcChat("Now remember Greldo, only a Draynor Manor",
												"cabbage will do! Don't get lazy and bring any old",
												"cabbage, THAT would ENTIRELY wreck the potion!",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 208){
				player.getDialogue().setLastNpcTalk(QuestConstants.GRELDO);
				player.getDialogue().sendNpcChat("Yeth, Mithtreth.",Dialogues.CONTENT);
				player.questStage[this.getId()] = 3;
				player.getDialogue().endDialogue();
				return true;
			}
			if(chatId == 100){
				player.getDialogue().setLastNpcTalk(QuestConstants.WITCH);
				player.getDialogue().sendNpcChat("Where has Greldo got to with that magic cabbage!",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 101){
				player.getDialogue().setLastNpcTalk(QuestConstants.BLACK_KNIGHT);
				player.getDialogue().sendNpcChat("What's that noise?",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 102){
				player.getDialogue().setLastNpcTalk(QuestConstants.WITCH);
				player.getDialogue().sendNpcChat("Hopefully Greldo with the cabbage... yes, look here it",
												"co....NOOOOOoooo!",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 103){
				player.getDialogue().setLastNpcTalk(QuestConstants.WITCH);
				player.getDialogue().sendNpcChat("My potion!",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 104){
				player.getDialogue().setLastNpcTalk(QuestConstants.BLACK_KNIGHT);
				player.getDialogue().sendNpcChat("Oh boy, this doesn't look good!",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 105){
				player.getDialogue().sendPlayerChat("Looks like my work here is done. Seems like that's",
													"successfully sabotaged their little secret weapon plan.", Dialogues.CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		}
		return false;
	}

}
