package com.rs2.model.npcs;

import java.util.ArrayList;
import java.util.Random;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.DailyTask;
import com.rs2.model.content.Following;
import com.rs2.model.content.GeOffer;
import com.rs2.model.content.combat.AttackType;
import com.rs2.model.content.combat.CombatScript;
import com.rs2.model.content.dungeons.Abyss;
import com.rs2.model.content.randomevents.EventsConstants;
import com.rs2.model.content.treasuretrails.ClueScroll;
import com.rs2.model.content.treasuretrails.KeyToClue;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.drop.NpcDropController;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;
import com.rs2.util.clip.Region;
import com.rs2.util.Area;

/**
 * A non-player-character.
 * 
 * @author blakeman8192
 */
public class Npc extends Entity {

	private int npcId;
	private int originalNpcId;
	private NpcDefinition definition;
	private Position minWalk = new Position(0, 0);
	private Position maxWalk = new Position(0, 0);
	private Position spawnPosition;
	private WalkType walkType = WalkType.STAND;
	private int currentX;
	private int currentY;
	private int hp;
	private int transformId;
	private int transformTimer;
	private int face;
	private boolean isVisible = true;
	private boolean transformUpdate;
	private boolean needsRespawn;
	private boolean realNpc;
	private int ownerIndex;
    private int hpRenewalTimer;
    private boolean stayStill = false;
    private Entity alwaysFollow = null;
    private int tick = -1;
    
    public Player linkedPlayer = null;

	public boolean teleported = false;

	public boolean hitByAirBlast = false;
	public boolean hitByWaterBlast = false;
	public boolean hitByEarthBlast = false;
	public boolean hitByFireBlast = false;
	
	private NpcCombatDef npcCombatDef;
	
	static Random foo = new Random();

	private static int AMOUNT_NPCS_ADDED = 0;
	
	public static int[] npcsTransformOnAggression = {1266, 1268, 2453, 2886, 2890};

	public static int[] npcsDontWalk = {1266, 1268, 2453, 2886, 2890, 1827, 2892, 2894, 2896};

	public static int[] npcsDontFollow = {1827, 2892, 2894, 2896};

	public static int[] npcsDontAttack = {2440, 2443, 2446};
	
	public static int[] npcsStayStill = {767,//cat box
		1274, 1275, 1276, 1277, //fremennik ppl
		1826, 1827, //hole in the wall, wall beast
		//tree ents
		1740, 1746, 1719, 1720, 1721, 1721, 1721, 1722, 1722, 1722, 1723, 1727, 1727, 1728, 1729, 1741, 1742, 1743, 1743, 1744, 1744, 1744, 1732, 1725, 1727, 1745, 1721, 1719, 1721, 1722, 1726, 1747, 1747, 1747, 1722, 1727, 1727, 1739,
		1736, 1737, 1737, 1738, 2535, 1735, 1749, 1750, 2534, 1734};

	 public boolean isStayStill() {
		 return stayStill;
	 }

	 public void setStayStill(boolean stayStill) {
		 this.stayStill = stayStill;
	 }
	 
	 public Entity getAlwaysFollow() {
		 return alwaysFollow;
	 }

	 public void setAlwaysFollow(Entity alwaysFollow) {
		 this.alwaysFollow = alwaysFollow;
	 }
	
	/**
	 * Creates a new Npc.
	 * 
	 * @param npcId
	 *            the NPC ID
	 */
	public Npc(int npcId) {
		NpcDefinition definition = World.getDefinitions()[npcId];
		setNpcId(npcId);
		setRealNpc(true);
		setOriginalNpcId(npcId);
		getUpdateFlags().setUpdateRequired(true);
		this.definition = definition == null ? NpcDefinition.produceDefinition(npcId) : definition;
		initAttributes();
		setAttackType(AttackTypes.MELEE);
		setUniqueId(AMOUNT_NPCS_ADDED++);
		this.npcCombatDef = NpcCombatDef.getDef(npcId);
		hp = getMaxHp();
		handleSpecials();
	}

    /**
	 * Creates a new Npc.
	 *
	 * @param npcId
	 *            the NPC ID
	 */
	public Npc(NpcDefinition definition, int npcId, CombatScript npcCombatScript) {
		setNpcId(npcId);
		setRealNpc(true);
		setOriginalNpcId(npcId);
		getUpdateFlags().setUpdateRequired(true);
        this.definition = definition == null ? NpcDefinition.produceDefinition(npcId) : definition;
		initAttributes();
		setAttackType(AttackTypes.MELEE);
		setUniqueId(AMOUNT_NPCS_ADDED++);
		this.npcCombatDef = NpcCombatDef.getDef(npcId);
		hp = getMaxHp();
        setCombatScript(npcCombatScript);
        handleSpecials();
	}

	@Override
	public void initAttributes() {
		getAttributes().put("doDamage", Boolean.FALSE);
		getAttributes().put("canTakeDamage", Boolean.TRUE);
	}

	@Override
	public void process() {
		handleTransformTick();
		expireHitRecords();
		npcRandomWalk();
		restoreHp();
        getFollowing().followTick();
        ownerCheck();
        if(this.tick == 0){
        	this.setVisible(false);
        	World.unregister(this);
        }
        if(this.tick > 0)
        	tick--;
	}

	public int getTick() {
		return tick;
	}

	public void setTick(int tick) {
		this.tick = tick;
	}

	public void handleSpecials(){
		if(this.getNpcId() == 896){
			forceTravelPath(new Position[] {new Position(2904,3463,0), new Position(2930,3463,0)});
		}
	}
	
	public void restoreHp() {
		if (hpRenewalTimer < 1 && !isDead()) {
			heal(1);
			hpRenewalTimer = 100;
		} else {
			hpRenewalTimer--;
		}
	}

	public void teleportPlayer(final Player player, final int x, final int y, final int z, String shout) {
		player.getDialogue().endDialogue();
		player.getActionSender().removeInterfaces();
		getUpdateFlags().sendAnimation(1818);
		getUpdateFlags().sendGraphic(343);
		if (shout != null)
			getUpdateFlags().setForceChatMessage(shout);
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.getTeleportation().teleportObelisk(x, y, z);
				container.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
			}
		}, 4);
	}
	
	public void teleportPlayerKolodion(final Player player, final int x, final int y, final int z, String shout) {
		player.getDialogue().endDialogue();
		player.getActionSender().removeInterfaces();
		final Npc npc = new Npc(907+player.kolodionStage);
		player.setSpawnedNpc(npc);
		getUpdateFlags().sendAnimation(717);
		if (shout != null)
			getUpdateFlags().setForceChatMessage(shout);
		player.getTeleportation().forcedObjectTeleport(x, y, z, null);
		final Tick timer4 = new Tick(12) {
            @Override
            public void execute() {
            	NpcLoader.spawnNpcPos(player, new Position(3106,3934,0), npc, true, false);
				npc.getUpdateFlags().sendGraphic(86, 0);
				if(player.kolodionStage == 0)
					npc.getUpdateFlags().setForceChatMessage("You must prove yourself... now!");
				else
					npc.getUpdateFlags().setForceChatMessage("Let us continue with our battle.");
				stop();
            }
        };
        World.getTickManager().submit(timer4);
	}

	public void teleportPlayerRunecraft(final Player player, final int x, final int y, final int z, String shout) {
		player.getDialogue().endDialogue();
		player.getActionSender().removeInterfaces();
		int id = this.getDefinition().getId();
		int anim = (id == 171 ? 200 : 717);
		getUpdateFlags().sendAnimation(anim);
		getUpdateFlags().sendGraphic(108);
		if (shout != null)
			getUpdateFlags().setForceChatMessage(shout);
		player.setStopPacket(true);
		player.getAttributes().put("canTakeDamage", Boolean.FALSE);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.getTeleportation().teleportRunecraft(x, y, z);
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, 4);
	}

	public void sendPlayerAway(final Player player, int emoteId, int playerEmote, final int x, final int y, final int z, String shout, final boolean disappear) {
		player.getDialogue().endDialogue();
		player.getActionSender().removeInterfaces();
		getUpdateFlags().sendAnimation(emoteId);
		player.getUpdateFlags().sendAnimation(playerEmote);
		player.getActionSender().sendInterface(8677);
		final Npc npc = this;
		if (shout != null)
			getUpdateFlags().setForceChatMessage(shout);
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.teleport(new Position(x, y, z));
				player.getActionSender().removeInterfaces();
				player.getUpdateFlags().sendAnimation(65535, 0);
				if (disappear) {
					player.getActionSender().sendStillGraphic(EventsConstants.RANDOM_EVENT_GRAPHIC, player.getSpawnedNpc().getPosition(), 100 << 16);
					NpcLoader.destroyNpc(npc);
				}
				container.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
			}
		}, 4);
	}

	@Override
	public void reset() {
		getUpdateFlags().sendAnimation(-1);
		getUpdateFlags().reset();
		transformUpdate = false;
		setPrimaryDirection(-1);
		getUpdateFlags().faceEntity(-1);
	}

	public void heal(int healAmount) {
		if (getCurrentHp() + healAmount >= getMaxHp()) {
			setCurrentHp(getMaxHp());
		} else {
			setCurrentHp(getCurrentHp() + healAmount);
		}
	}

	/**
	 * Makes walkable npcs walk, then updates it's position.
	 */
	public void npcRandomWalk() {
		if (this == null || isDead())
			return;
		impTele();
		if (isAttacking() || getFollowingEntity() != null || getInteractingEntity() != null || getCombatingEntity() != null)
			return;
		if (isDontWalk()) {
			if(travellingPath != null)
				handleTravelPath();
			if((forceX != -1 && forceY != -1) || !this.getMovementHandler().getWaypoints().isEmpty())
				handleForceMovement();
			return;
		}
		if (getWalkType() == WalkType.STAND) {
			getUpdateFlags().sendFaceToDirection(getFacingDirection(getPosition(), getFace()));
		} else if (!isFrozen() && !isStunned() && Misc.random(6) == 0) {
            int x = minWalk.getX(), y = minWalk.getY(), width = maxWalk.getX()-minWalk.getX(), length = maxWalk.getY()-minWalk.getY();
            int x1 = Misc.getRandom().nextInt(width), y1 = Misc.getRandom().nextInt(length);
            Position position = new Position(x+x1, y+y1, getPosition().getZ());
            walkTo(position, true);
		}
		randomShouts();
	}
	
	void handleForceMovement(){
		if(travellingPath != null){
			getMovementHandler().reset();
			getMovementHandler().addToPath(travellingPath[travellingPathGoal]);
			getMovementHandler().finish();
			return;
		}
		if((this.getPosition().getX() == forceX && this.getPosition().getY() == forceY) || (this.getPosition().getX() == this.getMovementHandler().getWaypoints().getLast().getX() && this.getPosition().getY() == this.getMovementHandler().getWaypoints().getLast().getY())){
			this.forceX = -1;
			this.forceY = -1;
			this.setDontWalk(false);
			System.out.println("Npc force-movement end");
		}
	}
	
	int forceX = -1;
	int forceY = -1;
	
	Position travellingPath[];
	int travellingPathGoal = -1;
	
	void handleTravelPath(){
		//System.out.println("ds "+travellingPathGoal+" "+getMovementHandler().canWalk());
		if(this.getPosition().getX() == travellingPath[travellingPathGoal].getX() && this.getPosition().getY() == travellingPath[travellingPathGoal].getY()){
			if(travellingPath.length > travellingPathGoal+1)
				travellingPathGoal++;
			else
				travellingPathGoal = 0;
			getMovementHandler().reset();
			//getMovementHandler().unlock();
			getMovementHandler().addToPath(travellingPath[travellingPathGoal]);
			getMovementHandler().finish();
		}
	}
	
	public void forceTravelPath(Position[] positions){
		setDontWalk(true);
		travellingPath = positions;
		travellingPathGoal = 0;
		try{
			getMovementHandler().reset();
			getMovementHandler().addToPath(travellingPath[travellingPathGoal]);
			getMovementHandler().finish();
		}catch(Exception ex){}
	}
	
	public void forceMovePath(Position[] positions){
		setDontWalk(true);
		getMovementHandler().reset();
		for (Position position : positions) {
			getMovementHandler().addToPath(position);
		}
		forceX = this.getMovementHandler().getWaypoints().getLast().getX();
		forceY = this.getMovementHandler().getWaypoints().getLast().getY();
		getMovementHandler().finish();
	}
	
	public void impTele(){
		int chance = 30;
		if(this.teleported)
			return;
		if (getNpcId() == 708) {
			if (Misc.random_(chance) == 0){
				int x = this.currentX+(Misc.random_(2) == 0 ? (-3-Misc.random_(4)) : (3+Misc.random_(4)));
				int y = this.currentY+(Misc.random_(2) == 0 ? (-3-Misc.random_(4)) : (3+Misc.random_(4)));
				if(Region.getClipping(x, y, this.getPosition().getZ()) == 0){
					getUpdateFlags().sendGraphic(86, 25);
					teleport(new Position(x, y));
				}
			}
		}
	}
	
	public void randomShouts(){
		int chance = 75;
		if (getNpcId() == 1765 || getNpcId() == 43) {
			if (Misc.random_(chance) == 0)
				getUpdateFlags().setForceChatMessage("Baa!");
		}
		if (getNpcId() == 81) {
			if (Misc.random_(chance) == 0)
				getUpdateFlags().setForceChatMessage("Moo!");
		}
		if (getNpcId() == 767) {
			if (Misc.random_(chance) == 0)
				getUpdateFlags().setForceChatMessage("Mew!");
		}
		chance = 30;
		if (getNpcId() == 3863) {//ge clerk
			if (Misc.random_(chance) == 0){
				if(Misc.random_(2) == 0){//sell
					if(GeOffer.listOfSellOffers.size() == 0)
						return;
					int idx = Misc.random_(GeOffer.listOfSellOffers.size());
					GeOffer go = GeOffer.listOfSellOffers.get(idx);
					String itemName = new Item(go.getItemId(), 1).getDefinition().getName();
					getUpdateFlags().setForceChatMessage(itemName+" is being sold on GE!");
				} else {//buy
					if(GeOffer.listOfBuyOffers.size() == 0)
						return;
					int idx = Misc.random_(GeOffer.listOfBuyOffers.size());
					GeOffer go = GeOffer.listOfBuyOffers.get(idx);
					String itemName = new Item(go.getItemId(), 1).getDefinition().getName();
					getUpdateFlags().setForceChatMessage(itemName+" is being bought on GE!");
				}
			}
		}
	}

	public void ownerCheck() {
		if (this == null) {
			return;
		}
		if (getPlayerIndex() > 0) {
			if (!this.isDead() && (getPlayerOwner() == null || (!Misc.goodDistance(getPosition(), getPlayerOwner().getPosition(), 15) && this.getNpcId() != 3098))) {
				NpcLoader.destroyNpc(this);
			}
			return;
		}
	}

	public int getTravelFace(){
		int destX = travellingPath[travellingPathGoal].getX();
		int destY = travellingPath[travellingPathGoal].getY();
		int diffX = this.getPosition().getX() - destX;
		int diffY = this.getPosition().getY() - destY;
		if(Math.abs(diffX) > Math.abs(diffY)){
			return (diffX < 0 ? Constants.EAST : Constants.WEST);
		}else{
			return (diffY < 0 ? Constants.NORTH : Constants.SOUTH);
		}
	}
	
	public static Position getFacingDirection(Position position, int facingType) {
		int x = position.getX();
		int y = position.getY();
		switch (facingType) {
			case 2 :
				return new Position(x, y + 1);
			case 3 :
				return new Position(x, y - 1);
			case 4 :
				return new Position(x + 1, y);
			case 5 :
				return new Position(x - 1, y);
		}
		return new Position(x, y - 1);
	}

	/**
	 * Adds to the NPCs position.
	 */
	public void appendNpcPosition(int xModifier, int yModifier) {
		currentX += xModifier;
		currentY += yModifier;
		getPosition().move(xModifier, yModifier);
	}

	public void sendTransform(int transformId, int transformTicks) {
		//NpcDefinition def = World.getDefinitions()[transformId];
		this.transformId = transformId;
		setTransformTimer(transformTicks);
		transformUpdate = true;
		setNpcId(transformId);
		getUpdateFlags().setUpdateRequired(true);
		this.definition = World.getDefinitions()[transformId];
		this.npcCombatDef = NpcCombatDef.getDef(transformId);
		hp = getMaxHp();
	}

	/**
	 * Sets the NPC ID.
	 * 
	 * @param npcId
	 *            the npcId
	 */
	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	/**
	 * Gets the NPC ID.
	 * 
	 * @return the npcId
	 */
	public int getNpcId() {
		return npcId;
	}

	public int getOriginalNpcId() {
		return originalNpcId;
	}

	public void setOriginalNpcId(int originalNpcId) {
		this.originalNpcId = originalNpcId;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public int getPlayerIndex() {
		return ownerIndex;
	}

	public Player getPlayerOwner() {
		if(ownerIndex == -1){
			this.setVisible(false);
			World.unregister(this);
			return null;
		}
		return World.getPlayers()[ownerIndex];
	}

	public void setPlayerOwner(int playerIndex) {
		this.ownerIndex = playerIndex;
	}

	public void setMinWalk(Position minWalk) {
		this.minWalk = minWalk;
	}

	public Position getMinWalk() {
		return minWalk;
	}

	public void setMaxWalk(Position maxWalk) {
		this.maxWalk = maxWalk;
	}

	public Position getMaxWalk() {
		return maxWalk;
	}

	public void setWalkType(WalkType walkType) {
		this.walkType = walkType;
	}

	public WalkType getWalkType() {
		return walkType;
	}

    public void setTransformUpdate(boolean transformUpdate) {
		this.transformUpdate = transformUpdate;
	}

	public boolean isTransformUpdate() {
		return transformUpdate;
	}

	public void setTransformId(int transformId) {
		this.transformId = transformId;
	}

	public int getTransformId() {
		return transformId;
	}

	public int getRespawnTimer() {
		return npcCombatDef.getSpawnDelay();
	}

	public void setNeedsRespawn(boolean needsRespawn) {
		this.needsRespawn = needsRespawn;
	}

	public boolean needsRespawn() {
		return needsRespawn;
	}

	public void setSpawnPosition(Position spawnPosition) {
		this.spawnPosition = spawnPosition;
	}

	public Position getSpawnPosition() {
		return spawnPosition;
	}

	public void setCurrentX(int currentX) {
		this.currentX = currentX;
	}

	public int getCurrentX() {
		return currentX;
	}

	public void setCurrentY(int currentY) {
		this.currentY = currentY;
	}

	public int getCurrentY() {
		return currentY;
	}

	@Override
	public int getCurrentHp() {
		return hp;
	}

	@Override
	public int getMaxHp() {
		return definition.getHitpoints();
	}

	@Override
	public int getDeathAnimation() {
		return definition.getDeathAnim();
	}
	
	@Override
	public int getAtkSound() {
		return definition.getAtkSound();
	}
	
	@Override
	public int getBlockSound() {
		return definition.getBlockSound();
	}
	
	@Override
	public int getDeathSound() {
		return definition.getDeathSound();
	}

	@Override
	public int getBlockAnimation() {
		return definition.getBlockAnim();
	}

	@Override
	public int getDeathAnimationLength() {
		return npcCombatDef.getDeathAnimationLength();
	}

	@Override
	public int getBaseAttackLevel(AttackType attackType) {
		return (int) (definition.getCombat() / 2);
	}

	@Override
	public int getBaseDefenceLevel(AttackType attackType) {
		return (int) (definition.getCombat() / 2);
	}

	@Override
	public boolean isProtectingFromCombat(AttackType attackType, Entity attacker) {
		if (attackType == AttackType.MELEE)
			return this.getDefinition().isProtectMelee();
		else if (attackType == AttackType.RANGED)
			return this.getDefinition().isProtectRanged();
		else if (attackType == AttackType.MAGIC)
			return this.getDefinition().isProtectMagic();
		return false;
	}

    @Override
    public void teleport(Position position) {
    	setVisible(false);
    	teleported = true;
        setPosition(position);
		getMovementHandler().reset();
		setVisible(true);
    }

    @Override
	public void setCurrentHp(int hp) {
		this.hp = hp;
	}

	@Override
	public void dropItems(Entity killer) {
			if (killer != null) {
				for (Item item : NpcDropController.getDrops(killer, getNpcId(), false)) {
					if (item != null) {
						if(item.getId() == -1 || item.getCount() <= 0)
							continue;
						if(item.getCount() > 1 && item.getDefinition().isNoted() == false && item.getDefinition().isStackable() == false){
							for(int r1 = 0; r1 < item.getCount(); r1++){
								GroundItem drop = new GroundItem(new Item(item.getId(), 1), this, killer, getDeathPosition());
								GroundItemManager.getManager().dropItem(drop);
							}
						} else {
							GroundItem drop = new GroundItem(new Item(item.getId(), item.getCount() < 1 ? 1 : item.getCount()), this, killer, getDeathPosition());
							GroundItemManager.getManager().dropItem(drop);
						}
					}
				}
                if (killer.isPlayer()){
                	((Player)killer).getQuestHandler().findQuestDrop(getNpcId(), (Player)killer, getDeathPosition());
                    Abyss.dropPouches((Player)killer, this);
                    KeyToClue.dropKey((Player)killer, this);
                    DailyTask task = ((Player)killer).getDailyTask();
					if(task.getTaskTarget() == getNpcId() && ((Player)killer).dailyTaskAmount < task.getReqAmount()){
						((Player)killer).dailyTaskAmount++;
						if(((Player)killer).dailyTaskAmount == task.getReqAmount())
							task.completed(((Player)killer));
					}
                }
			}
		}
	
	public NpcCombatDef getCombatDef() {
		return npcCombatDef;
	}

	public NpcDefinition getDefinition() {
		return definition;
    }

    public enum WalkType {
		STAND, WALK
	}

	public int getFace() {
		return face;
	}

	public void setFace(int face) {
		this.face = face;
	}

	public static Npc getNpcById(int npcId) {
		Npc npc_ = null;
		for (Npc npc : World.getNpcs()) {
			if (npc == null)
				continue;
			if(npc.isDead()){
				if(npc.getDefinition().getId() == npcId)
					npc_ = npc;
				continue;
			}
			if (npc.getDefinition().getId() == npcId)
				return npc;
		}
		return npc_;
	}
	
	public static Npc[] getNpcsInArea(Area area) {
		ArrayList<Npc> listOfNpcs = new ArrayList<Npc>();
		for (Npc npc : World.getNpcs()) {
			if (npc == null)
				continue;
			if(npc.isDead()){
				continue;
			}
			if (area.containsBorderIncluded(npc.getPosition()))
				listOfNpcs.add(npc);
		}
		return listOfNpcs.toArray(new Npc[listOfNpcs.size()]);
	}
	
	public static Npc getNpcByIdAndPos(int npcId, Position pos) {
		for (Npc npc : World.getNpcs()) {
			if (npc == null)
				continue;
			if (npc.getDefinition().getId() == npcId){
				if(npc.getPosition().equals(pos))
					return npc;
			}
		}
		return null;
	}

	public static void reloadTransformedNpcs(Player player) {
		for (Npc npc : World.getNpcs()) {
			if (npc == null || !Misc.goodDistance(player.getPosition(), npc.getPosition(), 25) || player.getPosition().getZ() != npc.getPosition().getZ())
				continue;
			if (npc.getTransformTimer() > 0) {
				npc.getUpdateFlags().setUpdateRequired(true);
			}
		}
	}

	public int getTransformTimer() {
		return transformTimer;
	}

	public void setTransformTimer(int transformTimer) {
		this.transformTimer = transformTimer;
	}

	public void handleTransformTick() {
		if (getTransformTimer() > 0 && getTransformTimer() < 999999) {
			setTransformTimer(getTransformTimer() - 1);
			if (getTransformTimer() < 1) {
				sendTransform(getOriginalNpcId(), 0);
			}
		}
	}

	public void setRealNpc(boolean realNpc) {
		this.realNpc = realNpc;
	}

	public boolean isRealNpc() {
		return realNpc;
	}

    public boolean checkWalk(int dirX, int dirY) {
    	if (!canMove(dirX, dirY)) {
    		return false;
    	}
    	/*if (walkIntoNpc(dirX, dirY)) {
    		return false;
    	}*/
    	return true;
    }

	public boolean walkIntoNpc(int dirX, int dirY) {
		for (Npc npc2 : World.getNpcs()) {
			if (npc2 != null && npc2 != this && !isDead() && !npc2.isDead() && npc2.getPosition().getZ() == getPosition().getZ()) {
				if (Misc.goodDistance(getPosition().getX(), getPosition().getY(), npc2.getPosition().getX(), npc2.getPosition().getY(), npc2.getSize() + getSize())) {
					if (npcInNpc(this, npc2, dirX, dirY)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean npcInNpc(Npc npc1, Npc npc2, int dirX, int dirY) {
		for (int x = npc1.getPosition().getX(); x < npc1.getPosition().getX() + npc1.getSize(); x++) {
			for (int y = npc1.getPosition().getY(); y < npc1.getPosition().getY() + npc1.getSize(); y++) {
				for (int x2 = npc2.getPosition().getX(); x2 < npc2.getPosition().getX() + npc2.getSize(); x2++) {
					for (int y2 = npc2.getPosition().getY(); y2 < npc2.getPosition().getY() + npc2.getSize(); y2++) {
						if (x2 == x + dirX && y2 == y + dirY) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public void resetActions() {
		getTask();
		setInteractingEntity(null);
		setCombatingEntity(null);
		setSkilling(null);
		getUpdateFlags().faceEntity(-1);
		Following.resetFollow(this);
	}

	public boolean getCorrectStandPosition(Position pos, int size) {
		int x = getPosition().getX();
		int y = getPosition().getY();
		int h = getPosition().getZ();
		switch(face) {
			case 2 :
				return pos.equals(new Position(x, y + size, h));
			case 3 :
				return pos.equals(new Position(x, y - size, h));
			case 4 :
				return pos.equals(new Position(x + size, y, h));
			case 5 :
				return pos.equals(new Position(x - size, y, h));
		}
		return Misc.goodDistance(getPosition(), pos, 1);
	}

	public Position getCorrectStandPosition(int size) {
		int x = getPosition().getX();
		int y = getPosition().getY();
		int h = getPosition().getZ();
		switch(face) {
			case 2 :
				return new Position(x, y + size, h);
			case 3 :
				return new Position(x, y - size, h);
			case 4 :
				return new Position(x + size, y, h);
			case 5 :
				return new Position(x - size, y, h);
		}
		return new Position(x, y + size, h);
	}

	public boolean isBoothBanker() {
		if (face == 1) {
			return false;
		}
		return getNpcId() == 166 || getNpcId() == 494 || getNpcId() == 495 || getNpcId() == 496 || getNpcId() == 499 || getNpcId() == 2619;
	}

	public static boolean isUndeadNpc(Entity victim) {
		if (victim.isPlayer()) {
			return false;
		}
		String name = ((Npc) victim).getDefinition().getName().toLowerCase();
		if (name.contains("spectre") || name.contains("banshee") || name.contains("shade") || name.contains("zombie") || name.contains("skeleton") || name.contains("ghost") || name.contains("crawling hand") || name.contains("skeletal hand") || name.contains("zombie hand") || name.contains("zogre") || name.contains("skorge") || name.contains("ankous")) {
			return true;
		}
		return false;
	}

}
