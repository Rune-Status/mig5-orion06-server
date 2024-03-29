package com.rs2.model.content.minigames.magetrainingarena;

import java.util.Random;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 01/01/12 Time: 00:47 To change
 * this template use File | Settings | File Templates.
 */
public class EnchantingChamber {
	// todo object on interface

	/* the default locations */

	private static final Position[] ENTERING_POSITION = {new Position(3373, 9640, 0), new Position(3374, 9639, 0), new Position(3375, 9640, 0), new Position(3374, 9641, 0), new Position(3362, 9650, 0), new Position(3363, 9651, 0), new Position(3364, 9650, 0), new Position(3363, 9649, 0), new Position(3363, 9628, 0), new Position(3364, 9629, 0), new Position(3363, 9630, 0), new Position(3362, 9629, 0), new Position(3354, 9640, 0), new Position(3353, 9639, 0), new Position(3352, 9640, 0), new Position(3353, 9641, 0)};

	private Player player;

	//death, blood, cosmic
	private static int[] rewardRune = {560, 564, 565};
	
	//private int tempPizzazPoint;
	public int enchantmentPizzazPoint;
	private int[] enchantSpellsUsed = new int[6];

	public static Npc guardian() {
		for (Npc npc : World.getNpcs()) {
			if (npc == null)
				continue;
			if (npc.getDefinition().getId() == 3100)
				return npc;
		}
		return null;
	}

	public EnchantingChamber(Player player) {
		this.player = player;
	}

	/* Setting up the constant field */

	private static final String[] colour = {"red", "yellow", "green", "blue"};

	private static final int redItem = 6901;
	private static final int yellowItem = 6899;
	private static final int greenItem = 6898;
	private static final int blueItem = 6900;
	private static final int orb = 6902;
	private static final int dragonStone = 6903;

	/* the item that give a bonus point */

	private static String bonusItem;

	/* the random instance */

	static Random random = new Random();

	/* loading the enchanting chamber part */

	public static void loadEnchantingChamber() {
		loadInitialVariables();
		loadEnchantingEvent();
	}

	/* loading initial variables */

	private static void loadInitialVariables() {
		bonusItem = "red";
	}

	/* loading the enchanting chamber event */

	private static void loadEnchantingEvent() {
		World.submit(new Tick(40) { // 24 seconds
			@Override
			public void execute() {
				bonusItem = colour[random.nextInt(colour.length)];
				for (Player player : World.getPlayers()) {
					if (player == null)
						continue;
					if (player.getEnchantingChamber().isInEnchantingChamber())
						player.getEnchantingChamber().showInterfaceComponent(bonusItem);
				}
				if (guardian() != null)
					guardian().getUpdateFlags().sendForceMessage("The color shape is now " + bonusItem + "!");

			}
		});
	}

	public boolean isInEnchantingChamber() {
		int x = player.getPosition().getX();
		int y = player.getPosition().getY();

		return player.getPosition().getZ() == 0 && x >= 3334 && x <= 3388 && y >= 9610 && y <= 9664;

	}

	/* loading interface for a single player */

	public void loadInterfacePlayer() {
		player.getActionSender().sendWalkableInterface(15917);
		//player.getActionSender().sendString("" + (enchantmentPizzazPoint + tempPizzazPoint), 15921);
		player.getActionSender().sendString("" + (enchantmentPizzazPoint), 15921);
	}

	public void showInterfaceComponent(String bonusItem) {
		removeInterfaceComponent();
		if (bonusItem == "red")
			player.getActionSender().sendFrame171(0, 15924);
		else if (bonusItem == "yellow")
			player.getActionSender().sendFrame171(0, 15922);
		else if (bonusItem == "green")
			player.getActionSender().sendFrame171(0, 15923);
		else if (bonusItem == "blue")
			player.getActionSender().sendFrame171(0, 15925);
	}

	public void removeInterfaceComponent() {
		player.getActionSender().sendFrame171(1, 15924);
		player.getActionSender().sendFrame171(1, 15922);
		player.getActionSender().sendFrame171(1, 15923);
		player.getActionSender().sendFrame171(1, 15925);
	}

	/* handle object clicking packet */

	public boolean handleObjectClicking(int objectId) {
		if(objectId == 10802 || objectId == 10799 || objectId == 10800 || objectId == 10801){
			addCorrespondingItem(objectId);
			return true;
		}
		if(objectId == 10782 && isInEnchantingChamber()){
			exit();
			return true;
		}
		if(objectId == 10779){
			enter();
			return true;
		}
		if(objectId == 10803){
			depositOrbs();
			return true;
		}
		/*switch (objectId) {
			case 10802 :// red pile
			case 10799 :// yellow pile
			case 10800 :// green pile
			case 10801 :// blue pile
				addCorrespondingItem(objectId);
				return true;
			case 10782 :// exiting object
				if (isInEnchantingChamber())
					exit();
				return true;
			case 10779 :// entering object
				enter();
				return true;
			case 10803 :// deposit orb object
				depositOrbs();
				return true;
		}*/
		return false;
	}

	/* adding the corresponding item for the object id */

	private void addCorrespondingItem(final int objectId) {
		if (player.getInventory().getItemContainer().freeSlots() <= 0) {
			player.getActionSender().sendMessage("Not enough space in your inventory.");
			return;
		}
		player.getUpdateFlags().sendAnimation(832, 0);
		player.getInventory().addItem(new Item(correspondingItem(objectId)));
	}

	/* gets the correspondint item for an object id */

	private static int correspondingItem(int objectId) {
		switch (objectId) {
			case 10802 :// red pile
				return redItem;
			case 10799 :// yellow pile
				return yellowItem;
			case 10800 :// green pile
				return greenItem;
			case 10801 :// blue pile
				return blueItem;
		}
		return 0;
	}

	/* gets the bonus item string with an id provided */

	private static String getStringById(int i) {
		if (i == redItem)
			return "red";
		if (i == yellowItem)
			return "yellow";
		if (i == greenItem)
			return "green";
		if (i == blueItem)
			return "blue";
		return "";
	}

	/* entering and leaving enchanting chamber */

	private void enter() {
		if (player.getSkill().getLevel()[Skill.MAGIC] < MageGameConstants.ENCHANTING_LEVEL) {
			player.getActionSender().sendMessage("You need a magic level of " + MageGameConstants.ENCHANTING_LEVEL + " to enter here.");
			return;
		}
		player.getActionSender().sendMessage("You've entered the Enchantment Chamber");
		int number = random.nextInt(ENTERING_POSITION.length);
		player.teleport(ENTERING_POSITION[number]);
		player.getEnchantingChamber().showInterfaceComponent(bonusItem);
	}

	private void exit() {
		player.getActionSender().sendMessage("You've left the Enchantment Chamber");
		player.getActionSender().sendWalkableInterface(-1);
		player.teleport(MageGameConstants.LEAVING_POSITION);
		removeItems();
		/*if(player.hasProgressHat())
			enchantmentPizzazPoint += tempPizzazPoint;
		tempPizzazPoint = 0;*/
		player.getActionSender().sendWalkableInterface(-1);
	}

	/* removing the minigame items */

	private void removeItems() {
		player.getInventory().removeItem(new Item(redItem, player.getInventory().getItemAmount(redItem)));
		player.getInventory().removeItem(new Item(yellowItem, player.getInventory().getItemAmount(yellowItem)));
		player.getInventory().removeItem(new Item(greenItem, player.getInventory().getItemAmount(greenItem)));
		player.getInventory().removeItem(new Item(blueItem, player.getInventory().getItemAmount(blueItem)));
		player.getInventory().removeItem(new Item(orb, player.getInventory().getItemAmount(orb)));
		player.getInventory().removeItem(new Item(dragonStone, player.getInventory().getItemAmount(dragonStone)));
	}

	/* getting the points given with the spell id */

	private static int getPointsBySpell(int spellId) {
		switch (spellId) {
			case 0 :// lv 1
				return 2;
			case 1 :// lv 2
				return 4;
			case 2 :// lv 3
				return 6;
			case 3 :// lv 4
				return 8;
			case 4 :// lv 5
				return 10;
			case 5 :// lv 6
				return 12;
		}
		return 0;
	}

	/*
	 * getting the extra points given with the enchanting spell index --see
	 * Player.java for more info
	 */

	private static int getExtraPointsByIndex(int index) {
		switch (index) {
			case 0 :// lv 1
				return 1;
			case 1 :// lv 2
				return 2;
			case 2 :// lv 3
				return 3;
			case 3 :// lv 4
				return 4;
			case 4 :// lv 5
				return 5;
			case 5 :// lv 6
				return 6;
		}
		return 0;
	}

	/* getting the enchanting index */

	private static int getEnchantingIndex(int spellId) {
		return getPointsBySpell(spellId) / 2 - 1;
	}

	public boolean isEnchantItem(int spellId, int itemId){
		if (itemId != redItem && itemId != yellowItem && itemId != greenItem && itemId != blueItem && itemId != dragonStone)
			return false;
		return true;
	}
	
	/* enchanting an item in the minigame */

	public void enchantItem(int spellId, int itemId) {
		/* checks if the item enchanted can be enchanted */
		if (itemId != redItem && itemId != yellowItem && itemId != greenItem && itemId != blueItem && itemId != dragonStone)
			return;

		/* dragonstone handling */

		if (itemId == dragonStone) {
			/* check if the max point is reached or not */

			/*if ((enchantmentPizzazPoint + tempPizzazPoint) <= MageGameConstants.MAX_ENCHANTING_POINT)

				tempPizzazPoint += getPointsBySpell(spellId);// Adding temporary
																// pizzaz point,
																// if the player
																// logout, he
																// loses them

			else
				tempPizzazPoint = MageGameConstants.MAX_ENCHANTING_POINT - enchantmentPizzazPoint;*/
			if (player.isCarryingProgressHat()){
				if(enchantmentPizzazPoint + getPointsBySpell(spellId) >= MageGameConstants.MAX_ENCHANTING_POINT)
					enchantmentPizzazPoint = MageGameConstants.MAX_ENCHANTING_POINT;
				else
					enchantmentPizzazPoint += getPointsBySpell(spellId);
			}

		} else {

			/* if the player is enchanting the item shown as bonus */

			//if (bonusItem == getStringById(itemId) && (enchantmentPizzazPoint + tempPizzazPoint) <= MageGameConstants.MAX_ENCHANTING_POINT && player.hasProgressHat()) {
			if (bonusItem == getStringById(itemId) && enchantmentPizzazPoint < MageGameConstants.MAX_ENCHANTING_POINT && player.hasProgressHat()) {
				player.getActionSender().sendMessage("You recieve 1 bonus point!");
				enchantmentPizzazPoint += 1;
			}

			/*
			 * extra points handling : win points depending on the enchanting
			 * point used: will be added when deposit orb
			 */

			enchantSpellsUsed[getEnchantingIndex(spellId)] += 1;
		}

		/* standard enchanting methods */
		player.getInventory().removeItem(new Item(itemId));
		player.getInventory().addItem(new Item(orb));

	}

	/* deposit the orbs into the hole */

	private void depositOrbs() {

		if (!player.getInventory().getItemContainer().contains(orb)) {
			player.getActionSender().sendMessage("You don't have any orbs to deposit.");
			return;
		}
		int count = player.getInventory().getItemContainer().getCount(orb);
		int extraPoints = 0;
		// checks if the max point is reached or not
		/*if ((enchantmentPizzazPoint + tempPizzazPoint) <= MageGameConstants.MAX_ENCHANTING_POINT) {
			tempPizzazPoint += Math.floor(count / 10) * 10;
			for (int i = 0; i < enchantSpellsUsed.length; i++)
				while (enchantSpellsUsed[i] - 10 >= 0) {
					enchantSpellsUsed[i] -= 10;
					tempPizzazPoint += getExtraPointsByIndex(i);
					extraPoints += getExtraPointsByIndex(i);
				}
		} else
			tempPizzazPoint = MageGameConstants.MAX_ENCHANTING_POINT - enchantmentPizzazPoint;*/
		if (enchantmentPizzazPoint < MageGameConstants.MAX_ENCHANTING_POINT && player.isCarryingProgressHat()) {
			enchantmentPizzazPoint += Math.floor(count / 10) * 10;
			for (int i = 0; i < enchantSpellsUsed.length; i++)
				while (enchantSpellsUsed[i] - 10 >= 0) {
					enchantSpellsUsed[i] -= 10;
					enchantmentPizzazPoint += getExtraPointsByIndex(i);
					extraPoints += getExtraPointsByIndex(i);
				}
			if(enchantmentPizzazPoint >= MageGameConstants.MAX_ENCHANTING_POINT)
				enchantmentPizzazPoint = MageGameConstants.MAX_ENCHANTING_POINT;
		}
		player.getActionSender().sendChatInterface(359);
		player.getActionSender().sendString("You've just deposited " + count + " orbs, earning you " + (int) (Math.floor(count / 10) * 10) + " Enchanting Pizazz", 360);
		player.getActionSender().sendString("Points and " + extraPoints + " extra points for the enchanting spell used.", 361);
		resetEnchantingSpells();
		player.getInventory().removeItem(new Item(orb, player.getInventory().getItemAmount(orb)));
		player.getUpdateFlags().sendAnimation(832, 0);
		if(count >= 20){
			int item = rewardRune[random.nextInt(rewardRune.length)];
			Item rewardItem = new Item(item, 3);
			player.getInventory().addItemOrDrop(rewardItem);
			player.getActionSender().sendMessage("Congratulations! You've been rewarded with 3 "+rewardItem.getDefinition().getName()+"s for your efforts.");
		}

	}

	/* reset the enchanting spells after depositing orbs */

	private void resetEnchantingSpells() {
		for (int i = 0; i < enchantSpellsUsed.length; i++)
			enchantSpellsUsed[i] = 0;
	}

}