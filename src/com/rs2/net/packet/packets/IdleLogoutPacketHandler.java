package com.rs2.net.packet.packets;

import com.rs2.model.players.Player;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;

/**
 * @author Purehit (http://rune-server.org)
 */

public class IdleLogoutPacketHandler implements PacketHandler {

	public static final int IDLELOGOUT = 202;

	@Override
	public void handlePacket(Player player, Packet packet) {
		player.setIdleTimer(player.getIdleTimer()+1);
		if(player.getIdleTimer() > 20) {
			if ((player.getStaffRights() < 2 && player.getPjTimer().completed()) || player.logoutSent){
				player.getActionSender().sendLogout();
				player.disconnect();
			}
		}
	}
}