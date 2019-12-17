package com.rs2.model.content;

import com.rs2.model.World;
import com.rs2.model.npcs.NpcDefinition;
import com.rs2.model.players.Player;
import com.rs2.model.players.ShopManager;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 5/16/12 Time: 1:16 AM To change
 * this template use File | Settings | File Templates.
 */
public class Shops {

	public static int findShop(Player player, int npcId) {
		NpcDefinition def = NpcDefinition.forId(npcId);
		int shop = def.getShop();
		if (shop > -1) {
			return shop;
		}
		return -1;
	}
	
	public static boolean openShop(Player player, int npcId) {
		int shop = findShop(player, npcId);
		if (shop > -1) {
			ShopManager.openShop(player, shop);
			return true;
		}
		return false;
	}
}
