package com.rs2.model.players.item.functions;

import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.GameObjectData;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.AttackType;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.combat.weapon.AttackStyle;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.npcs.Npc;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;
import com.rs2.util.clip.Region;

/**
 * @author Austin
 */
public class Cannon {

    private final int MAX_RANGE = 15;
    private final int MAX_DAMAGE = 30;

    private final int CANNON_BASE_OBJECT = 7;
    private final int CANNON_STAND_OBJECT = 8;
    private final int CANNON_BASE_SECONDARY = 9;
    private final int DWARF_MULTICANNON = 6;
    private final int CANNONBALLS = 2;
    private final Item[] REQUIRED_ITEMS = new Item[]{new Item(6, 1), new Item(8, 1), new Item(10, 1), new Item(12, 1)};
    private final int NORTH_WEST_ANIM = 521, NORTH_ANIM = 514, NORTH_EAST_ANIM = 515, EAST_ANIM = 516, SOUTH_EAST_ANIM = 517, SOUTH_ANIM = 518, SOUTH_WEST_ANIM = 519, WEST_ANIM = 520;

    private Player player;
    private Position position;
    private int setUpAmount = 4;
    private int cannonballsLeft = 0;
    private int currentAnim = NORTH_ANIM;
    private boolean settingUpCannon = false;
    private Rotation rotation;
    private boolean firing = false;

    public Cannon(Player player) {
        this.player = player;
    }

    public void tick() {
        if (getCannonballsLeft() <= 0 || getPosition() == null || !isFiring()) {
            return;
        }
        currentAnim++;
        if (currentAnim == 522) {
            currentAnim = 514;
        }
        setRotationForAnim(currentAnim);
        fireAtMobs();

        for (Player player : World.getPlayers()) {
            if (player == null) {
                continue;
            }
            player.getActionSender().animateGameObject(getPosition().getX(), getPosition().getY(), getPosition().getZ(), currentAnim);
        }
    }

    /**
     * Fires at a targeted mob and decrements the amount of cannonballs the
     * cannon has by one for each mob hit.
     */
    private void fireAtMobs() {

        /**
         * Target a mob
         */
        Npc hit = findAttackableNpc();

        /**
         * Get the damage to deal
         */
        int damage = Misc.random(MAX_DAMAGE);

        /**
         * Deal the damage based on if you are in a single or multi-combat area.
         */
        if (hit != null) {
            if (!hit.getDefinition().isAttackable() || hit.getNpcId() == 1266 || hit.getNpcId() == 1268) {
                return;
            }
            if (!Misc.checkClip(getPosition(), hit.getPosition(), false)) {
                return;
            }
            if (player.inMulti() || player.inWild()) {
                cannonProjectile(hit);
            } else {
                if ((!hit.getInCombatTick().completed() || !player.getInCombatTick().completed()) && hit != player.getFollowingEntity())
                    return;
                cannonProjectile(hit);
            }
            World.submit(new Tick(2) {
                @Override
                public void execute() {
                    final int dmg = damage > hit.getCurrentHp() ? (damage - hit.getCurrentHp()) : damage;
                    hit.hit(player, dmg, HitType.NORMAL, new AttackStyle(AttackType.RANGED, AttackStyle.Mode.RANGED_ACCURATE, AttackStyle.Bonus.RANGED));

                    hit.getInCombatTick().reset();
                    if (!hit.isDead()) {
                        player.getSkill().addExp(Skill.RANGED, dmg * 4);
                    }
                    if (player.getPosition().isViewableFrom(hit.getPosition())) {
                        CombatManager.attack(hit, player);
                    }
                    this.stop();
                }
            });
            cannonballsLeft--;
            if (cannonballsLeft <= 0) {
                player.getActionSender().sendMessage("You cannon has run out of ammo!");
            }
        }
    }

    /**
     * Fires the cannonballs (well not really, it just makes it so that we
     * actually see cannonball projectiles)
     */
    private void cannonProjectile(Npc n) {
        int oX = getPosition().getX() + 1;
        int oY = getPosition().getY() + 1;
        int offX = ((oX - n.getPosition().getX()) * -1);
        int offY = ((oY - n.getPosition().getY()) * -1);

        World.sendProjectile(new Position(oX, oY, getPosition().getZ()), offX, offY, 53, 35, 20, 85, n.getIndex() + 1, false);
    }

    /**
     * Locates npcs to attack.
     *
     * @return the npc to attack.
     */
    private Npc findAttackableNpc() {
        for (Npc npc : World.getNpcs()) {
            if (npc == null || npc.isDead()) {
                continue;
            }
            int myX = getPosition().getX();
            int myY = getPosition().getY();
            int theirX = npc.getPosition().getX();
            int theirY = npc.getPosition().getY();

            if (npc.getPosition().withinDistance(getPosition(), MAX_RANGE)) {
                if (getRotation() == null) {
                    setRotation(Rotation.NORTH);
                }

                switch (rotation) {
                    case NORTH:
                        if (theirY > myY && theirX >= myX - 1 && theirX <= myX + 1)
                            return npc;
                        break;
                    case NORTH_EAST:
                        if (theirX >= myX + 1 && theirY >= myY + 1)
                            return npc;
                        break;
                    case EAST:
                        if (theirX > myX && theirY >= myY - 1 && theirY <= myY + 1)
                            return npc;
                        break;
                    case SOUTH_EAST:
                        if (theirY <= myY - 1 && theirX >= myX + 1)
                            return npc;
                        break;
                    case SOUTH:
                        if (theirY < myY && theirX >= myX - 1 && theirX <= myX + 1)
                            return npc;
                        break;
                    case SOUTH_WEST:
                        if (theirX <= myX - 1 && theirY <= myY - 1)
                            return npc;
                        break;
                    case WEST:
                        if (theirX < myX && theirY >= myY - 1 && theirY <= myY + 1)
                            return npc;
                        break;
                    case NORTH_WEST:
                        if (theirX <= myX - 1 && theirY >= myY + 1)
                            return npc;
                        break;
                }
            }
        }
        return null;
    }

    /**
     * Sets the cannon rotation based on its current animation.
     *
     * @param anim the animation ID.
     */
    private void setRotationForAnim(int anim) {
        switch (anim) {
            case NORTH_WEST_ANIM:
                setRotation(Rotation.NORTH_WEST);
                break;
            case NORTH_ANIM:
                setRotation(Rotation.NORTH);
                break;
            case NORTH_EAST_ANIM:
                setRotation(Rotation.NORTH_EAST);
                break;
            case EAST_ANIM:
                setRotation(Rotation.EAST);
                break;
            case SOUTH_EAST_ANIM:
                setRotation(Rotation.SOUTH_EAST);
                break;
            case SOUTH_ANIM:
                setRotation(Rotation.SOUTH);
                break;
            case SOUTH_WEST_ANIM:
                setRotation(Rotation.SOUTH_WEST);
                break;
            case WEST_ANIM:
                setRotation(Rotation.WEST);
                break;
        }
    }

    /**
     * Executes a tick sequence for setting up your cannon.
     */
    public void setUpCannon() {
        if (getPosition() != null) {
            player.getActionSender().sendMessage("You already have a cannon set up!");
            return;
        }
        Position position1 = new Position(player.getPosition().getX() - 3, player.getPosition().getY(), player.getPosition().getZ());
        CacheObject cacheObject = ObjectLoader.object(position1.getX(), position1.getY(), position1.getZ());
        if (cacheObject != null && GameObjectData.forId(cacheObject.getDef().getId()).getName().length() > 0) {
            player.getActionSender().sendMessage("You can't set up your cannon here.");
            return;
        }
        for (int x = position1.getX() - 2; x < position1.getX() + 2; x++) {
            for (int y = position1.getY(); y < position1.getY() + 3; y++) {
                CacheObject object = ObjectLoader.object(x, y, position1.getZ());
                if (ObjectHandler.getInstance().getObject(x, y, position1.getZ()) != null || (object != null && (GameObjectData.forId(object.getDef().getId()) != null && GameObjectData.forId(object.getDef().getId()).getName().length() > 3))) {
                    player.getActionSender().sendMessage("You can't set up your cannon here.");
                    return;
                }
            }
        }
        if (!Region.canMove(position1.getX() - 2, position1.getY() -2, position1.getX() + 2, position1.getY() + 2, player.getPosition().getZ(), 1, 1)) {
            player.getActionSender().sendMessage("You can't set up your cannon here.");
            return;
        }
        if (ObjectHandler.getInstance().getObject(position1.getX(), position1.getY(), position1.getZ()) != null) {
            player.getActionSender().sendMessage("You can't set up your cannon here.");
            return;
        }
        for (Player players : World.getPlayers()) {
            if (players == null) {
                continue;
            }
            if (players.getDwarfMultiCannon().getPosition() != null && players.getDwarfMultiCannon().getPosition() == position1) {
                players.getActionSender().sendMessage("You can't set up your cannon here.");
                return;
            }
        }
        int count = 0;
        for (int i = 0; i < REQUIRED_ITEMS.length; i++) {
            for (Item inv : player.getInventory().getItemContainer().getItems()) {
                if (inv == null || !inv.validItem()) {
                    continue;
                }
                if (inv.getId() == REQUIRED_ITEMS[i].getId()) {
                    count++;
                }
            }
        }
        if (count < 4) {
            player.getActionSender().sendMessage("You do not have all of the parts required.");
            return;
        }
        setSettingUpCannon(true);
        player.getUpdateFlags().sendFaceToDirection(new Position(position1.getX(), position1.getY() + 1, position1.getZ()));
        player.setStopPacket(true);
        final Position setUpPosition = position1;
        CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                switch (setUpAmount) {
                    case 4:
                        container.setTick(4);
                        player.getUpdateFlags().sendAnimation(827);
                        CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
                            @Override
                            public void execute(CycleEventContainer container) {
                                new GameObject(CANNON_BASE_OBJECT, setUpPosition.getX(), setUpPosition.getY(), setUpPosition.getZ(), 0, 10, -1, 999999, false);
                                container.stop();
                            }

                            @Override
                            public void stop() {
                            }
                        }, 2);
                        break;
                    case 3:
                        container.setTick(4);
                        player.getUpdateFlags().sendAnimation(827);
                        CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
                            @Override
                            public void execute(CycleEventContainer container) {
                                new GameObject(CANNON_STAND_OBJECT, setUpPosition.getX(), setUpPosition.getY(), setUpPosition.getZ(), 0, 10, -1, 999999, false);
                                container.stop();
                            }

                            @Override
                            public void stop() {
                            }
                        }, 2);
                        break;
                    case 2:
                        container.setTick(4);
                        player.getUpdateFlags().sendAnimation(827);
                        CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
                            @Override
                            public void execute(CycleEventContainer container) {
                                new GameObject(CANNON_BASE_SECONDARY, setUpPosition.getX(), setUpPosition.getY(), setUpPosition.getZ(), 0, 10, -1, 999999, false);
                                container.stop();
                            }

                            @Override
                            public void stop() {
                            }
                        }, 2);
                        break;
                    case 1:
                        container.setTick(2);
                        player.getUpdateFlags().sendAnimation(827);
                        CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
                            @Override
                            public void execute(CycleEventContainer container) {
                                new GameObject(DWARF_MULTICANNON, setUpPosition.getX(), setUpPosition.getY(), setUpPosition.getZ(), 0, 10, -1, 999999, false);
                                container.stop();
                            }

                            @Override
                            public void stop() {
                            }
                        }, 2);
                        break;
                    case 0:
                        for (Item item : REQUIRED_ITEMS) {
                            player.getInventory().removeItem(item);
                        }
                        //World.getCannons().put(Cannon.this, player);
                        setSettingUpCannon(false);
                        setUpAmount = 4;
                        setPosition(setUpPosition);
                        container.stop();
                        break;
                }
                setUpAmount--;
            }

            @Override
            public void stop() {
                player.setStopPacket(false);
            }
        }, 2);
    }


    /**
     * Adds ammo to your cannon.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    public void addAmmo(int x, int y) {
        if (!checkCannon(x, y)) {
            player.getActionSender().sendMessage("This isn't your cannon.");
            return;
        }
        if (!player.getInventory().playerHasItem(CANNONBALLS)) {
            player.getActionSender().sendMessage("You do not have any cannonballs.");
            return;
        }
        if (getCannonballsLeft() == 30) {
            player.getActionSender().sendMessage("There are already 30 cannonballs loaded.");
            return;
        }
        int amountToAdd = player.getInventory().getItemContainer().getCount(CANNONBALLS);
        if (amountToAdd > 30) {
            amountToAdd = 30;
        }
        int amt = getCannonballsLeft();
        if ((amountToAdd + amt) > 30) {
            amountToAdd = (30 - amt);
        }
        setFiring(true);
        setCannonballsLeft(amountToAdd + getCannonballsLeft());
        player.getInventory().removeItem(new Item(CANNONBALLS, amountToAdd));
        player.getActionSender().sendMessage("You load " + amountToAdd + " cannonballs.");
    }

    /**
     * Removes the cannon.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    public void removeCannon(int x, int y, boolean bank) {
        if (!checkCannon(x, y)) {
            player.getActionSender().sendMessage("This isn't your cannon.");
            return;
        }
        if (player.getInventory().getItemContainer().contains(CANNONBALLS) && player.getInventory().getItemContainer().freeSlots() < 4 || !player.getInventory().getItemContainer().contains(CANNONBALLS) && player.getInventory().getItemContainer().freeSlots() < 5) {
            player.getActionSender().sendMessage("You do not have enough inventory space.");
            return;
        }
        if (getPosition() == null) {
            return;
        }
        new GameObject(Constants.EMPTY_OBJECT, getPosition().getX(), getPosition().getY(), getPosition().getZ(), 0, 10, -1, 999999, false);
        ObjectHandler.getInstance().removeObject(getPosition().getX(), getPosition().getY(), getPosition().getZ(), 10);
        currentAnim = NORTH_ANIM;
        setPosition(null);
        if (bank) {
            for (Item item : REQUIRED_ITEMS) {
                player.getBank().add(item);
            }
            player.getBank().add(new Item(CANNONBALLS, getCannonballsLeft()));
        } else {
            for (Item item : REQUIRED_ITEMS) {
                player.getInventory().addItem(item);
            }
            player.getInventory().addItem(new Item(CANNONBALLS, getCannonballsLeft()));
        }
        setCannonballsLeft(0);
    }

    /**
     * Checks the cannon.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @return true if the cannon belongs to you.
     */
    private boolean checkCannon(int x, int y) {
        for (Player players : World.getPlayers()) {
            if (players == null) {
                continue;
            }
            if (players.getDwarfMultiCannon().getPosition() != null) {
                if (player.getDwarfMultiCannon().getPosition() != null) {
                    if (players.getDwarfMultiCannon().getPosition() != getPosition() && players.getDwarfMultiCannon().getPosition().getX() == x && players.getDwarfMultiCannon().getPosition().getY() == y) {
                        return false;
                    }
                }
                if (player.getDwarfMultiCannon().getPosition() == null) {
                    return false;
                }
            }
            if (players.getDwarfMultiCannon().getPosition() != null && players.getDwarfMultiCannon().getPlayer().getUsername() != player.getUsername() && players.getDwarfMultiCannon().getPosition() == new Position(x, y)) {
                return false;
            }

        }
        return true;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getCannonballsLeft() {
        return cannonballsLeft;
    }

    public void setCannonballsLeft(int cannonballsLeft) {
        this.cannonballsLeft = cannonballsLeft;
    }

    public boolean isSettingUpCannon() {
        return settingUpCannon;
    }

    public void setSettingUpCannon(boolean settingUpCannon) {
        this.settingUpCannon = settingUpCannon;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isFiring() {
        return firing;
    }

    public void setFiring(boolean firing) {
        this.firing = firing;
    }

    private enum Rotation {
        NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST
    }

}

