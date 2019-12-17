package com.rs2.model.content.questing.quests;

import com.rs2.model.content.Shops;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class RuneMysteriesQuest extends Quest {
	
	final int rewardQP = 1;
	
	public RuneMysteriesQuest(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to Duke Horacio of ",
							"Lumbridge, upstairs in Lumbridge Castle."};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should bring the air talisman to head wizard at the",
							"Wizards' Tower, south-west of Lumbridge."};
			return text;	
		}
		if(stage == 3){
			String text[] = {"I should talk to head wizard at the Wizards' Tower",
							"to continue this quest."};
			return text;	
		}
		if(stage == 4){
			String text[] = {"I should deliver the package to Aubury, owner of the",
							"rune shop in Varrock."};
			return text;	
		}
		if(stage == 5){
			String text[] = {"I should talk to Aubury at Varrock rune shop",
							"to continue this quest."};
			return text;	
		}
		if(stage == 6){
			String text[] = {"I should bring the research notes to head wizard at the",
							"Wizards' Tower, south-west of Lumbridge."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "1 Quest Point", "Runecrafting skill", "Air talisman"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("Runecrafting skill", 12151);
		player.getActionSender().sendString("Air talisman", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.AIR_TALISMAN);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.DUKE_HORACIO){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Greetings. Welcome to my castle.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Have you any quest for me?",
													"Where can I find money?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Have you any quest for me?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Well, it's not really a quest but I recently discovered",
													"this strange talisman.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("It seems to be mystical and I have never seen anything",
													"like it before. Would you take it to the head wizard at",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("the Wizards' Tower for me? It's just south-west of here",
													"and should not take you very long at all. I would be",
													"awfully grateful.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendOption("Sure, no problem.",
													"Not right now.");
					return true;
				}
				if(chatId == 8){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Sure, no problem.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Thank you very much, stranger. I am sure the head",
													"wizard will reward you for such an interesting find.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendStatement("The Duke hands you an @dbl@air talisman.");
					return true;
				}
				if(chatId == 11){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.AIR_TALISMAN, 1));
					questStarted(player);
					player.getDialogue().endDialogue();
					return false;
				}
			}//end of stage 0
		}//end of npc 741
		if(npcId == QuestConstants.SEDRIDOR){
			if(stage == 2){
				if(!player.getInventory().playerHasItem(QuestConstants.AIR_TALISMAN))
					return false;
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Welcome adventurer, to the world renowned",
													"Wizards' Tower. How may I help you?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Nothing thanks. I'm just looking around.",
													"What are you doing down here?",
													"I'm looking for the head wizard.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("I'm looking for the head wizard.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Oh, you are, are you?",
													"And just why would you be doing that?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("The Duke of Lumbridge sent me to find him. I have",
														"this weird talisman he found. He said the head wizard",
														"would be very interested in it.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Did he now? HmmmMMMMMmmmmm.",
													"Well that IS interesting. Hand it over then adventurer,",
													"let me see what all the hubbub about it is.",
													"Just some amulet I'll wager.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendOption("Ok, here you are.",
													"No, I'll only give it to the head wizard.");
					return true;
				}
				if(chatId == 8){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Ok, here you are.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 9){
					player.getDialogue().sendStatement("You hand the Talisman to the wizard.");
					return true;
				}
				if(chatId == 10){
					if(player.getInventory().playerHasItem(QuestConstants.AIR_TALISMAN)){
						player.questStage[this.getId()] = 3;
						player.getInventory().removeItem(new Item(QuestConstants.AIR_TALISMAN, 1));
						player.getDialogue().sendNpcChat("Wow! This is... incredible!",Dialogues.CONTENT);
						player.getDialogue().setNextChatId(1);
						return true;
					}
				}
			}//end of stage 2
			if(stage == 3){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Th-this talisman you brought me...! It is the last piece",
													"of the puzzle, I think! Finally! The legacy of our",
													"ancestors... it will return to us once more!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("I need time to study this, "+player.getUsername()+". Can you please",
													"do me this task while I study this talisman you have",
													"brought me? In the mighty town of Varrock, which",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("is located North East of here, there is a certain shop",
													"that sells magical runes. I have in this package all of the",
													"research I have done relating to the Rune Stones, and",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("reguire somebody to take them to the shopkeeper so that",
													"he may share my research and offer me his insights.",
													"Do this thing for me, and bring back what he gives you,",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("and if my suspicions are correct, I will let you into the",
													"knowledge of one of the greatest secrets this world has",
													"ever known! A secret so powerful that it destroyed the",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("original Wizards' Tower all of those centuries",
													"ago! My research, combined with this mysterious",
													"talisman... I cannot believe the answer to",
													"the mysteries is so close now!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Do this thing for me "+player.getUsername()+". Be rewarded in a",
													"way you can never imagine.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendOption("Yes, certainly.",
													"No, I'm busy.");
					return true;
				}
				if(chatId == 9){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yes, certainly.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(10);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Take this package, and head directly North",
													"from here, through Draynor Village, until you reach",
													"the Barbarian Village. Then head East from there",
													"until you reach Varrock.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("Once in Varrock, take this package to the owner of the",
													"rune shop. His name is Aubury. You may find it",
													"helpful to ask one of Varrock's citizens for directions,",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("as Varrock can be confusing place for the first time",
													"visitor. He will give you a special item - bring it back to",
													"me, and I shall show you the mystery of the runes...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendStatement("The head wizard gives you a package.");
					return true;
				}
				if(chatId == 14){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.RESEARCH_PACKAGE, 1));
					player.questStage[this.getId()] = 4;
					player.getDialogue().sendNpcChat("Best of luck with your quest, "+player.getUsername()+".",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}	
			}//end of stage 3
			if(stage == 4 || stage == 6){
				if(stage == 6 && !player.getInventory().playerHasItem(QuestConstants.NOTES))
					return false;
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Welcome adventurer, to the world renowned",
													"Wizards' Tower. How may I help you?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Ah, "+player.getUsername()+". How goes your quest? Have you",
													"delivered the research notes to my friend Aubury yet?",Dialogues.CONTENT);
					if(stage == 4 && !player.hasItem(QuestConstants.RESEARCH_PACKAGE))
						player.getDialogue().setNextChatId(3);
					if(stage == 4 && player.hasItem(QuestConstants.RESEARCH_PACKAGE))
						player.getDialogue().setNextChatId(7);
					if(stage == 6)
						player.getDialogue().setNextChatId(8);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("No... I lost them.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Well fortunately I have another copy of it.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendStatement("The head wizard gives you a package.");
					return true;
				}
				if(chatId == 6){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.RESEARCH_PACKAGE, 1));
					player.getDialogue().sendNpcChat("Try not to lose it this time.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("Not yet.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendPlayerChat("Yes, I have. He gave me some research notes",
														"to pass on to you.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("May I have his notes then?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendPlayerChat("Sure. I have them here.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("Well, before you hand them over to me, as you",
													"have been nothing but truthful with me to this point,",
													"and I admire that in adventurer, I will let you",
													"into the secret of our research.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("Now as you may or may not know, many",
													"centuries ago, the wizards at this Tower",
													"learnt the secret of creating Rune Stones, which",
													"allowed us to cast Magic very easily.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("When this Tower was burnt down the secret of",
													"creating runes was lost to us for all time... except it",
													"wasn't. Some months ago, while searching these ruins",
													"for information from the old days,",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("I came upon a scroll, almost destroyed, that detailed a",
													"magical rock deep in the icefields of the North, closed off",
													"from access by anything other than magical means.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendNpcChat("This rock was called the 'Rune Essence' by the",
													"magicians who studied its powers. Apparently, by simply",
													"breaking a chunk from it, a Rune Stone could be",
													"fashioned very quickly and easily at certain",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("elemental altars that were scattered across the land",
													"back then. Now, this is an interesting little piece of",
													"history, but not much use to us as modern wizards",
													"without access to the Rune Essence,",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendNpcChat("or these elemental altars. This is where you and",
													"Aubury come into this story. A few weeks back,",
													"Aubury discovered in a standard delivery of runes",
													"to his store, a parchment detailing a",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendNpcChat("teleportation spell that he had never come across",
													"before. To his shock, when cast it took him to a",
													"strange rock he had never encountered before...",
													"yet that felt strangely familiar...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 19){
					player.getDialogue().sendNpcChat("As I'm sure you have now guessed, he had discovered a",
													"portal leading to the mythical Rune Essence. As soon as",
													"he told me of this spell, I saw the importance of his find,",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 20){
					player.getDialogue().sendNpcChat("for if we could but find the elemental altars spoken",
													"of in the ancient texts, we would once more be able",
													"to create runes as our ancestors had done! It would",
													"be the saviour of the wizards' art!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 21){
					player.getDialogue().sendPlayerChat("I'm still not sure how I fit into",
														"this little story of yours...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 22){
					player.getDialogue().sendNpcChat("You haven't guessed? This talisman you brought me...",
													"it is the key to the elemental altar of air! When",
													"you hold it next, it will direct you towards",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 23){
					player.getDialogue().sendNpcChat("the entrance to the long forgotten Air Altar! By",
													"bringing pieces of the Rune Essence to the Air Temple,",
													"you will be able to fashion your own Air Runes!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 24){
					player.getDialogue().sendNpcChat("And this is not all! By finding other talismans similar",
													"to this one, you will eventually be able to to craft every",
													"rune that is available on this world! Just",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 25){
					player.getDialogue().sendNpcChat("as our ancestors did! I cannot stress enough what a",
													"find this is! Now, due to the risks involved of letting",
													"this mighty power fall into the wrong hands",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 26){
					player.getDialogue().sendNpcChat("I will keep the teleport skill to the Rune Essence",
													"a closely guarded secret, shared only by myself",
													"and those Magic users around the world",
													"whom I trust enough to keep it.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 27){
					player.getDialogue().sendNpcChat("This means that if any evil power should discover",
													"the talisman required to enter the elemental",
													"temples, we will be able to prevent their access",
													"to the Rune Essence and prevent",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 28){
					player.getDialogue().sendNpcChat("tragedy befalling this world. I know not where the",
													"temples are located, nor do I know where the talismans",
													"have been scattered to in this land, but I now",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 29){
					player.getDialogue().sendNpcChat("return your Air Talisman to you. Find the Air",
													"Temple, and you will be able to charge your Rune",
													"Essences to become Air Runes at will. Any time",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 30){
					player.getDialogue().sendNpcChat("you wish to visit the Rune Essence, speak to me",
													"or Aubury and we will open a portal to that",
													"mystical place for you to visit.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 31){
					player.getDialogue().sendPlayerChat("So only you, Aubury, 0r10n and M1g3 know the",
														"teleport spell to the Rune Essence?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 32){
					player.getDialogue().sendNpcChat("No... there are others... whom I will tell of your",
													"authorisation to visit that place. When you speak",
													"to them, they will know you, and grant you",
													"access to that place when asked.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 33){
					player.getDialogue().sendNpcChat("Use the Air Talisman to locate the air temple,",
													"and use any further talismans you find to locate",
													"the other missing elemental temples.",
													"Now... my research notes please?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 34){
					player.getDialogue().sendStatement("You hand the head wizard the research notes.", 
														"He hands you back the Air Talisman.");
					return true;
				}
				if(chatId == 35){
					if(player.getInventory().playerHasItem(QuestConstants.NOTES)){
						player.getInventory().removeItem(new Item(QuestConstants.NOTES, 1));
						player.getInventory().addItemOrDrop(new Item(QuestConstants.AIR_TALISMAN, 1));
						questCompleted(player);
						player.getDialogue().endDialogue();
						return true;
					}
				}
			}
		}//end of npc 300
		if(npcId == QuestConstants.AUBURY){
			if(stage == 4){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Do you want to buy some runes?",Dialogues.CONTENT);
					if(!player.getInventory().playerHasItem(QuestConstants.RESEARCH_PACKAGE))
						player.getDialogue().setNextChatId(3);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Yes please!",
													"Oh, it's a rune shop. No thank you, then.",
													"I have been sent here with a package for you.");
					player.getDialogue().setNextChatId(4);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendOption("Yes please!",
													"Oh, it's a rune shop. No thank you, then.");
					return true;
				}
				if(chatId == 4){
					if(optionId == 1){
						Shops.openShop(player, npcId);
						player.getDialogue().dontCloseInterface();
						return false;
					}
					if(optionId == 2){
						player.getDialogue().endDialogue();
						return false;
					}
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("I have been sent here with a package for you. It's from",
															"the head wizard at the Wizards' Tower.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Really? But... surely he can't have..? Please, let me",
													"have it, it must be extremely important for him to have",
													"sent a stranger.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendStatement("You hand Aubury the research package.");
					return true;
				}
				if(chatId == 7){
					if(player.getInventory().playerHasItem(QuestConstants.RESEARCH_PACKAGE)){
						player.questStage[this.getId()] = 5;
						player.getInventory().removeItem(new Item(QuestConstants.RESEARCH_PACKAGE, 1));
						player.getDialogue().sendNpcChat("This... this is incredible. Please, give me a few moments",
														"to quickly look over this, and then talk to me again.",Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}
				}
			}//end of stage 4
			if(stage == 5){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("My gratitude to you adventurer for bringing me these",
													"research notes. I notice that you brought the head",
													"wizard a special talisman that was the key to our finally",
													"unlocking the puzzle.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Combined with the information I had already collated",
													"regarding the Rune Essence, I think we have finally",
													"unlocked the power to",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("...no. I am getting ahead of myself. Please take this",
													"summary of my research back to the head wizard at",
													"the Wizards' Tower. I trust his judgement on whether",
													"to let you in on our little secret or not.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendStatement("Aubury gives you his research notes.");
					player.getInventory().addItemOrDrop(new Item(QuestConstants.NOTES, 1));
					player.questStage[this.getId()] = 6;
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage == 6){
				if(player.getInventory().playerHasItem(QuestConstants.NOTES))
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I lost your notes.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Well fortunately I have another copy of it.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendStatement("Aubury gives you his research notes.");
					return true;
				}
				if(chatId == 4){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.NOTES, 1));
					player.getDialogue().sendNpcChat("Try not to lose it this time.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 553
		return false;
	}

}
