package com.rs2.model.content.skills.crafting;

import java.util.HashMap;

import com.rs2.Constants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 21/12/11 Time: 17:17 To change
 * this template use File | Settings | File Templates.
 */
public class GlassMaking {

	public static final int MOLTEN_GLASS = 1775;
	public static final int GLASSBLOWING_PIPE = 1785;
	public static final int BUCKET = 1925;
	public static final int BUCKET_OF_SAND = 1783;
	public static final int SODA_ASH = 1781;
	private static final int FILL_BUCKET = 895;
	private static final int GLASS_MAKING = 884;
	private static final int FURNACE = 899;

	public static void fillWithSand(Player player) {
		if (!Constants.CRAFTING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (!player.getInventory().getItemContainer().contains(BUCKET)) {
			player.getDialogue().sendStatement("You need a wooden bucket to do that.");
			return;
		}
		player.getUpdateFlags().sendAnimation(FILL_BUCKET);
		player.getActionSender().sendMessage("You fill your bucket with sand.");
		player.getInventory().removeItem(new Item(BUCKET));
		player.getInventory().addItem(new Item(BUCKET_OF_SAND));
	}

	public static void makeMoltenGlass(Player player) {
		if (!Constants.CRAFTING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (!player.getInventory().getItemContainer().contains(BUCKET_OF_SAND)) {
			player.getDialogue().sendStatement("You need a bucket of sand to do that.");
			return;
		}
		if (!player.getInventory().getItemContainer().contains(SODA_ASH)) {
			player.getDialogue().sendStatement("You need soda ash to do that.");
			return;
		}
		player.getUpdateFlags().sendAnimation(FURNACE);
		player.getSkill().addExp(Skill.CRAFTING, 20);
		player.getActionSender().sendMessage("You heat the sand and soda ash in the furnace to make glass.");
		player.getInventory().removeItem(new Item(BUCKET_OF_SAND));
		player.getInventory().removeItem(new Item(SODA_ASH));
		player.getInventory().addItem(new Item(BUCKET));
		player.getInventory().addItem(new Item(MOLTEN_GLASS));
	}
	public static enum GlassMake {
		VIAL(11474, 1775, 1781, 229, 1, 33, 35), VIAL5(11473, 1775, 1781, 229, 5, 33, 35), VIAL10(11472, 1775, 1781, 229, 10, 33, 35), VIALX(11471, 1775, 1781, 229, 0, 33, 35), ORB(12396, 1775, 1781, 567, 1, 46, 52.5), ORB5(12395, 1775, 1781, 567, 5, 46, 52.5), ORB10(12394, 1775, 1781, 567, 10, 46, 52.5), ORBX(11475, 1775, 1781, 567, 0, 46, 52.5), BEER(12400, 1775, 1781, 1919, 1, 1, 17.5), BEER5(12399, 1775, 1781, 1919, 5, 1, 17.5), BEER10(12398, 1775, 1781, 1919, 10, 1, 17.5), BEERX(12397, 1775, 1781, 1919, 0, 1, 17.5), CANDLE(12404, 1775, 1781, 4527, 1, 4, 19), CANDLE5(12403, 1775, 1781, 4527, 5, 4, 19), CANDLE10(12402, 1775, 1781, 4527, 10, 4, 19), CANDLEX(12401, 1775, 1781, 4527, 0, 4, 19), OIL_LAMP(12408, 1775, 1781, 4522, 1, 12, 25), OIL_LAMP5(12407, 1775, 1781, 4522, 5, 12, 25), OIL_LAMP10(12406, 1775, 1781, 4522, 10, 12, 25), OIL_LAMPX(12405, 1775, 1781, 4522, 0, 12, 25), FISHBOWL(6203, 1775, 1781, 6667, 1, 42, 42.5), FISHBOWL5(6202, 1775, 1781, 6667, 5, 42, 42.5), FISHBOWL10(
				6201, 1775, 1781, 6667, 10, 42, 42.5), FISHBOWLX(6200, 1775, 1781, 6667, 0, 12, 25), LANTERN_LENS(12412, 1775, 1781, 4542, 1, 49, 55), LANTERN_LENS5(12411, 1775, 1781, 4542, 5, 49, 55), LANTERN_LENS10(12410, 1775, 1781, 4542, 10, 49, 55), LANTERN_LENSX(12409, 1775, 1781, 4542, 0, 49, 55);

		private int buttonId;
		private int used;
		private int used2;
		private int result;
		private int amount;
		private int level;
		private double experience;

		public static HashMap<Integer, GlassMake> glassItems = new HashMap<Integer, GlassMake>();

		public static GlassMake forId(int id) {
			return glassItems.get(id);
		}

		static {
			for (GlassMake data : GlassMake.values()) {
				glassItems.put(data.buttonId, data);
			}
		}

		private GlassMake(int buttonId, int used, int used2, int result, int amount, int level, double experience) {
			this.buttonId = buttonId;
			this.used = used;
			this.used2 = used2;
			this.result = result;
			this.amount = amount;
			this.level = level;
			this.experience = experience;
		}

		public int getButtonId() {
			return buttonId;
		}

		public int getUsed() {
			return used;
		}

		public int getUsed2() {
			return used2;
		}

		public int getResult() {
			return result;
		}

		public int getAmount() {
			return amount;
		}

		public int getLevel() {
			return level;
		}

		public double getExperience() {
			return experience;
		}

	}
	public static boolean makeSilver(final Player player, int buttonId, final int amount) {
		final GlassMake glassMake = GlassMake.forId(buttonId);
		if (glassMake == null || (glassMake.getAmount() == 0 && amount == 0))
			return false;
		if (player.getStatedInterface() == "glassMaking") {
			if (!Constants.CRAFTING_ENABLED) {
				player.getActionSender().sendMessage("This skill is currently disabled.");
				return true;
			}
			if (!player.getInventory().getItemContainer().contains(GLASSBLOWING_PIPE)) {
				player.getDialogue().sendStatement("You need a glassblowing pipe to do this.");
				return true;
			}
			if (!player.getInventory().getItemContainer().contains(MOLTEN_GLASS)) {
				player.getDialogue().sendStatement("You need a molten glass to do this.");
				return true;
			}
			if (player.getSkill().getLevel()[Skill.CRAFTING] < glassMake.getLevel()) {
				player.getDialogue().sendStatement("You need a crafting level of " + glassMake.getLevel() + " to make this.");
				return true;
			}
		    player.getActionSender().removeInterfaces();
			player.getUpdateFlags().sendAnimation(GLASS_MAKING);

			final int task = player.getTask();
			player.setSkilling(new CycleEvent() {
				int amnt = glassMake.getAmount() != 0 ? glassMake.getAmount() : amount;

				@Override
				public void execute(CycleEventContainer container) {

					if (!player.checkTask(task) || amnt == 0 || !player.getInventory().getItemContainer().contains(MOLTEN_GLASS)) {
						container.stop();
						return;
					}
					container.setTick(3);
					player.getUpdateFlags().sendAnimation(GLASS_MAKING);
					player.getActionSender().sendMessage("You make the molten glass into a " + new Item(glassMake.getResult()).getDefinition().getName() + ".");
					player.getInventory().removeItem(new Item(MOLTEN_GLASS));
					player.getInventory().addItem(new Item(glassMake.getResult()));
					player.getSkill().addExp(Skill.CRAFTING, glassMake.getExperience());
					amnt--;

				}

				@Override
				public void stop() {
					player.resetAnimation();
				}
			});
			CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 1);
			return true;
		}
		return false;
	}

}
