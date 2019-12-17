package com.rs2.model.region.music;

import com.rs2.model.Position;
import com.rs2.model.players.Player;
import com.rs2.util.Area;
import com.rs2.util.FileOperations;
import com.rs2.util.Misc;
import com.rs2.util.Stream;

public class RegionMusic {
	
	public void playMusic(Player player) {
		int cmusic = getMusicId2(player);
		if(cmusic != -1){
			if(cmusic == player.currentMusic)
				return;
			player.currentMusic = cmusic;
			Music songDef = Music.forId(cmusic);
			if(songDef.getButton() != -1)
				player.getActionSender().playMusic(songDef);
		}
	}
	
	public int selectedArea = -1;
	
	public int music = -1;
	public int currentMusic = -1;
	
	boolean insideArea(int areaId, int pX, int pY){
		int region_ = Misc.coords2Region(pX, pY);
		MusicArea currentArea = MusicArea.forId(areaId);
		if(currentArea.getType() == 0){
			if(currentArea.getArea().containsBorderIncluded(new Position(pX, pY, 0)))
				return true;
		}else{
			int[] tempArray = currentArea.getRegions();
			for(int i53 = 0; i53 < currentArea.getType(); i53++)
				if(region_ == tempArray[i53])
					return true;
		}
		return false;
	}
	
	int getRealArea(int areas[], int length){
		int prio = 0;
		int area = -1;
		for(int i = 0; i < length; i++){
			MusicArea currentArea = MusicArea.forId(areas[i]);
			if(i == 0){
				prio = currentArea.getPriority();
				area = currentArea.getId();
			}else{
				if(prio < currentArea.getPriority()){
					prio = currentArea.getPriority();
					area = currentArea.getId();
				}
			}
		}
		return area;
	}
	
	private int getMusicId2(Player player) {
		int x = player.getPosition().getX(), y = player.getPosition().getY();
		int insideAreas[] = new int[5];
		int crun = 0;
		for(int i = 0; i < MusicArea.AREA_COUNT; i++){
			if(i == 0)
				crun = 0;
			if(insideArea(i,x,y)) {
				insideAreas[crun] = i;
				crun++;
			}
		}
		selectedArea = getRealArea(insideAreas, crun);//area priority handling
		if(selectedArea != -1){
			MusicArea currentArea = MusicArea.forId(selectedArea);
			int music2 = currentArea.getMusic();
			if(player.autoplayMusic){
				music = music2;
			}
			Music songDef = Music.forId(music2);
			if(songDef.getConfig() != -1){
				int value = player.configValue[songDef.getConfig()];
				int id = songDef.getConfigValue();
				if((value & id) == 0){
					unlockNewTrack(player, songDef);
					music = music2;
				}
			}
			return music;
		}
		return -1;
	}
	
	public static void unlockTrack(Player player, int music){//used to unlock some songs in the beginning.. (themes etc. which cant be unlocked ingame)
		Music songDef = Music.forId(music);
		int value = player.configValue[songDef.getConfig()];
		int id = songDef.getConfigValue();
		if((value & id) == 0){
			int config = songDef.getConfig();
			player.configValue[config] += id;
			player.getActionSender().sendConfig(config, player.configValue[config]);
		}
	}
	
	public void unlockNewTrack(Player player, Music songDef){
		player.autoplayMusic = true;
		int config = songDef.getConfig();
		int value = songDef.getConfigValue();
		player.configValue[config] += value;
		player.getActionSender().sendConfig(config, player.configValue[config]);
		player.getActionSender().sendMessage("You have unlocked a new music track: "+songDef.getName());
	}
	
	public static boolean songUnlocked(Player player, int id){
		Music songDef = Music.forId(id);
		int value = player.configValue[songDef.getConfig()];
		int cid = songDef.getConfigValue();
		if((value & cid) == 0 || cid == -1){
			return false;
		}else{
			return true;
		}
	}
	
}