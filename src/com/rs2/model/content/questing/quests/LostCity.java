package com.rs2.model.content.questing.quests;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.content.skills.Tools.Tool;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.tick.Tick;

public class LostCity extends Quest {
	
	final int rewardQP = 3;
	
	public LostCity(int id) {
		super(id);
		super.setRewardQP(rewardQP);
	}
	
	public String[] getQuestScrollText(final Player player, int stage){
		if(stage == 0){
			int craftLvl = player.getSkill().getPlayerLevel(Skill.CRAFTING);
			int wcLvl = player.getSkill().getPlayerLevel(Skill.WOODCUTTING);
			String text[] = {"I can start this quest by speaking to the Adventurers in",
							"the Swamp just south of Lumbridge.",
							"To complete this quest I need:",
							(craftLvl >= 31 ? "@str@" : "")+"Level 31 Crafting",
							(wcLvl >= 36 ? "@str@" : "")+"Level 36 Woodcutting",
							"and be able to defeat a Level 101 Spirit without weapons"};
			return text;
		}
		if(stage == 2){
			String text[] = {"I should search for leprechaun hiding in the",
							"trees next to the swamp."};
			return text;	
		}
		if(stage == 3){
			String text[] = {"I should go to cave located in Entrana and",
							"look for a dramen tree, to make a dramen staff."};
			return text;	
		}
		if(stage == 4){
			String text[] = {"I should now try entering the shed in Lumbridge",
							"swamp while wearing the dramen staff."};
			return text;	
		}
		if(stage == 1){
			String text[] = {"Quest Completed!", "", "You were awarded:", "3 Quest Points", "Access to Zanaris"};
			return text;	
		}
		return null;
	}
	
	public void questCompleted(final Player player){
		super.questCompleted_2(player);
		super.questCompleted_defaultStuff(player);
		player.getActionSender().sendString("3 Quest Points", 12150);
		player.getActionSender().sendString("Access to Zanaris", 12151);
		player.getActionSender().sendString("", 12152);
		player.getActionSender().sendString("", 12153);
		player.getActionSender().sendString("", 12154);
		player.getActionSender().sendString("", 12155);
		player.getActionSender().sendItemOnInterface(12145, 250, QuestConstants.DRAMEN_STAFF);
		player.getActionSender().sendInterface(12140);
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		if(objectId == 2409 && objectX == 3138 && objectY == 3212){//leprechaun tree
			if(stage == 2){
				if(Npc.getNpcById(QuestConstants.SHAMUS) == null)
					NpcLoader.spawnNpc(player, new Npc(QuestConstants.SHAMUS), 3138, 3211, 0, -1, false, false);
				return true;
			}
		}
		if(objectId == 1292 && objectX == 2860 && objectY == 9734){//dramen tree
			if(stage == 3){
				final Tool axe = Tools.getTool(player, Skill.WOODCUTTING);
				if(axe == null) {
					player.getActionSender().sendMessage("You do not have an axe which you have the woodcutting level to use.");
					return true;
				}
				if(player.getSkill().getLevel()[Skill.WOODCUTTING] < 36) {
					player.getActionSender().sendMessage("You need a Woodcutting level of " + 36 + " to cut this tree.");
					return true;
				}
				int id = QuestConstants.TREE_SPIRIT;
				String say = "You must defeat me before touching the tree!";
				if (!NpcLoader.checkSpawn(player, id)) {
					Npc npc = new Npc(id);
					NpcLoader.spawnNpc2(player, npc, 2860, 9737, 0, -1, true, false);
					npc.getUpdateFlags().setForceChatMessage(say);
				}
				return true;
			}
		}
		if(objectId == 2406 && objectX == 3202 && objectY == 3169){//zanaris shed door
			if((stage == 4 || stage == 1) && player.getEquipment().getId(Constants.WEAPON) == QuestConstants.DRAMEN_STAFF){
				if (player.getTeleportation().attemptTeleportJewellery(new Position(2452, 4473, 0))) {
					if(stage == 4){
						player.getActionSender().sendMessage("The world starts to shimmer...");
						final Tick timer1 = new Tick(4) {
				            @Override
				            public void execute() {
				            	questCompleted(player);
				                stop();
				            }
						};
						World.getTickManager().submit(timer1);
					}
				}
			} else {
				player.getActionSender().sendMessage("The door seems to be locked.");
			}
			return true;
		}
		return false;
	}
	
	public boolean handleNpcDeath(final Player player, int npcId, int stage){
		if(npcId == QuestConstants.TREE_SPIRIT){
			if(stage == 3){
				player.getDialogue().sendStatement("With the Tree Spirit defeated you can now chop the tree.");
				player.questStage[this.getId()] = 4;
				return true;
			}
		}
		return false;
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		if(npcId == QuestConstants.WARRIOR){
			if(stage == 0){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Hello there traveller.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendOption("What are you camped out here for?",
													"Do you know any good adventures I can go on?");
					return true;
				}
				if(chatId == 3){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("What are you camped here for?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(4);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 4){
					player.getDialogue().sendNpcChat("We're looking for Zanaris...GAH! I mean we're not",
													"here for any particular reason at all.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendOption("Who's Zanaris?",
													"What's Zanaris?",
													"What makes you think it's out here?");
					return true;
				}
				if(chatId == 6){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("Who's Zanaris?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(7);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 7){
					player.getDialogue().sendNpcChat("Ahahahaha! Zanaris isn't a person! It's a magical hidden",
													"city filled with treasures and rich.. uh, nothing. It's",
													"nothing.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 8){
					player.getDialogue().sendOption("If it's hidden how are you planning to find it?",
													"There's no such thing.");
					return true;
				}
				if(chatId == 9){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("If it's hidden how are you planning to find it?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(10);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Well, we don't want to tell anyone else about that,",
													"because we don't want anyone else sharing in all that",
													"glory and treasure.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendOption("Please tell me.",
													"Looks like you don't know either.");
					return true;
				}
				if(chatId == 12){
					if(optionId == 2){
						player.getDialogue().sendPlayerChat("Well, it looks to me like YOU don't know EITHER",
															"seeing as you're all just sat around here.", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(13);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("Of course we know! We just haven't found which tree",
													"the stupid leprechaun's hiding in yet!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("GAH! I didn't mean to tell you that! Look, just forget I",
													"said anything okay?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendPlayerChat("So a leprechaun knows where Zanaris is eh?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 16){
					player.getDialogue().sendNpcChat("Ye.. uh, no. No, not at all. And even if he did - which",
													"he doesn't - he DEFINITELY ISN'T hiding in some",
													"tree around here. Nope, definitely not. Honestly.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 17){
					player.getDialogue().sendPlayerChat("Thanks for the help!", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 18){
					player.getDialogue().sendNpcChat("Help? What help? I didn't help! Please don't say I did,",
													"I'll get in trouble!",Dialogues.CONTENT);
					questStarted(player);
					player.getDialogue().endDialogue();
					return true;
				}
			}//end of stage 0
		}//end of npc 650
		if(npcId == QuestConstants.SHAMUS){
			if(stage == 2){
				if(chatId == 1){
					player.getDialogue().sendNpcChat("Ay yer elephant! Yer've caught me, to be sure!",
													"What would an elephant like yer be wanting wid ol'",
													"Shamus then?",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 2){
					player.getDialogue().sendPlayerChat("I want to find Zanaris.", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 3){
					player.getDialogue().sendNpcChat("Zanaris is it now? Well well well... Yer'll be needing to",
													"be going to that funny little shed out there in the",
													"swamp, so you will.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 4){
					player.getDialogue().sendPlayerChat("...but... I thought... Zanaris was a city...?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 5){
					player.getDialogue().sendNpcChat("Aye that it is!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 6){
					player.getDialogue().sendOption("How does it fit in a shed then?",
													"I've been in that shed, I didn't see a city.");
					return true;
				}
				if(chatId == 7){
					if(optionId == 1){
						player.getDialogue().sendPlayerChat("...How does it fit in a shed then?", Dialogues.CONTENT);
						player.getDialogue().setNextChatId(8);
						return true;
					}else{
						player.getDialogue().endDialogue();
						return false;
					}
				}
				if(chatId == 8){
					player.getDialogue().sendNpcChat("Ah yer stupid elephant! The city isn't IN the shed! The",
													"doorway to the shed is being a portal to Zanaris, so it",
													"is.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 9){
					player.getDialogue().sendPlayerChat("So I just walk into the shed and end up in Zanaris",
														"then?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 10){
					player.getDialogue().sendNpcChat("Oh, was I fergetting to say? Yer need to be carrying a",
													"Dramenwood staff to be getting there! Otherwise Yer'll",
													"just be ending up in the shed.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 11){
					player.getDialogue().sendPlayerChat("So where would I get a staff?", Dialogues.CONTENT);
					return true;
				}
				if(chatId == 12){
					player.getDialogue().sendNpcChat("Dramenwood staffs are crafted from branches of the",
													"Dramen tree, so they are. I hear there's a Dramen",
													"tree over on the island of Entrana in a cave",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 13){
					player.getDialogue().sendNpcChat("or some such. There would probably be a good place",
													"for an elephant like yer to be starting looking I reckon.",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 14){
					player.getDialogue().sendNpcChat("The monks are running a ship from Port Sarim to",
													"Entrana, I hear too. Now leave me alone yer elephant!",Dialogues.CONTENT);
					return true;
				}
				if(chatId == 15){
					player.getDialogue().sendStatement("The leprechaun magically disappears.");
					player.questStage[this.getId()] = 3;
					Npc npcR = Npc.getNpcById(QuestConstants.SHAMUS);
					npcR.setVisible(false);
		            World.unregister(npcR);
					return true;
				}
			}//end of stage 2
		}//end of npc 654
		return false;
	}

}
