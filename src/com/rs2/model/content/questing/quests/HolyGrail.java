package com.rs2.model.content.questing.quests;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Area;
import com.rs2.util.Misc;

public class HolyGrail extends Quest {
	
	final int rewardQP = 2;
	
	public HolyGrail(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to King Arthur at",
							"Camelot Castle, just North West of Catherby",
							"To complete this quest I must be able to defeat a Level",
							"120 Black Knight Titan."};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should go ask Merlin for clues about Holy Grail."};
			return text;	
		}
		if(stage == 3){
			String text[] = {"Merlin told me that I should speak someone on the holy",
							"island, and could also go speak to Sir Galahad who lives",
							"west of McGrubor's Wood."};
			return text;	
		}
		if(stage == 4){
			String text[] = {"I need a magic whistle, which can be found inside",
							"a haunted manor, then I need to find the point of",
							"realm crossing."};
			return text;	
		}
		if(stage == 5){
			String text[] = {"I should find a way to enter the castle."};
			return text;	
		}
		if(stage == 6){
			String text[] = {"I should go look for Fisher King's son Percival",
							"who also was a knight of the round table."};
			return text;	
		}
		if(stage == 7){
			String text[] = {"King Arthur gave me a magic gold feather, which",
							"should assist me on finding Percival."};
			return text;	
		}
		if(stage == 8){
			String text[] = {"I should now go back to Fisher King's realm to",
							"meet with Percival."};
			return text;	
		}
		if(stage == 9){
			String text[] = {"I should now return the Holy Grail to King Arhur",
							"in Camelot."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "2 Quest Points", "11,000 Prayer XP", "15,300 Defence XP"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("2 Quest Points", 12150);
		player.getActionSender().sendString("11,000 Prayer XP", 12151);
		player.getActionSender().sendString("15,300 Defence XP", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.PRAYER, 11000);
		player.getSkill().addQuestExp(Skill.DEFENCE, 15300);
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.HOLY_GRAIL);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean getObjectDialog(int clickId, final Player player, int objectId, int chatId, int optionId, int npcChatId, int x, int y, int stage){
		if(player.questStage[61] != 1)
			return false;
		if(objectId == 23 && x == 2962 && y == 3506 && clickId == 2){//goblin village sack
			if(stage != 7 || !player.getInventory().playerHasItem(QuestConstants.MAGIC_WHISTLE))
				return false;
			if(chatId == 1){
				player.getDialogue().sendStatement("You hear muffled noises from the sack. You open the sack.");
				return true;
			}
			if(chatId == 2){
				Npc npc = new Npc(QuestConstants.SIR_PERCIVAL);
				NpcLoader.spawnNpcPos(player, new Position(2961,3505,0), npc, false, false);
				player.getUpdateFlags().faceEntity(npc.getFaceIndex());
				npc.getUpdateFlags().faceEntity(player.getFaceIndex());
				Dialogues.sendDialogue(player, QuestConstants.SIR_PERCIVAL, 100, 0);
				return true;
			}
		}
		return false;
	}
	
	Area brimhaven = new Area(2740, 3234, 2743, 3237, (byte)0); 
	
	public boolean handleFirstClickItem(final Player player, int interfaceId, int itemId, int stage){
		if(interfaceId == 3214){//inventory
			if(itemId == QuestConstants.MAGIC_WHISTLE && stage >= 4){
				int region = Misc.coords2Region(player.getPosition().getX(), player.getPosition().getY());
				if(brimhaven.containsBorderIncluded(player.getPosition())){
					if(stage < 8 && stage != 1)
						player.teleport(new Position(2805, 4715, 0));
					else
						player.teleport(new Position(2678, 4716, 0));
					return true;
				}
				if(region == 10569 || region == 11081){
					player.teleport(new Position(2741, 3235, 0));
					return true;
				}
				player.getDialogue().sendStatement("The whistle makes no noise. It will not work in this location.");
				player.getDialogue().endDialogue();
				return true;
			}
			if(itemId == QuestConstants.GRAIL_BELL){
				if((player.getPosition().getX() == 2761 || player.getPosition().getX() == 2762) && player.getPosition().getY() == 4694){
					Dialogues.sendDialogue(player, QuestConstants.GRAIL_MAIDEN, 100, 0);
					return true;
				}
			}
			if(itemId == QuestConstants.MAGIC_GOLD_FEATHER && stage == 7){
				int goalX = 2962;
				int goalY = 3506;
				int diffX = player.getPosition().getX() - goalX;
				int diffY = player.getPosition().getY() - goalY;
				if(Math.abs(diffX) > Math.abs(diffY)){
					player.getActionSender().sendMessage("The feather points to the "+(diffX < 0 ? "east" : "west")+".");
				}else{
					player.getActionSender().sendMessage("The feather points to the "+(diffY < 0 ? "north" : "south")+".");
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 24 && objectX == 2764 && objectY == 3503){//merlin door
			if(player.questStage[61] == 1){
				player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 1);
				player.getActionSender().walkTo(player.getPosition().getX() < 2765 ? 1 : -1, 0, true);
			}else{
				player.getActionSender().sendMessage("The door won't open.");
			}
			return true;
		}
		if(objectId == 22 && objectX == 3106 && objectY == 3361){//draynor manor door
			player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 2);
			player.getActionSender().walkTo(0, player.getPosition().getY() < 3362 ? 1 : -1, true);
			if(player.questStage[61] == 1 && player.getInventory().playerHasItem(QuestConstants.HOLY_TABLE_NAPKIN)){
				if(!player.hasItem(QuestConstants.MAGIC_WHISTLE, 2) && stage >= 4){
					GroundItem drop = new GroundItem(new Item(QuestConstants.MAGIC_WHISTLE, 1), player, new Position(3107,3359,2));
					GroundItemManager.getManager().dropItem(drop);
					if(stage < 8)
						GroundItemManager.getManager().dropItem(drop);
				}
			}
			return true;
		}
		if(player.questStage[61] != 1)
			return false;
		if(objectId == 23 && objectX == 2962 && objectY == 3506 && stage == 7){//goblin village sack
			player.getDialogue().sendStatement("You hear a muffled groan. The sack wriggles slightly.");
			player.getDialogue().endDialogue();
			return true;
		}
		return false;
	}
	
	public boolean controlDying(final Entity attacker, final Entity victim, int stage){
		if(victim.isNpc() && attacker.isPlayer()){
        	Player player = (Player) attacker;
        	Npc npc = (Npc) victim;
        	if(npc.getNpcId() == QuestConstants.BLACK_KNIGHT_TITAN){
        		if(player.getEquipment().getId(Constants.WEAPON) != QuestConstants.EXCALIBUR){
        			npc.setCurrentHp(npc.getMaxHp());
        		}else{
        			npc.setDead(true);
					CombatManager.startDeath(npc);
					player.getActionSender().walkTo(-2, 0, true);
					player.getActionSender().sendMessage("Well done! You have defeated the Black Knight Titan!");
					player.questStage[this.getId()] = 5;
        		}
        		return true;
        	}
        }
		return false;
	}
	
	public boolean allowedToAttackNpc(final Player player, int npcId, int stage){
		if(npcId == QuestConstants.BLACK_KNIGHT_TITAN){
			if(stage != 4)
				return false;
		}
		return true;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(player.questStage[61] != 1)
			return false;
		if(npcId == QuestConstants.KING_ARTHUR){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Now I am a knight of the round table, do you have",
														"any more quests for me?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Aha! I'm glad you are here! I am sending out various",
													"knights on an important quest. I was wondering if you",
													"too would like to take up this quest?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendOption("Tell me of this quest.",
													"I am weary of questing for the time being...");
					return true;
				}
				if(chatId == 4){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Tell me of this quest.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Well, we recently found out that the Holy Grail has",
													"passed into the RuneScape world.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("This is most fortuitous!.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("None of my knights ever did return with it last time.",
													"Now we have the opportunity to give it another go,",
													"maybe this time we will have more luck!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendOption("I'd enjoy trying that.",
													"I may come back and try that later.");
					return true;
				}
				if(chatId == 9){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I'd enjoy trying that.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(10);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Go speak to Merlin. He may be able to give a better",
													"clue as to where it is now you have freed him from that",
													"crystal.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("He has set up his workshop in the room next to the",
													"library.",Dialogues.CONTENT);
					questStarted(player);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 0
			if(stage >= 6 && stage < 8){
				if(player.hasItem(QuestConstants.MAGIC_GOLD_FEATHER))
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello, do you have a knight named Sir Percival?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Ah yes. I remember young Percival. He rode off on a",
													"quest a couple of months ago. We are getting a bit",
													"worried, he's not back yet...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("He was going to try and recover the golden boots of",
													"Arkaneeses.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("Any idea which way that would be?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Not exactly.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("We discovered, some magic golden feathers that are said",
													"to point the way to the boots...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("They certainly point somewhere.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Just blowing gently on them will supposedly show the",
													"way to go.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendStatement("King Arthur gives you a feather.");
					player.getInventory().addItemOrDrop(new Item(QuestConstants.MAGIC_GOLD_FEATHER, 1));
					player.questStage[this.getId()] = 7;
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 6
			if(stage == 9){
				if(!player.getInventory().playerHasItem(QuestConstants.HOLY_GRAIL))
					return false;
				if(chatId == 1){
					player.getDialogue().sendNpcChat("How goes thy quest?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("I have retrieved the Grail!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Wow! Incredible! You truly are a splendid knight!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getInventory().removeItem(new Item(QuestConstants.HOLY_GRAIL, 1));
					player.getDialogue().endDialogue();
					questCompleted(player);
					return true;
				}
			}//end of stage 9
		}//end of king arthur
		if(npcId == QuestConstants.MERLIN){
			if(stage == 2){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello. King Arthur has sent me on a quest for the",
														"Holy Grail. He thought you could offer some assistance.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Ah yes... the Holy Grail...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("That is a powerful artefact indeed. Returning it here",
													"would help Camelot a lot.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Due to its nature the Holy Grail is likely to reside in a",
													"holy place.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("Any suggestions?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("I believe there is a holy island somewhere not far",
													"away... I'm not entirely sure... I spent too long inside",
													"that crystal! Anyway, go and talk to someone over",
													"there.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("I suppose you could also try speaking to Sir Galahad?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("He returned from the quest many years after",
													"everyone else. He seems to know something about it,",
													"but he can only speak about those experiences",
													"cryptically.",Dialogues.CONTENT);
					player.questStage[this.getId()] = 3;
					return true;
				}
			}//end of stage 2
			if(stage == 3){
				if(chatId == 9){
					player.getDialogue().sendOption("Thank you for the advice.",
													"Where can I find Sir Galahad?");
					return true;
				}
				if(chatId == 10){
					if(optionId == 1){
						player.getDialogue().endDialogue();
						return false;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Where can I find Sir Galahad?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(11);
						return true;
					}
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("Galahad now lives a life of religious contemplation. He",
													"lives somewhere west of McGrubor's Wood I think.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 3
		}//end of merlin
		if(npcId == QuestConstants.HIGH_PRIEST){
			if(stage == 3){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Many greetings. Welcome to our fair island.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("Hello, I am in search of the Holy Grail.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("The object of which you speak did once pass through",
													"holy Entrana. I know not where it is now however.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Nor do I really care.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					Npc npc = new Npc(QuestConstants.CRONE);
					NpcLoader.spawnNpcPos(player, new Position(2851,3342,0), npc, false, false);
					npc.forceMovePath(new Position[] {new Position(player.getPosition().getX(), player.getPosition().getY()-1,0)});
					player.getUpdateFlags().faceEntity(npc.getFaceIndex());
					Dialogues.sendDialogue(player, QuestConstants.CRONE, 100, 0);
					return true;
				}
			}//end of stage 3
		}//end of high priest
		if(npcId == QuestConstants.CRONE){
			if(stage == 3){
				if(chatId == 100){
					player.getDialogue().sendNpcChat("Did you say the Grail? You are a Grail Knight, yes?",
													"Well you'd better hurry. A Fisher King is in pain.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 101){
					player.getDialogue().sendPlayerChat("Well I would, but I don't know where I am going!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 102){
					player.tempQuestInt = 0;
					player.getDialogue().sendNpcChat("Go to where the six heads face, blow the whistle and",
													"away you go!", Dialogues.CONTENT);
					player.questStage[this.getId()] = 4;
					return true;
				}
			}//end of stage 3
			if(stage == 4){
				if(chatId == 103){
					if(player.tempQuestInt == 0)
						player.getDialogue().sendOption("What are the six heads?",
														"What's a Fisher King?",
														"Ok, I will go searching.",
														"What do you mean by the whistle?");
					if(player.tempQuestInt == 1)
						player.getDialogue().sendOption("What's a Fisher King?",
														"Ok, I will go searching.",
														"What do you mean by the whistle?",
														"The point of realm crossing?");
					if(player.tempQuestInt == 2)
						player.getDialogue().sendOption("What are the six heads?",
														"Ok, I will go searching.",
														"What do you mean by the whistle?");
					if(player.tempQuestInt == 4)
						player.getDialogue().sendOption("What are the six heads?",
														"What's a Fisher King?",
														"Ok, I will go searching.");
					return true;
				}
				if(chatId == 104){
					if(optionId == 1){
						if(player.tempQuestInt == 0 || player.tempQuestInt == 2 || player.tempQuestInt == 4){
							player.getDialogue().sendPlayerChat("What are the six heads?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(109);
						}else{
							player.getDialogue().sendPlayerChat("What's a Fisher King?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(108);
						}
						return true;
					}
					if(optionId == 2){
						if(player.tempQuestInt == 0 || player.tempQuestInt == 4){
							player.getDialogue().sendPlayerChat("What's a Fisher King?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(108);
						}else{
							player.getDialogue().sendStatement("This option is currently missing...");
							player.getDialogue().setNextChatId(103);
						}
						return true;
					}
					if(optionId == 3){
						if(player.tempQuestInt == 0 || player.tempQuestInt == 4){
							player.getDialogue().sendStatement("This option is currently missing...");
							player.getDialogue().setNextChatId(103);
						}else{
							player.getDialogue().sendPlayerChat("What do you mean by the whistle?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(105);
						}
						return true;
					}
					if(optionId == 4){
						if(player.tempQuestInt == 0){
							player.getDialogue().sendPlayerChat("What do you mean by the whistle?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(105);
							return true;
						}
						if(player.tempQuestInt == 1){
							player.getDialogue().sendPlayerChat("The point of realm crossing?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(111);
							return true;
						}
						return false;
					}
				}
				if(chatId == 105){
					player.getDialogue().sendNpcChat("You don't know about the whistles yet? The whistles are",
													"easy.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 106){
					player.getDialogue().sendNpcChat("You will need one to get to and from the Fisher King's",
													"realm. They reside in a haunted manor house in",
													"Misthalin, though you may not perceive them unless",
													"you carry something from the realm of the Fisher",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 107){
					player.tempQuestInt = 4;
					player.getDialogue().sendNpcChat("King...",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(103);
					return true;
				}
				if(chatId == 108){
					player.tempQuestInt = 2;
					player.getDialogue().sendNpcChat("The Fisher King is the owner and slave of the Grail...",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(103);
					return true;
				}
				if(chatId == 109){
					player.getDialogue().sendNpcChat("The six stone heads have appeared just recently in the",
													"world. They all face the point of realm crossing. Find",
													"where two of the heads face,",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 110){
					player.tempQuestInt = 1;
					player.getDialogue().sendNpcChat("and you should be able to pinpoint where it is.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(103);
					return true;
				}
				if(chatId == 111){
					player.getDialogue().sendNpcChat("The realm of the Fisher King is not quite of this",
													"reality. It is of a reality very close to ours though...",
													"Where it is easiest to cross, THAT is a point of realm",
													"crossing.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 4
		}//end of crone
		if(npcId == QuestConstants.GALAHAD){
			if(stage >= 3 && !player.hasItem(QuestConstants.HOLY_TABLE_NAPKIN)){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Welcome to my home. It's rare for me to have quests!",
													"Would you like a cup of tea? I'll just put the kettle on.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendStatement("Brother Galahad hangs a kettle over the fire.");
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendOption("Are you any relation to Sir Galahad?",
													"I'm on a quest to find the Holy Grail!",
													"Do you get lonely here on your own?",
													"I seek an item from the realm of the Fisher King.");
					return true;
				}
				if(chatId == 4){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Are you any relation to Sir Galahad?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("I'm on a quest to find the Holy Grail!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					}
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("Do you get lonely out here on your own?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(13);
						return true;
					}
					if(optionId == 4){
						player.getDialogue().sendPlayerChat("I seek an item from the realm of the Fisher King.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(14);
						return true;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("I AM Sir Galahad.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Although I've retired as a Knight, and now live as a",
													"solitary monk. Also, I prefer to be known as brother",
													"rather than sir now. Half a moment, your cup of tea is",
													"ready.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendStatement("Sir Galahad gives you a cup of tea.");
					return true;
				}
				if(chatId == 8){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.CUP_OF_TEA, 1));
					player.getDialogue().endDialogue();
					return false;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Ah... the Grail... yes... that did fill me with wonder! Oh,",
													"that I could have stayed forever! The spear, the food,",
													"the people...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendOption("So how can I find it?",
													"What are you talking about?",
													"Why did you leave?",
													"Why didn't you bring the Grail with you?");
					return true;
				}
				if(chatId == 11){
					if(optionId == 4){
						player.getDialogue().sendPlayerChat("Why didn't you bring the Grail with you?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(12);
						return true;
					}
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("...I'm not sure. Because... it seemed to be... NEEDED",
													"in the Grail castle? Half a moment, your cup of tea is",
													"ready.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(7);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("Sometimes I do, yes. Still not many people to share my",
													"solidarity with, as most of the religious men around here",
													"are worshippers of Saradomin. Half a moment, your cup",
													"of tea is ready.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(7);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("Funny you should mention that, but when I left there",
													"I took this small cloth from the table as a keepsake.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendPlayerChat("I don't suppose I could borrow that? It could come in",
														"useful on my quest.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendStatement("Galahad reluctantly passes you a small cloth.");
					return true;
				}
				if(chatId == 17){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.HOLY_TABLE_NAPKIN, 1));
					player.getDialogue().endDialogue();
					return false;
				}
			}//end of stage 3
		}//end of galahad
		if(npcId == QuestConstants.FISHERMAN){
			if(player.hasItem(QuestConstants.GRAIL_BELL))
				return false;
			if(chatId == 1){
				player.getDialogue().sendNpcChat("Hi! I don't get many visitors here!",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 2){
				player.getDialogue().sendOption("How's the fishing?",
												"Any idea how to get into the castle?",
												"Yes, well, this place is a dump.");
				return true;
			}
			if(chatId == 3){
				if(optionId == 1){
					player.getDialogue().sendPlayerChat("How's the fishing?", Dialogues.CONTENT);
					player.getDialogue().setNextChatId(4);
					return true;
				}
				if(optionId == 2){
					player.getDialogue().sendPlayerChat("Any idea how to get into the castle?", Dialogues.CONTENT);
					player.getDialogue().setNextChatId(5);
					return true;
				}
				if(optionId == 3){
					player.getDialogue().endDialogue();
					return false;
				}
			}
			if(chatId == 4){
				player.getDialogue().sendNpcChat("Not amazing. Not many fish can live in this gungey",
												"stuff. I remember when this was a pleasant river",
												"teeming with every sort of fish...",Dialogues.CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
			if(chatId == 5){
				player.getDialogue().sendNpcChat("Why, that's easy!",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 6){
				player.getDialogue().sendNpcChat("Just ring one of the bells outside.",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 7){
				player.getDialogue().sendPlayerChat("...I didn't see any bells.",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 8){
				player.getDialogue().sendNpcChat("You must be blind then. There's ALWAYS bells there",
												"when I go to the castle.",Dialogues.CONTENT);
				GroundItem drop = new GroundItem(new Item(QuestConstants.GRAIL_BELL, 1), player, new Position(2762,4694,0));
		        GroundItemManager.getManager().dropItem(drop);
				player.getDialogue().endDialogue();
				return true;
			}
		}//end of fisherman
		if(npcId == QuestConstants.GRAIL_MAIDEN){
			if(chatId == 100){
				player.getDialogue().sendStatement("Ting-a-ling-a-ling!");
				return true;
			}
			if(chatId == 101){
				player.getDialogue().sendNpcChat("Welcome to the Grail castle. You should come inside, it's",
												"cold out here.",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 102){
				player.getDialogue().sendStatement("Somehow you are now inside the castle.");
				player.getActionSender().walkTo(0, -2, true);
				player.getDialogue().endDialogue();
				return true;
			}
		}//end of grail maiden
		if(npcId == QuestConstants.THE_FISHER_KING){
			if(stage == 5){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Ah! You got inside at last! You spent all that time",
													"fumbling around outside, I thought you'd never make it",
													"here.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("How did you know what I have been doing?",
													"I seek the Holy Grail",
													"You don't look too well.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("You don't look too well.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					} else {
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Nope, I don't feel so good either.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("I fear my life is running short... Alas, my son and heir",
													"is not here. I am waiting for my son to return to this",
													"castle.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("If you could find my son, that would be a great weight",
													"off my shoulders.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("Who is your son?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("He is known as Percival.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("I believe he is a knight of the round table.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendPlayerChat("I shall go and see if I can find him.", Dialogues.CONTENT);
					player.questStage[this.getId()] = 6;
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 5
		}//end of the fisher king
		if(npcId == QuestConstants.SIR_PERCIVAL){
			if(chatId == 100){
				player.tempQuestInt = 0;
				player.getDialogue().sendNpcChat("Wow, thank you! I could hardly breathe in there!",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 101){
				if(player.tempQuestInt == 0)
					player.getDialogue().sendOption("How did you end up in a sack?",
													"Come with me, I shall make you a king.",
													"Your father wishes to speak to you.");
				if(player.tempQuestInt == 1)
					player.getDialogue().sendOption("Come with me, I shall make you a king.",
													"Your father wishes to speak to you.");
				return true;
			}
			if(chatId == 102){
				if(optionId == 1){
					if(player.tempQuestInt == 0){
						player.getDialogue().sendPlayerChat("How did you end up in a sack?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(103);
					}else{
						player.getDialogue().sendStatement("This option is currently missing...");
						player.getDialogue().setNextChatId(101);
					}
					return true;
				}
				if(optionId == 2){
					if(player.tempQuestInt == 1){
						player.getDialogue().sendPlayerChat("Your father wishes to speak to you.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(108);
					}else{
						player.getDialogue().sendStatement("This option is currently missing...");
						player.getDialogue().setNextChatId(101);
					}
					return true;
				}
				if(optionId == 3){
					if(player.tempQuestInt == 0){
						player.getDialogue().sendPlayerChat("Your father wishes to speak to you.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(108);
						return true;
					}
				}
			}
			if(chatId == 103){
				player.getDialogue().sendNpcChat("It's a little embarrassing really.",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 104){
				player.getDialogue().sendNpcChat("After going on a long and challenging quest to retrieve",
												"the boots of Arkaneeses, defeating many powerful",
												"enemies on the way, I fell into a goblin trap!",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 105){
				player.getDialogue().sendNpcChat("I've been kept as a slave here for the last 3 months!",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 106){
				player.getDialogue().sendNpcChat("A day or so ago, they decided it was a fun game to put",
												"me in this sack: then they forgot about me!",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 107){
				player.tempQuestInt = 1;
				player.getDialogue().sendNpcChat("I'm now very hungry, and my bones feel very stiff.",Dialogues.CONTENT);
				player.getDialogue().setNextChatId(101);
				return true;
			}
			if(chatId == 108){
				player.getDialogue().sendNpcChat("My father? You have spoken to him recently?",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 109){
				player.getDialogue().sendPlayerChat("He is dying and wishes you to be his heir.", Dialogues.CONTENT);
				return true;
			}
			if(chatId == 110){
				player.getDialogue().sendNpcChat("I have been told that before.",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 111){
				player.getDialogue().sendNpcChat("I have not been able to find that castle again though...",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 112){
				player.getDialogue().sendPlayerChat("Well, I do have the means to get us there - a magic",
													"whistle!", Dialogues.CONTENT);
				return true;
			}
			if(chatId == 113){
				player.getDialogue().sendStatement("You give a whistle to Sir Percival. You tell sir Percival what to do",
												"with the whistle.");
				return true;
			}
			if(chatId == 114){
				player.getDialogue().sendNpcChat("Ok, I will see you there then!",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 115){
				if(player.getInventory().playerHasItem(QuestConstants.MAGIC_WHISTLE)){
					player.getInventory().removeItem(new Item(QuestConstants.MAGIC_WHISTLE, 1));
					player.questStage[this.getId()] = 8;
					player.getDialogue().endDialogue();
					return false;
				}
			}
		}
		if(npcId == QuestConstants.KING_PERCIVAL){
			if(stage == 8){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("You missed all the excitement!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("I got here and agreed to take over duties as king here,",
													"then before my eyes the most miraculous changes",
													"occured here... grass and trees were growing outside",
													"before our very eyes!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Thank you very much for showing me the way home.",Dialogues.CONTENT);
					player.questStage[this.getId()] = 9;
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 8
		}
		if(npcId == QuestConstants.BLACK_KNIGHT_TITAN){
			if(stage == 5){
				if(chatId == 1 && player.getPosition().getX() > 2790){
					player.getActionSender().walkTo(-2, 0, true);
					return false;
				}
			}//end of stage 5
		}//end of black knight titan
		return false;
	}

}
