package com.rs2.model.players.item;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.skills.farming.FarmingConstants;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.players.BankManager;
import com.rs2.model.players.Player;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;

public class ItemManager {

	private static ItemManager instance = new ItemManager();

	public void lowerAllItemsTimers(Player p) {
		for (int i = 0; i < 28; i++) {
			Item item = p.getInventory().getItemContainer().get(i);
			if (item == null || item.getTimer() < 0 || item.getDefinition().getSlot() != -1)
				continue;
			item.setTimer(item.getTimer() - 1);
			if (item.getTimer() == 0) {
				for (int items : FarmingConstants.WATERED_SAPPLING)
					if (items == item.getId())
						p.getSeedling().makeSaplingInInv(item.getId());
				if (item.getId() == 1995)
					p.getWine().fermentWineInInv(i);

			}
		}
		for (int i = 0; i < BankManager.SIZE; i++) {
			Item item = p.getBank().get(i);
			if (item == null || item.getTimer() < 0 || item.getDefinition().getSlot() != -1)
				continue;
			item.setTimer(item.getTimer() - 1);
			if (item.getTimer() == 0) {
				for (int items : FarmingConstants.WATERED_SAPPLING)
					if (items == item.getId())
						p.getSeedling().makeSaplingInBank(item.getId());
				if (item.getId() == 1995)
					p.getWine().fermentWineInBank(i);
			}
		}
	}

	public void pickupItem(final Player player, final int itemId, final Position pos) {
		final int task = player.getTask();
        World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (!player.checkTask(task)) {
					this.stop();
					return;
				}
				boolean b = false;
				if((pos.getX() == 2822 && pos.getY() == 3355)){
					b = true;
				}
				if (!Misc.goodDistance(player.getPosition(), pos, 1)) {
					return;
				}
				if (!Misc.checkClip(player.getPosition(), pos, false) && !b) {
					return;
				}
				if (Misc.checkClip(player.getPosition(), pos, true) && !player.getPosition().equals(pos)) {
					return;
				}
				if (player.getQuestHandler().pickQuestItem(itemId)) {
					this.stop();
					return;
				}
				if(itemId >= 5509 && itemId <= 5515){//pouches
					if(player.hasItem(itemId)){
						player.getActionSender().sendMessage("I already have that pouch!");
						this.stop();
						return;
					}
				}
                if (player.getPrimaryDirection() >= 0 || player.getSecondaryDirection() >= 0)
                    return;
                stop();

                GroundItem item = GroundItemManager.getManager().findItem(player, itemId, pos);
                if (item == null)
                    return;

                /*boolean takeFromTable = !player.getPosition().equals(pos) && !Misc.checkClip(player.getPosition(), pos, true);
            	if(takeFromTable)
            		player.getUpdateFlags().sendAnimation(832);*/
                if(!GroundItemManager.getManager().destroyItem(item, player)){
                	System.out.println("Bugged ground item!? "+item.getItem()+" "+player.getUsername()+" "+player.getGroundItems().contains(item));
                	player.getActionSender().sendMessage("That item does not seem to exist anymore.");
                	player.getActionSender().removeGroundItem(item);
                	return;
                }
                player.getActionSender().sendSound(356, 1, 0);
				if (item.getItem().getDefinition().isStackable()) {
					if (player.getInventory().addItem(new Item(player.getClickId(), item.getItem().getCount()))) {
						player.getEquipment().updateWeight();
	                    //GroundItemManager.getManager().destroyItem(item);
					}
				} else if (player.getInventory().addItem(new Item(player.getClickId(), 1))) {
					player.getEquipment().updateWeight();
                   //GroundItemManager.getManager().destroyItem(item);
				}
			}
		});
	}

	public int getItemValue(int itemId, String type, int currency) {
		double value = 0;
		if (type.equals("donator")) {
			value = new Item(itemId).getDefinition().getDonatorShopPrice();
		} else
		if (type.equals("buyfromshop")) {
			if(currency == 995)
				value = new Item(itemId).getDefinition().getShopPrice();
			if(currency == 6529)
				value = new Item(itemId).getDefinition().getTokkulPrice();
			value = value < 1 ? 1 : value;
		} else if (type.startsWith("selltoshop")) {
			if(currency == 995){
				if(type.contains("Specialty"))
					value = new Item(itemId).getDefinition().getHighAlcValue();
				else
					value = new Item(itemId).getDefinition().getLowAlcValue();
			}
			if(currency == 6529)
				value = (new Item(itemId).getDefinition().getTokkulPrice())*15/100;
		} else if (type.equals("lowalch") && currency == 995) {
			value = new Item(itemId).getDefinition().getLowAlcValue();
		} else if (type.equals("highalch") && currency == 995) {
			value = new Item(itemId).getDefinition().getHighAlcValue();
		}
		return (int) value;
    }

    public String getItemName(int itemId) {
        return new Item(itemId).getDefinition().getName();
    }

    public int getItemId(String itemName) {
        for (int i = 0; i < Constants.MAX_ITEM_ID; i++) {
            Item item = new Item(i);
            if (item.getDefinition().getName().equalsIgnoreCase(itemName)) {
                return i;
            }
        }
        return -1;
    }

    public static ItemManager getInstance() {
        return instance;
	}

}
