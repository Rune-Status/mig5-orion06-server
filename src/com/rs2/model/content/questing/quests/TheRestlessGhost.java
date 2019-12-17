package com.rs2.model.content.questing.quests;

import java.util.ArrayList;
import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;
import com.rs2.model.content.combat.projectile.Projectile;
import com.rs2.model.content.combat.projectile.ProjectileDef;
import com.rs2.model.content.combat.projectile.ProjectileTrajectory;
import com.rs2.model.content.cutscene.Cutscene;
import com.rs2.model.content.cutscene.cutscenes.RestlessGhostScene;

public class TheRestlessGhost extends Quest {
	
	final int rewardQP = 1;
	
	public TheRestlessGhost(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public boolean getQuestPickups(final Player player, int itemId, int stage){
		if(itemId == QuestConstants.SKULL){
			if(player.questStage[this.getId()] < 4 || (player.questStage[this.getId()] >= 4 && player.hasItem(QuestConstants.SKULL)))
				return true;
			else{
				if(player.questStage[this.getId()] == 4){
					NpcLoader.spawnNpc(player, new Npc(QuestConstants.SKELETON), true, false);
					player.questStage[this.getId()] = 5;
				}
				return false;
			}
		}
		return false;
	}
	
	public boolean useQuestItemOnObject(final Player player, int itemId, int objectId, int stage){
		if(player.questStage[this.getId()] >= 5){
			if(itemId == QuestConstants.SKULL && objectId == 2146 && Npc.getNpcById(QuestConstants.RESTLESS_GHOST) != null){
				player.getActionSender().sendMessage("You put the skull in the coffin.");
				ObjectHandler.getInstance().removeObject(3249, 3192, 0, 0);
				ObjectHandler.getInstance().addObject(new GameObject(2145, 3249, 3192, 0, 0, 10, 2145, 999999999), true);
				ArrayList<Npc> actors = new ArrayList<Npc>();
				actors.add(Npc.getNpcById(QuestConstants.RESTLESS_GHOST));
				Cutscene cs = new RestlessGhostScene(player, actors);
				cs.start();
				//RestlessGhostScene.play(player);
				/*player.getActionSender().sendMapRegion();
	            Npc npc = new Npc(QuestConstants.RESTLESS_GHOST);
				npc.setPosition(new Position(3248, 3193, 0));
				npc.setSpawnPosition(new Position(3248, 3193, 0));
				npc.setCurrentX(3248);
				npc.setCurrentY(3193);
				World.register(npc);
				
				Npc npcR = Npc.getNpcById(QuestConstants.RESTLESS_GHOST);
				npcR.setVisible(false);
	            World.unregister(npcR);
				player.teleport(new Position(3248,3192,0));
				player.getActionSender().spinCamera(3340, 3149, 330, 0, 100);
				player.getActionSender().stillCamera(3325, 3149, 298, 0, 100);
				player.getUpdateFlags().faceEntity(npc.getFaceIndex());
				player.questStage[this.getId()] = 6;
				Dialogues.startDialogue(player, QuestConstants.RESTLESS_GHOST);*/
				return true;
			}
		}
		return false;
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to Father Aereck in the",
							"church next to Lumbridge Castle", "I must be unafraid of a Level 13 Skeleton"};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should find Father Urhney for help to get rid",
							"of the ghost."};
			return text;	
		}
		if(stage == 3){
			String text[] = {"I should talk to the Ghost to find out why it is hunting the",
							"graveyard crypt."};
			return text;
		}
		if(stage == 4){
			String text[] = {"I should go and search the Wizard's Tower South West of",
							"Lumbridge for the Ghost's Skull."};
			return text;
		}
		if(stage >= 5){
			String text[] = {"I should bring the skull back to the graveyard crypt."};
			return text;
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "1 Quest Point", "1125 Prayer XP"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("1125 Prayer XP", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.PRAYER, 1125);
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.SKULL);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 2145 && objectX == 3249 && objectY == 3192){
			if(stage >= 2){
				ObjectHandler.getInstance().removeObject(3249, 3192, 0, 0);
				ObjectHandler.getInstance().addObject(new GameObject(2146, 3249, 3192, 0, 0, 10, 2145, 999999999), true);
				player.getActionSender().sendMessage("You open the coffin.");
				if(Npc.getNpcById(QuestConstants.RESTLESS_GHOST) == null)
					castProjectile(player, new Position(3250,3192,0), new Position(3250,3195,0), 605);
				return true;
			} else {
				return true;
			}
		}
		if(objectId == 2146 && objectX == 3249 && objectY == 3192){
			if(stage >= 2){
				ObjectHandler.getInstance().removeObject(3249, 3192, 0, 0);
				ObjectHandler.getInstance().addObject(new GameObject(2145, 3249, 3192, 0, 0, 10, 2145, 999999999), true);
				player.getActionSender().sendMessage("You close the coffin.");
				return true;
			} else {
				return true;
			}
		}
		return false;
	}
	
	public void castProjectile(final Player player, final Position start, final Position end, int gfx) {
		final int hitDelay = calculateHitDelay(start, end);
		new Projectile(start, 0, end, 0, new ProjectileDef(gfx, ProjectileTrajectory.SPELL)).show();
		final Tick tick = new Tick(hitDelay) {
		@Override
			public void execute() {
				NpcLoader.spawnNpc(player, new Npc(QuestConstants.RESTLESS_GHOST), 3250, 3195, 0, 1500, false, false);
				this.stop();
			}
		};
		World.getTickManager().submit(tick);
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.FATHER_AERECK){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Welcome to the church of holy Saradomin.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Who's Saradomin?",
													"Nice place you've got here.",
													"I'm looking for a quest!");
					return true;
				}
				if(chatId == 3){
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("I'm looking for a quest.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("That's lucky, I need someone to do a quest for me.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendOption("Ok, let me help then.",
													"Sorry, I don't have time right now.");
					return true;
				}
				if(chatId == 6){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Ok, let me help then.", Dialogues.CONTENT);
						questStarted(player);
						player.getDialogue().setNextChatId(7);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}//end of stage 0
			if(stage == 2){
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Thank you. The problem is, there is a ghost in the",
													"church graveyard. I would like you to get rid of it.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("If you need any help, my friend Father Urhney is an",
													"expert on ghosts.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("I believe he is currently living as a hermit in Lumbridge",
													"swamp. He has a little shack in the south-east of the",
													"swamps.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Exit the graveyard through the south gate to reach the",
													"swamp. I'm sure if you told him that I sent you he'd",
													"be willing to help.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("My name is Father Aereck by the way. Pleased to",
													"meet you.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendPlayerChat("Likewise.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("Take care travelling through the swamps, I have heard",
													"they can be quite dangerous.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendPlayerChat("I will, thanks.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Have you got rid of the ghost yet?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("I can't find Father Urhney at the moment.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Well, you can get to the swamp he lives in by going",
													"south through the cemetery.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("You'll have to go right into the eastern depths of the",
													"swamp, near the coastline. That is where his house is.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 456
		if(npcId == QuestConstants.FATHER_URHNEY){
			if(stage == 2){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Go away! I'm meditating!",Dialogues.ANGRY_1);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Well, that's friendly.",
													"Father Aereck sent me to talk to you.",
													"I've come to repossess your house.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Father Aereck sent me to talk to you.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("I suppose I'd better talk to you then. What problems",
													"has he got himself into this time?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendOption("He's got a ghost haunting his graveyard.",
													"You mean he gets himself into lots of problems?");
					return true;
				}
				if(chatId == 6){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("He's got a ghost haunting his graveyard.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(7);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Oh, the silly fool.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("I leave the town for just five months, and ALREADY he",
													"can't manage.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("(sigh)",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Well, I can't go back and exorcise it. I vowed not to",
													"leave this place. Until I had done a full two years of",
													"prayer and meditation.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("Tell you what I can do though; take this amulet.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendItem1Line("Father Urhney hands you an amulet.", new Item(QuestConstants.GHOSTSPEAK_AMULET, 1));
					player.getInventory().addItemOrDrop(new Item(QuestConstants.GHOSTSPEAK_AMULET, 1));
					player.questStage[this.getId()] = 3;
					return true;
				}
			}
			if(stage >= 3){
				if(chatId == 13){
					player.getDialogue().sendNpcChat("It is an Amulet of Ghostspeak.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("So called, because when you wear it you can speak to",
													"ghosts. A lot of ghosts are doomed to be ghosts because",
													"they have left some important task uncompleted.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendNpcChat("Maybe if you know what this task is, you can get rid of",
													"the ghost. I'm not making any guarantees mind you,",
													"but it is the best I can do right now.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendPlayerChat("Thank you. I'll give it a try!", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Go away! I'm meditating!",Dialogues.ANGRY_1);
					return true;
				}
				if(chatId == 2){
					if(!player.hasItem(QuestConstants.GHOSTSPEAK_AMULET)){
						player.getDialogue().sendOption("Well, that's friendly.",
														"I've come to repossess your house.",
														"I've lost the Amulet of Ghostspeak.");
					}else{
						player.getDialogue().sendOption("Well, that's friendly.",
														"I've come to repossess your house.");
					}
					return true;
				}
				if(chatId == 3){
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("I've lost the Amulet of Ghostspeak.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendStatement("Father Urhney sighs.");
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("How careless can you get? Those things aren't easy to",
													"come by you know! It's a good job I've got a spare.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendItem1Line("Father Urhney hands you an amulet.", new Item(QuestConstants.GHOSTSPEAK_AMULET, 1));
					player.getInventory().addItemOrDrop(new Item(QuestConstants.GHOSTSPEAK_AMULET, 1));
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Be more careful this time.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendPlayerChat("Ok, I'll try to be.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 458
		if(npcId == QuestConstants.RESTLESS_GHOST){
			if(stage == 3){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello ghost, how are you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					if(player.getEquipment().getItemContainer().get(Constants.AMULET) == null){
						player.getDialogue().sendNpcChat("woooo wooo wooooo woooo.",Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					} else
					if(player.getEquipment().getItemContainer().get(Constants.AMULET).getId() == QuestConstants.GHOSTSPEAK_AMULET){
						player.getDialogue().sendNpcChat("Not very good actually.",Dialogues.CONTENT);
					}else{
						player.getDialogue().sendNpcChat("woooo wooo wooooo woooo.",Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					}
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("What's the problem then?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Did you just understand what I said???",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendOption("Yep, now tell me what the problem is.",
													"No, you sound like you're speaking nonsense to me.",
													"Wow, this amulet works!");
					return true;
				}
				if(chatId == 6){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yep, now tell me what the problem is.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(18);
						return true;
					}
					else if(optionId == 3){
						player.getDialogue().sendPlayerChat("Wow, this amulet works!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(7);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Oh! It's your amulet that's doing it! I did wonder. I",
													"don't suppose you can help me? I don't like being a",
													"ghost.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendOption("Yes, ok. Do you know why you're a ghost?",
													"No, you're scary!");
					return true;
				}
				if(chatId == 9){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yes, ok. Do you know WHY you're a ghost?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(10);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Nope. I just know I can't do much of anything like",
													"this!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendPlayerChat("I've been told a certain task may need to be completed",
														"so you can rest in peace.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("I should think it is probably because a warlock has come",
													"along and stolen my skull. If you look inside my coffin",
													"there, you'll find my corpse without a head on it.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendPlayerChat("Do you know where this warlock might be now?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("I think it was one of the warlocks who lives in the big",
													"tower by the sea south-west from here.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendPlayerChat("Ok. I will try and get the skull back for you, then you",
														"can rest in peace.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("Ooh, thank you. That would be such a great relief!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendNpcChat("It is so dull being a ghost...",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					player.questStage[this.getId()] = 4;
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendNpcChat("WOW! This is INCREDIBLE! I didn't expect anyone",
													"to ever understand me again!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 19){
					player.getDialogue().sendPlayerChat("Ok, Ok, I can understand you!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 20){
					player.getDialogue().sendPlayerChat("But have you any idea WHY you're doomed to be a",
														"ghost?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 21){
					player.getDialogue().sendNpcChat("Well, to be honest... I'm not sure.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(11);
					return true;
				}
			}
			if(stage == 5){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello ghost, how are you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					if(player.getEquipment().getItemContainer().get(Constants.AMULET) == null){
						player.getDialogue().sendNpcChat("woooo wooo wooooo woooo.",Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					} else 
					if(player.getEquipment().getItemContainer().get(Constants.AMULET).getId() == QuestConstants.GHOSTSPEAK_AMULET){
						player.getDialogue().sendNpcChat("How are you doing finding my skull?",Dialogues.CONTENT);
					}else{
						player.getDialogue().sendNpcChat("woooo wooo wooooo woooo.",Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					}
					return true;
				}
				if(chatId == 3){
					if(player.hasItem(553)){
						player.getDialogue().sendPlayerChat("I have found it!", Dialogues.CONTENT);
					}else{
						player.getDialogue().sendPlayerChat("I haven't found it yet.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					}
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Hurrah! Now I can stop being a ghost! You just need",
													"to put it in my coffin there, and I will be free!",Dialogues.CONTENT);
					return true;
				}
			}
			/*if(stage == 6){
				if(chatId == 1){
					player.getActionSender().sendMapState(2);
					player.getActionSender().hideSideBars(new int[]{QuestConstants.ATTACK_TAB[0], QuestConstants.STATS_TAB[0], QuestConstants.QUEST_TAB[0], QuestConstants.INVENTORY_TAB[0], QuestConstants.EQUIPMENT_TAB[0],
							QuestConstants.PRAYER_TAB[0], QuestConstants.MAGIC_TAB[0], QuestConstants.OPTION_TAB[0], QuestConstants.EMOTE_TAB[0]});
					player.getDialogue().sendTimedNpcChat("","Release! Thank you stranger..","",Dialogues.CONTENT);
					player.getFrozenTimer().setWaitDuration(5000);
					Npc npc = Npc.getNpcById(QuestConstants.RESTLESS_GHOST);
					npc.getUpdateFlags().setForceChatMessage("Release! Thank you");
					final Tick timer1 = new Tick(3) {
			            @Override
			            public void execute() {
			            	Npc npc = Npc.getNpcById(QuestConstants.RESTLESS_GHOST);
			            	npc.getUpdateFlags().setForceChatMessage("stranger..");
			            	World.getTickManager().submit(timer2);
			                stop();
			            }
			            
			            final Tick timer2 = new Tick(3) {
				            @Override
				            public void execute() {
				            	Npc npc = Npc.getNpcById(QuestConstants.RESTLESS_GHOST);
				            	npc.getUpdateFlags().sendAnimation(1500);
				            	npc.getUpdateFlags().sendGraphic(189, 25);
				            	World.getTickManager().submit(timer3);
				                stop();
				            }
				        };
				        
				        final Tick timer3 = new Tick(2) {
				            @Override
				            public void execute() {
				            	Npc npcR = Npc.getNpcById(QuestConstants.RESTLESS_GHOST);
								npcR.setVisible(false);
					            World.unregister(npcR);
					            player.getActionSender().spinCamera(2659, 3195, 965, 0, 100);
								player.getActionSender().stillCamera(3660, 2500, 2000, 0, 100);
								player.getActionSender().spinCamera(2559, 3195, 1000, 0, 15);
								player.getActionSender().stillCamera(3560, 2400, 1100, 0, 15);
								castProjectile(new Position(3244,3193,0), new Position(3253,3179,0), 605);
								World.getTickManager().submit(timer4);
				                stop();
				            }
				        };

				        final Tick timer4 = new Tick(5) {
				            @Override
				            public void execute() {
				            	Npc npcR = Npc.getNpcById(QuestConstants.RESTLESS_GHOST);
								npcR.setVisible(false);
					            World.unregister(npcR);
				            	player.getFrozenTimer().setWaitDuration(0);
				            	player.getActionSender().resetCamera();
				            	player.getDialogue().endDialogue();
				            	player.getActionSender().removeInterfaces();
				            	player.getActionSender().sendMapState(0);
				            	player.getActionSender().sendSideBarInterfaces();
				            	player.getInventory().removeItem(new Item(QuestConstants.SKULL));
				            	questCompleted(player);
				                stop();
				            }
				        };
			        };
			        World.getTickManager().submit(timer1);
					return true;
				}
			}*/
		}//end of npc 457
		return false;
	}
	
}
