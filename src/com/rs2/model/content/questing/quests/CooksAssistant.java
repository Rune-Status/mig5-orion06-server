package com.rs2.model.content.questing.quests;

import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class CooksAssistant extends Quest {
	
	final int rewardQP = 1;
	
	public CooksAssistant(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to the Cook in the",
							"Kitchen on the ground floor of Lumbridge Castle."};
			return text;
		}
		if(stage >= 2 && stage < 9){
			int value = stage-2;
			String text[] = {"I should bring these items to the Cook in the",
							"Kitchen of Lumbridge Castle:",
							((value & 1) != 0 ? "@str@" : "") +"Bucket of milk",
							((value & 2) != 0 ? "@str@" : "") +"Pot of flour",
							((value & 4) != 0 ? "@str@" : "") +"Egg"};
			return text;	
		}
		if(stage == 9){
			String text[] = {"I should talk to the Cook in the Kitchen of Lumbridge",
							"Castle to finish this quest."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "1 Quest Point", "300 Cooking XP"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("300 Cooking XP", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.COOKING, 300);
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.CAKE);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		int value = stage-2;
		if(npcId == QuestConstants.MILLIE_MILLER){
			if(stage >= 2 && stage < 9){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hello Adventurer. Welcome to Mill Lane Mill. Can I",
							"help you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Who are you?", "What is this place?", "How do I mill flour?", "I'm fine, thanks.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("How do I mill flour?", Dialogues.CONTENT);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Making flour is pretty easy. First of all you need to",
													"get some grain. You can pick some from wheat fields.",
													"There is one just outside the Mill, but there are many",
													"others scattered across RuneScape. Feel free to pick", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("wheat from our field! There always seems to be plenty",
													"of wheat there.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("Then I bring my wheat here?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Yes, or one of the other mills in RuneScape. They all",
													"work the same way. Just take your grain to the top",
													"floor of the mill (up two ladders, there are three floors",
													"including this one) and then place some grain into the", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("hopper.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Then you need to start the grinding process by pulling",
													"the hopper lever. You can add more grain, but each",
													"time you add grain you have to pull the hopper lever",
													"again.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendPlayerChat("So where does the flour go then?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("The flour appears in this room here, you'll need a pot",
													"to put the flour into. One pot will hold the flour made",
													"by one load of grain", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("And that's it! You now have some pots of finely ground",
													"flour of the highest quality. Ideal for making tasty cakes",
													"or delicious bread. I'm not a cook so you'll have to ask a",
													"cook to find out how to bake things.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendPlayerChat("Great! Thanks for your help.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 3806
		if(npcId == QuestConstants.COOK){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("What am I to do?", Dialogues.VERY_SAD);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("What's wrong?", "Can you make me a cake?", "You don't look very happy.", "Nice hat!");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("What's wrong?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Oh dear, oh dear, oh dear, I'm in a terrible terrible",
													"mess! It's the Duke's birthday today, and I should be",
													"making him a lovely big birthday cake.",Dialogues.NEAR_TEARS);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("I've forgotten to buy the ingredients. I'll never get",
													"them in time now. He'll sack me! What will I do? I have",
													"four children and a goat to look after. Would you help",
													"me? Please?",Dialogues.NEAR_TEARS_2);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendOption("I'm always happy to help a cook in distress.", "I can't right now, Maybe later.");
					return true;
				}
				if(chatId == 7){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yes, I'll help you.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(8);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Oh thank you, thank you. I need milk, an egg and",
													"flour. I'd be very grateful if you can get them for me.",Dialogues.CALM_CONTINUED);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendPlayerChat("So where do I find these ingredients then?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendOption("Where do I find some flour?", "How about milk?", "And eggs? Where are they found?", "Actually, I know where to find this stuff.");
					questStarted(player);
					player.getDialogue().setNextChatId(3);
					return true;
				}
			}//end of stage 0
			if(stage >= 2 && stage < 9){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("How are you getting on with finding the ingredients?",Dialogues.SLIGHTLY_SAD);
					if((value & 1) == 0 && player.getInventory().playerHasItem(QuestConstants.BUCKET_OF_MILK)){
						player.getDialogue().setNextChatId(4);
						return true;
					}
					if((value & 2) == 0 && player.getInventory().playerHasItem(QuestConstants.POT_OF_FLOUR)){
						player.getDialogue().setNextChatId(5);
						return true;
					}
					if((value & 4) == 0 && player.getInventory().playerHasItem(QuestConstants.EGG)){
						player.getDialogue().setNextChatId(6);
						return true;
					}
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Where do I find some flour?", "How about milk?", "And eggs? Where are they found?", "Actually, I know where to find this stuff.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendNpcChat("There is a Mill fairly close, Go North and then West.",
														"Mill Lane Mill is just off the road to Draynor. I",
														"usually get my flour from there.",Dialogues.CALM);
						player.getDialogue().setNextChatId(7);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendNpcChat("There is a cattle field on the other side of the river,",
														"just across the road from the Groats' Farm.",Dialogues.CALM);
						player.getDialogue().setNextChatId(8);
						return true;
					}
					if(optionId == 3){
						player.getDialogue().sendNpcChat("I normally get my eggs from the Groats' farm, on the",
														"other side of the river.",Dialogues.CALM);
						player.getDialogue().setNextChatId(10);
						return true;
					}
					if(optionId == 4){
						player.getDialogue().sendPlayerChat("Actually, I know where to find this stuff.", Dialogues.CONTENT);
						//player.getDialogue().setNextChatId(50);
						player.getDialogue().endDialogue();
						return true;
					}
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Talk to Millie, she'll help, she's a lovely girl and a fine",
													"Miller..",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(2);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Talk to Gillie Groats, she looks after the Dairy cows -",
													"she'll tell you everything you need to know about",
													"milking cows!",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(2);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("But any chicken should lay eggs.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(2);
					return true;
				}
				/*if(chatId == 50){
					player.getDialogue().endDialogue();
					return false;
				}*/
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("Here's a bucket of milk.", Dialogues.CONTENT);
					player.getInventory().removeItem(new Item(QuestConstants.BUCKET_OF_MILK, 1));
					stage += 1;
					player.questStage[this.getId()] += 1;
					value += 1;
					if((value & 2) == 0 && player.getInventory().playerHasItem(QuestConstants.POT_OF_FLOUR)){
						player.getDialogue().setNextChatId(5);
						return true;
					} else
					if((value & 4) == 0 && player.getInventory().playerHasItem(QuestConstants.EGG)){
						player.getDialogue().setNextChatId(6);
						return true;
					} else
					if((value & 1) != 0 && (value & 2) != 0 && (value & 4) != 0){
						player.questStage[this.getId()] = 9;
						player.getDialogue().setNextChatId(1);
						return true;
					} else {
						//player.getDialogue().setNextChatId(50);
						player.getDialogue().endDialogue();
						return true;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("Here's a pot of flour.", Dialogues.CONTENT);
					player.getInventory().removeItem(new Item(QuestConstants.POT_OF_FLOUR, 1));
					stage += 2;
					player.questStage[this.getId()] += 2;
					value += 2;
					if((value & 4) == 0 && player.getInventory().playerHasItem(QuestConstants.EGG)){
						player.getDialogue().setNextChatId(6);
						return true;
					}  else
					if((value & 1) != 0 && (value & 2) != 0 && (value & 4) != 0){
						player.questStage[this.getId()] = 9;
						player.getDialogue().setNextChatId(1);
						return true;
					} else {
						//player.getDialogue().setNextChatId(50);
						player.getDialogue().endDialogue();
						return true;
					}
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("Here's a fresh egg.", Dialogues.CONTENT);
					player.getInventory().removeItem(new Item(QuestConstants.EGG, 1));
					stage += 4;
					player.questStage[this.getId()] += 4;
					value += 4;
					if((value & 1) != 0 && (value & 2) != 0 && (value & 4) != 0){
						player.questStage[this.getId()] = 9;
						player.getDialogue().setNextChatId(1);
						return true;
					} else {
						//player.getDialogue().setNextChatId(50);
						player.getDialogue().endDialogue();
						return true;
					}
				}
			}//end of stage 2-8
			if(stage == 9){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("You've brought me everything I need! I am saved!",
													"Thank you!",Dialogues.CALM_CONTINUED);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("So do I get to go to the Duke's Party?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("I'm afraid not, only the big cheeses get to dine with the",
													"Duke.",Dialogues.VERY_SAD);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("Well, maybe one day I'll be important enough to sit on",
														"the Duke's table.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Maybe, but I won't be holding my breath.",Dialogues.HAPPY);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().endDialogue();
					questCompleted(player);
					//player.getDialogue().dontCloseInterface();
					return true;
				}
			}
		}//end of npc 278
		return false;
	}

}
