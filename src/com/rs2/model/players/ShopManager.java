package com.rs2.model.players;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.players.container.Container;
import com.rs2.model.players.container.Container.Type;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.Tick;
import com.rs2.util.FileOperations;
import com.rs2.util.Misc;
import com.rs2.util.Packer;
import com.rs2.util.Stream;

public class ShopManager {

	public static final int SIZE = 40;

	private static List<Shop> shops = new ArrayList<Shop>(SIZE);

	public static void refresh(Player player, Shop shop) {
		Item[] shopItems = shop.getCurrentStock().toArray();
		player.getActionSender().sendUpdateItems(3900, shopItems);
	}

	public static void refreshAll(int shopId) {
		Shop shop = shops.get(shopId);
		for (Player players : World.getPlayers()) {
			if (players != null && players.getShopId() == shopId) {
				refresh(players, shop);
			}
		}
	}

	public static void openShop(Player player, int shopId) {
		if (shopId >= shops.toArray().length) {
			return;
		}
		Shop shop = shops.get(shopId);
		if(shop.getCurrencyType() == Shop.CurrencyTypes.DONATOR_POINTS)
			player.getActionSender().sendMessage("You have "+player.getDonatorPoints()+" Donator points.");
		Item[] shopItems = shop.getCurrentStock().toArray();
		//player.getActionSender().sendInterface(3824, 3822);
		player.getInventory().refresh(3823);
		player.getActionSender().sendUpdateItems(3900, shopItems);
		player.getActionSender().sendString(shop.getName(), 3901);
		player.getActionSender().sendInterface(3824, 3822); //remove this if broke
		player.setShopId(shopId);
		player.getAttributes().put("isShopping", Boolean.TRUE);
	}

	static boolean itemIsSkillcape(int id){
		if((id >= 7992 && id <= 7997) || (id >= 8001 && id <= 8012) || (id >= 8016 && id <= 8065))
			return true;
		return false;
	}
	
	static boolean isLinkedDonorItem(int id){
		if(id == 7958 || id == 7972 || id == 7974 || id == 7976 || id == 7982 || id == 7984 || id == 7986 || id == 8068 || id == 8076 || id == 8078)
			return true;
		return false;
	}
	
	public static void buyItem(Player player, int slot, int shopItem, int amount) {
		Shop shop = shops.get(player.getShopId());
		Container inventory = player.getInventory().getItemContainer();
		Item item = shop.getCurrentStock().get(slot);
		int currency, value;
		if (shop.getCurrencyType() == Shop.CurrencyTypes.ITEM) {
			currency = shop.getCurrency();
		} else {
			currency = getCurrencyForShopType(player, shop);
		}
		if (amount < 1 || shopItem < 0 || item == null || !item.validItem()) {
			return;
		}
		if (shopItem != item.getId()) {
			return;
		}
		boolean linked = false;
		if(isLinkedDonorItem(item.getId())){
			linked = true;
		}
		if (shop.getCurrencyType() == Shop.CurrencyTypes.ITEM) {
			value = ItemManager.getInstance().getItemValue(shopItem, "buyfromshop", currency);
			
		} else {
			value = ItemManager.getInstance().getItemValue(shopItem, "donator", currency);
			if (currency < value * amount) {
				player.getActionSender().sendMessage("You do not have enough donator points to buy this item.");
				return;
			}
			if(linked){
				ItemDefinition def = ItemDefinition.forId(item.getId());
				int amt = player.getInventory().getItemAmount(def.linkedTo);
				if (amt < amount) {
					player.getActionSender().sendMessage("You do not have enough "+ItemManager.getInstance().getItemName(def.linkedTo)+"(s) to buy this item.");
					return;
				}
			}
		}
		if (shop.getCurrentStock().get(slot).getCount() < amount) {
			amount = shop.getCurrentStock().get(slot).getCount();
		}
		int freeSpace = inventory.freeSlots();
		if (player.getInventory().playerHasExactItem(currency, value)) {
			freeSpace++;
		}
		int tVal = (!item.getDefinition().isStackable() ? amount : 1);
		if(itemIsSkillcape(item.getId()))
			tVal = 2*amount;
		if ((!item.getDefinition().isStackable() || !player.getInventory().playerHasItem(shopItem)) && freeSpace < tVal) {
			amount = freeSpace;
			player.getActionSender().sendMessage("Not enough space in your inventory.");
			if(itemIsSkillcape(item.getId()))
				return;//not sure if should return on other shops too....
		}
		if (shop.getCurrentStock().get(slot).getCount() < amount) {
			amount = shop.getCurrentStock().get(slot).getCount();
		}
		if (shop.isGeneralStore()) {
			if (shop.getCurrentStock().get(slot).getCount() == 0) {
				player.getActionSender().sendMessage("This item is out of stock.");
				return;
			}
		}

		int defaultStock = 0;
		if(!shop.isGeneralStore())
			defaultStock = shop.getStock().getById(shopItem).getCount();
		int shopAmount = shop.getCurrentStock().getCount(shopItem);
		int shopAmount2 = shopAmount-defaultStock;
		int lastItemValue;
		int realAmount = 0;
		
		for (int i = 0; i < amount; i++) {
			if (shop.getCurrencyType() == Shop.CurrencyTypes.ITEM) {
				int currencyValue = inventory.getCount(currency);
				int t = shopAmount2-i;
				lastItemValue = getDecreasedItemValue2(shopItem, currency, defaultStock, t, shop.isGeneralStore());
				if(lastItemValue > currencyValue){
					player.getActionSender().sendMessage("You do not have enough " + ItemManager.getInstance().getItemName(currency) + " to buy this item.");
					amount = realAmount;
					break;
				}
				player.getInventory().removeItem(new Item(currency, lastItemValue));
				realAmount = i+1;
				if(currency == 995)
					player.boughtItemsWorth += value;
			} else {
				if(linked){
					ItemDefinition def = ItemDefinition.forId(item.getId());
					player.getInventory().removeItem(new Item(def.linkedTo, 1));
				}
				decreaseCurrencyForSpecialShop(player, shop, value);
				if(shop.getCurrencyType() == Shop.CurrencyTypes.DONATOR_POINTS)
					player.getActionSender().sendMessage("You have "+player.getDonatorPoints()+" Donator points remaining.");
			}
		}
		if (shop.getStock().contains(item.getId())) {
			shop.getCurrentStock().removeOrZero(new Item(item.getId(), amount));
		} else {
			shop.getCurrentStock().remove(new Item(item.getId(), amount));
		}
		if(!itemIsSkillcape(shopItem)){
			Item itm = new Item(shopItem, amount);
			player.getInventory().addItem(itm);
			if(shop.getCurrencyType() == Shop.CurrencyTypes.DONATOR_POINTS)
				Misc.addWatchedItem(itm);
		}else{
			player.getInventory().addItem(new Item((player.getMaxSkillAmount() >= 2 && shopItem != 8058) ? shopItem+1 : shopItem, amount));
			player.getInventory().addItem(new Item(shopItem != 8058 ? shopItem+2 : shopItem+1, amount));//hood
		}
		player.getInventory().refresh(3823);
		refreshAll(player.getShopId());
	}
	
	static int getDecreasedItemValue(int itemId, int currency, int stock, int shopAmount, boolean isGeneral){//player selling item to shop
		String s = (!isGeneral ? "Specialty" : "General");
		int value = ItemManager.getInstance().getItemValue(itemId, "selltoshop_"+s, currency);
		int valueMin = ItemManager.getInstance().getItemValue(itemId, "selltoshop_General", currency);
		double stockMultiplier = 1.0;
		if(stock > 0){
			stockMultiplier = 1.894/Math.sqrt(stock);
		}
		double minimumAmount = -(stockMultiplier*0.033*2.25) * valueMin * 10 + valueMin;
		while(minimumAmount < 0){
			minimumAmount += valueMin;
		}
		double minimumValue = Math.floor(minimumAmount);
		double rate = 0.033;
		if(isGeneral)
			rate *= 2.25;
		double Z = -rate * stockMultiplier * value * shopAmount + value;
		double Z1 = Math.floor(Z);
		int realValue = (int) (Z1 < minimumValue ? minimumValue : Z1);
		return realValue;
	}
	
	static int getDecreasedItemValue2(int itemId, int currency, int stock, int shopAmount, boolean isGeneral){//shop selling item
		int value = ItemManager.getInstance().getItemValue(itemId, "buyfromshop", currency);
		int valueMin = ItemManager.getInstance().getItemValue(itemId, "selltoshop_General", currency);
		double stockMultiplier = 1.0;
		if(stock > 0){
			stockMultiplier = 1.65/Math.sqrt(stock);
		}
		double minimumAmount = -(stockMultiplier*0.033*2.25) * valueMin * 10 + valueMin;
		while(minimumAmount < 0){
			minimumAmount += valueMin;
		}
		double minimumValue = Math.floor(minimumAmount+1);
		if(isGeneral)
			value *= 1.3;
		double Z = -0.02 * stockMultiplier * value * shopAmount + value;
		double Z1 = Math.floor(Z);
		int realValue = (int) (Z1 < minimumValue ? minimumValue : Z1);
		return realValue;
	}

	public static void sellItem(Player player, int slot, int itemId, int amount) {
		Shop shop = shops.get(player.getShopId());
		Container inventory = player.getInventory().getItemContainer();
		Item item = inventory.get(slot);
		int currency = shop.getCurrency();
		if (item == null)
			return;
		int realItem = itemId;
		if(item.getDefinition().isNoted())
			realItem = item.getDefinition().getNormalId();
		int shopAmount = shop.getCurrentStock().getCount(realItem);
		if (!Constants.ADMINS_CAN_INTERACT && player.getStaffRights() >= 2) {
			player.getActionSender().sendMessage("This action is not allowed.");
			return;
		}
		if (item.getId() != itemId || !item.validItem()) {
			return;
		}
		if (shop.getCurrencyType() != Shop.CurrencyTypes.ITEM) {
			player.getActionSender().sendMessage("This shop can't buy anything.");
			return;
		}
		if (shop.getCurrentStock().freeSlots() < 1 && shopAmount <= 0) {
			player.getActionSender().sendMessage("The shop is currently full!");
			return;
		}
		if (itemId == 995) {
			player.getActionSender().sendMessage("You cannot sell coins to the shop.");
			return;
		}
		if ((!shop.isGeneralStore() && !shop.getCurrentStock().contains(realItem)) || item.getDefinition().isUntradable()) {
			player.getActionSender().sendMessage("You cannot sell this item in this shop.");
			return;
		}
		int totalItems = inventory.getCount(itemId);
		if (amount < 1 || itemId < 0) {
			return;
		}
		if (itemId > Constants.MAX_ITEM_ID) {
			player.getActionSender().sendMessage("This item is not supported yet.");
			return;
		}
		if (!inventory.contains(itemId)) {
			return;
		}
		if (amount >= totalItems) {
			amount = totalItems;
		}
		player.getInventory().removeItem(new Item(itemId, amount));
		String s = (!shop.isGeneralStore() ? "Specialty" : "General");
		if (ItemManager.getInstance().getItemValue(itemId, "selltoshop_"+s, currency) > 0) {
			int value = ItemManager.getInstance().getItemValue(itemId, "selltoshop_"+s, currency);
			double coins = 0.0;
			int defaultStock = 0;
			if(!shop.isGeneralStore())
				defaultStock = shop.getStock().getById(realItem).getCount();
			int shopAmount2 = shopAmount-defaultStock;
			for(int i = 0; i < amount; i++) {
				int t = shopAmount2+i;
				coins += getDecreasedItemValue(itemId, currency, defaultStock, t, shop.isGeneralStore());
			}
			player.getInventory().addItem(new Item(currency,  (int) coins));
			if(currency == 995)
				player.soldItemsWorth += coins;
		}
		if (shop.isGeneralStore() && shopAmount < 1) {
			shop.getCurrentStock().add(new Item(realItem, amount));
		} else {
			shop.getCurrentStock().set(shop.getCurrentStock().getSlotById(realItem), new Item(realItem, shopAmount + amount));
		}
		player.getInventory().refresh(3823);
		refreshAll(player.getShopId());
	}

	public static void getBuyValue(Player player, int id) {
		Shop shop = shops.get(player.getShopId());
		if (shop.getCurrencyType() == Shop.CurrencyTypes.ITEM) {
			int realId = id;
			int price = ItemManager.getInstance().getItemValue(id, "buyfromshop", shop.getCurrency());
			String currencyName = ItemManager.getInstance().getItemName(shop.getCurrency());
			
			int shopAmount = shop.getCurrentStock().getCount(realId);
			int minimum = 0;
			if(!shop.isGeneralStore())
				minimum = shop.getStock().getById(realId).getCount();
			int shopAmount2 = shopAmount-minimum;
			price = getDecreasedItemValue2(realId, shop.getCurrency(), minimum, shopAmount2, shop.isGeneralStore());
			
			player.getActionSender().sendMessage("" + ItemManager.getInstance().getItemName(id) + ": currently costs " + Misc.formatNumber(price) + " " + currencyName + ".");
		} else {
			int price = ItemManager.getInstance().getItemValue(id, "donator", shop.getCurrency());
			boolean linked = false;
			if(isLinkedDonorItem(id)){
				linked = true;
			}
			if(!linked)
				player.getActionSender().sendMessage("" + ItemManager.getInstance().getItemName(id) + ": currently costs " + price + " " + getCurrencyName(shop) + ".");
			else{
				ItemDefinition def = ItemDefinition.forId(id);
				def = ItemDefinition.forId(def.linkedTo);
				player.getActionSender().sendMessage("" + ItemManager.getInstance().getItemName(id) + ": currently costs " + price + " " + getCurrencyName(shop) + " + "+def.getName()+".");
			}
		}
	}

	public static void getSellValue(Player player, int id) {
		Shop shop = shops.get(player.getShopId());
		ItemDefinition def = ItemDefinition.forId(id);
		int currency = shop.getCurrency();
		int realId = id;
		if(def.isNoted())
			realId = def.getNormalId();
		if (shop.getCurrencyType() != Shop.CurrencyTypes.ITEM) {
			player.getActionSender().sendMessage("This shop can't buy anything.");
			return;
		}
		if (id == 995) {
			player.getActionSender().sendMessage("You cannot sell coins to the shop.");
			return;
		}
		if ((!shop.isGeneralStore() && !shop.getCurrentStock().contains(realId)) || def.isUntradable()) {
			player.getActionSender().sendMessage("You cannot sell this item in this shop.");
			return;
		}
		if (shop.getCurrencyType() == Shop.CurrencyTypes.ITEM || shop.isGeneralStore()) {
			String s = (!shop.isGeneralStore() ? "Specialty" : "General");
			int price = ItemManager.getInstance().getItemValue(id, "selltoshop_"+s, currency);
			ItemManager.getInstance().getItemName(shop.getCurrency());
			int shopAmount = shop.getCurrentStock().getCount(realId);
			int minimum = 0;
			if(!shop.isGeneralStore())
				minimum = shop.getStock().getById(realId).getCount();
			int shopAmount2 = shopAmount-minimum;
			price = getDecreasedItemValue(realId, currency, minimum, shopAmount2, shop.isGeneralStore());
			player.getActionSender().sendMessage("" + ItemManager.getInstance().getItemName(id) + ": shop will buy for " + Misc.formatNumber(price) + " " + getCurrencyName(shop) + ".");
		} else {
			player.getActionSender().sendMessage("You cannot sell this item to this shop.");
		}
	}

	public static int getCurrencyForShopType(Player player, Shop shop) {
		switch (shop.getCurrencyType()) {
		case DONATOR_POINTS :
			return player.getDonatorPoints();
		}
		return -1;
	}

	public static int getSpecialShopPrice(Player player, Shop shop, int buyId) { //this isn't used?
		switch (shop.getCurrencyType()) {
		case DONATOR_POINTS :
			switch (buyId) {
			case 4152 :
				return 10;
			case 8000://added theses 3
				return 100;
			case 7999:
				return 1000;
			case 7408:
				return 10000;
			}
			break;
		}
		return 0;
	}

	public static void decreaseCurrencyForSpecialShop(Player player, Shop shop, int value) {
		switch (shop.getCurrencyType()) {
		case DONATOR_POINTS :
			player.decreaseDonatorPoints(value);
			break;
		}
	}

	public static String getCurrencyName(Shop shop) {
		switch (shop.getCurrencyType()) {
		case DONATOR_POINTS :
			return "Donator Points";
		}
		return ItemManager.getInstance().getItemName(shop.getCurrency());
	}

	public static void loadShops() {
        int shopCount = 0;
        try {
			byte abyte2[] = FileOperations.ReadFile("./data/content/Shops.dat");
			Stream stream2 = new Stream(abyte2);
			shopCount = stream2.readUnsignedWord();
			for (int i2 = 0; i2 < shopCount; i2++) {
				Shop shop = new Shop();
				String name = stream2.readString();
				int type = stream2.readUnsignedByte();
				int itemAmt = stream2.readUnsignedByte();
				Container stock = new Container(Type.ALWAYS_STACK, SIZE);
				Container currentStock = new Container(Type.ALWAYS_STACK, SIZE);
				for (int i234 = 0; i234 < itemAmt; i234++) {
					int item_ = stream2.readUnsignedWord();
					int count = stream2.readUnsignedWord();
					stock.add(new Item(item_, count));
					currentStock.add(new Item(item_, count));
				}
				shop.setStock(stock);
				shop.setCurrentStock(currentStock);
				int currency = stream2.readUnsignedWord();
				if(Constants.EASY_AEON){
					if(type == 2){
						stock = new Container(Type.ALWAYS_STACK, SIZE);
						currentStock = new Container(Type.ALWAYS_STACK, SIZE);
						stock.add(new Item(7999, 1000));
						currentStock.add(new Item(7999, 1000));
						stock.add(new Item(8000, 1000));
						currentStock.add(new Item(8000, 1000));
//						stock.add(new Item(7408, 10));
//						currentStock.add(new Item(7408, 10));
						shop.setStock(stock);
						shop.setCurrentStock(currentStock);
					}
				}
				shop.setName(name);
				shop.setGeneralStore((type == 0 ? true : false));
				shop.setCurrency(currency);
				shop.setCurrencyType(Shop.CurrencyTypes.values()[(type == 2 ? 1 : 0)]);
				shop.shopId = i2;
				shops.add(shop);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Loaded " + shops.size() + " shop definitions.");
		ShopManager.process();
	}
	
	public static void process() {
		World.submit(new Tick(100) {//was 30
			@Override
			public void execute() {
				for (Shop shop : shops) {
					for (Item item : shop.getCurrentStock().getItems()) {
						if (item == null) {
							continue;
						}
						if (shop.getStock().contains(item.getId())) {
							int minimum = shop.getStock().getById(item.getId()).getCount();
							if (item.getCount() < minimum) {
								shop.getCurrentStock().add(new Item(item.getId()));
							} else if (item.getCount() > minimum) {
								shop.getCurrentStock().removeOrZero(new Item(item.getId()));
							}
						} else {
							shop.getCurrentStock().remove(new Item(item.getId()));
						}
					}
					refreshAll(shop.getShopId());
				}
			}
		});
	}

	public static void setShops(List<Shop> items) {
		ShopManager.shops = items;
	}

	public static List<Shop> getShops() {
		return shops;
	}

	public static class Shop {

		private int shopId;
		private String name;
		private Item[] items;
		private boolean isGeneralStore;
		private CurrencyTypes currencyType;
		private int currency;
		private Container stock;
		private Container currentStock;

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setShopId(int shopId) {
			this.shopId = shopId;
		}

		public int getShopId() {
			return shopId;
		}

		public void setGeneralStore(boolean isGeneralStore) {
			this.isGeneralStore = isGeneralStore;
		}

		public boolean isGeneralStore() {
			return isGeneralStore;
		}

		public void setCurrency(int currency) {
			this.currency = currency;
		}

		public int getCurrency() {
			return currency;
		}

		public CurrencyTypes getCurrencyType() {
			return currencyType;
		}
		
		public void setCurrencyType(CurrencyTypes type) {
			this.currencyType = type;
		}

		public void setItems(Item[] items) {
			this.items = items;
		}

		public Item[] getItems() {
			return items;
		}

		public void setStock(Container stock) {
			this.stock = stock;
		}

		public Container getStock() {
			return stock;
		}

		public void setCurrentStock(Container currentStock) {
			this.currentStock = currentStock;
		}

		public Container getCurrentStock() {
			return currentStock;
		}

		enum CurrencyTypes {
			ITEM, DONATOR_POINTS
		}
	}

}
