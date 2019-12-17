package com.rs2.model.content.questing.quests;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;

public class MerlinsCrystal extends Quest {
	
	final int rewardQP = 6;
	
	public MerlinsCrystal(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		int value = stage-7;
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to King Arthur at",
							"Camelot Castle, just North West of Catherby",
							"I must be able to defeat a level 37 enemy"};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should ask if the other knights know a way to",
							"free Merlin from the crystal."};
			return text;	
		}
		if(stage == 3){
			String text[] = {"I should find a way to get into Morgan Le Faye's",
							"stronghold."};
			return text;	
		}
		if(stage == 4){
			String text[] = {"Sir Lancelot hinted that the fort takes all its",
							"deliveries by boat."};
			return text;	
		}
		if(stage == 5){
			String text[] = {"I should find a way to get into Arhein's ship",
							"for the next delivery."};
			return text;	
		}
		if(stage == 6){
			String text[] = {"I should search the fort."};
			return text;	
		}
		if(stage >= 7 && stage < 23){
			String s1 = "";
			String s2 = "";
			String s3 = "";
			String s4 = "";
			if((value & 1) != 0 && !player.hasItem(QuestConstants.BLACK_CANDLE) && !player.hasItem(QuestConstants.LIT_BLACK_CANDLE)){
				if((value & 2) == 0 || ((value & 2) != 0 && (value & 4) != 0)){
					s1 = "Candle maker agreed to make me a black candle";
					s2 = "if I brought him bucket full of wax.";
				}
			}
			if((value & 2) != 0 && (value & 1) == 0 && (value & 4) == 0){
				s1 = "Lady of the lake told me to go upstairs of the";
				s2 = "jewellery store in Port Sarim.";
			}
			if((value & 2) != 0 && (value & 1) != 0 && (value & 4) == 0){
				s1 = "Lady of the lake told me to go upstairs of the";
				s2 = "jewellery store in Port Sarim.";
				if(!player.hasItem(QuestConstants.BLACK_CANDLE) && !player.hasItem(QuestConstants.LIT_BLACK_CANDLE)){
					s3 = "Candle maker agreed to make me a black candle";
					s4 = "if I brought him bucket full of wax.";
				}
			}
			String text[] = {"Morgan le faye told me that I need the following",
							"things to untrap Merlin:",
							(player.hasItem(QuestConstants.EXCALIBUR) ? "@str@" : "")+"Excalibur, from the lady of the lake.",
							((value & 8) != 0 ? "@str@" : "")+"Some magic words, from one of the chaos altars.",
							(player.getInventory().playerHasItem(QuestConstants.BAT_BONES, 1) ? "@str@" : "")+"Bat bones",
							(player.getInventory().playerHasItem(QuestConstants.LIT_BLACK_CANDLE, 1) ? "@str@" : "")+"Lit black candle",
							"",
							s1,
							s2,
							"",
							s3,
							s4};
			return text;	
		}
		if(stage == 23){
			String text[] = {"I should now be able to shatter the crystal",
							"with excalibur."};
			return text;	
		}
		if(stage == 24){
			String text[] = {"I should now go talk to King Arthur to finish",
							"this quest."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "6 Quest Points", "Excalibur"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("6 Quest Points", 12150);
		player.getActionSender().sendString("Excalibur", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.EXCALIBUR);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean useQuestItemOnObject(final Player player, int itemId, int objectId, final int stage){
		if(itemId == QuestConstants.INSECT_REPELLENT && objectId == 68){
			player.getDialogue().sendStatement("You pour insect repellent on the beehive. You see bees leaving the",
											"hive.");
			player.tempMiscInt = QuestConstants.INSECT_REPELLENT;
			player.getDialogue().endDialogue();
			return true;
		}
		if(itemId == QuestConstants.BUCKET && objectId == 68){
			if(player.tempMiscInt != QuestConstants.INSECT_REPELLENT){
				player.hit(2, HitType.NORMAL);
				player.getActionSender().sendMessage("Suddenly bees fly out of the hive and sting you.");
				return true;
			}
			Dialogues.sendDialogueObject(1, player, objectId, 100, 0, player.getPosition().getX(), player.getPosition().getY());
			player.tempMiscInt = 0;
			return true;
		}
		if(itemId == QuestConstants.EXCALIBUR && objectId == 62){
			if(stage == 23){
				new GameObject(Constants.EMPTY_OBJECT, 2767, 3493, player.getPosition().getZ(), 0, 10, objectId, 100);
    			Npc npc1 = new Npc(QuestConstants.MERLIN_FROM_CRYSTAL);
    			NpcLoader.spawnNpcWOwner(player, npc1, 2767, 3493, 2, -1, false, false);
				Dialogues.sendDialogue(player, QuestConstants.MERLIN_FROM_CRYSTAL, 1, 0);
				player.getActionSender().sendMessage("You attempt to smash the crystal...");
				player.getActionSender().sendMessage("... and it shatters under the force of Excalibur!");
			}
			return true;
		}
		return false;
	}
	
	public boolean getObjectDialog(int clickId, final Player player, int objectId, int chatId, int optionId, int npcChatId, int x, int y, int stage){
		if(objectId == 68){//beehive
			if(chatId == 100){
				player.getDialogue().sendStatement("You try and get some wax from the beehive.");
				return true;
			}
			if(chatId == 101){
				if(player.getInventory().playerHasItem(QuestConstants.BUCKET, 1)){
					player.getInventory().replaceItemWithItem(new Item(QuestConstants.BUCKET, 1), new Item(QuestConstants.BUCKET_OF_WAX, 1));
					player.getDialogue().sendStatement("You get some wax from the hive.");
					return true;
				}
			}
			if(chatId == 102){
				player.getDialogue().sendStatement("The bees fly back to the hive as the repellent wears off.");
				player.getDialogue().endDialogue();
				return true;
			}
		}
		if(objectId == 63 && x == 2801 && y == 3442 && clickId == 2){//catherby crate
			if(stage == 5){
				if(chatId == 1){
					player.getDialogue().sendStatement("The crate is empty. It's just about big enough to hide inside.");
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOptionWTitle("Would you like to hide inside the crate?", "Yes.", "No.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendStatement("You climb inside the crate and wait.");
						player.getDialogue().setNextChatId(4);
						player.getUpdateFlags().sendAnimation(827);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendStatement("You wait.");
					player.teleportB(new Position(2793,9819,0));
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendStatement("And wait...");
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendStatement("You hear voices outside the crate.",
														"@dbl@Is this your crate, Arhein?");
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendStatement("Yeah, I think so. Pack it aboard soon as you can.",
														"I'm on a tight schedule for deliveries!");
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendStatement("You feel the crate being lifted.",
														"@dbl@Oof. Wow, this is pretty heavy!",
														"@dbl@I never knew candles weighed so much!");
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendStatement("Quit your whining, and stow it in the hold.");
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendStatement("You feel the crate being put down inside the ship.");
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendStatement("You wait...");
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendStatement("And wait...");
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendStatement("Casting off!");
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendStatement("You feel the ship start to move.");
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendStatement("Feels like you're now out at sea.");
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendStatement("The ship comes to a stop.");
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendStatement("Unload Mordred's deliveries onto the jetty.",
														"@dbl@Aye-aye cap'n!");
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendStatement("You feel the crate being lifted.");
					return true;
				}
				if(chatId == 19){
					player.getDialogue().sendStatement("You can hear someone mumbling outside the crate.",
														"",
														"@dbl@...stupid Arhein... making me... candles...",
														"@dbl@never weigh THIS much....hurts....union about this!...");
					return true;
				}
				if(chatId == 20){
					player.getDialogue().sendStatement("@dbl@...if....MY ship be different!...",
														"@dbl@stupid Arhein...");
					return true;
				}
				if(chatId == 21){
					player.getDialogue().sendStatement("You feel the crate being put down.");
					player.teleportB(new Position(2778,9839,0));
					return true;
				}
				if(chatId == 22){
					player.getDialogue().sendOptionWTitle("Would you like to get back out of the crate?", "Yes.", "No.");
					return true;
				}
				if(chatId == 23){
					if(optionId == 1){
						player.getDialogue().sendStatement("You climb out of the crate.");
						player.getDialogue().setNextChatId(25);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendStatement("You wait.");
						player.getDialogue().setNextChatId(24);
						return true;
					}
				}
				if(chatId == 24){
					player.getDialogue().sendStatement("And wait...");
					player.getDialogue().setNextChatId(22);
					return true;
				}
				if(chatId == 25){
					player.teleport(new Position(2778,3401,0));
					player.questStage[this.getId()] = 6;
					player.getDialogue().endDialogue();
					return false;
				}
			}
		}
		return false;
	}
	
	public boolean handleItemDrop(final Player player, int item, int stage){
		if(item == QuestConstants.BAT_BONES){
			if(stage == 22){
				if(player.getPosition().getX() == 2780 && player.getPosition().getY() == 3515){
					boolean b = false;
					if(player.getEquipment().getItemContainer().get(Constants.WEAPON) != null)
						if(player.getEquipment().getItemContainer().get(Constants.WEAPON).getId() == QuestConstants.EXCALIBUR)
							b = true;
					if(player.getInventory().playerHasItem(QuestConstants.LIT_BLACK_CANDLE, 1) && (player.getInventory().playerHasItem(QuestConstants.EXCALIBUR, 1) || b)){
						player.setStopPacket(true);
						player.getActionSender().walkTo(-2, 0, true);
						final Tick timer = new Tick(3) {
							@Override
							public void execute() {
								player.setStopPacket(false);
				        		Npc npc1 = new Npc(QuestConstants.THRANTAX_THE_MIGHTY);
				        		NpcLoader.spawnNpcWOwner(player, npc1, 2780, 3515, 0, 86, false, false);
				        		Dialogues.sendDialogue(player, QuestConstants.THRANTAX_THE_MIGHTY, 100, 0);
				        		npc1.setDontWalk(true);
								stop();
							}
						};
						World.getTickManager().submit(timer);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean handleSecondClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		int value = stage-7;
		if(objectId == 61 && objectX == 3259 && objectY == 3381){//chaos altar @varrock
			if(stage >= 7 && (value & 8) == 0){
				player.questStage[this.getId()] += 8;
				player.getDialogue().sendStatement("You find a small inscription at the bottom of the altar. It reads:",
													"'Snarthon Candtrick Termanto'.");
				player.getDialogue().endDialogue();
				return true;
			}
		}
		return false;
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		int value = stage-7;
		if(objectId == 59 && objectX == 3016 && objectY == 3246){//port sarim jewellery shop door
			if((value & 2) != 0 && stage > 7 && !player.hasItem(QuestConstants.EXCALIBUR)){
				if(((value & 4) == 0 && player.getInventory().playerHasItem(QuestConstants.BREAD, 1)) || ((value & 4) != 0)){
					Npc npc1 = new Npc(QuestConstants.BEGGAR);
					if(player.getSpawnedNpc() != null){
						if(player.getSpawnedNpc().getNpcId() != QuestConstants.BEGGAR && player.getSpawnedNpc().getNpcId() != QuestConstants.LADY_OF_THE_LAKE)
							NpcLoader.spawnNpcWOwner(player, npc1, 3016, 3247, 0, -1, false, false);
					}else
						NpcLoader.spawnNpcWOwner(player, npc1, 3016, 3247, 0, -1, false, false);
					Dialogues.sendDialogue(player, QuestConstants.BEGGAR, 100, 0);
				}
			} else {
				player.getActionSender().walkTo((player.getPosition().getX() < 3016 ? 1 : -1), 0, true);
				player.getActionSender().walkThroughDoor(59, 3016, 3246, 0);
			}
			return true;
		}
		if(objectId == 66){//catherby crate
			if(stage == 5){
				player.teleport(new Position(2802,3442,0));
				return true;
			}
		}
		if(objectId == 65){//fort crate
			if(stage == 5){
				player.teleport(new Position(2778,3401,0));
				player.questStage[this.getId()] = 6;
				return true;
			}
		}
		if((objectId == 71 && objectX == 2763 && objectY == 3402) || (objectId == 72 && objectX == 2763 && objectY == 3401)){//fort doors
			if(stage >= 6 || stage == 1){
				player.getActionSender().walkTo((player.getPosition().getX() < 2764) ? 1 : -1, 0, true);
				player.getActionSender().walkThroughDoubleDoor(71, 72, 2763, 3402, 2763, 3401, 0);
			}else{
				player.getActionSender().sendMessage("You should find another way in.");
			}
			return true;
		}
		return false;
	}
	
	public boolean controlDying(final Entity attacker, final Entity victim, int stage){
		if(victim.isNpc() && attacker.isPlayer()){
        	Player player = (Player) attacker;
        	Npc npc = (Npc) victim;
        	if(npc.getNpcId() == QuestConstants.SIR_MORDRED){
        		CombatManager.resetCombat(npc);
        		CombatManager.resetCombat(player);
        		Npc npc1 = new Npc(QuestConstants.MORGAN_LE_FAYE);
        		NpcLoader.spawnNpcWOwner(player, npc1, 2770, 3403, 2, 86, false, false);
        		Dialogues.sendDialogue(player, QuestConstants.MORGAN_LE_FAYE, 100, 0);
        		return true;
        	}
        }
		return false;
	}
	
	public boolean allowedToAttackNpc(final Player player, int npcId, int stage){
		if(npcId == QuestConstants.SIR_MORDRED){
			if(stage != 6)
				return false;
		}
		if(npcId == QuestConstants.THRANTAX_THE_MIGHTY){
			return false;
		}
		return true;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		int value = stage-7;
		if(npcId == QuestConstants.KING_ARTHUR){
			//player.questStage[this.getId()] = 0;
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Welcome to my court. I am King Arthur.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("I want to become a knight of the round table!",
													"So what are you doing in RuneScape?",
													"Thank you very much.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I want to become a knight of the round table!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(7);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("So what are you doing in RuneScape?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}
					if(optionId == 3){
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Well legend says we will return to Britain in its time of",
													"greatest need. But that's not for quite a while yet.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("So we've moved the whole outfit here for now.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("We're passing the time in RuneScape!",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Well, in that case I think you need to go on a quest to",
													"prove yourself worthy.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("My knights all appreciate a good quest.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Unfortunately, our current quest is to rescue Merlin.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Back in England, he got himself trapped in some sort of",
													"magical Crystal. We've moved him from the cave we",
													"found him in and now he's upstairs in his tower.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendPlayerChat("I will see what I can do then.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("Talk to my knights if you need any help.",Dialogues.CONTENT);
					questStarted(player);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 0
			if(stage == 24){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I have freed Merlin from his crystal!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Ah. A good job, well done. I dub thee a Knight Of The",
													"Round Table. You are now an honorary knight.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().endDialogue();
					questCompleted(player);
					return true;
				}
			}//end of stage 16
		}//end of king arthur
		if(npcId == QuestConstants.SIR_GAWAIN){
			if(stage == 2){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Good day to you sir!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Good day.",
													"Any ideas on how to get Merlin out of that crystal?",
													"Do you know how Merlin got trapped?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().endDialogue();
						return false;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Any ideas on how to get Merlin out of that crystal?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("Do you know how Merlin got trapped?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("I'm a little stumped myself. We've tried opening it with",
													"anything and everything!",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("I would guess this is the work of the evil Morgan Le",
													"Faye!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("And where could I find her?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("She lives in her stronghold to the south of here,",
													"guarded by some renegade knights led by Sir Mordred.",Dialogues.CONTENT);
					player.questStage[this.getId()] = 3;
					return true;
				}
			}//end of stage 2
			if(stage == 3){
				if(chatId == 8){
					player.getDialogue().sendOption("Any idea how to get into Morgan Le Faye's stronghold?",
													"Thank you for the information.");
					return true;
				}
				if(chatId == 9){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Any idea how to get into Morgan Le Faye's stronghold?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(10);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("No, you've got me stumped there...",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 3
		}//end of sir gawain
		if(npcId == QuestConstants.SIR_LANCELOT){
			if(stage == 3){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Greetings! I am Sir Lancelot, the greatest Knight in the",
													"land! What do you want?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("I want to get Merlin out of the crystal.",
													"You're a little full of yourself aren't you?",
													"Any ideas on how to get into Morgan Le Faye's stronghold?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I want to get Merlin out of the crystal.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().endDialogue();
						return false;
					}
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("Any ideas on how to get into Morgan Le Faye's",
															"stronghold?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Well, if the Knights of the Round Table can't manage",
													"it, I can't see how a commoner like you could succeed",
													"where we have failed.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("That stronghold is built in a strong defensive position.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("It's on a big rock sticking out into the sea.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("There are two ways in that I know of, the large heavy",
													"front doors, and the sea entrance, only penetrable by",
													"boat.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("They take all their deliveries by boat.",Dialogues.CONTENT);
					player.questStage[this.getId()] = 4;
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 3
		}//end of sir lancelot
		if(npcId == QuestConstants.ARHEIN){
			if(stage == 4){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hello! Would you like to trade?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Yes.",
													"No thank you.",
													"Is that your ship?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().endDialogue();
						return false;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("No thanks.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("Is that your ship?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Yes, I use it to make deliveries to my customers up",
													"and down the coast. These crates here are all ready for",
													"my next trip.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendOption("Do you deliver to the fort just down the coast?",
													"Where do you deliver to?",
													"Are you rich then?");
					return true;
				}
				if(chatId == 6){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Do you deliver to the fort just down the coast?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(7);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Yes, I do have orders to deliver there from time to",
													"time. I think I may have some bits and pieces for them",
													"when I leave here next actually.",Dialogues.CONTENT);
					player.questStage[this.getId()] = 5;
					return true;
				}
			}//end of stage 4
			if(stage == 5){
				if(chatId == 8){
					player.getDialogue().sendOption("Can you drop me off on the way down please?",
													"Aren't you worried about supplying evil knights?");
					return true;
				}
				if(chatId == 9){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Can you drop me off on the way down please?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(10);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("I don't think Sir Mordred would like that. He wants as",
													"few outsiders visiting as possible. I wouldn't want to lose",
													"his business.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 5
		}//end of arhein
		if(npcId == QuestConstants.SIR_MORDRED){
			if(stage == 6){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("You DARE to invade MY stronghold?!?!",
													"Have at thee knave!!!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					Npc npc = Npc.getNpcById(QuestConstants.SIR_MORDRED);
					if(npc != null){
						if(!npc.isDead() && !npc.isAttacking()){
							CombatManager.attack(npc, player);
						}
					}
					return false;
				}
			}
		}//end of sir mordred
		if(npcId == QuestConstants.MORGAN_LE_FAYE && player.getSpawnedNpc().getNpcId() == QuestConstants.MORGAN_LE_FAYE){
			if(stage == 6){
				if(chatId == 1)
					chatId = 100;
				if(chatId == 100){
					player.getDialogue().sendNpcChat("STOP! Please... spare my son.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(2);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Tell me how to untrap Merlin and I might.",
													"No. He deserves to die.",
													"Ok then.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Tell me how to untrap Merlin and I might.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().sendStatement("This option is currently missing...");
						player.getDialogue().setNextChatId(2);
						return true;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("You have guessed correctly that I'm responsible for",
													"that.",Dialogues.CONTENT);
					Npc npc = new Npc(QuestConstants.SIR_MORDRED);
					if(npc != null)
						npc.setCurrentHp(npc.getMaxHp());
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("I suppose I can live with that fool Merlin being loose",
													"for the sake of my son.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Setting him free won't be easy though.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("You will need to find a magic symbol as close to the",
													"crystal as you can find.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("You will then need to drop some bats' bones on the",
													"magic symbol while holding a lit black candle.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("This will summon a mighty spirit named Thrantax.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("You will need to bind him with magic words.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.tempQuestInt = 0;
					player.questStage[this.getId()] = 7;
					player.getDialogue().sendNpcChat("Then you will need the sword Excalibur with which the",
													"spell was bound in order to shatter the crystal.",Dialogues.CONTENT);
					return true;
				}
			}//end of stage 6
			if(stage == 7){
				if(chatId == 1)
					chatId = 12;
				if(chatId == 12){
					if(player.tempQuestInt == 0)
						player.getDialogue().sendOption("So where can I find Excalibur?",
														"OK I will do all that.",
														"What are the magic words?");
					if(player.tempQuestInt == 1)
						player.getDialogue().sendOption("OK, I will go do all that.",
														"What are the magic words?");
					if(player.tempQuestInt == 2)
						player.getDialogue().sendOption("So where can I find Excalibur?",
														"OK I will go do all that.");
					return true;
				}
				if(chatId == 13){
					if(optionId == 1){
						if(player.tempQuestInt == 0 || player.tempQuestInt == 2){
							player.getDialogue().sendPlayerChat("So where can I find Excalibur?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(14);
						}
						if(player.tempQuestInt == 1){
							player.getDialogue().sendPlayerChat("Ok, I will go do all that.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(17);
						}
						return true;
					}
					if(optionId == 2){
						if(player.tempQuestInt == 0 || player.tempQuestInt == 2){
							player.getDialogue().sendPlayerChat("Ok, I will go do all that.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(17);
						}
						if(player.tempQuestInt == 1){
							player.getDialogue().sendPlayerChat("What are the magic words?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(15);
						}
						return true;
					}
					if(optionId == 3){
						if(player.tempQuestInt == 0){
							player.getDialogue().sendPlayerChat("What are the magic words?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(15);
						}
						return true;
					}
				}
				if(chatId == 14){
					player.tempQuestInt = 1;
					player.getDialogue().sendNpcChat("The lady of the lake has it. I don't know if she'll give it",
													"to you though, she can be rather temperamental.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(12);
					return true;
				}
				if(chatId == 15){
					player.tempQuestInt = 2;
					player.getDialogue().sendNpcChat("You will find the magic words at the base of one of the",
													"chaos altars.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("Which chaos altar I cannot remember.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(12);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendStatement("Morgan Le Faye vanishes.");
					Npc npc = Npc.getNpcById(QuestConstants.MORGAN_LE_FAYE);
					if(npc != null){
						npc.setVisible(false);
						World.unregister(npc);
						player.getActionSender().createStillGfx(86, npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ(), 0);
					}
					return true;
				}
			}//end of stage 7
		}//end of morgan le faye
		if(npcId == QuestConstants.CANDLE_MAKER){
			if(stage < 7)
				return false;
			if((value & 1) == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hi! Would you be interested in some of my fine",
													"candles?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Have you got any black candles?",
													"Yes please.",
													"No thank you.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Have you got any black candles?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("BLACK candles???",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Hmmm. In the candle making trade, we have a tradition",
													"that it's very bad luck to make black candles.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("VERY bad luck.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("I will pay good money for one...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("I still dunno...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Tell you what. I'll supply you with a black candle...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("IF you can bring me a bucket FULL of wax.",Dialogues.CONTENT);
					player.questStage[this.getId()] += 1;
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if((value & 1) != 0 && !player.hasItem(QuestConstants.BLACK_CANDLE) && !player.hasItem(QuestConstants.LIT_BLACK_CANDLE)){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Have you got any wax yet?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					if(player.getInventory().playerHasItem(QuestConstants.BUCKET_OF_WAX, 1)){
						player.getDialogue().sendPlayerChat("Yes, I have some now.", Dialogues.CONTENT);
					}else{
						player.getDialogue().sendPlayerChat("Nope.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					}
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendStatement("You exchange the wax with the candle maker for a black candle.");
					return true;
				}
				if(chatId == 4){
					if(player.getInventory().playerHasItem(QuestConstants.BUCKET_OF_WAX, 1)){
						player.getInventory().replaceItemWithItem(new Item(QuestConstants.BUCKET_OF_WAX, 1), new Item(QuestConstants.BLACK_CANDLE, 1));
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}
		}//end of candle maker
		if(npcId == QuestConstants.LADY_OF_THE_LAKE){
			if(stage == 1){//after quest is complete!
				if(player.hasItem(QuestConstants.EXCALIBUR))
					return false;
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Here is Excalibur, guard it well.",Dialogues.CONTENT);
					player.getInventory().addItemOrDrop(new Item(QuestConstants.EXCALIBUR, 1));
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage < 7)
				return false;
			if((value & 2) == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Good day to you "+(player.getGender() == 0 ? "sir" : "madam")+".",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Who are you?",
													"Good day.",
													"I seek the sword Excalibur.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("I seek the sword Excalibur.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Aye, I have that artefact in my possession.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("'Tis very valuable, and not an artefact to be given",
													"away lightly.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("I would want to give it away only to one who is worthy",
													"and good.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("And how am I meant to prove that?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("I shall set a test for you.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("First I need you to travel to Port Sarim. Then go to",
													"the upstairs room of the jeweller's shop there.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendPlayerChat("Ok. That seems easy enough.", Dialogues.CONTENT);
					player.questStage[this.getId()] += 2;
					return true;
				}
			}
		}//end of lady of the lake
		if(npcId == QuestConstants.BEGGAR){
			if(player.getSpawnedNpc() == null)
				return false;
			if(stage < 7)
				return false;
			if((value & 2) != 0){
				if(chatId == 1 || chatId == 100){
					if((value & 4) == 0)
						chatId = 100;
					else
						chatId = 7;
				}
				if((chatId <= 7 || chatId == 100) && player.getSpawnedNpc().getNpcId() != QuestConstants.BEGGAR)
					return false;
				if(chatId >= 8 && chatId != 100 && player.getSpawnedNpc().getNpcId() != QuestConstants.LADY_OF_THE_LAKE)
					return false;
				if(chatId == 100){
					player.getDialogue().sendNpcChat("Please kind "+(player.getGender() == 0 ? "sir" : "lady")+"... my family and I are starving...",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(2);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Could you find it in your heart to spare me a simple",
													"loaf of bread?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendOption("Yes certainly.",
													"No I don't have any bread with me.");
					return true;
				}
				if(chatId == 4){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yes, certainly.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}else{
						player.getDialogue().sendStatement("This option is currently missing...");
						player.getDialogue().setNextChatId(3);
						return true;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendStatement("You give the bread to the beggar.");
					return true;
				}
				if(chatId == 6){
					if(player.getInventory().playerHasItem(QuestConstants.BREAD, 1)){
						player.getInventory().removeItem(new Item(QuestConstants.BREAD, 1));
						player.getDialogue().sendNpcChat("Thank you very much!",Dialogues.CONTENT);
						player.questStage[this.getId()] += 4;
						return true;
					}
				}
				if(chatId == 7){
					player.getSpawnedNpc().sendTransform(QuestConstants.LADY_OF_THE_LAKE, 100);
					player.getDialogue().sendStatement("The beggar has turned into the Lady of the Lake!");
					player.getDialogue().setNextChatId(8);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().setLastNpcTalk(QuestConstants.LADY_OF_THE_LAKE);
					player.getDialogue().sendNpcChat("Well done. You have passed my test.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().setLastNpcTalk(QuestConstants.LADY_OF_THE_LAKE);
					player.getDialogue().sendNpcChat("Here is Excalibur, guard it well.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					if(player.getSpawnedNpc() != null){
						player.getSpawnedNpc().setVisible(false);
						World.unregister(player.getSpawnedNpc());
					}
					player.getInventory().addItemOrDrop(new Item(QuestConstants.EXCALIBUR, 1));
					player.getDialogue().endDialogue();
					return false;
				}
			}
		}//end of beggar
		if(npcId == QuestConstants.THRANTAX_THE_MIGHTY && player.getSpawnedNpc().getNpcId() == QuestConstants.THRANTAX_THE_MIGHTY){
			if(stage < 22)
				return false;
			if(chatId == 100){
				player.getDialogue().sendStatement("Suddenly a mighty spirit appears!");
				player.getDialogue().setNextChatId(2);
				return true;
			}
			if(chatId == 2){
				player.getDialogue().sendPlayerChat("Now what were those magic words again?", Dialogues.CONTENT);
				return true;
			}
			if(chatId == 3){
				player.getDialogue().sendOption("Snarthtrick Candanto Termon",
												"Snarthon Candtrick Termanto",
												"Snarthanto Candon Termtrick");
				return true;
			}
			if(chatId == 4){
				if(optionId == 2){
					player.getDialogue().sendPlayerChat("Snarthon...", Dialogues.CONTENT);
					player.getDialogue().setNextChatId(5);
					return true;
				}else{
					player.getDialogue().sendStatement("This option is currently missing...");
					player.getDialogue().setNextChatId(3);
					return true;
				}
			}
			if(chatId == 5){
				player.getDialogue().sendPlayerChat("Candtrick...", Dialogues.CONTENT);
				return true;
			}
			if(chatId == 6){
				player.getDialogue().sendPlayerChat("Termanto!", Dialogues.CONTENT);
				return true;
			}
			if(chatId == 7){
				Npc npc = player.getSpawnedNpc();
				if(npc != null)
					player.getUpdateFlags().faceEntity(npc.getFaceIndex());
				player.getDialogue().sendNpcChat("GRAAAAAARGH!",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 8){
				player.getDialogue().sendNpcChat("Thou hast me in thine control. So that I mayst return",
												"from whence I came, I must grant thee a boon.",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 9){
				player.getDialogue().sendNpcChat("What dost thou wish of me?",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 10){
				player.getDialogue().sendPlayerChat("I wish to free Merlin from his giant crystal!", Dialogues.CONTENT);
				return true;
			}
			if(chatId == 11){
				player.getDialogue().sendNpcChat("GRAAAAAARGH!",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 12){
				player.getDialogue().sendNpcChat("The deed is done.",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 13){
				player.getDialogue().sendNpcChat("Thou mayst now shatter Merlins' crystal with",
												"Excalibur,",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 14){
				player.getDialogue().sendNpcChat("and I can once more rest. Begone! And leave me once",
												"more in peace.",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 15){
				Npc npc = player.getSpawnedNpc();
				if(npc != null){
					npc.setVisible(false);
					World.unregister(npc);
				}
				player.questStage[this.getId()] = 23;
				player.getDialogue().endDialogue();
				return false;
			}
		}//end of thrantax the mighty
		if(npcId == QuestConstants.MERLIN_FROM_CRYSTAL && player.getSpawnedNpc().getNpcId() == QuestConstants.MERLIN_FROM_CRYSTAL){
			if(stage == 23){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Thank you! Thank you! Thank you!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("It's not fun being trapped in a giant crystal!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Go speak to King Arthur, I'm sure he'll reward you!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendStatement("You have set Merlin free. Now talk to King Arthur.");
					return true;
				}
				if(chatId == 5){
					Npc npc = player.getSpawnedNpc();
					if(npc != null){
						npc.setVisible(false);
						World.unregister(npc);
					}
					player.questStage[this.getId()] = 24;
					player.getDialogue().endDialogue();
					return false;
				}
			}//end of stage 15
		}//end of merlin
		return false;
	}

}
