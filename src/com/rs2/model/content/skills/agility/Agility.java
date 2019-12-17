package com.rs2.model.content.skills.agility;

import com.rs2.model.players.Player;

public class Agility {

	public static boolean handleObstacle(Player player, int objectId, int objectX, int objectY) {
		if(GnomeCourse.handleObstacle(player, objectId, objectX, objectY))
			return true;
		if(BarbarianCourse.handleObstacle(player, objectId, objectX, objectY))
			return true;
		if(WildernessCourse.handleObstacle(player, objectId, objectX, objectY))
			return true;
		if(Shortcuts.handleObstacle(player, objectId, objectX, objectY))
			return true;
		return false;
	}
}
