package com.rs2.model.content.questing.quests;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class Tutorial extends Quest {
	
	public Tutorial(int id) {
		super(id);
	}
	
	public boolean getFlashIcon(final Player player, int stage){
		player.setNextTutorialStage();
		return false;
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 3014 && objectX == 3098 && objectY == 3107){
			if(stage == 5){
				player.getActionSender().walkTo(1, 0, true);
				player.getActionSender().walkThroughDoor(3014, 3098, 3107, player.getPosition().getZ());
				player.setNextTutorialStage();
				return true;
			} else {
				return true;
			}
		}
		if((objectId == 3016 && objectX == 3089 && objectY == 3091) || (objectId == 3015 && objectX == 3089 && objectY == 3092)){
			if(stage == 15){
				player.getActionSender().walkTo(-1, 0, true);
				//player.getActionSender().walkThroughTutIsGate(3015, 3016, 3089, 3092, 3089, 3092 - 1, 0);
				player.getActionSender().sendSound(318, 1, 0);
				player.setNextTutorialStage();
				return true;
			} else {
				return true;
			}
		}
		if(objectId == 3017 && objectX == 3079 && objectY == 3084){
			if(stage == 16){
				player.getActionSender().walkTo(-1, 0, true);
				//player.getActionSender().walkThroughDoor2(3017, 3079, 3084, 0);
				player.getActionSender().walkThroughDoor(3017, 3079, 3084, 0);
				player.setNextTutorialStage();
				return true;
			} else {
				return true;
			}
		}
		if(objectId == 3018 && objectX == 3072 && objectY == 3090){
			if(stage >= 21){
				player.getActionSender().walkTo(-1, 0, true);
				//player.getActionSender().walkThroughDoor3(3018, 3072, 3090, 0);
				player.getActionSender().walkThroughDoor(3018, 3072, 3090, 0);
				if(stage == 21){
					player.getActionSender().createPlayerHints(1, -1);
					player.setNextTutorialStage();
				}
				return true;
			} else {
				return true;
			}
		}
		if(objectId == 3019 && objectX == 3086 && objectY == 3126){
			if(stage == 24){
				player.getActionSender().walkTo(0, -1, true);
				player.getActionSender().walkThroughDoor(3019, 3086, 3126, 0);
				player.setNextTutorialStage();
				return true;
			} else {
				return true;
			}
		}
		if(objectId == 3029 && objectX == 3088 && objectY == 3119){
			if(stage == 28){
				Ladders.climbLadder(player, new Position(3088, 9520, 0));
				player.setNextTutorialStage();
				return true;
			} else {
				return true;
			}
		}
		if((objectId == 3021 && objectX == 3094 && objectY == 9502) || (objectId == 3020 && objectX == 3094 && objectY == 9503)){
			if(stage == 39){
				player.getActionSender().walkTo(1, 0, true);
				//player.getActionSender().walkThroughDoor2(3021, 3020, 3094, 9502, 3094, 9503, 0);
				player.getActionSender().sendSound(318, 1, 0);
				player.setNextTutorialStage();
				return true;
			} else {
				return true;
			}
		}
		if((objectId == 3022 && objectX == 3111 && objectY == 9518) || (objectId == 3023 && objectX == 3111 && objectY == 9519)){
			if(stage >= 46 && stage <= 50){
				player.getActionSender().walkTo(player.getPosition().getX() < 3111 ? 1 : -1, 0, true);
				player.getActionSender().walkThroughDoor3(3022, 3023, 3111, 9518, 3111, 9519, 0);
				if(stage == 46){
					player.setNextTutorialStage();
				}
				return true;
			} else {
				return true;
			}
		}
		if(objectId == 3030 && objectX == 3111 && objectY == 9526){
			if(stage == 50){
				Ladders.climbLadder(player, new Position(3111, 3125, 0));
				player.setNextTutorialStage();
				return true;
			} else {
				return true;
			}
		}
		if(objectId == 3024 && objectX == 3125 && objectY == 3124){
			if(stage == 52){
				player.getActionSender().walkTo(1, 0, true);
				player.getActionSender().walkThroughDoor(3024, 3125, 3124, 0);
				player.setNextTutorialStage();
				return true;
			} else {
				return true;
			}
		}
		if(objectId == 3025 && objectX == 3130 && objectY == 3124){
			if(stage == 54){
				player.getActionSender().walkTo(1, 0, true);
				player.getActionSender().walkThroughDoor(3025, 3130, 3124, 0);
				player.setNextTutorialStage();
				return true;
			} else {
				return true;
			}
		}
		if(objectId == 3026 && objectX == 3122 && objectY == 3102){
			if(stage == 61){
				player.getActionSender().walkTo(0, -1, true);
				player.getActionSender().walkThroughDoor(3026, 3122, 3102, 0);
				player.setNextTutorialStage();
				return true;
			} else {
				return true;
			}
		}
		return false;
	}
	
	public void setStage(final Player player, int stage){
		player.getActionSender().sendSidebarInterface(QuestConstants.LOGOUT_TAB[0], QuestConstants.LOGOUT_TAB[1]);
		if(stage < 6)
			player.getActionSender().sendConfig(406, 0);
		player.getActionSender().sendFrame171(1, 12224);
		player.getActionSender().sendFrame171(1, 12225);
		player.getActionSender().sendFrame171(1, 12226);
		player.getActionSender().sendFrame171(1, 12227);
		player.getActionSender().sendFrame171(0, 12161);
		player.getActionSender().sendString("% Done", 12224);
		player.getActionSender().sendWalkableInterface(8680);
		if(stage >= 3)
			player.getActionSender().sendSidebarInterface(QuestConstants.OPTION_TAB[0], QuestConstants.OPTION_TAB[1]);
		if(stage >= 7)
			player.getActionSender().sendSidebarInterface(QuestConstants.INVENTORY_TAB[0], QuestConstants.INVENTORY_TAB[1]);
		if(stage >= 10)
			player.getActionSender().sendSidebarInterface(QuestConstants.STATS_TAB[0], QuestConstants.STATS_TAB[1]);
		if(stage >= 20)
			player.getActionSender().sendSidebarInterface(QuestConstants.MUSIC_TAB[0], QuestConstants.MUSIC_TAB[1]);
		if(stage >= 22)
			player.getActionSender().sendSidebarInterface(QuestConstants.EMOTE_TAB[0], QuestConstants.EMOTE_TAB[1]);
		if(stage >= 26)
			player.getActionSender().sendSidebarInterface(QuestConstants.QUEST_TAB[0], QuestConstants.QUEST_TAB[1]);
		if(stage >= 41)
			player.getActionSender().sendSidebarInterface(QuestConstants.EQUIPMENT_TAB[0], QuestConstants.EQUIPMENT_TAB[1]);
		if(stage >= 45){
			player.getActionSender().sendSidebarInterface(QuestConstants.ATTACK_TAB[0], QuestConstants.ATTACK_TAB[1]);
			player.getEquipment().sendWeaponInterface();
		}
		if(stage >= 56)
			player.getActionSender().sendSidebarInterface(QuestConstants.PRAYER_TAB[0], QuestConstants.PRAYER_TAB[1]);
		if(stage >= 58)
			player.getActionSender().sendSidebarInterface(QuestConstants.FRIEND_TAB[0], QuestConstants.FRIEND_TAB[1]);
		if(stage >= 59)
			player.getActionSender().sendSidebarInterface(QuestConstants.IGNORE_TAB[0], QuestConstants.IGNORE_TAB[1]);
		if(stage >= 63)
			player.getActionSender().sendSidebarInterface(QuestConstants.MAGIC_TAB[0], QuestConstants.MAGIC_TAB[1]);
		if(stage >= 6 && stage < 12)
			player.getActionSender().sendConfig(406, 2);
		if(stage >= 12 && stage < 16)
			player.getActionSender().sendConfig(406, 3);
		if(stage >= 16 && stage < 20)
			player.getActionSender().sendConfig(406, 4);
		if(stage >= 20 && stage < 22)
			player.getActionSender().sendConfig(406, 5);
		if(stage >= 22 && stage < 25)
			player.getActionSender().sendConfig(406, 6);
		if(stage >= 25 && stage < 29)
			player.getActionSender().sendConfig(406, 7);
		if(stage >= 29 && stage < 35)
			player.getActionSender().sendConfig(406, 8);
		if(stage >= 35 && stage < 40)
			player.getActionSender().sendConfig(406, 9);
		if(stage >= 40 && stage < 46)
			player.getActionSender().sendConfig(406, 10);
		if(stage >= 46 && stage < 49)
			player.getActionSender().sendConfig(406, 11);
		if(stage >= 49 && stage < 51)
			player.getActionSender().sendConfig(406, 12);
		if(stage >= 51 && stage < 53)
			player.getActionSender().sendConfig(406, 13);
		if(stage >= 53 && stage < 55)
			player.getActionSender().sendConfig(406, 14);
		if(stage >= 55 && stage < 60)
			player.getActionSender().sendConfig(406, 15);
		if(stage >= 60 && stage < 62)
			player.getActionSender().sendConfig(406, 16);
		if(stage >= 62 && stage < 64)
			player.getActionSender().sendConfig(406, 17);
		if(stage >= 64)
			player.getActionSender().sendConfig(406, 20);
		if(stage == 0){
			player.getActionSender().sendInterface(3559);
			if(!player.hasItem(995))
				player.getBank().add(new Item(995, 25));
		}
		if(stage == 0 || stage == 2){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.RUNESCAPE_GUIDE).getIndex());
			player.getDialogue().sendStartInfo("Getting Started",
												"To start the tutorial use your left mouse-button to click on the",
												"'Guide' in this room. He is indicated by a flashing",
												"yellow arrow above his head. If you can't see him, use your",
												"keyboard's arrow keys to rotate the view.", true);
		}
		if(stage == 3){
			player.getActionSender().flashSideBarIcon(QuestConstants.OPTION_TAB[0]);
			player.getDialogue().sendStartInfo("",
												"Player controls",
												"Please click on the flashing spanner icon found at the bottom",
												"right of your screen. This will display your player controls.",
												"", true);
		}
		if(stage == 4){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.RUNESCAPE_GUIDE).getIndex());
			player.getDialogue().sendStartInfo("Player controls",
					"On the side panel, you can now see a variety of options from",
					"changing the brightness of the screen and the volume of",
					"music, to selecting whether your player should accept help",
					"from other players. Don't worry about these too much for now.", true);
		}
		if(stage == 5){
			player.getActionSender().createObjectHints(3098, 3107, 130, 3);
			player.getDialogue().sendStartInfo("Interacting with scenery",
					"You can interact with many items of the scenery by simply clicking",
					"on them. Right clicking will also give more options. Feel free to",
					"try it with the things in this room, then click on the door",
					"indicated with the yellow arrow to go through.", true);
		}
		if(stage == 6){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.SURVIVAL_EXPERT).getIndex());
			player.getDialogue().sendStartInfo("Moving around",
											"Follow the path to find the next instructor. Clicking on the",
											"ground will walk you to that point. Talk to the Survival Expert by",
											"the pond to continue the tutorial. Remember you can rotate",
											"the view by pressing the arrow keys.", true);
		}
		if(stage == 7){
			player.getActionSender().flashSideBarIcon(QuestConstants.INVENTORY_TAB[0]);
			player.getDialogue().sendStartInfo("Viewing the items that you were given.",
											"Click on the flashing backpack icon to the right hand side of",
											"the main window to view your inventory. Your inventory is a list",
											"of everything you have in your backpack.",
											"", true);
		}
		if(stage == 8){
			player.getActionSender().createObjectHints(3100, 3095, 170, 3);
			player.getDialogue().sendStartInfo("Cut down a tree",
											"You can click on the backpack icon at any time to view the",
											"items that you currently have in your inventory. You will see",
											"that you now have an axe in your inventory. Use this to get",
											"some logs by clicking on one of the trees in the area.", true);
		}
		if(stage == 9){
			player.getDialogue().sendStartInfo("Making a fire",
											"Well done! You managed to cut some logs from the tree! Next,",
											"use the tinderbox in your inventory to light the logs.",
											"First click on the tinderbox to 'use' it.",
											"Then click on the logs in your inventory to light them.", true);
		}
		if(stage == 10){
			player.getActionSender().flashSideBarIcon(QuestConstants.STATS_TAB[0]);
			player.getDialogue().sendStartInfo("",
											"You gained some experience.",
											"Click on the flashing bar graph icon near the inventory button",
											"to see your skill stats.",
											"", true);
		}
		if(stage == 11){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.SURVIVAL_EXPERT).getIndex());
			player.getDialogue().sendStartInfo("Your skill stats.",
											"Here you will see how good your skills are. As you move your",
											"mouse over any of the icons in this panel, the small yellow",
											"popup box will show you the exact amount of experience you",
											"have and how much you need. Speak to Brynna to continue. ", true);
		}
		if(stage == 12){
			player.getActionSender().createObjectHints(3101, 3092, 70, 2);
			player.getDialogue().sendStartInfo("Catch some Shrimp.",
											"Click on the sparkling fishing spot, indicated by the flashing",
											"arrow. Remember, you can check your inventory by clicking the",
											"backpack icon.",
											"", true);
		}
		if(stage == 13){
			player.getDialogue().sendStartInfo("Cooking your shrimp.",
											"Now you have caught some shrimp, let's cook it. First light a",
											"fire: chop down a tree and then use the tinderbox on the logs.",
											"If you've lost your axe or tinderbox Brynna will give you",
											"another.", true);
		}
		if(stage == 14){
			player.getDialogue().sendStartInfo("Burning your shrimp.",
											"You have just burnt your first shrimp. This is normal. As you",
											"get more experience in Cooking, you will burn stuff less often.",
											"Let's try cooking without burning it this time. First catch some",
											"more shrimp, then use them on a fire.", true);
		}
		if(stage == 15){
			player.getActionSender().createObjectHints(3089, 3091, 120, 4);
			player.getDialogue().sendStartInfo("Well done, you've just cooked your first RuneScape meal.",
											"If you'd like a recap on anything you've learnt so far, speak to",
											"the Survival Expert. You can now move on to the next",
											"instructor. Click on the gate shown and follow the path.",
											"Remember, you can move the camera with the arrow keys.", true);
		}
		if(stage == 16){
			player.getActionSender().createObjectHints(3079, 3084, 130, 3);
			player.getDialogue().sendStartInfo("Find your next instructor.",
											"Follow the path until you get to the door with the yellow arrow",
											"above it. Click on the door to open it. Notice the mini-map in",
											"the top right; this shows a top down view of the area around",
											"you. This can also be used for navigation.", true);
		}
		if(stage == 17){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.MASTER_CHEF).getIndex());
			player.getDialogue().sendStartInfo("Find your next instructor.",
											"Talk to the chef indicated. He will teach you the more advanced",
											"aspects of Cooking such as combining ingredients. He will also",
											"teach you about your music player menu as well.",
											"", true);
		}
		if(stage == 18){
			player.getDialogue().sendStartInfo("Making dough.",
											"This is the base for many of the meals. To make dough we must",
											"mix flour and water. First, right click the bucket of water and",
											"select use, then left click on the pot of flour.",
											"", true);
		}
		if(stage == 19){
			player.getActionSender().createObjectHints(3076, 3081, 100, 2);
			player.getDialogue().sendStartInfo("Cooking dough.",
											"Now you have made dough, you can cook it. To cook the dough,",
											"use it with the range shown by the arrow. If you lose your",
											"dough, talk to Lev - he will give you more ingredients.",
											"", true);
		}
		if(stage == 20){
			player.getActionSender().flashSideBarIcon(QuestConstants.MUSIC_TAB[0]);
			player.getDialogue().sendStartInfo("Cooking dough.",
											"Well done! Your first loaf of bread. As you gain experience in",
											"Cooking, you will be able to make other things like pies, cakes",
											"and even kebabs. Now you've got the hang of cooking, let's",
											"move on. Click on the flashing icon in the bottom right.", true);
		}
		if(stage == 21){
			player.getActionSender().createObjectHints(3073, 3090, 130, 3);
			player.getDialogue().sendStartInfo("The music player.",
											"From this interface you can control the music that is played.",
											"As you explore the world, more of the tunes will become",
											"unlocked. Once you've examined this menu use the next door",
											"to continue. If you need a recap talk to the Master Chef.", true);
		}
		if(stage == 22){
			player.getActionSender().flashSideBarIcon(QuestConstants.EMOTE_TAB[0]);
			player.getDialogue().sendStartInfo("It's only a short distance to the next guide",
											"",
											"Why not try running there. Start by opening the player",
											"controls, that's the flashing icon of a running man.",
											"", true);
		}
		if(stage == 23){
			player.getDialogue().sendStartInfo("Running.",
											"In this menu you will see many options from waving to walking.",
											"At the top of the panel there are two buttons. One is walk the",
											"other one is run. Click the run button.",
											"", true);
		}
		if(stage == 24){
			player.getActionSender().createObjectHints(3086, 3126, 130, 5);
			player.getDialogue().sendStartInfo("Run to the next guide.",
											"Now that you have the run turned on follow the path, until you",
											"come to the end. You may notice that your energy left goes",
											"down. If this reaches zero you'll stop running. Click on the door",
											"to pass through it.", true);
		}
		if(stage == 25){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.QUEST_GUIDE).getIndex());
			player.getDialogue().sendStartInfo("",
											"Talk with the Quest Guide.",
											"",
											"He will tell you all about quests.",
											"", true);
		}
		if(stage == 26){
			player.getActionSender().flashSideBarIcon(QuestConstants.QUEST_TAB[0]);
			player.getDialogue().sendStartInfo("",
											"Open the Quest Journal.",
											"",
											"Click on the flashing icon next to your inventory.",
											"", true);
		}
		if(stage == 27){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.QUEST_GUIDE).getIndex());
			player.getDialogue().sendStartInfo("Your Quest Journal.",
											"",
											"This is your Quest Journal, a list of all the quests in the game.",
											"Talk to the Quest Guide again for an explanation.",
											"", true);
		}
		if(stage == 28){
			player.getActionSender().createObjectHints(3088, 3119, 100, 2);
			player.getDialogue().sendStartInfo("",
											"Moving on.",
											"It's time to enter some caves. Click on the ladder to go down to",
											"the next area.",
											"", true);
		}
		if(stage == 29){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.MINING_INSTRUCTOR).getIndex());
			player.getDialogue().sendStartInfo("Mining and Smithing.",
											"Next let's get you a weapon, or more to the point, you can",
											"make your first weapon yourself. Don't panic, the Mining",
											"Instructor will help you. Talk to him and he'll tell you all about it.",
											"", true);
		}
		if(stage == 30){
			player.getActionSender().createObjectHints(3076, 9504, 70, 2);
			player.getDialogue().sendStartInfo("Prospecting.",
											"To prospect a mineable rock, just right click it and select the",
											"'prospect rock' option. This will tell you the type of ore you can",
											"mine from it. Try it now on one of the rocks indicated.",
											"", true);
		}
		if(stage == 31){
			player.getActionSender().createObjectHints(3086, 9501, 70, 2);
			player.getDialogue().sendStartInfo("It's tin.",
											"",
											"So now you know there's tin in the grey rocks, try prospecting",
											"the brown ones next.",
											"", true);
		}
		if(stage == 32){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.MINING_INSTRUCTOR).getIndex());
			player.getDialogue().sendStartInfo("It's copper.",
											"Talk to the Mining Instructor to find out about these types of",
											"ore and how you can mine them. He'll even give you the",
											"required tools.",
											"", true);
		}
		if(stage == 33){
			player.getActionSender().createObjectHints(3076, 9504, 70, 2);
			player.getDialogue().sendStartInfo("Mining.",
											"It's quite simple really. All you need to do is right click on the",
											"rock and select 'mine'. You can only mine when you have a",
											"pickaxe. So give it a try: first mine one tin ore.",
											"", true);
		}
		if(stage == 34){
			player.getActionSender().createObjectHints(3086, 9501, 70, 2);
			player.getDialogue().sendStartInfo("Mining.",
											"Now you have some tin ore you just need some copper ore,",
											"then you'll have all you need to create a bronze bar. As you",
											"did before right click on the copper rock and select 'mine'.",
											"", true);
		}
		if(stage == 35){
			player.getActionSender().createObjectHints(3079, 9496, 120, 2);
			player.getDialogue().sendStartInfo("Smelting.",
											"You should now have both some copper and tin ore. So let's",
											"smelt them to make a bronze bar. To do this, right click on",
											"either tin or copper ore and select use then left click on the",
											"furnace. Try it now.", true);
		}
		if(stage == 36){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.MINING_INSTRUCTOR).getIndex());
			player.getDialogue().sendStartInfo("You've made a bronze bar!",
											"",
											"Speak to the Mining Instructor and he'll show you how to make",
											"it into a weapon.",
											"", true);
		}
		if(stage == 37){
			player.getActionSender().createObjectHints(3083, 9499, 70, 2);
			player.getDialogue().sendStartInfo("Smithing a dagger.",
											"To smith you'll need a hammer - like the one you were given by",
											"Dezzick - access to an anvil like the one with the arrow over it",
											"and enough metal bars to make what you are trying to smith.",
											"To start the process, use the bar on one of the anvils.", true);
		}
		if(stage == 38){
			player.getDialogue().sendStartInfo("Smithing a dagger.",
											"Now you have the Smithing menu open, you will see a list of all",
											"the things you can make. Only the dagger can be made at your",
											"skill level; this is shown by the white text under it. You'll need",
											"to select the dagger to continue.", true);
		}
		if(stage == 39){
			player.getActionSender().createObjectHints(3094, 9502, 120, 4);
			player.getDialogue().sendStartInfo("You've finished in this area.",
											"So let's move on. Go through the gates shown by the arrow.",
											"Remember, you may need to move the camera to see your",
											"surroundings. Speak to the guide for a recap at any time.",
											"", true);
		}
		if(stage == 40){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.COMBAT_INSTRUCTOR).getIndex());
			player.getDialogue().sendStartInfo("Combat.",
											"",
											"In this area you will find out about combat with swords and",
											"bows. Speak to the guide and he will tell you all about it.",
											"", true);
		}
		if(stage == 41){
			player.getActionSender().flashSideBarIcon(QuestConstants.EQUIPMENT_TAB[0]);
			player.getDialogue().sendStartInfo("Wielding weapons.",
											"",
											"You now have access to a new interface. Click on the flashing",
											"icon of a man, the one to the right of your backpack icon.",
											"", true);
		}
		if(stage == 42){
			player.getDialogue().sendStartInfo("This is your worn inventory.",
											"From here you can see what items you have equipped. Let's",
											"get one of those slots filled, go back to your inventory",
											"and right click your dagger, select wield from the menu.",
											"", true);
		}
		if(stage == 43){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.COMBAT_INSTRUCTOR).getIndex());
			player.getDialogue().sendStartInfo("You're now holding your dagger.",
											"Clothes, armour, weapons and many other items are equipped",
											"like this. You can unequip items by clicking on the item in the",
											"worn inventory. Speak to the Combat Instructor to continue.",
											"", true);
		}
		if(stage == 44){
			player.getDialogue().sendStartInfo("Unequipping items.",
											"In your worn inventory panel, right click on the dagger and",
											"select remove option from the drop down list. After you've",
											"unequipped the dagger, wield the sword and shield. As you",
											"pass the mouse over an item you will see it's name.", true);
		}
		if(stage == 45){
			player.getActionSender().flashSideBarIcon(QuestConstants.ATTACK_TAB[0]);
			player.getDialogue().sendStartInfo("Combat Interface.",
											"",
											"Click on the flashing crossed swords icon to see the combat",
											"interface.",
											"", true);
		}
		if(stage == 46){
			player.getActionSender().createObjectHints(3110, 9518, 120, 4);
			player.getDialogue().sendStartInfo("This is your combat interface.",
											"From this interface you can select the type of attack your",
											"character will use. Different monsters have different",
											"weaknesses. Now you have the tools needed for battle why",
											"not slay some rats. Click on the gate indicated to continue.", true);
		}
		if(stage == 47){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.GIANT_RAT).getIndex());
			player.getDialogue().sendStartInfo("Attacking.",
											"",
											"To attack the rat, right click it and select the attack option. You",
											"will then walk over to it and start hitting it.",
											"", true);
		}
		if(stage == 48){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.COMBAT_INSTRUCTOR).getIndex());
			player.getDialogue().sendStartInfo("Well done, you've made your first kill!",
											"",
											"Pass through the gate and talk to the Combat Instructor; he",
											"will give you your next task.",
											"", true);
		}
		if(stage == 49){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.GIANT_RAT).getIndex());
			player.getDialogue().sendStartInfo("Rat ranging.",
											"Now you have a bow and some arrows. Before you can use",
											"them you'll need to equip them. Once equipped with the",
											"ranging gear, try killing another rat. Remember: to attack, right",
											"click on the monster and select attack.", true);
		}
		if(stage == 50){
			player.getActionSender().createObjectHints(3111, 9526, 100, 2);
			player.getDialogue().sendStartInfo("Moving on.",
											"You have completed the tasks here. To move on, click on the",
											"ladder shown. If you need to go over any of what you learnt",
											"here, just talk to the Combat Instructor and he'll tell you what",
											"he can.", true);
		}
		if(stage == 51){
			player.getActionSender().createObjectHints(3122, 3124, 100, 2);
			player.getDialogue().sendStartInfo("Banking.",
											"Follow the path and you will come to the front of the building.",
											"This is the Bank of Runescape, where you can store all your",
											"most valued items. To open your bank box just right click on an",
											"open booth indicated and select 'use'.", true);
		}
		if(stage == 52){
			player.getActionSender().createObjectHints(3125, 3124, 130, 3);
			player.getDialogue().sendStartInfo("This is your bank box.",
											"You can store stuff here for safekeeping. If you die, anything",
											"in your bank will be saved. To deposit something, right click it",
											"and select 'store'. Once you've had a good look, close the",
											"window and move on through the door indicated.", true);
		}
		if(stage == 53){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.FINANCIAL_ADVISOR).getIndex());
			player.getDialogue().sendStartInfo("Financial advice.",
											"",
											"The guide here will tell you all about making cash. Just click on",
											"him to hear what he's got to say.",
											"", true);
		}
		if(stage == 54){
			player.getActionSender().createObjectHints(3130, 3124, 130, 3);
			player.getDialogue().sendStartInfo("",
											"",
											"Continue through the next door.",
											"",
											"", true);
		}
		if(stage == 55){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.BROTHER_BRACE).getIndex());
			player.getDialogue().sendStartInfo("Prayer.",
											"Follow the path to the chapel and enter it.",
											"Once inside talk to the monk. He'll tell you all about the Prayer",
											"skill.",
											"", true);
		}
		if(stage == 56){
			player.getActionSender().flashSideBarIcon(QuestConstants.PRAYER_TAB[0]);
			player.getDialogue().sendStartInfo("Your Prayer menu.",
											"",
											"Click on the flashing icon to open the Prayer menu.",
											"",
											"", true);
		}
		if(stage == 57){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.BROTHER_BRACE).getIndex());
			player.getDialogue().sendStartInfo("",
											"Your Prayer menu.",
											"",
											"Talk with Brother Brace and he'll tell you about prayers.",
											"", true);
		}
		if(stage == 58){
			player.getActionSender().flashSideBarIcon(QuestConstants.FRIEND_TAB[0]);
			player.getDialogue().sendStartInfo("",
											"Friends list.",
											"You should now see another new icon. Click on the flashing",
											"smiling face to open your friends list.",
											"", true);
		}
		if(stage == 59){
			player.getActionSender().flashSideBarIcon(QuestConstants.IGNORE_TAB[0]);
			player.getDialogue().sendStartInfo("This is your friends list.",
											"",
											"This will be explained by Brother Brace shortly, but first click",
											"on the other flashing face to the right of your screen.",
											"", true);
		}
		if(stage == 60){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.BROTHER_BRACE).getIndex());
			player.getDialogue().sendStartInfo("This is your ignore list.",
											"The two lists - friends and ignore - can be very helpful for",
											"keeping track of when your friends are online or for blocking",
											"messages from people you simply don't like. Speak with",
											"Brother Brace and he will tell you more.", true);
		}
		if(stage == 61){
			player.getActionSender().createObjectHints(3122, 3102, 130, 6);
			player.getDialogue().sendStartInfo("",
											"Your final instructor!",
											"You're almost finished on tutorial island. Pass through the",
											"door to find the path leading to your final instructor.",
											"", true);
		}
		if(stage == 62){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.MAGIC_INSTRUCTOR).getIndex());
			player.getDialogue().sendStartInfo("Your final instructor!",
											"Just follow the path to the Wizard's house, where you will be",
											"shown how to cast spells. Just talk with the mage indicated to",
											"find out more.",
											"", true);
		}
		if(stage == 63){
			player.getActionSender().flashSideBarIcon(QuestConstants.MAGIC_TAB[0]);
			player.getDialogue().sendStartInfo("Open up your final menu.",
											"",
											"Open up the Magic menu by clicking on the flashing icon next",
											"to the Prayer button you just learned about.",
											"", true);
		}
		if(stage == 64){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.MAGIC_INSTRUCTOR).getIndex());
			player.getDialogue().sendStartInfo("",
											"This is where all of your magic spells are.",
											"Talk to Terrova to learn more.",
											"",
											"", true);
		}
		if(stage == 65){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.CHICKEN).getIndex());
			player.getDialogue().sendStartInfo("Cast Wind Strike at a chicken.",
											"Now you have runes you should see the Wind Strike icon at the",
											"top left corner of the Magic interface - first in from the",
											"left. Walk over to the caged chickens, click the Wind Strike icon",
											"and then select one of the chickens to cast it on.", true);
		}
		if(stage == 66){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.CHICKEN).getIndex());
			player.getDialogue().sendStartInfo("Cast Wind Strike on a chicken.",
											"That's it, you cast a spell! Sadly it didn't have any effect this time, but",
											"the more you practice, the better you'll get. Repeat this process until",
											"you successfully cast the spell. Click the Wind Strike icon again and",
											"then select one of the chickens.", true);
		}
		if(stage == 67){
			player.getActionSender().createPlayerHints(1, Npc.getNpcById(QuestConstants.MAGIC_INSTRUCTOR).getIndex());
			player.getDialogue().sendStartInfo("You have almost completed the tutorial!",
											"",
											"All you need to do now is move on to the mainland. Just speak",
											"with Terrova and he'll teleport you to Lumbridge Castle.",
											"", true);
		}
		if(stage == 68){
			player.getActionSender().sendChatboxOverlay(-1);
			player.getActionSender().removeInterfaces();
			player.getActionSender().sendWalkableInterface(-1);
			player.getDialogue().sendStatement("Welcome to Lumbridge! To get more help, simply click on the", "Lumbridge Guide and he will give you some tips.", "He can be found by looking for the question mark icon on", "your minimap. If you find that you are lost any time, look for", "other players, they might help you to make your way back.");
			player.addStarterItems();
			player.questStage[0] = 1;
			if(player.testReward){
				Item itm = new Item(7956, 1);
				player.getInventory().addItem(itm);
				Misc.addWatchedItem(itm);
				player.getActionSender().sendMessage("You received a reward for participating the test week.");
			}
		}
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.RUNESCAPE_GUIDE){
			if(stage == 2){
				if(!Constants.ALLOW_TUTORIAL_SKIP && chatId < 3)
					chatId = 3;
				if(chatId == 1){
					player.getDialogue().sendOptionWTitle("Would you like to skip tutorial?",
													"Yes.",
													"No.");
					return true;
				}
				if(chatId == 2){
					if(optionId == 1){
						player.getActionSender().createPlayerHints(1, -1);
						player.teleport(new Position(3233, 3229, 0));
						player.getDialogue().resetDialogue();
						player.getDialogue().endDialogue();
						player.skipTutorial();
						return true;
					}else{
						chatId = 3;
					}
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Greetings! I see you are a new arrival to this land. My",
													"job is to welcome all new visitors. So welcome!",Dialogues.HAPPY);
					player.getDialogue().setNextChatId(4);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("You have already learned the first thing needed to",
													"succeed in this world talking to other people!",Dialogues.HAPPY);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("You will find many inhabitants of this world have useful",
													"things to say to you. By clicking on them with your",
													"mouse you can talk to them.",Dialogues.HAPPY);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("I would also suggest reading through some of the",
													"supporting information on the website. There you can",
													"find the Knowledge Base, which contains all the",
													"additional information you're ever likely to need. It also",Dialogues.HAPPY);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("contains maps and helpful tips to help you on your",
													"journey.",Dialogues.HAPPY);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("You will notice a flashing icon of a spanner, please click",
													"on this to continue the tutorial.",Dialogues.HAPPY);
					player.getActionSender().createPlayerHints(1, -1);
					player.setNextTutorialStage();
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 2
			if(stage == 4){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("I'm glad you're making progress!",Dialogues.HAPPY);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("To continue the tutorial go through that door over",
													"there and speak to your first instructor!",Dialogues.HAPPY);
					player.setNextTutorialStage();
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 945
		if(npcId == QuestConstants.SURVIVAL_EXPERT){
			if(stage == 6){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hello there, newcomer. My name is Brynna. My job is",
												"to teach you a few survival tips and tricks. First off",
												"we're going to start with the most basic survival skill of",
												"all: making a fire.",Dialogues.HAPPY);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().send2Items2Lines("The Survival Guide gives you a @dbl@tinderbox @bla@and a @dbl@bronze", "@dbl@axe@bla@!", new Item(590, 1), new Item(1351, 1));
					player.setClickId(0);
					player.getInventory().addItemOrDrop(new Item(QuestConstants.BRONZE_AXE, 1));
					player.getInventory().addItemOrDrop(new Item(QuestConstants.TINDER_BOX, 1));
					player.setNextTutorialStage();
					player.getActionSender().createPlayerHints(1, -1);
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage >= 7){
				if(chatId == 1 && (!player.hasItem(QuestConstants.BRONZE_AXE) || !player.hasItem(QuestConstants.TINDER_BOX))){
					player.getDialogue().send2Items2Lines("The Survival Guide gives you a @dbl@tinderbox @bla@and a @dbl@bronze", "@dbl@axe@bla@!", new Item(QuestConstants.TINDER_BOX, 1), new Item(QuestConstants.BRONZE_AXE, 1));
					player.setClickId(0);
					player.getInventory().addItemOrDrop(new Item(QuestConstants.BRONZE_AXE, 1));
					player.getInventory().addItemOrDrop(new Item(QuestConstants.TINDER_BOX, 1));
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage == 11){
				if(chatId == 1){
					player.getActionSender().sendConfig(406, 3);
					player.getDialogue().sendNpcChat("Well done! Next we need to get some food in our",
													"bellies. We'll need something to cook. There are shrimp",
													"in the pond there, so let's catch and cook some.",Dialogues.HAPPY);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendItem1Line("The Survival Guide gives you a @dbl@net@bla@!", new Item(QuestConstants.SMALL_FISHING_NET, 1));
					player.setClickId(0);
					player.getInventory().addItemOrDrop(new Item(QuestConstants.SMALL_FISHING_NET, 1));
					player.setNextTutorialStage();
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage >= 12){
				if(chatId == 1 && (!player.hasItem(QuestConstants.SMALL_FISHING_NET))){
					player.getDialogue().sendItem1Line("The Survival Guide gives you a @dbl@net@bla@!", new Item(QuestConstants.SMALL_FISHING_NET, 1));
					player.setClickId(0);
					player.getInventory().addItemOrDrop(new Item(QuestConstants.SMALL_FISHING_NET, 1));
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 943
		if(npcId == QuestConstants.MASTER_CHEF){
			if(stage == 17){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Ah! Welcome, newcomer. I am the Master Chef, Lev. It",
													"is here I will teach you how to cook food truly fit for a",
													"king.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("I already know how to cook. Brynna taught me just",
														"now.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Hahahahahaha! You call THAT cooking? Some shrimp",
													"on an open log fire? Oh, no, no, no. I am going to",
													"teach you the fine art of cooking bread.",Dialogues.LONGER_LAUGHING_2);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("And no fine meal is complete without good music, so",
													"we'll cover that while you're here too.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().send2Items2Lines("The Cooking Guide gives you a @dbl@bucket of water @bla@and a", "@dbl@pot of flour@bla@!", new Item(QuestConstants.BUCKET_OF_WATER, 1), new Item(QuestConstants.POT_OF_FLOUR, 1));
					player.setClickId(0);
					player.getActionSender().createPlayerHints(1, -1);
					player.getInventory().addItemOrDrop(new Item(QuestConstants.POT_OF_FLOUR, 1));
					player.getInventory().addItemOrDrop(new Item(QuestConstants.BUCKET_OF_WATER, 1));
					player.setNextTutorialStage();
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage >= 18 && stage < 20){
				if(chatId == 1 && !player.hasItem(QuestConstants.BREAD_DOUGH) && (!player.hasItem(QuestConstants.POT_OF_FLOUR) || !player.hasItem(QuestConstants.BUCKET_OF_WATER))){
					player.getDialogue().send2Items2Lines("The Cooking Guide gives you a @dbl@bucket of water @bla@and a", "@dbl@pot of flour@bla@!", new Item(QuestConstants.BUCKET_OF_WATER, 1), new Item(QuestConstants.POT_OF_FLOUR, 1));
					player.setClickId(0);
					player.getInventory().addItemOrDrop(new Item(QuestConstants.POT_OF_FLOUR, 1));
					player.getInventory().addItemOrDrop(new Item(QuestConstants.BUCKET_OF_WATER, 1));
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 942
		if(npcId == QuestConstants.QUEST_GUIDE){
			if(stage == 25){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Ah. Welcome, adventurer. I'm here to tell you all about",
													"quests. Let's start by opening the quest side panel.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.setNextTutorialStage();
					player.getActionSender().createPlayerHints(1, -1);
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					player.getActionSender().removeInterfaces();
					return true;
				}
			}
			if(stage == 27){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Now you have the journal open I'll tell you a bit about",
													"it. At the moment all quests are shown in red, which",
													"means you have not started them yet.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("When you start a quest it will change colour to yellow,",
													"and to green when you've finished. This is so you can",
													"easily see what's complete, what's started, and what's left",
													"to begin.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("The start of quests are easy to find. Look out for the",
													"star icons on the minimap, just like the one you should",
													"see marking my house.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("The quests themselves can vary greatly from collecting",
													"beads to hunting down dragons. Generally quests are",
													"started by talking to a non-player character like me,",
													"and will involve a series of tasks.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("There's not a lot more I can tell you about questing.",
													"You have to experience the thrill of it yourself to fully",
													"understand. You may find some adventure in the caves",
													"under my house.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.setNextTutorialStage();
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					player.getActionSender().removeInterfaces();
					return true;
				}
			}
		}//end of npc 949
		if(npcId == QuestConstants.MINING_INSTRUCTOR){
			if(stage == 29){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hi there. You must be new around here. So what do I",
													"call you? Newcomer' seems so impersonal, and if we're",
													"going to be working together, I'd rather call you by",
													"name.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("You can call me "+player.getUsername()+".", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Ok then, "+player.getUsername()+". My name is Dezzick and I'm a",
													"miner by trade. Let's prospect some of those rocks.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.setNextTutorialStage();
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					player.getActionSender().removeInterfaces();
					return true;
				}
			}
			if(stage == 32){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I prospected both types of rock! One set contains tin",
														"and the other has copper ore inside.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Absolutely right, "+player.getUsername()+". These two ore types can",
													"be smelted together to make bronze.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("So now you know what ore is in the rocks over there,",
													"why don't you have a go at mining some tin and",
													"copper? Here, you'll need this to start with.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendItem1Line("Dezzick gives you a @dbl@bronze pickaxe@bla@!", new Item(QuestConstants.BRONZE_PICKAXE, 1));
					player.setClickId(0);
					player.getInventory().addItemOrDrop(new Item(QuestConstants.BRONZE_PICKAXE, 1));
					player.setNextTutorialStage();
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage >= 33){
				if(chatId == 1 && (!player.hasItem(QuestConstants.BRONZE_PICKAXE))){
					player.getDialogue().sendItem1Line("Dezzick gives you a @dbl@bronze pickaxe@bla@!", new Item(QuestConstants.BRONZE_PICKAXE, 1));
					player.setClickId(0);
					player.getInventory().addItemOrDrop(new Item(QuestConstants.BRONZE_PICKAXE, 1));
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage == 36){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("How do I make a weapon out of this?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Okay, I'll show you how to make a dagger out of it.",
													"You'll be needing this...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendItem1Line("Dezzick gives you a @dbl@hammer@bla@!", new Item(QuestConstants.HAMMER, 1));
					player.setClickId(0);
					player.getInventory().addItemOrDrop(new Item(QuestConstants.HAMMER, 1));
					player.setNextTutorialStage();
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage >= 37){
				if(chatId == 1 && (!player.hasItem(QuestConstants.HAMMER))){
					player.getDialogue().sendItem1Line("Dezzick gives you a @dbl@hammer@bla@!", new Item(QuestConstants.HAMMER, 1));
					player.setClickId(0);
					player.getInventory().addItemOrDrop(new Item(QuestConstants.HAMMER, 1));
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 948
		if(npcId == QuestConstants.COMBAT_INSTRUCTOR){
			if(stage == 40){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hi! My name's "+player.getUsername()+".", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Do I look like I care? To me you're just another",
													"newcomer who thinks they're ready to fight.",Dialogues.ANNOYED);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("I am Vannaka, the greatest swordsman alive.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Let's get started by teaching you to wield a weapon.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getActionSender().createPlayerHints(1, -1);
					player.setNextTutorialStage();
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					player.getActionSender().removeInterfaces();
					return true;
				}
			}
			if(stage == 43){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Very good, but that little butter knife isn't going to",
													"protect you much. Here, take these.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().send2Items2Lines("The Combat Instructor gives you a @dbl@bronze sword @bla@and a", "@dbl@wooden shield@bla@!", new Item(QuestConstants.BRONZE_SWORD, 1), new Item(QuestConstants.WOODEN_SHIELD, 1));
					player.setClickId(0);
					player.getActionSender().createPlayerHints(1, -1);
					player.getInventory().addItemOrDrop(new Item(QuestConstants.WOODEN_SHIELD, 1));
					player.getInventory().addItemOrDrop(new Item(QuestConstants.BRONZE_SWORD, 1));
					player.setNextTutorialStage();
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage >= 44){
				if(chatId == 1 && (!player.hasItem(QuestConstants.WOODEN_SHIELD) || !player.hasItem(QuestConstants.BRONZE_SWORD))){
					player.getDialogue().send2Items2Lines("The Combat Instructor gives you a @dbl@bronze sword @bla@and a", "@dbl@wooden shield@bla@!", new Item(QuestConstants.BRONZE_SWORD, 1), new Item(QuestConstants.WOODEN_SHIELD, 1));
					player.setClickId(0);
					player.getActionSender().createPlayerHints(1, -1);
					player.getInventory().addItemOrDrop(new Item(QuestConstants.WOODEN_SHIELD, 1));
					player.getInventory().addItemOrDrop(new Item(QuestConstants.BRONZE_SWORD, 1));
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage == 48){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I did it! I killed a giant rat!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("I saw, "+player.getUsername()+". You seem better at this than I",
													"thought. Now that you have grasped basic swordplay,",
													"let's move on.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Let's try some ranged attacking, with this you can kill",
													"foes from a distance. Also, foes unable to reach you are",
													"as good as dead. You'll be able to attack the rats",
													"without entering the pit.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().send2Items2Lines("The Combat Instructor gives you some @dbl@bronze arrows @bla@and", "a @dbl@shortbow@bla@!", new Item(QuestConstants.BRONZE_ARROW, 50), new Item(QuestConstants.SHORTBOW, 1));
					player.setClickId(0);
					player.getInventory().addItemOrDrop(new Item(QuestConstants.SHORTBOW, 1));
					player.getInventory().addItemOrDrop(new Item(QuestConstants.BRONZE_ARROW, 50));
					player.setNextTutorialStage();
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage >= 49){
				if(chatId == 1 && (!player.hasItem(QuestConstants.SHORTBOW) && !player.hasItem(QuestConstants.BRONZE_ARROW))){
					player.getDialogue().send2Items2Lines("The Combat Instructor gives you some @dbl@bronze arrows @bla@and", "a @dbl@shortbow@bla@!", new Item(QuestConstants.BRONZE_ARROW, 50), new Item(QuestConstants.SHORTBOW, 1));
					player.setClickId(0);
					player.getInventory().addItemOrDrop(new Item(QuestConstants.SHORTBOW, 1));
					player.getInventory().addItemOrDrop(new Item(QuestConstants.BRONZE_ARROW, 50));
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 1 && (player.hasItem(QuestConstants.SHORTBOW) && !player.hasItem(QuestConstants.BRONZE_ARROW))){
					player.getDialogue().sendItem1Line("The Combat Instructor gives you some @dbl@bronze arrows@bla@!", new Item(QuestConstants.BRONZE_ARROW, 50));
					player.setClickId(0);
					player.getInventory().addItemOrDrop(new Item(QuestConstants.BRONZE_ARROW, 50));
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 1 && (!player.hasItem(QuestConstants.SHORTBOW) && player.hasItem(QuestConstants.BRONZE_ARROW))){
					player.getDialogue().sendItem1Line("The Combat Instructor gives you a @dbl@shortbow@bla@!", new Item(QuestConstants.SHORTBOW, 1));
					player.setClickId(0);
					player.getInventory().addItemOrDrop(new Item(QuestConstants.SHORTBOW, 1));
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 944
		if(npcId == QuestConstants.FINANCIAL_ADVISOR){
			if(stage == 53){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello. Who are you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("I'm the Financial Advisor. I'm here to tell people how to",
													"make money.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Okay. How can I make money then?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("How you can make money? Quite.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Well, there are three basic ways of making money here:",
													"combat, quests and trading. I will talk you through each",
													"of them very quickly.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Let's start with combat as it is probably still fresh in",
													"your mind. Many enemies, both human and monster,",
													"will drop items when they die.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Now, the next way to earn money quickly is by quests.",
													"Many people on RuneScape have things they need",
													"doing, which they will reward you for.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("By getting a high level in skills such as Cooking, Mining,",
													"Smithing or Fishing, you can create or catch your own",
													"items and sell them for pure profit.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Well, that about covers it. Come back if you'd like to go",
													"over this again.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.setNextTutorialStage();
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					player.getActionSender().removeInterfaces();
					return true;
				}
			}
		}//end of npc 947
		if(npcId == QuestConstants.BROTHER_BRACE){
			if(stage == 55){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Good day, brother, my name's "+player.getUsername()+".", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Hello, "+player.getUsername()+". I'm Brother Brace. I'm here to tell",
													"you all about Prayer.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getActionSender().createPlayerHints(1, -1);
					player.setNextTutorialStage();
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					player.getActionSender().removeInterfaces();
					return true;
				}
			}
			if(stage == 57){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("This is your Prayer list. Prayers can help a lot in",
													"combat. Click on the prayer you wish to use to activate",
													"it, and click it again to deactivate it.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Active prayers will drain your Prayer Points, which",
													"you can recharge by finding an altar or other holy spot",
													"and praying there.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("As you noticed, most enemies will drop bones when",
													"defeated. Burying bones, by clicking them in your",
													"inventory, will gain you Prayer experience.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("I'm also the community officer 'round here, so it's my",
													"job to tell you about your friends and ignore list.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getActionSender().createPlayerHints(1, -1);
					player.setNextTutorialStage();
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					player.getActionSender().removeInterfaces();
					return true;
				}
			}
			if(stage == 60){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Good. Now you have both menus open I'll tell you a",
													"little about each. You can add people to either list by",
													"clicking the add button then typing their name into the",
													"box that appears.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("You remove people from the lists in the same way. If",
													"you add someone to your ignore list they will not be",
													"able to talk to you or send any form of message to",
													"you.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Your friends list shows the online status of your",
													"friends. Friends in red are offline, friends in green are",
													"online and on the same server and friends in yellow",
													"are online, but on a different server.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("Are there rules on in-game behaviour?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Yes, you should read the rules of conduct on the",
													"website to make sure you do nothing to get yourself",
													"banned.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("But in general, always try to be courteous to other",
													"players - remember the people in the game are real",
													"people with real feelings.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("If you go 'round being abusive or causing trouble your",
													"character could end up being the one in trouble.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendPlayerChat("Okay, thanks. I'll bear that in mind.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.setNextTutorialStage();
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					player.getActionSender().removeInterfaces();
					return true;
				}
			}
		}//end of npc 954
		if(npcId == QuestConstants.MAGIC_INSTRUCTOR){
			if(stage == 62){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Good day, newcomer. My name is Terrova. I'm here",
													"to tell you about Magic. Let's start by opening your",
													"spell list.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getActionSender().createPlayerHints(1, -1);
					player.setNextTutorialStage();
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					player.getActionSender().removeInterfaces();
					return true;
				}
			}
			if(stage == 64){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Good. This is a list of your spells. Currently you can",
													"only cast one offensive spell called Wind Strike. Let's",
													"try it out on one of those chickens.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().send2Items2Lines("Terrova gives you five @dbl@air runes @bla@and five @dbl@mind runes@bla@!", "", new Item(QuestConstants.AIR_RUNE, 5), new Item(QuestConstants.MIND_RUNE, 5));
					player.setClickId(0);
					player.getInventory().addItemOrDrop(new Item(QuestConstants.AIR_RUNE, 5));
					player.getInventory().addItemOrDrop(new Item(QuestConstants.MIND_RUNE, 5));
					player.setNextTutorialStage();
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage >= 65 && stage < 67){
				if(chatId == 1 && (!player.hasItem(QuestConstants.AIR_RUNE) || !player.hasItem(QuestConstants.MIND_RUNE))){
					player.getDialogue().send2Items2Lines("Terrova gives you five @dbl@air runes @bla@and five @dbl@mind runes@bla@!", "", new Item(QuestConstants.AIR_RUNE, 5), new Item(QuestConstants.MIND_RUNE, 5));
					player.setClickId(0);
					player.getInventory().addItemOrDrop(new Item(QuestConstants.AIR_RUNE, 5));
					player.getInventory().addItemOrDrop(new Item(QuestConstants.MIND_RUNE, 5));
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage == 67){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Well you're all finished here now. I'll give you a",
													"reasonable number of runes when you leave.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOptionWTitle("Do you want to go to the mainland?", "Yes.",
													"No.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendNpcChat("When you get to the mainland you will find yourself in",
														"the town of Lumbridge. If you want some ideas on",
														"where to go next, talk to my friend the Lumbridge",
														"Guide. You can't miss him; he's holding a big staff with",Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("a question mark on the end. He also has a white beard",
													"and carries a rucksack full of scrolls. There are also",
													"many tutors willing to teach you about the many skills",
													"you could learn.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("If all else fails, visit the RuneScape website for a whole",
													"chestload of information on quests, skills and minigames",
													"as well as a very good starter's guide.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getActionSender().createPlayerHints(1, -1);
					player.teleport(new Position(3233, 3229, 0));
					player.getDialogue().resetDialogue();
					player.getDialogue().endDialogue();
					player.setNextTutorialStage();
					return true;
				}
			}
		}//end of npc 946
		return false;
	}

}
