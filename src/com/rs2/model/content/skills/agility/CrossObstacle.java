package com.rs2.model.content.skills.agility;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class CrossObstacle {

	public static void walkAcross(final Player player, final int xp, final int walkX, final int walkY, final int time, final int speed, final int startAnim) {
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (startAnim > 0) {
			player.getUpdateFlags().sendAnimation(startAnim);
		}
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
	        	player.getActionSender().animateObject(player.getClickX(), player.getClickY(), 3, 127);
				setForceMovement(player, walkX, walkY, 1, speed, time, true, xp, 0);
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, 2);
	}
	
	public static void setForceMovement(final Player player, final int x, final int y, final int speed1, final int speed2, final int time, final boolean stopPacket, final int xp, final int walkAnim) {
		if (stopPacket) {
			player.setStopPacket(true);
		}
		player.getMovementHandler().reset();
		if (walkAnim > 0) {
			player.setRunAnim(walkAnim);
			player.setWalkAnim(walkAnim);
			player.setAppearanceUpdateRequired(true);
		}
		int direction = 2;
		if (x > 0) {
			direction = 1;
		} else if (x < 0) {
			direction = 3;
		} else if (y > 0) {
			direction = 0;
		}
		final int endX = player.getPosition().getX() + x;
		final int endY = player.getPosition().getY() + y;
		final int endZ = player.getPosition().getZ();
		final int dir = direction;
		player.isCrossingObstacle = true;
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.getUpdateFlags().sendForceMovement(player, x, y, speed1, speed2, dir);
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, 1);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.getSkill().addExp(Skill.AGILITY, xp);
		        player.getUpdateFlags().setForceMovementUpdateRequired(false);
				if (stopPacket) {
					player.setStopPacket(false);
				}
				player.isCrossingObstacle = false;
				player.teleport(new Position(endX, endY, endZ));
				player.getUpdateFlags().resetForceMovement();
				player.setRunAnim(-1);
				player.setWalkAnim(-1);
				player.setAppearanceUpdateRequired(true);
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, time + 1);
	}
	
	public static void walkAcross2(final Player player, final int xp, final int walkX, final int walkY, final int time, final int speed, final int startAnim, final int actionAnim, final int endAnim) {
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (startAnim > 0) {
			player.getUpdateFlags().sendAnimation(startAnim);
		}
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
	        	player.getActionSender().animateObject(player.getClickX(), player.getClickY(), 3, 127);
				setForceMovement2(player, walkX, walkY, 1, speed, time, true, xp, actionAnim);
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, (startAnim > 0 ? 2 : 0));
	}

	public static void setForceMovement2(final Player player, final int x, final int y, final int speed1, final int speed2, final int time, final boolean stopPacket, final int xp, final int walkAnim) {
		if (stopPacket) {
			player.setStopPacket(true);
		}
		player.getMovementHandler().reset();
		if (walkAnim > 0) {
			player.setStandAnim(walkAnim);
			player.setRunAnim(walkAnim);
			player.setWalkAnim(walkAnim);
			player.setAppearanceUpdateRequired(true);
		}
		int direction = 2;
		if (x > 0) {
			direction = 1;
		} else if (x < 0) {
			direction = 3;
		} else if (y > 0) {
			direction = 0;
		}
		final int endX = player.getPosition().getX() + x;
		final int endY = player.getPosition().getY() + y;
		final int endZ = player.getPosition().getZ();
		final int dir = direction;
		player.isCrossingObstacle = true;
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.getUpdateFlags().sendForceMovement(player, x, y, speed1, speed2, dir);
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, 1);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.getSkill().addExp(Skill.AGILITY, xp);
				player.getActionSender().sendMessage("...You make it safely to the other side.");
		        player.getUpdateFlags().setForceMovementUpdateRequired(false);
				if (stopPacket) {
					player.setStopPacket(false);
				}
				player.isCrossingObstacle = false;
				player.teleport(new Position(endX, endY, endZ));
				player.getUpdateFlags().resetForceMovement();
				player.setStandAnim(-1);
				player.setRunAnim(-1);
				player.setWalkAnim(-1);
				player.setAppearanceUpdateRequired(true);
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, time + 1);
	}
	
	public static void walkAcross3(final Player player, final double xp, final int walkX, final int walkY, final int startAnim, final int walkAnim, final int endAnim, final int time, final String startMessage, final String successMessage){
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (startAnim > 0) {
			player.getUpdateFlags().sendAnimation(startAnim);
		}
		player.setStopPacket(true);
		player.getActionSender().sendMessage(startMessage);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.setStopPacket(false);
				player.getActionSender().walkTo2(walkX, walkY, true, walkAnim, endAnim, time, xp, player.getMovementHandler().isRunToggled(), successMessage);
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, (startAnim > 0 ? 2 : 0));//was 4
	}
	
	public static void climbNet(final Player player, final double xp, final int endX, final int endY, final int endZ, final int startAnim, final int walkAnim, final int endAnim, final int time, final String startMessage, final String endMessage){
		if (!Constants.AGILITY_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (startAnim > 0) {
			player.getUpdateFlags().sendAnimation(startAnim);
		}
		player.setStopPacket(true);//spam click fix
		player.getActionSender().sendMessage(startMessage);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.teleport(new Position(player.getPosition().getX()+endX, player.getPosition().getY()+endY, player.getPosition().getZ()+endZ));
				if(endZ == 0)
					player.getUpdateFlags().sendFaceToDirection(new Position(player.getPosition().getX()-endX, player.getPosition().getY()-endY, player.getPosition().getZ()-endZ));
				player.getSkill().addExp(Skill.AGILITY, xp);
				player.getActionSender().sendMessage(endMessage);
				player.setStopPacket(false);//spam click fix
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, time);
	}

}