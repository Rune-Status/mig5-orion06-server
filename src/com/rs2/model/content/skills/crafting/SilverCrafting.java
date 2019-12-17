package com.rs2.model.content.skills.crafting;

import java.util.HashMap;

import com.rs2.Constants;
import com.rs2.model.content.skills.Menus;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 21/12/11 Time: 16:05 To change
 * this template use File | Settings | File Templates.
 */
public class SilverCrafting {
	public static final int SILVER_BAR = 2355;
	private static final int SILVER_ANIMATION = 899;

	public static enum SilverCraft {
		UNSTRUNG(8889, 2355, 1714, 1, 16, 50),
		UNSTRUNG5(8888, 2355, 1714, 5, 16, 50),
		UNSTRUNG10(8887, 2355, 1714, 10, 16, 50),
		UNSTRUNGX(8886, 2355, 1714, 0, 16, 50),
		SICKLE(8893, 2355, 2961, 1, 18, 50),
		SICKLE5(8892, 2355, 2961, 5, 18, 50),
		SICKLE10(8891, 2355, 2961, 10, 18, 50),
		SICKLEX(8890, 2355, 2961, 0, 18, 50),
		TIARA(8897, 2355, 5525, 1, 23, 52.5),
		TIARA5(8896, 2355, 5525, 5, 23, 52.5),
		TIARA10(8895, 2355, 5525, 10, 23, 52.5),
		TIARAX(8894, 2355, 5525, 0, 23, 52.5);

		private int buttonId;
		private int used;
		private int result;
		private int amount;
		private int level;
		private double experience;

		public static HashMap<Integer, SilverCraft> silverItems = new HashMap<Integer, SilverCraft>();

		public static SilverCraft forId(int id) {
			return silverItems.get(id);
		}

		static {
			for (SilverCraft data : SilverCraft.values()) {
				silverItems.put(data.buttonId, data);
			}
		}

		private SilverCraft(int buttonId, int used, int result, int amount, int level, double experience) {
			this.buttonId = buttonId;
			this.used = used;
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
		final SilverCraft silverCraft = SilverCraft.forId(buttonId);
		if (silverCraft == null || (silverCraft.getAmount() == 0 && amount == 0)) {
			return false;
		}
		if (silverCraft.getUsed() == SILVER_BAR && player.getStatedInterface() == "silverCrafting") {
			if (!Constants.CRAFTING_ENABLED) {
				player.getActionSender().sendMessage("This skill is currently disabled.");
				return true;
			}
			if (!player.getInventory().getItemContainer().contains(SILVER_BAR)) {
				player.getDialogue().sendStatement("You need a silver bar to do this.");
				return true;
			}
			int mould = -1;
			if(silverCraft.getResult() == 1714)//holy symbol
				mould = 1599;
			if(silverCraft.getResult() == 2961)//sickle
				mould = 2976;
			if(silverCraft.getResult() == 5525)//tiara
				mould = 5523;
			if (!player.getInventory().getItemContainer().contains(mould)) {
				player.getDialogue().sendStatement("You don't have the reguired mould to do this.");
				return true;
			}
			if (player.getSkill().getLevel()[Skill.CRAFTING] < silverCraft.getLevel()) {
				player.getDialogue().sendStatement("You need a crafting level of " + silverCraft.getLevel() + " to make this.");
				return true;
			}
			player.getUpdateFlags().sendAnimation(SILVER_ANIMATION);
			player.getActionSender().sendSound(469, 1, 0);
		    player.getActionSender().removeInterfaces();

			final int task = player.getTask();
			player.setSkilling(new CycleEvent() {
				int amnt = silverCraft.getAmount() != 0 ? silverCraft.getAmount() : amount;

				@Override
				public void execute(CycleEventContainer container) {
					if (!player.checkTask(task) || amnt == 0 || !player.getInventory().getItemContainer().contains(SILVER_BAR)) {
						container.stop();
						return;
					}
					container.setTick(3);
					player.getUpdateFlags().sendAnimation(SILVER_ANIMATION);
					player.getActionSender().sendSound(469, 1, 0);
					player.getActionSender().sendMessage("You make the silver bar into " + Menus.determineAorAn(new Item(silverCraft.getResult()).getDefinition().getName().toLowerCase()) + " " + new Item(silverCraft.getResult()).getDefinition().getName().toLowerCase() + ".");
					player.getInventory().removeItem(new Item(SILVER_BAR));
					player.getInventory().addItem(new Item(silverCraft.getResult()));
					player.getSkill().addExp(Skill.CRAFTING, silverCraft.getExperience());
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
