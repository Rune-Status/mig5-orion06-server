package com.rs2.model.content.skills.agility;

import com.rs2.model.Position;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.util.Misc;

public class Shortcuts {
	
	public static boolean handleObstacle(Player player, int objectId, int objectX, int objectY) {
		switch (objectId) {
			case 2296://coal trucks log
				if((objectX == 2599 && objectY == 3477) || (objectX == 2602 && objectY == 3477)){
					if (player.getSkill().getLevel()[Skill.AGILITY] < 20) {
						player.getDialogue().sendStatement("You need a agility level of " + 20 + " to do that.");
						return true;
					} else {
						CrossObstacle.walkAcross3(player, (Misc.random(5) == 0 ? 7 : 0), (objectX < 2600 ? 5 : -5), 0, -1, 762, -1, 3, "You walk carefully across the slippery log...", "...You make it safely to the other side.");
						return true;
					}
				}
			break;
			
			case 9330://ardougne log
				if((objectX == 2601 && objectY == 3336)){
					if (player.getSkill().getLevel()[Skill.AGILITY] < 32) {
						player.getDialogue().sendStatement("You need a agility level of " + 32 + " to do that.");
						return true;
					} else {
						CrossObstacle.walkAcross3(player, 4, -4, 0, -1, 762, -1, 2, "You walk carefully across the slippery log...", "...You make it safely to the other side.");
						return true;
					}
				}
			break;
			
			case 9328://ardougne log
				if((objectX == 2599 && objectY == 3336)){
					if (player.getSkill().getLevel()[Skill.AGILITY] < 32) {
						player.getDialogue().sendStatement("You need a agility level of " + 32 + " to do that.");
						return true;
					} else {
						CrossObstacle.walkAcross3(player, 4, 4, 0, -1, 762, -1, 2, "You walk carefully across the slippery log...", "...You make it safely to the other side.");
						return true;
					}
				}
			break;
			
			case 12127:
				if((objectX == 2400 && objectY == 4403)){//cosmic shortcut #1
					if (player.getSkill().getLevel()[Skill.AGILITY] < 44) {
						player.getDialogue().sendStatement("You need a agility level of " + 44 + " to do that.");
						return true;
					} else {
						player.getUpdateFlags().sendAnimation(754);
						player.getSkill().addExp(Skill.AGILITY, 10);
						CrossObstacle.setForceMovement(player, 0, player.getPosition().getY() < 4404 ? 2 : -2, 1, 80, 2, true, 0, 0);
						return true;
					}
				}
				if((objectX == 2408 && objectY == 4395)){//cosmic shortcut #2
					if (player.getSkill().getLevel()[Skill.AGILITY] < 66) {
						player.getDialogue().sendStatement("You need a agility level of " + 66 + " to do that.");
						return true;
					} else {
						player.getUpdateFlags().sendAnimation(754);
						player.getSkill().addExp(Skill.AGILITY, 10);
						CrossObstacle.setForceMovement(player, 0, player.getPosition().getY() < 4396 ? 2 : -2, 1, 80, 2, true, 0, 0);
						return true;
					}
				}
				if((objectX == 2415 && objectY == 4402)){//cosmic shortcut #2
					if (player.getSkill().getLevel()[Skill.AGILITY] < 66) {
						player.getDialogue().sendStatement("You need a agility level of " + 66 + " to do that.");
						return true;
					} else {
						player.getUpdateFlags().sendAnimation(754);
						player.getSkill().addExp(Skill.AGILITY, 10);
						CrossObstacle.setForceMovement(player, 0, player.getPosition().getY() < 4403 ? 2 : -2, 1, 80, 2, true, 0, 0);
						return true;
					}
				}
			break;
			
			case 9302://yanille under wall
				if((objectX == 2575 && objectY == 3111)){
					if (player.getSkill().getLevel()[Skill.AGILITY] < 16) {
						player.getDialogue().sendStatement("You need a agility level of " + 16 + " to do that.");
						return true;
					} else {
						player.teleport(new Position(2575, 3107, 0));
						return true;
					}
				}
			break;
			
			case 9301://yanille under wall
				if((objectX == 2575 && objectY == 3108)){
					if (player.getSkill().getLevel()[Skill.AGILITY] < 16) {
						player.getDialogue().sendStatement("You need a agility level of " + 16 + " to do that.");
						return true;
					} else {
						player.teleport(new Position(2575, 3112, 0));
						return true;
					}
				}
			break;
			
			case 9294://tav. dung - strange floor
				if((objectX == 2879 && objectY == 9813)){
					if (player.getSkill().getLevel()[Skill.AGILITY] < 80) {
						player.getDialogue().sendStatement("You need a agility level of " + 80 + " to do that.");
						return true;
					} else {
						if(player.getPosition().getX() < 2880){
							player.teleport(new Position(2880, 9813, 0));
						}else{
							player.teleport(new Position(2878, 9813, 0));
						}
						return true;
					}
				}
			break;
			
			case 9326://frem slayer dung pyrefiend - cave crawler
				if((objectX == 2769 && objectY == 10002) || (objectX == 2774 && objectY == 10003)){
					if (player.getSkill().getLevel()[Skill.AGILITY] < 81) {
						player.getDialogue().sendStatement("You need a agility level of " + 81 + " to do that.");
						return true;
					} else {
						if((objectX == 2774 && objectY == 10003))
							player.teleport(new Position(2768, 10002, 0));
						else
							player.teleport(new Position(2775, 10003, 0));
						return true;
					}
				}
			break;
			
			case 9321://frem slayer dung basilisk - turoth
				if((objectX == 2734 && objectY == 10008) || (objectX == 2731 && objectY == 10008)){
					if (player.getSkill().getLevel()[Skill.AGILITY] < 61) {
						player.getDialogue().sendStatement("You need a agility level of " + 61 + " to do that.");
						return true;
					} else {
						if((objectX == 2731 && objectY == 10008))
							player.teleport(new Position(2735, 10008, 0));
						else
							player.teleport(new Position(2730, 10008, 0));
						return true;
					}
				}
			break;
			
			case 9324://fremmenik log
				if((objectX == 2722 && objectY == 3593)){
					if (player.getSkill().getLevel()[Skill.AGILITY] < 47) {
						player.getDialogue().sendStatement("You need a agility level of " + 47 + " to do that.");
						return true;
					} else {
						CrossObstacle.walkAcross3(player, 0, 0, 4, -1, 762, -1, 2, "You walk carefully across the slippery log...", "...You make it safely to the other side.");
						return true;
					}
				}
			break;
			
			case 9322://fremmenik log
				if((objectX == 2722 && objectY == 3595)){
					if (player.getSkill().getLevel()[Skill.AGILITY] < 47) {
						player.getDialogue().sendStatement("You need a agility level of " + 47 + " to do that.");
						return true;
					} else {
						CrossObstacle.walkAcross3(player, 0, 0, -4, -1, 762, -1, 2, "You walk carefully across the slippery log...", "...You make it safely to the other side.");
						return true;
					}
				}
			break;
			
			case 11844://crumbling wall @falador
				if(objectX == 2935 && objectY == 3355){
					if (player.getSkill().getLevel()[Skill.AGILITY] < 5) {
						player.getDialogue().sendStatement("You need a agility level of " + 5 + " to do that.");
						return true;
					} else {
						CrossObstacle.walkAcross3(player, 0.5, (player.getPosition().getX() < 2936 ? 2 : -2), 0, -1, 840, -1, 1, null, null);
						return true;
					}
				}
			break;
			
			case 9295://pipe @varrock sewer
				if(objectX == 3150 && objectY == 9906){
					if (player.getSkill().getLevel()[Skill.AGILITY] < 51) {
						player.getDialogue().sendStatement("You need a agility level of " + 51 + " to do that.");
						return true;
					} else {
						CrossObstacle.walkAcross3(player, 0, 6, 0, 746, 844, 748, 6, null, null);
						return true;
					}
				}
				if(objectX == 3153 && objectY == 9906){
					if (player.getSkill().getLevel()[Skill.AGILITY] < 51) {
						player.getDialogue().sendStatement("You need a agility level of " + 51 + " to do that.");
						return true;
					} else {
						CrossObstacle.walkAcross3(player, 0, -6, 0, 746, 844, 748, 6, null, null);
						return true;
					}
				}
			break;
			
			case 9293://pipe @tav. dung
				if(objectX == 2887 && objectY == 9799){
					if (player.getSkill().getLevel()[Skill.AGILITY] < 70) {
						player.getDialogue().sendStatement("You need a agility level of " + 70 + " to do that.");
						return true;
					} else {
						CrossObstacle.walkAcross3(player, 0, 6, 0, 746, 844, 748, 6, null, null);
						return true;
					}
				}
				if(objectX == 2890 && objectY == 9799){
					if (player.getSkill().getLevel()[Skill.AGILITY] < 70) {
						player.getDialogue().sendStatement("You need a agility level of " + 70 + " to do that.");
						return true;
					} else {
						CrossObstacle.walkAcross3(player, 0, -6, 0, 746, 844, 748, 6, null, null);
						return true;
					}
				}
			break;
			
		}
		return false;
	}
	
}
