package com.rs2.model.content.questing;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.Npc.WalkType;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.content.combat.projectile.ProjectileTrajectory;
import com.rs2.util.Misc;

public abstract class SpecialEvent {

	private int id;
	private int type = 0;
	private boolean initialised = false;
	
	public SpecialEvent(int id) {
		this.id = id;
	}
	
	public SpecialEvent(int id, int type) {
		this.id = id;
		this.type = type;
	}
	
	public int getId() {
		return id;
	}
	
	public int getType() {
		return type;
	}
	
	public boolean isInitialised() {
		return initialised;
	}

	public void setInitialised(boolean initialised) {
		this.initialised = initialised;
	}
	
	public void setEventStage(final Player player, final int stage){
		if(player.holidayEventStage[this.getId()] == 1)
			return;
		player.holidayEventStage[this.getId()] = stage;
	}
	
	public void addEventStage(final Player player, final int stage){
		if(player.holidayEventStage[this.getId()] == 1)
			return;
		player.holidayEventStage[this.getId()] += stage;
	}

	public int calculateHitDelay(Position start, Position end) {
		int hitDelay = 0;
		int distance = Misc.getDistance(start, end);
		ProjectileTrajectory trajectory = ProjectileTrajectory.SPELL;
		double delay = trajectory.getDelay() + trajectory.getSlowness() + distance * 5d;
		delay = Math.ceil((delay * 12d) / 600d);
		if (distance > 1)
			delay += 1;
		hitDelay += (int) delay;
		return hitDelay;
	}
	
	public int calculateWalkDelay(Position start, Position end) {
		int hitDelay = 0;
		int distance = Misc.getDistance(start, end);
		double delay = 100 + distance * 45d;
		delay = Math.ceil((delay * 12d) / 600d);
		if (distance > 1)
			delay += 1;
		hitDelay += (int) delay;
		return hitDelay;
	}
	
	public static void spawnNpc(int id, Position pos, int face) {
		NpcLoader.newNPC(id, pos.getX(), pos.getY(), pos.getZ(), face);
	}
	
	public boolean getDialog(final Player player, int npcId, int chatId, int optionId, int npcChatId, int stage){
		return false;
	}
	public boolean getObjectDialog(int clickId, final Player player, int npcId, int chatId, int optionId, int npcChatId, int x, int y, int stage){
		return false;
	}
	
	public boolean getQuestDrop(final Player killer, int npcId, Position deathPos, int stage){
		return false;
	}
	
	public boolean handleFirstClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		return false;
	}
	
	public boolean handleSecondClickObject(final Player player, int objectId, int objectX, int objectY, int stage){
		return false;
	}
	
	public boolean controlDying(final Entity attacker, final Entity victim, int stage){
		return false;
	}
	
	public int controlCombatDamage(final Entity attacker, final Entity victim, int stage){
		return -1;
	}
	
	public boolean handleNpcDeath(final Player player, int npcId, int stage){
		return false;
	}
	
	public boolean checkAreas(final Player player, int stage){
		return false;
	}
	
	public boolean allowedToAttackNpc(final Player player, int npcId, int stage){
		return true;
	}
	
	public boolean getFlashIcon(final Player player, int stage){
		return false;
	}
	
	public void setStage(final Player player, int stage){
		
	}
	
	public boolean getQuestPickups(final Player player, int itemId, int stage){
		return false;
	}
	
	public boolean useQuestItemOnObject(final Player player, int itemId, int objectId, int stage){
		return false;
	}
	
	public boolean handleItemOnItem(final Player player, int item1, int item2, int stage){
		return false;
	}
	
	public boolean handleItemDrop(final Player player, int item, int stage){
		return false;
	}
	
	public boolean handleFirstClickItem(final Player player, int interfaceId, int itemId, int stage){
		return false;
	}
	
	public boolean handleInterfaceButton(final Player player, int buttonId, int stage){
		return false;
	}
	
	public boolean handleFirstClickNpc(final Player player, int npcId, int stage){
		return false;
	}
	
	public boolean handleItemOnNpc(final Player player, int npcId, int itemId, int stage){
		return false;
	}
	
	public void initialize(){
	}

	public void eventCompleted(final Player player) {
		player.holidayEventStage[this.getId()] = 1;
	}
	
	public void eventStarted(final Player player){
		player.holidayEventStage[this.getId()] = 2;
	}
}
