package com.rs2.model.players;

import java.util.ArrayList;

import com.rs2.Constants;
import com.rs2.model.content.BankPin;
import com.rs2.model.content.BarrowsItem;
import com.rs2.model.players.Player.BankOptions;
import com.rs2.model.players.item.Item;

public class BankManager {

	public static final int SIZE = 352;
	public static final int NON_DONOR_LIMIT = 128;

    public static void openBank(Player player) {
        openBank(player, player);
    }

	public static void openBank(Player player, Player whoToOpenFor) {
		if(player.equals(whoToOpenFor)) {
			if (player.getBankPin().hasBankPin()) {
				if (!player.getBankPin().isBankPinVerified()) {
					player.getBankPin().startPinInterface(BankPin.PinInterfaceStatus.VERIFYING);
					return;
				}
			} else if (!player.isBankWarning()) {
				player.getActionSender().sendMessage("You do not have a bank pin, it is highly recommended you get one.");
				player.setBankWarning(true);
			}
		}
		Item[] inventory = whoToOpenFor.getInventory().getItemContainer().toArray();
		player.getBank().shift();
		Item[] bank = player.getBank().toArray();
		whoToOpenFor.getActionSender().sendUpdateItems(5064, inventory);
		whoToOpenFor.getActionSender().sendInterface(5292, 5063);
		whoToOpenFor.getActionSender().sendUpdateItems(5382, bank);
		whoToOpenFor.getActionSender().sendString("The Bank of OSRSPK ("+player.getBank().size()+"/"+(player.isDonator() ? SIZE : NON_DONOR_LIMIT )+")", 5383);
		whoToOpenFor.getAttributes().put("isBanking", Boolean.TRUE);
		if(player.questStage[0] == 51){
			player.setNextTutorialStage();
		}
	}

	public static void openDepositBox(Player player) {
		Item[] inventory = player.getInventory().getItemContainer().toArray();
		player.getActionSender().sendUpdateItems(7423, inventory);
		player.getActionSender().sendInterface(4465, 197);
		player.getAttributes().put("isBanking", Boolean.TRUE);
	}
	
	public static boolean bankEquipment(Player player){
		ArrayList<Integer> itemsToBank = new ArrayList<Integer>();
		for (Item item : player.getEquipment().getItemContainer().getItems()) {
			if (item == null)
				continue;
			if(!itemsToBank.contains(item.getId()))
				itemsToBank.add(item.getId());
		}
		int arr[] = new int[itemsToBank.size()];
		for(int i = 0; i < arr.length; i++)
			arr[i] = itemsToBank.get(i);
		if(ableToBankItems(player, arr)){
			for (Item item : player.getEquipment().getItemContainer().getItems()) {
				if (item == null)
					continue;
				player.getEquipment().removeItem(item);
				bankItem(player, item);
			}
			player.getEquipment().updateAfterRemove();
			updateBank(player);
			return true;
		}
		return false;
	}
	
	public static boolean bankInventory(Player player){
		ArrayList<Integer> itemsToBank = new ArrayList<Integer>();
		for (Item item : player.getInventory().getItemContainer().getItems()) {
			if (item == null)
				continue;
			if(!itemsToBank.contains(item.getId()))
				itemsToBank.add(item.getId());
		}
		int arr[] = new int[itemsToBank.size()];
		for(int i = 0; i < arr.length; i++)
			arr[i] = itemsToBank.get(i);
		if(ableToBankItems(player, arr)){
			for (Item item : player.getInventory().getItemContainer().getItems()) {
				if (item == null)
					continue;
				player.getInventory().removeItem(item);
				bankItem(player, item);
			}
			updateBank(player);
			return true;
		}
		return false;
	}
	
	public static boolean bankCarriedItems(Player player){
		ArrayList<Integer> itemsToBank = new ArrayList<Integer>();
		for (Item item : player.getInventory().getItemContainer().getItems()) {
			if (item == null)
				continue;
			if(!itemsToBank.contains(item.getId()))
				itemsToBank.add(item.getId());
		}
		for (Item item : player.getEquipment().getItemContainer().getItems()) {
			if (item == null)
				continue;
			if(!itemsToBank.contains(item.getId()))
				itemsToBank.add(item.getId());
		}
		int arr[] = new int[itemsToBank.size()];
		for(int i = 0; i < arr.length; i++)
			arr[i] = itemsToBank.get(i);
		if(ableToBankItems(player, arr)){
			for (Item item : player.getInventory().getItemContainer().getItems()) {
				if (item == null)
					continue;
				player.getInventory().removeItem(item);
				bankItem(player, item);
			}
			for (Item item : player.getEquipment().getItemContainer().getItems()) {
				if (item == null)
					continue;
				player.getEquipment().removeItem(item);
				bankItem(player, item);
			}
			player.getEquipment().updateAfterRemove();
			updateBank(player);
			return true;
		}
		return false;
	}
	
	static boolean enoughSpace(final Player player, final int item, final int currentSize){
		int bankCount = player.getBank().getCount(item);
		if(BarrowsItem.isDegradedItem(item))
			bankCount = 0;
		if(bankCount > 0)
			return true;
		int maxSize = player.isDonator() ? SIZE : NON_DONOR_LIMIT;
		//int currentSize = player.getBank().size();
		if(currentSize >= maxSize){
			player.getActionSender().sendMessage("You don't have enough space in your bank account.");
			return false;
		}
		return true;
	}
	
	public static void bankItem(Player player, Item item) {
		if (item == null) {
			return;
		}
		int amount = item.getCount();
		boolean isNote = item.getDefinition().isNoted();
		int transferId = isNote ? item.getDefinition().getNormalId() : item.getDefinition().getId();
		//int freeSlot = player.getBank().freeSlot();
		int bankCount = player.getBank().getCount(transferId);
		int timer = item.getTimer();
		if (!enoughSpace(player, transferId, player.getBank().size())) {
			return;
		}
		if(BarrowsItem.isDegradedItem(transferId))
			bankCount = 0;
		if (bankCount == 0) {
			player.getBank().add(new Item(transferId, amount, timer));
		} else {
			player.getBank().set(player.getBank().getSlotById(transferId), new Item(transferId, bankCount + amount, timer));
		}
	}
	
	static boolean ableToBankItems(Player player, int[] itemIds){
		int itemsInBank = player.getBank().size();
		for(int item_ : itemIds){
			Item item = new Item(item_);
			if(item.getDefinition().isNoted())
				item_ = item.getDefinition().getNormalId();
			//int freeSlot = player.getBank().freeSlot();
			int bankCount = player.getBank().getCount(item_);
			if(BarrowsItem.isDegradedItem(item_))
				bankCount = 0;
			if (!enoughSpace(player, item_, itemsInBank)) {
				return false;
			}
			if(bankCount == 0)
				itemsInBank++;
		}
		return true;
	}
	
	static void updateBank(final Player player){
		Item[] bankItems = player.getBank().toArray();
		player.getInventory().refresh(5064);
		player.getInventory().refresh(7423);
		player.getActionSender().sendUpdateItems(5382, bankItems);
		player.getActionSender().sendString("The Bank of OSRSPK ("+player.getBank().size()+"/"+(player.isDonator() ? SIZE : NON_DONOR_LIMIT )+")", 5383);
	}

	public static void bankItem(Player player, int slot, int bankItem, int bankAmount) {
		Item inventoryItem = player.getInventory().getItemContainer().get(slot);
		if (inventoryItem == null || inventoryItem.getId() != bankItem || !inventoryItem.validItem()) {
			return;
		}
		int amount = player.getInventory().getItemContainer().getCount(bankItem);
		int timer = inventoryItem.getTimer();
		boolean isNote = inventoryItem.getDefinition().isNoted();
		if (inventoryItem.getDefinition().getId() > Constants.MAX_ITEM_ID) {
			player.getActionSender().sendMessage("This item is not supported yet.");
			return;
		}
		int transferId = isNote ? inventoryItem.getDefinition().getNormalId() : inventoryItem.getDefinition().getId();
		//int freeSlot = player.getBank().freeSlot();
		int bankCount = player.getBank().getCount(transferId);
		if(BarrowsItem.isDegradedItem(transferId)){
			bankCount = 0;
			amount = 1;
		}
		if (!enoughSpace(player, transferId, player.getBank().size())) {
			return;
		}
		if (amount > bankAmount) {
			amount = bankAmount;
		}
		if (!inventoryItem.getDefinition().isStackable()) {
			for (int i = 0; i < amount; i++) {
				if(BarrowsItem.isDegradedItem(transferId)){
					player.getInventory().removeItemSlot(new Item(bankItem, 1, timer), slot);
				}else
					player.getInventory().removeItem(new Item(bankItem, 1, timer));
			}
		} else {
			if(BarrowsItem.isDegradedItem(transferId)){
				player.getInventory().removeItemSlot(new Item(bankItem, amount, timer), slot);
			}else
				player.getInventory().removeItem(new Item(bankItem, amount, timer));
		}
		if (bankCount == 0) {
			player.getBank().add(new Item(transferId, amount, timer));
		} else {
			player.getBank().set(player.getBank().getSlotById(transferId), new Item(transferId, bankCount + amount, timer));
		}
		updateBank(player);
	}

	public static void withdrawItem(Player player, int slot, int bankItem, int bankAmount) {
		Item item = new Item(bankItem);
		boolean withdrawAsNote = player.isWithdrawAsNote();
		boolean isNoteable = item.getDefinition().isNoteable();
		int notedid = item.getDefinition().getNotedId();
		int inBankAmount = player.getBank().getCount(bankItem);
		if (bankAmount < 1 || bankItem < 0 || player.getBank().get(slot) == null || player.getBank().get(slot).getId() != item.getId() || !item.validItem()) {
			return;
		}
		int timer = player.getBank().get(slot).getTimer();
		if(timer != -1)
			bankAmount = 1;
		if (bankItem > Constants.MAX_ITEM_ID) {
			player.getActionSender().sendMessage("This item is not supported yet.");
			return;
		}
		if (inBankAmount < bankAmount) {
			bankAmount = inBankAmount;
		}
		if (withdrawAsNote && !isNoteable) {
			player.getActionSender().sendMessage("This item cannot be withdrawn as a note.");
			withdrawAsNote = false;
		}
		int count = 0;
		if (!withdrawAsNote || !isNoteable) {
			count = player.getInventory().addItemCount(new Item(bankItem, bankAmount, timer));
		} else if (withdrawAsNote) {
			count = player.getInventory().addItemCount(new Item(notedid, bankAmount, timer));
		}
		player.getBank().remove(new Item(bankItem, count, timer), slot);
		//player.getBank().shift();
		Item[] bankItems = player.getBank().toArray();
		player.getInventory().refresh(5064);
		player.getActionSender().sendUpdateItems(5382, bankItems);
		player.getActionSender().sendString("The Bank of OSRSPK ("+player.getBank().size()+"/"+(player.isDonator() ? SIZE : NON_DONOR_LIMIT )+")", 5383);
	}

	public static void handleBankOptions(Player player, int fromSlot, int toSlot) {
        if (toSlot >= player.getBank().getCapacity() || fromSlot >= player.getBank().getCapacity())
            return;
		if (player.getBankOptions().equals(BankOptions.SWAP_ITEM)) {
			player.getBank().swap(fromSlot, toSlot);
		} else if (player.getBankOptions().equals(BankOptions.INSERT_ITEM)) {
			player.getBank().insert(fromSlot, toSlot);
		}
		player.getBank().shift();
		Item[] bankItems = player.getBank().toArray();
		player.getActionSender().sendUpdateItems(5382, bankItems);
	}

}
