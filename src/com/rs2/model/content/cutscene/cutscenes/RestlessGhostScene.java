package com.rs2.model.content.cutscene.cutscenes;

import java.util.ArrayList;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.projectile.Projectile;
import com.rs2.model.content.combat.projectile.ProjectileDef;
import com.rs2.model.content.combat.projectile.ProjectileTrajectory;
import com.rs2.model.content.cutscene.Cutscene;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.questing.QuestDefinition;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;

public class RestlessGhostScene extends Cutscene {
	
	static Quest quest = QuestDefinition.getQuest(12);
	
	public RestlessGhostScene(Player player, ArrayList<Npc> npcs){
		super(player, npcs);
	}
	
	Player player = this.getPlayer();
	Npc npc = this.getActor(0);
	
	public void build(){
		Scene sc1 = new Scene(3){
			public void playScene(){
				npc.getUpdateFlags().setForceChatMessage("stranger..");
			}
		};
		Scene sc2 = new Scene(3){
			public void playScene(){
				npc.getUpdateFlags().sendAnimation(1500);
            	npc.getUpdateFlags().sendGraphic(189, 25);
			}
		};
		Scene sc3 = new Scene(2){
			public void playScene(){
				npc.setVisible(false);
	            World.unregister(npc);
	            player.getActionSender().spinCamera(2659, 3195, 965, 0, 100);
				player.getActionSender().stillCamera(3660, 2500, 2000, 0, 100);
				player.getActionSender().spinCamera(2559, 3195, 1000, 0, 15);
				player.getActionSender().stillCamera(3560, 2400, 1100, 0, 15);
				castProjectile(new Position(3244,3193,0), new Position(3253,3179,0), 605);
			}
		};
		Scene sc4 = new Scene(5){
			public void playScene(){
            	player.getInventory().removeItem(new Item(QuestConstants.SKULL));
            	quest.questCompleted(player);
			}
		};
		this.addScene(sc1);
		this.addScene(sc2);
		this.addScene(sc3);
		this.addScene(sc4);
	}
	
	public void setStartPositions(){
		npc.teleport(new Position(3248, 3193));
		player.teleport(new Position(3248,3192,0));
		player.getActionSender().sendMapRegion();
		player.getActionSender().spinCamera(3340, 3149, 330, 0, 100);
		player.getActionSender().stillCamera(3325, 3149, 298, 0, 100);
		player.getUpdateFlags().faceEntity(npc.getFaceIndex());
		npc.getUpdateFlags().setFace(player.getPosition());
	}
	
	public void init(){
		player.getDialogue().setLastNpcTalk(npc.getNpcId());
		player.questStage[quest.getId()] = 6;
		player.getDialogue().sendTimedNpcChat("","Release! Thank you stranger..","",Dialogues.CONTENT);
		npc.getUpdateFlags().setForceChatMessage("Release! Thank you");
	}
	
	public static void castProjectile(final Position start, final Position end, int gfx) {
		new Projectile(start, 0, end, 0, new ProjectileDef(gfx, ProjectileTrajectory.SPELL)).show();
	}
	
}