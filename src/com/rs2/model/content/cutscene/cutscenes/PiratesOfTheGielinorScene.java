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

public class PiratesOfTheGielinorScene extends Cutscene {
	
	static Quest quest = QuestDefinition.getQuest(105);
	
	public PiratesOfTheGielinorScene(Player player, ArrayList<Npc> npcs){
		super(player, npcs);
	}
	
	Player player = this.getPlayer();
	
	public void build(){
		Scene sc1 = new Scene(10){
			public void playScene(){
				player.getActionSender().spinCamera(5000, 3150, 900, 0, 1);//w-e (e=+), n-s (n=+), height
				player.getActionSender().stillCamera(0, 3100, 0, 0, 1);//?, which way to look, ?
			}
		};
		Scene sc2 = new Scene(50){
			public void playScene(){
				player.getActionSender().spinCamera(3500, 4000, 310, 0, 3);//w-e (e=+), n-s (n=+), height
				player.getActionSender().stillCamera(3500, 0, 0, 0, 15);//?, which way to look, ?
			}
		};
		Scene sc3 = new Scene(20){
			public void playScene(){
				player.getActionSender().spinCamera(2600, 2900, 400, 0, 10);//w-e (e=+), n-s (n=+), height
				player.getActionSender().stillCamera(16000, 8000, 0, 0, 10);//?, which way to look, ?
			}
		};
		Scene sc4 = new Scene(8){
			public void playScene(){
				player.getActionSender().spinCamera(1000, 2900, 900, 0, 1);//w-e (e=+), n-s (n=+), height
				player.getActionSender().stillCamera(16000, 8000, 0, 0, 15);//?, which way to look, ?
			}
		};
		Scene sc5 = new Scene(34){
			public void playScene(){
				player.getActionSender().sendInterface(8677);
			}
		};
		Scene sc6 = new Scene(3){
			public void playScene(){
				player.teleport(new Position(3053, 3246, 0));
			}
		};
		Scene sc7 = new Scene(1){
			public void playScene(){
				player.getActionSender().removeInterfaces();
				//player.getActionSender().sendSong(35);
				quest.questCompleted(player);
			}
		};
		this.addScene(sc1);
		this.addScene(sc2);
		this.addScene(sc3);
		this.addScene(sc4);
		this.addScene(sc5);
		this.addScene(sc6);
		this.addScene(sc7);
	}
	
	public void setStartPositions(){
		player.teleport(new Position(2009, 4889, 1));
		player.getActionSender().sendMapRegion();
		player.getActionSender().spinCamera(3300, 3150, 310, 0, 100);//w-e (e=+), n-s (n=+), height
		player.getActionSender().stillCamera(0, 3100, 0, 0, 100);//?, which way to look, ?
		player.getActionSender().sendQuickSong(646, 3800);
		player.getUpdateFlags().sendFaceToDirection(new Position(player.getPosition().getX()+1, player.getPosition().getY(), player.getPosition().getZ()));
	}
	
}