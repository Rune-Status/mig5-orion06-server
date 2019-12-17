package com.rs2.net.packet.packets;

import java.util.Arrays;

import com.rs2.Constants;
import com.rs2.model.players.Player;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;
import com.rs2.util.Logger;
import com.rs2.util.TextUtil;
import com.rs2.util.Time;

public class ChatPacketHandler implements PacketHandler {

	public static final int CHAT = 4;

	@Override
	public void handlePacket(Player player, Packet packet) {
		int effects = packet.getIn().readByte(false, StreamBuffer.ValueType.S);
		int color = packet.getIn().readByte(false, StreamBuffer.ValueType.S);
		int chatLength = packet.getPacketLength() - 2;
		byte[] text = packet.getIn().readBytesReverse(chatLength, StreamBuffer.ValueType.A);
		String uncompressed = TextUtil.uncompress(text, chatLength);
		if (player.isMuted()) {
			player.getActionSender().sendMessage("You are muted and cannot talk. Mute expires in: "+(Time.hoursBetween(System.currentTimeMillis(), player.getMuteExpire())+1)+" hours.");
			return;
		}
		if (player.questStage[0] != 1)
			return;
		if(Constants.LOG_PUBLIC_CHAT){
			if(uncompressed != null)
			if(!uncompressed.toLowerCase().equals(player.lastMessage.toLowerCase())){
				Logger.logEvent(System.currentTimeMillis()+"§"+player.getUsername()+"§"+player.getPosition().getX()+"§"+player.getPosition().getY()+"§"+player.getPosition().getZ()+"§"+uncompressed, "public chat");
				player.lastMessage = uncompressed;
			}
		}
		player.setChatEffects(effects);
		player.setChatColor(color);
		player.setChatText(text);
		player.setChatUpdateRequired(true);
	}

}
