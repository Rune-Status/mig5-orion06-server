package com.rs2.model.content.skills.agility;

import com.rs2.model.Position;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.players.Player;

public class BarbarianCourse {
	
	public static boolean handleObstacle(Player player, int objectId, int objectX, int objectY) {
		switch (objectId) {
			case 2287://pipe
				if(objectX == 2552 && objectY == 3559){
					if(!SkillHandler.hasRequiredLevel(player, Skill.AGILITY, 35, "enter"))
						return true;
					CrossObstacle.walkAcross3(player, 10, 0, (player.getPosition().getY() >= 3560 ? -3 : 3), 746, 844, 748, 3, null, null);
					return true;
				}
			break;
			
			case 2282://rope
				if(objectX == 2551 && objectY == 3550){
					CrossObstacle.walkAcross2(player, 22, 0, -5, 1, 60, -1, 751, -1);
					player.getActionSender().animateObject(2551, 3550, 0, 127);
					player.agilityObstacle = 1;
					return true;
				}
			break;
			
			case 2294://log
				if(objectX == 2550 && objectY == 3546){
					CrossObstacle.walkAcross3(player, 13, -10, 0, -1, 762, -1, 5, "You walk carefully across the slippery log...", "...You make it safely to the other side.");
					if(player.agilityObstacle == 1)
						player.agilityObstacle = 2;
					return true;
				}
			break;
			
			case 2284://net
				if(objectX == 2538 && objectY == 3545){
					CrossObstacle.climbNet(player, 8, -1, 0, 1, 828, -1, -1, 2, "You climb the netting...", null);
					if(player.agilityObstacle == 2)
						player.agilityObstacle = 3;
					return true;
				}
			break;
			
			case 2302://ledge
				if(objectX == 2535 && objectY == 3547){
					CrossObstacle.walkAcross3(player, 22, -4, 0, -1, 756, -1, 3, null, null);
					if(player.agilityObstacle == 3)
						player.agilityObstacle = 4;
					return true;
				}
			break;
			
			case 1948://crumbling wall
				if(objectX == 2536 && objectY == 3553){
					CrossObstacle.walkAcross3(player, 13, 2, 0, -1, 840, -1, 1, null, null);
					if(player.agilityObstacle == 4)
						player.agilityObstacle = 5;
					return true;
				}
				if(objectX == 2539 && objectY == 3553){
					CrossObstacle.walkAcross3(player, 13, 2, 0, -1, 840, -1, 1, null, null);
					if(player.agilityObstacle == 5)
						player.agilityObstacle = 6;
					return true;
				}
				if(objectX == 2542 && objectY == 3553){
					int xp = (player.agilityObstacle == 6 ? 60 : 13);
					CrossObstacle.walkAcross3(player, xp, 2, 0, -1, 840, -1, 1, null, null);
					player.agilityObstacle = 0;
					return true;
				}
			break;
		}
		return false;
	}
	
}
