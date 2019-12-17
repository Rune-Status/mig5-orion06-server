package com.rs2.model.content.questing.quests;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class HeroesQuest extends Quest {
	
	final int rewardQP = 1;
	
	public HeroesQuest(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			int herbLvl = player.getSkill().getPlayerLevel(Skill.HERBLORE);
			int mineLvl = player.getSkill().getPlayerLevel(Skill.MINING);
			int fishLvl = player.getSkill().getPlayerLevel(Skill.FISHING);
			int cookLvl = player.getSkill().getPlayerLevel(Skill.COOKING);
			boolean b = false;
			if(player.getQuestPoints() >= 56 && player.questStage[16] == 1 && player.questStage[58] == 1 && player.questStage[5] == 1 &&
					player.questStage[61] == 1 && player.questStage[29] == 1)
				b = true;
			if(b){
				String text[] = {"I can start this quest by speaking to Achietties at the",
							"Heroes' Guild located North of Taverley",
							"as all reguired quests are complete, and I have enough QP.",
							"To complete this quest I need:",
							(herbLvl >= 25 ? "@str@" : "")+"Level 25 Herblore",
							(mineLvl >= 50 ? "@str@" : "")+"Level 50 Mining",
							(fishLvl >= 53 ? "@str@" : "")+"Level 53 Fishing",
							(cookLvl >= 53 ? "@str@" : "")+"Level 53 Cooking"};
				return text;
			}else{
				String text[] = {"I can start this quest by speaking to Achietties at the",
						"Heroes' Guild located North of Taverley.",
						"To start this quest I need:",
						(player.getQuestPoints() >= 56 ? "@str@" : "")+"56 Quest Points",
						(player.questStage[16] == 1 ? "@str@" : "")+"Complete Shield of Arrav",
						(player.questStage[58] == 1 ? "@str@" : "")+"Complete Lost City",
						(player.questStage[5] == 1 ? "@str@" : "")+"Complete Dragon Slayer",
						(player.questStage[61] == 1 ? "@str@" : "")+"Complete Merlin's Crystal",
						(player.questStage[29] == 1 ? "@str@" : "")+"Complete Druidic Ritual",
						"To complete this quest I need:",
						(herbLvl >= 25 ? "@str@" : "")+"Level 25 Herblore",
						(mineLvl >= 50 ? "@str@" : "")+"Level 50 Mining",
						(fishLvl >= 53 ? "@str@" : "")+"Level 53 Fishing",
						(cookLvl >= 53 ? "@str@" : "")+"Level 53 Cooking"};
				return text;
			}
		}
		if(stage == 2 || stage == 8){
			String text[] = {"Achietties will let me into the Heroes' Guild if I can get:",
							(player.hasItem(QuestConstants.FIRE_FEATHER) ? "@str@" : "")+"An Entranan Firebird Feather - I should check on Entrana",
							(player.hasItem(QuestConstants.LAVA_EEL) ? "@str@" : "")+"A cooked lava eel - I should speak to a fishing expert",
							(player.hasItem(QuestConstants.THIEVES_ARMBAND) ? "@str@" : "")+"A Master Thieves Armband - the "+(player.gang == QuestConstants.BLACK_ARM_GANG ? "Black Arm" : "Phoenix")+" Gang can help me",
							(playerHasAllItems(player) ? "I should bring the items to Achietties now." : "")};
			return text;	
		}
		if(stage == 3){
			String text[] = {"Achietties will let me into the Heroes' Guild if I can get:",
							(player.hasItem(QuestConstants.FIRE_FEATHER) ? "@str@" : "")+"An Entranan Firebird Feather - I should check on Entrana",
							(player.hasItem(QuestConstants.LAVA_EEL) ? "@str@" : "")+"A cooked lava eel - I should speak to a fishing expert",
							(player.hasItem(QuestConstants.THIEVES_ARMBAND) ? "@str@" : "")+"A Master Thieves Armband - the "+(player.gang == QuestConstants.BLACK_ARM_GANG ? "Black Arm" : "Phoenix")+" Gang can help me",
							"@str@I spoke to "+(player.gang == QuestConstants.BLACK_ARM_GANG ? "Katrine" : "Straven")+" about the Master Thieves Armband.",
							(player.gang == QuestConstants.BLACK_ARM_GANG ? "She" : "He")+" told me I can get one by stealing Pete's Candlestick",
							"I should use the password "+(player.gang == QuestConstants.BLACK_ARM_GANG ? "she" : "he")+" gave me at Brimhaven"};
			return text;	
		}
		if(stage == 4 && player.gang == QuestConstants.BLACK_ARM_GANG){
			String text[] = {"Achietties will let me into the Heroes' Guild if I can get:",
							(player.hasItem(QuestConstants.FIRE_FEATHER) ? "@str@" : "")+"An Entranan Firebird Feather - I should check on Entrana",
							(player.hasItem(QuestConstants.LAVA_EEL) ? "@str@" : "")+"A cooked lava eel - I should speak to a fishing expert",
							(player.hasItem(QuestConstants.THIEVES_ARMBAND) ? "@str@" : "")+"A Master Thieves Armband - the "+(player.gang == QuestConstants.BLACK_ARM_GANG ? "Black Arm" : "Phoenix")+" Gang can help me",
							"@str@I spoke to "+(player.gang == QuestConstants.BLACK_ARM_GANG ? "Katrine" : "Straven")+" about the Master Thieves Armband.",
							"I should impersonate Hartigen the Black Knight to gain",
							"access to the mansion."};
			return text;	
		}
		if(stage == 4 && player.gang == QuestConstants.PHOENIX_GANG){
			String text[] = {"Achietties will let me into the Heroes' Guild if I can get:",
							(player.hasItem(QuestConstants.FIRE_FEATHER) ? "@str@" : "")+"An Entranan Firebird Feather - I should check on Entrana",
							(player.hasItem(QuestConstants.LAVA_EEL) ? "@str@" : "")+"A cooked lava eel - I should speak to a fishing expert",
							(player.hasItem(QuestConstants.THIEVES_ARMBAND) ? "@str@" : "")+"A Master Thieves Armband - the "+(player.gang == QuestConstants.BLACK_ARM_GANG ? "Black Arm" : "Phoenix")+" Gang can help me",
							"@str@I spoke to "+(player.gang == QuestConstants.BLACK_ARM_GANG ? "Katrine" : "Straven")+" about the Master Thieves Armband.",
							"I should go talk to Charlie the Cook in the back",
							"of the bar."};
			return text;	
		}
		if(stage == 5 && player.gang == QuestConstants.BLACK_ARM_GANG){
			String text[] = {"Achietties will let me into the Heroes' Guild if I can get:",
							(player.hasItem(QuestConstants.FIRE_FEATHER) ? "@str@" : "")+"An Entranan Firebird Feather - I should check on Entrana",
							(player.hasItem(QuestConstants.LAVA_EEL) ? "@str@" : "")+"A cooked lava eel - I should speak to a fishing expert",
							(player.hasItem(QuestConstants.THIEVES_ARMBAND) ? "@str@" : "")+"A Master Thieves Armband - the "+(player.gang == QuestConstants.BLACK_ARM_GANG ? "Black Arm" : "Phoenix")+" Gang can help me",
							"@str@I spoke to "+(player.gang == QuestConstants.BLACK_ARM_GANG ? "Katrine" : "Straven")+" about the Master Thieves Armband.",
							"I should go talk to Grip."};
			return text;	
		}
		if(stage == 5 && player.gang == QuestConstants.PHOENIX_GANG){
			String text[] = {"Achietties will let me into the Heroes' Guild if I can get:",
							(player.hasItem(QuestConstants.FIRE_FEATHER) ? "@str@" : "")+"An Entranan Firebird Feather - I should check on Entrana",
							(player.hasItem(QuestConstants.LAVA_EEL) ? "@str@" : "")+"A cooked lava eel - I should speak to a fishing expert",
							(player.hasItem(QuestConstants.THIEVES_ARMBAND) ? "@str@" : "")+"A Master Thieves Armband - the "+(player.gang == QuestConstants.BLACK_ARM_GANG ? "Black Arm" : "Phoenix")+" Gang can help me",
							"@str@I spoke to "+(player.gang == QuestConstants.BLACK_ARM_GANG ? "Katrine" : "Straven")+" about the Master Thieves Armband.",
							"Charlie told me that there is a side entrance to the mansion",
							"that I can use."};
			return text;	
		}
		if(stage == 6 && player.gang == QuestConstants.BLACK_ARM_GANG){
			String text[] = {"Achietties will let me into the Heroes' Guild if I can get:",
							(player.hasItem(QuestConstants.FIRE_FEATHER) ? "@str@" : "")+"An Entranan Firebird Feather - I should check on Entrana",
							(player.hasItem(QuestConstants.LAVA_EEL) ? "@str@" : "")+"A cooked lava eel - I should speak to a fishing expert",
							(player.hasItem(QuestConstants.THIEVES_ARMBAND) ? "@str@" : "")+"A Master Thieves Armband - the "+(player.gang == QuestConstants.BLACK_ARM_GANG ? "Black Arm" : "Phoenix")+" Gang can help me",
							"@str@I spoke to "+(player.gang == QuestConstants.BLACK_ARM_GANG ? "Katrine" : "Straven")+" about the Master Thieves Armband.",
							(player.hasItem(QuestConstants.MISCELLANEOUS_KEY) ? "I should find out what the key Grip gave me opens." : "I should talk to Grip.")};
			return text;	
		}
		if(stage == 7 && player.gang == QuestConstants.BLACK_ARM_GANG){
			String text[] = {"Achietties will let me into the Heroes' Guild if I can get:",
							(player.hasItem(QuestConstants.FIRE_FEATHER) ? "@str@" : "")+"An Entranan Firebird Feather - I should check on Entrana",
							(player.hasItem(QuestConstants.LAVA_EEL) ? "@str@" : "")+"A cooked lava eel - I should speak to a fishing expert",
							(player.hasItem(QuestConstants.THIEVES_ARMBAND) ? "@str@" : "")+"A Master Thieves Armband - the "+(player.gang == QuestConstants.BLACK_ARM_GANG ? "Black Arm" : "Phoenix")+" Gang can help me",
							"@str@I spoke to "+(player.gang == QuestConstants.BLACK_ARM_GANG ? "Katrine" : "Straven")+" about the Master Thieves Armband.",
							"I should now give the other candlestick to my partner and",
							"then return to Katrine."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "1 Quest Point", "Access to the Heroes' Guild", "A total of 29,232 XP spread", "over twelve skills"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("Access to the Heroes' Guild", 12151);
		player.getActionSender().sendString("A total of 29,232 XP spread", 12152);
		player.getActionSender().sendString("over twelve skills", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.ATTACK, 3075);
		player.getSkill().addQuestExp(Skill.DEFENCE, 3075);
		player.getSkill().addQuestExp(Skill.STRENGTH, 3075);
		player.getSkill().addQuestExp(Skill.HITPOINTS, 3075);
		player.getSkill().addQuestExp(Skill.RANGED, 2075);
		player.getSkill().addQuestExp(Skill.FISHING, 2725);
		player.getSkill().addQuestExp(Skill.COOKING, 2825);
		player.getSkill().addQuestExp(Skill.WOODCUTTING, 1575);
		player.getSkill().addQuestExp(Skill.FIREMAKING, 1575);
		player.getSkill().addQuestExp(Skill.SMITHING, 2257);
		player.getSkill().addQuestExp(Skill.MINING, 2575);
		player.getSkill().addQuestExp(Skill.HERBLORE, 1325);
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.DRAGON_BATTLEAXE);
		player.getActionSender().sendInterface(12140);
	}
	
	boolean meetsRequirements(final Player player){
		if(player.getSkill().getPlayerLevel(Skill.HERBLORE) < 25 || player.getSkill().getPlayerLevel(Skill.MINING) < 50 || player.getSkill().getPlayerLevel(Skill.FISHING) < 53 || player.getSkill().getPlayerLevel(Skill.COOKING) < 53)
			return false;
		if(player.getQuestPoints() < 56 || player.questStage[16] != 1 || player.questStage[58] != 1 || player.questStage[5] != 1 || player.questStage[61] != 1 || player.questStage[29] != 1)
			return false;
		return true;
	}
	
	boolean playerHasAllItems(final Player player){
		if(player.getInventory().playerHasItem(QuestConstants.FIRE_FEATHER) && player.getInventory().playerHasItem(QuestConstants.LAVA_EEL) &&
				player.getInventory().playerHasItem(QuestConstants.THIEVES_ARMBAND)){
			return true;
		}
		return false;
	}
	
	public boolean getQuestPickups(final Player player, int itemId, int stage){
		if(itemId == QuestConstants.FIRE_FEATHER){
			if(stage == 0){
				player.getActionSender().sendMessage("It looks dangerously hot, and you have no reason to take it.");
				return true;
			}
			if(stage >= 2 && player.getEquipment().getId(Constants.HANDS) != QuestConstants.ICE_GLOVES){
				player.getActionSender().sendMessage("It looks dangerously hot, you should find a way to pick it up.");
				return true;
			}
		}
		return false;
	}
	
	boolean wearingBlackArmor(final Player player){
		if(player.getEquipment().getId(Constants.HAT) == QuestConstants.BLACK_FULL_HELM && player.getEquipment().getId(Constants.CHEST) == QuestConstants.BLACK_PLATEBODY && player.getEquipment().getId(Constants.LEGS) == QuestConstants.BLACK_PLATELEGS)
			return true;
		return false;
	}
	
	public boolean useQuestItemOnObject(final Player player, int itemId, int objectId, final int stage){
		if(itemId == QuestConstants.MISCELLANEOUS_KEY && objectId == 2622){
			player.getActionSender().walkTo(0, (player.getPosition().getY() < 3197) ? 1 : -1, true);
			player.getActionSender().walkThroughDoor(objectId, 2781, 3197, 0);
			return true;
		}
		return false;
	}
	
	public boolean getObjectDialog(int clickId, final Player player, int objectId, int chatId, int optionId, int npcChatId, int x, int y, int stage){
		if(objectId == 2633 && x == 2766 && y == 3199 && clickId == 1){
			if(stage == 6){
				if(chatId == 1){
					player.getDialogue().sendStatement("You find two candlesticks in the chest. So that will be one for you,",
														"and one for the person who killed Grip for you.");
					return true;
				}
				if(chatId == 2){
					player.getDialogue().endDialogue();
					player.getInventory().addItemOrDrop(new Item(QuestConstants.PETES_CANDLESTICK, 2));
					ObjectHandler.getInstance().removeObject(2766, 3199, 0, 0);
					ObjectHandler.getInstance().addObject(new GameObject(2632, 2766, 3199, 0, 2, 10, 2632, 999999999), true);
					player.questStage[this.getId()] = 7;
					return false;
				}
			}
		}
		return false;
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 2621 && objectX == 2764 && objectY == 3197){//treasure room door
			if(stage >= 6){
				if(player.getPosition().getX() < 2764){
					if(player.getInventory().playerHasItem(QuestConstants.GRIPS_KEYRING)){
						player.getActionSender().walkTo((player.getPosition().getX() < 2764) ? 1 : -1, 0, true);
						player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
						player.getActionSender().sendMessage("Grip's key unlocks the door.");
						return true;
					}
				} else {
					player.getActionSender().walkTo((player.getPosition().getX() < 2764) ? 1 : -1, 0, true);
					player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
					player.getActionSender().sendMessage("The door locks shut behind you.");
					return true;
				}
			}
			player.getDialogue().sendStatement("This door is locked.");
			player.getDialogue().endDialogue();
			return true;
		}
		if(objectId == 2632 && objectX == 2766 && objectY == 3199){//treasure chest
			if(stage == 6){
				ObjectHandler.getInstance().removeObject(2766, 3199, 0, 0);
				ObjectHandler.getInstance().addObject(new GameObject(2633, 2766, 3199, 0, 2, 10, 2632, 999999999), true);
				player.getActionSender().sendMessage("You open the chest.");
			}
			return true;
		}
		if(objectId == 2626 && objectX == 2811 && objectY == 3170){//black arm gang hideout door @brimhaven
			if(stage == 3 && player.gang == QuestConstants.BLACK_ARM_GANG){
				Dialogues.sendDialogue(player, QuestConstants.GRUBOR, 100, 0);
			}
			if(stage >= 4 && player.gang == QuestConstants.BLACK_ARM_GANG){
				player.getActionSender().walkTo((player.getPosition().getX() < 2811) ? 1 : -1, 0, true);
				player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
			}
			return true;
		}
		if(objectId == 2627 && objectX == 2774 && objectY == 3187){//scarface pete's mansion entrance
			if(stage == 4 && player.gang == QuestConstants.BLACK_ARM_GANG){
				Dialogues.sendDialogue(player, QuestConstants.GARV, 100, 0);
			}
			if(stage >= 5 && player.gang == QuestConstants.BLACK_ARM_GANG){
				player.getActionSender().walkTo(0, (player.getPosition().getY() < 3188) ? 1 : -1, true);
				player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
			}
			return true;
		}
		if(objectId == 2628 && objectX == 2788 && objectY == 3189){//door to back of the bar
			if(stage < 4 && player.gang == QuestConstants.PHOENIX_GANG){
				Dialogues.sendDialogue(player, QuestConstants.ALFONSE_THE_WAITER, 100, 0);
			}
			if(stage >= 4 && player.gang == QuestConstants.PHOENIX_GANG){
				player.getActionSender().walkTo(0, (player.getPosition().getY() < 3190) ? 1 : -1, true);
				player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
			}
			return true;
		}
		if(objectId == 2629 && objectX == 2787 && objectY == 3190){//push wall
			if(stage >= 5){
				player.getActionSender().walkTo((player.getPosition().getX() < 2787) ? 1 : -1, 0, true);
				player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
			}
			return true;
		}
		if(objectId == 2622 && objectX == 2781 && objectY == 3197){//door
			if(player.getPosition().getY() < 3197){
				player.getDialogue().sendStatement("This door is locked.");
				player.getDialogue().endDialogue();
			}else{
				player.getActionSender().walkTo(0, (player.getPosition().getY() < 3197) ? 1 : -1, true);
				player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
			}
			return true;
		}
		if((objectId == 2635 || objectId == 2636) && objectX == 2775 && objectY == 3196){//cupboard
			if(stage == 6){
				if(objectId == 2635){
					ObjectHandler.getInstance().removeObject(2775, 3196, 0, 0);
					ObjectHandler.getInstance().addObject(new GameObject(2636, 2775, 3196, 0, 3, 10, 2635, 999999999), true);
				}
				Dialogues.sendDialogue(player, QuestConstants.PIRATE_GUARD, 100, 0);
			}
			return true;
		}
		return false;
	}
	
	public boolean handleNpcDeath(final Player player, int npcId, int stage){
		if(npcId == QuestConstants.GRIP){
			Npc npc = Npc.getNpcById(QuestConstants.GRIP);
			GroundItem drop = new GroundItem(new Item(QuestConstants.GRIPS_KEYRING, 1), npc.getPosition(), false);
	        GroundItemManager.getManager().dropItem(drop);
			return true;
		}
		return false;
	}
	
	public boolean handleItemOnItem(final Player player, int item1, int item2, int stage){
		if(stage >= 2){
			if((item1 == QuestConstants.UNFINISHED_HARRALANDER_POTION && item2 == QuestConstants.BLAMISH_SNAIL_SLIME) || (item1 == QuestConstants.BLAMISH_SNAIL_SLIME && item2 == QuestConstants.UNFINISHED_HARRALANDER_POTION)){
				player.getInventory().removeItem(new Item(item1, 1));
	    		player.getInventory().removeItem(new Item(item2, 1));
	    		player.getInventory().addItem(new Item(QuestConstants.BLAMISH_OIL, 1));
	    		player.getInventory().addItem(new Item(QuestConstants.SAMPLE_BOTTLE, 1));
				player.getActionSender().sendMessage("You mix the slime into your potion.");
				return true;
			}
			if((item1 == QuestConstants.BLAMISH_OIL && item2 == QuestConstants.FISHING_ROD) || (item1 == QuestConstants.FISHING_ROD && item2 == QuestConstants.BLAMISH_OIL)){
				player.getInventory().removeItem(new Item(item1, 1));
	    		player.getInventory().removeItem(new Item(item2, 1));
	    		player.getInventory().addItem(new Item(QuestConstants.OILY_FISHING_ROD, 1));
	    		player.getInventory().addItem(new Item(QuestConstants.VIAL, 1));
				player.getActionSender().sendMessage("You rub the oil into the fishing rod.");
				return true;
			}
		}
		return false;
	}
	
	public boolean allowedToAttackNpc(final Player player, int npcId, int stage){
		if(npcId == QuestConstants.GRIP && player.gang == QuestConstants.BLACK_ARM_GANG){
			player.getDialogue().sendPlayerChat("I can't attack the head guard here! There are too",
												"many witnesses around to see me do it! I'd have the",
												"whole of Brimhaven after me! Besides, if he dies I want",
												"the promotion!", Dialogues.CONTENT);
			player.getDialogue().endDialogue();
			return false;
		}
		return true;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.ACHIETTIES){
			if(!meetsRequirements(player))
				return false;
			if(chatId == 1){
				player.getDialogue().sendNpcChat("Greetings. Welcome to the Heroes' Guild.",Dialogues.CONTENT);
				return true;
			}
			if(stage == 0){
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Only the greatest heroes of this land may gain",
													"entrance to this guild.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendOption("I'm a hero, may I apply to join?",
													"Good for the foremost heroes of the land.");
					return true;
				}
				if(chatId == 4){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I'm a hero - may I apply to join?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Well, you have a lot of quest points, and you have done",
													"all of the required quests, so you may now begin the",
													"tasks to meet the entry requirements for membership in",
													"the Heroes' Guild. The three items required",Dialogues.CONTENT);
					questStarted(player);
					return true;
				}
			}//end of stage 0
			if(stage >= 2){
				if(chatId == 2){
					player.getDialogue().sendNpcChat("How goes thy quest adventurer?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					if(playerHasAllItems(player) && stage == 8){
						player.getDialogue().sendPlayerChat("I have all the required items.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(14);
					} else
						player.getDialogue().sendPlayerChat("It's tough. I've not done it yet.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Remember, the items you need to enter are:",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(10);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("for entrance are: An Entranan Firebird feather, a",
													"Master Thieves' armband, and a cooked Lava Eel.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendOption("Any hints on getting the armband?",
													"Any hints on getting the feather?",
													"Any hints on getting the eel?",
													"I'll start looking for all those things then.");
					return true;
				}
				if(chatId == 8){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Any hints on getting the thieves armband?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Any hints on getting the feather?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(11);
						return true;
					}
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("Any hints on getting the eel?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(12);
						return true;
					}
					if(optionId == 4){
						player.getDialogue().sendPlayerChat("I'll start looking for all those things then.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(13);
						return true;
					}
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("I'm sure you have relevant contacts to find out about",
													"that.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("An Entranan Firebirds' feather, A Master Thieves",
													"armband, and a cooked Lava Eel.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(6);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("Not really - other than Entranan firebirds tend to live",
													"on Entrana.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("Maybe go and find someone who knows a lot about",
													"fishing?",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("Good luck with that.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("I see that you have. Well done; Now, to complete the",
													"quest, and gain entry to the Heroes' Guild in your final",
													"task all that you have to do is...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendPlayerChat("W-what? What do you mean? There's MORE???", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("I'm sorry, I was just having a little fun with you. Just",
													"a little Heroes' Guild humour there. What I really",
													"meant was",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendNpcChat("Congratulations! You have completed the Heroes' Guild",
													"entry requirements! You will find the door now open",
													"for you! Enter, Hero! And take this reward!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					if(playerHasAllItems(player)){
						player.getDialogue().endDialogue();
						player.getInventory().removeItem(new Item(QuestConstants.FIRE_FEATHER, 1));
						player.getInventory().removeItem(new Item(QuestConstants.LAVA_EEL, 1));
						player.getInventory().removeItem(new Item(QuestConstants.THIEVES_ARMBAND, 1));
						questCompleted(player);
						return true;
					}
				}
			}//end of stage 2-8
		}//end of achietties
		if(npcId == QuestConstants.GERRANT){
			if(stage >= 2){
				if(player.hasItem(QuestConstants.BLAMISH_SNAIL_SLIME) || player.hasItem(QuestConstants.BLAMISH_OIL) || player.hasItem(QuestConstants.OILY_FISHING_ROD) || player.hasItem(QuestConstants.RAW_LAVA_EEL) || player.hasItem(QuestConstants.LAVA_EEL))
					return false;
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Welcome! You can buy fishing equipment at my store.",
													"We'll also buy anything you catch off you.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Let's see what you've got then.",
													"Sorry, I'm not interested.",
													"I want to find out how to catch a lava eel.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("I want to find out how to catch a lava eel.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					} else {
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Lava eels eh? That's a tricky one that is, you'll need a",
													"lava-proof fishing line. The method for making this would",
													"be to take an ordinary fishing rod, and then cover it",
													"with the fire-proof Blamish Oil.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("You know... thinking about it... I may have a jar of",
													"Blamish Slime around here somewhere... Now where did",
													"I put it?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendStatement("Gerrant searches around a bit.");
					return true;
				}
				if(chatId == 7){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.BLAMISH_SNAIL_SLIME, 1));
					player.getDialogue().sendNpcChat("Aha! Here it is! Take this slime, mix it with some",
													"Harralander and water and you'll have the Blamish Oil",
													"you need for treating your fishing rod.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 2
		}//end of gerrant
		if(npcId == QuestConstants.STRAVEN){
			if(player.gang != QuestConstants.PHOENIX_GANG)
				return false;
			if(stage == 2){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("How would I go about getting a Master Thief",
														"armband?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Ooh... tricky stuff. Took me YEARS to get that rank.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Well, what some of the more aspiring thieves in our",
													"gang are working on right now is to steal some very",
													"valuable candlesticks from Scarface Pete - the pirate",
													"leader on Karamja.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("His security is excellent, and the target very valuable so",
													"that might be enough to get you the rank.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Go talk to our man Alfonse, the waiter in the Shrimp",
													"and Parrot.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Use the secret word 'gherkin' to show you're one of us.",Dialogues.CONTENT);
					player.questStage[this.getId()] = 3;
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 2
			if(stage == 5){
				if(!player.getInventory().playerHasItem(QuestConstants.PETES_CANDLESTICK))
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I have retrieved a candlestick!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Hmmm. Not bad, not bad. Let's see it, make sure it's",
													"genuine.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendStatement("You hand Straven the candlestick.");
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("So is this enough to get me a Master Thief armband?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Hmm...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("I dunno...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Aww, go on then. I suppose I'm in a generous mood",
													"today.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendStatement("Straven hands you a Master Thief armband.");
					return true;
				}
				if(chatId == 9){
					player.questStage[this.getId()] = 8;
					player.getInventory().removeItem(new Item(QuestConstants.PETES_CANDLESTICK, 1));
					player.getInventory().addItemOrDrop(new Item(QuestConstants.THIEVES_ARMBAND, 1));
					player.getDialogue().endDialogue();
					return false;
				}
			}//end of stage 5
			if(stage == 8){
				if(player.hasItem(QuestConstants.THIEVES_ARMBAND))
					return false;
				if(chatId == 1){
					player.getDialogue().sendStatement("Straven hands you a Master Thief armband.");
					return true;
				}
				if(chatId == 2){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.THIEVES_ARMBAND, 1));
					player.getDialogue().endDialogue();
					return false;
				}
			}//end of stage 8
		}//end of straven
		if(npcId == QuestConstants.KATRINE){
			if(player.gang != QuestConstants.BLACK_ARM_GANG)
				return false;
			if(stage == 2){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hey.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Hey.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendOption("Who are all those people in there?",
													"Is there any way I can get the rank of master thief?");
					return true;
				}
				if(chatId == 4){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Is there any way I can get the rank of master thief?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					} else {
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Master thief? Ain't we the ambitious one!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Well, you're gonna have to do something pretty",
													"amazing.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("Anything you can suggest?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Well, some of the MOST coveted prizes in thiefdom",
													"right now are in the pirate town of Brimhaven on",
													"Karamja.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("The pirate leader Scarface Pete has a pair of extremely",
													"valuable candlesticks.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("His security is VERY good.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("We, of course, have gang members in a town like",
													"Brimhaven who may be able to help you.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("Visit our hideout in the alleyway on palm street.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("To get in you will need to tell them the secret password",
													"'four leafed clover'.",Dialogues.CONTENT);
					player.questStage[this.getId()] = 3;
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 2
			if(stage == 7){
				if(!player.getInventory().playerHasItem(QuestConstants.PETES_CANDLESTICK))
					return false;
				if(chatId == 1){
					player.getDialogue().sendOption("Who are all those people in there?",
													"I have a candlestick now.");
					return true;
				}
				if(chatId == 2){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("I have a candlestick now!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(3);
						return true;
					} else {
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Wow.... is... it REALLY it?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("This really is a FINE bit of thievery.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Us thieves have been trying to get hold of this one for",
													"a while!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("You wanted to be ranked as a master thief didn't you?",
													"Well, I guess this just about ranks as good enough!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendStatement("Katrine gives you a master thief armband.");
					return true;
				}
				if(chatId == 8){
					player.questStage[this.getId()] = 8;
					player.getInventory().removeItem(new Item(QuestConstants.PETES_CANDLESTICK, 1));
					player.getInventory().addItemOrDrop(new Item(QuestConstants.THIEVES_ARMBAND, 1));
					player.getDialogue().endDialogue();
					return false;
				}
			}//end of stage 7
			if(stage == 8){
				if(player.hasItem(QuestConstants.THIEVES_ARMBAND))
					return false;
				if(chatId == 1){
					player.getDialogue().sendStatement("Katrine gives you a master thief armband.");
					return true;
				}
				if(chatId == 2){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.THIEVES_ARMBAND, 1));
					player.getDialogue().endDialogue();
					return false;
				}
			}//end of stage 8
		}//end of katrine
		if(npcId == QuestConstants.GRUBOR){
			if(player.gang != QuestConstants.BLACK_ARM_GANG)
				return false;
			if(stage == 3){
				if(chatId == 100){
					player.getDialogue().sendNpcChat("Yes? What do you want?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 101){
					player.getDialogue().sendOption("Rabbit's foot.",
													"Four leaved clover.",
													"Lucky horseshoe.",
													"Black cat.");
					return true;
				}
				if(chatId == 102){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Four leaved clover.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(103);
						return true;
					} else {
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 103){
					player.getDialogue().sendNpcChat("Oh, you're one of the gang are you? Ok, hold up a",
													"second, I'll just let you in through here.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 104){
					player.getDialogue().sendStatement("You hear the door being unbarred from inside.");
					player.questStage[this.getId()] = 4;
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 3
			if(stage != 3){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hi.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Hi. I'm a little busy right now.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of grubor
		if(npcId == QuestConstants.TROBERT){
			if(player.gang != QuestConstants.BLACK_ARM_GANG)
				return false;
			if(stage == 4){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hi. Welcome to our Brimhaven headquarters. I'm",
													"Trobert and I'm in charge here.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("So can you help me get Scarface Pete's candlesticks?",
													"Pleased to meet you.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("So can you help me get Scarface Pete's candlesticks?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					} else {
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Well, we have made some progress there. We know that",
													"one of the only keys to Pete's treasure room is carried",
													"by Grip, the head guard, so we thought it might be good",
													"to get close to him somehow.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Grip was taking on a new deputy called Hartigen, an",
													"Asgarnian Black Knight who was deserting the Black",
													"Knight Fortress and seeking new employment here on",
													"Brimhaven.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("We managed to waylay him on the journey here, and",
													"steal his I.D. papers. Now all we need is to find",
													"somebody willing to impersonate him and take the",
													"deputy role to get that key for us.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendOption("I volunteer to undertake that mission!",
													"Well, good luck then.");
					return true;
				}
				if(chatId == 8){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I volunteer to undertake that mission!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					} else {
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Good good. Well, Here's the I.D. papers, take them and",
													"introduce yourself to the guards at Scarface Pete's",
													"mansion, we'll have that treasure in no time.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.ID_PAPERS, 1));
					player.getDialogue().endDialogue();
					return false;
				}
			}//end of stage 4
		}//end of trobert
		if(npcId == QuestConstants.GARV){
			if(player.gang != QuestConstants.BLACK_ARM_GANG)
				return false;
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hello. What do you want?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Can I go in there?",
													"I want for nothing!");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Can I go in there?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("I want for nothing!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}
				}
				if(chatId == 4){
					if(stage <= 4)
						player.getDialogue().sendNpcChat("No. In there is private.", Dialogues.CONTENT);
					if(stage >= 5)
						player.getDialogue().sendNpcChat("Yes, of course you can. You DO work here.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("You're one of a very lucky few then.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("Can I go in there?", Dialogues.CONTENT);
					player.getDialogue().setNextChatId(4);
					return true;
				}
				if(chatId == 100){
					player.getDialogue().sendNpcChat("Oi! Where do you think you're going pal?", Dialogues.CONTENT);
					if(stage < 4)
						player.getDialogue().setNextChatId(6);
					return true;
				}
				if(chatId == 101){
					player.getDialogue().sendPlayerChat("Hi. I'm Hartigen. I've come to work here.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 102){
					if(!wearingBlackArmor(player)){
						player.getDialogue().sendNpcChat("Hartigen the Black Knight? I don't think so. He doesn't",
													"dress like that.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					} else {
						player.getDialogue().sendNpcChat("I assume you have your I.D. papers then?", Dialogues.CONTENT);
					}
					return true;
				}
				if(chatId == 103){
					if(!player.getInventory().playerHasItem(QuestConstants.ID_PAPERS)){
						player.getDialogue().sendPlayerChat("Actually I don't.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					} else {
						player.getDialogue().sendNpcChat("You'd better come in then. Grip will want to talk to",
														"you.", Dialogues.CONTENT);
						player.questStage[this.getId()] = 5;
					}
					return true;
				}
		}//end of garv
		if(npcId == QuestConstants.GRIP){
			if(player.gang != QuestConstants.BLACK_ARM_GANG)
				return false;
			if(stage == 5){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hi there. I am Hartigen. reporting for duty as your",
														"new deputy sir!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Ah good, at last. You took your time getting here! Now",
													"let me see...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("I'll get your hours and duty roster sorted out in a",
													"while. Oh, and do you have your I.D. papers with you?",
													"Internal security is almost as important as external",
													"security for a guard.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("Right here sir!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendStatement("You hand the ID papers over to Grip.");
					return true;
				}
				if(chatId == 6){
					if(player.getInventory().playerHasItem(QuestConstants.ID_PAPERS)){
						player.getDialogue().sendOption("So can I guard the treasure room please?",
														"So what do my duties involve?",
														"Well, I'd better sort my new room out.");
						player.getInventory().removeItem(new Item(QuestConstants.ID_PAPERS, 1));
						player.questStage[this.getId()] = 6;
						player.getDialogue().setNextChatId(10);
						return true;
					}
				}
			}//end of stage 5
			if(stage == 6){
				if(chatId == 1 && !player.hasItem(QuestConstants.MISCELLANEOUS_KEY)){
					player.getDialogue().sendNpcChat("You'll have various guard related duties on various",
													"shifts. I'll assign specific duties as they are reguired as",
													"and when they become necessary. Just so you know, if",
													"anything happens to me", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					if(optionId == 2){
						player.getDialogue().sendNpcChat("You'll have various guard related duties on various",
														"shifts. I'll assign specific duties as they are reguired as",
														"and when they become necessary. Just so you know, if",
														"anything happens to me", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(2);
						return true;
					} else {
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("you'll need to take over as head guard here. You'll find",
													"important keys to the treasure room and Pete's",
													"quarters inside my jacket - although I doubt anything",
													"bad's going to happen to", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("me anytime soon!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendStatement("Grip laughs to himself at the thought.");
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendOption("So can I guard the treasure room please?",
													"Well, I'd better sort my new room out.",
													"Anything I can do now?");
					return true;
				}
				if(chatId == 6){
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("Anything I can do now?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(7);
						return true;
					} else {
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Hmm. Well, you could find out what this key opens for",
													"me. Apparently it's for something in this building, but",
													"for the life of me I can't find what.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendStatement("Grip hands you a key.");
					return true;
				}
				if(chatId == 9){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.MISCELLANEOUS_KEY, 1));
					player.getDialogue().endDialogue();
					return false;
				}
			}//end of stage 6
		}//end of grip
		if(npcId == QuestConstants.ALFONSE_THE_WAITER){
			if(player.gang != QuestConstants.PHOENIX_GANG)
				return false;
			if(stage < 4){
				if(chatId == 100){
					player.getDialogue().sendNpcChat("Hey, you can't go in there!", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage == 3){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Welcome to the Shrimp and Parrot.",
													"Would you like to order, "+(player.getGender() == 0 ? "sir" : "madam")+"?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Yes please.",
													"No thank you.",
													"Do you sell Gherkins?",
													"Where do you get your Karambwan from?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("Do you sell Gherkins?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					} else {
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Hmmmm Gherkins eh? Ask Charlie the cook, round the",
													"back. He may have some 'gherkins' for you!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendStatement("Alfonse winks at you.");
					player.questStage[this.getId()] = 4;
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 3
		}//end of alfonse the waiter
		if(npcId == QuestConstants.CHARLIE_THE_COOK){
			if(player.gang != QuestConstants.PHOENIX_GANG)
				return false;
			if(stage == 4){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hey! What are you doing back here?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("I'm looking for a gherkin...",
													"I'm a fellow member of the Phoenix gang.",
													"Just exploring...");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I'm looking for a gherkin...", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					} else {
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Aaaaaah... a fellow Phoenix! So, tell me compadre... what",
													"brings you to sunny Brimhaven?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendOption("Sun, sand and the fresh sea air!",
													"I want to steal Scarface Pete's candlesticks.");
					return true;
				}
				if(chatId == 6){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("I want to steal Scarface Pete's candlesticks.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(7);
						return true;
					} else {
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Ah yes, of course. The candlesticks. Well, I have to be",
													"honest with you compadre, we haven't made much",
													"progress in that task ourselves so far. We can however",
													"offer", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("a little assistance. The setting up of this restaurant was",
													"the start of things; we have a secret door out the back",
													"of here that leads through the back of Mr Olbors'",
													"garden.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Now, at the other side of Mr Olbors' garden, is an old",
													"side entrance to Scarface Pete's mansion. It seems to",
													"have been blocked off from the rest of the mansion",
													"some years ago", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("and we can't seem to find a way through. We're",
													"positive this is the key to entering the house undetected",
													"however, and I promise to let you know if we find",
													"anything there.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendPlayerChat("Mind if I check it out for myself?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("Not at all! The more minds we have working on the",
													"problem, the quicker we get that loot!", Dialogues.CONTENT);
					player.questStage[this.getId()] = 5;
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 4
		}//end of charlie the cook
		if(npcId == QuestConstants.PIRATE_GUARD){
			if(chatId == 100){
				player.getDialogue().sendNpcChat("I don't think Mr Grip will like you opening that. That's",
												"his private drinks cabinet.", Dialogues.CONTENT);
				return true;
			}
			if(chatId == 101){
				player.getDialogue().sendOption("He won't notice me having a quick look.",
												"Ok, I'll leave it.");
				return true;
			}
			if(chatId == 102){
				if(optionId == 1){
					player.getDialogue().sendPlayerChat("He won't notice me having a quick look.", Dialogues.CONTENT);
					player.getDialogue().setNextChatId(103);
					return true;
				} else {
					player.getDialogue().endDialogue();
					return false;
				}
			}
			if(chatId == 103){
				Npc npc = Npc.getNpcById(QuestConstants.GRIP);
				if(npc != null){
					if(!npc.isDead()){
						npc.getUpdateFlags().setForceChatMessage("Stay out of my drinks cabinet!");
						if(npc.getPosition().getY() < 3196)
							npc.forceMovePath(new Position[] {new Position(2777,3194,0), new Position(2777,3198,0)});
						else
							npc.forceMovePath(new Position[] {new Position(2777,3198,0)});
					}
				}
				player.getDialogue().endDialogue();
				return false;
			}
		}//end of pirate guard
		return false;
	}

}
