package com.rs2.model.content.questing.quests;

import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class DoricsQuest extends Quest {
	
	final int rewardQP = 1;
	
	public DoricsQuest(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to Doric who is North of",
							"Falador",
							"",
							"There aren't any requirements but Level 15 Mining will help"};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should bring these items to Doric at his house in",
							"North of Falador:",
							"6 x Clay",
							"4 x Copper ore",
							"2 x iron ore"};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "1 Quest Point", "1300 Mining XP", "180 Coins", "Use of Doric's Anvils"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("1300 Mining XP", 12151);
		player.getActionSender().sendString("180 Coins", 12152);
		player.getActionSender().sendString("Use of Doric's Anvils", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.MINING, 1300);
		player.getInventory().addItemOrDrop(new Item(QuestConstants.COINS, 180));
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.STEEL_PICKAXE);
		player.getActionSender().sendInterface(12140);
	}
	
	boolean playerHasAtleastOneItem(final Player player){
		if(!player.getInventory().playerHasItem(QuestConstants.CLAY) && !player.getInventory().playerHasItem(QuestConstants.COPPER_ORE) &&
				!player.getInventory().playerHasItem(QuestConstants.IRON_ORE)){
			return false;
		}
		return true;
	}
	
	boolean playerHasAllItems(final Player player){
		if(player.getInventory().playerHasItem(QuestConstants.CLAY, 6) && player.getInventory().playerHasItem(QuestConstants.COPPER_ORE, 4) &&
				player.getInventory().playerHasItem(QuestConstants.IRON_ORE, 2)){
			return true;
		}
		return false;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.DORIC){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hello traveller, what brings you to my humble smithy?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("I wanted to use your anvils.",
													"I want to use your whetstone.",
													"Mind your own business, shortstuff!",
													"I was just checking out the landscape.",
													"What do you make here?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I wanted to use your anvils.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("My anvils get enough work with my own use. I make",
													"pickaxes, and it takes a lot of hard work. If you could",
													"get me some more materials, then I could let you use",
													"them.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendOption("Yes, I will get you the materials.",
													"No, hitting rocks is for the boring people, sorry.");
					return true;
				}
				if(chatId == 6){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yes, I will get you the materials.", Dialogues.CONTENT);
						questStarted(player);
						player.getDialogue().setNextChatId(7);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}//end of stage 0
			if(stage == 2){
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Clay is what I use more than anything, to make casts.",
													"Could you get me 6 clay, 4 copper ore, and 2 iron ore,",
													"please? I could pay a little, and let you use my anvils.",
													"Take this pickaxe with you just in case you need it.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.BRONZE_PICKAXE, 1));
					player.getDialogue().sendOption("Where can I find those?",
													"Certainly, I'll be right back!");
					return true;
				}
				if(chatId == 9){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Where can I find those?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(10);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("You'll be able to find all those ores in the rocks just",
													"inside the Dwarven Mine. Head east from here and",
													"you'll find the entrance in the side of Ice Mountain.",Dialogues.CONTENT);
					if(player.getSkill().getPlayerLevel(Skill.MINING) >= 15){
						player.getDialogue().endDialogue();
					}
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendPlayerChat("But I'm not a good enough miner to get iron ore.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("Oh well, you could practice mining until you can. Can't",
													"beat a bit of mining - it's a useful skill. Failing that, you",
													"might be able to find a more experienced adventurer to",
													"buy the iron ore off.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Have you got my materials yet, traveller?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					if(!playerHasAtleastOneItem(player)){
						player.getDialogue().sendPlayerChat("No, I have none of them yet.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					}
					if(playerHasAtleastOneItem(player) && !playerHasAllItems(player)){
						player.getDialogue().sendPlayerChat("I have found some of the things you asked for.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(3);
					}
					if(playerHasAllItems(player)){
						player.getDialogue().sendPlayerChat("I have everything you need.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
					}
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Great, but I'll need the other materials as well.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Many thanks. Pass them here, please. I can spare you",
													"some coins for your trouble, and please use my anvils",
													"any time you want.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendItem1Line("You hand the clay, copper, and iron to Doric.", new Item(QuestConstants.COPPER_ORE, 1));
					return true;
				}
				if(chatId == 6){
					if(playerHasAllItems(player)){
						player.getInventory().removeItem(new Item(QuestConstants.CLAY, 6));
						player.getInventory().removeItem(new Item(QuestConstants.COPPER_ORE, 4));
						player.getInventory().removeItem(new Item(QuestConstants.IRON_ORE, 2));
						questCompleted(player);
						player.getDialogue().endDialogue();
						return true;
					}
				}
			}
		}//end of npc 284
		return false;
	}

}
