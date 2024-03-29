package com.rs2.net.packet.packets;

import com.rs2.Constants;
import com.rs2.model.World;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.content.skills.magic.SpellBook;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.WalkToActionHandler;
import com.rs2.model.players.WalkToActionHandler.Actions;
import com.rs2.model.players.item.Item;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;

public class NpcPacketHandler implements PacketHandler {

	public static final int FIRST_CLICK = 155;
	public static final int SECOND_CLICK = 17;
	public static final int THIRD_CLICK = 21;
	public static final int FOURTH_CLICK = 230;
	public static final int ATTACK = 72;
	public static final int MAGIC_ON_NPC = 131;
	public static final int ITEM_ON_NPC = 57;

	@Override
	public void handlePacket(Player player, Packet packet) {
		if (player.stopPlayerPacket()) {
			return;
		}
		player.getActionSender().removeInterfaces();
		player.resetAllActions();
		switch (packet.getOpcode()) {
			case FIRST_CLICK :
				handleFirstClick(player, packet);
				break;
			case SECOND_CLICK :
				handleSecondClick(player, packet);
				break;
			case THIRD_CLICK :
				handleThirdClick(player, packet);
				break;
			case FOURTH_CLICK :
				handleFourthClick(player, packet);
				break;
			case ATTACK :
				handleAttack(player, packet);
				break;
			case MAGIC_ON_NPC :
				handleMagicOnNpc(player, packet);
				break;
			case ITEM_ON_NPC :
				handleItemOnNpc(player, packet);
				break;
		}
	}

	private void handleFirstClick(Player player, Packet packet) {
		int npcSlot = packet.getIn().readShort(true, StreamBuffer.ByteOrder.LITTLE);
		if (npcSlot < 0 || npcSlot > World.getNpcs().length) {
			return;
		}
		Npc npc = World.getNpcs()[npcSlot];
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		player.setClickId(npc.getNpcId());
		player.setClickX(npc.getPosition().getX());
		player.setClickY(npc.getPosition().getY());
		player.setClickZ(player.getPosition().getZ());
		player.setNpcClickIndex(npcSlot);
		player.getUpdateFlags().faceEntity(npcSlot);
		player.setFollowDistance(1);
		player.setFollowingEntity(npc);
		if (Constants.SERVER_DEBUG) {
			player.getActionSender().sendMessage("First click npc: "+player.getClickId());
		}
		WalkToActionHandler.setActions(Actions.NPC_FIRST_CLICK);
		WalkToActionHandler.doActions(player);
	}

	private void handleSecondClick(Player player, Packet packet) {
		int npcSlot = packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE) & 0xFFFF;
		if (npcSlot < 0 || npcSlot > World.getNpcs().length) {
			return;
		}
		Npc npc = World.getNpcs()[npcSlot];
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		player.setClickId(npc.getNpcId());
		player.setClickX(npc.getPosition().getX());
		player.setClickY(npc.getPosition().getY());
		player.setClickZ(player.getPosition().getZ());
		player.setNpcClickIndex(npcSlot);
		player.getUpdateFlags().faceEntity(npcSlot);
		player.setFollowDistance(1);
		player.setFollowingEntity(npc);
		if (Constants.SERVER_DEBUG) {
			player.getActionSender().sendMessage("Second click npc: "+player.getClickId());
		}
		WalkToActionHandler.setActions(Actions.NPC_SECOND_CLICK);
		WalkToActionHandler.doActions(player);
	}

	private void handleThirdClick(Player player, Packet packet) {
		int npcSlot = packet.getIn().readShort(true);
		if (npcSlot < 0 || npcSlot > World.getNpcs().length) {
			return;
		}
		Npc npc = World.getNpcs()[npcSlot];
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		player.setClickId(npc.getNpcId());
		player.setClickX(npc.getPosition().getX());
		player.setClickY(npc.getPosition().getY());
		player.setClickZ(player.getPosition().getZ());
		player.setNpcClickIndex(npcSlot);
		player.getUpdateFlags().faceEntity(npcSlot);
		player.setFollowDistance(1);
		player.setFollowingEntity(npc);
		if (Constants.SERVER_DEBUG) {
			player.getActionSender().sendMessage("Third click npc: "+player.getClickId());
		}
		WalkToActionHandler.setActions(Actions.NPC_THIRD_CLICK);
		WalkToActionHandler.doActions(player);
	}

	private void handleFourthClick(Player player, Packet packet) {
	}

	boolean canAttackSpecialNpc(Player player, int npcId){
		if(player.gang == 2 && npcId == 643)
			return false;
		return true;
	}
	
	private void handleAttack(final Player player, Packet packet) {
		int npcSlot = packet.getIn().readShort(StreamBuffer.ValueType.A);
		if (npcSlot < 0 || npcSlot > World.getNpcs().length) {
			return;
		}
		final Npc npc = World.getNpcs()[npcSlot];
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		player.setCastedSpell(null);
		if (npc.getPlayerOwner() != null && npc.getPlayerOwner() != player) {
			player.getActionSender().sendMessage(npc.getDefinition().getName() + " is not interested in interacting with you right now.");
			return;
		}
		if (npc.getDefinition().isAttackable() && canAttackSpecialNpc(player, npc.getDefinition().getId()))
			CombatManager.attack(player, npc);
		else
			player.getActionSender().sendMessage("You cannot attack that npc!");
	}

	private void handleMagicOnNpc(final Player player, Packet packet) {
		player.getMovementHandler().reset();
		int npcSlot = packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		if (npcSlot < 0 || npcSlot > World.getNpcs().length) {
			return;
		}
		int magicId = packet.getIn().readShort(StreamBuffer.ValueType.A);
		final Npc npc = World.getNpcs()[npcSlot];
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		if (npc.getPlayerOwner() != null && npc.getPlayerOwner() != player) {
			player.getActionSender().sendMessage(npc.getDefinition().getName() + " is not interested in interacting with you right now.");
			return;
		}
		Spell spell = SpellBook.getSpell(player, magicId);
		if (spell != null) {
			if(!player.isInMageArena()){
				if(spell == Spell.SARADOMIN_STRIKE){
					if(player.saradominStrikeCasts > 0){
						player.getActionSender().sendMessage("You need to cast this spell "+player.saradominStrikeCasts+" times at Mage arena first.");
						return;
					}
				}
				if(spell == Spell.FLAMES_OF_ZAMORAK){
					if(player.flameOfZamorakCasts > 0){
						player.getActionSender().sendMessage("You need to cast this spell "+player.flameOfZamorakCasts+" times at Mage arena first.");
						return;
					}
				}
				if(spell == Spell.CLAWS_OF_GUTHIX){
					if(player.clawsOfGuthixCasts > 0){
						player.getActionSender().sendMessage("You need to cast this spell "+player.clawsOfGuthixCasts+" times at Mage arena first.");
						return;
					}
				}
			}
			player.setCastedSpell(spell);
			if (spell == Spell.TELEOTHER_CAMELOT || spell == Spell.TELEOTHER_FALADOR || spell == Spell.TELEOTHER_LUMBRIDGE || spell == Spell.TELEBLOCK) {
				player.getActionSender().sendMessage("Nothing interesting happens.");
			} else {
				CombatManager.attack(player, npc);
			}
		} else if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG)
			System.out.println("Magic id: " + magicId);
	}

	private void handleItemOnNpc(final Player player, Packet packet) {
		final int itemId = packet.getIn().readShort(StreamBuffer.ValueType.A);
		final int npcSlot = packet.getIn().readShort(StreamBuffer.ValueType.A);
		final int itemSlot = packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE);
		if (itemSlot > 28 && itemSlot < 0) {
			return;
		}
		Item item = player.getInventory().getItemContainer().get(itemSlot);
		if (item == null || item.getId() != itemId)
			return;
        if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG)
        	System.out.println(itemId + " " + npcSlot + " " + itemSlot);
        if (npcSlot < 0 || npcSlot > World.getNpcs().length) {
			return;
		}
		final Npc npc = World.getNpcs()[npcSlot];
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		player.setSlot(itemSlot);
		player.setNpcClickIndex(npcSlot);
		player.setInteractingEntity(npc);
		player.setClickItem(itemId);
		player.setClickId(npc.getNpcId());
		player.setClickX(npc.getPosition().getX());
		player.setClickY(npc.getPosition().getY());
		player.setClickZ(player.getPosition().getZ());
		player.getUpdateFlags().faceEntity(npcSlot);
		player.setFollowDistance(1);
		player.setFollowingEntity(npc);
		WalkToActionHandler.setActions(Actions.ITEM_ON_NPC);
		WalkToActionHandler.doActions(player);
	}

}
