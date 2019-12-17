package com.rs2.model.region;

import com.rs2.util.Area;
import com.rs2.util.FileOperations;
import com.rs2.util.Stream;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 17/04/12 Time: 23:55 To change
 * this template use File | Settings | File Templates.
 */
public class MultiCombatArea {
	
	private int id;
	private int type;
	private Area multiArea;
	private int regions[];
	
	public static int AREA_COUNT = 0;
	
	public static MultiCombatArea[] definitions = new MultiCombatArea[0];
	
	public static void loadMultiCombatAreas(){
		byte abyte2[] = FileOperations.ReadFile("./data/areas/Multiway.dat");
		Stream stream2 = new Stream(abyte2);
		AREA_COUNT = stream2.readUnsignedWord();
		definitions = new MultiCombatArea[AREA_COUNT];
		for(int id = 0; id < AREA_COUNT; id++){
			int type = 0;
			int x1 = 0;
			int y1 = 0;
			int x2 = 0;
			int y2 = 0;
			int regions[] = null;
			Area area = null;
			type = stream2.readUnsignedByte();
			if(type == 0){
				x1 = stream2.readUnsignedWord();
				y1 = stream2.readUnsignedWord();
				x2 = stream2.readUnsignedWord();
				y2 = stream2.readUnsignedWord();
				area = new Area(x1, y1, x2, y2, (byte)0);
			}else{
				regions = new int[type];
				for(int i52 = 0; i52 < type; i52++)
					regions[i52] = stream2.readUnsignedWord();
			}
			definitions[id] = new MultiCombatArea(id, type, area, regions);
		}
	}
	
	private MultiCombatArea(int id, int type, Area area, int[] regions){
		this.id = id;
		this.type = type;
		this.multiArea = area;
		this.regions = regions;
	}
	
	public int getId() {
		return id;
	}
	
	public Area getArea() {
		return multiArea;
	}
	
	public int getType() {
		return type;
	}
	
	public int[] getRegions() {
		return regions;
	}
	
	public static MultiCombatArea forId(int id) {
		if (id < 0) {
			id = 1;
		}
		MultiCombatArea def = definitions[id];
		if (def == null) {
			def = new MultiCombatArea(id, 0, null, null);
		}
		return def;
	}
	
}
