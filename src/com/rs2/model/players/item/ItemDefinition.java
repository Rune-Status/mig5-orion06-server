package com.rs2.model.players.item;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import com.rs2.Constants;
import com.rs2.cache.Archive;
import com.rs2.cache.Cache;
import com.rs2.model.content.questing.QuestDefinition;
import com.rs2.util.FileOperations;
import com.rs2.util.Stream;

public class ItemDefinition {

	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(ItemDefinition.class.getName());

	/**
	 * The definition array.
	 */
	private static ItemDefinition[] definitions = new ItemDefinition[Constants.MAX_ITEM_ID + 1];

	/**
	 * Gets a definition for the specified id.
	 * 
	 * @param id
	 *            The id.
	 * @return The definition.
	 */
	public static ItemDefinition forId(int id) {
		if (id < 0) {
			id = 1;
		}
		ItemDefinition def = definitions[id];
		if (def == null) {
			def = new ItemDefinition(id, "# + id", "It's an item!", "NONE", false, false, false, -1, -1, true, 0, 0, 0, 0, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 0, new int[25], 0, new boolean[QuestDefinition.QUEST_COUNT], 0, 0, 0);
		}
		return def;
	}
	
	public static void loadItemDef() {
        int itemCount = 0;
        try {
			byte abyte2[] = FileOperations.ReadFile("./data/content/itemDefinitions.dat");
			Stream stream2 = new Stream(abyte2);
			itemCount = stream2.readUnsignedWord();
			for (int i2 = 0; i2 < itemCount; i2++) {
				final ItemDefinition def = forId(i2);
				definitions[i2] = def;
				int equipType = stream2.readUnsignedByte();
				if(equipType == 200)
					continue;
				if(equipType > 200){
					int i = stream2.readSignedWord();
					final ItemDefinition def2 = forId(i);
					def.linkedTo = i;
					def.getSlot = def2.getSlot;
					def.untradable = def2.untradable;
					//untra[i2] = def2.untradable;
					def.twoHanded = def2.twoHanded;
					def.hideType = def2.hideType;
					for(int skill_ = 0; skill_ < 25; skill_++){
						def.skillReq[skill_] = def2.skillReq[skill_];
					}
					def.qpReq = def2.qpReq;
					for(int quest_ = 0; quest_ < 104; quest_++){
						def.questReq[quest_] = def2.questReq[quest_];
					}
					def.shopPrice = def2.shopPrice;
					def.highAlcValue = def2.highAlcValue;
					def.lowAlcValue = def2.lowAlcValue;
					def.tokkulPrice = def2.tokkulPrice;
					def.rangeTicketPrice = def2.rangeTicketPrice;
					def.weight = def2.weight;
					for(int bonus_ = 0; bonus_ < 12; bonus_++){
						def.bonus[bonus_] = def2.bonus[bonus_];
					}
					if(equipType == 202)
						def.donorPointPrice = stream2.readSignedWord();
				}else{
				def.getSlot = equipType-1;
				def.untradable = (stream2.readUnsignedByte() == 1 ? true : false);
				//untra[i2] = def.untradable;
				if(equipType != 0){
					def.twoHanded = (stream2.readUnsignedByte() == 1 ? true : false);
					def.hideType = stream2.readUnsignedByte();
					do
					{
						int i = stream2.readUnsignedByte();
						if(i == 0){
							break;
						}
						if(i == 1){
							int skill = stream2.readUnsignedByte();
							int lvl = stream2.readUnsignedByte();
							def.skillReq[skill] = lvl;
						}
						if(i == 2){
							int val_ = stream2.readUnsignedByte();
							if(val_ == 250)
								val_ = QuestDefinition.getMaxQuestPoints();
							def.qpReq = val_;
						}
						if(i == 3){
							int qid = stream2.readUnsignedByte();
							def.questReq[qid] = true;
						}
					} while(true);	
					
					int PriceType = stream2.readUnsignedByte();
					if((PriceType & 1) != 0){
						def.shopPrice = stream2.readDWord();
					}
					if((PriceType & 2) != 0){
						def.highAlcValue = stream2.readDWord();
					}
					if((PriceType & 4) != 0){
						def.lowAlcValue = stream2.readDWord();
					}
					if((PriceType & 8) != 0){
						def.tokkulPrice = stream2.readDWord();
					}
					if((PriceType & 16) != 0){
						def.rangeTicketPrice = stream2.readDWord();
					}
					double d = stream2.readSignedWord();
					def.weight = d/1000;
					for(int bonus_ = 0; bonus_ < 12; bonus_++){
						def.bonus[bonus_] = stream2.readSignedByte();
					}
				}else{
					int PriceType = stream2.readUnsignedByte();
					if((PriceType & 1) != 0){
						def.shopPrice = stream2.readDWord();
					}
					if((PriceType & 2) != 0){
						def.highAlcValue = stream2.readDWord();
					}
					if((PriceType & 4) != 0){
						def.lowAlcValue = stream2.readDWord();
					}
					if((PriceType & 8) != 0){
						def.tokkulPrice = stream2.readDWord();
					}
					if((PriceType & 16) != 0){
						def.rangeTicketPrice = stream2.readDWord();
					}
					double d = stream2.readSignedWord();
					def.weight = d/1000;
				}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        loadObjDat();
		//System.out.println("Loaded");
	}
	
	public static int totalItems = 0;
	
	public static void loadObjDat()
	{
		Cache cache = Cache.getSingleton();
		Stream stream2 = null;
		try {
			stream2 = new Stream(new Archive(cache.getFile(0, 2)).getFile("obj.dat"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		totalItems = cache.getIndexTable().getItemDefinitionIndices().length;
		for(int k2 = 0; k2 < totalItems; k2++){
			readValues(stream2, k2);
		}
	}
	
	private static void readValues(Stream stream, int id) {
		ItemDefinition def = definitions[id];
		def.stackable = false;
		def.noteable = false;
		def.noted = false;
		def.members = false;
        do {
            int i = stream.readUnsignedByte();
            if(i == 0){
                return;
            }if(i == 1){
				stream.readUnsignedWord();
            }else if(i == 2){
				def.name = stream.readString();
            }else if(i == 3){
				String str = new String(stream.readBytes());
				def.examine = str;
            }else if(i == 4){
				stream.readUnsignedWord();
            }else if(i == 5){
				stream.readUnsignedWord();
            }else if(i == 6){
				stream.readUnsignedWord();
            }else if(i == 7) {
				stream.readUnsignedWord();
            } else if(i == 8) {
				stream.readUnsignedWord();
            } else if(i == 10)
                stream.readUnsignedWord();
            else if(i == 11){
				def.stackable = true;
            }else if(i == 12){
				stream.readDWord();
            }else if(i == 16){
				def.members = true;
            }else if(i == 23) {
				stream.readUnsignedWord();
				stream.readSignedByte();
            } else if(i == 24){
				stream.readUnsignedWord();
            }else if(i == 25) {
				stream.readUnsignedWord();
				stream.readSignedByte();
            } else if(i == 26){
				stream.readUnsignedWord();
            }else if(i >= 30 && i < 35) {
				stream.readString();
            } else if(i >= 35 && i < 40) {
				stream.readString();
            } else if(i == 40) {
                int j = stream.readUnsignedByte();
                for(int k = 0; k < j; k++) {
					stream.readUnsignedWord();
					stream.readUnsignedWord();
                }
            } else if(i == 78){
				stream.readUnsignedWord();
            }else if(i == 79){
				stream.readUnsignedWord();
            }else if(i == 90){
				stream.readUnsignedWord();
            }else if(i == 91){
				stream.readUnsignedWord();
            }else if(i == 92){
				stream.readUnsignedWord();
            }else if(i == 93){
				stream.readUnsignedWord();
            }else if(i == 95){
				stream.readUnsignedWord();
            }else if(i == 97){
            	int ID_ = stream.readUnsignedWord();
				def.noted = true;
				def.parentId = ID_;
				ItemDefinition def2 = definitions[ID_];
				def2.noteable = true;
				def2.notedId = id;
            }else if(i == 98){
				stream.readUnsignedWord();
            }else if(i >= 100 && i < 110) {
				stream.readUnsignedWord();
				stream.readUnsignedWord();
            } else if(i == 110){
				stream.readUnsignedWord();
            }else if(i == 111){
				stream.readUnsignedWord();
            }else if(i == 112){
				stream.readUnsignedWord();
            }else if(i == 113){
				stream.readSignedByte();
            }else if(i == 114){
				stream.readSignedByte();
            }else if(i == 115){
				stream.readUnsignedByte();
			}else if(i == 121){
				stream.readUnsignedWord();
			}else if(i == 122){
				stream.readUnsignedWord();
			} else if(i == 140) {
                int j = stream.readUnsignedByte();
                for(int k = 0; k < j; k++) {
					stream.readUnsignedWord();
					stream.readUnsignedWord();
                }
			}else if(i == 177){
				//def.members = true;//isOnGe = true
				//ge[id] = true;
			}
        } while(true);
    }

	/**
	 * Id.
	 */
	private int id;

	/**
	 * Name.
	 */
	private String name;

	/**
	 * Description.
	 */
	private String examine;

	/**
	 * Noted flag.
	 */
	private boolean noted;

	/**
	 * Noteable flag.
	 */
	private boolean noteable;

	/**
	 * Stackable flag.
	 */
	private boolean stackable;

	/**
	 * Non-noted id.
	 */
	private int parentId;

	/**
	 * Noted id.
	 */
	private int notedId;

	/**
	 * Members flag.
	 */
	private boolean members;

	/**
	 * The tokkul store price.
	 */
	private int tokkulPrice;
	
	private int rangeTicketPrice;

	/**
	 * Shop value.
	 */
	private int generalStorePrice;

	/**
	 * High alc value.
	 */
	private int highAlcValue;

	/**
	 * Low alc value.
	 */
	private int lowAlcValue;
	
	private int donorPointPrice;

	/**
	 * Bonus values.
	 */
	private final int[] bonus;
	
	private final int[] skillReq;
	
	private final boolean[] questReq;

	/**
	 * Equipment type
	 */
	private final String equipmentType;

	/**
	 * Equipment type
	 */
	private int getSlot;

	private int hideType;
	
	private int qpReq;
	
	public int linkedTo;
	
	private double weight;
	
	/**
	 * Two-handed weapon
	 */
	private boolean twoHanded;

	/**
	 * Shop price
	 */
	private int shopPrice;

	/**
	 * Untradable
	 */
	private boolean untradable;

	/**
	 * Creates the item definition.
	 * 
	 * @param id
	 *            The id.
	 * @param name
	 *            The name.
	 * @param examine
	 *            The description.
	 * @param noted
	 *            The noted flag.
	 * @param noteable
	 *            The noteable flag.
	 * @param stackable
	 *            The stackable flag.
	 * @param parentId
	 *            The non-noted id.
	 * @param notedId
	 *            The noted id.
	 * @param members
	 *            The members flag. The shop price.
	 * @param highAlcValue
	 *            The high alc value.
	 * @param lowAlcValue
	 *            The low alc value.
	 */
	private ItemDefinition(int id, String name, String examine, String equipmentType, boolean noted, boolean noteable, boolean stackable, int parentId, int notedId, boolean members, int tokkulPrice, int generalStorePrice, int highAlcValue, int lowAlcValue, int[] bonus, int hideType, int[] skillReq, int qpReq, boolean[] questReq, double weight, int donorPrice, int rangeTicketPrice) {
		this.id = id;
		this.name = name;
		this.examine = examine;
		this.equipmentType = equipmentType;
		this.noted = noted;
		this.noteable = noteable;
		this.stackable = stackable;
		this.parentId = parentId;
		this.notedId = notedId;
		this.members = members;
		this.tokkulPrice = tokkulPrice;
		this.rangeTicketPrice = rangeTicketPrice;
		this.generalStorePrice = generalStorePrice;
		this.highAlcValue = highAlcValue;
		this.lowAlcValue = lowAlcValue;
		this.bonus = bonus;
		this.getSlot = getSlot(equipmentType);
		//this.twoHanded = isTwoHanded(name);
		this.hideType = hideType;
		this.skillReq = skillReq;
		this.qpReq = qpReq;
		this.questReq = questReq;
		this.weight = weight;
		this.donorPointPrice = donorPrice;
		//this.shopPrice = (int) Math.ceil(highAlcValue * (1.666666666666));
		//this.untradable = isUntradable(id, stackable, noteable);
	}

	public ItemDefinition addSlot() {
		this.getSlot = getSlot(this.equipmentType);
		return this;
	}

	public ItemDefinition addTwoHanded() {
		//this.twoHanded = isTwoHanded(this.name);
		return this;
	}

	public ItemDefinition addUntradable() {
		//this.untradable = isUntradable(this.id, this.stackable, this.noteable);
		return this;
	}

	private ItemDefinition addShopPrice() {
		//this.shopPrice = (int) Math.ceil(highAlcValue * (1.666666666666));
		return this;
	}

	/**
	 * Gets the id.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the name.
	 * 
	 * @return The name.
	 */
	/*public String getName() {
		return name;
	}*/
	
	public String getName() {
		if (noted && parentId != -1 && parentId != id) {
			name = forId(parentId).getName();
		}
		return name;
	}

	/**
	 * Gets the description.
	 * 
	 * @return The description.
	 */
	/*public String getDescription() {
		return examine;
	}*/
	
	public String getDescription() {
		if (noted && parentId != -1 && parentId != id) {
			examine = "Swap this note at any bank for the equivalent item.";
		}
		return examine;
	}

	/**
	 * Gets the noted flag.
	 * 
	 * @return The noted flag.
	 */
	public boolean isNoted() {
		return noted;
	}

	/**
	 * Gets the noteable flag.
	 * 
	 * @return The noteable flag.
	 */
	public boolean isNoteable() {
		return noteable;
	}

	/**
	 * Gets the stackable flag.
	 * 
	 * @return The stackable flag.
	 */
	public boolean isStackable() {
		return stackable || noted;
	}

	/**
	 * Gets the normal id.
	 * 
	 * @return The normal id.
	 */
	public int getNormalId() {
		return parentId;
	}

	/**
	 * Gets the noted id.
	 * 
	 * @return The noted id.
	 */
	public int getNotedId() {
		return notedId;
	}

	/**
	 * Gets the members only flag.
	 * 
	 * @return The members only flag.
	 */
	public boolean isMembersOnly() {
		return members;
	}

	/**
	 * Gets the value.
	 * 
	 * @return The value.
	 */
	/*public int getPrice() {
		int price = generalStorePrice;
		if (this.isNoted() && parentId != -1 && parentId != id) {
			price = forId(parentId).getPrice();
		}
		if (price == 0) {
			return 1;
		}
		return price;
	}*/
	public int getPrice() {//this method is used in player class, but the method its used in isnt used anywhere?
		return getShopPrice();
	}

	public int getTokkulPrice() {
		int price = tokkulPrice;
		if (this.isNoted() && parentId != -1 && parentId != id) {
			if (price < forId(parentId).getTokkulPrice()) {
				price = forId(parentId).getTokkulPrice();
			}
		}
		if (price == 0) {
			return 1;
		}
		return price;
	}
	
	public int getRangeTicketPrice() {
		int price = rangeTicketPrice;
		if (this.isNoted() && parentId != -1 && parentId != id) {
			if (price < forId(parentId).getRangeTicketPrice()) {
				price = forId(parentId).getRangeTicketPrice();
			}
		}
		if (price == 0) {
			return 1;
		}
		return price;
	}

	public int getShopPrice() {
		if (noted && parentId != -1 && parentId != id) {
			if (shopPrice < forId(parentId).getShopPrice()) {
				shopPrice = forId(parentId).getShopPrice();
			}
		}
		return shopPrice;
	}
	
	public int getDonatorShopPrice() {
		if (noted && parentId != -1 && parentId != id) {
			if (donorPointPrice < forId(parentId).getDonatorShopPrice()) {
				donorPointPrice = forId(parentId).getDonatorShopPrice();
			}
		}
		return donorPointPrice;
	}

	/**
	 * Gets the low alc value.
	 * 
	 * @return The low alc value.
	 */
	/*public int getLowAlcValue() {
		if (lowAlcValue == 0) {
			if (noted && parentId != -1 && parentId != id) {
				return forId(parentId).getLowAlcValue();
			}
		}
		return lowAlcValue;
	}*/
	
	public int getLowAlcValue() {
		int val = lowAlcValue;
		if (val == 0) {
			if (noted && parentId != -1 && parentId != id) {
				val = forId(parentId).getLowAlcValue();
			}
			if(val == 0){
				val = getShopPrice()*2/5;
			}
		}
		return val;
	}

	/**
	 * Gets the high alc value.
	 * 
	 * @return The high alc value.
	 */
	/*public int getHighAlcValue() {
		if (highAlcValue == 0) {
			if (noted && parentId != -1 && parentId != id) {
				return forId(parentId).getHighAlcValue();
			}
		}
		return highAlcValue;
	}*/
	
	public int getHighAlcValue() {
		int val = highAlcValue;
		if (val == 0) {
			if (noted && parentId != -1 && parentId != id) {
				val = forId(parentId).getHighAlcValue();
			}
			if(val == 0){
				val = getShopPrice()*3/5;
			}
		}
		return val;
	}

	/**
	 * Gets the array of bonuses.
	 * 
	 * @return The array of item bonuses.
	 */
	public int[] getBonuses() {
		return bonus;
	}

	/**
	 * Gets a specific bonus based on this item definition..
	 * 
	 * @return The bonus value
	 */
	public int getBonus(int id) {
		return bonus[id];
	}
	
	public int getSkillReq(int id) {
		return skillReq[id];
	}
	
	public boolean getQuestReq(int id) {
		return questReq[id];
	}

	public double getWeight() {
		return weight;
	}

	public String getEquipmentType() {
		return equipmentType;
	}

	public int getSlot() {
		return getSlot;
	}
	
	public int getHideType() {
		return hideType;
	}
	
	public int getQpReq() {
		return qpReq;
	}

	public boolean isTwoHanded() {
		return twoHanded;
	}

	public boolean isUntradable() {
		boolean untrade = untradable;
		if (noted && parentId != -1 && parentId != id) {
			untrade = forId(parentId).isUntradable();
		}
		return untrade;
	}

	public int getSlot(String equipmentType) {
		if (equipmentType.equals("HAT")) {
			return Constants.HAT;
		}
		if (equipmentType.equals("CAPE")) {
			return Constants.CAPE;
		}
		if (equipmentType.equals("AMULET")) {
			return Constants.AMULET;
		}
		if (equipmentType.equals("WEAPON")) {
			return Constants.WEAPON;
		}
		if (equipmentType.equals("BODY")) {
			return Constants.CHEST;
		}
		if (equipmentType.equals("SHIELD")) {
			return Constants.SHIELD;
		}
		if (equipmentType.equals("LEGS")) {
			return Constants.LEGS;
		}
		if (equipmentType.equals("GLOVES")) {
			return Constants.HANDS;
		}
		if (equipmentType.equals("BOOTS")) {
			return Constants.FEET;
		}
		if (equipmentType.equals("RING")) {
			return Constants.RING;
		}
		if (equipmentType.equals("ARROWS")) {
			return Constants.ARROWS;
		}
		return -1;
	}

	public static int getItemId(String name) {
		name = name.toLowerCase();
		for (ItemDefinition def : definitions) {
			if (def.getName().toLowerCase().equalsIgnoreCase(name)) {
				return def.getId();
			}
		}
		return 0;
	}
	
}
