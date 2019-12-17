package com.rs2.model.content.minigames.fightcaves;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;

/**
 * Created by IntelliJ IDEA.
 * User: vayken
 * Date: 5/18/12
 * Time: 9:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class FightCavesMainData {

    @SuppressWarnings("unused")
	private Player player;

    public FightCavesMainData(Player player){
        this.player = player;
    }
    
    Position outside = new Position(2438, 5168, 0);

    @SuppressWarnings("unused")
	private int currentWave;

    
    public void handleDeath(boolean forfeit) {
		player.resetEffects();
		endFightCaveMinigame();
	}

	public void handleLogin() {
		if (player.inCaves())
			endFightCaveMinigame();
	}
	
	public void endFightCaveMinigame(){
		player.teleport(outside);
		for (Npc caveMonsters : player.getFightCaveMonsters()) {
			caveMonsters.setVisible(false);
            World.unregister(caveMonsters);
		}
	}
	
	public void startNextWave(){
		player.fightCaveWave += 1;
		WavesHandling.spawnWave(player, player.fightCaveWave);
	}
	
	public void finishMinigame(){
		player.getUpdateFlags().sendAnimation(862);
		final Tick timer1 = new Tick(3) {
            @Override
            public void execute() {
            	endFightCaveMinigame();
            	player.getDialogue().setLastNpcTalk(2617);
        		player.getDialogue().sendNpcChat("You even defeated TzTok-Jad, I am most impressed!",
        										"Please accept this gift as a reward.",Dialogues.CONTENT);
        		player.getDialogue().endDialogue();
        		player.getInventory().addItemOrDrop(new Item(6570, 1));
        		player.getInventory().addItemOrDrop(new Item(6529, 8032));
                stop();
            }
            
		};
		World.getTickManager().submit(timer1);
	}
	
	public void startFightCaveMinigame(){
		player.fightCaveWave = 0;
		player.teleport(new Position(2411, 5114, 0), true);
		player.getDialogue().setLastNpcTalk(2617);
		player.getDialogue().sendNpcChat("You're on your own now JalYt, prepare to fight for",
										"your life!",Dialogues.CONTENT);
		player.getDialogue().endDialogue();
		WavesHandling.spawnWave(player, player.fightCaveWave);
	}
    
}
