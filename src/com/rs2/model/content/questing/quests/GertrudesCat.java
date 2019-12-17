package com.rs2.model.content.questing.quests;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;

public class GertrudesCat extends Quest {
	
	final int rewardQP = 1;
	
	public GertrudesCat(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			String text[] = {"I can start this quest by speaking to Gertrude.",
							"She can be found to the west of Varrock."};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should go talk to the boys who can be found",
							"in the Varrock marketplace."};
			return text;	
		}
		if(stage == 3){
			String text[] = {"The boys told me to go to the abandoned lumber",
							"mill just beyond Jolly Boar Inn."};
			return text;	
		}
		if(stage == 4){
			String text[] = {"Maybe I should bring Fluffs something to eat."};
			return text;	
		}
		if(stage == 5){
			String text[] = {"I should go look for a kitten."};
			return text;	
		}
		if(stage == 6){
			String text[] = {"I should now go talk to Gertrude."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "1 Quest Point", "A kitten!", "1525 Cooking XP", "A chocolate cake", "A bowl of stew.", "Raise cats."};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		player.getActionSender().moveInterfaceComponent(12145, 60, 130);
		super.questCompleted_2(player);
		player.getActionSender().sendString("You have completed "+this.getDefinition().getName()+"!", 12144);
		player.getActionSender().sendString("1 Quest Point", 12150);
		player.getActionSender().sendString("A kitten!", 12151);
		player.getActionSender().sendString("1525 Cooking XP", 12152);
		player.getActionSender().sendString("A chocolate cake", 12153);
		player.getActionSender().sendString("A bowl of stew.", 12154);
		player.getActionSender().sendString("Raise cats.", 12155);
		//reward here!
		player.getSkill().addQuestExp(Skill.COOKING, 1525);
		//
		player.getActionSender().sendQuickSong(238, 320);
		player.getActionSender().sendString(""+player.getQuestPoints(), 12147);
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.PET_CAT);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 2618 && objectX == 3305 && objectY == 3493){//broken fence
			player.getActionSender().walkTo(0, player.getPosition().getY() < 3493 ? 2 : -2, true);
			player.getUpdateFlags().sendAnimation(882);
			return true;
		}
		return false;
	}
	
	public boolean handleFirstClickNpc(final Player player, int npcId, final int stage){
		if(npcId == QuestConstants.GERTRUDES_CAT){
			Npc npc = Npc.getNpcById(QuestConstants.GERTRUDES_CAT);
			if(stage >= 3){
				npc.getUpdateFlags().setForceChatMessage("Hisss!");
				player.getUpdateFlags().setForceChatMessage("Ouch!");
				player.setStopPacket(true);
				final Tick timer = new Tick(2) {
					@Override
		            public void execute() {
						if(stage == 3)
							player.getDialogue().sendStatement("Maybe the cat is thirsty?");
						if(stage == 4)
							player.getDialogue().sendStatement("Maybe the cat is hungry?");
						if(stage == 5)
							player.getDialogue().sendStatement("The cat seems afraid to leave.",
															"In the distance you can hear kittens mewing...");
						player.setStopPacket(false);
						stop();
					}
				};
				World.getTickManager().submit(timer);
				return true;
			}
		}
		if(npcId == QuestConstants.KITTEN_CRATE){
			if(stage == 5 && !player.hasItem(QuestConstants.FLUFFS_KITTEN)){
				player.getActionSender().sendMessage("You search the crate.");
				player.setStopPacket(true);
				final Tick timer = new Tick(3) {
					@Override
		            public void execute() {
						if(Misc.random_(5) == 0){
							player.getDialogue().sendStatement("You find a kitten! You carefully place it in your backpack.");
							player.getInventory().addItemOrDrop(new Item(QuestConstants.FLUFFS_KITTEN, 1));
						}else
							player.getActionSender().sendMessage("You find nothing.");
						player.setStopPacket(false);
						stop();
					}
				};
				World.getTickManager().submit(timer);
			}
		}
		return false;
	}
	
	public boolean handleItemOnNpc(final Player player, int npcId, int itemId, int stage){
		if(itemId == QuestConstants.BUCKET_OF_MILK && npcId == QuestConstants.GERTRUDES_CAT){
			Npc npc = Npc.getNpcById(QuestConstants.GERTRUDES_CAT);
			if(stage == 3){
				player.getInventory().replaceItemWithItem(new Item(QuestConstants.BUCKET_OF_MILK, 1), new Item(QuestConstants.BUCKET, 1));
				npc.getUpdateFlags().setForceChatMessage("Mew!");
				player.questStage[this.getId()] = 4;
				return true;
			}
		}
		if(itemId == QuestConstants.SEASONED_SARDINE && npcId == QuestConstants.GERTRUDES_CAT){
			Npc npc = Npc.getNpcById(QuestConstants.GERTRUDES_CAT);
			if(stage == 4){
				player.getInventory().removeItem(new Item(QuestConstants.SEASONED_SARDINE, 1));
				npc.getUpdateFlags().setForceChatMessage("Mew!");
				player.questStage[this.getId()] = 5;
				return true;
			}
		}
		if(itemId == QuestConstants.FLUFFS_KITTEN && npcId == QuestConstants.GERTRUDES_CAT){
			final Npc npc = Npc.getNpcById(QuestConstants.GERTRUDES_CAT);
			if(stage == 5){
				player.getInventory().removeItem(new Item(QuestConstants.FLUFFS_KITTEN, 1));
				player.getUpdateFlags().sendAnimation(827);
				final Npc npc1 = new Npc(QuestConstants.FLUFFS_OFFSPRING);
				NpcLoader.spawnNpcPos(player, player.getPosition(), npc1, false, false);
				npc.getUpdateFlags().setForceChatMessage("Purr...");
				npc1.getUpdateFlags().setForceChatMessage("Purr...");
				player.questStage[this.getId()] = 6;
				player.setStopPacket(true);
				final Tick timer = new Tick(2) {
					@Override
		            public void execute() {
						npc1.forceMovePath(new Position[] {new Position(3309, 3509,1)});
						npc.setFollowDistance(1);
						npc.setFollowingEntity(npc);
						World.getTickManager().submit(timer2);
						stop();
					}
					
					final Tick timer2 = new Tick(3) {
			            @Override
			            public void execute() {
			            	CombatManager.endDeath(npc, player, false);
			            	NpcLoader.destroyNpc(npc1);
			            	player.setStopPacket(false);
			            	player.getDialogue().sendStatement("Fluffs has run off home with her offspring.");
			                stop();
			            }
					};
			        
				};
				World.getTickManager().submit(timer);
				return true;
			}
		}
		return false;
	}
	
	public boolean handleItemOnItem(final Player player, int item1, int item2, int stage){
		if(stage != 0){
			if((item1 == QuestConstants.DOOGLE_LEAVES && item2 == QuestConstants.RAW_SARDINE) || (item1 == QuestConstants.RAW_SARDINE && item2 == QuestConstants.DOOGLE_LEAVES)){
				player.getInventory().removeItem(new Item(item1, 1));
	    		player.getInventory().removeItem(new Item(item2, 1));
	    		player.getInventory().addItem(new Item(QuestConstants.SEASONED_SARDINE, 1));
	    		player.getDialogue().sendStatement("You rub the doogle leaves over the sardine.");
				return true;
			}
		}
		return false;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.GERTRUDE){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello, are you ok?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("Do I look ok? Those kids drive me crazy.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("I'm sorry. It's just that I've lost her.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("Lost who?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Fluffs, poor Fluffs. She never hurt anyone.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendPlayerChat("Who's Fluffs?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("My beloved feline friend Fluffs. She's been purring by",
													"my side for almost a decade. Please, could you go",
													"search for her while I look over the kids?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendOption("Well, I suppose I could.",
													"What's in it for me?",
													"Sorry, I'm too busy to play pet rescue.");
					return true;
				}
				if(chatId == 9){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Well, I suppose I could.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(10);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Really? Thank you so much! I really have no idea",
													"where she could be!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("I think my sons, Shilop and Wilough, saw the cat last.",
													"They'll be out in the market place.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendPlayerChat("Alright then, I'll see what I can do.", Dialogues.CONTENT);
					questStarted(player);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 0
			if(stage == 6){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello Gertrude. Fluffs ran off with her kitten.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("You're back! Thank you! Thank you! Fluffs just came",
													"back! I think she was just upset as she couldn't find her",
													"kitten.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendStatement("Gertrude gives you a hug.");
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("If you hadn't found her kitten it would have died out",
													"there!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("That's ok, I like to do my bit.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("I don't know how to thank you. I have no real material",
													"possessions. I do have kittens! I can only really look",
													"after one.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendPlayerChat("Well, if it needs a home.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("I would sell it to my cousin in West Ardougne. I hear",
													"there's a rat epidemic there. But it's too far.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Here you go, look after her and thank you again!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Oh by the way, the kitten can live in your backpack,",
													"but to make it grow you must take it out and feed and",
													"stroke it often.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendStatement("Gertrude gives you a kitten.");
					return true;
				}
				if(chatId == 12){
					player.getDialogue().endDialogue();
					player.getPets().newPet(QuestConstants.PET_CAT, 768);
					player.setStopPacket(true);
					final Tick timer = new Tick(5) {
						@Override
			            public void execute() {
							player.getActionSender().sendMessage("... and some food!");
							player.getInventory().addItemOrDrop(new Item(QuestConstants.CHOCOLATE_CAKE, 1));
							player.getInventory().addItemOrDrop(new Item(QuestConstants.STEW, 1));
							World.getTickManager().submit(timer2);
							stop();
						}
						
						final Tick timer2 = new Tick(4) {
				            @Override
				            public void execute() {
				            	player.setStopPacket(false);
				            	questCompleted(player);
				                stop();
				            }
						};
				        
					};
					World.getTickManager().submit(timer);
					return false;
				}
			}//end of stage 6
		}//end of gertrude
		if(npcId == QuestConstants.WILOUGH){
			if(stage == 2){
				if(chatId == 1){
					player.getDialogue().sendPlayerChat("Hello there, I've been looking for you.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendNpcChat("I didn't mean to take it! I just forgot to pay.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendPlayerChat("What? I'm trying to help your mum find Fluffs.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("Ohh...well, in that case I might be able to help. Fluffs",
													"followed me to my secret play area, I haven't seen her",
													"since.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendPlayerChat("Where is this play area?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendNpcChat("If I told you that, it wouldn't be a secret.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 7){
					player.getDialogue().sendOption("Tell me sonny, or I will hurt you.",
													"What will make you tell me?",
													"Well never mind, it's Fluffs' loss.");
					return true;
				}
				if(chatId == 8){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("What will make you tell me?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(9);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 9){
					player.getDialogue().sendNpcChat("Well...now you ask, I am a bit short on cash.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendPlayerChat("How much?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendNpcChat("100 coins should cover it.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendPlayerChat("100 coins! Why should I pay you?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("You shouldn't, but I won't help otherwise. I never liked",
													"that cat anyway, so what do you say?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendOption("I'm not paying you a penny.",
													"Okay then, I'll pay.");
					return true;
				}
				if(chatId == 15){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Okay then, I'll pay.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(16);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 16){
					if(player.getInventory().playerHasItem(QuestConstants.COINS, 100)){
						player.getInventory().removeItem(new Item(QuestConstants.COINS, 100));
						player.getDialogue().sendItem1Line("You give the lad 100 coins.", new Item(QuestConstants.COINS, 100));
						player.questStage[this.getId()] = 3;
						return true;
					}
				}
			}//end of stage 2
			if(stage == 3){
				if(chatId == 17){
					player.getDialogue().sendPlayerChat("There you go, now where did you see Fluffs?", Dialogues.CONTENT);
					player.getDialogue().setNextChatId(1);
					return true;
				}
				if(chatId == 1){
					player.getDialogue().sendNpcChat("I play at an abandoned lumber mill to the north east.",
													"Just beyond the Jolly Boar Inn. I saw Fluffs running",
													"around in there.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("Anything else?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Well, you'll have to find the broken fence to get in. I'm",
													"sure you can manage that.",Dialogues.CONTENT);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 3
		}//end of wilough
		return false;
	}

}
