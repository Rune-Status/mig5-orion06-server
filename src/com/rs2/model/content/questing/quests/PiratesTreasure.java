package com.rs2.model.content.questing.quests;

import com.rs2.Constants;
import com.rs2.model.World;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
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

public class PiratesTreasure extends Quest {
	
	final int rewardQP = 2;
	
	public PiratesTreasure(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to Redbeard Frank who",
							"is at Port Sarim.",
							"",
							"There aren't any requirements for this quest."};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should bring a bottle of karamjan rum to Redbeard Frank."};
			return text;	
		}
		if(stage == 3){
			String text[] = {"I have stashed a bottle of karamjan rum in the crate.",
							"I should fill the crate with bananas and talk to Luthas",
							"when done."};
			return text;	
		}
		if(stage == 4){
			String text[] = {"I should go find the crate of bananas in Port Sarim and",
							"retrieve the karamjan rum."};
			return text;	
		}
		if(stage == 5){
			String text[] = {"I should go get white apron to get a job at the store."};
			return text;	
		}
		if(stage == 6){
			String text[] = {"I should now search for the crate."};
			return text;	
		}
		if(stage == 7){
			String text[] = {"I should bring a bottle of karamjan rum to Redbeard Frank."};
			return text;
		}
		if(stage == 8){
			String text[] = {"I should go to Blue Moon Inn in Varrock and try the key",
							"to a chest in Hector's old room."};
			return text;
		}
		if(stage == 9 || stage == 10){
			String text[] = {"I should solve Hector's clue in the message",
							"to find the treasure."};
			return text;
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "2 Quest Points", "450 Coins", "Gold ring", "Emerald"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("2 Quest Points", 12150);
		player.getActionSender().sendString("450 Coins", 12151);
		player.getActionSender().sendString("Gold ring", 12152);
		player.getActionSender().sendString("Emerald", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getInventory().addItemOrDrop(new Item(QuestConstants.COINS, 450));
		player.getInventory().addItemOrDrop(new Item(QuestConstants.GOLD_RING, 1));
		player.getInventory().addItemOrDrop(new Item(QuestConstants.EMERALD, 1));
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.EMERALD);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean handleFirstClickItem(final Player player, int interfaceId, int itemId, int stage){
		if(interfaceId == 3214){//inventory
			if(itemId == QuestConstants.PIRATE_MESSAGE){
				for(int id2 = 0; id2 < 8; id2++){
					player.getActionSender().sendString("", 6968+id2);
				}
				player.getActionSender().sendString("Visit the city of the White Knights.", 6970);
				player.getActionSender().sendString("In the park, Saradomin points to the X", 6971);
				player.getActionSender().sendString("which marks the spot.", 6972);
				player.getActionSender().sendInterface(6965);
				return true;
			}
			if(itemId == QuestConstants.SPADE){
				if (player.getPosition().getY() == 3383 && (player.getPosition().getX() == 2999 || player.getPosition().getX() == 3000)) {
					if (!player.getPjTimer().completed()) {
						player.getActionSender().sendMessage("You can't dig during combat.");
						return true;
					}
					if(stage == 9){
						player.questStage[this.getId()] = 10;
						Npc npc = new Npc(QuestConstants.GARDENER);
						NpcLoader.spawnNpc(player, npc, true, true);
			            npc.getUpdateFlags().setForceChatMessage("First moles, now this! Take this, vandal!");
						return true;
					}
					if(stage == 10){
						player.getUpdateFlags().sendAnimation(830);
						player.getActionSender().sendSound(232, 1, 0);
						player.getActionSender().sendMessage("You dig into the ground...");

						final int task = player.getTask();
						player.setSkilling(new CycleEvent() {

							@Override
							public void execute(CycleEventContainer container) {
								if (!player.checkTask(task)) {
									container.stop();
									return;
								}
								player.getActionSender().sendMessage("and find the treasure.");
								questCompleted(player);
								container.stop();
								return;
							}

							@Override
							public void stop() {
								player.resetAnimation();
							}
						});
						CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 2);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean getObjectDialog(int clickId, final Player player, int objectId, int chatId, int optionId, int npcChatId, int x, int y, int stage){
		if(objectId == 2071 && x == 3009 && y == 3207){//banana crate @port sarim
			if(stage == 6 && player.hasItem(QuestConstants.KARAMJAN_RUM))
				player.questStage[this.getId()] = 7;
			if(stage >= 6 && optionId == 0){
				final Tick timer1 = new Tick(3) {
		            @Override
		            public void execute() {
		            	player.getInventory().addItemOrDrop(new Item(QuestConstants.KARAMJAN_RUM, 1));
		            	player.getUpdateFlags().sendAnimation(832);
		            	player.getActionSender().sendMessage("You find your bottle of rum in amongst the bananas.");
		            	World.getTickManager().submit(timer2);
		                stop();
		            }
		            final Tick timer2 = new Tick(2) {
			            @Override
			            public void execute() {
			            	player.getDialogue().sendOptionWTitle("Do you want to take a banana?", "Yes.",
																"No.");
			                stop();
			            }
			        };
		        };
		        final Tick timer2 = new Tick(2) {
		        	@Override
		        	public void execute() {
		        		player.getDialogue().sendOptionWTitle("Do you want to take a banana?", "Yes.",
															"No.");
		                stop();
		            }
		        };
				player.getActionSender().sendMessage("There are a lot of bananas in the crate.");
				if(!player.hasItem(QuestConstants.KARAMJAN_RUM) && chatId == 1)
					World.getTickManager().submit(timer1);
				else if(chatId == 1)
					World.getTickManager().submit(timer2);
			} else if(optionId == 0 && chatId == 0){
				final Tick timer1 = new Tick(2) {
		            @Override
		            public void execute() {
		            	player.getDialogue().sendOptionWTitle("Do you want to take a banana?", "Yes.",
															"No.");
		            	stop();
		            }
		        };
				player.getActionSender().sendMessage("There are a lot of bananas in the crate.");
				World.getTickManager().submit(timer1);
			} else if(optionId == 1){
				player.getInventory().addItemOrDrop(new Item(QuestConstants.BANANA, 1));
				player.getUpdateFlags().sendAnimation(832);
				player.getActionSender().sendMessage("You take a banana.");
				player.getDialogue().endDialogue();
				player.getActionSender().removeInterfaces();
			} else if(optionId == 2){
				player.getDialogue().endDialogue();
				player.getActionSender().removeInterfaces();
			}
			return true;
		}
		return false;
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 2072 && objectX == 2943 && objectY == 3151){//banana crate @karamja
			if(player.bananaCrate == 0){
				player.getActionSender().sendMessage("The crate is completely empty.");
			}
			if(player.bananaCrate > 0 && player.bananaCrate < 4){
				player.getActionSender().sendMessage("There is a few bananas.");
			}
			if(player.bananaCrate >= 4 && player.bananaCrate <= 6){
				player.getActionSender().sendMessage("The crate is around half full.");
			}
			if(player.bananaCrate > 6 && player.bananaCrate < 10){
				player.getActionSender().sendMessage("The crate is almost full.");
			}
			if(player.bananaCrate == 10){
				player.getActionSender().sendMessage("The crate is full.");
			}
			return true;
		}
		if(objectId == 2069 && objectX == 3012 && objectY == 3204){//port sarim shop door
			if(stage == 4)
				Dialogues.sendDialogue(player, QuestConstants.WYDIN, 100, 0);
			if(stage == 5)
				Dialogues.sendDialogue(player, QuestConstants.WYDIN, 108, 0);
			if(stage >= 6 && player.getEquipment().getItemContainer().get(Constants.CHEST) == null)
				Dialogues.sendDialogue(player, QuestConstants.WYDIN, 112, 0);
			else if(stage >= 6 && player.getEquipment().getItemContainer().get(Constants.CHEST).getId() != QuestConstants.WHITE_APRON)
				Dialogues.sendDialogue(player, QuestConstants.WYDIN, 112, 0);
			else if(stage >= 6 && player.getEquipment().getItemContainer().get(Constants.CHEST).getId() == QuestConstants.WHITE_APRON){
				player.getActionSender().walkTo(player.getPosition().getX() < 3012 ? 1 : -1, 0, true);
				player.getActionSender().walkThroughDoor(2069, 3012, 3204, 0);
			}
			return true;
		}
		return false;
	}
	
	public boolean useQuestItemOnObject(final Player player, int itemId, int objectId, int stage){
		if(itemId == QuestConstants.HECTORS_KEY && objectId == 2079 && player.getInventory().playerHasItem(QuestConstants.HECTORS_KEY)){
			final Tick timer1 = new Tick(4) {
				@Override
	            public void execute() {
					player.getInventory().removeItem(new Item(QuestConstants.HECTORS_KEY, 1));
					player.getInventory().addItemOrDrop(new Item(QuestConstants.PIRATE_MESSAGE, 1));
					player.getActionSender().sendMessage("You take the message from the chest.");
	                stop();
	            }
			};
			ObjectHandler.getInstance().addObject(new GameObject(2080, 3218, 3396, 1, 1, 10, 2079, 4), true);
			player.getActionSender().sendMessage("You unlock the chest.");
			player.getActionSender().sendMessage("All that's in the chest is a message...");
			player.questStage[this.getId()] = 9;
			World.getTickManager().submit(timer1);
			return true;
		}
		if(itemId == QuestConstants.BANANA && objectId == 2072 && player.getInventory().playerHasItem(QuestConstants.BANANA)){
			if(player.bananaCrate < 10){
				player.bananaCrate += 1;
				player.getInventory().removeItem(new Item(QuestConstants.BANANA, 1));
				if(player.bananaCrate < 10)
					player.getDialogue().sendStatement("You pack a banana into the crate.");
				else
					player.getDialogue().sendStatement("You pack a banana into the crate.",
														"The crate is now full.");
				player.resetAllActions();
				player.getDialogue().endDialogue();
			}
			else{
				player.getDialogue().sendStatement("The crate is already full.");
				player.resetAllActions();
				player.getDialogue().endDialogue();
			}
			return true;
		}
		if(itemId == QuestConstants.KARAMJAN_RUM && objectId == 2072 && player.getInventory().playerHasItem(QuestConstants.KARAMJAN_RUM)){
			if(stage == 2){
				player.getInventory().removeItem(new Item(QuestConstants.KARAMJAN_RUM, 1));
				player.getDialogue().sendStatement("You stash the rum in the crate.");
				player.questStage[this.getId()] = 3;
				player.resetAllActions();
				player.getDialogue().endDialogue();
				return true;
			}
			if(stage == 3){
				player.getDialogue().sendStatement("You have already stashed a bottle of rum there.");
				player.resetAllActions();
				player.getDialogue().endDialogue();
				return true;
			}
		}
		return false;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		//player.questStage[this.getId()] = 4;
		if(npcId == QuestConstants.REDBEARD_FRANK){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Arr, Matey!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("I'm in search of treasure.",
													"Arr!",
													"Do you have anything for trade?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I'm in search of treasure.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Arr, treasure you be after eh? Well I might be able to",
													"tell you where to find some... For a price...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("What sort of price?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Well for example if you can get me a bottle of rum...",
													"Not just any rum mind...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("I'd like some rum made on Karamja Island. There's no",
													"rum like Karamja Rum!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendOption("Ok, I will bring you some rum",
													"Not right now");
					return true;
				}
				if(chatId == 9){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Ok, I will bring you some rum.", Dialogues.CONTENT);
						questStarted(player);
						player.getDialogue().setNextChatId(10);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}//end of stage 0
			if(stage >= 2){
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Yer a saint, although it'll take a miracle to get it off",
													"Karamja.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendPlayerChat("What do you mean?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("The Customs office has been clampin' down on the",
													"export of spirits. You seem like a resourceful young",
													"lass, I'm sure ye'll be able to find a way to slip the stuff",
													"past them.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendPlayerChat("Well I'll give it a shot.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("Arr, that's the spirit!",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 1){
					if(player.questStage[this.getId()] >= 8 && !player.hasItem(QuestConstants.HECTORS_KEY) && !player.hasItem(QuestConstants.PIRATE_MESSAGE)){
						player.getInventory().addItemOrDrop(new Item(QuestConstants.HECTORS_KEY, 1));
						player.getDialogue().sendItem1Line("Frank hands you a key", new Item(QuestConstants.HECTORS_KEY, 1));
						player.getDialogue().setNextChatId(8);
						return true;
					}	
				}
				if(chatId == 1){
					if(player.questStage[this.getId()] < 8){
						player.getDialogue().sendNpcChat("Arr, Matey!",Dialogues.CONTENT);
						return true;
					}
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Have ye brought some rum for yer ol' mate Frank?",Dialogues.CONTENT);
					if(player.getInventory().playerHasItem(QuestConstants.KARAMJAN_RUM))
						player.getDialogue().setNextChatId(3);
					else
						player.getDialogue().setNextChatId(17);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Yes, I've got some.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Now a deal's a deal, I'll tell ye about the treasure. I",
													"used to serve under a pirate captain called One-Eyed",
													"Hector.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Hector were very succesful and became very rich.",
													"But about a year ago we were boarded by the Customs",
													"and Excise Agents.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Hector were killed along with many of the crew, I were",
													"one of the few to escape and I escaped with this.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					if(player.getInventory().playerHasItem(QuestConstants.KARAMJAN_RUM)){
						player.getInventory().removeItem(new Item(QuestConstants.KARAMJAN_RUM, 1));
						player.getInventory().addItemOrDrop(new Item(QuestConstants.HECTORS_KEY, 1));
						player.questStage[this.getId()] = 8;
						player.getDialogue().sendItem1Line("Frank happily takes the rum... ... and hands you a key", new Item(QuestConstants.HECTORS_KEY, 1));
						return true;
					}
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("This be Hector's key. I believe it opens his chest in his",
													"old room in the Blue Moon Inn in Varrock.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("With any luck his treasure will be in there.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(15);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendOption("Ok thanks, I'll go and get it.",
													"So why didn't you ever get it?");
					return true;
				}
				if(chatId == 16){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Ok thanks, I'll go and get it.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 17){
					player.getDialogue().sendPlayerChat("No, not yet.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendNpcChat("Not surprising, tis no easy task to get it off Karamja.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(11);
					return true;
				}
			}
		}//end of redbeard frank
		if(npcId == QuestConstants.WYDIN){
			if(chatId == 100){
				player.getDialogue().sendNpcChat("Hey, you can't go in there. Only employees of the",
												"grocery store can go in.",Dialogues.CONTENT);
				return true;
			}
			if(chatId == 101){
				player.getDialogue().sendOption("Well, can I get a job here?",
												"Sorry, I didn't realise.");
				return true;
			}
			if(chatId == 102){
				if(optionId == 1){
					player.getDialogue().sendPlayerChat("Can I get a job here?", Dialogues.CONTENT);
					player.getDialogue().setNextChatId(103);
					return true;
				}else{
					player.getDialogue().endDialogue();
					return false;
				}
			}
			if(chatId == 103){
				player.getDialogue().sendNpcChat("Well, you're keen, I'll give you that. Okay, I'll give you",
												"a go. Have you got your own white apron?",Dialogues.CONTENT);
				if(player.getEquipment().getItemContainer().get(Constants.CHEST) == null && !player.getInventory().playerHasItem(QuestConstants.WHITE_APRON)){
					player.getDialogue().setNextChatId(106);
				} else
				if(player.getEquipment().getItemContainer().get(Constants.CHEST) == null && player.getInventory().playerHasItem(QuestConstants.WHITE_APRON)){
					player.getDialogue().setNextChatId(104);
				} else
				if(player.getEquipment().getItemContainer().get(Constants.CHEST).getId() == QuestConstants.WHITE_APRON || player.getInventory().playerHasItem(QuestConstants.WHITE_APRON))
					player.getDialogue().setNextChatId(104);
				else
					player.getDialogue().setNextChatId(106);
				return true;
			}
			if(chatId == 104){
				player.getDialogue().sendPlayerChat("Yes, I have one right here.", Dialogues.CONTENT);
				return true;
			}
			if(chatId == 105){
				player.getDialogue().sendNpcChat("Wow - you are well prepared! You're hired. Go through",
												"to the back and tidy up for me, please.",Dialogues.CONTENT);
				player.questStage[this.getId()] = 6;
				player.getDialogue().endDialogue();
				return true;
			}
			if(chatId == 106){
				player.getDialogue().sendPlayerChat("No, I don't.", Dialogues.CONTENT);
				return true;
			}
			if(chatId == 107){
				player.getDialogue().sendNpcChat("Well, if you find one come back.",Dialogues.CONTENT);
				player.questStage[this.getId()] = 5;
				player.getDialogue().endDialogue();
				return true;
			}
			if(chatId == 108){
				player.getDialogue().sendNpcChat("Have you found the apron yet?",Dialogues.CONTENT);
				if(player.getEquipment().getItemContainer().get(Constants.CHEST) == null && !player.getInventory().playerHasItem(QuestConstants.WHITE_APRON)){
					player.getDialogue().setNextChatId(109);
				} else
				if(player.getEquipment().getItemContainer().get(Constants.CHEST) == null && player.getInventory().playerHasItem(QuestConstants.WHITE_APRON)){
					player.getDialogue().setNextChatId(110);
				} else
				if(player.getEquipment().getItemContainer().get(Constants.CHEST).getId() == QuestConstants.WHITE_APRON || player.getInventory().playerHasItem(QuestConstants.WHITE_APRON))
					player.getDialogue().setNextChatId(110);
				else
					player.getDialogue().setNextChatId(109);
				return true;
			}
			if(chatId == 109){
				player.getDialogue().sendPlayerChat("No, I'm still looking.", Dialogues.CONTENT);
				player.getDialogue().setNextChatId(107);
				return true;
			}
			if(chatId == 110){
				player.getDialogue().sendPlayerChat("Yes, I have.", Dialogues.CONTENT);
				return true;
			}
			if(chatId == 111){
				player.getDialogue().sendNpcChat("Ok, You're hired. Go through to the back and",
												"tidy up for me, please.",Dialogues.CONTENT);
				player.questStage[this.getId()] = 6;
				player.getDialogue().endDialogue();
				return true;
			}
			if(chatId == 112){
				player.getDialogue().sendNpcChat("Hey! Employees need to wear their",
												"white apron!",Dialogues.CONTENT);
				player.getDialogue().endDialogue();
				return true;
			}
		}//end of wydin
		return false;
	}

}
