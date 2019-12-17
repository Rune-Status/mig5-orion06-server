package com.rs2.model.npcs.drop;

import com.rs2.model.npcs.NpcDefinition;
import com.rs2.model.players.item.Item;
import com.rs2.util.Stream;

public class NPCDropTable {
	
	private static NPCDropTable[] definitions = new NPCDropTable[0];
	
	private NPCItemDrop[] alwaysDrop;
	private NPCItemDrop[] commonDrop;
	private NPCItemDrop[] uncommonDrop;
	private NPCItemDrop[] rareDrop;
	private NPCItemDrop[] veryRareDrop;
	
	public static void unpackDrops()
	{
		if(com.rs2.util.FileOperations.FileExists("./data/npcs/Npc-drops.dat")){
			byte abyte2[] = com.rs2.util.FileOperations.ReadFile("./Data/npcs/Npc-drops.dat");
			Stream stream2 = new Stream(abyte2);
			int npcs = stream2.readUnsignedWord();
			definitions = new NPCDropTable[npcs];
			for(int npcId = 0; npcId < npcs; npcId++){
				int hasDrops = stream2.readUnsignedByte();
				if(hasDrops == 2){
					int id_new = stream2.readUnsignedWord();
					final NpcDefinition npc = NpcDefinition.forId(npcId);
					npc.setDropsLinkedTo(id_new);
				}
				if(hasDrops == 1){
					NPCItemDrop[] alwaysTable = null;
					NPCItemDrop[] commonTable = null;
					NPCItemDrop[] uncommonTable = null;
					NPCItemDrop[] rareTable = null;
					NPCItemDrop[] veryRareTable = null;
					for(int rateType = 0; rateType < 5; rateType++){
						int itemCount = stream2.readUnsignedByte();
						if(rateType == 0)
							alwaysTable = new NPCItemDrop[itemCount];
						if(rateType == 1)
							commonTable = new NPCItemDrop[itemCount];
						if(rateType == 2)
							uncommonTable = new NPCItemDrop[itemCount];
						if(rateType == 3)
							rareTable = new NPCItemDrop[itemCount];
						if(rateType == 4)
							veryRareTable = new NPCItemDrop[itemCount];
						for(int item = 0; item < itemCount; item++){
							NPCItemDrop item_d = readDropValues(stream2, npcId, item);
							if(rateType == 0)
								alwaysTable[item] = item_d;
							if(rateType == 1)
								commonTable[item] = item_d;
							if(rateType == 2)
								uncommonTable[item] = item_d;
							if(rateType == 3)
								rareTable[item] = item_d;
							if(rateType == 4)
								veryRareTable[item] = item_d;
						}
					}
					definitions[npcId] = new NPCDropTable(alwaysTable, commonTable, uncommonTable, rareTable, veryRareTable);
				}
			}
			//System.out.println("Loaded.");
		}
	}
    
    public static NPCItemDrop readDropValues(Stream stream, int npc, int item){//rateType: 0 - always, 1 - common, 2 - uncommon, 3 - rare, 4 - very rare
    	int dropType = stream.readUnsignedByte();
    	int itemId = -1;
    	int itemAmount = -1;
    	int itemMinVal = -1;
    	int itemMaxVal = -1;
    	int[] itemVal = null;
    	int[] itemValI = null;
    	if(dropType != 3){
    		itemId = stream.readUnsignedWord();
    	}
    	if(dropType == 0){
    		itemAmount = stream.readUnsignedWord();
    	}
    	if(dropType == 1){
    		itemMinVal = stream.readUnsignedWord();
    		itemMaxVal = stream.readUnsignedWord();
    	}
    	if(dropType == 2){
    		int count = stream.readUnsignedByte();
    		itemVal = new int[count];
    		for(int a1 = 0; a1 < count; a1++){
    			itemVal[a1] = stream.readUnsignedWord();
    		}
    	}
    	if(dropType == 3){
    		int count = stream.readUnsignedByte();
    		itemVal = new int[count];
    		itemValI = new int[count];
    		for(int a1 = 0; a1 < count; a1++){
    			itemValI[a1] = stream.readUnsignedWord();
    			itemVal[a1] = stream.readUnsignedWord();
    		}
    	}
    	return new NPCItemDrop(itemId, itemAmount, itemMinVal, itemMaxVal, itemValI, itemVal);
    }
	
	public NPCDropTable(NPCItemDrop[] always, NPCItemDrop[] common, NPCItemDrop[] uncommon, NPCItemDrop[] rare, NPCItemDrop[] veryRare) {
		this.alwaysDrop = always;
		this.commonDrop = common;
		this.uncommonDrop = uncommon;
		this.rareDrop = rare;
		this.veryRareDrop = veryRare;
	}
	
	public static NPCDropTable forId(int id) {
		if (id < 0) {
			id = 1;
		}
		NPCDropTable def = definitions[id];
		if (def == null) {
			//System.out.println(id);//to check missing drop
			def = new NPCDropTable(null, null, null, null, null);
		}
		return def;
	}
	
	public NPCItemDrop[] getAlwaysDrops() {
		return alwaysDrop;
	}
	
	public NPCItemDrop[] getCommonDrops() {
		return commonDrop;
	}
	
	public NPCItemDrop[] getUncommonDrops() {
		return uncommonDrop;
	}
	
	public NPCItemDrop[] getRareDrops() {
		return rareDrop;
	}
	
	public NPCItemDrop[] getVeryRareDrops() {
		return veryRareDrop;
	}
	
}
