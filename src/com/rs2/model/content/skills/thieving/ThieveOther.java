package com.rs2.model.content.skills.thieving;

import java.util.Random;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.functions.Doors;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

public class ThieveOther {// todo hit method for poison chest and chest and doors

	private static final Random r = new Random();

	public static void pickLock(final Player player, final Position position, final int objectId, int level, final double xp, final int destX, final int destY) {
		if (!Constants.THIEVING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (player.getSkill().getLevel()[Skill.THIEVING] < level) {
			player.getActionSender().sendMessage("Your thieving level is not high enough to pick this lock.");
			return;
		}
		if (!player.getInventory().getItemContainer().contains(1523)) {
			player.getActionSender().sendMessage("You need a lockpick to do that.");
			return;
		}
		player.getUpdateFlags().sendAnimation(2246);
		player.getActionSender().sendMessage("You attempt to pick the lock...");
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (r.nextInt(30) < 5) {
					player.getActionSender().sendMessage("But fail to pick it.");
					player.setStopPacket(false);
					container.stop();
					return;
				}
				player.getActionSender().sendMessage("And manage to pass through it.");
				player.getActionSender().sendSound(1502, 1, 0);
				player.getSkill().addExp(Skill.THIEVING, xp);

				//Doors.handlePassThroughDoor(player, objectId, position.getX(), position.getY(), position.getZ(), destX, destY);
				Doors.handlePassThroughDoor(player, objectId, position.getX(), position.getY(), player.getPosition().getZ(), destX, destY);
				container.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
				player.resetAnimation();
			}
		}, 4);

	}

	public static void pickTrap(final Player player, final int objectId, final int objectX, final int objectY, int level, final double xp, final Item[] rewards) {
		if (!Constants.THIEVING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if (player.getSkill().getLevel()[Skill.THIEVING] < level) {
			player.getActionSender().sendMessage("Your thieving level is not high enough to disarm traps.");
			return;
		}
		player.setStopPacket(true);
		player.getUpdateFlags().sendAnimation(2246);
		player.getActionSender().sendMessage("You attempt to disarm the traps...");
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (r.nextInt(30) < 5) {
					player.getActionSender().sendMessage("But fail to disarm it, and get hit by the traps.");
					int numb = r.nextInt(1);
					player.hit(Misc.random(10), numb == 1 ? HitType.POISON : HitType.NORMAL);
					if (numb == 1)
						player.getUpdateFlags().sendGraphic(184);
					container.stop();
					return;
				}
				player.getActionSender().sendMessage("And manage to disarm it.");
				player.getActionSender().sendSound(1502, 1, 0);
				player.getSkill().addExp(Skill.THIEVING, xp);
				for (Item item : rewards)
					player.getInventory().addItemOrDrop(item);
				new GameObject(2588, objectX, objectY, player.getPosition().getZ(), SkillHandler.getFace(objectId, objectX, objectY, player.getPosition().getZ()), 10, objectId, 10);
				container.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
				player.resetAnimation();
			}
		}, 3);

	}

	public static boolean handleObjectClick2(final Player player, int objectId, int objectX, int objectY) {
		int x = player.getPosition().getX();
		int y = player.getPosition().getY();
		switch (objectId) {
		case 2550:
			if (x == 2674 && y == 3305)
				pickLock(player, new Position(objectX, objectY), objectId, 1, 3.5, 0, 1);
			else
				pickLock(player, new Position(objectX, objectY), objectId, 16, 15, 0, -1);
			return true;
		case 2556:
			if (x == 2610 && y == 3316)
				pickLock(player, new Position(objectX, objectY), objectId, 14, 15, 1, 0);
			else
				pickLock(player, new Position(objectX, objectY), objectId, 13, 15, -1, 0);
			return true;
		case 2551:
			if (x == 2674 && y == 3304)
				pickLock(player, new Position(objectX, objectY), objectId, 16, 15, 0, -1);
			else
				pickLock(player, new Position(objectX, objectY), objectId, 16, 15, 0, 1);
			return true;
		case 2555:
			if (x == 2572 && y == 3288)
				pickLock(player, new Position(objectX, objectY), objectId, 61, 50, 0, -1);
			else
				pickLock(player, new Position(objectX, objectY), objectId, 61, 50, 0, 1);
			return true;	
		case 2558:
			if (x == 3037 && y == 3956)
				pickLock(player, new Position(objectX, objectY), objectId, 39, 35, 1, 0);
			else if (x == 3041 && y == 3960)
				pickLock(player, new Position(objectX, objectY), objectId, 39, 35, 0, -1);
			else if (x == 3041 && y == 3959)
				pickLock(player, new Position(objectX, objectY), objectId, 39, 35, 0, 1);
			else if (x == 3045 && y == 3956)
				pickLock(player, new Position(objectX, objectY), objectId, 39, 35, -1, 0);
			else if (x == 3044 && y == 3956)
				pickLock(player, new Position(objectX, objectY), objectId, 39, 35, 1, 0);
			else
				pickLock(player, new Position(objectX, objectY), objectId, 39, 35, -1, 0);
			return true;
		case 2557:
			if (x == 3190 && y == 3957)
				pickLock(player, new Position(objectX, objectY), objectId, 42, 23, 0, 1);
			else if (x == 3190 && y == 3958)
				pickLock(player, new Position(objectX, objectY), objectId, 42, 23, 0, -1);
			else if (x == 3191 && y == 3962)
				pickLock(player, new Position(objectX, objectY), objectId, 42, 23, 0, 1);
			else if (x == 3191 && y == 3963)
				pickLock(player, new Position(objectX, objectY), objectId, 42, 23, 0, -1);
			return true;
		case 2566:
			if (objectX == 2673 && objectY == 3307)
				pickTrap(player, objectId, objectX, objectY, 10, 7, new Item[] { new Item(995, 10) });
			return true;
		}
		return false;
	}
}
