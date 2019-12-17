package com.rs2.net.packet.packets;

import java.util.Arrays;

import com.rs2.model.World;
import com.rs2.model.players.Player;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;
import com.rs2.util.Logger;
import com.rs2.util.NameUtil;
import com.rs2.util.Time;

public class ChatSettingsPacketHandler implements PacketHandler {

	public static final int CHAT_SETTING = 95;

	@Override
	public void handlePacket(Player player, Packet packet) {
		byte publicMode = (byte) packet.getIn().readByte(false);
        byte privateMode = (byte) packet.getIn().readByte(false);//0 - on, 1 - friends, 2 - off
        byte tradeMode = (byte) packet.getIn().readByte(false);
        player.setPrivateChatMode(privateMode);
        player.setPublicChatMode(publicMode);
        player.setTradeMode(tradeMode);
        player.getPrivateMessaging().refresh(false);
	}

}