package com.rs2.model.content.questing.quests;

import com.rs2.Constants;
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
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.util.Area;
import com.rs2.util.Misc;

public class WitchHouse extends Quest {
	
	final int rewardQP = 4;
	
	public WitchHouse(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to the little boy",
							"standing by the long garden just north of taverley",
							"I must be able to defeat a level 53 enemy"};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should find a way to enter the witches house."};
			return text;	
		}
		if(stage == 3){
			String text[] = {"I should be able to get to the ball with the clues",
							"from the diary."};
			return text;	
		}
		if(stage == 4){
			String text[] = {"I should now take the ball back to the boy."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "4 Quest Points", "6325 Hitpoints XP"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("4 Quest Points", 12150);
		player.getActionSender().sendString("6325 Hitpoints XP", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.HITPOINTS, 6325);
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.BALL);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean getObjectDialog(int clickId, final Player player, int objectId, int chatId, int optionId, int npcChatId, int x, int y, int stage){
		if(objectId == 2867 && x == 2900 && y == 3474 && clickId == 1){//witch house plant
			if(stage != 0 && !player.hasItem(QuestConstants.DOOR_KEY)){
				if(chatId == 1){
					player.getDialogue().sendStatement("You find a key hidden under the flower pot.");
					return true;
				}
				if(chatId == 2){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.DOOR_KEY, 1));
					player.getDialogue().endDialogue();
					return false;
				}
			}
		}
		if(objectId == 2869 && x == 2898 && y == 9873 && clickId == 1){//witch house cupboard
			if(!player.hasItem(QuestConstants.MAGNET)){
				if(chatId == 1){
					player.getDialogue().sendStatement("You find a magnet in the cupboard.");
					return true;
				}
				if(chatId == 2){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.MAGNET, 1));
					player.getDialogue().endDialogue();
					return false;
				}
			}
		}
		if(objectId == 2864 && x == 2909 && y == 3470 && clickId == 2){//witch house fountain
			if(!player.hasItem(QuestConstants.SHED_KEY)){
				if(chatId == 1){
					player.getDialogue().sendStatement("You search for the secret compartment mentioned in the diary.",
													"Inside it you find a small key. You take the key.");
					return true;
				}
				if(chatId == 2){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.SHED_KEY, 1));
					player.getDialogue().endDialogue();
					return false;
				}
			}
		}
		return false;
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 2861 && objectX == 2901 && objectY == 3473){//witch house door
			if(player.getInventory().playerHasItem(QuestConstants.DOOR_KEY) || player.getPosition().getX() >= 2901){
				player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
				player.getActionSender().walkTo(player.getPosition().getX() < 2901 ? 1 : -1, 0, true);
			}else{
				player.getActionSender().sendMessage("This door is locked.");
			}
			return true;
		}
		if((objectId == 2866 && objectX == 2902 && objectY == 9873) || (objectId == 2865 && objectX == 2902 && objectY == 9874)){//basement gate
			if(player.getEquipment().getId(Constants.HANDS) == QuestConstants.LEATHER_GLOVES){
				player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
				player.getActionSender().walkTo(player.getPosition().getX() < 2903 ? 1 : -1, 0, true);
			}else{
				player.getActionSender().sendMessage("The gate does not seem to open.");
			}
			return true;
		}
		if(objectId == 2868 && objectX == 2898 && objectY == 9873){//basement cupboard (closed)
			ObjectHandler.getInstance().removeObject(2898, 9873, 0, 0);
			ObjectHandler.getInstance().addObject(new GameObject(2869, 2898, 9873, 0, 0, 10, 2868, 999999999), true);
			return true;
		}
		if(objectId == 2862 && objectX == 2901 && objectY == 3465){//witch house door to garden
			if(player.tempQuestInt == QuestConstants.MAGNET || player.getPosition().getY() < 3466){
				player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
				player.getActionSender().walkTo(0, player.getPosition().getY() < 3466 ? 1 : -1, true);
				player.tempQuestInt = 0;
			}else{
				player.getActionSender().sendMessage("This door is locked.");
			}
			return true;
		}
		if(objectId == 2863 && objectX == 2934 && objectY == 3463){//shed door
			if(player.getPosition().getX() >= 2934){
				player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
				player.getActionSender().walkTo(player.getPosition().getX() < 2934 ? 1 : -1, 0, true);
			}else{
				player.getActionSender().sendMessage("The shed door is locked.");
			}
			return true;
		}
		return false;
	}
	
	public boolean useQuestItemOnObject(final Player player, int itemId, int objectId, final int stage){
		if(itemId == QuestConstants.CHEESE && objectId == 2870){
			if(stage < 3)
				return false;
			player.getInventory().removeItem(new Item(QuestConstants.CHEESE, 1));
			Npc npc = new Npc(QuestConstants.MOUSE);
			NpcLoader.spawnNpcPos(player, new Position(2903,3466,0), npc, false, false);
			player.getDialogue().sendStatement("A mouse runs out of a hole.");
			player.getDialogue().endDialogue();
			return true;
		}
		if(itemId == QuestConstants.SHED_KEY && objectId == 2863){//shed door
			if(player.getInventory().playerHasItem(QuestConstants.SHED_KEY)){
				player.getActionSender().walkThroughDoor(objectId, 2934, 3463, 0);
				player.getActionSender().walkTo(player.getPosition().getX() < 2934 ? 1 : -1, 0, true);
				if(stage == 3){
					Npc npc = new Npc(QuestConstants.WITCHES_EXPERIMENT_LVL19);
					NpcLoader.spawnNpcPos(player, new Position(2935,3462,0), npc, false, false);
				}
			}else{
				player.getActionSender().sendMessage("The shed door is locked.");
			}
			return true;
		}
		return false;
	}
	
	public boolean handleItemOnNpc(final Player player, int npcId, int itemId, int stage){
		if(itemId == QuestConstants.MAGNET && npcId == QuestConstants.MOUSE && player.getSpawnedNpc().getNpcId() == QuestConstants.MOUSE){
			player.getDialogue().sendStatement("You attach the magnet to the mouse's harness. The mouse finishes",
											"the cheese and runs back into its hole. You hear some odd noises",
											"from inside the walls. There is a strange whirring noise from above",
											"the door frame.");
			Npc npc = player.getSpawnedNpc();
			NpcLoader.destroyNpc(npc);
			player.tempQuestInt = QuestConstants.MAGNET;
			player.getInventory().removeItem(new Item(QuestConstants.MAGNET, 1));
			player.getDialogue().endDialogue();
			return true;
		}
		return false;
	}
	
	public boolean controlDying(final Entity attacker, final Entity victim, int stage){
		if(victim.isNpc() && attacker.isPlayer()){
        	Player player = (Player) attacker;
        	Npc npc = (Npc) victim;
        	if(npc.getNpcId() == QuestConstants.WITCHES_EXPERIMENT_LVL19){
        		npc.setDead(true);
				CombatManager.startDeath(npc);
				player.getActionSender().sendMessage("The shapeshifters' body begins to deform!");
				player.getActionSender().sendMessage("The shapeshifter turns into a spider!");
				Npc npc1 = new Npc(QuestConstants.WITCHES_EXPERIMENT_LVL30);
				NpcLoader.spawnNpcPos(player, new Position(2935,3462,0), npc1, false, false);
        		return true;
        	}
        	if(npc.getNpcId() == QuestConstants.WITCHES_EXPERIMENT_LVL30){
        		npc.setDead(true);
				CombatManager.startDeath(npc);
				player.getActionSender().sendMessage("The shapeshifters' body begins to twist!");
				player.getActionSender().sendMessage("The shapeshifter turns into a bear!");
				Npc npc1 = new Npc(QuestConstants.WITCHES_EXPERIMENT_LVL42);
				NpcLoader.spawnNpcPos(player, new Position(2935,3462,0), npc1, false, false);
        		return true;
        	}
        	if(npc.getNpcId() == QuestConstants.WITCHES_EXPERIMENT_LVL42){
        		npc.setDead(true);
				CombatManager.startDeath(npc);
				player.getActionSender().sendMessage("The shapeshifters' body pulses!");
				player.getActionSender().sendMessage("The shapeshifter turns into a wolf!");
				Npc npc1 = new Npc(QuestConstants.WITCHES_EXPERIMENT_LVL53);
				NpcLoader.spawnNpcPos(player, new Position(2935,3462,0), npc1, false, false);
        		return true;
        	}
        }
		return false;
	}
	
	public boolean handleNpcDeath(final Player player, int npcId, int stage){
		if(npcId == QuestConstants.WITCHES_EXPERIMENT_LVL53){
			player.getActionSender().sendMessage("You finally kill the shapeshifter once and for all.");
			player.questStage[this.getId()] = 4;
			return true;
		}
		return false;
	}
	
	public boolean getQuestPickups(final Player player, int itemId, int stage){
		if(itemId == QuestConstants.BALL){
			if(player.questStage[this.getId()] == 4 && !player.hasItem(QuestConstants.BALL))
				return false;
			else{
				player.getActionSender().sendMessage("You have to defeat the witches experiment first.");
				return true;
			}
		}
		return false;
	}
	
	Area garden = new Area(2901, 3460, 2933, 3466, (byte) 0);
	
	/*public boolean checkAreas(final Player player, int stage){
		if(garden.containsBorderIncluded(player.getPosition())){
			Npc npc = Npc.getNpcById(QuestConstants.NORA_T_HAGG);
			if(Misc.canBeSeen(player, npc)){
				npc.getUpdateFlags().setForceChatMessage("Get out of my property!");
				teleportOut(player);
			}
			return true;
		}
		return false;
	}*/
	
	public boolean checkAreas(final Player player, int stage){
		if(player.tempAreaEffectInt == QuestConstants.NORA_T_HAGG)
			return true;
		if(garden.containsBorderIncluded(player.getPosition())){
			final Tick timer1 = new Tick(1) {
	            @Override
	            public void execute() {
	            	if(!player.isLoggedIn()){
	            		stop();
	            		return;
	            	}
	            	if(garden.containsBorderIncluded(player.getPosition())){
	            		Npc npc = Npc.getNpcById(QuestConstants.NORA_T_HAGG);
	        			if(Misc.canBeSeen(player, npc)){
	        				npc.getUpdateFlags().setForceChatMessage("Get out of my property!");
	        				teleportOut(player);
	        			}
	            	}else{
	            		player.tempAreaEffectInt = -1;
	            		stop();
	            	}
	            }
			};
	        World.getTickManager().submit(timer1);
	        player.tempAreaEffectInt = QuestConstants.NORA_T_HAGG;
			return true;
		}
		return false;
	}
	
	public void teleportOut(final Player player) {
		player.getUpdateFlags().sendHighGraphic(110);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int teleTimer = 6;
			@Override
			public void execute(CycleEventContainer container) {
				teleTimer--;
				if (!player.isDead()) {
					if (teleTimer == 3) {
						if(player.getInventory().playerHasItem(QuestConstants.BALL))
							player.getInventory().removeItem(new Item(QuestConstants.BALL, 1));
						player.teleport(new Position(2917,3456,0));
					}
				} else {
					teleTimer = 0;
				}
				if (teleTimer < 1) {
					container.stop();
				}
			}
			@Override
			public void stop() {
				player.setStopPacket(false);
				player.getAttributes().put("canTakeDamage", Boolean.TRUE);
			}
		}, 1);
	}
	
	public String[] getBookText(int page){
		if(page == 0){
			String text[] = {"Witches' Diary",//title
							"",//page1
							"",//page2
							"@red@2nd of Pentember",//line1 - page1
							"Experiment is growing",
							"larger daily. Making",
							"excellent progress now. I",
							"am currently feeding it",
							"on a mixture of fungus,",
							"tar and clay.",
							"It seems to like this",
							"combination a lot!",
							"",
							"",
							"@red@3rd of Pentember",//line1 - page2
							"Experiment still going",
							"extremely well. Moved it",
							"to the wooden garden",
							"shed; it does too much",
							"damage in the house! It",
							"is getting very strong",
							"now, but unfortunately is",
							"not too intelligent yet. It",
							"has a really mean stare",
							"too!"};
			return text;
		}
		if(page == 1){
			String text[] = {"Witches' Diary",//title
							"",//page1
							"",//page2
							"@red@4th of Pentember",//line1 - page1
							"Sausages for dinner",
							"tonight! Lovely!",
							"",
							"@red@5th of Pentember",
							"A guy called Professor",
							"Oddenstein installed a",
							"new security system for",
							"me in the basement. He",
							"seems to have a lot of",
							"good security ideas.",
							"@red@6th of Pentember",//line1 - page2
							"Don't want people getting",
							"into back garden to see",
							"the experiment. Professor",
							"Oddenstein is fitting me a",
							"new security system,",
							"after his successful",
							"installation in the cellar."};
			return text;
		}
		if(page == 2){
			String text[] = {"Witches' Diary",//title
							"",//page1
							"",//page2
							"@red@7th of Pentember",//line1 - page1
							"That pesky kid keeps",
							"kicking his ball into my",
							"garden. I swear, if he",
							"does it AGAIN, I'm going",
							"to lock his ball away in",
							"the shed.",
							"",
							"@red@8th of Pentember",
							"The security system is",
							"done. By Zamorak! Wow,",
							"is it contrived! Now, to",//line1 - page2
							"open my own back door,",
							"I lure a mouse out of a",
							"hole in the back porch, I",
							"fit a magic curved piece",
							"of metal to the harness",
							"on its back, the mouse",
							"goes back in the hole, and",
							"the door unlocks! The",
							"prof tells me that this is",
							"cutting edge technology!"};
			return text;
		}
		if(page == 3){
			String text[] = {"Witches' Diary",//title
							"",//page1
							"",//page2
							"As an added precaution I",//line1 - page1
							"have hidden the key to",
							"the shed in a secret",
							"compartment of the",
							"fountain in the garden.",
							"No one will ever look",
							"there!",
							"",
							"@red@9th of Pentember",
							"Still can't think of a good",
							"name for 'The",
							"Experiment'. Leaning",//line1 - page2
							"towards 'Fritz'... Although",
							"am considering Lucy as",
							"it reminds me of my",
							"mother!"};
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
	
	int bookPages = 3;
	
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
		if(player.questStage[this.getId()] == 2){
			if(page == 3)
				player.questStage[this.getId()] = 3;
		}
	}
	
	public boolean handleInterfaceButton(final Player player, int buttonId, int stage){
		if(player.readingBook == QuestConstants.DIARY){
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
			if(itemId == QuestConstants.DIARY){
				openBookPage(player, 0);
				player.readingBook = itemId;
				player.readingBookPage = 0;
				player.getActionSender().sendInterface(837);
				return true;
			}
		}
		return false;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.BOY){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello young man.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendStatement("The boy sobs.");
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendOption("What's the matter?",
													"Well if you're not going to answer, I'll go.");
					return true;
				}
				if(chatId == 4){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("What's the matter?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("I've kicked my ball over that hedge, into that garden!",
													"The old lady who lives there is scary... She's locked the",
													"ball in her wooden shed! Can you get my ball back for",
													"me please?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendOption("Ok, I'll see what I can do.",
													"Get it back yourself.");
					return true;
				}
				if(chatId == 7){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Ok, I'll see what I can do.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(8);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Thanks mister!",Dialogues.CONTENT);
					questStarted(player);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 0
			if(stage == 4){
				if(!player.getInventory().playerHasItem(QuestConstants.BALL))
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hi, I have got your ball back. It was MUCH harder",
														"than I thought it would be.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendStatement("You give the ball back.");
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Thank you so much!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getInventory().removeItem(new Item(QuestConstants.BALL, 1));
					player.getDialogue().endDialogue();
					questCompleted(player);
					return true;
				}
			}//end of stage 4
		}//end of boy
		return false;
	}

}
