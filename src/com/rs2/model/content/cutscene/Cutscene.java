package com.rs2.model.content.cutscene;

import java.util.ArrayList;

import com.rs2.model.World;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.tick.Tick;

public class Cutscene {
	
	private ArrayList<Scene> scenes = new ArrayList<Scene>();
	private ArrayList<Npc> actors = new ArrayList<Npc>();
	private Player player;
	
	public Cutscene(ArrayList<Scene> scenes){
		this.scenes = scenes;
	}
	
	public Cutscene(){
	}
	
	public Cutscene(final Player player, ArrayList<Npc> npcs){
		this.player = player;
		if(npcs != null)
		for (Npc npc : npcs) {
			this.addActor(npc);
		}
		Scene sc0a = new Scene(4){
			public void playScene(){
				player.getActionSender().sendInterface(18679);
				setStartPositions();
			}
		};
		Scene sc0b = new Scene(1){
			public void playScene(){
				player.getActionSender().removeInterfaces();
				init();
			}
		};
		this.addScene(sc0a);
		this.addScene(sc0b);
		build();
	}
	
	public void addActor(Npc npc){
		actors.add(npc);
	}
	
	public void addScene(Scene scene){
		scenes.add(scene);
	}
	
	public Scene getScene(int i){
		return scenes.get(i);
	}
	
	public Npc getActor(int i){
		return actors.get(i);
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public void build(){
		
	}
	
	public void init(){
		
	}
	
	public void setStartPositions(){
		
	}
	
	public void begin(){
		player.inCutScene = true;
		player.getActionSender().sendMapState(2);
		player.getActionSender().sendInterface(8677);
		player.getActionSender().hideSideBars(new int[]{QuestConstants.ATTACK_TAB[0], QuestConstants.STATS_TAB[0], QuestConstants.QUEST_TAB[0], QuestConstants.INVENTORY_TAB[0], QuestConstants.EQUIPMENT_TAB[0],
				QuestConstants.PRAYER_TAB[0], QuestConstants.MAGIC_TAB[0], QuestConstants.OPTION_TAB[0], QuestConstants.EMOTE_TAB[0]});
		player.getFrozenTimer().setWaitDuration(this.getSceneDurationInMillis());//was: 5000, new: 7800
		if(actors != null)
		for (Npc npc : actors) {
			npc.getFrozenTimer().setWaitDuration(this.getSceneDurationInMillis());
		}
	}
	
	public void end(){
		player.inCutScene = false;
		player.getFrozenTimer().setWaitDuration(0);
    	player.getActionSender().resetCamera();
    	player.getDialogue().endDialogue();
    	player.getActionSender().sendMapState(0);
    	player.getActionSender().sendSideBarInterfaces();
	}
	
	public void start(){
		begin();
		int duration = 0;
		for (final Scene scene : scenes) {
			duration += scene.getDuration();
			final Tick timer = new Tick(duration) {
				@Override
	            public void execute() {
					scene.playScene();
					stop();
				}
			};
			World.getTickManager().submit(timer);
		}
		final Tick timer = new Tick(getSceneDuration()) {
			@Override
            public void execute() {
				end();
				stop();
			}
		};
		World.getTickManager().submit(timer);
	}
	
	public int getSceneDurationInMillis(){
		int duration = 0;
		for (final Scene scene : scenes) {
			duration += scene.getDuration();
		}
		return duration*600;
	}
	
	public int getSceneDuration(){
		int duration = 0;
		for (final Scene scene : scenes) {
			duration += scene.getDuration();
		}
		return duration;
	}
	
	public class Scene{
		
		private int duration;
		
		public Scene(int duration){
			this.duration = duration;
		}
		
		public int getDuration(){
			return this.duration;
		}
		
		public void playScene(){
		}
		
	}
	
}
