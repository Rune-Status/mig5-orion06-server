package com.rs2.model.content.questing.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.rs2.model.Position;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.questing.SpecialEvent;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class Hween14 extends SpecialEvent{
	
	public Hween14(int id, int type) {
		super(id, type);
	}
	
	public void initialize(){
		System.out.println("Hween 14 event started! (1st hween event) [CUSTOM]");
		spawnNpc(QuestConstants.BETTY_HWEEN14, new Position(3098,3369,0), 1);
	}

	public void eventCompleted(final Player player) {
		player.holidayEventStage[this.getId()] = 1;
		player.getActionSender().sendMessage("Congratulations! H'ween event Complete!");
		player.getInventory().addItemOrDrop(new Item(QuestConstants.NOTED_FINE_CLOTH, 5));
		player.getInventory().addItemOrDrop(new Item(QuestConstants.XP_LAMP, 3));
		player.getInventory().addItemOrDrop(new Item(Misc.random_(2) == 0 ? QuestConstants.BONE_SPEAR : QuestConstants.BONE_CLUB, 1));
	}
	
	private List<Integer> items = Arrays.asList(526, 592, 221, 1939);

	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		List<Integer> itemToGet_ = new ArrayList<Integer>(items);
		Collections.shuffle(itemToGet_, new Random(player.uniqueRandomInt));
		Item itemToGet = new Item(itemToGet_.get(0), 1);
		if(npcId == QuestConstants.BETTY_HWEEN14){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("NOW WHAT?!",Dialogues.ANGRY_1);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("What's wrong?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("I was going to cook, but noticed I was missing",
													"an ingredient. What do you care anyway?",Dialogues.ANGRY_1);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("Maybe I could go and get it for you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("You...?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendStatement("The witch thinks.");
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Yes, why not. I would need you to bring me",
													itemToGet.getDefinition().getName()+", You think you can do that?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendOption("Yes, I will be back soon.",
													"Maybe later.");
					return true;
				}
				if(chatId == 9){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yes, I will be back soon.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						eventStarted(player);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}//end of stage 0
			if(stage == 2){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I'm back!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Did you bring the "+itemToGet.getDefinition().getName()+"?",Dialogues.CONTENT);
					if(player.getInventory().playerHasItem(itemToGet.getId()))
						player.getDialogue().setNextChatId(5);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Actually... I forgot.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Of course you did.",
													"*sigh*",
													"Well, maybe you should try again.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("Yes, I have it right here!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Good, give it to me now.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					if(player.getInventory().playerHasItem(itemToGet.getId())){
						player.holidayEventStage[this.getId()] = 3;
						player.getInventory().removeItem(new Item(itemToGet.getId(), 1));
						player.getDialogue().sendStatement("The witch sighs.");
						return true;
					}
				}
			}//end of stage 2
			if(stage == 3){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Have you visited Hetty yet?", Dialogues.CONTENT);
					if(player.getInventory().playerHasItem(QuestConstants.ROTTEN_FOOD))
						player.getDialogue().setNextChatId(4);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("No, not yet.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Remember she is living in Rimmington.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();	
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("Yes, she gave me this.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendStatement("The witch takes a look.");
					return true;
				}
				if(chatId == 6){
					if(player.getInventory().playerHasItem(QuestConstants.ROTTEN_FOOD)){
						player.holidayEventStage[this.getId()] = 4;
						player.getInventory().removeItem(new Item(QuestConstants.ROTTEN_FOOD, 1));
						player.getDialogue().sendNpcChat("Yes, this is just what I needed.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(1);
						return true;
					}
				}
				if(chatId == 8){
					player.getDialogue().sendPlayerChat("What now?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("I just remembered I need one more thing.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("My sister Hetty should be able to help,",
													"she lives in Rimmington. You have probably",
													"already met her.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendPlayerChat("I'll be on my way.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 3
			if(stage == 4){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Please, take these for all your troubles.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().endDialogue();
					eventCompleted(player);
					return false;
				}
			}
		}//end of betty
		if(npcId == QuestConstants.HETTY){
			if(stage == 3){
				if(chatId == 1){
					if(player.hasItem(QuestConstants.ROTTEN_FOOD))
						return false;
					player.getDialogue().sendPlayerChat("Hey, by any chance do you have a sister called",
														"Betty?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("As a matter of fact I do, Why do you ask?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("She needs some ingredient, and said",
														"you should be able to help.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Did she say what she needs exactly?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("Now that I think of it, she forgot to do so.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("No worries, every now and then she is",
													"asking me for this specific stuff... So",
													"that's probably what she wants now too.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendStatement("The witch looks around a bit.");
					return true;
				}
				if(chatId == 8){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.ROTTEN_FOOD, 1));
					player.getDialogue().sendNpcChat("Ahh! This should do it.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendPlayerChat("Oh, but that smells horrible...", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("You will get used to it after a while.", Dialogues.CONTENT);
					player.getDialogue().endDialogue();	
					return true;
				}
			}
		}
		return false;
	}
	
}
