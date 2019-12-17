package com.rs2.model.npcs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.Graphic;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.Following;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.npcs.Npc.WalkType;
import com.rs2.model.npcs.drop.NPCDropTable;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.FileOperations;
import com.rs2.util.Stream;
import com.rs2.util.clip.Region;

/**
 * Having anything to do with any type of npc data loading.
 * 
 * @author BFMV
 */
public class NpcLoader {
	
	public static void loadSpawns() {
        int spawns = 0;
		try {
			byte abyte2[] = FileOperations.ReadFile("./data/npcs/Npc-spawn.dat");
			Stream stream2 = new Stream(abyte2);
			spawns = stream2.readUnsignedWord();
			for (int i2 = 0; i2 < spawns; i2++) {
				int id = stream2.readUnsignedWord();
				int type = stream2.readUnsignedByte();
				int face = stream2.readUnsignedByte();
				int x = stream2.readUnsignedWord();
				int y = stream2.readUnsignedWord();
				int z = stream2.readUnsignedByte();
				if(Constants.F2P_CONTENT_ONLY && type == 1)
					continue;
				newNPC(id, x, y, z, face);
				final NpcDefinition npc = NpcDefinition.forId(id);
				int linked = npc.getDropsLinkedTo();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Loaded " + spawns + " npc spawns.");
		if(Constants.GE_ENABLED)
			spawnGENpcs();
	}
	
	private static void spawnGENpcs(){
		int geClerk = 3863;
		newNPC(geClerk, 3252, 3423, 0, Constants.SOUTH);//varrock small bank
		newNPC(geClerk, 3180, 3439, 0, Constants.EAST);//varrock big bank
//		newNPC(geClerk, 3091, 3493, 0, Constants.EAST);//edge
//		newNPC(geClerk, 2943, 3373, 0, Constants.SOUTH);
//		newNPC(geClerk, 3011, 3358, 0, Constants.SOUTH);
//		newNPC(geClerk, 3095, 3246, 0, Constants.SOUTH);
//		newNPC(geClerk, 2806, 3440, 0, Constants.EAST);
//		newNPC(geClerk, 2723, 3490, 0, Constants.EAST);
//		newNPC(geClerk, 2618, 3334, 0, Constants.SOUTH);
//		newNPC(geClerk, 2654, 3287, 0, Constants.SOUTH);
//		newNPC(geClerk, 2610, 3097, 0, Constants.SOUTH);
//		newNPC(geClerk, 3513, 3476, 0, Constants.WEST);
//		newNPC(geClerk, 3269, 3171, 0, Constants.SOUTH);
//		newNPC(geClerk, 2588, 3417, 0, Constants.EAST);
	}
	
	public static void newNPC(int id, int x, int y, int heightLevel, int face) {
		//clip npcs: does not allow walking over it!
		if(id == 767 || // cat crate
				id == 1826)//hole in the wall/wall beast
			Region.addObject(2620, x, y, heightLevel, 0, 10, true);
		Npc npc = new Npc(id);
		npc.setPosition(new Position(x, y, heightLevel));
		npc.setSpawnPosition(new Position(x, y, heightLevel));
        npc.setNeedsRespawn(true);
		npc.setMinWalk(new Position(x - Constants.NPC_WALK_DISTANCE, y - Constants.NPC_WALK_DISTANCE));
		npc.setMaxWalk(new Position(x + Constants.NPC_WALK_DISTANCE, y + Constants.NPC_WALK_DISTANCE));
        npc.setWalkType(face == 1 || face > 5 ? WalkType.WALK : WalkType.STAND);
		npc.setFace(face);
		npc.setCurrentX(x);
		npc.setCurrentY(y);
        npc.setNeedsRespawn(true);
		World.register(npc);
	}
	
	public static void newNPCcommand(int id, int x, int y, int heightLevel, int face) {
		if(id == 767)//make cat crate clipped... does not allow walking over it
			Region.addObject(2620, x, y, heightLevel, 0, 10, true);
		Npc npc = new Npc(id);
		npc.setPosition(new Position(x, y, heightLevel));
		npc.setSpawnPosition(new Position(x, y, heightLevel));
		npc.setMinWalk(new Position(x - Constants.NPC_WALK_DISTANCE, y - Constants.NPC_WALK_DISTANCE));
		npc.setMaxWalk(new Position(x + Constants.NPC_WALK_DISTANCE, y + Constants.NPC_WALK_DISTANCE));
        npc.setWalkType(face == 1 || face > 5 ? WalkType.WALK : WalkType.STAND);
		npc.setFace(face);
		npc.setCurrentX(x);
		npc.setCurrentY(y);
        npc.setNeedsRespawn(false);
		World.register(npc);
	}
	
	public static void spawnNewNPC(int id, Npc npc2, int anim) {
		Npc npc = new Npc(id);
		npc.setPosition(npc2.getPosition());
		npc.setSpawnPosition(npc2.getSpawnPosition());
		npc.setMinWalk(npc2.getMinWalk());
		npc.setMaxWalk(npc2.getMaxWalk());
        npc.setWalkType(npc2.getWalkType());
		npc.setFace(npc2.getFace());
		npc.setCurrentX(npc2.getCurrentX());
		npc.setCurrentY(npc2.getCurrentY());
        npc.setNeedsRespawn(false);
		World.register(npc);
		npc.getUpdateFlags().sendAnimation(anim);
	}

	public static void spawnNpcPos(Player player, Position pos, Npc npc, boolean attack, boolean hintIcon) {
		npc.setPosition(pos);
		npc.setSpawnPosition(pos);
		npc.setWalkType(Npc.WalkType.STAND);
		npc.setCurrentX(pos.getX());
		npc.setCurrentY(pos.getY());
		World.register(npc);
		player.setSpawnedNpc(npc);
		npc.setPlayerOwner(player.getIndex());
		npc.getUpdateFlags().sendFaceToDirection(player.getPosition());
		if (attack)
			CombatManager.attack(npc, player);
		if(hintIcon)
            player.getActionSender().createPlayerHints(1, (npc).getIndex());
	}
	
	public static void spawnNpc(Player player, Npc npc, boolean attack, boolean hintIcon) {
		int x = 0, y = 0;
		if (player.canMove(1, 0)) {
			x = 1;
			y = 0;
		} else if (player.canMove(-1, 0)) {
			x = -1;
			y = 0;
		} else if (player.canMove(0, 1)) {
			x = 0;
			y = 1;
		} else if (player.canMove(0, -1)) {
			x = 0;
			y = -1;
		}
		x = player.getPosition().getX() + x;
		y = player.getPosition().getY() + y;
		npc.setPosition(new Position(x, y, player.getPosition().getZ()));
		npc.setSpawnPosition(new Position(x, y, player.getPosition().getZ()));
		npc.setWalkType(Npc.WalkType.STAND);
		npc.setCurrentX(x);
		npc.setCurrentY(y);
		World.register(npc);
		player.setSpawnedNpc(npc);
		npc.setPlayerOwner(player.getIndex());
		npc.getUpdateFlags().sendFaceToDirection(player.getPosition());
		if (attack)
			CombatManager.attack(npc, player);
        else {
            npc.setFollowDistance(1);
            npc.setFollowingEntity(player);
        }
        if(hintIcon)
            player.getActionSender().createPlayerHints(1, (npc).getIndex());
	    if (npc.getNpcId() == 77) {
	    	npc.getUpdateFlags().sendGraphic(Graphic.lowGraphic(78));
	    }
	}

	public static void spawnNpc(Player player, Npc npc, int x, int y, int z, int gfx, boolean attack, boolean hintIcon) {
		npc.setPosition(new Position(x, y, z));
		npc.setSpawnPosition(new Position(x, y, z));
		npc.setWalkType(Npc.WalkType.WALK);
		npc.setCurrentX(x);
		npc.setCurrentY(y);
		npc.setMinWalk(new Position(x - Constants.NPC_WALK_DISTANCE, y - Constants.NPC_WALK_DISTANCE));
		npc.setMaxWalk(new Position(x + Constants.NPC_WALK_DISTANCE, y + Constants.NPC_WALK_DISTANCE));
		World.register(npc);
		npc.getUpdateFlags().sendAnimation(gfx);
		npc.getUpdateFlags().sendFaceToDirection(player.getPosition());
		if (attack)
			CombatManager.attack(npc, player);
        if(hintIcon)
            player.getActionSender().createPlayerHints(1, (npc).getIndex());
	    if (npc.getNpcId() == 77) {
	    	npc.getUpdateFlags().sendGraphic(Graphic.lowGraphic(78));
	    }
	}
	
	public static void spawnTimedStillNpc(Npc npc, int x, int y, int z, int time) {
		npc.setPosition(new Position(x, y, z));
		npc.setSpawnPosition(new Position(x, y, z));
		npc.setCurrentX(x);
		npc.setCurrentY(y);
		npc.setTick(time);
		npc.setStayStill(true);
		World.register(npc);
	}
	
	public static boolean spawnNpcWOwner(Player player, Npc npc, int x, int y, int z, int gfx, boolean attack, boolean hintIcon) {
		if(player.getSpawnedNpc() != null){
			if(!player.getSpawnedNpc().isDead()){
				player.getSpawnedNpc().setVisible(false);
                World.unregister(player.getSpawnedNpc());
			}
		}
		npc.setPosition(new Position(x, y, z));
		npc.setSpawnPosition(new Position(x, y, z));
		npc.setWalkType(Npc.WalkType.WALK);
		npc.setCurrentX(x);
		npc.setCurrentY(y);
		npc.setMinWalk(new Position(x - Constants.NPC_WALK_DISTANCE, y - Constants.NPC_WALK_DISTANCE));
		npc.setMaxWalk(new Position(x + Constants.NPC_WALK_DISTANCE, y + Constants.NPC_WALK_DISTANCE));
		World.register(npc);
		player.setSpawnedNpc(npc);
		npc.setPlayerOwner(player.getIndex());
		npc.getUpdateFlags().sendAnimation(gfx);
		npc.getUpdateFlags().sendFaceToDirection(player.getPosition());
		if (attack)
			CombatManager.attack(npc, player);
        if(hintIcon)
            player.getActionSender().createPlayerHints(1, (npc).getIndex());
	    if (npc.getNpcId() == 77) {
	    	npc.getUpdateFlags().sendGraphic(Graphic.lowGraphic(78));
	    }
	    return true;
	}
	
	public static void spawnNpc2(Player player, Npc npc, int x, int y, int z, int gfx, boolean attack, boolean hintIcon) {
		npc.setPosition(new Position(x, y, z));
		npc.setSpawnPosition(new Position(x, y, z));
		npc.setWalkType(Npc.WalkType.WALK);
		npc.setCurrentX(x);
		npc.setCurrentY(y);
		npc.setMinWalk(new Position(x - Constants.NPC_WALK_DISTANCE, y - Constants.NPC_WALK_DISTANCE));
		npc.setMaxWalk(new Position(x + Constants.NPC_WALK_DISTANCE, y + Constants.NPC_WALK_DISTANCE));
		World.register(npc);
		player.setSpawnedNpc(npc);
		npc.setPlayerOwner(player.getIndex());
		npc.getUpdateFlags().sendAnimation(gfx);
		npc.getUpdateFlags().sendFaceToDirection(player.getPosition());
		if (attack)
			CombatManager.attack(npc, player);
        if(hintIcon)
            player.getActionSender().createPlayerHints(1, (npc).getIndex());
	    if (npc.getNpcId() == 77) {
	    	npc.getUpdateFlags().sendGraphic(Graphic.lowGraphic(78));
	    }
	}
	
	public static boolean checkSpawn(Player player, int id) {
		return player.getSpawnedNpc() != null && !player.getSpawnedNpc().isDead() && player.getSpawnedNpc().getNpcId() == id;
	}

	public static void spawnNpc(Entity entityToAttack, Npc npc, Position spawningPosition, boolean hintIcon, String message) {
		npc.setPosition(spawningPosition);
		npc.setWalkType(Npc.WalkType.STAND);
		npc.setCurrentX(spawningPosition.getX());
		npc.setCurrentY(spawningPosition.getY());
        npc.setNeedsRespawn(false);
		World.register(npc);
		if (entityToAttack != null){
			npc.setFollowingEntity(entityToAttack);
            CombatManager.attack(npc, entityToAttack);
		    npc.getUpdateFlags().sendFaceToDirection(entityToAttack.getPosition());
        }
        if(entityToAttack.isPlayer() && hintIcon)
            ((Player)entityToAttack).getActionSender().createPlayerHints(1, (npc).getIndex());
	    if(message != null)
            npc.getUpdateFlags().sendForceMessage(message);
    }
	
	public static void spawnNpcAlwaysFollow(Entity entityToAttack, Npc npc, Position spawningPosition, boolean hintIcon, String message) {
		npc.setAlwaysFollow(entityToAttack);
		npc.setPosition(spawningPosition);
		npc.setWalkType(Npc.WalkType.STAND);
		npc.setCurrentX(spawningPosition.getX());
		npc.setCurrentY(spawningPosition.getY());
        npc.setNeedsRespawn(false);
		World.register(npc);
		if (entityToAttack != null){
			npc.setFollowingEntity(entityToAttack);
            CombatManager.attack(npc, entityToAttack);
		    npc.getUpdateFlags().sendFaceToDirection(entityToAttack.getPosition());
        }
        if(entityToAttack.isPlayer() && hintIcon)
            ((Player)entityToAttack).getActionSender().createPlayerHints(1, (npc).getIndex());
	    if(message != null)
            npc.getUpdateFlags().sendForceMessage(message);
	    if(npc.getPosition().getZ() != entityToAttack.getPosition().getZ()){
	    	System.out.println("FIGHT CAVE! "+npc.getPosition().getZ()+" "+entityToAttack.getPosition().getZ());
	    }
    }

	public static void destroyNpc(Npc npc) {
		if (npc.getPlayerOwner() != null) {
			npc.getPlayerOwner().setSpawnedNpc(null);
		}
		npc.setVisible(false);
		Following.resetFollow(npc);
		World.unregister(npc);
	}

}
