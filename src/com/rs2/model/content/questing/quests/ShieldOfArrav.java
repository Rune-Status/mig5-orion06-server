package com.rs2.model.content.questing.quests;

import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class ShieldOfArrav extends Quest {
	
	final int rewardQP = 1;
	
	public ShieldOfArrav(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to Reldo in Varrock's",
							"Palace library, or by speaking to Charlie the Tramp near",
							"the Blue Moon Inn in Varrock.",
							"I will need a friend to help me and some combat experience",
							"may be an advantage."};
			return text;
		}
		if(stage == 2){
			String text[] = {"Reldo says there is a quest hidden in one of the books in",
							"his library somewhere. I should look for it and see."};
			return text;	
		}
		if(stage == 3){
			String text[] = {"I should show the book to Reldo now."};
			return text;	
		}
		if(stage == 4){
			String text[] = {"Reldo told me to talk to baraek about Phoenix Gang."};
			return text;	
		}
		if(stage == 5){
			String text[] = {"Baraek gave me the location of the Phoenix Gang HQ, and",
							"revealed they are operating under the name of",
							"VTAM corporation."};
			return text;	
		}
		if(stage == 99){
			String text[] = {"I spoke to Charlie the tramp, who gave me the location",
							"of the Black Arm gang HQ."};
			return text;
		}
		if(stage == 6 && player.gang == QuestConstants.BLACK_ARM_GANG){
			String text[] = {"Katrine said if I wanted to join the Black Arm Gang, I'd",
							"have to steal two Phoenix crossbows from the rival gang.",
							"Maybe Charlie the tramp can give me some ideas about",
							"how to do this."};
			return text;
		}
		if(stage == 7 && player.gang == QuestConstants.BLACK_ARM_GANG){
			String text[] = {"I should now enter the Black Arm Gang HQ and search",
							"for the shield."};
			return text;
		}
		if(stage == 6 && player.gang == QuestConstants.PHOENIX_GANG){
			if(!player.hasItem(QuestConstants.SCROLL)){
				String text[] = {"I was told to kill Jonny the Beard in Blue Moon Inn",
								"and bring back intelligence report to Phoenix Gang HQ",
								"from him."};
				return text;
			} else {
				String text[] = {"I should bring the intelligence report back to",
								"Phoenix Gang HQ."};
				return text;
			}
		}
		if(stage == 7 && player.gang == QuestConstants.PHOENIX_GANG){
			String text[] = {"I should now enter the Phoenix Gang HQ and search",
							"for the shield."};
			return text;
		}
		if(stage == 8){
			if(!player.hasItem(QuestConstants.CERTIFICATE)){
				String text[] = {"Either you or your friend should now go talk to",
								"curator of Varrock museum with both of the shield",
								"halves with him."};
				return text;
			} else {
				String text[] = {"I should now go show the certificate to King roald",
								"to claim my reward."};
				return text;
			}
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "1 Quest Point", "600 Coins"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("600 Coins", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getInventory().addItemOrDrop(new Item(QuestConstants.COINS, 600));
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.PHOENIX_CROSSBOW);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean getQuestPickups(final Player player, int itemId, int stage){
		if(itemId == QuestConstants.PHOENIX_CROSSBOW){
			Npc npc = Npc.getNpcById(QuestConstants.WEAPONS_MASTER);
			if((!player.goodDistanceEntity(npc, 10) || !player.inEntity(npc)) && player.getPosition().getZ() != npc.getPosition().getZ()){
				return false;
			}
			if(!npc.isDead() && player.gang == QuestConstants.BLACK_ARM_GANG){
				player.getDialogue().setLastNpcTalk(QuestConstants.WEAPONS_MASTER);
				player.getDialogue().sendNpcChat("Stop! Thief!",Dialogues.CONTENT);
				player.getDialogue().endDialogue();
				CombatManager.attack(npc, player);
				return true;
			} else if(!npc.isDead() && player.gang == QuestConstants.PHOENIX_GANG){
				player.getDialogue().setLastNpcTalk(QuestConstants.WEAPONS_MASTER);
				player.getDialogue().sendNpcChat("Hey, you are not allowed to take it!",Dialogues.CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 2398 && objectX == 3251 && objectY == 3385){//Phoenix weapon storage door
			if(player.getInventory().playerHasItem(QuestConstants.PHOENIX_WEAPON_STORAGE_KEY)){
				player.getActionSender().walkTo(0, player.getPosition().getY() < 3386 ? 1 : -1, true);
				player.getActionSender().walkThroughDoor(2398, 3251, 3385, 0);
				player.getActionSender().sendMessage("You unlock the door.");
				return true;
			} else {
				player.getActionSender().sendMessage("It is locked.");
				return true;
			}
		}
		if(objectId == 2397 && objectX == 3247 && objectY == 9779){//Phoenix HQ door
			if(player.gang == QuestConstants.PHOENIX_GANG && (player.questStage[this.getId()] == 1 || player.questStage[this.getId()] >= 7)){
				player.getActionSender().walkTo(0, player.getPosition().getY() < 9780 ? 1 : -1, true);
				player.getActionSender().walkThroughDoor(2397, 3247, 9779, 0);
				player.getActionSender().sendMessage("The door automatically opens for you.");
				return true;
			} else {
				player.getDialogue().setLastNpcTalk(QuestConstants.STRAVEN);
				player.getDialogue().sendNpcChat("Hey! You can't go in there. Only authorised personnel",
												"of the VTAM Corporation are allowed beyond this point.",Dialogues.CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		}
		if(objectId == 2399 && objectX == 3185 && objectY == 3388){//Black Arm HQ door
			if(player.gang == QuestConstants.BLACK_ARM_GANG && (player.questStage[this.getId()] == 1 || player.questStage[this.getId()] >= 7)){
				player.getActionSender().walkTo(0, player.getPosition().getY() < 3388 ? 1 : -1, true);
				player.getActionSender().walkThroughDoor(2399, 3185, 3388, 0);
				player.getActionSender().sendMessage("You hear heavy bolts being drawn back as the door is unlocked.");
				return true;
			} else {
				player.getActionSender().sendMessage("It is locked.");
				return true;
			}
		}
		if(objectId == 2403 && objectX == 3235 && objectY == 9761){
			ObjectHandler.getInstance().removeObject(3235, 9761, 0, 0);
			ObjectHandler.getInstance().addObject(new GameObject(2404, 3235, 9761, 0, 0, 10, 2403, 999999999), true);
			return true;
		}
		if(objectId == 2400 && objectX == 3189 && objectY == 3385){
			ObjectHandler.getInstance().removeObject(3189, 3385, 1, 0);
			ObjectHandler.getInstance().addObject(new GameObject(2401, 3189, 3385, 1, 2, 10, 2400, 999999999), true);
			return true;
		}
		if(objectId == 2401 && objectX == 3189 && objectY == 3385){
			ObjectHandler.getInstance().removeObject(3189, 3385, 1, 0);
			ObjectHandler.getInstance().addObject(new GameObject(2400, 3189, 3385, 1, 2, 10, 2400, 999999999), true);
			return true;
		}
		return false;
	}
	
	public boolean handleSecondClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 2404 && objectX == 3235 && objectY == 9761){
			ObjectHandler.getInstance().removeObject(3235, 9761, 0, 0);
			ObjectHandler.getInstance().addObject(new GameObject(2403, 3235, 9761, 0, 0, 10, 2403, 999999999), true);
			return true;
		}
		return false;
	}
	
	public boolean getObjectDialog(int clickId, final Player player, int objectId, int chatId, int optionId, int npcChatId, int x, int y, int stage){
		if(objectId == 2402 && x == 3212 && y == 3493 && clickId == 2){
			if(stage >= 2 && !player.hasItem(QuestConstants.SHIELD_OF_ARRAV_BOOK)){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Aha! 'The Shield Of Arrav'! Exactly what I was looking",
														"for.", Dialogues.CONTENT);
				}
				if(chatId == 2){
					player.getDialogue().sendStatement("You take the book from the bookcase.");
				}
				if(chatId == 3){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.SHIELD_OF_ARRAV_BOOK, 1));
					player.getDialogue().endDialogue();
					player.getActionSender().removeInterfaces();
					player.questStage[this.getId()] = 3;
				}
				return true;
			}
		}
		if(objectId == 2404 && x == 3235 && y == 9761 && clickId == 1){
			if(!player.hasItem(QuestConstants.SHIELD_OF_ARRAV_LEFT_HALF) && stage != 1){
				if(chatId == 1){
					player.getDialogue().sendStatement("You search the chest.");
				}
				if(chatId == 2){
					player.getDialogue().sendItem1Line("You find half of a shield, which you take.", new Item(QuestConstants.SHIELD_OF_ARRAV_LEFT_HALF, 1));
				}
				if(chatId == 3){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.SHIELD_OF_ARRAV_LEFT_HALF, 1));
					player.questStage[this.getId()] = 8;
					player.getDialogue().endDialogue();
					player.getActionSender().removeInterfaces();
				}
				return true;
			}
		}
		if(objectId == 2401 && x == 3189 && y == 3385 && clickId == 2){
			if(!player.hasItem(QuestConstants.SHIELD_OF_ARRAV_RIGHT_HALF) && stage != 1){
				if(chatId == 1){
					player.getDialogue().sendItem1Line("You find half of a shield, which you take.", new Item(QuestConstants.SHIELD_OF_ARRAV_RIGHT_HALF, 1));
				}
				if(chatId == 2){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.SHIELD_OF_ARRAV_RIGHT_HALF, 1));
					player.questStage[this.getId()] = 8;
					player.getDialogue().endDialogue();
					player.getActionSender().removeInterfaces();
				}
				return true;
			}
		}
		return false;
	}
	
	public String[] getBookText(int page){
		if(page == 0){
			String text[] = {"The Shield of Arrav",//title
							"",//page1
							"",//page2
							"The Shield of Arrav",//line1 - page1
							"",
							"by A. R. Wright",
							"",
							"",
							"",
							"Arrav is probably the best",
							"known hero of the 4th",
							"age. Many legends are",
							"told of his heroics. One",
							"surviving artefact from",
							"the 4th age is a fabulous",//line1 - page2
							"shield.",
							"",
							"This shield is believed to",
							"have once belonged to",
							"Arrav and is now indeed",
							"known as the Shield of",
							"Arrav. For over 150",
							"years it was the prize",
							"piece in the royal",
							"museum of Varrock."};
			return text;
		}
		if(page == 1){
			String text[] = {"The Shield of Arrav",//title
							"",//page1
							"",//page2
							"However, in the year 143",//line1 - page1
							"of the fifth age a gang of",
							"thieves called the Phoenix",
							"Gang broke into the",
							"museum and stole the",
							"shield in a daring raid. As",
							"a result, the current",
							"ruler, King Roald, put a",
							"1200 gold bounty (a",
							"massive sum of money in",
							"those days) on the return",
							"of the shield, hoping that",//line1 - page2
							"one of the culprits would",
							"betray his fellows out of",
							"greed."};
			return text;
		}
		if(page == 2){
			String text[] = {"The Shield of Arrav",//title
							"",//page1
							"",//page2
							"This tactic did not work",//line1 - page1
							"however, and the thieves",
							"who stole the shield have",
							"since gone on to become",
							"the most powerful crime",
							"gang in Varrock, despite",
							"making an enemy of the",
							"Royal Family many",
							"years ago.",
							"",
							"",
							"The reward for the",//line1 - page2
							"return of the shield still",
							"stands."};
			return text;
		}
		return null;
	}
	
	void clearBookStrings(final Player player){
		player.getActionSender().sendString("", 14165);
		player.getActionSender().sendString("", 14166);
		for(int id2 = 843; id2 <= 864; id2++){
			player.getActionSender().sendString("", id2);
		}
	}
	
	int bookPages = 2;
	
	void openBookPage(final Player player, int page){
		clearBookStrings(player);
		String text[] = getBookText(page);
		player.getActionSender().sendString(text[0], 903);
		player.getActionSender().sendString(text[1], 14165);
		player.getActionSender().sendString(text[2], 14166);
		for(int id2 = 3; id2 < text.length; id2++){
			player.getActionSender().sendString(text[id2], 843+id2-3);
		}
		player.getActionSender().sendFrame171((player.readingBookPage == 0 ? 1 : 0), 840);//previous page
		player.getActionSender().sendFrame171((player.readingBookPage == bookPages ? 1 : 0), 842);//next page
	}
	
	public boolean handleInterfaceButton(final Player player, int buttonId, int stage){
		if(player.readingBook == QuestConstants.SHIELD_OF_ARRAV_BOOK){
			if(buttonId == 841 && player.readingBookPage < bookPages){
				player.readingBookPage++;
				openBookPage(player, player.readingBookPage);
				return true;
			}
			if(buttonId == 839 && player.readingBookPage > 0){
				player.readingBookPage--;
				openBookPage(player, player.readingBookPage);
				return true;
			}
		}
		return false;
	}
	
	public boolean handleFirstClickItem(final Player player, int interfaceId, int itemId, int stage){
		if(interfaceId == 3214){//inventory
			if(itemId == QuestConstants.SHIELD_OF_ARRAV_BOOK){
				openBookPage(player, 0);
				player.readingBook = itemId;
				player.readingBookPage = 0;
				player.getActionSender().sendInterface(837);
				return true;
			}
		}
		return false;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		//player.questStage[this.getId()] = 0;
		if(npcId == QuestConstants.RELDO){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hello stranger.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("I'm in search of a quest.",
													"Do you have anything to trade?",
													"What do you do?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I'm in search of a quest.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Hmmm. I don't... believe there are any here...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Let me think actually...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Ah yes. I know. If you look in a book called 'The Shield",
													"of Arrav', you'll find a quest in there.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("I'm not sure where the book is mind you... but I'm",
													"sure it's around here somewhere.",Dialogues.CONTENT);
					questStarted(player);
					return true;
				}
			}//end of stage 0
			if(stage == 2){
				if(chatId == 8){
					player.getDialogue().sendPlayerChat("Thank you.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage == 3){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Ok. I've read the book. Do you know where I can find",
														"the Phoenix Gang?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("No, I don't. I think I know someone who might",
													"however.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("If I were you I would talk to Baraek, the fur trader in",
													"the market place. I've heard he has connections with the",
													"Phoenix Gang.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("Thanks, I'll try that!", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					player.questStage[this.getId()] = 4;
					return true;
				}
			}
		}//end of npc 2661
		if(npcId == QuestConstants.BARAEK){
			if(stage == 4){
				if(chatId == 1){
					player.getDialogue().sendOption("Can you tell me where I can find the Phoenix Gang?",
													"Can you sell me some furs?",
													"Hello. I am in search of a quest.");
					return true;
				}
				if(chatId == 2){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Can you tell me where I can find the Phoenix Gang?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(3);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Sh sh sh, not so loud! You don't want to get me in",
													"trouble!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("So DO you know where they are?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("I may do.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("But I don't want to get into trouble for revealing their",
													"hideout.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Of course, if I was, say 20 gold coins richer I may",
													"happen to be more inclined to take that sort of risk...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendOption("Okay. Have 20 gold coins.",
													"No. I don't like things like bribery.",
													"Yes. I'd like to be 20 gold coins richer too.");
					return true;
				}
				if(chatId == 9){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Okay. Have 20 gold coins.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(10);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 10){
					if(player.getInventory().playerHasItem(QuestConstants.COINS, 20)){
						player.getDialogue().sendNpcChat("Ok, to get to the gang hideout, enter Varrock through",
														"the south gate. Then, if you take the first turning east,",
														"somewhere along there is an alleyway to the south. The",
														"door at the end of there is the entrance to the Phoenix",Dialogues.CONTENT);
						player.getInventory().removeItem(new Item(QuestConstants.COINS, 20));
						player.questStage[this.getId()] = 5;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
					return true;
				}
			}
			if(stage == 5){
				if(chatId == 11){
					player.getDialogue().sendNpcChat("Gang. They're operating there under the name of the",
													"VTAM Corporation. Be careful. The Phoenixes ain't",
													"the types to be messed about.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendPlayerChat("Thanks!", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 547
		if(npcId == QuestConstants.TRAMP){
			if(stage == 0 || (stage >= 2 && stage < 6)){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Spare some change guv?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Who are you?",
													"Sorry, I haven't got any.",
													"Go get a job!",
													"Ok. Here you go.",
													"Is there anything down this alleyway?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 5){
						player.getDialogue().sendPlayerChat("Is there anything down this alleyway?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Funny you should mention that...there is actually.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("The ruthless and notorious criminal gang known as the",
													"Black Arm Gang have their headquarters down there.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendOption("Thanks for the warning!",
													"Do you think they would let me join?");
					return true;
				}
				if(chatId == 7){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Do you think they would let me join?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(8);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("You never know. You'll find a lady down there called",
													"Katrine. Speak to her.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("But don't upset her, she's pretty dangerous.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					if(player.questStage[this.getId()] == 0){
						questStarted(player);
						player.questStage[this.getId()] = 99;
					}
					return true;
				}
			}
		}//end of npc 641
		if(npcId == QuestConstants.STRAVEN){
			if(stage == 5){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("What's through that door?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Hey! You can't go in there. Only authorised personnel",
													"of the VTAM Corporation are allowed beyond this point.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendOption("I know who you are!",
													"How do I get a job with the VTAM corporation?",
													"Why not?");
					return true;
				}
				if(chatId == 4){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I know who you are!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Really.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Well?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Who are we then?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendPlayerChat("This is the headquarters of the Phoenix Gang, the most",
														"powerful crime syndicate this city has ever seen!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("No, this is a legitimate business run by legitimate",
													"businessmen.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Supposing we were this crime gang however, what would",
													"you want with us?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendOption("I'd like to offer you my services.",
													"I want nothing. I was just making sure you were them.");
					return true;
				}
				if(chatId == 12){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I'd like to offer you my services.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(13);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("You mean you'd like to join the Phoenix Gang?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("Well, obviously I can't speak for them, but the Phoenix",
													"Gang doesn't let people join just like that.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendNpcChat("You can't be too careful, you understand.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("Generally someone has to prove their loyalty before",
													"they can join.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendPlayerChat("How would I go about doing that?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendNpcChat("Obviously, I would have no idea about that.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 19){
					player.getDialogue().sendNpcChat("Although having said that, a rival gang of ours, er,",
													"theirs, called the Black Arm Gang is supposedly meeting",
													"a contact from Port Sarim today in the Blue Moon",
													"Inn.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 20){
					player.getDialogue().sendNpcChat("The Blue Moon Inn is just by the south entrance to",
													"this city, and supposedly the name of the contact is",
													"Jonny the Beard.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 21){
					player.getDialogue().sendNpcChat("OBVIOUSLY I know NOTHING about the dealings",
													"of the Phoenix Gang, but I bet if SOMEBODY were",
													"to kill him and bring back his intelligence report, they",
													"would be considered loyal enough to join.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 22){
					player.getDialogue().sendPlayerChat("Ok, I'll get right on it.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					player.questStage[this.getId()] = 6;
					player.gang = QuestConstants.PHOENIX_GANG;
					return true;
				}
			}
			if(stage == 6 && player.gang == QuestConstants.PHOENIX_GANG){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("How's your little mission going?",Dialogues.CONTENT);
					if(player.getInventory().playerHasItem(QuestConstants.SCROLL))
						player.getDialogue().setNextChatId(3);
					else
						player.getDialogue().setNextChatId(2);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("I'm still working on it.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("I have the intelligence report!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Let's see it then.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendStatement("You hand over the report. The man reads the report.");
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Yes. Yes, this is very good.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Ok! You can join the Phoenix Gang! I am Straven, one",
													"of the gang leaders.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendPlayerChat("Nice to meet you.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Take this key.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					if(player.getInventory().playerHasItem(QuestConstants.SCROLL)) {
						player.getInventory().removeItem(new Item(QuestConstants.SCROLL, 1));
						player.getInventory().addItemOrDrop(new Item(QuestConstants.PHOENIX_WEAPON_STORAGE_KEY, 1));
						player.getDialogue().sendItem1Line("Straven hands you a key.", new Item(QuestConstants.PHOENIX_WEAPON_STORAGE_KEY, 1));
						player.questStage[this.getId()] = 7;
						player.getDialogue().setNextChatId(2);
					} else {
						player.getDialogue().endDialogue();
					}
					return true;
				}
			}
			if(stage >= 7 && player.gang == QuestConstants.PHOENIX_GANG){
				if(chatId == 1 && !player.hasItem(QuestConstants.PHOENIX_WEAPON_STORAGE_KEY)){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.PHOENIX_WEAPON_STORAGE_KEY, 1));
					player.getDialogue().sendItem1Line("Straven hands you a key.", new Item(QuestConstants.PHOENIX_WEAPON_STORAGE_KEY, 1));
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("This key will give you access to our weapons supply",
													"depot round the front of this building.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 644
		if(npcId == QuestConstants.KATRINE){
			if((stage >= 2 && stage < 6 || stage == 99)){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("What is this place?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("It's a private business. Can I help you at all?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendOption("I've heard you're the Black Arm Gang.",
													"What sort of business?",
													"I'm looking for fame and riches.");
					return true;
				}
				if(chatId == 4){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I've heard you're the Black Arm Gang.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Who told you that?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendOption("I'd rather not reveal my sources.",
													"It was Charlie, the tramp outside.",
													"Everyone knows - it's no great secret.");
					return true;
				}
				if(chatId == 7){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("It was Charlie, the tramp outside.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(8);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Is that guy still out there? He's getting to be a",
													"nuisance. Remind me to send someone to kill him.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("So now you've found us, what do you want?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendOption("I want to become a member of your gang.",
													"I want some hints for becoming a thief.",
													"I'm looking for the door out of here.");
					return true;
				}
				if(chatId == 11){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I want to become a member of your gang.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(12);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("How unusual.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("Normally we recruit for our gang by watching local",
													"thugs and thieves in action. People don't normally waltz",
													"in here saying 'hello, can I play'.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("How can I be sure you can be trusted?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendOption("Well, you can give me a try can't you?",
													"Well, people tell me I have an honest face.");
					return true;
				}
				if(chatId == 16){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Well, you can give me a try can't you?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(17);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 17){
					player.getDialogue().sendNpcChat("I'm not so sure.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendNpcChat("Thinking about it... I may have a solution actually.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 19){
					player.getDialogue().sendNpcChat("Our rival gang - the Phoenix Gang - has a weapons",
													"stash a little east of here.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 20){
					player.getDialogue().sendNpcChat("We're fresh out of crossbows, so if you could steal a",
													"couple of crossbows for us it would be very much",
													"appreciated.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 21){
					player.getDialogue().sendNpcChat("Then I'll be happy to call you a Black Arm.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 22){
					player.getDialogue().sendPlayerChat("Sounds simple enough. Any particular reason you need",
														"two of them?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 23){
					player.getDialogue().sendNpcChat("I have an idea for framing a local merchant who is",
													"refusing to pay our, very reasonable, 'keep-your-life-",
													"pleasant' insurance rates. I need two phoenix crossbows;",
													"one to kill somebody important with and other to",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 24){
					player.getDialogue().sendNpcChat("hide in the merchant's house where the local law can",
													"find it! When they find it, they'll suspect him of",
													"murdering the target for the Phoenix gang and,",
													"hopefully, arrest the whole gang! Leaving us as the only",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 25){
					player.getDialogue().sendNpcChat("thieves gang in Varrock! Brilliant, eh?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 26){
					player.getDialogue().sendPlayerChat("Yeah, brilliant. So who are you planning to murder?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 27){
					player.getDialogue().sendNpcChat("I haven't decided yet, but it'll need to be somebody",
													"important. Say, why you being so nosey? You aren't",
													"with the law are you?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 28){
					player.getDialogue().sendPlayerChat("No, no! Just curious.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 29){
					player.getDialogue().sendNpcChat("You'd better just keep your mouth shut about this plan,",
													"or I'll make sure it stays shut for you. Now, are you",
													"going to go get those crossbows or not?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 30){
					player.getDialogue().sendOption("Ok, no problem.",
													"Sounds a little tricky. Got anything easier?");
					return true;
				}
				if(chatId == 31){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Ok, no problem.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(32);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 32){
					player.getDialogue().sendNpcChat("Great! You'll find the Phoenix gang's weapon stash just",
													"next to a temple, due east of here.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					player.questStage[this.getId()] = 6;
					player.gang = QuestConstants.BLACK_ARM_GANG;
					return true;
				}
			}
			if(stage == 6 && player.gang == QuestConstants.BLACK_ARM_GANG){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Have you got those crossbows for me yet?",Dialogues.CONTENT);
					if(player.getInventory().playerHasItem(QuestConstants.PHOENIX_CROSSBOW, 2))
						player.getDialogue().setNextChatId(3);
					else
						player.getDialogue().setNextChatId(2);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("Not yet.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Yes, I have.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendStatement("You give the crossbows to Katrine.");
					return true;
				}
				if(chatId == 5){
					if(player.getInventory().playerHasItem(QuestConstants.PHOENIX_CROSSBOW, 2)) {
						player.getInventory().removeItem(new Item(QuestConstants.PHOENIX_CROSSBOW, 2));
						player.getDialogue().sendNpcChat("Ok. You can join our gang now. Feel free to enter",
														"any of the rooms of the ganghouse.",Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						player.questStage[this.getId()] = 7;
					} else {
						player.getDialogue().endDialogue();
					}
					return true;
				}
			}
		}//end of npc 642
		if(npcId == QuestConstants.CURATOR){
			if(stage == 8){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Welcome to the museum of Varrock.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					if(player.getInventory().playerHasItem(QuestConstants.SHIELD_OF_ARRAV_LEFT_HALF, 1) && player.getInventory().playerHasItem(QuestConstants.SHIELD_OF_ARRAV_RIGHT_HALF, 1)) {
						player.getDialogue().sendPlayerChat("I have the shield of Arrav here. Can I get a",
															"reward?", Dialogues.CONTENT);
					} else {
						player.getDialogue().endDialogue();
						player.getActionSender().removeInterfaces();
					}
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("The Shield of Arrav! Goodness, the Museum has been",
													"searching for that for years! The late King Roald II",
													"offered a reward for it years ago!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("Well, I'm here to claim it.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Let me have a look at it first.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendStatement("The curator peers at the shield.");
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("This is incredible!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("That shield has been missing for over twenty-five years!.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Leave the shield here with me and I'll write you out a",
													"certificate saying that you have returned the shield, so",
													"that you can claim your reward from the King.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendPlayerChat("Can I have two certificates please?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("Yes, certainly. Please hand over the shield.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendStatement("You hand over the shield halves.");
					return true;
				}
				if(chatId == 13){
					if(player.getInventory().playerHasItem(QuestConstants.SHIELD_OF_ARRAV_LEFT_HALF, 1) && player.getInventory().playerHasItem(QuestConstants.SHIELD_OF_ARRAV_RIGHT_HALF, 1)) {
						player.getInventory().removeItem(new Item(QuestConstants.SHIELD_OF_ARRAV_LEFT_HALF, 1));
						player.getInventory().removeItem(new Item(QuestConstants.SHIELD_OF_ARRAV_RIGHT_HALF, 1));
						player.getInventory().addItemOrDrop(new Item(QuestConstants.CERTIFICATE, 2));
						player.getDialogue().sendItem1Line("The curator writes out two certificates.", new Item(QuestConstants.CERTIFICATE, 1));
					} else {
						player.getDialogue().endDialogue();
					}
					return true;
				}
				if(chatId == 14){
					player.getDialogue().endDialogue();
					player.getActionSender().removeInterfaces();
				}
			}
		}//end of npc 646
		if(npcId == QuestConstants.KING_ROALD){
			if(stage == 8){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Greetings, your majesty.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					if(player.getInventory().playerHasItem(QuestConstants.CERTIFICATE, 1)){
						player.getDialogue().sendPlayerChat("Your majesty, I have come to claim the reward for the",
															"return of the Shield Of Arrav.", Dialogues.CONTENT);
					} else {
						player.getDialogue().endDialogue();
						player.getActionSender().removeInterfaces();
					}
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendItem1Line("You show the certificate to the king.", new Item(QuestConstants.CERTIFICATE, 1));
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("My goodness! This claim is for the reward offered by",
													"my father many years ago!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("I never thought I would live to see the day when",
													"someone came forward to claim this reward!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("I heard that you found half the shield, so I will give",
													"you half of the bounty. That comes to exactly 600gp!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendItem1Line("You hand over the certificate. The king gives you 600gp.", new Item(QuestConstants.COINS, 600));
					return true;
				}
				if(chatId == 8){
					if(player.getInventory().playerHasItem(QuestConstants.CERTIFICATE, 1)){
						player.getInventory().removeItem(new Item(QuestConstants.CERTIFICATE, 1));
						questCompleted(player);
						player.getDialogue().dontCloseInterface();
					} else {
						player.getDialogue().endDialogue();
						player.getActionSender().removeInterfaces();
					}
					return true;
				}
			}
		}//end of npc 648
		return false;
	}

}
