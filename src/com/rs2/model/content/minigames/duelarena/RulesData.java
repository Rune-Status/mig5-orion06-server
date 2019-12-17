package com.rs2.model.content.minigames.duelarena;

import com.rs2.Constants;
import com.rs2.model.content.combat.AttackType;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 4/22/12 Time: 10:14 PM To change
 * this template use File | Settings | File Templates.
 */
public enum RulesData {
	NO_RANGED(2, 6725, 6698) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			if (NO_MELEE.activated(player) && NO_MAGIC.activated(player)) {
				if (message)
					player.getActionSender().sendMessage("You can't have no ranged, no melee and no magic - how would you fight?");
				return false;
			}
			if (!activated(player)) {
				if (player.getDuelMainData().getPlayerAttackType() == AttackType.RANGED)
					player.getDuelMainData().getItemToRemove().add(new Item(player.getEquipment().getId(Constants.WEAPON)));
			} else {
				if (player.getDuelMainData().getPlayerAttackType() == AttackType.RANGED)
					player.getDuelMainData().getItemToRemove().remove(new Item(player.getEquipment().getId(Constants.WEAPON)));
			}
			return true;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, "Players cannot use range");
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	},
	NO_MELEE(3, 6726, 6699) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			if (NO_RANGED.activated(player) && NO_MAGIC.activated(player)) {
				if (message)
					player.getActionSender().sendMessage("You can't have no ranged, no melee and no magic - how would you fight?");
				return false;
			}
			if (!activated(player)) {
				if (player.getDuelMainData().getPlayerAttackType() == AttackType.MELEE)
					player.getDuelMainData().getItemToRemove().add(new Item(player.getEquipment().getId(Constants.WEAPON)));
			} else {
				if (player.getDuelMainData().getPlayerAttackType() == AttackType.MELEE)
					player.getDuelMainData().getItemToRemove().remove(new Item(player.getEquipment().getId(Constants.WEAPON)));
			}
			return true;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, "Players cannot use melee");
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	},
	NO_MAGIC(4, 6727, 6697) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			if (NO_RANGED.activated(player) && NO_MELEE.activated(player)) {
				if (message)
					player.getActionSender().sendMessage("You can't have no ranged, no melee and no magic - how would you fight?");
				return false;
			}
			return true;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, "Players cannot use magic");
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	},

	NO_SPEC(10, 7816, 7817) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			return true;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, "Players cannot use special attacks.");
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	},
	FUN_WEAPON(8, 670, 669) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			boolean hasFunWeapon = false;
			for (int i : Constants.FUN_WEAPONS) {
				if (player.getInventory().getItemContainer().contains(i))
					hasFunWeapon = true;
			}
			if (!hasFunWeapon)
				if (message)
					player.getActionSender().sendMessage("Neither player has a 'fun weapon' for that.");
			return hasFunWeapon;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, "'Fun weapons' will be allowed.");
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	},
	NO_FORFEIT(0, 6721, 6696) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			if (NO_MOVEMENT.activated(player)) {
				if (message)
					player.getActionSender().sendMessage("You can't have no forfeit and no movement - you could run out of ammo");
				return false;
			} else if (NO_MELEE.activated(player)) {
				if (message)
					player.getActionSender().sendMessage("You can't have no forfeit and no melee - you could run out of ammo");
				return false;
			}
			return true;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, "Players cannot forfeit!");
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	},
	NO_DRINKS(5, 6728, 6701) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			return true;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, "Players cannot drink potions.");
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	},
	NO_FOOD(6, 6729, 6702) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			return true;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, "Players cannot eat foods.");
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	},
	NO_PRAYER(7, 6730, 6703) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			return true;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, "Players cannot use Prayers.");
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	},
	NO_MOVEMENT(1, 6722, 6704) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			if (OBSTACLES.activated(player)) {
				if (message)
					player.getActionSender().sendMessage("You can't have no movement and obstacles");
				return false;
			} else if (NO_FORFEIT.activated(player)) {
				if (message)
					player.getActionSender().sendMessage("You can't have no forfeit and no movement - you could run out of ammo");
				return false;
			}
			return true;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, "Players cannot move.");
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	},
	OBSTACLES(8, 6732, 6731) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			if (NO_MOVEMENT.activated(player)) {
				if (message)
					player.getActionSender().sendMessage("You can't have no movement and obstacles");
				return false;
			}
			return true;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, "There will be obstacles in the arena.");
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	},
	NO_HEAD(11, 13813, -1) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			if (!activated(player)) {
				player.getDuelMainData().getItemToRemove().add(new Item(player.getEquipment().getId(Constants.HAT)));
			} else {
				player.getDuelMainData().getItemToRemove().remove(new Item(player.getEquipment().getId(Constants.HAT)));
			}
			return true;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, null);
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	},
	NO_CAPE(12, 13814, -1) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			if (!activated(player)) {
				player.getDuelMainData().getItemToRemove().add(new Item(player.getEquipment().getId(Constants.CAPE)));
			} else {
				player.getDuelMainData().getItemToRemove().remove(new Item(player.getEquipment().getId(Constants.CAPE)));
			}
			return true;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, null);
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	},
	NO_AMULET(13, 13815, -1) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			if (!activated(player)) {
				player.getDuelMainData().getItemToRemove().add(new Item(player.getEquipment().getId(Constants.AMULET)));
			} else {
				player.getDuelMainData().getItemToRemove().remove(new Item(player.getEquipment().getId(Constants.AMULET)));
			}
			return true;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, null);
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	},
	NO_ARROW(21, 13816, -1) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			if (!activated(player)) {
				player.getDuelMainData().getItemToRemove().add(new Item(player.getEquipment().getId(Constants.ARROWS)));
			} else {
				player.getDuelMainData().getItemToRemove().remove(new Item(player.getEquipment().getId(Constants.ARROWS)));
			}
			return true;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, null);
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	},
	NO_WEAPON(14, 13817, -1) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			if (!activated(player)) {
				player.getDuelMainData().getItemToRemove().add(new Item(player.getEquipment().getId(Constants.WEAPON)));
			} else {
				player.getDuelMainData().getItemToRemove().remove(new Item(player.getEquipment().getId(Constants.WEAPON)));
			}
			return true;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, null);
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	},
	NO_BODY(15, 13818, -1) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			if (!activated(player)) {
				player.getDuelMainData().getItemToRemove().add(new Item(player.getEquipment().getId(Constants.CHEST)));
			} else {
				player.getDuelMainData().getItemToRemove().remove(new Item(player.getEquipment().getId(Constants.CHEST)));
			}
			return true;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, null);
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	},
	NO_SHIELD(16, 13819, -1) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			if (!activated(player)) {
				player.getDuelMainData().getItemToRemove().add(new Item(player.getEquipment().getId(new Item(player.getEquipment().getId(Constants.WEAPON)).getDefinition().isTwoHanded() ? Constants.WEAPON : Constants.SHIELD)));
			} else {
				player.getDuelMainData().getItemToRemove().remove(new Item(player.getEquipment().getId(new Item(player.getEquipment().getId(Constants.WEAPON)).getDefinition().isTwoHanded() ? Constants.WEAPON : Constants.SHIELD)));
			}
			return true;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, null);
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	},
	NO_LEGS(17, 13820, -1) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			if (!activated(player)) {
				player.getDuelMainData().getItemToRemove().add(new Item(player.getEquipment().getId(Constants.LEGS)));
			} else {
				player.getDuelMainData().getItemToRemove().remove(new Item(player.getEquipment().getId(Constants.LEGS)));
			}
			return true;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, null);
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	},
	NO_GLOVES(18, 13823, -1) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			if (!activated(player)) {
				player.getDuelMainData().getItemToRemove().add(new Item(player.getEquipment().getId(Constants.HANDS)));
			} else {
				player.getDuelMainData().getItemToRemove().remove(new Item(player.getEquipment().getId(Constants.HANDS)));
			}
			return true;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, null);
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	},
	NO_BOOTS(19, 13822, -1) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			if (!activated(player)) {
				player.getDuelMainData().getItemToRemove().add(new Item(player.getEquipment().getId(Constants.FEET)));
			} else {
				player.getDuelMainData().getItemToRemove().remove(new Item(player.getEquipment().getId(Constants.FEET)));
			}
			return true;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, null);
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	},
	NO_RINGS(20, 13821, -1) {
		@Override
		public boolean canActivate(Player player, boolean message) {
			if (!activated(player)) {
				player.getDuelMainData().getItemToRemove().add(new Item(player.getEquipment().getId(Constants.RING)));
			} else {
				player.getDuelMainData().getItemToRemove().remove(new Item(player.getEquipment().getId(Constants.RING)));
			}
			return true;
		}
		@Override
		public void activateRule(Player player, boolean message) {
			if (canActivate(player, message))
				player.getDuelInterfaces().activateRule(this.ruleIndex, null);
		}
		@Override
		public boolean activated(Player player) {
			return player.getDuelMainData().getRuleStates()[this.ruleIndex];
		}
	};

	public int ruleIndex;
	private int buttonId;
	private int additionalButton;

	public abstract boolean canActivate(Player player, boolean message);
	public abstract void activateRule(Player player, boolean message);
	public abstract boolean activated(Player player);

	RulesData(int ruleIndex, int buttonId, int additionalButton) {
		this.ruleIndex = ruleIndex;
		this.buttonId = buttonId;
		this.additionalButton = additionalButton;

	}

	public static RulesData forId(int buttonId) {
		for (RulesData rulesData : RulesData.values())
			if (rulesData.buttonId == buttonId || rulesData.additionalButton == buttonId)
				return rulesData;
		return null;
	}
}
