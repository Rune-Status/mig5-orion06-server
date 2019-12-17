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

public class WayOfTheNecromancer extends Quest {
	
	final int rewardQP = 1;
	
	public WayOfTheNecromancer(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			boolean b = false;
			if(player.questStage[18] == 1 && player.questStage[103] == 1)
				b = true;
			if(b){
				String text[] = {"I can start this quest by speaking to Hetty in her house in",
							"Rimmington, West of Port Sarim",
							"as all reguired quests are complete."};
				return text;
			}else{
				String text[] = {"I can start this quest by speaking to Hetty in her house in",
						"Rimmington, West of Port Sarim",
						"To start this quest I need:",
						(player.questStage[18] == 1 ? "@str@" : "")+"Complete Witch's Potion",
						(player.questStage[103] == 1 ? "@str@" : "")+"Complete Witch's House"};
				return text;
			}
		}
		if(stage == 2){
			String text[] = {"I should bring these items to Hetty at her house in",
							"Rimmington, West of Port Sarim:",
							"Bones",
							"Big bones",
							"Bat bones",
							"Burnt bones"};
			return text;	
		}
		if(stage == 3){
			String text[] = {"I should drink from the cauldron at Hetty's house to",
							"finish this quest."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "1 Quest Point", "500 Magic XP", "500 Prayer XP", "Book of the dead", "Ensouled goblin head", "Nature rune", "2 Body runes"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("500 Magic and Prayer XP", 12151);
		player.getActionSender().sendString("Book of the dead", 12152);
		player.getActionSender().sendString("Ensouled goblin head", 12153);
		player.getActionSender().sendString("Nature rune", 12154);
		player.getActionSender().sendString("2 Body runes", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.MAGIC, 500);
		player.getSkill().addQuestExp(Skill.PRAYER, 500);
		player.getInventory().addItemOrDrop(new Item(QuestConstants.BOOK_OF_THE_DEAD, 1));
		player.getInventory().addItemOrDrop(new Item(QuestConstants.ENSOULED_GOBLIN_HEAD, 1));
		player.getInventory().addItemOrDrop(new Item(QuestConstants.NATURE_RUNE, 1));
		player.getInventory().addItemOrDrop(new Item(QuestConstants.BODY_RUNE, 2));
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.ENSOULED_GOBLIN_HEAD);
		player.getActionSender().sendInterface(12140);
	}
	
	boolean playerHasAtleastOneItem(final Player player){
		if(!player.getInventory().playerHasItem(QuestConstants.BONES) && !player.getInventory().playerHasItem(QuestConstants.BIG_BONES) &&
				!player.getInventory().playerHasItem(QuestConstants.BAT_BONES) && !player.getInventory().playerHasItem(QuestConstants.BURNT_BONES)){
			return false;
		}
		return true;
	}
	
	boolean playerHasAllItems(final Player player){
		if(player.getInventory().playerHasItem(QuestConstants.BONES) && player.getInventory().playerHasItem(QuestConstants.BIG_BONES) &&
				player.getInventory().playerHasItem(QuestConstants.BAT_BONES) && player.getInventory().playerHasItem(QuestConstants.BURNT_BONES)){
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
	
	boolean meetsRequirements(final Player player){
		if(player.questStage[18] != 1 || player.questStage[103] != 1)
			return false;
		return true;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.HETTY){
			if(stage == 1){//quest complete, retrieve book if lost.
				if(!player.hasItem(QuestConstants.BOOK_OF_THE_DEAD)){
					if(chatId == 1){
						player.getDialogue().sendNpcChat("Try not to lose the book this time.",Dialogues.CONTENT);
						player.getInventory().addItemOrDrop(new Item(QuestConstants.BOOK_OF_THE_DEAD, 1));
						player.getDialogue().endDialogue();
						return true;
					}
				}
			}
			if(stage == 0){
				if(!meetsRequirements(player))
					return false;
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Could you help me improve my magic further?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Hmmm... Maybe I can think of something for you.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Would you like to learn some dark magic?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendOption("Sure thing.", "Maybe some other time.");
					return true;
				}
				if(chatId == 5){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Sure thing.", Dialogues.CONTENT);
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
					player.getDialogue().sendNpcChat("Ok I'm going to make a potion to help bring out your",
													"darker self, so you can read the book of dark magic.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("You will need certain ingredients.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendPlayerChat("What do I need?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("You need bones, big bones, bat bones... Oh",
													"and also burnt bones.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendPlayerChat("Great, I'll go and get them.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
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
						player.getInventory().removeItem(new Item(QuestConstants.BONES, 1));
						player.getInventory().removeItem(new Item(QuestConstants.BIG_BONES, 1));
						player.getInventory().removeItem(new Item(QuestConstants.BAT_BONES, 1));
						player.getInventory().removeItem(new Item(QuestConstants.BURNT_BONES, 1));
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
