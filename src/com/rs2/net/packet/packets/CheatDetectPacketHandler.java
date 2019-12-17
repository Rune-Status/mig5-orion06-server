package com.rs2.net.packet.packets;

import java.util.Arrays;

import com.rs2.model.World;
import com.rs2.model.players.Player;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;
import com.rs2.util.Logger;
import com.rs2.util.NameUtil;
import com.rs2.util.Time;

public class CheatDetectPacketHandler implements PacketHandler {

	public static final int CHEAT_DETECTION = 11;

	@Override
	public void handlePacket(Player player, Packet packet) {
		byte cheatType = (byte) packet.getIn().readByte(false);//1 = autoclick
		Logger.logMainDataFolder(System.currentTimeMillis()+"§"+player.getUsername()+"§"+player.getPosition().getX()+"§"+player.getPosition().getY()+"§"+player.getPosition().getZ()+"$"+cheatType, "AutoBans");
		int banHours = 48;
		if(cheatType == 1){
			player.setBanExpire(Time.addHours(System.currentTimeMillis(), banHours));
			System.out.println("Autobanned "+player.getUsername()+" for 48 hours! Reason: Autoclicking.");
		}
		player.getActionSender().sendLogout();
		player.disconnect();
	}

}