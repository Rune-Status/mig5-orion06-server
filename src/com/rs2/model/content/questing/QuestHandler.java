package com.rs2.model.content.questing;

import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.players.Player;

public class QuestHandler {

	private Player player;
	
	public QuestHandler(Player player) {
		this.player = player;
	}
	
	public void cleanQuestScroll(){
		player.getActionSender().sendString("", 8145);
		for(int id2 = 8147; id2 <= 8195; id2++){
			player.getActionSender().sendString("", id2);
		}
		for(int id2 = 12174; id2 <= 12223; id2++){
			player.getActionSender().sendString("", id2);
		}
	}
	
	public void setQuestScrollText(int id){
		Quest quest = QuestDefinition.getQuest(id);
		String text[] = quest.getQuestScrollText(player, player.questStage[id]);
		player.getActionSender().sendString(text[0], 8145);
		for(int id2 = 1; id2 < text.length; id2++){
			player.getActionSender().sendString(text[id2], 8146+id2);
		}
	}
	
	public boolean handleItemOnItem(final int item1, final int item2) {
		for(int id = 0; id < SpecialEventDefinition.EVENT_COUNT; id++){
			SpecialEvent event = SpecialEventDefinition.getEvent(id);
			if(event.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(event.handleItemOnItem(player, item1, item2, player.holidayEventStage[id]))
				return true;
		}
		for(int id = 1; id < QuestDefinition.QUEST_COUNT; id++){
			Quest quest = QuestDefinition.getQuest(id);
			if(quest.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(quest.handleItemOnItem(player, item1, item2, player.questStage[id]))
				return true;
		}
		return false;
	}
	
	public boolean handleItemDrop(final int item) {
		for(int id = 0; id < SpecialEventDefinition.EVENT_COUNT; id++){
			SpecialEvent event = SpecialEventDefinition.getEvent(id);
			if(event.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(event.handleItemDrop(player, item, player.holidayEventStage[id]))
				return true;
		}
		for(int id = 1; id < QuestDefinition.QUEST_COUNT; id++){
			Quest quest = QuestDefinition.getQuest(id);
			if(quest.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(quest.handleItemDrop(player, item, player.questStage[id]))
				return true;
		}
		return false;
	}
	
	public boolean handleFirstClickNpc(final int npcId) {
		for(int id = 0; id < SpecialEventDefinition.EVENT_COUNT; id++){
			SpecialEvent event = SpecialEventDefinition.getEvent(id);
			if(event.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(event.handleFirstClickNpc(player, npcId, player.holidayEventStage[id]))
				return true;
		}
		for(int id = 1; id < QuestDefinition.QUEST_COUNT; id++){
			Quest quest = QuestDefinition.getQuest(id);
			if(quest.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(quest.handleFirstClickNpc(player, npcId, player.questStage[id]))
				return true;
		}
		return false;
	}
	
	public boolean handleFirstClickItem(final int interfaceId, final int itemId) {
		for(int id = 0; id < SpecialEventDefinition.EVENT_COUNT; id++){
			SpecialEvent event = SpecialEventDefinition.getEvent(id);
			if(event.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(event.handleFirstClickItem(player, interfaceId, itemId, player.holidayEventStage[id]))
				return true;
		}
		for(int id = 1; id < QuestDefinition.QUEST_COUNT; id++){
			Quest quest = QuestDefinition.getQuest(id);
			if(quest.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(quest.handleFirstClickItem(player, interfaceId, itemId, player.questStage[id]))
				return true;
		}
		return false;
	}
	
	public boolean handleFirstClickObject(final int objectId, final int objectX, final int objectY) {
		for(int id = 0; id < SpecialEventDefinition.EVENT_COUNT; id++){
			SpecialEvent event = SpecialEventDefinition.getEvent(id);
			if(event.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(event.handleFirstClickObject(player, objectId, objectX, objectY, player.holidayEventStage[id]))
				return true;
		}
		for(int id = 0; id < QuestDefinition.QUEST_COUNT; id++){
			Quest quest = QuestDefinition.getQuest(id);
			if(quest.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(quest.handleFirstClickObject(player, objectId, objectX, objectY, player.questStage[id]))
				return true;
		}
		return false;
	}
	
	public boolean handleSecondClickObject(final int objectId, final int objectX, final int objectY) {
		for(int id = 0; id < SpecialEventDefinition.EVENT_COUNT; id++){
			SpecialEvent event = SpecialEventDefinition.getEvent(id);
			if(event.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(event.handleSecondClickObject(player, objectId, objectX, objectY, player.holidayEventStage[id]))
				return true;
		}
		for(int id = 0; id < QuestDefinition.QUEST_COUNT; id++){
			Quest quest = QuestDefinition.getQuest(id);
			if(quest.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(quest.handleSecondClickObject(player, objectId, objectX, objectY, player.questStage[id]))
				return true;
		}
		return false;
	}
	
	public boolean pickQuestItem(final int itemId) {
		for(int id = 0; id < SpecialEventDefinition.EVENT_COUNT; id++){
			SpecialEvent event = SpecialEventDefinition.getEvent(id);
			if(event.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(event.getQuestPickups(player, itemId, player.holidayEventStage[id]))
				return true;
		}
		for(int id = 1; id < QuestDefinition.QUEST_COUNT; id++){
			Quest quest = QuestDefinition.getQuest(id);
			if(quest.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(quest.getQuestPickups(player, itemId, player.questStage[id]))
				return true;
		}
		return false;
	}
	
	public boolean useQuestItemOnObject(int item, int oid){
		for(int id = 0; id < SpecialEventDefinition.EVENT_COUNT; id++){
			SpecialEvent event = SpecialEventDefinition.getEvent(id);
			if(event.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(event.useQuestItemOnObject(player, item, oid, player.holidayEventStage[id]))
				return true;
		}
		for(int id = 1; id < QuestDefinition.QUEST_COUNT; id++){
			Quest quest = QuestDefinition.getQuest(id);
			if(quest.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(quest.useQuestItemOnObject(player, item, oid, player.questStage[id]))
				return true;
		}
		return false;
	}
	
	public boolean handleItemOnNpc(int npcId, int itemId){
		for(int id = 0; id < SpecialEventDefinition.EVENT_COUNT; id++){
			SpecialEvent event = SpecialEventDefinition.getEvent(id);
			if(event.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(event.handleItemOnNpc(player, npcId, itemId, player.holidayEventStage[id]))
				return true;
		}
		for(int id = 1; id < QuestDefinition.QUEST_COUNT; id++){
			Quest quest = QuestDefinition.getQuest(id);
			if(quest.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(quest.handleItemOnNpc(player, npcId, itemId, player.questStage[id]))
				return true;
		}
		return false;
	}
	
	public boolean allowedToAttackNpc(int npcId){
		for(int id = 0; id < SpecialEventDefinition.EVENT_COUNT; id++){
			SpecialEvent event = SpecialEventDefinition.getEvent(id);
			if(event.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(!event.allowedToAttackNpc(player, npcId, player.holidayEventStage[id]))
				return false;
		}
		for(int id = 1; id < QuestDefinition.QUEST_COUNT; id++){
			Quest quest = QuestDefinition.getQuest(id);
			if(quest.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(!quest.allowedToAttackNpc(player, npcId, player.questStage[id]))
				return false;
		}
		return true;
	}
	
	public boolean checkAreas(){
		for(int id = 0; id < SpecialEventDefinition.EVENT_COUNT; id++){
			SpecialEvent event = SpecialEventDefinition.getEvent(id);
			if(event.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(event.checkAreas(player, player.holidayEventStage[id]))
				return true;
		}
		for(int id = 1; id < QuestDefinition.QUEST_COUNT; id++){
			Quest quest = QuestDefinition.getQuest(id);
			if(quest.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(quest.checkAreas(player, player.questStage[id]))
				return true;
		}
		return false;
	}
	
	public boolean controlDying(Entity attacker, Entity victim){
		for(int id = 0; id < SpecialEventDefinition.EVENT_COUNT; id++){
			SpecialEvent event = SpecialEventDefinition.getEvent(id);
			if(event.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(event.controlDying(attacker, victim, player.holidayEventStage[id]))
				return true;
		}
		for(int id = 1; id < QuestDefinition.QUEST_COUNT; id++){
			Quest quest = QuestDefinition.getQuest(id);
			if(quest.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(quest.controlDying(attacker, victim, player.questStage[id]))
				return true;
		}
		return false;
	}
	
	public int controlCombatDamage(Entity attacker, Entity victim){
		for(int id = 0; id < SpecialEventDefinition.EVENT_COUNT; id++){
			SpecialEvent event = SpecialEventDefinition.getEvent(id);
			if(event.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(event.controlCombatDamage(attacker, victim, player.holidayEventStage[id]) != -1)
				return event.controlCombatDamage(attacker, victim, player.holidayEventStage[id]);
		}
		for(int id = 1; id < QuestDefinition.QUEST_COUNT; id++){
			Quest quest = QuestDefinition.getQuest(id);
			if(quest.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(quest.controlCombatDamage(attacker, victim, player.questStage[id]) != -1)
				return quest.controlCombatDamage(attacker, victim, player.questStage[id]);
		}
		return -1;
	}
	
	public boolean handleNpcDeath(int npcId){
		for(int id = 0; id < SpecialEventDefinition.EVENT_COUNT; id++){
			SpecialEvent event = SpecialEventDefinition.getEvent(id);
			if(event.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(event.handleNpcDeath(player, npcId, player.holidayEventStage[id]))
				return true;
		}
		for(int id = 1; id < QuestDefinition.QUEST_COUNT; id++){
			Quest quest = QuestDefinition.getQuest(id);
			if(quest.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(quest.handleNpcDeath(player, npcId, player.questStage[id]))
				return true;
		}
		return false;
	}
	
	public void setTutorialStage(){
		int id = 0;
		Quest quest = QuestDefinition.getQuest(id);
		quest.setStage(player, player.questStage[id]);
	}
	
	public boolean findQuestDrop(int npcId, Player killer, Position deathPos){
		for(int id = 0; id < SpecialEventDefinition.EVENT_COUNT; id++){
			SpecialEvent event = SpecialEventDefinition.getEvent(id);
			if(event.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(event.getQuestDrop(killer, npcId, deathPos, player.holidayEventStage[id]))
				return true;
		}
		for(int id = 1; id < QuestDefinition.QUEST_COUNT; id++){
			Quest quest = QuestDefinition.getQuest(id);
			if(quest.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(quest.getQuestDrop(killer, npcId, deathPos, player.questStage[id]))
					return true;
		}
		return false;
	}
	
	public boolean findQuestDialogObject(int clickId, int Id, int chatId, int optionId, int npcChatId, int x, int y){
		for(int id = 0; id < SpecialEventDefinition.EVENT_COUNT; id++){
			SpecialEvent event = SpecialEventDefinition.getEvent(id);
			if(event.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(event.getObjectDialog(clickId, player, Id, chatId, optionId, npcChatId, x, y, player.holidayEventStage[id]))
				return true;
		}
		for(int id = 0; id < QuestDefinition.QUEST_COUNT; id++){
			Quest quest = QuestDefinition.getQuest(id);
			if(quest.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(quest.getObjectDialog(clickId, player, Id, chatId, optionId, npcChatId, x, y, player.questStage[id]))
				return true;
		}
		return false;
	}
	
	public boolean findQuestDialog(int Id, int chatId, int optionId, int npcChatId){
		for(int id = 0; id < SpecialEventDefinition.EVENT_COUNT; id++){
			SpecialEvent event = SpecialEventDefinition.getEvent(id);
			if(event.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(event.getDialog(player, Id, chatId, optionId, npcChatId, player.holidayEventStage[id]))
				return true;
		}
		for(int id = 0; id < QuestDefinition.QUEST_COUNT; id++){
			Quest quest = QuestDefinition.getQuest(id);
			if(quest.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(quest.getDialog(player, Id, chatId, optionId, npcChatId, player.questStage[id])){
//				if(player.questStage[quest.getId()] == 0/* && quest.getDefinition().isMembers() && !player.isDonator()*/){
//					player.getActionSender().removeInterfaces();
//					player.getActionSender().sendMessage("You need a donator rank to start this quest: "+quest.getDefinition().getName());
//					return false;
//				}
				return true;
			}
		}
		return false;
	}
	
	public boolean handleFlashIcon(){
		for(int id = 0; id < QuestDefinition.QUEST_COUNT; id++){
			Quest quest = QuestDefinition.getQuest(id);
			if(quest.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(quest.getFlashIcon(player, player.questStage[id]))
				return true;
		}
		return false;
	}
	
	public void refreshQuestPoints(){
		player.getActionSender().sendString("QP: "+player.getQuestPoints(), 3985);
	}
	
	public boolean handleButton(int buttonId) {
		for(int id = 1; id < QuestDefinition.QUEST_COUNT; id++){
			QuestDefinition questDef = QuestDefinition.forId(id);
			String questName = questDef.getName();
			int questButton = questDef.getButton();
			if(buttonId == questButton){
				cleanQuestScroll();
				player.getActionSender().sendString(questName, 8144);
				player.getQuestPoints();
				setQuestScrollText(id);
				player.getActionSender().sendInterface(8134);
				return true;
			}
		}
		if(handleInterfaceButton(buttonId)){
			return true;
		}
		return false;
	}
	
	public boolean handleInterfaceButton(int buttonId){
		for(int id = 1; id < QuestDefinition.QUEST_COUNT; id++){
			Quest quest = QuestDefinition.getQuest(id);
			if(quest.getId() == -1)
				continue;
			/*if(player.questStage[quest.getId()] == 1)
				continue;*/
			if(quest.handleInterfaceButton(player, buttonId, player.questStage[id]))
				return true;
		}
		return false;
	}
	
}
