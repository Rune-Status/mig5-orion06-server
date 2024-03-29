package com.rs2.model.content.dialogue;

import com.rs2.Constants;
import com.rs2.model.World;
import com.rs2.model.npcs.NpcDefinition;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class DialogueManager {

	private Player player;

	public DialogueManager(Player player) {
		this.player = player;
	}

    private int dialogueId;
    private int dialogueType;
    private int dialogueX;
    private int dialogueY;
    private int chatId;
    private int clickId;
    private int lastNpcTalk;
	/**
	 * @param chatId the chatId to set
	 */
	public void setChatId(int chatId) {
		this.chatId = chatId;
	}

	/**
	 * @param chatId the chatId to set
	 */
	public void setNextChatId(int chatId) {
		this.chatId = chatId - 1;
	}
	
	public void setClickId(int clickId) {
		this.clickId = clickId;
	}

	/**
	 * @return the chatId
	 */
	public int getChatId() {
		return chatId;
	}

	public void endDialogue() {
		chatId = 9001;
	}

	/**
	 * @param chatId the chatId to set
	 */
	public void dontCloseInterface() {
		chatId = -1;
	}

	public boolean checkEndDialogue() {
		return chatId > 9000 || chatId < 0 || dialogueId < 0;
	}

	/**
	 * @param dialogueId the dialogueId to set
	 */
	public void setDialogueId(int dialogueId) {
		this.dialogueId = dialogueId;
	}

	/**
	 * @return the dialogueId
	 */
	public int getDialogueId() {
		return dialogueId;
	}
	
	public void setDialogueType(int dialogueType) {
		this.dialogueType = dialogueType;
	}
	
	public int getDialogueType() {
		return dialogueType;
	}
	
	public void setDialogueX(int x) {
		this.dialogueX = x;
	}

	public int getDialogueX() {
		return dialogueX;
	}
	
	public void setDialogueY(int y) {
		this.dialogueY = y;
	}

	public int getDialogueY() {
		return dialogueY;
	}
	
	public int getClickId() {
		return clickId;
	}

	public void resetDialogue() {
		setChatId(0);
		setDialogueId(-1);
		setDialogueType(0);
		setDialogueX(-1);
		setDialogueY(-1);
	}

	/**
	 * The buttons for the option interfaces.
	 */
	public boolean optionButtons(int buttonId) {
		switch (buttonId) {// optionInterface, optionId
			case 2461 :// 2options, option1
			case 2471 :// 3options, option1
			case 2482 :// 4options, option1
			case 2494 :// 5options, option1
				if(getDialogueType() == 0)
					Dialogues.sendDialogue(player, getDialogueId(), getChatId() + 1, 1);
				if(getDialogueType() == 1)
					Dialogues.sendDialogueObject(getClickId(), player, getDialogueId(), getChatId() + 1, 1, getDialogueX(), getDialogueY());
				return true;
			case 2462 :// 2options, option2
			case 2472 :// 3options, option2
			case 2483 :// 4options, option2
			case 2495 :// 5options, option2
				if(getDialogueType() == 0)
					Dialogues.sendDialogue(player, getDialogueId(), getChatId() + 1, 2);
				if(getDialogueType() == 1)
					Dialogues.sendDialogueObject(getClickId(), player, getDialogueId(), getChatId() + 1, 2, getDialogueX(), getDialogueY());
				return true;
			case 2473 :// 3options, option3
			case 2484 :// 4options, option3
			case 2496 :// 5options, option3
				if(getDialogueType() == 0)
					Dialogues.sendDialogue(player, getDialogueId(), getChatId() + 1, 3);
				if(getDialogueType() == 1)
					Dialogues.sendDialogueObject(getClickId(), player, getDialogueId(), getChatId() + 1, 3, getDialogueX(), getDialogueY());
				return true;
			case 2485 :// 4options, option4
			case 2497 :// 5options, option4
				if(getDialogueType() == 0)
					Dialogues.sendDialogue(player, getDialogueId(), getChatId() + 1, 4);
				if(getDialogueType() == 1)
					Dialogues.sendDialogueObject(getClickId(), player, getDialogueId(), getChatId() + 1, 4, getDialogueX(), getDialogueY());
				return true;
			case 2498 :// 5options, option5
				if(getDialogueType() == 0)
					Dialogues.sendDialogue(player, getDialogueId(), getChatId() + 1, 5);
				if(getDialogueType() == 1)
					Dialogues.sendDialogueObject(getClickId(), player, getDialogueId(), getChatId() + 1, 5, getDialogueX(), getDialogueY());
				return true;
		}
		return false;
	}

	/**
	 * An information box.
	 */
	public void sendInformationBox(String title, String line1, String line2, String line3, String line4) {// check
		player.getActionSender().sendString(title, 6180);
		player.getActionSender().sendString(line1, 6181);
		player.getActionSender().sendString(line2, 6182);
		player.getActionSender().sendString(line3, 6183);
		player.getActionSender().sendString(line4, 6184);
		player.getActionSender().sendChatInterface(6179);
	}

	public void sendOptionWTitle(String title, String option1, String option2) {
		player.getActionSender().sendString(option1, 2461);
		player.getActionSender().sendString(option2, 2462);
		player.getActionSender().sendString(title, 2460);
		player.getActionSender().sendFrame171(1, 2465);//swords with short distance
		player.getActionSender().sendFrame171(0, 2468);//swords with long distance
		player.getActionSender().sendChatInterface(2459);
	}
	
	public void sendOptionWTitle(String title, String option1, String option2, String option3) {
		player.getActionSender().sendString(option1, 2471);
		player.getActionSender().sendString(option2, 2472);
		player.getActionSender().sendString(option3, 2473);
		player.getActionSender().sendString(title, 2470);
		player.getActionSender().sendFrame171(1, 2476);//swords with short distance
		player.getActionSender().sendFrame171(0, 2479);//swords with long distance
		player.getActionSender().sendChatInterface(2469);
	}

	public void sendOptionWTitle(String title, String option1, String option2, String option3, String option4) {
		player.getActionSender().sendString(option1, 2482);
		player.getActionSender().sendString(option2, 2483);
		player.getActionSender().sendString(option3, 2484);
		player.getActionSender().sendString(option4, 2485);
		player.getActionSender().sendString(title, 2481);
		player.getActionSender().sendFrame171(1, 2488);//swords with short distance
		player.getActionSender().sendFrame171(0, 2489);//swords with long distance
		player.getActionSender().sendChatInterface(2480);
	}

	public void sendOptionWTitle(String title, String option1, String option2, String option3, String option4, String option5) {
		player.getActionSender().sendString(option1, 2494);
		player.getActionSender().sendString(option2, 2495);
		player.getActionSender().sendString(option3, 2496);
		player.getActionSender().sendString(option4, 2497);
		player.getActionSender().sendString(option5, 2498);
		player.getActionSender().sendString(title, 2493);
		player.getActionSender().sendFrame171(1, 2501);//swords with short distance
		player.getActionSender().sendFrame171(0, 2502);//swords with long distance
		player.getActionSender().sendChatInterface(2492);
	}
	/**
	 * Option selection.
	 */
	public void sendOption(String option1, String option2) {
		player.getActionSender().sendString(option1, 2461);
		player.getActionSender().sendString(option2, 2462);
		player.getActionSender().sendString("Select an Option", 2460);
		player.getActionSender().sendFrame171(0, 2465);//swords with short distance
		player.getActionSender().sendFrame171(1, 2468);//swords with long distance
		player.getActionSender().sendChatInterface(2459);
	}

	public void sendOption(String option1, String option2, String option3) {
		player.getActionSender().sendString(option1, 2471);
		player.getActionSender().sendString(option2, 2472);
		player.getActionSender().sendString(option3, 2473);
		player.getActionSender().sendString("Select an Option", 2470);
		player.getActionSender().sendFrame171(0, 2476);//swords with short distance
		player.getActionSender().sendFrame171(1, 2479);//swords with long distance
		player.getActionSender().sendChatInterface(2469);
	}

	public void sendOption(String option1, String option2, String option3, String option4) {
		player.getActionSender().sendString(option1, 2482);
		player.getActionSender().sendString(option2, 2483);
		player.getActionSender().sendString(option3, 2484);
		player.getActionSender().sendString(option4, 2485);
		player.getActionSender().sendString("Select an Option", 2481);
		player.getActionSender().sendFrame171(0, 2488);//swords with short distance
		player.getActionSender().sendFrame171(1, 2489);//swords with long distance
		player.getActionSender().sendChatInterface(2480);
	}

	public void sendOption(String option1, String option2, String option3, String option4, String option5) {
		player.getActionSender().sendString(option1, 2494);
		player.getActionSender().sendString(option2, 2495);
		player.getActionSender().sendString(option3, 2496);
		player.getActionSender().sendString(option4, 2497);
		player.getActionSender().sendString(option5, 2498);
		player.getActionSender().sendString("Select an Option", 2493);
		player.getActionSender().sendFrame171(0, 2501);//swords with short distance
		player.getActionSender().sendFrame171(1, 2502);//swords with long distance
		player.getActionSender().sendChatInterface(2492);
	}

	public void sendOption(String[] string) {
		switch (string.length) {
			case 2 :
				sendOption(string[0], string[1]);
				break;
			case 3 :
				sendOption(string[0], string[1], string[2]);
				break;
			case 4 :
				sendOption(string[0], string[1], string[2], string[3]);
				break;
			case 5 :
				sendOption(string[0], string[1], string[2], string[3], string[4]);
				break;
		}
	}

	/**
	 * Statements.
	 */
	public void sendStatement(String[] lines) {
		switch (lines.length) {
			case 1 :
				sendStatement(lines[0]);
				break;
			case 2 :
				sendStatement(lines[0], lines[1]);
				break;
			case 3 :
				sendStatement(lines[0], lines[1], lines[2]);
				break;
			case 4 :
				sendStatement(lines[0], lines[1], lines[2], lines[3]);
				break;
			case 5 :
				sendStatement(lines[0], lines[1], lines[2], lines[3], lines[4]);
				break;
		}
	}

	public void sendStatement(String line1) {
		player.getActionSender().sendString(line1, 357);
		player.getActionSender().sendChatInterface(356);
		if(player.questStage[0] != 1)
			player.getDialogue().endDialogue();
	}

	public void sendStatement(String line1, int itemId) {
		player.getActionSender().sendItemOnInterface(307, 150, itemId);
		player.getActionSender().sendString(line1, 308);
		player.getActionSender().sendChatInterface(306);
		if(player.questStage[0] != 1)
			player.getDialogue().endDialogue();
	}

	public void sendStatement(String line1, String line2) {
		player.getActionSender().sendString(line1, 360);
		player.getActionSender().sendString(line2, 361);
		player.getActionSender().sendChatInterface(359);
		if(player.questStage[0] != 1)
			player.getDialogue().endDialogue();
	}

	public void sendStatement(String line1, String line2, String line3) {
		player.getActionSender().sendString(line1, 364);
		player.getActionSender().sendString(line2, 365);
		player.getActionSender().sendString(line3, 366);
		player.getActionSender().sendChatInterface(363);
		if(player.questStage[0] != 1)
			player.getDialogue().endDialogue();
	}

	public void sendStatement(String line1, String line2, String line3, String line4) {
		player.getActionSender().sendString(line1, 369);
		player.getActionSender().sendString(line2, 370);
		player.getActionSender().sendString(line3, 371);
		player.getActionSender().sendString(line4, 372);
		player.getActionSender().sendChatInterface(368);
		if(player.questStage[0] != 1)
			player.getDialogue().endDialogue();
	}

	public void sendStatement(String line1, String line2, String line3, String line4, String line5) {
		player.getActionSender().sendString(line1, 375);
		player.getActionSender().sendString(line2, 376);
		player.getActionSender().sendString(line3, 377);
		player.getActionSender().sendString(line4, 378);
		player.getActionSender().sendString(line5, 379);
		player.getActionSender().sendChatInterface(374);
		if(player.questStage[0] != 1)
			player.getDialogue().endDialogue();
	}

	/**
	 * Timed statements. These statements have no close/click options, so should
	 * only be used with a timer.
	 */
	public void sendTimedStatement(String line1) {
		player.getActionSender().sendString(line1, 12789);
		player.getActionSender().sendChatInterface(12788);
	}

	public void sendTimedStatement(String line1, String line2) {
		player.getActionSender().sendString(line1, 12791);
		player.getActionSender().sendString(line2, 12792);
		player.getActionSender().sendChatInterface(12790);
	}

	public void sendTimedStatement(String line1, String line2, String line3) {
		player.getActionSender().sendString(line1, 12794);
		player.getActionSender().sendString(line2, 12795);
		player.getActionSender().sendString(line3, 12796);
		player.getActionSender().sendChatInterface(12793);
	}

	public void sendTimedStatement(String line1, String line2, String line3, String line4) {
		player.getActionSender().sendString(line1, 12798);
		player.getActionSender().sendString(line2, 12799);
		player.getActionSender().sendString(line3, 12800);
		player.getActionSender().sendString(line4, 12801);
		player.getActionSender().sendChatInterface(12797);
	}

	public void sendTimedStatement(String line1, String line2, String line3, String line4, String line5) {
		player.getActionSender().sendString(line1, 12803);
		player.getActionSender().sendString(line2, 12804);
		player.getActionSender().sendString(line3, 12805);
		player.getActionSender().sendString(line4, 12806);
		player.getActionSender().sendString(line5, 12807);
		player.getActionSender().sendChatInterface(12802);
	}

	/**
	 * NPC dialogue.
	 */
	public void sendNpcChat(String[] lines, int emotion) {
		switch (lines.length) {
			case 1 :
				sendNpcChat(lines[0], emotion);
				break;
			case 2 :
				sendNpcChat(lines[0], lines[1], emotion);
				break;
			case 3 :
				sendNpcChat(lines[0], lines[1], lines[2], emotion);
				break;
			case 4 :
				sendNpcChat(lines[0], lines[1], lines[2], lines[3], emotion);
				break;
		}
	}

	/**
	 * NPC dialogue.
	 */
	public void sendNpcChat(String line1, int emotion) {
		int id = getLastNpcTalk() < 0 || getLastNpcTalk() > Constants.MAX_NPC_ID ? 0 : getLastNpcTalk();
		NpcDefinition def = World.getDefinitions()[id];
		String npcName = def.getName();
		player.getActionSender().sendDialogueAnimation(4883, emotion);
		player.getActionSender().sendString(npcName, 4884);
		player.getActionSender().sendString(line1, 4885);
		player.getActionSender().sendNPCDialogueHead(id, 4883);
		player.getActionSender().sendChatInterface(4882);
	}

	public void sendNpcChat(String line1, String line2, int emotion) {
		int id = getLastNpcTalk() < 0 || getLastNpcTalk() > Constants.MAX_NPC_ID ? 0 : getLastNpcTalk();
		NpcDefinition def = World.getDefinitions()[id];
		String npcName = def.getName();
		player.getActionSender().sendDialogueAnimation(4888, emotion);
		player.getActionSender().sendString(npcName, 4889);
		player.getActionSender().sendString(line1, 4890);
		player.getActionSender().sendString(line2, 4891);
		player.getActionSender().sendNPCDialogueHead(id, 4888);
		player.getActionSender().sendChatInterface(4887);
	}

	public void sendNpcChat(String line1, String line2, String line3, int emotion) {
		int id = getLastNpcTalk() < 0 || getLastNpcTalk() > Constants.MAX_NPC_ID ? 0 : getLastNpcTalk();
		NpcDefinition def = World.getDefinitions()[id];
		String npcName = def.getName();
		player.getActionSender().sendDialogueAnimation(4894, emotion);
		player.getActionSender().sendString(npcName, 4895);
		player.getActionSender().sendString(line1, 4896);
		player.getActionSender().sendString(line2, 4897);
		player.getActionSender().sendString(line3, 4898);
		player.getActionSender().sendNPCDialogueHead(id, 4894);
		player.getActionSender().sendChatInterface(4893);
	}

	public void sendNpcChat(String line1, String line2, String line3, String line4, int emotion) {
		int id = getLastNpcTalk() < 0 || getLastNpcTalk() > Constants.MAX_NPC_ID ? 0 : getLastNpcTalk();
		NpcDefinition def = World.getDefinitions()[id];
		String npcName = def.getName();
		player.getActionSender().sendDialogueAnimation(4901, emotion);
		player.getActionSender().sendString(npcName, 4902);
		player.getActionSender().sendString(line1, 4903);
		player.getActionSender().sendString(line2, 4904);
		player.getActionSender().sendString(line3, 4905);
		player.getActionSender().sendString(line4, 4906);
		player.getActionSender().sendNPCDialogueHead(id, 4901);
		player.getActionSender().sendChatInterface(4900);
	}

	/**
	 * Timed NPC dialogue. These NPC dialogues have no close/click options, so
	 * should only be used with a timer.
	 */
	public void sendTimedNpcChat(String line1, String line2, int emotion) {
		int id = getLastNpcTalk() < 0 || getLastNpcTalk() > Constants.MAX_NPC_ID ? 0 : getLastNpcTalk();
		NpcDefinition def = World.getDefinitions()[id];
		String npcName = def.getName();
		player.getActionSender().sendDialogueAnimation(12379, emotion);
		player.getActionSender().sendString(npcName, 12380);
		player.getActionSender().sendString(line1, 12381);
		player.getActionSender().sendString(line2, 12382);
		player.getActionSender().sendNPCDialogueHead(id, 12379);
		player.getActionSender().sendChatInterface(12378);
	}

	public void sendTimedNpcChat(String line1, String line2, String line3, int emotion) {
		int id = getLastNpcTalk() < 0 || getLastNpcTalk() > Constants.MAX_NPC_ID ? 0 : getLastNpcTalk();
		NpcDefinition def = World.getDefinitions()[id];
		String npcName = def.getName();
		player.getActionSender().sendDialogueAnimation(12384, emotion);
		player.getActionSender().sendString(npcName, 12385);
		player.getActionSender().sendString(line1, 12386);
		player.getActionSender().sendString(line2, 12387);
		player.getActionSender().sendString(line3, 12388);
		player.getActionSender().sendNPCDialogueHead(id, 12384);
		player.getActionSender().sendChatInterface(12383);
	}

	public void sendTimedNpcChat(String line1, String line2, String line3, String line4, int emotion) {
		int id = getLastNpcTalk() < 0 || getLastNpcTalk() > Constants.MAX_NPC_ID ? 0 : getLastNpcTalk();
		NpcDefinition def = World.getDefinitions()[id];
		String npcName = def.getName();
		player.getActionSender().sendDialogueAnimation(11892, emotion);
		player.getActionSender().sendString(npcName, 11893);
		player.getActionSender().sendString(line1, 11894);
		player.getActionSender().sendString(line2, 11895);
		player.getActionSender().sendString(line3, 11896);
		player.getActionSender().sendString(line4, 11897);
		player.getActionSender().sendNPCDialogueHead(id, 11892);
		player.getActionSender().sendChatInterface(11891);
	}

	/**
	 * Player dialogue.
	 */
	public void sendPlayerChat(String line1, int emotion) {
		player.getActionSender().sendDialogueAnimation(969, emotion);
		player.getActionSender().sendString(Misc.formatPlayerName(player.getUsername()), 970);
		player.getActionSender().sendString(line1, 971);
		player.getActionSender().sendPlayerDialogueHead(969);
		player.getActionSender().sendChatInterface(968);
	}

	public void sendPlayerChat(String line1, String line2, int emotion) {
		player.getActionSender().sendDialogueAnimation(974, emotion);
		player.getActionSender().sendString(Misc.formatPlayerName(player.getUsername()), 975);
		player.getActionSender().sendString(line1, 976);
		player.getActionSender().sendString(line2, 977);
		player.getActionSender().sendPlayerDialogueHead(974);
		player.getActionSender().sendChatInterface(973);
	}

	public void sendPlayerChat(String line1, String line2, String line3, int emotion) {
		player.getActionSender().sendDialogueAnimation(980, emotion);
		player.getActionSender().sendString(Misc.formatPlayerName(player.getUsername()), 981);
		player.getActionSender().sendString(line1, 982);
		player.getActionSender().sendString(line2, 983);
		player.getActionSender().sendString(line3, 984);
		player.getActionSender().sendPlayerDialogueHead(980);
		player.getActionSender().sendChatInterface(979);
	}

	public void sendPlayerChat(String line1, String line2, String line3, String line4, int emotion) {
		player.getActionSender().sendDialogueAnimation(987, emotion);
		player.getActionSender().sendString(Misc.formatPlayerName(player.getUsername()), 988);
		player.getActionSender().sendString(line1, 989);
		player.getActionSender().sendString(line2, 990);
		player.getActionSender().sendString(line3, 991);
		player.getActionSender().sendString(line4, 992);
		player.getActionSender().sendPlayerDialogueHead(987);
		player.getActionSender().sendChatInterface(986);
	}

	/**
	 * Timed player dialogue. These player dialogues have no close/click
	 * options, so should only be used with a timer.
	 */
	public void sendTimedPlayerChat(String line1, int emotion) {
		player.getActionSender().sendDialogueAnimation(12774, emotion);
		player.getActionSender().sendString(Misc.formatPlayerName(player.getUsername()), 12775);
		player.getActionSender().sendString(line1, 12776);
		player.getActionSender().sendPlayerDialogueHead(12774);
		player.getActionSender().sendChatInterface(12773);
	}

	public void sendTimedPlayerChat(String line1, String line2, int emotion) {
		player.getActionSender().sendDialogueAnimation(12778, emotion);
		player.getActionSender().sendString(Misc.formatPlayerName(player.getUsername()), 12779);
		player.getActionSender().sendString(line1, 12780);
		player.getActionSender().sendString(line2, 12781);
		player.getActionSender().sendPlayerDialogueHead(12778);
		player.getActionSender().sendChatInterface(12777);
	}

	public void sendTimedPlayerChat(String line1, String line2, String line3, int emotion) {
		player.getActionSender().sendDialogueAnimation(12783, emotion);
		player.getActionSender().sendString(Misc.formatPlayerName(player.getUsername()), 12784);
		player.getActionSender().sendString(line1, 12785);
		player.getActionSender().sendString(line2, 12786);
		player.getActionSender().sendString(line3, 12787);
		player.getActionSender().sendPlayerDialogueHead(12783);
		player.getActionSender().sendChatInterface(12782);
	}

	public void sendTimedPlayerChat(String line1, String line2, String line3, String line4, int emotion) {
		player.getActionSender().sendDialogueAnimation(11885, emotion);
		player.getActionSender().sendString(Misc.formatPlayerName(player.getUsername()), 11886);
		player.getActionSender().sendString(line1, 11887);
		player.getActionSender().sendString(line2, 11888);
		player.getActionSender().sendString(line3, 11889);
		player.getActionSender().sendString(line4, 11890);
		player.getActionSender().sendPlayerDialogueHead(11885);
		player.getActionSender().sendChatInterface(11884);
	}

	public void sendStartInfo(String title, String text, String text1, String text2, String text3, boolean send) {
		player.getActionSender().sendString(title, 6180);
		player.getActionSender().sendString(text, 6181);
		player.getActionSender().sendString(text1, 6182);
		player.getActionSender().sendString(text2, 6183);
		player.getActionSender().sendString(text3, 6184);
		if (send)
			player.getActionSender().sendChatboxOverlay(6179);
	}

	public void sendTutorialIslandWaitingInfo(String title, String text, String text1, String text2, String text3) {
		player.getActionSender().sendString(title, 6180);
		player.getActionSender().sendString(text, 6181);
		player.getActionSender().sendString(text1, 6182);
		player.getActionSender().sendString(text2, 6183);
		player.getActionSender().sendString(text3, 6184);
	}

	public void sendScrollDownInfo(String title, String[] lines, boolean send) {
		if (!send)
			return;
		int[] childIds = {18789, 18790, 18791, 18794, 18796, 18797, 18798, 18799, 18800, 18801, 18802, 18803, 18804, 18805, 18806, 18807, 18808};
		player.getActionSender().sendString(title, 18795);
		// cleaning the interface
		for (int i = 0; i < childIds.length; i++)
			player.getActionSender().sendString("", childIds[i]);
		// putting the lines
		for (int i = 0; i < lines.length; i++)
			player.getActionSender().sendString(lines[i], childIds[i]);
		player.getActionSender().sendChatboxOverlay(18786);
	}

	//old, uses fletch lvl up interface
	/*public void sendGiveItemNpc(String text1, String text2, Item item1, Item item2) {
		player.getActionSender().sendString(text1, 6232);
		player.getActionSender().sendString(text2, 6233);
		player.getActionSender().sendItemOnInterface(6235, 170, item1.getId());
		player.getActionSender().sendItemOnInterface(6236, 170, item2.getId());
		player.getActionSender().sendChatInterface(6231);
	}*/
	
	public void send2Items2Lines(String text1, String text2, Item item1, Item item2) {
		player.getActionSender().sendString("", 4953);
		player.getActionSender().sendString(text1, 4952);
		player.getActionSender().sendString(text2, 4955);
		player.getActionSender().sendString("", 4956);
		player.getActionSender().sendItemOnInterface(4951, 170, item1.getId());
		player.getActionSender().sendItemOnInterface(4957, 170, item2.getId());
		player.getActionSender().sendChatInterface(4950);
	}

	public void sendItem1Line(String text, Item item) {
		player.getActionSender().sendString(text, 308);
		player.getActionSender().sendItemOnInterface(307, 200, item.getId());
		player.getActionSender().sendChatInterface(306);
	}
	
	public void sendItem3Lines(String text, String text2, String text3, Item item) {
		player.getActionSender().sendString(text, 318);
		player.getActionSender().sendString(text2, 317);
		player.getActionSender().sendString(text3, 320);
		player.getActionSender().sendItemOnInterface(316, 200, item.getId());
		player.getActionSender().sendChatInterface(315);
	}

	/**
	 * @param lastNpcTalk the lastNpcTalk to set
	 */
	public void setLastNpcTalk(int lastNpcTalk) {
		this.lastNpcTalk = lastNpcTalk;
	}

	/**
	 * @return the lastNpcTalk
	 */
	public int getLastNpcTalk() {
		return lastNpcTalk;
	}

}
