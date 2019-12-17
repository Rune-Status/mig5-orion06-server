package com.rs2.model.players.container.equipment;

import java.text.DecimalFormat;

import com.rs2.Constants;
import com.rs2.model.content.Following;
import com.rs2.model.content.WalkInterfaces;
import com.rs2.model.content.combat.special.SpecialType;
import com.rs2.model.content.combat.weapon.Weapon;
import com.rs2.model.content.minigames.duelarena.RulesData;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestDefinition;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.runecrafting.Tiaras;
import com.rs2.model.players.Player;
import com.rs2.model.players.container.Container;
import com.rs2.model.players.container.Container.Type;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;
import com.rs2.model.players.item.ItemManager;
import com.rs2.util.Misc;

public class Equipment {

	public static final int EQUIPMENT_INTERFACE = 1688;
	public static final int SIZE = 14;
	public static final int ACCURATE = 0;
	public static final int AGGRESSIVE = 1;
	public static final int CONTROLLED = 2;
	public static final int DEFENSIVE = 3;
	public int walkAnim;

	public int attackLevelReq, strengthLevelReq, defenceLevelReq, rangeLevelReq, magicLevelReq, prayerLevelReq, slayerLevelReq;

	private Player player;

	private Container itemContainer = new Container(Type.STANDARD, 14) {
		@Override
		public void clear() {
			super.clear();
			player.getEquipment().updateWeight();
		}
	};

	public Equipment(Player player) {
		this.player = player;
	}

	public void sendEquipmentOnLogin() {
		refresh();
	}

	public void refresh() {
		Item[] items = itemContainer.toArray();
		player.getActionSender().sendUpdateItems(EQUIPMENT_INTERFACE, items);
		player.getEquipment().checkRangeGear();
		player.getEquipment().checkBarrowsGear();
		player.setEquippedWeapon(Weapon.getWeapon(items[Constants.WEAPON]));
		sendBonus(player);
		sendWeaponInterface();
	}

	public boolean removeItem(Item item) {
		if (item == null || !item.validItem()) {
			return false;
		}
		itemContainer.remove(item);
		if (item.getId() == 6583 || item.getId() == 7927) {
			player.transformNpc = -1;
			player.getActionSender().sendSideBarInterfaces();
			player.setAppearanceUpdateRequired(true);
		}
		return true;
	}
	
	public boolean removeItem2(Item item) {
		if (item == null || !item.validItem()) {
			return false;
		}
		if (!playerHasItem(item)) {
			return false;
		}
		itemContainer.remove(item);
		refresh();
		player.getEquipment().updateWeight();
		return true;
	}
	
	public boolean playerHasItem(Item item) {
        return playerHasItem(item.getId(), item.getCount());
    }

	public boolean playerHasItem(int id) {
		return getItemContainer().getSlotById(id) > -1;
	}

	public boolean playerHasItem(int id, int amount) {
		if (!playerHasItem(id))
			return false;
		return getItemContainer().getCount(id) >= amount;
	}
	
	public void updateAfterRemove(){
		refresh();
		Tiaras.handleTiara(player, -1);
		player.getTask();
		player.setEquippedWeapon(null);
		player.setSpecialType(null);
		player.setAutoSpell(null);
		updateWeight();
        WalkInterfaces.checkChickenOption(player);
		player.setAppearanceUpdateRequired(true);
	}
	
	public void refresh(int slot, Item item) {
		player.getActionSender().sendUpdateItem(slot, EQUIPMENT_INTERFACE, item);
		player.getEquipment().checkRangeGear();
		player.getEquipment().checkBarrowsGear();
		sendBonus(player);
		sendWeaponInterface();
	}

	public void replaceEquipment(int id, int slot) {
		Item currentEquip = new Item(player.getEquipment().getId(slot));
		Item newItem = new Item(id);
		itemContainer.remove(currentEquip, slot);
		itemContainer.set(slot, newItem);
		refresh();
		player.setSpecialAttackActive(false);
		sendWeaponInterface();
		player.setInstigatingAttack(false);
		Following.resetFollow(player);
		player.getInventory().refresh();
		player.setAppearanceUpdateRequired(true);
		player.getAttributes().put("usedGlory", Boolean.FALSE);
	}
	
	public void replaceEquipment2(int id, int slot) {
		Item newItem = new Item(id);
		itemContainer.set(slot, newItem);
		refresh();
		player.setSpecialAttackActive(false);
		sendWeaponInterface();
		player.setInstigatingAttack(false);
		Following.resetFollow(player);
		player.getInventory().refresh();
		player.setAppearanceUpdateRequired(true);
	}

	public void equip(int slot) {
		Item item = player.getInventory().getItemContainer().get(slot);
		if (item == null) {
			return;
		}
		int equipSlot = item.getDefinition().getSlot();
        if(!player.getInventory().playerHasItem(item))
            return;
		if (!checkRequirements(item.getId(), equipSlot)) {
			return;
		}
		//if (!player.canTeleport()) {
		//	return;
		//}
		if (player.questStage[0] < 42 && player.questStage[0] != 1) {
			player.getDialogue().sendStatement("You haven't learned how to wield items yet!");
			return;
		}
		if (player.questStage[0] == 44) {
			if ((item.getId() == 1171 && player.getEquipment().getItemContainer().contains(1277) || (item.getId() == 1277 && player.getEquipment().getItemContainer().contains(1171)))){
				player.setNextTutorialStage();
			}
			player.getQuestHandler().setTutorialStage();
		}
		if (item.getId() == 1205 && player.questStage[0] == 42){
			player.setNextTutorialStage();
		}
        if (player.inDuelArena()) {
            for (int funWeapon : Constants.FUN_WEAPONS) {
                if (!RulesData.FUN_WEAPON.activated(player) && item.getId() == funWeapon) {
                    player.getActionSender().sendMessage("Usage of 'Fun weapons' haven't been enabled during this fight!");
                    return;
                }
            }
        }
		boolean disabled = false;
		switch (equipSlot) {
			case Constants.HAT :
				disabled = RulesData.NO_HEAD.activated(player);
				break;
			case Constants.CAPE :
				disabled = RulesData.NO_CAPE.activated(player);
				break;
			case Constants.AMULET :
				disabled = RulesData.NO_AMULET.activated(player);
				break;
			case Constants.ARROWS :
				disabled = RulesData.NO_ARROW.activated(player);
				break;
			case Constants.WEAPON :
				disabled = RulesData.NO_WEAPON.activated(player) || (item.getDefinition().isTwoHanded() && RulesData.NO_SHIELD.activated(player));
				break;
			case Constants.CHEST :
				disabled = RulesData.NO_BODY.activated(player);
				break;
			case Constants.SHIELD :
				disabled = RulesData.NO_SHIELD.activated(player);
				break;
			case Constants.LEGS :
				disabled = RulesData.NO_LEGS.activated(player);
				break;
			case Constants.HANDS :
				disabled = RulesData.NO_GLOVES.activated(player);
				break;
			case Constants.FEET :
				disabled = RulesData.NO_BOOTS.activated(player);
				break;
			case Constants.RING :
				disabled = RulesData.NO_RINGS.activated(player);
				break;
		}
		if (disabled) {
			player.getActionSender().sendMessage("You cannot wear this during this fight!");
			return;
		}
		boolean removedItem = false;
		if (item.getDefinition().isStackable()) {
			int slotType = equipSlot;
			Item equipItem = itemContainer.get(slotType);
			player.getInventory().removeItemSlot(item, slot);
			removedItem = true;
			if (itemContainer.get(slotType) != null) {
				if (item.getId() == equipItem.getId()) {
					itemContainer.set(slotType, new Item(item.getId(), item.getCount() + equipItem.getCount(), item.getTimer()));
				} else {
					player.getInventory().addItemToSlot(equipItem, slot);
					itemContainer.set(slotType, item);
				}
			} else {
				itemContainer.set(slotType, item);
			}
		} else {
			int slotType = equipSlot;
			if (slotType == Constants.WEAPON) {
				if (item.getDefinition().isTwoHanded()) {
					if (itemContainer.get(Constants.WEAPON) != null && itemContainer.get(Constants.SHIELD) != null && player.getInventory().getItemContainer().freeSlot() == -1) {
						player.getActionSender().sendMessage("Not enough space in your inventory.");
						return;
					}
					player.getInventory().removeItemSlot(item, slot);
					removedItem = true;
					unequip(Constants.SHIELD);
					if (itemContainer.get(Constants.SHIELD) != null) {
						return;
					}
				}
			}
			if (slotType == Constants.SHIELD && itemContainer.get(Constants.WEAPON) != null) {
				if (itemContainer.get(Constants.WEAPON).getDefinition().isTwoHanded()) {
					player.getInventory().removeItemSlot(item, slot);
					removedItem = true;
					unequip(Constants.WEAPON);
					if (itemContainer.get(Constants.WEAPON) != null) {
						return;
					}
				}
			}
			if (itemContainer.get(slotType) != null) {
				Item equipItem = itemContainer.get(slotType);
				if (!removedItem) {
					player.getInventory().removeItemSlot(item, slot);
					player.getInventory().addItemToSlot(equipItem, slot);
				} else {
					player.getInventory().addItem(equipItem);
				}
			} else {
				player.getInventory().removeItemSlot(item, slot);
			}
			itemContainer.set(slotType, new Item(item.getId(), item.getCount(), item.getTimer()));
		}
		player.getTask();
		player.setSpecialAttackActive(false);
		Following.resetFollow(player);
		player.getInventory().refresh();
		if (equipSlot == Constants.WEAPON) {
			player.setEquippedWeapon(Weapon.getWeapon(item));
			player.setSpecialType(SpecialType.getSpecial(item));
			player.setAutoSpell(null);
		}
		refresh();
		updateWeight();
		player.getAttributes().put("usedGlory", Boolean.FALSE);
		if (item.getId() == 6583 || item.getId() == 7927) {
			player.transformNpc = item.getId() == 6583 ? 2626 : 3689 + Misc.random(5);
			player.setAppearanceUpdateRequired(true);
			player.getActionSender().hideAllSideBars();
			player.getActionSender().sendSidebarInterface(3, 6014);
		}
        WalkInterfaces.checkChickenOption(player);
		Tiaras.handleTiara(player, item.getId());
		player.getActionSender().sendSound(equipSounds[Misc.random_(equipSounds.length)], 1, 0);
		player.setAppearanceUpdateRequired(true);
	}
	
	int[] equipSounds = {1342,1343,1344,1346};

	public void unequip(int slot) {
		Item item = itemContainer.get(slot);
		if (item == null) {
			return;
		}
        if (player.getInventory().getItemContainer().freeSlot() == -1) {
            player.getActionSender().sendMessage("Not enough space in your inventory.");
            return;
        }
		if (item.getId() == 6583 || item.getId() == 7927) {
			player.transformNpc = -1;
			player.getActionSender().sendSideBarInterfaces();
			player.setAppearanceUpdateRequired(true);
		}
		if (slot == Constants.HAT) {
			Tiaras.handleTiara(player, -1);
		}
        if(!player.getEquipment().getItemContainer().contains(item.getId()))
            return;
		player.getTask();
		itemContainer.remove(item, slot);
		player.getInventory().addItem(new Item(item.getId(), item.getCount(), item.getTimer()));
		if (slot == Constants.WEAPON) {
			player.setEquippedWeapon(null);
           player.setSpecialType(null);
			player.setAutoSpell(null);
		}
		refresh(slot, new Item(-1, 0));
		updateWeight();
		player.getActionSender().sendSound(equipSounds[Misc.random_(equipSounds.length)], 1, 0);
        WalkInterfaces.checkChickenOption(player);
		player.setAppearanceUpdateRequired(true);
	}

	public void updateWeight() {
		double totalWeight = 0;
		for (int i = 0; i < 11; i++)
			if (player.getEquipment().getItemContainer().get(i) != null)
				totalWeight += player.getEquipment().getItemContainer().get(i).getDefinition().getWeight();
				//totalWeight += ItemDefinition.getWeight(player.getEquipment().getItemContainer().get(i).getId());

		for (int i = 0; i < 28; i++)
			if (player.getInventory().getItemContainer().get(i) != null) {
				totalWeight += player.getInventory().getItemContainer().get(i).getDefinition().getWeight();
				//totalWeight += ItemDefinition.getWeight(player.getInventory().getItemContainer().get(i).getId());
				if (player.getInventory().getItemContainer().get(i).getId() == 88)// boots
																					// of
																					// lightness
					totalWeight += 4.8;
			}

		player.totalWeight = totalWeight;
		player.getActionSender().sendWeight();
		// player.getActionSender().sendString((int)
		// Math.floor(player.totalWeight) + "kg", 184);

	}

	public void removeAmount(int slot, int amount) {
		Item item = itemContainer.get(slot);
		if (item == null) {
			return;
		}
		int total = itemContainer.remove(item, slot) - amount;
		if (total > 0) {
			Item newItem = new Item(item.getId(), total, item.getTimer());
			itemContainer.add(newItem, slot);
			//refresh(slot, newItem);
		} else {
			itemContainer.add(new Item(-1, 0), slot);
			//itemContainer.remove(item);
			//refresh(slot, new Item(-1, 0));
		}
		refresh();
		player.setAppearanceUpdateRequired(true);
		sendBonus(player);
	}

	public void setBonus(Player player) {
		for (int i = 0; i < 14; i++) {
			if (player.getEquipment().getItemContainer().get(i) == null || i == 13) {
				continue;
			}
			Item item = player.getEquipment().getItemContainer().get(i);
			for (int bonus = 0; bonus < 12; bonus++) {
				player.setBonuses(bonus, (int) (item.getDefinition().getBonuses()[bonus] + player.getBonuses().get(bonus)));

			}
		}
	}

	public void sendBonus(Player player) {
		int offset = 0;
		String send = "";
		for (int i = 0; i < 12; i++) {
			player.setBonuses(i, 0);
		}
		setBonus(player);
		for (int i = 0; i < 12; i++) {
			if (player.getBonuses().get(i) >= 0) {
				send = Constants.BONUS_NAME[i] + ": +" + player.getBonuses().get(i);
			} else {
				send = Constants.BONUS_NAME[i] + ": -" + Math.abs(player.getBonuses().get(i));
			}
			if (i == 10) {
				offset = 1;
			}
			player.getActionSender().sendString(send, (1675 + i + offset));
		}
	}

	/**
	 * Wear Item
	 **/

	public void sendWeaponInterface() {
		Weapon weapon = Weapon.getWeapon(getItemContainer().get(Constants.WEAPON));
		if(player.questStage[0] >= 45 || player.questStage[0] == 1)
		player.getActionSender().sendSidebarInterface(0, weapon.getWeaponInterface().getInterfaceId());
        player.getActionSender().sendString(getItemContainer().get(Constants.WEAPON) == null ? "Unarmed" : getItemContainer().get(Constants.WEAPON).getDefinition().getName(), weapon.getWeaponInterface().weaponNameChild());
        adjustFightMode(weapon);
        player.getActionSender().sendConfig(43, player.getFightMode());
    	if(weapon.getWeaponInterface().weaponDisplayChild() != -1)
        	player.getActionSender().sendItemOnInterface(weapon.getWeaponInterface().weaponDisplayChild(), 200, getItemContainer().get(Constants.WEAPON).getId());
        if(weapon.getWeaponInterface().getSpecialBarId() != -1)
	        if(SpecialType.getSpecial(getItemContainer().get(Constants.WEAPON)) == null)
	            player.getActionSender().sendFrame171(1, weapon.getWeaponInterface().getSpecialBarId());
	        else {
	            //System.out.println(weapon.getWeaponInterface().getSpecialBarId());
	            player.getActionSender().sendFrame171(0, weapon.getWeaponInterface().getSpecialBarId());
	            player.updateSpecialBar();
	        }

    }

	public void adjustFightMode(Weapon weapon) {
		if (weapon.getAttackAnimations().length < 4 && player.getFightMode() == 3)  {
			player.setFightMode(2);
		}
	}

    public boolean setFightMode(int buttonId){
        Weapon weapon = Weapon.getWeapon(getItemContainer().get(Constants.WEAPON));
        for(int i = 0; i < weapon.getWeaponInterface().getAttackStyles().length; i++){
            if(buttonId == weapon.getWeaponInterface().getAttackStyles()[i].getButtonId()) {
            	player.disableAutoCast();
                player.setFightMode(i);
                player.resetAllActions();
                //player.setFollowDistance(DistanceCheck.getDistanceForCombatType(player));
                //player.resetActions();
                return true;
            }
        }
        return false;
    }


	public int getId(int slot) {
		Item item = getItemContainer().get(slot);
		if (item != null) {
			return item.getId();
		}
		return 0;
	}

	public int getStandAnim() {
		return player.getEquippedWeapon().getMovementAnimations()[0];
	}

	public int getWalkAnim() {
		return player.getEquippedWeapon().getMovementAnimations()[1];
	}

	public int getRunAnim() {
		return player.getEquippedWeapon().getMovementAnimations()[2];
	}

	public Container getItemContainer() {
		return itemContainer;
	}

	public int getRangeLevelReq() {
		return rangeLevelReq;
	}

	public int getSlayerLevelReq() {
		return slayerLevelReq;
	}

	public class AttackStyles {

		private int accurate = 422;
		private int aggressive = 423;
		private int controlled = 422;
		private int defensive = 422;

		public int get(int style) {
			switch (style) {
				case 0 :
					return accurate;
				case 1 :
					return aggressive;
				case 2 :
					return controlled;
				case 3 :
					return defensive;
			}
			return accurate;
		}
	}

	public boolean checkRequirements(int itemId, int targetSlot) {
		if (targetSlot == Constants.WEAPON) {
			for (int i2 = 0; i2 < Skill.SKILL_COUNT; i2++) {
				int plrLvl = player.getSkill().getPlayerLevel(i2);
				int lvlReq = new Item(itemId).getDefinition().getSkillReq(i2);
				if (plrLvl < lvlReq) {
					String s = "a";
					char c = Skill.SKILL_NAME[i2].charAt(0);
					if(c == 65 || c == 69 || c == 73 || c == 79 || c == 85) {
						s = "an";
					}
					player.getActionSender().sendMessage("You need "+s+" "+Skill.SKILL_NAME[i2]+" level of " + lvlReq + " to wield this weapon.");
					return false;
				}
			}
			int plrQP = player.getQuestPoints();
			int QPReq = new Item(itemId).getDefinition().getQpReq();
			if (plrQP < QPReq) {
				player.getActionSender().sendMessage("You need "+QPReq+" quest points to wield this weapon.");
				return false;
			}
			for (int i2 = 0; i2 < QuestDefinition.QUEST_COUNT; i2++) {
				boolean questReq = new Item(itemId).getDefinition().getQuestReq(i2);
				boolean questDone = (player.questStage[i2] == 1 ? true : false);
				if (questReq && !questDone) {
					QuestDefinition questDef = QuestDefinition.forId(i2);
					String questName = questDef.getName();
					player.getActionSender().sendMessage("You need to complete "+questName+" to wield this weapon.");
					return false;
				}
			}
		} else if (targetSlot == Constants.FEET || targetSlot == Constants.LEGS || targetSlot == Constants.SHIELD || targetSlot == Constants.CHEST || targetSlot == Constants.HAT || targetSlot == Constants.HANDS) {
			for (int i2 = 0; i2 < Skill.SKILL_COUNT; i2++) {
				int plrLvl = player.getSkill().getPlayerLevel(i2);
				int lvlReq = new Item(itemId).getDefinition().getSkillReq(i2);
				if (plrLvl < lvlReq) {
					String s = "a";
					char c = Skill.SKILL_NAME[i2].charAt(0);
					if(c == 65 || c == 69 || c == 73 || c == 79 || c == 85) {
						s = "an";
					}
					player.getActionSender().sendMessage("You need "+s+" "+Skill.SKILL_NAME[i2]+" level of " + lvlReq + " to wear this item.");
					return false;
				}
			}
			int plrQP = player.getQuestPoints();
			int QPReq = new Item(itemId).getDefinition().getQpReq();
			if (plrQP < QPReq) {
				player.getActionSender().sendMessage("You need "+QPReq+" quest points to wear this item.");
				return false;
			}
			for (int i2 = 0; i2 < QuestDefinition.QUEST_COUNT; i2++) {
				boolean questReq = new Item(itemId).getDefinition().getQuestReq(i2);
				boolean questDone = (player.questStage[i2] == 1 ? true : false);
				if (questReq && !questDone) {
					QuestDefinition questDef = QuestDefinition.forId(i2);
					String questName = questDef.getName();
					player.getActionSender().sendMessage("You need to complete "+questName+" to wear this item.");
					return false;
				}
			}
		} else if (targetSlot == Constants.CAPE) {
			for (int i2 = 0; i2 < Skill.SKILL_COUNT; i2++) {
				int plrLvl = player.getSkill().getPlayerLevel(i2);
				int lvlReq = new Item(itemId).getDefinition().getSkillReq(i2);
				if (plrLvl < lvlReq) {
					String s = "a";
					char c = Skill.SKILL_NAME[i2].charAt(0);
					if(c == 65 || c == 69 || c == 73 || c == 79 || c == 85) {
						s = "an";
					}
					player.getActionSender().sendMessage("You need "+s+" "+Skill.SKILL_NAME[i2]+" level of " + lvlReq + " to wear this item.");
					return false;
				}
			}
			int plrQP = player.getQuestPoints();
			int QPReq = new Item(itemId).getDefinition().getQpReq();
			if (plrQP < QPReq) {
				player.getActionSender().sendMessage("You need "+QPReq+" quest points to wear this item.");
				return false;
			}
			for (int i2 = 0; i2 < QuestDefinition.QUEST_COUNT; i2++) {
				boolean questReq = new Item(itemId).getDefinition().getQuestReq(i2);
				boolean questDone = (player.questStage[i2] == 1 ? true : false);
				if (questReq && !questDone) {
					QuestDefinition questDef = QuestDefinition.forId(i2);
					String questName = questDef.getName();
					player.getActionSender().sendMessage("You need to complete "+questName+" to wear this item.");
					return false;
				}
			}
		} else if (targetSlot == Constants.ARROWS) {
			for (int i2 = 0; i2 < Skill.SKILL_COUNT; i2++) {
				int plrLvl = player.getSkill().getPlayerLevel(i2);
				int lvlReq = new Item(itemId).getDefinition().getSkillReq(i2);
				if (plrLvl < lvlReq) {
					String s = "a";
					char c = Skill.SKILL_NAME[i2].charAt(0);
					if(c == 65 || c == 69 || c == 73 || c == 79 || c == 85) {
						s = "an";
					}
					player.getActionSender().sendMessage("You need "+s+" "+Skill.SKILL_NAME[i2]+" level of " + lvlReq + " to equip this ammo.");
					return false;
				}
			}
			int plrQP = player.getQuestPoints();
			int QPReq = new Item(itemId).getDefinition().getQpReq();
			if (plrQP < QPReq) {
				player.getActionSender().sendMessage("You need "+QPReq+" quest points to equip this ammo.");
				return false;
			}
			for (int i2 = 0; i2 < QuestDefinition.QUEST_COUNT; i2++) {
				boolean questReq = new Item(itemId).getDefinition().getQuestReq(i2);
				boolean questDone = (player.questStage[i2] == 1 ? true : false);
				if (questReq && !questDone) {
					QuestDefinition questDef = QuestDefinition.forId(i2);
					String questName = questDef.getName();
					player.getActionSender().sendMessage("You need to complete "+questName+" to equip this ammo.");
					return false;
				}
			}
		}
		return true;
	}

	/* some math method */
	public String cutDouble(double Number) {
		double dNumber = Number;
		String masque = new String("#0.##");
		DecimalFormat form = new DecimalFormat(masque);
		return form.format(dNumber).replaceAll(",", ".");
	}

	public void checkBarrowsGear() {
		player.setFullAhrim(fullAhrim());
		player.setFullKaril(fullKaril());
		player.setFullDharok(fullDharok());
		player.setFullVerac(fullVerac());
		player.setFullTorag(fullTorag());
		player.setFullGuthan(fullGuthan());
	}

	public void checkRangeGear() {
		String wep = player.getEquippedWeapon().name().toLowerCase();
		player.setUsingBow((wep.contains("seercull") || wep.contains("bow")) && !wep.contains("crossbow"));
		player.setUsingCross(wep.contains("crossbow") || wep.contains("x-bow"));
		player.setUsingOtherRangedWeapon(wep.contains("knife") || wep.contains("dart") || wep.contains("javelin") || wep.contains("thrownaxe") || wep.contains("toktz-xil-ul") || wep.contains("throwing axe"));
		player.setUsingCrystalBow(wep.contains("crystal bow"));
		String ammo = ItemManager.getInstance().getItemName(player.getEquipment().getId(Constants.ARROWS)).toLowerCase();
		player.setUsingArrows(ammo.contains("arrow"));
		player.setUsingBolts(ammo.contains("bolt"));
		player.setDropArrow(!wep.contains("crystal") && !wep.contains("karils"));
	}

	public boolean fullAhrim() {
		if (player.getEquipment().getItemContainer().get(Constants.HAT) == null || player.getEquipment().getItemContainer().get(Constants.LEGS) == null || player.getEquipment().getItemContainer().get(Constants.CHEST) == null || player.getEquipment().getItemContainer().get(Constants.WEAPON) == null)
			return false;

		return (player.getEquipment().getItemContainer().get(Constants.HAT).getDefinition().getName().toLowerCase().contains("ahrims hood") && player.getEquipment().getItemContainer().get(Constants.CHEST).getDefinition().getName().toLowerCase().contains("ahrims robetop") && player.getEquipment().getItemContainer().get(Constants.LEGS).getDefinition().getName().toLowerCase().contains("ahrims robeskirt") && player.getEquipment().getItemContainer().get(Constants.WEAPON).getDefinition().getName().toLowerCase().contains("ahrims staff"));

	}

	public boolean fullKaril() {
		if (player.getEquipment().getItemContainer().get(Constants.HAT) == null || player.getEquipment().getItemContainer().get(Constants.LEGS) == null || player.getEquipment().getItemContainer().get(Constants.CHEST) == null || player.getEquipment().getItemContainer().get(Constants.WEAPON) == null)
			return false;

		return (player.getEquipment().getItemContainer().get(Constants.HAT).getDefinition().getName().toLowerCase().contains("karils coif") && player.getEquipment().getItemContainer().get(Constants.CHEST).getDefinition().getName().toLowerCase().contains("karils leathertop") && player.getEquipment().getItemContainer().get(Constants.LEGS).getDefinition().getName().toLowerCase().contains("karils leatherskirt") && player.getEquipment().getItemContainer().get(Constants.WEAPON).getDefinition().getName().toLowerCase().contains("karils crossbow"));
	}

	public boolean fullVerac() {
		if (player.getEquipment().getItemContainer().get(Constants.HAT) == null || player.getEquipment().getItemContainer().get(Constants.LEGS) == null || player.getEquipment().getItemContainer().get(Constants.CHEST) == null || player.getEquipment().getItemContainer().get(Constants.WEAPON) == null)
			return false;

		return (player.getEquipment().getItemContainer().get(Constants.HAT).getDefinition().getName().toLowerCase().contains("veracs helm") && player.getEquipment().getItemContainer().get(Constants.CHEST).getDefinition().getName().toLowerCase().contains("veracs brassard") && player.getEquipment().getItemContainer().get(Constants.LEGS).getDefinition().getName().toLowerCase().contains("veracs plateskirt") && player.getEquipment().getItemContainer().get(Constants.WEAPON).getDefinition().getName().toLowerCase().contains("veracs flail"));
	}

	public boolean fullGuthan() {
		if (player.getEquipment().getItemContainer().get(Constants.HAT) == null || player.getEquipment().getItemContainer().get(Constants.LEGS) == null || player.getEquipment().getItemContainer().get(Constants.CHEST) == null || player.getEquipment().getItemContainer().get(Constants.WEAPON) == null)
			return false;

		return (player.getEquipment().getItemContainer().get(Constants.HAT).getDefinition().getName().toLowerCase().contains("guthans helm") && player.getEquipment().getItemContainer().get(Constants.CHEST).getDefinition().getName().toLowerCase().contains("guthans platebody") && player.getEquipment().getItemContainer().get(Constants.LEGS).getDefinition().getName().toLowerCase().contains("guthans chainskirt") && player.getEquipment().getItemContainer().get(Constants.WEAPON).getDefinition().getName().toLowerCase().contains("guthans warspear"));
	}

	public boolean fullDharok() {
		if (player.getEquipment().getItemContainer().get(Constants.HAT) == null || player.getEquipment().getItemContainer().get(Constants.LEGS) == null || player.getEquipment().getItemContainer().get(Constants.CHEST) == null || player.getEquipment().getItemContainer().get(Constants.WEAPON) == null)
			return false;

		return (player.getEquipment().getItemContainer().get(Constants.HAT).getDefinition().getName().toLowerCase().contains("dharoks helm") && player.getEquipment().getItemContainer().get(Constants.CHEST).getDefinition().getName().toLowerCase().contains("dharoks platebody") && player.getEquipment().getItemContainer().get(Constants.LEGS).getDefinition().getName().toLowerCase().contains("dharoks platelegs") && player.getEquipment().getItemContainer().get(Constants.WEAPON).getDefinition().getName().toLowerCase().contains("dharoks greataxe"));
	}

	public boolean fullTorag() {
		if (player.getEquipment().getItemContainer().get(Constants.HAT) == null || player.getEquipment().getItemContainer().get(Constants.LEGS) == null || player.getEquipment().getItemContainer().get(Constants.CHEST) == null || player.getEquipment().getItemContainer().get(Constants.WEAPON) == null)
			return false;

		return (player.getEquipment().getItemContainer().get(Constants.HAT).getDefinition().getName().toLowerCase().contains("torags helm") && player.getEquipment().getItemContainer().get(Constants.CHEST).getDefinition().getName().toLowerCase().contains("torags platebody") && player.getEquipment().getItemContainer().get(Constants.LEGS).getDefinition().getName().toLowerCase().contains("torags platelegs") && player.getEquipment().getItemContainer().get(Constants.WEAPON).getDefinition().getName().toLowerCase().contains("torags hammers"));
	}

}
