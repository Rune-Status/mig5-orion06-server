package com.rs2.model.content.skills.runecrafting;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.questing.QuestDefinition;
import com.rs2.model.content.skills.runecrafting.Runecrafting.Runes;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class RunecraftAltars {

	public enum Altar {
		AIR_ALTAR(1438, 5527, 2452, new Position(2987, 3292, 0), 2478, 2465, new Position(2841, 4829, 0), 25), // air
		MIND_ALTAR(1448, 5529, 2453, new Position(2982, 3512, 0), 2479, 2466, new Position(2793, 4828, 0), 27.5), // mind
		WATER_ALTAR(1444, 5531, 2454, new Position(3183, 3165, 0), 2480, 2467, new Position(2726, 4832, 0), 30), // water
		EARTH_ALTAR(1440, 5535, 2455, new Position(3304, 3474, 0), 2481, 2468, new Position(2655, 4830, 0), 32.5), // earth
		FIRE_ALTAR(1442, 5537, 2456, new Position(3312, 3253, 0), 2482, 2469, new Position(2574, 4849, 0), 35), // fire
		BODY_ALTAR(1446, 5533, 2457, new Position(3051, 3445, 0), 2483, 2470, new Position(2523, 4826, 0), 37.5), // body
		COSMIC_ALTAR(1454, 5539, 2458, new Position(2407, 4379, 0), 2484, 2471, new Position(2142, 4813, 0), 40), // cosmic
		CHAOS_ALTAR(1452, 5543, 2461, new Position(3062, 3591, 0), 2487, 2474, new Position(2275, 4847, 3), 42.5), // chaos
		NATURE_ALTAR(1462, 5541, 2460, new Position(2868, 3017, 0), 2486, 2473, new Position(2400, 4835, 0), 45), // nature
		LAW_ALTAR(1458, 5545, 2459, new Position(2859, 3379, 0), 2485, 2472, new Position(2464, 4818, 0), 47.5), // law
		DEATH_ALTAR(1456, 5547, 2462, new Position(1862, 4639, 0), 2488, 2475, new Position(2208, 4830, 0), 50), // death
		BLOOD_ALTAR(1450, 5549, 2464, new Position(3469, 3391, 0), 2490, 2477, new Position(2254, 4913, 0), 52.5), // blood
		SOUL_ALTAR(1460, 5551, 2463, new Position(2241, 3199, 0), 2489, 2476, new Position(2331, 4826, 0), 55); // soul

		int talisman;
		int tiara;
		int ruinId;
		int altarId;
		int altarPortalId;
		Position altarPos;
		Position ruinPos;
		double tiaraXp;

		private Altar(int talisman, int tiara, int ruinId, Position ruinPos, int altarId, int altarPortalId, Position altarPos, double tiaraXp) {
			this.talisman = talisman;
			this.tiara = tiara;
			this.ruinId = ruinId;
			this.ruinPos = ruinPos;
			this.altarId = altarId;
			this.altarPortalId = altarPortalId;
			this.altarPos = altarPos;
			this.tiaraXp = tiaraXp;
		}
		
		public int getTalisman() {
			return talisman;
		}

		public int getTiara() {
			return tiara;
		}
		
		public double getTiaraXp() {
			return tiaraXp;
		}

		public int getRuinId() {
			return ruinId;
		}

		public Position getRuinPosition() {
			return ruinPos;
		}

		public int getAltarId() {
			return altarId;
		}

		public int getAltarPortalId() {
			return altarPortalId;
		}

		public Position getAltarPosition() {
			return altarPos;
		}

	}

	public static Altar getAltarByTalismanId(int id) {
		for (Altar altar : Altar.values()) {
			if (id == altar.getTalisman()) {
				return altar;
			}
		}
		return null;
	}

	public static Altar getAltarRuinId(int id) {
		for (Altar altar : Altar.values()) {
			if (id == altar.getRuinId()) {
				return altar;
			}
		}
		return null;
	}

	public static Altar getAltarPortalId(int id) {
		for (Altar altar : Altar.values()) {
			if (id == altar.getAltarPortalId()) {
				return altar;
			}
		}
		return null;
	}

	public static Altar getAltarByRuinId(int talisman, int ruinId) {
		for (Altar altar : Altar.values()) {
			if (talisman == altar.getTalisman() && ruinId == altar.getRuinId()) {
				return altar;
			}
		}
		return null;
	}

	public static Altar getAltarByAltarId(int talisman, int ruinId) {
		for (Altar altar : Altar.values()) {
			if (talisman == altar.getTalisman() && ruinId == altar.getAltarId()) {
				return altar;
			}
		}
		return null;
	}

	public static boolean runecraftAltar(Player player, int objectId) {
		switch (objectId) {
		// Runecraft
		case 2489:
			Runecrafting.craftRunes(player, Runes.SOUL);
			return true;
		case 2490:
			Runecrafting.craftRunes(player, Runes.BLOOD);
			return true;
		case 2482:
			Runecrafting.craftRunes(player, Runes.FIRE);
			return true;
		case 2480:
			Runecrafting.craftRunes(player, Runes.WATER);
			return true;
		case 2478:
			Runecrafting.craftRunes(player, Runes.AIR);
			return true;
		case 2481:
			Runecrafting.craftRunes(player, Runes.EARTH);
			return true;
		case 2479:
			Runecrafting.craftRunes(player, Runes.MIND);
			return true;
		case 2483:
			Runecrafting.craftRunes(player, Runes.BODY);
			return true;
		case 2488:
			Runecrafting.craftRunes(player, Runes.DEATH);
			return true;
		case 2486:
			Runecrafting.craftRunes(player, Runes.NATURE);
			return true;
		case 2487:
			Runecrafting.craftRunes(player, Runes.CHAOS);
			return true;
		case 2485:
			Runecrafting.craftRunes(player, Runes.LAW);
			return true;
		case 2484:
			Runecrafting.craftRunes(player, Runes.COSMIC);
			return true;
		case 7141: // blood
			boolean questDone1 = (player.questStage[72] == 1 ? true : false);
			if(!questDone1){
				QuestDefinition questDef = QuestDefinition.forId(72);
				String questName = questDef.getName();
				player.getDialogue().sendStatement("You need to complete "+questName+" first.");
				player.getDialogue().endDialogue();
			}else
				player.getTeleportation().teleportObelisk(2254, 4913, 0);
			return true;
		case 7138: // soul
			if(player.getStaffRights() < 2)
				player.getActionSender().sendMessage("This has been disabled temporarily.");
			else
				player.getTeleportation().teleportObelisk(2331, 4826, 0);
			return true;
		case 7133://nature
			player.getTeleportation().teleportObelisk(2400, 4835, 0);
			return true;
		case 7132://cosmic
			boolean questDone2 = (player.questStage[58] == 1 ? true : false);
			if(!questDone2){
				QuestDefinition questDef = QuestDefinition.forId(58);
				String questName = questDef.getName();
				player.getDialogue().sendStatement("You need to complete "+questName+" first.");
				player.getDialogue().endDialogue();
			}else
				player.getTeleportation().teleportObelisk(2142, 4813, 0);
			return true;
		case 7129://fire
			player.getTeleportation().teleportObelisk(2574, 4849, 0);
			return true;
		case 7130://earth
			player.getTeleportation().teleportObelisk(2655, 4830, 0);
			return true;
		case 7131://body
			player.getTeleportation().teleportObelisk(2523, 4826, 0);
			return true;
		case 7140://mind
			player.getTeleportation().teleportObelisk(2793, 4828, 0);
			return true;
		case 7139://air
			player.getTeleportation().teleportObelisk(2841, 4829, 0);
			return true;
		case 7137://water
			player.getTeleportation().teleportObelisk(2726, 4832, 0);
			return true;
		case 7136://death
			if(player.getStaffRights() < 2)
				player.getActionSender().sendMessage("This has been disabled temporarily.");
			else
				player.getTeleportation().teleportObelisk(2208, 4830, 0);
			return true;
		case 7135://law
			if (player.hasCombatEquipment()) {
				player.getDialogue().sendStatement("You cannot bring combat items to Entrana!");
				player.getDialogue().endDialogue();
			} else
				player.getTeleportation().teleportObelisk(2464, 4818, 0);
			return true;
		case 7134://chaos
			player.getTeleportation().teleportObelisk(2281, 4837, 0);
			return true;
		default:
			return false;
		}
	}

	public static boolean clickRuin(Player player, int objectId) {
		Altar ruinAltar = getAltarRuinId(objectId);
		Altar portalAltar = getAltarPortalId(objectId);
		if(ruinAltar == null && portalAltar == null)
			return false;
		if (!Constants.RUNECRAFTING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if(player.questStage[14] != 1){
			QuestDefinition questDef = QuestDefinition.forId(14);
			String questName = questDef.getName();
			player.getActionSender().sendMessage("You need to complete "+questName+" to do this.");
			return true;
		}
		if (ruinAltar != null) {
			player.teleport(ruinAltar.getAltarPosition());
			player.getActionSender().sendMessage("You feel a powerful force take hold of you...");
			return true;
		}
		if (portalAltar != null) {
			player.teleport(portalAltar.getRuinPosition());
			//}
			player.getActionSender().sendMessage("You step through the portal...");
			return true;
		}
		return false;
	}

	public static boolean useTaliOnRuin(final Player player, final int itemId, int objectId) {
		final Altar altar = getAltarByRuinId(itemId, objectId);
		if (altar == null) {
			return false;
		}
		if (!Constants.RUNECRAFTING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if(player.questStage[14] != 1){
			QuestDefinition questDef = QuestDefinition.forId(14);
			String questName = questDef.getName();
			player.getActionSender().sendMessage("You need to complete "+questName+" to do this.");
			return true;
		}
		player.getActionSender().sendMessage("You hold the " + ItemManager.getInstance().getItemName(altar.getTalisman()) + " towards the mysterious ruins.");
		player.getUpdateFlags().sendAnimation(827);
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.getActionSender().sendMessage("You feel a powerful force take hold of you...");
				player.teleport(altar.getAltarPosition());
				container.stop();
			}
			@Override
			public void stop() {
				player.setStopPacket(false);
			}
		}, 2);
		return true;
	}

}
