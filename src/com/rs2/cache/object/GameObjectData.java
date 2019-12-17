package com.rs2.cache.object;

import java.io.IOException;
import java.util.logging.Logger;

import com.rs2.util.clip.ObjectDef;


/**
 * Represents a single type of object.
 * 
 * @author Graham Edgecombe
 * 
 */
public class GameObjectData {

	/**
	 * The maximum number of object definitions
	 */
	public static final int MAX_DEFINITIONS = 25374;

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(GameObjectData.class.getName());

	public static GameObjectData[] definitions;

	/**
	 * Adds a definition. TODO better way?
	 * 
	 * @param def
	 *            The definition.
	 */
	public static void addDefinition(GameObjectData def) {
		definitions[def.getId()] = def;
	}
	
	public static GameObjectData forId(int id) {
		if (id < definitions.length) {
			if (definitions[id] == null)
				definitions[id] = produce(id);
            return definitions[id];
		}
        return null;
	}
    
    private static GameObjectData produce(int id) {
        return new GameObjectData(id, "Object: #" + id, "Its an object!", 1, 1, false, false, false, true, 2);
    }

	/**
	 * The id.
	 */
	private int id;

	/**
	 * The name.
	 */
	public String name;

	/**
	 * The description.
	 */
	public String description;

	/**
	 * X size.
	 */
	public int xLength;

	/**
	 * Y size.
	 */
	public int yLength;

	/**
	 * Solid flag.
	 */
	public boolean isSolid;

	/**
	 * Walkable flag.
	 */
	public boolean isWalkable;

	/**
	 * 'Has actions' flag.
	 */
	public boolean hasActions;

    public boolean boolean64;
    
    public boolean rangeAble;

    public boolean canShootThru;

	public GameObjectData(int id, String name, String desc, int sizeX, int sizeY, boolean isSolid, boolean isWalkable, boolean hasActions, boolean unknown, int walkType) {
		this.id = id;
		this.name = name;
        if (name == null)
            this.name = "";
		this.description = desc;
		this.xLength = sizeX;
		this.yLength = sizeY;
		this.isSolid = isSolid;
		this.isWalkable = isWalkable;
		this.hasActions = hasActions;
        this.boolean64 = unknown;
        this.rangeAble = walkType <= 1 || (walkType == 2 && !isSolid);
        this.canShootThru = setRangeAble();
	}

    public boolean unknown() {
        return boolean64;
    }

	/**
	 * Gets the id.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Gets the name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the description.
	 * 
	 * @return The description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the x size.
	 * 
	 * @return The x size.
	 */
	public int getSizeX() {
		return xLength;
	}

	/**
	 * Gets the y size.
	 * 
	 * @return The y size.
	 */
	public int getSizeY() {
		return yLength;
	}

	public int getSizeX(int rotation) {
		if (rotation == 1 || rotation == 3) {
			return yLength;
		} else {
			return xLength;
		}
	}

	public int getSizeY(int rotation) {
		if (rotation == 1 || rotation == 3) {
			return xLength;
		} else {
			return yLength;
		}
	}

	/**
	 * Checks if this object is solid.
	 * 
	 * @return The solid flag.
	 */
	public boolean isSolid() {
		return isSolid;
	}

	/**
	 * Checks if this object is walkable.
	 * 
	 * @return The walkable flag.
	 */
	public boolean isWalkable() {
		return isWalkable;
	}

	/**
	 * Checks if this object has any actions.
	 * 
	 * @return A flag indicating that this object has some actions.
	 */
	public boolean hasActions() {
		return hasActions;
	}

	public int getBiggestSize() {
		if (yLength > xLength) {
			return yLength;
		}
		return xLength;
	}
    
    public boolean isRangeAble() {
        return rangeAble;
    }

    public boolean canShootThru() {
        return canShootThru;
    }

    public boolean setRangeAble() {
    	if(id == 1116 || id == 1117)//witch house bushes
    		return false;
        int[] rangableObjects = {2637, 9563, 9565, 14462, 14464, 14465, 14466, 14467, 14468, 14470, 14502, 11754, 3007, 980, 997, 4262, 14437, 14438, 4437, 4439, 3487, 3457};
        for (int i : rangableObjects) {
            if (i == id) {
                return true;
            }
        }
        if (name != null) {
            final String name1 = name.toLowerCase();
            String[] rangables = {"fungus", "mushroom", "sarcophagus", "counter", "plant", "altar", "pew", "log", "stump", "stool", "sign", "cart", "chest", "rock", "bush", "hedge", "chair", "table", "crate", "barrel", "box", "skeleton", "corpse", "vent", "stone", "rockslide"};
            for (String i : rangables) {
                if (name1.contains(i)) {
                    return true;
                }
            }
        }
        return false;
    }

}
