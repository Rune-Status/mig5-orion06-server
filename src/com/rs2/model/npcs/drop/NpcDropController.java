package com.rs2.model.npcs.drop;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.content.skills.runecrafting.Pouches;
import com.rs2.model.content.treasuretrails.ClueScroll;
import com.rs2.model.npcs.NpcDefinition;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;


public class NpcDropController {
	
	static Random random = new Random();
	
	public static int herbs[] = {
		199, //guam leaf
		201, //marrentill
		203, //tarromin
		205, //harralander
		207, //ranarr
		209, //irit
		211, //avantoe
		213, //kwuarm
		215, //cadantine
		217, //dwarf weed
		219, //torstol
		2485, //lantadyme
		3049, //toadflax
		3051}; //snapdragon
	
	public static int herbSeeds[] = {
		5291, //guam leaf
		5292, //marrentill
		5293, //tarromin
		5294, //harralander
		5295, //ranarr
		5297, //irit
		5298, //avantoe
		5299, //kwuarm
		5301, //cadantine
		5303, //dwarf weed
		5304, //torstol
			
		5302, //lantadyme
		5296, //toadflax
		5300}; //snapdragon
	
	public static int gems[] = {
		1623, //sapphire
		1621, //emerald
		1619, //ruby
		1617, //diamond
		//1631 //dragonstone
		};
	
	public static int herbsA[] = {
		4, //guam leaf
		4, //marrentill
		4, //tarromin
		4, //harralander
		4, //ranarr
		3, //irit
		3, //avantoe
		3, //kwuarm
		2, //cadantine
		2, //dwarf weed
		2, //torstol
		1, //lantadyme
		1, //toadflax
		1}; //snapdragon

	public static int herbSeedsA[] = {
		4, //guam leaf
		4, //marrentill
		4, //tarromin
		4, //harralander
		4, //ranarr
		3, //irit
		3, //avantoe
		3, //kwuarm
		2, //cadantine
		2, //dwarf weed
		2, //torstol
		
		1, //lantadyme
		1, //toadflax
		1}; //snapdragon
	
	public static int gemsA[] = {
		4, //sapphire
		3, //emerald
		2, //ruby
		1, //diamond
		};
	
	static ArrayList<Integer> rew = new ArrayList<Integer>();
	
	public static int getSpecialDrop(Entity killer, int itemId){
		Random random = new Random();
		boolean cantDropClue = false;
		if(killer.isPlayer()){
			if(((Player)killer).hasClueScroll())
				cantDropClue = true;
		} else {
			cantDropClue = true;
		}
		rew.clear();
		int packId = itemId-65000;
		switch(packId){
		
			case 0://empty drop
				return -1;
			
			case 1://Clue scroll (easy)
				if (cantDropClue)
					return -1;
				return ClueScroll.getRandomClue(1);
				
			case 2://Clue scroll (medium)
				if (cantDropClue)
					return -1;
				return ClueScroll.getRandomClue(2);
				
			case 3://Clue scroll (hard)
				if (cantDropClue)
					return -1;
				return ClueScroll.getRandomClue(3);
				
			case 4://Clue scroll (elite)	--- no elite clues back in 2006!!
				if (cantDropClue)
					return -1;
				return ClueScroll.getRandomClue(3);
				
			case 5://herbs
				for(int i = 0; i < herbs.length; i++){
					int a = herbsA[i];
					for(int j = 0; j < a; j++)
						rew.add(herbs[i]);
				}
				return rew.get(random.nextInt(rew.size()));
			
			case 6://gems
				for(int i = 0; i < gems.length; i++){
					int a = gemsA[i];
					for(int j = 0; j < a; j++)
						rew.add(gems[i]);
				}
				return rew.get(random.nextInt(rew.size()));
				
			case 7://herb seeds
				for(int i = 0; i < herbSeeds.length; i++){
					int a = herbSeedsA[i];
					for(int j = 0; j < a; j++)
						rew.add(herbSeeds[i]);
				}
				return rew.get(random.nextInt(rew.size()));
		}
		return -1;

	//SPECIAL ITEMS/PACKS [above]
	}
	
	public static boolean isSpecialItem(int itemId){
		if(itemId >= 65000 && itemId <= 65000 + 8)
			return true;
		return false;
	}
	
	public static int getRandomNumberFrom(int min, int max) {
        int randomNumber = random.nextInt((max + 1) - min) + min;
        return randomNumber;

    }

	public static NPCDropTable getDropTable(int npcId) {
		final NpcDefinition npc = NpcDefinition.forId(npcId);
		int linked = npc.getDropsLinkedTo();
		int tableId = (linked == -1 ? npcId : linked);
		return NPCDropTable.forId(tableId);
	}
	
	public static Item[] getDrops(Entity killer, int npcId, boolean onlyAlwaysDrops) {
		Item alwaysItems[] = null;
		Item randomItems[] = null;
		Item actualDrops[] = null;
		NPCDropTable table = getDropTable(npcId);
		NPCItemDrop[] always = table.getAlwaysDrops();
		if(always != null)
		if(always.length > 0){//100% drops below
			alwaysItems = new Item[always.length];
			for(int c = 0; c < always.length; c++){
				int itemz = always[c].getId();
				//int amount = always[c].getAmount();
				int amount = 1;
				if(always[c].getAmount() > 0){//Drop type 0
					amount = always[c].getAmount();
				}
				if(always[c].getMinVal() > 0){//Drop type 1
					amount = getRandomNumberFrom(always[c].getMinVal(), always[c].getMaxVal());
				}
				if(always[c].getAmounts().length > 0 && always[c].getId() > 0){//Drop type 2
					int count = random.nextInt(always[c].getAmounts().length);
					amount = always[c].getAmounts()[count];
				}
				if(isSpecialItem(itemz)){
					itemz = getSpecialDrop(killer, itemz);
				}
				//POUCH BELOW
				if(itemz == 5509){
					itemz = Pouches.getPouchDrop(killer, itemz);
				}
				//POUCH ABOVE
				alwaysItems[c] = addItem(itemz, amount);
			}
		}//100% drops
		if(!onlyAlwaysDrops)
			randomItems = getDropItem(killer, getRateTable(killer, table));
		int c1 = (alwaysItems == null ? 0 : alwaysItems.length);
		int c2 = (randomItems == null ? 0 :randomItems.length);
		actualDrops = new Item[c1 + c2];
		if(alwaysItems != null)
		for(int c = 0; c < c1; c++){
			actualDrops[c] = alwaysItems[c];
		}
		if(randomItems != null)
		for(int c = 0; c < c2; c++){
			actualDrops[c+c1] = randomItems[c];
		}
		return actualDrops;
	}
	
	public static NPCItemDrop[] getRateTable(Entity killer, NPCDropTable table){
		double mod = 1.0;
		NPCItemDrop[] veryRare = table.getVeryRareDrops();
		NPCItemDrop[] rare = table.getRareDrops();
		NPCItemDrop[] uncommon = table.getUncommonDrops();	
		NPCItemDrop[] common = table.getCommonDrops();
		if(veryRare != null || rare != null)
			if(veryRare.length > 0 || rare.length > 0)
				if(killer.isPlayer()){
					if(((Player)killer).ringOfWealthEffect()){
						mod = 0.667;
				}
		}
		int veryRareChance = (int) ((Constants.EASY_AEON ? 100 : 510)*mod);//340
		int rareChance = (int) ((Constants.EASY_AEON ? 50 : 90)*mod);//60
		int uncommonChance = (int) (3*mod);//2
		if(veryRare != null)
			if (random.nextInt(veryRareChance) == 0 && veryRare.length > 0)//Very rare
				return veryRare;
		if(rare != null)
			if (random.nextInt(rareChance) == 0 && rare.length > 0)//Rare
				return rare;	
		if(uncommon != null)
			if (random.nextInt(uncommonChance) == 0 && uncommon.length > 0)//Uncommon.. tweaked was at 10.
				return uncommon;	
		if(common != null)
			if(common.length > 0)//Common
				return common;
		return null;
	}
	
	public static Item[] getDropItem(Entity killer, NPCItemDrop[] table){
		if(table == null)
			return null;
		
		Item items[] = new Item[1];
		int itemc = random.nextInt(table.length);
		int itemz = 0;
		int amount = 0;
		
		if(table[itemc].getItems().length > 0){
			int count = random.nextInt(table[itemc].getItems().length);
			itemz = table[itemc].getItems()[count];
		} else {
			itemz = table[itemc].getId();
		}
		if(isSpecialItem(itemz)){
			itemz = getSpecialDrop(killer, itemz);
		}
		//POUCH BELOW
		if(itemz == 5509){
			itemz = Pouches.getPouchDrop(killer, itemz);
		}
		//POUCH ABOVE
		if(table[itemc].getAmount() > 0){//Drop type 0
			amount = table[itemc].getAmount();
		}
		if(table[itemc].getMinVal() > 0){//Drop type 1
			amount = getRandomNumberFrom(table[itemc].getMinVal(), table[itemc].getMaxVal());
		}
		if(table[itemc].getAmounts().length > 0 && table[itemc].getId() > 0){//Drop type 2
			int count = random.nextInt(table[itemc].getAmounts().length);
			amount = table[itemc].getAmounts()[count];
		}
		if(table[itemc].getItems().length > 1){//Drop type 3
			items = new Item[table[itemc].getItems().length];
			for(int r1 = 0; r1 < table[itemc].getItems().length; r1++){
				int id12 = table[itemc].getItems()[r1];
				if(isSpecialItem(id12))
					id12 = getSpecialDrop(killer, id12);
				//POUCH BELOW
				if(id12 == 5509){
					id12 = Pouches.getPouchDrop(killer, id12);
				}
				//POUCH ABOVE
				items[r1] = addItem(id12, table[itemc].getAmounts()[r1]);
			}
		}else{
			items[0] = addItem(itemz, amount);
		}
		return items;
	}

	public static Item addItem(int id, int amount){
		return new Item(id, amount);
	}
	
}
