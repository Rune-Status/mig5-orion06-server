package com.rs2.model.content.skills.agility;

import com.rs2.model.Position;
import com.rs2.model.players.Player;

public class GnomeCourse {
	
	public static boolean handleObstacle(Player player, int objectId, int objectX, int objectY) {
		switch (objectId) {

			case 2295://log
				if(objectX == 2474 && objectY == 3435){
					CrossObstacle.walkAcross3(player, 7.5, 0, -7, -1, 762, -1, 4, "You walk carefully across the slippery log...", "...You make it safely to the other side.");
					player.agilityObstacle = 1;
					return true;
				}
			break;
			
			case 2285://nets in beginning
				if(((objectX == 2475 && objectY == 3425) || (objectX == 2473 && objectY == 3425) || (objectX == 2471 && objectY == 3425)) && player.getPosition().getY() >= 3426){
					CrossObstacle.climbNet(player, 7.5, 0, -2, 1, 828, -1, -1, 2, "You climb the netting...", null);
					if(player.agilityObstacle == 1)
						player.agilityObstacle = 2;
					return true;
				}
			break;
			
			case 2313://tree up
				if((objectX == 2473 && objectY == 3422)){
					int i = 3421 - player.getPosition().getY();
					CrossObstacle.climbNet(player, 5, 0, i, 1, 828, -1, -1, 2, "You climb the tree...", "...To the platform above.");
					if(player.agilityObstacle == 2)
						player.agilityObstacle = 3;
					return true;
				}
			break;
			
			case 2312://rope
				if(objectX == 2478 && objectY == 3420){
					CrossObstacle.walkAcross3(player, 7.5, 6, 0, -1, 762, -1, 3, "You carefully cross the tightrope.", null);
					if(player.agilityObstacle == 3)
						player.agilityObstacle = 4;
					return true;
				}
			break;
			
			case 2314://tree down
				if(objectX == 2486 && objectY == 3419){
					CrossObstacle.climbNet(player, 5, 0, 0, -2, 828, -1, -1, 2, "You climb down the tree...", "You land on the ground.");
					if(player.agilityObstacle == 4)
						player.agilityObstacle = 5;
					return true;
				}
			break;
			
			case 2286://nets in end
				if(((objectX == 2483 && objectY == 3426) || (objectX == 2485 && objectY == 3426) || (objectX == 2487 && objectY == 3426)) && player.getPosition().getY() <= 3425){
					CrossObstacle.climbNet(player, 7.5, 0, 2, 0, 828, -1, -1, 2, "You climb the netting...", null);
					if(player.agilityObstacle == 5)
						player.agilityObstacle = 6;
					return true;
				}
			break;
			
			case 154:
			case 4058://pipes
				if((objectX == 2484 && objectY == 3431) || (objectX == 2487 && objectY == 3431)){
					double xp = (player.agilityObstacle == 6 ? 46.5 : 7.5);
					CrossObstacle.walkAcross3(player, xp, 0, 7, 746, 844, 748, 7, null, null);
					player.agilityObstacle = 0;
					return true;
				}
			break;
		}
		return false;
	}
	
}
