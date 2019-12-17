package com.rs2.model.content;

import com.rs2.Constants;
import com.rs2.cache.interfaces.RSInterface;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.minigames.barrows.Barrows;
import com.rs2.model.players.Player;
import com.rs2.util.Misc;

/**
 * By Mikey` of Rune-Server
 */

public class WalkInterfaces {

	public static void addWalkableInterfaces(Player player) {
		if (player.inWild(player.getPosition().getX(), player.getPosition().getY()))
			player.wildyWarned = true;
		sendGameInterface(player);
		sendMultiInterface(player);
		
		if (player.getAlchemistPlayground().isInAlchemistPlayGround())
			player.getAlchemistPlayground().loadInterfacePlayer();
		if (player.getEnchantingChamber().isInEnchantingChamber())
			player.getEnchantingChamber().loadInterfacePlayer();
		if (player.getTelekineticTheatre().isInTelekineticTheatre())
			player.getTelekineticTheatre().loadInterfacePlayer();
		if (player.getCreatureGraveyard().isInCreatureGraveyard()) {
			player.getCreatureGraveyard().loadInterfacePlayer();
		}
	}

	public static void sendGameInterface(Player player) {
		if(!player.inWild(player.getPosition().getX(), player.getPosition().getY())){
			if(!player.isSkulled)
			player.getActionSender().sendString("@gre@Safe"/* + player.getWildernessLevel()*/, 199);
			if (player.inSafeZone(player.getPosition().getX(), player.getPosition().getY()) && player.isSkulled) {
				player.hit(Misc.random(3) + 3, HitType.NORMAL);
				}
			
	   } else if (player.inWild(player.getPosition().getX(), player.getPosition().getY())) {
			changeWalkableInterface(player, 197);
				player.getActionSender().sendPlayerOption("Attack", 1, false);//try
				
//			
//			if(player.currentDlvl != player.getWildernessLevel()){
//				player.getActionSender().sendMessage("pvp");
				if(!player.isSkulled)
				player.getActionSender().sendString("@red@PvP"/* + player.getWildernessLevel()*/, 199);
//				player.currentDlvl = player.getWildernessLevel();
			
		} else if (player.inDuelArena()) {
			player.getActionSender().sendPlayerOption("Attack", 1, false);
			changeWalkableInterface(player, 201);
		} else if (player.isInDuelArea()) {
			player.getActionSender().sendPlayerOption("Challenge", 1, false);
			changeWalkableInterface(player, 201);
		} else if (player.inBarrows()) {
			if(player.bKC != player.getKillCount()){
				player.getActionSender().sendString("Kill count: " + player.getKillCount(), 4536);
				player.bKC = player.getKillCount();
			}
			Barrows.showHead(player);
			if (changeWalkableInterface(player, 4535)) {
				player.getActionSender().sendPlayerOption("null", 1, false);
				player.getActionSender().sendMapState(2);
			}
		} else if (player.inWaterbirthIsland()) {
			if (changeWalkableInterface(player, 11877)) {
				player.getActionSender().sendPlayerOption("null", 1, false);
			}
		} else if (player.inSmokeDungeon()) {
			if (changeWalkableInterface(player, 13103)) {
				player.getActionSender().sendPlayerOption("null", 1, false);
			}	
		} else {
			if (changeWalkableInterface(player, -1)) {
				player.getActionSender().sendPlayerOption("null", 1, false);
				RSInterface inter = RSInterface.forId(11092);
				if(!player.hasInterfaceOpen(inter))
					player.getActionSender().sendMapState(0);
			}
		}
	}

	public static void checkChickenOption(Player player) {
		if (player.getEquipment().getId(Constants.WEAPON) == 4566)
			player.getActionSender().sendPlayerOption("Whack", 5, false);
		else
			player.getActionSender().sendPlayerOption("null", 5, false);
	}

	public static void sendMultiInterface(Player player) {
		if (player.inMulti()) {
			player.getActionSender().sendMultiInterface(true);
		} else {
			player.getActionSender().sendMultiInterface(false);
		}
	}

	public static boolean changeWalkableInterface(Player player, int id) {
		if (player.getCurrentWalkableInterface() == id) {
			return false;
		} else {
			if(player.earthquake && id == 4535)
				return true;
			player.setCurrentWalkableInterface(id);
			player.getActionSender().sendWalkableInterface(id);
			return true;
		}
	}
}
