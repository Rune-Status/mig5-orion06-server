package com.rs2.model.content.questing.quests;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class JunglePotion extends Quest {
	
	final int rewardQP = 1;
	
	public JunglePotion(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to Trufitus Shakaya",
							"who lives in the main hut in Tai Bwo Wannai",
							"village on the island of Karamja."};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should go look for snake weed, which can be found near",
							"the vines south-west of the village."};
			return text;	
		}
		if(stage == 3){
			String text[] = {"I should go look for ardrigal, which can be found from the",
							"peninsula east of the village."};
			return text;	
		}
		if(stage == 4){
			String text[] = {"I should go look for sito foil, which can be found where",
							"ground is blackened by living flame."};
			return text;	
		}
		if(stage == 5){
			String text[] = {"I should go look for volencia moss, which can be found near",
							"the rocks south-east of the village."};
			return text;	
		}
		if(stage == 6){
			String text[] = {"I should go look for rogues purse, which can be found in",
							"caverns located in the northern part of the island."};
			return text;	
		}
		if(stage == 7){
			String text[] = {"I should speak with Trufitus to finish this quest."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "1 Quest Point", "775 Herblore  XP"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("775 Herblore  XP", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.HERBLORE, 775);
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.ARDRIGAL);
		player.getActionSender().sendInterface(12140);
	}

	public boolean getObjectDialog(int clickId, final Player player, int objectId, int chatId, int optionId, int npcChatId, int x, int y, int stage){
		if(objectId == 2585 && x == 2830 && y == 9522 && clickId == 1){
				if(chatId == 1){
					player.getDialogue().sendStatement("You attempt to climb the rocks back out.");
					return true;
				}
				if(chatId == 2){
					player.teleport(new Position(2823,3120,0));
					return false;
				}
		}
		if(objectId == 2584 && x == 2824 && y == 3118 && clickId == 2){
			if(chatId == 1){
				player.getDialogue().sendStatement("You search the rocks... You find an entrance into some caves.");
				return true;
			}
			if(chatId == 2){
				player.getDialogue().sendOptionWTitle("Would you like to enter the caves?",
													"Yes, I'll enter the cave.",
													"No thanks, I'll give it a miss.");
				return true;
			}
			if(chatId == 3){
				if(optionId == 1){
					player.getDialogue().sendStatement("You decide to enter the caves. You climb down several steep rock",
													"faces into the cavern below.");
					player.getDialogue().setNextChatId(4);
					return true;
				}else{
					player.getDialogue().endDialogue();
					return false;
				}
			}
			if(chatId == 4){
				player.teleport(new Position(2830,9520,0));
				return false;
			}
		}
		return false;
	}
	
	void startSearching(final Player player, final int itemReceived, final int object, final int obX, final int obY, final boolean replaceObject){
		final int task = player.getTask();
		player.resetAnimation();
		player.getUpdateFlags().sendAnimation(832);
		player.setSkilling(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task)) {
					container.stop();
					return;
				}
				boolean finished = false;
				final GameObject p = ObjectHandler.getInstance().getObject(obX, obY, player.getPosition().getZ());
				if (p != null && p.getDef().getId() != object) {
					container.stop();
					return;
				}
                container.setTick(3);
			    player.getUpdateFlags().sendAnimation(832);
				if (Misc.random_(5) == 0) {
					int newItem = itemReceived;
					player.getInventory().addItem(new Item(newItem, 1));
					player.getDialogue().sendItem1Line("You find a herb.", new Item(newItem, 1));
					if(replaceObject){
						try {
							int face = SkillHandler.getFace(object, obX, obY, player.getPosition().getZ());
							int type = SkillHandler.getType(object, obX, obY, player.getPosition().getZ());
							new GameObject(2576, obX, obY, player.getPosition().getZ(), face, type, object, 10);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					container.stop();
					finished = true;
				}
				if(!finished)
				if (!player.getInventory().canAddItem(new Item(itemReceived, 1))) {
					container.stop();
					return;
				}
			}
			@Override
			public void stop() {
				player.getUpdateFlags().sendAnimation(-1);
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 3);
	}
	
	public boolean handleSecondClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 2575){//vine
			if(stage == 2){
				player.getActionSender().sendMessage("You search the vine...");
				startSearching(player, QuestConstants.SNAKE_WEED_UIDF, objectId, objectX, objectY, true);
				return true;
			}
		}
		if(objectId == 2577){//palm tree
			if(stage == 3){
				player.getActionSender().sendMessage("You search the palm...");
				player.getDialogue().sendItem1Line("You find a herb.", new Item(QuestConstants.ARDRIGAL_UIDF, 1));
				player.getInventory().addItemOrDrop(new Item(QuestConstants.ARDRIGAL_UIDF, 1));
				return true;
			}
		}
		if(objectId == 2579){//scorched earth
			if(stage == 4){
				player.getActionSender().sendMessage("You search the scorched earth...");
				player.getDialogue().sendItem1Line("You find a herb.", new Item(QuestConstants.SITO_FOIL_UIDF, 1));
				player.getInventory().addItemOrDrop(new Item(QuestConstants.SITO_FOIL_UIDF, 1));
				return true;
			}
		}
		if(objectId == 2581){//rock
			if(stage == 5){
				player.getActionSender().sendMessage("You search the rock...");
				player.getDialogue().sendItem1Line("You find a herb.", new Item(QuestConstants.VOLENCIA_MOSS_UIDF, 1));
				player.getInventory().addItemOrDrop(new Item(QuestConstants.VOLENCIA_MOSS_UIDF, 1));
				return true;
			}
		}
		if(objectId == 2583){//fungus wall
			if(stage == 6){
				player.getActionSender().sendMessage("You search the wall...");
				startSearching(player, QuestConstants.ROGUES_PURSE_UIDF, objectId, objectX, objectY, true);
				return true;
			}
		}
		return false;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.TRUFITUS){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Greetings Bwana! I am Trufitus Shakaya of the Tai",
													"Bwo Wannai village.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Welcome to our humble village.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendOption("What does Bwana mean?",
													"Tai Bwo Wannai? What does that mean?",
													"It's a nice village, where is everyone?");
					return true;
				}
				if(chatId == 4){
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("It's a nice village, where is everyone?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("My people are afraid to stay in the village. They have",
													"returned to the jungle. I need to commune with the",
													"gods to see what fate befalls us.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("You may be able to help with this.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendOption("Me? How can I help?",
													"I am sorry, but I am very busy.");
					return true;
				}
				if(chatId == 8){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Me? How can I help?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("I need to make a special brew! A potion that helps me",
													"to commune with the gods. For this potion, I need very",
													"special herbs, that are only found in the deep jungle.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("I can only guide you so far as the herbs are not easy",
													"to find. With some luck, you will find each herb in turn",
													"and bring it to me. I will then give you details of where",
													"to find the next herb.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("In return for this great favour I will give you training",
													"in Herblore.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendOption("Hmmm, sounds difficult, I don't know if I am ready for the challenge.",
													"It sounds like just the challenge for me.");
					return true;
				}
				if(chatId == 13){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("It sounds like just the challenge for me. And it would",
															"make a nice break from killing things!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(14);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("That is excellent Bwana! The first herb that you need",
													"to gather is called",Dialogues.CONTENT);
					questStarted(player);
					return true;
				}
			}//end of stage 0
			if(stage == 2){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hello Bwana, do you have the Snake Weed?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Of course!",
													"Not yet, sorry, what's the clue again?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Of course!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Not yet, sorry, what's the clue again?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(16);
						return true;
					}
				}
				if(chatId == 4){
					if(player.getInventory().playerHasItem(QuestConstants.SNAKE_WEED, 1)){
						player.getInventory().removeItem(new Item(QuestConstants.SNAKE_WEED, 1));
						player.getDialogue().sendItem1Line("You give the Snake Weed to Trufitus.", new Item(QuestConstants.SNAKE_WEED, 1));
						player.questStage[this.getId()] = 3;
						return true;
					}
				}
				if(chatId == 15){
					player.getDialogue().sendNpcChat("Snake Weed.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("It grows near vines in an area to the south west where",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendNpcChat("the ground turns soft and the water kisses your feet.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 2
			if(stage == 3){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hello Bwana, have you been able to get the Ardrigal?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Of course!",
													"Not yet, sorry, what's the clue again?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Of course!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Not yet, sorry, what's the clue again?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(6);
						return true;
					}
				}
				if(chatId == 4){
					if(player.getInventory().playerHasItem(QuestConstants.ARDRIGAL, 1)){
						player.getInventory().removeItem(new Item(QuestConstants.ARDRIGAL, 1));
						player.getDialogue().sendItem1Line("You give the Ardrigal to Trufitus.", new Item(QuestConstants.ARDRIGAL, 1));
						player.questStage[this.getId()] = 4;
						return true;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Great, you have the Snake Weed! Many thanks. Ok,",
													"the next herb is called Ardrigal. It is related to the palm",
													"and grows to the east in its brother's shady profusion.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("To the east you will find a small peninsula, it is just",
													"after the cliffs come down to meet the sands, here is",
													"where you should search for it.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 3
			if(stage == 4){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hello Bwana, have you been successful in getting",
													"the Sito Foil?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Of course!",
													"Not yet, sorry, what's the clue again?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Of course!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Not yet, sorry, what's the clue again?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(6);
						return true;
					}
				}
				if(chatId == 4){
					if(player.getInventory().playerHasItem(QuestConstants.SITO_FOIL, 1)){
						player.getInventory().removeItem(new Item(QuestConstants.SITO_FOIL, 1));
						player.getDialogue().sendItem1Line("You give the Sito Foil to Trufitus.", new Item(QuestConstants.SITO_FOIL, 1));
						player.questStage[this.getId()] = 5;
						return true;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Great, you have the Ardrigal! Many thanks.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("You are doing well Bwana. The next herb is called Sito",
													"Foil, and it grows best where the ground has been",
													"blackened by the living flame.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 4
			if(stage == 5){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Greetings Bwana, have you been successful in getting",
													"the Volencia Moss?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Of course!",
													"Not yet, sorry, what's the clue again?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Of course!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Not yet, sorry, what's the clue again?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(6);
						return true;
					}
				}
				if(chatId == 4){
					if(player.getInventory().playerHasItem(QuestConstants.VOLENCIA_MOSS, 1)){
						player.getInventory().removeItem(new Item(QuestConstants.VOLENCIA_MOSS, 1));
						player.getDialogue().sendItem1Line("You give the Volencia Moss to Trufitus.", new Item(QuestConstants.VOLENCIA_MOSS, 1));
						player.questStage[this.getId()] = 6;
						return true;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Well done Bwana, just two more herbs to collect.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("The next herb is called Volencia Moss. It clings to",
													"rocks for its existence. It is difficult to see, so you",
													"must search for it well.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("It prefers rocks of high metal content and a frequently",
													"disturbed environment. There is some, I believe to the",
													"south east of this village.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 5
			if(stage == 6){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Greetings Bwana, have you been successful in getting",
													"the Rogues Purse?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Of course!",
													"Not yet, sorry, what's the clue again?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Of course!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Not yet, sorry, what's the clue again?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(6);
						return true;
					}
				}
				if(chatId == 4){
					if(player.getInventory().playerHasItem(QuestConstants.ROGUES_PURSE, 1)){
						player.getInventory().removeItem(new Item(QuestConstants.ROGUES_PURSE, 1));
						player.getDialogue().sendItem1Line("You give the Rogues Purse to Trufitus.", new Item(QuestConstants.ROGUES_PURSE, 1));
						player.questStage[this.getId()] = 7;
						player.getDialogue().setNextChatId(1);
						return true;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Ah Volencia Moss, beautiful. One final herb and the",
													"potion will be complete.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("This is the most difficult to find as it inhabits the",
													"darkness of the underground. It is called Rogues",
													"Purse, and is only to be found in caverns",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("in the northern part of this island. A secret entrance to",
													"the caverns is set into the northern cliffs of this land.",
													"Take care Bwana as it may be dangerous.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 6
			if(stage == 7){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Most excellent Bwana! You have returned all the herbs",
													"to me and, I can finish the preparations for the potion,",
													"and at last divine with the gods.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Many blessings on you! I must now prepare, please",
													"excuse me while I make the arrangements.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendStatement("Trufitus shows you some techniques in Herblore. You gain some",
														"experience in Herblore.");
					return true;
				}
				if(chatId == 4){
					questCompleted(player);
					player.getDialogue().dontCloseInterface();
					return true;
				}
			}//end of stage 7
		}//end of npc 740
		return false;
	}

}
