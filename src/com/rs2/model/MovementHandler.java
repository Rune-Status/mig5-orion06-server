package com.rs2.model;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.rs2.Constants;
import com.rs2.model.content.DesertHeat;
import com.rs2.model.content.Following;
import com.rs2.model.content.SmokeDungeon;
import com.rs2.model.content.WalkInterfaces;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.MovementLock;
import com.rs2.model.players.Player;
import com.rs2.model.players.TradeManager;
import com.rs2.model.region.music.RegionMusic;
import com.rs2.util.Misc;

/**
 * Handles the movement of a Player.
 * 
 * @author blakeman8192
 */
public class MovementHandler {

	private final Entity entity;
	private Deque<Point> waypoints = new LinkedList<Point>();
	private Deque<Point> waypointHistory = new LinkedList<Point>();
	private boolean runToggled = false;
	private boolean runPath = false;
	private MovementLock movementLock;

	public static final byte[] DIRECTION_DELTA_X = new byte[] { -1, 0, 1, -1, 1, -1, 0, 1 };

	public static final byte[] DIRECTION_DELTA_Y = new byte[] { 1, 1, 1, 0, 0, -1, -1, -1 };
	private boolean canWalk = true;

	public Deque<Point> getWaypoints() {
		return waypoints;
	}

	/**
	 * Creates a new MovementHandler.
	 * 
	 * @param entity
	 *            the Player
	 */
	public MovementHandler(Entity entity) {
		this.entity = entity;
	}

	/**
	 * Travels back and inserts points into the waypoint queue from the movement
	 * history until the argued position is connectable in a straight line.
	 * 
	 * @param firstPos
	 *            The client-side first position
	 * @return true if the position could be connected, false otherwise
	 */
	public boolean travelback(Position firstPos) {
		List<Point> travelbackPath = new LinkedList<Point>();
		for (Iterator<Point> iter = waypointHistory.descendingIterator(); iter.hasNext();) {
			Point pos = iter.next();
			travelbackPath.add(pos);

			// Check if a connection could be made.
			if (pos.isConnectable(firstPos)) {
				// Success! Queue the travelback path and return true.
				for (Point travelbackPos : travelbackPath) {
					waypoints.add(travelbackPos);
				}
				return true;
			}
		}

		// Could not find a connection, we're SOL.
		return false;
	}

	public void stopMovement() {
		reset();
	}

	public void lock(MovementLock lock) {
		if (lock == null) {
			this.movementLock = lock;
			return;
		}
		// don't set movement lock until after start
		lock.start(entity);
		this.movementLock = lock;
	}

	public void unlock() {
		if (movementLock == null)
			return;
		if (entity.isMoving())
			entity.teleport(waypoints.getLast());
		this.movementLock.end(entity);
		this.movementLock = null;
	}

	public void process() {
		if (entity.isDead() || !entity.getMovementPaused().completed()) {
			return;
		}
		Point walkPoint = null;
		Point runPoint = null;
		// Handle the movement.

		// Delete old positions from the waypoint history.
		if (waypointHistory.size() >= 25) {
			for (Iterator<Point> iter = waypointHistory.iterator(); iter.hasNext() && waypointHistory.size() >= 10;) {
				iter.next();
				iter.remove();
			}
		}

		walkPoint = waypoints.poll();


		if (walkPoint == null || walkPoint.getDirection() == -1)
			unlock();
		final boolean forcesRun = movementLock != null && movementLock.forcesRun();
		if (entity.isPlayer()) {
			Player player = (Player) entity;
			if (isRunToggled() || isRunPath() || forcesRun) {
				if (player.getEnergy() > 0 || forcesRun) {
					runPoint = waypoints.poll();
				}
			}
		}
		boolean forceMovement = movementLock != null || (entity.isPlayer() && ((Player) entity).isCrossingObstacle);
		if (walkPoint != null && walkPoint.getDirection() != -1) {
			int x = DIRECTION_DELTA_X[walkPoint.getDirection()], y = DIRECTION_DELTA_Y[walkPoint.getDirection()];
			if (!forceMovement && !walkCheck(x, y)) {
				reset();
				return;
			}
			entity.getPosition().move(x, y);
			entity.setPrimaryDirection(walkPoint.getDirection());
			waypointHistory.add(walkPoint);
		}
		if (runPoint != null && runPoint.getDirection() != -1) {
			int x = DIRECTION_DELTA_X[runPoint.getDirection()], y = DIRECTION_DELTA_Y[runPoint.getDirection()];
			if (!forceMovement && !walkCheck(x, y)) {
				reset();
				return;
			}
			if (entity.isPlayer()) {
				Player player = (Player) entity;
				if (isRunToggled() || isRunPath()) {
					if (player.getEnergy() > 0) {
						if (!forcesRun) {
							if (!Constants.SERVER_DEBUG) {
								//player.setEnergy(player.getEnergy() - Math.log(player.totalWeight + 20) / 6);//new formula below
								/*double drainRate = 0.7D;
								double weightFactor = (player.totalWeight > 1.371D ? (int) player.totalWeight * 0.00729166D : 0.0D);
								// Truncated the weight factor to the nearest hundredth
								weightFactor = Math.round(weightFactor * 100.0D) / 100.0D;
								player.setEnergy(player.getEnergy() - (drainRate + weightFactor));*/
								double weight = player.totalWeight;
								double drainRate = 0.0083*weight+0.6723;
								player.setEnergy(player.getEnergy() - drainRate);
	                        }
							if (player.getEnergy() <= 0) {
                                setRunToggled(false);
                                player.getActionSender().sendConfig(173, 0);
                            }
                            player.getActionSender().sendEnergy();
                        }
					}
				}
			}
			entity.getPosition().move(x, y);
			entity.setSecondaryDirection(runPoint.getDirection());
			waypointHistory.add(runPoint);
		}
		if (entity.isPlayer()) {
			Player player = (Player) entity;
			if(player.inWild(player.getPosition().getX(), player.getPosition().getY()) && !player.wildyWarned)
				player.wildyWarned = true;
			if (entity.nextToWildy(entity.getPosition().getX(), entity.getPosition().getY()) && !player.wildyWarned) {
				player.wildyWarned = true;
				player.getActionSender().sendInterface(1908);
				Following.resetFollow(player);
				reset();
			}
			//if (!player.currentArea.equals(Position.getCurrentArea(player)))
			WalkInterfaces.addWalkableInterfaces(player);
			player.getQuestHandler().checkAreas();
			DesertHeat.checkAreas(player);
			SmokeDungeon.checkAreas(player);
			new RegionMusic().playMusic(player);
		}
	}

	/**
	 * Resets the walking queue.
	 */
	public void reset() {
		if (movementLock != null) {
			if (entity.isDead())
				unlock();
			else
				return;
		}
		setRunPath(false);
		waypoints.clear();
		// Set the base point as this position.
		Position p = entity.getPosition();
		waypoints.add(new Point(p.getX(), p.getY(), -1));
	}

	/**
	 * Finishes the current path.
	 */
	public void finish() {
		if (waypoints.size() > 0)
			waypoints.removeFirst();

	}

	/**
	 * Adds a position to the path.
	 * 
	 * @param position
	 *            the position
	 */
	public void addToPath(Position position) {
		if (movementLock != null)
			return;
		if (waypoints.size() == 0) {
			reset();
		}
		Point last = waypoints.peekLast();
		int deltaX = position.getX() - last.getX();
		int deltaY = position.getY() - last.getY();
		int max = Math.max(Math.abs(deltaX), Math.abs(deltaY));
		for (int i = 0; i < max; i++) {
			if (deltaX < 0) {
				deltaX++;
			} else if (deltaX > 0) {
				deltaX--;
			}
			if (deltaY < 0) {
				deltaY++;
			} else if (deltaY > 0) {
				deltaY--;
			}
			addStep(position.getX() - deltaX, position.getY() - deltaY);
		}

	}

	/**
	 * Adds a step.
	 * 
	 * @param x
	 *            the X coordinate
	 * @param y
	 *            the Y coordinate
	 */
	public void addStep(int x, int y) {
		if (movementLock != null)
			return;
		if (waypoints.size() >= 100) {
			return;
		}
		Point last = waypoints.peekLast();
		int deltaX = x - last.getX();
		int deltaY = y - last.getY();
		int direction = Misc.direction(deltaX, deltaY);
		if (direction > -1) {
			waypoints.add(new Point(x, y, direction));
		}
	}

	/**
	 * Toggles the running flag.
	 * 
	 * @param runToggled
	 *            the flag
	 */
	public void setRunToggled(boolean runToggled) {
		this.runToggled = runToggled;
	}

	/**
	 * Gets whether or not run is toggled.
	 * 
	 * @return run toggled
	 */
	public boolean isRunToggled() {
		if (movementLock != null)
			return false;
		return runToggled;
	}

	/**
	 * Toggles running for the current path only.
	 * 
	 * @param runPath
	 *            the flag
	 */
	public void setRunPath(boolean runPath) {
		this.runPath = runPath;
	}

	/**
	 * Gets whether or not we're running for the current path.
	 * 
	 * @return running
	 */
	public boolean isRunPath() {
		return runPath;
	}

	public void resetOnWalkPacket() {
		if (entity.isPlayer()) {
			Player player = (Player) entity;
			player.getAttributes().put("isBanking", Boolean.FALSE);
			player.getAttributes().put("isShopping", Boolean.FALSE);
			TradeManager.declineTrade(player);
			if (player.questStage[0] == 1)
				player.getActionSender().removeInterfaces();
		}
		entity.getUpdateFlags().faceEntity(65535);
	}

	public boolean canWalk() {
		return canWalk && !entity.isTeleblocked() && !entity.cantTeleport();
	}

	public void setCanWalk(boolean bool) {
		canWalk = bool;
	}

	/**
	 * An internal Position type class with support for direction.
	 * 
	 * @author blakeman8192
	 */
	public class Point extends Position {

		private int direction;

		/**
		 * Creates a new Point.
		 * 
		 * @param x
		 *            the X coordinate
		 * @param y
		 *            the Y coordinate
		 * @param direction
		 *            the direction to this point
		 */
		public Point(int x, int y, int direction) {
			super(x, y);
			setDirection(direction);
		}

		/**
		 * Sets the direction.
		 * 
		 * @param direction
		 *            the direction
		 */
		public void setDirection(int direction) {
			this.direction = direction;
		}

		/**
		 * Gets the direction.
		 * 
		 * @return the direction
		 */
		public int getDirection() {
			return direction;
		}

	}

	public static final int[][] unclipped = {{2491, 10146},
		{2491, 10147},
		{2491, 10148},
		{2491, 10162},
		{2491, 10163},
		{2491, 10164},
		{2491, 10130},
		{2491, 10131},
		{2491, 10132},
		{2809, 3437},
		{2543, 10143},
		{2545, 10141},
		{2545, 10145},
		{3225, 3238},
		};
	
	public boolean walkCheck(int x, int y) {
		boolean unclip = false;
		for (int[] clip : unclipped) {
			if (entity.getPosition().getX() + x == clip[0] && entity.getPosition().getY() + y == clip[1]) {
				unclip = true;
				break;
			}
		}
		if (!unclip && !entity.canMove(entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getX() + x, entity.getPosition().getY() + y, entity.getPosition().getZ(), entity.getSize(), entity.getSize())) {
			return false;
		}
		// Prevents npc from walking into other npcs while in combat
		//if (entity.isNpc() && entity.getFollowingEntity() != null) {
		// Prevents npc from walking into other npcs
		if (entity.isNpc()) {//was commented out, possibly bugged??
			Npc n = (Npc) entity;
			if (n.walkIntoNpc(x, y)) {
				return false;
			}
		}
		// Prevents walking if in range of attack
		if (entity.getCombatingEntity() != null) {
			return !Following.withinRange(entity, entity.getCombatingEntity());
		}
		// Prevents walking if in range of duel/trade
		/*if (entity.getInteractingEntity() != null) {
			return !Following.withinRange(entity, entity.getInteractingEntity());
		}*/
		return true;
	}

}