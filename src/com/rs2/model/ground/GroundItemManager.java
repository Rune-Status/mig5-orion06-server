package com.rs2.model.ground;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.ItemDefinition;
import com.rs2.util.Misc;

/**
 *
 */
public class GroundItemManager implements Runnable {
    
    static {
        manager = new GroundItemManager();
    }

    private LinkedList<GroundItem> groundItems = new LinkedList<GroundItem>();

    //private static final int UPDATE_TIME = (int)Misc.secondsToTicks(3);

	public static final int	HIDDEN_TICKS = (int)Misc.secondsToTicks(100);
	public static final int PLAYER_HIDE_TICKS = (int)Misc.secondsToTicks(100);
	public static final int PUBLIC_HIDE_TICKS = (int)Misc.secondsToTicks(200);
	public static final int UNTRADABLE_HIDE_TICKS = (int)Misc.secondsToTicks(200);

    @Override
    public void run() {
        try {
            int size = groundItems.size();
            Queue<GroundItem> publicItemsList = new LinkedList<GroundItem>();
            for (int i = 0; i < size;) {
                GroundItem item = groundItems.get(i);
                if(item == null)
                	continue;
                switch (item.getStage()) {
                	case PUBLIC:
                		publicItemsList.add(item);
                	break;
                }
                i++;
            }
            iterateItems: for (int i = 0; i < size;) {
                GroundItem item = groundItems.get(i);
                if(item == null)
                	continue;
                switch (item.getStage()) {
                    case HIDDEN:
                    	if(item.getItem().getId() == 6903){//enchanting chamber d stone
                    		if (item.getTimer().elapsed() < 700) {//7mins
                            	break;
                            }
                    	}
                        if (item.getTimer().elapsed() < HIDDEN_TICKS) {
                        	break;
                        }
                        //going to update
                        item.getTimer().reset();
                        //make it respawn as public
                        item.setStage(GroundItem.GroundStage.PUBLIC);
                        //try merging
                        if (item.getItem().getDefinition().isStackable()) {
                            for (GroundItem other : publicItemsList) {
                            	if(other == null)
                            		continue;
                                if (item.canMergeWithItem(other)) {
                                    merge(item, other, World.getPlayers());
                                    groundItems.remove(i);
                                    size--;
                                    continue iterateItems;
                                }
                            }
                        }
                        addItem(item, World.getPlayers());
                        break;
                    case PUBLIC:
                        if (item.getTimer().elapsed() < PUBLIC_HIDE_TICKS) {
                        	break;
                        }
                        //going to update
                        item.getTimer().reset();
                        //remove if doesn't respawn
                        if (!item.respawns()) {
                            publicItemsList.remove(item);
                            removeItem(item, World.getPlayers());
                            //groundItems.remove(i);
                            size--;
                            continue iterateItems;
                        }
                        break;
                    case PRIVATE: 
                        //if item is not tradeable then remove it from viewfirst
                        if (item.getItem().getDefinition().isUntradable()) {
                        	Entity entity;
                        	if(item.getItem().getId() == 6888){//telekinetic theatre statue
                        		if (item.getViewFirst().getEntity() == null) {
                        			groundItems.remove(item);
                        			size--;
                                    continue iterateItems;
                        		}
                        		break;
                        	}
                            if (item.getTimer().elapsed() < UNTRADABLE_HIDE_TICKS) {
                            	break;
                            }
                            //going to update
                            item.getTimer().reset();
                            if (item.getViewFirst() != null && (entity = item.getViewFirst().getEntity()) != null && entity.isPlayer()) {
                                removeItem(item, new Player[] {(Player) entity});
                            }
                            //groundItems.remove(i);
                            size--;
                            continue iterateItems;
                        } else {
                            if (item.getTimer().elapsed() < PLAYER_HIDE_TICKS) {
                            	break;
                            }
                            //going to update
                            item.getTimer().reset();
                            //make it public
                            item.setStage(GroundItem.GroundStage.PUBLIC);
                            /*if(item.getItem().getId() == 556 && item.getPosition().getX() == 2942 && item.getPosition().getY() == 3159)
                            		System.out.println("check "+i);*/
                            //try merging
                            if (item.getItem().getDefinition().isStackable()) {
                                for (GroundItem other : publicItemsList) {
                                	if(other == null)
                                		continue;
                                	/*if(item.getItem().getId() == 556 && other.getItem().getId() == 556 && item.getPosition().getX() == 2942 && item.getPosition().getY() == 3159)
                                		System.out.println("checked");*/
                                    if (item.canMergeWithItem(other)) {
                                    	/*if(item.getItem().getId() == 556)
                                    		System.out.println("2");*/
                                        merge(item, other, World.getPlayers());
                                        groundItems.remove(i);
                                        size--;
                                        continue iterateItems;
                                    }
                                }
                            }
                            //couldn't merge, add to global list
                            publicItemsList.add(item);
                            Player[] players = World.getPlayers();
                            Entity viewFirst = item.getViewFirst() == null ? null : item.getViewFirst().getEntity();
                            if (viewFirst != null && !viewFirst.isPlayer())
                                viewFirst = null;
                            for (int p = 0; p < players.length; p++) {
                                Player player = players[p];
                                if (player == null || player == viewFirst && player.getGroundItems().contains(item))
                                    continue;
                                if (item.canBeSeenByPlayer(player)) {
                                	/*if(item.getItem().getId() == 556)
                                		System.out.println("1: "+player.getUsername());*/
                                    player.getGroundItems().add(item);
                                    player.getActionSender().sendGroundItem(item);
                                }
                            }
                        }
                        break;
                    }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void merge(GroundItem item, GroundItem other, Player[] possiblePlayers) {
        List<Player> list = new LinkedList<Player>();
        if(item == null || other == null)
        	return;
        if (possiblePlayers != null) {
            for (Player player : possiblePlayers) {
                if (player == null)
                    continue;
                if (other.canBeSeenByPlayer(player)) {
                    if (player.getGroundItems().contains(other)){
                        player.getActionSender().removeGroundItem(other);
                        player.getGroundItems().remove(other);
                    }
                    if (player.getGroundItems().contains(item)){
                        player.getActionSender().removeGroundItem(item);
                        player.getGroundItems().remove(item);
                    }
                    list.add(player);
                }
            }
        }
        possiblePlayers = list.toArray(new Player[list.size()]);
        other.getItem().setCount(other.getItem().getCount()+item.getItem().getCount());
        other.getTimer().reset();
        if (possiblePlayers != null) {
            for (Player player : possiblePlayers) {
                if (item.canBeSeenByPlayer(player)){
                    player.getActionSender().sendGroundItem(other);
                    player.getGroundItems().add(other);
                }
            }
        }
    }

    private void removeItem(GroundItem item, Player[] players) {
    	if(item == null)
    		return;
        for (Player player : players) {
            if (player == null)
                continue;
            if (item.canBeSeenByPlayer(player)) {
                player.getGroundItems().remove(item);
                player.getActionSender().removeGroundItem(item);
            }
        }
        //System.out.println("check remove");
        groundItems.remove(item);
    }

    private void addItem(GroundItem item, Player[] players) {
    	if(item == null)
    		return;
        for (Player player : players) {
            if (player == null)
                continue;
            if (item.canBeSeenByPlayer(player)) {
                player.getGroundItems().add(item);
                player.getActionSender().sendGroundItem(item);
            }
        }
    }
    
    public boolean destroyItem(GroundItem item, Player player1) {
        if (item == null)
            return false;
        Player[] updatePlayers = null;
        if (item.getStage() == GroundItem.GroundStage.PRIVATE) {
            Entity entity;
            /*if (item.getViewFirst() != null && (entity = item.getViewFirst().getEntity()) != null && entity.isPlayer())
                updatePlayers = new Player[] { (Player)entity };*/
            if (item.getViewFirst() != null){
            	entity = item.getViewFirst().getEntity();
            	//System.out.println("priv des");
            	if(entity != null)
            	if(entity.isPlayer())
            		updatePlayers = new Player[] { (Player)entity };
            }
            
        }  else if (item.getStage() == GroundItem.GroundStage.PUBLIC) {
            updatePlayers = World.getPlayers();
        }
        
        if (item.getStage() == GroundItem.GroundStage.PUBLIC && item.respawns()) {
            item.setStage(GroundItem.GroundStage.HIDDEN);
            item.getTimer().reset();
        }
        else{ 
        	if(!groundItems.contains(item)){
        		System.out.println("Item being removed from ground while it doesnt exist!? "+item);
        		return false;
        	}
        	groundItems.remove(item);
        }

        if (updatePlayers == null)
            return false;
        
        for (Player player : updatePlayers) {
            if (player == null)
                continue;
            if (item.canBeSeenByPlayer(player)) {
            	/*System.out.println(player.getGroundItems().size());
            	for(int i = 0; i < player.getGroundItems().size(); i++){
            		if(player.getGroundItems().get(i) != null)
            			System.out.println(player.getGroundItems().get(i).getItem().getId());
            	}*/
            	if(!player.getGroundItems().contains(item))
            		System.out.println("Player ground item being removed while it doesnt exist!? "+item);
                player.getGroundItems().remove(item);
                player.getActionSender().removeGroundItem(item);
            }
        }
        if(player1.getGroundItems().contains(item)){
        	System.out.println("Ground item was not removed from players list!");
        }
        return true;
    }


    public void dropItem(GroundItem item) {
        Player[] updatePlayers = null;
        if(item == null)
        	return;
        int size = groundItems.size();
        Queue<GroundItem> publicItemsList = new LinkedList<GroundItem>();
        for (int i = 0; i < size;) {
            GroundItem item2 = groundItems.get(i);
            if(item2 == null)
            	continue;
            switch (item2.getStage()) {
            	case PUBLIC:
            		publicItemsList.add(item2);
            	break;
            }
            i++;
        }
        switch (item.getStage()) {
            case PRIVATE:
                Entity entity;
                if (item.getViewFirst() != null && (entity = item.getViewFirst().getEntity()) != null && entity.isPlayer())
                    updatePlayers = new Player[]{(Player) entity};
                if (ItemDefinition.forId(item.getItem().getId()).isStackable()) {
                    for (GroundItem other : groundItems) {
                        if (item.canMergeWithItem(other)) {
                            merge(item, other, updatePlayers);
                            return;
                        }
                    }
                }
                break;
            case PUBLIC:
                updatePlayers = World.getPlayers();
                if (ItemDefinition.forId(item.getItem().getId()).isStackable()) {
                    for (GroundItem other : publicItemsList) {
                        if (item.canMergeWithItem(other)) {
                            merge(item, other, updatePlayers);
                            return;
                        }
                    }
                }
                break;
        }
        groundItems.add(item);

        if (updatePlayers == null)
            return;

        addItem(item, updatePlayers);

    }

    private static GroundItemManager manager;

    public static GroundItemManager getManager() {
        return manager;
    }
    
    public boolean itemExists(Player player, GroundItem groundItem) {
    	if(player == null || groundItem == null)
    		return false;
        for (GroundItem item : player.getGroundItems())
            if (item.equals(groundItem))
                return true;
        return false;
    }

    public GroundItem findItem(Player player, int itemId, Position pos) {
    	if(player == null)
    		return null;
        for (GroundItem item : player.getGroundItems())
            if (item.getItem().getId() == itemId && item.getPosition().equals(pos))
                return item;
        return null;
    }

    public void removeGroundItems(Player player) {
    	if(player == null)
    		return;
        for (GroundItem item : player.getGroundItems())
            player.getActionSender().removeGroundItem(item);
        player.getGroundItems().clear();
        //System.out.println("check");
    }
    
    public void addGroundItems(Player player) {
    	if(player == null)
    		return;
    	for (GroundItem item : groundItems) {
    		if(item == null)
    			continue;
            if (!item.canBeSeenByPlayer(player))
                continue;
            if (item.getStage() == GroundItem.GroundStage.PUBLIC) { 
                player.getGroundItems().add(item);
                player.getActionSender().sendGroundItem(item);
                //System.out.println("public");
            } else if(item.getStage() == GroundItem.GroundStage.PRIVATE && item.getViewFirst() != null
                    && item.getViewFirst().equals(player)) {
                player.getGroundItems().add(item);
                player.getActionSender().sendGroundItem(item);
                //System.out.println("private");
                /*System.out.println(player.getGroundItems().size());
            	for(int i = 0; i < player.getGroundItems().size(); i++){
            		if(player.getGroundItems().get(i) != null)
            			System.out.println(player.getGroundItems().get(i).getItem().getId());
            	}*/
            }
            
        }
    }
    
}
