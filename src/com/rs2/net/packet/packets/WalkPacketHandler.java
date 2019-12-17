package com.rs2.net.packet.packets;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.minigames.duelarena.RulesData;
import com.rs2.model.players.Player;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;
import com.rs2.util.plugin.PluginManager;

public class WalkPacketHandler implements PacketHandler {

	public static final int MINI_MAP_WALK = 248;
	public static final int MAIN_WALK = 164;
	public static final int OTHER_WALK = 98;

	@Override
	public void handlePacket(Player player, Packet packet) {
		int length = packet.getPacketLength();
		if (packet.getOpcode() == MINI_MAP_WALK) {
			length -= 14;
		}
		if ((boolean) player.getAttributes().get("Agility") == true) {
			return;
		}
		if (player.isDead() || player.stopPlayerPacket()) {
			return;
		}
		if (player.isFrozen()) {
			player.getActionSender().sendMessage("A magical force stops you from moving.");
			return;
		}
		if (player.isStunned()) {
			player.getActionSender().sendMessage("You are stunned.");
			return;
		}
	    if (RulesData.NO_MOVEMENT.activated(player)) {
		    player.getActionSender().sendMessage("Movements have been disabled during this fight!");
			return;
		}
		if (packet.getOpcode() != OTHER_WALK) {
			player.resetAllActions();
			if (player.questStage[0] != 1){
				player.getActionSender().removeInterfaces();
				//player.getQuestHandler().setTutorialStage();
			}
			if (player.getStatedInterface() == "duel") {
				if (player.getDuelMainData().getOpponent() != null && !player.inDuelArena()) {
					player.getDuelMainData().getOpponent().getDuelInteraction().endDuelInteraction(true);
					player.getDuelMainData().getOpponent().getActionSender().sendMessage("Other played declined the duel.");
				}
				player.getDuelInteraction().endDuelInteraction(true);
			}
			PluginManager.reset();
		}
        if (player.countDownEscape())
            player.resetEscapeTimer();
		if (player.getEquipment().getItemContainer().contains(6583) || player.getEquipment().getItemContainer().contains(7927))
			player.getEquipment().unequip(Constants.RING);
		player.getMovementHandler().resetOnWalkPacket();
		if (player.questStage[0] == 0){
			player.questStage[0] = 2;
		} if (player.questStage[0] != 1){
			player.getQuestHandler().setTutorialStage();
		} if (player.questStage[0] == 1)
			player.getActionSender().sendSidebarInterface(3, 3213);
		int steps = (length - 5) / 2;
		int[][] path = new int[steps][2];
		int firstStepX = packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		for (int i = 0; i < steps; i++) {
			path[i][0] = packet.getIn().readByte();
			path[i][1] = packet.getIn().readByte();
		}
		int firstStepY = packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE);
		player.getMovementHandler().reset();
		player.getMovementHandler().setRunPath(packet.getIn().readByte(StreamBuffer.ValueType.C) == 1);

		// Insert the steps.
		player.getMovementHandler().addToPath(new Position(firstStepX, firstStepY));
		for (int i = 0; i < steps; i++) {
			path[i][0] += firstStepX;
			path[i][1] += firstStepY;
			player.getMovementHandler().addToPath(new Position(path[i][0], path[i][1]));
		}

		player.getMovementHandler().finish();
	}
}
