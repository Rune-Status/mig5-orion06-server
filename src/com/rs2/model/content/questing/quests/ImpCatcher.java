package com.rs2.model.content.questing.quests;

import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class ImpCatcher extends Quest {
	
	final int rewardQP = 1;
	
	public ImpCatcher(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to Wizard Mizgog who is",
							"in the Wizards' Tower.",
							"",
							"There aren't any requirements for this quest."};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should bring these items to Wizard Mizgog who is",
							"in the Wizards' Tower:",
							"White bead",
							"Red bead",
							"Black bead",
							"Yellow bead"};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "1 Quest Point", "875 Magic XP", "Amulet of Accuracy"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("875 Magic XP", 12151);
		player.getActionSender().sendString("Amulet of Accuracy", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.MAGIC, 875);
		player.getInventory().addItemOrDrop(new Item(QuestConstants.AMULET_OF_ACCURACY, 1));
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.AMULET_OF_ACCURACY);
		player.getActionSender().sendInterface(12140);
	}
	
	boolean playerHasAtleastOneItem(final Player player){
		if(!player.getInventory().playerHasItem(QuestConstants.WHITE_BEAD) && !player.getInventory().playerHasItem(QuestConstants.RED_BEAD) &&
				!player.getInventory().playerHasItem(QuestConstants.BLACK_BEAD) && !player.getInventory().playerHasItem(QuestConstants.YELLOW_BEAD)){
			return false;
		}
		return true;
	}
	
	boolean playerHasAllItems(final Player player){
		if(player.getInventory().playerHasItem(QuestConstants.WHITE_BEAD) && player.getInventory().playerHasItem(QuestConstants.RED_BEAD) &&
				player.getInventory().playerHasItem(QuestConstants.BLACK_BEAD) && player.getInventory().playerHasItem(QuestConstants.YELLOW_BEAD)){
			return true;
		}
		return false;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.WIZARD_MIZGOG){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Give me a quest!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Give me a quest what?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendOption("Give me a quest please.",
													"Give me a quest or else!",
													"Just stop messing around and give me a quest!");
					return true;
				}
				if(chatId == 4){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Give me a quest please.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(5);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Well seeing as you asked nicely... I could do with some",
													"help.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("The wizard Grayzag next door decided he didn't like",
													"me so he enlisted an army of hundreds of imps.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("These imps stole all sorts of my things. Most of these",
													"things I don't really care about, just eggs and balls of",
													"string and things.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("But they stole my four magical beads. There was a red",
													"one, a yellow one, a black one, and a white one.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("These imps have now spread out all over the kingdom.",
													"Could you get my beads back for me?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendOption("I'll try.",
													"I've better things to do than chase imps.");
					return true;
				}
				if(chatId == 11){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("I'll try.", Dialogues.CONTENT);
						questStarted(player);
						player.getDialogue().setNextChatId(12);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}//end of stage 0
			if(stage == 2){
				if(chatId == 12){
					player.getDialogue().sendNpcChat("That's great, thank you.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 1){
					player.getDialogue().sendNpcChat("So how are you doing finding my beads?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					if(!playerHasAtleastOneItem(player)){
						player.getDialogue().sendPlayerChat("I have none of them yet.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
					}
					if(playerHasAtleastOneItem(player) && !playerHasAllItems(player)){
						player.getDialogue().sendPlayerChat("I have found some of them.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(3);
					}
					if(playerHasAllItems(player)){
						player.getDialogue().sendPlayerChat("I've got all four beads. It was hard work I can tell you.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
					}
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Great, but I'll need the other ones as well.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Give them here and I'll check that they really are MY",
													"beads, before I give you your reward. You'll like it, it's",
													"an amulet of accuracy.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendStatement("You give four coloured beads to Wizard Mizgog.");
					return true;
				}
				if(chatId == 6){
					if(playerHasAllItems(player)){
						player.getInventory().removeItem(new Item(QuestConstants.WHITE_BEAD, 1));
						player.getInventory().removeItem(new Item(QuestConstants.RED_BEAD, 1));
						player.getInventory().removeItem(new Item(QuestConstants.BLACK_BEAD, 1));
						player.getInventory().removeItem(new Item(QuestConstants.YELLOW_BEAD, 1));
						questCompleted(player);
						player.getDialogue().endDialogue();
						return true;
					}
				}
			}
		}//end of npc 706
		return false;
	}

}
