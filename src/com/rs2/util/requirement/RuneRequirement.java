package com.rs2.util.requirement;

import java.util.ArrayList;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.content.minigames.magetrainingarena.AlchemistPlayground;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;

/**
 *
 */
public abstract class RuneRequirement extends InventoryRequirement {

	private Spell spell;
	private int runeId;
	private int runeAmount;
	private ArrayList<Item> reqRunes = new ArrayList<Item>();

	public RuneRequirement(int itemId, int itemAmount) {
		super(itemId, itemAmount);
		this.runeId = itemId;
		this.runeAmount = itemAmount;
	}
	
	public RuneRequirement(Spell spell) {
		super(1, 1);
		this.spell = spell;
	}
	
	void updateReq(){
		super.updateReq(reqRunes);
	}

	@Override
	public void execute(Entity entity) {
		if (!entity.isPlayer())
			return;
		super.execute(entity);
	}

	@Override
	boolean meetsRequirement(Entity entity) {
		if (!entity.isPlayer())
			return true;
		Player player = (Player) entity;
		Item[] ab = spell.getRunesRequired().clone();
		if((spell == Spell.LOW_ALCH || spell == Spell.HIGH_ALCH) && player.getAlchemistPlayground().isInAlchemistPlayGround() && AlchemistPlayground.freeCast == player.tempMiscInt){
			ArrayList<Item> runes2Remove = new ArrayList<Item>();
			runes2Remove.add(new Item(561, 0));
			this.reqRunes = runes2Remove;
		} else {
			this.reqRunes = checkReqs(player, ab);
		}
		this.updateReq();
		return super.meetsRequirement(entity);
	}
	
	ArrayList<Item> checkReqs(Player player, Item[] req){
		ArrayList<Item> runes2Remove = new ArrayList<Item>();
		ArrayList<Item> unusedRunes = new ArrayList<Item>();
		ArrayList<Integer> index2Remove = new ArrayList<Integer>();
		ArrayList<Integer> index2Add = new ArrayList<Integer>();
		for (Item rune : req) {
			int runeId = rune.getId();
			int runeAmount = rune.getCount();
			if(isUsingStaff(player, runeId))
				continue;
			Item normalRune = player.getInventory().getItemContainer().getById(runeId);
			int normalRuneCount = 0;
			if(normalRune != null)
				normalRuneCount = normalRune.getCount();
			if(normalRuneCount >= runeAmount){
				runes2Remove.add(new Item(runeId, runeAmount));
				continue;
			}
			if(hasCombinationRune(player, runeId, runeAmount)){
				if(runes2Remove.size() != 0){
					boolean matchFound = false;
					for (int i = 0; i < runes2Remove2.size(); i++) {
						Item removeRune2 = runes2Remove2.get(i);
						for (Item removeRune : runes2Remove) {
							if(removeRune.getId() == removeRune2.getId()){
								if(removeRune.getCount() < removeRune2.getCount()){
									removeRune.setCount(removeRune2.getCount());
									matchFound = true;
								}
							}
						}
						if(!matchFound){
							index2Add.add(i);
						}
					}
					for (int i : index2Add)
						runes2Remove.add(runes2Remove2.get(i));
					index2Add.clear();
				} else {
					runes2Remove.addAll(runes2Remove2);
				}
				if(unusedRunes.size() != 0){
					boolean matchFound = false;
					for (int i = 0; i < unusedRunes2.size(); i++) {
						Item unusedRune2 = unusedRunes2.get(i);
						for (Item unusedRune : unusedRunes) {
							if(unusedRune.getId() == unusedRune2.getId()){
								if(unusedRune.getCount() < unusedRune.getCount()){
									unusedRune.setCount(unusedRune.getCount());
									matchFound = true;
								}
							}
						}
						if(!matchFound){
							index2Add.add(i);
						}
					}
					for (int i : index2Add)
						unusedRunes.add(unusedRunes2.get(i));
					index2Add.clear();
				} else {
					unusedRunes.addAll(unusedRunes2);
				}
				continue;
			}
			runes2Remove.add(new Item(runeId, runeAmount));
		}
		for (int i = 0; i < runes2Remove.size(); i++) {
			Item removeRune = runes2Remove.get(i);
			for (Item unusedRune : unusedRunes) {
				if(removeRune.getId() == unusedRune.getId()){
					if(removeRune.getCount() <= unusedRune.getCount())
						index2Remove.add(i);	
					else{
						removeRune.setCount(removeRune.getCount()-unusedRune.getCount());
					}
				}
			}
		}
		for (int i : index2Remove)
			runes2Remove.remove(i);
		return runes2Remove;
	}

	private static boolean isUsingStaff(Entity entity, int runeId) {
		if (!entity.isPlayer())
			return true;
		Player player = (Player) entity;
		Item weapon = player.getEquipment().getItemContainer().get(Constants.WEAPON);
		if (weapon == null)
			return false;
		String weaponName = ItemDefinition.forId(weapon.getId()).getName().toLowerCase();
		if (!weaponName.contains("staff"))
			return false;
		switch (runeId) {
			case 554 :
				return weaponName.contains("fire") || weaponName.contains("lava");
			case 555 :
				return weaponName.contains("water") || weaponName.contains("mud");
			case 556 :
				return weaponName.contains("air");
			case 557 :
				return weaponName.contains("earth") || weaponName.contains("lava") || weaponName.contains("mud");
		}
		return false;
	}
	
	ArrayList<Item> runes2Remove2 = new ArrayList<Item>();
	ArrayList<Item> unusedRunes2 = new ArrayList<Item>();
	
	int getUnusedRune(int combined, int runeId){
		switch (runeId) {
			case 554 ://fire
				if(combined == 4697)
					return 556;
				if(combined == 4694)
					return 555;
				if(combined == 4699)
					return 557;
				break;
		
			case 555 ://water
				if(combined == 4695)
					return 556;
				if(combined == 4694)
					return 554;
				if(combined == 4698)
					return 557;
				break;
		
			case 556 ://air
				if(combined == 4695)
					return 555;
				if(combined == 4696)
					return 557;
				if(combined == 4697)
					return 554;
				break;
		
			case 557 ://earth
				if(combined == 4696)
					return 556;
				if(combined == 4698)
					return 555;
				if(combined == 4699)
					return 554;
				break;
		}
		return -1;
	}
	
	private boolean hasCombinationRune(Player player, int runeId, int runeAmount) {//TODO: clean up this method!
		runes2Remove2.clear();
		unusedRunes2.clear();
		ArrayList<Item> combinationRunes = new ArrayList<Item>();
		Item normalRune = player.getInventory().getItemContainer().getById(runeId);
		int normalRuneCount = 0;
		if(normalRune != null)
			normalRuneCount = normalRune.getCount();
		if(normalRuneCount >= runeAmount)
			return true;
		for(Item item : player.getInventory().getItemContainer().getItems()){
			if(item == null)
				continue;
			switch (runeId) {
				case 554 ://fire
					if(item.getId() == 4697 || item.getId() == 4694 || item.getId() == 4699){
						combinationRunes.add(item);
					}
				break;
				
				case 555 ://water
					if(item.getId() == 4695 || item.getId() == 4694 || item.getId() == 4698){
						combinationRunes.add(item);
					}
				break;
				
				case 556 ://air
					if(item.getId() == 4695 || item.getId() == 4696 || item.getId() == 4697){
						combinationRunes.add(item);
					}
				break;
				
				case 557 ://earth
					if(item.getId() == 4696 || item.getId() == 4698 || item.getId() == 4699){
						combinationRunes.add(item);
					}
				break;
			}
		}
		int combinationRuneCount = 0;
		for(Item item : combinationRunes){
			combinationRuneCount += item.getCount();
		}
		if(combinationRunes.size() == 0)
			return false;
		if(normalRuneCount+combinationRuneCount >= runeAmount){
			int amountLeft = runeAmount;
			if(normalRune != null)
				runes2Remove2.add(normalRune);
			amountLeft -= normalRuneCount;
			if(amountLeft >= combinationRunes.get(0).getCount()){
				runes2Remove2.add(combinationRunes.get(0));
				unusedRunes2.add(new Item(getUnusedRune(combinationRunes.get(0).getId(), runeId), combinationRunes.get(0).getCount()));
				amountLeft -= combinationRunes.get(0).getCount();
			}else{
				runes2Remove2.add(new Item(combinationRunes.get(0).getId(), amountLeft));
				unusedRunes2.add(new Item(getUnusedRune(combinationRunes.get(0).getId(), runeId), amountLeft));
			}
			if(normalRuneCount+combinationRunes.get(0).getCount() >= runeAmount){
				return true;
			}
			if(amountLeft >= combinationRunes.get(1).getCount()){
				runes2Remove2.add(combinationRunes.get(1));
				amountLeft -= combinationRunes.get(1).getCount();
				unusedRunes2.add(new Item(getUnusedRune(combinationRunes.get(1).getId(), runeId), combinationRunes.get(1).getCount()));
			}else{
				runes2Remove2.add(new Item(combinationRunes.get(1).getId(), amountLeft));
				unusedRunes2.add(new Item(getUnusedRune(combinationRunes.get(1).getId(), runeId), amountLeft));
			}
			if(normalRuneCount+combinationRunes.get(0).getCount()+combinationRunes.get(1).getCount() >= runeAmount){
				return true;
			}
			if(amountLeft >= combinationRunes.get(2).getCount()){
				runes2Remove2.add(combinationRunes.get(2));
				amountLeft -= combinationRunes.get(2).getCount();
				unusedRunes2.add(new Item(getUnusedRune(combinationRunes.get(2).getId(), runeId), combinationRunes.get(2).getCount()));
			}else{
				runes2Remove2.add(new Item(combinationRunes.get(2).getId(), amountLeft));
				unusedRunes2.add(new Item(getUnusedRune(combinationRunes.get(2).getId(), runeId), amountLeft));
			}
			if(normalRuneCount+combinationRunes.get(0).getCount()+combinationRunes.get(1).getCount()+combinationRunes.get(2).getCount() >= runeAmount){
				return true;
			}
		}
		return false;
	}

}
