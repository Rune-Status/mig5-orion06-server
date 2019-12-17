package com.rs2.launcher;

import java.io.*;
import java.util.*;

import com.rs2.util.FileOperations;
import com.rs2.util.Stream;

public class MapIndex {
	
	private int maxRegions = 6000;
	
	public int regionCount;

	public void unpack(){
		byte abyte2[] = FileOperations.ReadFile("./data/launcher/map_index.dat");
		Stream stream2 = new Stream(abyte2);
		regionCount = stream2.readUnsignedWord();
		for(int i2 = 0; i2 < regionCount; i2++){
			regionId[i2] = stream2.readUnsignedWord();
			floorMap[i2] = stream2.readUnsignedWord();
			objectMap[i2] = stream2.readUnsignedWord();
		}
	}

	public int regionId[] = new int[maxRegions];
	public int floorMap[] = new int[maxRegions];
	public int objectMap[] = new int[maxRegions];
	
}
