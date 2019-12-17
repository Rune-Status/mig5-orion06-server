package com.rs2.model.content;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.rs2.model.World;
import com.rs2.model.content.hiscore.Hplayer;
import com.rs2.model.players.GeManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.FileOperations;
import com.rs2.util.Packer;
import com.rs2.util.PlayerSave;
import com.rs2.util.Stream;

public class GeOffer {

	String playerName;
	boolean isSell;
	int itemId;
	int boxId;
	int fullAmount;
	int itemAmount;
	int price;
	
	public static ArrayList<GeOffer> listOfSellOffers = new ArrayList<GeOffer>();
	public static ArrayList<GeOffer> listOfBuyOffers = new ArrayList<GeOffer>();
	
	public GeOffer(String playerName, int boxId, boolean isSell, int itemId, int itemAmount, int fullAmount, int price){
		this.playerName = playerName;
		this.itemId = itemId;
		this.isSell = isSell;
		this.itemAmount = itemAmount;
		this.price = price;
		this.boxId = boxId;
		this.fullAmount = fullAmount;
		if(isSell)
			listOfSellOffers.add(this);
		else
			listOfBuyOffers.add(this);
		checkGeOffers(this, isSell);
	}
	
	public static void cancelOffer(Player player, int boxId){
		String playerName = player.getUsername();
		if(player.geOfferType[boxId]){//sell
			for (GeOffer go : listOfSellOffers) {
				if(!go.getPlayerName().equals(playerName))
					continue;
				if(go.getBoxId() != boxId)
					continue;
				if(checkValidOffer(player, go)){
					removeSell.add(go);
					break;
				}
			}
		} else {
			for (GeOffer go : listOfBuyOffers) {
				if(!go.getPlayerName().equals(playerName))
					continue;
				if(go.getBoxId() != boxId)
					continue;
				if(checkValidOffer(player, go)){
					removeBuy.add(go);
					break;
				}
			}
		}
		for (GeOffer offer2Remove : removeBuy) {
			listOfBuyOffers.remove(offer2Remove);
		}
		for (GeOffer offer2Remove : removeSell) {
			listOfSellOffers.remove(offer2Remove);
		}
	}
	
	public static ArrayList<GeOffer> removeBuy = new ArrayList<GeOffer>();
	public static ArrayList<GeOffer> removeSell = new ArrayList<GeOffer>();
	
	public void checkGeOffers(GeOffer o, boolean isSell){
		removeBuy.clear();
		removeSell.clear();
		if(isSell){
			for (GeOffer go : listOfBuyOffers) {
				if(go.getPlayerName().equals(o.getPlayerName()))
					continue;
				if(go.getItemId() != o.getItemId())
					continue;
				if(go.getPrice() < o.getPrice())
					continue;
				findPlayers(o, go);
				if(go.getItemAmount() == 0){
					removeBuy.add(go);
				}
				if(o.getItemAmount() == 0){
					removeSell.add(o);
					break;
				}
			}
		}else{
			for (GeOffer go : listOfSellOffers) {
				if(go.getPlayerName().equals(o.getPlayerName()))
					continue;
				if(go.getItemId() != o.getItemId())
					continue;
				if(go.getPrice() > o.getPrice())
					continue;
				findPlayers(go, o);
				if(go.getItemAmount() == 0){
					removeSell.add(go);
				}
				if(o.getItemAmount() == 0){
					removeBuy.add(o);
					break;
				}
			}
		}
		for (GeOffer offer2Remove : removeBuy) {
			listOfBuyOffers.remove(offer2Remove);
		}
		for (GeOffer offer2Remove : removeSell) {
			listOfSellOffers.remove(offer2Remove);
		}
	}

	public void findPlayers(GeOffer sellerOffer, GeOffer buyerOffer){
		boolean sellerFound = false;
		boolean buyerFound = false;
		String sellerName = sellerOffer.getPlayerName();
		String buyerName = buyerOffer.getPlayerName();
		Player playerSeller = null;
		Player playerBuyer = null;
		Hplayer hplayerSeller = null;
		Hplayer hplayerBuyer = null;
		//System.out.println("check1 findPlayer");
		for(Player player : World.getPlayers()){
			if(player == null)
				continue;
			if(player.getUsername().equals(sellerName)){
				if(checkValidOffer(player, sellerOffer)){
					playerSeller = player;
					sellerFound = true;
				} else {
					removeSell.add(sellerOffer);
					return;
				}
			} else if(player.getUsername().equals(buyerName)){
				if(checkValidOffer(player, buyerOffer)){
					playerBuyer = player;
					buyerFound = true;
				} else {
					removeBuy.add(buyerOffer);
					return;
				}
			}
			if(sellerFound && buyerFound)
				break;
		}
		//System.out.println("check2 findPlayer "+sellerFound+" "+buyerFound);
		if(!sellerFound || !buyerFound){
			for(Hplayer hplayer : PlayerSave.listOfPlayersAll){
				if(hplayer == null)
					continue;
				if(!sellerFound)
				if(hplayer.getUsername().equals(sellerName)){
					if(checkValidOffer(hplayer, sellerOffer)){
						hplayerSeller = hplayer;
						sellerFound = true;
					} else {
						removeSell.add(sellerOffer);
						return;
					}
				}
				if(!buyerFound)
				if(hplayer.getUsername().equals(buyerName)){
					if(checkValidOffer(hplayer, buyerOffer)){
						hplayerBuyer = hplayer;
						buyerFound = true;
					} else {
						removeBuy.add(buyerOffer);
						return;
					}
				}
				if(sellerFound && buyerFound)
					break;
			}
		}
		/*System.out.println("check3 findPlayer "+sellerFound+" "+buyerFound);
		System.out.println(hplayerBuyer+" "+hplayerSeller);
		System.out.println(playerBuyer+" "+playerSeller);*/
		if(hplayerBuyer != null && hplayerSeller != null){
			doGeTrade(hplayerBuyer, buyerOffer, hplayerSeller, sellerOffer);
		}
		else if(playerBuyer != null && playerSeller != null){
			playerBuyer.geTradeInProgress = true;
			playerSeller.geTradeInProgress = true;
			doGeTrade(playerBuyer, buyerOffer, playerSeller, sellerOffer);
			playerBuyer.geTradeInProgress = false;
			playerSeller.geTradeInProgress = false;
		}
		else if(hplayerBuyer != null && playerSeller != null){
			playerSeller.geTradeInProgress = true;
			doGeTrade(hplayerBuyer, buyerOffer, playerSeller, sellerOffer);
			playerSeller.geTradeInProgress = false;
		}
		else if(playerBuyer != null && hplayerSeller != null){
			playerBuyer.geTradeInProgress = true;
			doGeTrade(playerBuyer, buyerOffer, hplayerSeller, sellerOffer);
			playerBuyer.geTradeInProgress = false;
		}
	}
	
	public void doGeTrade(Hplayer buyer, GeOffer buyerOffer, Hplayer seller, GeOffer sellerOffer){
		int maxAmount = sellerOffer.getItemAmount();
		int transferredAmount = (buyerOffer.getItemAmount() >= maxAmount) ? maxAmount : buyerOffer.getItemAmount();
		int transferPrice = sellerOffer.getPrice();
		int totalCashTransfer = sellerOffer.getPrice()*transferredAmount;
		buyerOffer.setItemAmount(buyerOffer.getItemAmount()-transferredAmount);
		sellerOffer.setItemAmount(sellerOffer.getItemAmount()-transferredAmount);
		buyer.geItemsTraded[buyerOffer.getBoxId()] += transferredAmount;
		seller.geItemsTraded[sellerOffer.getBoxId()] += transferredAmount;
		buyer.geCashMoved[buyerOffer.getBoxId()] += totalCashTransfer;
		seller.geCashMoved[sellerOffer.getBoxId()] += totalCashTransfer;
		if(transferPrice < buyerOffer.getPrice()){
			int addEa = buyerOffer.getPrice()-transferPrice;
			int addTotal = addEa*transferredAmount;
			buyer.geCollectOtherItem[buyerOffer.getBoxId()] += addTotal;
		}
		buyer.geCollectItem2Receive[buyerOffer.getBoxId()] += transferredAmount;
		seller.geCollectItem2Receive[sellerOffer.getBoxId()] += totalCashTransfer;
		if(buyerOffer.getItemAmount() == 0)
			buyer.geNotifyProgress[buyerOffer.getBoxId()] = true;
		if(sellerOffer.getItemAmount() == 0){
			seller.geNotifyProgress[sellerOffer.getBoxId()] = true;
			new CompletedOffer(sellerOffer);
		}
		PlayerSave.savePlayerFile(buyer);
		PlayerSave.savePlayerFile(seller);
	}
	
	public void doGeTrade(Player buyer, GeOffer buyerOffer, Player seller, GeOffer sellerOffer){
		int maxAmount = sellerOffer.getItemAmount();
		int transferredAmount = (buyerOffer.getItemAmount() >= maxAmount) ? maxAmount : buyerOffer.getItemAmount();
		int transferPrice = sellerOffer.getPrice();
		int totalCashTransfer = sellerOffer.getPrice()*transferredAmount;
		buyerOffer.setItemAmount(buyerOffer.getItemAmount()-transferredAmount);
		sellerOffer.setItemAmount(sellerOffer.getItemAmount()-transferredAmount);
		buyer.geItemsTraded[buyerOffer.getBoxId()] += transferredAmount;
		seller.geItemsTraded[sellerOffer.getBoxId()] += transferredAmount;
		buyer.geCashMoved[buyerOffer.getBoxId()] += totalCashTransfer;
		seller.geCashMoved[sellerOffer.getBoxId()] += totalCashTransfer;
		if(transferPrice < buyerOffer.getPrice()){
			int addEa = buyerOffer.getPrice()-transferPrice;
			int addTotal = addEa*transferredAmount;
			buyer.geCollectOtherItem[buyerOffer.getBoxId()] += addTotal;
		}
		buyer.geCollectItem2Receive[buyerOffer.getBoxId()] += transferredAmount;
		seller.geCollectItem2Receive[sellerOffer.getBoxId()] += totalCashTransfer;
		//String itemName = new Item(buyer.geOfferItemId[buyerOffer.getBoxId()], 1).getDefinition().getName();
		if(buyerOffer.getItemAmount() == 0){
			//buyer.getActionSender().sendMessage("Finished buying "+buyer.geOfferItemAmount[buyerOffer.getBoxId()]+" "+itemName+".");
			GeManager.notifyBox(buyer, buyerOffer.getBoxId());
		}/* else {
			buyer.getActionSender().sendMessage("Bought "+buyer.geItemsTraded[buyerOffer.getBoxId()]+"/"+buyer.geOfferItemAmount[buyerOffer.getBoxId()]+" "+itemName+".");
		}*/
		if(sellerOffer.getItemAmount() == 0){
			//seller.getActionSender().sendMessage("Finished selling "+seller.geOfferItemAmount[sellerOffer.getBoxId()]+" "+itemName+".");
			GeManager.notifyBox(seller, sellerOffer.getBoxId());
			new CompletedOffer(sellerOffer);
		}/* else {
			seller.getActionSender().sendMessage("Sold "+seller.geItemsTraded[sellerOffer.getBoxId()]+"/"+seller.geOfferItemAmount[sellerOffer.getBoxId()]+" "+itemName+".");
		}*/
		if(buyer.getInterface() == GeManager.GE_MAIN_INTERFACE)
			GeManager.updateOfferBoxes(buyer);
		if(buyer.getInterface() == GeManager.GE_COLLECT_INTERFACE && buyer.geOfferBox_temp == buyerOffer.getBoxId())
			GeManager.updateCollectStatusScreen(buyer);
		if(seller.getInterface() == GeManager.GE_MAIN_INTERFACE)
			GeManager.updateOfferBoxes(seller);
		if(seller.getInterface() == GeManager.GE_COLLECT_INTERFACE && seller.geOfferBox_temp == sellerOffer.getBoxId())
			GeManager.updateCollectStatusScreen(seller);
		buyer.save();
		seller.save();
	}
	
	public void doGeTrade(Player buyer, GeOffer buyerOffer, Hplayer seller, GeOffer sellerOffer){
		int maxAmount = sellerOffer.getItemAmount();
		int transferredAmount = (buyerOffer.getItemAmount() >= maxAmount) ? maxAmount : buyerOffer.getItemAmount();
		int transferPrice = sellerOffer.getPrice();
		int totalCashTransfer = sellerOffer.getPrice()*transferredAmount;
		buyerOffer.setItemAmount(buyerOffer.getItemAmount()-transferredAmount);
		sellerOffer.setItemAmount(sellerOffer.getItemAmount()-transferredAmount);
		buyer.geItemsTraded[buyerOffer.getBoxId()] += transferredAmount;
		seller.geItemsTraded[sellerOffer.getBoxId()] += transferredAmount;
		buyer.geCashMoved[buyerOffer.getBoxId()] += totalCashTransfer;
		seller.geCashMoved[sellerOffer.getBoxId()] += totalCashTransfer;
		if(transferPrice < buyerOffer.getPrice()){
			int addEa = buyerOffer.getPrice()-transferPrice;
			int addTotal = addEa*transferredAmount;
			buyer.geCollectOtherItem[buyerOffer.getBoxId()] += addTotal;
		}
		buyer.geCollectItem2Receive[buyerOffer.getBoxId()] += transferredAmount;
		seller.geCollectItem2Receive[sellerOffer.getBoxId()] += totalCashTransfer;
		//String itemName = new Item(buyer.geOfferItemId[buyerOffer.getBoxId()], 1).getDefinition().getName();
		if(buyerOffer.getItemAmount() == 0){
			//buyer.getActionSender().sendMessage("Finished buying "+buyer.geOfferItemAmount[buyerOffer.getBoxId()]+" "+itemName+".");
			GeManager.notifyBox(buyer, buyerOffer.getBoxId());
		}/* else {
			buyer.getActionSender().sendMessage("Bought "+buyer.geItemsTraded[buyerOffer.getBoxId()]+"/"+buyer.geOfferItemAmount[buyerOffer.getBoxId()]+" "+itemName+".");
		}*/
		if(sellerOffer.getItemAmount() == 0){
			seller.geNotifyProgress[sellerOffer.getBoxId()] = true;
			new CompletedOffer(sellerOffer);
		}
		if(buyer.getInterface() == GeManager.GE_MAIN_INTERFACE)
			GeManager.updateOfferBoxes(buyer);
		if(buyer.getInterface() == GeManager.GE_COLLECT_INTERFACE && buyer.geOfferBox_temp == buyerOffer.getBoxId())
			GeManager.updateCollectStatusScreen(buyer);
		PlayerSave.savePlayerFile(seller);
		buyer.save();
	}
	
	public void doGeTrade(Hplayer buyer, GeOffer buyerOffer, Player seller, GeOffer sellerOffer){
		int maxAmount = sellerOffer.getItemAmount();
		int transferredAmount = (buyerOffer.getItemAmount() >= maxAmount) ? maxAmount : buyerOffer.getItemAmount();
		int transferPrice = sellerOffer.getPrice();
		int totalCashTransfer = sellerOffer.getPrice()*transferredAmount;
		buyerOffer.setItemAmount(buyerOffer.getItemAmount()-transferredAmount);
		sellerOffer.setItemAmount(sellerOffer.getItemAmount()-transferredAmount);
		buyer.geItemsTraded[buyerOffer.getBoxId()] += transferredAmount;
		seller.geItemsTraded[sellerOffer.getBoxId()] += transferredAmount;
		buyer.geCashMoved[buyerOffer.getBoxId()] += totalCashTransfer;
		seller.geCashMoved[sellerOffer.getBoxId()] += totalCashTransfer;
		if(transferPrice < buyerOffer.getPrice()){
			int addEa = buyerOffer.getPrice()-transferPrice;
			int addTotal = addEa*transferredAmount;
			buyer.geCollectOtherItem[buyerOffer.getBoxId()] += addTotal;
		}
		buyer.geCollectItem2Receive[buyerOffer.getBoxId()] += transferredAmount;
		seller.geCollectItem2Receive[sellerOffer.getBoxId()] += totalCashTransfer;
		//String itemName = new Item(buyer.geOfferItemId[buyerOffer.getBoxId()], 1).getDefinition().getName();
		if(sellerOffer.getItemAmount() == 0){
			//seller.getActionSender().sendMessage("Finished selling "+seller.geOfferItemAmount[sellerOffer.getBoxId()]+" "+itemName+".");
			GeManager.notifyBox(seller, sellerOffer.getBoxId());
			new CompletedOffer(sellerOffer);
		}/* else {
			seller.getActionSender().sendMessage("Sold "+seller.geItemsTraded[sellerOffer.getBoxId()]+"/"+seller.geOfferItemAmount[sellerOffer.getBoxId()]+" "+itemName+".");
		}*/
		if(buyerOffer.getItemAmount() == 0)
			buyer.geNotifyProgress[buyerOffer.getBoxId()] = true;
		if(seller.getInterface() == GeManager.GE_MAIN_INTERFACE)
			GeManager.updateOfferBoxes(seller);
		if(seller.getInterface() == GeManager.GE_COLLECT_INTERFACE && seller.geOfferBox_temp == sellerOffer.getBoxId())
			GeManager.updateCollectStatusScreen(seller);
		PlayerSave.savePlayerFile(buyer);
		seller.save();
	}
	
	public boolean checkValidOffer(Hplayer player, GeOffer offer){
		if(player.geIsCanceled[offer.boxId])
			return false;
		if(player.geOfferType[offer.boxId] == offer.isSell() &&
				player.geOfferItemId[offer.boxId] == offer.getItemId() &&
				player.geOfferItemAmount[offer.boxId] == offer.getFullAmount() &&
				player.geOfferPrice[offer.boxId] == offer.getPrice()){
			return true;
		}
		return false;
	}
	
	public static boolean checkValidOffer(Player player, GeOffer offer){
		if(player.geIsCanceled[offer.boxId])
			return false;
		if(player.geOfferType[offer.boxId] == offer.isSell() &&
				player.geOfferItemId[offer.boxId] == offer.getItemId() &&
				player.geOfferItemAmount[offer.boxId] == offer.getFullAmount() &&
				player.geOfferPrice[offer.boxId] == offer.getPrice()){
			return true;
		}
		return false;
	}
	
	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
	public int getBoxId() {
		return boxId;
	}

	public void setBoxId(int boxId) {
		this.boxId = boxId;
	}
	
	public int getFullAmount() {
		return fullAmount;
	}

	public void setFullAmount(int fullAmount) {
		this.fullAmount = fullAmount;
	}

	public int getItemAmount() {
		return itemAmount;
	}

	public void setItemAmount(int itemAmount) {
		this.itemAmount = itemAmount;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
	public boolean isSell(){
		return isSell;
	}
	
	public void setIsSell(boolean isSell) {
		this.isSell = isSell;
	}
	
	public static class CompletedOffer{
		
		long time;
		int itemId;
		int itemAmount;
		int price;
		
		public static ArrayList<CompletedOffer> listOfCompletedOffers = new ArrayList<CompletedOffer>();
		
		CompletedOffer(GeOffer offer){
			this.time = System.currentTimeMillis();
			this.itemId = offer.getItemId();
			this.itemAmount = offer.getFullAmount();
			this.price = offer.getPrice();
			listOfCompletedOffers.add(this);
			try {
				writeCompletedOfferData();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		CompletedOffer(long time, int itemId, int itemAmount, int price){
			this.time = time;
			this.itemId = itemId;
			this.itemAmount = itemAmount;
			this.price = price;
			//System.out.println(itemId+" "+itemAmount+" "+price+" "+time);
			listOfCompletedOffers.add(this);
		}
		
		public static int getGuidePriceForItem(int itemId){
			int price = 0;
			int amount = 0;
			for(CompletedOffer offer : listOfCompletedOffers){
				if(offer == null)
					continue;
				if(offer.getItemId() != itemId)
					continue;
				price += offer.getItemAmount()*offer.getPrice();
				amount += offer.getItemAmount();
			}
			if(price == 0 || amount == 0)
				return -1;
			return price/amount;
		}
		
		public static void loadCompletedOfferData(){
			listOfCompletedOffers.clear();
			if(!FileOperations.FileExists("./data/geOfferData.dat"))
				return;
			byte[] abyte0 = FileOperations.ReadFile("./data/geOfferData.dat");
			Stream stream = new Stream(abyte0);
			int dataCount = stream.readDWord();
			for(int i = 0; i < dataCount; i++){
				long time = stream.readQWord();
				int itemId = stream.readUnsignedWord();
				int itemAmount = stream.readDWord();
				int price = stream.readDWord();
				new CompletedOffer(time, itemId, itemAmount, price);
			}
		}
		
		public void writeCompletedOfferData() throws IOException{
			Packer dat = new Packer(new FileOutputStream("./data/geOfferData.dat"));
			dat.writeInt(listOfCompletedOffers.size());
			for(CompletedOffer offer: listOfCompletedOffers){
				dat.writeLong(offer.getTime());
				dat.writeShort(offer.getItemId());
				dat.writeInt(offer.getItemAmount());
				dat.writeInt(offer.getPrice());
			}
			dat.close();
		}
		
		public long getTime(){
			return time;
		}
		
		public void setTime(long time) {
			this.time = time;
		}
		
		public int getItemId(){
			return itemId;
		}
		
		public void setItemId(int itemId) {
			this.itemId = itemId;
		}
		
		public int getItemAmount(){
			return itemAmount;
		}
		
		public void setItemAmount(int itemAmount) {
			this.itemAmount = itemAmount;
		}
		
		public int getPrice(){
			return price;
		}
		
		public void setPrice(int price) {
			this.price = price;
		}
		
	}
	
}
