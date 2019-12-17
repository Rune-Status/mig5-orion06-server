package com.rs2.model.content.questing.quests;

import com.rs2.model.World;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;

public class RomeoAndJuliet extends Quest {
	
	final int rewardQP = 5;
	
	public RomeoAndJuliet(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to Romeo in Varrock",
							"central square by the fountain."};
			return text;
		}
		if(stage == 2){
			String text[] = {"Romeo told me to find Juliet."};
			return text;	
		}
		if(stage == 3){
			String text[] = {"I should bring the message from Juliet to Romeo."};
			return text;	
		}
		if(stage == 4){
			String text[] = {"I should speak to Father Lawrence in the church",
							"located North East of Varrock."};
			return text;	
		}
		if(stage == 5){
			String text[] = {"Father Lawrence told me to go to the Apothecary",
							"and ask for Cadava potion."};
			return text;	
		}
		if(stage == 6){
			String text[] = {"I should find and bring Cadava berries to Apothecary."};
			return text;	
		}
		if(stage == 7){
			String text[] = {"I should bring the Cadava potion to Juliet."};
			return text;	
		}
		if(stage == 8){
			String text[] = {"I should go tell Romeo that he can find Juliet from",
							"the crypt."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "5 Quest Points"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("5 Quest Points", 12150);
		player.getActionSender().sendString("", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.CADAVA_POTION);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		//player.questStage[this.getId()] = 8;
		if(npcId == QuestConstants.ROMEO){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Oh woe is me that I cannot find my Juliet! You haven't",
													"seen Juliet have you?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("Yes, I have seen her actually!",
													"No, sorry. I haven't seen her.",
													"Perhaps I could help to find her for you?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("Perhaps I can help find her for you? What does she",
															"look like?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Oh would you? That would be great! She has this sort",
													"of hair...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("Hair...check..", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("...and she these...great lips...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("Lips...right.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Oh and she has these lovely shoulders as well..",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendPlayerChat("Shoulders...right, so she has hair, lips and shoulders...that",
														"should cut it down a bit.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Oh yes, Juliet is very different...please tell her that she",
													"is the love of my long and that I life to be with her?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendPlayerChat("What?",
														"Surely you mean that 'she is the love of your life and",
														"that you long to be with her?'", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("Oh yeah...what you said...tell her that, it sounds much",
													"better!",
													"Oh you're so good at this!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendOption("Yes, ok, I'll let her know.",
													"Sorry Romeo, I've got better things to do right now but maybe later?");
					return true;
				}
				if(chatId == 14){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Yes, ok, I'll let her know.", Dialogues.CONTENT);
						questStarted(player);
						player.getDialogue().setNextChatId(15);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}//end of stage 0
			if(stage == 2){
				if(chatId == 15){
					player.getDialogue().sendNpcChat("Oh great! And tell her that I want to kiss her a give.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendPlayerChat("You mean you want to give her a kiss!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendNpcChat("Oh you're good...you are good!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendNpcChat("I see I've picked a true professional...!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 19){
					player.getDialogue().sendOption("Where can I find Juliet?",
													"Is there anything else you can tell me about Juliet?",
													"Ok, thanks.");
					return true;
				}
				if(chatId == 20){
					if(optionId == 3){
						player.getDialogue().sendPlayerChat("Ok, thanks.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}
			if(stage == 3){
				if(player.getInventory().playerHasItem(QuestConstants.MESSAGE)){
					if(chatId == 1){
						player.getDialogue().sendPlayerChat("Romeo...great news...I've been in touch with Juliet!",
															"She's written a message for you...", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 2){
						player.getDialogue().sendItem1Line("You hand over Juliet's message to Romeo.", new Item(QuestConstants.MESSAGE, 1));
						return true;
					}
					if(chatId == 3){
						player.getDialogue().sendNpcChat("Oh, a message! A message! I've never had a message",
														"before...",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 4){
						player.getDialogue().sendPlayerChat("Really?", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 5){
						player.getDialogue().sendNpcChat("No, no, not one!",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 6){
						player.getDialogue().sendNpcChat("Oh, well, except for the occasional court summons.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 7){
						player.getDialogue().sendNpcChat("But they're not really 'nice' messages. Not like this one!",
														"I'm sure that this message will be lovely.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 8){
						player.getDialogue().sendPlayerChat("Well are you going to open it or not?", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 9){
						player.getDialogue().sendNpcChat("Oh yes, yes, of course!",
														"'Dearest Romeo, I am very pleased that you sent",
														player.getUsername()+" to look for me and to tell me that you still",
														"hold affliction...', Affliction! She thinks I'm diseased?",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 10){
						player.getDialogue().sendPlayerChat("'Affection?'", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 11){
						player.getDialogue().sendNpcChat("Ahh yes...'still hold affection for me. I still feel great",
														"affection for you, but unfortunately my Father opposes",
														"our marriage.'",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 12){
						player.getDialogue().sendPlayerChat("Oh dear...that doesn't sound too good.", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 13){
						player.getDialogue().sendNpcChat("What? '...great affection for you. Father opposes our..",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 14){
						player.getDialogue().sendNpcChat("'...marriage and will...",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 15){
						player.getDialogue().sendNpcChat("...will kill you if he sees you again!'",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 16){
						player.getDialogue().sendPlayerChat("I have to be honest, it's not getting any better...", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 17){
						player.getDialogue().sendNpcChat("'Our only hope is that Father Lawrence, our long time",
														"confidant, can help us in some way.'",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 18){
						player.getDialogue().sendItem1Line("Romeo folds the message away.", new Item(755, 1));
						return true;
					}
					if(chatId == 19){
						player.getDialogue().sendNpcChat("Well, that's it then...we haven't got a chance...",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 20){
						player.getDialogue().sendPlayerChat("What about Father Lawrence?", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 21){
						player.getDialogue().sendNpcChat("...our love is over...the great romance, the life of my",
														"love...",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 22){
						player.getDialogue().sendPlayerChat("...or you could speak to Father Lawrence!", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 23){
						player.getDialogue().sendNpcChat("Oh, my aching, breaking, heart...how useless the situation",
														"is now...we have no one to turn to...",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 24){
						player.getDialogue().sendPlayerChat("FATHER LAWRENCE!", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 25){
						player.getInventory().removeItem(new Item(QuestConstants.MESSAGE, 1));
						player.getDialogue().sendNpcChat("Father Lawrence?",Dialogues.CONTENT);
						player.questStage[this.getId()] = 4;
						return true;
					}
				}
			}
			if(stage == 4){
				if(chatId == 26){
					player.getDialogue().sendNpcChat("Oh yes, Father Lawrence...he's our long time confidant,",
													"he might have a solution! Yes, yes, you have to go and",
													"talk to Lather Fawrence for us and ask him if he's got",
													"any suggestions for our predicament?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 27){
					player.getDialogue().sendPlayerChat("Where can I find Father Lawrence?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 28){
					player.getDialogue().sendNpcChat("Lather Fawrence! Oh he's...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 29){
					player.getDialogue().sendNpcChat("You know he's not my 'real' Father don't you?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 30){
					player.getDialogue().sendPlayerChat("I think I suspected that he wasn't.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 31){
					player.getDialogue().sendNpcChat("Well anyway...he tells these song, loring bermons...and",
													"keeps these here Carrockian vitizens snoring in his",
													"church to the East North.",Dialogues.CONTENT);
					player.getDialogue().setNextChatId(1);
					return true;
				}
				if(chatId == 1){
					player.getDialogue().sendOption("How are you?",
													"Where can I find Father Lawrence?",
													"Have you heard anything from Juliet?",
													"Ok, thanks.");
					return true;
				}
				if(chatId == 2){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Where can I find Father Lawrence?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(28);
						return true;
					}else
					if(optionId == 4){
						player.getDialogue().sendPlayerChat("Ok, thanks.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}
			if(stage == 8){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Romeo, it's all set. Juliet has drunk the potion.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Ah right, the potion! Great...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("What potion would that be then?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("The one to get her to the crypt.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Ah right, So she is dead then, Ah thats a shame.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Thanks for your help anyway.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					questCompleted(player);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 639
		if(npcId == QuestConstants.JULIET){
			if(stage == 2){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Juliet, I come from Romeo.",
														"He begs me to tell you that he cares still.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Oh how my heart soars to hear this news! Please take",
													"this message to him with great haste.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Well, I hope it's good news...he was quite upset when I",
														"left him.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("He's quite often upset...the poor sensitive soul. But I",
													"don't think he's going to take this news very well,",
													"however, all is not lost.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Everything is explained in the letter, would you be so",
													"kind and deliver it to him please?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("Certainly, I'll do so straight away.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Many thanks! Oh, I'm so very grateful. You may be",
													"our only hope.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.MESSAGE, 1));
					player.getDialogue().sendItem1Line("Juliet gives you a message.", new Item(QuestConstants.MESSAGE, 1));
					player.questStage[this.getId()] = 3;
					return true;
				}
			}
			if(stage >= 2 && stage < 4){
				if(!player.hasItem(QuestConstants.MESSAGE)){
					if(chatId == 1){
						player.getInventory().addItemOrDrop(new Item(QuestConstants.MESSAGE, 1));
						player.getDialogue().sendItem1Line("Juliet gives you a message.", new Item(QuestConstants.MESSAGE, 1));
						player.getDialogue().setNextChatId(52);
						return true;
					}
				}
				if(chatId == 52){
					player.getDialogue().sendNpcChat("Now don't lose it this time.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage == 3){
				if(chatId == 9){
					player.getActionSender().removeInterfaces();
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage == 7){
				if(player.getInventory().playerHasItem(QuestConstants.CADAVA_POTION)){
					if(chatId == 1){
						player.getDialogue().sendPlayerChat("Hi Juliet! I have an interesting proposition for",
															"you...suggested by Father Lawrence. It may be the",
															"only way you'll be able to escape from this house and",
															"be with Romeo.", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 2){
						player.getDialogue().sendNpcChat("Go on...",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 3){
						player.getDialogue().sendPlayerChat("I have a Cadava potion here, suggested by Father",
															"Lawrence. If you drink it, it will make you appear dead!", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 4){
						player.getDialogue().sendNpcChat("Yes...",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 5){
						player.getDialogue().sendPlayerChat("And when you appear dead...your still and lifeless",
															"corpse will be removed to the crypt!", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 6){
						player.getDialogue().sendNpcChat("Oooooh, a cold dark creepy crypt...",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 7){
						player.getDialogue().sendNpcChat("...sounds just peachy!",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 8){
						player.getDialogue().sendPlayerChat("Then...Romeo can steal into the crypt and rescue you",
															"just as you wake up!", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 9){
						player.getDialogue().sendNpcChat("...and this is the great idea for getting me out of here?",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 10){
						player.getDialogue().sendPlayerChat("To be fair, I can't take all the credit...in fact...it was all",
															"Father Lawrence's suggestion...", Dialogues.CONTENT);
						return true;
					}
					if(chatId == 11){
						player.getDialogue().sendNpcChat("Ok...if this is the best we can do...hand over the potion!",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 12){
						player.getDialogue().send2Items2Lines("You pass the suspicious potion to Juliet.", "", new Item(-1, 1), new Item(756, 1));
						return true;
					}
					if(chatId == 13){
						player.getDialogue().sendNpcChat("Wonderful! I just hope Romeo can remember to get",
														"me from the crypt.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 14){
						player.getDialogue().sendNpcChat("Please go to Romeo and make sure he understands.",
														"Although I love his gormless, lovelorn soppy ways, he",
														"can be a bit dense sometimes and I don't want to wake",
														"up in that crypt on my own.",Dialogues.CONTENT);
						player.getInventory().removeItem(new Item(QuestConstants.CADAVA_POTION, 1));
						player.questStage[this.getId()] = 8;
						player.getDialogue().endDialogue();
						return true;
					}
				}
			}
		}//end of npc 637
		if(npcId == QuestConstants.FATHER_LAWRENCE){
			if(stage == 4){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("\"...and let Saradomin light the way for you... \"",
													"Urgh!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Can't you see that I'm in the middle of a Sermon?!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("But Romeo sent me!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("But I'm busy delivering a sermon to my congregation!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("Yes, well, it certainly seems like you have a captive",
														"audience!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Ok, ok...what do you want so I can get rid of you and",
													"continue with my sermon?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("Romeo sent me. He says you may be able to help.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Ah Romeo, yes. A fine lad, but a little bit confused.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendPlayerChat("Yes, very confused...Anyway, Romeo wishes to be",
														"married to Juliet! She must be rescued from her",
														"father's control!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("I agree, and I think I have an idea! A potion to make",
													"her appear dead...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendPlayerChat("Dead! Sounds a bit creepy to me...but please, continue.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("The potion will only make Juliet 'appear' dead...then",
													"she'll be taken to the crypt...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendPlayerChat("Crypt! Again...very creepy! You must have some",
														"strange hobbies.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("Then Romeo can collect her from the crypt! Go to the",
													"Apothecary, tell him I sent you and that you'll need a",
													"'Cadava' potion.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendPlayerChat("Apart from the strong overtones of death, this is",
														"turning out to be a real love story.",Dialogues.CONTENT);
					player.questStage[this.getId()] = 5;
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 640
		if(npcId == QuestConstants.APOTHECARY){
			if(stage == 5){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Apothecary. Father Lawrence sent me.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("I need a Cadava potion to help Romeo and Juliet.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Cadava potion. It's pretty nasty. And hard to make.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Wing of rat. tail of frog.",
													"Ear of snake and horn of dog.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("I have all that, but I need some Cadava berries.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("You will have to find them while I get the rest ready.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Bring them here when you have them. But be careful.",
													"They are nasty.",Dialogues.CONTENT);
					player.questStage[this.getId()] = 6;
					return true;
				}
			}
			if(stage == 6){
				if(chatId == 8){
					player.getDialogue().sendOption("What do these berries look like?",
													"Where can I get these berries?",
													"How are these berries dangerous?",
													"Ok, thanks.");
					return true;
				}
				if(chatId == 9){
					if(optionId == 4){
						player.getDialogue().sendPlayerChat("Ok, thanks.", Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(player.getInventory().playerHasItem(QuestConstants.CADAVA_BERRIES)){
					if(chatId == 1){
						player.getDialogue().sendNpcChat("Well done. You have the berries.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 2){
						player.getDialogue().send2Items2Lines("You hand over the berries, which the Apothecary shakes",
															"up in a vial of strange liquid.", new Item(-1, 1), new Item(QuestConstants.CADAVA_BERRIES, 1));
						return true;
					}
					if(chatId == 3){
						player.getActionSender().removeInterfaces();
						final Tick timer1 = new Tick(2) {
				            @Override
				            public void execute() {
				            	Npc npc = Npc.getNpcById(QuestConstants.APOTHECARY);
				            	npc.getUpdateFlags().sendAnimation(363);
				            	npc.getUpdateFlags().sendGraphic(189, 25);
				            	World.getTickManager().submit(timer2);
				                stop();
				            }
				            
				            final Tick timer2 = new Tick(2) {
					            @Override
					            public void execute() {
					            	player.getDialogue().sendNpcChat("Phew! Here is what you need.",Dialogues.CONTENT);
					                stop();
					            }
					        };
				            
						};
						World.getTickManager().submit(timer1);
						return true;
					}
					if(chatId == 4){
						player.getInventory().removeItem(new Item(QuestConstants.CADAVA_BERRIES, 1));
						player.getInventory().addItemOrDrop(new Item(QuestConstants.CADAVA_POTION, 1));
						player.getDialogue().send2Items2Lines("Apothecary gives you a Cadava potion.", "", new Item(-1, 1), new Item(QuestConstants.CADAVA_POTION, 1));
						player.questStage[this.getId()] = 7;
						return true;
					}
				}
			}
			if(stage == 7){
				if(chatId == 5){
					player.getDialogue().endDialogue();
					player.getActionSender().removeInterfaces();
					return true;
				}
				if(!player.hasItem(QuestConstants.CADAVA_POTION)){
					if(chatId == 1){
						player.getInventory().addItemOrDrop(new Item(QuestConstants.CADAVA_POTION, 1));
						player.getDialogue().send2Items2Lines("Apothecary gives you a Cadava potion.", "", new Item(-1, 1), new Item(QuestConstants.CADAVA_POTION, 1));
						return true;
					}
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Now don't lose it this time.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 638
		return false;
	}

}
