package com.rs2.model.transport;

import com.rs2.model.Position;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

/*
 * @author Liberty
 */

public class GnomeGlider {

	public static boolean flightButtons(Player player, int button) {
		for (GliderRoute route : GliderRoute.values()) {
			if (button == route.buttonId) {
				if(!player.getInteractingEntity().isNpc())
					return false;
				Npc npc = (Npc) player.getInteractingEntity();
				handleFlight(player, npc.getNpcId(), route);
				return true;
			}
		}
		return false;
	}

	public static void handleFlight(final Player player, final int npcId, final GliderRoute route) {
        if (player.getInteractingEntity() == null)
            return;
		player.getActionSender().sendInterface(802);
		int flight = getConfig(npcId, route);
		if (flight < 1) {
			player.getActionSender().sendMessage("You can't fly there from here.");
			return;
		}
		if (flight == 20) {
			player.getActionSender().sendMessage("You can't fly to the same place you are at.");
			return;
		}
		if (flight == 25) {
			player.getActionSender().sendMessage("You can't fly to that place yet.");
			return;
		}
		player.getActionSender().sendConfig(153, flight);
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer e) {
				player.teleport(route.to);
				e.stop();
			}
			@Override
			public void stop() {
			}
		}, 3);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer e) {
				player.setStopPacket(false);
				player.getActionSender().removeInterfaces();
				player.getActionSender().sendConfig(153, -1);
				e.stop();
			}
			@Override
			public void stop() {
			}
		}, 4);
	}

    public enum GliderRoute {
        SINDARPOS(826, new Position(2848, 3497), 0),//3810
        TA_QUIR_PRIW(825, new Position(2465, 3501, 3), 1),//170
        LEMANTO_ANDRA(827, new Position(3321, 3427), 2),//3811 - cant fly
        KAR_HEWO(828, new Position(3278, 3212), 3),//3809
        GANDIUS(824, new Position(2971, 2969), 4),//3812
        LEMANTOLLY_UNDRI(12342, new Position(2544, 2970), 5);//1800

        int buttonId;
        Position to;
        int move;
        
        GliderRoute(int buttonId, Position to, int move) {
            this.buttonId = buttonId;
            this.to = to;
            this.move = move;
        }
    }

	private static int getConfig(int pilot, final GliderRoute route) {
		int startPoint = 0, endPoint = route.move;
		if(pilot == 3810)
			startPoint = 0;
		else if(pilot == 170)
			startPoint = 1;
		else if(pilot == 3809)
			startPoint = 3;
		else if(pilot == 3812)
			startPoint = 4;
		else if(pilot == 1800)
			startPoint = 5;
		if (startPoint == endPoint) {
			return 20; // returns "cant fly to same place" error
		}
		switch (startPoint) {
			case 0 : // Sin
				if (endPoint == 1) {
					return 2;
				}
				break;
			case 1 : // Ta
				if (endPoint == 0) {
					return 1;
				} else if (endPoint == 2) {
					return 3;
				} else if (endPoint == 3) {
					return 4;
				} else if (endPoint == 4) {
					return 7;
				} else if (endPoint == 5) {
					return 25;// return 10;
				}
				break;
			case 3 : // Kar
				if (endPoint == 1) {
					return 5;
				}
				break;
			case 4 : // Gan
				if (endPoint == 1) {
					return 6;
				}
				break;
			case 5 : // Lem und
				if (endPoint == 1) {
					return 11;
				}
				break;
		}
		return 0;
	}

}