package com.rs2.model.transport;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;

public class Sailing {
    
    public static boolean sailShip(final Player player, final ShipRoute route) {
        if (route.cost > 0) {
            final Item gold = new Item(995, route.cost);
            if (!player.getInventory().playerHasItem(gold)) {
                player.getDialogue().sendPlayerChat("Sorry, I don't have enough coins for that.", Dialogues.SAD);
                player.getDialogue().endDialogue();
                return false;
            }
            player.getInventory().removeItem(gold);
        }
        if (route == ShipRoute.PORT_SARIM_TO_ENTRANA) {
        	//combat item scan
        	if (player.hasCombatEquipment()) {
        		Dialogues.sendDialogue(player, 657, 4, 0);
                return false;
        	}
        }
        player.setStopPacket(true);
        player.teleport(route.to.clone());
        if (route.ordinal() < 16) {
            player.getActionSender().sendConfig(75, route.ordinal() + 1);
            player.getActionSender().sendInterface(3281);
        } else {
            player.getActionSender().sendInterface(11092);
        }
        player.getActionSender().sendQuickSong((route.songDuration > 250 ? 225 : 298), route.songDuration);
        player.getActionSender().sendMapState(2);

        final Tick clearInterface = new Tick(route.duration) {
            @Override
            public void execute() {
                if (route.ordinal() < 16) {
                	player.getActionSender().sendConfig(75, -1);
                }
                if (route == ShipRoute.PORT_SARIM_TO_CRANDOR)
                	Dialogues.sendDialogue(player, 743, 5, 0);
                else if(route.ordinal() >= 16){
                	String dest = "Unknown destination";
                	if(route == ShipRoute.RELLEKKA_TO_MISCELLANIA)
                		dest = "Miscellania";
                	if(route == ShipRoute.MISCELLANIA_TO_RELLEKKA || route == ShipRoute.WATERBIRTH_TO_RELLEKKA)
                		dest = "Rellekka";
                	if(route == ShipRoute.RELLEKKA_TO_WATERBIRTH)
                		dest = "Waterbirth Island";
                	player.getDialogue().sendStatement("The ship arrives at "+dest+".");
                	player.getDialogue().endDialogue();
                }else
                	player.getActionSender().removeInterfaces();
                player.getActionSender().sendMapState(0);
                player.setStopPacket(false);
                stop();
            }
        };
        World.getTickManager().submit(clearInterface);
        return true;
    }

    public enum ShipRoute {
        
        PORT_SARIM_TO_ENTRANA(new Position(2834, 3335), 15, 0, 420),
        ENTRANA_TO_PORT_SARIM(new Position(3048, 3234), 15, 0, 420),
        PORT_SARIM_TO_CRANDOR(new Position(2852, 3235), 15, 0, 420),
        CRANDOR_TO_PORT_SARIM(new Position(2834, 3335), 14, 0, 350),
        PORT_SARIM_TO_KARAMJA(new Position(2956, 3146), 10, 30, 250),
        KARAMJA_TO_PORT_SARIM(new Position(3029, 3217), 10, 30, 250),
        ARDOUGNE_TO_BRIMHAVEN(new Position(2772, 3234), 4, 30, 100),
        BRIMHAVEN_TO_ARDOUGNE(new Position(2683, 3271), 4, 30, 100),
        UNUSED1(null, 0, 0, 0),
        UNUSED2(null, 0, 0, 0),
        PORT_KHAZARD_TO_SHIP_YARD(new Position(2998, 3043), 24, 0, 600),
        SHIP_YARD_TO_PORT_KHAZARD(new Position(2676, 3170), 24, 0, 600),
        CAIRN_ISLAND_TO_SHIP_YARD(new Position(2998, 3043), 17, 0, 425),
        PORT_SARIM_TO_PEST_CONTROL(new Position(2659, 2676), 12, 0, 300),
        PEST_CONTROL_TO_PORT_SARIM(new Position(3041, 3202), 12, 0, 300),
        FELDIP_HILLS_TO_CAIRN_ISLAND(new Position(2763, 2956, 1), 10, 0, 250),
        RELLEKKA_TO_WATERBIRTH(new Position(2550, 3759, 0), 10, 1000, 250),
        WATERBIRTH_TO_RELLEKKA(new Position(2620, 3686, 0), 10, 0, 250),
        RELLEKKA_TO_MISCELLANIA(new Position(2581, 3845, 0), 10, 0, 250),
        MISCELLANIA_TO_RELLEKKA(new Position(2629, 3693, 0), 10, 0, 250);
        
        int cost;
        Position to;
        int duration;
        int songDuration;
        
        ShipRoute(Position to, int duration, int cost, int songDuration) {
            this.to = to;
            this.duration = duration;
            this.cost = cost;
            this.songDuration = songDuration;
        }
    }
    
}
