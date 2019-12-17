package com.rs2.model.content.skills;

import com.rs2.Constants;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class Tools {

	public enum Tool {
		
		DRAGON_AXE(6739, 61, 80, new int[]{2846,2847}, 0, 6743, 492, 6741, Skill.WOODCUTTING),
		RUNE_AXE(1359, 41, 70, new int[]{867,868}, 0, 520, 492, 506, Skill.WOODCUTTING),
		ADAMANT_AXE(1357, 31, 60, new int[]{869,870}, 0, 518, 492, 504, Skill.WOODCUTTING),
		MITHRIL_AXE(1355, 21, 50, new int[]{871,872}, 0, 516, 492, 502, Skill.WOODCUTTING),
		BLACK_AXE(1361, 6, 40, new int[]{873,874}, 0, 514, 492, 500, Skill.WOODCUTTING),
		STEEL_AXE(1353, 6, 30, new int[]{875,876}, 0, 512, 492, 498, Skill.WOODCUTTING),
		IRON_AXE(1349, 1, 20, new int[]{877,878}, 0, 510, 492, 496, Skill.WOODCUTTING),
		BRONZE_AXE(1351, 1, 0, new int[]{879,880}, 0, 508, 492, 494, Skill.WOODCUTTING),
		
		RUNE_PICKAXE(1275, 41, 70, new int[]{624}, 3, 490, 466, 478, Skill.MINING),
		ADAMANT_PICKAXE(1271, 31, 60, new int[]{628}, 4, 488, 466, 476, Skill.MINING),
		MITHRIL_PICKAXE(1273, 21, 50, new int[]{629}, 6, 486, 466, 474, Skill.MINING),
		STEEL_PICKAXE(1269, 6, 30, new int[]{627}, 7, 484, 466, 472, Skill.MINING),
		IRON_PICKAXE(1267, 1, 20, new int[]{626}, 8, 482, 466, 470, Skill.MINING),
		BRONZE_PICKAXE(1265, 1, 0, new int[]{625}, 9, 480, 466, 468, Skill.MINING);

		private int id;
		private int level;
		private int[] animation;
		private int bonus;
		private int essSpeed;
		private int head;
		private int handle;
		private int broken;
		private int skill;

		private Tool(int id, int level, int bonus, int[] animation, int essSpeed, int head, int handle, int broken, int skill) {
			this.id = id;
			this.level = level;
			this.bonus = bonus;
			this.animation = animation;
			this.essSpeed = essSpeed;
			this.head = head;
			this.broken = broken;
			this.skill = skill;
		}

		public int getId() {
			return id;
		}

		public int getLevel() {
			return level;
		}

		public int getHead() {
			return head;
		}

		public int getAnimation() {
			return animation[0];
		}
		
		public int getSpecialAnim() {
			return animation[1];
		}

		public int getBonus() {
			return bonus;
		}

		public int getEssSpeed() {
			return essSpeed;
		}

		public int getBroken() {
			return broken;
		}

		public int getSkill() {
			return skill;
		}

		public int getHandle() {
			return handle;
		}

		public int getRepairCost() {
			Item broken = new Item(this.getBroken(), 1);
			return broken.getDefinition().getPrice();
		}

	}

	public static Tool getTool(Player player, int skillId) {
        for (Tool tool : Tool.values()) {
        	if (tool.getSkill() == skillId) {
        		if (player.getSkill().getLevel()[skillId] >= tool.getLevel() && (player.getEquipment().getId(Constants.WEAPON) == tool.getId() || player.getInventory().playerHasItem(tool.getId()))) {
					return tool;
				}
			}
		}
        return null;
	}
	
	public static Tool getBrokenTool(Player player, int itemId) {
        for (Tool tool : Tool.values()) {
        	if (tool.getBroken() == itemId) {
        		return tool;
			}
		}
        return null;
	}

	public static boolean attachTool(Player player, int item1, int item2) {
        for (Tool tool : Tool.values()) {
        	if ((tool.getHead() == item1 || tool.getHead() == item2) && (tool.getHandle() == item1 || tool.getHandle() == item2)) {
        		player.getInventory().removeItem(new Item(item1, 1));
        		player.getInventory().removeItem(new Item(item2, 1));
        		player.getInventory().addItem(new Item(tool.getId(), 1));
        		return true;
			}
        }
        return false;
	}

	public static void breakTool(Player player, int skillId) {
		final Tool tool = Tools.getTool(player, skillId);
		if (player.getEquipment().getId(Constants.WEAPON) == tool.getId()) {
			player.getEquipment().replaceEquipment(tool.getBroken(), Constants.WEAPON);
		} else if (player.getInventory().playerHasItem(tool.getId(), 1)) {
			player.getInventory().removeItem(new Item(tool.getId(), 1));
			player.getInventory().addItem(new Item(tool.getBroken(), 1));
		}
	}

	public static boolean repairTool(Player player, int item) {
        for (Tool tool : Tool.values()) {
        	if (tool.getBroken() == item) {
        		if (!player.getInventory().playerHasItem(new Item(995, tool.getRepairCost()))) {
        			player.getActionSender().sendMessage("You don't have enough coins to fix that.");
        			return false;
        		}
        		player.getInventory().removeItem(new Item(item, 1));
        		player.getInventory().removeItem(new Item(995, tool.getRepairCost()));
        		player.getInventory().addItem(new Item(tool.getId(), 1));
        		return true;
			}
        }
        return false;
	}

}
