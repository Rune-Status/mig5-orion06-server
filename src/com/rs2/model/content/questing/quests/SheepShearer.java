package com.rs2.model.content.questing.quests;

import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class SheepShearer extends Quest {
	
	final int rewardQP = 1;
	
	public SheepShearer(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to Farmer Fred at his",
							"farm just a little way North West of Lumbridge"};
			return text;
		}
		if(stage >= 2 && stage < 22){
			int removeAmt = 22-stage;
			String text[] = {"I should bring these items to the Farmer Fred at his",
							"farm just a little way North West of Lumbridge:",
							removeAmt+" x Ball of wool"};
			return text;	
		}
		if(stage == 22){
			String text[] = {"I should talk to Farmer Fred at his farm to",
							"finish this quest."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "1 Quest Point", "150 Crafting XP", "60 Coins"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("150 Crafting XP", 12151);
		player.getActionSender().sendString("60 Coins", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.CRAFTING, 150);
		player.getInventory().addItemOrDrop(new Item(QuestConstants.COINS, 60));
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.SHEARS);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.FRED_THE_FARMER){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("What are you doing on my land? You're not the one",
													"who keeps leaving all my gates open and letting out all",
													"my sheep are you?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("I'm looking for a quest.", "I'm looking for something to kill.", "I'm lost.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I'm looking for a quest.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("You're after a quest, you say? Actually I could do with",
							"a bit of help.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("My sheep are getting mighty woolly. I'd be much",
													"obliged if you could shear them. And while you're at it",
													"spin the wool for me too.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Yes, that's it. Bring me 20 balls of wool. And I'm sure",
													"I could sort out some sort of payment. Of course,",
													"there's the small matter of The Thing.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendOption("Yes okay. I can do that.", "That doesn't sound a very exciting quest.", "What do you mean, The Thing?");
					return true;
				}
				if(chatId == 8){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yes okay. I can do that.", Dialogues.CONTENT);
						questStarted(player);
						player.getDialogue().setNextChatId(10);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}//end of stage 0
			if(stage >= 2){
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Good! Now one more thing, do you actually know how",
													"to shear a sheep?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendOption("Of course!", "Err. No, I don't know actually.");
					return true;
				}
				if(chatId == 12){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Of course!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(23);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Err. No, I don't know actually..", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(13);
						return true;
					}
				}
				if(chatId == 13){
					if(player.getInventory().playerHasItem(QuestConstants.SHEARS))
						player.getDialogue().sendNpcChat("Well, you're halfway there already! You have a set of",
													"shears in your inventory. Just use those on a Sheep to",
													"shear it.",Dialogues.CONTENT);
					else
						player.getDialogue().sendNpcChat("Well, you have to find a set of shears first.",
														"Then just use those on a Sheep to shear it.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendPlayerChat("That's all I have to do?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendNpcChat("Well once you've collected some wool you'll need to spin",
													"it into balls.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("Do you know how to spin wool?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 23){
					player.getDialogue().sendNpcChat("Good, do you know how to spin wool?",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(17);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendOption("I don't know how to spin wool, sorry.", "I'm something of an expert actually!");
					return true;
				}
				if(chatId == 18){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I don't know how to spin wool, sorry.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(19);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 19){
					player.getDialogue().sendNpcChat("Don't worry, it's quite simple!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 20){
					player.getDialogue().sendNpcChat("The nearest Spinning Wheel can be found on the first",
													"floor of Lumbridge Castle.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 21){
					player.getDialogue().sendNpcChat("To get to Lumbridge Castle just follow the road east.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 22){
					player.getDialogue().sendPlayerChat("Thank you!", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 1){
					player.getDialogue().sendNpcChat("What are you doing on my land?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("I'm back!", "Fred! Fred! I've seen The Thing!");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I'm back!", Dialogues.CONTENT);
						if(stage < 22)
							player.getDialogue().setNextChatId(4);
						else
							player.getDialogue().setNextChatId(8);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("How are you doing getting those balls of wool?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					if(player.getInventory().playerHasItem(QuestConstants.BALL_OF_WOOL)){
						player.getDialogue().sendPlayerChat("I have some.", Dialogues.CONTENT);
						return true;
					}else{
						player.getDialogue().sendPlayerChat("I have none.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Give 'em here then.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					int amt = player.getInventory().getItemAmount(QuestConstants.BALL_OF_WOOL);
					int removeAmt = 22-stage;
					int realRemoveAmt = (amt < removeAmt ? amt : removeAmt);
					stage += realRemoveAmt;
					player.questStage[this.getId()] += realRemoveAmt;
					if(stage < 22){
						player.getDialogue().sendPlayerChat("That's all I have for now.", Dialogues.CONTENT);
						player.getInventory().removeItem(new Item(QuestConstants.BALL_OF_WOOL, realRemoveAmt));
						player.getDialogue().setNextChatId(24);
					}
					if(stage == 22){
						player.getDialogue().sendPlayerChat("That's the last of them.", Dialogues.CONTENT);
						player.getInventory().removeItem(new Item(QuestConstants.BALL_OF_WOOL, realRemoveAmt));
					}
					return true;
				}
				if(chatId == 24){
					int removeAmt = 22-stage;
					player.getDialogue().sendNpcChat("You still need to bring me "+removeAmt+" more.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("I guess I'd better pay you then.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().endDialogue();
					questCompleted(player);
					//player.getDialogue().dontCloseInterface();
					return true;
				}
			}
		}//end of npc 758
		return false;
	}

}
