package com.rs2.model.content.questing.quests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.content.skills.Tools.Tool;
import com.rs2.model.content.skills.agility.CrossObstacle;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;

public class TreeGnomeVillage extends Quest {
	
	final int rewardQP = 2;
	
	public TreeGnomeVillage(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		int val = player.questStage[this.getId()] - 5;
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to Bolren at the center",
							"of the Tree Gnome maze, West of Port Khazard",
							"",
							"I need to be able to defeat a level 112 Warlord"};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should speak with Commander Montai, who can be found",
							"somewhere in the battlefield."};
			return text;
		}
		if(stage == 3){
			String text[] = {"Commander Montai asked me to bring: 6 x Logs."};
			return text;
		}
		if(stage == 4){
			String text[] = {"I should speak with Commander Montai for further",
							"instructions."};
			return text;
		}
		if(stage >= 5 && stage < 19){
			String text[] = {"I need to find the tracker gnomes to get the coordinates:",
							(((val & Misc.getActualValue(1)) == 0 || val == 0) ? "" : "@str@")+ "Tracker gnome 1",
							(((val & Misc.getActualValue(2)) == 0 || val == 0) ? "" : "@str@")+ "Tracker gnome 2",
							(((val & Misc.getActualValue(3)) == 0 || val == 0) ? "" : "@str@")+ "Tracker gnome 3"
							};
			return text;
		}
		if(stage == 19){
			String text[] = {"I should fire the ballista."};
			return text;
		}
		if(stage == 20){
			if(!player.hasItem(QuestConstants.ORB_OF_PROTECTION)){
				String text[] = {"I should go search the stronghold for the orb."};
				return text;
			} else {
				String text[] = {"I should bring the orb to King Bolren."};
				return text;
			}
		}
		if(stage == 21){
			if(!player.hasItem(QuestConstants.ORBS_OF_PROTECTION)){
				String text[] = {"I need to find the Khazard warlord and bring back the orbs."};
				return text;
			} else {
				String text[] = {"I should bring the orbs to King Bolren."};
				return text;
			}
		}
		if(stage == 22){
			String text[] = {"I should speak with King Bolren to finish this quest."};
			return text;
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "2 Quest Points", "11,450 Attack XP"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("2 Quest Points", 12150);
		player.getActionSender().sendString("11,450 Attack XP", 12151);
		player.getActionSender().sendString("Gnome Amulet of Protection", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.ATTACK, 11450);
		player.getInventory().addItemOrDrop(new Item(QuestConstants.GNOME_AMULET, 1));
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.GNOME_AMULET);
		player.getActionSender().sendInterface(12140);
	}
	
	private List<Integer> answers = Arrays.asList(1, 2, 3, 4);
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 2184 && objectX == 2502 && objectY == 3250){//door
			if(player.getPosition().getY() < 3251){
				player.getActionSender().sendMessage("It is locked.");
			} else {
				player.getActionSender().walkTo(0, -1, true);
				player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
			}
			return true;
		}
		if(objectId == 2183 && objectX == 2506 && objectY == 3259){//chest closed
			ObjectHandler.getInstance().addObject(new GameObject(2182, 2506, 3259, 1, 2, 10, 2183, 50), true);
			return true;
		}
		if(objectId == 2182 && objectX == 2506 && objectY == 3259){//chest open
			if(stage == 20){
				if(!player.hasItem(QuestConstants.ORB_OF_PROTECTION)){
					player.getDialogue().sendStatement("You search the chest. Inside you find the gnomes' stolen orb of",
													"protection.");
					player.getInventory().addItemOrDrop(new Item(QuestConstants.ORB_OF_PROTECTION, 1));
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean getObjectDialog(int clickId, final Player player, int objectId, int chatId, int optionId, int npcChatId, int x, int y, int stage){
		if(objectId == 2181 && x == 2508 && y == 3210 && clickId == 2){//ballista
			if(stage == 19){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("That tracker gnome was a bit vague about the x",
														"coordinate! What could it be?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOptionWTitle("Enter the x-coordinate of the stronghold",
														"0001",
														"0002",
														"0003",
														"0004");
					return true;
				}
				if(chatId == 3){
					List<Integer> answers_ = new ArrayList<Integer>(answers);
					Collections.shuffle(answers_, new Random(player.uniqueRandomInt));
					player.getDialogue().sendStatement("You enter the height and y coordinates you got from the tracker",
													"gnomes.");
					if(answers_.get(0) == optionId){
						player.getDialogue().setNextChatId(4);
					}else{
						player.getDialogue().setNextChatId(5);
					}
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendStatement("The huge spear flies through the air and screams down directly into",
													"the Khazard stronghold. A deafening crash echoes over the battlefield",
													"as the front entrance is reduced to rubble.");
					player.questStage[this.getId()] = 20;
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendStatement("The huge spear completely misses the Khazard stronghold!");
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("Evidently that wasn't the right x coordinate.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}
		if(objectId == 2185 && x == 2509 && y == 3253 && clickId == 1){//wall
			if(player.getPosition().getY() >= 3254){
				player.getActionSender().sendMessage("I can't get over the wall from this side.");
				player.getDialogue().endDialogue();
				return true;
			}
			if(stage == 20){
				if(chatId == 1){
					player.getDialogue().sendStatement("The wall has been reduced to rubble. It should be possible to climb",
													"over the remains.");
					return true;
				}
				if(chatId == 2){
					CrossObstacle.walkAcross3(player, 0, 0, 2, -1, 840, -1, 1, null, null);
					player.getDialogue().endDialogue();
					return false;
				}
			}
		}
		return false;
	}
	
	public boolean handleNpcDeath(final Player player, int npcId, int stage){
		if(npcId == QuestConstants.KHAZARD_WARLORD){
			if(stage == 21 && !player.hasItem(QuestConstants.ORBS_OF_PROTECTION)){
				player.getDialogue().sendStatement("As the warlord falls to the ground, a ghostly vapour floats upwards",
												"from his battle-worn armour. Out of sight you hear a shrill scream in",
												"the still air of the valley. You search his satchel and find the orbs of",
												"protection.");
				player.getInventory().addItemOrDrop(new Item(QuestConstants.ORBS_OF_PROTECTION, 1));
				return true;
			}
		}
		return false;
	}
	
	public boolean allowedToAttackNpc(final Player player, int npcId, int stage){
		if(npcId == QuestConstants.KHAZARD_WARLORD){
			if(stage != 21)
				return false;
			if(stage == 21 && player.hasItem(QuestConstants.ORBS_OF_PROTECTION))
				return false;
		}
		return true;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.KING_BOLREN){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Well hello stranger. My name's Bolren, I'm the king of",
													"the tree gnomes.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("I'm suprised you made it in, maybe I made the maze",
													"too easy.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("Maybe.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("I'm afraid I have more serious concerns at the",
													"moment. Very serious.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendOption("I'll leave you to it then.",
													"Can I help at all?");
					return true;
				}
				if(chatId == 7){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Can I help at all?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(8);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("I'm glad you asked.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("The truth is my people are in grave danger. We have",
													"always been protected by the Spirit Tree. No creature",
													"of dark can harm us while its three orbs are in place.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("We are not a violent race, but we fight when we must.",
													"Many gnomes have fallen battling the dark forces of",
													"Khazard to the North.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("We became desperate, so we took one orb of protection",
													"to the battlefield. It was a foolish move.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("Khazard troops seized the orb. Now we are completely",
													"defenceless.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendPlayerChat("How can I help?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("You would be a huge benefit on the battlefield. If you",
													"would go there and try to retrieve the orb, my people",
													"and I will be forever grateful.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendOption("I would be glad to help.",
													"I'm sorry but I won't be involved.");
					return true;
				}
				if(chatId == 16){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I would be glad to help.", Dialogues.CONTENT);
						questStarted(player);
						player.getDialogue().setNextChatId(17);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}//end of stage 0
			if(stage == 2){
				if(chatId == 17){
					player.getDialogue().sendNpcChat("Thank you. The battlefield is to the north of the maze.",
													"Commander Montai will inform you of their current",
													"situation.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendNpcChat("That is if he's still alive.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(1);
					return true;
				}
				if(chatId == 1){
					player.getDialogue().sendNpcChat("My assistant shall guide you out. Good luck friend, try",
													"your best to return the orb.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					Dialogues.sendDialogue(player, QuestConstants.ELKOY_OUTSIDE_MAZE, 100, 0);
					player.teleportB(new Position(2504, 3191, 0));
					return true;
				}
			}//end of stage 2
			if(stage == 20){
				if(player.getInventory().playerHasItem(QuestConstants.ORB_OF_PROTECTION)){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I have the orb.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Oh my... The misery, the horror!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("King Bolren, are you OK?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Thank you traveller, but it's too late. We're all doomed.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("What happened?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("They came in the night. I don't know how many, but",
													"enough.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("Who?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Khazard troops. They slaughtered anyone who got in",
													"their way. Women, children, my wife.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendPlayerChat("I'm sorry.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("They took the other orbs, now we are defenceless.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendPlayerChat("Where did they take them?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("They headed north of the stronghold. A warlord carries",
													"the orbs.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendOption("I will find the warlord and bring back the orbs.",
													"I'm sorry but I can't help.");
					return true;
				}
				if(chatId == 14){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I will find the warlord and bring back the orbs.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(15);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 15){
					player.getDialogue().sendNpcChat("You are brave, but this task will be tough even for you.",
													"I wish you the best of luck. Once again you are our",
													"only hope.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getInventory().removeItem(new Item(QuestConstants.ORB_OF_PROTECTION, 1));
					player.getDialogue().sendNpcChat("I will safeguard this orb and pray for your safe return.",
													"My assistant will guide you out.",Dialogues.CONTENT);
					player.questStage[this.getId()] = 21;
					return true;
				}
				}
			}//end of stage 20
			if(stage == 21){
				if(chatId == 17){
					Dialogues.sendDialogue(player, QuestConstants.ELKOY_OUTSIDE_MAZE, 100, 0);
					player.teleportB(new Position(2504, 3191, 0));
					return true;
				}
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Bolren, I have returned.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("You made it back! Do you have the orbs?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					if(player.getInventory().playerHasItem(QuestConstants.ORBS_OF_PROTECTION)){
						player.getDialogue().sendPlayerChat("I have them here.", Dialogues.CONTENT);
					}else{
						player.getDialogue().sendPlayerChat("Unfortunately I don't have them with me.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					}
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Hooray, you're amazing. I didn't think it was possible",
													"but you've saved us.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Once the orbs are replaced we will be safe once more.",
													"We must begin the ceremony immediately.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("What does the ceremony involve?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("The spirit tree has looked over us for centuries. Now",
													"we must pay our respects.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					if(player.getInventory().playerHasItem(QuestConstants.ORBS_OF_PROTECTION)){
						player.getInventory().removeItem(new Item(QuestConstants.ORBS_OF_PROTECTION, 1));
						player.getDialogue().sendStatement("The gnomes begin to chant. Meanwhile, King Bolren holds the orbs",
														"of protection out in front of him.");
						player.questStage[this.getId()] = 22;
						return true;
					}
				}
			}//end of stage 21
			if(stage == 22){
				if(chatId == 9){
					player.getDialogue().sendStatement("The orbs of protection come to rest gently in the branches of the",
													"ancient spirit tree.");
					player.getDialogue().setNextChatId(1);
					return true;
				}
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Now at last my people are safe once more. We can live",
													"in peace again.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("I'm pleased I could help.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("You are modest brave traveller.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Please, for your efforts take this amulet. It's made",
													"from the same sacred stone as the orbs of protection. It",
													"will help keep you safe on your journeys.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("Thank you King Bolren.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("The tree has many other powers, some of which I",
													"cannot reveal. As a friend of the gnome people, I can",
													"now allow you to use the tree's magic to teleport to",
													"other trees grown from related seeds.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().endDialogue();
					questCompleted(player);
					return true;
				}
			}
		}//end of king bolren
		if(npcId == QuestConstants.ELKOY_OUTSIDE_MAZE){
			if(stage == 1){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello Elkoy.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Would you like me to show you the way to the Village?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendOption("Yes please.",
													"No thanks Elkoy.");
					return true;
				}
				if(chatId == 4){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yes please.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Ok then, follow me.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.teleport(new Position(2515, 3159, 0));
					player.getDialogue().endDialogue();
					return false;
				}
			}
			if(stage == 2){
				if(chatId == 100){
					player.getDialogue().sendNpcChat("We're out the maze now. Please hurry, we must have",
													"the orb if we are to survive.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 2
			if(stage == 20){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello Elkoy.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("You're back! And the orb?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					if(player.getInventory().playerHasItem(QuestConstants.ORB_OF_PROTECTION)){
						player.getDialogue().sendPlayerChat("I have it here.", Dialogues.CONTENT);
					}else{
						player.getDialogue().sendPlayerChat("I don't have it with me.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					}
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("You're our saviour. Please return it to the village and",
													"we are all saved. Would you like me to show you the",
													"way to the village?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendOption("Yes please.",
													"No thanks Elkoy.");
					return true;
				}
				if(chatId == 6){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yes please.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(7);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Ok then, follow me.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					Dialogues.sendDialogue(player, QuestConstants.ELKOY_INSIDE_MAZE, 100, 0);
					player.teleportB(new Position(2515, 3159, 0));
					return true;
				}
			}//end of stage 20
			if(stage == 21){
				if(chatId == 100){
					player.getDialogue().sendNpcChat("Good luck friend.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(player.getInventory().playerHasItem(QuestConstants.ORBS_OF_PROTECTION)){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello Elkoy.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("You truly are a hero.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Thanks.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("You saved us by returning the orbs of protection. I'm",
													"humbled and wish you well.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Would you like me to show you the way to the Village?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendOption("Yes please.",
													"No thanks Elkoy.");
					return true;
				}
				if(chatId == 7){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yes please.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(8);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Ok then, follow me.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					Dialogues.sendDialogue(player, QuestConstants.ELKOY_INSIDE_MAZE, 100, 0);
					player.teleportB(new Position(2515, 3159, 0));
					return true;
				}
				}
			}//end of stage 21
		}//end of elkoy outside maze
		if(npcId == QuestConstants.ELKOY_INSIDE_MAZE){
			if(stage == 1){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello Elkoy.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Would you like me to show you the way out of the Village?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendOption("Yes please.",
													"No thanks Elkoy.");
					return true;
				}
				if(chatId == 4){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yes please.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Ok then, follow me.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.teleport(new Position(2504, 3191, 0));
					player.getDialogue().endDialogue();
					return false;
				}
			}
			if(stage == 20){
				if(chatId == 100){
					player.getDialogue().sendNpcChat("Here we are. Take the orb to King Bolren, I'm sure",
													"he'll be pleased to see you.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 20
			if(stage == 21){
				if(chatId == 100){
					player.getDialogue().sendNpcChat("Here we are. Feel free to have a look around.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 21
		}//end of elkoy inside maze
		if(npcId == QuestConstants.COMMANDER_MONTAI){
			if(stage == 2){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Hello traveller, are you here to help or just to watch?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("I've been sent by King Bolren to retrieve the orb of",
														"protection.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Excellent we need all the help we can get.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("I'm commander Montai. The orb is in the Khazard",
													"stronghold to the north, but until we weaken their",
													"defences we can't get close.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("What can I do?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Firstly we need to strengthen our own defences. We",
													"desperately need wood to make more battlements, once",
													"the battlements are gone it's all over. Six loads of",
													"normal logs should do it.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendOption("Ok, I'll gather some wood.",
													"Sorry, I no longer want to be involved.");
					return true;
				}
				if(chatId == 9){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Ok, I'll gather some wood.", Dialogues.CONTENT);
						player.questStage[this.getId()] = 3;
						player.getDialogue().setNextChatId(10);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}//end of stage 2
			if(stage == 3){
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Please be as quick as you can, I don't know how much",
													"longer we can hold out.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Hello again, we're still desperate for wood soldier.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					if(player.getInventory().playerHasItem(QuestConstants.LOGS, 6)){
						player.getDialogue().sendPlayerChat("I have some here.",
															"@whi@(You give six loads of logs to the commander.)", Dialogues.CONTENT);
						player.getInventory().removeItem(new Item(QuestConstants.LOGS, 6));
						player.questStage[this.getId()] = 4;
					}else{
						player.getDialogue().sendPlayerChat("Sorry, I don't have them yet.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					}
					return true;
				}
			}//end of stage 3
			if(stage == 4){
				if(chatId == 4){
					player.getDialogue().sendNpcChat("That's excellent, now we can make more defensive",
													"battlements. Give me a moment to organise the troops",
													"and then come speak to me. I'll inform you of our next",
													"phase of attack.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("How are you doing Montai?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("We're hanging in there soldier. For the next phase of",
													"our attack we need to breach their stronghold.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("The ballista can break through the stronghold wall, and",
													"then we can advance and seize back the orb.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(5);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("So what's the problem?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("From this distance we can't get an accurate enough",
													"shot. We need the correct coordinates of the",
													"stronghold for a direct hit. I've sent out three tracker",
													"gnomes to gather them.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("Have they returned?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("I'm afraid not, and we're running out of time. I need",
													"you to go into the heart of the battlefield, find the",
													"trackers, and bring back the coordinates.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Do you think you can do it?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendOption("No, I've had enough of your battle.",
													"I'll try my best.");
					return true;
				}
				if(chatId == 11){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("I'll try my best.", Dialogues.CONTENT);
						player.questStage[this.getId()] = 5;
						player.getDialogue().setNextChatId(12);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}//end of stage 4
			if(stage == 5){
				if(chatId == 12){
					player.getDialogue().sendNpcChat("Thank you, you're braver than most.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("I don't know how long I will be able to hold out. Once",
													"you have the coordinates come back and fire the ballista",
													"right into those monsters.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("If you can retrieve the orb and bring safety back to",
													"my people, none of the blood spilled on this field will be",
													"in vain.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 5
		}//end of commander montai
		if(npcId == QuestConstants.TRACKER_GNOME_1){
			if(stage >= 5 && stage < 20){
				int val = player.questStage[this.getId()] - 5;
				if((val & Misc.getActualValue(1)) == 0 || val == 0){
					if(chatId == 1){
						player.getDialogue().sendPlayerChat("Do you know the coordinates of the Khazard",
															"stronghold?", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 2){
						player.getDialogue().sendNpcChat("I managed to get one, although it wasn't easy.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 3){
						player.getDialogue().sendStatement("The gnome tells you the @blu@height@bla@ coordinate.");
						player.questStage[this.getId()] += Misc.getActualValue(1);
						return true;
					}
				}//talked to gnome
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("Well done.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("The other two tracker gnomes should have the other",
													"coordinates if they're still alive.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("OK, take care.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 5+
		}//end of tracker gnome 1
		if(npcId == QuestConstants.TRACKER_GNOME_2){
			if(stage >= 5 && stage < 20){
				int val = player.questStage[this.getId()] - 5;
				if((val & Misc.getActualValue(2)) == 0 || val == 0){
					if(chatId == 1){
						player.getDialogue().sendPlayerChat("Are you OK?", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 2){
						player.getDialogue().sendNpcChat("They caught me spying on the stronghold. They beat",
														"and tortured me.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 3){
						player.getDialogue().sendNpcChat("But I didn't crack. I told them nothing. They can't",
														"break me!",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 4){
						player.getDialogue().sendPlayerChat("I'm sorry little man.", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 5){
						player.getDialogue().sendNpcChat("Don't be. I have the position of the stronghold!",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 6){
						player.getDialogue().sendStatement("The gnome tells you the @blu@y coordinate@bla@.");
						player.questStage[this.getId()] += Misc.getActualValue(2);
						return true;
					}
				}//talked to gnome
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("Well done.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Now leave before they find you and all is lost.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendPlayerChat("Hang in there.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Go!",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 5+
		}//end of tracker gnome 2
		if(npcId == QuestConstants.TRACKER_GNOME_3){
			if(stage >= 5 && stage < 20){
				int val = player.questStage[this.getId()] - 5;
					if(chatId == 1){
						player.getDialogue().sendPlayerChat("Are you OK?", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 2){
						player.getDialogue().sendNpcChat("OK? Who's OK? Not me! Hee hee!",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 3){
						player.getDialogue().sendPlayerChat("What's wrong?", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 4){
						player.getDialogue().sendNpcChat("You can't see me, no one can. Monsters, demons,",
														"they're all around me!",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 5){
						player.getDialogue().sendPlayerChat("What do you mean?", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 6){
						player.getDialogue().sendNpcChat("They're dancing, all of them, hee hee.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 7){
						player.getDialogue().sendStatement("He's clearly lost the plot.");
						return true;
					}
					if(chatId == 8){
						player.getDialogue().sendPlayerChat("Do you have the coordinate for the Khazard",
															"stronghold?", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 9){
						player.getDialogue().sendNpcChat("Who holds the stronghold?",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 10){
						player.getDialogue().sendPlayerChat("What?", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 11){
						List<Integer> answers_ = new ArrayList<Integer>(answers);
						Collections.shuffle(answers_, new Random(player.uniqueRandomInt));
						String s = "If you see this, theres a bug!";
						if(answers_.get(0) == 1)
							s = "Less than my hands.";
						else if(answers_.get(0) == 2)
							s = "More than my head, less than my fingers.";
						else if(answers_.get(0) == 3)
							s = "More than we, less than our feet.";
						else if(answers_.get(0) == 4)
							s = "My legs and your legs, ha ha ha!";
						player.getDialogue().sendNpcChat(s, Dialogues.CONTENT);
						if((val & Misc.getActualValue(3)) == 0 || val == 0){
							player.questStage[this.getId()] += Misc.getActualValue(3);
						}
						return true;
					}
				if(chatId == 12){
					player.getDialogue().sendPlayerChat("You're mad.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("Dance with me, and Khazard's men are beat.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendStatement("The toll of war has affected his mind.");
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendPlayerChat("I'll pray for you little man.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("All day we pray in the hay, hee hee.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 5+
		}//end of tracker gnome 3
		if(npcId == QuestConstants.KHAZARD_WARLORD){
			if(stage == 21){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("You there, stop!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Go back to your pesky little green friends.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("I've come for the orbs.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("You're out of your depth traveller. These orbs are part",
													"of a much larger picture.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("They're stolen goods, now give them here!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Ha, you really think you stand a chance? I'll crush",
													"you.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					Npc npc = Npc.getNpcById(QuestConstants.KHAZARD_WARLORD);
					CombatManager.attack(npc, player);
					player.getDialogue().endDialogue();
					return false;
				}
			}//end of stage 21
		}//end of khazard warlord
		return false;
	}

}
