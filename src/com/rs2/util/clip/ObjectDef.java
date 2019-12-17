package com.rs2.util.clip;

import java.io.IOException;

import com.rs2.cache.Archive;
import com.rs2.cache.Cache;
import com.rs2.util.FileOperations;
import com.rs2.util.Stream;

public final class ObjectDef {

    static {
        loadLocDat();
    }

    public static ObjectDef getObjectDef(int i) {
        if (i < 0) {
            return null;
        }
        for (int j = 0; j < 20; j++) {
            if (cacheo[j].type == i) {
                return cacheo[j];
            }
        }
        cacheIndex = (cacheIndex + 1) % 20;
        ObjectDef class46 = cacheo[cacheIndex];
        stream.currentOffset = streamIndices[i];
        class46.type = i;
        class46.setDefaults();
        class46.readValues(stream, i);
        return class46;
    }

    private void setDefaults() {
        anIntArray773 = null;
        anIntArray776 = null;
        name = null;
        description = null;
        modifiedModelColors = null;
        originalModelColors = null;
        yLength = 1;
        xLength = 1;
        isSolid = true;
        isWalkable = true;
        hasActions = false;
        aBoolean762 = false;
        aBoolean764 = false;
        anInt781 = -1;
        anInt775 = 16;
        actions = null;
        anInt746 = -1;
        anInt758 = -1;
        boolean64 = true;
        anInt768 = 0;
        aBoolean736 = false;
        anInt774 = -1;
        anInt749 = -1;
        childrenIDs = null;
    }

    private static Stream stream;
	private static int[] streamIndices;
    
    public static void loadLocDat()
	{
    	Cache cache = Cache.getSingleton();
		Stream stream2 = null;
		try {
			stream = new Stream(new Archive(cache.getFile(0, 2)).getFile("loc.dat"));
			stream2 = new Stream(new Archive(cache.getFile(0, 2)).getFile("loc.idx"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int totalObjects = stream2.readUnsignedWord();
		streamIndices = new int[totalObjects];
		cacheo = new ObjectDef[20];
		int i = 2;
		for(int j = 0; j < totalObjects; j++)
		{
			streamIndices[j] = i;
			i += stream2.readUnsignedWord();
		}
		for (int k = 0; k < 20; k++) {
            cacheo[k] = new ObjectDef();
        }
	}
    
    private void readValues(Stream stream, int id)
    {
        int i = -1;
label0:
        do
        {
            int j;
            do
            {
                j = stream.readUnsignedByte();
                if(j == 0)
                    break label0;
                if(j == 1)
                {
                    int k = stream.readUnsignedByte();
                    if(k > 0){
                        if(anIntArray773 == null)
                        {
                        	anIntArray776 = new int[k];
                            anIntArray773 = new int[k];
                            for(int k1 = 0; k1 < k; k1++)
                            {
                            	anIntArray773[k1] = stream.readUnsignedWord();
                            	anIntArray776[k1] = stream.readUnsignedByte();
                            }
					}
                        } else
                        {
                            stream.currentOffset += k * 3;
                        }
                } else
                if(j == 2)
                	name = stream.readString();
                else
                if(j == 3){
                	description = stream.readBytes();
                }else
                if(j == 5)
                {
                    int l = stream.readUnsignedByte();
                    if(l > 0){
                        if(anIntArray773 == null)
                        {
                        	anIntArray776 = null;
                            anIntArray773 = new int[l];
                            for(int l1 = 0; l1 < l; l1++)
                            	anIntArray773[l1] = stream.readUnsignedWord();
					}			

                        } else
                        {
                            stream.currentOffset += l * 2;
                        }
                } else
                if(j == 14)
                	xLength = stream.readUnsignedByte();
                else
                if(j == 15)
                	yLength = stream.readUnsignedByte();
                else
                if(j == 17){
                	isSolid = false;
                    walkType = 0;
                }else
                if(j == 18)
                	isWalkable = false;
                else
                if(j == 19)
                {
                	hasActions = stream.readUnsignedByte() == 1;
                } else
                if(j == 21)
                	aBoolean762 = true;
                else
                if(j == 22){
                	//fences?
                }else
                if(j == 23)
                	aBoolean764 = true;
                else
                if(j == 24)
                {
                	anInt781 = stream.readUnsignedWord();
                    if (anInt781 == 65535) {
                        anInt781 = -1;
                    }
                } else
                if(j == 28)
                	anInt775 = stream.readUnsignedByte();
                else
                if(j == 29){
                	// better (fences, gates)
                    stream.readSignedByte();
                }else
                if(j == 39)
                	stream.readSignedByte();
                else
                if(j >= 30 && j < 39)
                {
                	if (actions == null) {
                        actions = new String[5];
                    }
                    actions[j - 30] = stream.readString();
                    hasActions = true;
                    if (actions[j - 30].equalsIgnoreCase("hidden")) {
                        actions[j - 30] = null;
                    }
                } else
                if(j == 40)
                {
                	int i1 = stream.readUnsignedByte();
                    modifiedModelColors = new int[i1];
                    originalModelColors = new int[i1];
                    for (int i2 = 0; i2 < i1; i2++) {
                        modifiedModelColors[i2] = stream.readUnsignedWord();
                        originalModelColors[i2] = stream.readUnsignedWord();
                    }

                } else
                if(j == 60)
                	anInt746 = stream.readUnsignedWord();
                else
                if(j == 62){
                }else
                if(j == 64)
                	boolean64 = false;
                else
                if(j == 65)
                	stream.readUnsignedWord();
                else
                if(j == 66)
                	stream.readUnsignedWord();
                else
                if(j == 67)
                	stream.readUnsignedWord();
                else
                if(j == 68)
                	anInt758 = stream.readUnsignedWord();
                else
                if(j == 69)
                	anInt768 = stream.readUnsignedByte();
                else
                if(j == 70)
                	stream.readSignedWord();
                else
                if(j == 71)
                	stream.readSignedWord();
                else
                if(j == 72)
                	stream.readSignedWord();
                else
                if(j == 73)
                	aBoolean736 = true;
                else
                if(j == 74)
                {
                } else
                {
                    if(j != 75)
                        continue;
                    stream.readUnsignedByte();
                }
                continue label0;
            } while(j != 77);
            anInt774 = stream.readUnsignedWord();
            if(anInt774 == 65535)
            	anInt774 = -1;
            anInt749 = stream.readUnsignedWord();
            if(anInt749 == 65535)
            	anInt749 = -1;
            int j1 = stream.readUnsignedByte();
            childrenIDs = new int[j1+1];
            for(int j2 = 0; j2 <= j1; j2++)
            {
            	childrenIDs[j2] = stream.readUnsignedWord();
                if(childrenIDs[j2] == 65535)
                	childrenIDs[j2] = -1;
            }

        } while(true);
    }

    private ObjectDef() {
        type = -1;
    }

    public boolean hasActions() {
        return hasActions;
    }

    public boolean hasName() {
        return name != null && name.length() > 1;
    }

    public String getName() {
        return name != null ? name : "no-name";
    }

    public boolean solid() {
        return boolean64;
    }

    public int yLength() {
        return yLength;
    }

    public int xLength() {
        return xLength;
    }

    public boolean aBoolean767() {
        return isSolid;
    }

    public boolean aBoolean757() {
        return isWalkable;
    }

    public boolean rangableObject() {
        int[] rangableObjects = {11754, 3007, 980, 997, 4262, 14437, 14438, 4437, 4439, 3487, 3457};
        for (int i : rangableObjects) {
            if (i == type) {
                return true;
            }
        }
        if (name != null) {
            final String name1 = name.toLowerCase();
            String[] rangables = {"gate", "fungus", "mushroom", "sarcophagus", "counter", "plant", "altar", "pew", "log", "stump", "stool", "sign", "cart", "chest", "rock", "bush", "hedge", "chair", "table", "crate", "barrel", "box", "skeleton", "corpse", "vent", "stone", "rockslide"};
            for (String i : rangables) {
                if (name1.contains(i)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean aBoolean736;
    public String name;
    public int yLength;
    public int anInt746;
    private int[] originalModelColors;
    public int anInt749;
    public static boolean lowMem;
    public int type;
    public boolean isWalkable;
    public int anInt758;
    public int childrenIDs[];
    public int xLength;
    public boolean aBoolean762;
    public boolean aBoolean764;
    public boolean isSolid;
    public int anInt768;
    private static int cacheIndex;
    private int[] anIntArray773;
    public int anInt774;
    public int anInt775;
    private int[] anIntArray776;
    public byte description[];
    public boolean hasActions;
    public boolean boolean64;
    public int anInt781;
    private static ObjectDef[] cacheo;
    private int[] modifiedModelColors;
    public String actions[];
    private static MemoryArchive archive;
    public int walkType = 2;

}