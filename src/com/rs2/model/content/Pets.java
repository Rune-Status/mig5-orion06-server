package com.rs2.model.content;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * By Mikey` of Rune-Server
 */
public class Pets {

	private Player player;
	private Npc pet;
	private int itemId;

	public Pets(Player player) {
		this.player = player;
	}

	public static final int[][] PET_IDS = {// itemId, petId
	{1561, 768}, {7585, 3507}, {8070, 3854}, {8071, 3855}, {8072, 3856}, {8073, 3857}};

	/**
	 * Registers a pet for the player, and drops it.
	 */
	public void registerPet(int itemId, int petId) {
		if (player.getInventory().getItemContainer().getCount(itemId) == 0) {
			return;
		}
		if (pet != null) {
			player.getActionSender().sendMessage("You already have a pet following you!");
			return;
		}
		int value = player.getBossDefeats();
		if(itemId == 8070 && (value & 1) == 0){
			player.getActionSender().sendMessage("You need to defeat King Black Dragon first!");
			return;
		}
		if(itemId == 8071 && (value & 2) == 0){ 
			player.getActionSender().sendMessage("You need to defeat Kalphite Queen first!");
			return;
		}
		if(itemId == 8072 && (value & 4) == 0){
			player.getActionSender().sendMessage("You need to defeat Chaos Elemental first!");
			return;
		}
		if(itemId == 8073 && (value & 8) == 0){
			player.getActionSender().sendMessage("You need to defeat TzTok-Jad first!");
			return;
		}
		player.getInventory().removeItem(new Item(itemId, 1));
		this.itemId = itemId;
		this.pet = new Npc(petId);
		pet.setPosition(new Position(player.getPosition().getX() - 1, player.getPosition().getY(), player.getPosition().getZ()));
		pet.setSpawnPosition(new Position(player.getPosition().getX() - 1, player.getPosition().getY(), player.getPosition().getZ()));
		pet.setCurrentX(player.getPosition().getX() - 1);
		//pet.setCurrentY(player.getPosition().getX() - 1);//old
		pet.setCurrentY(player.getPosition().getY() - 1);
		World.register(pet);
		pet.setFollowingEntity(player);
	}
	
	public void newPet(int itemId, int npcId) {
		this.itemId = itemId;
		this.pet = new Npc(npcId);
		pet.setPosition(new Position(player.getPosition().getX(), player.getPosition().getY() - 1, player.getPosition().getZ()));
		pet.setSpawnPosition(new Position(player.getPosition().getX(), player.getPosition().getY() - 1, player.getPosition().getZ()));
		pet.setCurrentX(player.getPosition().getX() - 1);
		pet.setCurrentY(player.getPosition().getY() - 1);
		World.register(pet);
		pet.setFollowingEntity(player);
		pet.getUpdateFlags().setForceChatMessage("Miaow!");
	}

	/**
	 * Unregisters a pet for the player, and picks it up it.
	 */
	public void unregisterPet() {
		if (pet == null) {
			return;
		}
		player.getActionSender().sendMessage("You pick up your pet.");
		if (player.getInventory().canAddItem(new Item(itemId, 1)))
			player.getInventory().addItem(new Item(itemId, 1));
		else{
			int freeSlot = player.getBank().freeSlot();
			int bankCount = player.getBank().getCount(itemId);
			if(freeSlot != -1){
				if (bankCount == 0) {
					player.getBank().add(new Item(itemId, 1));
				} else {
					player.getBank().set(player.getBank().getSlotById(itemId), new Item(itemId, bankCount + 1));
				}
			}
		}
		this.itemId = -1;
		pet.setVisible(false);
		Following.resetFollow(pet);
		World.unregister(pet);
		pet = null;
	}

	/**
	 * Returns the pet instance of the Npc class.
	 */
	public Npc getPet() {
		return pet;
	}

}
