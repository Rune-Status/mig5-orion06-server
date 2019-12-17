package com.rs2.model.content.questing.quests;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.content.skills.Tools.Tool;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.content.skills.smithing.Smelting;
import com.rs2.model.World;
import com.rs2.model.tick.Tick;

public class ElementalWorkshop extends Quest {
	
	final int rewardQP = 1;
	
	public ElementalWorkshop(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		int value = stage-3;
		if(stage == 0){
			int craftLvl = player.getSkill().getPlayerLevel(Skill.CRAFTING);
			int mineLvl = player.getSkill().getPlayerLevel(Skill.MINING);
			int smithLvl = player.getSkill().getPlayerLevel(Skill.SMITHING);
			String text[] = {"I can start this quest by reading a",
							"book found in Seers village.",
							"",
							"Minimum requirements:",
							(mineLvl >= 20 ? "@str@" : "")+"Level 20 Mining",
							(smithLvl >= 20 ? "@str@" : "")+"Level 20 Smithing",
							(craftLvl >= 20 ? "@str@" : "")+"Level 20 Crafting",};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should now search for the hidden workshop located",
							"in the village of the Seers."};
			return text;	
		}
		if(stage >= 3 && stage < 66){
			String text[] = {"I should do the following things now:",
							((value & 4) != 0 ? "@str@" : "")+"Get the waterwheel running",
							((value & 16) != 0 ? "@str@" : "")+"Start the bellows",
							((value & 32) != 0 ? "@str@" : "")+"Warm up the furnace",};
			return text;
		}
		if(stage == 66){
			String text[] = {"I have fixed everything, and should now be able to make",
							"the elemental shield."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "1 Quest Point", "5000 Crafting  XP", "5000 Smithing XP", "The ability to make elemental shields."};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("5000 Crafting  XP", 12151);
		player.getActionSender().sendString("5000 Smithing XP", 12152);
		player.getActionSender().sendString("The ability to make elemental shields.", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.CRAFTING, 5000);
		player.getSkill().addQuestExp(Skill.SMITHING, 5000);
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.ELEMENTAL_SHIELD);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		int value = stage-3;
		if(objectId == 3403){//elemental rock
			if(player.spawnedNpc != null){
				if(!player.spawnedNpc.isDead())
					if(player.spawnedNpc.getNpcId() == QuestConstants.EARTH_ELEMENTAL)
						return true;
			}
			final Tool pickaxe = Tools.getTool(player, Skill.MINING);
			if (pickaxe == null) {
				player.getActionSender().sendMessage("You do not have a pickaxe that you can use.");
				return true;
			}
			if (!SkillHandler.hasRequiredLevel(player, Skill.MINING, 20, "mine here")) {
				return true;
			}
			new GameObject(Constants.EMPTY_OBJECT, objectX, objectY, player.getPosition().getZ(), 0, 10, objectId, 100);
			Npc npc = new Npc(QuestConstants.EARTH_ELEMENTAL);
			NpcLoader.spawnNpc(player, npc, true, false);
			npc.getUpdateFlags().sendAnimation(1038);
			npc.getUpdateFlags().setForceChatMessage("Grr... Ge'roff us!");
			return true;
		}
		if(objectId == 3389 && objectX == 2716 && objectY == 3481){//bookcase
			if(player.questStage[this.getId()] == 0 && this.getDefinition().isMembers() && !player.isDonator()){
				player.getActionSender().removeInterfaces();
				player.getActionSender().sendMessage("You need a donator rank to start this quest: "+this.getDefinition().getName());
				return true;
			}
			if(!player.hasItem(QuestConstants.BATTERED_BOOK)){
				player.getInventory().addItemOrDrop(new Item(QuestConstants.BATTERED_BOOK, 1));
				player.getDialogue().sendItem1Line("You find a book titled 'The Elemental Shield'.", new Item(QuestConstants.BATTERED_BOOK, 1));
				return true;
			}
		}
		if(objectId == 3410 && objectX == 2734 && objectY == 9882){//bellows
			if(stage == 1)
				return false;
			if((value & 8) != 0){
				player.getActionSender().sendMessage("You have already fixed the bellows.");
				return true;
			}
			if (!SkillHandler.hasRequiredLevel(player, Skill.CRAFTING, 20, "fix the bellows")) {
				return true;
			}
			if(player.getInventory().playerHasItem(QuestConstants.LEATHER, 1) && player.getInventory().playerHasItem(QuestConstants.NEEDLE, 1) && player.getInventory().playerHasItem(QuestConstants.THREAD, 1)){
				player.getActionSender().sendMessage("You stitch the leather over the hole in the bellows.");
				player.getInventory().removeItem(new Item(QuestConstants.LEATHER, 1));
				player.getInventory().removeItem(new Item(QuestConstants.THREAD, 1));
				player.questStage[this.getId()] += 8;
				return true;
			}
		}
		if(objectId == 3397 && objectX == 2724 && objectY == 9894){//boxes
			if(!player.hasItem(QuestConstants.STONE_BOWL_EMPTY)){
				player.getInventory().addItemOrDrop(new Item(QuestConstants.STONE_BOWL_EMPTY, 1));
				player.getActionSender().sendMessage("You find a stone bowl.");
			}else
				player.getActionSender().sendMessage("It's empty.");
			return true;
		}
		if((objectId == 3390 && objectX == 2710 && objectY == 3495) || (objectId == 3391 && objectX == 2709 && objectY == 3495)){//odd wall
			if(player.getPosition().getY() < 3496){
				if(player.getInventory().playerHasItem(QuestConstants.BATTERED_KEY, 1)){
					player.getActionSender().walkTo(0, 1, true);
					player.getActionSender().walkThroughDoubleDoor2(3391, 3390, 2709, 3495, 2710, 3495, 0);
					player.getActionSender().sendMessage("You use the battered key to open the doors.");
					return true;
				}
			} else {
				player.getActionSender().walkTo(0, -1, true);
				player.getActionSender().walkThroughDoubleDoor2(3391, 3390, 2709, 3495, 2710, 3495, 0);
				return true;
			}
		}
		if(objectId == 3415 && objectX == 2710 && objectY == 3497){//stairs down
			player.teleport(new Position(2716, 9888, 0));
			if(stage == 2){
				player.getDialogue().sendPlayerChat("Now to explore this area thoroughly, to find what",
													"forgotten secrets it contains.", Dialogues.CONTENT);
				player.getDialogue().endDialogue();
				player.questStage[this.getId()] = 3;
			}
			return true;
		}
		if(objectId == 3416 && objectX == 2714 && objectY == 9887){//stairs up
			player.teleport(new Position(2709, 3498, 0));
			return true;
		}
		if(objectId == 3404 && objectX == 2726 && objectY == 9908){//valve e
			if(stage == 1)
				return false;
			if((value & 1) == 0){
				player.getActionSender().sendMessage("You turn the handle.");
				player.questStage[this.getId()] += 1;
			}else{
				player.getActionSender().sendMessage("You have already turned this handle.");
			}
			return true;
		}
		if(objectId == 3405 && objectX == 2713 && objectY == 9908){//valve w
			if(stage == 1)
				return false;
			if((value & 1) == 0){
				player.getActionSender().sendMessage("It doesn't seem to work quite yet.");
			}
			if((value & 2) == 0 && (value & 1) != 0){
				player.getActionSender().sendMessage("You turn the handle.");
				player.questStage[this.getId()] += 2;
			}
			if((value & 2) != 0){
				player.getActionSender().sendMessage("You have already turned this handle.");
			}
			return true;
		}
		if(objectId == 3406 && objectX == 2722 && objectY == 9906){//water lever
			if(stage == 1)
				return false;
			if((value & 1) == 0 || (value & 2) == 0){
				player.getActionSender().sendMessage("It doesn't seem to work quite yet.");
			}
			if((value & 4) == 0 && (value & 1) != 0 && (value & 2) != 0){
				player.getActionSender().sendMessage("You pull the lever.");
				player.getActionSender().sendMessage("You hear the sound of a water wheel starting up.");
				player.questStage[this.getId()] += 4;
			}
			if((value & 4) != 0){
				player.getActionSender().sendMessage("You have already fixed the waterwheel.");
			}
			return true;
		}
		if(objectId == 3409 && objectX == 2734 && objectY == 9887){//bellows lever
			if(stage == 1)
				return false;
			if((value & 8) == 0){
				player.getActionSender().sendMessage("You should fix the bellows first before pulling the lever.");
			}
			if((value & 16) == 0 && (value & 8) != 0){
				player.getActionSender().sendMessage("You pull the lever.");
				player.getActionSender().sendMessage("The bellows pump air down the pipe.");
				player.questStage[this.getId()] += 16;
			}
			if((value & 16) != 0){
				player.getActionSender().sendMessage("You have already pulled the lever.");
			}
			return true;
		}
		return false;
	}
	
	/*
	 * qStage = 3;
	 * 
	 * (value & 4) == 0 //+4 has not been added yet
	 * (value & 4) != 0 //+4 has been added
	 * 
	 * water:
			valve e: 3404
			2726,9908
			qs = 1
			
			valve w: 3405
			2713,9908
			qs = 2
			
			lever: 3406
			2722,9906
			qs = 4
			
	air:
		lever: 3409
		2734,9887
		qs = 16
		
	bellow fix
		qs = 8
		
	lava to furnace
		qs = 32
	 */
	
	
	public boolean handleItemOnItem(final Player player, int item1, int item2, int stage){
		if((item1 == QuestConstants.BATTERED_BOOK && item2 == QuestConstants.KNIFE) || (item1 == QuestConstants.KNIFE && item2 == QuestConstants.BATTERED_BOOK)){
			if(stage != 0){
				if(!player.hasItem(QuestConstants.BATTERED_KEY)){
					player.getInventory().addItem(new Item(QuestConstants.BATTERED_KEY, 1));
					player.getActionSender().sendMessage("You make a small cut in the spine of the book.");
					player.getActionSender().sendMessage("Inside you find a small, old, battered key.");
					return true;
				}
			}else{
				player.getActionSender().sendMessage("You don't want to damage the book.");
				return true;	
			}
		}
		return false;
	}
	
	public boolean useQuestItemOnObject(final Player player, int itemId, int objectId, final int stage){
		int value = stage-3;
		if(itemId == QuestConstants.STONE_BOWL_EMPTY && objectId == 3414){
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.STONE_BOWL_EMPTY, 1), new Item(QuestConstants.STONE_BOWL_LAVA, 1));
			player.getActionSender().sendMessage("You fill the bowl with hot lava.");
			return true;
		}
		if(itemId == QuestConstants.STONE_BOWL_LAVA && objectId == 3413){
			if(stage == 1)
				return false;
			if((value & 32) != 0){
				player.getActionSender().sendMessage("You have already added lava to the furnace.");
				return true;
			}
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.STONE_BOWL_LAVA, 1), new Item(QuestConstants.STONE_BOWL_EMPTY, 1));
			player.getActionSender().sendMessage("You empty the lava into the furnace.");
			player.questStage[this.getId()] += 32;
			return true;
		}
		if(itemId == QuestConstants.ELEMENTAL_ORE && objectId == 3413){
			if(stage == 66 || stage == 1){
				Smelting.oreOnFurnace(player, QuestConstants.ELEMENTAL_ORE);
				return true;
			}
		}
		if(itemId == QuestConstants.ELEMENTAL_METAL && objectId == 3402){
			if(stage == 66 || stage == 1){
			if(player.getInventory().playerHasItem(QuestConstants.HAMMER, 1)){
				if(stage != 1){
					if(player.getInventory().playerHasItem(QuestConstants.BATTERED_BOOK, 1))
						player.getActionSender().sendMessage("Following the instructions in the book you make an elemental shield.");
					else
						return true;
				}
				player.getActionSender().sendSound(468, 1, 0);
				player.getUpdateFlags().sendAnimation(898);
				final Tick timer = new Tick(3) {
					@Override
		            public void execute() {
						player.getInventory().replaceItemWithItem(new Item(QuestConstants.ELEMENTAL_METAL, 1), new Item(QuestConstants.ELEMENTAL_SHIELD, 1));
						if(stage != 1)
							questCompleted(player);
						stop();
					}
				};
				World.getTickManager().submit(timer);
				return true;
			}
			}
		}
		return false;
	}
	
	public String[] getBookText(int page){
		if(page == 0){
			String text[] = {"Book of the elemental shield",//title
							"",//page1
							"",//page2
							"Within the pages of this",//line1 - page1
							"book you will find the",
							"secret to working the",
							"very elements themselves.",
							"Early in the fifth age, a",
							"new ore was discovered.",
							"This ore has a unique",
							"property of absorbing,",
							"transforming or focusing",
							"elemental energy. A",
							"workshop was erected",
							"close by to work this new",//line1 - page2
							"material. The workshop",
							"was set up for artisans",
							"and inventors to be able",
							"to come and create",
							"devices made from the",
							"unique ore, found only in",
							"the village of the Seers."};
			return text;
		}
		if(page == 1){
			String text[] = {"Book of the elemental shield",//title
							"",//page1
							"",//page2
							"After some time of",//line1 - page1
							"successful industry the",
							"true power of this ore",
							"became apparent, as",
							"greater and more",
							"powerful weapons were",
							"created. Realising the",
							"threat this posed, the magi",
							"of the time closed down",
							"the workshop and bound",
							"it under lock and key,",
							"also trying to destroy all",//line1 - page2
							"knowledge of",
							"manufacturing processes.",
							"Yet this book remains and",
							"you may still find a way",
							"to enter the workshop",
							"within this leather bound",
							"volume."};
			return text;
		}
		return null;
	}
	
	void clearBookStrings(final Player player){
		player.getActionSender().sendString("", 14165);
		player.getActionSender().sendString("", 14166);
		for(int id2 = 843; id2 <= 864; id2++){
			player.getActionSender().sendString("", id2);
		}
	}
	
	int bookPages = 1;
	
	void openBookPage(final Player player, int page){
		clearBookStrings(player);
		String text[] = getBookText(page);
		player.getActionSender().sendString(text[0], 903);
		player.getActionSender().sendString(text[1], 14165);
		player.getActionSender().sendString(text[2], 14166);
		for(int id2 = 3; id2 < text.length; id2++){
			player.getActionSender().sendString(text[id2], 843+id2-3);
		}
		player.getActionSender().sendFrame171((player.readingBookPage == 0 ? 1 : 0), 840);//previous page
		player.getActionSender().sendFrame171((player.readingBookPage == bookPages ? 1 : 0), 842);//next page
	}
	
	public boolean handleInterfaceButton(final Player player, int buttonId, int stage){
		if(player.readingBook == QuestConstants.BATTERED_BOOK){
			if(buttonId == 841 && player.readingBookPage < bookPages){
				player.readingBookPage++;
				openBookPage(player, player.readingBookPage);
				return true;
			}
			if(buttonId == 839 && player.readingBookPage > 0){
				player.readingBookPage--;
				openBookPage(player, player.readingBookPage);
				return true;
			}
		}
		return false;
	}
	
	public boolean handleFirstClickItem(final Player player, int interfaceId, int itemId, int stage){
		if(interfaceId == 3214){//inventory
			if(itemId == QuestConstants.BATTERED_BOOK){
				openBookPage(player, 0);
				player.readingBook = itemId;
				player.readingBookPage = 0;
				player.getActionSender().sendInterface(837);
				if(stage == 0){
					player.getActionSender().sendMessage("The book has two parts: an introduction and an instruction section.");
					player.getActionSender().sendMessage("You flip the book open to the introduction and start reading.");
					questStarted(player);
				}
				return true;
			}
		}
		return false;
	}

}
