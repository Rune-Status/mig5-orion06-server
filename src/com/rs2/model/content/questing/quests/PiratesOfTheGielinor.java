package com.rs2.model.content.questing.quests;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.cutscene.Cutscene;
import com.rs2.model.content.cutscene.cutscenes.PiratesOfTheGielinorScene;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;

public class PiratesOfTheGielinor extends Quest {
	
	final int rewardQP = 3;
	
	public PiratesOfTheGielinor(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			int thiefLvl = player.getSkill().getPlayerLevel(Skill.THIEVING);
			boolean b = false;
			if(player.getQuestPoints() >= 58 && player.questStage[10] == 1)
				b = true;
			if(b){
				String text[] = {"I can start this quest by speaking to Redbeard Frank who",
							"is at Port Sarim.",
							"as all reguired quests are complete, and I have enough QP.",
							"To complete this quest I need:",
							(thiefLvl >= 60 ? "@str@" : "")+"Level 60 Thieving"};
				return text;
			}else{
				String text[] = {"I can start this quest by speaking to Redbeard Frank who",
						"is at Port Sarim.",
						"To start this quest I need:",
						(player.getQuestPoints() >= 58 ? "@str@" : "")+"58 Quest Points",
						(player.questStage[10] == 1 ? "@str@" : "")+"Complete Pirate's Treasure",
						"To complete this quest I need:",
						(thiefLvl >= 60 ? "@str@" : "")+"Level 60 Thieving"};
				return text;
			}
		}
		if(stage == 2){
			String text[] = {"I should speak with Redbeard Frank when I'm ready to",
							"go and steal a ship."};
			return text;	
		}
		if(stage == 3){
			String text[] = {"Items I should bring to Redbeard Frank in Mos Le'Harmless:",
							"5,000 x gold coins",
							"3 x braindeth rum"};
			return text;	
		}
		if(stage == 4){
			String text[] = {"I should go and speak with Port Sarim's Jewelry shopkeeper."};
			return text;	
		}
		if(stage == 5){
			String text[] = {"I should go to Mos Le'Harmless and speak with",
							"Redbeard Frank."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "3 Quest Points", "23,000 Thieving XP", "2 x Dragonstone", "6 x Diamond", "8 x Ruby", "100 x Gold bars"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		player.getActionSender().moveInterfaceComponent(12145, 0, 30);
		super.questCompleted_2(player);
		//super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("You have completed "+this.getDefinition().getName()+"!", 12144);
		player.getActionSender().sendString("3 Quest Points", 12150);
		player.getActionSender().sendString("23,000 Thieving XP", 12151);
		player.getActionSender().sendString("2 x Dragonstone", 12152);
		player.getActionSender().sendString("6 x Diamond", 12153);
		player.getActionSender().sendString("8 x Ruby", 12154);
		player.getActionSender().sendString("100 x Gold bar", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.THIEVING, 23000);
		player.getInventory().addItemOrDrop(new Item(QuestConstants.DRAGONSTONE_NOTED, 2));
		player.getInventory().addItemOrDrop(new Item(QuestConstants.DIAMOND_NOTED, 6));
		player.getInventory().addItemOrDrop(new Item(QuestConstants.RUBY_NOTED, 8));
		player.getInventory().addItemOrDrop(new Item(QuestConstants.GOLD_BAR_NOTED, 100));
		//
		player.getActionSender().sendQuickSong(238, 320);
		player.getActionSender().sendString(""+player.getQuestPoints(), 12147);
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.MODEL_SHIP);
		player.getActionSender().sendInterface(12140);
	}
	
	boolean meetsRequirements(final Player player){
		if(player.getSkill().getPlayerLevel(Skill.THIEVING) < 60)
			return false;
		if(player.getQuestPoints() < 58 || player.questStage[10] != 1)
			return false;
		return true;
	}
	
	public boolean controlDying(final Entity attacker, final Entity victim, int stage){
		Player player = null;
		if(victim.isNpc() && attacker.isNpc()){
        	Npc def = (Npc) victim;
        	Npc atk = (Npc) attacker;
        	if(def.getNpcId() == QuestConstants.SAILOR_CUSTOM && atk.getNpcId() == QuestConstants.REDBEARD_FRANK_CUSTOM && atk.linkedPlayer != null){
            	player = atk.linkedPlayer;
            	def.setDead(true);
				CombatManager.startDeath(def);
            	atk.getUpdateFlags().setForceChatMessage(player.getUsername()+" ye'll be controlling our direction, while I guide ye!");
            	player.customSailTime = 6;
            	player.tempQuestInt = 0;
            	startSailing(player, stage);
        		return true;
        	}
        	if(def.getNpcId() == QuestConstants.SAILOR_CUSTOM && atk.getNpcId() == QuestConstants.PIRATE_CUSTOM && atk.linkedPlayer != null){
            	player = atk.linkedPlayer;
            	def.setDead(true);
				CombatManager.startDeath(def);
				if(player.inSailingShip() && def.inSailingShip()){
    				boolean wasShipNpc = false;
    				for (Npc playerPirateShipNpcs : player.getPirateShipNpcs()) {
    					if(def == playerPirateShipNpcs){
    						player.removePirateShipNpcs(def);
    						wasShipNpc = true;
    						break;
    					}
    				}
    				if(wasShipNpc){
    					boolean clearOfSailors = true;
    					for (Npc playerPirateShipNpcs : player.getPirateShipNpcs()) {
        					if(playerPirateShipNpcs.getNpcId() == QuestConstants.SAILOR_CUSTOM){
        						clearOfSailors = false;
        						break;
        					}
        				}
    					if(clearOfSailors){
    						Cutscene cs = new PiratesOfTheGielinorScene(player, null);
    						cs.start();
    						for (Npc playerPirateShipNpcs : player.getPirateShipNpcs()) {
    							playerPirateShipNpcs.setVisible(false);
    				            World.unregister(playerPirateShipNpcs);
    						}
    					}
    				}
    			}
        		return true;
        	}
        }
		if(attacker.isPlayer() && victim.isNpc()){
			//sailing ship check
			Npc def = (Npc) victim;
			player = (Player) attacker;
			if(player.inSailingShip() && def.inSailingShip()){
				boolean wasShipNpc = false;
				for (Npc playerPirateShipNpcs : player.getPirateShipNpcs()) {
					if(def == playerPirateShipNpcs){
						player.removePirateShipNpcs(def);
						wasShipNpc = true;
						break;
					}
				}
				if(wasShipNpc){
					boolean clearOfSailors = true;
					for (Npc playerPirateShipNpcs : player.getPirateShipNpcs()) {
    					if(playerPirateShipNpcs.getNpcId() == QuestConstants.SAILOR_CUSTOM){
    						clearOfSailors = false;
    						break;
    					}
    				}
					if(clearOfSailors){
						Cutscene cs = new PiratesOfTheGielinorScene(player, null);
						cs.start();
						for (Npc playerPirateShipNpcs : player.getPirateShipNpcs()) {
							playerPirateShipNpcs.setVisible(false);
				            World.unregister(playerPirateShipNpcs);
						}
					}
				}
			}
		}
		return false;
	}
	
	static Position[] crashPositions = {new Position(2754, 3004, 0), new Position(2960, 2996, 0), new Position(2919, 3110, 0), new Position(2721, 3183, 0), new Position(2674, 3221, 0), new Position(2653, 2966, 0)};
	
	public void crash(final Player player){
		final Tick timer1 = new Tick(4) {
            @Override
            public void execute() {
            	if(!player.isLoggedIn()){
            		stop();
            		return;
            	}
		player.teleportB(crashPositions[Misc.random_(crashPositions.length)]);
		player.getFrozenTimer().setWaitDuration(0);
    	player.getActionSender().resetCamera();
    	player.getActionSender().sendMapState(0);
    	player.getActionSender().sendSideBarInterfaces();
    	player.getActionSender().removeInterfaces();
    	if(player.tempQuestInt == 1)
    		player.getDialogue().sendStatement("The storm was too strong! You managed to get to shore.");
    	else if(player.tempQuestInt == 2)
    		player.getDialogue().sendStatement("The ship crashed into rocks! You managed to get to shore.");
    	else if(player.tempQuestInt == 3)
    		player.getDialogue().sendStatement("The cannons damaged the ship too much! You managed to get to shore.");
    	player.tempQuestInt = 0;
    	player.getDialogue().endDialogue();
    	if(player.getSpawnedNpc() != null){
    		player.getSpawnedNpc().setVisible(false);
    		World.unregister(player.getSpawnedNpc());
    	}
    	stop();
            }
		};
        World.getTickManager().submit(timer1);
	}
	
	public boolean allowedToAttackNpc(final Player player, int npcId, int stage){
		if(npcId == QuestConstants.PIRATE_CUSTOM){
			return false;
		}
		return true;
	}
	
	public void endSailing(final Player player, final int stage){
		final int qId = this.getId();
		final Tick timer1 = new Tick(4) {
            @Override
            public void execute() {
            	if(!player.isLoggedIn()){
            		stop();
            		return;
            	}
    	if(stage == 2) {
    		player.questStage[qId] = 3;
    		player.teleportB(new Position(3684, 2954));
    	}
    	if(stage == 5) {
    		int height = 1 + 4 + player.getIndex()*4;
    		player.teleportB(new Position(1815, 4834, height));
    		for(int i = 0; i < 2; i++){
    			Npc def = new Npc(QuestConstants.SAILOR_CUSTOM);
    			Position defPos = new Position(1814+(i*2), 4829, height);
    			def.setPosition(defPos);
    			def.setSpawnPosition(defPos);
				def.setCurrentX(defPos.getX());
				def.setCurrentY(defPos.getY());
				def.linkedPlayer = player;
				World.register(def);
				player.addPirateShipNpcs(def);
				Npc atk = new Npc(QuestConstants.PIRATE_CUSTOM);
				Position atkPos = new Position(1814+(i*2), 4830, height);
    			atk.setPosition(atkPos);
    			atk.setSpawnPosition(atkPos);
				atk.setCurrentX(atkPos.getX());
				atk.setCurrentY(atkPos.getY());
				atk.linkedPlayer = player;
				World.register(atk);
				player.addPirateShipNpcs(atk);
				CombatManager.attack(atk, def);
    		}
    		Npc def = new Npc(QuestConstants.SAILOR_CUSTOM);
			Position defPos = new Position(1815, 4839, height);
			def.setPosition(defPos);
			def.setSpawnPosition(defPos);
			def.setCurrentX(defPos.getX());
			def.setCurrentY(defPos.getY());
			def.linkedPlayer = player;
			World.register(def);
			player.addPirateShipNpcs(def);
			Npc atk = new Npc(QuestConstants.PIRATE_CUSTOM);
			Position atkPos = new Position(1815, 4838, height);
			atk.setPosition(atkPos);
			atk.setSpawnPosition(atkPos);
			atk.setCurrentX(atkPos.getX());
			atk.setCurrentY(atkPos.getY());
			atk.linkedPlayer = player;
			World.register(atk);
			player.addPirateShipNpcs(atk);
			CombatManager.attack(atk, def);
			Npc defP = new Npc(QuestConstants.SAILOR_CUSTOM);
			Position defPPos = new Position(1815, 4831, height);
			defP.setPosition(defPPos);
			defP.setSpawnPosition(defPPos);
			defP.setCurrentX(defPPos.getX());
			defP.setCurrentY(defPPos.getY());
			defP.linkedPlayer = player;
			World.register(defP);
			player.addPirateShipNpcs(defP);
			CombatManager.attack(defP, player);
    	}
    	player.getFrozenTimer().setWaitDuration(0);
    	player.getActionSender().resetCamera();
    	player.getActionSender().sendMapState(0);
    	player.getActionSender().sendSideBarInterfaces();
    	player.getActionSender().removeInterfaces();
    	player.tempQuestInt = 0;
    	if(player.getSpawnedNpc() != null){
    		player.getSpawnedNpc().setVisible(false);
    		World.unregister(player.getSpawnedNpc());
    	}
    	if(stage == 2){
    		Dialogues.sendDialogue(player, QuestConstants.REDBEARD_FRANK_CUSTOM, 110, 0);
    	}
    	if(stage == 5){
    		player.getDialogue().sendStatement("You attack the ship.");
    	}
    	stop();
            }
		};
        World.getTickManager().submit(timer1);
	}
	
	public static boolean wearingPirateGear(final Player player){
		if(player.getEquipment().getId(Constants.HAT) != QuestConstants.PIRATE_BANDANNA_1 &&
				player.getEquipment().getId(Constants.HAT) != QuestConstants.PIRATE_BANDANNA_2 &&
				player.getEquipment().getId(Constants.HAT) != QuestConstants.PIRATE_BANDANNA_3 &&
				player.getEquipment().getId(Constants.HAT) != QuestConstants.PIRATE_BANDANNA_4)
			return false;
		if(player.getEquipment().getId(Constants.LEGS) != QuestConstants.PIRATE_LEGGINGS_1 &&
				player.getEquipment().getId(Constants.LEGS) != QuestConstants.PIRATE_LEGGINGS_2 &&
				player.getEquipment().getId(Constants.LEGS) != QuestConstants.PIRATE_LEGGINGS_3 &&
				player.getEquipment().getId(Constants.LEGS) != QuestConstants.PIRATE_LEGGINGS_4)
			return false;
		if(player.getEquipment().getId(Constants.CHEST) != QuestConstants.STRIPY_PIRATE_SHIRT_1 &&
				player.getEquipment().getId(Constants.CHEST) != QuestConstants.STRIPY_PIRATE_SHIRT_2 &&
				player.getEquipment().getId(Constants.CHEST) != QuestConstants.STRIPY_PIRATE_SHIRT_3 &&
				player.getEquipment().getId(Constants.CHEST) != QuestConstants.STRIPY_PIRATE_SHIRT_4)
			return false;
		if(player.getEquipment().getId(Constants.FEET) != QuestConstants.PIRATE_BOOTS)
			return false;
		return true;
	}
	
	public void startSailing(final Player player, final int stage){
			final Tick timer1 = new Tick(10) {
	            @Override
	            public void execute() {
	            	//System.out.println("sail "+player.customSailTime);
	            	if(!player.isLoggedIn()){
	            		stop();
	            		return;
	            	}
	            	if(player.tempQuestInt == 0){//normal
	            		/*if(player.customSailTime <= 0){
	            			endSailing(player);
	            			stop();
	            			return;
	            		}*/
	            		if(Misc.random_(3) == 0){
	            			int i = 100+Misc.random_(3);
	            			Dialogues.sendDialogue(player, QuestConstants.REDBEARD_FRANK_CUSTOM, i, 0);
	            			stop();
	            			return;
	            		}
	            	}
	            	else if(player.tempQuestInt == 1){//storm
	            		if(Misc.random_(4) == 0){
	            			player.getActionSender().sendInterface(8677);
	            			crash(player);
	            			stop();
	            			return;
	            		} else {
	            			player.getSpawnedNpc().getUpdateFlags().setForceChatMessage("We survived the storm!");
	            			player.tempQuestInt = 0;
	            		}
	            	}
	            	else if(player.tempQuestInt == 2){//rock
	            		if(Misc.random_(6) == 0){
	            			player.getActionSender().sendInterface(8677);
	            			crash(player);
	            			stop();
	            			return;
	            		} else {
	            			player.getSpawnedNpc().getUpdateFlags().setForceChatMessage("I guess it was deep enough.");
	            			player.tempQuestInt = 0;
	            		}
	            	}
	            	else if(player.tempQuestInt == 3){//cannon
	            		if(Misc.random_(2) == 0){
	            			player.getActionSender().sendInterface(8677);
	            			crash(player);
	            			stop();
	            			return;
	            		} else {
	            			player.getSpawnedNpc().getUpdateFlags().setForceChatMessage("Their gunpowder was probably wet, we got lucky!");
	            			player.tempQuestInt = 0;
	            		}
	            	}
	            	player.customSailTime--;
	            	if(player.customSailTime <= 0){
	            		if(stage == 2)
	            			player.getSpawnedNpc().getUpdateFlags().setForceChatMessage("Land in sight!");
	            		if(stage == 5)
	            			player.getSpawnedNpc().getUpdateFlags().setForceChatMessage("I see the ship!");
	            		player.getActionSender().sendInterface(8677);
	            		endSailing(player, stage);
	            		stop();
	            	}
	            }
			};
	        World.getTickManager().submit(timer1);
	}
	
	boolean playerHasAllItems(final Player player){
		if(player.getInventory().playerHasItem(QuestConstants.COINS, 5000) && player.getInventory().playerHasItem(QuestConstants.BRAINDEATH_RUM, 3)){
			return true;
		}
		return false;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.REDBEARD_FRANK){
			if(!meetsRequirements(player))
				return false;
			if(stage >= 3 || stage == 1){
				if(chatId == 1){
					player.getDialogue().sendOption("Sail to Mos Le'Harmless.",
													"Never mind.");
					return true;
				}
				if(chatId == 2){
					if(optionId == 1){
						player.getActionSender().sendInterface(18679);
						player.getActionSender().sendMapState(2);
						player.getDialogue().dontCloseInterface();
						player.setStopPacket(true);
						final Tick timer1 = new Tick(2) {
				            @Override
				            public void execute() {
				            	if(!player.isLoggedIn()){
				            		stop();
				            		return;
				            	}
				            	player.teleportB(new Position(3684, 2954, 0));
				            	player.getActionSender().removeInterfaces();
				            	player.getActionSender().sendMapState(0);
				            	player.getDialogue().sendStatement("You arrive at Mos Le'Harmless.");
				            	player.setStopPacket(false);
				            	stop();
				            }
						};
				        World.getTickManager().submit(timer1);
						return false;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}//end of stage 3+
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Arr, Matey!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Would ye like to become a pirate?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendOption("Sure!",
													"No, thanks.");
					return true;
				}
				if(chatId == 4){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Sure!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Arr, that's the spirit!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("I'd like to get back into business,",
													"but can't do that without yer help.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("So, what do you need me to do?",Dialogues.CONTENT);
					questStarted(player);
					player.getDialogue().setNextChatId(1);
					return true;
				}
			}//end of stage 0
			if(stage == 2){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Well, first we are going to need a ship",
													"and I need yer help to steal one.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("We can use my boat to get close to a ship",
													"and sneak into it then.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("So, are ye ready to go now?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendOption("Yea, lets do this!",
													"I am sorry, but I am very busy.");
					return true;
				}
				if(chatId == 5){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yea, lets do this!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(6);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 6){//black screen
					int height = 1 + 4 + player.getIndex()*4;
					player.teleportB(new Position(2017, 4887, height));
					player.getActionSender().sendMapState(2);
					player.getActionSender().sendWalkableInterface(18679);
					player.getFrozenTimer().setWaitDuration(9999999);
					player.getActionSender().hideSideBars(new int[]{QuestConstants.ATTACK_TAB[0], QuestConstants.STATS_TAB[0], QuestConstants.QUEST_TAB[0], QuestConstants.INVENTORY_TAB[0], QuestConstants.EQUIPMENT_TAB[0],
															QuestConstants.PRAYER_TAB[0], QuestConstants.MAGIC_TAB[0], QuestConstants.OPTION_TAB[0], QuestConstants.EMOTE_TAB[0]});
					player.getActionSender().sendMapRegion();
					player.getActionSender().spinCamera(3550, 4000, 400, 0, 100);//w-e (e=+), n-s (n=+), height
					player.getActionSender().stillCamera(3400, 0, 0, 0, 100);//?, which way to look, ?
					player.getDialogue().sendStatement("You leave the port in Frank's boat.");
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendStatement("Frank sees a ship and guides you next to it.");
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendStatement("You sneak into the ship.");
					return true;
				}
				if(chatId == 9){
					player.getActionSender().sendWalkableInterface(-1);
					int height = 1 + 4 + player.getIndex()*4;
					Npc def = new Npc(QuestConstants.SAILOR_CUSTOM);
					Position defPos = new Position(2024, 4888, height);
					def.setPosition(defPos);
					def.setSpawnPosition(defPos);
					def.setCurrentX(defPos.getX());
					def.setCurrentY(defPos.getY());
					World.register(def);
					def.linkedPlayer = player;
					Npc atk = new Npc(QuestConstants.REDBEARD_FRANK_CUSTOM);
					Position atkPos = new Position(2023, 4888, height);
					atk.setFace(Constants.EAST);
					atk.setPosition(atkPos);
					atk.setSpawnPosition(atkPos);
					atk.setCurrentX(atkPos.getX());
					atk.setCurrentY(atkPos.getY());
					World.register(atk);
					atk.linkedPlayer = player;
					player.setSpawnedNpc(atk);
					atk.getUpdateFlags().setForceChatMessage("We are taking the ship!");
					def.getUpdateFlags().setForceChatMessage("You can't do that!");
					CombatManager.attack(atk, def);
					player.getDialogue().endDialogue();
					return false;
				}
			}//end of stage 2
		}//end of redbeard frank
		if(npcId == QuestConstants.REDBEARD_FRANK_CUSTOM){
			if(stage >= 2 || stage == 1){
				if(chatId == 1){
					player.getDialogue().sendOption("Sail to Port Sarim.",
													(stage == 1 ? "Never mind." : "Talk about Pirates of Gielinor."));
					return true;
				}
				if(chatId == 2){
					if(optionId == 1){
						player.getActionSender().sendInterface(18679);
						player.getActionSender().sendMapState(2);
						player.getDialogue().dontCloseInterface();
						player.setStopPacket(true);
						final Tick timer1 = new Tick(2) {
				            @Override
				            public void execute() {
				            	if(!player.isLoggedIn()){
				            		stop();
				            		return;
				            	}
				            	player.teleportB(new Position(3053, 3246, 0));
				            	player.getActionSender().removeInterfaces();
				            	player.getActionSender().sendMapState(0);
				            	player.getDialogue().sendStatement("You arrive at Port Sarim.");
				            	player.setStopPacket(false);
				            	stop();
				            }
						};
				        World.getTickManager().submit(timer1);
						return false;
					}else{
						if(stage == 1){
							player.getDialogue().endDialogue();
							return false;
						} else {
							if(stage == 3){
								player.getDialogue().sendNpcChat("Do you have the stuff I asked for?",Dialogues.CONTENT);
								player.getDialogue().setNextChatId(3);
							}
							if(stage == 4){
								player.getDialogue().sendNpcChat("Try asking from jewellery shop in Port Sarim",
																"when are they getting a refill.",Dialogues.CONTENT);
								player.getDialogue().setNextChatId(6);
							}
							if(stage == 5){
								player.getDialogue().sendPlayerChat("Theres a jewellery ship on its way to Port Sarim.", Dialogues.CONTENT);
								player.getDialogue().setNextChatId(7);
							}
							return true;
						}
					}
				}
				if(chatId == 3){
					if(playerHasAllItems(player)){
						player.getDialogue().sendPlayerChat("Yup, Here you go.", Dialogues.CONTENT);
					} else {
						player.getDialogue().sendPlayerChat("No, not yet.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					}
					return true;
				}
				if(chatId == 4){
					if(playerHasAllItems(player)){
						player.getInventory().removeItem(new Item(QuestConstants.COINS, 5000));
						player.getInventory().removeItem(new Item(QuestConstants.BRAINDEATH_RUM, 3));
						player.getDialogue().sendNpcChat("We want to attack a ship that has gold or",
														"jewellery in its cargo.",Dialogues.CONTENT);
						player.questStage[this.getId()] = 4;
						return true;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Try asking from jewellery shop in Port Sarim",
													"when are they getting a refill.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("I'll try to recruit some men while ye are at it.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("We have everything we need then.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Are ye ready to go sailing and attack the ship?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendOption("Yes.",
													"No.");
					return true;
				}
				if(chatId == 10){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yes!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(11);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 11){
					if(wearingPirateGear(player)){
						player.getDialogue().sendNpcChat("Great, Let's go then!",Dialogues.CONTENT);
					} else {
						player.getDialogue().sendNpcChat("Ye can't be dressed like that!",
														"Get something more suitable for a pirate!",Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					}
					return true;
				}
				if(chatId == 12){//black screen
					int height = 1 + 4 + player.getIndex()*4;
					player.teleportB(new Position(2017, 4887, height));
					player.getActionSender().sendMapRegion();
					player.getActionSender().spinCamera(3550, 4000, 400, 0, 100);//w-e (e=+), n-s (n=+), height
					player.getActionSender().stillCamera(3400, 0, 0, 0, 100);//?, which way to look, ?
					player.getActionSender().sendMapState(2);
					player.getActionSender().sendWalkableInterface(18679);
					player.getFrozenTimer().setWaitDuration(9999999);
					player.getActionSender().hideSideBars(new int[]{QuestConstants.ATTACK_TAB[0], QuestConstants.STATS_TAB[0], QuestConstants.QUEST_TAB[0], QuestConstants.INVENTORY_TAB[0], QuestConstants.EQUIPMENT_TAB[0],
															QuestConstants.PRAYER_TAB[0], QuestConstants.MAGIC_TAB[0], QuestConstants.OPTION_TAB[0], QuestConstants.EMOTE_TAB[0]});
					player.getDialogue().sendStatement("You leave the port.");
					return true;
				}
				if(chatId == 13){
					int height = 1 + 4 + player.getIndex()*4;
					Npc atk = new Npc(QuestConstants.REDBEARD_FRANK_CUSTOM);
					Position atkPos = new Position(2023, 4888, height);
					atk.setFace(Constants.EAST);
					atk.setPosition(atkPos);
					atk.setSpawnPosition(atkPos);
					atk.setCurrentX(atkPos.getX());
					atk.setCurrentY(atkPos.getY());
					World.register(atk);
					player.setSpawnedNpc(atk);
					player.getActionSender().sendWalkableInterface(-1);
					player.getDialogue().endDialogue();
					player.getSpawnedNpc().getUpdateFlags().setForceChatMessage("I'll help ye with the sailing!");
					player.customSailTime = 6;
	            	player.tempQuestInt = 0;
	            	startSailing(player, stage);
					return false;
				}
				if(chatId == 100){
					player.getSpawnedNpc().getUpdateFlags().setForceChatMessage("It looks like theres a storm ahead!");
					player.getDialogue().sendOption("Turn away from the storm.",
													"Keep the direction.");
					player.getDialogue().setNextChatId(103);
					return true;
				}
				if(chatId == 103){
					if(optionId == 1){
						player.customSailTime += 3;
						player.tempQuestInt = 0;
					} else {
						player.tempQuestInt = 1;
					}
					startSailing(player, stage);
					player.getDialogue().endDialogue();
					return false;
				}
				if(chatId == 101){
					player.getSpawnedNpc().getUpdateFlags().setForceChatMessage("The water is getting a bit shallow for our ship!");
					player.getDialogue().sendOption("Turn to sail to deeper waters.",
													"Keep the direction.");
					player.getDialogue().setNextChatId(104);
					return true;
				}
				if(chatId == 104){
					if(optionId == 1){
						player.customSailTime += 2;
						player.tempQuestInt = 0;
					} else {
						player.tempQuestInt = 2;
					}
					startSailing(player, stage);
					player.getDialogue().endDialogue();
					return false;
				}
				if(chatId == 102){
					player.getSpawnedNpc().getUpdateFlags().setForceChatMessage("It's the royal fleet, its over if they get in range!");
					player.getDialogue().sendOption("Turn away before they can use their cannons.",
													"Keep the direction.");
					player.getDialogue().setNextChatId(105);
					return true;
				}
				if(chatId == 105){
					if(optionId == 1){
						player.customSailTime += 1;
						player.tempQuestInt = 0;
					} else {
						player.tempQuestInt = 3;
					}
					startSailing(player, stage);
					player.getDialogue().endDialogue();
					return false;
				}
				if(chatId == 110){
					player.getDialogue().sendNpcChat("We're here, ye did good my friend!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 111){
					player.getDialogue().sendNpcChat("Now we need to recruit some more men.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 112){
					player.getDialogue().sendNpcChat("Bring me 5,000 gold coins and a few bottles of rom,",
													"and I'll handle the recruiting.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 2+
		}//end of custom redbeard frank
		if(npcId == QuestConstants.GRUM){
			if(stage == 4){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("You don't seem to have much for sale now,",
														"are you expecting a refill?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Anytime now, there should be a ship on its way here.",Dialogues.CONTENT);
					player.questStage[this.getId()] = 5;
					return true;
				}
			}//end of stage 4
		}//end of grum
		return false;
	}

}
