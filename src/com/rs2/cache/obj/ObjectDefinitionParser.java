package com.rs2.cache.obj;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.rs2.cache.Archive;
import com.rs2.cache.Cache;
import com.rs2.cache.index.impl.StandardIndex;
import com.rs2.cache.object.GameObjectData;
import com.rs2.cache.util.ByteBufferUtils;
import com.rs2.util.Stream;

/**
 * A class which parses object definitions in the game cache.
 * 
 * @author Graham Edgecombe
 * 
 */
public class ObjectDefinitionParser {

	/**
	 * The cache.
	 */
	private Cache cache;

	/**
	 * The index.
	 */
	private StandardIndex[] indices;

	/**
	 * The listener.
	 */
	private ObjectDefinitionListener listener;

    private boolean boolean64 = true;

	/**
	 * Creates the object definition parser.
	 * 
	 * @param cache
	 *            The cache.
	 * @param indices
	 *            The indices in the cache.
	 * @param listener
	 *            The object definition listener.
	 */
	public ObjectDefinitionParser(Cache cache, StandardIndex[] indices, ObjectDefinitionListener listener) {
		this.cache = cache;
		this.indices = indices;
		this.listener = listener;
	}

	/**
	 * Parses the object definitions in the cache.
	 * 
	 * @throws java.io.IOException
	 *             if an I/O error occurs.
	 */
	public void parse() throws IOException {
		ByteBuffer buf = new Archive(cache.getFile(0, 2)).getFileAsByteBuffer("loc.dat");

		for (StandardIndex index : indices) {
			int id = index.getIdentifier();
			int offset = index.getFile(); // bad naming, should be getOffset()
			buf.position(offset);

			// TODO read the object definition now

			String name = "null";
			String desc = "null";
			int sizeX = 1;
			int sizeY = 1;
			boolean isSolid = true;
			boolean isWalkable = true;
			boolean hasActions = false;

			outer_loop : do {
				int configCode;
				do {
					configCode = buf.get() & 0xFF;
					if (configCode == 0) {
						break outer_loop;
					}
					switch (configCode) {
						case 1 :
							int someCounter = buf.get() & 0xFF;
							for (int i = 0; i < someCounter; i++) {
								buf.getShort();
								buf.get();
							}
							break;
						case 2 :
							name = ByteBufferUtils.getString(buf);
							break;
						case 3 :
							desc = ByteBufferUtils.getString(buf);
							break;
						case 5 :
							someCounter = buf.get() & 0xFF;
							for (int i = 0; i < someCounter; i++) {
								buf.getShort();
							}
							break;
						case 14 :
							sizeX = buf.get() & 0xFF;
							break;
						case 15 :
							sizeY = buf.get() & 0xFF;
							break;
						case 17 :
							isSolid = false;
							break;
						case 18 :
							isWalkable = false;
							break;
						case 19 :
							// has actions?
							if (buf.get() == 1) {
								hasActions = true;
							}
							break;
						case 21 :
							// some boolean
							break;
						case 22 :
							// some boolean
							break;
						case 23 :
							// some boolean
							break;
						case 24 :
							buf.getShort();
							break;
						case 28 :
							buf.get();
							break;
						case 29 :
							buf.get();
							break;
						case 39 :
							buf.get();
							break;
						case 30 :
						case 31 :
						case 32 :
						case 33 :
						case 34 :
						case 35 :
						case 36 :
						case 37 :
						case 38 :
							ByteBufferUtils.getString(buf); // actions
							break;
						case 40 :
							someCounter = buf.get() & 0xFF; // model colours
							for (int i = 0; i < someCounter; i++) {
								buf.getShort();
								buf.getShort();
							}
							break;
						case 60 :
							buf.getShort();
							break;
						case 62 :
							break;
						case 64 :
                            boolean64 = false;
							break;
						case 65 :
							buf.getShort();
							break;
						case 66 :
							buf.getShort();
							break;
						case 67 :
							buf.getShort();
							break;
						case 68 :
							buf.getShort();
							break;
						case 69 :
							buf.get();
							break;
						case 70 :
							buf.getShort();
							break;
						case 71 :
							buf.getShort();
							break;
						case 72 :
							buf.getShort();
							break;
						case 73 :
							break;
						case 74 :
							break;
						case 75 :
							break;
						default :
							buf.get();
							break;
					}
				} while (configCode != 77);

				buf.getShort();
				buf.getShort();

				int counter = buf.get();
				for (int i = 0; i <= counter; i++) {
					buf.getShort();
				}
			} while (true);

			listener.objectDefinitionParsed(new GameObjectData(id, name, desc, sizeX, sizeY, isSolid, isWalkable, hasActions, boolean64, 2));
            
		}
	}
	
	public static void loadLocDat()
	{
		Cache cache = Cache.getSingleton();
		Stream stream2 = null;
		try {
			stream2 = new Stream(new Archive(cache.getFile(0, 2)).getFile("loc.dat"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int totalObjects = cache.getIndexTable().getObjectDefinitionIndices().length;
		GameObjectData.definitions = new GameObjectData[totalObjects];
		stream2.currentOffset = 2;
		for(int k2 = 0; k2 < totalObjects; k2++){
			readValues(stream2, k2);
		}
	}
	
	/*private static void readValues(Stream stream, int id)
    {
		final GameObjectData def = GameObjectData.forId(id);
        int i = -1;
        int walkType = 2;
        def.name = null;
        def.description = null;
        def.yLength = 1;
        def.xLength = 1;
        def.isSolid = false;
        def.isWalkable = false;
        def.hasActions = false;
        def.boolean64 = true;
label0:
        do
        {
            int j;
            do
            {
                j = stream.readUnsignedByte();
                if(j == 0){
                	if (def.name == null)
                        def.name = "";
                	 def.rangeAble = walkType <= 1 || (walkType == 2 && !def.isSolid);
                     def.canShootThru = def.setRangeAble();
                    break label0;
                }
                if(j == 1)
                {
                    int k = stream.readUnsignedByte();
                    if(k > 0){
                            for(int k1 = 0; k1 < k; k1++)
                            {
                            	stream.readUnsignedWord();
                            	stream.readUnsignedByte();
                            }
                    }
                } else
                if(j == 2){
                	def.name = stream.readString();
                }else
                if(j == 3){
                	def.description = new String(stream.readBytes());
                }else
                if(j == 5)
                {
                    int l = stream.readUnsignedByte();
                    if(l > 0){
                            for(int l1 = 0; l1 < l; l1++){
                            	//def.anIntArray773[l1] = stream.readUnsignedWord();
                            	stream.readUnsignedWord();
                            }
                    }
                } else
                if(j == 14)
                	def.xLength = stream.readUnsignedByte();
                else
                if(j == 15)
                	def.yLength = stream.readUnsignedByte();
                else
                if(j == 17){
                	def.isWalkable = true;
                }else
                if(j == 18){
                	//def.aBoolean757 = false;
                }else
                if(j == 19)
                {
                	def.hasActions = stream.readUnsignedByte() == 1;
                } else
                if(j == 21){
                	//def.isSolid = true;
                    //walkType = 0;
                }else
                if(j == 22){
                	//def.aBoolean769 = true;
                }else
                if(j == 23){
                	//def.aBoolean764 = true;
                }else
                if(j == 24)
                {
                	stream.readUnsignedWord();
                } else
                if(j == 28){
                	//def.anInt775 = stream.readUnsignedByte();
                	stream.readUnsignedByte();
                }else
                if(j == 29){
                	// better (fences, gates)
                    stream.readSignedByte();
                }else
                if(j == 39)
                	stream.readSignedByte();
                else
                if(j >= 30 && j < 39)
                {
                	stream.readString();
                	def.hasActions = true;
                } else
                if(j == 40)
                {
                	int i1 = stream.readUnsignedByte();
                    for (int i2 = 0; i2 < i1; i2++) {
                    	stream.readUnsignedWord();
                    	stream.readUnsignedWord();
                    }

                } else
                if(j == 60){
                	//def.anInt746 = stream.readUnsignedWord();
                	stream.readUnsignedWord();
                }else
                if(j == 62){
                	//def.aBoolean751 = true;
                }else
                if(j == 64){
                	//def.aBoolean779 = true;
                }else
                if(j == 65)
                	stream.readUnsignedWord();
                else
                if(j == 66)
                	stream.readUnsignedWord();
                else
                if(j == 67)
                	stream.readUnsignedWord();
                else
                if(j == 68){
                	//def.anInt758 = stream.readUnsignedWord();
                	stream.readUnsignedWord();
                }else
                if(j == 69){
                	//def.anInt768 = stream.readUnsignedByte();
                	stream.readUnsignedByte();
                }else
                if(j == 70)
                	stream.readSignedWord();
                else
                if(j == 71)
                	stream.readSignedWord();
                else
                if(j == 72)
                	stream.readSignedWord();
                else
                if(j == 73){
                	//def.aBoolean736 = true;
                }else
                if(j == 74)
                {
                	//def.aBoolean766 = true;
                	def.isSolid = true;
                    walkType = 0;
                } else
                {
                    if(j != 75)
                        continue;
                    stream.readUnsignedByte();
                }
                continue label0;
            } while(j != 77);
            stream.readUnsignedWord();
            stream.readUnsignedWord();
            int j1 = stream.readUnsignedByte();
            for(int j2 = 0; j2 <= j1; j2++)
            {
            	stream.readUnsignedWord();
            }
            
            if(def.isSolid)
            {
            	def.isWalkable = true;
            }

        } while(true);
    }*/
	
	private static void readValues(Stream stream, int id)
    {
		final GameObjectData def = GameObjectData.forId(id);
        int i = -1;
        int walkType = 2;
        def.name = null;
        def.description = null;
        def.yLength = 1;
        def.xLength = 1;
        def.isSolid = true;
        def.isWalkable = true;
        def.hasActions = false;
        def.boolean64 = true;
label0:
        do
        {
            int j;
            do
            {
                j = stream.readUnsignedByte();
                if(j == 0){
                	if (def.name == null)
                        def.name = "";
                	 def.rangeAble = walkType <= 1 || (walkType == 2 && !def.isSolid);
                     def.canShootThru = def.setRangeAble();
                    break label0;
                }
                if(j == 1)
                {
                    int k = stream.readUnsignedByte();
                    if(k > 0){
                            for(int k1 = 0; k1 < k; k1++)
                            {
                            	stream.readUnsignedWord();
                            	stream.readUnsignedByte();
                            }
                    }
                } else
                if(j == 2){
                	def.name = stream.readString();
                }else
                if(j == 3){
                	def.description = new String(stream.readBytes());
                }else
                if(j == 5)
                {
                    int l = stream.readUnsignedByte();
                    if(l > 0){
                            for(int l1 = 0; l1 < l; l1++){
                            	//def.anIntArray773[l1] = stream.readUnsignedWord();
                            	stream.readUnsignedWord();
                            }
                    }
                } else
                if(j == 14)
                	def.xLength = stream.readUnsignedByte();
                else
                if(j == 15)
                	def.yLength = stream.readUnsignedByte();
                else
                if(j == 17){
                	def.isSolid = false;
                    walkType = 0;
                }else
                if(j == 18)
                	def.isWalkable = false;
                else
                if(j == 19)
                {
                	def.hasActions = stream.readUnsignedByte() == 1;
                } else
                if(j == 21){
                	//def.aBoolean762 = true;
                }else
                if(j == 22){
                	//fences?
                }else
                if(j == 23){
                	//def.aBoolean764 = true;
                }else
                if(j == 24)
                {
                	stream.readUnsignedWord();
                } else
                if(j == 28){
                	//def.anInt775 = stream.readUnsignedByte();
                	stream.readUnsignedByte();
                }else
                if(j == 29){
                	// better (fences, gates)
                    stream.readSignedByte();
                }else
                if(j == 39)
                	stream.readSignedByte();
                else
                if(j >= 30 && j < 39)
                {
                	stream.readString();
                	def.hasActions = true;
                } else
                if(j == 40)
                {
                	int i1 = stream.readUnsignedByte();
                    for (int i2 = 0; i2 < i1; i2++) {
                    	stream.readUnsignedWord();
                    	stream.readUnsignedWord();
                    }

                } else
                if(j == 60){
                	//def.anInt746 = stream.readUnsignedWord();
                	stream.readUnsignedWord();
                }else
                if(j == 62){
                }else
                if(j == 64)
                	def.boolean64 = false;
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
                if(j == 68){
                	//def.anInt758 = stream.readUnsignedWord();
                	stream.readUnsignedWord();
                }else
                if(j == 69){
                	//def.anInt768 = stream.readUnsignedByte();
                	stream.readUnsignedByte();
                }else
                if(j == 70)
                	stream.readSignedWord();
                else
                if(j == 71)
                	stream.readSignedWord();
                else
                if(j == 72)
                	stream.readSignedWord();
                else
                if(j == 73){
                	//def.aBoolean736 = true;
                }else
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
            stream.readUnsignedWord();
            stream.readUnsignedWord();
            int j1 = stream.readUnsignedByte();
            for(int j2 = 0; j2 <= j1; j2++)
            {
            	stream.readUnsignedWord();
            }

        } while(true);
    }
	
}
