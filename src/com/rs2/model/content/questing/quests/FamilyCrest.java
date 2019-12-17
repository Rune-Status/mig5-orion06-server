package com.rs2.model.content.questing.quests;

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
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class FamilyCrest extends Quest {
	
	final int rewardQP = 1;
	
	public FamilyCrest(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			int mineLvl = player.getSkill().getPlayerLevel(Skill.MINING);
			int craftLvl = player.getSkill().getPlayerLevel(Skill.CRAFTING);
			int smithLvl = player.getSkill().getPlayerLevel(Skill.SMITHING);
			int mageLvl = player.getSkill().getPlayerLevel(Skill.MAGIC);
			String text[] = {"I can start this quest by speaking to Dimintheis in East",
							"Varrock",
							"To complete this quest I need:",
							(mineLvl >= 40 ? "@str@" : "")+"Level 40 Mining",
							(craftLvl >= 40 ? "@str@" : "")+"Level 40 Crafting",
							(smithLvl >= 40 ? "@str@" : "")+"Level 40 Smithing",
							(mageLvl >= 59 ? "@str@" : "")+"Level 59 Magic",
							"I also need to be able to defeat a level 170 Demon"};
			return text;
		}
		if(stage == 2){
			String text[] = {"@str@I have agreed to restore Dimintheis' family crest to him.",
							"I need to find his son Caleb. The last Dimintheis heard of",
							"him he was a great chef beyond White Wolf Mountain"};
			return text;	
		}
		if(stage == 3){
			String text[] = {"I should bring the following cooked fish to Caleb:",
							"Swordfish",
							"Bass",
							"Tuna",
							"Salmon",
							"Shrimps"};
			return text;	
		}
		if(stage == 4){
			String text[] = {"I have one crest piece, I need to find the other two"};
			return text;	
		}
		if(stage == 5){
			String text[] = {"Caleb told me his brother Avan is on a treasure hunt in the",
							"desert somewhere - I should search for him there"};
			return text;	
		}
		if(stage == 6){
			String text[] = {"The Al-Kharid Gem Trader told me that Avan is wearing a",
							"yellow cape, and headed into the desert by the scorpions"};
			return text;	
		}
		if(stage == 7){
			String text[] = {"I should find a dwarf named Boot who lives somewhere in",
							"the mountains and knows where to find 'Perfect Gold'"};
			return text;	
		}
		if(stage == 8){
			String text[] = {"Avan will give me his crest piece if I can bring him a ruby",
							"ring and ruby necklace made from 'perfect gold'",
							"Boot told me 'perfect gold' can be found underground",
							"somewhere East of Ardougne, but is difficult to get to",
							"I need to make a ruby ring made of 'perfect gold'",
							"I need to make a ruby necklace made of 'perfect gold'"};
			return text;	
		}
		if(stage == 9){
			String text[] = {"I should ask Avan if he knows where to find his brother."};
			return text;	
		}
		if(stage == 10){
			String text[] = {"Avan told me I could possibly find Johnathon in a tavern",
							"near the edge of Wilderness."};
			return text;	
		}
		if(stage == 11){
			String text[] = {"I should try giving antipoison to Johnathon"};
			return text;	
		}
		if(stage == 12){
			String text[] = {"Johnathon is cured, I should now ask him for crest piece"};
			return text;	
		}
		if(stage == 13){
			if(!player.hasItem(QuestConstants.CREST_PART_CHRONOZON) && !player.hasItem(QuestConstants.FAMILY_CREST)){
				String text[] = {"I can find Chronozon somewhere below Obelisk of Air"};
				return text;
			}else{
				String text[] = {"I should now bring the family crest back to Dimintheis"};
				return text;
			}
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "1 Quest Point", "Steel Gauntlets", "A skill imbue for the gauntlets"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("Steel Gauntlets", 12151);
		player.getActionSender().sendString("A skill imbue for the gauntlets", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getInventory().addItemOrDrop(new Item(QuestConstants.STEEL_GAUNTLETS, 1));
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.STEEL_GAUNTLETS);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 2425 && objectX == 2722 && objectY == 9718){//Lever 1
			if((player.questStageSub1[this.getId()] & Misc.getActualValue(1)) != 0){
				player.questStageSub1[this.getId()] -= Misc.getActualValue(1);
				player.getActionSender().sendMessage("The lever is now down.");
			}else{
				player.questStageSub1[this.getId()] += Misc.getActualValue(1);
				player.getActionSender().sendMessage("The lever is now up.");
			}
			player.getActionSender().sendSound(319, 1, 0);
			player.getUpdateFlags().sendAnimation(835);
			return true;
		}
		if(objectId == 2421 && objectX == 2722 && objectY == 9710){//Lever 2
			if(stage > 1 && stage < 8){
				player.getActionSender().sendMessage("You have no reason to do that.");
				return true;
			}
			if((player.questStageSub1[this.getId()] & Misc.getActualValue(2)) != 0){
				player.questStageSub1[this.getId()] -= Misc.getActualValue(2);
				player.getActionSender().sendMessage("The lever is now down.");
			}else{
				player.questStageSub1[this.getId()] += Misc.getActualValue(2);
				player.getActionSender().sendMessage("The lever is now up.");
			}
			player.getActionSender().sendSound(319, 1, 0);
			player.getUpdateFlags().sendAnimation(835);
			return true;
		}
		if(objectId == 2423 && objectX == 2724 && objectY == 9669){//Lever 3
			if((player.questStageSub1[this.getId()] & Misc.getActualValue(3)) != 0){
				player.questStageSub1[this.getId()] -= Misc.getActualValue(3);
				player.getActionSender().sendMessage("The lever is now down.");
			}else{
				player.questStageSub1[this.getId()] += Misc.getActualValue(3);
				player.getActionSender().sendMessage("The lever is now up.");
			}
			player.getActionSender().sendSound(319, 1, 0);
			player.getUpdateFlags().sendAnimation(835);
			return true;
		}
		if(objectId == 2431 && objectX == 2723 && objectY == 9711){//door #1
			if((player.questStageSub1[this.getId()] & Misc.getActualValue(2)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(3)) != 0){
				player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
				player.getActionSender().walkTo(0, player.getPosition().getY() < 9711 ? 1 : -1, true);	
			}else{
				player.getActionSender().sendMessage("This door is locked.");
			}
			return true;
		}
		if(objectId == 2430 && objectX == 2727 && objectY == 9690){//door #2
			if((player.questStageSub1[this.getId()] & Misc.getActualValue(1)) != 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(2)) != 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(3)) == 0){
				player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
				player.getActionSender().walkTo(player.getPosition().getX() < 2728 ? 1 : -1, 0, true);	
			}else{
				player.getActionSender().sendMessage("This door is locked.");
			}
			return true;
		}
		if(objectId == 2427 && objectX == 2719 && objectY == 9671){//door #3w
			if((player.questStageSub1[this.getId()] & Misc.getActualValue(2)) != 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(3)) != 0){
				player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
				player.getActionSender().walkTo(0, player.getPosition().getY() < 9672 ? 1 : -1, true);	
			}else{
				player.getActionSender().sendMessage("This door is locked.");
			}
			return true;
		}
		if(objectId == 2429 && objectX == 2722 && objectY == 9671){//door #3e
			if((player.questStageSub1[this.getId()] & Misc.getActualValue(2)) != 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(3)) == 0){
				player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
				player.getActionSender().walkTo(0, player.getPosition().getY() < 9672 ? 1 : -1, true);	
			}else{
				player.getActionSender().sendMessage("This door is locked.");
			}
			return true;
		}
		return false;
	}
	
	public boolean handleItemOnItem(final Player player, int item1, int item2, int stage){
		if(hasAllCrestParts(player)){
			if(isCrestPart(item1) && isCrestPart(item2)){
				player.getInventory().removeItem(new Item(QuestConstants.CREST_PART_AVAN, 1));
	    		player.getInventory().removeItem(new Item(QuestConstants.CREST_PART_CALEB, 1));
	    		player.getInventory().removeItem(new Item(QuestConstants.CREST_PART_CHRONOZON, 1));
	    		player.getInventory().addItem(new Item(QuestConstants.FAMILY_CREST, 1));
	    		player.getActionSender().sendMessage("You have restored the Family Crest.");
				return true;
			}
		}
		return false;
	}
	
	public boolean handleItemOnNpc(final Player player, int npcId, int itemId, int stage){
		if(stage == 11){
			if(isAntiPoison(itemId) && npcId == QuestConstants.JOHNATHON){
				player.getInventory().removeItem(new Item(itemId, 1));
				player.questStage[this.getId()] = 12;
				Dialogues.sendDialogue(player, QuestConstants.JOHNATHON, 100, 0);
				return true;
			}
		}
		return false;
	}
	
	boolean isAntiPoison(int id){
		switch(id){
			case 2446 : // Antipoison
			case 175 :
			case 177 :
			case 179 :
			case 2448 : // Super antipoison
			case 181 :
			case 183 :
			case 185 :
			case 5943 : // Antipoison+
			case 5945 :
			case 5947 :
			case 5949 :
			case 5952 : // Antipoison++
			case 5954 :
			case 5956 :
			case 5958 :
				return true;
		}
		return false;
	}
	
	boolean isCrestPart(int id){
		if(id == QuestConstants.CREST_PART_AVAN || id == QuestConstants.CREST_PART_CALEB || id == QuestConstants.CREST_PART_CHRONOZON)
			return true;
		return false;
	}
	
	boolean hasAllCrestParts(final Player player){
		if(player.getInventory().playerHasItem(QuestConstants.CREST_PART_AVAN) && player.getInventory().playerHasItem(QuestConstants.CREST_PART_CALEB) && player.getInventory().playerHasItem(QuestConstants.CREST_PART_CHRONOZON))
			return true;
		return false;
	}
	
	public boolean getQuestDrop(final Player killer, int npcId, Position deathPos, int stage){
		if(npcId == QuestConstants.CHRONOZON){
			if(stage == 13 && !killer.hasItem(QuestConstants.CREST_PART_CHRONOZON) && !killer.hasItem(QuestConstants.FAMILY_CREST)){
				GroundItem drop = new GroundItem(new Item(QuestConstants.CREST_PART_CHRONOZON, 1), killer, deathPos);
		        GroundItemManager.getManager().dropItem(drop);
		        return true;
			}
		}
		return false;
	}
	
	public boolean controlDying(final Entity attacker, final Entity victim, int stage){
		if(victim.isNpc() && attacker.isPlayer()){
        	Player player = (Player) attacker;
        	Npc npc = (Npc) victim;
        	if(npc.getNpcId() == QuestConstants.CHRONOZON){
        		if(!npc.hitByAirBlast || !npc.hitByWaterBlast || !npc.hitByEarthBlast || !npc.hitByFireBlast){
        			npc.setCurrentHp(npc.getMaxHp());
        			npc.hitByAirBlast = false;
        			npc.hitByWaterBlast = false;
        			npc.hitByEarthBlast = false;
        			npc.hitByFireBlast = false;
        			player.getActionSender().sendMessage("Chronozon regenerates.");
        		}else{
        			npc.setDead(true);
        			npc.hitByAirBlast = false;
        			npc.hitByWaterBlast = false;
        			npc.hitByEarthBlast = false;
        			npc.hitByFireBlast = false;
					CombatManager.startDeath(npc);
        		}
        		return true;
        	}
        }
		return false;
	}
	
	boolean gauntletsNotLost(final Player player){
		if(player.hasItem(QuestConstants.STEEL_GAUNTLETS) || player.hasItem(QuestConstants.COOKING_GAUNTLETS) || player.hasItem(QuestConstants.GOLDSMITH_GAUNTLET) || player.hasItem(QuestConstants.CHAOS_GAUNTLETS)){
			return true;
		}
		return false;
	}
	
	boolean playerHasGauntlets(final Player player){
		if(player.getInventory().playerHasItem(QuestConstants.STEEL_GAUNTLETS) || player.getInventory().playerHasItem(QuestConstants.COOKING_GAUNTLETS) || player.getInventory().playerHasItem(QuestConstants.GOLDSMITH_GAUNTLET) || player.getInventory().playerHasItem(QuestConstants.CHAOS_GAUNTLETS)){
			return true;
		}
		return false;
	}
	
	boolean playerHasAtleastOneItem(final Player player){
		if(!player.getInventory().playerHasItem(QuestConstants.SWORDFISH) && !player.getInventory().playerHasItem(QuestConstants.BASS) &&
				!player.getInventory().playerHasItem(QuestConstants.TUNA) && !player.getInventory().playerHasItem(QuestConstants.SALMON) && !player.getInventory().playerHasItem(QuestConstants.SHRIMPS)){
			return false;
		}
		return true;
	}
	
	boolean playerHasAllItems(final Player player){
		if(player.getInventory().playerHasItem(QuestConstants.SWORDFISH) && player.getInventory().playerHasItem(QuestConstants.BASS) &&
				player.getInventory().playerHasItem(QuestConstants.TUNA) && player.getInventory().playerHasItem(QuestConstants.SALMON) && player.getInventory().playerHasItem(QuestConstants.SHRIMPS)){
			return true;
		}
		return false;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.DIMINTHEIS){
			if(stage == 1){
				if(gauntletsNotLost(player))
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I lost my gauntlets.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Here you go.",Dialogues.CONTENT);
					player.getInventory().addItemOrDrop(new Item(player.familyGauntlets, 1));
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 1
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hello. My name is Dimintheis, of the noble family",
													"Fitzharmon.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Why would a nobleman live in a dump like this?",
													"You're rich then? Can I have some money?",
													"Hi, I am a bold adventurer.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Why would a nobleman live in a dump like this?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(19);
						return true;
					}
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("Hi, I am a bold adventurer.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("An adventurer hmmm? How lucky. I may have an",
													"adventure for you. I desperately need my family crest",
													"returning to me. It is of utmost importance.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendOption("Why are you so desperate for it?",
													"So where is this crest?",
													"I'm not interested in that adventure right now.");
					return true;
				}
				if(chatId == 6){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Why are you so desperate for it?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(7);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("So where is this crest?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(13);
						return true;
					}
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("I'm not interested in that adventure right now.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Well, there is a long standing rule of chivalry amongst",
													"the Varrockian aristocracy, where each noble family is",
													"in",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("possession of a unique crest, which signifies the honour",
													"and lineage of the family. More than this however, it",
													"also represents the lawful rights of each family",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("to prove their ownership of their wealth and lands. If",
													"the family crest is lost, then the family's estate is handed",
													"over to the current monarch until the crest is restored.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("This dates back to the times when there was much in-",
													"fighting amongst the noble families and their clans, and",
													"was introduced as a way of reducing the",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("bloodshed that was devestating the ranks of the ruling",
													"classes at that time. When you captured a rival family's",
													"clan, you also captured their lands and wealth.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendPlayerChat("So where is this crest?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("Well, my three sons took it with them many years ago",
													"when they rode out to fight in the war against the",
													"undead necromancer and his army in",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("the battle to save Varrock. For many years I had",
													"assumed them all dead, as I had heard no word from",
													"them, but recently I heard word that my son",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendNpcChat("Caleb is alive and well, and trying to earn his fortune",
													"as a great chef in the lands beyond White Wolf",
													"Mountain, although I know not where.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendOption("Ok, I will help you.",
													"I'm not interested in that adventure right now.");
					return true;
				}
				if(chatId == 17){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Ok, I will help you.", Dialogues.CONTENT);
						questStarted(player);
						player.getDialogue().setNextChatId(18);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 19){
					player.getDialogue().sendNpcChat("The King has taken my estate from me until such time",
													"as I can show my family crest to him.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 20){
					player.getDialogue().sendOption("Why would he do that?",
													"So where is this crest?");
					return true;
				}
				if(chatId == 21){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Why would he do that?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(7);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("So where is this crest?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(13);
						return true;
					}
				}
			}//end of stage 0
			if(stage == 2){
				if(chatId == 18){
					player.getDialogue().sendNpcChat("I thank you greatly adventurer! Also... if you find",
													"Caleb... please... let him know his father still loves him...",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 2
			if(stage == 13){
				if(chatId == 1){
					if(player.getInventory().playerHasItem(QuestConstants.FAMILY_CREST)){
						player.getDialogue().sendPlayerChat("I have retrieved your crest.", Dialogues.CONTENT);
						return true;
					}
				}
				if(chatId == 2){
					if(player.getInventory().playerHasItem(QuestConstants.FAMILY_CREST)){
						player.getInventory().removeItem(new Item(QuestConstants.FAMILY_CREST, 1));
						player.questStage[this.getId()] = 14;
						player.getDialogue().sendNpcChat("Adventurer... I can only thank you for your kindness,",
														"although the words are insufficient to express the",
														"gratitude I feel!",Dialogues.CONTENT);
						player.getDialogue().setNextChatId(1);
						return true;
					}
				}
			}//end of stage 13
			if(stage == 14){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("You are truly a hero in every sense, and perhaps your",
													"efforts can begin to patch the wounds that have torn",
													"this family apart...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("I know not how I can adequately reward you for your",
													"efforts... although",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("I do have these mystical gauntlets, a family heirloom",
													"that through some power unknown to me, have always",
													"returned to the head of the family",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("whenever lost, or if the owner has died. I will pledge",
													"these to you, and if you should lose them return to me,",
													"and they will be here.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("They can also be granted extra powers. Take them to",
													"one of my sons, they should be able to imbue them with",
													"a skill for you.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().endDialogue();
					questCompleted(player);
					return true;
				}
			}//end of stage 14
		}//end of dimintheis
		if(npcId == QuestConstants.CALEB){
			if(stage == 1 && playerHasGauntlets(player) && player.familyGauntlets != QuestConstants.COOKING_GAUNTLETS){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("I hear you have brought the completed crest to my",
													"father. I must say, That was awfully impressive work.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("I believe your father mentioned you would be able to",
														"improve these gauntlets for me...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Yes, that is correct. I can slightly alter these gauntlets",
													"to allow you some of my skill at preparing seafood by",
													"making them cooking gauntlets.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("When you wear them you will burn lobsters, sharks and",
													"swordfish a lot less. It will be a permanent modification",
													"however, so make sure this is what you want.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					if(player.familyGauntlets != QuestConstants.STEEL_GAUNTLETS){
						player.getDialogue().sendOption("Can you change my gauntlets for me?",
														"No thanks.");
					} else {
						player.getDialogue().sendOption("Yes, please do that for me.",
														"I'll see what your brothers have to offer first.");
					}
					return true;
				}
				if(chatId == 6){
					if(optionId == 1){
						if(player.familyGauntlets != QuestConstants.STEEL_GAUNTLETS){
							player.getDialogue().sendPlayerChat("Can you change my gauntlets for me?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(8);
						} else {
							player.getDialogue().sendPlayerChat("Yes, please do that for me.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(7);
						}
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 7){
					if(player.familyGauntlets != QuestConstants.STEEL_GAUNTLETS)
						player.getInventory().removeItem(new Item(995, 25000));
					player.getDialogue().sendItem3Lines("Caleb takes the gauntlets from you, pours some herbs",
														"and seasonings on them, bakes them on his range for a",
														"short time, then hands them back to you.", new Item(QuestConstants.COOKING_GAUNTLETS, 1));
					player.getInventory().removeItem(new Item(player.familyGauntlets, 1));
					player.familyGauntlets = QuestConstants.COOKING_GAUNTLETS;
					player.getInventory().addItemOrDrop(new Item(player.familyGauntlets, 1));
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Yes certainly, though it will cost you 25,000 coins?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendOption("Great thanks!",
													"No that's ok thanks.");
					return true;
				}
				if(chatId == 10){
					if(optionId == 1 && player.getInventory().playerHasItem(995, 25000)){
						player.getDialogue().sendPlayerChat("Great thanks!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(7);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}//end of stage 1
			if(stage == 2){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Who are you? What are you after?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Are you Caleb Fitzharmon?",
													"Nothing, I will be on my way.",
													"I see you are a chef, will you cook me anything?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Are you Caleb Fitzharmon?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Why... yes I am, but I don't believe I know you... how",
													"did you know my name?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("I have been sent by your father. He wishes the",
														"Fitzharmon Crest to be restored.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Ah... well... hmmm... yes... I do have a piece of it",
													"anyway...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendOption("Uh... what happened to the rest of it?",
													"So can I have your bit?");
					return true;
				}
				if(chatId == 8){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Uh... what happened to the rest of it?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(100);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("So can I have your bit?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(12);
						return true;
					}
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("Well, I am the oldest son, so by the rules of chivalry, I",
													"am most entitled to be rightful bearer of the crest.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendPlayerChat("It's not really much use without the other fragments is",
														"it though?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("Well that is true... perhaps it is time to put my pride",
													"aside... I'll tell you what: I'm struggling to complete this",
													"fish salad of mine,",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendNpcChat("so if you will assist me in my search for the",
													"ingredients, then I will let you take my piece as reward",
													"for your assistance.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendPlayerChat("So what ingredients are you missing?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendNpcChat("I require the following cooked fish: Swordfish, Bass,",
													"Tuna, Salmon and Shrimp.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendOption("Ok, I will get those.",
													"Why don't you just give me the crest?");
					return true;
				}
				if(chatId == 19){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Ok, I will get those.", Dialogues.CONTENT);
						player.questStage[this.getId()] = 3;
						player.getDialogue().setNextChatId(20);
						return true;
					}else{
						player.getDialogue().sendStatement("This option is currently missing...");
						player.getDialogue().setNextChatId(18);
						return true;
					}
				}
			}//end of stage 2
			if(stage == 3){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("How is the fish collecting going?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					if(!playerHasAtleastOneItem(player)){
						player.getDialogue().sendPlayerChat("I have none of them yet.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					}
					if(playerHasAtleastOneItem(player) && !playerHasAllItems(player)){
						player.getDialogue().sendPlayerChat("I don't have all of them yet.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					}
					if(playerHasAllItems(player)){
						player.getDialogue().sendPlayerChat("Got them all with me.", Dialogues.CONTENT);
					}
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendStatement("You exchange the fish for Caleb's piece of the crest.");
					return true;
				}
				if(chatId == 4){
					if(playerHasAllItems(player)){
						player.getInventory().removeItem(new Item(QuestConstants.SWORDFISH, 1));
						player.getInventory().removeItem(new Item(QuestConstants.BASS, 1));
						player.getInventory().removeItem(new Item(QuestConstants.TUNA, 1));
						player.getInventory().removeItem(new Item(QuestConstants.SALMON, 1));
						player.getInventory().removeItem(new Item(QuestConstants.SHRIMPS, 1));
						player.getInventory().addItemOrDrop(new Item(QuestConstants.CREST_PART_CALEB, 1));
						player.questStage[this.getId()] = 4;
						player.getDialogue().sendOption("Uh... what happened to the rest of it?",
														"Thank you very much!");
						return true;
					}
				}
				if(chatId == 20){
					player.getDialogue().sendNpcChat("You will? It would help me a lot!",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 3
			if(stage >= 4){
				if(!player.hasItem(QuestConstants.CREST_PART_CALEB) && !player.hasItem(QuestConstants.FAMILY_CREST)){
					if(chatId == 1){
						player.getInventory().addItemOrDrop(new Item(QuestConstants.CREST_PART_CALEB, 1));
						player.getDialogue().sendNpcChat("Don't lose it this time!",Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}
				}
			}
			if(stage == 4){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hello again. I'm just putting the finishing touches to my",
													"masterful salad.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Uh.. what happened to the rest of the crest?",
													"Good luck with that then.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Uh... what happened to the rest of it?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(100);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 5){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Uh... what happened to the rest of it?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(100);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Thank you very much.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(6);
						return true;
					}
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("You're welcome.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 4
			if(stage >= 2 && stage <= 4){
				if(chatId == 100){
					player.getDialogue().sendNpcChat("Well... my brothers and I had a slight disagreement",
													"about it... we all wanted to be heir to my fathers' lands,",
													"and we each ended up with a piece of the crest.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 101){
					player.getDialogue().sendNpcChat("None of us wanted to give up our rights to our",
													"brothers, so we didn't want to give up our pieces of the",
													"crest, but none of us wanted to face our father by",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 102){
					player.getDialogue().sendNpcChat("returning to him with an incomplete crest... We each",
													"went our seperate ways many years past, none of us",
													"seeing our father or willing to give up our fragments.",Dialogues.CONTENT);
					if(stage < 4)
						player.getDialogue().endDialogue();
					return true;
				}
				
				if(chatId == 103){
					player.getDialogue().sendPlayerChat("So do you know where I could find any of your",
														"brothers?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 104){
					player.getDialogue().sendNpcChat("Well, we haven't really kept in touch... what with the",
													"dispute over the crest and all... I did hear from my",
													"brother Avan a while ago though..",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 105){
					player.getDialogue().sendNpcChat("He said he was on some kind of search for treasure, or",
													"gold, or something out in the desert. You might want to",
													"ask around there for him.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 106){
					player.getDialogue().sendNpcChat("Avan always did have expensive tastes however. You",
													"may find he is not prepared to hand over his crest",
													"piece to you as easily as I have.",Dialogues.CONTENT);
					player.questStage[this.getId()] = 5;
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stages 2-4
		}//end of caleb
		if(npcId == QuestConstants.GEM_TRADER){
			if(stage == 5){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Good day to you traveller. Would you be interested in",
													"buying some gems?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Yes please.",
													"No thank you.",
													"I'm in search of a man named Avan Fitzharmon.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("I'm in search of a man named Avan Fitzharmon.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().sendStatement("This option is currently missing...");
						player.getDialogue().setNextChatId(2);
						return true;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Fitzharmon eh? Hmmm.... if I'm not mistaken that's the",
													"family name of a member of the Varrockian nobility.",
													"You know, I HAVE seen someone of that",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("persuasion around here recently... Wearing a poncey",
													"yellow cape he was! Came in here all la-di-dah, high and",
													"mighty, asking for jewelry made",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("from 'perfect gold' - whatever that is - like 'normal'",
													"golds just not good enough for little lord fancy pants",
													"there! I told him to head to the desert",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("'Cos I know theres gold out there, in them there sand",
													"dunes... and if it's not up to his lordships high standards",
													"of 'gold perfection'...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Maybe we'll all get lucky and the scorpions will deal with",
													"him.",Dialogues.CONTENT);
					player.questStage[this.getId()] = 6;
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 5
		}//end of gem trader
		if(npcId == QuestConstants.AVAN_FITZHARMON){
			if(stage == 1 && playerHasGauntlets(player) && player.familyGauntlets != QuestConstants.GOLDSMITH_GAUNTLET){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("I have received word from my father of your assistance",
													"to our family in this matter. You have my thanks for",
													"restoring our honour.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("Not a problem.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Yes well... Is there anything I can help you with?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					if(player.familyGauntlets != QuestConstants.STEEL_GAUNTLETS)
						player.getDialogue().sendOption("Can you change my gauntlets for me?",
														"No thanks.");
					else
						player.getDialogue().sendOption("Enchant my gauntlets please.",
														"I'll see what your brothers have to offer first.");
					return true;
				}
				if(chatId == 5){
					if(optionId == 1){
						if(player.familyGauntlets != QuestConstants.STEEL_GAUNTLETS){
							player.getDialogue().sendPlayerChat("Can you change my gauntlets for me?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(6);
						}else{
							player.getDialogue().sendPlayerChat("Enchant my gauntlets please.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(9);
						}
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Yes certainly, though it will cost you 25,000 coins?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendOption("Great thanks!",
													"No that's ok thanks.");
					return true;
				}
				if(chatId == 8){
					if(optionId == 1 && player.getInventory().playerHasItem(995, 25000)){
						player.getDialogue().sendPlayerChat("Great thanks!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 9){
					if(player.familyGauntlets != QuestConstants.STEEL_GAUNTLETS)
						player.getInventory().removeItem(new Item(995, 25000));
					player.getDialogue().sendStatement("Avan takes your gauntlets, takes out a small hammer, and pounds",
													"them into a slightly new shape, then hands them back to you.");
					player.getInventory().removeItem(new Item(player.familyGauntlets, 1));
					player.familyGauntlets = QuestConstants.GOLDSMITH_GAUNTLET;
					player.getInventory().addItemOrDrop(new Item(player.familyGauntlets, 1));
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 1
			if(stage == 6){
				if(chatId == 1){
					player.getDialogue().sendOption("Why are you hanging around in a scorpion pit?",
													"I'm looking for a man named Avan Fitzharmon.");
					return true;
				}
				if(chatId == 2){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("I'm looking for a man... his name is Avan Fitzharmon.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(3);
						return true;
					}else{
						player.getDialogue().sendStatement("This option is currently missing...");
						player.getDialogue().setNextChatId(1);
						return true;
					}
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Then you have found him. My name is Avan",
													"Fitzharmon.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("You have a part of your family crest. I am on a quest",
														"to retrieve all of the fragmented pieces and restore the",
														"crest.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Ha! I suppose one of my worthless brothers has sent",
													"you on this quest then?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("No, it was your father who has asked me to do this for",
														"him.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("My... my father wishes this? Then that is a different",
													"matter. I will let you have my crest shard, adventurer,",
													"but you must first do something for me.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("There is a certain lady I am trying to impress. As a",
													"man of noble birth, I can not give her just any old",
													"trinket to show my devotion. What I intend to give her,",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("is a golden ring, embedded with the finest precious red",
													"stone available, and a necklace to match this ring. The",
													"problem however for me, is that",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("not just any gold will be suitable. I seek only the",
													"purest, the most high quality of gold - what I seek, if",
													"you will, is perfect gold.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("None of the gold around here is even remotely suitable",
													"in terms of quality. I have searched far and wide for",
													"the perfect gold I desire, but have had no success so",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("in finding it I am afraid. If you can find me my",
													"perfect gold, make a ring and necklace from it, and add",
													"rubies to them, I will",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("gladly hand over my fragment of the family crest to",
													"you.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendPlayerChat("Can you give me any help on finding this 'perfect gold'?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendNpcChat("I thought I had found a solid lead on its whereabouts",
													"when I heard of a dwarf who is an expert on gold who",
													"goes by the name of 'Boot'.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("Unfortunately he has apparently returned to his home,",
													"somewhere in the mountains, and I have no idea how to",
													"find him.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendPlayerChat("Well, I'll see what I can do.", Dialogues.CONTENT);
					player.questStage[this.getId()] = 7;
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 6
			if(stage >= 7 && stage < 9){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("So how are you doing getting me my perfect gold",
													"jewelry?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					if(player.getInventory().playerHasItem(QuestConstants.PERFECT_NECKLACE) && player.getInventory().playerHasItem(QuestConstants.PERFECT_RING))
						player.getDialogue().sendPlayerChat("I have the ring and necklace right here.", Dialogues.CONTENT);
					else {
						player.getDialogue().sendPlayerChat("Still working on getting them.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					}
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendStatement("You hand Avan the perfect gold ring and necklace.");
					return true;
				}
				if(chatId == 4){
					if(player.getInventory().playerHasItem(QuestConstants.PERFECT_NECKLACE) && player.getInventory().playerHasItem(QuestConstants.PERFECT_RING)){
						player.getInventory().removeItem(new Item(QuestConstants.PERFECT_RING, 1));
						player.getInventory().removeItem(new Item(QuestConstants.PERFECT_NECKLACE, 1));
						player.getInventory().addItemOrDrop(new Item(QuestConstants.CREST_PART_AVAN, 1));
						player.getDialogue().sendNpcChat("These... these are exquisite! EXACTLY what I was",
														"searching for all of this time! Please, take my crest",
														"fragment!",Dialogues.CONTENT);
						player.getDialogue().setNextChatId(1);
						player.questStage[this.getId()] = 9;
						return true;
					}
				}
			}//end of stage 7
			if(stage >= 9){
				if(!player.hasItem(QuestConstants.CREST_PART_AVAN) && !player.hasItem(QuestConstants.FAMILY_CREST)){
					if(chatId == 1){
						player.getInventory().addItemOrDrop(new Item(QuestConstants.CREST_PART_AVAN, 1));
						player.getDialogue().sendNpcChat("Don't lose it this time!",Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}
				}
			}
			if(stage == 9 || stage == 10){
				if(chatId == 1){
					if(stage == 9)
						player.getDialogue().sendNpcChat("Now, I suppose you will be wanting to find my brother",
														"Johnathon who is in possession of the final piece of the",
														"family's crest?",Dialogues.CONTENT);
					else{
						player.getDialogue().sendPlayerChat("Where did you say I could find your brother",
															"Johnathon again?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(3);
					}
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("That's correct.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Well, the last I heard of my brother Johnathon, he was",
													"studying the magical arts, and trying to hunt some",
													"demon or other out in The Wilderness.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Unsurprisingly, I do not believe he is doing a",
													"particularly good job of things, and spends most of his",
													"time recovering from his injuries in",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("some tavern or other near the edge of The Wilderness.",
													"You'll probably find him still there.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("Thanks Avan.", Dialogues.CONTENT);
					if(stage == 9)
						player.questStage[this.getId()] = 10;
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 9,10
		}//end of avan fitzharmon
		if(npcId == QuestConstants.BOOT){
			if(stage >= 7){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hello tall person.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Hello. I'm in search of very high quality gold.",
													"Hello short person.",
													"Why are you called boot?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Hello. I'm in search of very high quality gold.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().sendStatement("This option is currently missing...");
						player.getDialogue().setNextChatId(2);
						return true;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("High quality gold eh? Hmmm... Well, the very best",
													"quality gold that I know of, is just East of the large",
													"city Ardougne, underground somewhere.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("I don't believe it's exactly easy to get to though...",Dialogues.CONTENT);
					if(stage == 7)
						player.questStage[this.getId()] = 8;
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 7
		}//end of boot
		if(npcId == QuestConstants.JOHNATHON){
			if(stage == 1 && playerHasGauntlets(player) && player.familyGauntlets != QuestConstants.CHAOS_GAUNTLETS){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hello again.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("Your father tells me you can improve these gauntlets",
														"for me...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("And right he is at that. Although my skill with Death",
													"Rune magicks was not great enough to defeat",
													"Chronozon, I am adequately skilled at Chaos Rune",
													"magicks.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("If you give me your gauntlets I will bestow upon them",
													"some of my power, and make any bolt spells you wish",
													"to cast more effective.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					if(player.familyGauntlets != QuestConstants.STEEL_GAUNTLETS){
						player.getDialogue().sendOption("Can you change my gauntlets for me?",
														"No thanks.");
					} else {
						player.getDialogue().sendOption("That sounds good to me.",
														"I shall see what options your brothers can offer me first.");
					}
					return true;
				}
				if(chatId == 6){
					if(optionId == 1){
						if(player.familyGauntlets != QuestConstants.STEEL_GAUNTLETS){
							player.getDialogue().sendPlayerChat("Can you change my gauntlets for me?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(8);
						} else {
							player.getDialogue().sendPlayerChat("That sounds good to me!", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(7);
						}
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 7){
					if(player.familyGauntlets != QuestConstants.STEEL_GAUNTLETS)
						player.getInventory().removeItem(new Item(995, 25000));
					player.getDialogue().sendStatement("Johnathon takes your gauntlets from you, and begins a low chant",
													"over them. You see them begin to glow and sparkle, before he",
													"returns them to you.");
					player.getInventory().removeItem(new Item(player.familyGauntlets, 1));
					player.familyGauntlets = QuestConstants.CHAOS_GAUNTLETS;
					player.getInventory().addItemOrDrop(new Item(player.familyGauntlets, 1));
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Yes certainly, though it will cost you 25,000 coins?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendOption("Great thanks!",
													"No that's ok thanks.");
					return true;
				}
				if(chatId == 10){
					if(optionId == 1 && player.getInventory().playerHasItem(995, 25000)){
						player.getDialogue().sendPlayerChat("Great thanks!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(7);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}//end of stage 1
			if(stage == 10){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Greetings. Would you happen to be Johnathon",
														"Fitzharmon?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("That... I am...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("I am here to retrieve your fragment of the Fitzharmon",
														"family crest.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("The... poison... it is all... too much... My head... will",
													"not... stop spinning...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendStatement("Sweat is pouring down Johnathons' face.");
					player.questStage[this.getId()] = 11;
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 10
			if(stage == 12){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Ooooh... thank you... Wow! I'm feeling a lot better now!",
													"That potion really seems to have done the trick!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("How can I reward you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("I've come here for your piece of the Fitzharmon family",
														"crest.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("You have? Unfortunately I don't have it any more... in",
													"my attempts to slay the fiendish Chronozon, the blood",
													"demon, I lost a lot of equipment in", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.questStage[this.getId()] = 13;
					player.tempQuestInt = 0;
					player.getDialogue().sendNpcChat("our last battle when he bested me and forced me away",
													"from his den. He probably still has it now.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 100){
					player.getDialogue().sendStatement("You use the potion on Johnathon.");
					player.getDialogue().setNextChatId(1);
					return true;
				}
			}//end of stage 12
			if(stage == 13){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I'm trying to kill this demon Chronozon that you",
														"mentioned...", Dialogues.CONTENT);
					player.tempQuestInt = 13;
					player.getDialogue().setNextChatId(6);
					return true;
				}
				if(chatId == 6){
					if(player.tempQuestInt == 0)
						player.getDialogue().sendOption("So is this Chronozon hard to defeat?",
														"Where can I find Chronozon?",
														"So how did you end up getting poisoned?");
					if(player.tempQuestInt == 1)
						player.getDialogue().sendOption("Where can I find Chronozon?",
														"So how did you end up getting poisoned?",
														"I will be on my way now.");
					if(player.tempQuestInt == 2)
						player.getDialogue().sendOption("So is this Chronozon hard to defeat?",
														"So how did you end up getting poisoned?",
														"I will be on my way now.");
					if(player.tempQuestInt == 3)
						player.getDialogue().sendOption("So is this Chronozon hard to defeat?",
														"Where can I find Chronozon?",
														"I will be on my way now.");
					if(player.tempQuestInt == 13)
						player.getDialogue().sendOption("So is this Chronozon hard to defeat?",
														"Where can I find Chronozon?",
														"Wish me luck.");
					return true;
				}
				if(chatId == 7){
					if(optionId == 1){
						if(player.tempQuestInt == 1){
							player.getDialogue().sendPlayerChat("Where can I find Chronozon?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(11);
						}else{
							player.getDialogue().sendPlayerChat("So is this Chronozon hard to defeat?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(8);
						}
						return true;
					}
					if(optionId == 2){
						if(player.tempQuestInt == 0 || player.tempQuestInt == 3){
							player.getDialogue().sendPlayerChat("Where can I find Chronozon?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(11);
						} else {
							player.getDialogue().sendPlayerChat("So how did you end up getting poisoned?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(10);
						}
						return true;
					}
					if(optionId == 3){
						if(player.tempQuestInt == 0){
							player.getDialogue().sendPlayerChat("So how did you end up getting poisoned?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(10);
						} else {
							player.getDialogue().sendPlayerChat("I will be on my way now.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(12);
						}
						return true;
					}
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Well... you will have to be a skilled Mage to defeat him,",
													"and my powers are not good enough yet. You will need",
													"to hit him once with each of the four elemental spells of",
													"death", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("before he will be defeated.", Dialogues.CONTENT);
					player.tempQuestInt = 1;
					player.getDialogue().setNextChatId(6);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Those accursed poison spiders that surround the",
													"entrance to Chronozon's lair... I must have taken a nip",
													"from one of them as I attempted to make my escape.", Dialogues.CONTENT);
					player.tempQuestInt = 3;
					player.getDialogue().setNextChatId(6);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("The fiend has made his lair in the Wilderness below the",
													"Obelisk of Air.", Dialogues.CONTENT);
					player.tempQuestInt = 2;
					player.getDialogue().setNextChatId(6);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("My thanks for the assistance adventurer.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 13
		}//end of johnathon
		return false;
	}

}
