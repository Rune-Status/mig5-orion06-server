package com.rs2.model.content.questing.quests;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.projectile.Projectile;
import com.rs2.model.content.combat.projectile.ProjectileDef;
import com.rs2.model.content.combat.projectile.ProjectileTrajectory;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;

public class ErnestTheChicken extends Quest {
	
	final int rewardQP = 0;//was 4
	
	public ErnestTheChicken(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to Veronica who is",
							"outside Draynor Manor",
							"",
							"There aren't any requirements for this quest",
							"",
							"This quest is currently broken!"};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should go into Draynor Manor and look for Ernest"};
			return text;	
		}
		if(stage == 3 || stage == 4){
			String text[] = {"I should bring these items to Professor Oddenstein:",
							"Pressure gauge",
							"Rubber tube",
							"Oil can"};
			return text;	
		}
		if(stage == 5){
			String text[] = {"I should talk to Professor Oddenstein to",
							"finish this quest."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "4 Quest Points", "300 Coins"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("4 Quest Points", 12150);
		player.getActionSender().sendString("300 Coins", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getInventory().addItemOrDrop(new Item(QuestConstants.COINS, 300));
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.FEATHER);
		player.getActionSender().sendInterface(12140);
	}
	
	boolean playerHasAllItems(final Player player){
		if(player.getInventory().playerHasItem(QuestConstants.PRESSURE_GAUGE, 1) && player.getInventory().playerHasItem(QuestConstants.RUBBER_TUBE, 1) &&
				player.getInventory().playerHasItem(QuestConstants.OIL_CAN, 1)){
			return true;
		}
		return false;
	}
	
	void updateBasementDoors(final Player player){
		int config = 668;
		if(player.configValue[33] == 4){
			player.configValue[config] = 4;
		}
		if(player.configValue[33] == 6){
			player.configValue[config] = 132;
		}
		if(player.configValue[33] == 14){
			player.configValue[config] = 0;
		}
		if(player.configValue[33] == 30){
			player.configValue[config] = 256;
		}
		if(player.configValue[33] == 22){
			player.configValue[config] = 396;
		}
		if(player.configValue[33] == 20){
			player.configValue[config] = 332;
		}
		if(player.configValue[33] == 16){
			player.configValue[config] = 328;
		}
		if(player.configValue[33] == 80){
			player.configValue[config] = 34;
		}
		if(player.configValue[33] == 48){
			player.configValue[config] = 0;
		}
		if(player.configValue[33] == 112){
			player.configValue[config] = 3;
		}
		if(player.configValue[33] == 96){
			player.configValue[config] = 1;
		}
		if(player.configValue[33] == 120){
			player.configValue[config] = 19;
		}
		if(player.configValue[33] == 104){
			player.configValue[config] = 1;
		}
		if(player.configValue[33] == 56){
			player.configValue[config] = 64;
		}
		if(player.configValue[33] == 88){
			player.configValue[config] = 306;
		}
		player.getActionSender().sendConfig(config, player.configValue[config]);
	}
	
	public boolean useQuestItemOnObject(final Player player, int itemId, int objectId, int stage){
		if(player.questStage[this.getId()] >= 3){
			if(itemId == QuestConstants.POISONED_FISH_FOOD && objectId == 153 && player.getInventory().playerHasItem(QuestConstants.POISONED_FISH_FOOD)){
				final Tick timer1 = new Tick(1) {
		            @Override
		            public void execute() {
		            	player.getActionSender().sendMessage("The piranhas start eating the food...");
		            	World.getTickManager().submit(timer2);
		                stop();
		            }
		            
		            final Tick timer2 = new Tick(2) {
			            @Override
			            public void execute() {
			            	player.getActionSender().sendMessage("...Then die and float to the surface.");
			                stop();
			            }
			        };
			        
		        };
				player.getActionSender().sendMessage("You pour the poisoned fish food into the fountain.");
				player.getInventory().removeItem(new Item(QuestConstants.POISONED_FISH_FOOD, 1));
				if(player.questStage[this.getId()] == 3){
					player.questStage[this.getId()] = 4;
					World.getTickManager().submit(timer1);
				}
				return true;
			}
		}
		/*if(player.questStage[this.getId()] >= 3)//cheap hax fix
			return true;*/
		return false;
	}
	
	public boolean getObjectDialog(int clickId, final Player player, int objectId, int chatId, int optionId, int npcChatId, int x, int y, int stage){
		if(objectId == 153 && x == 3087 && y == 3334 && clickId == 1){
			if(stage == 4 && !player.hasItem(QuestConstants.PRESSURE_GAUGE)){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("There seems to be a pressure gauge in here...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("... and a lot of dead fish.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("You get the pressure gauge from the fountain.", Dialogues.CONTENT);
					player.getInventory().addItemOrDrop(new Item(QuestConstants.PRESSURE_GAUGE, 1));
					player.getActionSender().removeInterfaces();
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if((objectId == 134 && objectX == 3108 && objectY == 3353) || (objectId == 135 && objectX == 3109 && objectY == 3353)){//main entrance door
			if(player.getPosition().getY() < 3354){
				player.getActionSender().walkTo(0, 1, true);
				player.getActionSender().walkThroughDoubleDoor2(134, 135, 3108, 3353, 3109, 3353, 0);
				player.getActionSender().sendMessage("The doors slam shut behind you.");
				return true;
			} else {
				player.getActionSender().sendMessage("The doors won't open.");
				return true;
			}
		}
		if((objectId == 155 && objectX == 3097 && objectY == 3358) || (objectId == 156 && objectX == 3097 && objectY == 3359)){//hidden bookcase door
			if(player.getPosition().getX() >= 3098){
				player.getActionSender().walkTo(-2, 0, true);
				/*ObjectHandler.getInstance().removeObject(3097, 3358, 0, 0);
				ObjectHandler.getInstance().removeObject(3097, 3359, 0, 0);*/
				ObjectHandler.getInstance().addObject(new GameObject(Constants.EMPTY_OBJECT, 3097, 3358, 0, 0, 10, 155, 2), true);
				ObjectHandler.getInstance().addObject(new GameObject(Constants.EMPTY_OBJECT, 3097, 3359, 0, 0, 10, 156, 2), true);
				ObjectHandler.getInstance().addObject(new GameObject(155, 3097, 3357, 0, 0, 10, 11474, 2), true);
				ObjectHandler.getInstance().addObject(new GameObject(156, 3097, 3360, 0, 0, 10, 11512, 2), true);
				player.getActionSender().sendMessage("You've found a secret door!");
				player.getActionSender().sendSound(318, 1, 0);
				return true;
			} else {
				player.getActionSender().sendMessage("It won't open.");
				return true;
			}
		}
		if(objectId == 160 && objectX == 3096 && objectY == 3357){//hidden bookcase lever
			final Tick timer1 = new Tick(3) {
	            @Override
	            public void execute() {
	            	ObjectHandler.getInstance().addObject(new GameObject(Constants.EMPTY_OBJECT, 3097, 3358, 0, 0, 10, 155, 2), true);
	    			ObjectHandler.getInstance().addObject(new GameObject(Constants.EMPTY_OBJECT, 3097, 3359, 0, 0, 10, 156, 2), true);
	    			ObjectHandler.getInstance().addObject(new GameObject(155, 3097, 3357, 0, 0, 10, 11474, 2), true);
	    			ObjectHandler.getInstance().addObject(new GameObject(156, 3097, 3360, 0, 0, 10, 11512, 2), true);
	    			player.getActionSender().walkTo(2, 0, true);
	                stop();
	            }
	        };
			player.getActionSender().walkTo(0, 1, true);
			player.getActionSender().sendSound(319, 1, 0);
			player.getUpdateFlags().sendAnimation(835);
			player.getActionSender().sendMessage("The lever opens the secret door!");
			World.getTickManager().submit(timer1);
			return true;
		}
		if(objectId == 136 && objectX == 3123 && objectY == 3361){//Door to backyard
			player.getActionSender().walkTo(0, player.getPosition().getY() < 3361 ? 1 : -1, true);
			player.getActionSender().walkThroughDoor(136, 3123, 3361, 0);
			return true;
		}
		if(objectId == 131 && objectX == 3107 && objectY == 3367){//Door to rubber tube
			if(player.getInventory().playerHasItem(QuestConstants.RUBBER_TUBE_ROOM_KEY)){
				player.getActionSender().walkTo(player.getPosition().getX() < 3108 ? 1 : -1, 0, true);
				player.getActionSender().walkThroughDoor(131, 3107, 3367, 0);
				player.getActionSender().sendMessage("You unlock the door.");
				return true;
			} else {
				player.getActionSender().sendMessage("It is locked.");
				return true;
			}
		}
		if(objectId == 152 && objectX == 3084 && objectY == 3360){//Compost heap (to get key)
			if(stage >= 3 && !player.hasItem(QuestConstants.RUBBER_TUBE_ROOM_KEY)){
				if(player.getInventory().playerHasItem(QuestConstants.SPADE)){
					final Tick timer1 = new Tick(2) {
			            @Override
			            public void execute() {
			            	player.getInventory().addItemOrDrop(new Item(QuestConstants.RUBBER_TUBE_ROOM_KEY, 1));
			            	player.getActionSender().sendMessage("...and find a small key.");
			                stop();
			            }
			        };
					player.getUpdateFlags().sendAnimation(830);
					player.getActionSender().sendSound(232, 1, 0);
					player.getActionSender().sendMessage("You dig through the compost...");
					World.getTickManager().submit(timer1);
					return true;
				} else {
					player.getActionSender().sendMessage("I need a spade to do that.");
					return true;
				}
			}
		}
		if(objectId == 133 && objectX == 3092 && objectY == 3362){
			Ladders.climbLadder(player, new Position(3117,9753,0));
			return true;
		}
		if(objectId == 132 && objectX == 3117 && objectY == 9754){
			Ladders.climbLadder(player, new Position(3092,3361,0));
			return true;
		}
		if(objectId == 146 && objectX == 3108 && objectY == 9745){//Lever A
			int config = 33;
			int value = 2;
			if((player.configValue[config] & value) != 0){
				player.configValue[config] -= value;
				player.getActionSender().sendMessage("You pull lever A up.");
			}else{
				player.configValue[config] += value;
				player.getActionSender().sendMessage("You pull lever A down.");
			}
			player.getActionSender().sendConfig(config, player.configValue[config]);
			player.getActionSender().sendSound(319, 1, 0);
			player.getUpdateFlags().sendAnimation(835);
			player.getActionSender().sendMessage("You hear a clunk.");
			updateBasementDoors(player);
			return true;
		}
		if(objectId == 147 && objectX == 3118 && objectY == 9752){//Lever B
			int config = 33;
			int value = 4;
			if((player.configValue[config] & value) != 0){
				player.configValue[config] -= value;
				player.getActionSender().sendMessage("You pull lever B up.");
			}else{
				player.configValue[config] += value;
				player.getActionSender().sendMessage("You pull lever B down.");
			}
			player.getActionSender().sendConfig(config, player.configValue[config]);
			player.getActionSender().sendSound(319, 1, 0);
			player.getUpdateFlags().sendAnimation(835);
			player.getActionSender().sendMessage("You hear a clunk.");
			updateBasementDoors(player);
			return true;
		}
		if(objectId == 148 && objectX == 3112 && objectY == 9760){//Lever C
			int config = 33;
			int value = 8;
			if((player.configValue[config] & value) != 0){
				player.configValue[config] -= value;
				player.getActionSender().sendMessage("You pull lever C up.");
			}else{
				player.configValue[config] += value;
				player.getActionSender().sendMessage("You pull lever C down.");
			}
			player.getActionSender().sendConfig(config, player.configValue[config]);
			player.getActionSender().sendSound(319, 1, 0);
			player.getUpdateFlags().sendAnimation(835);
			player.getActionSender().sendMessage("You hear a clunk.");
			updateBasementDoors(player);
			return true;
		}
		if(objectId == 149 && objectX == 3108 && objectY == 9767){//Lever D
			int config = 33;
			int value = 16;
			if((player.configValue[config] & value) != 0){
				player.configValue[config] -= value;
				player.getActionSender().sendMessage("You pull lever D up.");
			}else{
				player.configValue[config] += value;
				player.getActionSender().sendMessage("You pull lever D down.");
			}
			player.getActionSender().sendConfig(config, player.configValue[config]);
			player.getActionSender().sendSound(319, 1, 0);
			player.getUpdateFlags().sendAnimation(835);
			player.getActionSender().sendMessage("You hear a clunk.");
			updateBasementDoors(player);
			return true;
		}
		if(objectId == 150 && objectX == 3097 && objectY == 9767){//Lever E
			int config = 33;
			int value = 32;
			if((player.configValue[config] & value) != 0){
				player.configValue[config] -= value;
				player.getActionSender().sendMessage("You pull lever E up.");
			}else{
				player.configValue[config] += value;
				player.getActionSender().sendMessage("You pull lever E down.");
			}
			player.getActionSender().sendConfig(config, player.configValue[config]);
			player.getActionSender().sendSound(319, 1, 0);
			player.getUpdateFlags().sendAnimation(835);
			player.getActionSender().sendMessage("You hear a clunk.");
			updateBasementDoors(player);
			return true;
		}
		if(objectId == 151 && objectX == 3096 && objectY == 9765){//Lever F
			int config = 33;
			int value = 64;
			if((player.configValue[config] & value) != 0){
				player.configValue[config] -= value;
				player.getActionSender().sendMessage("You pull lever F up.");
			}else{
				player.configValue[config] += value;
				player.getActionSender().sendMessage("You pull lever F down.");
			}
			player.getActionSender().sendConfig(config, player.configValue[config]);
			player.getActionSender().sendSound(319, 1, 0);
			player.getUpdateFlags().sendAnimation(835);
			player.getActionSender().sendMessage("You hear a clunk.");
			updateBasementDoors(player);
			return true;
		}
		/*if(objectId == 144 && objectX == 3108 && objectY == 9758){
			player.getActionSender().sendMessage("You hear a clunk.");
			return true;
		}*/
		return false;
	}
	
	public void castProjectile(final Player player, final Position start, final Position end, int gfx) {
		final int hitDelay = calculateHitDelay(start, end);
		new Projectile(start, 0, end, 0, new ProjectileDef(gfx, ProjectileTrajectory.SPELL)).show();
		final Tick tick = new Tick(hitDelay) {
		@Override
			public void execute() {
				Npc npc = Npc.getNpcById(QuestConstants.ERNEST_CHICKEN);
				npc.sendTransform(QuestConstants.ERNEST, 100);
				Dialogues.sendDialogue(player, QuestConstants.ERNEST, 100, 0);
				this.stop();
			}
		};
		World.getTickManager().submit(tick);
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.VERONICA){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Can you please help me? I'm in a terrible spot of",
													"trouble.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Aha, sounds like a quest. I'll help.",
													"No, I'm looking for something to kill.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Aha, sounds like a quest. I'll help.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						questStarted(player);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}//end of stage 0
			if(stage == 2){
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Yes yes, I suppose it is a quest. My fiance Ernest and",
													"I came upon this house.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Seeing as we were a little lost Ernest decided to go in",
													"and ask for directions.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("That was an hour ago. That house looks spooky, can",
													"you go and see if you can find him for me?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("Ok, I'll see what I can do.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Thank you, thank you. I'm very grateful.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//veronica
		if(npcId == QuestConstants.PROFESSOR_ODDENSTEIN){
			if(stage == 2){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Be careful in here, there's lots of dangerous equipment.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("I'm looking for a guy called Ernest.",
													"What does this machine do?",
													"Is this your house?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I'm looking for a guy called Ernest.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Ah Ernest, top notch bloke. He's helping me with my",
													"experiments.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("So you know where he is then?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("He's that chicken over there.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("Ernest is a chicken..? Are you sure..?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Oh, he isn't normally a chicken, or at least he wasn't",
													"until he helped me test my pouletmorph machine.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("It was originally going to be called a transmutation",
													"machine. But after testing pouletmorph seems more",
													"appropriate.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendOption("I'm glad Veronica didn't actually get engaged to a chicken.",
													"Change him back this instant!");
					return true;
				}
				if(chatId == 11){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I'm glad Veronica didn't actually get engaged to a",
															"chicken.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(12);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("Who's Veronica?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendPlayerChat("Ernest's fiancee. She probably doesn't want to marry a",
														"chicken.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("Ooh I dunno. She could have free eggs for breakfast.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendPlayerChat("I think you'd better change him back.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("Umm... It's not so easy...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendNpcChat("My machine is broken, and the house gremlins have",
													"run off with some vital bits.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendPlayerChat("Well I can look for them.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 19){
					player.getDialogue().sendNpcChat("That would be a help. They'll be somewhere in the",
													"manor house or its grounds, the gremlins never get",
													"further than entrance gate.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 20){
					player.getDialogue().sendNpcChat("I'm missing the pressure gauge and a rubber tube.",
													"They've also taken my oil can, which I'm going to need",
													"to get this thing started again.",Dialogues.CONTENT);
					player.questStage[this.getId()] = 3;
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage == 3 || stage == 4){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Have you found anything yet?",Dialogues.CONTENT);
					if(playerHasAllItems(player))
						player.getDialogue().setNextChatId(2);
					else
						player.getDialogue().setNextChatId(5);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("I'm still looking.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("I have everything!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Give 'em here then.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getActionSender().removeInterfaces();
					player.getActionSender().sendMessage("You give a rubber tube, a pressure gauge,");
					player.getActionSender().sendMessage("and a can of oil to the professor.");
					Npc npc = Npc.getNpcById(QuestConstants.PROFESSOR_ODDENSTEIN);
					npc.getMovementHandler().addToPath(new Position((npc.getPosition().getX() < 3108 ? 3107 : 3111),3367,2));
					Position start = npc.getPosition();
					Position end = new Position((npc.getPosition().getX() < 3108 ? 3107 : 3111),3367,2);
					
					final int hitDelay = calculateWalkDelay(start, end);
					final Tick tick = new Tick(hitDelay) {
					@Override
						public void execute() {
							player.getActionSender().sendMessage("Oddenstein starts up the machine.");
							World.getTickManager().submit(timer2);
							this.stop();
						}
					
						final Tick timer2 = new Tick(1) {
			            @Override
			            	public void execute() {
			            		player.getActionSender().sendMessage("The machine hums and shakes.");
			            		Npc npc2 = Npc.getNpcById(QuestConstants.ERNEST_CHICKEN);
								castProjectile(player, new Position(3111,3366,2), npc2.getPosition(), 605);
			            		stop();
			            	}
						};
					};
					World.getTickManager().submit(tick);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage == 5){
				questCompleted(player);
			}
		}//oddenstein
		if(npcId == QuestConstants.ERNEST){
			if(chatId == 100 && playerHasAllItems(player)){
				player.questStage[this.getId()] = 5;
				player.getInventory().removeItem(new Item(QuestConstants.PRESSURE_GAUGE, 1));
				player.getInventory().removeItem(new Item(QuestConstants.RUBBER_TUBE, 1));
				player.getInventory().removeItem(new Item(QuestConstants.OIL_CAN, 1));
				player.getDialogue().sendNpcChat("Thank you sir. It was dreadfully irritating being a",
												"chicken. How can I ever thank you?",Dialogues.CONTENT);
				return true;
			}
			if(stage == 4){
				if(chatId == 101){
					player.getDialogue().sendPlayerChat("Well a cash reward is always nice...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 102){
					player.getDialogue().sendNpcChat("Of course, of course.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 103){
					player.getActionSender().sendMessage("Ernest hands you 300 coins.");
					questCompleted(player);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}
		return false;
	}

}
