package com.rs2.model.region.music;

import com.rs2.util.FileOperations;
import com.rs2.util.Stream;

public class Music {
	
	private int id;
	private String name;
	private int button;
	private int config;
	private int configValue;
	
	public static int SONG_COUNT = 0;
	
	private static Music[] definitions = new Music[0];
	
	public static void loadSongs(){
		byte abyte2[] = FileOperations.ReadFile("./data/Songs.dat");
		Stream stream2 = new Stream(abyte2);
		SONG_COUNT = stream2.readUnsignedWord();
		definitions = new Music[SONG_COUNT];
		String name;
		int config;
		int configValue;
		int button;
		for(int id = 0; id < SONG_COUNT; id++){
			int k2 = stream2.readUnsignedByte();
			if(k2 == 0){
				definitions[id] = new Music(id, " ", -1, -1, -1);
			}
			if(k2 == 1){
				name = stream2.readString();
				definitions[id] = new Music(id, name, -1, -1, -1);
			}
			if(k2 == 2){
				name = stream2.readString();
				config = stream2.readUnsignedWord();
				int j65 = stream2.readUnsignedByte();
				configValue = (int)Math.pow(2,(j65-1));
				if(j65 == (31+1))
					configValue = Integer.MIN_VALUE;
				button = stream2.readUnsignedWord();
				definitions[id] = new Music(id, name, button, config, configValue);
			}
			if(k2 == 3){
				name = stream2.readString();
				button = stream2.readUnsignedWord();
				definitions[id] = new Music(id, name, button, -1, -1);
			}
			if(k2 == 4){
				name = stream2.readString();
				config = stream2.readUnsignedWord();
				int j65 = stream2.readUnsignedByte();
				configValue = (int)Math.pow(2,(j65-1));
				if(j65 == (31+1))
					configValue = Integer.MIN_VALUE;
				definitions[id] = new Music(id, name, -1, config, configValue);
			}
		}
	}
	
	private Music(int id, String name, int button, int config, int configValue){
		this.id = id;
		this.name = name;
		this.button = button;
		this.config = config;
		this.configValue = configValue;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getButton() {
		return button;
	}
	
	public int getConfig() {
		return config;
	}
	
	public int getConfigValue() {
		return configValue;
	}
	
	public static Music forId(int id) {
		if (id < 0) {
			id = 1;
		}
		Music def = definitions[id];
		if (def == null) {
			def = new Music(id, " ", -1, -1, -1);
		}
		return def;
	}
	
}
