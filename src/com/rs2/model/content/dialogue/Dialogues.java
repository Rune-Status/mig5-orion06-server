package com.rs2.model.content.dialogue;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.content.skills.Tools.Tool;
import com.rs2.model.content.BankPin;
import com.rs2.model.content.BarrowsItem;
import com.rs2.model.content.DailyTask;
import com.rs2.model.content.Shops;
import com.rs2.model.content.dungeons.Abyss;
import com.rs2.model.content.minigames.barrows.Barrows;
import com.rs2.model.content.minigames.duelarena.RulesData;
import com.rs2.model.content.questing.QuestDefinition;
import com.rs2.model.content.randomevents.RandomEvent;
import com.rs2.model.content.randomevents.TalkToEvent;
import com.rs2.model.content.randomevents.InterfaceClicking.impl.SandwichLady;
import com.rs2.model.content.skills.Menus;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.farming.Farmers;
import com.rs2.model.content.skills.magic.Teleportation;
import com.rs2.model.content.skills.slayer.Slayer;
import com.rs2.model.content.skills.slayer.Slayer.SlayerMasterData;
import com.rs2.model.content.skills.slayer.Slayer.SlayerTipsData;
import com.rs2.model.content.treasuretrails.ClueScroll;
import com.rs2.model.content.treasuretrails.CoordinateScrolls;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcDefinition;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.BankManager;
import com.rs2.model.players.GeManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.ShopManager;
import com.rs2.model.players.item.Item;
import com.rs2.model.transport.JewelleryTeleport;
import com.rs2.model.transport.Sailing;
import com.rs2.model.transport.Sailing.ShipRoute;
import com.rs2.model.transport.Travel;
import com.rs2.model.transport.Travel.Route;
import com.rs2.util.Misc;
import com.rs2.util.Time;

public class Dialogues {

	// Dialogue expressions
	public static final int HAPPY = 588, CALM = 589, CALM_CONTINUED = 590, CONTENT = 591, EVIL = 592, EVIL_CONTINUED = 593, DELIGHTED_EVIL = 594, ANNOYED = 595, DISTRESSED = 596, DISTRESSED_CONTINUED = 597, NEAR_TEARS = 598, SAD = 599, DISORIENTED_LEFT = 600, DISORIENTED_RIGHT = 601, UNINTERESTED = 602, SLEEPY = 603, PLAIN_EVIL = 604, LAUGHING = 605, LONGER_LAUGHING = 606, LONGER_LAUGHING_2 = 607, LAUGHING_2 = 608, EVIL_LAUGH_SHORT = 609, SLIGHTLY_SAD = 610, VERY_SAD = 611, OTHER = 612, NEAR_TEARS_2 = 613, ANGRY_1 = 614, ANGRY_2 = 615, ANGRY_3 = 616, ANGRY_4 = 617;

	public static boolean startDialogue(Player player, int id) {
		player.getDialogue().resetDialogue();
		return sendDialogue(player, id, 1, 0);
	}

	public static boolean sendDialogue(Player player, int id, int chatId, int optionId) {
		return sendDialogue(player, id, chatId, optionId, id);
	}

	public static void setNextDialogue(Player player, int id, int chatId) {
		player.getDialogue().setDialogueId(id);
		player.getDialogue().setNextChatId(chatId);
	}
	
	public static boolean startDialogueObject(int clickId, Player player, int id, int x, int y) {
		player.getDialogue().resetDialogue();
		return sendDialogueObject(clickId, player, id, 1, 0, x, y);
	}

	public static boolean sendDialogueObject(int clickId, Player player, int id, int chatId, int optionId, int x, int y) {
		return sendDialogueObject(clickId, player, id, chatId, optionId, id, x, y);
	}
	
	public static boolean sendDialogueObject(int clickId, Player player, int id, int chatId, int optionId, int npcChatId, int x, int y) {
		player.getDialogue().setChatId(chatId);
		player.getDialogue().setClickId(clickId);
		player.getDialogue().setDialogueId(id);
		player.getDialogue().setDialogueType(1);
		player.getDialogue().setDialogueX(x);
		player.getDialogue().setDialogueY(y);
		player.getDialogue().setLastNpcTalk(npcChatId);
		if(player.getQuestHandler().findQuestDialogObject(clickId, id, chatId, optionId, npcChatId, x, y))
			return true;
		switch(id) {
			case 2878 : //pool @mage arena
				switch(player.getDialogue().getChatId()) {
					case 1:
						if(player.kolodionStage < 5)
							return false;
						player.getDialogue().sendStatement("You step into the pool of sparkling water. You feel energy rush",
															"through your veins.");
						return true;
					case 2:
						player.getTeleportation().forcedObjectTeleport2(2509, 4689, 0, null, 5, 804, -1, 68);
						break;
				}
			break;
			
			case 2879 : //pool @mage arena
				switch(player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendStatement("You step into the pool of sparkling water. You feel energy rush",
															"through your veins.");
						return true;
					case 2:
						player.getTeleportation().forcedObjectTeleport2(2542, 4718, 0, null, 5, 804, -1, 68);
						break;
				}
			break;
			
			case 2874 : //zamorak @mage arena
				if(player.hasGodCape()){
					player.getDialogue().sendStatement("You already have a God cape!");
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 1)
					player.getActionSender().walkToNoForceAbs(2516,4719);
				switch(player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendStatement("You kneel and begin to chant to Zamorak...");
						return true;
					case 2:
						player.getUpdateFlags().sendFaceToDirection(new Position(player.getPosition().getX(), player.getPosition().getY()+1));
						player.getUpdateFlags().sendAnimation(645);
						player.prayAtGodStatue(player, id);
						break;
					case 10:
						player.getDialogue().sendStatement("You feel a rush of energy charge through your veins. Suddenly a",
															"cape appears before you.");
						player.getDialogue().endDialogue();
						return true;
				}
			break;
			
			case 2875 : //guthix @mage arena
				if(player.hasGodCape()){
					player.getDialogue().sendStatement("You already have a God cape!");
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 1)
					player.getActionSender().walkToNoForceAbs(2507,4722);
				switch(player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendStatement("You kneel and begin to chant to Guthix...");
						return true;
					case 2:
						player.getUpdateFlags().sendFaceToDirection(new Position(player.getPosition().getX(), player.getPosition().getY()+1));
						player.getUpdateFlags().sendAnimation(645);
						player.prayAtGodStatue(player, id);
						break;
					case 10:
						player.getDialogue().sendStatement("You feel a rush of energy charge through your veins. Suddenly a",
															"cape appears before you.");
						player.getDialogue().endDialogue();
						return true;
				}
			break;
			
			case 2873 : //saradomin @mage arena
				if(player.hasGodCape()){
					player.getDialogue().sendStatement("You already have a God cape!");
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 1)
					player.getActionSender().walkToNoForceAbs(2500,4719);
				switch(player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendStatement("You kneel and begin to chant to Saradomin...");
						return true;
					case 2:
						player.getUpdateFlags().sendFaceToDirection(new Position(player.getPosition().getX(), player.getPosition().getY()+1));
						player.getUpdateFlags().sendAnimation(645);
						player.prayAtGodStatue(player, id);
						break;
					case 10:
						player.getDialogue().sendStatement("You feel a rush of energy charge through your veins. Suddenly a",
															"cape appears before you.");
						player.getDialogue().endDialogue();
						return true;
				}
			break;
		}
		if (player.getDialogue().getChatId() > 1) {
			player.getActionSender().removeInterfaces();
		}
		if (player.getDialogue().getDialogueId() > -1) {
			player.getDialogue().resetDialogue();
		}
		return false;
	}
	
	/** Instruction
	 * 
	 * Use same format as example of Man below
	 * 
	 * When you need to end a dialogue and remove interfaces on the spot, use a "break"
	 * otherwise always use "return true"
	 * 
	 * When you need to end a dialogue after you click continue,
	 * place player.getDialogue().endDialogue(); before the "return true"
	 *
	 * If you have a method that calls an interface,
	 * place player.getDialogue().dontCloseInterface(); before the "break"
	 */
	public static boolean sendDialogue(Player player, int id, int chatId, int optionId, int npcChatId) {
		player.getDialogue().setChatId(chatId);
		player.getDialogue().setDialogueId(id);
		player.getDialogue().setDialogueType(0);
		player.getDialogue().setLastNpcTalk(npcChatId);
		if(player.getQuestHandler().findQuestDialog(id, chatId, optionId, npcChatId))
			return true;
		switch(id) {
			case 605 : //SIR VYVIN... TEMPORARY USING THIS DIALOGUE!!!
			case 1 : //Man
			case 2 : //Man
			case 3 : //Man
			case 16 : //Man
			case 24 : //Man
			case 351 : //Man
			case 359 : //Man
			case 663 : //Man
			case 726 : //Man
			case 727 : //Man
			case 728 : //Man
			case 729 : //Man
			case 730 : //Man
			case 1024 : //Man
			case 1025 : //Man
			case 1026 : //Man
			case 1086 : //Man
			case 2675 : //Man
			case 3223 : //Man
			case 3224 : //Man
			case 3225 : //Man
			case 4 : //Woman
			case 5 : //Woman
			case 6 : //Woman
			case 25 : //Woman
			case 352 : //Woman
			case 353 : //Woman
			case 354 : //Woman
			case 360 : //Woman
			case 361 : //Woman
			case 362 : //Woman
			case 363 : //Woman
			case 1027 : //Woman
			case 1028 : //Woman
			case 1029 : //Woman
			case 2776 : //Woman
			case 3226 : //Woman
			case 3227 : //Woman
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendPlayerChat("Hello, how's it going?", HAPPY);
						return true;
					case 2 :
						int random = Misc.random(12);
						if (random == 0) {
							player.getDialogue().sendNpcChat("How can I help you?", CONTENT);
						} else if (random == 1) {
							player.getDialogue().sendNpcChat("I'm fine, how are you?", HAPPY);
							player.getDialogue().setNextChatId(5);
						} else if (random == 2) {
							player.getDialogue().sendNpcChat("I'm busy right now.", CONTENT);
							player.getDialogue().endDialogue();
						} else if (random == 3) {
							player.getDialogue().sendNpcChat("No, I don't want to buy anything!", ANGRY_1);
							player.getDialogue().endDialogue();
						} else if (random == 4) {
							player.getDialogue().sendNpcChat("No I don't have any spare change.", ANNOYED);
							player.getDialogue().endDialogue();
						} else if (random == 5) {
							player.getDialogue().sendNpcChat("I'm very well thank you.", HAPPY);
							player.getDialogue().endDialogue();
						} else if (random == 6) {
							player.getDialogue().sendNpcChat("Hello there! Nice weather we've been having.", HAPPY);
							player.getDialogue().endDialogue();
						} else if (random == 7) {
							player.getDialogue().sendNpcChat("That is classified information.", CONTENT);
							player.getDialogue().endDialogue();
						} else if (random == 8) {
							player.getDialogue().sendNpcChat("Get out of my way, I'm in a hurry!", ANGRY_1);
							player.getDialogue().endDialogue();
						} else if (random == 9) {
							player.getDialogue().sendNpcChat("Hello.", HAPPY);
							player.getDialogue().endDialogue();
						} else if (random == 10) {
							player.getDialogue().sendNpcChat("Do I know you? I'm in a hurry!", HAPPY);
							player.getDialogue().endDialogue();
						} else if (random == 11) {
							player.getDialogue().sendNpcChat("I'm sorry I can't help you there.", ANNOYED);
							player.getDialogue().endDialogue();
						} else if (random == 12) {
							player.getDialogue().sendNpcChat("Not too bad thanks.", HAPPY);
							player.getDialogue().endDialogue();
						}
						return true;
					case 3 :
						player.getDialogue().sendOption("Do you wish to trade?", "I'm in search of a quest.", "I'm in search of enemies to kill.");
						return true;
					case 4 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendNpcChat("No, I have nothing I wish to get rid of.", "If you want to do some trading, there are", "plent of shops and market stalls around though.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 2 :
								player.getDialogue().sendNpcChat("I'm sorry I can't help you there.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 3 :
								player.getDialogue().sendNpcChat("I've heard there are many fearsome creatures", "that dwell under the ground...", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 5 :
						player.getDialogue().sendPlayerChat("Very well thank you.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
				
			case 3097 : //entrance guardian
				if(player.hasProgressHat() && chatId == 1)
					return false;
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendPlayerChat("Hi.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("Greetings. What wisdom do you seek?", CONTENT);
						return true;	
					case 3 :
						player.getDialogue().sendOption("I'm new to this place. Where am I?",
														"None, I don't really care.");
						return true;
					case 4 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("I'm new to this place. Where am I?", CONTENT);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("None, I don't really care.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 5 :
						player.getDialogue().sendNpcChat("Well young one, you have entered the Magic Training",
														"Arena. It was built at the start of the Fifth Age, when",
														"runestones were first discovered. It was made because",
														"of the many pointless accidents caused by inexperienced", CONTENT);
						return true;
					case 6 :
						player.getDialogue().sendNpcChat("mages.", CONTENT);
						return true;	
					case 7 :
						player.getDialogue().sendPlayerChat("Who created it?", CONTENT);
						return true;	
					case 8 :
						player.getDialogue().sendNpcChat("Good question. It was originally made by the ancestors",
														"of the wizards in the Wizards Tower. However, it was",
														"destroyed by melee and ranged warriors who took",
														"offence at the use of this new 'Magic Art'. Recently,", CONTENT);
						return true;
					case 9 :
						player.getDialogue().sendNpcChat("the current denizens of the Wizards Tower have",
														"resurrected the arena including various Guardians you",
														"will see as you look around. We are here to help and to",
														"ensure things run smoothly.", CONTENT);
						return true;
					case 10 :
						player.getDialogue().sendPlayerChat("Interesting. So what can I do here?", CONTENT);
						return true;
					case 11 :
						player.getDialogue().sendNpcChat("You may train up your skill in the magic arts by",
														"travelling through one of the portals at the back of this",
														"entrance hall. By training up in one of these areas you",
														"will be awarded special Pizazz Points unique to each", CONTENT);
						return true;
					case 12 :
						player.getDialogue().sendNpcChat("room. With these points you may claim a variety of",
														"items from my fellow guardian up the stairs.", CONTENT);
						return true;
					case 13 :
						player.getDialogue().sendPlayerChat("How do you record the points I have earned?", CONTENT);
						return true;
					case 14 :
						player.getDialogue().sendNpcChat("You really are full of questions! You will need a special",
														"Pizazz Progress Hat! I can give you one if you so",
														"wish to train here.", CONTENT);
						return true;
					case 15 :
						player.getDialogue().sendPlayerChat("Yes Please!", CONTENT);
						return true;
					case 16 :
						player.getInventory().addItemOrDrop(new Item(6885, 1));
						player.getDialogue().sendNpcChat("Here you go. Talk to the hat to find out your current",
														"Pizazz Point totals.", CONTENT);
						return true;
					case 17 :
						player.getDialogue().sendPlayerChat("Talk to it?", CONTENT);
						return true;
					case 18 :
						player.getDialogue().sendNpcChat("Well of course, it's a magic Pizazz Progress Hat! Mind",
														"your manners though, hats have feelings too!", CONTENT);
						return true;
					case 19 :
						player.getDialogue().sendPlayerChat("Er... if you insist.", CONTENT);
						return true;
					case 20 :
						player.getDialogue().sendNpcChat("Oh, and a word of warning: should you decide to leave",
														"the rooms by any method other than the exit portals,",
														"you will be teleported to the entrance and have any",
														"items that you picked up in the room removed.", CONTENT);
						return true;
					case 21 :
						player.getDialogue().sendPlayerChat("Can you explain the different portals?", CONTENT);
						return true;
					case 22 :
						player.getDialogue().sendNpcChat("They lead to four areas to train your magic: The",
														"Telekinetic Theatre, The Alchemists' Playground, The",
														"Enchanting Chamber, and The Creature Graveyard.", CONTENT);
						return true;
					case 23 :
						player.getDialogue().sendOption("What's the Telekinetic Theatre?",
														"What's the Alchemists' Playground?",
														"What's the Enchanting Chamber?",
														"What's the Creature Graveyard?",
														"Thanks, Bye!");
						return true;
					case 24 :
						switch(optionId) {
							case 1 :
							case 2 :
							case 3 :
							case 4 :
								player.getDialogue().sendStatement("This option is currently missing...");
								player.getDialogue().setNextChatId(23);
								return true;
							case 5 :
								player.getDialogue().sendPlayerChat("Thanks, Bye!", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;	
				}
				break;	
			case 3098 : //telekinetic guardian
				if(!player.getTelekineticTheatre().mazeCompleted)
					return false;
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendPlayerChat("Hi!", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("Would you like to try another maze?", CONTENT);
						return true;	
					case 3 :
						player.getDialogue().sendOption("Yes please!",
														"No thanks.");
						return true;
					case 4 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Yes please!", CONTENT);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("No thanks.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 5 :
						player.getDialogue().sendNpcChat("Very well, I shall teleport you.", CONTENT);
						return true;
					case 6 :
						player.getTelekineticTheatre().startNewMaze();
						player.getDialogue().endDialogue();
						break;	
				}
				break;	
			case 389 : //thormac
				switch(player.getDialogue().getChatId()) {
					case 1 :
						if(player.questStage[80] != 1)
							return false;
						player.getDialogue().sendNpcChat("Hello there, would you like me to enchant a battlestaff",
														"for 40k coins for you?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes.",
														"No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.getDialogue().endDialogue();
								player.getDialogue().dontCloseInterface();
								player.getActionSender().sendItemOnInterface(1734, 200, 1397);
								player.getActionSender().sendItemOnInterface(1735, 200, 1395);
								player.getActionSender().sendItemOnInterface(1736, 200, 1399);
								player.getActionSender().sendItemOnInterface(1737, 200, 1393);
								player.getActionSender().sendItemOnInterface(1738, 200, 3053);
								player.getActionSender().sendItemOnInterface(15348, 200, 6562);
								player.getActionSender().sendInterface(205);//992
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("No thanks.", HAPPY);
								player.getDialogue().endDialogue();
								return true;
						}
						break;	
				}
				break;
			case 3853 : //skill master
				switch(player.getDialogue().getChatId()) {
					case 1 :
						DailyTask task1 = player.getDailyTask();
						if(task1.getReqAmount() == player.dailyTaskAmount){
							player.getDialogue().sendNpcChat("I see you have completed your daily task!", HAPPY);
							return true;
						}
					case 2 :
						DailyTask task2 = player.getDailyTask();
						if(task2.getReqAmount() == player.dailyTaskAmount){
							player.getDialogue().sendNpcChat("Take these for your troubles.", HAPPY);
							player.dailyTaskAmount++;
							task2.getReward(player);
							/*Item a = task2.getRewardItem();
							if(a.getId() == 1437 && player.getSkill().getPlayerLevel(Skill.MINING) >= 30){
								player.getInventory().addItemOrDrop(new Item(7937, a.getCount()));
							} else
								player.getInventory().addItemOrDrop(task2.getRewardItem());*/
							player.getDialogue().endDialogue();
							return true;
						}
				}
				break;	
			case 970 : //diango
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello there.", CONTENT);
						if((player.loyaltyItem & Misc.getActualValue(3)) != 0)
							player.getDialogue().endDialogue();
						return true;
					case 2 :
						int days = Time.daysBetween(player.accountCreated, System.currentTimeMillis());
						int D = 1;
						if((player.loyaltyItem & Misc.getActualValue(1)) != 0 && (player.loyaltyItem & Misc.getActualValue(2)) == 0)
							D = 2;
						if((player.loyaltyItem & Misc.getActualValue(1)) != 0 && (player.loyaltyItem & Misc.getActualValue(2)) != 0 && (player.loyaltyItem & Misc.getActualValue(3)) == 0)
							D = 3;
						int dleft = (180*D)-days;
						if(dleft <= 0){
							if(Time.getHours(player.getPlayingTimeTotal()) >= (D*180)){
								player.getDialogue().sendNpcChat("It appears you are eligible to receive a",
																"loyalty item!", HAPPY);
								return true;
							}else{
								player.getDialogue().sendNpcChat("Your playing time is not high",
																"enough to receive an item.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							}
						}else{
							player.getDialogue().sendNpcChat("You have "+dleft+" days left before you can",
															"retrieve your loyalty item.", CONTENT);
							player.getDialogue().endDialogue();
							return true;
						}
					case 3 :
						D = 1;
						if((player.loyaltyItem & Misc.getActualValue(1)) != 0 && (player.loyaltyItem & Misc.getActualValue(2)) == 0)
							D = 2;
						if((player.loyaltyItem & Misc.getActualValue(1)) != 0 && (player.loyaltyItem & Misc.getActualValue(2)) != 0 && (player.loyaltyItem & Misc.getActualValue(3)) == 0)
							D = 3;
						player.getDialogue().sendNpcChat("Here you go! Hope you like it!", HAPPY);
						int itemId = 8074;
						if(D == 2)
							itemId = 7956;
						if(D == 3)
							itemId = 8066;
						Item itm = new Item(itemId, 1);
						player.getInventory().addItemOrDrop(itm);
						Misc.addWatchedItem(itm);
						player.loyaltyItem += Misc.getActualValue(D);
						player.getDialogue().endDialogue();
						return true;	
				}
				break;
			case 904 : //Chamber guardian @mage arena
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendPlayerChat("Hi.", CONTENT);
						if(player.kolodionStage == 6 || !player.hasGodCape())
							player.getDialogue().endDialogue();
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("Hello adventurer, have you made your choice?", CONTENT);
						return true;
					case 3 :
						player.getDialogue().sendPlayerChat("I have.", CONTENT);
						return true;
					case 4 :
						player.getDialogue().sendNpcChat("Good, good, I hope you have chosen well. I will now",
														"present you with a magic staff. This, along with the",
														"cape awarded to you by your chosen god, are all the",
														"weapons and armour you will need here.", CONTENT);
						return true;
					case 5 :
						int item = 1;
						if(player.hasItem(2412))
							item = 2415;
						if(player.hasItem(2413))
							item = 2416;
						if(player.hasItem(2414))
							item = 2417;
						player.getDialogue().sendItem1Line("The guardian hands you an ornate magic staff.", new Item(item, 1));
						player.getInventory().addItemOrDrop(new Item(item, 1));
						player.kolodionStage = 6;
						return true;
				}
				break;
			case 905 : //Kolodion @mage arena
				switch(player.getDialogue().getChatId()) {
					case 1 :
						if(player.kolodionStage == 6)
							return false;
						if(player.kolodionStage == 0){
							player.getDialogue().sendPlayerChat("Hello there. What is this place?", CONTENT);
							return true;
						}
						else if(player.kolodionStage <= 5){
							player.getDialogue().sendPlayerChat("Hello, Kolodion.", CONTENT);
							if(player.kolodionStage == 5)
								player.getDialogue().setNextChatId(52);
							else
								player.getDialogue().setNextChatId(10);
							return true;
						}
					case 2 :
						player.getDialogue().sendNpcChat("I am the great Kolodion, master of battle magic, and",
														"this is my battle arena. Top wizards travel from all over",
														"RuneScape to fight here.", CONTENT);
						return true;
					case 3 :
						player.getDialogue().sendOption("Can I fight here?",
														"What's the point of that?",
														"That's barbaric!");
						return true;
					case 4 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Can I fight here?", CONTENT);
								player.getDialogue().setNextChatId(5);
								return true;
							default :
								player.getDialogue().sendStatement("This option is currently missing...");
								player.getDialogue().setNextChatId(3);
								return true;
						}
						//break;
					case 5 :
						player.getDialogue().sendNpcChat("My arena is open to any high level wizard, but this is",
														"no game. Many wizards fall in this arena, never to rise",
														"again. The strongest mages have been destroyed.", CONTENT);
						return true;
					case 6 :
						player.getDialogue().sendNpcChat("If you're sure you want in?", CONTENT);
						return true;
					case 7 :
						player.getDialogue().sendOption("Yes indeedy.",
														"No I don't.");
						return true;
					case 8 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Yes indeedy.", CONTENT);
								player.getDialogue().setNextChatId(9);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("No I don't.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 9 :
						player.getDialogue().sendNpcChat("Good, good. You have a healthy sense of competition.", CONTENT);
						return true;
					case 10 :
						player.getDialogue().sendNpcChat("Remember, traveller - in my arena, hand-to-hand",
														"combat is useless. Your strength will diminish as you",
														"enter the arena, but the spells you can learn are",
														"amongst the most powerful in all of RuneScape.", CONTENT);
						return true;
					case 11 :
						player.getDialogue().sendNpcChat("Before I can accept you in, we must duel.", CONTENT);
						return true;
					case 12 :
						player.getDialogue().sendOption("Okay, let's fight.",
														"No thanks.");
						return true;
					case 13 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Okay, let's fight.", CONTENT);
								player.getDialogue().setNextChatId(14);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("No thanks.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 14 :
						player.getDialogue().sendNpcChat("I must first check that you are up to scratch.", CONTENT);
						return true;
					case 15 :
						if(player.getSkill().getPlayerLevel(Skill.MAGIC) >= 60)
							player.getDialogue().sendPlayerChat("You don't need to worry about that.", CONTENT);
						else{
							player.getDialogue().sendNpcChat("Sorry, you are not experienced enough to enter.", CONTENT);
							player.getDialogue().endDialogue();
						}
						return true;
					case 16 :
						player.getDialogue().sendNpcChat("Not just any magician can enter - only the most",
														"powerful and most feared. Before you can use the",
														"power of this arena, you must prove yourself against",
														"me.", CONTENT);
						return true;
					case 17 :
						if(player.getInteractingEntity().isNpc()){
							Npc npc = (Npc) player.getInteractingEntity();
							if(npc.getNpcId() == 905)
								npc.teleportPlayerKolodion(player, 3105, 3934, 0, null);
						}
						return false;
					case 52 : //after fight
						player.getDialogue().sendNpcChat("Hello, young mage. You're a tough one.", CONTENT);
						return true;
					case 53 :
						player.getDialogue().sendPlayerChat("What now?", CONTENT);
						return true;
					case 54 :
						player.getDialogue().sendNpcChat("Step into the magic pool. It will take you to a chamber.",
														"There, you must decide which god you will represent in",
														"the arena.", CONTENT);
						return true;
					case 55 :
						player.getDialogue().sendPlayerChat("Thanks, Kolodion.", CONTENT);
						return true;
					case 56 :
						player.getDialogue().sendNpcChat("That's what I'm here for.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 1263 : //Splitbark wizard
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello there, can I help you?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("What do you do here?",
														"What's that you're wearing?",
														"Can you make me some armour please?",
														"No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 3 :
								player.getDialogue().sendPlayerChat("Can you make me some armour please?", CONTENT);
								player.getDialogue().setNextChatId(4);
								return true;
							case 1 :
							case 2 :
							case 4 :
								player.getDialogue().sendStatement("This option is currently missing...");
								player.getDialogue().setNextChatId(2);
								return true;
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("Certainly, what would you like me to make?", CONTENT);
						return true;	
					case 5 :
						player.getDialogue().endDialogue();
						player.setStatedInterface("splitbark");
						Menus.display5ItemWTitle(player, 3385, 3387, 3389, 3391, 3393, "Helm", "Body", "Legs", "Gauntlets", "Boots", "Splitbark Armour");
						return true;
					case 6 :
						player.getDialogue().sendNpcChat("There you go, enjoy your new armour!", CONTENT);
						player.getDialogue().endDialogue();
						return true;	
				}
				break;	
			case 736 : //Bartender @falador
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Heya! What can I get you?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendPlayerChat("What ales are you serving?", CONTENT);
						return true;
					case 3 :
						player.getDialogue().sendNpcChat("Well, we've got Asgarnian Ale, Wizard's Mind Bomb",
														"and Dwarven Stout, all for only 3 coins.", CONTENT);
						return true;	
					case 4 :
						player.getDialogue().sendOption("One Asgarnian Ale, please.",
														"I'll try the Mind Bomb.",
														"Can I have a Dwarven Stout?",
														"I don't feel like any of those.");
						return true;
					case 5 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("One Asgarnian Ale, please.", CONTENT);
								player.setTempMiscInt(1917);
								player.getDialogue().setNextChatId(6);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("I'll try the Mind Bomb.", CONTENT);
								player.setTempMiscInt(1907);
								player.getDialogue().setNextChatId(6);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("Can I have a Dwarven Stout?", CONTENT);
								player.setTempMiscInt(1913);
								player.getDialogue().setNextChatId(6);
								return true;
							case 4 :
								player.getDialogue().sendPlayerChat("I don't feel like any of those.", CONTENT);
								player.getDialogue().endDialogue();
								return true;	
						}
						break;
					case 6 :
						if(player.getInventory().playerHasItem(995, 3)){
							player.getDialogue().sendPlayerChat("Thanks, "+new Npc(id).getDefinition().getName()+".", CONTENT);
							player.getInventory().removeItem(new Item(995, 3));
							player.getInventory().addItemOrDrop(new Item(player.getTempMiscInt(), 1));
							player.getDialogue().endDialogue();
						}else{
							player.getDialogue().sendNpcChat("I said 3 coins! You haven't got 3 coins!", ANGRY_1);
							player.getDialogue().endDialogue();
						}
						return true;
				}
				break;
			case 734 : //Bartender @port sarim
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendOption("Could I buy a beer please?",
														"Have you heard any rumours here?");
						return true;
					case 2 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Could I buy a beer please?", CONTENT);
								player.getDialogue().setNextChatId(3);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("Have you heard any rumours here?", CONTENT);
								player.getDialogue().setNextChatId(6);
								return true;
						}
						break;
					case 3 :
						player.getDialogue().sendNpcChat("Sure, that will be 2 gold coins please.", CONTENT);
						return true;	
					case 4 :
						if(player.getInventory().playerHasItem(995, 2)){
							player.getDialogue().sendPlayerChat("Ok, here you go.", CONTENT);
						}else{
							player.getDialogue().sendPlayerChat("I don't have enough coins.", CONTENT);
							player.getDialogue().endDialogue();
						}
						return true;
					case 5 :
						player.getInventory().removeItem(new Item(995, 2));
						player.getInventory().addItemOrDrop(new Item(1917, 1));
						player.getDialogue().send2Items2Lines("You buy a pint of beer!","", new Item(-1, 1), new Item(1917, 1));
						player.getDialogue().endDialogue();
						return true;
					case 6 :
						player.getDialogue().sendNpcChat("No, it hasn't been very busy lately.", CONTENT);
						return true;
				}
				break;	
			case 731 : //Bartender @varrock (jolly boar inn)
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Can I help you?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("I'll have a beer please.",
														"Any hints where I can go adventuring?",
														"Heard any good gossip?");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("I'll have a pint of beer please.", CONTENT);
								player.getDialogue().setNextChatId(4);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("Any hints where I can go adventuring?", CONTENT);
								player.getDialogue().setNextChatId(6);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("Heard any good gossip?", CONTENT);
								player.getDialogue().setNextChatId(10);
								return true;
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("Ok, that'll be two coins please.", CONTENT);
						return true;	
					case 5 :
						if(player.getInventory().playerHasItem(995, 2)){
							player.getActionSender().sendMessage("You buy a pint of beer.");
							player.getInventory().removeItem(new Item(995, 2));
							player.getInventory().addItemOrDrop(new Item(1917, 1));
							break;
						}else{
							player.getDialogue().sendPlayerChat("Oh dear, I don't seem to have enough money!", CONTENT);
							player.getDialogue().endDialogue();
						}
						return true;
					case 6 :
						player.getDialogue().sendNpcChat("Ooh, now. Let me see...", CONTENT);
						return true;
					case 7 :
						player.getDialogue().sendNpcChat("Well there is the Varrock sewers. There are tales of",
														"untold horrors coming out at night and stealing babies",
														"from houses.", CONTENT);
						return true;
					case 8 :
						player.getDialogue().sendPlayerChat("Sounds perfect! Where's the entrance?", CONTENT);
						return true;
					case 9 :
						player.getDialogue().sendNpcChat("It's just to the east of the palace.", CONTENT);
						player.getDialogue().endDialogue();
						return true;	
					case 10 :
						player.getDialogue().sendNpcChat("I'm not that well up on the gossip out here. I've heard",
														"that the bartender in the Blue Moon Inn has gone a",
														"little crazy, he keeps claiming he is a part of something",
														"called a computer game.", CONTENT);
						return true;
					case 11 :
						player.getDialogue().sendNpcChat("What that means, I don't know.",
														"That's probably old news by now though.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				break;	
			case 733 : //Bartender @varrock (blue moon inn)
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("What can I do yer for?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("A glass of your finest ale please.",
														"Can you recommend where an adventurer might make his fortune?",
														"Do you know where I can get some good equipment?");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("A glass of your finest ale please.", CONTENT);
								player.getDialogue().setNextChatId(4);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("Can you recommend where an adventurer","might make his fortune?", CONTENT);
								player.getDialogue().setNextChatId(6);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("Do you know where I can get some good equipment?", CONTENT);
								player.getDialogue().setNextChatId(14);
								return true;
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("No problemo. That'll be 2 coins.", CONTENT);
						return true;	
					case 5 :
						if(player.getInventory().playerHasItem(995, 2)){
							player.getActionSender().sendMessage("You buy a pint of beer.");
							player.getInventory().removeItem(new Item(995, 2));
							player.getInventory().addItemOrDrop(new Item(1917, 1));
							break;
						}else{
							player.getDialogue().sendPlayerChat("Sorry, don't have that right now.", CONTENT);
							player.getDialogue().endDialogue();
						}
						return true;
					case 6 :
						player.getDialogue().sendNpcChat("Ooh I don't know if I should be giving away information,","makes the computer game too easy.", CONTENT);
						return true;
					case 7 :
						player.getDialogue().sendOption("Oh ah well...",
														"Computer game? What are you talking about?",
														"Just a small clue?");
						return true;
					case 8 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Oh ah well...", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("Computer game? What are you talking about?", CONTENT);
								player.getDialogue().setNextChatId(9);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("Just a small clue?", CONTENT);
								player.getDialogue().setNextChatId(13);
								return true;
						}
						break;	
					case 9 :
						player.getDialogue().sendNpcChat("This world around us... is a computer game...","called RuneScape.", CONTENT);
						return true;
					case 10 :
						player.getDialogue().sendPlayerChat("Nope, still don't understand what you are talking about.","What's a computer?", CONTENT);
						return true;
					case 11 :
						player.getDialogue().sendNpcChat("It's a sort of magic box thing,","which can do all sorts of stuff.", CONTENT);
						return true;
					case 12 :
						player.getDialogue().sendPlayerChat("I give up. You're obviously completely mad!", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 13 :
						player.getDialogue().sendNpcChat("Go and talk to bartender at the Jolly Boar Inn,","he doesn't seem to mind giving away clues.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 14 :
						player.getDialogue().sendNpcChat("Well, there's the sword shop across the road, or ", "there's also all sort of shops up around the market.", CONTENT);
						player.getDialogue().endDialogue();
						return true;	
				}
				break;	
			case 36 : //Wyson the gardener @falador
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("I'm the head gardener around here.",
														"If you're looking for woad leaves, or if you need help",
														"with owt, I'm yer man.", CALM);
						return true;
					case 2 :
						player.getDialogue().sendOptionWTitle("What would you like to say?",
															"Yes please, I need woad leaves.",
															"How about ME helping YOU instead?",
															"Sorry, but I'm not interested.");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Yes please, I need woad leaves.", CALM);
								player.getDialogue().setNextChatId(4);
								return true;
							case 2 :
							case 3 :
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("How much are you willing to pay?", ANNOYED);
						return true;
					case 5 :
						player.getDialogue().sendOptionWTitle("What would you like to say?",
															"How about 5 coins?",
															"How about 10 coins?",
															"How about 15 coins?",
															"How about 20 coins?");
						return true;
					case 6 :
						switch(optionId) {
							case 4 :
								player.getDialogue().sendPlayerChat("How about 20 coins?", CALM);
								player.getDialogue().setNextChatId(7);
								return true;
							case 1 :
							case 2 :
							case 3 :	
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 7 :
						player.getDialogue().sendNpcChat("Ok that's more than fair.", CALM);
						return true;
					case 8 :
						player.getDialogue().sendNpcChat("Here, have two, you're a generous person.", CALM);
						return true;
					case 9 :
						if(player.getInventory().playerHasItem(995, 20)){
							player.getDialogue().sendPlayerChat("Thanks.", CALM);
							player.getInventory().removeItem(new Item(995, 20));
							player.getInventory().addItemOrDrop(new Item(1793, 2));
							player.getDialogue().endDialogue();
						}else{
							player.getDialogue().sendNpcChat("You don't have enough coins with you.", CALM);
							player.getDialogue().endDialogue();
						}
						return true;	
				}
				break;
			case 922 : //Aggie - dye witch @draynor
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("What can I help you with?", CALM);
						return true;
					case 2 :
						player.getDialogue().sendOption("What could you make for me?",
														"Cool, do you turn people into frogs?",
														"You mad old witch, you can't help me.",
														"Can you make dyes for me please?");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("What could you make for me?", CALM);
								player.getDialogue().setNextChatId(11);
								return true;
							case 2 :
							case 3 :
								player.getDialogue().endDialogue();
								return true;
							case 4 :
								player.getDialogue().sendPlayerChat("Can you make dyes for me please?", CALM);
								player.getDialogue().setNextChatId(4);
								return true;
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("What sort of dye would you like? Red, yellow or blue?", CALM);
						return true;
					case 5 :
						player.getDialogue().sendOption("What do you need to make red dye?",
														"What do you need to make yellow dye?",
														"What do you need to make blue dye?");
						return true;
					case 6 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("What do you need to make red dye?", CALM);
								player.getDialogue().setNextChatId(7);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("What do you need to make yellow dye?", CALM);
								player.getDialogue().setNextChatId(14);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("What do you need to make blue dye?", CALM);
								player.getDialogue().setNextChatId(18);
								return true;
						}
						break;
					case 7 :
						player.getDialogue().sendNpcChat("3 lots of redberries and 5 coins to you.", CALM);
						return true;
					case 8 :
						player.getDialogue().sendOption("Okay, make me some red dye please.",
														"I don't think I have all the ingredients yet.",
														"I can do without dye at that price.",
														"Where do I get redberries?",
														"What other colours can you make?");
						return true;
					case 9 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Ok make me some red dye please.", CALM);
								player.getDialogue().setNextChatId(10);
								return true;
							case 2 :
							case 3 :
							case 4 :
							case 5 :
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 10 :
						if(player.getInventory().playerHasItem(1951, 3) && player.getInventory().playerHasItem(995, 5)){
							player.getDialogue().send2Items2Lines("You hand the berries and payment to Aggie. Aggie",
																"produces a red bottle and hands it to you.", new Item(-1, 1), new Item(1763, 1));
							player.getInventory().removeItem(new Item(1951, 3));
							player.getInventory().removeItem(new Item(995, 5));
							player.getInventory().addItemOrDrop(new Item(1763, 1));
							player.getDialogue().endDialogue();
						}else{
							player.getDialogue().sendNpcChat("You don't have the ingredients for me to do that.", CALM);
							player.getDialogue().endDialogue();
						}
						return true;
					case 11 :
						player.getDialogue().sendNpcChat("I mostly just make what I find pretty. I sometimes",
														"make dye for the women's clothes to brighten the place",
														"up. I can make red, yellow and blue dyes. If you'd like",
														"some, just bring me the appropriate ingredients.", CALM);
						return true;
					case 12 :
						player.getDialogue().sendOption("What do you need to make red dye?",
														"What do you need to make yellow dye?",
														"What do you need to make blue dye?",
														"No thanks, I am happy the colour I am.");
						return true;
					case 13 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("What do you need to make red dye?", CALM);
								player.getDialogue().setNextChatId(7);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("What do you need to make yellow dye?", CALM);
								player.getDialogue().setNextChatId(14);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("What do you need to make blue dye?", CALM);
								player.getDialogue().setNextChatId(18);
								return true;
							case 4 :
								player.getDialogue().sendPlayerChat("No thanks, I am happy the colour I am.", CALM);
								player.getDialogue().endDialogue();
								return true;	
						}
						break;
					case 14 :
						player.getDialogue().sendNpcChat("Yellow is a strange colour to get, comes from onion",
														"skins. I need 2 onions and 5 coins to make yellow dye.", CALM);
						return true;
					case 15 :
						player.getDialogue().sendOption("Okay, make me some yellow dye please.",
														"I don't think I have all the ingredients yet.",
														"I can do without dye at that price.",
														"Where do I get onions?",
														"What other colours can you make?");
						return true;
					case 16 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Ok make me some yellow dye please.", CALM);
								player.getDialogue().setNextChatId(17);
								return true;
							case 2 :
							case 3 :
							case 4 :
							case 5 :
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 17 :
						if(player.getInventory().playerHasItem(1957, 2) && player.getInventory().playerHasItem(995, 5)){
							player.getDialogue().send2Items2Lines("You hand the onions and payment to Aggie. Aggie",
																"produces a yellow bottle and hands it to you.", new Item(-1, 1), new Item(1765, 1));
							player.getInventory().removeItem(new Item(1957, 2));
							player.getInventory().removeItem(new Item(995, 5));
							player.getInventory().addItemOrDrop(new Item(1765, 1));
							player.getDialogue().endDialogue();
						}else{
							player.getDialogue().sendNpcChat("You don't have the ingredients for me to do that.", CALM);
							player.getDialogue().endDialogue();
						}
						return true;
					case 18 :
						player.getDialogue().sendNpcChat("2 woad leaves and 5 coins to you.", CALM);
						return true;
					case 19 :
						player.getDialogue().sendOption("Okay, make me some blue dye please.",
														"I don't think I have all the ingredients yet.",
														"I can do without dye at that price.",
														"Where do I get woad leaves?",
														"What other colours can you make?");
						return true;
					case 20 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Ok make me some blue dye please.", CALM);
								player.getDialogue().setNextChatId(21);
								return true;
							case 2 :
							case 3 :
							case 4 :
							case 5 :
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 21 :
						if(player.getInventory().playerHasItem(1793, 2) && player.getInventory().playerHasItem(995, 5)){
							player.getDialogue().send2Items2Lines("You hand the woad leaves and payment to Aggie. Aggie",
																"produces a blue bottle and hands it to you.", new Item(-1, 1), new Item(1767, 1));
							player.getInventory().removeItem(new Item(1793, 2));
							player.getInventory().removeItem(new Item(995, 5));
							player.getInventory().addItemOrDrop(new Item(1767, 1));
							player.getDialogue().endDialogue();
						}else{
							player.getDialogue().sendNpcChat("You don't have the ingredients for me to do that.", CALM);
							player.getDialogue().endDialogue();
						}
						return true;
				}
				break;
			case 918 : //Ned @ draynor
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Why, hello there, lad. Me friends call me Ned. I was a",
														"man of the sea, but it's past me now. Could I be",
														"making or selling you some rope?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes, I would like some rope.",
														"No thanks Ned, I don't need any.");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Yes, I would like some rope.", CONTENT);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("No thanks Ned, I don't need any.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("Well, I can sell you some rope for 15 coins. Or I can",
														"be making you some if you gets me 4 balls of wool. I",
														"strands them together I does, makes em strong.", CONTENT);
						return true;
					case 5 :
						player.getDialogue().sendPlayerChat("You make rope from wool?", CONTENT);
						return true;
					case 6 :
						player.getDialogue().sendNpcChat("Of course you can!", CONTENT);
						return true;
					case 7 :
						player.getDialogue().sendPlayerChat("I thought you needed hemp or jute.", CONTENT);
						return true;
					case 8 :
						player.getDialogue().sendNpcChat("Do you want some rope or not?", CONTENT);
						return true;
					case 9 :
						player.getDialogue().sendOption("Okay, please sell me some rope.",
														"I have balls of wool. Could you make me some rope?",
														"That's a little more than I want to pay.");
						return true;
					case 10 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Okay, please sell me some rope.", CONTENT);
								player.getDialogue().setNextChatId(11);
								if(!player.getInventory().playerHasItem(995, 15))
									player.getDialogue().endDialogue();
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("I have balls of wool. Could you make me some rope?", CONTENT);
								player.getDialogue().setNextChatId(14);
								if(!player.getInventory().playerHasItem(1759, 4))
									player.getDialogue().endDialogue();
								return true;	
							case 3 :
								player.getDialogue().sendPlayerChat("That's a little more than I want to pay.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 11 :
						player.getDialogue().sendNpcChat("There you go, finest rope in RuneScape.",	CONTENT);
						return true;	
					case 12 :
						player.getDialogue().sendStatement("You hand Ned 15 coins. Ned gives you a coil of rope.");
						return true;
					case 13 :
						if(player.getInventory().playerHasItem(995, 15)){
							player.getInventory().removeItem(new Item(995, 15));
							player.getInventory().addItemOrDrop(new Item(954, 1));
							player.getDialogue().endDialogue();
						}
						break;
					case 14 :
						player.getDialogue().sendNpcChat("There you go, finest rope in RuneScape.",	CONTENT);
						return true;	
					case 15 :
						player.getDialogue().sendStatement("You hand Ned 4 balls of wool. Ned gives you a coil of rope.");
						return true;
					case 16 :
						if(player.getInventory().playerHasItem(1759, 4)){
							player.getInventory().removeItem(new Item(1759, 4));
							player.getInventory().addItemOrDrop(new Item(954, 1));
							player.getDialogue().endDialogue();
						}
						break;	
				}
				break;
			case 379 : //Luthas @ Karamja
				if(player.bananaCrate != 10){
					switch(player.getDialogue().getChatId()) {
						case 1 :
							player.getDialogue().sendNpcChat("Hello I'm Luthas, I run the banana plantation here.", CONTENT);
							return true;
						case 2 :
							player.getDialogue().sendOption("Could you offer me employment on your plantation?", "That customs officer is annoying isn't she?");
							return true;
						case 3 :
							switch(optionId) {
								case 1 :
									player.getDialogue().sendPlayerChat("Could you offer me employment on your plantation?", CONTENT);
									return true;
								case 2 :
									player.getDialogue().endDialogue();
							}
							break;
						case 4 :
							player.getDialogue().sendNpcChat("Yes, I can sort something out. There's a crate ready to",
															"be loaded onto the ship.", CONTENT);
							return true;
						case 5 :
							player.getDialogue().sendNpcChat("You wouldn't believe the demand for bananas from",
															"Wydin's shop over in Port Sarim. I think this is the",
															"third crate I've shipped him this month..", CONTENT);
							return true;
						case 6 :
							player.getDialogue().sendNpcChat("If you could fill it up with bananas, I'll pay you 30",
															"gold.", CONTENT);
							player.getDialogue().endDialogue();
							return true;	
					}
				}else{
					switch(player.getDialogue().getChatId()) {
						case 1 :
							player.getDialogue().sendPlayerChat("I've filled a crate with bananas.", CONTENT);
							return true;
						case 2 :
							player.getDialogue().sendNpcChat("Well done, here's your payment.", CONTENT);
							return true;
						case 3 :
							player.bananaCrate = 0;
							if(player.questStage[10] == 3){
								player.questStage[10] = 4;
							}
							player.getActionSender().sendMessage("Luthas hands you 30 coins.");
							player.getInventory().addItemOrDrop(new Item(995, 30));
							player.getDialogue().endDialogue();
					}
				}
				break;
			case 488 : //Observatory professor //gives u chart
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendPlayerChat("Hi, are you busy?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("What would you like to talk about?", CONTENT);
						return true;	
					case 3 :
						player.getDialogue().sendOption("Talk about the Observatory Quest.", "Talk about Treasure Trails.");
						return true;
					case 4 :
						switch(optionId) {
							case 1 ://quest not added yet...
								player.getDialogue().endDialogue();
								player.getActionSender().removeInterfaces();
								return true;
							case 2 :
								if(player.hasItem(CoordinateScrolls.CHART)){
									player.getDialogue().sendNpcChat("I have already given you a chart!", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
								if(player.getInventory().playerHasItem(CoordinateScrolls.SEXTANT) && player.getInventory().playerHasItem(CoordinateScrolls.WATCH)){
									player.getDialogue().sendPlayerChat("I've got the sextant and watch!", CONTENT);
									player.getDialogue().setNextChatId(14);
									return true;
								}else{
									player.getDialogue().sendPlayerChat("Can you teach me to solve Treasure Trail clues?", CONTENT);
									player.getDialogue().setNextChatId(5);
									return true;
								}
						}
						break;
					case 5 :
						player.getDialogue().sendNpcChat("Ah, I get asked about treasure trails all the time!",
														"Listen carefully and I shall tell you what I know...", CONTENT);
						return true;
					case 6 :
						player.getDialogue().sendNpcChat("Lots of clues have degrees and minutes written on",
														"them. These are the coordinates of the place where the",
														"treasure is buried.", CONTENT);
						return true;
					case 7 :
						player.getDialogue().sendNpcChat("You have to walk to the correct spot, so that your",
														"coordinates are exactly the same as the values written",
														"on the clue scroll.", CONTENT);
						return true;
					case 8 :
						player.getDialogue().sendNpcChat("To do this, you must use a sextant, a watch and a",
														"chart to find the coordinates of where you are.", CONTENT);
						return true;
					case 9 :
						player.getDialogue().sendNpcChat("Once you know the coordinates of your position, you",
														"know which way you have to walk to get to the",
														"treasure's coordinates!", CONTENT);
						return true;
					case 10 :
						player.getDialogue().sendPlayerChat("Riiight. So where do I get these items from?", CONTENT);
						return true;
					case 11 :
						player.getDialogue().sendNpcChat("I think Murphy, the owner of the Fishing Trawler",
														"moored at Port Khazard, might be able to spare you a",
														"sextant. After that, the nearest clock tower is south of",
														"Ardougne - you could probably get a watch there. I've", CONTENT);
						return true;
					case 12 :
						player.getDialogue().sendNpcChat("got plenty of charts myself; just come back here when",
														"you've got the sextant and watch, and I'll give you one",
														"and teach you how to use them.", CONTENT);
						player.talkedWithObservatoryProf = true;
						return true;
					case 13 :
						player.getDialogue().sendPlayerChat("Thanks, I'll see you later.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 14 :
						player.getDialogue().sendNpcChat("Well done!", CONTENT);
						return true;
					case 15 :
						player.getDialogue().sendNpcChat("You use the sextant to measure the angle that the sun",
														"is currently at. You need the watch so that you know",
														"what the time is back here at the observatory.", CONTENT);
						return true;
					case 16 :
						player.getDialogue().sendNpcChat("You then need this chart to work out your position.",
														"Your position is recorded in terms of latitude and",
														"longitude. Latitude is your position above the equator",
														"and longitude is your position relative to here.", CONTENT);
						return true;
					case 17 :
						player.getInventory().addItemOrDrop(new Item(CoordinateScrolls.CHART, 1));
						player.getDialogue().sendStatement("The professor has given you a navigation chart.", CoordinateScrolls.CHART);
						return true;
					case 18 :
						player.getDialogue().sendNpcChat("So, if you have your sextant, watch and chart with you",
														"then you can work out exactly where you are!", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				break;

			case 3863 : //ge Clerk
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("What can I do for you?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("I'd like to set up trade offers please.", "I'm fine, thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("I'd like to set up trade offers please.", CONTENT);
								player.getDialogue().setNextChatId(4);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("I'm fine, thanks.", CONTENT);
								player.getDialogue().endDialogue();
								return true;	
						}
						break;
					case 4 :
						GeManager.openGe(player);
						player.getDialogue().dontCloseInterface();
						break;
				}
				break;
				
			case 223 : //Brother Kojo
				switch(player.getDialogue().getChatId()) {
					case 1 :
						if(player.hasItem(CoordinateScrolls.WATCH) || !player.talkedWithObservatoryProf)
							return false;
						player.getDialogue().sendPlayerChat("Hello.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("Hello, traveller, how can I help?", CONTENT);
						return true;	
					case 3 :
						player.getDialogue().sendPlayerChat("I'm trying to learn how to be a navigator.", CONTENT);
						return true;
					case 4 :
						player.getDialogue().sendNpcChat("I don't know if I can help you there.", CONTENT);
						return true;
					case 5 :
						player.getDialogue().sendPlayerChat("The professor from the Observatory says that I need a",
															"watch.", CONTENT);
						return true;
					case 6 :
						player.getDialogue().sendNpcChat("Ah, that I can help you with. I've been tinkering with",
														"this new idea of a watch and made a few. The problem",
														"is the villagers don't see the point as they have the Clock",
														"Tower!", CONTENT);
						return true;
					case 7 :
						player.getDialogue().sendPlayerChat("Can I have one?", CONTENT);
						return true;
					case 8 :
						player.getDialogue().sendNpcChat("You can have this one! It's the display model.", CONTENT);
						return true;
					case 9 :
						player.getInventory().addItemOrDrop(new Item(CoordinateScrolls.WATCH, 1));
						player.getDialogue().sendStatement("Brother Kojo has given you a watch.", CoordinateScrolls.WATCH);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 463 : //Murphy, gives u sextant
				switch(player.getDialogue().getChatId()) {
					case 1 :
						if(player.hasItem(CoordinateScrolls.SEXTANT) || !player.talkedWithObservatoryProf)
							return false;
						player.getDialogue().sendPlayerChat("Ahoy there!", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("Ahoy!", CONTENT);
						return true;	
					case 3 :
						player.getDialogue().sendPlayerChat("I'm trying to learn how to be a navigator.", CONTENT);
						return true;
					case 4 :
						player.getDialogue().sendNpcChat("Well, you've come to the right place, m'hearty! What do",
														"you need to know?", CONTENT);
						return true;
					case 5 :
						player.getDialogue().sendPlayerChat("The professor said that I need to have a sextant. Do",
															"you know where I can get one?", CONTENT);
						return true;
					case 6 :
						player.getDialogue().sendNpcChat("Hmm. I used to use a sextant when I was a young",
														"fella.", CONTENT);
						return true;
					case 7 :
						player.getDialogue().sendPlayerChat("Do you still have it?", CONTENT);
						return true;
					case 8 :
						player.getDialogue().sendNpcChat("Aye.", CONTENT);
						return true;
					case 9 :
						player.getDialogue().sendPlayerChat("Could I have it?", CONTENT);
						return true;
					case 10 :
						player.getDialogue().sendNpcChat("Aye.", CONTENT);
						return true;
					case 11 :
						player.getInventory().addItemOrDrop(new Item(CoordinateScrolls.SEXTANT, 1));
						player.getDialogue().sendStatement("Murphy has given you his old sextant.", CoordinateScrolls.SEXTANT);
						return true;
					case 12 :
						player.getDialogue().sendPlayerChat("Don't you still need it?", CONTENT);
						return true;
					case 13 :
						player.getDialogue().sendNpcChat("I can tell from the taste of the sea spray where I am,",
														"m'hearty!", CONTENT);
						return true;
					case 14 :
						player.getDialogue().sendPlayerChat("Wow!", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 543 : //Karim
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to buy a nice kebab? Only one gold.", CALM);
						return true;
					case 2 :
						player.getDialogue().sendOption("I think I'll give it a miss.", "Yes please.");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("I think I'll give it a miss.", DISORIENTED_RIGHT);
								player.getDialogue().endDialogue();
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("Yes please.", CONTENT);
								return true;
						}
						break;
					case 4 :
						if (player.getInventory().playerHasItem(new Item(995))) {
							player.getInventory().removeItem(new Item(995));
							player.getInventory().addItemOrDrop(new Item(1971));
							player.getActionSender().sendMessage("You buy a kebab.");
							break;
						} else {
							player.getDialogue().sendPlayerChat("Oops, I forgot to bring any money with me.", SAD);
							player.getDialogue().endDialogue();
						}
						return true;
				}
				break;
			case 539 : //Silk trader
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Do you want to buy any fine silks?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("How much are they?", "No, silk doesn't suit me.");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("How much are they?", CONTENT);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("No, silk doesn't suit me.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("3gp.", CONTENT);
						return true;
					case 5 :
						player.getDialogue().sendOption("No, that's too much for me.", "Okay, that sounds good.");
						return true;
					case 6 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("No, that's too much for me.", CONTENT);
								return true;
							case 2 : //buy silk 3gp
								player.getDialogue().sendPlayerChat("Okay, that sounds good.", CONTENT);
								player.getDialogue().setNextChatId(12);
								return true;
						}
						break;
					case 7 :
						player.getDialogue().sendNpcChat("2gp and that's as low as I'll go.", CONTENT);
						return true;
					case 8 :
						player.getDialogue().sendNpcChat("I'm not selling it for any less. You'll only", "go and sell it in Varrock for a profit.", CONTENT);
						return true;
					case 9 :
						player.getDialogue().sendOption("2gp sounds good.", "No, really, I don't want it.");
						return true;
					case 10 :
						switch(optionId) {
							case 1 : //buy silk 2gp
								player.getDialogue().sendPlayerChat("2gp sounds good.", CONTENT);
								player.getDialogue().setNextChatId(14);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("No, really, I don't want it.", CONTENT);
								return true;
						}
						break;
					case 11 :
						player.getDialogue().sendNpcChat("Okay, but that's the best price your going to get.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 12 :
						if (player.getInventory().playerHasItem(new Item(995, 3))) {
							player.getInventory().removeItem(new Item(995, 3));
							player.getInventory().addItemOrDrop(new Item(950));
							player.getDialogue().sendItem1Line("You buy some silk for 3gp.", new Item(950));
							player.getDialogue().endDialogue();
						} else {
							player.getDialogue().sendPlayerChat("Oh dear. I don't have enough money.", SAD);
						}
						return true;
					case 13 :
						player.getDialogue().sendNpcChat("Well, come back when you do have some money.", ANGRY_1);
						player.getDialogue().endDialogue();
						return true;
					case 14 :
						if (player.getInventory().playerHasItem(new Item(995, 2))) {
							player.getInventory().removeItem(new Item(995, 2));
							player.getInventory().addItemOrDrop(new Item(950));
							player.getDialogue().sendItem1Line("You buy some silk for 2gp.", new Item(950));
							player.getDialogue().endDialogue();
						} else {
							player.getDialogue().sendPlayerChat("Oh dear. I don't have enough money.", SAD);
							player.getDialogue().setNextChatId(13);
						}
						return true;
				}	
				break;
			case 2238 : //Donie
				switch(player.getDialogue().getChatId()) {
					case 1:
						player.getDialogue().sendNpcChat("Hello there, can I help you?", HAPPY);
						int random = Misc.random(2);
						player.getDialogue().setNextChatId(random == 0 ? 2 : random == 1 ? 6 : 4);
						return true;
					case 2:
						player.getDialogue().sendOption("Where am I?", "How are you today?", "Are there any quests I can do here?", "Where can I get a haircut like yours?");
						return true;
					case 3:
						switch(optionId) {
							case 1:
								player.getDialogue().sendNpcChat("This is the town of Lumbridge my friend.", HAPPY);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("Aye, not too bad thank you. Lovely weather", "in 06Scape this fine day.", HAPPY);
								player.getDialogue().setNextChatId(10);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("What kind of quest are you looking for?", ANNOYED);
								player.getDialogue().setNextChatId(20);
								return true;
							case 4:
								player.getDialogue().sendNpcChat("Yes, it does look like you need a hairdresser.", HAPPY);
								player.getDialogue().setNextChatId(15);
								return true;
						}
						break;
					case 4:
						player.getDialogue().sendOption("How are you today?", "Are there any quests I can do here?", "Your shoe lace is united.");
						return true;
					case 5:
						switch(optionId) {
							case 1:
								player.getDialogue().sendNpcChat("Aye, not too bad thank you. Lovely weather", "in 06Scape this fine day.", HAPPY);
								player.getDialogue().setNextChatId(10);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("What kind of quest are you looking for?", ANNOYED);
								player.getDialogue().setNextChatId(20);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("No it's not!", ANGRY_1);
								player.getDialogue().setNextChatId(18);
								return true;
						}
						break;
					case 6:
						player.getDialogue().sendOption("Do you have anything of value which I can have?", "Are there any quests I can do here?", "Can I buy your stick?");
						return true;
					case 7:
						switch(optionId) {
							case 1:
								player.getDialogue().sendNpcChat("Are you asking for free stuff?", ANNOYED);
								return true;
							case 2:
								player.getDialogue().sendNpcChat("What kind of quest are you looking for?", ANNOYED);
								player.getDialogue().setNextChatId(20);
								return true;
							case 3:
								player.getDialogue().sendNpcChat("It's not a stick! I'll have you know it's", "a very powerful staff!", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 8:
						player.getDialogue().sendPlayerChat("Well... er... yes.", ANNOYED);
						return true;
					case 9:
						player.getDialogue().sendNpcChat("No I dont not have anything I can give you.", "If I did have anything of value I wouldn't", "want to give it away.", ANGRY_1);
						player.getDialogue().endDialogue();
						return true;
					case 10:
						player.getDialogue().sendPlayerChat("Weather?", CALM);
						return true;
					case 11:
						player.getDialogue().sendNpcChat("Yes weather, you know.", HAPPY);
						return true;
					case 12:
						player.getDialogue().sendNpcChat("The state or condition of the atmosphere", "at a time and place, with respect to variables", "such as temperature, moisture, wind velocity,", "and barometric pressure.", ANNOYED);
						return true;
					case 13:
						player.getDialogue().sendPlayerChat("...", CALM);
						return true;
					case 14:
						player.getDialogue().sendNpcChat("Not just a pretty face eh? Ha ha ha.", LAUGHING);
						player.getDialogue().endDialogue();
						return true;
					case 15:
						player.getDialogue().sendPlayerChat("Oh thanks.", ANGRY_1);
						return true;
					case 16:
						player.getDialogue().sendNpcChat("No problem. The hairdresser in Falador", "will probably be able to sort you out.", LAUGHING);
						return true;
					case 17:
						player.getDialogue().sendNpcChat("The Lumbridge general store sells useful maps", "if you don't know the way.", LAUGHING);
						player.getDialogue().endDialogue();
						return true;
					case 18:
						player.getDialogue().sendPlayerChat("No you're right. I have nothing to back that up.", ANGRY_1);
						return true;
					case 19:
						player.getDialogue().sendNpcChat("Fool! Leave me alone!", ANGRY_1);
						player.getDialogue().endDialogue();
						return true;
					case 20:
						player.getDialogue().sendNpcChat("Sorry, quests have not been added yet.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 801 : //Abbot
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello brother, welcome to our monastery.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Can you heal me? I'm injured.", "Can I climb up those stairs?", "Good bye.");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								if (player.getSkill().getLevel()[Skill.HITPOINTS] < player.getSkill().getPlayerLevel(Skill.HITPOINTS)) {
									if (player.getInteractingEntity() != null) {
										player.getInteractingEntity().getUpdateFlags().sendAnimation(717);
									}
									player.getDialogue().sendNpcChat("Sure, here you go.", CONTENT);
									player.heal((int) (player.getSkill().getPlayerLevel(Skill.HITPOINTS) * 0.3));
									player.getUpdateFlags().sendGraphic(84);
									break;
								} else {
									player.getDialogue().sendNpcChat("You already have full hp.", CONTENT);
									player.getDialogue().endDialogue();
								}
								return true;
							case 2 :
								player.getDialogue().sendNpcChat("Up those stairs is the prayer guild. You need a", "level of 31 Prayer to enter. There you will find", "an altar that can boost your prayer by 2 points,", "as well as some monk robes.", CONTENT);
								player.getDialogue().setNextChatId(2);
								return true;
						}
						break;
				}
				break;
			case 9999 : //Alkharid Gate
				player.getDialogue().setLastNpcTalk(926);
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendPlayerChat("Can I come through this gate?", CONTENT);
						return true;
					case 2 :
						if(player.questStage[11] == 1 || player.questStage[11] == 9){
							player.getDialogue().sendNpcChat("You may pass for free, you are a friend of Al-Kharid.", CONTENT);
							player.getDialogue().setNextChatId(19);
						}else{
							player.getDialogue().sendNpcChat("You must pay a toll of 10 gold coins to pass.", CONTENT);
							if (!player.getInventory().playerHasItem(995, 10)) {
								player.getDialogue().setNextChatId(7);
							}
						}
						return true;
					case 3 :
						player.getDialogue().sendOption("Ok, I'll pay.", "Who does the money go to?", "No thanks, I'll walk around.");
						return true;
					case 4 :
						switch(optionId) {
							case 1 :
								if (player.getInventory().playerHasItem(995, 10)) {
									player.getInventory().removeItem(new Item(995, 10));
									player.getActionSender().walkTo(player.getPosition().getX() < 3268 ? 1 : -1, 0, true);
									player.getActionSender().walkThroughDoubleDoor(2882, 2883, 3268, 3227, 3268, 3228, 0);
								}
								break;
							case 2 :
								player.getDialogue().sendNpcChat("The money goes to the city of Al-Kharid.", "Will you pay the toll?", CONTENT);
								return true;
							case 3 :
								player.getDialogue().sendNpcChat("As you wish. Don't go too near the scorpions.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 5 :
						player.getDialogue().sendOption("Ok, I'll pay.", "No thanks, I'll walk around.");
						return true;
					case 6 :
						switch(optionId) {
							case 1 :
								if (player.getInventory().playerHasItem(995, 10)) {
									player.getInventory().removeItem(new Item(995, 10));
									player.getActionSender().walkTo(player.getPosition().getX() < 3268 ? 1 : -1, 0, true);
									player.getActionSender().walkThroughDoubleDoor(2882, 2883, 3268, 3227, 3268, 3228, 0);
								}
								break;
							case 2 :
								player.getDialogue().sendNpcChat("As you wish. Don't go too near the scorpions.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 7 :
						player.getDialogue().sendOption("Who does the money go to?", "I haven't got that much.");
						return true;
					case 19 :
						player.getActionSender().walkTo(player.getPosition().getX() < 3268 ? 1 : -1, 0, true);
						player.getActionSender().walkThroughDoubleDoor(2882, 2883, 3268, 3227, 3268, 3228, 0);
						break;
					case 8 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendNpcChat("The money goes to the city of Al-Kharid.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
				}
				break;
			case 166 : //banker
			case 902 : //gundai @mage bank
			case 494 :
			case 495 :
			case 496 :
			case 2619 :
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("What can I do for you?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("I would like to access my bank account.", "I would like to edit my Bank Pin settings.", "Nothing.");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("I would like to access my bank account.", CONTENT);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("I would like to edit my Bank Pin settings.", CONTENT);
								player.getDialogue().setNextChatId(6);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("Nothing.", CONTENT);
								player.getDialogue().setNextChatId(5);
								return true;
						}
						break;
					case 4 :
						BankManager.openBank(player);
						player.getDialogue().dontCloseInterface();
						break;
					case 5 :
						player.getDialogue().sendNpcChat("Well, just let me know when I can help.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 6 :
						player.getDialogue().sendNpcChat("What would you like to do?", CONTENT);
						return true;
					case 7 :
						if (player.getBankPin().hasBankPin() && !player.getBankPin().hasPendingBankPinRequest()) {
							player.getDialogue().sendOption("I would like to change my bank pin.", "I would like to delete my bank pin.");
						} else if (player.getBankPin().hasBankPin() && player.getBankPin().hasPendingBankPinRequest()) {
							player.getDialogue().sendOption("I would like to delete my pending bank pin request.", "No, nevermind.");
							player.getDialogue().setNextChatId(22);
						} else {
							player.getDialogue().sendOption("I would like to set a bank pin.", "No, nevermind.");
							player.getDialogue().setNextChatId(28);
						}
						return true;
					case 8 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("I would like to change my bank pin.", CONTENT);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("I would like to delete my bank pin.", CONTENT);
								player.getDialogue().setNextChatId(18);
								return true;
						}
						break;
					case 9 :
						player.getDialogue().sendNpcChat("Please carefully select your bank pin.", CONTENT);
						return true;
					case 10 :
						player.getBankPin().startPinInterface(BankPin.PinInterfaceStatus.CHANGING);
						player.getDialogue().dontCloseInterface();
						break;
					case 11 :
						int[] pBP = player.getBankPin().getPendingBankPin();
						player.getDialogue().sendNpcChat("Your bank pin will be set to " + pBP[0] + " " + pBP[1] + " " + pBP[2] + " " + pBP[3] + ".", "Does that sound correct?", CONTENT);
						return true;
					case 12 :
						player.getDialogue().sendOption("Yes.", "No, may I try again?", "No, nevermind.");
						return true;
					case 13 :
						switch(optionId) {
							case 1 :
								player.getBankPin().setChangingBankPin();
								player.getDialogue().sendPlayerChat("Yes.", CONTENT);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("No, may I try again?", CONTENT);
								player.getDialogue().setNextChatId(16);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("No, nevermind.", CONTENT);
								player.getDialogue().setNextChatId(17);
								return true;
						}
						break;
					case 14 :
						if (player.getBankPin().hasBankPin()) {
							player.getDialogue().sendNpcChat("Changes will take affect in 7 days.", "Return to me to edit or delete this change.", CONTENT);
						} else {
							player.getDialogue().sendNpcChat("Your bank pin will be set accordingly.", CONTENT);
						}
						player.getBankPin().checkBankPinChangeStatus();
						return true;
					case 15 :
						player.getDialogue().sendPlayerChat("Will do.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 16 :
						player.getDialogue().sendNpcChat("Sure.", CONTENT);
						player.getBankPin().clearPendingBankPinRequest();
						player.getDialogue().setNextChatId(10);
						return true;
					case 17 :
						player.getDialogue().sendNpcChat("Return to me if you change your mind.", CONTENT);
						player.getBankPin().clearPendingBankPinRequest();
						player.getDialogue().endDialogue();
						return true;
					case 18 :
						player.getDialogue().sendPlayerChat("I would like to delete my bank pin.", CONTENT);
						return true;
					case 19 :
						player.getDialogue().sendNpcChat("Are you sure you would like to delete your bank pin?", CONTENT);
						return true;
					case 20 :
						player.getDialogue().sendOption("Yes.", "No, nevermind.");
						return true;
					case 21 :
						switch(optionId) {
							case 1 :
								player.getBankPin().setDeletingBankPin();
								player.getDialogue().sendPlayerChat("Yes.", CONTENT);
								player.getDialogue().setNextChatId(14);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("No, nevermind.", CONTENT);
								player.getDialogue().setNextChatId(29);
								return true;
						}
						break;
					case 22 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("I would like to delete my pending bank pin request.", CONTENT);
								return true;
						}
						break;
					case 23 :
						player.getDialogue().sendNpcChat("Are you sure?", "This clears any deletion or change request.", CONTENT);
						return true;
					case 24 :
						player.getDialogue().sendOption("Yes.", "No, nevermind.");
						return true;
					case 25 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Yes.", CONTENT);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("No, nevermind.", CONTENT);
								player.getDialogue().setNextChatId(29);
								return true;
						}
						break;
					case 26 :
						player.getBankPin().clearPendingBankPinRequest();
						player.getDialogue().sendNpcChat("Your pending bank pin request has been deleted.", CONTENT);
						return true;
					case 27 :
						player.getDialogue().sendPlayerChat("Thanks.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 28 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("I would like to set my bank pin.", CONTENT);
								player.getDialogue().setNextChatId(9);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("No, nevermind.", CONTENT);
								return true;
						}
						break;
					case 29 :
						player.getDialogue().sendNpcChat("Return to me if you change your mind.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 10001 : // barrows
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendStatement("You've found a hidden tunnel, do you want to enter?");
						return true;
					case 2 :
						player.getDialogue().sendOption("Yeah I'm fearless!", "No way, that looks scary!");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								/*player.getUpdateFlags().sendAnimation(714);
								player.getUpdateFlags().sendHighGraphic(301);
								player.teleport(new Position(3551, 9693));*/
								/*player.getUpdateFlags().sendAnimation(714);
								player.getUpdateFlags().sendHighGraphic(301);
								player.getTeleportation().teleport(3551, 9693, 0, true);*/
								Barrows.shuffleMaze(player, true);
								break;
							case 2 :
								player.getDialogue().sendPlayerChat("No way, that looks scary!", DISTRESSED);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
				}
				break;
			case 960 : //duel arena doctor
			case 961 :
			case 962 :
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hey, what's up? you can call me " + NpcDefinition.forId(player.getClickId()).getName(), "my mission here is to take care of those players", "who would need some help to cure their wounds", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("So, anything I can do for someone like you?", CONTENT);
						return true;
					case 3 :
						player.getDialogue().sendOption("Yes, I would like you to heal me please.", "No, I am just playing around.");
						return true;
					case 4 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Yes, I would like you to heal me please.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("No, I am just playing around.", CONTENT);
								player.getDialogue().setNextChatId(6);
								return true;
								
						}
					case 5 :
						player.getDuelMainData().healPlayer();
						break;
					case 6 :
						player.getDialogue().sendNpcChat("So don't waste my time, seriously...", ANGRY_1);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 741 : // duke horacio
				switch(player.getDialogue().getChatId()) {
					case 1 :
						if(player.questStage[5] == 1 && !player.hasItem(1540)){//antidragon shield
							player.getDialogue().sendNpcChat("Hello, welcome to my kingdom.", CONTENT);
							return true;
						}
					case 2 :
						player.getDialogue().sendOption("Can I have an anti-fire shield?", "Nevermind.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendNpcChat("Sure, it will only cost you 1k.", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Nevermind.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 4 :
						player.getDialogue().sendOption("Ok, here you go.", "Nevermind.");
						return true;
					case 5 :
						switch(optionId) {
							case 1:
								if (!player.getInventory().playerHasItem(new Item(995, 1000))) {
									player.getDialogue().sendPlayerChat("Sorry, I don't have enough coins.", SAD);
								} else if (player.getInventory().getItemContainer().freeSlots() < 1 && player.getInventory().getItemContainer().getCount(995) != 1000) {
									player.getDialogue().sendNpcChat("Looks like you don't have enough inventory space.", CONTENT);
								} else {
									player.getInventory().removeItem(new Item(995, 1000));
									player.getInventory().addItem(new Item(1540, 1));
									break;
								}
								player.getDialogue().endDialogue();
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Nevermind.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
				}
				break;
			case 656 : // entrana dungeon
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Be careful going in there! You are unarmed, and there",
														"is much evilness lurking down there! The evilness seems",
														"to block off our contact with our gods,", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("so our prayers seem to have less effect down there. Oh,",
														"also, you won't be able to come back this way - This",
														"ladder only goes one way!", CONTENT);
						return true;
					case 3 :
						player.getDialogue().sendNpcChat("The only exit from the caves below is a portal which",
														"leads only to the deepest wilderness!", CONTENT);
						return true;
					case 4 :
						player.getDialogue().sendOption("I don't think I'm strong enough to enter then.", "Well that is a risk I will have to take.");
						return true;
					case 5 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("I don't think I'm strong enough to enter then.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Well that is a risk I will have to take.", CONTENT);
								return true;
						}
						break;
					case 6 :
						Ladders.climbLadder(player, new Position(2822, 9774, 0));
						player.getSkill().setSkillLevel(Skill.PRAYER, 1);
						player.getSkill().refresh(Skill.PRAYER);
						player.getDialogue().endDialogue();
						break;	
				}
				break;
			case 2728 : //port sarim monk
			case 2729 :
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to sail to Entrana?", "I will need to check you for dangerous equipment.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								Sailing.sailShip(player, ShipRoute.PORT_SARIM_TO_ENTRANA);
								player.getDialogue().dontCloseInterface();
								break;
							case 2:
								player.getDialogue().sendPlayerChat("No thanks.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
				}
				break;
			case 1304 : //rellekka sailor
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendPlayerChat("Hello. Can I get a ride on your ship?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("Hello again, brother Rilkal. If you're ready to jump",
														"aboard, we're all ready to set sail with the tide!", CONTENT);
						return true;
					case 3 :
						player.getDialogue().sendOption("Let's go!", "Actually, no.");
						return true;
					case 4 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Let's go!", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Actually, no.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 5 :
						Sailing.sailShip(player, ShipRoute.RELLEKKA_TO_MISCELLANIA);
						player.getDialogue().dontCloseInterface();
						player.getActionSender().sendMessage("You board the longship...");
						break;
				}
				break;
			case 1385 : //miscellania sailor
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendPlayerChat("Hello. Can I get a ride on your ship?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("Hello again, brother Rilkal. If you're ready to jump",
														"aboard, we're all ready to set sail with the tide!", CONTENT);
						return true;
					case 3 :
						player.getDialogue().sendOption("Let's go!", "Actually, no.");
						return true;
					case 4 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Let's go!", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Actually, no.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 5 :
						Sailing.sailShip(player, ShipRoute.MISCELLANIA_TO_RELLEKKA);
						player.getDialogue().dontCloseInterface();
						player.getActionSender().sendMessage("You board the longship...");
						break;
				}
				break;	
			case 657 : // entrana monk
				switch(player.getDialogue().getChatId()) {
				case 1 :
					player.getDialogue().sendNpcChat("Would you like to sail back to Port Sarim?", CONTENT);
					return true;
				case 2 :
					player.getDialogue().sendOption("Yes please.", "No thanks.");
					return true;
				case 3 :
					switch(optionId) {
						case 1:
							Sailing.sailShip(player, ShipRoute.ENTRANA_TO_PORT_SARIM);
							player.getDialogue().dontCloseInterface();
							break;
						case 2:
							player.getDialogue().sendPlayerChat("No thanks.", CONTENT);
							player.getDialogue().endDialogue();
							return true;
					}
					break;
				case 4 :
					player.getDialogue().sendNpcChat("How dare you try to take dangerous equipment?", "Come back when you have left it all behind.", ANGRY_1);
					player.getDialogue().endDialogue();
					return true;
			}
			break;
			case 10088 : //item repairing
				switch(player.getDialogue().getChatId()) {
					case 1 :
						Tool broken = Tools.getBrokenTool(player, player.tempMiscInt);
						if(broken == null)
							break;
						player.getDialogue().setLastNpcTalk(player.tempMiscInt2);
						String s = "free";
						if(broken.getRepairCost() != 0)
							s = broken.getRepairCost()+"gp";
						player.getDialogue().sendNpcChat("Quite badly damaged, but easy to repair. Would you",
														"like me to repair it for "+s+"?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes, please.", "No, thank you.");
						return true;	
					case 3 :
						switch(optionId) {
							case 1:
								broken = Tools.getBrokenTool(player, player.tempMiscInt);
								if(broken == null)
									break;
								String itemType = "axe";
								if(broken.getSkill() == Skill.MINING)
									itemType = "pickaxe";
								s = "free";
								if(broken.getRepairCost() != 0)
									s = broken.getRepairCost()+"gp";
								Npc npc = new Npc(player.tempMiscInt2);
								if(Tools.repairTool(player, player.tempMiscInt))
									player.getActionSender().sendMessage(npc.getDefinition().getName()+" fixes your "+itemType+" for "+s+".");
								player.tempMiscInt = -1;
								player.tempMiscInt2 = -1;
								player.getDialogue().endDialogue();
								break;
							case 2:
								player.tempMiscInt = -1;
								player.tempMiscInt2 = -1;
								player.getDialogue().endDialogue();
								break;	
						}
						break;
				}
				break;
			case 10089 : //barrows repairing
				switch(player.getDialogue().getChatId()) {
					case 1 :
						Item item2repair = player.tempMiscItem;
						if(item2repair == null)
							break;
						player.getDialogue().setLastNpcTalk(player.tempMiscInt2);
						player.getDialogue().sendNpcChat("That'll cost you "+Misc.formattedValue(BarrowsItem.repairPrice(item2repair))+" gold coins to fix, are you sure?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes I'm sure!", "On second thoughts, no thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Yes I'm sure!", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("On second thoughts, no thanks.", CONTENT);
								player.tempMiscInt2 = -1;
								player.tempMiscItem = null;
								player.getDialogue().endDialogue();
								return true;
						}
						break;	
					case 4 :
						item2repair = player.tempMiscItem;
						if(item2repair  == null)
							break;
						player.getDialogue().setLastNpcTalk(player.tempMiscInt2);
						if(BarrowsItem.repairItem(player, item2repair))
							player.getDialogue().sendNpcChat("There you go, happy doing business with you!", CONTENT);
						else
							player.getDialogue().sendNpcChat("You do not have enough gold coins!", ANGRY_1);
						player.tempMiscInt2 = -1;
						player.tempMiscItem = null;
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 10002 : //games necklace
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendOption("Burthorpe", "Nowhere");
						return true;
					case 2 :
						switch(optionId) {
							case 1:
								JewelleryTeleport.teleport(player, Teleportation.GAMES_ROOM);
								break;
						}
						break;
				}
				break;
			case 10003 : //glory
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendOption("Edgeville", "Karamja", "Draynor Village", "Al Kharid", "Nowhere");
						return true;
					case 2 :
						switch(optionId) {
						case 1:
							JewelleryTeleport.teleport(player, Teleportation.EDGEVILLE);
							break;
						case 2:
							JewelleryTeleport.teleport(player, Teleportation.KARAMJA);
							break;
						case 3:
							JewelleryTeleport.teleport(player, Teleportation.DRAYNOR_VILLAGE);
							break;
						case 4:
							JewelleryTeleport.teleport(player, Teleportation.AL_KHARID);
							break;
						}
						break;
				}
				break;
			case 10004 : //ring of duel
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendOption("Duel Arena", "Castle Wars", "Nowhere");
						return true;
					case 2 :
						switch(optionId) {
						case 1:
							JewelleryTeleport.teleport(player, Teleportation.DUEL_ARENA);
							break;
						case 2:
							JewelleryTeleport.teleport(player, Teleportation.CASTLE_WARS);
							break;
						}
						break;
				}
				break;
			case 170 : //gnome pilot
			case 1800 : //gnome pilot
			case 3809 : //gnome pilot
			case 3810 : //gnome pilot
			case 3812 : //gnome pilot
				switch(player.getDialogue().getChatId()) {
					case 1 :
						if(player.questStage[95] != 1){
							QuestDefinition questDef = QuestDefinition.forId(95);
							String questName = questDef.getName();
							player.getActionSender().sendMessage("You need to complete "+questName+" to use the glider.");
							player.getDialogue().endDialogue();
							return true;
						}
						player.getDialogue().sendNpcChat("Would you like to fly somewhere on the glider?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Sure.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.getActionSender().sendInterface(802);
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			case 510 : //hajedy
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to travel to Shilo village?", "It will only cost you 10gp.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								Travel.startTravel(player, Route.BRIMHAVEN_TO_SHILO);
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			case 511 : // Vigroy
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to travel to Brimhaven?", "It will cost 10gp.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
						case 1:
							Travel.startTravel(player, Route.SHILO_TO_BRIMHAVEN);
							player.getDialogue().dontCloseInterface();
							break;
						}
						break;
				}
				break;
			case 0 : //Hans
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello, what are you doing here?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("I'm looking for whoever is in charge of this place.", "I have come to kill everyone in this castle!", "I don't know. I'm lost. Where am I?");
						return true;
					case 3 :
						switch(optionId) {
						case 1:
							player.getDialogue().sendNpcChat("The person in charge here is Duke Horacio.", "You can usually find him upstairs in his castle.", CONTENT);
							player.getDialogue().endDialogue();
							return true;
						case 2:
							player.getDialogue().sendPlayerChat("I have come to kill everyone in this castle!", ANGRY_1);
							return true;
						case 3:
							player.getDialogue().sendNpcChat("You are at the Lumbridge Castle.", CONTENT);
							player.getDialogue().endDialogue();
							return true;
						}
						break;
					case 4 :
						final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
						npc.getUpdateFlags().sendForceMessage("Help! Help!");
						npc.getFollowing().stepAway();
						break;
				}
				break;
			case 10005 : //iron ladder
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendOption("Climb up the ladder.", "Climb down the ladder.", "Never mind");
						return true;
					case 2 :
						switch(optionId) {
							case 1:
								Ladders.climbLadder(player, new Position(2544, 3741, 0));
								break;
							case 2:
								Ladders.climbLadder(player, new Position(1798, 4407, 3));
								break;
						}
						break;
				}
				break;
			case 2437 : //jarvald
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to sail to Waterbirth Island?", "It will only cost you 1000 coins.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("YES", "NO");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								Sailing.sailShip(player, ShipRoute.RELLEKKA_TO_WATERBIRTH);
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("So do you have the 1000 coins for my service, and are", "you ready to leave now?", CONTENT);
						player.getDialogue().setNextChatId(3);
						break;
				}
				break;
			case 2436 : // jarvald travel back
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to sail back to Rellekka?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("YES", "NO");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								Sailing.sailShip(player, ShipRoute.WATERBIRTH_TO_RELLEKKA);
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			case 802 : //jered
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello brother, would you like me to bless all", "of your unblessed symbols?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								if (!player.getInventory().playerHasItem(1716)) {
									player.getDialogue().sendNpcChat("It look's like you dont have any symbols", "in your inventory. Come back when you do.", CONTENT);
								} else {
									for (Item item : player.getInventory().getItemContainer().getItems()) {
										if (item != null && item.getId() == 1716) {
											player.getInventory().replaceItemWithItem(new Item(1716), new Item(1718));
										}
									}
									player.getDialogue().sendNpcChat("There you go, your welcome.", CONTENT);
								}
								player.getDialogue().endDialogue();
								return true;
						}
						break;
				}
				break;
			case 10006 : //ladder
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendOption("Climb up the ladder.", "Climb down the ladder.", "Never mind");
						return true;
					case 2 :
						switch(optionId) {
							case 1:
								Ladders.checkClimbLadder(player, "up");
								break;
							case 2:
								Ladders.checkClimbLadder(player, "down");
								break;
						}
						break;
				}
				break;
			case 10007 : //staircase
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendOption("Go up the stairs.", "Go down the stairs.", "Never mind.");
						return true;
					case 2 :
						switch(optionId) {
							case 1:
								Ladders.checkClimbTallStaircase(player, "up");
								break;
							case 2:
								Ladders.checkClimbTallStaircase(player, "down");
								break;
						}
						break;
				}
				break;
			case 222 : //monk
				switch(player.getDialogue().getChatId()) {
				case 1 :
					player.getDialogue().sendNpcChat("Greetings traveller.", CONTENT);
					return true;
				case 2 :
					player.getDialogue().sendOption("Can you heal me? I'm injured.", "Nevermind.");
					return true;
				case 3 :
					switch(optionId) {
					case 1:
						if (player.getSkill().getLevel()[Skill.HITPOINTS] < player.getSkill().getPlayerLevel(Skill.HITPOINTS)) {
							if (player.getInteractingEntity() != null && !player.getInteractingEntity().isDead()) {
								player.getInteractingEntity().getUpdateFlags().sendAnimation(717);
								player.getDialogue().sendNpcChat("Sure, here you go.", CONTENT);
								player.heal((int) (player.getSkill().getPlayerLevel(Skill.HITPOINTS) * 0.3));
								player.getUpdateFlags().sendGraphic(84);
							} else {
								break;
							}
						} else {
							player.getDialogue().sendNpcChat("You already have full hp.", CONTENT);
						}
						player.getDialogue().endDialogue();
						return true;
					}
					break;
				}
				break;
			case 2258 : //mage of zammy
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("How can I help a fellow Zamorak follower?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Can you teleport me to the abyss?", "Can I see your shop?", "I'm not a Zamorak follower!");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Can you teleport me to the abyss?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Can I see your shop?", CONTENT);
								player.getDialogue().setNextChatId(5);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("I'm not a Zamorak follower!", CONTENT);
								player.getDialogue().setNextChatId(6);
								return true;
						}
						break;
					case 4 :
						if(player.questStage[14] != 1){
							QuestDefinition questDef = QuestDefinition.forId(14);
							String questName = questDef.getName();
							player.getDialogue().sendStatement("You need to complete "+questName+" to do this.");
							player.getDialogue().endDialogue();
							return true;
						}
						final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
						Abyss.teleportToAbyss(player, npc);
						break;
					case 5 :
						ShopManager.openShop(player, Shops.findShop(player, id));
						player.getDialogue().dontCloseInterface();
						break;
					case 6 :
						player.getDialogue().sendNpcChat("Then get out of my sight!", ANGRY_1);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 376 : //port sarim sailors
			case 377 :
			case 378 :
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to sail to Karamja?", "It will only cost you 30gp.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								Sailing.sailShip(player, ShipRoute.PORT_SARIM_TO_KARAMJA);
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			case 3852 ://donation vendor
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendOption("Access shop", (player.unlockedSkins != 1 ? "What is a skeleton skin?" : "Switch to skeleton skin."));
						return true;
					case 2 :
						switch(optionId) {
							case 1:
								ShopManager.openShop(player, Shops.findShop(player, id));
								player.getDialogue().dontCloseInterface();
								break;
							case 2:
								if(player.unlockedSkins != 1){
									player.getDialogue().sendNpcChat("A skeleton skin is a character model","It overwrites your current character model","You will never lose it, it is account bound when ativated.", HAPPY);
									return true;
								} else {
									player.setSkeletonAppearance();
									player.getDialogue().sendNpcChat("Come back anytime.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
						}
						break;
//					case 3 :
//						switch(optionId) {
//							case 1:
//								if(player.getDonatorPoints() >= 700){
//									player.setSkeletonAppearance();
//									player.decreaseDonatorPoints(700);
//									player.unlockedSkins = 1;
//								}
//								player.getDialogue().sendNpcChat("Come back anytime.", CONTENT);
//								player.getDialogue().endDialogue();
//								return true;
//							case 2:
//								player.getDialogue().sendNpcChat("Come back anytime.", CONTENT);
//								player.getDialogue().endDialogue();
//								return true;
//						}
//						break;
				}
				break;	
			case 380 : //karamja sailor, customs officer
				switch(player.getDialogue().getChatId()) {
					case 1 :
						if(player.getPosition().getX() > 2815)
							player.getDialogue().sendNpcChat("Would you like to sail back to Port Sarim?", "It will cost 30gp.", CONTENT);
						else
							player.getDialogue().sendNpcChat("Would you like to sail back to Ardougne?", "It will cost 30gp.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								if(player.getPosition().getX() > 2815)
									Sailing.sailShip(player, ShipRoute.KARAMJA_TO_PORT_SARIM);
								else
									Sailing.sailShip(player, ShipRoute.BRIMHAVEN_TO_ARDOUGNE);
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			case 381 : //captain barnaby
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to sail to Brimhaven?", "It will cost 30gp.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								Sailing.sailShip(player, ShipRoute.ARDOUGNE_TO_BRIMHAVEN);
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			case 3117 : //sandwich lady
				SandwichLady lady = new SandwichLady(true);
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("You look hungry to me. I tell you what - ", "have a " + lady.stringSent()[0] + " on me.", HAPPY);
						player.getRandomInterfaceClick().randomNumber = lady.getRandomNumber();
						return true;
					case 2 :
						player.getRandomInterfaceClick().openInterface(3117);
						//player.getRandomInterfaceClick().sendModelsRotation(3117);
						player.getDialogue().dontCloseInterface();
						break;
				}
				break;
			case 1595 : //saniboch
				switch(player.getDialogue().getChatId()) {
					case 1 :
						if (player.isBrimhavenDungeonOpen()) {
							player.getDialogue().sendNpcChat("You have already paid the entrance fee.", "You may enter the dungeon whenever you wish.", CONTENT);
							player.getDialogue().endDialogue();
							return true;
						}
						player.getDialogue().sendNpcChat("Hey, there is a 875 coin fee if you wish", "to enter this dungeon.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Ok, here's 875 coins.", "Nevermind.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								if (player.isBrimhavenDungeonOpen()) {
									player.getDialogue().sendNpcChat("You have already paid the entrance fee.", "You may enter the dungeon whenever you wish.", CONTENT);
									player.getDialogue().endDialogue();
									return true;
								}
								player.getDialogue().sendPlayerChat("Ok, here's 875 coins", CONTENT);
								return true;
						}
						break;
					case 4 :
						if (player.getInventory().removeItem(new Item(995, 875))) {
							player.getDialogue().sendStatement("You give Saniboch 875 coins");
							player.setBrimhavenDungeonOpen(true);
						} else {
							player.getDialogue().sendPlayerChat("Looks like I don't have enough coins.", SAD);
							player.getDialogue().endDialogue();
						}
						return true;
					case 5 :
						player.getDialogue().sendNpcChat("Many thanks. You may now pass the door.", "May your death be a glorious one!", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 10008 : //shop keeper
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello "+player.getUsername()+",", "would you like to see my shop?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								if (player.getInteractingEntity() != null && player.getInteractingEntity().isNpc())
									Shops.openShop(player, ((Npc) player.getInteractingEntity()).getNpcId());
								player.getDialogue().dontCloseInterface();
								break;
						}
						break;
				}
				break;
			case 513 : // yohnus
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendPlayerChat("Can I come through this door?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendNpcChat("Sure, you can enter for a small fee of 20 coins.", CONTENT);
						return true;
					case 3 :
						player.getDialogue().sendOption("Ok, I'll pay.", "No thanks.");
						return true;
					case 4 :
						switch(optionId) {
							case 1:
								if (player.getInventory().playerHasItem(995, 20)) {
									player.getInventory().removeItem(new Item(995, 20));
									player.getActionSender().walkTo(0, player.getPosition().getY() == 2963 ? 1 : 2, true);
									player.getActionSender().walkThroughDoor(2266, 2856, 2963, 0);
									break;
								} else {
									player.getDialogue().sendPlayerChat("Sorry, I don't have that many coins.", SAD);
								}
								player.getDialogue().endDialogue();
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("No thanks.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
				}
				break;
			case 798 : //velrak
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Thank you for rescuing me! It isn't very comfy", "in this cell!", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("So... do you know anywhere good to explore?", "Do I get a reward?");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("So... do you know anywhere good to explore?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Do I get a reward?", CONTENT);
								player.getDialogue().setNextChatId(10);
								return true;
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("Well, this dungeon was quite good to explore ...until I", "got captured, anyway. I was given a key to an inner", "part of this dungeon by a mysterious cloaked", "stranger!", CONTENT);
						return true;
					case 5 :
						player.getDialogue().sendNpcChat("It's rather tough for me to get that far into the", "dungeon however... I just keep getting", "captured! Would you like to give it a go?", CONTENT);
						return true;
					case 6 :
						player.getDialogue().sendOption("Yes please!", "No, it's too dangerous for me too.");
						return true;
					case 7 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Yes please!", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("No, it's too dangerous for me too.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 8 :
						player.getDialogue().sendStatement("Velrak reaches somewhere mysterious and passes you a key.");
						return true;
					case 9 :
						if (player.getInventory().getItemContainer().freeSlots() < 1) {
							player.getDialogue().sendNpcChat("Looks like you don't have enough room", "in your inventory.", CONTENT);
						} else if (player.hasItem(1590)) {
							player.getDialogue().sendNpcChat("I already gave you the key!", CONTENT);
						} else {
							player.getInventory().addItem(new Item(1590));
							break;
						}
						player.getDialogue().endDialogue();
						return true;
					case 10 :
						player.getDialogue().sendNpcChat("I don't have anything expensive to give, sorry.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 10009 : //treasure trials
				boolean b = true;
				switch(player.getDialogue().getChatId()) {
					case 1 :
						if(player.tempItem != null){
							//for(int r1 = 0; r1 < player.tempItem.length; r1++){
								if(!player.getInventory().playerHasItem(player.tempItem[0]))
									b = false;
							//}
							if(b){
								for(int r1 = 0; r1 < player.tempItem.length; r1++){
									player.getInventory().removeItem(player.tempItem[r1]);
								}
								player.tempItem = null;
							}else{
								return false;
							}
						}
						player.puzzleDone = false;
						ClueScroll.itemReward(player, player.clueLevel);
						player.getDialogue().dontCloseInterface();
						break;
					case 2 :
						if(player.tempItem != null){
							//for(int r1 = 0; r1 < player.tempItem.length; r1++){
								if(!player.getInventory().playerHasItem(player.tempItem[0]))
									b = false;
							//}
							if(b){
								/*for(int r1 = 0; r1 < player.tempItem.length; r1++){
									player.getInventory().removeItem(player.tempItem[r1]);
								}
								player.tempItem = null;*/
							}else{
								return false;
							}
						}
						player.puzzleDone = false;
						ClueScroll.clueReward(player, player.clueLevel, "You recieve another clue!", true, "Here is your reward");
						return true;
					case 3 :
						if(player.tempItem != null){
							//for(int r1 = 0; r1 < player.tempItem.length; r1++){
								if(!player.getInventory().playerHasItem(player.tempItem[0]))
									b = false;
							//}
							if(b){
								/*for(int r1 = 0; r1 < player.tempItem.length; r1++){
									player.getInventory().removeItem(player.tempItem[r1]);
								}
								player.tempItem = null;*/
							}else{
								return false;
							}
						}
						player.getActionSender().removeInterfaces();
						player.getDialogue().resetDialogue();
						player.getActionSender().openXInterface(207);
						player.getDialogue().dontCloseInterface();
						break;
				}
				break;
			case 2824:
			case 804:
			case 1041: //tanner
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Greetings friend. I am a manufacturer of leather.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Can I buy some leather then?", "Leather is rather weak stuff.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Can I buy some leather then?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Leather is rather weak stuff.", CONTENT);
								player.getDialogue().setNextChatId(5);
								return true;
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("I make leather from animal hides. Bring me some", "cowhides and one gold per hide, and I'll tan them", "into soft leather for you.", HAPPY);
						player.getDialogue().endDialogue();
						return true;
					case 5 :
						player.getDialogue().sendNpcChat("Normal leather may be quite weak, but it's", "very cheap - I make it from cowhides for only 1 gp", "per hide - and it's so easy to craft that anyone", "can work with it.", HAPPY);
						return true;
					case 6 :
						player.getDialogue().sendNpcChat("Alternatively you could try hard leather. It's", "not so easy to craft, but I only charge 3 gp", "per cowhide to prepare it, and it makes much", "sturdier armour.", HAPPY);
						return true;
					case 7 :
						player.getDialogue().sendNpcChat("I can also tan snake hides and dragonhides,", "suitable for crafting into the highest quality", "armour for rangers.", HAPPY);
						return true;
					case 8 :
						player.getDialogue().sendPlayerChat("Thanks, I'll bear it in mind.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 10011 : //spirit tree
				player.getDialogue().setLastNpcTalk(3637);
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello " + player.getUsername(), "Would you like a free trip somewhere?", -1);
						return true;
					case 2 :
						player.getDialogue().sendOption("The Tree Gnome Village", "The Gnome Stronghold", "Battlefield", "Varrock", "Nowhere");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendStatement("You place your hands on the dry tough bark of the spirit tree, and", "feel a surge of energy run through your veins.");
								return true;
							case 2:
								player.getDialogue().sendStatement("You place your hands on the dry tough bark of the spirit tree, and", "feel a surge of energy run through your veins.");
								player.getDialogue().setNextChatId(5);
								return true;
							case 3:
								player.getDialogue().sendStatement("You place your hands on the dry tough bark of the spirit tree, and", "feel a surge of energy run through your veins.");
								player.getDialogue().setNextChatId(6);
								return true;
							case 4:
								player.getDialogue().sendStatement("You place your hands on the dry tough bark of the spirit tree, and", "feel a surge of energy run through your veins.");
								player.getDialogue().setNextChatId(7);
								return true;	
						}
						break;
					case 4 :
						Ladders.climbLadder(player, new Position(2542, 3169, 0));
						break;
					case 5 :
						Ladders.climbLadder(player, new Position(2462, 3444, 0));
						break;
					case 6 :
						Ladders.climbLadder(player, new Position(2556, 3259, 0));
						break;
					case 7 :
						Ladders.climbLadder(player, new Position(3179, 3506, 0));
						break;
				}
				break;
			case 3636 : //spirit tree
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hello " + player.getUsername() + ", First of all -", "Thank you for giving me life !", "Let me thank you by offering you a trip", -1);
						return true;
					case 2 :
						player.getDialogue().sendOption("The Tree Gnome Village", "The Gnome Stronghold", "Battlefield", "Varrock", "Nowhere");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendStatement("You place your hands on the dry tough bark of the spirit tree, and", "feel a surge of energy run through your veins.");
								return true;
							case 2:
								player.getDialogue().sendStatement("You place your hands on the dry tough bark of the spirit tree, and", "feel a surge of energy run through your veins.");
								player.getDialogue().setNextChatId(5);
								return true;
							case 3:
								player.getDialogue().sendStatement("You place your hands on the dry tough bark of the spirit tree, and", "feel a surge of energy run through your veins.");
								player.getDialogue().setNextChatId(6);
								return true;
							case 4:
								player.getDialogue().sendStatement("You place your hands on the dry tough bark of the spirit tree, and", "feel a surge of energy run through your veins.");
								player.getDialogue().setNextChatId(7);
								return true;	
						}
						break;
					case 4 :
						Ladders.climbLadder(player, new Position(2542, 3169, 0));
						break;
					case 5 :
						Ladders.climbLadder(player, new Position(2462, 3444, 0));
						break;
					case 6 :
						Ladders.climbLadder(player, new Position(2556, 3259, 0));
						break;
					case 7 :
						Ladders.climbLadder(player, new Position(3179, 3506, 0));
						break;	
				}
				break;
			case 2323 ://farmer
			case 2324 :
			case 2325 :
			case 2326 :
			case 2327 :
			case 2330 :
			case 2331 :
			case 2332 :
			case 2333 :
			case 2334 :
			case 2335 :
			case 2336 :
			case 2337 :
			case 2338 :
			case 2339 :
			case 2340 :
			case 2341 :
			case 2342 :
			case 2343 :
			case 2344 :
				if (!Constants.FARMING_ENABLED) {
					player.getActionSender().sendMessage("This skill is currently disabled.");
					break;
				}
				Farmers.FarmersData farmersData = Farmers.FarmersData.forId(player.getClickId());
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Hey, I am one of the master farmers of this world", "but you can call me " + NpcDefinition.forId(player.getClickId()).getName(), "So, what do you need from me?", CONTENT);
						if (farmersData.getFieldProtected() != "tree")
							player.getDialogue().setNextChatId(16);
						return true;
					case 2 :
						player.getDialogue().sendOption("Would you chop my tree down for me?", "Could you take care of my crops for me?", "Can you give me any farming advice?", "Can you sell me something?", "That's all, thanks");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Would you chop my tree down for me?", CONTENT);
								player.getDialogue().setNextChatId(11);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("Could you take care of my crops for me?", CONTENT);
								if (farmersData.getFieldProtected() != "allotment")
									player.getDialogue().setNextChatId(7);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("Can you give me any farming advice?", CONTENT);
								player.getDialogue().setNextChatId(8);
								return true;
							case 4 :
								player.getDialogue().sendPlayerChat("Can you sell me something?", CONTENT);
								player.getDialogue().setNextChatId(9);
								return true;
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("I Might, Which one were you thinking of?", HAPPY);
						return true;
					case 5 :
						player.getDialogue().sendOption(farmersData.getDialogueOptions());
						return true;
					case 6 :
						switch(optionId) {
							case 1:
								Farmers.ProtectPlant(player, 0, "allotment", player.getClickId(), 1);
								//player.getDialogue().dontCloseInterface();
								return true;
							case 2:
								Farmers.ProtectPlant(player, 1, "allotment", player.getClickId(), 1);
								//player.getDialogue().dontCloseInterface();
								return true;
						}
						break;
					case 7 :
						Farmers.ProtectPlant(player, -1, farmersData.getFieldProtected(), player.getClickId(), 1);
						//player.getDialogue().dontCloseInterface();
						return true;
					case 8 :
						Farmers.sendFarmingAdvice(player);
						player.getDialogue().dontCloseInterface();
						return true;
					case 9 :
						player.getDialogue().sendNpcChat("Sure, I have a bunch of tools for you to use.", HAPPY);
						return true;
					case 10 :
						//ShopManager.openShop(player, farmersData.getShopId());
						ShopManager.openShop(player, Shops.findShop(player, id));
						player.getDialogue().dontCloseInterface();
						break;
					case 11 :
						player.getDialogue().sendNpcChat("Sure, for only 200gp, I will chop it down for you", CONTENT);
						return true;
					case 12 :
						player.getDialogue().sendOption("Sure, here you go", "Sorry, I am a little broke.");
						return true;
					case 13 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Sure, Here you go", CONTENT);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("Sorry, I am a litle broke", ANNOYED);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 14 :
						Farmers.chopDownTree(player, player.getClickId());
						player.getDialogue().dontCloseInterface();
						return true;
					case 15 :
						player.getDialogue().sendNpcChat("Sorry, but you have no tree growing in this patch.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 16 :
						player.getDialogue().sendOption("Could you take care of my crops for me?", "Can you give me any farming advice?", "Can you sell me something?", "That's all, thanks");
						return true;
					case 17 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Could you take care of my crops for me?", CONTENT);
								if (farmersData.getFieldProtected() != "allotment")
									player.getDialogue().setNextChatId(7);
								else
									player.getDialogue().setNextChatId(4);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Can you give me any farming advice?", CONTENT);
								player.getDialogue().setNextChatId(8);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Can you sell me something?", CONTENT);
								player.getDialogue().setNextChatId(9);
								return true;
						}
						break;
					case 18 :
						player.getDialogue().sendOption("Sure, here you go", "Sorry, I don't have those at the moment.");
						return true;
					case 19 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Sure, here you go", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("Sorry, I don't have those at the moment.", ANNOYED);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 20 :
						Farmers.ProtectPlant(player, player.getTempInteger(), farmersData.getFieldProtected(), player.getClickId(), 2);
						player.getDialogue().dontCloseInterface();
						break;
				}
				break;
			case 953 : //tut banker
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Good day, would you like to access your bank account?", HAPPY);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
						case 1 :
							BankManager.openBank(player);
							player.getDialogue().dontCloseInterface();
							break;
						}
						break;
				}
				break;
			case 3021 : //leprechaun
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Ah, 'tis a foine day to be sure! Were yez wantin' me to", "store yer tools, or maybe ye might be wantin' yer stuff", "back from me?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("What tools can you store?", "Open your tool store, please.", "Actually, I'm fine.");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("What tools can you store?", CONTENT);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("Open your tool store, please.", CONTENT);
								player.getDialogue().setNextChatId(7);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("Actually, I'm fine.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("We'll hold onto yer rake, seed dibber, spade, secateurs,", "waterin' can and trowel - but mind it's not one of them", "fancy trowels only archaeologist use.", HAPPY);
						return true;
					case 5 :
						player.getDialogue().sendNpcChat("We'll take a few buckets off yer hand if you want", "too, and even yer compost and supercompost. There's", "room in our shed for plenty of compost, so bring it on.", HAPPY);
						return true;
					case 6 :
						player.getDialogue().sendNpcChat("Also, if ye hands us yer Farming produce,", "we might be able to change it into banknotes.", HAPPY);
						player.getDialogue().setNextChatId(2);
						return true;
					case 7 :
						player.getFarmingTools().loadInterfaces();
						player.getDialogue().dontCloseInterface();
						break;
				}
				break;
			case 2244 : //lumbridge guide
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Greetings adventurer. I am Phileas the Lumbridge", "Guide. I am here to give information and directions to", "new players. Do you require any help?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes please.", "No, I can find things myself thank you.");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Yes please.", CONTENT);
								return true;
							case 2 :
								player.getDialogue().sendNpcChat("If you ever need help, you can talk to me again.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("First I must warn you to take every precaution to", "keep your password and PIN secure. The", "most important thing to remember is to never give your", "password to, or share your account with, anyone.", CONTENT);
						return true;
					case 5 :
						player.getDialogue().sendNpcChat("I have much more information to impart; what would", "you like to know about?", CONTENT);
						return true;
					case 6:
						player.getDialogue().sendOption("Where can I find a quest to go on?", "What monsters should I fight?", "Where can I make money?", "How can I heal myself?", "Where can I find a bank?");
						return true;
					case 7:
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Where can I find a quest to go on?", CONTENT);
								player.getDialogue().setNextChatId(16);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("What monsters should I fight?", CONTENT);
								player.getDialogue().setNextChatId(34);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("Where can I make money?", CONTENT);
								player.getDialogue().setNextChatId(18);
								return true;
							case 4:
								player.getDialogue().sendPlayerChat("How can I heal myself?", CONTENT);
								player.getDialogue().setNextChatId(12);
								return true;
							case 5 :
								player.getDialogue().sendPlayerChat("Where can I find a bank?", CONTENT);
								return true;
						}
						break;
					case 8:
						player.getDialogue().sendNpcChat("The nearest bank is in Draynor Village - go", "west from here.", CONTENT);
						return true;
					case 9:
						player.getDialogue().sendNpcChat("Is there anything else you need help with?", CONTENT);
						return true;
					case 10:
						player.getDialogue().sendOption("No thank you.", "Yes please.");
						return true;
					case 11:
						switch(optionId) {
							case 1 :
								player.getDialogue().sendNpcChat("If you ever need help, you can talk to me again.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("Yes please.", CONTENT);
								player.getDialogue().setNextChatId(6);
								return true;
						}
						break;
					case 12:
						player.getDialogue().sendNpcChat("You will always heal slowly over time, but people", "normally choose to heal themselves faster by eating food.", CONTENT);
						return true;
					case 13:
						player.getDialogue().sendNpcChat("There are many different foods in the game such as", "cabbage, fish, meat and many more. Which do you wish", "to hear about?", CONTENT);
						return true;
					case 14:
						player.getDialogue().sendOption("How do I get cabbages?", "How do I fish?", "Where can I find meat?", "Nevermind.");
						return true;
					case 15:
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("How do I get cabbages?", CONTENT);
								player.getDialogue().setNextChatId(19);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("How do I fish?", CONTENT);
								player.getDialogue().setNextChatId(23);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("Where can I find meat?", CONTENT);
								player.getDialogue().setNextChatId(29);
								return true;
							case 4 :
								player.getDialogue().sendNpcChat("If you ever need help, you can talk to me again.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 16 :
						player.getDialogue().sendNpcChat("Well, I heard my friend the cook was in need of a spot", "of help. He'll be in the kitchen of this here castle. Just", "talk to him and he'll set you off.", CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					/*case 17 :
						player.getDialogue().sendNpcChat("You should fight monsters that are near", "your combat level.", CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;*/
					case 18 :
						player.getDialogue().sendNpcChat("There are many ways to make money in the game. I", "would suggest either killing monsters or doing a trade", "skill such as Smithing or Fishing.", CONTENT);
						player.getDialogue().setNextChatId(31);
						return true;
					case 19 :
						player.getDialogue().sendNpcChat("There is a field a little distance to the north of here", "packed full of cabbages which are there for the picking.", CONTENT);
						player.getDialogue().setNextChatId(20);
						return true;
					case 20:
						player.getDialogue().sendNpcChat("Is there anything else you need help with?", CONTENT);
						return true;
					case 21:
						player.getDialogue().sendOption("No thank you.", "I'd like to know about other food.", "Yes please.");
						return true;
					case 22:
						switch(optionId) {
							case 1 :
								player.getDialogue().sendNpcChat("If you ever need help, you can talk to me again.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 2 :
								player.getDialogue().sendOption("How do I get cabbages?", "How do I fish?", "Where can I find meat?", "Nevermind.");
								player.getDialogue().setNextChatId(15);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("Yes please.", CONTENT);
								player.getDialogue().setNextChatId(6);
								return true;
						}
						break;
					case 23:
						player.getDialogue().sendNpcChat("Fishing spots require different levels and equipment to", "use. To start Fishing, you'll want to talk to the Fishing", "tutor who can be found in the swamps south of here.", "He will also give you a small fishing net if you don't", CONTENT);
						return true;
					case 24 :
						player.getDialogue().sendNpcChat("own one already.", CONTENT);
						return true;
					case 25 :
						player.getDialogue().sendNpcChat("You will need some Fishing equipment. At the Fishing", "spots to the south you can only use a small fishing net.", CONTENT);
						return true;
					case 26 :
						player.getDialogue().sendPlayerChat("Where could I find one of those?", CONTENT);
						return true;
					case 27 :
						player.getDialogue().sendNpcChat("You can get them from a Fishing shop or our Fishing", "Tutor south of here in the swamp. There is a Fishing", "shop in Port Sarim; you can find it on the world map.", "Port Sarim is some way to the west of here, beyond", CONTENT);
						return true;
					case 28 :
						player.getDialogue().sendNpcChat("the village of Draynor.", CONTENT);
						player.getDialogue().setNextChatId(20);
						return true;
					case 29 :
						player.getDialogue().sendNpcChat("I suggest you go and kill some chickens. The roads on", "either side if this river eventually go past a chicken", "farm. When you have killed some chickens, cook them.", "You cold either make a fire or use a range.", CONTENT);
						return true;
					case 30 :
						player.getDialogue().sendNpcChat("There is a range at the southern end in this town and", "a Cooking tutor in south Lumbridge near Bob's Brilliant", "Axes shop.", CONTENT);
						player.getDialogue().setNextChatId(20);
						return true;
					case 31 :
						player.getDialogue().sendNpcChat("Please don't try to get money by begging of other", "players. It will make you unpopular. Nobody likes a", "beggar. It is very irritating to have other players asking", "for your hard-earned cash.", CONTENT);
						return true;
					case 32:
						player.getDialogue().sendOption("Where can I smith?", "How do I fish?", "What monsters should I fight?");
						return true;
					case 33:
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Where can I smith?", CONTENT);
								player.getDialogue().setNextChatId(45);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("How do I fish?", CONTENT);
								player.getDialogue().setNextChatId(23);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("What monsters should I fight?", CONTENT);
								return true;
						}
						break;
					case 34 :
						player.getDialogue().sendNpcChat("There's lots of beasts to fight in the woods around here,", "especially to the west. There are certainly some goblins", "and spiders that are pests and could do with being", "cleared out. There's also a chicken farm or two up the", CONTENT);
						return true;
					case 35 :
						player.getDialogue().sendNpcChat("road for some fairly easy picking. Non-player", "characters usually appear as yellow dots on your mini-", "map, although there are some that you won't be able to", "fight, such as myself. A monster's combat level is shown", CONTENT);
						return true;
					case 36 :
						player.getDialogue().sendNpcChat("next to their 'Attack' option. If that level is coloured", "green it means the monster is weaker than you. If it is", "red, it means that the monster is tougher than you.", CONTENT);
						return true;
					case 37 :
						player.getDialogue().sendNpcChat("Remember, you will do better if you have better", "armour and weapons and it's always worth carrying a", "bit of food to heal yourself.", CONTENT);
						return true;
					case 38 :
						player.getDialogue().sendOption("Where can I get food to heal myself?", "Where can I get better armour and weapons?", "Okay, thanks, I will go and kill things.", "Can I kill other players?", "I'd like to know about something else.");
						return true;
					case 39 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("Where can I get food to heal myself?", CONTENT);
								player.getDialogue().setNextChatId(13);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("Where can I get better armour and weapons?", CONTENT);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("Okay, thanks, I will go and kill things.", CONTENT);
								player.getDialogue().endDialogue();
								return true;
							case 4 :
								player.getDialogue().sendPlayerChat("Can I kill other players?", CONTENT);
								player.getDialogue().setNextChatId(49);
								return true;
							case 5 :
								player.getDialogue().sendPlayerChat("I'd like to know about something else.", CONTENT);
								player.getDialogue().setNextChatId(6);
								return true;
						}
						break;
					case 40 :
						player.getDialogue().sendNpcChat("Well, you can make them, you buy them or talk to the", "combat tutors just west of here.", CONTENT);
						return true;
					case 41 :
						player.getDialogue().sendOption("How do I make a weapon?", "Where can I buy a weapon?", "Could I get a staff like yours?");
						return true;
					case 42 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendPlayerChat("How do I make a weapon?", CONTENT);
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("Where can I buy a weapon?", CONTENT);
								player.getDialogue().setNextChatId(47);
								return true;
							case 3 :
								player.getDialogue().sendPlayerChat("Could I get a staff like yours?", CONTENT);
								player.getDialogue().setNextChatId(48);
								return true;
						}
						return true;
					case 43 :
						player.getDialogue().sendNpcChat("The Smithing skill allows you to make armour and", "weapons. Talk to the boy who smelts metal in the", "furnace, I'm sure he can help", CONTENT);
						return true;
					case 44 :
						player.getDialogue().sendPlayerChat("Where can I smith?", CONTENT);
						return true;
					case 45 :
						player.getDialogue().sendNpcChat("You will find a helpful Smithing tutor in the west of", "Varrock - that's north of here. Follow the path across", "the river and head north", CONTENT);
						return true;
					case 46 :
						player.getDialogue().sendNpcChat("I suggest you go and mine some ore; find the Mining", "symbol - with the guide symbol near it - in the swamp", "south of here. The Mining guide there can teach you", "how to mine ore.", CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					case 47 :
						player.getDialogue().sendNpcChat("You can buy a sword from any sword shop, such as", "the one in Varrock - located north of here. Simply", "look for the sword icon on the mini-map, and you'll", "find the store.", CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					case 48 :
						player.getDialogue().sendNpcChat("Sorry, my staff is not for sale. However, if your", "interested in buy a staff, visit Zeke's staff", "shop located in Varrock, abit north of here.", CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					case 49 :
						player.getDialogue().sendNpcChat("To fight other players, you need to visit the duel", "arena, where you can fight players for fun or", "for stakes. However, if you want a more dangerous", "challenge, you can visit the wilderness.", CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
				}
				break;
			case 599 : //makeover mage
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Greetings, " + Misc.formatPlayerName(player.getUsername()) + ".", "How may I assist you?", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Can you change my appearance?", "That's a nice necklace you have, can I buy one?", "I'm fine, thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.getDialogue().sendPlayerChat("Can you change my appearance?", CONTENT);
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("That's a nice necklace you have, can I buy one?", CONTENT);
								player.getDialogue().setNextChatId(8);
								return true;
							case 3:
								player.getDialogue().sendPlayerChat("I'm fine, thanks.", CONTENT);
								player.getDialogue().setNextChatId(7);
								return true;
									
						}
						break;
					case 4 :
						player.getDialogue().sendNpcChat("Sure. It will only cost you 1000 coins.", CONTENT);
						return true;
					case 5 :
						player.getDialogue().sendOption("Alright, here you go.", "Nevermind.");
						return true;
					case 6 :
						switch(optionId) {
							case 1:
								if (player.getInventory().removeItem(new Item(995, 1000))) {
									player.getActionSender().sendInterface(3559);
									player.getDialogue().dontCloseInterface();
									break;
								} else {
									player.getDialogue().sendPlayerChat("Sorry, looks like I don't have enough coins for that.", SAD);
								}
								player.getDialogue().endDialogue();
								return true;
							case 2:
								player.getDialogue().sendPlayerChat("I'm fine, thanks.", CONTENT);
								return true;
						}
						break;
					case 7 :
						player.getDialogue().sendNpcChat("I'm a busy man.", "Come back when you need something.", CONTENT);
						player.getDialogue().endDialogue();
						return true;
					case 8 :
						player.getDialogue().sendNpcChat("Sure, I can sell you a copy for 100 coins.", CONTENT);
						return true;
					case 9 :
						player.getDialogue().sendOption("Alright, here you go.", "Nevermind.");
						return true;
					case 10 :
						switch(optionId) {
							case 1:
								if (player.getInventory().removeItem(new Item(995, 100))) {
									player.getInventory().addItem(new Item(7803));
									player.getDialogue().sendNpcChat("Thanks, here's your amulet.", CONTENT);
								} else {
									player.getDialogue().sendPlayerChat("Sorry, looks like I don't have enough coins for that.", SAD);
								}
								player.getDialogue().endDialogue();
								return true;
						}
						break;
				}
				break;
			case 10010 : //duel arena forfeit
				if (!player.getDuelMainData().canStartDuel()) {
					break;
				}
				if (RulesData.NO_FORFEIT.activated(player)) {
					player.getActionSender().sendMessage("Forfeiting is disabled in this match!");
					break;
				}
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendStatement("Are you sure you want to forfeit?");
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes, I want to give up.", "No, I'll keep fighting!");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.getDuelMainData().handleDeath(true);
								return true;
						}
						break;
				}
				break;
			case 10012 : // enchanted gem
				if (player.getSlayer().slayerMaster < 1) {
					player.getDialogue().sendStatement("You have currently no task assigned. Talk to any", "slayer master to recieve one.");
					player.getDialogue().endDialogue();
					return true;
				}
				SlayerMasterData slayerMasterData = SlayerMasterData.forId(player.getSlayer().slayerMaster);
				player.getDialogue().setLastNpcTalk(player.getSlayer().slayerMaster);
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat((new StringBuilder()).append("Hello there, ").append(player.getUsername()).append(", what can I help you with?").toString(), HAPPY);
						return true;
					case 2 :
						player.getDialogue().sendOption(new String[] { "How am I doing so far?", "Who are you?", "Where are you?", "Got any tips for me?", "Nothing really." });
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								player.getDialogue().sendNpcChat((new StringBuilder()).append("You're currently assigned to kill ").append(player.getSlayer().slayerTask).append("s;").toString(), (new StringBuilder()).append("only ").append(player.getSlayer().taskAmount).append(" more to go.").toString(), HAPPY);
								player.getDialogue().setNextChatId(2);
								return true;
							case 2 :
								player.getDialogue().sendNpcChat((new StringBuilder()).append("My name's ").append(new Npc(player.getSlayer().slayerMaster).getDefinition().getName()).append("; I'm a Slayer Master.").toString(), HAPPY);
								player.getDialogue().setNextChatId(2);
								return true;
							case 3 :
								player.getDialogue().sendNpcChat((new StringBuilder()).append("I'm in ").append(slayerMasterData.getMasterLocation()).append(". Only a fool would forget that.").toString(), HAPPY);
								player.getDialogue().setNextChatId(2);
								return true;
							case 4 :
								SlayerTipsData slayerTipsData = SlayerTipsData.forName(player.getSlayer().slayerTask);
								if (slayerTipsData == null) {
									player.getDialogue().sendNpcChat("There is no tips about this npc yet.", HAPPY);
								} else {
									player.getDialogue().sendNpcChat(slayerTipsData.getMonsterTips(), HAPPY);
								}
								player.getDialogue().setNextChatId(2);
								return true;
						}
						break;
				}
				break;
			case 70 : //slayer masters
			case 1596 :
			case 1597 :
			case 1598 :
			case 1599 :
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("'Ello, and what are you after then?", HAPPY);
						return true;
					case 2 :
						player.getDialogue().sendOption("I need another assignment", "Do you have anything for trade?", "Er...nothing");
						return true;
					case 3 :
						switch(optionId) {
							case 1 :
								if(id != Slayer.TURAEL){
									if (player.getSlayer().slayerMaster != 0) {
										player.getDialogue().sendNpcChat((new StringBuilder()).append("You're still hunting ").append(player.getSlayer().slayerTask).append("s; come back").toString(), "when you've finished your task.", Dialogues.HAPPY);
										player.getDialogue().endDialogue();
									} else {
										player.getSlayer().assignNewTask(id);
									}
								} else {
									if (player.getSlayer().slayerMaster == Slayer.TURAEL) {
										player.getDialogue().sendNpcChat((new StringBuilder()).append("You're still hunting ").append(player.getSlayer().slayerTask).append("s; come back").toString(), "when you've finished your task.", Dialogues.HAPPY);
										player.getDialogue().endDialogue();
									} else {
										player.getSlayer().assignNewTask(id);
									}
								}
								return true;
							case 2 :
								player.getDialogue().sendNpcChat("I have a wide selection of Slayer equipment; take a look!", Dialogues.HAPPY);
								player.getDialogue().setNextChatId(6);
								return true;
						}
						break;
					case 4 :
						player.getDialogue().sendOption("Do you have any tips for me?", "Thanks, I'll be on my way.");
						return true;
					case 5 :
						switch(optionId) {
							case 1 :
								SlayerTipsData slayerTipsData = SlayerTipsData.forName(player.getSlayer().slayerTask);
								if (slayerTipsData == null) {
									player.getDialogue().sendNpcChat("There is no tips about this npc yet.", HAPPY);
								} else {
									player.getDialogue().sendNpcChat(slayerTipsData.getMonsterTips(), HAPPY);
								}
								player.getDialogue().endDialogue();
								return true;
							case 2 :
								player.getDialogue().sendPlayerChat("Thanks, I'll be on my way.", HAPPY);
								player.getDialogue().endDialogue();
								return true;
						}
						break;
					case 6 :
						ShopManager.openShop(player, 162);
						player.getDialogue().dontCloseInterface();
						break;
				}
				break;
			case 956 : //drunken dwarf
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("'Ere, matey, 'ave some 'o the good stuff.", HAPPY);
						return true;
					case 2 :
						player.getInventory().addItemOrDrop(new Item(1971));
						player.getInventory().addItemOrDrop(new Item(1917));
						player.getDialogue().send2Items2Lines("The dwarf gives you beer and a kebab.", "", new Item(1971), new Item(1917));
						RandomEvent.destroyEventNpc(player);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 409 : //genie
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Here you go "+player.getUsername()+".", HAPPY);
						return true;
					case 2 :
						player.getInventory().addItemOrDrop(new Item(2528));
						player.getDialogue().sendItem1Line("The genie gives you a lamp.", new Item(2528));
						RandomEvent.destroyEventNpc(player);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 2476 : //rick
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Today is your lucky day, sirrah!", "I am  donating to the victims of crime to atone", "for my past actions!", HAPPY);
						return true;
					case 2 :
						Item[] items = new Item[]{new Item(995, 50), new Item(1969), new Item(985), new Item(987), new Item(1623), new Item(1621), new Item(1619), new Item(1617)};
						Item reward = items[Misc.randomMinusOne(items.length)];
						player.getInventory().addItemOrDrop(reward);
						player.getDialogue().sendItem1Line("Rick hands you "+reward.getDefinition().getName().toLowerCase()+".", reward);
						RandomEvent.destroyEventNpc(player);
						player.getDialogue().endDialogue();
						return true;
				}
				break;
			case 2540 : //dr jekyll
				switch(player.getDialogue().getChatId()) {
				case 1 :
					if (player.getRandomHerb() == null) {
						player.setRandomHerb(TalkToEvent.randomHerb());
					}
					player.getDialogue().sendNpcChat("Hello "+player.getUsername()+",", "would you happen to have a "+player.getRandomHerb().getDefinition().getName().toLowerCase()+"?", HAPPY);
					return true;
				case 2 :
					player.getDialogue().sendOption("Yes I do, here you go.", "No I don't, sorry.");
					return true;
				case 3 :
					switch(optionId) {
						case 1:
							if (player.getInventory().removeItem(player.getRandomHerb())) {
								player.getDialogue().sendNpcChat("Oh thank you so much, here have this potion.", HAPPY);
							} else {
								player.getDialogue().sendNpcChat("Looks like you don't have it. Oh well,", "have this potion I don't need anyways.", HAPPY);
								player.setRandomHerb(new Item(1));
							}
							return true;
						case 2:
							player.setRandomHerb(new Item(1));
							player.getDialogue().sendNpcChat("Oh well, was worth a try.", "Here, have this potion I don't need.", HAPPY);
							return true;
					}
					break;
				case 4 :
					Item reward = TalkToEvent.rewardForHerb(player.getRandomHerb().getId());
					player.getInventory().addItemOrDrop(reward);
					//player.getDialogue().sendGiveItemNpc("Jekyll hands you "+reward.getDefinition().getName().toLowerCase()+".", reward);
					player.getDialogue().send2Items2Lines("Jekyll hands you "+reward.getDefinition().getName().toLowerCase()+".", "", new Item(-1, 1), reward);
					player.setRandomHerb(null);
					RandomEvent.destroyEventNpc(player);
					player.getDialogue().endDialogue();
					return true;
				}
				break;
			case 500 : //Mosol Rei
				switch(player.getDialogue().getChatId()) {
					case 1 :
						player.getDialogue().sendNpcChat("Would you like to enter this village?", "Note that once you enter, you cannot get out", "through this way.", CONTENT);
						return true;
					case 2 :
						player.getDialogue().sendOption("Yes let me in please.", "No thanks.");
						return true;
					case 3 :
						switch(optionId) {
							case 1:
								player.fadeTeleport(new Position(2876, 2952));
								return true;
						}
						break;
				}
				
				
		}
		if (player.getDialogue().getChatId() > 1) {
			player.getActionSender().removeInterfaces();
		}
		if (player.getDialogue().getDialogueId() > -1) {
			player.getDialogue().resetDialogue();
		}
		return false;
	}

}
