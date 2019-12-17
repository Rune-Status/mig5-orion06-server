package com.rs2.model.content.questing.quests;

import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class PrinceAliRescue extends Quest {
	
	final int rewardQP = 3;
	
	public PrinceAliRescue(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to Hassan at the palace",
							"in Al-Kharid."};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should go talk to Osman outside the palace in",
							"Al-Kharid."};
			return text;
		}
		if(stage == 3){
			String text[] = {"Osman told me find Leela near Draynor Village and",
							"get these items:",
							"Rope",
							"Pink skirt",
							"Blonde wig",
							"Skin paste",
							"",
							"He also told me to bring imprint of the key and a",
							"bronze bar to him."};
			return text;
		}
		if(stage == 4){
			String text[] = {"Leela told me I need the following items to help",
							"the prince escape:",
							"Rope",
							"Skirt",
							"Blonde wig",
							"Skin paste",
							"Key"};
			return text;
		}
		if(stage == 5){
			String text[] = {"Leela told me I need the following items to help",
							"the prince escape:",
							"Rope",
							"Skirt",
							"Blonde wig",
							"Skin paste",
							"Key - I can get this from Leela"};
			return text;
		}
		if(stage == 6){
			String text[] = {"I should now go talk to Prison guard Joe and",
							"find a weakness in him."};
			return text;
		}
		if(stage == 7){
			String text[] = {"Prison guard Joe seems to like beer, maybe I",
							"should get him drunk."};
			return text;
		}
		if(stage == 8){
			String text[] = {"Prison guard Joe is drunk, I should now tie up Lady",
							"Keli with a rope, and then go rescue Prince Ali."};
			return text;
		}
		if(stage == 9){
			String text[] = {"I should go talk to Hassan at the palace in Al-Kharid",
							"to finish this quest."};
			return text;
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "3 Quest Points", "700 Coins"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("3 Quest Points", 12150);
		player.getActionSender().sendString("700 Coins", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getInventory().addItemOrDrop(new Item(QuestConstants.COINS, 700));
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.COINS);
		player.getActionSender().sendInterface(12140);
	}
	
	boolean playerHasAllItems(final Player player){
		if(player.getInventory().playerHasItem(QuestConstants.BLONDE_WIG, 1) && player.getInventory().playerHasItem(QuestConstants.BRONZE_KEY, 1) &&
				player.getInventory().playerHasItem(QuestConstants.SKIN_PASTE, 1) && player.getInventory().playerHasItem(QuestConstants.ROPE, 1) && player.getInventory().playerHasItem(QuestConstants.PINK_SKIRT, 1)){
			return true;
		}
		return false;
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 2881 && objectX == 3123 && objectY == 3243){
			if((player.getInventory().playerHasItem(QuestConstants.BRONZE_KEY) && stage == 8) || stage == 9){
				if(stage == 8){
					Npc npc = Npc.getNpcById(QuestConstants.LADY_KELI);
					if(npc != null && npc.isVisible())
						return true;
				}
				player.getActionSender().walkTo(0, player.getPosition().getY() < 3244 ? 1 : -1, true);
				player.getActionSender().walkThroughDoor(2881, 3123, 3243, 0);
				if(stage == 8)
					player.getActionSender().sendMessage("You unlock the door.");
				return true;
			} else {
				player.getActionSender().sendMessage("It is locked.");
				return true;
			}
		}
		return false;
	}
	
	public boolean handleItemOnItem(final Player player, int item1, int item2, int stage){
		if(stage != 0){
			if((item1 == QuestConstants.YELLOW_DYE && item2 == QuestConstants.WIG) || (item1 == QuestConstants.WIG && item2 == QuestConstants.YELLOW_DYE)){
				player.getInventory().removeItem(new Item(item1, 1));
	    		player.getInventory().removeItem(new Item(item2, 1));
	    		player.getInventory().addItem(new Item(QuestConstants.BLONDE_WIG, 1));
				player.getActionSender().sendMessage("You dye the wig blonde.");
				return true;
			}
		}
		return false;
	}
	
	public boolean handleItemOnNpc(final Player player, int npcId, int itemId, int stage){
		if(stage == 8){
			if(itemId == QuestConstants.ROPE && npcId == QuestConstants.LADY_KELI){
				Dialogues.sendDialogue(player, QuestConstants.LADY_KELI, 100, 0);
				return true;
			}
		}
		return false;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		//player.questStage[this.getId()] = 0;
		if(npcId == QuestConstants.HASSAN){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Greetings I am Hassan, Chancellor to the Emir of Al-",
													"Kharid.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Can I help you? You must need some help here in the desert.",
													"It's just too hot here. How can you stand it?",
													"Do you mind if I just kill your warriors?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Can I help you? You must need some help here in the",
															"desert.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("I need the services of someone, yes. If you are",
													"interested, see the spymaster, Osman. I manage the",
													"finances here. Come to me when you need payment.",Dialogues.CONTENT);
					questStarted(player);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 0
			if(stage == 9){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("You have the eternal gratitude of the Emir for",
													"rescuing his son. I am authorised to pay you 700",
													"coins.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					questCompleted(player);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 923
		if(npcId == QuestConstants.OSMAN){
			if(stage >= 2 && stage < 4){
				if(stage > 2 && chatId == 1){
					chatId = 3;
					player.tempQuestInt = 2;
				}
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("The chancellor trusts me. I have come for instructions.", Dialogues.CONTENT);
					player.tempQuestInt = 0;
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Our prince is captive by the Lady Keli. We just need",
													"to make the rescue. There are two things we need",
													"you to do.",Dialogues.CONTENT);
					player.questStage[this.getId()] = 3;
					return true;
				}
				if(chatId == 3){
					if(player.tempQuestInt == 0){
						player.getDialogue().sendOption("What is the first thing I must do?",
														"What is the second thing you need?");
					}
					if(player.tempQuestInt == 1){
						player.getDialogue().sendOption("Explain the first thing again.",
														"What is the second thing you need?",
														"Okay, I better go find some things.");
					}
					if(player.tempQuestInt == 2){
						player.getDialogue().sendOption("What is the first thing I must do?",
														"What exactly is the second thing you need?",
														"Okay, I better go find some things.");
					}
					player.getDialogue().setNextChatId(4);
					return true;
				}
				if(chatId == 4){
					if(optionId == 1){
						if(player.tempQuestInt == 0 || player.tempQuestInt == 2)
							player.getDialogue().sendPlayerChat("What is the first thing I must do?", Dialogues.CONTENT);
						else
							player.getDialogue().sendPlayerChat("Explain the first thing again.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}
					if(optionId == 2){
						if(player.tempQuestInt == 0 || player.tempQuestInt == 1)
							player.getDialogue().sendPlayerChat("What is the second thing you need?", Dialogues.CONTENT);
						else
							player.getDialogue().sendPlayerChat("What exactly is the second thing you need?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(12);
						return true;
					}
					if(player.tempQuestInt > 0){
						if(optionId == 3){
							player.getDialogue().sendPlayerChat("Okay, I had better go find some things.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(15);
							return true;
						}
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("The prince is guarded by some stupid guards and a",
													"clever woman. The woman is our only way to get the",
													"prince out. Only she can walk freely about the area.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("I think you will need to tie her up. One coil of rope",
													"should do for that. Then, disguise the prince as her to",
													"get him out without suspicion.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("How good must the disguise be?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Only enough to fool the guards at a distance. Get a",
													"skirt like hers. Same colour, same style. We will only",
													"have a short time.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Get a blonde wig, too. That is up to you to make or",
													"find. Something to colour the skin of the prince.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("My daughter and top spy, Leela, can help you. She has",
													"sent word that she has discovered where they are",
													"keeping the prince.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("It's near Draynor Village. She is lurking somewhere",
													"near there now.",Dialogues.CONTENT);
					player.tempQuestInt = 1;
					player.getDialogue().setNextChatId(3);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("We need the key, or we need a copy made. If you can",
													"get some soft clay then you can copy the key...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("...If you can convince Lady Keli to show it to you",
													"for a moment. She is very boastful.",
													"It should not be too hard.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("Bring the imprint to me, with a bar of bronze.",Dialogues.CONTENT);
					player.tempQuestInt = 2;
					player.getDialogue().setNextChatId(3);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendNpcChat("May good luck travel with you. Don't forget to find",
													"Leela. It can't be done without her help.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage == 4){
				if(player.getInventory().playerHasItem(QuestConstants.KEY_PRINT, 1) && player.getInventory().playerHasItem(QuestConstants.BRONZE_BAR, 1)){
					if(chatId == 1){
						player.getDialogue().sendNpcChat("Well done; we can make the key now.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 2){
						player.getInventory().removeItem(new Item(QuestConstants.KEY_PRINT, 1));
						player.getInventory().removeItem(new Item(QuestConstants.BRONZE_BAR, 1));
						player.getDialogue().sendStatement("Osman takes the key imprint and the bronze bar.");
						player.questStage[this.getId()] = 5;
						return true;
					}
				}
			}
			if(stage == 5){
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Pick the key up from Leela.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendOption("Thank you. I will try to find the other items.",
													"Can you tell me what I still need to get?");
					return true;
				}
				if(chatId == 5){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Thank you. I will try to find the other items.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}
		}//end of npc 924
		if(npcId == QuestConstants.LEELA){
			if(stage >= 3 && stage < 5){
				if(stage > 3 && chatId == 1){
					chatId = 3;
					player.tempQuestInt = 0;
				}
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I am here to help you free the prince.", Dialogues.CONTENT);
					player.tempQuestInt = 0;
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Your employment is known to me. Now, do you know",
													"all that we need to make the break?",Dialogues.CONTENT);
					player.questStage[this.getId()] = 4;
					return true;
				}
				if(chatId == 3){
					if(player.tempQuestInt == 0){
						player.getDialogue().sendOption("I must make a disguise. What do you suggest?",
														"I need to get the key made.",
														"What can I do with the guards?",
														"I will go and get the rest of the escape equipment.");
					}
					if(player.tempQuestInt == 1){
						player.getDialogue().sendOption("I need to get the key made.",
														"What can I do with the guards?",
														"I will go and get the rest of the escape equipment.");
					}
					if(player.tempQuestInt == 2){
						player.getDialogue().sendOption("I must make a disguise. What do you suggest?",
														"What can I do with the guards?",
														"I will go and get the rest of the escape equipment.");
					}
					if(player.tempQuestInt == 3){
						player.getDialogue().sendOption("I must make a disguise. What do you suggest?",
														"I need to get the key made.",
														"I will go and get the rest of the escape equipment.");
					}
					player.getDialogue().setNextChatId(4);
					return true;
				}
				if(chatId == 4){
					if(optionId == 1){
						if(player.tempQuestInt != 1){
							player.getDialogue().sendPlayerChat("I must make a disguise. What do you suggest?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(5);
						}
						else{
							player.getDialogue().sendPlayerChat("I need to get the key made.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(7);
						}
						return true;
					}
					if(optionId == 2){
						if(player.tempQuestInt != 0 && player.tempQuestInt != 3){
							player.getDialogue().sendPlayerChat("What can I do with the guards?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(9);
						}
						else{
							player.getDialogue().sendPlayerChat("I need to get the key made.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(7);
						}
						return true;
					}
					if(optionId == 3){
						if(player.tempQuestInt != 0){
							player.getDialogue().sendPlayerChat("I will go and get the rest of the escape equipment.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(11);
						}
						else{
							player.getDialogue().sendPlayerChat("What can I do with the guards?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(9);
						}
						return true;
					}
					if(optionId == 4){
						if(player.tempQuestInt == 0){
							player.getDialogue().sendPlayerChat("I will go and get the rest of the escape equipment.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(11);
							return true;
						}
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Only the lady Keli, can wander about outside the jail.",
													"The guards will shoot to kill if they see the prince out",
													"so we need a disguise good enough to fool them at a",
													"distance.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("You need a wig, maybe made from wool. If you find",
													"someone who can work with wool ask them about it.",
													"There's a witch nearby may be able to help you dye it.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(12);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Yes, that is most important. There is no way you can",
													"get the real key. It is on a chain around Keli's neck.",
													"Almost impossible to steal.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Get some soft clay and get her to show you the key",
													"somehow. Then take the print, with bronze, to my",
													"father.",Dialogues.CONTENT);
					player.tempQuestInt = 2;
					player.getDialogue().setNextChatId(3);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Most of the guards will be easy. The disguise will get",
													"past them. The only guard who will be a problem will be",
													"the one at the door.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("We can discuss this more when you have the rest of",
													"the escape kit.",Dialogues.CONTENT);
					player.tempQuestInt = 3;
					player.getDialogue().setNextChatId(3);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("Good, I shall await your return with everything.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 12){
					if(player.getInventory().playerHasItem(QuestConstants.PINK_SKIRT, 1))
						player.getDialogue().sendNpcChat("You have got the skirt, good.",Dialogues.CONTENT);
					else
						player.getDialogue().sendNpcChat("You also need to find a pink skirt.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					if(player.getInventory().playerHasItem(QuestConstants.SKIN_PASTE, 1))
						player.getDialogue().sendNpcChat("You have got the skin paste, good.",Dialogues.CONTENT);
					else
						player.getDialogue().sendNpcChat("We still need something to colour the Prince's skin",
														"lighter. There's a witch close to here. She knows about",
														"many things. She may know some way to make the",
														"skin lighter.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					if(player.getInventory().playerHasItem(QuestConstants.ROPE, 1))
						player.getDialogue().sendNpcChat("You have a rope I see, to tie up Keli. That will be the",
														"most dangerous part of the plan.",Dialogues.CONTENT);
					else
						player.getDialogue().sendNpcChat("You need to get some rope somewhere too.",Dialogues.CONTENT);
					player.tempQuestInt = 1;
					player.getDialogue().setNextChatId(3);
					return true;
				}
			}
			if(stage == 5){
				if(!player.hasItem(QuestConstants.BRONZE_KEY)){
					if(chatId == 1){
						player.getDialogue().sendNpcChat("My father sent this key for you.",
														"Be careful not to lose it.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 2){
						player.getDialogue().sendStatement("Leela gives you a copy of the key to the prince's door.");
						return true;
					}
					if(chatId == 3){
						player.getInventory().addItemOrDrop(new Item(QuestConstants.BRONZE_KEY, 1));
						if(playerHasAllItems(player)){
							player.getDialogue().sendNpcChat("Good, you have all the basic equipment. Next to deal",
															"with the guard on the door. He is talkative, try to find",
															"a weakness in him.",Dialogues.CONTENT);
							player.questStage[this.getId()] = 6;
						}else{
							player.getDialogue().sendNpcChat("Come back to me when you have the rest of the",
															"items we need.",Dialogues.CONTENT);
						}
						player.getDialogue().endDialogue();
						return true;
					}
				}else{
					if(chatId == 1){
						if(playerHasAllItems(player)){
							player.getDialogue().sendNpcChat("Good, you have all the basic equipment. Next to deal",
															"with the guard on the door. He is talkative, try to find",
															"a weakness in him.",Dialogues.CONTENT);
							player.questStage[this.getId()] = 6;
						}else{
							player.getDialogue().sendNpcChat("Come back to me when you have the rest of the",
															"items we need.",Dialogues.CONTENT);
						}
						player.getDialogue().endDialogue();
						return true;
					}
				}
			}
			if(stage == 6){
				if(!player.hasItem(QuestConstants.BRONZE_KEY)){
					if(chatId == 1){
						player.getDialogue().sendStatement("Leela gives you a copy of the key to the prince's door.");
						return true;
					}
					if(chatId == 2){
						player.getDialogue().sendNpcChat("Do not lose the key this time.",Dialogues.CONTENT);
						player.getInventory().addItemOrDrop(new Item(QuestConstants.BRONZE_KEY, 1));
						player.getDialogue().endDialogue();
						return true;
					}
				}
			}
		}//end of npc 915
		if(npcId == QuestConstants.NED){
			if(stage >= 4 && stage < 9){
				if(chatId < 111 && (player.hasItem(QuestConstants.WIG) || player.hasItem(QuestConstants.BLONDE_WIG)))
					return false;
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Why, hello there, lad. Me friends call me Ned. I was a",
													"man of the sea, but it's past me now. Could I be",
													"making or selling you some rope?",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(100);
					return true;
				}
				if(chatId == 100){
					player.getDialogue().sendOption("Ned, could you make other things from wool?",
													"Yes, I would like some rope.",
													"No thanks Ned, I don't need any.");
					return true;
				}
				if(chatId == 101){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Ned, could you make other things from wool?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(102);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 102){
					player.getDialogue().sendNpcChat("I am sure I can. What are you thinking of?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 103){
					player.getDialogue().sendOption("Could you knit me a sweater?",
													"How about some sort of a wig?",
													"Could you repair the arrow holes in the back of my shirt?");
					return true;
				}
				if(chatId == 104){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("How about some sort of a wig?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(105);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 105){
					player.getDialogue().sendNpcChat("Well... That's an interesting thought. Yes, I think I",
													"could do something. Give me 3 balls of wool and I",
													"might be able to do it.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 106){
					player.getDialogue().sendOption("I have that now. Please, make me a wig.",
													"I will come back when I need you to make me one.");
					return true;
				}
				if(chatId == 107){
					if(optionId == 1 && player.getInventory().playerHasItem(QuestConstants.BALL_OF_WOOL, 3)){
						player.getDialogue().sendPlayerChat("I have that now. Please, make me a wig.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(108);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 108){
					player.getDialogue().sendNpcChat("Okay, I will have a go.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 109){
					player.getDialogue().sendStatement("You hand Ned 3 balls of wool. Ned works with the wool.",
													"His hands move with a speed you couldn't imagine.");
					return true;
				}
				if(chatId == 110){
					if(player.getInventory().playerHasItem(QuestConstants.BALL_OF_WOOL, 3)){
						player.getInventory().removeItem(new Item(QuestConstants.BALL_OF_WOOL, 3));
						player.getInventory().addItemOrDrop(new Item(QuestConstants.WIG, 1));
						player.getDialogue().sendNpcChat("Here you go, how's that for a quick effort?",
														"Not bad I think!",Dialogues.CONTENT);
						player.getDialogue().setNextChatId(111);
						return true;
					}
				}
				if(chatId == 111){
					player.getDialogue().sendStatement("Ned gives you a pretty good wig.");
					return true;
				}
				if(chatId == 112){
					player.getDialogue().sendPlayerChat("Thanks Ned, there's more to you than meets the eye.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 918
		if(npcId == QuestConstants.AGGIE){
			if(stage >= 4 && stage < 9){
				if(chatId != 109 && player.hasItem(QuestConstants.SKIN_PASTE))
					return false;
				if(chatId == 1){
					player.getDialogue().sendNpcChat("What can I help you with?",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(100);
					return true;
				}
				if(chatId == 100){
					player.getDialogue().sendOption("Could you think of a way to make skin paste?",
													"What could you make for me?",
													"Cool, do you turn people into frogs?",
													"You mad old witch, you can't help me.",
													"Can you make dyes for me please?");
					return true;
				}
				if(chatId == 101){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Could you think of a way to make skin paste?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(102);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 102){
					if(player.getInventory().playerHasItem(QuestConstants.ASHES, 1) && player.getInventory().playerHasItem(QuestConstants.RED_BERRIES, 1) &&
							player.getInventory().playerHasItem(QuestConstants.POT_OF_FLOUR, 1) && player.getInventory().playerHasItem(QuestConstants.BUCKET_OF_WATER, 1)){
						player.getDialogue().sendNpcChat("Yes I can, I see you already have the ingredients.",
														"Would you like me to mix some for you now?",Dialogues.CONTENT);
					}else{
						player.getDialogue().sendNpcChat("Yes I can, I need the following ingredients:",
														"ashes, flour, water and redberries.",Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					}
					return true;
				}
				if(chatId == 103){
					player.getDialogue().sendOption("Yes please. Mix me some skin paste.",
													"No thank you, I don't need any skin paste right now.");
					return true;
				}
				if(chatId == 104){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yes please. Mix me some skin paste.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(105);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 105){
					player.getDialogue().sendNpcChat("That should be simple. Hand the things to Aggie then.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 106){
					player.getDialogue().sendStatement("You hand the ash, flour, water and redberries to Aggie.",
													"Aggies tips the ingredients into a cauldron",
													"and mutters some words.");
					return true;
				}
				if(chatId == 107){
					player.getDialogue().sendNpcChat("Tourniquet, Fenderbaum, Tottenham, Marshmallow,",
													"MarbleArch.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 108){
					if(player.getInventory().playerHasItem(QuestConstants.ASHES, 1) && player.getInventory().playerHasItem(QuestConstants.RED_BERRIES, 1) &&
						player.getInventory().playerHasItem(QuestConstants.POT_OF_FLOUR, 1) && player.getInventory().playerHasItem(QuestConstants.BUCKET_OF_WATER, 1)){
						player.getInventory().removeItem(new Item(QuestConstants.ASHES, 1));
						player.getInventory().removeItem(new Item(QuestConstants.RED_BERRIES, 1));
						player.getInventory().removeItem(new Item(QuestConstants.POT_OF_FLOUR, 1));
						player.getInventory().removeItem(new Item(QuestConstants.BUCKET_OF_WATER, 1));
						player.getInventory().addItemOrDrop(new Item(QuestConstants.SKIN_PASTE, 1));
						player.getDialogue().sendStatement("Aggie hands you the skin paste.");
						player.getDialogue().setNextChatId(123);
						return true;
					}
				}
				if(chatId == 109){
					player.getDialogue().sendNpcChat("There you go dearie, your skin potion. That will make",
													"you look good at the Varrock dances.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}	
		}//end of npc 922
		if(npcId == QuestConstants.LADY_KELI){
			if(stage == 4){
				if(chatId != 25 && player.hasItem(QuestConstants.KEY_PRINT))
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Are you the famous Lady Keli? Leader of the toughest",
														"gang of mercenary killers around?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("I am Keli, you have heard of me then?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendOption("Heard of you? You are famous in RuneScape!",
													"I have heard a little, but I think Katrine is tougher.",
													"I have heard rumours that you kill people.",
													"No I have never really heard of you.");
					return true;
				}
				if(chatId == 4){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("The great Lady Keli, of course I have heard of you.",
															"You are famous in RuneScape!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("That's very kind of you to say. Reputations are",
													"not easily earned. I have managed to succeed",
													"where many fail.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendOption("I think Katrine is still tougher.",
													"What is your latest plan then?",
													"You must have trained a lot for this work.",
													"I should not disturb someone as tough as you.");
					return true;
				}
				if(chatId == 7){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("What is your latest plan then? Of course, you need",
															"not go into specific details.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(8);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Well, I can tell you I have a valuable prisoner here",
													"in my cells.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("I can expect a high reward to be paid very soon for",
													"this guy. I can't tell you who he is, but he is a lot",
													"colder now.",Dialogues.CONTENT);
					player.tempQuestInt = 0;
					return true;
				}
				if(chatId == 10){
					if(player.tempQuestInt == 0)
						player.getDialogue().sendOption("Ah, I see. You must have been very skillful.",
														"Thats great, are you sure they will pay?",
														"Can you be sure they will not try to get him out?",
														"I should not disturb someone as tough as you.");
					if(player.tempQuestInt == 1)
						player.getDialogue().sendOption("Are you sure they will pay?",
														"Can you be sure they will not try to get him out?",
														"I should not disturb someone as tough as you.");
					return true;
				}
				if(chatId == 11){
					if(optionId == 1){
						if(player.tempQuestInt == 0){
							player.getDialogue().sendPlayerChat("You must have been very skillful.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(12);
							return true;
						}
					}else
					if(optionId == 2){
						if(player.tempQuestInt == 1){
							player.getDialogue().sendPlayerChat("Can you be sure they will not try to get him out?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(14);
							return true;
						}
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("Yes, I did most of the work. We had to grab the Pr...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("Er, we had to grab him without his ten bodyguards",
													"noticing. It was a stroke of genius.",Dialogues.CONTENT);
					player.tempQuestInt = 1;
					player.getDialogue().setNextChatId(10);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("There is no way to release him. The only key to the",
													"door is on a chain around my neck and the locksmith",
													"who made the lock died suddenly when he had finished.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendNpcChat("There is not another key like this in the world.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendOption("Could I see the key please?",
													"That is a good way to keep secrets.",
													"I should not disturb someone as tough as you.");
					return true;
				}
				if(chatId == 17){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Could I see the key please? Just for a moment. It",
															"would be something I can tell my grandchildren. When",
															"you are even more famous than you are now.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(18);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 18){
					player.getDialogue().sendNpcChat("As you put it that way I am sure you can see it. You",
													"cannot steal the key, it is on a Runite chain.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 19){
					player.getDialogue().sendStatement("Keli shows you a small key on a strong looking chain.");
					return true;
				}
				if(chatId == 20){
					player.getDialogue().sendOption("Could I touch the key for a moment please?",
													"I should not disturb someone as tough as you.");
					return true;
				}
				if(chatId == 21){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Could I touch the key for a moment please?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(22);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 22){
					if(player.getInventory().playerHasItem(QuestConstants.SOFT_CLAY, 1)){
						player.getDialogue().sendNpcChat("Only for a moment then.",Dialogues.CONTENT);
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
					return true;
				}
				if(chatId == 23){
					player.getDialogue().sendStatement("You put a piece of your soft clay in your hand. As you touch the",
													"key, you take an imprint of it.");
					return true;
				}
				if(chatId == 24){
					if(player.getInventory().playerHasItem(QuestConstants.SOFT_CLAY, 1)){
						player.getInventory().removeItem(new Item(QuestConstants.SOFT_CLAY, 1));
						player.getInventory().addItemOrDrop(new Item(QuestConstants.KEY_PRINT, 1));
						player.getDialogue().sendPlayerChat("Thank you so much, you are too kind, o great Keli.", Dialogues.CONTENT);
						return true;
					}
				}
				if(chatId == 25){
					player.getDialogue().sendNpcChat("You are welcome, run along now. I am very busy.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage == 8){//used rope on Keli
				if(chatId == 100){
					player.getDialogue().sendStatement("You overpower Keli, tie her up, and put her in a cupboard.");
					return true;
				}
				if(chatId == 101){
					if(player.getInventory().playerHasItem(QuestConstants.ROPE, 1)){
						Npc npc = Npc.getNpcById(QuestConstants.LADY_KELI);
						if(npc != null && !npc.isDead()){
							player.getInventory().removeItem(new Item(QuestConstants.ROPE, 1));
							CombatManager.endDeath(npc, player, false);
						}
						player.getDialogue().endDialogue();
						player.getActionSender().removeInterfaces();
						return true;
					}
				}
			}
		}//end of npc 919
		if(npcId == QuestConstants.JOE){
			if(stage == 6){
				if(chatId == 1){
					player.getDialogue().sendOption("I have some beer here, fancy one?",
													"Tell me about the life of a guard.",
													"What did you want to be when you were a boy?",
													"I had better leave, I don't want trouble.");
					return true;
				}
				if(chatId == 2){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I have some beer here, fancy one?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(3);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Ah, that would be lovely, just one now,",
													"just to wet my throat.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("Of course, it must be tough being here without a drink.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					if(player.getInventory().playerHasItem(QuestConstants.BEER, 1)){
						player.getInventory().removeItem(new Item(QuestConstants.BEER, 1));
						player.getDialogue().sendStatement("You hand a beer to the guard, he drinks it in seconds.");
						player.questStage[this.getId()] = 7;
						player.getDialogue().setNextChatId(1);
						return true;
					}
				}
			}
			if(stage == 7){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("That was perfect, I can't thank you enough.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("How are you? Still ok? Not too drunk?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Would you care for another, my friend?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("I better not, I don't want to be drunk on duty.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("Here, just keep these for later,",
														"I hate to see a thirsty guard.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					if(player.getInventory().playerHasItem(QuestConstants.BEER, 2)){
						player.getInventory().removeItem(new Item(QuestConstants.BEER, 2));
						player.getDialogue().sendStatement("You hand two more beers to the guard.",
														"He takes a sip of one, and then he drinks them both.");
						player.questStage[this.getId()] = 8;
						player.getDialogue().setNextChatId(1);
						return true;
					}
				}
			}
			if(stage == 8){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Franksh, that was just what I need to shtay on guard.",
													"No more beersh, I don't want to get drunk.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendStatement("The guard is drunk, and no longer a problem.");
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 916
		if(npcId == QuestConstants.PRINCE_ALI){
			if(stage == 8){
				if(!player.getInventory().playerHasItem(QuestConstants.BRONZE_KEY, 1) || !player.getInventory().playerHasItem(QuestConstants.BLONDE_WIG, 1) ||
						!player.getInventory().playerHasItem(QuestConstants.PINK_SKIRT, 1) || !player.getInventory().playerHasItem(QuestConstants.SKIN_PASTE, 1)){
					return false;
				}
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Prince, I come to rescue you.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("That is very kind of you, how do I get out?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("With a disguise. I have removed the Lady Keli. She is",
														"tied up, but will not stay tied up for long.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("Take this disguise, and this key.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendStatement("You hand the disguise and the key to the prince.");
					return true;
				}
				if(chatId == 6){
					Npc npc = Npc.getNpcById(QuestConstants.PRINCE_ALI);
					if(npc != null && npc.isVisible()){
						player.getInventory().removeItem(new Item(QuestConstants.BRONZE_KEY, 1));
						player.getInventory().removeItem(new Item(QuestConstants.BLONDE_WIG, 1));
						player.getInventory().removeItem(new Item(QuestConstants.PINK_SKIRT, 1));
						player.getInventory().removeItem(new Item(QuestConstants.SKIN_PASTE, 1));
						npc.sendTransform(QuestConstants.PRINCE_ALI_IN_DISGUISE, 100);
						player.getDialogue().setLastNpcTalk(QuestConstants.PRINCE_ALI_IN_DISGUISE);
						player.getDialogue().sendNpcChat("Thank you my friend, I must leave you now. My",
														"father will pay you well for this.",Dialogues.CONTENT);
						player.questStage[this.getId()] = 9;
					}
					return true;
				}
			}
			if(stage == 9){
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("Go to Leela, she is close to here.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					Npc npc = Npc.getNpcById(QuestConstants.PRINCE_ALI_IN_DISGUISE);
					if(npc != null && npc.isVisible()){
						CombatManager.endDeath(npc, player, false);
					}
					player.getDialogue().sendStatement("The prince has escaped, well done! You are now a friend of Al-",
													"Kharid and may pass through the Al-Kharid toll gate for free.");
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 920
		return false;
	}

}
