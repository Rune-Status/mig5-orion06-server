package com.rs2.model.npcs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.rs2.Constants;
import com.rs2.cache.Archive;
import com.rs2.cache.Cache;
import com.rs2.model.Entity;
import com.rs2.model.World;
import com.rs2.model.content.combat.AttackScript;
import com.rs2.model.content.combat.attacks.BasicAttack;
import com.rs2.model.content.combat.weapon.AttackStyle;
import com.rs2.model.players.ShopManager;
import com.rs2.model.players.ShopManager.Shop;
import com.rs2.model.players.container.Container;
import com.rs2.model.players.container.Container.Type;
import com.rs2.model.players.item.Item;
import com.rs2.util.FileOperations;
import com.rs2.util.Packer;
import com.rs2.util.Stream;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 07/04/12 Time: 23:11 To change
 * this template use File | Settings | File Templates.
 */
public class NpcDefinition {
	
	static int npcCount = 0;
	
	public static void loadNpcDef() {
        try {
			byte abyte2[] = FileOperations.ReadFile("data/npcs/npcDefinitions.dat");
			Stream stream2 = new Stream(abyte2);
			npcCount = stream2.readUnsignedWord();
			for (int i2 = 0; i2 < npcCount; i2++) {
				final NpcDefinition def = forId(i2);
				int type = stream2.readUnsignedByte();
				def.id = i2;
				def.hitpoints = 0;
				if(type == 1){
					def.respawn = 60;//was 10
					def.aggressive = (stream2.readUnsignedByte() == 1 ? true : false);
					def.retreats = (stream2.readUnsignedByte() == 1 ? true : false);
					def.poisonous = (stream2.readUnsignedByte() == 1 ? true : false);
					def.maxHit = stream2.readUnsignedByte()-1;
					def.hitpoints = stream2.readUnsignedWord()-1;
					def.attackSpeed = stream2.readUnsignedWord()-1;
					def.attackAnim = stream2.readUnsignedWord()-1;
					def.defenceAnim = stream2.readUnsignedWord()-1;
					def.deathAnim = stream2.readUnsignedWord()-1;
					def.atkSound = stream2.readUnsignedWord()-1;
					def.blockSound = stream2.readUnsignedWord()-1;
					def.deathSound = stream2.readUnsignedWord()-1;
					def.attackBonus = stream2.readUnsignedWord()-1;
					def.defenceMelee = stream2.readUnsignedWord()-1;
					def.defenceRange = stream2.readUnsignedWord()-1;
					def.defenceMage = stream2.readUnsignedWord()-1;
				}
				if(type == 2){
					def.shopId = stream2.readUnsignedWord()-2;
				}
				if(type == 3){
					int i = stream2.readUnsignedWord()-1;
					final NpcDefinition def2 = forId(i);
					def.respawn = def2.respawn;
					def.aggressive = def2.aggressive;
					def.retreats = def2.retreats;
					def.poisonous = def2.poisonous;
					def.maxHit = def2.maxHit;
					def.hitpoints = def2.hitpoints;
					def.attackSpeed = def2.attackSpeed;
					def.attackAnim = def2.attackAnim;
					def.defenceAnim = def2.defenceAnim;
					def.deathAnim = def2.deathAnim;
					def.atkSound = def2.atkSound;
					def.blockSound = def2.blockSound;
					def.deathSound = def2.deathSound;
					def.attackBonus = def2.attackBonus;
					def.defenceMelee = def2.defenceMelee;
					def.defenceRange = def2.defenceRange;
					def.defenceMage = def2.defenceMage;
					def.shopId = def2.shopId;
				}
				//handling
				World.getDefinitions()[def.getId()] = def;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        loadNpcDat();
        handleCombatDef();
		//System.out.println("Loaded");
	}
	
	static void handleCombatDef(){
		for (int i2 = 0; i2 < npcCount; i2++) {
			final NpcDefinition def = forId(i2);
		if((i2 < 1290 || i2 > 1293) && def.hitpoints > 0){//not touching koschei the deathless bonuses (frem trials)
			if(def.attackBonus != 0)
				def.attackBonus = (int) (def.hitpoints);
			if(def.defenceMelee != 0)
				def.defenceMelee = (int) (def.hitpoints);
			if(def.defenceMage != 0)
				def.defenceMage = (int) (def.hitpoints);
			if(def.defenceRange != 0)
				def.defenceRange = (int) (def.hitpoints);
		}
        boolean defExists = NpcCombatDef.defExists(def.getId());
        NpcCombatDef combatDef = NpcCombatDef.getDef(def.getId());
        if (defExists) {
        	if (!combatDef.isAttBonusSet() && !combatDef.isDefBonusSet()) {
        		combatDef = combatDef.bonusAtt(def.attackBonus, def.attackBonus, def.attackBonus, def.attackBonus, def.attackBonus).bonusDef(def.defenceMelee, def.defenceMelee, def.defenceMelee, def.defenceMage, def.defenceRange);
        	} else if (!combatDef.isAttBonusSet()) {
           		combatDef = combatDef.bonusAtt(def.attackBonus, def.attackBonus, def.attackBonus, def.attackBonus, def.attackBonus);
            } else if (!combatDef.isDefBonusSet()) {
        		combatDef = combatDef.bonusDef(def.defenceMelee, def.defenceMelee, def.defenceMelee, def.defenceMage, def.defenceRange);
        	}
         } else {
			combatDef = new NpcCombatDef() {
                @Override
                public AttackScript[] attackScripts(Entity attacker, Entity victim) {
                    // Entity attacker, Entity victim, final AttackStyle.Mode
                    // mode, final AttackStyle.Bonus bonus, final int damage,
                    // final int delay, final int animation
                    return new AttackScript[]{BasicAttack.meleeAttack(attacker, victim, AttackStyle.Mode.MELEE_ACCURATE, AttackStyle.Bonus.STAB, def.maxHit, def.attackSpeed / 600, def.attackAnim)};
                }
            }.respawnSeconds(def.respawn).bonusAtt(def.attackBonus, def.attackBonus, def.attackBonus, def.attackBonus, def.attackBonus).bonusDef(def.defenceMelee, def.defenceMelee, def.defenceMelee, def.defenceMage, def.defenceRange);
            if(def.getId() == 919 || def.getId() == 920 || def.getId() == 385 || def.getId() == 386 || def.getId() == 387 || def.getId() == 759){
            	combatDef.respawn(150);
            }
            if(def.getName().toLowerCase().equals("fishing spot")){
            	combatDef.respawn(50);
            }
         }
		NpcCombatDef.add(new int[]{def.getId()}, combatDef);
		}
	}
	
	public static void loadNpcDat()
	{
		Cache cache = Cache.getSingleton();
		Stream stream2 = null;
		try {
			stream2 = new Stream(new Archive(cache.getFile(0, 2)).getFile("npc.dat"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int totalNPCs = cache.getIndexTable().getNpcDefinitionIndices().length;
		stream2.currentOffset = 2;
		for(int k2 = 0; k2 < totalNPCs; k2++){
			readValues(stream2, k2);
		}
	}
	
	private static void readValues(Stream stream, int id)
	{
		final NpcDefinition def = forId(id);
		do
		{
			int i = stream.readUnsignedByte();
			if(i == 0){
				return;
			}	
			if(i == 1)
			{
				int j = stream.readUnsignedByte();
				for(int j1 = 0; j1 < j; j1++)
					stream.readUnsignedWord();
			} else
			if(i == 2){
				def.name = stream.readString();
			}else
			if(i == 3){
				String str = new String(stream.readBytes());
				def.examine = str;
			}else
			if(i == 12)
				def.size = stream.readSignedByte();
			else
			if(i == 13)
				stream.readUnsignedWord();
			else
			if(i == 14)
				stream.readUnsignedWord();
			else
			if(i == 17)
			{
				stream.readUnsignedWord();
				stream.readUnsignedWord();
				stream.readUnsignedWord();
				stream.readUnsignedWord();
			} else
			if(i >= 30 && i < 40)
			{
				String s = stream.readString();
				if(s.toLowerCase().equals("attack") && def.hitpoints > 0)
					def.attackable = true;
			} else
			if(i == 40)
			{
				int k = stream.readUnsignedByte();
				for(int k1 = 0; k1 < k; k1++)
				{
					stream.readUnsignedWord();
					stream.readUnsignedWord();
				}

			} else
			if(i == 60)
			{
				int l = stream.readUnsignedByte();
				for(int l1 = 0; l1 < l; l1++)
					stream.readUnsignedWord();

			} else
			if(i == 90)
				stream.readUnsignedWord();
			else
			if(i == 91)
				stream.readUnsignedWord();
			else
			if(i == 92)
				stream.readUnsignedWord();
			else
			if(i == 93){
			}else
			if(i == 95)
				def.combat = stream.readUnsignedWord();
			else
			if(i == 97)
				stream.readUnsignedWord();
			else
			if(i == 98)
				stream.readUnsignedWord();
			else
			if(i == 99){
			}else
			if(i == 100)
				stream.readSignedByte();
			else
			if(i == 101)
				stream.readSignedByte();
			else
			if(i == 102){
				int pray = stream.readUnsignedWord();
				if(pray == 0){
					def.protectMelee = true;
				}else if(pray == 1){
					def.protectRanged = true;
				}else if(pray == 2){
					def.protectMagic = true;
				}else if(pray == 6){
					def.protectRanged = true;
					def.protectMagic = true;
				}
			}else
			if(i == 103)
				stream.readUnsignedWord();
			else
			if(i == 106)
			{
						
				stream.readUnsignedWord();
				stream.readUnsignedWord();
				int i1 = stream.readUnsignedByte();
				for(int i2 = 0; i2 <= i1; i2++)
				{
					stream.readUnsignedWord();
				}
			} else
			if(i == 107){
			}
		} while(true);
	}

	public static class NPCINFO {
		private final String name;
		private final int level;
		private final boolean aggressive;
		private final boolean retreats;
		private final boolean poisonous;

		public NPCINFO(String name, int level, boolean aggressive, boolean retreats, boolean poisonous) {
			this.name = name;
			this.level = level;
			this.aggressive = aggressive;
			this.retreats = retreats;
			this.poisonous = poisonous;
		}

		public String getName() {
			return name;
		}

		public int getLevel() {
			return level;
		}

		public boolean isAggressive() {
			return aggressive;
		}

		public boolean isRetreats() {
			return retreats;
		}

		public boolean isPoisonous() {
			return poisonous;
		}

	}

	public static NpcDefinition forName(String name) {
		for (NpcDefinition d : World.getDefinitions()) {
			if (d.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase())) {
				return d;
			}
		}
		return null;
	}

	public static NpcDefinition forId(int id) {
		NpcDefinition d = World.getDefinitions()[id];
		if (d == null) {
			d = produceDefinition(id);
		}
		return d;
	}

	private int shopId = -1;
	
	private int id;
	private int dropsLinkedTo = -1;
	private String name, examine;
	public int respawn = 0, attackBonus = 20, defenceMelee = 20, defenceRange = 20, defenceMage = 20;
	private int combat = 0, hitpoints = 1, maxHit = 0, size = 1, attackSpeed = 4000, attackAnim = 422, defenceAnim = 404, deathAnim = 2304;
	private int blockSound = -1, atkSound = -1, deathSound = -1;

	private boolean attackable = false;
	private boolean aggressive = false;
	private boolean retreats = false;
	private boolean poisonous = false;
	
	private boolean protectMelee = false;
	private boolean protectRanged = false;
	private boolean protectMagic = false;

	public boolean isProtectMelee() {
		return protectMelee;
	}

	public boolean isProtectRanged() {
		return protectRanged;
	}

	public boolean isProtectMagic() {
		return protectMagic;
	}

	public int getId() {
		return id;
	}

	public int getBlockSound() {
		if(blockSound == 0)
			return -1;
		return blockSound;
	}
	
	public int getAtkSound() {
		if(atkSound == 0)
			return -1;
		return atkSound;
	}
	
	public int getDropsLinkedTo() {
		return dropsLinkedTo;
	}
	
	public void setDropsLinkedTo(int npc) {
		dropsLinkedTo = npc;
	}
	
	public int getDeathSound() {
		if(deathSound == 0)
			return -1;
		return deathSound;
	}
	
	public int getShop() {
		return shopId;
	}
	
	public String getName() {
		return name;
	}

	public String getExamine() {
		return examine;
	}

	public int getDeathAnim() {
		return deathAnim;
	}

	public int getBlockAnim() {
		return defenceAnim;
	}

	public int getAttackAnim() {
		return attackAnim;
	}

	public int getCombat() {
		return combat;
	}

	public int getSize() {
		return size;
	}

	public boolean isAggressive() {
		return aggressive;
	}

	public boolean retreats() {
		return retreats;
	}

	public boolean isPoisonous() {
		return poisonous;
	}

	public int getHitpoints() {
		return hitpoints;
	}

	public int getMaxhit() {
		return maxHit;
	}

	public static NpcDefinition produceDefinition(int id) {
		final NpcDefinition def = new NpcDefinition();
		def.id = id;
		def.name = "NPC #" + def.id;
		def.examine = "It's an NPC.";
		return def;
	}

	public boolean isAttackable() {
		return attackable;
	}

}
