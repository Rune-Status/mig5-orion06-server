package com.rs2.model.content.questing.quests;

import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class DruidicRitual extends Quest {
	
	final int rewardQP = 4;
	
	public DruidicRitual(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to Kaqemeex who is at",
							"the Druids Circle just North of Taverley."};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should go and speak with Sanfew, who can be found in",
							"Taverley."};
			return text;	
		}
		if(stage == 3){
			String text[] = {"I should go and dip the following meats in the",
							"cauldron of thunder, located somewhere underground",
							"south of Taverley. And then bring the meats back to Sanfew.",
							"Raw bear meat",
							"Raw rat meat",
							"Raw beef",
							"Raw chicken"};
			return text;	
		}
		if(stage == 4){
			String text[] = {"I should go and speak with Kaqemeex to finish this quest."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "4 Quest Points", "Herblore skill", "250 Herblore XP"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("4 Quest Points", 12150);
		player.getActionSender().sendString("Herblore skill", 12151);
		player.getActionSender().sendString("250 Herblore XP", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.HERBLORE, 250);
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.MARRENTILL);
		player.getActionSender().sendInterface(12140);
	}
	
	boolean playerHasAtleastOneItem(final Player player){
		if(!player.getInventory().playerHasItem(QuestConstants.ENCHANTED_BEAR) && !player.getInventory().playerHasItem(QuestConstants.ENCHANTED_RAT) &&
				!player.getInventory().playerHasItem(QuestConstants.ENCHANTED_BEEF) && !player.getInventory().playerHasItem(QuestConstants.ENCHANTED_CHICKEN)){
			return false;
		}
		return true;
	}
	
	boolean playerHasAllItems(final Player player){
		if(player.getInventory().playerHasItem(QuestConstants.ENCHANTED_BEAR) && player.getInventory().playerHasItem(QuestConstants.ENCHANTED_RAT) &&
				player.getInventory().playerHasItem(QuestConstants.ENCHANTED_BEEF) && player.getInventory().playerHasItem(QuestConstants.ENCHANTED_CHICKEN)){
			return true;
		}
		return false;
	}
	
	public boolean useQuestItemOnObject(final Player player, int itemId, int objectId, int stage){
		if(player.questStage[this.getId()] == 3){
			if(objectId == 2142){
				if(itemId == QuestConstants.RAW_BEAR && player.getInventory().playerHasItem(QuestConstants.RAW_BEAR)){
					player.getActionSender().sendMessage("You dip the raw bear meat in the cauldron.");
					player.getInventory().removeItem(new Item(QuestConstants.RAW_BEAR, 1));
					player.getInventory().addItem(new Item(QuestConstants.ENCHANTED_BEAR, 1));
					return true;
				}
				if(itemId == QuestConstants.RAW_RAT && player.getInventory().playerHasItem(QuestConstants.RAW_RAT)){
					player.getActionSender().sendMessage("You dip the raw rat meat in the cauldron.");
					player.getInventory().removeItem(new Item(QuestConstants.RAW_RAT, 1));
					player.getInventory().addItem(new Item(QuestConstants.ENCHANTED_RAT, 1));
					return true;
				}
				if(itemId == QuestConstants.RAW_BEEF && player.getInventory().playerHasItem(QuestConstants.RAW_BEEF)){
					player.getActionSender().sendMessage("You dip the raw beef in the cauldron.");
					player.getInventory().removeItem(new Item(QuestConstants.RAW_BEEF, 1));
					player.getInventory().addItem(new Item(QuestConstants.ENCHANTED_BEEF, 1));
					return true;
				}
				if(itemId == QuestConstants.RAW_CHICKEN && player.getInventory().playerHasItem(QuestConstants.RAW_CHICKEN)){
					player.getActionSender().sendMessage("You dip the raw chicken in the cauldron.");
					player.getInventory().removeItem(new Item(QuestConstants.RAW_CHICKEN, 1));
					player.getInventory().addItem(new Item(QuestConstants.ENCHANTED_CHICKEN, 1));
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.KAQEMEEX){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello there.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("What brings you to our holy monument?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendOption("Who are you?",
													"I'm in search of a quest.",
													"Did you build this?");
					return true;
				}
				if(chatId == 4){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("I'm in search of a quest.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Hmm. I think I may have a worthwhile quest for you",
													"actually. I don't know if you are familiar with the stone",
													"circle south of Varrock or not, but...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("That used to be OUR stone circle. Unfortunately,",
													"many many years ago, dark wizards cast a wicked spell",
													"upon it so that they could corrupt its power for their",
													"own and evil ends.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("When they cursed the rocks for their rituals they made",
													"them useless to us and our magics. We require a brave",
													"adventurer to go on a quest for us to help purify the",
													"circle of Varrock.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendOption("Ok, I will try and help.",
													"No, that doesn't sound very interesting.",
													"So... is there anything in this for me?");
					return true;
				}
				if(chatId == 9){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Ok, I will try and help.", Dialogues.CONTENT);
						questStarted(player);
						player.getDialogue().setNextChatId(10);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}//end of stage 0
			if(stage == 2){
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Excellent. Go to the village south of this place and speak",
													"to my fellow Sanfew who is working on the purification",
													"ritual. He knows better than I what is required to",
													"complete it.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendPlayerChat("Will do.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 2
			if(stage == 4){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello there.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("I have word from Sanfew that you have been very",
													"helpful in assisting him with his preparations for the",
													"purification ritual. As promised I will now teach you the",
													"ancient arts of Herblore.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("I will now explain the fundamentals of Herblore:",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Herblore is the skill of working with herbs and other",
													"ingredients, to make useful potions and poison.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("First you will need a vial, which can be found or made",
													"with the crafting skill.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Then you must gather the herbs needed to make the",
													"potion you want.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Refer to the Council's instructions in the Skills section",
													"of the website for the items needed to make a particular",
													"kind of potion.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("You must fill your vial with water and add the",
													"ingredients you need. There are normally 2 ingredients",
													"to each type of potion.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Bear in mind, you must first identify each herb, to see",
													"what it is.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("You may also have to grind some herbs, before you can",
													"use them. You will need a pestle and mortar in order",
													"to do this.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("Herbs can be found on the ground, and are also",
													"dropped by some monsters when you kill them.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("Let's try an example Attack potion: The first ingredient",
													"is Guam leaf, the next is Eye of Newt.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("Mix these in your water-filled vial, and you will produce",
													"an Attack potion.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("Drink this potion to increase your Attack level.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendNpcChat("Different potions also require different Herblore levels",
													"before you can make them.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("Once again, check the instructions found on the",
													"Council's website for the levels needed to make a",
													"particular potion.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendNpcChat("Good luck with your Herblore practices, Good day",
													"Adventurer.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendPlayerChat("Thanks for your help.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 19){
					player.getDialogue().endDialogue();
					questCompleted(player);
					return true;
				}
			}//end of stage 4
			/*
			if(stage == 1){
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Herblore is the skill of working with herbs and other",
													"ingredients, to make useful potions and poison.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("First you will need a vial, which can be found or made",
													"with the crafting skill.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Then you must gather the herbs needed to make the",
													"potion you want.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Refer to the Council's instructions in the Skills section",
													"of the website for the items needed to make a particular",
													"kind of potion.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("You must fill your vial with water and add the",
													"ingredients you need. There are normally 2 ingredients",
													"to each type of potion.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Bear in mind, you must first identify each herb, to see",
													"what it is.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("You may also have to grind some herbs, before you can",
													"use them. You will need a pestle and mortar in order",
													"to do this.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("Herbs can be found on the ground, and are also",
													"dropped by some monsters when you kill them.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("Let's try an example Attack potion: The first ingredient",
													"is Guam leaf, the next is Eye of Newt.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("Mix these in your water-filled vial, and you will produce",
													"an Attack potion.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("Drink this potion to increase your Attack level.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendNpcChat("Different potions also require different Herblore levels",
													"before you can make them.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("Once again, check the instructions found on the",
													"Council's website for the levels needed to make a",
													"particular potion.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendNpcChat("Good luck with your Herblore practices, Good day",
													"Adventurer.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendPlayerChat("Thanks for your help.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 1
			*/
		}//end of npc 455
		if(npcId == QuestConstants.SANFEW){
			if(stage == 2){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("What can I do for you young 'un?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("I've been sent to help purify the Varrock stone circle.",
													"Actually, I don't need to speak to you.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I've been sent to assist you with the ritual to purify the",
															"Varrockian stone circle.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Well, what I'm struggling with right now is the meats",
													"needed for the potion to honour Guthix. I need the raw",
													"meat of four different animals for it, but not just any",
													"old meats will do.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Each meat has to be dipped individually into the",
													"Cauldron of Thunder for it to work correctly.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendOption("Where can I find this cauldron?",
													"Ok, I'll do that then.");
					return true;
				}
				if(chatId == 7){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Where can I find this cauldron?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(8);
						return true;
					}else{
						player.getDialogue().sendPlayerChat("Ok, I'll do that then.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						player.questStage[this.getId()] = 3;
						return true;
					}
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("It is located somewhere in the mysterious underground",
													"halls which are located somewhere in the woods just",
													"South of here. They are too dangerous for me to go",
													"myself however.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					player.questStage[this.getId()] = 3;
					return true;
				}
			}//end of stage 2
			if(stage == 3){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Did you bring me the required ingredients for the potion?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					if(!playerHasAtleastOneItem(player)){
						player.getDialogue().sendPlayerChat("No, I have none of them yet.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					}
					if(playerHasAtleastOneItem(player) && !playerHasAllItems(player)){
						player.getDialogue().sendPlayerChat("I have some of the things you asked for.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(6);
					}
					if(playerHasAllItems(player)){
						player.getDialogue().sendPlayerChat("Yes, I have all four now!", Dialogues.CONTENT);
					}
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Well hand 'em over then lad!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Thank you so much adventurer! These meats will allow",
													"our potion to honour Guthix to be completed, and bring",
													"one step closer to reclaiming our stone circle!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					if(playerHasAllItems(player)){
						player.getInventory().removeItem(new Item(QuestConstants.ENCHANTED_BEAR, 1));
						player.getInventory().removeItem(new Item(QuestConstants.ENCHANTED_RAT, 1));
						player.getInventory().removeItem(new Item(QuestConstants.ENCHANTED_BEEF, 1));
						player.getInventory().removeItem(new Item(QuestConstants.ENCHANTED_CHICKEN, 1));
						player.getDialogue().sendNpcChat("Now go and talk to Kaqemeex and he will introduce",
														"you to the wonderful world of herblore and potion",
														"making!",Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						player.questStage[this.getId()] = 4;
						return true;
					}
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Great, but I'll need the other ingredients as well.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			
		}
		return false;
	}

}
