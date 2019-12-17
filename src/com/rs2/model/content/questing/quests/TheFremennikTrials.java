package com.rs2.model.content.questing.quests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.questing.Room;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;
import com.rs2.util.Area;

public class TheFremennikTrials extends Quest {
	
	final int rewardQP = 3;
	
	public TheFremennikTrials(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public static Room rooms[] = {
		new Room(1, new Position(2630,10037,0), new Area(2627, 10034, 2633, 10040, (byte) 0)),
		new Room(2, new Position(2642,10039,0), new Area(2639, 10035, 2645, 10042, (byte) 0)),
		new Room(3, new Position(2642,10026,0), new Area(2639, 10023, 2645, 10031, (byte) 0)),
		new Room(4, new Position(2654,10026,0), new Area(2651, 10023, 2657, 10030, (byte) 0)),
		new Room(5, new Position(2631,10015,0), new Area(2628, 10012, 2634, 10018, (byte) 0)),
		new Room(6, new Position(2653,10015,0), new Area(2649, 10012, 2656, 10019, (byte) 0)),
		new Room(7, new Position(2642,10005,0), new Area(2639, 10002, 2645, 10008, (byte) 0)),
		new Room(8, new Position(2664,10004,0), new Area(2661, 10000, 2667, 10007, (byte) 0)),
	};
	
	public static Area fightSpots[] = {
		new Area(2653, 10087, 2666, 10095, (byte) 2),
		new Area(2661, 10078, 2667, 10085, (byte) 2),
		new Area(2651, 10068, 2659, 10075, (byte) 2),
		new Area(2645, 10073, 2649, 10078, (byte) 2),
		new Area(2644, 10081, 2648, 10085, (byte) 2),
	};
	
	public static Position draugenSpots[] = {
		new Position(2697,3561),
		new Position(2649,3569),
		new Position(2678,3605),
		new Position(2618,3626),
		new Position(2666,3568),
		new Position(2689,3571),
	};
	
	Area longhall = new Area(2655, 3665, 2662, 3677, (byte) 0);
	
	Area longhall_stage = new Area(2655, 3682, 2662, 3685, (byte) 0);
	
	String letters [] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
	
	private List<String> answers = Arrays.asList("TIME", "MIND", "WIND", "TREE", "LIFE");//FIRE (after wind), MAGE (after fire)
	
	private List<String> femaleNames = Arrays.asList(
			"Baldur",
			"Baldar",
			"Baldor",
			"Balkal",
			"Balkar",
			"Balkur",
			"Ballah",
			"Ballor",
			"Baltin",
			"Bardor",
			"Barkar",
			"Barkir",
			"Barlah",
			"Barlak",
			"Barlim",
			"Barlor",
			"Barrak",
			"Bartor",
			"Barton",
			"Barvald",
			"Berkal",
			"Daldar",
			"Daldor",
			"Daldur",
			"Dalkal",
			"Dalkar",
			"Dalkir",
			"Dallah",
			"Dallak",
			"Daltin",
			"Dalton",
			"Daltor",
			"Danlek",
			"Dardar",
			"Darkal",
			"Darkar",
			"Darkir",
			"Darkur",
			"Darlah",
			"Darlim",
			"Darlor",
			"Dartin",
			"Dartor",
			"Dendor",
			"Dendur",
			"Denkal",
			"Denkar",
			"Denkir",
			"Denkur",
			"Denlah",
			"Denlak",
			"Denrak",
			"Denlim",
			"Denlor",
			"Dentor",
			"Dokdor",
			"Dokdur",
			"Dokkal",
			"Dokkir",
			"Dokkur",
			"Doklah",
			"Doklim",
			"Doklak",
			"Doklor",
			"Dokrak",
			"Doktin",
			"Dokton",
			"Doktor",
			"Jardar",
			"Jarlah",
			"Jarlak",
			"Jarlor",
			"Jarkal",
			"Jarkur",
			"Jarrak",
			"Jartor",
			"Jikarak",
			"Jikdar",
			"Jikdor",
			"Jikkal",
			"Jikkar",
			"Jikkir",
			"Jikkur",
			"Jiklah",
			"Jiklak",
			"Jiklim",
			"Jiklor",
			"Jiktin",
			"Jiktor",
			"Jikvald",
			"Jokkul",
			"Lardar",
			"Larkal",
			"Larkir",
			"Larkur",
			"Larlah",
			"Larlak",
			"Larlim",
			"Larlor",
			"Larton",
			"Lartor",
			"Larvald",
			"Rakdar",
			"Rakdur",
			"Rakkal",
			"Rakkar",
			"Rakkir",
			"Rakkur",
			"Raklak",
			"Raklim",
			"Raklor",
			"Rakrak",
			"Rakton",
			"Raktor",
			"Rakvald",
			"Raldar",
			"Raldur",
			"Raldor",
			"Ralkar",
			"Ralkur",
			"Rallah",
			"Rallak",
			"Rallim",
			"Rallor",
			"Raltin",
			"Ralton",
			"Raltor",
			"Ralvald",
			"Rildar",
			"Rildor",
			"Rilkal",
			"Rilkur",
			"Rilkir",
			"Rillah",
			"Rillak",
			"Rillim",
			"Rillor",
			"Riltin",
			"Rilton",
			"Riltor",
			"Rivgar",
			"Rilvald",
			"Sigdar",
			"Sigdor",
			"Sigdur",
			"Sigkal",
			"Sigkar",
			"Sigkir",
			"Sigkur",
			"Siglah",
			"Siglak",
			"Siglim",
			"Sigrak",
			"Sigvald",
			"Sigtin",
			"Sigton",
			"Sigtor",
			"Taldar",
			"Taldur",
			"Talkal",
			"Talkar",
			"Talkir",
			"Tallah",
			"Tallak",
			"Tallim",
			"Tallor",
			"Talrak",
			"Talton",
			"Tarlak",
			"Thordar",
			"Thordur",
			"Thorkur",
			"Thortin",
			"Thortor",
			"Thorkal",
			"Thorkar",
			"Thorkir",
			"Thorlah",
			"Thorlim",
			"Thorlor",
			"Thorrak",
			"Thorton",
			"Thortor",
			"Thorvald",
			"Tondar",
			"Tondur",
			"Tonkal",
			"Tonkar",
			"Tonkir",
			"Tonkur",
			"Tonlah",
			"Tonlim",
			"Tonlin",
			"Tonlor",
			"Tonrak",
			"Tontin",
			"Tonton",
			"Tontor",
			"Tonvald"
	);
	
	private List<String> maleNames = Arrays.asList(
			"Baldar",
			"Baldor",
			"Baldur",
			"Balkal",
			"Balkar",
			"Balkir",
			"Balkur",
			"Ballah",
			"Ballak",
			"Ballim",
			"Ballor",
			"Balrak",
			"Baltin",
			"Balton",
			"Baltor",
			"Balvald",
			"Bardar",
			"Bardor",
			"Barkal",
			"Barlah",
			"Barlak",
			"Barlim",
			"Barlor",
			"Barkar",
			"Barkir",
			"Barkur",
			"Barrak",
			"Bartin",
			"Barton",
			"Bartor",
			"Barvald",
			"Dalkal",
			"Daldar",
			"Daldor",
			"Daldur",
			"Dalkir",
			"Dalkur",
			"Dallah",
			"Dallak",
			"Dallim",
			"Dallor",
			"Dalrak",
			"Daltin",
			"Daltor",
			"Dalvald",
			"Dardar",
			"Dardor",
			"Dardur",
			"Darlak",
			"Darlor",
			"Darlim",
			"Darkal",
			"Darlah",
			"Darkar",
			"Darkir",
			"Darkur",
			"Darrak",
			"Dartin",
			"Dartis",
			"Darton",
			"Dartor",
			"Darvald",
			"Dendar",
			"Dendor",
			"Dendur",
			"Denkar",
			"Denkal",
			"Denkir",
			"Denkur",
			"Denlah",
			"Denlak",
			"Denlim",
			"Denlor",
			"Denrak",
			"Dentin",
			"Denton",
			"Dentor",
			"Denvald",
			"Dentin",
			"Dokdar",
			"Dokdor",
			"Dokdur",
			"Dokkal",
			"Dokkar",
			"Dokkir",
			"Dokkur",
			"Doklah",
			"Doklak",
			"Doklim",
			"Doklor",
			"Dokrak",
			"Doktin",
			"Dokton",
			"Doktor",
			"Dokvald",
			"Jardar",
			"Jardor",
			"Jardur",
			"Jarkal",
			"Jarlor",
			"Jarkar",
			"Jarkir",
			"Jarkur",
			"Jarlah",
			"Jarlak",
			"Jarlim",
			"Jarlor",
			"Jarrak",
			"Jartin",
			"Jarton",
			"Jardor",
			"Jartor",
			"Jarvald",
			"Jikdar",
			"Jikdor",
			"Jikdur",
			"Jikkal",
			"Jikkar",
			"Jikkir",
			"Jikkur",
			"Jiklah",
			"Jiklak",
			"Jiklim",
			"Jiklor",
			"Jikrak",
			"Jiktin",
			"Jikton",
			"Jiktor",
			"Jikvald",
			"Lardar",
			"Larkal",
			"Lardor",
			"Larkar",
			"Larkir",
			"Larkur",
			"Larlah",
			"Larlak",
			"Larlim",
			"Larlor",
			"Larrak",
			"Larravak",
			"Lartin",
			"Larton",
			"Lartor",
			"Larvald",
			"Lardur",
			"Rakdar",
			"Rakdur",
			"Rakkal",
			"Rakkar",
			"Rakkir",
			"Rakkur",
			"Raklak",
			"Raklah",
			"Raklim",
			"Rakrak",
			"Raktin",
			"Rakton",
			"Rakdor",
			"Raklor",
			"Rakrak",
			"Raktor",
			"Rakvald",
			"Raldor",
			"Raldar",
			"Ralkal",
			"Ralkar",
			"Ralkir",
			"Ralkur",
			"Rallah",
			"Rallak",
			"Rallim",
			"Rallor",
			"Ralrak",
			"Raltin",
			"Ralton",
			"Raltor",
			"Ralvald",
			"Riklar",
			"Rildur",
			"Rilkar",
			"Rilkir",
			"Rilkur",
			"Rillim",
			"Rillah",
			"Rildor",
			"Rillor",
			"Rillak",
			"Rilrak",
			"Rilrar",
			"Riltin",
			"Rilton",
			"Riltor",
			"Rilvald",
			"Rildar",
			"Rilkal",
			"Rallor",
			"Sigkal",
			"Sigkar",
			"Sigkir",
			"Sigkur",
			"Siglah",
			"Siglim",
			"Siglor",
			"Sigrak",
			"Sigtin",
			"Sigton",
			"Sigtor",
			"Sigvald",
			"Siglak",
			"Sigdar",
			"Sigdor",
			"Sigdur",
			"Taldar",
			"Taldor",
			"Taldur",
			"Talkal",
			"Talkar",
			"Talkir",
			"Talkur",
			"Tallah",
			"Tallak",
			"Tallim",
			"Tallor",
			"Talrak",
			"Taltin",
			"Talton",
			"Taltor",
			"Talvald",
			"Thorak",
			"Thordar",
			"Thordor",
			"Thordur",
			"Thorkal",
			"Thorkir",
			"Thorkar",
			"Thorkur",
			"Thorlah",
			"Thorlak",
			"Thorlim",
			"Thorlor",
			"Thorrak",
			"Thortin",
			"Thorton",
			"Thortor",
			"Thorvald",
			"Tondar",
			"Tondor",
			"Tondur",
			"Tonkal",
			"Tonkar",
			"Tonkir",
			"Tonlah",
			"Tonlak",
			"Tonlim",
			"Tonlor",
			"Tonrak",
			"Tonton",
			"Tontor",
			"Tonvald",
			"Tontin",
			"Tonkur"
			);
	
	public String[] getQuestScrollText(final Player player, int stage){
		/*String text[] = {"Quest not added yet!"};
		return text;*/
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to Chieftain Brundt in",
							"the Fremennik Longhall, which is in the town of Rellekka to",
							"the north of Sinclair Mansion.",
							"To complete this quest I need:",
							"Level 40 Woodcutting",
							"Level 40 Crafting",
							"Level 25 Fletching",
							"I must also be able to defeat a level 69 enemy and must",
							"not be afraid of combat without any weapons or armour"};
			return text;
		}
		if(stage >= 2){
			String text[] = {"I need the votes from following persons before I",
							"should go talk to Brundt the Chieftain again:",
							((stage & Misc.getActualValue(3)) != 0 ? "@str@" : "")+"Manni the Reveller",
							((stage & Misc.getActualValue(5)) != 0 ? "@str@" : "")+"Olaf the Bard",
							((stage & Misc.getActualValue(7)) != 0 ? "@str@" : "")+"Sigmund the Merchant",
							((stage & Misc.getActualValue(9)) != 0 ? "@str@" : "")+"Sigli the Huntsman",
							((stage & Misc.getActualValue(11)) != 0 ? "@str@" : "")+"Swensen the Navigator",
							((stage & Misc.getActualValue(13)) != 0 ? "@str@" : "")+"Peer the Seer",
							((stage & Misc.getActualValue(15)) != 0 ? "@str@" : "")+"Thorvald the Warrior"};
			return text;
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "3 Quest Points", "2.8k XP in:", "Strength, Defence, Attack,", "Hitpoints, Fishing, Thieving,", "Agility, Crafting, Fletching,", "Woodcutting"};
			return text;	
		}
		return null;
	}
	
	boolean meetsRequirements(final Player player){
		if(player.getSkill().getPlayerLevel(Skill.WOODCUTTING) < 40 || player.getSkill().getPlayerLevel(Skill.CRAFTING) < 40 || player.getSkill().getPlayerLevel(Skill.FLETCHING) < 25)
			return false;
		return true;
	}
	
	boolean allTasksDone(final Player player){
		int stage = player.questStage[this.getId()];
		if((stage & Misc.getActualValue(3)) != 0 && (stage & Misc.getActualValue(5)) != 0 && (stage & Misc.getActualValue(7)) != 0 &&
				(stage & Misc.getActualValue(9)) != 0 && (stage & Misc.getActualValue(11)) != 0 && (stage & Misc.getActualValue(13)) != 0 &&
				(stage & Misc.getActualValue(15)) != 0)
			return true;
		return false;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("3 Quest Points, 2.8k XP in:", 12150);
		player.getActionSender().sendString("Strength, Defence, Attack,", 12151);
		player.getActionSender().sendString("Hitpoints, Fishing, Thieving,", 12152);
		player.getActionSender().sendString("Agility, Crafting, Fletching,", 12153);
		player.getActionSender().sendString("Woodcutting", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.STRENGTH, 2800);
		player.getSkill().addQuestExp(Skill.DEFENCE, 2800);
		player.getSkill().addQuestExp(Skill.ATTACK, 2800);
		player.getSkill().addQuestExp(Skill.HITPOINTS, 2800);
		player.getSkill().addQuestExp(Skill.FISHING, 2800);
		player.getSkill().addQuestExp(Skill.THIEVING, 2800);
		player.getSkill().addQuestExp(Skill.AGILITY, 2800);
		player.getSkill().addQuestExp(Skill.CRAFTING, 2800);
		player.getSkill().addQuestExp(Skill.FLETCHING, 2800);
		player.getSkill().addQuestExp(Skill.WOODCUTTING, 2800);
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.FREMENNIK_HELM);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean useQuestItemOnObject(final Player player, int itemId, int objectId, final int stage){
		if(stage < 2)
			return false;
		if(itemId == QuestConstants.NUMBER_5_BUCKET && objectId == 4176){
			player.getActionSender().sendMessage("You fill the bucket from the tap.");
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.NUMBER_5_BUCKET, 1), new Item(QuestConstants.FULL_BUCKET, 1));
			return true;
		}
		if(itemId >= 3722 && itemId <= 3726 && objectId == 4175){
			player.getActionSender().sendMessage("You empty the bucket down the drain.");
			player.getInventory().replaceItemWithItem(new Item(itemId, 1), new Item(QuestConstants.NUMBER_5_BUCKET, 1));
			return true;
		}
		if(itemId == QuestConstants.NUMBER_3_JUG && objectId == 4176){
			player.getActionSender().sendMessage("You fill the jug from the tap.");
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.NUMBER_3_JUG, 1), new Item(QuestConstants.FULL_JUG, 1));
			return true;
		}
		if(itemId == QuestConstants.BUCKET_45 && objectId == 4170){
			player.getActionSender().sendMessage("You take a strange looking vase out of the chest.");
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.BUCKET_45, 1), new Item(QuestConstants.VASE, 1));
			return true;
		}
		if(itemId == QuestConstants.RED_HERRING && objectId == 4172){
			player.getDialogue().sendStatement("As you cook the herring on the stove, the colouring on it peels off",
											"separately as a red sticky goop...");
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.RED_HERRING, 1), new Item(QuestConstants.HERRING, 1));
			player.getInventory().addItemOrDrop(new Item(QuestConstants.STICKY_RED_GOOP, 1));
			return true;
		}
		if(itemId == QuestConstants.RED_DISK && objectId == 4179){
			player.getActionSender().sendMessage("You put the red disk into the empty hole on the mural.");
			player.getActionSender().sendMessage("It is a perfect fit!");
			player.getInventory().removeItem(new Item(QuestConstants.RED_DISK, 1));
			if((player.questStageSub1[this.getId()] & Misc.getActualValue(23)) == 0){
				player.questStageSub1[this.getId()] += Misc.getActualValue(23);
			}
			else if(((player.questStageSub1[this.getId()] & Misc.getActualValue(22)) != 0) && ((player.questStageSub1[this.getId()] & Misc.getActualValue(23)) != 0)){//second red disk
				player.getActionSender().sendMessage("The center of the mural falls out!");
				player.getInventory().addItemOrDrop(new Item(QuestConstants.VASE_LID, 1));
			}
			return true;
		}
		if(itemId == QuestConstants.VASE && objectId == 4176){
			player.getActionSender().sendMessage("You fill the strange looking vase with water.");
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.VASE, 1), new Item(QuestConstants.VASE_OF_WATER, 1));
			return true;
		}
		if(itemId == QuestConstants.SEALED_VASE_OF_WATER && objectId == 4169){
			player.getActionSender().sendMessage("The water expands as it freezes, and shatters the vase.");
			player.getActionSender().sendMessage("You are left with a key encased in ice.");
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.SEALED_VASE_OF_WATER, 1), new Item(QuestConstants.FROZEN_KEY, 1));
			return true;
		}
		if(itemId == QuestConstants.FROZEN_KEY && objectId == 4172){
			player.getActionSender().sendMessage("The heat of the range melts the ice around the key.");
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.FROZEN_KEY, 1), new Item(QuestConstants.SEERS_KEY, 1));
			return true;
		}
		if(itemId == QuestConstants.LIT_STRANGE_OBJECT && objectId == 4162){
			if((player.questStageSub1[this.getId()] & Misc.getActualValue(2)) == 0){
				player.getActionSender().sendMessage("You put the lit strange object into the pipe.");
				player.getInventory().removeItem(new Item(QuestConstants.LIT_STRANGE_OBJECT, 1));
				player.questStageSub1[this.getId()] += Misc.getActualValue(2);
				player.getDialogue().sendPlayerChat("That is going to make a really loud bang when it goes",
													"off! It would be a perfect distraction to help me cheat in",
													"the drinking contest!", Dialogues.CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		}
		if(itemId == QuestConstants.PET_ROCK && objectId == 4149){
			if((player.questStageSub1[this.getId()] & Misc.getActualValue(4)) == 0){
				player.getActionSender().sendMessage("You put your pet rock into the cauldron.");
				player.getInventory().removeItem(new Item(QuestConstants.PET_ROCK, 1));
				player.questStageSub1[this.getId()] += Misc.getActualValue(4);
				Dialogues.sendDialogue(player, QuestConstants.LALLI, 100, 0);
				return true;
			}
		}
		if(itemId == QuestConstants.CABBAGE && objectId == 4149){
			if((player.questStageSub1[this.getId()] & Misc.getActualValue(5)) == 0){
				player.getActionSender().sendMessage("You put a cabbage into the cauldron.");
				player.getInventory().removeItem(new Item(QuestConstants.CABBAGE, 1));
				player.questStageSub1[this.getId()] += Misc.getActualValue(5);
				Dialogues.sendDialogue(player, QuestConstants.LALLI, 100, 0);
				return true;
			}
		}
		if(itemId == QuestConstants.POTATO && objectId == 4149){
			if((player.questStageSub1[this.getId()] & Misc.getActualValue(6)) == 0){
				player.getActionSender().sendMessage("You put a potato into the cauldron.");
				player.getInventory().removeItem(new Item(QuestConstants.POTATO, 1));
				player.questStageSub1[this.getId()] += Misc.getActualValue(6);
				Dialogues.sendDialogue(player, QuestConstants.LALLI, 100, 0);
				return true;
			}
		}
		if(itemId == QuestConstants.ONION && objectId == 4149){
			if((player.questStageSub1[this.getId()] & Misc.getActualValue(7)) == 0){
				player.getActionSender().sendMessage("You put an onion into the cauldron.");
				player.getInventory().removeItem(new Item(QuestConstants.ONION, 1));
				player.questStageSub1[this.getId()] += Misc.getActualValue(7);
				Dialogues.sendDialogue(player, QuestConstants.LALLI, 100, 0);
				return true;
			}
		}
		if(itemId == QuestConstants.GOLDEN_FLEECE && objectId == 2644){
			player.getActionSender().sendMessage("You spin the fleece into some golden string.");
			player.getUpdateFlags().sendAnimation(896);
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.GOLDEN_FLEECE, 1), new Item(QuestConstants.GOLDEN_WOOL, 1));
			return true;
		}
		if((itemId == QuestConstants.RAW_SHARK || itemId == QuestConstants.RAW_MANTA_RAY || itemId == QuestConstants.RAW_SEA_TURTLE) && objectId == 4141 && player.getInventory().playerHasItem(QuestConstants.LYRE, 1)){
			player.getInventory().removeItem(new Item(QuestConstants.LYRE, 1));
			player.getInventory().replaceItemWithItem(new Item(itemId, 1), new Item(QuestConstants.ENCHANTED_LYRE, 1));
			Npc npc1 = new Npc(QuestConstants.FOSSEGRIMEN);
			NpcLoader.spawnNpcPos(player, new Position(2626,3596,0), npc1, false, false);
			Dialogues.sendDialogue(player, QuestConstants.FOSSEGRIMEN, 100, 0);
			player.getActionSender().sendMessage("Fossegrimen has enchanted your lyre so that you may play it.");
			return true;
		}
		return false;
	}

	public boolean getObjectDialog(int clickId, final Player player, int objectId, int chatId, int optionId, int npcChatId, int x, int y, int stage){
		if(objectId == 4181 && x == 2632 && y == 3660 && clickId == 1){//unicorn head
			if(player.getInventory().playerHasItem(QuestConstants.RED_DISK, 1))
				return false;
			if(chatId == 1){
				player.getDialogue().sendStatement("You notice there is something unusual about the left eye of this",
												"unicorn head...");
				return true;
			}
			if(chatId == 2){
				player.getDialogue().sendStatement("It is not an eye at all, but some kind of red coloured disk. You take",
												"it from the head.");
				player.getInventory().addItemOrDrop(new Item(QuestConstants.RED_DISK, 1));
				player.getDialogue().endDialogue();
				return true;
			}
		}
		if(objectId == 4182 && x == 2634 && y == 3660 && clickId == 1){//bull head
			if(player.getInventory().playerHasItem(QuestConstants.WOODEN_DISK, 1))
				return false;
			if(chatId == 1){
				player.getDialogue().sendStatement("You notice there is something unusual about the right eye of this",
												"bulls' head...");
				return true;
			}
			if(chatId == 2){
				player.getDialogue().sendStatement("It is not an eye at all, but some kind of disk made of wood. You",
												"take it from the head.");
				player.getInventory().addItemOrDrop(new Item(QuestConstants.WOODEN_DISK, 1));
				player.getDialogue().endDialogue();
				return true;
			}
		}
		if(objectId == 4165 && x == 2631 && y == 3667 && clickId == 1){//door to seers house
			if((player.questStageSub1[this.getId()] & Misc.getActualValue(21)) != 0 || stage <= 1 || (player.questStage[this.getId()] & Misc.getActualValue(12)) == 0){
				player.getActionSender().sendMessage("This door is locked tightly shut.");
				player.getDialogue().endDialogue();
				return true;
			}
			if(chatId == 1){
				player.getDialogue().sendStatement("There is a combination lock on this door. Above the lock you can see",
												"that there is a metal plaque with a riddle on it.");
				return true;
			}
			if(chatId == 2){
				player.getDialogue().sendOptionWTitle("What would you like to do?",
													"Read the riddle",
													"Solve the riddle",
													"Forget it");
				return true;
			}
			if(chatId == 3){
				if(optionId == 1){
					List<String> answers_ = new ArrayList<String>(answers);
					Collections.shuffle(answers_, new Random(player.uniqueRandomInt));
					getRiddle(player, answers_.get(0), 1);
					player.getDialogue().setNextChatId(4);
					return true;
				}
				if(optionId == 2){
					player.getDialogue().endDialogue();
					player.tempMiscInt_1 = 0;
					player.tempMiscInt_2 = 0;
					player.tempMiscInt_3 = 0;
					player.tempMiscInt_4 = 0;
					player.getActionSender().sendString(letters[0], 13884);
					player.getActionSender().sendString(letters[0], 13885);
					player.getActionSender().sendString(letters[0], 13886);
					player.getActionSender().sendString(letters[0], 13887);
					player.getActionSender().sendInterface(671);
					return true;
				}
				if(optionId == 3){
					player.getDialogue().endDialogue();
					return false;
				}
			}
			if(chatId == 4){
				List<String> answers_ = new ArrayList<String>(answers);
				Collections.shuffle(answers_, new Random(player.uniqueRandomInt));
				getRiddle(player, answers_.get(0), 2);
				player.getDialogue().endDialogue();
				return true;
			}
		}
		return false;
	}
	
	void getRiddle(final Player player, String s, int part){
		if(s.equals("TIME")){
			if(part == 1){
				player.getDialogue().sendStatement("My first is in water, and also in tea.",
												"My second in fish, but not in the sea.",
												"My third in mountains, but not underground.",
												"My last is in strike, but not in pound.");
			} else {
				player.getDialogue().sendStatement("My whole crushes mountains, drains rivers, and destroys civilisations.",
												"All that live fear my passing.",
												"What am I?");
			}
		} else if(s.equals("MIND")){
			if(part == 1){
				player.getDialogue().sendStatement("My first is in mage, but not in wizard.",
						"My second in goblin, and also in lizard.",
						"My third is in night, but not in the day.",
						"My last is in fields, but not in the hay.");
			} else {
				player.getDialogue().sendStatement("My whole is the most powerful tool you will ever possess.",
						"What am I?");
			}
		} else if(s.equals("WIND")){
			if(part == 1){
				player.getDialogue().sendStatement("My first is in wizard, but not in a mage.",
						"My second in jail, but not in a cage.",
						"My third is in anger, but not in a rage.",
						"My last is in a drawing, but not on a page.");
			} else {
				player.getDialogue().sendStatement("My whole helps to make bread, let birds fly and boats sail.",
						"What am I?");
			}
		} else if(s.equals("TREE")){
			if(part == 1){
				player.getDialogue().sendStatement("My first is in tar, but not in a swamp.",
						"My second in fire, but not in a camp.",
						"My third is in eagle, but never in air.",
						"My last is in hate, but also in care.");
			} else {
				player.getDialogue().sendStatement("My whole wears more rings, the older I get.",
						"What am I?");
			}
		} else if(s.equals("LIFE")){
			if(part == 1){
				player.getDialogue().sendStatement("My first is in the well, but not at sea.",
						"My second in 'I', but not in 'me'.",
						"My third is in flies, but insects not found.",
						"My last is in earth, but not in the ground.");
			} else {
				player.getDialogue().sendStatement("My whole when stolen from you, causes you death.",
						"What am I?");
			}
		}
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 4171 && (objectX == 2634 || objectX == 2635) && objectY == 3665){//bookcase
			if(player.getInventory().playerHasItem(QuestConstants.RED_HERRING, 1))
				return false;
			player.getActionSender().sendMessage("You search the bookcase...");
			player.getActionSender().sendMessage("Hidden behind some old books, you find a red herring.");
			player.getInventory().addItemOrDrop(new Item(QuestConstants.RED_HERRING, 1));
			return true;
		}
		if(objectId == 4148 && objectX == 2667 && objectY == 3683){//stage door
			if(player.getPosition().getX() < 2667){
				player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
				player.getActionSender().walkTo(1, 0, true);
			}else{
				if(player.getInventory().playerHasItem(QuestConstants.ENCHANTED_LYRE, 1)){
					player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
					player.getActionSender().walkTo(-1, 0, true);
					Dialogues.sendDialogue(player, QuestConstants.LONGHALL_BOUNCER, 100, 0);
				}else{
					Dialogues.sendDialogue(player, QuestConstants.LONGHALL_BOUNCER, 101, 0);
				}
			}
			return true;
		}
		if(objectId == 4166 && objectX == 2636 && objectY == 3667){//end door
			if(player.getPosition().getY() < 3667 && player.getInventory().playerHasItem(QuestConstants.SEERS_KEY, 1)){
				player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
				player.getActionSender().walkTo(0, 1, true);
				player.removeCarriedUntradeableItems();
				player.getActionSender().sendMessage("You unlock the door with your key.");
				Dialogues.sendDialogue(player, QuestConstants.PEER_THE_SEER, 100, 0);
			}else
				player.getActionSender().sendMessage("This door is locked tightly shut.");
			return true;
		}
		if(objectId == 4165 && objectX == 2631 && objectY == 3667){//start door
			if((player.questStageSub1[this.getId()] & Misc.getActualValue(21)) != 0 && stage >= 2 && (stage & Misc.getActualValue(13)) == 0){
				if(!player.isCarryingItems()){
					player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
					player.getActionSender().walkTo(0, player.getPosition().getY() < 3667 ? 1 : -1, true);
				}else
					player.getActionSender().sendMessage("You can't bring any items inside!");
				return true;
			}else{
				return false;
			}
		}
		if(objectId == 4177 && objectX == 2629 && objectY == 3660){//cupboard
			ObjectHandler.getInstance().removeObject(2629, 3660, 2, 0);
			ObjectHandler.getInstance().addObject(new GameObject(4178, 2629, 3660, 2, 3, 10, 4177, 999999999), true);
			return true;
		}
		if(objectId == 4167 && objectX == 2635 && objectY == 3660){//chest
			ObjectHandler.getInstance().removeObject(2635, 3660, 2, 0);
			ObjectHandler.getInstance().addObject(new GameObject(4168, 2635, 3660, 2, 0, 10, 4167, 999999999), true);
			return true;
		}
		if(objectId == 4167 && objectX == 2638 && objectY == 3662){//chest
			ObjectHandler.getInstance().removeObject(2638, 3662, 2, 0);
			ObjectHandler.getInstance().addObject(new GameObject(4168, 2638, 3662, 2, 3, 10, 4167, 999999999), true);
			return true;
		}
		if(objectId == 4188 && objectX == 2672 && objectY == 10099){//ladders out of fight
			Ladders.climbLadder(player, new Position(2666,3694,0));
			return true;
		}
		if(objectId == 4158 && objectX == 2644 && objectY == 3657){//ladders to swensen maze
			if(stage < 2)
				return false;
			if((stage & Misc.getActualValue(10)) != 0 && (stage & Misc.getActualValue(11)) == 0){
				Ladders.climbLadder(player, new Position(2631,10004,0));
				return true;
			}
		}
		if(objectId == 100 && objectX == 2650 && objectY == 3661){
			player.getDialogue().sendStatement("You try to open the trapdoor but it won't budge! It looks like the",
												"trapdoor can only be opened from the other side.");
			player.getActionSender().sendMessage("You try to open the trapdoor but it won't budge!");
			player.getActionSender().sendMessage("It looks like the trapdoor can only be opened from the other side.");
			player.getDialogue().endDialogue();
			return true;
		}
		if(objectId == 4160 && objectX == 2665 && objectY == 10037){//ladders to swensen maze end
			//Ladders.climbLadder(player, new Position(2649,3661,0));
			player.getUpdateFlags().sendAnimation(828);
			player.teleport(new Position(2649,3661,0));
			Dialogues.sendDialogue(player, QuestConstants.SWENSEN_THE_NAVIGATOR, 100, 0);
			return true;
		}
		if(objectId == 4159 && objectX == 2631 && objectY == 10005){//ladders to swensen maze start
			Ladders.climbLadder(player, new Position(2644,3658,0));
			return true;
		}
		if(objectId == 4161){//rope to swensen maze start
			Ladders.climbLadder(player, new Position(2647,3658,0));
			return true;
		}
		if(objectId == 4150 && objectX == 2631 && objectY == 10002){//portal #1
			player.teleport(new Position(2642,10018,0));
			final Tick timer1 = new Tick(1) {
	            @Override
	            public void execute() {
	            	player.getActionSender().walkTo(0, -1, true);
	                stop();
	            }
	            
			};
			World.getTickManager().submit(timer1);
			return true;
		}
		if(objectId == 4151 && objectX == 2639 && objectY == 10015){//portal #2
			player.teleport(new Position(2650,10004,0));
			final Tick timer1 = new Tick(1) {
	            @Override
	            public void execute() {
	            	player.getActionSender().walkTo(1, 0, true);
	                stop();
	            }
	            
			};
			World.getTickManager().submit(timer1);
			return true;
		}
		if(objectId == 4152 && objectX == 2656 && objectY == 10004){//portal #3
			player.teleport(new Position(2668,10015,0));
			final Tick timer1 = new Tick(1) {
	            @Override
	            public void execute() {
	            	player.getActionSender().walkTo(-1, 0, true);
	                stop();
	            }
	            
			};
			World.getTickManager().submit(timer1);
			return true;
		}
		if(objectId == 4153 && objectX == 2665 && objectY == 10018){//portal #4
			player.teleport(new Position(2630,10029,0));
			final Tick timer1 = new Tick(1) {
	            @Override
	            public void execute() {
	            	player.getActionSender().walkTo(0, -1, true);
	                stop();
	            }
	            
			};
			World.getTickManager().submit(timer1);
			return true;
		}
		if(objectId == 4154 && objectX == 2630 && objectY == 10023){//portal #5
			player.teleport(new Position(2653,10034,0));
			final Tick timer1 = new Tick(1) {
	            @Override
	            public void execute() {
	            	player.getActionSender().walkTo(0, 1, true);
	                stop();
	            }
	            
			};
			World.getTickManager().submit(timer1);
			return true;
		}
		if(objectId == 4155 && objectX == 2656 && objectY == 10037){//portal #6
			player.teleport(new Position(2669,10026,0));
			final Tick timer1 = new Tick(1) {
	            @Override
	            public void execute() {
	            	player.getActionSender().walkTo(-1, 0, true);
	                stop();
	            }
	            
			};
			World.getTickManager().submit(timer1);
			return true;
		}
		if(objectId == 4156 && objectX == 2666 && objectY == 10029){//portal #7
			player.teleport(new Position(2665,10038,0));
			return true;
		}
		if(objectId == 4157){//wrong portals
			int playerRoom = 0;
			for (int i = 0; i < rooms.length; i++) {
				Room room = rooms[i];
				if(room.getArea().containsBorderIncluded(player.getPosition())){
					playerRoom = room.getId();
					break;
				}
			}
			int x = 0;
			int y = 3;
			if(playerRoom != 0){
				x = 0;
				y = 0;
				Room room = rooms[playerRoom-1];
				if(objectX > room.getMiddle().getX())
					x = 3;
				if(objectX < room.getMiddle().getX())
					x = -3;
				if(objectY > room.getMiddle().getY())
					y = 3;
				if(objectY < room.getMiddle().getY())
					y = -3;
			}
			ArrayList<Integer> possibleRooms = new ArrayList<Integer>();
			for(int i = 0; i < rooms.length; i++){
				Room room = rooms[i];
				if(room.getId() != playerRoom)
					possibleRooms.add(i);
			}
			int index = Misc.random_(possibleRooms.size());
			Room nextRoom = rooms[possibleRooms.get(index)];
			player.teleport(new Position(nextRoom.getMiddle().getX()+x,nextRoom.getMiddle().getY()+y,0));
			final int y_ = y;
			final int x_ = x;
			final Tick timer1 = new Tick(1) {
	            @Override
	            public void execute() {
	            	if(y_ != 0)
	            		player.getActionSender().walkTo(0, (y_ < 0 ? 1 : -1), true);
	            	if(x_ != 0)
	            		player.getActionSender().walkTo((x_ < 0 ? 1 : -1), 0, true);
	                stop();
	            }
	            
			};
			World.getTickManager().submit(timer1);
			return true;
		}
		return false;
	}
	
	public boolean handleSecondClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 4187 && objectX == 2667 && objectY == 3694){//ladders to fight
			if(stage < 2)
				return false;
			if((stage & Misc.getActualValue(14)) != 0 && (stage & Misc.getActualValue(15)) == 0){
				if (player.hasCombatEquipment()) {
					player.getActionSender().sendMessage("You can't bring combat equipment here!");
					return true;
				}
				Ladders.climbLadder(player, new Position(2671,10098,2));
				player.getActionSender().sendMessage("Explore this battleground and find your foe...");
				player.tempAreaEffectInt = 0;
				return true;
			}
		}
		if(objectId == 4178 && objectX == 2629 && objectY == 3660){
			if(player.getInventory().playerHasItem(QuestConstants.NUMBER_5_BUCKET, 1))
				return false;
			player.getActionSender().sendMessage("You search the cupboard...");
			player.getActionSender().sendMessage("You find a bucket with a number five painted on it.");
			player.getInventory().addItemOrDrop(new Item(QuestConstants.NUMBER_5_BUCKET, 1));
			return true;
		}
		if(objectId == 4168 && objectX == 2638 && objectY == 3662){
			if(player.getInventory().playerHasItem(QuestConstants.NUMBER_3_JUG, 1))
				return false;
			player.getActionSender().sendMessage("You search the chest...");
			player.getActionSender().sendMessage("You find a jug with a number three painted on it.");
			player.getInventory().addItemOrDrop(new Item(QuestConstants.NUMBER_3_JUG, 1));
			return true;
		}
		return false;
	}
	
	/*public boolean checkAreas(final Player player, int stage){
		if(player.tempAreaEffectInt > 12 || (player.questStageSub1[this.getId()] & Misc.getActualValue(24)) != 0 || stage < 2)
			return false;
		for(int i = 0; i < fightSpots.length; i++){
			if(fightSpots[i].contains(player.getPosition())){
				if(player.tempAreaEffectInt == 12){
					int id = QuestConstants.KOSCHEI_THE_DEATHLESS_FORM1;
					if (!NpcLoader.checkSpawn(player, id)) {
						Npc npc = new Npc(id);
						NpcLoader.spawnNpc(player, npc, true, true);
						npc.getUpdateFlags().setForceChatMessage("Prepare to die, "+player.getUsername()+"!");
						player.tempAreaEffectInt = 13;
					}
					return true;
				}
				if(player.tempAreaEffectInt < 12)
					player.tempAreaEffectInt++;
				return true;
			}
		}
		return false;
	}*/
	
	public boolean checkAreas(final Player player, int stage){
		if((player.questStageSub1[this.getId()] & Misc.getActualValue(24)) != 0 || stage < 2)
			return false;
		if(player.tempAreaEffectInt == QuestConstants.KOSCHEI_THE_DEATHLESS_FORM1)
			return true;
		for(int i = 0; i < fightSpots.length; i++){
			if(fightSpots[i].contains(player.getPosition())){
				final Tick timer1 = new Tick(10) {
		            @Override
		            public void execute() {
		            	if(!player.isLoggedIn()){
		            		stop();
		            		return;
		            	}
		            	boolean b = false;
		            	for(int i = 0; i < fightSpots.length; i++){
		        			if(fightSpots[i].contains(player.getPosition())){
		        				b = true;
		        				int id = QuestConstants.KOSCHEI_THE_DEATHLESS_FORM1;
		    					if (!NpcLoader.checkSpawn(player, id)) {
		    						Npc npc = new Npc(id);
		    						NpcLoader.spawnNpc(player, npc, true, true);
		    						npc.getUpdateFlags().setForceChatMessage("Prepare to die, "+player.getUsername()+"!");
		    					}
		        				stop();
		        			}
		            	}
		            	if(!b){
		            		player.tempAreaEffectInt = -1;
		            		stop();
		            	}
		            }
				};
		        World.getTickManager().submit(timer1);
		        player.tempAreaEffectInt = QuestConstants.KOSCHEI_THE_DEATHLESS_FORM1;
		        return true;
			}
		}
		return false;
	}
	
	public boolean controlDying(final Entity attacker, final Entity victim, int stage){
		if(victim.isNpc() && attacker.isPlayer()){
        	Player player = (Player) attacker;
        	Npc npc = (Npc) victim;
        	if(npc.getNpcId() == QuestConstants.KOSCHEI_THE_DEATHLESS_FORM1){
        		CombatManager.resetCombat(npc);
        		CombatManager.resetCombat(player);
        		Dialogues.sendDialogue(player, QuestConstants.KOSCHEI_THE_DEATHLESS_FORM1, 100, 0);
        		return true;
        	}
        	if(npc.getNpcId() == QuestConstants.KOSCHEI_THE_DEATHLESS_FORM2){
        		CombatManager.resetCombat(npc);
        		CombatManager.resetCombat(player);
        		Dialogues.sendDialogue(player, QuestConstants.KOSCHEI_THE_DEATHLESS_FORM2, 100, 0);
        		return true;
        	}
        	if(npc.getNpcId() == QuestConstants.KOSCHEI_THE_DEATHLESS_FORM3){
        		CombatManager.resetCombat(npc);
        		CombatManager.resetCombat(player);
        		Dialogues.sendDialogue(player, QuestConstants.KOSCHEI_THE_DEATHLESS_FORM3, 100, 0);
        		/*if((player.questStageSub1[this.getId()] & Misc.getActualValue(24)) == 0)
        			player.questStageSub1[this.getId()] += Misc.getActualValue(24);*/
        		return true;
        	}
        	if(npc.getNpcId() == QuestConstants.KOSCHEI_THE_DEATHLESS_FORM4){
        		if((player.questStage[this.getId()] & Misc.getActualValue(15)) == 0){
        			player.questStage[this.getId()] += Misc.getActualValue(15);
        			player.getActionSender().sendMessage("Congratulations! You have passed the warrior's trial!");
        		}
        		npc.setDead(true);
				CombatManager.startDeath(npc);
        		return true;
        	}
        	if(npc.getNpcId() == QuestConstants.THE_DRAUGEN){
        		//npc.getUpdateFlags().sendGraphic(86, 25);
        		player.getActionSender().createStillGfx(86, npc.getPosition().getX(), npc.getPosition().getY(), npc.getPosition().getZ(), 0);
        		CombatManager.resetCombat(npc);
        		CombatManager.resetCombat(player);
        		CombatManager.endDeath(npc, player, false);
        		if(player.getInventory().playerHasItem(QuestConstants.HUNTERS_TALISMAN_EMPTY, 1)){
        			player.getActionSender().sendMessage("You absorb the Draugen's essence into your talisman.");
        			player.getInventory().removeItem(new Item(QuestConstants.HUNTERS_TALISMAN_EMPTY, 1));
        			player.getInventory().addItemOrDrop(new Item(QuestConstants.HUNTERS_TALISMAN_FULL, 1));
        		}
        		return true;
        	}
        }
		if(victim.isPlayer() && attacker.isNpc()){
			Player player = (Player) victim;
        	Npc npc = (Npc) attacker;
        	if(npc.getNpcId() == QuestConstants.KOSCHEI_THE_DEATHLESS_FORM4){
        		CombatManager.resetCombat(npc);
        		CombatManager.resetCombat(player);
        		player.getActionSender().sendMessage("Oh dear you are...");
        		player.setCurrentHp(player.getMaxHp());
        		player.teleport(new Position(2667,3692,1));
        		if((player.questStage[this.getId()] & Misc.getActualValue(15)) == 0){
        			player.questStage[this.getId()] += Misc.getActualValue(15);
        			Dialogues.sendDialogue(player, QuestConstants.THORVALD_THE_WARRIOR, 100, 0);
        			player.getActionSender().sendMessage("Congratulations! You have passed the warrior's trial!");
        		}
        		return true;
        	}
		}
		return false;
	}
	
	public boolean handleInterfaceButton(final Player player, int buttonId, int stage){
		if(buttonId == 13888 || buttonId == 13889){//#1
			int i = player.tempMiscInt_1;
		 	if(buttonId == 13889){
		 		if(i == letters.length-1)
		 			i = 0;
		 		else
		 			i++;
		 	} else {
		 		if(i == 0)
		 			i = letters.length-1;
		 		else
		 			i--;
		 	}
		 	player.tempMiscInt_1 = i;
		 	player.getActionSender().sendString(letters[i], 13884);
		 	return true;
		}
		if(buttonId == 13890 || buttonId == 13891){//#2
		 	int i = player.tempMiscInt_2;
		 	if(buttonId == 13891){
		 		if(i == letters.length-1)
		 			i = 0;
		 		else
		 			i++;
		 	} else {
		 		if(i == 0)
		 			i = letters.length-1;
		 		else
		 			i--;
		 	}
		 	player.tempMiscInt_2 = i;
		 	player.getActionSender().sendString(letters[i], 13885);
		 	return true;
		}
		if(buttonId == 13892 || buttonId == 13893){//#3
		 	int i = player.tempMiscInt_3;
		 	if(buttonId == 13893){
		 		if(i == letters.length-1)
		 			i = 0;
		 		else
		 			i++;
		 	} else {
		 		if(i == 0)
		 			i = letters.length-1;
		 		else
		 			i--;
		 	}
		 	player.tempMiscInt_3 = i;
		 	player.getActionSender().sendString(letters[i], 13886);
		 	return true;
		}
		if(buttonId == 13894 || buttonId == 13895){//#4
		 	int i = player.tempMiscInt_4;
		 	if(buttonId == 13895){
		 		if(i == letters.length-1)
		 			i = 0;
		 		else
		 			i++;
		 	} else {
		 		if(i == 0)
		 			i = letters.length-1;
		 		else
		 			i--;
		 	}
		 	player.tempMiscInt_4 = i;
		 	player.getActionSender().sendString(letters[i], 13887);
		 	return true;
		}
		if(buttonId == 13898){//solve
			List<String> answers_ = new ArrayList<String>(answers);
			Collections.shuffle(answers_, new Random(player.uniqueRandomInt));
			String answer = answers_.get(0);
			String guess = letters[player.tempMiscInt_1]+letters[player.tempMiscInt_2]+letters[player.tempMiscInt_3]+letters[player.tempMiscInt_4];
			//player.getActionSender().sendMessage("Answer: "+answer+", Guess: "+guess);
			if(answer.equals(guess)){
				player.getActionSender().removeInterfaces();
				player.getActionSender().sendMessage("You have solved the riddle!");
				if((player.questStageSub1[this.getId()] & Misc.getActualValue(21)) == 0 && stage >= 2){
					player.questStageSub1[this.getId()] += Misc.getActualValue(21);
				}
			} else {
				player.getActionSender().sendMessage("Your answer is not correct!");
			}
			return true;
		}
		return false;
	}
	
	String song [][] = { {"The thought of lots of questing,",
							"Leaves some people unfulfilled,",
							"But I have done my simple best, in",
							"Entering the Champions Guild."},
							
							{"PLAYERNAME, is my name,",
							"I haven't much to say.",
							"But since I have to sing this song,",
							"I'll just go ahead and play."}};
	
	String insults[] = {"Please, ye gods! Make it stop!", "YOU ARE A TERRIBLE BARD!"};
	int insultNpc[] = {1274, 1275, 1276, 1277};
	
	public boolean handleFirstClickItem(final Player player, int interfaceId, int itemId, int stage){
		if(interfaceId == 3214){//inventory
			if(itemId == QuestConstants.ENCHANTED_LYRE){
				if(longhall_stage.containsBorderIncluded(player.getPosition())){
					player.getUpdateFlags().sendFaceToDirection(new Position(player.getPosition().getX(), player.getPosition().getY()-1));
					player.getActionSender().sendMessage("You withdraw your lyre.");
					player.setStopPacket(true);
					final int song_index = Misc.random_(2);
					final int insultString_index = Misc.random_(2);
					final int insultNpc_index = Misc.random_(4);
					//final int qID = this.getId();
					final Tick timer1 = new Tick(2) {
			            @Override
			            public void execute() {
			            	String s = song[song_index][0].replaceAll("PLAYERNAME", player.getUsername());
			            	player.getUpdateFlags().setForceChatMessage(s);
			            	World.getTickManager().submit(timer2);
			                stop();
			            }
			            
			            final Tick timer2 = new Tick(4) {
				            @Override
				            public void execute() {
				            	player.getUpdateFlags().setForceChatMessage(song[song_index][1]);
				            	World.getTickManager().submit(timer3);
				                stop();
				            }
				        };
				        
				        final Tick timer3 = new Tick(4) {
				            @Override
				            public void execute() {
				            	player.getUpdateFlags().setForceChatMessage(song[song_index][2]);
				            	World.getTickManager().submit(timer4);
				                stop();
				            }
				        };
				        
				        final Tick timer4 = new Tick(4) {
				            @Override
				            public void execute() {
				            	Npc npc = Npc.getNpcById(insultNpc[insultNpc_index]);
								if(npc != null){
									npc.getUpdateFlags().setForceChatMessage(insults[insultString_index]);
								}
								player.getUpdateFlags().setForceChatMessage(song[song_index][3]);
				            	World.getTickManager().submit(timer5);
				                stop();
				            }
				        };
				        
				        final Tick timer5 = new Tick(4) {
				            @Override
				            public void execute() {
				            	player.setStopPacket(false);
				            	Dialogues.sendDialogue(player, QuestConstants.OLAF_THE_BARD, 100, 0);
				                stop();
				            }
				        };
				        
					};
					World.getTickManager().submit(timer1);
					return true;
				}
			}//end of enchanted lyre
			if(itemId == QuestConstants.HUNTERS_TALISMAN_EMPTY){
				int id = QuestConstants.THE_DRAUGEN;
				if (NpcLoader.checkSpawn(player, id))
					return false;
				if(player.tempMiscInt != QuestConstants.THE_DRAUGEN){
					player.tempMiscInt = QuestConstants.THE_DRAUGEN;
					player.tempMiscInt2 = Misc.random_(draugenSpots.length);
				} else {
					if(Misc.goodDistance(player.getPosition(), draugenSpots[player.tempMiscInt2], 1)){
						player.getActionSender().sendMessage("The Draugen is here! Beware!");
						if (!NpcLoader.checkSpawn(player, id)) {
							Npc npc = new Npc(id);
							NpcLoader.spawnNpc(player, npc, true, false);
						}
						return true;
					} else {
						if(Misc.random_(100) == 0)
							player.tempMiscInt2 = Misc.random_(draugenSpots.length);
					}
				}
				int goalX = draugenSpots[player.tempMiscInt2].getX();
				int goalY = draugenSpots[player.tempMiscInt2].getY();
				int diffX = player.getPosition().getX() - goalX;
				int diffY = player.getPosition().getY() - goalY;
				String dir = "";
				if(diffY != 0)
					dir += (diffY < 0 ? "north" : "south");
				if(diffY != 0 && diffX != 0)
					dir += "-";
				if(diffX != 0)
					dir += (diffX < 0 ? "east" : "west");
				if(diffX != 0 || diffY != 0)
					player.getActionSender().sendMessage("The talisman guides you "+dir+".");
				return true;
			}
			if(itemId == QuestConstants.HUNTERS_TALISMAN_FULL){
				player.getActionSender().sendMessage("You have already fought against the Draugen.");
				return true;
			}
		}
		return false;
	}
	
	public boolean handleItemOnItem(final Player player, int item1, int item2, int stage){
		if(stage < 2)
			return false;
		if((item1 == QuestConstants.VASE_OF_WATER && item2 == QuestConstants.VASE_LID) || (item2 == QuestConstants.VASE_OF_WATER && item1 == QuestConstants.VASE_LID)){
			player.getActionSender().sendMessage("You screw the lid on tightly.");
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.VASE_OF_WATER, 1), new Item(QuestConstants.SEALED_VASE_OF_WATER, 1));
			player.getInventory().removeItem(new Item(QuestConstants.VASE_LID, 1));
			return true;
		}
		if((item1 == QuestConstants.WOODEN_DISK && item2 == QuestConstants.STICKY_RED_GOOP) || (item2 == QuestConstants.WOODEN_DISK && item1 == QuestConstants.STICKY_RED_GOOP)){
			player.getActionSender().sendMessage("You coat the wooden coin with the sticky red goop.");
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.WOODEN_DISK, 1), new Item(QuestConstants.RED_DISK, 1));
			player.getInventory().removeItem(new Item(QuestConstants.STICKY_RED_GOOP, 1));
			if((player.questStageSub1[this.getId()] & Misc.getActualValue(22)) == 0){
				player.questStageSub1[this.getId()] += Misc.getActualValue(22);
			}
			return true;
		}
		if((item1 == QuestConstants.FULL_JUG && item2 == QuestConstants.NUMBER_5_BUCKET) || (item2 == QuestConstants.FULL_JUG && item1 == QuestConstants.NUMBER_5_BUCKET)){
			player.getActionSender().sendMessage("You empty the jug into the bucket.");
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.FULL_JUG, 1), new Item(QuestConstants.NUMBER_3_JUG, 1));
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.NUMBER_5_BUCKET, 1), new Item(QuestConstants.BUCKET_35, 1));
			return true;
		}
		if((item1 == QuestConstants.FULL_JUG && item2 == QuestConstants.BUCKET_35) || (item2 == QuestConstants.FULL_JUG && item1 == QuestConstants.BUCKET_35)){
			player.getActionSender().sendMessage("You fill the bucket to the brim.");
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.FULL_JUG, 1), new Item(QuestConstants.JUG_13, 1));
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.BUCKET_35, 1), new Item(QuestConstants.FULL_BUCKET, 1));
			return true;
		}
		if((item1 == QuestConstants.JUG_13 && item2 == QuestConstants.NUMBER_5_BUCKET) || (item2 == QuestConstants.JUG_13 && item1 == QuestConstants.NUMBER_5_BUCKET)){
			player.getActionSender().sendMessage("You empty the jug into the bucket.");
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.JUG_13, 1), new Item(QuestConstants.NUMBER_3_JUG, 1));
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.NUMBER_5_BUCKET, 1), new Item(QuestConstants.BUCKET_15, 1));
			return true;
		}
		if((item1 == QuestConstants.FULL_JUG && item2 == QuestConstants.BUCKET_15) || (item2 == QuestConstants.FULL_JUG && item1 == QuestConstants.BUCKET_15)){
			player.getActionSender().sendMessage("You empty the jug into the bucket.");
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.FULL_JUG, 1), new Item(QuestConstants.NUMBER_3_JUG, 1));
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.BUCKET_15, 1), new Item(QuestConstants.BUCKET_45, 1));
			return true;
		}
		if((item1 == QuestConstants.TINDER_BOX && item2 == QuestConstants.STRANGE_OBJECT) || (item2 == QuestConstants.TINDER_BOX && item1 == QuestConstants.STRANGE_OBJECT)){
			player.getActionSender().sendMessage("You light the string of the strange object. It starts to hiss slightly.");
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.STRANGE_OBJECT, 1), new Item(QuestConstants.LIT_STRANGE_OBJECT, 1));
			return true;
		}
		if((item1 == QuestConstants.KNIFE && item2 == QuestConstants.MUSICAL_TREE_BRANCH) || (item2 == QuestConstants.KNIFE && item1 == QuestConstants.MUSICAL_TREE_BRANCH)){
			player.getActionSender().sendMessage("You craft an unstrung lyre out of the branch.");
			player.getUpdateFlags().sendAnimation(1248);
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.MUSICAL_TREE_BRANCH, 1), new Item(QuestConstants.UNSTRUNG_LYRE, 1));
			return true;
		}
		if((item1 == QuestConstants.KEG_OF_BEER && item2 == QuestConstants.LOW_ALCOHOL_KEG) || (item2 == QuestConstants.KEG_OF_BEER && item1 == QuestConstants.LOW_ALCOHOL_KEG)){
			player.getActionSender().sendMessage("You empty the keg and refill it with low alcohol beer.");
			player.getInventory().removeItem(new Item(QuestConstants.KEG_OF_BEER, 1));
			player.getInventory().removeItem(new Item(QuestConstants.LOW_ALCOHOL_KEG, 1));
			player.getInventory().addItemOrDrop(new Item(QuestConstants.KEG_OF_BEER_LOW_ALCOHOL, 1));
			if(longhall.containsBorderIncluded(player.getPosition())){
				Npc[] npcs = Npc.getNpcsInArea(longhall);
				for(Npc npc : npcs){
					npc.getUpdateFlags().setForceChatMessage("What was THAT?");
				}
			}
			return true;
		}
		if((item1 == QuestConstants.GOLDEN_WOOL && item2 == QuestConstants.UNSTRUNG_LYRE) || (item2 == QuestConstants.GOLDEN_WOOL && item1 == QuestConstants.UNSTRUNG_LYRE)){
			player.getActionSender().sendMessage("You attach the golden strings to the lyre.");
			player.getInventory().removeItem(new Item(QuestConstants.UNSTRUNG_LYRE, 1));
			player.getInventory().removeItem(new Item(QuestConstants.GOLDEN_WOOL, 1));
			player.getInventory().addItemOrDrop(new Item(QuestConstants.LYRE, 1));
			return true;
		}
		return false;
	}
	
	public boolean handleItemOnNpc(final Player player, int npcId, int itemId, int stage){
		if(stage < 2)
			return false;
		if((stage & Misc.getActualValue(3)) == 0 && !player.hasItem(QuestConstants.STRANGE_OBJECT)){
			if(npcId == QuestConstants.COUNCIL_WORKMAN && (itemId == QuestConstants.BEER || itemId == QuestConstants.BEER_RELLEKKA)){
				player.getInventory().removeItem(new Item(itemId, 1));
				player.getInventory().addItemOrDrop(new Item(QuestConstants.STRANGE_OBJECT, 1));
				Dialogues.sendDialogue(player, QuestConstants.COUNCIL_WORKMAN, 100, 0);
				return true;
			}
		}
		return false;
	}
	
	boolean soupReady(final Player player){
		if((player.questStageSub1[this.getId()] & Misc.getActualValue(4)) != 0 &&
				(player.questStageSub1[this.getId()] & Misc.getActualValue(5)) != 0 &&
				(player.questStageSub1[this.getId()] & Misc.getActualValue(6)) != 0 &&
				(player.questStageSub1[this.getId()] & Misc.getActualValue(7)) != 0){
			return true;
		}
		return false;
	}
	
	boolean hasMerchantItem(final Player player){
		for(int i = 3698; i < 3711; i++){
			if(player.hasItem(i))
				return true;
		}
		return false;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.BRUNDT_THE_CHIEFTAIN){
			if(stage == 0){
				if(!meetsRequirements(player))
					return false;
				if(chatId == 1){
					player.tempQuestInt = 0;
					player.getDialogue().sendNpcChat("Greetings outerlander!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					if(player.tempQuestInt == 0)
					player.getDialogue().sendOption("What is this place?",
													"Why will no-one talk to me?",
													"Do you have any quests?",
													"Nice hat!");
					if(player.tempQuestInt == 1)
						player.getDialogue().sendOption("Do you have any quests?",
														"Why will no-one talk to me?",
														"Why do you call me outerlander?",
														"I seek an army to destroy Asgarnia!",
														"Who are the Fremennik?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						if(player.tempQuestInt == 0){
							player.getDialogue().sendPlayerChat("What is this place?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(4);
						}
						if(player.tempQuestInt == 1){
							player.getDialogue().sendPlayerChat("Do you have any quests?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(7);
						}
						return true;
					}
					if(optionId == 3){
						if(player.tempQuestInt == 0){
							player.getDialogue().sendPlayerChat("Do you have any quests?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(7);
							return true;
						}
					}
					player.getDialogue().sendStatement("This option is currently missing...");
					player.getDialogue().setNextChatId(2);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("This place? Why, this is Rellekka! Homeland of all",
													"Fremennik! I do not recognise your face outerlander;",
													"Where do you come from?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("That's not important...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.tempQuestInt = 1;
					player.getDialogue().sendNpcChat("Hmmm... I will not press the issue then outerlander.",
													"How may my tribe and I help you?", Dialogues.CONTENT);
					player.getDialogue().setNextChatId(2);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Quests, you say outerlander? Well, I would not call it a",
													"quest as such, but if you are brave of heart and strong",
													"of body, perhaps...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("No, you would not be interested. Forget I said",
													"anything, outerlander.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendOption("Yes, I am interested.",
													"No, I'm not interested.");
					return true;
				}
				if(chatId == 10){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Actually, I would be very interested to hear what you",
															"have to offer.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(11);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("You would? These are unusual sentiments to hear from",
													"an outerlander! My suggestion was going to be that if",
													"you crave adventure and battle,", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("and your heart sings for glory, then perhaps you would",
													"be interested in joining our clan, and becoming a",
													"Fremennik yourself?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendPlayerChat("What would that involve exactly?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("Well, there are two ways to become a member of our",
													"clan and call yourself a Fremennik: be born a",
													"Fremennik, or be voted in by our council of elders.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendPlayerChat("Well, I think I've missed the first way, but how can I",
														"get the council of elders to let me join your",
														"clan?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("Well, that I cannot answer myself. You will need to",
													"speak to each of them and see what they require of you",
													"as proof of your dedication.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendNpcChat("There are twelve council members around this village;",
													"you will need to gain a majority vote of at least seven",
													"councillors in your favour.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendNpcChat("So what you say? Give me the word, and I will tell all",
													"of my tribe of your intentions, be they yea or nay.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 19){
					player.getDialogue().sendOption("I want to become a Fremennik!",
													"I don't want to become a Fremennik.");
					return true;
				}
				if(chatId == 20){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I think I would enjoy the challenge of becoming an",
															"honorary Fremennik. Where and how do I start?", Dialogues.CONTENT);
						questStarted(player);
						player.getDialogue().setNextChatId(21);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}//end of stage 0
			if(stage == 2){
				if(chatId == 21){
					player.getDialogue().sendNpcChat("As I say outerlander, you must find and speak to the",
													"twelve members of the council of elders, and see what",
													"tasks they might set you.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 22){
					player.getDialogue().sendNpcChat("If you can gain the support of seven of the twelve, then",
													"you will be accepted as one of us without question.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 2
			if(stage < 2)
				return false;
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(11)) != 0){
				if(chatId <= 2 && !player.getInventory().playerHasItem(QuestConstants.HUNTERS_MAP, 1))
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I got Sigli's hunting map for you.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getInventory().replaceItemWithItem(new Item(QuestConstants.HUNTERS_MAP, 1), new Item(QuestConstants.FISCAL_STATEMENT, 1));
					player.getDialogue().sendNpcChat("Excellent work outerlander! And so quickly, too! Here,",
													"you may take my financial report promising reduced",
													"sales taxes on all goods.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(10)) != 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(11)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I don't suppose you have any idea where I could find a",
														"guarantee of a reduction on sales taxes, do you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("A reduction on sales taxes? Why, I am the only one in",
													"the Fremennik who may authorise such a thing. What",
													"does an outerlander want with that?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Actually, it's not for me. I need to get it as part of my",
														"trials.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Hmmm. Interesting. Your trials seem to be very",
													"different to those I took as a young lad. Well, I am not",
													"adverse in principle to giving a slight tax break to our",
													"shops.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("There will of course be a shortfall in the tribe's income,",
													"that will need to be made up for elsewhere, however.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("How about this. For many years Sigli has been the only",
													"one in tribe who knows the locations of the best",
													"hunting grounds where game is easiest to catch.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("If you can persuade him to let the entire tribe know",
													"these hunting grounds, then we can increase",
													"productivity within the tribe, and any shortfall caused", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("by lowering sales taxes will be covered. I think this is a",
													"more than fair arrangement to make, don't you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendPlayerChat("Yeah, that sounds very fair.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Speak to Sigli then, and you may have my promise to",
													"reduce our sales taxes. And best of luck with the rest",
													"of your trials.", Dialogues.CONTENT);
					player.questStageSub1[this.getId()] += Misc.getActualValue(11);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(allTasksDone(player)){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Greetings again outerlander! How goes your attempts",
													"to gain votes with the council of elders?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("I have seven members of the council prepared to vote",
														"in my favour now!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("I know outerlander, for I have been closely monitoring",
													"your progress so far!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Then let us put the formality aside, and let me",
													"personally welcome you into the Fremennik! May you",
													"bring us honour!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					List<String> fremennikNames = new ArrayList<String>(player.getGender() == 0 ? maleNames : femaleNames);
					Collections.shuffle(fremennikNames, new Random(player.uniqueRandomInt));
					String name = fremennikNames.get(0);
					player.getDialogue().sendNpcChat("From this day onward, you are outerlander no more!",
													"In honour of your acceptance into the Fremennik You",
													"gain a new name to be known as; You will now be",
													"called "+name, Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().endDialogue();
					questCompleted(player);
					return true;
				}
			}
		}//end of brundt the chieftain
		if(npcId == QuestConstants.MANNI_THE_REVELLER){
			if(stage < 2)
				return false;
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(18)) != 0){
				if(chatId <= 3 && !player.getInventory().playerHasItem(QuestConstants.LEGENDARY_COCKTAIL, 1))
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hey. I got your cocktail for you.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("...It is true! The legendary cocktail! I have waited for",
													"this day ever since I first started drinking!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getInventory().replaceItemWithItem(new Item(QuestConstants.LEGENDARY_COCKTAIL, 1), new Item(QuestConstants.CHAMPIONS_TOKEN, 1));
					player.getDialogue().sendNpcChat("Here outerlander, you may take my token. I will",
													"happily give up my place at the longhalls table of",
													"champions just for a taste of this exquisite beverage!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("It's just a drink...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("No, it is an artform. A drink such as this should be",
													"appreciated, and admired. It is like a fine painting, or a",
													"tasteful sculpture.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("If what I hear is true, then all other drinks become like",
													"unpalatable water in comparison to this!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("I guess you're happy with the trade then!", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(17)) != 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(18)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I don't suppose you have any idea where I could find a",
														"token to allow a seat at the champions table, do you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("As a matter of fact, I do. I have one right here. I",
													"earnt my place here at the longhall for surviving over",
													"5000 battles and raiding parties.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Due to my contribution to the tribe, I am now",
													"permitted to spend my days here in the longhall",
													"listening to the epic tales of the bard, and drinking beer.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("Cool. That sounds pretty sweet! So I guess you don't",
														"want to give it away?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("I think it sounds better than it actually is outerlander.",
													"I miss my glory days of combat on the battlefield. And",
													"to tell you the truth, the beer here isn't great, and the",
													"bards' music is lousy.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("I would happily give up my token if it were not for the",
													"one thing that keeps me here. Our barkeep is one of",
													"the best in the world, and has worked in taverns across",
													"the land.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("When she was younger, she experimented a lot with her",
													"drinks, and invented a cocktail so alcoholic and tasty",
													"that it has become something of a legend to all who",
													"enjoy a drink.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Unfortunately, she decided that cocktails were not a",
													"suitable drink for Fremennik warriors, and vowed to",
													"never again make it.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("I have been here every day since she returned, hoping",
													"that someday she might change her mind and I might",
													"try this legendary cocktail for myself. Alas, it has never",
													"come to pass...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("If you can persuade her to make me her legendary",
													"cocktail, I will be happy to never let another drop of",
													"alcohol pass my lips, and will give you my champions",
													"token.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendPlayerChat("That's all?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("That's all.", Dialogues.CONTENT);
					player.questStageSub1[this.getId()] += Misc.getActualValue(18);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if((stage & Misc.getActualValue(2)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello there!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Hello outerlander. I overheard your conversation with",
													"Brundt just now. You wish to become a member of the",
													"Fremennik?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("That's right! Why, are you on the council?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Do not let my drink-soused appearance fool you, I",
													"earnt my place on the council many years past. I am",
													"always glad to see new blood enter our tribe, and will",
													"happily vote for you.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("Great!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("...Providing you can pass a little test for me. As a",
													"Fremennik, you will need to show cunning, stamina,",
													"fortitude, and an iron constitution. I know of only one",
													"way to test all of these.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("And what's that?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Why, a drinking contest!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("The task is simple enough! You versus me, a stiff drink",
													"each, last man standing wins the trial. So what say you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendOption("Yes",
													"No");
					return true;
				}
				if(chatId == 11){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("A drinking contest? Easy. Set them up, and I'll knock",
															"them back.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(12);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("When you are ready to begin, go and pick up a keg",
													"from that table over there, and come back here.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("We start when you have your keg of beer with you,",
													"and finish when one of us can drink no more and",
													"yields.", Dialogues.CONTENT);
					player.questStage[this.getId()] += Misc.getActualValue(2);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of starting manni's task
			if((stage & Misc.getActualValue(2)) != 0 && (stage & Misc.getActualValue(3)) == 0){
				if(chatId == 1){
					if(player.getInventory().playerHasItem(QuestConstants.KEG_OF_BEER, 1) || player.getInventory().playerHasItem(QuestConstants.KEG_OF_BEER_LOW_ALCOHOL, 1)){
						player.getDialogue().sendNpcChat("Ah, I see you have your keg of beer. Are we ready to",
														"drink against each other?", Dialogues.CONTENT);
						return true;
					}
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Yes",
													"No");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yes, let's start this drinking contest!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("As you wish outerlander; I will drink first, then you will",
													"drink.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					if(player.getInventory().playerHasItem(QuestConstants.KEG_OF_BEER, 1) || player.getInventory().playerHasItem(QuestConstants.KEG_OF_BEER_LOW_ALCOHOL, 1)){
					player.getActionSender().removeInterfaces();
					player.setStopPacket(true);
					Npc npc = Npc.getNpcById(QuestConstants.MANNI_THE_REVELLER);
					npc.getUpdateFlags().sendAnimation(1327);
					if(player.getInventory().playerHasItem(QuestConstants.KEG_OF_BEER_LOW_ALCOHOL, 1))
						player.tempQuestInt = QuestConstants.KEG_OF_BEER_LOW_ALCOHOL;
					player.getActionSender().sendMessage("The Fremennik drinks his tankard first. He staggers a little bit.");
					final int qID = this.getId();
					final Tick timer1 = new Tick(6) {
				            @Override
				            public void execute() {
				            	//System.out.println("chek");
				            	if(player.tempQuestInt == QuestConstants.KEG_OF_BEER_LOW_ALCOHOL){
				            		player.getInventory().removeItem(new Item(QuestConstants.KEG_OF_BEER_LOW_ALCOHOL, 1));
				            		player.getActionSender().sendMessage("You drink from your keg. You don't feel at all drunk.");	
				            	}else{
				            		player.getInventory().removeItem(new Item(QuestConstants.KEG_OF_BEER, 1));
				            		player.getActionSender().sendMessage("You drink from your keg. You feel extremely drunk...");
				            	}
				            	player.getUpdateFlags().sendAnimation(1330);
				            	World.getTickManager().submit(timer2);
				                stop();
				            }
				            
				            final Tick timer2 = new Tick(7) {
					            @Override
					            public void execute() {
					            	player.setStopPacket(false);
					            	if((player.questStageSub1[qID] & Misc.getActualValue(1)) == 0)
					            		player.questStageSub1[qID] += Misc.getActualValue(1);
					            	if(player.tempQuestInt == QuestConstants.KEG_OF_BEER_LOW_ALCOHOL){
					            		if((player.questStage[qID] & Misc.getActualValue(3)) == 0)
					            			player.questStage[qID] += Misc.getActualValue(3);
					            		player.getDialogue().sendPlayerChat("Aaaah, lovely stuff. So you want to get the next round",
					            											"in, or shall I? You don't look so good there!", Dialogues.CONTENT);
					            		player.getActionSender().sendMessage("Congratulations! You have completed the Revellers' trial.");
					            		player.getDialogue().setNextChatId(8);
					            	} else {
					            		player.getDialogue().sendPlayerChat("Ish no' fair! (hic) I canna drink another drop! I alsho",
					            											"feel veddy, veddy ill...", Dialogues.CONTENT);
					            	}
					                stop();
					            }
					        };
				            
						};
					World.getTickManager().submit(timer1);
					return true;
					}
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("I guessh I win then ouddaladder! (hic) Niche try",
													"anyway!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Come back if'n you fanshy a rematch! (hic) Jusht let me",
													"have a coffee firsht...", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//manni part 2
			if((stage & Misc.getActualValue(3)) != 0){
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Wassha? Guh? You drank that whole keg! But it dinna",
													"affect you at all! I conshede! You can probably",
													"outdrink me!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("I jusht can't (hic) believe it! Thatsh shome mighty fine",
													"drinking legs you got! Anyone who can drink like",
													"THAT getsh my vote atta consh.. counsh... gets my",
													"vote!", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//manni part 3
		}//end of manni the revelller
		if(npcId == QuestConstants.POISON_SALESMAN){
			if(stage < 2)
				return false;
			if((stage & Misc.getActualValue(3)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Howdy! You seem like someone with discerning taste!",
													"Howsabout you try my brand new range of alcohol?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Didn't you used to sell poison?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("That I did, indeed! Peter Potter's Patented",
													"Multipurpose poison! A miracle of modern apothecarys!",
													"My exclusive concoction has been tested on...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("Uh, yeah, I've already heard the sales pitch.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Sorry stranger, old habits die hard I guess.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("So you don't sell poison any more?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Well, I would, but I ran out of stock. Business wasn't",
													"helped with that stuff that happened up at the Sinclair",
													"Mansion much either, I'll be honest.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("So, being the man of enterprise that I am I decided to",
													"branch out a little bit!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendPlayerChat("Into alcohol?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("Absolutely! The basic premise between alcohol and poison",
													"is pretty much the same, after all! The difference is that",
													"my alcohol has a unique property others do not!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendPlayerChat("And what is that?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendStatement("The salesman takes a deep breath.");
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("Ever been too drunk to find your own home? Ever",
													"wished that you could party away all night long, and",
													"still wake up fresh as a daisy the next morning?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendNpcChat("Thanks to the miracles of modern magic we have come",
													"up with just the solution you need! Peter Potter's",
													"Patented Party Potions!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("It looks like beer! It tastes just like beer! It smells",
													"just like beer! But... it's not beer!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendNpcChat("Our mages have mused for many moments to bring",
													"you this miracle of modern magic! It has all the great",
													"tastes you'd expect, but contains absolutely no alcohol!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendNpcChat("That's right! You can drink Peter Potters Patented",
													"Party Potion as much as you want, and suffer",
													"absolutely no ill effects whatsoever!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 19){
					player.getDialogue().sendNpcChat("The clean fresh taste you know you can trust, from",
													"the people who brought you; Peter Potters Patented",
													"multipurpose poison, Peter Potters peculiar paint packs", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 20){
					player.getDialogue().sendNpcChat("and Peter Potters paralysing panic pins. Available now",
													"from all good stockists! Ask your local bartender now,",
													"and experience the taste revolution of the century!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 21){
					player.getDialogue().sendStatement("He seems to have finished for the time being.");
					return true;
				}
				if(chatId == 22){
					player.getDialogue().sendPlayerChat("So... when you say 'all good stockists'...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 23){
					player.getDialogue().sendNpcChat("Yes?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 24){
					player.getDialogue().sendPlayerChat("How many inns actually sell this stuff?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 25){
					player.getDialogue().sendNpcChat("Well... nobody has actually bought any yet. Everyone I",
													"try and sell it to always asks me what exactly the point",
													"of beer that has absolutely no effect on you is.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 26){
					player.getDialogue().sendPlayerChat("So what is the point?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 27){
					player.getDialogue().sendNpcChat("Well... Um... Er... Hmmm. You, er, don't get drunk.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 28){
					player.getDialogue().sendPlayerChat("I see...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 29){
					player.getDialogue().sendNpcChat("Aw man... You don't want any now do you? I've really",
													"tried to push this product, but I just don't think the",
													"world is ready for beer that doesn't get you drunk.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 30){
					player.getDialogue().sendNpcChat("I'm a man ahead of my time I tell you! It's not that",
													"my products are bad, it's that they're too good for the",
													"market!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 31){
					player.getDialogue().sendPlayerChat("Actually, I would like some. How much do you want for",
														"it?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 32){
					player.getDialogue().sendNpcChat("Y-you would??? Um, okay! I knew I still had the old",
													"salesmans skills going on!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 33){
					player.getDialogue().sendNpcChat("I'll sell you a keg of it for only 250 gold pieces! So",
													"what do you say?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 34){
					player.getDialogue().sendOption("Yes",
													"No");
					return true;
				}
				if(chatId == 35){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yes please!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(36);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 36){
					if(player.getInventory().playerHasItem(QuestConstants.COINS, 250)){
						player.getInventory().removeItem(new Item(QuestConstants.COINS, 250));
						player.getInventory().addItemOrDrop(new Item(QuestConstants.LOW_ALCOHOL_KEG, 1));
					}
					player.getDialogue().endDialogue();
					return false;
				}
			}
		}//end of poison salesman
		if(npcId == QuestConstants.COUNCIL_WORKMAN){
			if(chatId == 100){
				player.getDialogue().sendNpcChat("Ta very much, like. That'll hit the spot nicely. Here,",
												"you can have this. I picked it up as a souvenir on me",
												"last hols.", Dialogues.CONTENT);
				return true;
			}
			if(chatId == 101){
				player.getDialogue().sendPlayerChat("What is it?", Dialogues.CONTENT);
				return true;
			}
			if(chatId == 102){
				player.getDialogue().sendNpcChat("I dunno rightly, but if you use a tinderbox on it, it",
												"don't half make a loud noise!", Dialogues.CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		}
		if(npcId == QuestConstants.OLAF_THE_BARD){
			if(stage < 2)
				return false;
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(9)) != 0){
				if(chatId <= 4 && !player.getInventory().playerHasItem(QuestConstants.STURDY_BOOTS, 1))
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello Olaf. Do you have a beautiful love song written",
														"for me?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("That depends outerlander... Do you have some new",
													"boots for me? My feet get so tired roaming the land...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("As a matter of fact - I do!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getInventory().replaceItemWithItem(new Item(QuestConstants.STURDY_BOOTS, 1), new Item(QuestConstants.FREMENNIK_BALLAD, 1));
					player.getDialogue().sendNpcChat("Oh! Superb! Those are great! They're just what I was",
													"looking for! Here, take this song with my compliments!",
													"It is one of my finest works yet!", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(8)) != 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(9)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I don't suppose you have any idea where I could find a",
														"love ballad, do you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Well, as official Fremennik bard, it falls within my remit",
													"to compose all music for the tribe. I am fully versed in",
													"all the various types of romantic music.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Great! Can you write me one then?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Well... normally I would be thrilled at the chance to",
													"show my skill as a poet in composing a seductively",
													"romantic ballad...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("let me guess; here comes the 'but'.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("...but unfortunately I cannot concentrate fully upon",
													"my work recently.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("Why is that then?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("It is these old worn out shoes of mine... As a bard I",
													"am expected to wander the lands, singing of the glorious",
													"battles of our warriors.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("If you can find me a pair of sturdy boots to replace",
													"these old worn out ones of mine, I will be happy to",
													"spend the time on composing you a romantic ballad.", Dialogues.CONTENT);
					player.questStageSub1[this.getId()] += Misc.getActualValue(9);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if((stage & Misc.getActualValue(4)) == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hello? Yes? You want something, outerlander?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("Are you a member of the council?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Why, indeed I am, outerlander! My talents as an",
													"exemplary musician made it difficult for them not to",
													"accept me! Why do you wish to know this?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("Well, I ask because I am currently doing the",
														"Fremennik trials so as to join your clan. I need seven",
														"of the twelve council of elders to vote for me.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Ahhh... and you wish to earn my vote? I will gladly",
													"accept you as a Fremennik should you be able to prove",
													"yourself to have a little musical ability!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("So how would I do that?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Why, by playing in our longhall! All you need to do is",
													"impress the revellers there with a few verses of an epic",
													"of your own creation!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("So what say you outerlander? Are you up for the",
													"challenge?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendOption("Yes",
													"No");
					return true;
				}
				if(chatId == 10){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Sure! This certainly sounds pretty easy to accomplish -",
															"I'll have your vote in no time!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(11);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("That is great news outerlander! We always need more",
													"music lovers here!", Dialogues.CONTENT);
					player.questStage[this.getId()] += Misc.getActualValue(4);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of olaf start
			if((stage & Misc.getActualValue(4)) != 0 && (stage & Misc.getActualValue(5)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("So how would I go about writing this epic?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Well, first of all you are going to need an instrument.",
													"Like all true bards you are going to have to make this",
													"yourself.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("How do I make an instrument?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Well, it is a long and drawn-out process. Just east of",
													"this village there is an unusually musical tree that can",
													"be used to make very high quality instruments.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Cut a piece from it, and then carve it into a special",
													"shape that will allow you to string it. Using a knife as",
													"you would craft any other wooden object would be best",
													"for this.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("Then what do I need to do?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Next you will need to string your lyre. There is a troll",
													"to the South-east who has some golden wool. I would",
													"not recommend using anything else to string your lyre",
													"with.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendPlayerChat("Anything else?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Well, when you have crafted your lyre you will need",
													"the blessing of the Fossegrimen to tune your lyre to",
													"perfection before you even consider a public",
													"performance.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendPlayerChat("Who or what is Fossegrimen?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("Fossegrimen is a lake spirit that lives just a little way",
													"South-west of this village. Make her an offering of fish,",
													"and you will then be ready for your performance.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("Make sure you give her suitable offering however. If",
													"the offering is found to be unworthy, then you may",
													"find yourself unable to play your lyre with any skill at",
													"all!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendPlayerChat("So what would be a worthy offering?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("A raw shark, manta ray, or sea turtle should be",
													"sufficient as an offering.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendPlayerChat("Okay, what else do I need to do?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("When you have crafted your lyre and been blessed by",
													"Fossegrimen, then you will finally be ready to make",
													"your performance to the revellers at the longhall.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendNpcChat("Head past the bouncers and onto the stage, and then",
													"begin to play. If all goes well, you should find the music",
													"spring to your mind and sing your own epic on the",
													"spot.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendNpcChat("I will observe both you and the audience, and if you",
													"show enough talent, I will happily vote in your favour at",
													"the council of elders.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 19){
					player.getDialogue().sendNpcChat("Is that clear enough, outerlander? Would you like me",
													"to repeat anything?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 20){
					player.getDialogue().sendOption("Remind me about crafting a lyre",
													"Remind me about Fossegrimen",
													"Remind me about playing on the stage",
													"I don't need a reminder");
					return true;
				}
				if(chatId == 21){
					if(optionId == 4){
						player.getDialogue().endDialogue();
						return false;
					}else{
						player.getDialogue().sendStatement("This option is currently missing...");
						player.getDialogue().setNextChatId(20);
						return true;
					}
				}
				if(chatId == 100){
					player.getDialogue().sendNpcChat("Wow! That was awesome! You are one of the greatest",
													"bards I have ever had the pleasure of watching",
													"performing!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 101){
					player.getDialogue().sendNpcChat("You have certainly earned my vote! I hope we can",
													"duet together soon!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 102){
					if((player.questStage[this.getId()] & Misc.getActualValue(5)) == 0)
	            		player.questStage[this.getId()] += Misc.getActualValue(5);
					player.getActionSender().sendMessage("Congratulations! You have completed the Bard's Trial!");
					player.getActionSender().sendMessage("As you finished playing you felt Fossegrimen's power leave you...");
					player.getActionSender().sendMessage("You feel the musical ability from the Fossegrimen leave you...");
					if(player.getInventory().playerHasItem(QuestConstants.ENCHANTED_LYRE, 1)){
						player.getInventory().removeItem(new Item(QuestConstants.ENCHANTED_LYRE, 1));
						player.getInventory().addItemOrDrop(new Item(QuestConstants.LYRE, 1));
					}
					player.getDialogue().endDialogue();
					return false;
				}
			}//olaf part 2
		}//end of olaf the bard
		if(npcId == QuestConstants.LALLI){
			if(stage < 2)
				return false;
			if((player.questStageSub1[this.getId()] & Misc.getActualValue(3)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello there.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Bah! Puny humans always try steal Lallis' golden",
													"apples! You go away now!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Actually, I'm not after your golden apples. I was",
														"wondering if I could have some golden wool; I need it",
														"to string a lyre.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Ha! You not fool me human! Me am smart! Other",
													"trolls so jealous of how brainy I are, they kick me out",
													"of camp and make me live down here in cave! But me",
													"have last funny!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Me find golden apples on tree and me build wall to stop",
													"anyone who not Lalli eating lovely golden apples! Did",
													"me not tell you I are smart?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("Yes, yes, you are incredibly clever. Now please can I",
														"have some golden wool?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Hum, me think you not really think I are clever. Me",
													"think you is trying trick Lalli. Me not like you as much",
													"as other human. He give me present. I give him wool.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendOption("Other human?",
													"No, honest, you're REALLY clever.",
													"Can I give you a present?");
					return true;
				}
				if(chatId == 9){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Other human? You mean someone else has been there",
															"and you gave them wool?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(10);
						return true;
					}else{
						player.getDialogue().sendStatement("This option is currently missing...");
						player.getDialogue().setNextChatId(8);
					}
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Human call itself Askeladden! It not trick Lalli, Lalli do",
													"good deal with human! Stupid human get some dumb",
													"wool, but did not get golden apples!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendPlayerChat("I see... okay, well, bye!", Dialogues.CONTENT);
					player.questStageSub1[this.getId()] += Misc.getActualValue(3);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if((player.questStageSub1[this.getId()] & Misc.getActualValue(3)) != 0){
				if(soupReady(player) && !player.hasItem(QuestConstants.GOLDEN_FLEECE) && !player.hasItem(QuestConstants.GOLDEN_WOOL) &&
						!player.hasItem(QuestConstants.LYRE) && !player.hasItem(QuestConstants.ENCHANTED_LYRE) && !player.hasItem(QuestConstants.ENCHANTED_LYRE1)){
					if(chatId == 1){
						player.getDialogue().sendPlayerChat("Hello there.", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 2){
						player.getDialogue().sendNpcChat("Your soup very tasty, human! But me still not want",
														"trade golden apples for your stone. Me think pet rock",
														"get jealous.", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 3){
						player.getDialogue().sendPlayerChat("I. DON'T. WANT. ANY. GOLDEN APPLES. ALL.",
															"I. WANT. IS. A. GOLDEN. FLEECE.", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 4){
						player.getDialogue().sendNpcChat("Gee, sorry human, all you have do is ask, me not need",
														"you to shout. You act like you think Lalli am stupid or",
														"something...", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 5){
						player.getInventory().addItemOrDrop(new Item(QuestConstants.GOLDEN_FLEECE, 1));
						player.getDialogue().sendNpcChat("Here you go. Hah! Me trick you human! All you got is",
														"worthless golden fleece! Me got very rare soup-making",
														"stone!", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 6){
						player.getDialogue().sendPlayerChat("Glad you're happy Lalli!", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}
				}
				if(chatId == 100){
					player.getDialogue().sendNpcChat("It am ready now?", Dialogues.CONTENT);
					if(soupReady(player)){
						player.getDialogue().setNextChatId(102);
					}
					return true;
				}
				if(chatId == 101){
					player.getDialogue().sendPlayerChat("Not just yet...", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 102){
					player.getDialogue().sendPlayerChat("Indeed it is. Try it and see.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 103){
					player.getDialogue().sendNpcChat("Hmm... YUM! That are delicious! Me never know",
													"human know how to make soup out of stone? It some special",
													"stone?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 104){
					player.getDialogue().sendPlayerChat("Indeed it is. But I'm willing to trade it.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 105){
					player.getDialogue().sendNpcChat("Let me think about that, me like to think.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of lalli
		if(npcId == QuestConstants.ASKELADDEN){
			if(stage < 2)
				return false;
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(19)) != 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(20)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I don't suppose you have any idea where I could find a",
														"written promise from Askeladden to stay out of the",
														"Longhall?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("What? I can't believe she asked you to get a written",
													"promise from me to stay out!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Yup, she really did.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Awwwwwww.... but the longhall is just SO MUCH FUN!",
													"I'd live there if I could! I suppose you really need that",
													"promise to help become a Fremennik, huh?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("Yeah, I really do...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Well I'll tell you what buddy; As it's you, I'll give you",
													"that written promise. All I ask in return for it is a",
													"measly 5000 gold. What do you say?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendOption("Yes",
													"No");
					return true;
				}
				if(chatId == 8){
					if(optionId == 1 && player.getInventory().playerHasItem(QuestConstants.COINS, 5000)){
						player.getDialogue().sendPlayerChat("That's all you want in return? Sure thing. Here you",
															"go.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 9){
					if(player.getInventory().playerHasItem(QuestConstants.COINS, 5000)){
						player.getInventory().removeItem(new Item(QuestConstants.COINS, 5000));
						player.getInventory().addItemOrDrop(new Item(QuestConstants.PROMISSORY_NOTE, 1));
						player.getDialogue().sendNpcChat("Done, and done. Let me know if you got any more",
														"cash burning a hole in your pocket I can relieve you",
														"of, buddy.", Dialogues.CONTENT);
						player.questStageSub1[this.getId()] += Misc.getActualValue(20);
						player.getDialogue().endDialogue();
						return true;
					}
				}
			}
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(20)) != 0 && !hasMerchantItem(player)){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Here you go, Don't lose it this time.", Dialogues.CONTENT);
					player.getInventory().addItemOrDrop(new Item(QuestConstants.PROMISSORY_NOTE, 1));
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if((player.questStageSub1[this.getId()] & Misc.getActualValue(3)) != 0 && (stage & Misc.getActualValue(5)) == 0 && !player.hasItem(QuestConstants.PET_ROCK) && !player.hasItem(QuestConstants.GOLDEN_FLEECE) &&
					!player.hasItem(QuestConstants.GOLDEN_WOOL) && !player.hasItem(QuestConstants.LYRE) && !player.hasItem(QuestConstants.ENCHANTED_LYRE)){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello there. I understand you managed to get some",
														"golden wool from Lalli?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("HAHAHA! Yeah, that Lalli... what a maroon!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("So how did you manage to get the wool?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Well, as you know, I am doing the same trials that you",
													"are as part of my test of manhood, and that troll is the",
													"only one who can get that wool.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("You might have noticed he's kind of... messed in the",
													"head buddy! He's real paranoid about people stealing his",
													"golden apples, isn't he?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("Indeed he is. So how did you manage to get some",
														"golden wool from him?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("It was easy buddy! I persuaded him he needed a pet to",
													"help him guard his apples. A pet that would never sleep!",
													"A pet that would never need food, or exercise!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendPlayerChat("What pet is this then?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("A pet ROCK!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Man, can you believe that stupid troll traded me some",
													"of his golden wool for a worthless ROCK?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("Buddy, I hafta say; if brains were explosives, that troll",
													"wouldn't have enough to blow his nose!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendPlayerChat("Do you have any spare rocks then?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.PET_ROCK, 1));
					player.getDialogue().sendNpcChat("Sure thing buddy, although I have to say, I doubt even",
													"that troll is stupid enough to fall for the SAME trick",
													"TWICE in a row! You can try anyway though!", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of askeladden
		if(npcId == QuestConstants.FOSSEGRIMEN){
			if(stage < 2)
				return false;
			if(chatId == 100){
				player.getDialogue().sendNpcChat("Many thanks for the offering outerlander. Please",
												"accept this gift as your ability to play the lyre...", Dialogues.CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		}//end of fossegrimen
		if(npcId == QuestConstants.LONGHALL_BOUNCER){
			if(chatId == 100){
				player.getDialogue().sendNpcChat("Yeah, you're good to go through. Olaf tells me you're",
												"some kind of outerlander bard here on tour. I doubt",
												"you're worse than Olaf is.", Dialogues.CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
			if(chatId == 101){
				player.getDialogue().sendNpcChat("Hey! You have no business in there!", Dialogues.CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		}//end of longhall bouncer
		if(npcId == QuestConstants.SIGMUND_THE_MERCHANT){
			if(stage < 2)
				return false;
			if((stage & Misc.getActualValue(6)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello there!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Hello outerlander.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Are you a member of the council?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("That I am outerlander; it is a position that brings my",
													"family and I pride.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("I was wondering if I can count on your vote at the",
														"council of elders?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("You wish to become a Fremennik? I may be persuaded",
													"to swing my vote to your favour, but you will first",
													"need to do a little task for me.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("How did I know it wouldn't be that simple for your",
														"vote?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Calm yourself outerlander. It is but a small task",
													"really... I simply require a flower.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendPlayerChat("A flower? What's the catch?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("The catch? Well... it is not just any flower. Someone in",
													"this town has an extremely rare flower from a far off",
													"land that they picked up on their travels.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("I would like you to demonstrate your merchanting skills",
													"to me, by persuading them to part with it, and then give",
													"it to me for my vote.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendPlayerChat("Well... I guess that doesn't sound too hard...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("Excellent! You will obtain this rare flower for me then?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendOption("Yes",
													"No");
					return true;
				}
				if(chatId == 15){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Okay. I don't think this will be too difficult. Any",
															"suggestions on where to start looking for this flower?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(16);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("Ah, well outerlander, if I knew where to start looking I",
													"would simply do it myself!", Dialogues.CONTENT);
					player.questStage[this.getId()] += Misc.getActualValue(6);
					return true;
				}
			}//sigmund start
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0){
				if(chatId == 17){
					player.getDialogue().sendPlayerChat("No help at ALL?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendNpcChat("We are a very insular clan, so I would not expect you",
													"to have to leave this town to find whatever you need.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId <= 2 && !player.getInventory().playerHasItem(QuestConstants.EXOTIC_FLOWER, 1))
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello there!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("Here's that flower you wanted.", Dialogues.CONTENT);
					player.getInventory().removeItem(new Item(QuestConstants.EXOTIC_FLOWER, 1));
					player.questStage[this.getId()] += Misc.getActualValue(7);
					return true;
				}
			}//sigmund part 2
			if((stage & Misc.getActualValue(7)) != 0){
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Incredible! Your merchanting skills might even match",
													"my own! I have no choice but to recommend you to",
													"the council of elders!", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//sigmund part 3
		}//end of sigmund the merchant
		if(npcId == QuestConstants.SAILOR){
			if(stage < 2)
				return false;
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(8)) != 0){
				if(chatId <= 2 && !player.getInventory().playerHasItem(QuestConstants.FREMENNIK_BALLAD, 1))
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("You'll be glad to know I have had a love song written",
														"just for you by Olaf. So can I have that flower of",
														"yours now?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getInventory().replaceItemWithItem(new Item(QuestConstants.FREMENNIK_BALLAD, 1), new Item(QuestConstants.EXOTIC_FLOWER, 1));
					player.getDialogue().sendNpcChat("Oh. It's by Olaf? Hmm. Well, a deal's a deal. I just",
													"hope it's better than the usual rubbish he comes up with,",
													"or my chances are worse than ever.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(8)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I don't suppose you have any idea where I could find a",
														"rare flower from across the sea, do you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Ah! Even the outerlanders have heard of my mysterious",
													"flower! I found it in a country far far away from here!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Can I buy it from you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("I'm afraid not, outerlander. There is a woman in this",
													"village whose heart I seek to capture, and I think giving",
													"her this strange flower might be my best bet with her.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("Maybe you could let me have the flower and do",
														"something else to impress her?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Hmm... that is not a totally stupid idea outerlander. I",
													"know she is a lover of music, and a romantic ballad",
													"might be just the thing with which to woo her.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Unfortunately I don't have a musical bone in my entire",
													"body, so someone else will have to write it for me.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendPlayerChat("So if I can find someone to write you a romantic",
														"ballad, you will give me your flower?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("That sounds like a fair deal to me, outerlander.", Dialogues.CONTENT);
					player.questStageSub1[this.getId()] += Misc.getActualValue(8);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of sailor
		if(npcId == QuestConstants.YRSA){
			if(stage < 2)
				return false;
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(10)) != 0){
				if(chatId <= 2 && !player.getInventory().playerHasItem(QuestConstants.FISCAL_STATEMENT, 1))
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello. Can I have those boots now? Here is a written",
														"statement from Brundt outlining future tax burdens",
														"upon Fremennik merchants and shopkeepers for the",
														"year.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getInventory().replaceItemWithItem(new Item(QuestConstants.FISCAL_STATEMENT, 1), new Item(QuestConstants.STURDY_BOOTS, 1));
					player.getDialogue().sendNpcChat("Certainly! Let me have a look at what he has written",
													"here, just give me a moment...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Yes, that all appears in order. Tell Olaf to come to me",
													"next time for shoes!", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(9)) != 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(10)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I don't suppose you have any idea where I could find",
														"some custom sturdy boots, do you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Well, I don't usually have many shoes in stock here in",
													"my little clothes shop... I will be able to make you up a",
													"pair if you are really desperate though?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("They're not for me... I need them for Olaf.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Oh, that foolish bard... Why didn't he just ask me to",
													"make him some? It is his stupid pride, I believe!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("I will tell you what I will do outerlander; I know that",
													"you must have the ear of the chieftain for him to",
													"consider you as worthy of becoming a Fremennik by",
													"trial.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("I will make you a pair of sturdy boots for Olaf if you",
													"will persuade him to reduce the sales tax placed upon all",
													"Fremennik shopkeepers. It does nothing but hurt my",
													"business now.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("Okay, I will see what I can do.", Dialogues.CONTENT);
					player.questStageSub1[this.getId()] += Misc.getActualValue(10);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of yrsa
		if(npcId == QuestConstants.SIGLI_THE_HUNTSMAN){
			if(stage < 2)
				return false;
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(12)) != 0){
				if(chatId <= 3 && !player.getInventory().playerHasItem(QuestConstants.CUSTOM_BOW_STRING, 1))
					return false;
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Greetings outerlander.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("Here. I have your bowstring. Give me your map to the",
														"hunting grounds.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getInventory().replaceItemWithItem(new Item(QuestConstants.CUSTOM_BOW_STRING, 1), new Item(QuestConstants.HUNTERS_MAP, 1));
					player.getDialogue().sendNpcChat("Well met, outerlander. I see some hunting potential",
													"within you. Here, take my map, I was getting too",
													"dependent on it for my skill anyway.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(11)) != 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(12)) == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Greetings outerlander.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("I don't suppose you have any idea where I could find a",
														"map to unspoiled hunting grounds, do you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Well, of course I do. I wouldn't be much of a",
													"huntsman if I didn't know where to find my prey now,",
													"would I outerlander?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("No, I guess not. So can I have it?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Directions to my hunting grounds could mean the end",
													"of my livelihood. The only way I would be prepared to",
													"give them up would be...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("What? Power? Money? Women? Wine?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("...a new string for my hunting bow. Not just any",
													"bowstring; I need a custom bowstring, balanced for my",
													"bow precisely to keep my hunt competitive.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Only in this way would I allow the knowledge of my",
													"hunting grounds to be passed on to strangers.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendPlayerChat("So where would I get that?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("I have no idea. But then again, I'm happy with my old",
													"bowstring and being the only person who knows where",
													"my hunting ground is.", Dialogues.CONTENT);
					player.questStageSub1[this.getId()] += Misc.getActualValue(12);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if((stage & Misc.getActualValue(8)) == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("What do you want outerlander?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("Are you a member of the council?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("The Fremennik council of elders? I am pleased to say",
													"that I am. My value as a huntsman is recognised by",
													"my position there.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("I was wondering if I could persuade you to vouch for",
														"me as a member of your clan?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("You? ... well... I am not totally against the idea",
													"outerlander. If you can demonstrate some hunting skills",
													"then perhaps I may offer my vote.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("How can I prove my hunting skills to you? I can go",
														"kill, like, a hundred chickens for you right now!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Chickens? You think that would impress me?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendPlayerChat("Cows then? I can kill cows until, er, the cows come",
														"home.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("No. The prey I have in mind for you to prove your",
													"worth to me is something far more dangerous. Far",
													"more difficult. Far more deadly.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendPlayerChat("Not... Giant Rats?!?!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("I suspect you are mocking me outerlander. You will",
													"need to prove your skill as a hunter to me by tracking",
													"and defeating... The Draugen.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendOption("What's a Draugen?",
													"Forget it.");
					return true;
				}
				if(chatId == 13){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("What's a Draugen? Some kind of cheap barbarian rip-",
															"off of a dragon?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(14);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("Hmmm. No, the words are slightly similar I suppose,",
													"but they are very different creatures.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendNpcChat("The Draugen is an evil ghost from Fremennik",
													"mythology, that devours the souls of those brave",
													"warriors who meet their ends at sea.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("It stalks the coastlines, invisible to all. It brings us bad",
													"fortunes, and curses our journeys across the seas. It is",
													"also unkillable by normal means.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendPlayerChat("...Let me get this straight; You want me to hunt an",
														"unkillable, invincible, and invisible enemy? How am I",
														"supposed to do that?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendNpcChat("Well outerlander, should you accept my challenge I will",
													"show you a special hunter's trick that will help you. Do",
													"you accept the challenge?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 19){
					player.getDialogue().sendOption("Yes",
													"No");
					return true;
				}
				if(chatId == 20){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Well, I need every vote I can get in the council of",
															"elders, but this certainly sounds impossible to do...", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(21);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 21){
					player.getDialogue().sendNpcChat("Not at all outerlander. The Draugen is indeed",
													"impossible to kill, but that is not the same as being",
													"impossible to fight against.", Dialogues.CONTENT);
					player.questStage[this.getId()] += Misc.getActualValue(8);
					return true;
				}
			}//sigli part 1
			if((stage & Misc.getActualValue(8)) != 0 && (stage & Misc.getActualValue(9)) == 0){
				if(chatId == 1){
					if(!player.hasItem(QuestConstants.HUNTERS_TALISMAN_EMPTY) && !player.hasItem(QuestConstants.HUNTERS_TALISMAN_FULL)){
						player.getInventory().addItemOrDrop(new Item(QuestConstants.HUNTERS_TALISMAN_EMPTY, 1));
						player.getDialogue().sendNpcChat("Don't lose it this time!", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						return true;
					} else if (player.hasItem(QuestConstants.HUNTERS_TALISMAN_FULL)){
						player.getDialogue().sendNpcChat("I saw the entire hunt. Let me take that talisman from",
														"you; I would be honoured to speak out for you to our",
														"council of elders after such a hunt, outerlander.", Dialogues.CONTENT);
						return true;
					}
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("Thanks!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					if(player.hasItem(QuestConstants.HUNTERS_TALISMAN_FULL)){
						player.getInventory().removeItem(new Item(QuestConstants.HUNTERS_TALISMAN_FULL, 1));
						player.questStage[this.getId()] += Misc.getActualValue(9);
					}
					player.getDialogue().endDialogue();
					return false;
				}
				if(chatId == 22){
					player.getDialogue().sendNpcChat("Every time he takes a Fremennik life, he gains in",
													"power, so to keep it from becoming too powerful we",
													"hunters hunt it and steal its life force.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 23){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.HUNTERS_TALISMAN_EMPTY, 1));
					player.getDialogue().sendNpcChat("We do this with a special talisman. Here, take it; it will",
													"let you track the Draugen while it's invisible, and when",
													"you defeat it will absorb its essence.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 24){
					player.getDialogue().sendNpcChat("I want you to track the Draugen, defeat it, and store",
													"its essence in that talisman for me. If you can do this",
													"important task for my clan, I will vote for you.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 25){
					player.getDialogue().sendNpcChat("Take care of the talisman, and see me when you have",
													"completed this task.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//sigli part 2
		}//end of sigli the huntsman
		if(npcId == QuestConstants.SKULGRIMEN){
			if(stage < 2)
				return false;
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(13)) != 0){
				if(chatId <= 2 && !player.getInventory().playerHasItem(QuestConstants.UNUSUAL_FISH, 1))
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hi there. I got your fish, so can I have that bowstring",
														"for Sigli now?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getInventory().replaceItemWithItem(new Item(QuestConstants.UNUSUAL_FISH, 1), new Item(QuestConstants.CUSTOM_BOW_STRING, 1));
					player.getDialogue().sendNpcChat("Ohh... That's a nice fish. Very pleased. Here. Take the",
													"bowstring. You fulfilled agreement. only fair I do same.",
													"Good work outerlander.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Thanks!", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(12)) != 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(13)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I don't suppose you have any idea where I could find a",
														"finely balanced custom bowstring, do you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Aye, I have a few in stock. What would an outerlander",
													"be wanting with equipment like that?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("It's for Sigli. It needs to be weighted precisely to suit",
														"his hunting bow.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("For Sigli eh? Well, I made his bow in the first place, so",
													"I'll be able to select the right string for you... just one",
													"small problem.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("What's that?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("This string you'll be wanting... Very rare. Take a lot of",
													"time to recreate. Not sure you have the cash for it.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("Then maybe you'll accept something else...?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Heh. Good thinking outerlander. Well, it's true, there is",
													"more to life than just making money. Making weapons",
													"is good money, but it's not why I do it.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("I'll tell you what. I heard a rumour that one of the",
													"fishermen down by the docks caught some weird looking",
													"fish as they were fishing the other day.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("From what I hear this fish is unique. Nobody's ever",
													"seen its like before. This intrigues me. I'd like to have it",
													"for myself. Make a good trophy.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("You get me that fish, I give you the bowstring. What",
													"do you say? We got a deal?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendPlayerChat("Sounds good to me.", Dialogues.CONTENT);
					player.questStageSub1[this.getId()] += Misc.getActualValue(13);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of skulgrimen
		if(npcId == QuestConstants.FISHERMAN_RELLEKKA){
			if(stage < 2)
				return false;
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(14)) != 0){
				if(chatId <= 2 && !player.getInventory().playerHasItem(QuestConstants.SEA_FISHING_MAP, 1))
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Here. I got you your map.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getInventory().replaceItemWithItem(new Item(QuestConstants.SEA_FISHING_MAP, 1), new Item(QuestConstants.UNUSUAL_FISH, 1));
					player.getDialogue().sendNpcChat("Great work outerlander! With this, I can finally catch",
													"enough fish to make an honest living from it! Here, have",
													"the stupid rare fish.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(13)) != 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(14)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I don't suppose you have any idea where I could find",
														"an exotic and extremely rare fish, do you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Ah, so even outerlanders have heard of my amazing",
													"catch the other day!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("You have it? Can I trade you something for it?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("As exotic looking as it is, it is bad eating. I will happily",
													"trade it if you can find me the secret map of the best",
													"fishing spots that the navigator has hidden away.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("Is that all?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Indeed it is, outerlander. The only reason I sit out",
													"here in the cold all day long is so I don't have to pay",
													"his outrageous prices.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("By getting me his copy of that map, I will finally be self",
													"sufficient. I might even make a profit!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendPlayerChat("I'll see what I can do.", Dialogues.CONTENT);
					player.questStageSub1[this.getId()] += Misc.getActualValue(14);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of fisherman
		if(npcId == QuestConstants.SWENSEN_THE_NAVIGATOR){
			if(stage < 2)
				return false;
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(15)) != 0){
				if(chatId <= 6 && !player.getInventory().playerHasItem(QuestConstants.WEATHER_FORECAST, 1))
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I would like your map of fishing spots.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("I have already told you outerlander; I will not",
													"exchange it for anything other than a divination on the",
													"weather from our seer himself!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("What, like this one I have here?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("W-what...? I don't believe it! How did you...? I suppose",
													"it doesn't matter, you have my gratitude outerlander!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("With this forecast I will be able to plan a safe course",
													"for our next raiding expedition!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getInventory().replaceItemWithItem(new Item(QuestConstants.WEATHER_FORECAST, 1), new Item(QuestConstants.SEA_FISHING_MAP, 1));
					player.getDialogue().sendNpcChat("Here, outerlander; you may take my map of local",
													"fishing patterns with my gratitude!", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(14)) != 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(15)) == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Greetings outerlander.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("I don't suppose you have any idea where I could find a",
														"map of deep sea fishing spots do you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Hmmm? Why of course! As the navigator for the",
													"Fremennik I keep all of our maps secure right here.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("Great! Can I have it?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Have it? Just like that? I think not outerlander. This",
													"map shows all of the prime fishing locations nearby.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("It is very valuable to our clan. I am afraid I can not",
													"just give it away.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("Perhaps I can trade you something for it?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("A trade? For a map of the best fishing spots in a",
													"hundred leagues? I will trade it for no less than a",
													"weather forecast from our Seer.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("As a navigator, the weather is extremely important for",
													"plotting the best course. Unfortunately the Seer is",
													"always too busy to help me with a forecast.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendPlayerChat("Where could I get a weather forecast from then?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("I just told you: from the Seer. You will need to",
													"persuade him to take the time to make a forecast",
													"somehow.", Dialogues.CONTENT);
					player.questStageSub1[this.getId()] += Misc.getActualValue(15);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if((stage & Misc.getActualValue(10)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("I am trying to become a member of the Fremennik",
														"clan! The Chieftain told me that I may be able to gain",
														"your vote at the council of elders?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("You wish to stop being an outerlander? I can",
													"understand that! I have no reason why I would prevent",
													"you becoming a Fremennik...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("...but you must first pass a little test for me to prove",
													"you are worthy.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("What kind of test?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Well, I serve our clan as a navigator. The seas can be",
													"a fearful place when you know not where you are",
													"heading.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Should something happen to me, all members of our",
													"tribe have some basic sense of direction so that they",
													"may always return safely home.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("If you are able to demonstrate to me that you too have",
													"a good sense of direction then I will recommend you to",
													"the rest of the council of elders immediately.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendPlayerChat("Well, how would I go about showing that?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Ah, a simple task! Below this building I have constructed",
													"a maze; should you be able to walk from one side to the",
													"other that will be proof to me.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("You wish to try my challenge?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendOption("Yes",
													"No");
					return true;
				}
				if(chatId == 13){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("A maze? Is that all? Sure, it sounds simple enough.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(14);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("I will warn you outerlander, this maze was designed by",
													"myself, and is of the most fiendish complexity!", Dialogues.CONTENT);
					player.questStage[this.getId()] += Misc.getActualValue(10);
					return true;
				}
			}//swensen part 1
			if((stage & Misc.getActualValue(10)) != 0 && (stage & Misc.getActualValue(11)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("Man, your maze is pretty tough!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Hahahaha it is the most complex route I have ever",
													"devised! I am truly a genius at navigation! The world",
													"will remember my name!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("Can't I do something else for your vote at the council",
														"of elders?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("No, you cannot. It is my maze, or nothing.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendPlayerChat("Oh really? Watch and learn...", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 100){
					player.getDialogue().sendNpcChat("Outerlander! You have finished my maze! I am",
													"genuinely impressed!", Dialogues.CONTENT);
					player.questStage[this.getId()] += Misc.getActualValue(11);
					return true;
				}
			}//swensen part 2
			if((stage & Misc.getActualValue(11)) != 0){
				if(chatId == 101){
					player.getDialogue().sendPlayerChat("So does that mean I can rely on your vote at the",
														"council of elders to allow me into your village?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 102){
					player.getDialogue().sendNpcChat("Of course outerlander! I am nothing if not a man of",
													"my word!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 103){
					player.getDialogue().sendPlayerChat("Thanks!", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//swensen part 3
		}//end of swensen
		if(npcId == QuestConstants.PEER_THE_SEER){
			if(stage < 2)
				return false;
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(16)) != 0){
				if(chatId <= 4 && !player.getInventory().playerHasItem(QuestConstants.WARRIORS_CONTRACT, 1))
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Can I have a weather forecast now please?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("I have already told you outerlander; You may have a",
													"reading from me when I have a signed contract from a",
													"warrior guaranteeing my proctection.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Yeah, I know; I have one right here from Thorvald.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getInventory().replaceItemWithItem(new Item(QuestConstants.WARRIORS_CONTRACT, 1), new Item(QuestConstants.WEATHER_FORECAST, 1));
					player.getDialogue().sendNpcChat("You have not only persuaded one of the Fremennik to",
													"act as a servant to me, but you have enlisted the aid of",
													"mighty Thorvald himself???", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("You may take this forecast with my blessing",
													"outerlander. You have offered me the greatest security",
													"I can imagine.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(15)) != 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(16)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I don't suppose you have any idea where I could find a",
														"weather forecast from the Fremennik Seer do you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Er.... Yes, because I AM the Fremennik Seer.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Can I have a weather forecast then please?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("You require a divination of the weather? This is a",
													"simple matter for me, but I will require something in",
													"return from you for this small service.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("I knew you were going to say that...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Do not fret, outerlander; it is fairly simple matter. I",
													"require a bodyguard for protection. Find someone",
													"willing to offer me this service.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("That's all?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("That is all.", Dialogues.CONTENT);
					player.questStageSub1[this.getId()] += Misc.getActualValue(16);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if((stage & Misc.getActualValue(12)) == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hello outerlander. What do you want?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("Hello. I'm looking for members of the council of elders",
														"to vote for me to become a Fremennik.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Are you now? Well that is interesting. Usually",
													"outerlanders do not concern themselves with our ways",
													"like that.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("I am one of the members of the council of elders, and",
													"should you be able to prove to me that you have",
													"something to offer my clan", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("I will vote in your favour at the next meeting.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("How can I prove that to you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Well, I have but a simple test. This building behind me",
													"is my house. Inside I have constructed a puzzle.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("As a Seer to the clan, I value intelligence very highly,",
													"so you may think of it as an intelligence test of sorts.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendPlayerChat("An intelligence test? I thought barbarians were stupid!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("That is the opinion that outerlanders usually hold of my",
													"people, it is true. But that is because people often",
													"confuse knowledge with wisdom.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("My puzzle tests not what you know, but what you can",
													"work out. All members of our clan have been tested",
													"when they took their trials.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendPlayerChat("So what exactly does this puzzle consist of, then?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("Well, firstly you must enter my house with no items,",
													"weapons or armour. Then it is a simple matter of",
													"entering through one door and leaving by the other.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendPlayerChat("I can't take anything in there with me?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendNpcChat("That is correct outerlander. Everything you need to",
													"complete this puzzle you will find inside the building.",
													"Nothing more.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("So what say you outerlander? You think you have the",
													"wit to earn yourself my vote?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendOption("Yes",
													"No");
					return true;
				}
				if(chatId == 18){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yes, I accept your challenge. I have one small question,",
															"however...", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(19);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 19){
					player.getDialogue().sendNpcChat("Yes, outerlander?", Dialogues.CONTENT);
					player.questStage[this.getId()] += Misc.getActualValue(12);
					return true;
				}
			}//seer part 1
			if((stage & Misc.getActualValue(12)) != 0 && (stage & Misc.getActualValue(13)) == 0){
				if(chatId == 20){
					player.getDialogue().sendPlayerChat("Well... you say I can bring nothing with me when I",
														"enter your house...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 21){
					player.getDialogue().sendNpcChat("Yes, outerlander?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 22){
					player.getDialogue().sendPlayerChat("Well.....", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 23){
					player.getDialogue().sendNpcChat("Yes, outerlander?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 24){
					player.getDialogue().sendPlayerChat("Where is the nearest bank?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 25){
					player.getDialogue().sendNpcChat("Ah, I see your problem outerlander. The nearest bank",
													"to here is the place known to outerlanders as the Seers",
													"Village. It is some way South.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 26){
					player.getDialogue().sendNpcChat("I do however have an alternative, should you wish to",
													"take it.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 27){
					player.getDialogue().sendPlayerChat("And what is that?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 28){
					player.getDialogue().sendNpcChat("I can store all the weapons, armour and items that you",
													"have upon you directly into your bank account.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 29){
					player.getDialogue().sendNpcChat("This will tax what little magic I possess however, so you",
													"will have to manually travel to the bank to withdraw",
													"them again.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 30){
					player.getDialogue().sendNpcChat("What say you outerlander? you wish me to do this for",
													"you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 31){
					player.getDialogue().sendOption("Yes",
													"No");
					return true;
				}
				if(chatId == 32){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yes, thank you!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(33);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 33){
					if(player.bankCarriedItems()) {
						player.getDialogue().sendNpcChat("The task is done. I wish you luck with your test,",
														"outerlander.", Dialogues.CONTENT);
					}else{
						player.getDialogue().sendNpcChat("I'm not able to! I wish you luck with your test,",
														"outerlander.", Dialogues.CONTENT);
					}
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 100){
					player.getDialogue().sendNpcChat("Incredible! To have solved my puzzle so quickly! I have",
													"no choice but to vote in your favour!", Dialogues.CONTENT);
					player.questStage[this.getId()] += Misc.getActualValue(13);
					return true;
				}
			}//seer part 2
			if((stage & Misc.getActualValue(13)) != 0){
				if(chatId == 101){
					player.getDialogue().sendPlayerChat("So you will vote for me at the council?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 102){
					player.getDialogue().sendNpcChat("Absolutely, outerlander. Your wisdom in passing my test",
													"marks you as worthy in my eyes.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//seer part 3
			if(chatId == 1){
				player.getDialogue().sendNpcChat("You wish me to bank your items for",
												"you?", Dialogues.CONTENT);
				return true;
			}
			if(chatId == 2){
				player.getDialogue().sendOption("Yes",
												"No");
				return true;
			}
			if(chatId == 3){
				if(optionId == 1){
					player.getDialogue().sendPlayerChat("Yes, thank you!", Dialogues.CONTENT);
					player.getDialogue().setNextChatId(4);
					return true;
				}else{
					player.getDialogue().endDialogue();
					return false;
				}
			}
			if(chatId == 4){
				if(player.bankCarriedItems()) {
					player.getDialogue().sendNpcChat("The task is done.", Dialogues.CONTENT);
				}else{
					player.getDialogue().sendNpcChat("I'm not able to!", Dialogues.CONTENT);
				}
				player.getDialogue().endDialogue();
				return true;
			}
		}//end of peer the seer
		if(npcId == QuestConstants.THORVALD_THE_WARRIOR){
			if(stage < 2)
				return false;
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(17)) != 0){
				if(chatId <= 6 && !player.getInventory().playerHasItem(QuestConstants.CHAMPIONS_TOKEN, 1))
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I would like your contract to offer your services as a",
														"bodyguard.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Oh you would, would you outerlander? I have already",
													"told you, I will not demean myself with such a baby",
													"sitting job until I can sit in the Longhall with pride.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("It's a good thing I have the Champions' Token right",
														"here then, isn't it?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Ah... well this is a different matter. With that token I",
													"can claim my rightful place as a champion in the Long",
													"hall!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Here outerlander, I can suffer the indignity of playing",
													"babysitter if it means that I can then revel with my",
													"warrior equals in the Long Hall afterwards!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getInventory().replaceItemWithItem(new Item(QuestConstants.CHAMPIONS_TOKEN, 1), new Item(QuestConstants.WARRIORS_CONTRACT, 1));
					player.getDialogue().sendNpcChat("Here outerlander, take this contract; I will fulfill it to",
													"my utmost.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(16)) != 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(17)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I don't suppose you have any idea where I could find a",
														"brave and powerful warrior to act as a bodyguard?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Know you not who I am outerlander? There are none",
													"more brave or powerful than me amongst all the",
													"Fremennik!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("However... The role of bodyguard is below me, as a",
													"noble warrior. You might as well ask me to babysit the",
													"children!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("Is there no way you would do this for me?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("There is but one way outerlander. Since I was steeled",
													"in battle, I have dreamt of earning my place at the",
													"Champions Table in the Long Hall.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("It is a tradition amongst us that the bravest and",
													"strongest are honoured with a table of champions to",
													"drink and feast all that they can in our Long Hall.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Unfortunately, there are only a fixed number of places",
													"available at the table, and these places were all filled",
													"many moons ago by others.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Although my worthiness is undeniable, the only way I",
													"may take my place is if one of those already there die,",
													"or give up their place to me voluntarily.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendPlayerChat("So you want me to go kill one of them off for you?",
														"Make it look like an accident?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("WHAT? No, no, not at all! I am shocked you would",
													"suggest such a thing!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("If you can persuade one of the Revellers to give up",
													"their Champions' Token to you so that I might take",
													"their place, you may have my contract as a bodyguard.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendPlayerChat("Okay, I'll see what I can do.", Dialogues.CONTENT);
					player.questStageSub1[this.getId()] += Misc.getActualValue(17);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if((stage & Misc.getActualValue(14)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Hello yourself, outerlander. What brings you to dare",
													"speak to a mighty Fremennik warrior such as myself?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Erm... are you a member of the council?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("The Fremennik council of elders? Why, of course I",
													"am. I am recognised as one the clans mightiest",
													"warriors. What is it to you outerlander?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("Well... I was wondering if you could vote for me to",
														"become a Fremennik.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("An outerlander wishes to become a Fremennik!?!? Ha!",
													"That is priceless!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Well, let us say that I am not totally against this",
													"concept. As a warrior, I appreciate the value of brave",
													"and powerful warriors to our clan,", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("and even though you may be an outerlander, I will not",
													"hold this against you if you can prove yourself to be",
													"fierce of heart in a combat situation to me.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendPlayerChat("So how can I prove that? You want me to fight? Come",
														"on then, bring it on! Right here, right now, buddy!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Hahahahaha! You certainly show some spirit for an",
													"outerlander!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("But spirit does not always make a good warrior. It",
													"takes both skill and spirit to be so. I have a test that I",
													"give all Fremenniks on their path to be a member of the",
													"clan.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("My test will challenge both your combat prowess and",
													"your bravery equally. Should you pass it you will earn",
													"my vote at the council, and more importantly my",
													"respect for you as a warrior.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("So what say you, outerlander? Are you prepared for",
													"the battle?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendOption("Yes",
													"No");
					return true;
				}
				if(chatId == 15){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Am I prepared? I'll show you what combat's all about,",
															"you big sissy barbarian type guy!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(16);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("Hahahahaha! I'm beginning to like you already,",
													"outerlander!", Dialogues.CONTENT);
					player.questStage[this.getId()] += Misc.getActualValue(14);
					return true;
				}
			}//thorvald part 1
			if((stage & Misc.getActualValue(14)) != 0 && (stage & Misc.getActualValue(15)) == 0){
				if(chatId == 17){
					player.getDialogue().sendNpcChat("Then allow me to present you with my challenge; This",
													"ladder here will take you to a place of combat. I have",
													"placed a special warrior down there to challenge you.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendNpcChat("Battle him to death, and you will pass my challenge.",
													"If at any point you wish to leave combat, simply climb",
													"back up the ladder, to leave that place.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 19){
					player.getDialogue().sendNpcChat("If you leave you will of course fail the test. You may",
													"retry my test in the future if you fail, but you must",
													"stay down there until the death if you wish for my vote",
													"at the council.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 20){
					player.getDialogue().sendNpcChat("You must defeat him three times to prove that you are",
													"worthy. The fourth time that you fight him will be to",
													"the death, so do not show cowardice.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 21){
					player.getDialogue().sendPlayerChat("Is that all? It will be easy!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 22){
					player.getDialogue().sendNpcChat("No, there is one more important rule;", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 23){
					player.getDialogue().sendNpcChat("You may not enter the battleground with any armour",
													"or weaponry of any kind.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 24){
					player.getDialogue().sendNpcChat("If you need to place your equipment into your bank",
													"account, I recommend that you speak to the seer, who",
													"knows a spell that will do that for you.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//thorvald part 2
			if((stage & Misc.getActualValue(15)) != 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("So can I count on your vote at the council of elders",
														"now Thorvald?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Absolutely! I watched the entire battle, and was",
													"extremely impressed with your bravery in combat!", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 100){
					player.getDialogue().sendNpcChat("Hahaha! Well fought outerlander! Now come down from",
													"there, you have passed my trial with flying colours!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 101){
					player.getDialogue().sendPlayerChat("But... I don't understand... I did not manage to beat",
														"Koschei...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 102){
					player.getDialogue().sendNpcChat("I did not say you had to, outerlander! All I asked was",
													"that you fought to the death! And you did! The death",
													"was your own!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 103){
					player.getDialogue().sendNpcChat("I was not interested in how strong you are! I was",
													"interested in how BRAVE you are! You fought a",
													"superior enemy to your very last breath - THAT is",
													"bravery.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 104){
					player.getDialogue().sendNpcChat("I would be honoured to represent you to the council as",
													"worthy of being a Fremennik after watching that superb",
													"battle!", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//thorvald part 3
		}//end of thorvald the warrior
		if(npcId == QuestConstants.THORA_THE_BARKEEP){
			if(stage < 2)
				return false;
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(19)) != 0){
				if(chatId <= 3 && !player.getInventory().playerHasItem(QuestConstants.PROMISSORY_NOTE, 1))
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hi! Can I please have one of your legendary cocktails",
														"now?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("What?!?! I can't believe you... Let me look at that...",
													"Askeladden would NEVER... Gosh. It looks legitimate.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getInventory().replaceItemWithItem(new Item(QuestConstants.PROMISSORY_NOTE, 1), new Item(QuestConstants.LEGENDARY_COCKTAIL, 1));
					player.getDialogue().sendNpcChat("Here you go, on the house! You have made my life SO",
													"much easier! Knowing that little monster won't be",
													"bugging me in here all the time anymore!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("That little weasel will have to abide by this written",
													"promise that Askeladden can never ever enter the",
													"Longhall again! He can't get round this one!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("Uh... yeah... yeah, you probably won't see someone",
														"called Askeladden coming in here...", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if((stage & Misc.getActualValue(6)) != 0 && (stage & Misc.getActualValue(7)) == 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(18)) != 0 && (player.questStageSub1[this.getId()] & Misc.getActualValue(19)) == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I don't suppose you have any idea where I could find",
														"the longhall barkeeps' legendary cocktail, do you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("How did you hear about that?!?!?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("I didn't think anybody knew about that... Well, it is",
													"true that in my younger years as a barkeep, I",
													"wandered the lands trying various alcoholic delicacies.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Did you ever realise just how many different types of",
													"alcohol there are here in RuneScape? Lots!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Well, anyway, I used a fusion of various drinks from",
													"all around the world to create the greatest cocktail ever",
													"made!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Of course, when my wanderlust was gone, and I",
													"returned back to Rellekka to serve as barkeep here, I",
													"gave all that up.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("But you still remember how to make it, right?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Of course.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendPlayerChat("And you have all the ingredients here? I don't need to",
														"go chasing round the world for obscure ingredients to",
														"make it?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("No, I have them all here. Why?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendPlayerChat("Can you make me your legendary cocktail then?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("I would rather not; it is a reminder of a life I left",
													"behind when I came back.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendPlayerChat("Any way I could change your mind?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("You need this to become a Fremennik, right? Well, you",
													"seem okay for an outerlander, it would be a shame to",
													"see you fail. You know Askeladden?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendPlayerChat("That kid outside? Sure.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("He is nothing but a pest. He keeps sneaking in and",
													"stealing beer. I shudder to think what he will be like",
													"when he has passed his trial of manhood,", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendNpcChat("and is allowed in here legitimately. If you can get him",
													"to sign a contract promising that he will NEVER",
													"EVER EVER darken my doorway here again, you",
													"get the drink.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendPlayerChat("Any idea how I can get him to do that?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 19){
					player.getDialogue().sendNpcChat("Knowing that little horror, he'll probably be willing to in",
													"exchange for some cash. You should go ask him",
													"yourself though.", Dialogues.CONTENT);
					player.questStageSub1[this.getId()] += Misc.getActualValue(19);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of thora the barkeep
		if(npcId == QuestConstants.KOSCHEI_THE_DEATHLESS_FORM1){
			if(chatId == 100){
				player.getDialogue().sendNpcChat("It seems you have some idea of combat after all,",
													"outerlander! I will not hold back so much this time!", Dialogues.CONTENT);
				Npc npc = player.getSpawnedNpc();
				if(npc != null){
					if(npc.getNpcId() == QuestConstants.KOSCHEI_THE_DEATHLESS_FORM1){
						npc.sendTransform(QuestConstants.KOSCHEI_THE_DEATHLESS_FORM2, 999999999);
						npc.setCurrentHp(npc.getMaxHp());
					}
				}
				return true;
			}
			if(chatId == 101){
				Npc npc = player.getSpawnedNpc();
				if(npc != null){
					if(npc.getNpcId() == QuestConstants.KOSCHEI_THE_DEATHLESS_FORM2){
						npc.getUpdateFlags().setForceChatMessage("Outerlander... you made a mistake coming down here!");
						CombatManager.attack(npc, player);
					}
				}
				return true;
			}
		}
		if(npcId == QuestConstants.KOSCHEI_THE_DEATHLESS_FORM2){
			if(chatId == 100){
				player.getDialogue().sendNpcChat("Impressive start... But now we fight for real!", Dialogues.CONTENT);
				Npc npc = player.getSpawnedNpc();
				if(npc != null){
					if(npc.getNpcId() == QuestConstants.KOSCHEI_THE_DEATHLESS_FORM2){
						npc.sendTransform(QuestConstants.KOSCHEI_THE_DEATHLESS_FORM3, 999999999);
						npc.setCurrentHp(npc.getMaxHp());
					}
				}
				return true;
			}
			if(chatId == 101){
				Npc npc = player.getSpawnedNpc();
				if(npc != null){
					if(npc.getNpcId() == QuestConstants.KOSCHEI_THE_DEATHLESS_FORM3){
						npc.getUpdateFlags().setForceChatMessage("What kind of person calls themself "+player.getUsername()+"?");
						CombatManager.attack(npc, player);
					}
				}
				return true;
			}
		}
		if(npcId == QuestConstants.KOSCHEI_THE_DEATHLESS_FORM3){
			if(chatId == 100){
				player.getDialogue().sendNpcChat("You show some skill at combat... I will hold back no",
												"longer! This time you lose your prayer however, and",
												"fight like a warrior!", Dialogues.CONTENT);
				player.getSkill().getLevel()[Skill.PRAYER] = 0;
				player.getSkill().refresh(Skill.PRAYER);
				Npc npc = player.getSpawnedNpc();
				if(npc != null){
					if(npc.getNpcId() == QuestConstants.KOSCHEI_THE_DEATHLESS_FORM3){
						npc.sendTransform(QuestConstants.KOSCHEI_THE_DEATHLESS_FORM4, 999999999);
						npc.setCurrentHp(npc.getMaxHp());
					}
				}
				return true;
			}
			if(chatId == 101){
				Npc npc = player.getSpawnedNpc();
				if(npc != null){
					if(npc.getNpcId() == QuestConstants.KOSCHEI_THE_DEATHLESS_FORM4){
						npc.getUpdateFlags().setForceChatMessage("Prepare to face my power, outerlander!");
						CombatManager.attack(npc, player);
					}
				}
				return true;
			}
		}
		return false;
	}
	
}
