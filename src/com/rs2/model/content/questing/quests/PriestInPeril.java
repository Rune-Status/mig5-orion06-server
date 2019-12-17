package com.rs2.model.content.questing.quests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.rs2.model.Position;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class PriestInPeril extends Quest {
	
	final int rewardQP = 1;
	
	public PriestInPeril(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to King Roald in Varrock",
							"Palace",
							"",
							"I must be able to defeat a level 30 enemy"};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should investigate what happened to Drezel, who lives",
							"in a temple east of Varrock."};
			return text;	
		}
		if(stage == 3){
			String text[] = {"Drezel told me there is an annoying dog below the temple",
							"and asked me to kill it."};
			return text;	
		}
		if(stage == 4){
			String text[] = {"I should go tell Drezel now that I have taken",
							"care of the dog."};
			return text;	
		}
		if(stage == 5){
			String text[] = {"I should go tell King Roald that everything is fine now",
							"and claim my reward."};
			return text;	
		}
		if(stage == 6){
			String text[] = {"I must return to the temple and find out what happened",
							"to the real Drezel, or the King will have me executed!"};
			return text;	
		}
		if(stage == 7){
			String text[] = {"I found the real Drezel locked in a makeshift cell",
							"upstairs, guarded by a vampyre.",
							"",
							"I need to find the key to his cell and free him!"};
			return text;	
		}
		if(stage == 8){
			String text[] = {"I unlocked the door to Drezel's cell, I should go",
							"and ask him what to do about the vampyre."};
			return text;	
		}
		if(stage == 10){
			String text[] = {"Drezel told me that I should try to get hold of",
							"some water of the Salve."};
			return text;	
		}
		/*if(stage == 10){
			String text[] = {"Drezel told me to use the blessed water on the",
							"coffin."};
			return text;	
		}*/
		if(stage == 11){
			String text[] = {"I should tell Drezel that the vampire is trapped",
							"now."};
			return text;	
		}
		if(stage == 12){
			String text[] = {"Drezel told me to meet him down by the monument",
							"below the temple."};
			return text;	
		}
		if(stage >= 13 && stage < 63){
			int amount = 50+13-stage;
			String text[] = {"Drezel told me to bring him some rune essence.",
							"",
							"Drezel needs "+amount+" more essences."};
			return text;	
		}
		if(stage == 63){
			String text[] = {"I should speak with Drezel to finish this quest."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "1 Quest Point", "1406 Prayer XP", "Wolfbane dagger", "Route to Canifis"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("1406 Prayer XP", 12151);
		player.getActionSender().sendString("Wolfbane dagger", 12152);
		player.getActionSender().sendString("Route to Canifis", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.PRAYER, 1406);
		player.getInventory().addItemOrDrop(new Item(QuestConstants.WOLFBANE, 1));
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.WOLFBANE);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean useQuestItemOnObject(final Player player, int itemId, int objectId, int stage){
		List<Integer> shuffledMonuments = new ArrayList<Integer>(monuments);
		Collections.shuffle(shuffledMonuments, new Random(player.uniqueRandomInt));
		if(objectId == shuffledMonuments.get(5)){
			if(itemId == QuestConstants.GOLDEN_KEY){
				player.getInventory().removeItem(new Item(QuestConstants.GOLDEN_KEY, 1));
				player.getInventory().addItemOrDrop(new Item(QuestConstants.IRON_KEY, 1));
				player.getActionSender().sendMessage("You swap the Golden key for the Iron key.");
				player.getUpdateFlags().sendAnimation(832);
				return true;
			}
		}
		if(objectId == 3463){//cell door
			if(itemId == QuestConstants.IRON_KEY){
				player.getDialogue().setLastNpcTalk(QuestConstants.DREZEL_1);
				player.getDialogue().sendNpcChat("Oh! Thank you! You have found the key!",Dialogues.CONTENT);
				player.getInventory().removeItem(new Item(QuestConstants.IRON_KEY, 1));
				player.questStage[this.getId()] = 8;
				return true;
			}
		}//end of cell door
		if(objectId == 3485){//well
			if(itemId == QuestConstants.BUCKET){
				player.getInventory().removeItem(new Item(QuestConstants.BUCKET, 1));
				if(stage == 1)
					player.getInventory().addItemOrDrop(new Item(QuestConstants.BUCKET_OF_WATER, 1));
				else
					player.getInventory().addItemOrDrop(new Item(QuestConstants.BUCKET_OF_MURKY_WATER, 1));
				player.getActionSender().sendMessage("You fill the bucket from the well.");
				player.getUpdateFlags().sendAnimation(832);
				player.getActionSender().sendSound(1039, 1, 0);
				return true;
			}
		}
		if(objectId == 3480){//coffin
			if(stage == 10){
				if(itemId == QuestConstants.BUCKET_OF_BLESSED_WATER){
					player.getInventory().removeItem(new Item(QuestConstants.BUCKET_OF_BLESSED_WATER, 1));
					player.getInventory().addItemOrDrop(new Item(QuestConstants.BUCKET, 1));
					player.getActionSender().sendMessage("You pour the blessed water over the coffin...");
					player.getUpdateFlags().sendAnimation(832);
					player.questStage[this.getId()] = 11;
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean handleSecondClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 3496 && objectX == 3423 && objectY == 9884 || //monument #1
			objectId == 3498 && objectX == 3427 && objectY == 9885 || //monument #2
			objectId == 3495 && objectX == 3428 && objectY == 9890 || //monument #3
			objectId == 3497 && objectX == 3427 && objectY == 9894 || //monument #4
			objectId == 3494 && objectX == 3423 && objectY == 9895 || //monument #5
			objectId == 3499 && objectX == 3418 && objectY == 9894 || //monument #6	
			objectId == 3493 && objectX == 3416 && objectY == 9890){//monument #7
				if(stage != 1){
					player.hit(Misc.random(6), HitType.NORMAL);
					player.getActionSender().sendMessage("A holy power prevents you stealing from the monument!");
					player.getUpdateFlags().sendAnimation(832);
				} else {
					player.getActionSender().sendMessage("It would be wrong to dishonour this monument.");
				}
				return true;
		}
		return false;
	}
	
	private List<Integer> monuments = Arrays.asList(3493, 3494, 3495, 3496, 3497, 3498, 3499);
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		List<Integer> shuffledMonuments = new ArrayList<Integer>(monuments);
		Collections.shuffle(shuffledMonuments, new Random(player.uniqueRandomInt));
		if(objectId == 3443 && objectX == 3440 && objectY == 9886){//holy barrier to canifis
			if(stage == 1){
				player.teleport(new Position(3423, 3485, 0));
				player.getActionSender().sendMessage("You step through the holy barrier and appear in Canifis.");
			}else{
				player.getActionSender().sendMessage("You need to complete Priest in Peril Quest to pass-through.");
			}
			return true;
		}//end of holy barrier to canifis
		if(objectId == 3480 && objectX == 3413 && objectY == 3486){//coffin
			player.getDialogue().sendPlayerChat("It sounds like there's something alive inside it. I don't",
												"think it would be a very good idea to open it...",Dialogues.CONTENT);
			player.getDialogue().endDialogue();
			return true;
		}//end of coffin
		if(objectId == 3463 && objectX == 3415 && objectY == 3489){//cell door
			if(stage >= 8 || stage == 1){
				player.getActionSender().walkTo((player.getPosition().getX() > 3415 ? -1 : 1), 0, true);
				player.getActionSender().walkThroughDoor(3463, 3415, 3489, player.getPosition().getZ());
			}else{
				player.getActionSender().sendMessage("The door is securely locked shut.");
			}
			return true;
		}//end of cell door
		if(objectId == 3444 && objectX == 3405 && objectY == 9895){//gate #1
			if(stage >= 7 || stage == 1){
				player.getActionSender().walkTo(0, (player.getPosition().getY() > 9894 ? -1 : 1), true);
				player.getActionSender().walkThroughDoor(3444, 3405, 9895, player.getPosition().getZ());
			}else{
				player.getActionSender().sendMessage("This door is locked.");
			}
			return true;
		}
		if(objectId == 3445 && objectX == 3431 && objectY == 9897){//gate #2
			if(stage >= 12 || stage == 1){
				player.getActionSender().walkTo((player.getPosition().getX() > 3431 ? -1 : 1), 0, true);
				player.getActionSender().walkThroughDoor(3445, 3431, 9897, player.getPosition().getZ());
			}else{
				player.getActionSender().sendMessage("This door is locked.");
			}
			return true;
		}
		if((objectId == 3489 && objectX == 3408 && objectY == 3489) || (objectId == 3490 && objectX == 3408 && objectY == 3488)){//temple door
			if(stage == 0 || (stage >= 2 && stage < 6)){
				player.getActionSender().sendMessage("This door is securely locked from inside.");
				return true;
			}else{
				player.getActionSender().walkTo((player.getPosition().getX() > 3408 ? -1 : 1), 0, true);
				player.getActionSender().walkThroughDoubleDoor(3489, 3490, 3408, 3489, 3408, 3488, 0);
				return true;
			}
		}
		if(objectId == 3485 && objectX == 3423 && objectY == 9890){//well
			if(stage != 1){
				player.getDialogue().sendStatement("You look down the well and see the filthy polluted water of the river",
												"Salve moving slowly along.");
			}else{
				player.getDialogue().sendStatement("You look down the well and see the fresh water of the river Salve",
												"moving swiftly along.");
			}
			return true;
		}
		if(stage != 1){
			//if(objectId == 3496 && objectX == 3423 && objectY == 9884){//monument #1
			if(objectId == shuffledMonuments.get(0)){//monument #1
				player.getActionSender().sendItemOnInterface(8111, 250, QuestConstants.GOLDEN_HAMMER);
				player.getActionSender().sendString("Saradomin is the\\n" +
													"hammer that\\n" +
													"crushes evil\\n" +
													"everywhere.", 8112);
				player.getActionSender().sendInterface(8016);
				return true;
			}
			//if(objectId == 3498 && objectX == 3427 && objectY == 9885){//monument #2
			if(objectId == shuffledMonuments.get(1)){//monument #2
				player.getActionSender().sendItemOnInterface(8111, 200, QuestConstants.GOLDEN_NEEDLE);
				player.getActionSender().sendString("Saradomin is the\\n" +
													"needle that binds\\n" +
													"our lives\\n" +
													"together.", 8112);
				player.getActionSender().sendInterface(8016);
				return true;
			}
			//if(objectId == 3495 && objectX == 3428 && objectY == 9890){//monument #3
			if(objectId == shuffledMonuments.get(2)){//monument #3
				player.getActionSender().sendItemOnInterface(8111, 250, QuestConstants.GOLDEN_POT);
				player.getActionSender().sendString("Saradomin is the\\n" +
													"vessel that\\n" +
													"keeps us safe\\n" +
													"from harm.", 8112);
				player.getActionSender().sendInterface(8016);
				return true;
			}
			//if(objectId == 3497 && objectX == 3427 && objectY == 9894){//monument #4
			if(objectId == shuffledMonuments.get(3)){//monument #4
				player.getActionSender().sendItemOnInterface(8111, 200, QuestConstants.GOLDEN_FEATHER);
				player.getActionSender().sendString("Saradomin is the\\n" +
													"delicate touch\\n" +
													"that brushes us\\n" +
													"with love.", 8112);
				player.getActionSender().sendInterface(8016);
				return true;
			}
			//if(objectId == 3494 && objectX == 3423 && objectY == 9895){//monument #5
			if(objectId == shuffledMonuments.get(4)){//monument #5
				player.getActionSender().sendItemOnInterface(8111, 250, QuestConstants.GOLDEN_CANDLE);
				player.getActionSender().sendString("Saradomin is the\\n" +
													"light that shines\\n" +
													"throughout\\n" +
													"our lives.", 8112);
				player.getActionSender().sendInterface(8016);
				return true;
			}
			//if(objectId == 3499 && objectX == 3418 && objectY == 9894){//monument #6
			if(objectId == shuffledMonuments.get(5)){//monument #6
				player.getActionSender().sendItemOnInterface(8111, 250, ((!player.hasItem(QuestConstants.IRON_KEY) && stage < 8) ? QuestConstants.IRON_KEY : QuestConstants.GOLDEN_KEY));
				player.getActionSender().sendString("Saradomin is the\\n" +
													"key that unlocks\\n" +
													"the mysteries\\n" +
													"of life.", 8112);
				player.getActionSender().sendInterface(8016);
				return true;
			}
			//if(objectId == 3493 && objectX == 3416 && objectY == 9890){//monument #7
			if(objectId == shuffledMonuments.get(6)){//monument #7
				player.getActionSender().sendItemOnInterface(8111, 250, QuestConstants.GOLDEN_TINDERBOX);
				player.getActionSender().sendString("Saradomin is the\\n" +
													"spark that lights\\n" +
													"the fire in\\n" +
													"our hearts.", 8112);
				player.getActionSender().sendInterface(8016);
				return true;
			}
		}else{//stage == 1
			if(objectId == 3496 && objectX == 3423 && objectY == 9884 || //monument #1
					objectId == 3498 && objectX == 3427 && objectY == 9885 || //monument #2
					objectId == 3495 && objectX == 3428 && objectY == 9890 || //monument #3
					objectId == 3497 && objectX == 3427 && objectY == 9894 || //monument #4
					objectId == 3494 && objectX == 3423 && objectY == 9895 || //monument #5
					objectId == 3499 && objectX == 3418 && objectY == 9894 || //monument #6	
					objectId == 3493 && objectX == 3416 && objectY == 9890){//monument #7
						player.getActionSender().sendMessage("A monument dedicated to the fallen.");
						return true;
			}
		}
		return false;
	}
	
	public boolean getObjectDialog(int clickId, final Player player, int objectId, int chatId, int optionId, int npcChatId, int x, int y, int stage){
		if(clickId == 2){
			if(objectId == 3463 && x == 3415 && y == 3489){//cell door
				if(stage == 6){
					player.getDialogue().setLastNpcTalk(QuestConstants.DREZEL_1);
					if(chatId == 1){
						player.getDialogue().sendPlayerChat("Hello.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 2){
						player.getDialogue().sendNpcChat("Oh! You do not appear to be one of those Zamorakians",
														"who imprisoned me here! Who are you and why are",
														"you here?",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 3){
						player.getDialogue().sendPlayerChat("My name's "+player.getUsername()+". King Roald sent me to find",
															"out what was going on at the temple. I take it you are",
															"Drezel?",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 4){
						player.getDialogue().sendNpcChat("That is right! Oh, praise be to Saradomin! All is not yet",
														"lost! I feared that when those Zamorakians attacked this",
														"place and imprisoned",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 5){
						player.getDialogue().sendNpcChat("me up here, Misthalin would be doomed! If they should",
														"manage to desecrate the holy river Salve we would be",
														"defenceless against Morytania!",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 6){
						player.getDialogue().sendPlayerChat("How is a river a good defence then?",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 7){
						player.getDialogue().sendNpcChat("Well, it is a long tale, and I am not sure we have time!",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 8){
						player.getDialogue().sendOption("Tell me anyway.",
														"You're right, we don't.");
						return true;
					}
					if(chatId == 9){
						if(optionId == 1){
							player.getDialogue().sendPlayerChat("Tell me anyway. I'd like to know the full facts before",
																"acting any further.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(10);
							return true;
						}
						if(optionId == 2){
							player.getDialogue().sendPlayerChat("You're right, we don't. it doesn't matter anyway.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(48);
							return true;
						}
					}
					if(chatId == 48){
						player.getDialogue().sendNpcChat("Well, let's just say if we cannot undo whatever damage",
														"has been done here, the entire land is in grave peril!",Dialogues.CONTENT);
						player.getDialogue().setNextChatId(32);
						return true;
					}
					if(chatId == 10){
						player.getDialogue().sendNpcChat("Ah, Saradomin has granted you wisdom I see. Well, the",
														"story of the river Salve and how it protects Misthalin",
														"is the story of this temple,",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 11){
						player.getDialogue().sendNpcChat("and of the seven warrior priests who died here long ago,",
														"from whom I am descended. Once long ago Misthalin",
														"did not have the borders that",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 12){
						player.getDialogue().sendNpcChat("it currently does. This entire area, as far West as",
														"Varrock itself was under the control of an evil god.",
														"There was frequent skirmishing",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 13){
						player.getDialogue().sendNpcChat("along the borders, as the brave heroes of Varrock",
														"fought to keep the evil creatures that now are trapped",
														"on the eastern side of the River Salve from over",
														"running",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 14){
						player.getDialogue().sendNpcChat("the human encampments, who worshipped Saradomin.",
														"Then one day, Saradomin himself appeared to one of",
														"our mighty heroes, whose name has been forgotten by",
														"history,",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 15){
						player.getDialogue().sendNpcChat("and told him that should we be able to take the pass that",
														"this temple now stands in, Saradomin would use his",
														"power to bless this river, and make it",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 16){
						player.getDialogue().sendNpcChat("impassable to all creatures with evil in their hearts. This",
														"unknown hero grouped together all of the mightiest",
														"fighters whose hearts were pure",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 17){
						player.getDialogue().sendNpcChat("that he could find, and the seven of them rode here to",
														"make a final stand. The enemies swarmed across the",
														"Salve but they did not yield.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 18){
						player.getDialogue().sendNpcChat("For ten days and nights they fought, never sleeping,",
														"never eating, fuelled by their desire to make the world a",
														"better place for humans to live.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 19){
						player.getDialogue().sendNpcChat("On the eleventh day they were to be joined by",
														"reinforcements from a neighbouring encampment, but",
														"when those reinforcements arrived all they found",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 20){
						player.getDialogue().sendNpcChat("were the bodies of these seven brave but unknown",
														"heroes, surrounded by the piles of the dead creatures of",
														"evil that had tried to defeat them.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 21){
						player.getDialogue().sendNpcChat("The men were saddened at the loss of such pure and",
														"mighty warriors, yet their sacrifice had not been in",
														"vain; for the water of the Salve",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 22){
						player.getDialogue().sendNpcChat("had indeed been filled with the power of Saradomin, and",
														"the evil creatures of Morytania were trapped beyond",
														"the river banks forever, by their own evil.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 23){
						player.getDialogue().sendNpcChat("In memory of this brave sacrifice my ancestors built",
														"this temple so that the land would always be free of the",
														"evil creatures",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 24){
						player.getDialogue().sendNpcChat("who wish to destroy it, and laid the bodies of those brave",
														"warriors in tombs of honour below this temple with",
														"golden gifts on the tombs as marks of respect.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 25){
						player.getDialogue().sendNpcChat("They also built a statue on the river mouth so that all",
														"who might try and cross into Misthalin from Morytania",
														"would know that these lands are protected",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 26){
						player.getDialogue().sendNpcChat("by the glory of Saradomin and that good will always",
														"defeat evil, no matter how the odds are stacked against",
														"them.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 27){
						player.getDialogue().sendPlayerChat("Ok, I can see how the river protects the border, but I",
															"can't see how anything could affect that from this",
															"temple.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 28){
						player.getDialogue().sendNpcChat("Well, as much as it saddens me to say so adventurer,",
														"Lord Saradomin's presence has not been felt on the",
														"land for many years now, and even",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 29){
						player.getDialogue().sendNpcChat("though all true Saradominists know that he watches over",
														"us, his power upon the land is not as strong as it once",
														"was.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 30){
						player.getDialogue().sendNpcChat("I fear that should those Zamorakians somehow pollute",
														"the Salve and desecrate his blessing, his power might not",
														"be able to stop",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 31){
						player.getDialogue().sendNpcChat("the army of evil that lurks to the east, longing for the",
														"opportunity to invade and destroy us all!",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 32){
						player.getDialogue().sendNpcChat("So what do you say adventurer? Will you aid me and",
														"all of Misthalin in foiling this Zamorakian plot?",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 33){
						player.getDialogue().sendOption("Yes.",
														"No.");
						return true;
					}
					if(chatId == 34){
						if(optionId == 1){
							player.getDialogue().sendPlayerChat("Yes, of course. Any threat to Misthalin must be",
																"neutralised immediately. So what can I do to help?", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(35);
							return true;
						}else{
							player.getDialogue().endDialogue();
							return false;
						}
					}
					if(chatId == 35){
						player.getDialogue().sendNpcChat("Well, the immediate problem is that I am trapped in this",
														"cell. I know that the key to free me is nearby, for none",
														"of the Zamorakians",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 36){
						player.getDialogue().sendNpcChat("who imprisoned me here were ever gone for long",
														"periods of time. Should you find the key however, as",
														"you may have noticed,",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 37){
						player.getDialogue().sendNpcChat("there is a vampire in that coffin over there. I do not",
														"know how they managed to find it, but it is one of the",
														"ones that somehow",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 38){
						player.getDialogue().sendNpcChat("survived the battle here all those years ago, and is by",
														"now quite, quite, mad. It has been trapped on this side",
														"of the river for centuries,",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 39){
						player.getDialogue().sendNpcChat("and as those fiendish Zamorakians pointed out to me",
														"with delight, as a descendant of one of those who",
														"trapped it here, it will recognise",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 40){
						player.getDialogue().sendNpcChat("the smell of my blood should I come anywhere near it.",
														"It will of course then wake up and kill me, very",
														"probably slowly and painfully.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 41){
						player.getDialogue().sendPlayerChat("Maybe I could kill it somehow then while it is asleep?",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 42){
						player.getDialogue().sendNpcChat("No adventurer, I do not think it would be wise for you",
														"to wake it at all. As I say, it is little more than a wild",
														"animal, and must",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 43){
						player.getDialogue().sendNpcChat("be extremely powerful to have survived until today. I",
														"suspect your best chance would be to incapacitate it",
														"somehow.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 44){
						player.getDialogue().sendPlayerChat("Okay, find the key to your cell, and do something about",
															"the vampire.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 45){
						player.getDialogue().sendNpcChat("When you have done both of those I will be able to",
														"inspect the damage which those Zamorakians have done",
														"to the purity of the Salve.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 46){
						player.getDialogue().sendNpcChat("Depending on the severity of the damage, I may",
														"require further assistance from you in restoring its",
														"purity.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 47){
						player.getDialogue().sendPlayerChat("Okay, well first thing's first; let's get you out of here.",Dialogues.CONTENT);
						player.questStage[this.getId()] = 7;
						player.getDialogue().endDialogue();
						return true;
					}
				}//end of stage 6
			}//end of cell door
			if((objectId == 3489 && x == 3408 && y == 3489) || (objectId == 3490 && x == 3408 && y == 3488)){//temple door
				if(stage == 2){
					if(chatId == 1){
						player.getDialogue().sendStatement("You knock at the door...You hear a voice from inside. @dbl@Who are you",
														"@dbl@and what do you want?");
						player.getActionSender().sendSound(2466, 1, 0);
						return true;
					}
					if(chatId == 2){
						player.getDialogue().sendPlayerChat("Ummmm.....",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 3){
						player.getDialogue().sendOption("Roald sent me to check on Drezel.",
														"Hi, I just moved in next door...",
														"I hear this place is of historic interest",
														"The council sent me to check your pipes.");
						return true;
					}
					if(chatId == 4){
						if(optionId == 1){
							player.getDialogue().sendPlayerChat("Roald sent me to check on Drezel.", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(5);
							return true;
						}else{
							player.getDialogue().endDialogue();
							return false;
						}
					}
					if(chatId == 5){
						player.getDialogue().sendStatement("@dbl@(Psst... Hey... Who's Roald? Who's Drezel?)@bla@(Uh... isn't Drezel that",
														"lude upstairs? Oh, wait, Roald's the King of Varrock right?)@dbl@(He is???",
														"@dbl@Aw man... Hey, you deal with this okay?) He's just coming! Wait a",
														"@dbl@second!@bla@Hello, my name is Drevil. @dbl@(Drezel!)@bla@ I mean Drezel. How can",
														"I help?");
						return true;
					}
					if(chatId == 6){
						player.getDialogue().sendPlayerChat("Well, as I say, the King sent me to make sure",
															"everything's okay with you.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 7){
						player.getDialogue().sendStatement("And, uh, what would you do if everything wasn't okay with me?");
						return true;
					}
					if(chatId == 8){
						player.getDialogue().sendPlayerChat("I'm not sure. Ask you what help you need I suppose.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 9){
						player.getDialogue().sendStatement("Ah, good, well, I don't think... @dbl@(Psst... hey... the dog!)@bla@ OH! Yes, of",
														"course! Will you do me a favor adventurer?");
						return true;
					}
					if(chatId == 10){
						player.getDialogue().sendOption("Sure.",
														"Nope.");
						return true;
					}
					if(chatId == 11){
						if(optionId == 1){
							player.getDialogue().sendPlayerChat("Sure. I'm helpful person!", Dialogues.CONTENT);
							player.getDialogue().setNextChatId(12);
							return true;
						}else{
							player.getDialogue().endDialogue();
							return false;
						}
					}
					if(chatId == 12){
						player.getDialogue().sendStatement("HAHAHAHA! Really? Thanks buddy! You see that mausoleum out",
														"there? There's a horrible big dog underneath it that I'd like you to",
														"kill for me! It's been really bugging me! Barking all the time and",
														"stuff! Please kill it for me buddy!");
						player.questStage[this.getId()] = 3;
						return true;
					}
				}//end of stage 2
				if(stage == 3){
					if(chatId == 13){
						player.getDialogue().sendPlayerChat("Okey-dokey, one dead dog coming up.",Dialogues.CONTENT);
						player.getDialogue().endDialogue();
						return true;
					}
				}//end of stage 3
				if(stage == 4){
					if(chatId == 1){
						player.getDialogue().sendStatement("You knock at the door...You hear a voice from inside.@dbl@You again?",
														"@dbl@What do you want now?");
						return true;
					}
					if(chatId == 2){
						player.getDialogue().sendPlayerChat("I killed that dog for you.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 3){
						player.getDialogue().sendStatement("@dbl@HAHAHAHAHA! Really? Hey, that's great!@bla@ Yeah thanks a lot buddy!",
														"HAHAHAHAHAHA");
						return true;
					}
					if(chatId == 4){
						player.getDialogue().sendPlayerChat("What's so funny?",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 5){
						player.getDialogue().sendStatement("@dbl@HAHAHAHA nothing buddy! We're just so grateful to you!",
														"@dbl@HAHAHA@bla@ Yeah, maybe you should go tell the King what a great job",
														"you did buddy! HAHAHA");
						player.questStage[this.getId()] = 5;
						player.getDialogue().endDialogue();
						return true;
					}
				}//end of stage 4
				if(stage == 1 || stage >= 6){
					if(chatId == 1){
						player.getDialogue().sendStatement("You knock at the door...The door swings open as you knock.");
						return true;
					}
					if(chatId == 2){
						player.getActionSender().walkThroughDoubleDoor(3489, 3490, 3408, 3489, 3408, 3488, 0);
						player.getActionSender().walkTo((player.getPosition().getX() > 3408 ? -1 : 1), 0, true);
						player.getDialogue().endDialogue();
						player.getActionSender().removeInterfaces();
						return true;
					}
				}
			}//end of temple door
		}//end of click 2
		return false;
	}
	
	public boolean allowedToAttackNpc(final Player player, int npcId, int stage){
		if(npcId == QuestConstants.TEMPLE_GUARDIAN){
			if(stage != 3)
				return false;
		}
		return true;
	}
	
	public boolean handleNpcDeath(final Player player, int npcId, int stage){
		if(npcId == QuestConstants.TEMPLE_GUARDIAN){
			if(stage == 3){
				player.questStage[this.getId()] = 4;
				return true;
			}
		}
		return false;
	}
	
	public boolean getQuestDrop(final Player killer, int npcId, Position deathPos, int stage){
		if(npcId == QuestConstants.MONK_OF_ZAMORAK_LVL30){
			if(stage != 1 && !killer.hasItem(QuestConstants.GOLDEN_KEY) && !killer.hasItem(QuestConstants.IRON_KEY) && stage < 8){
				GroundItem drop = new GroundItem(new Item(QuestConstants.GOLDEN_KEY, 1), killer, deathPos);
		        GroundItemManager.getManager().dropItem(drop);
		        return true;
			}
		}
		return false;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.KING_ROALD){
			if(stage == 0 || stage == 5){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Greetings, your majesty.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Well hello there. What do you want?",Dialogues.CONTENT);
					return true;
				}
			}
			if(stage == 0){
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("I am looking for a quest!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("A quest you say? Hmm, what an odd request to make",
													"of the king. It's funny you should mention it though, as",
													"there is something you can do for me.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Are you aware of the temple east of here? It stands on",
													"the river Salve and guards the entrance to the lands of",
													"Morytania?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("No, I don't think I know it...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Hmm, how strange that you don't. Well anyway, it has",
													"been some days since last I heard from Drezel, the",
													"priest who lives there.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Be a sport and go make sure that nothing untoward",
													"has happened to the silly old codger for me, would you?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendOption("Sure.",
													"No, that sounds boring.");
					return true;
				}
				if(chatId == 10){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Sure. I don't have anything better to do right now.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(11);
						questStarted(player);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
			}//end of stage 0
			if(stage == 2){
				if(chatId == 11){
					player.getDialogue().sendNpcChat("Many thanks adventurer! I would have sent one of my",
													"squires but they wanted payment for it!",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage == 5){
				if(chatId == 3){
					player.getDialogue().sendNpcChat("You have news of Drezel for me?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("Yeah, I spoke to the guys at the temple and they said",
														"they were being bothered by that dog in the crypt, so I",
														"went and killed it for them. No problem.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("YOU DID WHAT???",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Are you mentally deficient??? That guard dog was",
													"protecting the route to Morytania! Without it we could",
													"be in severe peril of attack!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("Did I make a mistake?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("YES YOU DID!!!!! You need to get there right now",
													"and find out what is happening! Before it is too late for",
													"us all!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendPlayerChat("B-but Drezel TOLD me to...!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("No, you absolute cretin! Obviously some fiend has done",
													"something to Drezel and tricked your feeble intellect",
													"into helping them kill that guard dog!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("You get back there and do whatever is necessary to",
													"safeguard my kingdom from attack, or I will see you",
													"beheaded for high treason!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendPlayerChat("Y-yes your Highness.",Dialogues.CONTENT);
					player.questStage[this.getId()] = 6;
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 648
		if(npcId == QuestConstants.DREZEL_1){
			if(stage >= 8 && stage <= 10){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("The key fitted the lock! You're free to leave now!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Well excellent work adventurer! Unfortunately, as you",
													"know, I cannot risk waking that vampire in the coffin.",Dialogues.CONTENT);
					if(stage >= 9){
						if(player.getInventory().playerHasItem(QuestConstants.BUCKET_OF_MURKY_WATER))
							player.getDialogue().setNextChatId(6);
						if(player.getInventory().playerHasItem(QuestConstants.BUCKET_OF_BLESSED_WATER))
							player.getDialogue().setNextChatId(8);
					}
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("Do you have any ideas about dealing with the vampire?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Well, the water of the Salve should still have enough",
													"power to work against the vampire despite what those",
													"Zamorakians might have done to it...",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Maybe you should try and get hold of some from",
													"somewhere?",Dialogues.CONTENT);
					player.questStage[this.getId()] = 10;
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("I have some water from the Salve. It seems to have",
														"been desecrated though. Do you think you could bless",
														"it for me?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					if(player.getInventory().playerHasItem(QuestConstants.BUCKET_OF_MURKY_WATER)){
						player.getDialogue().sendNpcChat("Yes, good thinking adventurer! Give it to me, I will bless",
														"it!",Dialogues.CONTENT);
						player.getActionSender().sendMessage("The priest blesses the water for you.");
						player.getInventory().removeItem(new Item(QuestConstants.BUCKET_OF_MURKY_WATER, 1));
						player.getInventory().addItemOrDrop(new Item(QuestConstants.BUCKET_OF_BLESSED_WATER, 1));
						player.getDialogue().endDialogue();
						return true;
					}
				}
				if(chatId == 8){
					player.getDialogue().sendPlayerChat("I have some blessed water from the Salve in this bucket.",
														"Do you think it would help against that vampire?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Yes! Great idea! If his coffin is closed in the blessed",
													"water he will be unable to leave it! Use it on his coffin,",
													"quickly!",Dialogues.CONTENT);
					//player.questStage[this.getId()] = 10;
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 8
			if(stage == 11){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("I poured the blessed water over the vampires coffin. I",
														"think that should trap him in there long enough for you",
														"to escape.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Excellent work adventurer! I am free at last! Let me",
													"ensure that evil vampire is trapped for good. I will meet",
													"you down by the monument.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Look for me down there, I need to assess what damage",
													"has been done to our holy barrier by those evil",
													"Zamorakians!",Dialogues.CONTENT);
					player.questStage[this.getId()] = 12;
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 1048
		if(npcId == QuestConstants.DREZEL_2){
			if(stage == 12){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Ah, "+player.getUsername()+". I see you finally made it down here.",
													"Things are worse than I feared. I'm not sure if I will",
													"be able to repair the damage.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("Why, what's happened?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("From what I can tell, after you killed the guard dog",
													"who protected the entrance to the monuments, those",
													"Zamorakians forced the door into the main chamber",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("and have used some kind of evil potion upon the well",
													"which leads to the source of the river Salve. As they",
													"have done this at the very mouth of the river",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("it will spread along the entire river, disrupting the",
													"blessing placed upon it and allowing the evil creatures of",
													"Morytania to invade at their leisure.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("What can we do to prevent that?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Well, as you can see, I have placed a holy barrier on",
													"the entrance to this room from the South, but it is not",
													"very powerful and requires me to remain",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("here focussing upon it to keep it intact. Should an",
													"attack come, they would be able to breach this defence",
													"very quickly indeed. What we need to do is",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("find some kind of way of removing or counteracting the",
													"evil magic that has been put into the river source at the",
													"well, so that the river will flow pure once again.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendPlayerChat("Couldn't you bless the river to purify it? Like you did",
														"with the water I took from the well?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("No, that would not work, the power I have from",
													"Saradomin is not great enough to cleanse an entire",
													"river of this foul Zamorakian pollutant.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("I have only one idea how we could possibly cleanse the",
													"river.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendPlayerChat("What's that?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("I have heard rumours recently that Mages have found",
													"some secret ore that absorbs magic into it and allows",
													"them to create runes.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendNpcChat("Should you be able to collect enough of this ore, it is",
													"possible it will soak up the evil potion that has been",
													"poured into the river, and purify it.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendPlayerChat("Kind of like a filter? Okay, I guess it's worth a try.",
														"How many should I get?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendNpcChat("Well I have no knowledge of these ores other than",
													"speculation and gossip, but if the things I hear are true",
													"around fifty should be sufficient for the task.",Dialogues.CONTENT);
					player.questStage[this.getId()] = 13;
					player.getDialogue().endDialogue();
					return true;
				}
			}
			if(stage >= 13 && stage < 63){
				if(player.getInventory().playerHasItem(QuestConstants.RUNE_ESSENCE) || player.getInventory().playerHasItem(QuestConstants.PURE_ESSENCE)){
					if(chatId == 1){
						if(stage == 13)
							player.getDialogue().sendPlayerChat("I brought you some Rune Essence.",Dialogues.CONTENT);
						else
							player.getDialogue().sendPlayerChat("I brought you some more essences.",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 2){
						if(stage == 13)
							player.getDialogue().sendNpcChat("Quickly, give it to me!",Dialogues.CONTENT);
						else
							player.getDialogue().sendNpcChat("Quickly, give them to me!",Dialogues.CONTENT);
						return true;
					}
					if(chatId == 3){
						int amt = player.getInventory().getItemAmount(QuestConstants.RUNE_ESSENCE);
						int removeAmt = 50+13-stage;
						int realRemoveAmt = (amt < removeAmt ? amt : removeAmt);
						player.getInventory().removeItem(new Item(QuestConstants.RUNE_ESSENCE, realRemoveAmt));
						player.questStage[this.getId()] += realRemoveAmt;
						player.getActionSender().sendMessage("You give the priest your blank runes.");
						if(50+13-player.questStage[this.getId()] != 0 && player.getInventory().playerHasItem(QuestConstants.PURE_ESSENCE)){//check for pure ess
							amt = player.getInventory().getItemAmount(QuestConstants.PURE_ESSENCE);
							removeAmt = 50+13-player.questStage[this.getId()];
							realRemoveAmt = (amt < removeAmt ? amt : removeAmt);
							player.getInventory().removeItem(new Item(QuestConstants.PURE_ESSENCE, realRemoveAmt));
							player.questStage[this.getId()] += realRemoveAmt;
						}
						if(50+13-player.questStage[this.getId()] == 0){
							player.getDialogue().sendNpcChat("Excellent! That should do it! I will bless these stones",
															"and place them within the well, and Misthalin should be",
															"protected once more!",Dialogues.CONTENT);
							player.getDialogue().setNextChatId(2);
						}else{
							player.getDialogue().endDialogue();
							player.getActionSender().removeInterfaces();
						}
						return true;
					}
				}//has essence check
			}
			if(stage == 63){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Excellent! That should do it! I will bless these stones",
													"and place them within the well, and Misthalin should be",
													"protected once more!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Please take this dagger; it has been handled down within",
													"my family for generations and is filled with the power of",
													"Saradomin. You will find that",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("it has the power to prevent werewolves from adopting",
													"their wolf form in combat as long as you have it",
													"equipped.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					questCompleted(player);
					player.getDialogue().endDialogue();
					return true;
				}
			}
		}//end of npc 1049
		return false;
	}

}
