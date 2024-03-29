package com.rs2.cache.interfaces;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.rs2.cache.Archive;
import com.rs2.cache.Cache;
import com.rs2.net.StreamBuffer;
import com.rs2.util.FileOperations;

/**
 */
public class RSInterface {

    private static int SIZE = 0;
    private static RSInterface[] INTERFACES;

    private int id, parentId;
    @SuppressWarnings("unused")
	private byte type;
    private boolean sideBarInterface;

    public RSInterface(int id, byte type, int parentId, boolean sideBarInterface) {
        this.id = id;
        this.type = type;
        this.parentId = parentId;
        this.sideBarInterface = sideBarInterface;
    }
    
    public int getParentId() {
        return parentId;
    }

    public boolean isSideBarInterface() {
        return sideBarInterface;
    }

    public int getId() {
        return id;
    }

    public static RSInterface forId(int id) {
        if (id < 0 || id >= SIZE)
            return null;
        return INTERFACES[id];
    }
    
    public static void load() {
        try {
            Archive interfacesArchive = new Archive(Cache.getSingleton().getFile(0, 3));
            StreamBuffer.InBuffer in = StreamBuffer.newInBuffer(interfacesArchive.getFileAsByteBuffer("data"));
            //StreamBuffer.InBuffer in = StreamBuffer.newInBuffer(ByteBuffer.wrap(FileOperations.ReadFile("./data/default 377 interfaces.dat")));
            
            //amount of interfaces
            SIZE = in.readShort();
            INTERFACES = new RSInterface[SIZE];
            System.out.println("RSInterface size: "+SIZE);
            int parentId = -1;

            
            while (in.getBuffer().hasRemaining()) {
                
                int interfaceId = in.readShort();

                if (interfaceId == 65535) {
                    parentId = in.readShort();
                    interfaceId = in.readShort();
                }
                
                int type = in.readByte();
                int atActionType = in.readByte();
                @SuppressWarnings("unused")
				int int214 = in.readShort();
                int width = in.readShort();
                int height = in.readShort();
                @SuppressWarnings("unused")
				int byte254 = in.readByte();
                int int230 = in.readByte();
                boolean sideBarInterface = width == 512 && height == 334;

                if (interfaceId < SIZE && interfaceId > 0)
                    INTERFACES[interfaceId] = new RSInterface(interfaceId, (byte)type, parentId, sideBarInterface);



                if (int230 != 0)
                    in.readByte();
                int i1 = in.readByte();
                if (i1 > 0) 
                    in.readBytes(i1*3);
                int k1 = in.readByte();
                if (k1 > 0) {
                    for (int i = 0; i < k1; i++) {
                        int i3 = in.readShort();
                        in.readBytes(i3*2);
                    }
                }

                if (type == 0) {
                    @SuppressWarnings("unused")
					int scrollMax = in.readShort();
                    @SuppressWarnings("unused")
					boolean b = in.readByte() == 1;
                    int i2 = in.readShort();
                    in.readBytes(i2*6);
                }
                if (type == 1)
                    in.readBytes(3);
                if (type == 2) {
                    in.readBytes(6);
                    for (int i = 0; i < 20; i++) {
                        int k3 = in.readByte();
                        if (k3 == 1) {
                            in.readBytes(4);
                            in.readString();
                        }
                    }
                    for (int i = 0; i < 5; i++) 
                        in.readString();
                }
                if (type == 3)
                    in.readByte();
                if (type == 4 || type == 1)
                    in.readBytes(3);
                if (type == 4) {
                    in.readString();
                    in.readString();
                }
                if (type == 1 || type == 3 || type == 4) {
                    in.readInt();
                    if (type != 1)
                        in.readBytes(12);
                }
                if (type == 5 || type == 17) {
                    in.readString();
                    in.readString();
                    if(type == 17){
                    	in.readString();
                        in.readString();
                    }
                }
                if (type == 6) {
                    int l = in.readByte();
                    if (l != 0)
                        in.readByte();
                    l = in.readByte();
                    if (l != 0)
                        in.readByte();
                    l = in.readByte();
                    if (l != 0)
                        in.readByte();
                    l = in.readByte();
                    if (l != 0)
                        in.readByte();
                    in.readBytes(6);
                }
                if (type == 7) {
                    in.readBytes(12);
                    for (int i = 0; i < 5; i++)
                        in.readString();
                }
                if (atActionType == 2 || type == 2) {
                    in.readString();
                    in.readString();
                    in.readShort();
                }
                if (type == 8)
                    in.readString();
                if (atActionType == 1 || atActionType == 4 || atActionType == 5 || atActionType == 6)
                    in.readString();

            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    /*public static void load() {
        try {
            Archive interfacesArchive = new Archive(Cache.getSingleton().getFile(0, 3));
            StreamBuffer.InBuffer in = StreamBuffer.newInBuffer(interfacesArchive.getFileAsByteBuffer("data"));
            
            //amount of interfaces
            int amt = in.readShort();
            System.out.println("RSInterface size: "+amt);
            int parentId = -1;

            
            while (in.getBuffer().hasRemaining()) {
                
                int interfaceId = in.readShort();

                if (interfaceId == 65535) {
                    parentId = in.readShort();
                    interfaceId = in.readShort();
                }
                
                int type = in.readByte();
                int atActionType = in.readByte();
				int int214 = in.readShort();
                int width = in.readShort();
                int height = in.readShort();
				int byte254 = in.readByte();
                int int230 = in.readByte();
                boolean sideBarInterface = width == 512 && height == 334;

                if (interfaceId < SIZE && interfaceId > 0)
                    INTERFACES[interfaceId] = new RSInterface(interfaceId, (byte)type, parentId, sideBarInterface);



                if (int230 != 0)
                    in.readByte();
                int i1 = in.readByte();
                int k1;
                if(i1 > 0) {
                    for(k1 = 0; k1 < i1; ++k1) {
                    	in.readByte();
                    	in.readShort();
                    }
                 }
                k1 = in.readByte();
                int k4;
                if (k1 > 0) {
                    for (int i = 0; i < k1; i++) {
                        int i3 = in.readShort();
                        for(int s1 = 0; s1 < i3; ++s1) {
                        	in.readShort();
                         }
                    }
                }

                if (type == 0) {
					int scrollMax = in.readShort();
					boolean b = in.readByte() == 1;
                    int i2 = in.readShort();
                    for(k4 = 0; k4 < i2; ++k4) {
                    	in.readShort();
                    	in.readShort();
                    	in.readShort();
                     }
                }
                if (type == 1){
                	in.readShort();
                	in.readByte();
                }
                if (type == 2) {
                	in.readByte();
                	in.readByte();
                	in.readByte();
                	in.readByte();
                	in.readByte();
                	in.readByte();
                    for (int i = 0; i < 20; i++) {
                        int k3 = in.readByte();
                        if (k3 == 1) {
                        	in.readShort();
                        	in.readShort();
                            in.readString();
                        }
                    }
                    for (int i = 0; i < 5; i++) 
                        in.readString();
                }
                if (type == 3)
                    in.readByte();
                if (type == 4 || type == 1)
                	in.readByte();
                	in.readByte();
                	in.readByte();
                if (type == 4) {
                    in.readString();
                    in.readString();
                }
                if (type == 1 || type == 3 || type == 4) {
                    in.readInt();
                }
                if (type == 3 || type == 4) {
                    in.readInt();
                    in.readInt();
                    in.readInt();
                }
                if (type == 5) {
                    in.readString();
                    in.readString();
                }
                if (type == 6) {
                    int l = in.readByte();
                    if (l != 0)
                        in.readByte();
                    l = in.readByte();
                    if (l != 0)
                        in.readByte();
                    l = in.readByte();
                    if (l != 0)
                        in.readByte();
                    l = in.readByte();
                    if (l != 0)
                        in.readByte();
                    in.readShort();
                    in.readShort();
                    in.readShort();
                }
                if (type == 7) {
                	in.readByte();
                	in.readByte();
                	in.readByte();
                	in.readInt();
                	in.readShort();
                	in.readShort();
                	in.readByte();
                    for (int i = 0; i < 5; i++)
                        in.readString();
                }
                if (atActionType == 2 || type == 2) {
                    in.readString();
                    in.readString();
                    in.readShort();
                }
                if (type == 8)
                    in.readString();
                if (atActionType == 1 || atActionType == 4 || atActionType == 5 || atActionType == 6)
                    in.readString();

            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }*/
    
}
