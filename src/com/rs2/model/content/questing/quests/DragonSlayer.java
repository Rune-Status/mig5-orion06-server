package com.rs2.model.content.questing.quests;

import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;
import com.rs2.model.transport.Sailing;
import com.rs2.model.transport.Sailing.ShipRoute;
import com.rs2.util.Misc;

public class DragonSlayer extends Quest {
	
	final int rewardQP = 2;
	
	public DragonSlayer(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to the Guild master in",
							"the Champions' Guild, south-west of Varrock.",
							"I will need to be able to defeat a level 83 dragon.",
							(player.getQuestPoints() >= 32 ? "@str@" : "")+"To enter the Champions' Guild I need 32 Quest Points."};
			return text;
		}
		if(stage == 2){
			String text[] = {"The Guildmaster told me to go speak with Oziach, who",
							"can be found by the cliffs west of Edgeville."};
			return text;	
		}
		if(stage == 3){
			String text[] = {"Oziach told me to slay the dragon of Crandor. I should",
							"go and ask some tips from the Guildmaster."};
			return text;	
		}
		if(stage >= 4 && stage < 260){
			String text[] = {"I should now find the following things:",
							"- Map to Crandor",
							"- Ship and Captain",
							"- Way to protect from dragonbreath"};
			return text;	
		}
		if(stage == 260){
			String text[] = {"I should go back to the ship, when I'm ready to go",
							"to Crandor to defeat the dragon."};
			return text;	
		}
		if(stage == 261){
			String text[] = {"I should now find and kill the dragon."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "2 Quest Points", "Ability to wear rune platebody", "18,650 Strength XP", "18,650 Defence XP"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("2 Quest Points", 12150);
		player.getActionSender().sendString("Ability to wear rune platebody", 12151);
		player.getActionSender().sendString("18,650 Strength XP", 12152);
		player.getActionSender().sendString("18,650 Defence XP", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.STRENGTH, 18650);
		player.getSkill().addQuestExp(Skill.DEFENCE, 18650);
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.RUNE_PLATEBODY);
		player.getActionSender().sendInterface(12140);
	}
	
	/*	
	 
	 3047,9640,1 repair
	 3047,9640,2 fixed
	 3047,9640,3 fixed, ned
	 
	 
	 save points:
	 1. buy ship
	 2. repair
	 3. repair
	 4. repair
	 5. item1
	 6. item2
	 7. item3
	 8. item4
	 9. spoke to ned
	 
	 1. 1
	 2. 2
	 3. 4
	 4. 8
	 5. 16
	 6. 32
	 7. 64
	 8. 128
	 ----255
	 
	 ----259 = all map part + ship fixed (not spoken to ned)
	 ----260 = spoken to ned
	 ----261 = arrived @crandor

	*/
	
	public boolean useQuestItemOnObject(final Player player, int itemId, int objectId, final int stage){
		int value = stage-4;
		if(stage < 4)
			return false;
		if(itemId == QuestConstants.WIZARDS_MIND_BOMB && objectId == 2586){
			if((value & 16) != 0){
				player.getActionSender().sendMessage("You already have Wizard's Mind Bomb there.");
				return true;
			}
			player.getInventory().replaceItemWithItem(new Item(QuestConstants.WIZARDS_MIND_BOMB, 1), new Item(QuestConstants.BEER_GLASS, 1));
			player.getActionSender().sendMessage("You pour the Wizard's Mind Bomb into the opening in the door.");
			player.questStage[this.getId()] += 16;
			checkDoor(player, player.questStage[this.getId()]);
			return true;
		}
		if(itemId == QuestConstants.UNFIRED_BOWL && objectId == 2586){
			if((value & 32) != 0){
				player.getActionSender().sendMessage("You already have Unfired bowl there.");
				return true;
			}
			player.getInventory().removeItem(new Item(QuestConstants.UNFIRED_BOWL, 1));
			player.getActionSender().sendMessage("You put the unfired bowl into the opening in the door.");
			player.questStage[this.getId()] += 32;
			checkDoor(player, player.questStage[this.getId()]);
			return true;
		}
		if(itemId == QuestConstants.SILK && objectId == 2586){
			if((value & 64) != 0){
				player.getActionSender().sendMessage("You already have Silk there.");
				return true;
			}
			player.getInventory().removeItem(new Item(QuestConstants.SILK, 1));
			player.getActionSender().sendMessage("You put the silk into the opening in the door.");
			player.questStage[this.getId()] += 64;
			checkDoor(player, player.questStage[this.getId()]);
			return true;
		}
		if(itemId == QuestConstants.LOBSTER_POT && objectId == 2586){
			if((value & 128) != 0){
				player.getActionSender().sendMessage("You already have Lobster pot there.");
				return true;
			}
			player.getInventory().removeItem(new Item(QuestConstants.LOBSTER_POT, 1));
			player.getActionSender().sendMessage("You put the lobster pot into the opening in the door.");
			player.questStage[this.getId()] += 128;
			checkDoor(player, player.questStage[this.getId()]);
			return true;
		}
		return false;
	}
	
	void checkDoor(final Player player, int stage){
		int value = stage-4;
		if((value & 16) != 0 && (value & 32) != 0 && (value & 64) != 0 && (value & 128) != 0){
			player.getActionSender().sendMessage("The door opens...");
		}
	}
	
	public boolean getObjectDialog(int clickId, final Player player, int objectId, int chatId, int optionId, int npcChatId, int x, int y, int stage){
		if(objectId == 2604 && x == 2935 && y == 9657 && clickId == 1){
			if(!player.hasItem(QuestConstants.MAP_PIECE_MELZAR) && !player.hasItem(QuestConstants.CRANDOR_MAP)){
				if(chatId == 1){
					player.getDialogue().sendItem1Line("You find a map piece in the chest.", new Item(QuestConstants.MAP_PIECE_MELZAR, 1));
					return true;
				}
				if(chatId == 2){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.MAP_PIECE_MELZAR, 1));
					ObjectHandler.getInstance().removeObject(2935, 9657, 0, 0);
					ObjectHandler.getInstance().addObject(new GameObject(2603, 2935, 9657, 0, 2, 10, 2603, 999999999), true);
					return false;
				}
			}
		}
		if(objectId == 2587 && x == 3057 && y == 9841 && clickId == 1){
			if(!player.hasItem(QuestConstants.MAP_PIECE_THALZAR) && !player.hasItem(QuestConstants.CRANDOR_MAP)){
				if(chatId == 1){
					player.getDialogue().sendStatement("As you open the chest, you notice an inscription on the lid:");
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendStatement("Here I rest the map to my beloved home. To whoever finds it, I beg",
													"of you, let it be. I was honour-bound not to destroy the map piece,",
													"but I have used all my magical skill to keep it from being recovered.");
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendStatement("This map leads to the lair of the beast that destroyed my home,",
													"devoured my family, and burned to a cinder all that I love. But",
													"revenge would not benefit me now, and to disturb this beast is to risk",
													"bringing its wrath down upon another land.");
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendStatement("I cannot stop you from taking this map piece now, but think on this:",
													"if you can slay the Dragon of Crandor, you are a greater hero than",
													"my land ever produced. There is no shame in backing out now.");
					return true;
				}
				if(chatId == 5){
					ObjectHandler.getInstance().removeObject(3057, 9841, 0, 0);
					ObjectHandler.getInstance().addObject(new GameObject(2588, 3057, 9841, 0, 3, 10, 2587, 999999999), true);
					return false;
				}
			}
		}
		if(objectId == 2588 && x == 3057 && y == 9841 && clickId == 1){
			if(!player.hasItem(QuestConstants.MAP_PIECE_THALZAR) && !player.hasItem(QuestConstants.CRANDOR_MAP)){
				if(chatId == 1){
					player.getDialogue().sendItem1Line("You find a map piece in the chest.", new Item(QuestConstants.MAP_PIECE_THALZAR, 1));
					return true;
				}
				if(chatId == 2){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.MAP_PIECE_THALZAR, 1));
					ObjectHandler.getInstance().removeObject(3057, 9841, 0, 0);
					ObjectHandler.getInstance().addObject(new GameObject(2587, 3057, 9841, 0, 3, 10, 2587, 999999999), true);
					return false;
				}
			}
		}
		return false;
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		final int value = stage - 4;
		final int qId = this.getId();
		if(objectId == 2589 && objectX == 3047 && objectY == 9639){//repair hole
			if(player.getInventory().playerHasItem(QuestConstants.HAMMER) && player.getInventory().playerHasItem(QuestConstants.PLANK) && player.getInventory().playerHasItem(QuestConstants.STEEL_NAILS, 30)){
				player.setStopPacket(true);
				player.getUpdateFlags().sendAnimation(898);
				final Tick timer = new Tick(2) {
					@Override
					public void execute() {
						int i = 2;
						if((value & 8) == 0)
							i = 8;
						if((value & 4) == 0)
							i = 4;
						if((value & 2) == 0)
							i = 2;
						player.setStopPacket(false);
						player.getInventory().removeItem(new Item(QuestConstants.PLANK, 1));
						player.getInventory().removeItem(new Item(QuestConstants.STEEL_NAILS, 30));
						if(i == 2){
							player.getDialogue().sendStatement("You nail a plank over the hole, but you still need more planks to",
																"close the hole completely.");
							player.questStage[qId] += i;
						}
						if(i == 4){
							player.getDialogue().sendStatement("You nail a plank over the hole, but you still need one more plank to",
																"close the hole completely.");
							player.questStage[qId] += i;
						}
						if(i == 8){
							player.teleport(new Position(player.getPosition().getX(),player.getPosition().getY(),2));
							player.getDialogue().sendStatement("You nail a final plank over the hole. You have successfully patched",
																"the hole in the ship.");
							player.questStage[qId] += i;
						}
						stop();
					}
				};
				World.getTickManager().submit(timer);
				return true;
			}
		}
		if((objectId == 2607 && objectX == 2847 && objectY == 9636) || (objectId == 2608 && objectX == 2847 && objectY == 9637)){//Elvarg gate
			if(stage == 261){
				player.getActionSender().walkTo(player.getPosition().getX() < 2847 ? 1 : -1, 0, true);
				player.getActionSender().walkThroughDoor3(2607, 2608, 2847, 9636, 2847, 9637, 0);
			}
			return true;
		}
		if(objectId == 2606 && objectX == 2836 && objectY == 9600){//secret wall door
			if(stage == 261 || stage == 1){
				player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
				player.getActionSender().walkTo(0, player.getPosition().getY() < 9600 ? 1 : -1, true);
			}else{
				player.getActionSender().sendMessage("It is locked.");
			}
			return true;
		}
		if(objectId == 2593 && objectX == 3047 && objectY == 3205){//gangplank to ship
			if(((value & 1) != 0 && stage > 4) || stage > 259){
				player.getActionSender().sendMessage("You board the ship.");
				player.teleport(new Position(3047,3207,1));
				return true;
			}
		}
		if(objectId == 2594 && objectX == 3047 && objectY == 3206){//gangplank out of ship
			if(((value & 1) != 0 && stage > 4) || stage > 259){
				player.getActionSender().sendMessage("You disembark the ship.");
				player.teleport(new Position(3047,3204,0));
				return true;
			}
		}
		if(objectId == 2590 && objectX == 3049 && objectY == 3208){//ladders to ship bottom
			int h = 1;
			if((value & 8) != 0)
				h = 2;
			if(stage == 260)
				h = 3;
			Ladders.climbLadder(player, new Position(3048,9640,h));
			return true;
		}
		if(objectId == 2592 && objectX == 3049 && objectY == 9640){//ladders to ship top
			Ladders.climbLadder(player, new Position(3048,3208,1));
			return true;
		}
		if(objectId == 9563 && objectX == 3012 && objectY == 3189){//Wormbrain cell door
			player.getActionSender().sendMessage("It is locked.");
			return true;
		}
		if(objectId == 2586 && objectX == 3051 && objectY == 9840){//Thalzar door
			if(value > 0 && (value & 16) != 0 && (value & 32) != 0 && (value & 64) != 0 && (value & 128) != 0){
				player.getActionSender().walkTo(player.getPosition().getX() < 3051 ? 1 : -1, 0, true);
				player.getActionSender().walkThroughDoor(2586, 3051, 9840, 0);
			}else
				player.getActionSender().sendMessage("You can't see any way to open the door.");
			return true;
		}
		if(objectId == 2603 && objectX == 2935 && objectY == 9657){//Melzar's chest (closed)
			ObjectHandler.getInstance().removeObject(2935, 9657, 0, 0);
			ObjectHandler.getInstance().addObject(new GameObject(2604, 2935, 9657, 0, 2, 10, 2603, 999999999), true);
			player.getActionSender().sendMessage("You open the chest.");
			return true;
		}
		if(objectId == 2595 && objectX == 2941 && objectY == 3248){//Main maze door
			if(player.getInventory().playerHasItem(QuestConstants.MELZAR_MAZE_KEY)){
				player.getActionSender().walkTo(player.getPosition().getX() < 2941 ? 1 : -1, 0, true);
				player.getActionSender().walkThroughDoor(2595, 2941, 3248, 0);
				player.getActionSender().sendMessage("You use the key and the door opens.");
				return true;
			} else {
				player.getActionSender().sendMessage("It is locked.");
				return true;
			}
		}
		if(objectId == 2596 && objectX == 2926){//red door
			if(player.getInventory().playerHasItem(QuestConstants.MELZAR_MAZE_RED_KEY)){
				player.getInventory().removeItem(new Item(QuestConstants.MELZAR_MAZE_RED_KEY, 1));
				player.getActionSender().walkTo(player.getPosition().getX() < 2926 ? 1 : -1, 0, true);
				player.getActionSender().walkThroughDoor(2596, 2926, objectY, 0);
				player.getActionSender().sendMessage("The key disintegrates as it unlocks the door.");
				return true;
			} else {
				player.getActionSender().sendMessage("It is locked.");
				return true;
			}
		}
		if(objectId == 2597 && objectX == 2931){//orange door
			if(player.getInventory().playerHasItem(QuestConstants.MELZAR_MAZE_ORANGE_KEY)){
				player.getInventory().removeItem(new Item(QuestConstants.MELZAR_MAZE_ORANGE_KEY, 1));
				player.getActionSender().walkTo(player.getPosition().getX() < 2931 ? 1 : -1, 0, true);
				player.getActionSender().walkThroughDoor(2597, 2931, objectY, 1);
				player.getActionSender().sendMessage("The key disintegrates as it unlocks the door.");
				return true;
			} else {
				player.getActionSender().sendMessage("It is locked.");
				return true;
			}
		}
		if(objectId == 2598 && objectY == 3249){//yellow door
			if(player.getInventory().playerHasItem(QuestConstants.MELZAR_MAZE_YELLOW_KEY)){
				player.getInventory().removeItem(new Item(QuestConstants.MELZAR_MAZE_YELLOW_KEY, 1));
				player.getActionSender().walkTo(0, player.getPosition().getY() < 3250 ? 1 : -1, true);
				player.getActionSender().walkThroughDoor(2598, objectX, 3249, 2);
				player.getActionSender().sendMessage("The key disintegrates as it unlocks the door.");
				return true;
			} else {
				player.getActionSender().sendMessage("It is locked.");
				return true;
			}
		}
		if(objectId == 2598 && objectX == 2936 && objectY == 3256){//yellow door
			if(player.getInventory().playerHasItem(QuestConstants.MELZAR_MAZE_YELLOW_KEY)){
				player.getInventory().removeItem(new Item(QuestConstants.MELZAR_MAZE_YELLOW_KEY, 1));
				player.getActionSender().walkTo(player.getPosition().getX() < 2936 ? 1 : -1, 0, true);
				player.getActionSender().walkThroughDoor(2598, 2936, 3256, 2);
				player.getActionSender().sendMessage("The key disintegrates as it unlocks the door.");
				return true;
			} else {
				player.getActionSender().sendMessage("It is locked.");
				return true;
			}
		}
		if(objectId == 2599 && objectX == 2931 && objectY == 9643){//blue door
			if(player.getInventory().playerHasItem(QuestConstants.MELZAR_MAZE_BLUE_KEY)){
				player.getInventory().removeItem(new Item(QuestConstants.MELZAR_MAZE_BLUE_KEY, 1));
				player.getActionSender().walkTo(player.getPosition().getX() < 2931 ? 1 : -1, player.getPosition().getY() < 9643 ? 1 : 0, true);
				player.getActionSender().walkThroughDoor(2599, 2931, 9643, 0);
				player.getActionSender().sendMessage("The key disintegrates as it unlocks the door.");
				return true;
			} else {
				player.getActionSender().sendMessage("It is locked.");
				return true;
			}
		}
		if(objectId == 2600 && objectX == 2929 && objectY == 9652){//magenta door
			if(player.getInventory().playerHasItem(QuestConstants.MELZAR_MAZE_MAGENTA_KEY)){
				player.getInventory().removeItem(new Item(QuestConstants.MELZAR_MAZE_MAGENTA_KEY, 1));
				player.getActionSender().walkTo(0, player.getPosition().getY() < 9652 ? 1 : -1, true);
				player.getActionSender().walkThroughDoor(2600, 2929, 9652, 0);
				player.getActionSender().sendMessage("The key disintegrates as it unlocks the door.");
				return true;
			} else {
				player.getActionSender().sendMessage("It is locked.");
				return true;
			}
		}
		if(objectId == 2601 && objectX == 2936 && objectY == 9655){//green door
			if(player.getInventory().playerHasItem(QuestConstants.MELZAR_MAZE_GREEN_KEY)){
				player.getInventory().removeItem(new Item(QuestConstants.MELZAR_MAZE_GREEN_KEY, 1));
				player.getActionSender().walkTo(0, player.getPosition().getY() < 9656 ? 1 : -1, true);
				player.getActionSender().walkThroughDoor(2601, 2936, 9655, 0);
				player.getActionSender().sendMessage("The key disintegrates as it unlocks the door.");
				return true;
			} else {
				player.getActionSender().sendMessage("It is locked.");
				return true;
			}
		}
		if(objectId == 2602 && objectX == 2938 && objectY == 3252){//door
			if(player.getPosition().getX() > 2937){
				player.getActionSender().walkTo(-1, 0, true);
				player.getActionSender().walkThroughDoor(2602, 2938, 3252, 0);
			}else
				player.getActionSender().sendMessage("It won't open.");
			return true;
		}
		if(objectId == 2602 && objectX == 2924 && objectY == 9654){//door
			if(player.getPosition().getX() > 2923){
				player.getActionSender().walkTo(-1, 0, true);
				player.getActionSender().walkThroughDoor(2602, 2924, 9654, 0);
			}else
				player.getActionSender().sendMessage("It won't open.");
			return true;
		}
		if(objectId == 2602 && objectX == 2927 && objectY == 9649){//door
			if(player.getPosition().getX() > 2926){
				player.getActionSender().walkTo(-1, 0, true);
				player.getActionSender().walkThroughDoor(2602, 2927, 9649, 0);
			}else
				player.getActionSender().sendMessage("It won't open.");
			return true;
		}
		if(objectId == 2602 && objectX == 2931 && objectY == 9640){//door
			if(player.getPosition().getX() > 2930){
				player.getActionSender().walkTo(-1, 0, true);
				player.getActionSender().walkThroughDoor(2602, 2931, 9640, 0);
			}else
				player.getActionSender().sendMessage("It won't open.");
			return true;
		}
		if(objectId == 1530 && objectX == 2935 && objectY == 3256){//door
			if(player.getPosition().getY() < 3257){
				player.getActionSender().walkTo(0, 1, true);
				player.getActionSender().walkThroughDoor(1530, 2935, 3256, 1);
			}else
				player.getActionSender().sendMessage("It won't open.");
			return true;
		}
		if(objectId == 2605 && objectX == 2932 && objectY == 3240){
			Ladders.climbLadder(player, new Position(2933,9640,0));
			return true;
		}
		return false;
	}
	
	public boolean handleFirstClickItem(final Player player, int interfaceId, int itemId, int stage){
		if(interfaceId == 3214){//inventory
			if(itemId == QuestConstants.MAP_PIECE_MELZAR){
				player.getDialogue().sendItem1Line("This is Melzar's piece of the map.", new Item(QuestConstants.MAP_PIECE_MELZAR, 1));
				return true;
			}
			if(itemId == QuestConstants.MAP_PIECE_THALZAR){
				player.getDialogue().sendItem1Line("This is Thalzar's piece of the map.", new Item(QuestConstants.MAP_PIECE_THALZAR, 1));
				return true;
			}
			if(itemId == QuestConstants.MAP_PIECE_LOZAR){
				player.getDialogue().sendItem1Line("This is Lozar's piece of the map.", new Item(QuestConstants.MAP_PIECE_LOZAR, 1));
				return true;
			}
		}
		return false;
	}
	
	public boolean handleItemOnItem(final Player player, int item1, int item2, int stage){
		if(hasAllMapParts(player)){
			if(isMapPart(item1) && isMapPart(item2)){
				player.getInventory().removeItem(new Item(QuestConstants.MAP_PIECE_MELZAR, 1));
	    		player.getInventory().removeItem(new Item(QuestConstants.MAP_PIECE_LOZAR, 1));
	    		player.getInventory().removeItem(new Item(QuestConstants.MAP_PIECE_THALZAR, 1));
	    		player.getInventory().addItem(new Item(QuestConstants.CRANDOR_MAP, 1));
				player.getDialogue().send2Items2Lines("You put the three pieces together and assemble a map",
													"that shows the route through the reefs to Crandor.", new Item(-1, 1), new Item(QuestConstants.CRANDOR_MAP, 1));
				player.getDialogue().endDialogue();
				return true;
			}
		}
		return false;
	}
	
	boolean isMapPart(int id){
		if(id == QuestConstants.MAP_PIECE_MELZAR || id == QuestConstants.MAP_PIECE_LOZAR || id == QuestConstants.MAP_PIECE_THALZAR)
			return true;
		return false;
	}
	
	boolean hasAllMapParts(final Player player){
		if(player.getInventory().playerHasItem(QuestConstants.MAP_PIECE_MELZAR) && player.getInventory().playerHasItem(QuestConstants.MAP_PIECE_LOZAR) && player.getInventory().playerHasItem(QuestConstants.MAP_PIECE_THALZAR))
			return true;
		return false;
	}
	
	public boolean handleNpcDeath(final Player player, int npcId, int stage){
		if(npcId == QuestConstants.ELVARG){
			if(stage == 261){
				player.teleport(new Position(2846, 9636, 0));
				questCompleted(player);
				return true;
			}
		}
		return false;
	}
	
	public boolean controlDying(final Entity attacker, final Entity victim, int stage){
		if(victim.isNpc() && attacker.isPlayer()){
        	Player player = (Player) attacker;
        	Npc npc = (Npc) victim;
        	if(npc.getNpcId() == QuestConstants.WORMBRAIN){
        		npc.setCurrentHp(npc.getMaxHp());
        		npc.getUpdateFlags().setForceChatMessage("Ow!");
        		player.getActionSender().sendMessage("Wormbrain drops a map piece on the floor.");
        		GroundItem drop = new GroundItem(new Item(QuestConstants.MAP_PIECE_LOZAR, 1), player, npc.getPosition());
		        GroundItemManager.getManager().dropItem(drop);
        		return true;
        	}
        }
		return false;
	}
	
	String[] mes = {"Feel the wrath of my feet!", "Let me drink my tea in peace!", "By the Power of Custard!", "Leave me alone, I need to feed my pet rock!"};
	
	public int controlCombatDamage(final Entity attacker, final Entity victim, int stage){
		if(attacker.isNpc()){
        	Npc npc = (Npc) attacker;
        	if(npc.getNpcId() == QuestConstants.MELZAR_THE_MAD){
        		npc.getUpdateFlags().setForceChatMessage(mes[Misc.random(mes.length-1)]);
        	}
        }
		return -1;
	}
	
	public boolean allowedToAttackNpc(final Player player, int npcId, int stage){
		if(npcId == QuestConstants.WORMBRAIN){
			if(stage >= 4 && stage < 260){
				for(int i = 0; i < player.getGroundItems().size(); i++){
					GroundItem gi = player.getGroundItems().get(i);
					if(gi.getItem().getId() == QuestConstants.MAP_PIECE_LOZAR){
						player.getActionSender().sendMessage("You have just beaten Wormbrain up. Give the poor goblin a break.");
						return false;
					}
				}
				if(player.hasItem(QuestConstants.MAP_PIECE_LOZAR) || player.hasItem(QuestConstants.CRANDOR_MAP)){
					player.getActionSender().sendMessage("You already have the map piece from Wormbrain.");
					return false;
				}else
					return true;
			}
			if(stage <= 3)
				return false;
		}
		return true;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		int value = stage-4;
		//player.questStage[this.getId()] = 0;
		if(npcId == QuestConstants.GUILD_MASTER){
			if(stage == 0 || stage == 2 || stage == 3 || stage == 4 || (stage > 4 && stage < 260)){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Greetings!",Dialogues.CONTENT);
					if(stage == 3)
						player.getDialogue().setNextChatId(14);
					if(stage >= 4)
						player.getDialogue().setNextChatId(14);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("What is this place?",
													"Can I have a quest?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("What is this place?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Can I have a quest?", Dialogues.CONTENT);
						if(stage == 0)
							player.getDialogue().setNextChatId(4);
						if(stage == 2)
							player.getDialogue().setNextChatId(10);
						return true;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Aha!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Yes! A mighty and perilous quest fit only for the most",
													"powerful champions! And, at the end of it, you will earn",
													"the right to wear the legendary rune platebody!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("So, what is this quest?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("You'll have to speak to Oziach, the maker of rune",
													"armour. He sets the quests that champions must",
													"complete in order to wear it.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Oziach lives in a hut, by the cliffs to the west of",
													"Edgeville. He can be a little...odd...sometimes, though.",Dialogues.CONTENT);
					questStarted(player);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("This is the Champions' Guild. Only adventurers who",
													"have proved themselves worthy by gaining influence",
													"from quests are allowed in here.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("You're already on a quest for me, If I recall correctly.",
													"Have you talked to Oziach yet?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendPlayerChat("No, not yet.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("Well, he's the only one who can grant you the right to",
													"wear rune platemail. He lives in a hut, by the cliffs west",
													"of Edgeville.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendPlayerChat("Okay, I'll go and talk to him.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendOption("What is this place?",
													"I talked to Oziach...");
					return true;
				}
				if(chatId == 15){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("What is this place?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("I talked to Oziach and he gave me a quest.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(16);
						return true;
					}
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("Oh? What did he tell you to do?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendPlayerChat("Defeat the dragon of Crandor.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendNpcChat("The dragon of Crandor?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 19){
					player.getDialogue().sendPlayerChat("Um, yes...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 20){
					player.getDialogue().sendNpcChat("Goodness, he hasn't given you an easy job, has he?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 21){
					player.getDialogue().sendPlayerChat("What's so special about this dragon?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 22){
					player.getDialogue().sendNpcChat("Thirty years ago, Crandor was thriving community",
													"with a great tradition of mages and adventurers. Many",
													"Crandorians even earned the right to be part of the",
													"Champions' Guild!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 23){
					player.getDialogue().sendNpcChat("One of their adventurers went too far, however. He",
													"descended into the volcano in the centre of Crandor",
													"and woke the dragon Elvarg.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 24){
					player.getDialogue().sendNpcChat("He must have fought valiantly against the dragon",
													"because they say that, to this day, she has a scar down",
													"her side,",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 25){
					player.getDialogue().sendNpcChat("but the dragon still won the fight. She emerged and laid",
													"waste to the whole of Crandor with her fire breath!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 26){
					player.getDialogue().sendNpcChat("Some refugees managed to escape in fishing boats.",
													"They landed on the coast, north of Rimmington, and",
													"set up camp but the dragon followed them and burned",
													"the camp to the ground.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 27){
					player.getDialogue().sendNpcChat("Out of all the people of Crandor there were only three",
													"survivors: a trio of wizards who used magic to escape.",
													"Their names were Thalzar, Lozar and Melzar.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 28){
					player.getDialogue().sendNpcChat("If you're serious about taking on Elvarg, first you'll",
													"need to get to Crandor. The island is surrounded by",
													"dangerous reefs, so you'll need a ship capable of getting",
													"through them and a map to show you the way.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 29){
					player.tempQuestInt = 0;
					player.getDialogue().sendNpcChat("When you reach Crandor, you'll also need some kind of",
													"protection against the dragon's breath.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 30){
					if(player.tempQuestInt == 0)
						player.getDialogue().sendOption("How can I find the route to Crandor?",
														"Where can I find the right ship?",
														"How can I protect myself from the dragon's breath?",
														"What's so special about this dragon?",
														"Okay, I'll get going!");
					if(player.tempQuestInt == 1)
						player.getDialogue().sendOption("Where is Thalzar's map piece?",
														"Where is Lozar's map piece?",
														"Where can I find the right ship?",
														"How can I protect myself from the dragon's breath?",
														"Okay, I'll get going!");
					if(player.tempQuestInt == 2)
						player.getDialogue().sendOption("Where is Melzar's map piece?",
														"Where is Thalzar's map piece?",
														"Where can I find the right ship?",
														"How can I protect myself from the dragon's breath?",
														"Okay, I'll get going!");
					if(player.tempQuestInt == 4)
						player.getDialogue().sendOption("How can I find the route to Crandor?",
														"How can I protect myself from the dragon's breath?",
														"Okay, I'll get going!");
					if(player.tempQuestInt == 8)
						player.getDialogue().sendOption("How can I find the route to Crandor?",
														"Where can I find the right ship?",
														"Okay, I'll get going!");
					if(player.tempQuestInt == 16)
						player.getDialogue().sendOption("Where is Melzar's map piece?",
														"Where is Lozar's map piece?",
														"Where can I find the right ship?",
														"How can I protect myself from the dragon's breath?",
														"Okay, I'll get going!");
					return true;
				}
				if(chatId == 31){
					if(optionId >= 1 && optionId < 5 && stage == 3){
						player.questStage[this.getId()] = 4;
					}
					if(optionId == 1){
						if(player.tempQuestInt == 0 || player.tempQuestInt == 4 || player.tempQuestInt == 8){
							player.getDialogue().sendPlayerChat("How can I find the route to Crandor?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(32);
						}
						if(player.tempQuestInt == 1){
							player.getDialogue().sendPlayerChat("Where is Thalzar's map piece?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(55);
						}
						if(player.tempQuestInt == 2 || player.tempQuestInt == 16){
							player.getDialogue().sendPlayerChat("Where is Melzar's map piece?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(35);
						}
						return true;
					}
					if(optionId == 2){
						if(player.tempQuestInt == 0 || player.tempQuestInt == 8){
							player.getDialogue().sendPlayerChat("Where can I find the right ship?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(45);
						}
						if(player.tempQuestInt == 1 || player.tempQuestInt == 16){
							player.getDialogue().sendPlayerChat("Where is Lozar's map piece?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(41);
						}
						if(player.tempQuestInt == 2){
							player.getDialogue().sendPlayerChat("Where is Thalzar's map piece?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(55);
						}
						if(player.tempQuestInt == 4){
							player.getDialogue().sendPlayerChat("How can I protect myself from the dragon's breath?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(50);
						}
						return true;
					}
					if(optionId == 3){
						if(player.tempQuestInt == 0){
							player.getDialogue().sendPlayerChat("How can I protect myself from the dragon's breath?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(50);
						}
						if(player.tempQuestInt != 0 && player.tempQuestInt != 4 && player.tempQuestInt != 8){
							player.getDialogue().sendPlayerChat("Where can I find the right ship?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(45);
						}
						if(player.tempQuestInt == 4 || player.tempQuestInt == 8){
							player.getDialogue().sendPlayerChat("Okay, I'll get going!", Dialogues.CONTENT);
							player.getDialogue().endDialogue();
						}
						return true;
					}
					if(optionId == 4){
						if(player.tempQuestInt == 0){
							player.getDialogue().endDialogue();
							return false;
						}
						if(player.tempQuestInt != 0){
							player.getDialogue().sendPlayerChat("How can I protect myself from the dragon's breath?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(50);
						}
					}
					if(optionId == 5){
						player.getDialogue().sendPlayerChat("Okay, I'll get going!", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}
				}
				if(chatId == 32){
					player.getDialogue().sendNpcChat("Only one map exists that shows the route through the",
													"reefs of Crandor. That map was split into three pieces",
													"by Melzar, Thalzar and Lozar, the wizards who escaped",
													"from the dragon. Each of them took one piece.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 33){
					player.getDialogue().sendOption("Where is Melzar's map piece?",
													"Where is Thalzar's map piece?",
													"Where is Lozar's map piece?");
					return true;
				}
				if(chatId == 34){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Where is Melzar's map piece?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(35);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Where is Thalzar's map piece?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(55);
						return true;
					}
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("Where is Lozar's map piece?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(41);
						return true;
					}
				}
				if(chatId == 35){
					player.tempQuestInt = 1;
					player.getDialogue().sendNpcChat("Melzar built a castle on the site of the Crandorian",
													"refugee camp, north of Rimmington. He's locked himself",
													"in there and no one's seen him for years.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 36){
					player.getDialogue().sendNpcChat("The inside of his castle is like a maze, and is populated",
													"by undead monsters. Maybe, if you could get all the",
													"way through the maze, you could find his piece of the",
													"map.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 37){
					player.getDialogue().sendNpcChat("Adventurers sometimes go in there to prove themselves,",
													"so I can give you this key to Melzar's Maze.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 38){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.MELZAR_MAZE_KEY, 1));
					player.getDialogue().sendItem1Line("The Guild master hands you a key.", new Item(QuestConstants.MELZAR_MAZE_KEY, 1));
					player.getDialogue().setNextChatId(30);
					return true;
				}
				if(chatId == 41){
					player.tempQuestInt = 2;
					player.getDialogue().sendNpcChat("A few weeks ago, I'd have told you to speak to Lozar",
													"herself, in her house across the river from Lumbridge.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 42){
					player.getDialogue().sendNpcChat("Unfortunately, goblin raiders killed her and stole",
													"everything. One of the goblins from the Goblin Village",
													"probably has the map piece now.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(30);
					return true;
				}
				if(chatId == 45){
					player.tempQuestInt = 4;
					player.getDialogue().sendNpcChat("Even if you find the right route, only a ship made to",
													"the old crandorian design would be able to navigate",
													"through the reefs to the island.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 46){
					player.getDialogue().sendNpcChat("If there's still one in existence, it's probably in Port",
													"Sarim.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 47){
					player.getDialogue().sendNpcChat("Then, of course, you'll need to find a captain willing to",
													"sail to Crandor, and I'm not sure where you'd find one",
													"of them!",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(30);
					return true;
				}
				if(chatId == 50){
					player.tempQuestInt = 8;
					player.getDialogue().sendNpcChat("That part shouldn't be too difficult, actually. I believe",
													"the Duke of Lumbridge has a special shield in his",
													"armoury that is enchanted against dragon's breath.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(30);
					return true;
				}
				if(chatId == 53){
					player.getDialogue().sendOption("What is this place?",
													"About my quest to kill the dragon...");
					return true;
				}
				if(chatId == 54){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("What is this place?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("About my quest to kill the dragon...", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(28);
						return true;
					}
				}
				if(chatId == 55){
					player.tempQuestInt = 16;
					player.getDialogue().sendNpcChat("Thalzar was the most paranoid of the three wizards. He",
													"hid his map piece and took the secret of its location to",
													"his grave.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 56){
					player.getDialogue().sendNpcChat("I don't think you'd be able to find out where it is by",
													"ordinary means. You'll need to talk to the Oracle on",
													"Ice Mountain.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(30);
					return true;
				}
			}//end of stage 0,2,3,4
		}//end of npc 198
		if(npcId == QuestConstants.OZIACH){
			if(stage == 2){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Aye, 'tis a fair day my friend.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Can you sell me a rune platebody?",
													"I'm not your friend.",
													"Yes, it's a very nice day.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Can you sell me a rune platebody?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("Yes, it's a very nice day.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Aye, may the gods walk by yer side. Now leave me",
													"alone.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("So, how does thee know I 'ave some?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendOption("The Guild master of the Champions' Guild told me.",
													"I am a master detective.");
					return true;
				}
				if(chatId == 7){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("The Guild master of the Champions' Guild told me.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(8);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Yes, I suppose he would, wouldn't he? He's always",
													"sending you fancy-pants 'heroes' up to bother me.",
													"Telling me I'll give them a quest or sommat like that.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Well, I'm not going to let just anyone wear my rune",
													"platemail. It's only for heroes. So, leave me alone.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendOption("I thought you were going to give me a quest.",
													"That's a pity, I'm not a hero.");
					return true;
				}
				if(chatId == 11){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I thought you were going to give me a quest.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(12);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("*Sigh*",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("All right, I'll give ye a quest. I'll let ye wear my rune",
													"platemail if ye...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("Slay the dragon of Crandor!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendOption("A dragon, that sounds like fun.",
													"I may be a champion, but I don't think I'm up to dragon-killing yet.");
					return true;
				}
				if(chatId == 16){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("A dragon, that sounds like fun!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(17);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 17){
					player.getDialogue().sendNpcChat("Hah, yes, you are a typical reckless adventurer, aren't",
													"you? Now go kill the dragon and get out of my face.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendPlayerChat("But how can I defeat the dragon?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 19){
					player.getDialogue().sendNpcChat("Go talk to the Guild master in the Champions' Guild. He'll",
													"help ye out if yer so keen on doing a quest. I'm not",
													"going to be handholding any adventurers.",Dialogues.CONTENT);
					player.questStage[this.getId()] = 3;
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 2
		}//end of npc 747
		if(npcId == QuestConstants.ORACLE){
			if(stage == 4){
				if(chatId == 1){
					player.getDialogue().sendOption("I seek a piece of the map to the island of Crandor.",
													"Can you impart your wise knowledge to me, O Oracle?");
					return true;
				}
				if(chatId == 2){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I seek a piece of the map to the island of Crandor.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(3);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Can you impart your wise knowledge to me, O Oracle?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("The map's behind a door below,",
													"but entering is rather tough.",
													"This is what you need to know:",
													"You must use the following stuff.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("First, a drink used by a mage.",
													"Next, some worm string, changed to sheet.",
													"Then, a small crustacean cage.",
													"Last, a bowl that's not seen heat.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("The God Wars are over...as long as the thing they",
													"were fighting over remains hidden.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 4
		}//end of npc 746
		if(npcId == QuestConstants.KLARENSE){
			if(stage >= 4 && (value & 1) == 0 && stage < 259){
				if(chatId == 1){
					player.tempQuestInt = 0;
					player.getDialogue().sendNpcChat("So, are you interested in buying a ship? Now, I'll be",
													"straight with you: she's not quite seaworthy right now,",
													"but give her a bit of work and she'll be the nippiest ship",
													"this side of Port Khazard.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					if(player.tempQuestInt == 0)
						player.getDialogue().sendOption("Do you know when she will be seaworthy?",
														"Would you take me to Crandor when she's ready?",
														"Why is she damaged?",
														"I'd like to buy her.",
														"Ah well, never mind.");
					if(player.tempQuestInt == 1)
						player.getDialogue().sendOption("Would you take me to Crandor when she's ready?",
														"Why is she damaged?",
														"I'd like to buy her.",
														"Ah well, never mind.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						if(player.tempQuestInt == 0){
							player.getDialogue().sendPlayerChat("Do you know when she will be seaworthy?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(4);
						}
						if(player.tempQuestInt == 1){
							player.getDialogue().endDialogue();
							return false;
						}
						return true;
					}
					if(optionId == 2){
						player.getDialogue().endDialogue();
						return false;
					}
					if(optionId == 3){
						if(player.tempQuestInt == 1){
							player.getDialogue().sendPlayerChat("I'd like to buy her.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(5);
							return true;
						}
					}
					if(optionId == 4){
						if(player.tempQuestInt == 0){
							player.getDialogue().sendPlayerChat("I'd like to buy her.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(5);
							return true;
						}
					}
				}
				if(chatId == 4){
					player.tempQuestInt = 1;
					player.getDialogue().sendNpcChat("No, not really. Port Sarim's shipbuilders aren't very",
													"efficient so it could be quite a while.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(2);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Of course! I'm sure the work needed to do on it",
													"wouldn't be too expensive.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("How does 2,000 gold sound? I'll even throw in my",
													"cabin boy, Jenkins, for free! He'll swab the decks and",
													"splice the mainsails for you!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendOption("Yep, sounds good.",
													"I'm not paying that much for a broken boat!");
					return true;
				}
				if(chatId == 8){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yep, sounds good.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 9){
					if(player.getInventory().playerHasItem(QuestConstants.COINS, 2000)){
						player.getInventory().removeItem(new Item(QuestConstants.COINS, 2000));
						player.questStage[this.getId()] += 1;
						player.getDialogue().sendNpcChat("Okey dokey, she's all yours!",Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}
				}
			}
		}//end of npc 744
		if(npcId == QuestConstants.DUKE_HORACIO){
			if(stage >= 4 && !player.hasItem(QuestConstants.ANTIDRAGON_SHIELD)){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Greetings. Welcome to my castle.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("I seek a shield that will protect me from dragonbreath.",
													"Have you any quests for me?",
													"Where can I find money?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I seek a shield that will protect me from dragonbreath.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("A knight going on a dragon quest, hmm? What dragon",
													"do you intend to slay?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendOption("Elvarg, the dragon of Crandor island!",
													"Oh, no dragon in particular");
					return true;
				}
				if(chatId == 6){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Elvarg, the dragon of Crandor island!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(7);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Elvarg? Are you sure?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendOptionWTitle("Well, are you sure?",
														"Yes",
														"No");
					return true;
				}
				if(chatId == 9){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yes!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(10);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Well, you're a braver man than I!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendPlayerChat("Why is everyone so scared of this dragon?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("Back in my father's day, Crandor was an important",
													"city-state. Politically, it was as important as Falador or",
													"Varrock and its ships traded with every port.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("But, one day when I was little, all contact was lost. The",
													"trading ships and the diplomatic envoys just stopped",
													"coming.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("I remember my father being very scared. He posted",
													"lookouts on the roof to warn if the dragon was",
													"approaching. All the city rulers were worried that",
													"Elvarg would devastate the whole continent.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendOption("I'd better leave that dragon alone.",
													"So, are you going to give me the shield or not?");
					return true;
				}
				if(chatId == 16){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("So, are you going to give me the shield or not?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(17);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 17){
					player.getDialogue().sendNpcChat("If you really think you're up to it then perhaps you",
													"are the one who can kill this dragon.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.ANTIDRAGON_SHIELD, 1));
					player.getDialogue().sendItem1Line("The Duke hands you a heavy orange shield.", new Item(QuestConstants.ANTIDRAGON_SHIELD, 1));
					player.getDialogue().setNextChatId(19);
					return true;
				}
				if(chatId == 19){
					player.getDialogue().sendNpcChat("Take care out there. If you kill it...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 20){
					player.getDialogue().sendNpcChat("If you kill it, for Saradomin's sake make sure it's really",
													"dead!",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 741
		if(npcId == QuestConstants.NED){
			if(stage == 259){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Why, hello there, lad. Me friends call me Ned. I was a",
													"man of the sea, but it's past me now. Could I be",
													"making or selling you some rope?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("You're a sailor? Could you take me to Crandor?",
													"Yes, I would like some rope.",
													"No thanks Ned, I don't need any.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("You're a sailor? Could you take me to the island of",
															"Crandor?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Well, I was a sailor. I've not been able to get work at",
													"sea these days, though. They say I'm too old.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Sorry, where was it you said you wanted to go?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("To the island of Crandor.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Crandor?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("But... It would be a chance to sail a ship once more.",
													"I'd sail anywhere if it was a chance to sail again.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Then again, no captain in his right mind would sail to",
													"that island...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Ah, you only live once! I'll do it!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("So, where's your ship?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendPlayerChat("It's the Lady Lumbridge, in Port Sarim.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("That old pile of junk? Last I heard, she wasn't",
													"seaworthy.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendPlayerChat("I fixed her up!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendNpcChat("You did? Excellent!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("Just show me the map and we can get ready to go!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendPlayerChat("Here you go.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					if(player.getInventory().playerHasItem(QuestConstants.CRANDOR_MAP)){
						player.getInventory().removeItem(new Item(QuestConstants.CRANDOR_MAP, 1));
						player.questStage[this.getId()] = 260;
						player.getDialogue().sendItem1Line("You hand the map to Ned.", new Item(QuestConstants.CRANDOR_MAP, 1));
						return true;
					}
				}
			}
			if(stage == 260){
				if(chatId == 19){
					player.getDialogue().sendNpcChat("Excellent! I'll meet you at the ship, then.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 918
		if(npcId == QuestConstants.CABIN_BOY_JENKINS){
			if(chatId == 1){
				player.getDialogue().sendNpcChat("Ahoy! What d'ye think of yer ship then?",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 2){
				player.getDialogue().sendOption("I'm ready to go back to shore.",
												"I'd like to inspect her some more.",
												"Can you sail this ship to Crandor?");
				return true;
			}
			if(chatId == 3){
				if(optionId == 1){
					player.getDialogue().endDialogue();
					return false;
				}
				if(optionId == 2){
					player.getDialogue().sendPlayerChat("I'd like to inspect her some more.", Dialogues.CONTENT);
					player.getDialogue().setNextChatId(9);
					return true;
				}
				if(optionId == 3){
					player.getDialogue().sendPlayerChat("Can you sail this ship to Crandor?", Dialogues.CONTENT);
					player.getDialogue().setNextChatId(4);
					return true;
				}
			}
			if(chatId == 4){
				player.getDialogue().sendNpcChat("Not me, sir! I'm just an 'umble cabin boy, You'll need",
												"a proper cap'n.",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 5){
				player.getDialogue().sendPlayerChat("Where can I find a captain?", Dialogues.CONTENT);
				return true;
			}
			if(chatId == 6){
				player.getDialogue().sendNpcChat("The cap'n s round 'ere seem to be a mite scared of",
												"Crandor. I ask 'em why and they just say it was afore",
												"my time,",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 7){
				player.getDialogue().sendNpcChat("but there is one cap'n I reckon might 'elp. I 'eard",
												"there's a retired 'un who lives in Draynor Village who's",
												"so desperate to sail again 'e'd take any job.",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 8){
				player.getDialogue().sendNpcChat("I can't remember 'is name, but 'e lives in Draynor",
												"Village an' makes rope.",Dialogues.CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
			if(chatId == 9){
				player.getDialogue().sendNpcChat("Aye.",Dialogues.CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		}//end of npc 754
		if(npcId == QuestConstants.WORMBRAIN){
			if(stage >= 4 && ((!player.hasItem(QuestConstants.MAP_PIECE_LOZAR) && !player.hasItem(QuestConstants.CRANDOR_MAP) && chatId < 11) || chatId == 11)){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Whut you want?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("I believe you've got a piece of map that I need.",
													"What are you in for?",
													"Sorry, thought this was a zoo.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I believe you've got a piece of map that I need.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("So? Why should I be giving it to you? What you do",
													"for Wormbrain?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendOption("I'm not going to do anything for you. Forget it.",
													"I'll let you live. I could just kill you.",
													"I suppose I could pay you for the map piece...",
													"Where did you get the map piece from?");
					return true;
				}
				if(chatId == 6){
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("I suppose I could pay you for the map piece. Say, 500",
															"coins?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(7);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Me not stoopid, it worth at least 10,000 coins!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendOption("You must be joking! Forget it.",
													"Alright then, 10,000 it is.");
					return true;
				}
				if(chatId == 9){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Alright then, 10,000 coins it is.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(10);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 10){
					if(player.getInventory().playerHasItem(QuestConstants.COINS, 10000)){
						player.getInventory().removeItem(new Item(QuestConstants.COINS, 10000));
						player.getInventory().addItemOrDrop(new Item(QuestConstants.MAP_PIECE_LOZAR, 1));
						player.getDialogue().sendItem1Line("You buy the map piece from Wormbrain.", new Item(QuestConstants.MAP_PIECE_LOZAR, 1));
						return true;
					}
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("Fank you very much! Now me can bribe da guards,",
													"hehehe.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 745
		if(npcId == QuestConstants.NED_ON_BOAT){
			if(stage == 260){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Ah! There you are! Ready to go?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Yep, lets go!",
													"No, I'm not quite ready yet...");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yep, lets go!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					Sailing.sailShip(player, ShipRoute.PORT_SARIM_TO_CRANDOR);
					player.questStage[this.getId()] = 261;
					player.getActionSender().sendMessage("You feel the ship begin to move...");
					player.getActionSender().sendMessage("The ship is sailing across the ocean...");
					player.getDialogue().dontCloseInterface();
					return false;
				}
			}//end of stage 260
			if(stage == 261){
				if(chatId == 5){
					player.getDialogue().sendStatement("You hear a loud crack as the ship comes to a juddering halt.");
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Um.... I think we're there. I probably should have paid",
													"closer attention.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("Gee... you think?", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 261
		}//end of npc 743
		return false;
	}

}
