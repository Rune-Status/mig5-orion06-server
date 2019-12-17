package com.rs2.model.content.questing.quests;

import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class TheKnightsSword extends Quest {
	
	final int rewardQP = 1;
	
	public TheKnightsSword(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to the Squire in the",
							"courtyard of the White Knights' Castle in southern Falador",
							"To complete this quest I need:",
							"Level 10 Mining",
							"and to be unafraid of Level 57 Ice Warriors."};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should go speak with Reldo in the library of the",
							"Palace of Varrock."};
			return text;	
		}
		if(stage == 3){
			String text[] = {"Reldo couldn't give me much information about the",
							"Imcando except a few live on the southern peninsula of",
							"Asgarnia, they dislike strangers, and LOVE redberry pies."};
			return text;	
		}
		if(stage == 4){
			String text[] = {"I gave Thurgo a redberry pie, I should now ask him",
							"to make me the sword."};
			return text;	
		}
		if(stage == 5){
			String text[] = {"Thurgo needs a picture of the sword before he can help.",
							"I should probably ask the Squire about obtaining one"};
			return text;	
		}
		if(stage == 6){
			String text[] = {"Squire told me there might be a picture of the sword",
							"in the cupboard in Sir Vyvin's room."};
			return text;	
		}
		if(stage == 7){
			String text[] = {"I found a picture of the sword. I should now take",
							"the picture to Thurgo."};
			return text;	
		}
		if(stage == 8){
			String text[] = {"According to Thurgo to make a replica sword he will need",
							"two Iron Bars and some Blurite Ore. Blurite Ore can only be",
							"found deep in the caves below Thurgo's house"};
			return text;	
		}
		if(stage == 9){
			String text[] = {"I should now bring the sword back to the squire."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "1 Quest Point", "12,725 Smithing XP"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("12,725 Smithing XP", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.SMITHING, 12725);
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.BLURITE_SWORD);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 2271 && objectX == 2984 && objectY == 3336){//Sir vyvin cupboard
			ObjectHandler.getInstance().removeObject(2984, 3336, 2, 0);
			ObjectHandler.getInstance().addObject(new GameObject(2272, 2984, 3336, 2, 1, 10, 2271, 999999999), true);
			return true;
		}
		return false;
	}
	
	public boolean getObjectDialog(int clickId, final Player player, int objectId, int chatId, int optionId, int npcChatId, int x, int y, int stage){
		if(objectId == 2272 && x == 2984 && y == 3336 && clickId == 1){//Sir vyvin cupboard
			if(stage >= 6 && stage < 8 && !player.hasItem(QuestConstants.PORTRAIT)){
				Npc npc = Npc.getNpcById(QuestConstants.SIR_VYVIN);
				boolean playerCantBeSeenByVyvin = false;
				if(npc.getInteractingEntity() != null){
					if(npc.getInteractingEntity() != player)
						playerCantBeSeenByVyvin = true;
				}
				if(!Misc.checkClip(player.getPosition(), npc.getPosition(), false))
					playerCantBeSeenByVyvin = true;
				if(chatId == 1){
					if(playerCantBeSeenByVyvin){
						player.getDialogue().sendStatement("You find a small portrait in here which you take.");
						return true;
					}else{
						player.getDialogue().setLastNpcTalk(QuestConstants.SIR_VYVIN);
						player.getDialogue().sendNpcChat("HEY! Just WHAT do you THINK you are",
														"DOING??? STAY OUT of MY cupboard!", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					}
				}
				if(chatId == 2){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.PORTRAIT, 1));
					player.questStage[this.getId()] = 7;
					player.getActionSender().removeInterfaces();
					player.getDialogue().endDialogue();
				}
				return true;
			}
		}
		return false;
	}
	
	boolean playerHasAllItems(final Player player){
		if(player.getInventory().playerHasItem(QuestConstants.BLURITE_ORE, 1) && player.getInventory().playerHasItem(QuestConstants.IRON_BAR, 2)){
			return true;
		}
		return false;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.SQUIRE){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hello. I am the squire to Sir Vyvin.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("And how is life as a squire?",
													"Wouldn't you prefer to be a squire for me?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("And how is life as a squire?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Well, Sir Vyvin is a good guy to work for, however,",
													"I'm in a spot of trouble today. I've gone and lost Sir",
													"Vyvin's sword!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendOption("Do you know where you lost it?",
													"I can make a new sword if you like...",
													"Is he angry?");
					return true;
				}
				if(chatId == 6){
					if (optionId == 1){
						player.getDialogue().endDialogue();
						return false;
					}
					if (optionId == 2){
						player.getDialogue().sendPlayerChat("I can make a new sword if you like...", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(10);
						return true;
					}
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("Is he angry?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(7);
						return true;
					}
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("He doesn't know yet. I was hoping I could think of",
													"something to do before he does find out, But I find",
													"myself at a loss.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendOption("Well, do you know the VAGUE AREA you lost it?",
													"I can make a new sword if you like...",
													"Well, the kingdom is fairly abundant with swords...",
													"Well, I hope you find it soon.");
					return true;
				}
				if(chatId == 9){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("I can make a new sword if you like...", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(10);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Thanks for the offer. I'd be surprised if you could",
													"though.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("The thing is, this sword is a family heirloom. It has been",
													"passed down through Vyvin's family for five",
													"generations! It was originally made by the Imcando",
													"dwarves, who were",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("a particularly skilled tribe of dwarven smiths. I doubt",
													"anyone could make it in the style they do.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendOption("So would these dwarves make another one?",
													"Well I hope you find it soon.");
					return true;
				}
				if(chatId == 14){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("So would these dwarves make another one?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(15);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 15){
					player.getDialogue().sendNpcChat("I'm not a hundred percent sure the Imcando tribe",
													"exists anymore. I should think Reldo, the palace",
													"librarian in Varrock, will know; he has done a lot of",
													"research on the races of RuneScape.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("I don't suppose you could try and track down the",
													"Imcando dwarves for me? I've got so much work to",
													"do...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendOption("Ok, I'll give it a go.",
													"No, I've got lots of mining work to do.");
					return true;
				}
				if(chatId == 18){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Ok, I'll give it a go.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(19);
						questStarted(player);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}//end of stage 0
			if(stage == 2){
				if(chatId == 19){
					player.getDialogue().sendNpcChat("Thank you very much! As I say, the best place to start",
													"should be with Reldo...",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage == 5){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("So how are you doing getting a sword?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("I've found an Imcando dwarf but he needs a picture of",
														"the sword before he can make it.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("A picture eh? Hmmm.... The only one I can think of is",
													"in a small portrait of Sir Vyvin's father... Sir Vyvin",
													"keeps it in a cupboard in his room I think.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("Ok, I'll try and get that then.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Please don't let him catch you! He MUSTN'T know",
													"what happened!",Dialogues.CONTENT);
					player.questStage[this.getId()] = 6;
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 5
			if(stage == 9){
				if(player.getInventory().playerHasItem(QuestConstants.BLURITE_SWORD)){
					if(chatId == 1){
						player.getDialogue().sendPlayerChat("I have retrieved your sword for you.", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 2){
						player.getDialogue().sendNpcChat("Thank you, thank you, thank you! I was seriously",
														"worried I would have to own up to Sir Vyvin!",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 3){
						player.getDialogue().sendStatement("You give the sword to the squire.");
						return true;
					}
					if(chatId == 4){
						player.getInventory().removeItem(new Item(QuestConstants.BLURITE_SWORD, 1));
						questCompleted(player);
						player.getDialogue().dontCloseInterface();
						return true;
					}
				}
			}
		}//end of Squire
		if(npcId == QuestConstants.RELDO){
			if(stage == 2){
				if(chatId == 1)
					chatId = 100;
				if(chatId == 100){
					player.getDialogue().sendPlayerChat("What do you know about the Imcando dwarves?", Dialogues.CONTENT);
					player.getDialogue().setNextChatId(101);
					return true;
				}
				if(chatId == 101){
					player.getDialogue().sendNpcChat("The Imcando dwarves, you say?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 102){
					player.getDialogue().sendNpcChat("Ah yes... for many hundreds of years they were the",
													"world's most skilled smiths. They used secret smithing",
													"knowledge passed down from generation to generation.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 103){
					player.getDialogue().sendNpcChat("Unfortunately, about a century ago, the once thriving",
													"race was wiped out during the barbarian invasions of",
													"that time.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 104){
					player.getDialogue().sendPlayerChat("So are there any Imcando left at all?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 105){
					player.getDialogue().sendNpcChat("I believe a few of them survived, but with the bulk of",
													"their population destroyed their numbers have dwindled",
													"even further.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 106){
					player.getDialogue().sendNpcChat("I believe I remember a couple living in Asgarnia near",
													"the cliffs on the Asgarnian southern peninsula, but they",
													"DO tend to keep to themselves.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 107){
					player.getDialogue().sendNpcChat("They tend not to tell people that they're the",
													"descendants of the Imcando, which is why people think",
													"that the tribe has died out totally, but you may well",
													"have more luck talking to them if you bring them some",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 108){
					player.getDialogue().sendNpcChat("redberry pie. They REALLY like redberry pie.",Dialogues.CONTENT);
					player.questStage[this.getId()] = 3;
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of reldo
		if(npcId == QuestConstants.THURGO){
			if(stage == 3){
				if(chatId == 1){
					player.getDialogue().sendOption("Hello. Are you an Imcando dwarf?",
													"Would you like some redberry pie?");
					return true;
				}
				if(chatId == 2){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Hello. Are you an Imcando dwarf?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(3);
						return true;
					}
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Would you like some redberry pie?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(6);
						return true;
					}	
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Maybe. Who wants to know?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendOption("Would you like some redberry pie?",
													"Can you make me a special sword?");
					return true;
				}
				if(chatId == 5){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Would you like some redberry pie?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(6);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 6){
					player.getDialogue().sendStatement("You see Thurgo's eyes light up.");
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("I'd never say no to a redberry pie! They're GREAT",
													"stuff!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					if(player.getInventory().playerHasItem(QuestConstants.REDBERRY_PIE, 1)){
						player.getDialogue().sendStatement("You hand over the pie. Thurgo eats the pie. Thurgo pats his",
														"stomach.");
						return true;
					}
				}
				if(chatId == 9){
					if(player.getInventory().playerHasItem(QuestConstants.REDBERRY_PIE, 1)){
						player.getInventory().removeItem(new Item(QuestConstants.REDBERRY_PIE, 1));
						player.getDialogue().sendNpcChat("By Guthix! THAT was good pie! Anyone who makes pie",
														"like THAT has got to be alright!",Dialogues.CONTENT);
						player.questStage[this.getId()] = 4;
						player.getDialogue().endDialogue();
						return true;
					}
				}
			}//end of stage 3
			if(stage == 4){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Can you make me a special sword?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Well, after bringing me my favorite food I guess I",
													"should give it a go. What sort of sword is it?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("I need you to make a sword for one of Falador's",
														"knights. He had one which was passed down through five",
														"generations, but his squire has lost it. So we need an",
														"identical one to replace it.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("A Knight's sword eh? Well I'd need to know exactly",
													"how it looked before I could make a new one.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("All the Faladian knights used to have swords with unique",
													"designs according to their position. Could you bring me",
													"a picture or something?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("I'll go and ask his squire and see if I can find one.", Dialogues.CONTENT);
					player.questStage[this.getId()] = 5;
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 4
			if(stage == 7){
				if(player.getInventory().playerHasItem(QuestConstants.PORTRAIT)){
					if(chatId == 1){
						player.getDialogue().sendPlayerChat("About that sword...", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 2){
						player.getDialogue().sendPlayerChat("I have found a picture of the sword I would like you to",
															"make.", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 3){
						player.getDialogue().sendStatement("You give the portrait to Thurgo. Thurgo studies the portrait.");
						return true;
					}
					if(chatId == 4){
						player.getDialogue().sendNpcChat("Ok. You'll need to get me some stuff in order for me",
														"to make this.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 5){
						player.getDialogue().sendNpcChat("I'll need two iron bars to make the sword to start with.",
														"I'll also need an ore called blurite. It's useless for",
														"making actual weapons for fighting with except",
														"crossbows, but I'll need some as decoration for the hilt.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 6){
						player.getDialogue().sendNpcChat("It is fairly rare sort of ore... The only place I know",
														"where to get it is under this cliff here...",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 7){
						player.getDialogue().sendNpcChat("But it is guarded by a very powerful ice giant.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 8){
						player.getDialogue().sendNpcChat("Most of the rocks in that cliff are pretty useless, and",
														"don't contain much of anything, but there's",
														"DEFINITELY some blurite in there.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 9){
						player.getDialogue().sendNpcChat("You'll need a little bit of mining experience to be able to",
														"find it.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 10){
						player.getDialogue().sendPlayerChat("Ok. I'll go and find them then.", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 11){
						player.getInventory().removeItem(new Item(QuestConstants.PORTRAIT, 1));
						player.questStage[this.getId()] = 8;
						player.getDialogue().endDialogue();
						player.getActionSender().removeInterfaces();
						return true;
					}
				}
			}//end of stage 7
			if(stage >= 8){
				if(stage == 9 && player.hasItem(QuestConstants.BLURITE_SWORD) && chatId != 7)
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("About that sword...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("How are you doing finding those sword materials?",Dialogues.CONTENT);
					if(!playerHasAllItems(player))
						player.getDialogue().setNextChatId(3);
					else
						player.getDialogue().setNextChatId(4);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("I don't have them yet.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("I have them right here.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendStatement("You give the blurite ore and two iron bars to Thurgo. Thurgo starts",
													"to make the sword. Thurgo hands you a sword.");
					return true;
				}
				if(chatId == 6){
					if(playerHasAllItems(player)){
						player.getInventory().removeItem(new Item(QuestConstants.BLURITE_ORE, 1));
						player.getInventory().removeItem(new Item(QuestConstants.IRON_BAR, 2));
						player.getInventory().addItemOrDrop(new Item(QuestConstants.BLURITE_SWORD, 1));
						player.getDialogue().sendPlayerChat("Thank you very much!", Dialogues.CONTENT);
						player.questStage[this.getId()] = 9;
						return true;
					}
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Just remember to call in with more pie some time!",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 8,9
		}//end of Thurgo
		return false;
	}

}
