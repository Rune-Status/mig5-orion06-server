package com.rs2.model.content.skills.magic;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.players.Player;
import com.rs2.model.players.Player.ExitType;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

/**
 * By Mikey` of Rune-Server
 */

public class Teleportation {

	private Player player;

	public Teleportation(Player player) {
		this.player = player;
	}

	public static final Position HOME = new Position(Constants.RESPAWN_X, Constants.RESPAWN_Y, Constants.RESPAWN_Z);

	public static final Position EDGEVILLE = new Position(3087, 3495);
	public static final Position KARAMJA = new Position(2912, 3170);
	public static final Position DRAYNOR_VILLAGE = new Position(3104, 3249);
	public static final Position AL_KHARID = new Position(3293, 3162);
	public static final Position CASTLE_WARS = new Position(2441, 3090);
	public static final Position DUEL_ARENA = new Position(3317, 3233);
	public static final Position GAMES_ROOM = new Position(2207, 4941);
	
	public int x, y, height, teleTimer;

	public boolean attemptTeleport(Position pos) { //hold on I remember now
		if (player.getEnchantingChamber().isInEnchantingChamber() ||
				player.getAlchemistPlayground().isInAlchemistPlayGround() ||
				player.getCreatureGraveyard().isInCreatureGraveyard() ||
				player.getTelekineticTheatre().isInTelekineticTheatre()) {
			player.getActionSender().sendMessage("You can't teleport out of here.");
			return false;
		}
//		if (player.inWild() && player.getWildernessLevel() > 20) {
//			player.getActionSender().sendMessage("You can't teleport above level 20 in the wilderness.");
//			return false;
//		}
		if (player.isTeleblocked()) {
			player.getActionSender().sendMessage("A magical force prevents you from teleporting.");
			return false;
		}
		if (player.cantTeleport()) {
			player.getActionSender().sendMessage("You can't teleport from here.");
			return false;
		} 
        if (!player.getTeleportTimer().completed()) {
            return false;
        }
        
        if (!player.getExitType().canAttempt() || (player.getExitType() == null || player.getExitType() != ExitType.TELEPORT)) {  
            player.getExitType().setTeleportPosition(pos); 
            player.setExitType(Player.ExitType.TELEPORT);
            player.setCountDownEscape(true);
            return false;
        }
        if (player.escapeTimer > 0) { 
        	return false;
        }
        
        player.getTeleportTimer().reset();
		boolean a = player.getMagicBookType() == SpellBook.MODERN;
		if(player.getMagicBookType() == SpellBook.NECROMANCY){
			a = player.tempMagicBookType == SpellBook.MODERN;
		}
		teleport(pos.getX(), pos.getY(), pos.getZ(), a);
		return true;
	}

	public boolean attemptTeleportJewellery(Position pos) {
		if (player.getEnchantingChamber().isInEnchantingChamber() ||
				player.getAlchemistPlayground().isInAlchemistPlayGround() ||
				player.getCreatureGraveyard().isInCreatureGraveyard() ||
				player.getTelekineticTheatre().isInTelekineticTheatre()) {
			player.getActionSender().sendMessage("You can't teleport out of here.");
			return false;
		}
//		if (player.inWild() && player.getWildernessLevel() > 30) {
//			player.getActionSender().sendMessage("You can't teleport above level 30 in the wilderness.");
//			return false;
//		}
		if (player.isTeleblocked()) {
			player.getActionSender().sendMessage("A magical force prevents you from teleporting.");
			return false;
		}
		if (player.cantTeleport()) {
			player.getActionSender().sendMessage("You can't teleport from here.");
			return false;
		}
		player.getUpdateFlags().sendAnimation(714);
		player.getUpdateFlags().sendHighGraphic(301);
		player.getActionSender().sendSound(202, 1, 0);
		teleport(pos.getX(), pos.getY(), pos.getZ(), true);
		return true;
	}
	
	public void forcedObjectTeleport2(final int x, final int y, final int height, final String s, final int time, final int anim1, final int anim2, final int gfx) {
		player.setStopPacket(true);
		player.getAttributes().put("canTakeDamage", Boolean.FALSE);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int teleTimer = time;
			@Override
			public void execute(CycleEventContainer container) {
				teleTimer--;
				if (!player.isDead()) {
					if (teleTimer == 4) {
						player.getUpdateFlags().sendAnimation(anim1);
						player.getUpdateFlags().sendHighGraphic(gfx);
					}
					if (teleTimer == 2) {
						player.setStopPacket(false);
						player.getUpdateFlags().sendAnimation(anim2);
						player.teleport(new Position(x, y, height));
						if(s != null)
							player.getActionSender().sendMessage(s);
					}
				} else {
					teleTimer = 0;
				}
				if (teleTimer < 1) {
					container.stop();
				}
			}
			@Override
			public void stop() {
				player.setStopPacket(false);
				player.getAttributes().put("canTakeDamage", Boolean.TRUE);
			}
		}, 1);
	}

	public void forcedObjectTeleport(final int x, final int y, final int height, final String s) {
		player.setStopPacket(true);
		player.getAttributes().put("canTakeDamage", Boolean.FALSE);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int teleTimer = 8;
			@Override
			public void execute(CycleEventContainer container) {
				teleTimer--;
				if (!player.isDead()) {
					if (teleTimer == 4) {
						player.getUpdateFlags().sendAnimation(714);
						player.getUpdateFlags().sendHighGraphic(301);
					}
					if (teleTimer == 2) {
						player.getUpdateFlags().sendAnimation(715);
						player.teleport(new Position(x, y, height));
						if(s != null)
							player.getActionSender().sendMessage(s);
					}
				} else {
					teleTimer = 0;
				}
				if (teleTimer < 1) {
					container.stop();
				}
			}
			@Override
			public void stop() {
				player.setStopPacket(false);
				player.getAttributes().put("canTakeDamage", Boolean.TRUE);
			}
		}, 1);
	}
	
	public void teleport(final int x, final int y, final int height, final boolean modern) {
        if (player.isTeleblocked() || player.cantTeleport()) {
            player.getActionSender().sendMessage("You can't teleport out of here!");
            return;
        }
		player.setStopPacket(true);
		player.getAttributes().put("canTakeDamage", Boolean.FALSE);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int teleTimer = 6;
			@Override
			public void execute(CycleEventContainer container) {
				teleTimer--;
				if (!player.isDead()) {
					if (teleTimer == 2) {
						if (modern) {
							player.getUpdateFlags().sendAnimation(715);
						}
						player.teleport(new Position(x, y, height));
					}
				} else {
					teleTimer = 0;
				}
				if (teleTimer < 1) {
					container.stop();
				}
			}
			@Override
			public void stop() {
				player.setStopPacket(false);
				player.getAttributes().put("canTakeDamage", Boolean.TRUE);
			}
		}, 1);
	}

    public void teleportObelisk(Position position) {
        teleportObelisk(position.getX(), position.getY(), position.getZ(), true, null);
    }

    public void teleportObelisk(final int x, final int y, final int height) {
        teleportObelisk(x, y, height, true, null);
    }

	public void teleportObelisk(final int x, final int y, final int height, boolean graphic, final String message) {
        if (player.isTeleblocked()) {
            player.getActionSender().sendMessage("A magical force prevents you from teleporting.");
            return;
        }
		if (player.cantTeleport()) {
			player.getActionSender().sendMessage("You can't teleport from here.");
			return;
		}
        if (graphic)
            player.getUpdateFlags().sendHighGraphic(342);
		player.getUpdateFlags().sendAnimation(1816);
		player.setStopPacket(true);
		player.getAttributes().put("canTakeDamage", Boolean.FALSE);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int teleTimer = 6;
			@Override
			public void execute(CycleEventContainer container) {
				teleTimer--;
				if (!player.isDead()) {
                    if (teleTimer == 3) {
                        player.getUpdateFlags().sendAnimation(715);
                        player.teleport(new Position(x, y, height));
                        if (message != null)
                            player.getActionSender().sendMessage(message);
                    }
                } else {
                    teleTimer = 0;
                }
                if (teleTimer < 1) {
                    container.stop();
                }
			}
			@Override
			public void stop() {
				player.setStopPacket(false);
				player.getAttributes().put("canTakeDamage", Boolean.TRUE);
			}
		}, 1);
	}

	public void teleportRunecraft(final int x, final int y, final int height) {
        if (player.isTeleblocked() || player.cantTeleport()) {
            player.getActionSender().sendMessage("You can't teleport out of here!");
            return;
        }
		player.getUpdateFlags().sendHighGraphic(110);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			int teleTimer = 6;
			@Override
			public void execute(CycleEventContainer container) {
				teleTimer--;
				if (!player.isDead()) {
					if (teleTimer == 3) {
						player.teleport(new Position(x, y, height));
					}
				} else {
					teleTimer = 0;
				}
				if (teleTimer < 1) {
					container.stop();
				}
			}
			@Override
			public void stop() {
				player.setStopPacket(false);
				player.getAttributes().put("canTakeDamage", Boolean.TRUE);
			}
		}, 1);
	}

}
