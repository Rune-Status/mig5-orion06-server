package com.rs2.model.content.skills.cooking;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.rs2.Constants;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.GameObjectData;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Position;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class Cooking {

	private Player player;
    public Position firePosition;

	public Cooking(Player player) {
		this.player = player;
	}

	private static final int GAUNTLET = 775;

	public static enum CookingItems { // raw cooked burnt level exp burn
										// stoplevel burnstoplevelwith
		BURNT_MEAT(2142, 2146, 2146, 1, 0, 0, 0, true), SEA_WEED(401, 1781, 1781, 1, 0, 0, 0, true), KELP(7516, 1781, 1781, 1, 0, 0, 0, true), BEEF_MEAT(2132, 2142, 2146, 1, 30.0, 7, 7, true), RAT_MEAT(2134, 2142, 2146, 1, 30.0, 7, 7, true), BEAR_MEAT(2136, 2142, 2146, 1, 30.0, 7, 7, true), CHICKEN(2138, 2140, 2144, 1, 30.0, 7, 7, true), RABBIT(3226, 3228, 7222, 1, 30.0, 7, 7, true), UHTHANKI(1859, 1861, 323, 1, 40.0, 7, 7, true), THIN_SNAIL(3363, 3369, 3375, 12, 70, 17, 17, true), LEAN_SNAIL(3365, 3371, 3375, 17, 80, 26, 26, true), SPIDER_STICK(6293, 6297, 6301, 16, 80, 25, 25, true), SPIDER_SHAFT(6295, 6299, 6303, 16, 80,
				25, 25, true), ROASTED_RABBIT(3226, 7223, 7222, 16, 72, 25, 25, true), FAT_SNAIL(3367, 3373, 3375, 22, 95, 28, 28, true), CHOMPY(2876, 2878, 2880, 30, 140, 38, 38, true), RED_BERRY_PIE(2321, 2325, 2329, 10, 78, 15, 15, false), MEAT_PIE(2317, 2327, 2329, 20, 110, 25, 25, false), MUD_PIE(2319, 7170, 2329, 29, 128, 35, 35, false), APPLE_PIE(7168, 2323, 2329, 30, 130, 35, 35, false), GARDEN_PIE(7176, 7178, 2329, 34, 138, 39, 39, false), FISH_PIE(7186, 7188, 2329, 47, 164, 52, 52,
				false), ADMIRAL_PIE(7196, 7198, 2329, 70, 210, 77, 77, false), WILD_PIE(7206, 7208, 2329, 85, 240, 90, 90, false), SUMMER_PIE(7216, 7218, 2329, 95, 260, 100, 100, false), STEW(2001, 2003, 2005, 25, 117, 30, 30, true), CURRY(2009, 2011, 2013, 60, 280, 65, 65, true), PIZZA(2287, 2289, 2305, 35, 143, 38, 38, true), CAKE(1889, 1891, 1903, 40, 180, 38, 38, false), BREAD(2307, 2309, 2311, 1, 40, 5, 5, false), PITTA_BREAD(1863, 1865, 1867, 58, 40, 65, 65, true), SPICY_SAUCE(7072, 7072,
				2880, 9, 25, 38, 38, true), SCRAMBLED_EGG(7076, 7078, 7090, 13, 50, 16, 16, true), FRIED_ONION(1871, 7084, 7092, 42, 60, 45, 45, true), FRIED_MUSHROOM(7080, 7082, 7094, 46, 60, 52, 52, true), POTATO(1942, 6701, 6699, 9, 25, 38, 38, false), NETTLE(4237, 4239, 6699, 9, 25, 38, 38, true), BARLEY(6006, 6008, 6008, 1, 0, 1, 1, true), SHRIMP(317, 315, 323, 1, 30.0, 34, 34, true), KARAMBWANJI(3150, 3151, 3148, 1, 10, 34, 34, true), SARDINE(327, 325, 369, 1, 40, 38, 38, true), ANCHOVIES(
				321, 319, 323, 1, 30, 34, 34, true), HERRING(345, 347, 357, 5, 50, 37, 37, true), MACKEREL(353, 355, 357, 10, 60, 35, 35, true), TROUT(335, 333, 343, 15, 70, 50, 50, true), COD(341, 339, 343, 17, 75, 39, 39, true), PIKE(349, 351, 343, 20, 80, 52, 52, true), SALMON(331, 329, 343, 25, 90, 58, 58, true), SLIMY_EEL(3379, 3381, 3383, 28, 95, 58, 58, true), TUNA(359, 361, 367, 30, 100, 65, 65, true), KARAMBWAN(3142, 3144, 3148, 30, 190, 100, 100, true), CAVE_EEL(5001, 5003, 5002, 38,
				115, 40, 40, true), LOBSTER(377, 379, 381, 40, 120, 74, 66, true), BASS(363, 365, 367, 43, 130, 80, 80, true), SWORDFISH(371, 373, 375, 45, 140, 86, 81, true), LAVA_EEL(2148, 2149, 3383, 53, 60, 72, 72, true), SHARK(383, 385, 387, 80, 210, 104, 94, true), SEA_TURTLE(395, 397, 399, 82, 212, 110, 110, true), MANTA_RAY(389, 391, 393, 91, 216, 112, 112, true);

		private int rawId;
		private int cookedId;
		private int burntId;
		private int cookLevel;
		private double experience;
		private int burnStopLevel;
		private int burnStopLevelWith;
		private boolean fireCook;

		private static Map<Integer, CookingItems> cookingItems = new HashMap<Integer, CookingItems>();

		public static CookingItems forId(int id) {
			return cookingItems.get(id);
		}

		static {
			for (CookingItems item : CookingItems.values()) {
				cookingItems.put(item.rawId, item);
			}
		}

		private CookingItems(int rawId, int cookedId, int burntId, int cookLevel, double experience, int burnStopLevel, int burnStopLevelWith, boolean fireCook) {
			this.rawId = rawId;
			this.cookedId = cookedId;
			this.burntId = burntId;
			this.cookLevel = cookLevel;
			this.experience = experience;
			this.burnStopLevel = burnStopLevel;
			this.burnStopLevelWith = burnStopLevelWith;
			this.fireCook = fireCook;
		}

		public int getRawId() {
			return rawId;
		}

		public int getCookedId() {
			return cookedId;
		}

		public int getBurntID() {
			return burntId;
		}

		public int getCookLevel() {
			return cookLevel;
		}

		public double getExperience() {
			return experience;
		}

		public int getBurnStopLevel() {
			return burnStopLevel;
		}

		public int getBurnStopLevelWith() {
			return burnStopLevelWith;
		}

		public boolean fireCook() {
			return fireCook;
		}
	}

	public boolean handleInterface(int item, int objectId, int objectX, int objectY) {
		CookingItems cook = CookingItems.forId(item);
		if (cook == null)
			return false;
		player.getActionSender().removeInterfaces();
		if (!Constants.COOKING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		player.setOldObject(objectId);
		final CacheObject obj = ObjectLoader.object(objectId, objectX, objectY, player.getPosition().getZ());
		final GameObjectDef def = SkillHandler.getObject(objectId, objectX, objectY, player.getPosition().getZ());
        if (obj != null || def != null) {
			String name = GameObjectData.forId(obj != null ? obj.getDef().getId() : def.getId()).getName().toLowerCase();
			if (name.equalsIgnoreCase("fire") || name.equalsIgnoreCase("fireplace")) {
				player.setNewSkillTask();
				player.setStatedInterface("cookFire");
                firePosition = new Position(objectX,  objectY, player.getPosition().getZ());
				player.setTempInteger(item);
				if (player.questStage[0] != 1 || player.getInventory().getItemAmount(item) == 1) {
					handleCookingTick(player, 1);
					return true;
				}
				Item itemDef = new Item(item);
				player.getActionSender().sendItemOnInterface(13716, 200, item);
				player.getActionSender().sendString("" + itemDef.getDefinition().getName() + "", 13717);
				player.getActionSender().sendChatInterface(1743);
				return true;
			}
			if (name.equalsIgnoreCase("stove") || name.equalsIgnoreCase("range") || name.equalsIgnoreCase("cooking range") || name.equalsIgnoreCase("cooking pot")) {
				player.setNewSkillTask();
				player.setStatedInterface("cookRange");
				player.setTempInteger(item);
				if (player.questStage[0] != 1 || player.getInventory().getItemAmount(item) == 1) {
					handleCookingTick(player, 1);
					player.getUpdateFlags().sendFaceToDirection(new Position(player.getPosition().getX(), player.getPosition().getY() - 1));
					return true;
				}
				Item itemDef = new Item(item);
				player.getActionSender().sendItemOnInterface(13716, 200, item);
				player.getActionSender().sendString("" + itemDef.getDefinition().getName() + "", 13717);
				player.getActionSender().sendChatInterface(1743);
				return true;
			}
		}
		return true;
	}

	public static void handleCookingTick(final Player player, final int amount) {
		final int task = player.getTask();
		player.getMovementHandler().reset();
		player.getActionSender().removeInterfaces();
		player.setSkilling(new CycleEvent() {
			int cookAmount = amount;
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkNewSkillTask() || !player.checkTask(task) || !player.getInventory().getItemContainer().contains(player.getTempInteger()) || cookAmount == 0) {
					player.setTempInteger(0);
					container.stop();
					return;
				}

                if(player.getStatedInterface().equals("cookFire")){
                	if (!SkillHandler.checkObject(player.getOldObject(), player.getCooking().firePosition.getX(), player.getCooking().firePosition.getY(), player.getCooking().firePosition.getZ())) {
                		container.stop();
                        return;
                	}
                }
				handleCooking(player);
				cookAmount--;
				container.setTick(4);
			}

			@Override
			public void stop() {
				player.resetAnimation();
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 1);

	}

	// TODO sounds + correct message & amount interface which is a basic version
	// you can see above ^
	// the ground
	public static void handleCooking(final Player player) {
		final CookingItems toCook = CookingItems.forId(player.getTempInteger());
		if (toCook == null)
			return; // Means the item exists in cooking enum
		if (player.getSkill().getLevel()[Skill.COOKING] < toCook.getCookLevel()) {
			player.getDialogue().sendStatement("You need a cooking level of " + toCook.getCookLevel() + " to cook this.");
			return;
		}
		player.getActionSender().removeInterfaces();
		player.getInventory().removeItem(new Item(player.getTempInteger()));
		if (player.getStatedInterface() == "cookFire")
			player.getUpdateFlags().sendAnimation(897);
		else if (player.getStatedInterface() == "cookRange")
			player.getUpdateFlags().sendAnimation(883);
		player.getActionSender().sendSound(357, 1, 0);
		if (player.questStage[0] != 1) {
			if (player.questStage[0] == 13) {
				giveRewards(player, player.getTempInteger(), true);
				player.setNextTutorialStage();
			} else if (player.questStage[0] == 14) {
				giveRewards(player, player.getTempInteger(), false);
				player.setNextTutorialStage();
			} else if (player.questStage[0] == 19 && player.getTempInteger() == 2307) {
				giveRewards(player, player.getTempInteger(), false);
				player.getActionSender().createPlayerHints(1, -1);
				player.setNextTutorialStage();
			} else {
				giveRewards(player, player.getTempInteger(), false);
			}
			player.getQuestHandler().setTutorialStage();
			return;
		}
		if (!toCook.fireCook() && player.getStatedInterface() == "cookFire")
			giveRewards(player, player.getTempInteger(), true);
		else
			giveRewards(player, player.getTempInteger(), false);

		/* some food always burns on fire */

	}

	public static boolean cookedSuccessfully(Player player, int cookLevel, int burnStopLevelWith, int burnStopLevel) {
		/*
		 * The burning formula by Clifton
		 */
		final double burnBonus = 3.0;
		double burn_chance = 50.0 - burnBonus; //55.0
		final double cook_level = player.getSkill().getLevel()[Skill.COOKING];
		final double lev_needed = cookLevel;
		final double burn_stop = (player.getEquipment().getItemContainer().get(Constants.HANDS) != null && player.getEquipment().getItemContainer().get(Constants.HANDS).getId() == GAUNTLET) ? burnStopLevelWith : burnStopLevel;
		final double multi_a = burn_stop - lev_needed;
		final double burn_dec = burn_chance / multi_a;
		final double multi_b = cook_level - lev_needed;
		burn_chance -= multi_b * burn_dec;
		double randNum = new Random().nextDouble() * 100.0;
		return burn_chance <= randNum;
	}

	public static void giveRewards(Player player, int item, boolean forcedBurn) {
		CookingItems food = CookingItems.forId(item);
		if ((cookedSuccessfully(player, food.getCookLevel(), food.getBurnStopLevelWith(), food.getBurnStopLevel()) && !forcedBurn) || (player.questStage[0] == 14 || player.questStage[0] == 19)) {
			player.getInventory().addItem(new Item(food.getCookedId()));
			player.getSkill().addExp(Skill.COOKING, food.getExperience());
			if(food.getCookedId() != 2146)
				player.getActionSender().sendMessage("You successfully cook a " + ItemDefinition.forId(food.getCookedId()).getName().toLowerCase() + ".");
			else
				player.getActionSender().sendMessage("You deliberately burn the perfectly good piece of meat.");
		} else {
			player.getInventory().addItem(new Item(food.getBurntID()));
			if(food.getCookedId() != 2146)
				player.getActionSender().sendMessage("You accidentally burn the " + ItemDefinition.forId(food.getCookedId()).getName().toLowerCase() + ".");
			else
				player.getActionSender().sendMessage("You deliberately burn the perfectly good piece of meat.");
		}
	}

	@SuppressWarnings("unused")
	private static int[] stoveIds = { 3039, 114, 2727, 2728, 2729, 2730, 2731 };
	@SuppressWarnings("unused")
	private static int[] fireIds = { 2732, 2724, 2725, 2726 };

	// TODO set amount limit, so it stops when u chose 5 and not goes futher
	// then that.
	// playSound(240, 0, 0); -- good
	// playSound(1053, 0, 0); -- burned
	/**
	 * All objects containing water ingame.
	 */
	public static final int[] WATER_CONTAINING_OBJECTS = { 153, // Fountain
			879, // Fountain
			880, // Fountain
			2654, // Sinclair family fountain
			2864, // Fountain
			6232, // Fountain
			10436, // Fountain
			10437, // Fountain
			11007, // Fountain
			11759, // Fountain
			13478, // Small fountain
			13479, // Large fountain
			13480, // Ornamental fountain
			21764, // Fountain
			24161, // Fountain
			24214, // Fountain
			24265, // Fountain
			884, 11793, 43,

			873, // Sink
			874, // Sink
			4063, // Sink
			6151, // Sink
			8699, // Sink
			9143, // Sink
			9684, // Sink
			10175, // Sink
			12279, // Sink
			12974, // Sink
			13563, // Sink
			13564, // Sink
			14868, // Sink
			14917, // Sink
			15678, // Sink
			16704, // Sink
			16705, // Sink
			20358, // Sink
			22715, // Sink
			24112, // Sink
			24314, // Sink

			11661, // Waterpump in falador.

			4176, // Tap
			4285, // Tap
			4482, // Tap
			6827, // Waterpump in Morytania
			

	};

	public static boolean handleButtons(Player player, int buttonId) {
		switch (buttonId) {
		case 13720:// cook 1
			handleCookingTick(player, 1);
			return true;
		case 13719:// cook 5
			handleCookingTick(player, 5);
			return true;
		case 13717:// cook all
			handleCookingTick(player, 28);
			return true;
		}
		return false;
	}

	public boolean handleFillingObjectWater(int item, int object) {
		for (int i = 0; i < WATER_CONTAINING_OBJECTS.length; i++) {
			if (object == WATER_CONTAINING_OBJECTS[i]) {
				if(item >= 5331 && item < 5340){// watering can
					player.getInventory().getItemContainer().replace(item, 5340);
					player.getUpdateFlags().sendAnimation(832);
					player.getActionSender().sendMessage("You fill the " + ItemDefinition.forId(5331).getName().toLowerCase() + " from the " + GameObjectData.forId(object).getName().toLowerCase() + ".");
					player.getActionSender().sendSound(1039, 1, 0);
					return true;
				}
				switch (item) {
				case 1831:
				case 1829:
				case 1827:
				case 1825:
					player.getInventory().getItemContainer().replace(item, 1823);
					player.getUpdateFlags().sendAnimation(832);
					player.getActionSender().sendMessage("You fill the " + ItemDefinition.forId(item).getName().toLowerCase() + " from the " + GameObjectData.forId(object).getName().toLowerCase() + ".");
					player.getActionSender().sendSound(1039, 1, 0);
					return true;

				case 229:// Vial
					player.getInventory().getItemContainer().replace(229, 227);
					player.getUpdateFlags().sendAnimation(832);
					player.getActionSender().sendMessage("You fill the " + ItemDefinition.forId(item).getName().toLowerCase() + " from the " + GameObjectData.forId(object).getName().toLowerCase() + ".");
					player.getActionSender().sendSound(1039, 1, 0);
					return true;

				case 1925:// Bucket
					player.getInventory().getItemContainer().replace(1925, 1929);
					player.getUpdateFlags().sendAnimation(832);
					player.getActionSender().sendMessage("You fill the " + ItemDefinition.forId(item).getName().toLowerCase() + " from the " + GameObjectData.forId(object).getName().toLowerCase() + ".");
					player.getActionSender().sendSound(1039, 1, 0);
					return true;

				case 1923:// Bowl
					player.getInventory().getItemContainer().replace(1923, 1921);
					player.getUpdateFlags().sendAnimation(832);
					player.getActionSender().sendMessage("You fill the " + ItemDefinition.forId(item).getName().toLowerCase() + " from the " + GameObjectData.forId(object).getName().toLowerCase() + ".");
					player.getActionSender().sendSound(1039, 1, 0);
					return true;

				case 1935:// jug
					player.getInventory().getItemContainer().replace(1935, 1937);
					player.getUpdateFlags().sendAnimation(832);
					player.getActionSender().sendMessage("You fill the " + ItemDefinition.forId(item).getName().toLowerCase() + " from the " + GameObjectData.forId(object).getName().toLowerCase() + ".");
					player.getActionSender().sendSound(1039, 1, 0);
					return true;

				case 1980:// cup
					player.getInventory().getItemContainer().replace(1980, 4458);
					player.getUpdateFlags().sendAnimation(832);
					player.getActionSender().sendMessage("You fill the " + ItemDefinition.forId(item).getName().toLowerCase() + " from the " + GameObjectData.forId(object).getName().toLowerCase() + ".");
					player.getActionSender().sendSound(1039, 1, 0);
					return true;

				/*case 5331:// watering can
					player.getInventory().getItemContainer().replace(5331, 5340);
					player.getUpdateFlags().sendAnimation(832);
					player.getActionSender().sendMessage("You fill the " + ItemDefinition.forId(item).getName().toLowerCase() + " from the " + GameObjectData.forId(object).getName().toLowerCase() + ".");
					player.getActionSender().sendSound(1039, 1, 0);
					return true;*/
				}
			}
		}
		return false;

	}
}
