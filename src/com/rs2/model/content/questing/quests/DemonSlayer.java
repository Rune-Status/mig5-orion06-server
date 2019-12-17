package com.rs2.model.content.questing.quests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class DemonSlayer extends Quest {
	
	final int rewardQP = 3;
	
	public DemonSlayer(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to the Gypsy in the tent",
							"in Varrock's main square.",
							"",
							"",
							"I must be able to defeat a level 27 apocalyptic demon!"};
			return text;
		}
		if(stage == 2){
			String text[] = {"Gypsy told me to go talk to Sir Prysin who can be found",
							"in the palace in Varrock."};
			return text;	
		}
		if(stage == 3){
			String text[] = {"I need 3 keys to get the Silverlight from Sir Prysin:",
							(player.hasItem(QuestConstants.KEY_CAPTAIN_ROVIN) ? "@str@" : "")+"Key from Captain Rovin",
							(player.hasItem(QuestConstants.KEY_DRAIN) ? "@str@" : "")+"Key from the drain",
							(player.hasItem(QuestConstants.KEY_TRAIBORN) ? "@str@" : "")+"Key from Wizard Traiborn"};
			return text;	
		}
		if(stage >= 4 && stage < 29){
			int amt = 4+25-stage;
			String text[] = {"I need 3 keys to get the Silverlight from Sir Prysin:",
							(player.hasItem(QuestConstants.KEY_CAPTAIN_ROVIN) ? "@str@" : "")+"Key from Captain Rovin",
							(player.hasItem(QuestConstants.KEY_DRAIN) ? "@str@" : "")+"Key from the drain",
							(player.hasItem(QuestConstants.KEY_TRAIBORN) ? "@str@" : "")+"Key from Wizard Traiborn",
							"I need to bring "+amt+" bones to Wizard Traiborn."};
			return text;	
		}
		if(stage == 29){
			String text[] = {"I need 3 keys to get the Silverlight from Sir Prysin:",
							(player.hasItem(QuestConstants.KEY_CAPTAIN_ROVIN) ? "@str@" : "")+"Key from Captain Rovin",
							(player.hasItem(QuestConstants.KEY_DRAIN) ? "@str@" : "")+"Key from the drain",
							(player.hasItem(QuestConstants.KEY_TRAIBORN) ? "@str@" : "")+"Key from Wizard Traiborn",
							(player.hasItem(QuestConstants.KEY_TRAIBORN) ? "" : "I should talk to Wizard Traiborn to get the key.")+""};
			return text;	
		}
		if(stage == 30){
			String text[] = {"I should now go slay the demon South of Varrock",
							"with the Silverlight."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "3 Quest Points", "Silverlight"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("3 Quest Points", 12150);
		player.getActionSender().sendString("Silverlight", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.SILVERLIGHT);
		player.getActionSender().sendInterface(12140);
	}
	
	private List<String> magicWord = Arrays.asList("Carlem", "Aber", "Camerinthum", "Purchai", "Gabindo");
	
	public boolean controlDying(final Entity attacker, final Entity victim, int stage){
		if(victim.isNpc() && attacker.isPlayer()){
        	Player player = (Player) attacker;
        	Npc npc = (Npc) victim;
        	if(npc.getNpcId() == QuestConstants.DELRITH){
        		if(player.getEquipment().getId(Constants.WEAPON) != QuestConstants.SILVERLIGHT){
        			npc.setCurrentHp(npc.getMaxHp());
        		}else{
        			CombatManager.resetCombat(npc);
        			CombatManager.resetCombat(player);
        			Dialogues.sendDialogue(player, QuestConstants.DELRITH, 100, 0);
        		}
        		return true;
        	}
        }
		return false;
	}
	
	public boolean allowedToAttackNpc(final Player player, int npcId, int stage){
		if(npcId == QuestConstants.DELRITH){
			if(stage != 30)
				return false;
		}
		return true;
	}
	
	public int controlCombatDamage(final Entity attacker, final Entity victim, int stage){
		if(victim.isNpc()){
        	Player player = (Player) attacker;
        	Npc npc = (Npc) victim;
        	if(npc.getNpcId() == QuestConstants.DELRITH){
        		if(player.getEquipment().getId(Constants.WEAPON) != QuestConstants.SILVERLIGHT){
        			player.getActionSender().sendMessage("You should use the silverlight againts the demon.");
        			return 0;
        		}
        	}
        }
		return -1;
	}
	
	public boolean handleNpcDeath(final Player player, int npcId, int stage){
		if(npcId == QuestConstants.DELRITH){
			if(stage == 30){
				Dialogues.sendDialogue(player, QuestConstants.DELRITH, 104, 0);
				return true;
			}
		}
		return false;
	}
	
	public boolean useQuestItemOnObject(final Player player, int itemId, int objectId, final int stage){
		if(itemId == QuestConstants.BUCKET_OF_WATER && objectId == 2843){
			if(stage >= 3 && player.tempMiscInt != QuestConstants.KEY_DRAIN && !player.hasItem(QuestConstants.KEY_DRAIN) && stage < 30){
				player.getDialogue().sendPlayerChat("OK, I think I've washed the key down into the sewer.",
													"I'd better go down and get it!",Dialogues.CONTENT);
				player.getDialogue().endDialogue();
				player.getUpdateFlags().sendAnimation(827);
				player.getInventory().replaceItemWithItem(new Item(QuestConstants.BUCKET_OF_WATER, 1), new Item(QuestConstants.BUCKET, 1));
				player.getActionSender().sendMessage("You pour the liquid down the drain.");
				player.tempMiscInt = QuestConstants.KEY_DRAIN;
				return true;
			}
		}
		return false;
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 882 && objectX == 3237 && objectY == 3458){
			if(stage >= 3 && player.tempMiscInt == QuestConstants.KEY_DRAIN && !player.hasItem(QuestConstants.KEY_DRAIN) && stage < 30){
				Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY() + 6400));
				GroundItem drop = new GroundItem(new Item(QuestConstants.KEY_DRAIN, 1), player, new Position(3225,9897,0));
				if(!GroundItemManager.getManager().itemExists(player, drop)){//this check does not work for some reason!
					GroundItemManager.getManager().dropItem(drop);
				}
				return true;
			}
		}
		return false;
	}
	
	boolean playerHasAllItems(final Player player){
		if(player.getInventory().playerHasItem(QuestConstants.KEY_CAPTAIN_ROVIN) && player.getInventory().playerHasItem(QuestConstants.KEY_DRAIN) &&
				player.getInventory().playerHasItem(QuestConstants.KEY_TRAIBORN)){
			return true;
		}
		return false;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		List<String> incantation = new ArrayList<String>(magicWord);
		Collections.shuffle(incantation, new Random(player.uniqueRandomInt));
		if(npcId == QuestConstants.GYPSY){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hello young one.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Cross my palm with silver and the future will be",
													"revealed to you.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendOptionWTitle("What would you like to say?",
														"Ok, here you go.",
														"Who are you calling young one?!",
														"No, I don't believe in that stuff.",
														"With silver?");
					return true;
				}
				if(chatId == 4){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Ok, here you go.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}else{
						player.getDialogue().sendStatement("This option is currently missing...");
						player.getDialogue().setNextChatId(3);
						return true;
					}
				}
				if(chatId == 5){
					if(player.getInventory().playerHasItem(QuestConstants.COINS, 1)){
						player.getInventory().removeItem(new Item(QuestConstants.COINS, 1));
						player.getDialogue().sendNpcChat("Come closer, and listen carefully to what the future",
														"holds for you, as I peer into the swirling mists of the",
														"crystal ball.",Dialogues.CONTENT);
						return true;
					}
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("I can see images forming. I can see you.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("You are holding a very impressive looking sword. I'm",
													"sure I recognise that sword...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("There is a big dark shadow appearing now.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Aaargh!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendPlayerChat("Are you all right?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("It's Delrith! Delrith is coming!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendPlayerChat("Who's Delrith?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("Delrith...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("Delrith is a powerful demon.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendNpcChat("Oh! I really hope he didn't see me looking at him",
													"through my crystal ball!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("He tried to destroy this city 150 years ago. He was",
													"stopped just in time by the great hero Wally.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendNpcChat("Using his magic sword Silverlight, Wally managed to",
													"trap the demon in the stone circle just south",
													"of this city.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.tempQuestInt = 0;
					player.getDialogue().sendNpcChat("Ye gods! Silverlight was the sword you were holding in",
													"my vision! You are the one destined to stop the demon",
													"this time.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 19){
					if(player.tempQuestInt == 0)
						player.getDialogue().sendOptionWTitle("What would you like to say?",
															"How am I meant to fight a demon who can destroy cities?",
															"Okay, where is he? I'll kill him for you!",
															"Wally doesn't sound like a very heroic name.");
					if(player.tempQuestInt == 1)
						player.getDialogue().sendOptionWTitle("What would you like to say?",
															"Okay, where is he? I'll kill him for you!",
															"Wally doesn't sound like a very heroic name.",
															"So how did Wally kill Delrith?");
					if(player.tempQuestInt == 3)
						player.getDialogue().sendOptionWTitle("What would you like to say?",
															"How am I meant to fight a demon who can destroy cities?",
															"Okay, where is he? I'll kill him for you!",
															"So how did Wally kill Delrith?");
					return true;
				}
				if(chatId == 20){
					if(optionId == 1){
						if(player.tempQuestInt == 0 || player.tempQuestInt == 3){
							player.getDialogue().sendPlayerChat("How am I meant to fight a demon who can destroy",
																"cities?!", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(24);
							return true;
						}else{
							player.getDialogue().sendStatement("This option is currently missing...");
							player.getDialogue().setNextChatId(19);
							return true;
						}
					}
					if(optionId == 2){
						if(player.tempQuestInt == 1){
							player.getDialogue().sendPlayerChat("Wally doesn't sound a very heroic name.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(21);
							return true;
						}else{
							player.getDialogue().sendStatement("This option is currently missing...");
							player.getDialogue().setNextChatId(19);
							return true;
						}
					}
					if(optionId == 3){
						if(player.tempQuestInt == 0){
							player.getDialogue().sendPlayerChat("Wally doesn't sound a very heroic name.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(21);
							return true;
						}else{
							player.getDialogue().sendPlayerChat("So how did Wally kill Delrith?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(26);
							return true;
						}
					}
				}
				if(chatId == 21){
					player.getDialogue().sendNpcChat("Yes I know. Maybe that is why history doesn't",
													"remember him. However he was a very great hero.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 22){
					player.getDialogue().sendNpcChat("Who knows how much pain and suffering Delrith would",
													"have brought forth without Wally to stop him!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 23){
					player.tempQuestInt = 3;
					player.getDialogue().sendNpcChat("It looks like you are going to need to perform similar",
													"heroics.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(19);
					return true;
				}
				if(chatId == 24){
					player.getDialogue().sendNpcChat("If you face Delrith while he is still weak from being",
													"summoned, and use the correct weapon, you will not",
													"find the task too arduous.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 25){
					player.tempQuestInt = 1;
					player.getDialogue().sendNpcChat("Do not fear. If you follow the path of the great hero",
													"Wally, then you are sure to defeat the demon.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(19);
					return true;
				}
				if(chatId == 26){
					player.getDialogue().sendNpcChat("Wally managed to arrive at the stone circle just as",
													"Delrith was summoned by a cult of chaos druids...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 27){
					player.getDialogue().sendNpcChat("By reciting the correct magical incantation, and",
													"thrusting Silverlight into Delrith while he was newly",
													"summoned, Wally was able to imprison Delrith in the",
													"stone block in the centre of the circle.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 28){
					player.getDialogue().sendNpcChat("Delrith will come forth from the stone circle again.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 29){
					player.tempQuestInt = 0;
					player.getDialogue().sendNpcChat("I would imagine an evil sorceror is already starting on",
													"the rituals to summon Delrith as we speak.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 30){
					if(player.tempQuestInt == 0)
						player.getDialogue().sendOptionWTitle("What would you like to say?",
															"How am I meant to fight a demon who can destroy cities?",
															"Okay, where is he? I'll kill him for you!",
															"What is the magical incantation?",
															"Where can I find Silverlight?");
					if(player.tempQuestInt == 3)
						player.getDialogue().sendOptionWTitle("What would you like to say?",
															"How am I meant to fight a demon who can destroy cities?",
															"Okay, where is he? I'll kill him for you!",
															"Wally doesn't sound like a very heroic name.",
															"Where can I find Silverlight?",
															"Okay, thanks. I'll do my best to stop the demon.");
					if(player.tempQuestInt == 4)
						player.getDialogue().sendOptionWTitle("What would you like to say?",
															"How am I meant to fight a demon who can destroy cities?",
															"Okay, where is he? I'll kill him for you!",
															"Wally doesn't sound like a very heroic name.",
															"What is the magical incantation?",
															"Okay, thanks. I'll do my best to stop the demon.");
					return true;
				}
				if(chatId == 31){
					if(optionId == 3){
						if(player.tempQuestInt == 0){
							player.getDialogue().sendPlayerChat("What is the magical incantation?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(34);
							return true;
						}
					}
					if(optionId == 4){
						if(player.tempQuestInt == 0 || player.tempQuestInt == 3){
							player.getDialogue().sendPlayerChat("Where can I find Silverlight?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(32);
							return true;
						}
					}
					if(optionId == 5){
						if(player.tempQuestInt == 3 || player.tempQuestInt == 4){
							player.getDialogue().sendPlayerChat("Okay, thanks. I'll do my best to stop the demon.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(37);
							return true;
						}
					}
				}
				if(chatId == 32){
					player.getDialogue().sendNpcChat("Silverlight has been passed down through Wally's",
													"descendants. I believe it is currently in the care of one",
													"of the King's knights called Sir Prysin.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 33){
					player.tempQuestInt = 4;
					player.getDialogue().sendNpcChat("He shouldn't be too hard to find. He lives in the royal",
													"palace in this city. Tell him Gypsy Aris sent you.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(30);
					return true;
				}
				if(chatId == 34){
					player.getDialogue().sendNpcChat("Oh yes, let me think a second...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 35){
					player.getDialogue().sendNpcChat("Alright, I think I've got it now, it goes.... "+incantation.get(0)+"...",
													incantation.get(1)+"... "+incantation.get(2)+"... "+incantation.get(3)+"... "+incantation.get(4)+". Have you got",
													"that?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 36){
					player.tempQuestInt = 3;
					player.getDialogue().sendPlayerChat("I think so, yes.", Dialogues.CONTENT);
					player.getDialogue().setNextChatId(30);
					return true;
				}
				if(chatId == 37){
					player.getDialogue().sendNpcChat("Good luck, and may Guthix be with you!",Dialogues.CONTENT);
					questStarted(player);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 0
			if(stage >= 2){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("What was the magical incantation again?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Let me think a second...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Alright, I think I've got it now, it goes.... "+incantation.get(0)+"...",
													incantation.get(1)+"... "+incantation.get(2)+"... "+incantation.get(3)+"... "+incantation.get(4)+". Have you got",
													"that?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("I think so, yes.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 2+
		}//end of gypsy
		if(npcId == QuestConstants.SIR_PRYSIN){
			if(stage == 2){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hello, who are you?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("I am a mighty adventurer. Who are you?",
													"I'm not sure, I was hoping you could tell me.",
													"Gypsy Aris said I should come and talk to you.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I am a mighty adventurer. Who are you?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("Gypsy Aris said I should come and talk to you.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("I am Sir Prysin. A bold and famous knight of the",
													"realm.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Gypsy Aris? Is she still alive? I remember her from",
													"when I was pretty young. Well what do you need to",
													"talk to me about?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendOption("I need to find Silverlight.",
													"Yes, she is still alive.");
					return true;
				}
				if(chatId == 7){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I need to find Silverlight.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(8);
						return true;
					} else {
						player.getDialogue().sendStatement("This option is currently missing...");
						player.getDialogue().setNextChatId(6);
					}
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("What do you need to find that for?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendPlayerChat("I need it to fight Delrith.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Delrith? I thought the world was rid of him, thanks to",
													"my great-grandfather.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendOption("Well, the gypsy's crystal ball seems to think otherwise.",
													"He's back and unfortunately I've got to deal with him.");
					return true;
				}
				if(chatId == 12){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Well, the gypsy's crystal ball seems to think otherwise.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(13);
						return true;
					} else {
						player.getDialogue().sendStatement("This option is currently missing...");
						player.getDialogue().setNextChatId(11);
					}
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("Well if the ball says so, I'd better help you.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("The problem is getting Silverlight.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendPlayerChat("You mean you don't have it?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("Oh I do have it, but it is so powerful that the king",
													"made me put it in a special box which needs three",
													"different keys to open it. That way it won't fall into the",
													"wrong hands.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendOption("So give me the keys!",
													"And why is this a problem?");
					return true;
				}
				if(chatId == 18){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("And why is this a problem?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(19);
						return true;
					} else {
						player.getDialogue().sendStatement("This option is currently missing...");
						player.getDialogue().setNextChatId(17);
					}
				}
				if(chatId == 19){
					player.getDialogue().sendNpcChat("I kept one of the keys. I gave the other two to other",
													"people for safe keeping.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 20){
					player.getDialogue().sendNpcChat("One I gave to Rovin, the captain of the palace guard.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 21){
					player.tempQuestInt = 0;
					player.questStage[this.getId()] = 3;
					player.getDialogue().sendNpcChat("I gave the other to the wizard Traiborn.",Dialogues.CONTENT);
					return true;
				}
			}//end of stage 2
			if(stage == 3){
				if(chatId == 22){
					if(player.tempQuestInt == 0)
						player.getDialogue().sendOption("Can you give me your key?",
														"Where can I find Captain Rovin?",
														"Where does the wizard live?");
					if(player.tempQuestInt == 1)
						player.getDialogue().sendOption("So what does the drain lead to?",
														"Where can I find Captain Rovin?",
														"Where does the wizard live?");
					if(player.tempQuestInt == 2)
						player.getDialogue().sendOption("Can you give me your key?",
														"Where does the wizard live?",
														"Well I'd better go key hunting.");
					if(player.tempQuestInt == 3)
						player.getDialogue().sendOption("Can you give me your key?",
														"Where can I find Captain Rovin?",
														"Well I'd better go key hunting.");
					if(player.tempQuestInt == 11)
						player.getDialogue().sendOption("Where can I find Captain Rovin?",
														"Where does the wizard live?",
														"Well I'd better go key hunting.");
					return true;
				}
				if(chatId == 23){
					if(optionId == 1){
						if(player.tempQuestInt == 0 || player.tempQuestInt == 2 || player.tempQuestInt == 3){
							player.getDialogue().sendPlayerChat("Can you give me your key?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(27);
							return true;
						}
						if(player.tempQuestInt == 1){
							player.getDialogue().sendPlayerChat("So what does the drain connect to?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(30);
							return true;
						}
						if(player.tempQuestInt == 11){
							player.getDialogue().sendPlayerChat("Where can I find Captain Rovin?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(26);
							return true;
						}
					}
					if(optionId == 2){
						if(player.tempQuestInt == 0 || player.tempQuestInt == 1 || player.tempQuestInt == 3){
							player.getDialogue().sendPlayerChat("Where can I find Captain Rovin?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(26);
							return true;
						} else {
							player.getDialogue().sendPlayerChat("Where does the wizard live?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(24);
							return true;
						}
					}
					if(optionId == 3){
						if(player.tempQuestInt == 0 || player.tempQuestInt == 1){
							player.getDialogue().sendPlayerChat("Where does the wizard live?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(24);
							return true;
						} else {
							player.getDialogue().sendPlayerChat("Well I'd better go key hunting.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(31);
							return true;
						}
					}
				}
				if(chatId == 24){
					player.getDialogue().sendNpcChat("Wizard Traiborn?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 25){
					player.tempQuestInt = 3;
					player.getDialogue().sendNpcChat("He is one of the wizards who lives in the tower on the",
													"little island just off the south coast. I believe his",
													"quarters are on the first floor of the tower.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(22);
					return true;
				}
				if(chatId == 26){
					player.tempQuestInt = 2;
					player.getDialogue().sendNpcChat("Captain Rovin lives at the top of the guards' quarters in",
													"the north-west wing of this palace.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(22);
					return true;
				}
				if(chatId == 27){
					player.getDialogue().sendNpcChat("Um.... ah....",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 28){
					player.getDialogue().sendNpcChat("Well there's a problem there as well.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 29){
					player.tempQuestInt = 1;
					player.getDialogue().sendNpcChat("I managed to drop the key in the drain just outside the",
													"palace kitchen. It is just inside and I can't reach it.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(22);
					return true;
				}
				if(chatId == 30){
					player.tempQuestInt = 11;
					player.getDialogue().sendNpcChat("It is the drain for the drainpipe running from the sink",
													"in the kitchen down to the palace sewers.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(22);
					return true;
				}
				if(chatId == 31){
					player.getDialogue().sendNpcChat("Ok, goodbye.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 3
			if(stage >= 3){
				if(stage == 30 && !player.hasItem(QuestConstants.SILVERLIGHT))
					chatId = 5;
				if(chatId == 1){
					player.getDialogue().sendNpcChat("So how are you doing with getting the keys?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					if(playerHasAllItems(player)){
						player.getDialogue().sendPlayerChat("I've got all three keys!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
					}else{
						player.getDialogue().sendPlayerChat("I don't have them yet.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(3);
					}
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Well, come back when you do.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Excellent! Now I can give you Silverlight.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					if(stage < 30){
						if(!playerHasAllItems(player))
							return false;
						player.getInventory().removeItem(new Item(QuestConstants.KEY_CAPTAIN_ROVIN, 1));
						player.getInventory().removeItem(new Item(QuestConstants.KEY_DRAIN, 1));
						player.getInventory().removeItem(new Item(QuestConstants.KEY_TRAIBORN, 1));
						player.questStage[this.getId()] = 30;
					}
					player.getInventory().addItemOrDrop(new Item(QuestConstants.SILVERLIGHT, 1));
					player.getDialogue().sendItem1Line("Sir Prysin hands you a very shiny sword.", new Item(QuestConstants.SILVERLIGHT, 1));
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("That sword belonged to my great-grandfather. Make",
													"sure you treat it with respect!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Now go kill that demon!",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of sir prysin
		if(npcId == QuestConstants.CAPTAIN_ROVIN){
			if(stage >= 3 && stage < 30){
				if(player.hasItem(QuestConstants.KEY_CAPTAIN_ROVIN))
					return false;
				if(chatId == 1){
					player.getDialogue().sendNpcChat("What are you doing up here? Only the palace guards",
													"are allowed up here.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("I am one of the palace guards.",
													"What about the King?",
													"Yes I know, but this is important.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("Yes, I know, but this is important.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					} else {
						player.getDialogue().sendStatement("This option is currently missing...");
						player.getDialogue().setNextChatId(2);
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Ok, I'm listening. Tell me what's so important.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendOption("There's a demon who wants to invade this city.",
													"Erm I forgot.",
													"The castle has just received its ale delivery.");
					return true;
				}
				if(chatId == 6){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("There's a demon who wants to invade this city.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(7);
						return true;
					} else {
						player.getDialogue().sendStatement("This option is currently missing...");
						player.getDialogue().setNextChatId(5);
					}
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Is it a powerful demon?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendOption("Not really.",
													"Yes, very.");
					return true;
				}
				if(chatId == 9){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Yes, very.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(10);
						return true;
					} else {
						player.getDialogue().sendStatement("This option is currently missing...");
						player.getDialogue().setNextChatId(8);
					}
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("As good as the palace guards are, I don't know if",
													"they're up to taking on a very powerful demon.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendOption("Yeah, the palace guards are rubbish!",
													"It's not them who are going to fight the demon, it's me.");
					return true;
				}
				if(chatId == 12){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("It's not them who are going to fight the demon, it's me.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(13);
						return true;
					} else {
						player.getDialogue().sendStatement("This option is currently missing...");
						player.getDialogue().setNextChatId(11);
					}
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("What, all by yourself? How are you going to do that?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendPlayerChat("I'm going to use the powerful sword Silverlight, which I",
														"believe you have one of the keys for?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.tempQuestInt = 0;
					player.getDialogue().sendNpcChat("Yes, I do. But why should I give it to you?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					if(player.tempQuestInt == 0)
						player.getDialogue().sendOption("Gypsy Aris said I was destined to kill the demon.",
														"Otherwise the demon will destroy the city!",
														"Sir Prysin said you would give me the key.");
					if(player.tempQuestInt == 1)
						player.getDialogue().sendOption("Otherwise the demon will destroy the city!",
														"Sir Prysin said you would give me the key.");
					if(player.tempQuestInt == 2)
						player.getDialogue().sendOption("Gypsy Aris said I was destined to kill the demon.",
														"Sir Prysin said you would give me the key.");
					if(player.tempQuestInt == 13)
						player.getDialogue().sendOption("Why did he give you one of the keys then?",
														"Gypsy Aris said I was destined to kill the demon.",
														"Otherwise the demon will destroy the city!");
					return true;
				}
				if(chatId == 17){
					if(optionId == 1){
						if(player.tempQuestInt == 0 || player.tempQuestInt == 2){
							player.getDialogue().sendPlayerChat("Gypsy Aris said I was destined to kill the demon.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(18);
							return true;
						}
						if(player.tempQuestInt == 13){
							player.getDialogue().sendPlayerChat("Why did he give you one of the keys then?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(22);
							return true;
						}
						if(player.tempQuestInt == 1){
							player.getDialogue().sendPlayerChat("Otherwise the demon will destroy the city!", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(19);
							return true;
						}
					}
					if(optionId == 2){
						if(player.tempQuestInt == 0){
							player.getDialogue().sendPlayerChat("Otherwise the demon will destroy the city!", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(19);
							return true;
						}
						if(player.tempQuestInt == 1 || player.tempQuestInt == 2){
							player.getDialogue().sendPlayerChat("Sir Prysin said you would give me the key.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(20);
							return true;
						}
						if(player.tempQuestInt == 13){
							player.getDialogue().sendPlayerChat("Gypsy Aris said I was destined to kill the demon.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(18);
							return true;
						}
					}
					if(optionId == 3){
						if(player.tempQuestInt == 0){
							player.getDialogue().sendPlayerChat("Sir Prysin said you would give me the key.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(20);
							return true;
						}
						if(player.tempQuestInt == 13){
							player.getDialogue().sendPlayerChat("Otherwise the demon will destroy the city!", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(19);
							return true;
						}
					}
				}
				if(chatId == 18){
					player.tempQuestInt = 1;
					player.getDialogue().sendNpcChat("A gypsy? Destiny? I don't believe in that stuff. I got",
													"where I am today by hard work, not by destiny! Why",
													"should I care what that mad old gypsy says?",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(16);
					return true;
				}
				if(chatId == 19){
					player.tempQuestInt = 2;
					player.getDialogue().sendNpcChat("You can't fool me! How do I know you haven't just",
													"made that story up to get my key?",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(16);
					return true;
				}
				if(chatId == 20){
					player.getDialogue().sendNpcChat("Oh, he did, did he? Well I don't report to Sir Prysin, I",
													"report directly to the king!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 21){
					player.tempQuestInt = 13;
					player.getDialogue().sendNpcChat("I didn't work my way up through the ranks of the",
													"palace guards so I could take orders from an ill-bred",
													"moron who only has his job because his great-",
													"grandfather was a hero with a silly name!",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(16);
					return true;
				}
				if(chatId == 22){
					player.getDialogue().sendNpcChat("Only because the king ordered him to! The king",
													"couldn't get Sir Prysin to part with his precious",
													"ancestral sword, but he made him lock it up so he",
													"couldn't lose it.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 23){
					player.getDialogue().sendNpcChat("I got one key and I think some wizard got another.",
													"Now what happened to the third one?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 24){
					player.getDialogue().sendPlayerChat("Sir Prysin dropped it down a drain!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 25){
					player.getDialogue().sendNpcChat("Ha ha ha! The idiot!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 26){
					player.getDialogue().sendNpcChat("Okay, I'll give you the key, just so that it's you that",
													"kills the demon and not Sir Prysin!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 27){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.KEY_CAPTAIN_ROVIN, 1));
					player.getDialogue().sendItem1Line("Captain Rovin hands you a key.", new Item(QuestConstants.KEY_CAPTAIN_ROVIN, 1));
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 3
		}//end of captain rovin
		if(npcId == QuestConstants.TRAIBORN){
			if(stage == 3){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Ello young thingummywut.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("What's a thingummywut?",
													"Teach me to be a mighty and powerful wizard.",
													"I need to get a key given to you by Sir Prysin.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("I need to get a key given to you by Sir Prysin.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					} else {
						player.getDialogue().sendStatement("This option is currently missing...");
						player.getDialogue().setNextChatId(2);
						return true;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Sir Prysin? Who's that? What would I want his key",
													"for?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendOption("He told me you were looking after it for him.",
													"He's one of the King's knights.",
													"Well, have you got any keys knocking around?");
					return true;
				}
				if(chatId == 6){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("He told me you were looking after it for him.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(7);
						return true;
					}
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("Well, have you got any keys knocking around?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(10);
						return true;
					}
					player.getDialogue().sendStatement("This option is currently missing...");
					player.getDialogue().setNextChatId(5);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("That wasn't very clever of him. I'd lose my head if it",
													"wasn't screwed on. Go and tell him to find someone else",
													"to look after his valuables in future.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendOption("Ok, I'll go and tell him that.",
													"Well, have you got any keys knocking around?");
					return true;
				}
				if(chatId == 9){
					if(optionId == 1){
						player.getDialogue().sendStatement("This option is currently missing...");
						player.getDialogue().setNextChatId(8);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Well, have you got any keys knocking around?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(10);
						return true;
					}
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Now you come to mention it, yes I do have a key. It's",
													"in my special closet of valuable stuff. Now how do I get",
													"into that?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("I sealed it using one of my magic rituals. So it would",
													"make sense that another ritual would open it again.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendPlayerChat("So do you know what ritual to use?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("Let me think a second.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("Yes a simple drazier style ritual should suffice. Hmm,",
													"main problem with that is I'll need 25 sets of bones.",
													"Now where am I going to get hold of something like",
													"that?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendOption("Hmm, that's too bad. I really need that key.",
													"I'll get the bones for you.");
					return true;
				}
				if(chatId == 16){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("I'll help get the bones for you.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(17);
						return true;
					}
				}
				if(chatId == 17){
					player.getDialogue().sendNpcChat("Ooh that would be very good of you.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendPlayerChat("Ok I'll speak to you when I've got some bones.", Dialogues.CONTENT);
					player.questStage[this.getId()] = 4;
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 3
			if(stage >= 4 && stage < 30){
				if(chatId == 1 && player.hasItem(QuestConstants.KEY_TRAIBORN))
					return false;
				if(chatId == 1 && stage == 29)
					chatId = 7;
				if(chatId == 1){
					player.getDialogue().sendNpcChat("How are you doing finding bones?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					if(player.getInventory().playerHasItem(QuestConstants.BONES)){
						player.getDialogue().sendPlayerChat("I have some bones.", Dialogues.CONTENT);
						return true;
					}else{
						player.getDialogue().sendPlayerChat("I have none.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Give 'em here then.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					int amt = player.getInventory().getItemAmount(QuestConstants.BONES);
					int removeAmt = 4+25-stage;
					int realRemoveAmt = (amt < removeAmt ? amt : removeAmt);
					player.getDialogue().sendStatement("You give Traiborn "+realRemoveAmt+" sets of bones.");
					stage += realRemoveAmt;
					if(stage < 29){
						player.getDialogue().setNextChatId(10);
					}
					if(stage == 29){
						player.getDialogue().setNextChatId(5);
					}
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Hurrah! That's all 25 sets of bones.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Wings of dark and colour too,",
													"Spreading in the morning dew;",
													"Locked away I have a key;",
													"Return it now, please, unto me.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					if(stage < 29){
						int amt = player.getInventory().getItemAmount(QuestConstants.BONES);
						int removeAmt = 4+25-stage;
						int realRemoveAmt = (amt < removeAmt ? amt : removeAmt);
						player.questStage[this.getId()] += realRemoveAmt;
						player.getInventory().removeItem(new Item(QuestConstants.BONES, realRemoveAmt));
					}
					player.getInventory().addItemOrDrop(new Item(QuestConstants.KEY_TRAIBORN, 1));
					player.getDialogue().sendItem1Line("Traiborn hands you a key.", new Item(QuestConstants.KEY_TRAIBORN, 1));
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendPlayerChat("Thank you very much.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Not a problem for a friend of Sir What's-his-face.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 10){
					int amt = player.getInventory().getItemAmount(QuestConstants.BONES);
					int removeAmt = 4+25-stage;
					int realRemoveAmt = (amt < removeAmt ? amt : removeAmt);
					player.questStage[this.getId()] += realRemoveAmt;
					player.getInventory().removeItem(new Item(QuestConstants.BONES, realRemoveAmt));
					stage += realRemoveAmt;
					removeAmt = 4+25-stage;
					player.getDialogue().sendNpcChat("You still need to bring me "+removeAmt+" more.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 4-29
		}//end of traiborn
		if(npcId == QuestConstants.DELRITH){
			if(stage == 30){
				if(chatId == 100){
					player.tempQuestInt = 0;
					player.tempMiscInt = 0;
					player.getDialogue().sendPlayerChat("Now what was that incantation again?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 101){
					player.getDialogue().sendOptionWTitle("Select a word",
														"Carlem",
														"Aber",
														"Camerinthum",
														"Purchai",
														"Gabindo");
					return true;
				}
				if(chatId == 102){
					if(optionId == 1){//5
						if(incantation.get(player.tempMiscInt).equals(magicWord.get(0)))
							player.tempQuestInt++;
						String s = "Carlem"+(player.tempMiscInt == 4 ? "!" : "...");
						player.getDialogue().sendPlayerChat(s, Dialogues.CONTENT);
						player.getUpdateFlags().setForceChatMessage(s);
						if(player.tempQuestInt == 5){
							player.getDialogue().setNextChatId(103);
							return true;
						}
						player.tempMiscInt++;
						if(player.tempMiscInt == 5){
							Npc npc = Npc.getNpcById(QuestConstants.DELRITH);
				        	npc.setCurrentHp(npc.getMaxHp());
				        	player.getActionSender().sendMessage("You didn't remember the words correctly!");
							player.getDialogue().endDialogue();
						}else
							player.getDialogue().setNextChatId(101);
						return true;
					}
					if(optionId == 2){//4
						if(incantation.get(player.tempMiscInt).equals(magicWord.get(1)))
							player.tempQuestInt++;
						String s = "Aber"+(player.tempMiscInt == 4 ? "!" : "...");
						player.getDialogue().sendPlayerChat(s, Dialogues.CONTENT);
						player.getUpdateFlags().setForceChatMessage(s);
						if(player.tempQuestInt == 5){
							player.getDialogue().setNextChatId(103);
							return true;
						}
						player.tempMiscInt++;
						if(player.tempMiscInt == 5){
							Npc npc = Npc.getNpcById(QuestConstants.DELRITH);
				        	npc.setCurrentHp(npc.getMaxHp());
				        	player.getActionSender().sendMessage("You didn't remember the words correctly!");
							player.getDialogue().endDialogue();
						}else
							player.getDialogue().setNextChatId(101);
						return true;
					}
					if(optionId == 3){//3
						if(incantation.get(player.tempMiscInt).equals(magicWord.get(2)))
							player.tempQuestInt++;
						String s = "Camerinthum"+(player.tempMiscInt == 4 ? "!" : "...");
						player.getDialogue().sendPlayerChat(s, Dialogues.CONTENT);
						player.getUpdateFlags().setForceChatMessage(s);
						if(player.tempQuestInt == 5){
							player.getDialogue().setNextChatId(103);
							return true;
						}
						player.tempMiscInt++;
						if(player.tempMiscInt == 5){
							Npc npc = Npc.getNpcById(QuestConstants.DELRITH);
				        	npc.setCurrentHp(npc.getMaxHp());
				        	player.getActionSender().sendMessage("You didn't remember the words correctly!");
							player.getDialogue().endDialogue();
						}else
							player.getDialogue().setNextChatId(101);
						return true;
					}
					if(optionId == 4){//1
						if(incantation.get(player.tempMiscInt).equals(magicWord.get(3)))
							player.tempQuestInt++;
						String s = "Purchai"+(player.tempMiscInt == 4 ? "!" : "...");
						player.getDialogue().sendPlayerChat(s, Dialogues.CONTENT);
						player.getUpdateFlags().setForceChatMessage(s);
						if(player.tempQuestInt == 5){
							player.getDialogue().setNextChatId(103);
							return true;
						}
						player.tempMiscInt++;
						if(player.tempMiscInt == 5){
							Npc npc = Npc.getNpcById(QuestConstants.DELRITH);
				        	npc.setCurrentHp(npc.getMaxHp());
				        	player.getActionSender().sendMessage("You didn't remember the words correctly!");
							player.getDialogue().endDialogue();
						}else
							player.getDialogue().setNextChatId(101);
						return true;
					}
					if(optionId == 5){//2
						if(incantation.get(player.tempMiscInt).equals(magicWord.get(4)))
							player.tempQuestInt++;
						String s = "Gabindo"+(player.tempMiscInt == 4 ? "!" : "...");
						player.getDialogue().sendPlayerChat(s, Dialogues.CONTENT);
						player.getUpdateFlags().setForceChatMessage(s);
						if(player.tempQuestInt == 5){
							player.getDialogue().setNextChatId(103);
							return true;
						}
						player.tempMiscInt++;
						if(player.tempMiscInt == 5){
							Npc npc = Npc.getNpcById(QuestConstants.DELRITH);
				        	npc.setCurrentHp(npc.getMaxHp());
				        	player.getActionSender().sendMessage("You didn't remember the words correctly!");
							player.getDialogue().endDialogue();
						}else
							player.getDialogue().setNextChatId(101);
						return true;
					}
				}
				if(chatId == 103){
					player.tempQuestInt = 0;
					player.tempMiscInt = 0;
					player.getDialogue().sendTimedStatement("Delrith is sucked into the vortex...");
					Npc npc = Npc.getNpcById(QuestConstants.DELRITH);
					npc.setDead(true);
					CombatManager.startDeath(npc);
					return true;
				}
				if(chatId == 104){
					player.getDialogue().sendStatement("...back into the dark dimension from which he came.");
					return true;
				}
				if(chatId == 105){
					player.getDialogue().endDialogue();
					questCompleted(player);
					return true;
				}
			}//end of stage 30
		}//end of delrith
		return false;
	}

}
