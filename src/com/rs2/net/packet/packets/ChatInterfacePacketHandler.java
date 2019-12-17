package com.rs2.net.packet.packets;

import com.rs2.Constants;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.skills.crafting.DramenBranch;
import com.rs2.model.content.skills.crafting.GlassMaking;
import com.rs2.model.content.skills.crafting.LeatherMakingHandler;
import com.rs2.model.content.skills.crafting.PotteryMaking;
import com.rs2.model.content.skills.crafting.SilverCrafting;
import com.rs2.model.content.skills.crafting.Spinning;
import com.rs2.model.content.skills.crafting.Tanning;
import com.rs2.model.content.skills.crafting.Weaving;
import com.rs2.model.content.skills.fletching.HandleLogCutting;
import com.rs2.model.content.skills.cooking.Cooking;
import com.rs2.model.content.skills.magic.OrbCharging;
import com.rs2.model.content.skills.smithing.Smelting;
import com.rs2.model.content.treasuretrails.ChallengeScrolls;
import com.rs2.model.players.BankManager;
import com.rs2.model.players.GeManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.ShopManager;
import com.rs2.model.players.TradeManager;
import com.rs2.model.players.item.Item;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;

public class ChatInterfacePacketHandler implements PacketHandler {

	public static final int DIALOGUE = 40;
	public static final int SHOW_ENTER_X = 135;
	public static final int ENTER_X = 208;

	@Override
	public void handlePacket(Player player, Packet packet) {
		switch (packet.getOpcode()) {
			case DIALOGUE :
				handleDialogue(player, packet);
				break;
			case SHOW_ENTER_X :
				showEnterX(player, packet);
				break;
			case ENTER_X :
				handleEnterX(player, packet);
				break;
		}
	}

	private void handleDialogue(Player player, Packet packet) {
		if (Constants.SERVER_DEBUG) {
			player.getActionSender().sendMessage("dialogue: "+player.getDialogue().getDialogueId()+" chat: "+player.getDialogue().getChatId());
		}
		if (player.getDialogue().checkEndDialogue()) {
			player.getActionSender().removeInterfaces();
			player.getDialogue().resetDialogue();
			return;
		}
		if(player.getDialogue().getDialogueType() == 1){
			Dialogues.sendDialogueObject(player.getDialogue().getClickId(), player, player.getDialogue().getDialogueId(), player.getDialogue().getChatId() + 1, 0, player.getDialogue().getDialogueX(), player.getDialogue().getDialogueY());
			return;
		}
		if(player.getDialogue().getDialogueType() == 0){
			Dialogues.sendDialogue(player, player.getDialogue().getDialogueId(), player.getDialogue().getChatId() + 1, 0);
			return;
		}
		System.out.println("Unhandled dialogue type: "+player.getDialogue().getDialogueType());
	}

	private void showEnterX(Player player, Packet packet) {
		player.setEnterXSlot(packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE));
		player.setEnterXInterfaceId(packet.getIn().readShort(StreamBuffer.ValueType.A));
		player.setEnterXId(packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE));
		if(player.getEnterXInterfaceId() == 3900) {
			ShopManager.buyItem(player, player.getEnterXSlot(), player.getEnterXId(), 100);
			return;
		}
		if(player.getEnterXInterfaceId() == 3823) {
			ShopManager.sellItem(player, player.getEnterXSlot(), player.getEnterXId(), 100);
			return;
		}
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2);
		out.writeHeader(player.getEncryptor(), 27);
		player.send(out.getBuffer());
	}

	private void handleEnterX(Player player, Packet packet) {
		int amount = packet.getIn().readInt();

        if (amount <= 0)
            return;

		if (player.getEnterXInterfaceId() == 5064 && player.getInterface() == 5292) {
			BankManager.bankItem(player, player.getEnterXSlot(), player.getEnterXId(), amount);
			return;
		} else if (player.getEnterXInterfaceId() == 5382  && player.getInterface() == 5292) {
			BankManager.withdrawItem(player, player.getEnterXSlot(), player.getEnterXId(), amount);
			return;
		} else if (player.getEnterXInterfaceId() == 3322) {
			if (player.getStatedInterface() == "duel")
				player.getDuelMainData().stakeItem(new Item(player.getEnterXId(), amount), player.getInventory().getItemContainer().getSlotById(player.getEnterXId()));
			else
				TradeManager.offerItem(player, player.getEnterXSlot(), player.getEnterXId(), amount);
			return;
		} else if (player.getEnterXInterfaceId() == 3415) {
			TradeManager.removeTradeItem(player, player.getEnterXSlot(), player.getEnterXId(), amount);
			return;
		} else if (player.getEnterXInterfaceId() == 15682 || player.getEnterXInterfaceId() == 15683) {
			player.getFarmingTools().withdrawItems(player.getClickItem(), amount);
			return;
		} else if (player.getEnterXInterfaceId() == 15594 || player.getEnterXInterfaceId() == 15595) {
			player.getFarmingTools().storeItems(player.getClickItem(), amount);
			return;
		} else if (player.getEnterXInterfaceId() == 6669) {
			player.getDuelMainData().removeStakedItem(new Item(player.getEnterXId(), amount));
			return;
		} else if (player.getEnterXInterfaceId() == 207) {
			ChallengeScrolls.handleAnswer(player, amount, player.challengeScroll);
			return;
		} else if (player.getEnterXInterfaceId() == 13718) {
			Cooking.handleCookingTick(player, amount);
			return;
		}
		if (Smelting.handleSmelting(player, player.getEnterXInterfaceId(), amount)) {
			return;
		}
		if (PotteryMaking.makePottery(player, player.getEnterXInterfaceId(), amount)) {
			return;
		}
		if (SilverCrafting.makeSilver(player, player.getEnterXInterfaceId(), amount)) {
			return;
		}
		if (Spinning.spin(player, player.getEnterXInterfaceId(), amount)) {
			return;
		}
		if (GlassMaking.makeSilver(player, player.getEnterXInterfaceId(), amount)) {
			return;
		}
		if (LeatherMakingHandler.handleButtons(player, player.getEnterXInterfaceId(), amount)) {
			return;
		}
		if (Weaving.weave(player, player.getEnterXInterfaceId(), amount)) {
			return;
		}
		if (DramenBranch.cutDramen(player, player.getEnterXInterfaceId(), amount)) {
			return;
		}
		if (OrbCharging.chargeOrb(player, player.getEnterXInterfaceId(), amount)) {
			return;
		}
		if (HandleLogCutting.handleButtons(player, player.getEnterXInterfaceId(), amount)) {
			return;
		}
		if (Tanning.handleButtonsX(player, player.getEnterXInterfaceId(), amount)) {
			return;
		}
		if (player.getEnterXInterfaceId() == 18900) {
			GeManager.updateItemAmountX(player, amount);
			return;
		} else 
		if (player.getEnterXInterfaceId() == 18901) {
			GeManager.updatePriceX(player, amount);
			return;
		}
	}

}
