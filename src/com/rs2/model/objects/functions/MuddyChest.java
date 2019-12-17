package com.rs2.model.objects.functions;

import com.rs2.Constants;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class MuddyChest {

	public static void openMuddyChest(Player player) {
		if (!player.getInventory().removeItem(new Item(991))) {
			return;
		}
		player.getUpdateFlags().sendAnimation(832);
		player.getActionSender().sendMessage("You unlock the chest with your key.");
		new GameObject(171, 3089, 3859, 0, 1, 10, 170, 2);
		player.getInventory().addItemOrDrop(new Item(1619, 1));//uncut ruby
		player.getInventory().addItemOrDrop(new Item(2359, 1));//mithril bar
		player.getInventory().addItemOrDrop(new Item(1209, 1));//mithril dagger
		player.getInventory().addItemOrDrop(new Item(2297, 1));//anchovy pizza
		player.getInventory().addItemOrDrop(new Item(563, 2));//law runes
		player.getInventory().addItemOrDrop(new Item(560, 2));//death runes
		player.getInventory().addItemOrDrop(new Item(562, 10));//chaos runes
		player.getInventory().addItemOrDrop(new Item(995, 50));//coins
	}
}
