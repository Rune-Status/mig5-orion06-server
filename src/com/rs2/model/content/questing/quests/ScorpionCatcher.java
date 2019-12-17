package com.rs2.model.content.questing.quests;

import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;

public class ScorpionCatcher extends Quest {
	
	final int rewardQP = 1;
	
	public ScorpionCatcher(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to Thormac who is in the",
							"Sorcerer's Tower",
							"",
							"Requirements:",
							"You'll need level 31 Prayer"};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should go to Seers village and find someone to help me",
							"locate the Kharid scorpions."};
			return text;	
		}
		if(stage == 3){
			String text[] = {"I should now go and look for the scorpions and return",
							"to Thormac when done."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "1 Quest Point", "6625 Strength  XP"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("6625 Strength  XP", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.STRENGTH, 6625);
		//
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.SCORPION_CAGE_FULL);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean handleFirstClickNpc(final Player player, int npcId, int stage){
		if(stage != 1){
			if(npcId == QuestConstants.KHARID_SCORPION_TAV && hasCatchedTavScorpion(player))
				return false;
			if(npcId == QuestConstants.KHARID_SCORPION_BARB && hasCatchedBarbScorpion(player))
				return false;
			if(npcId == QuestConstants.KHARID_SCORPION_MONA && hasCatchedMonaScorpion(player))
				return false;
			if(npcId == QuestConstants.KHARID_SCORPION_TAV || npcId == QuestConstants.KHARID_SCORPION_BARB || npcId == QuestConstants.KHARID_SCORPION_MONA){
				player.hit(3, HitType.NORMAL);
				player.getActionSender().sendMessage("The scorpion stings you!");
				return true;
			}
		}
		return false;
	}
	
	boolean hasCatchedTavScorpion(final Player player){
		if(player.hasItem(QuestConstants.SCORPION_CAGE_FULL) || player.hasItem(QuestConstants.SCORPION_CAGE_TAV) || player.hasItem(QuestConstants.SCORPION_CAGE_TAV_BARB) || player.hasItem(QuestConstants.SCORPION_CAGE_TAV_MONA))
			return true;
		return false;
	}
	
	boolean hasCatchedBarbScorpion(final Player player){
		if(player.hasItem(QuestConstants.SCORPION_CAGE_FULL) || player.hasItem(QuestConstants.SCORPION_CAGE_BARB) || player.hasItem(QuestConstants.SCORPION_CAGE_TAV_BARB) || player.hasItem(QuestConstants.SCORPION_CAGE_BARB_MONA))
			return true;
		return false;
	}
	
	boolean hasCatchedMonaScorpion(final Player player){
		if(player.hasItem(QuestConstants.SCORPION_CAGE_FULL) || player.hasItem(QuestConstants.SCORPION_CAGE_MONA) || player.hasItem(QuestConstants.SCORPION_CAGE_TAV_MONA) || player.hasItem(QuestConstants.SCORPION_CAGE_BARB_MONA))
			return true;
		return false;
	}
	
	public boolean handleItemOnNpc(final Player player, int npcId, int itemId, int stage){
		if(stage >= 2){
			if(npcId == QuestConstants.KHARID_SCORPION_TAV && itemId >= 456 && itemId < 463 && !hasCatchedTavScorpion(player)){
				int itm = QuestConstants.SCORPION_CAGE_TAV;
				if(hasCatchedBarbScorpion(player) && !hasCatchedMonaScorpion(player))
					itm = QuestConstants.SCORPION_CAGE_TAV_BARB;
				if(hasCatchedMonaScorpion(player) && !hasCatchedBarbScorpion(player))
					itm = QuestConstants.SCORPION_CAGE_TAV_MONA;
				if(hasCatchedMonaScorpion(player) && hasCatchedBarbScorpion(player))
					itm = QuestConstants.SCORPION_CAGE_FULL;
				player.getInventory().removeItem(new Item(itemId, 1));
				player.getInventory().addItem(new Item(itm, 1));
				player.getActionSender().sendMessage("You catch a scorpion!");
				Npc npc = Npc.getNpcById(npcId);
				CombatManager.endDeath(npc, player, false);
				return true;
			}
			if(npcId == QuestConstants.KHARID_SCORPION_BARB && itemId >= 456 && itemId < 463 && !hasCatchedBarbScorpion(player)){
				int itm = QuestConstants.SCORPION_CAGE_BARB;
				if(hasCatchedTavScorpion(player) && !hasCatchedMonaScorpion(player))
					itm = QuestConstants.SCORPION_CAGE_TAV_BARB;
				if(hasCatchedMonaScorpion(player) && !hasCatchedTavScorpion(player))
					itm = QuestConstants.SCORPION_CAGE_BARB_MONA;
				if(hasCatchedMonaScorpion(player) && hasCatchedTavScorpion(player))
					itm = QuestConstants.SCORPION_CAGE_FULL;
				player.getInventory().removeItem(new Item(itemId, 1));
				player.getInventory().addItem(new Item(itm, 1));
				player.getActionSender().sendMessage("You catch a scorpion!");
				Npc npc = Npc.getNpcById(npcId);
				CombatManager.endDeath(npc, player, false);
				return true;
			}
			if(npcId == QuestConstants.KHARID_SCORPION_MONA && itemId >= 456 && itemId < 463 && !hasCatchedMonaScorpion(player)){
				int itm = QuestConstants.SCORPION_CAGE_MONA;
				if(hasCatchedBarbScorpion(player) && !hasCatchedTavScorpion(player))
					itm = QuestConstants.SCORPION_CAGE_BARB_MONA;
				if(hasCatchedTavScorpion(player) && !hasCatchedBarbScorpion(player))
					itm = QuestConstants.SCORPION_CAGE_TAV_MONA;
				if(hasCatchedTavScorpion(player) && hasCatchedBarbScorpion(player))
					itm = QuestConstants.SCORPION_CAGE_FULL;
				player.getInventory().removeItem(new Item(itemId, 1));
				player.getInventory().addItem(new Item(itm, 1));
				player.getActionSender().sendMessage("You catch a scorpion!");
				Npc npc = Npc.getNpcById(npcId);
				CombatManager.endDeath(npc, player, false);
				return true;
			}
		}
		return false;
	}

	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 2117 && objectX == 2875 && objectY == 9799){//old wall
			if(stage >= 2){
				if(player.getPosition().getY() >= 9799)
					player.getActionSender().sendMessage("You've found a secret door");
				player.getActionSender().walkTo(0, player.getPosition().getY() < 9799 ? 1 : -1, true);
				player.getActionSender().walkThroughDoor(2117, 2875, 9799, 0);
				return true;
			}
		}
		return false;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.THORMAC){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hello I am Thormac the sorceror. I don't suppose you",
													"could be of assistance to me?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("What do you need assistance with?",
													"I'm a little busy.");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("What do you need assistance with?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("I've lost my pet scorpions. They're lesser Kharid",
													"scorpions, a very rare breed.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("I left their cage door open, now I don't know where",
													"they've gone.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("There's three of them, and they're quick little beasties.",
													"They're all over RuneScape.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendOption("So how would I go about catching them then?",
													"What's in it for me?",
													"I'm not interested then.");
					return true;
				}
				if(chatId == 8){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("So how would I go about catching them then?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Well I have a scorpion cage here which you can use to",
													"catch them in.",Dialogues.CONTENT);
					boolean b = false;
					for(int i = 456; i < 464; i++){
						if(player.hasItem(i))
							b = true;
					}
					if(!b){
						player.getInventory().addItemOrDrop(new Item(QuestConstants.SCORPION_CAGE_EMPTY, 1));
						player.getActionSender().sendMessage("Thormac gives you a cage.");
						player.getDialogue().setNextChatId(52);
					}
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("If you go up to the village of Seers, to the North of",
													"here, one of them will be able to tell you where the",
													"scorpions are now.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendOption("What's in it for me?",
													"Ok, I will do it then");
					return true;
				}
				if(chatId == 12){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Ok, I will do it then.", Dialogues.CONTENT);
						questStarted(player);
						player.getDialogue().endDialogue();
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 52){
					player.setStopPacket(true);
					player.getActionSender().removeInterfaces();
					final Tick timer = new Tick(4) {
						@Override
			            public void execute() {
							player.setStopPacket(false);
							Dialogues.sendDialogue(player, QuestConstants.THORMAC, 10, 0);
							stop();
						}
					};
					World.getTickManager().submit(timer);
					return true;
				}
			}//end of stage 0
			if(stage >= 2 && !player.getInventory().playerHasItem(QuestConstants.SCORPION_CAGE_FULL)){
				for(int i = 456; i < 464; i++){
					if(player.hasItem(i))
						return false;
				}
				if(chatId == 1){
					player.getInventory().addItemOrDrop(new Item(QuestConstants.SCORPION_CAGE_EMPTY, 1));
					player.getActionSender().sendMessage("Thormac gives you a cage.");
					player.getDialogue().sendNpcChat("If you go up to the village of Seers, to the North of",
													"here, one of them will be able to tell you where the",
													"scorpions are now.",Dialogues.CONTENT);
					return true;
				}
			}//end of stage 2
			if(stage == 3){
				if(!player.getInventory().playerHasItem(QuestConstants.SCORPION_CAGE_FULL)){
					return false;
				}
				if(chatId == 1){
					player.getDialogue().sendNpcChat("How goes your quest?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("I have retrieved all your scorpions.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Aha, my little scorpions home at last!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					if(player.getInventory().playerHasItem(QuestConstants.SCORPION_CAGE_FULL)){
						player.getInventory().removeItem(new Item(QuestConstants.SCORPION_CAGE_FULL, 1));
						questCompleted(player);
						player.getDialogue().dontCloseInterface();
						return true;
					}
				}
			}//end of stage 3
		}//end of npc 389
		if(npcId == QuestConstants.SEER){
			if(stage == 2){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Many greetings.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("I need to locate some scorpions.",
													"Your friend Thormac sent me to speak to you.",
													"I seek knowledge and power!");
					return true;
				}
				if(chatId == 3){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Your friend Thormac sent me to speak to you.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("What does the old fellow want?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("He's lost his valuable lesser Kharid scorpions.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("Well you have come to the right place, I am a master",
													"of animal detection.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Let me look into my looking glass.",Dialogues.CONTENT);
					player.getActionSender().sendMessage("The seer produces a small mirror");
					player.getDialogue().setNextChatId(52);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("I can see a scorpion that you seek. It would appear to",
													"be near some nasty spiders. I can see two coffins there",
													"as well.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("The scorpion seems to be going through some crack in",
													"the wall. It's gone into some sort of secret room.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Well see if you can find that scorpion then, and I'll try",
													"and get you some information on the others.",Dialogues.CONTENT);
					player.questStage[this.getId()] = 3;
					player.getDialogue().endDialogue();
					return true;
				}
				if(chatId == 52){
					player.getActionSender().removeInterfaces();
					player.setStopPacket(true);
					final Tick timer = new Tick(4) {
						@Override
			            public void execute() {
							player.getActionSender().sendMessage("The seer gazes into the mirror");
							World.getTickManager().submit(timer2);
							stop();
						}
						
						final Tick timer2 = new Tick(4) {
							@Override
				            public void execute() {
								player.getActionSender().sendMessage("The seer smoothes his hair with his hand");
								World.getTickManager().submit(timer3);
								stop();
							}
						};
						
						final Tick timer3 = new Tick(4) {
							@Override
				            public void execute() {
								player.setStopPacket(false);
								Dialogues.sendDialogue(player, QuestConstants.SEER, 8, 0);
								stop();
							}
						};
						
					};
					World.getTickManager().submit(timer);
					return true;
				}
			}//end of stage 2
		}//end of npc 388
		return false;
	}

}
