package com.rs2.model.region.music;

import com.rs2.util.Area;
import com.rs2.util.FileOperations;
import com.rs2.util.Stream;

public class MusicArea {
	
	private int id;
	private int type;
	private int priority;
	private int areaMusic;
	private Area musicArea;
	private int regions[];
	
	public static int AREA_COUNT = 0;
	
	private static MusicArea[] definitions = new MusicArea[0];
	
	public static void loadMusicAreas(){
		byte abyte2[] = FileOperations.ReadFile("./data/areas/Music.dat");
		Stream stream2 = new Stream(abyte2);
		AREA_COUNT = stream2.readUnsignedWord();
		definitions = new MusicArea[AREA_COUNT];
		for(int id = 0; id < AREA_COUNT; id++){
			int type = 0;
			int priority = 0;
			int music = 0;
			int x1 = 0;
			int y1 = 0;
			int x2 = 0;
			int y2 = 0;
			int regions[] = null;
			Area area = null;
			type = stream2.readUnsignedByte();
			priority = stream2.readUnsignedByte();
			music = stream2.readUnsignedWord();
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
			definitions[id] = new MusicArea(id, type, priority, music, area, regions);
		}
	}
	
	private MusicArea(int id, int type, int priority, int music, Area area, int[] regions){
		this.id = id;
		this.type = type;
		this.priority = priority;
		this.areaMusic = music;
		this.musicArea = area;
		this.regions = regions;
	}
	
	public int getId() {
		return id;
	}
	
	public int getType() {
		return type;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public int getMusic() {
		return areaMusic;
	}
	
	public Area getArea() {
		return musicArea;
	}
	
	public int[] getRegions() {
		return regions;
	}
	
	public static MusicArea forId(int id) {
		if (id < 0) {
			id = 1;
		}
		MusicArea def = definitions[id];
		if (def == null) {
			def = new MusicArea(id, 0, 0, 0, null, null);
		}
		return def;
	}
	
}
