package com.rs2.model.content.questing.quests;

import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
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
import com.rs2.util.Area;
import com.rs2.util.Misc;

public class VampireSlayer extends Quest {
	
	final int rewardQP = 3;
	
	public VampireSlayer(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to Morgan who is in",
							"Draynor Village.",
							"",
							"Requirements:",
							"Must be able to kill a level 34 Vampire"};
			return text;
		}
		if(stage == 2){
			String text[] = {"Morgan told me to speak with Dr Harlow, who can",
							"normally be found in Jolly Boar Inn in Varrock."};
			return text;	
		}
		if(stage == 3){
			String text[] = {"I should go and get some beer for Dr Harlow."};
			return text;	
		}
		if(stage == 4){
			String text[] = {"I should now ask Dr Harlow some tips to kill",
							"vampires."};
			return text;	
		}
		if(stage == 5){
			String text[] = {"Dr Harlow told me that I need the following items",
							"to kill a vampire:",
							"Garlic",
							"Stake",
							"Hammer"};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "3 Quest Points", "4825 Attack XP"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("3 Quest Points", 12150);
		player.getActionSender().sendString("4825 Attack XP", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.ATTACK, 4825);
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.STAKE);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean controlDying(final Entity attacker, final Entity victim, int stage){
		if(victim.isNpc() && attacker.isPlayer()){
        	Player player = (Player) attacker;
        	Npc npc = (Npc) victim;
        	if(npc.getNpcId() == 757){
        		if(!player.getInventory().playerHasItem(QuestConstants.HAMMER) || !player.getInventory().playerHasItem(QuestConstants.STAKE)){
        			npc.setCurrentHp(npc.getMaxHp());
        			player.getActionSender().sendMessage("The vampire regenerates.");
        		}else{
        			npc.setDead(true);
					CombatManager.startDeath(npc);
					player.getActionSender().sendMessage("You hammer the stake into the vampire's chest!");
        		}
        		return true;
        	}
        }
		return false;
	}
	
	String[] mes = {"Ow!", "Eeek!", "Oooch!", "Gah!"};
	
	public int controlCombatDamage(final Entity attacker, final Entity victim, int stage){
		if(victim.isNpc()){
        	Player player = (Player) attacker;
        	Npc npc = (Npc) victim;
        	if(npc.getNpcId() == 757){
        		if(!player.getInventory().playerHasItem(1550))
        			return 0;
        	}
        }
		return -1;
	}
	
	Area basement = new Area(3075, 9768, 3080, 9778, (byte) 0);
	
	/*public boolean checkAreas(final Player player, int stage){
		if(basement.contains(player.getPosition())){
			if(player.tempAreaEffectInt > 5)
				player.tempAreaEffectInt = 0;
			if(player.tempAreaEffectInt == 5){
				player.getActionSender().sendMessage("The candles burn your feet!");
				player.getUpdateFlags().setForceChatMessage(mes[Misc.random(mes.length-1)]);
				player.tempAreaEffectInt = 0;
				return true;
			}
			player.tempAreaEffectInt++;
			return true;
		}
		return false;
	}*/
	
	public boolean checkAreas(final Player player, int stage){
		if(player.tempAreaEffectInt == QuestConstants.COUNT_DRAYNOR)
			return true;
		if(basement.contains(player.getPosition())){
			final Tick timer1 = new Tick(5) {
	            @Override
	            public void execute() {
	            	if(!player.isLoggedIn()){
	            		stop();
	            		return;
	            	}
	            	if(basement.contains(player.getPosition())){
	            		player.getActionSender().sendMessage("The candles burn your feet!");
	    				player.getUpdateFlags().setForceChatMessage(mes[Misc.random(mes.length-1)]);
	            	}else{
	            		player.tempAreaEffectInt = -1;
	            		stop();
	            	}
	            }
			};
	        World.getTickManager().submit(timer1);
	        player.tempAreaEffectInt = QuestConstants.COUNT_DRAYNOR;
			return true;
		}
		return false;
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 2612 && objectX == 3096 && objectY == 3269){
			ObjectHandler.getInstance().removeObject(3096, 3269, 1, 0);
			ObjectHandler.getInstance().addObject(new GameObject(2613, 3096, 3269, 1, 0, 10, 2612, 999999999), true);
			return true;
		}
		if(objectId == 2613 && objectX == 3096 && objectY == 3269){
			String s = "The cupboard contains garlic. You take a clove.";
			if(player.tempQuestInt == QuestConstants.GARLIC)
				s = "You take a clove of garlic.";
			player.getActionSender().sendMessage(s);
			player.getInventory().addItemOrDrop(new Item(QuestConstants.GARLIC, 1));
			player.tempQuestInt = QuestConstants.GARLIC;
			return true;
		}
		if(objectId == 2614 && objectX == 3077 && objectY == 9775){
			if(stage == 1)
				return true;
			if(player.spawnedNpc != null){
				if(!player.spawnedNpc.isDead())
					if(player.spawnedNpc.getNpcId() == QuestConstants.COUNT_DRAYNOR){
						System.out.println("[Vampire Slayer]: vampire not spawned! (reason: spawnedNpcId = vampire id)");
						return true;
					}
			}
			/*if(player.tempQuestInt == QuestConstants.COUNT_DRAYNOR){
				System.out.println("[Vampire Slayer]: vampire not spawned! (reason: tempQuestInt = vampire id)");
				return true;
			}*/
			ObjectHandler.getInstance().removeObject(3077, 9775, 0, 0);
			ObjectHandler.getInstance().addObject(new GameObject(11208, 3077, 9775, 0, 3, 10, 2614, 10), true);
			final Npc npc = new Npc(QuestConstants.COUNT_DRAYNOR);
			//player.tempQuestInt = QuestConstants.COUNT_DRAYNOR;
			player.setStopPacket(true);
			final Tick timer = new Tick(3) {
				@Override
	            public void execute() {
					npc.sendTransform(3154, 5);
					NpcLoader.spawnNpc2(player, npc, 3077, 9775, 0, -1, false, false);
					//player.tempQuestInt = -1;
					player.setStopPacket(false);
					npc.getUpdateFlags().setFace(new Position(3077, 9770));
					npc.getUpdateFlags().sendAnimation(3114);
					World.getTickManager().submit(timer);
					stop();
				}
				
				final Tick timer = new Tick(5) {
					@Override
		            public void execute() {
						npc.getUpdateFlags().sendAnimation(808);
						CombatManager.attack(npc, player);
						npc.teleport(new Position(3077,9774,0));
						if(player.getInventory().playerHasItem(QuestConstants.GARLIC))
							player.getActionSender().sendMessage("The vampire seems to weaken.");
						//npc.getMovementHandler().addToPath(new Position(3077,9774,0));
						stop();
					}
				};
				
			};
			World.getTickManager().submit(timer);
			return true;
		}
		return false;
	}
	
	public boolean handleSecondClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 2613 && objectX == 3096 && objectY == 3269){
			ObjectHandler.getInstance().removeObject(3096, 3269, 1, 0);
			ObjectHandler.getInstance().addObject(new GameObject(2612, 3096, 3269, 1, 0, 10, 2612, 999999999), true);
			return true;
		}
		return false;
	}
	
	public boolean handleNpcDeath(final Player player, int npcId, int stage){
		if(npcId == QuestConstants.COUNT_DRAYNOR){
			if(stage == 5 && player.getInventory().playerHasItem(QuestConstants.STAKE)){
				player.getInventory().removeItem(new Item(QuestConstants.STAKE, 1));
				questCompleted(player);
				return true;
			}
		}
		return false;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.MORGAN){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Please please help us, bold adventurer!",Dialogues.NEAR_TEARS);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("What's the problem?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Our little village has been dreadfully ravaged by an evil",
													"vampire! He lives in the basement of the manor to the",
													"north, we need someone to get rid of him once and for",
													"all!",Dialogues.NEAR_TEARS);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendOption("No, vampires are scary!",
													"Ok, I'm up for an adventure.",
													"Have you got any tips on killing the vampire?");
					return true;
				}
				if(chatId == 5){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Ok, I'm up for an adventure.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(6);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("I think first you should seek help. I have a friend who",
													"is a retired vampire hunter, his name is Dr. Harlow. He",
													"may be able to give you some tips. He can normally be",
													"found in the Jolly Boar Inn in Varrock, he's a bit of",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("an old soak these days. Mention his old friend Morgan,",
													"I'm sure he wouldn't want me killed by a vampire.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendPlayerChat("I'll look him up then.", Dialogues.CONTENT);
					questStarted(player);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 0
		}//end of npc 755
		if(npcId == QuestConstants.DR_HARLOW){
			if(stage >= 2 && stage < 4){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Buy me a drrink pleassh...",Dialogues.DISORIENTED_LEFT);
					if(stage == 3 && player.getInventory().playerHasItem(QuestConstants.BEER))
						player.getDialogue().setNextChatId(9);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("No, you've had enough.",
													"Morgan needs your help!");
					return true;
				}
				if(chatId == 3){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Morgan needs your help!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Morgan you shhay..?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("His village is being terrorised by a vampire! He told me",
														"to ask you about how I can stop it.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Buy me a beer... then I'll teash you what you need to",
													"know...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("But this is your friend Morgan we're talking about!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Buy ush a drink anyway...",Dialogues.CONTENT);
					player.questStage[this.getId()] = 3;
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendPlayerChat("Here you go.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					if(player.getInventory().playerHasItem(QuestConstants.BEER)){
						player.getInventory().removeItem(new Item(QuestConstants.BEER, 1));
						player.getDialogue().send2Items2Lines("You give a beer to Dr Harlow.","", new Item(-1, 1), new Item(QuestConstants.BEER, 1));
						player.getDialogue().setNextChatId(1);
						player.questStage[this.getId()] = 4;
						return true;
					}
				}
			}//end of stage 2,3
			if(stage == 4){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Cheersh matey...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("So tell me how to kill vampires then.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Yesh Yesh vampires, I was very good at",
													"killing em once...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendStatement("Dr Harlow appears to sober up slightly.");
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Well you're gonna to need a stake, otherwise he'll just",
													"regenerate. Yes, you must have a stake to finish it off...",
													"I just happen to have one with me.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.STAKE, 1));
					player.getDialogue().sendItem1Line("Dr Harlow hands you a stake.", new Item(QuestConstants.STAKE, 1));
					player.questStage[this.getId()] = 5;
					return true;
				}
			}//end of stage 4
			if(stage == 5){
				if(chatId == 1){
					if(!player.hasItem(QuestConstants.STAKE)){
						player.getInventory().addItemOrDrop(new Item(QuestConstants.STAKE, 1));
						player.getDialogue().sendItem1Line("Dr Harlow hands you a stake.", new Item(QuestConstants.STAKE, 1));
						player.getDialogue().setNextChatId(7);
						return true;
					}
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("You'll need a hammer as well, to drive it in properly,",
													"your everyday general store hammer will do. One last",
													"thing... It's wise to carry garlic with you, vampires are",
													"somewhat weakened if they can smell garlic. Morgan",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("always liked garlic, you should try his house. But",
													"remember, a vampire is still a dangerous foe!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendPlayerChat("Thank you very much!", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 5
		}//end of npc 756
		return false;
	}

}
