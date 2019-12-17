package com.rs2.model.content.skills.agility;

import com.rs2.model.Position;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.players.Player;

public class WildernessCourse {
	
	public static boolean handleObstacle(Player player, int objectId, int objectX, int objectY) {
		switch (objectId) {
			case 2309://door
				if(objectX == 2998 && objectY == 3917){//2998,3916
					if(!SkillHandler.hasRequiredLevel(player, Skill.AGILITY, 52, "enter"))
						return true;
					player.getActionSender().walkThroughDoor(objectId, objectX, objectY, 0);
					CrossObstacle.walkAcross3(player, 13, 0, 15, -1, 762, -1, 8, "You go through the gate and try to edge over the ridge...", "...You skillfully balance across the ridge...");
					return true;
				}
			break;
			
			case 2307:
			case 2308://gate
				if((objectX == 2998 && objectY == 3931) || (objectX == 2997 && objectY == 3931)){//2998,3931
					CrossObstacle.walkAcross3(player, 13, 0, -15, -1, 762, -1, 8, "You go through the gate and try to edge over the ridge...", "...You skillfully balance across the ridge...");
					return true;
				}
			break;
			
			case 2288://pipe
				if(objectX == 3004 && objectY == 3938){//3004,3937 - 3004,3950
					if(!SkillHandler.hasRequiredLevel(player, Skill.AGILITY, 49, "to go through."))
						return true;
					CrossObstacle.walkAcross3(player, 12.5, 0, 13, 746, 844, 748, 9, null, null);
					player.agilityObstacle = 1;
					return true;
				}
			break;
			
			case 2283://rope
				if(objectX == 3005 && objectY == 3952){//3005,3953 - 3005,3958
					CrossObstacle.walkAcross2(player, 20, 0, 7, 1, 60, -1, 751, -1);
					player.getActionSender().animateObject(2551, 3550, 0, 127);
					player.getActionSender().sendMessage("You skillfully swing across.");
					if(player.agilityObstacle == 1)
						player.agilityObstacle = 2;
					return true;
				}
			break;
			
			case 2311://stepping stone
				if(objectX == 3001 && objectY == 3960){//3002,3960 - 2996,3960
					player.teleport(new Position(2996, 3960, 0));
					player.getSkill().addExp(Skill.AGILITY, 20);
					if(player.agilityObstacle == 2)
						player.agilityObstacle = 3;
					return true;
				}
			break;
			
			case 2297://log
				if(objectX == 3001 && objectY == 3945){//3002,3945 - 2994,3945
					CrossObstacle.walkAcross3(player, 20, -8, 0, -1, 762, -1, 4, "You walk carefully across the slippery log...", "You skillfully edge across the gap.");
					if(player.agilityObstacle == 3)
						player.agilityObstacle = 4;
					return true;
				}
			break;
			
			case 2328://rocks //2995,3937 - 2995,3933
				int xp = (player.agilityObstacle == 4 ? 499 : 0);
				CrossObstacle.climbNet(player, xp, 0, -4, 0, 828, -1, -1, 1, null, null);
				player.agilityObstacle = 0;
				return true;
				
		}
		return false;
	}
	
}
