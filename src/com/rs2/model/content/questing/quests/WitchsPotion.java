package com.rs2.model.content.questing.quests;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class WitchsPotion extends Quest {
	
	final int rewardQP = 1;
	
	public WitchsPotion(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to Hetty in her house in",
							"Rimmington, West of Port Sarim"};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should bring these items to Hetty at her house in",
							"Rimmington, West of Port Sarim:",
							"Eye of newt",
							"Rat's tail",
							"Onion",
							"Burnt meat"};
			return text;	
		}
		if(stage == 3){
			String text[] = {"I should drink from the cauldron at Hetty's house to",
							"finish this quest."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "1 Quest Point", "325 Magic XP"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("325 Magic XP", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.MAGIC, 325);
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.EYE_OF_NEWT);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean getQuestDrop(final Player killer, int npcId, Position deathPos, int stage){
		if(npcId == QuestConstants.RAT_LVL1){
			if(stage == 2){
				GroundItem drop = new GroundItem(new Item(QuestConstants.RATS_TAIL, 1), killer, deathPos);
		        GroundItemManager.getManager().dropItem(drop);
		        return true;
			}
		}
		return false;
	}
	
	boolean playerHasAtleastOneItem(final Player player){
		if(!player.getInventory().playerHasItem(QuestConstants.EYE_OF_NEWT) && !player.getInventory().playerHasItem(QuestConstants.RATS_TAIL) &&
				!player.getInventory().playerHasItem(QuestConstants.ONION) && !player.getInventory().playerHasItem(QuestConstants.BURNT_MEAT)){
			return false;
		}
		return true;
	}
	
	boolean playerHasAllItems(final Player player){
		if(player.getInventory().playerHasItem(QuestConstants.EYE_OF_NEWT) && player.getInventory().playerHasItem(QuestConstants.RATS_TAIL) &&
				player.getInventory().playerHasItem(QuestConstants.ONION) && player.getInventory().playerHasItem(QuestConstants.BURNT_MEAT)){
			return true;
		}
		return false;
	}
	
	public boolean getObjectDialog(int clickId, final Player player, int objectId, int chatId, int optionId, int npcChatId, int x, int y, int stage){
		if(objectId == 2024 && x == 2967 && y == 3205 && clickId == 1){
			if(stage == 3){
				if(chatId == 1){
					player.getDialogue().sendStatement("You drink from the cauldron, it tastes horrible! You feel yourself",
						"imbued with power.");
					return true;
				}
				if(chatId == 2){
					questCompleted(player);
					player.getDialogue().dontCloseInterface();
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.HETTY){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("What could you want with an old woman like me?",Dialogues.HAPPY);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("I am in search of a quest.", "I've heard that you are a witch.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I am in search of a quest.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Hmmm... Maybe I can think of something for you.",Dialogues.HAPPY);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Would you like to become more proficient in the dark",
													"arts?",Dialogues.CALM_CONTINUED);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendOption("Yes help me become one with my darker side.",
													"No I have my principles and honour.",
													"What, you mean improve my magic?");
					return true;
				}
				if(chatId == 7){
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("What, you mean improve my magic?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(8);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 8){
					player.getDialogue().sendStatement("The witch sighs.");
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Yes, improve your magic...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Do you have no sense of drama?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendOption("Yes I'd like to improve my magic.",
													"No I'm not interested.",
													"Show me the mysteries of the dark arts...");
					return true;
				}
				if(chatId == 12){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yes I'd like to improve my magic.", Dialogues.CONTENT);
						questStarted(player);
						player.getDialogue().setNextChatId(10);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}//end of stage 0
			if(stage == 2){
				if(chatId == 10){
					player.getDialogue().sendStatement("The witch sighs.");
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("Ok I'm going to make a potion to help bring out your",
													"darker self.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("You will need certain ingredients.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendPlayerChat("What do I need?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("You need an eye of newt, a rat's tail, an onion... Oh",
													"and a piece of burnt meat.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendPlayerChat("Great, I'll go and get them.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 1){
					player.getDialogue().sendNpcChat("So have you found the things for the potion?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					if(!playerHasAtleastOneItem(player)){
						player.getDialogue().sendPlayerChat("No, I have none of them yet.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					}
					if(playerHasAtleastOneItem(player) && !playerHasAllItems(player)){
						player.getDialogue().sendPlayerChat("I have found some of the things you asked for.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(3);
					}
					if(playerHasAllItems(player)){
						player.getDialogue().sendPlayerChat("Yes I have everything!", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
					}
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Great, but I'll need the other ingredients as well.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Excellent, can I have them then?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendStatement("You pass the ingredients to Hetty and she puts them all into her",
														"cauldron. Hetty closes her eyes and begins to chant. The cauldron",
														"bubbles mysteriously.");
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("Well, is it ready?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					if(playerHasAllItems(player)){
						player.getInventory().removeItem(new Item(QuestConstants.EYE_OF_NEWT, 1));
						player.getInventory().removeItem(new Item(QuestConstants.RATS_TAIL, 1));
						player.getInventory().removeItem(new Item(QuestConstants.ONION, 1));
						player.getInventory().removeItem(new Item(QuestConstants.BURNT_MEAT, 1));
						player.getDialogue().sendNpcChat("Ok, now drink from the cauldron.",Dialogues.CONTENT);
						player.questStage[this.getId()] = 3;
						player.getDialogue().endDialogue();
						return true;
					}
				}
			}
		}//end of npc 307
		return false;
	}

}
