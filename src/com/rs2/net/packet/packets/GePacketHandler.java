package com.rs2.net.packet.packets;

import com.rs2.Constants;
import com.rs2.cache.interfaces.RSInterface;
import com.rs2.model.Position;
import com.rs2.model.content.Dyeing;
import com.rs2.model.content.Pets;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.minigames.barrows.Barrows;
import com.rs2.model.content.skills.Menus;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.content.skills.crafting.BasicCraft;
import com.rs2.model.content.skills.crafting.GemCrafting;
import com.rs2.model.content.skills.crafting.GemCutting;
import com.rs2.model.content.skills.crafting.GemData;
import com.rs2.model.content.skills.crafting.GlassMaking;
import com.rs2.model.content.skills.crafting.LeatherMakingHandler;
import com.rs2.model.content.skills.crafting.StaffCrafting;
import com.rs2.model.content.skills.fletching.ArrowMaking;
import com.rs2.model.content.skills.fletching.BoltTipsMaking;
import com.rs2.model.content.skills.fletching.BowStringing;
import com.rs2.model.content.skills.fletching.LogCuttingInterfaces;
import com.rs2.model.content.skills.cooking.OneIngredients;
import com.rs2.model.content.skills.cooking.ThreeIngredients;
import com.rs2.model.content.skills.cooking.TwoIngredients;
import com.rs2.model.content.skills.farming.MithrilSeeds;
import com.rs2.model.content.skills.farming.Sacks;
import com.rs2.model.content.skills.herblore.Cleaning;
import com.rs2.model.content.skills.herblore.Coconut;
import com.rs2.model.content.skills.herblore.Grinding;
import com.rs2.model.content.skills.herblore.PoisoningWeapon;
import com.rs2.model.content.skills.herblore.PotionMaking;
import com.rs2.model.content.skills.magic.MagicSkill;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.content.skills.runecrafting.Pouches;
import com.rs2.model.content.skills.runecrafting.Runecrafting;
import com.rs2.model.content.skills.slayer.Slayer;
import com.rs2.model.content.skills.smithing.SmithBars;
import com.rs2.model.content.treasuretrails.AnagramsScrolls;
import com.rs2.model.content.treasuretrails.ChallengeScrolls;
import com.rs2.model.content.treasuretrails.ClueScroll;
import com.rs2.model.content.treasuretrails.CoordinateScrolls;
import com.rs2.model.content.treasuretrails.DiggingScrolls;
import com.rs2.model.content.treasuretrails.MapScrolls;
import com.rs2.model.content.treasuretrails.Puzzle;
import com.rs2.model.content.treasuretrails.SearchScrolls;
import com.rs2.model.content.treasuretrails.Sextant;
import com.rs2.model.content.treasuretrails.SpeakToScrolls;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.players.BankManager;
import com.rs2.model.players.GeManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.ShopManager;
import com.rs2.model.players.TradeManager;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.players.item.functions.Casket;
import com.rs2.model.players.item.functions.Nests;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;
import com.rs2.util.Misc;

public class GePacketHandler implements PacketHandler {

	public static final int SEARCH_ITEM = 19;

	@Override
	public void handlePacket(Player player, Packet packet) {
		if (player.stopPlayerPacket()) {
			return;
		}
		switch (packet.getOpcode()) {
			case SEARCH_ITEM :
				searchItem(player, packet);
				break;
		}
	}


	private void searchItem(final Player player, Packet packet) {
		int itemId = packet.getIn().readShort();
		if (itemId < 0 || itemId > Constants.MAX_ITEM_ID) {
			return;
		}
		if(itemId == 995)//you can't buy money!
			return;
		if(player.getInterface() != GeManager.GE_BUY_INTERFACE)
			return;
		Item item = new Item(itemId, 1);
		if(item.getDefinition().isUntradable())
			return;
		player.geOfferItemId_temp = itemId;
		player.geOfferItemAmount_temp = 1;
		player.geOfferPrice_temp = GeManager.getGuidePrice(itemId);
		player.getActionSender().sendGeItemSprite(18938, player.geOfferItemId_temp);
		player.getActionSender().sendString(""+Misc.format(GeManager.getGuidePrice(itemId)), 18919);
		player.getActionSender().sendString(""+player.geOfferItemAmount_temp, 18920);
		player.getActionSender().sendString(Misc.format(player.geOfferPrice_temp)+" coins", 18921);
		player.getActionSender().sendString(Misc.format(player.geOfferPrice_temp*player.geOfferItemAmount_temp)+" coins", 18922);
	}

}
