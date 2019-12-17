package com.rs2.net.packet.packets;

import java.util.Arrays;

import com.rs2.model.World;
import com.rs2.model.players.Player;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;
import com.rs2.util.Logger;
import com.rs2.util.NameUtil;
import com.rs2.util.Time;

public class ReportPacketHandler implements PacketHandler {

	public static final int REPORT = 218;

	@Override
	public void handlePacket(Player player, Packet packet) {
		long playerName_L = packet.getIn().readLong();
		byte rule = (byte) packet.getIn().readByte(false);
		boolean mute = ((byte) packet.getIn().readByte(false) == 1 ? true : false);
		Player player2 = playerOnline(playerName_L);
		if(player2 == null){
			player.getActionSender().sendMessage("You cannot report offline players.");
			return;
		}
		if(player.getUsernameAsLong() == playerName_L){
			player.getActionSender().sendMessage("You cannot report yourself!");
			return;
		}
		if(System.currentTimeMillis() - player.getLastReport() < (60*1000)){//60secs - is not being saved to char file... so resets on login
			player.getActionSender().sendMessage("You can only report a player once every 60 seconds.");
			return;
		}
		player.getActionSender().sendMessage("Thank-you, your abuse report has been received.");
		if(mute && player.getStaffRights() >= 1 && player2.getStaffRights() == 0){
			if (player2.isMuted())
				return;
			player2.setMuteExpire(Time.addHours(System.currentTimeMillis(), 48));
		}
		player.setLastReport(System.currentTimeMillis());
		Logger.logEvent(System.currentTimeMillis()+"§"+player.getUsername()+"§"+player.getPosition().getX()+"§"+player.getPosition().getY()+"§"+player.getPosition().getZ()+"§"+player2.getUsername()+"§"+player2.getPosition().getX()+"§"+player2.getPosition().getY()+"§"+player2.getPosition().getZ()+"§"+rule+"§"+(mute ? 1 : 0), "reports");
	}
	
	Player playerOnline(long playerName_L){
		for (Player players : World.getPlayers()) {
			if (players == null) {
				continue;
			}
			if (players.getUsernameAsLong() == playerName_L) {
				return players;
			}
		}
		return null;
	}

}