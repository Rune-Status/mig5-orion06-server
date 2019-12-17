package com.rs2.model.content.questing.quests;

import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class GoblinDiplomacy extends Quest {
	
	final int rewardQP = 5;
	
	public GoblinDiplomacy(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to Generals Wartface",
							"and Bentnoze in the Goblin Village.",
							"There are no requirements for this quest."};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should bring orange goblin armour to Generals Wartface",
							"and Bentnoze in the Goblin Village."};
			return text;	
		}
		if(stage == 3){
			String text[] = {"I should bring blue goblin armour to Generals Wartface",
							"and Bentnoze in the Goblin Village."};
			return text;	
		}
		if(stage == 4){
			String text[] = {"I should bring brown goblin armour to Generals Wartface",
							"and Bentnoze in the Goblin Village."};
			return text;	
		}
		if(stage == 5){
			String text[] = {"I should speak with Generals Wartface and Bentnoze",
							"in the Goblin Village to finish this quest."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "5 Quest Points", "200 Crafting XP", "A gold bar"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("5 Quest Points", 12150);
		player.getActionSender().sendString("200 Crafting XP", 12151);
		player.getActionSender().sendString("A gold bar", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.CRAFTING, 200);
		player.getInventory().addItemOrDrop(new Item(QuestConstants.GOLD_BAR, 1));
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.BROWN_ARMOUR);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean handleItemOnItem(final Player player, int item1, int item2, int stage){
		if(stage == 1)
			return false;
		if(stage != 0){
			if((item1 == QuestConstants.ORANGE_DYE && item2 == QuestConstants.BROWN_ARMOUR) || (item1 == QuestConstants.BROWN_ARMOUR && item2 == QuestConstants.ORANGE_DYE)){
				player.getInventory().removeItem(new Item(item1, 1));
	    		player.getInventory().removeItem(new Item(item2, 1));
	    		player.getInventory().addItem(new Item(QuestConstants.ORANGE_ARMOUR, 1));
				player.getActionSender().sendMessage("You dye the goblin armour orange.");
				return true;
			}
			if((item1 == QuestConstants.BLUE_DYE && item2 == QuestConstants.BROWN_ARMOUR) || (item1 == QuestConstants.BROWN_ARMOUR && item2 == QuestConstants.BLUE_DYE)){
				player.getInventory().removeItem(new Item(item1, 1));
	    		player.getInventory().removeItem(new Item(item2, 1));
	    		player.getInventory().addItem(new Item(QuestConstants.BLUE_ARMOUR, 1));
				player.getActionSender().sendMessage("You dye the goblin armour blue.");
				return true;
			}
		}
		return false;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		//player.questStage[this.getId()] = 0;
		if(chatId == 1){
			player.tempQuestInt = Misc.random_(4);
		}
		int dialog = player.tempQuestInt;
		if(npcId == QuestConstants.GENERAL_BENTNOZE || npcId == QuestConstants.GENERAL_WARTFACE){
			//if(stage == 0){
			if(stage == 5){
				if(chatId == 34){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("It a deal then. Brown armour it is.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(1);
					return true;
				}
				if(chatId == 1){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("Thank you for sorting out our argument. Take this",
													"gold bar as reward!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					questCompleted(player);
					player.getDialogue().dontCloseInterface();
					return true;
				}
			}
			if(dialog == 0){
				if(chatId == 1){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("I tell all goblins in village to wear green armour now!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("They not listen to you! I already tell them wear red",
													"armour!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("They listen to me not you! They know me bigger",
													"general!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("Me bigger general! They listen to me!",Dialogues.CONTENT);
					return true;	
				}
				if(chatId == 5){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("Human! What colour armour they wearing out there?",Dialogues.CONTENT);
					return true;	
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("Half of them are wearing red and half of them green.", Dialogues.CONTENT);
					return true;	
				}
				if(chatId == 7){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("Shut up human! They wearing green armour really!",
													"Human lying because he scared of you!",Dialogues.CONTENT);
					return true;	
				}
				if(chatId == 8){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("Human scared of me not you! Then you think me",
													"bigger general!",Dialogues.CONTENT);
					return true;	
				}
				if(chatId == 9){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("What? Me mean...",Dialogues.CONTENT);
					return true;	
				}
				if(chatId == 10){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("Shut up! Me bigger general!",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(50);
					return true;	
				}
			}
				if(chatId == 50){
					if(stage == 0){
						player.getDialogue().sendOption("Why are you arguing about the colour of your armour?",
														"Wouldn't you prefer peace?",
														"Do you want me to pick an armour colour for you?");
					}
					if((stage == 2 && !player.getInventory().playerHasItem(QuestConstants.ORANGE_ARMOUR, 1)) || (stage == 3 && !player.getInventory().playerHasItem(QuestConstants.BLUE_ARMOUR, 1)) || (stage == 4 && !player.getInventory().playerHasItem(QuestConstants.BROWN_ARMOUR, 1))){
						player.getDialogue().sendOption("Why are you arguing about the colour of your armour?",
														"Wouldn't you prefer peace?");
					}
					if(stage == 2 && player.getInventory().playerHasItem(QuestConstants.ORANGE_ARMOUR, 1)){
						player.getDialogue().sendOption("Why are you arguing about the colour of your armour?",
														"Wouldn't you prefer peace?",
														"I have some orange armour here");
					}
					if(stage == 3 && player.getInventory().playerHasItem(QuestConstants.BLUE_ARMOUR, 1)){
						player.getDialogue().sendOption("Why are you arguing about the colour of your armour?",
														"Wouldn't you prefer peace?",
														"I have some blue armour here");
					}
					if(stage == 4 && player.getInventory().playerHasItem(QuestConstants.BROWN_ARMOUR, 1)){
						player.getDialogue().sendOption("Why are you arguing about the colour of your armour?",
														"Wouldn't you prefer peace?",
														"I have some brown armour here");
					}
					return true;
				}
				if(chatId == 51){
					if(optionId == 3 && stage == 4){
						player.getDialogue().sendPlayerChat("I have some brown armour here.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(33);
						return true;
					} else
					if(optionId == 3 && stage == 3){
						player.getDialogue().sendPlayerChat("I have some blue armour here.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(28);
						return true;
					} else	
					if(optionId == 3 && stage == 2){
						player.getDialogue().sendPlayerChat("I have some orange armour here.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(23);
						return true;
					} else	
					if(optionId == 3 && stage == 0){
						player.getDialogue().sendPlayerChat("Do you want me to pick an armour colour for you?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(13);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 13){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("Yes, as long as you pick green.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("No you have to pick red!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendOption("You should wear red",
													"You should wear green",
													"What about a different colour?");
					return true;
				}
				if(chatId == 16){
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("What about a different colour? Not green or red?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(17);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 17){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("That would mean me wrong... but at least Wartface",
													"not right!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("Me dunno what that look like. Have to see armour",
													"before we decide.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 19){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("Human! You bring us armour in new colour!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 20){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("What colour we try?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 21){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("Orange armour might be good.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 22){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("Yep. bring us orange armour.",Dialogues.CONTENT);
					questStarted(player);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 23){
					if(player.getInventory().playerHasItem(QuestConstants.ORANGE_ARMOUR, 1)){
						player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
						player.getDialogue().sendNpcChat("No I don't like that much.",Dialogues.CONTENT);
						player.getInventory().removeItem(new Item(QuestConstants.ORANGE_ARMOUR, 1));
						player.questStage[this.getId()] = 3;
						return true;
					}else{
						player.getDialogue().endDialogue();
						return true;
					}
				}
				if(chatId == 24){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("It clashes with skin colour.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 25){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("We need darker colour, like blue.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 26){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("Yeah blue might be good.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 27){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("Human! Get us blue armour!",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 28){
					if(player.getInventory().playerHasItem(QuestConstants.BLUE_ARMOUR, 1)){
						player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
						player.getDialogue().sendNpcChat("That not right. Not goblin colour at all.",Dialogues.CONTENT);
						player.getInventory().removeItem(new Item(QuestConstants.BLUE_ARMOUR, 1));
						player.questStage[this.getId()] = 4;
						return true;
					}else{
						player.getDialogue().endDialogue();
						return true;
					}
				}
				if(chatId == 29){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("Goblins wear dark earthy colours like brown.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 30){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("Yeah brown might be good.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 31){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("Human! Get us brown armour!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 32){
					player.getDialogue().sendPlayerChat("I thought that was the armour you were changing",
														"from. Never mind, anything is worth a try.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 33){
					if(player.getInventory().playerHasItem(QuestConstants.BROWN_ARMOUR, 1)){
						player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
						player.getDialogue().sendNpcChat("That colour quite nice. Me can see myself wearing",
														"that",Dialogues.CONTENT);
						player.getInventory().removeItem(new Item(QuestConstants.BROWN_ARMOUR, 1));
						player.questStage[this.getId()] = 5;
						return true;
					}else{
						player.getDialogue().endDialogue();
						return true;
					}
				}
			if(dialog == 1){
				if(chatId == 1){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("Red armour best.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("No it has to be green!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("Go away human, we busy.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(50);
					return true;
				}
			}
			if(dialog == 2){
				if(chatId == 1){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("All goblins should wear red armour!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("Not red! Red armour make you look fat.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("Everything make YOU look fat!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("Shut up!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("Fatty!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("SHUT UP!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("Even this human think you look fat! Don't you, human?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendPlayerChat("Um...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendOption("Yes, Wartface looks fat",
													"No, he doesn't look fat");
					return true;
				}
				if(chatId == 10){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("No, he doesn't look fat.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(11);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 11){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("Shut up human! Wartface fat and human stupid!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("Shut up Bentnoze!",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(50);
					return true;
				}
			}
			if(dialog == 3){
				if(chatId == 1){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("We should wear green armour!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("Green armour? Are you stupid?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("You stupid! Only stupid goblins think red armour",
													"better!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("No they don't! Me think red armour better!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("That because you stupid!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("Then why you not like green armour?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("Me not stupid!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_BENTNOZE);
					player.getDialogue().sendNpcChat("Because red armour better!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().setLastNpcTalk(QuestConstants.GENERAL_WARTFACE);
					player.getDialogue().sendNpcChat("Only stupid goblins think that! You stupid!",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(50);
					return true;
				}
			}
		}//end of npc 296,297
		return false;
	}

}
