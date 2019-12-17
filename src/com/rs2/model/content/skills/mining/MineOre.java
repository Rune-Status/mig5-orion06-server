package com.rs2.model.content.skills.mining;

import com.rs2.Constants;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Position;
import com.rs2.model.content.DailyTask;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.randomevents.SpawnEvent;
import com.rs2.model.content.randomevents.SpawnEvent.RandomNpc;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.content.skills.Tools.Tool;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Area;
import com.rs2.util.Misc;
import com.rs2.util.NameUtil;

/**
 * Mining class
 */
// TODO: Add rune ess mining
public class MineOre {

	private Player player;

	public MineOre(Player player) {
		this.player = player;
	}

	public static final int[] granites = {6981, 6979, 6983};
	public static final int[] sandstone = {6977, 6971, 6975, 6973};

	public static final int[] normalGems = {1623, 1623, 1623, 1623, 1621, 1621, 1621, 1619, 1619, 1617};
	public static final int[] specialGems = {1625, 1625, 1627, 1627, 1629};

	public enum MiningData {
		COPPER(new int[]{2090, 2091, 3042, 9708, 9709, 9710, 11936, 11937, 11938, 11960, 11961, 11962}, 436, 1, 18, 5), TIN(new int[]{2094, 2095, 3043, 9714, 9715, 9716, 11933, 11934, 11935, 11957, 11958, 11959}, 438, 1, 18, 5), BLURITE(new int[]{2110, 10583, 10584}, 668, 10, 18, 42), IRON(new int[]{2092, 2093, 9717, 9718, 9719, 11954, 11955, 11956, 14856, 14857, 14858}, 440, 15, 35, 14), COAL(new int[]{2096, 2097, 10948, 11930, 11931, 11932, 11963, 11964, 11965, 14850, 14851, 14852}, 453, 30, 50, 75), GEM(new int[]{2111}, -1, 40, 65, 50), SANDSTONE(new int[]{10946}, -1, 35, 0, 15), GRANITE(new int[]{10947}, -1, 45, 0, 15), GOLD(new int[]{2098, 2099, 9720, 9721, 9722, 11183, 11184, 11185, 11951, 11952, 11953}, 444, 40, 65, 140), SILVER(new int[]{2100, 2101, 11186, 11187, 11188, 11948, 11949, 11950}, 442, 20, 40, 140), MITHRIL(new int[]{2102, 2103, 11942, 11943, 11944, 11945, 11946, 11947, 14853, 14854, 14855}, 447, 55, 80, 300), ADAMANTITE(new int[]{2104, 2105, 11939, 11940, 11941, 14862, 14863, 14864}, 449, 70, 95, 600), RUNITE(new int[]{2106, 2107, 14859, 14860, 14861}, 451, 85, 125, 1800), CLAY(new int[]{2108, 2109, 9711, 9712, 9713, 10949, 11189, 11190, 11191}, 434, 1, 5, 2), EMPTY(new int[]{10944,
				9723, 9724, 9725, 11555, 11552, 11553, 11554, 11557, 11556, 450, 451, 10587, 10585, 10586, 14832, 14833, 14834, 10945}, 0, 0, 0, 0);

		private int[] objectIDs;
		private int oreReceived;
		private int levelRequired;
		private int expReceived;
		private int respawnTimer;

		MiningData(int[] objectIDs, int oreReceived, int levelRequired, int expReceived, int respawnTimer) {
			this.objectIDs = objectIDs;
			this.oreReceived = oreReceived;
			this.levelRequired = levelRequired;
			this.expReceived = expReceived;
			this.respawnTimer = respawnTimer;
		}

		public static MiningData forId(int objectId) {
			for (MiningData miningData : MiningData.values())
				for (int object : miningData.objectIDs)
					if (objectId == object || getNormalFromSmokingRock(objectId) == object)
						return miningData;
			return null;
		}
	}

	public boolean canMine(final int object) {

		if (!miningRocks(object)) {
			return false;
		}
		if (!Constants.MINING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return false;
		}
		if (player.getInventory().getItemContainer().freeSlots() <= 0) {
			player.getActionSender().sendSound(1878, 1, 0);
			player.getActionSender().sendMessage("Not enough space in your inventory.");
			if (player.questStage[0] != 1) {
				player.getDialogue().sendStatement("Not enough space in your inventory.");
				player.setClickId(0);
			}
			return false;
		}
		if (Tools.getTool(player, Skill.MINING) == null) {
			player.getActionSender().sendMessage("You do not have a pickaxe that you can use.");
			if (player.questStage[0] != 1) {
				player.getDialogue().sendStatement("You do not have a pickaxe that you can use.");
				player.setClickId(0);
			}
			return false;
		}
		if (!SkillHandler.hasRequiredLevel(player, Skill.MINING, getLevelReq(object), "mine here")) {
			return false;
		}
		return true;
	}

	Area perfectGoldMine = new Area(2727, 9681, 2742, 9696, (byte)0);
	
	public void startMining(final int object, final int obX, final int obY) {
		if (!SkillHandler.checkObject(object, obX, obY, player.getPosition().getZ())) {
			return;
		}
		final GameObject p = ObjectHandler.getInstance().getObject(obX, obY, player.getPosition().getZ());
		if(getNormalFromSmokingRock(object) == -1)
		if (p != null) {
			player.getActionSender().sendMessage("There is currently no ores remaining in this rock.");
			if (player.questStage[0] != 1) {
				player.getDialogue().sendStatement("There is currently no ores remaining in this rock.");
				player.setClickId(0);
			}
			player.getActionSender().sendSound(429, 1, 0);
			return;
		}
		if (player.getInventory().getItemContainer().freeSlots() <= 0) {
			player.getActionSender().sendMessage("Not enough space in your inventory.");
			return;
		}

		player.getActionSender().sendMessage("You swing your pick at the rock.");
		if (player.questStage[0] != 1) {
			player.getDialogue().sendStartInfo("Please wait.", "", "Your character is now attempting to mine the rock.", "This should only take a few seconds.", "", true);
		}
		final int task = player.getTask();
		final MiningData miningData = MiningData.forId(object);
		if (miningData == null)
			return;
		final Tool pickaxe = Tools.getTool(player, Skill.MINING);
		if (pickaxe == null) {
			player.getActionSender().sendMessage("You do not have a pickaxe that you can use.");
			return;
		}
		final int itemReceived = miningData.oreReceived;
		if(itemReceived == 0){
			player.getActionSender().sendMessage("There is currently no ores remaining in this rock.");
			player.getActionSender().sendSound(429, 1, 0);
			return;
		}	
		final int getXp = miningData.expReceived;
		final int respawnTimer = getRespawnTimerFormula(miningData.respawnTimer);
		final int levelReq = miningData.levelRequired;
		player.resetAnimation();
		player.getUpdateFlags().sendAnimation(pickaxe.getAnimation());
		player.getActionSender().sendSound(432, 1, 0);
		player.breakToolCounter = 0;
		if(Misc.random_(800) == 0 && player.questStage[0] == 1){
			int smokingRock = getSmokingRock(object, new Position(obX, obY, player.getPosition().getZ()));
			GameObject o = ObjectHandler.getInstance().getObject(obX, obY, player.getPosition().getZ());
			if(o == null && smokingRock != -1){
				int time = 15;
				int face = SkillHandler.getFace(object, obX, obY, player.getPosition().getZ());
				int type = SkillHandler.getType(object, obX, obY, player.getPosition().getZ());
				ObjectHandler.getInstance().addObject(new GameObject(getSmokingRock(object, new Position(obX, obY, player.getPosition().getZ())), obX, obY, player.getPosition().getZ(), face, type, object, time), true);
			}
		}
		player.setSkilling(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task)) {
					container.stop();
					return;
				}
				boolean finishedMining = false;
				boolean smokeEvent = false;
				if(getNormalFromSmokingRock(object) == -1){
					int smokingRock = getSmokingRock(object, new Position(obX, obY, player.getPosition().getZ()));
					GameObject o = ObjectHandler.getInstance().getObject(obX, obY, player.getPosition().getZ());
					if(o != null && smokingRock != -1){
						if(o.getDef().getId() == smokingRock)
							smokeEvent = true;
					}
				}else{
					smokeEvent = true;
				}
				if(smokeEvent){
					if(player.breakToolCounter >= 2){
						Tools.breakTool(player, Skill.MINING);
						player.getActionSender().createStillGfx(157, obX, obY, 0, 1);
						player.getActionSender().sendMessage("Your pickaxe has been broken by the rock!");
						player.hit((Misc.random(player.getMaxHp()/20) + (player.getMaxHp()/20))+1, HitType.NORMAL);
						player.getUpdateFlags().sendAnimation(-1);
						player.getActionSender().sendSound(42, 1, 0);
						container.stop();
						return;
					} else {
						player.getUpdateFlags().sendAnimation(pickaxe.getAnimation());
					    player.getActionSender().sendSound(432, 1, 0);
					    player.breakToolCounter++;
					    return;
					}
				} else {
				final GameObject p = ObjectHandler.getInstance().getObject(obX, obY, player.getPosition().getZ());
				if (p != null && p.getDef().getId() != object) {
					if (player.questStage[0] != 1) {
						player.getDialogue().sendStatement("There is no more ore in this rock.");
						player.setClickId(0);
					}
					player.getActionSender().sendMessage("There is no more ore in this rock.");
					player.getActionSender().sendSound(429, 1, 0);
					container.stop();
					return;
				}
				if (SkillHandler.doSpawnEvent(player)) {
					SpawnEvent.spawnNpc(player, RandomNpc.ROCK_GOLEM);
				}
                container.setTick(3);
                if (Misc.random_(282) == 0 && player.questStage[0] == 1) {
                	Item gem = new Item(normalGems[Misc.random_(normalGems.length)], 1);
                	player.getInventory().addItem(gem);
                	String gemName = gem.getDefinition().getName().replace("Uncut ", "");
                	player.getActionSender().sendMessage("You just found "+NameUtil.getNameWithArticle(gemName)+"!");
                	if (!player.getInventory().canAddItem(new Item(getItemRecieved(object, itemReceived), 1))) {
    					container.stop();
    					return;
    				}
                }
			    player.getUpdateFlags().sendAnimation(pickaxe.getAnimation());
			    player.getActionSender().sendSound(432, 1, 0);
				if (SkillHandler.skillCheck(player.getSkill().getLevel()[Skill.MINING], levelReq, pickaxe.getBonus())) {
					int newItem = getItemRecieved(object, itemReceived);
					if(newItem == 444){
						int itemToGet = newItem;
						if(perfectGoldMine.contains(player.getPosition()))
							itemToGet = 446;
						player.getInventory().addItem(new Item(itemToGet, 1));
					}else
						player.getInventory().addItem(new Item(newItem, 1));
					player.getActionSender().sendMessage("You manage to mine some " + (object == 2111 ? "gem" : object == 10946 ? "sandstone" : object == 10947 ? "granite" : ItemManager.getInstance().getItemName(itemReceived).toLowerCase() + "."));
					if (player.questStage[0] != 1) {
						player.getDialogue().sendStatement("You manage to mine some " + (object == 2111 ? "gem" : object == 10946 ? "sandstone" : object == 10947 ? "granite" : ItemManager.getInstance().getItemName(itemReceived).toLowerCase() + "."));
						player.getDialogue().endDialogue();
						player.setClickId(0);
						if (player.questStage[0] == 33 && object == 3043){
							player.setNextTutorialStage();
						}else if (player.questStage[0] == 34 && object == 3042){
							player.setNextTutorialStage();
						}
						player.getQuestHandler().setTutorialStage();
					}
					player.getSkill().addExp(Skill.MINING, getExp(newItem, getXp));
					DailyTask task = player.getDailyTask();
					if(task.getTaskTarget() == newItem && player.dailyTaskAmount < task.getReqAmount()){
						player.dailyTaskAmount++;
						if(player.dailyTaskAmount == task.getReqAmount())
							task.completed(player);
					}
					try {
						int face = SkillHandler.getFace(object, obX, obY, player.getPosition().getZ());
						int type = SkillHandler.getType(object, obX, obY, player.getPosition().getZ());
						new GameObject(emptyOre(object), obX, obY, player.getPosition().getZ(), type == 22 ? getFace(face) : face, type == 11 ? 11 : 10, object, respawnTimer);
					} catch (Exception e) {
						e.printStackTrace();
					}
					container.stop();
					finishedMining = true;
				}
				if(!finishedMining)
				if (!player.getInventory().canAddItem(new Item(getItemRecieved(object, itemReceived), 1))) {
					container.stop();
					return;
				}
				}
			}
			@Override
			public void stop() {
				player.getUpdateFlags().sendAnimation(-1);
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 3);
	}

	public static int getFace(int face) {
		switch (face) {
			case 1 :
				return 2;
			case 2 :
				return 4;
			case 3 :
				return 1;
			case 4 :
				return 0;

		}
		return face;
	}

	private static double getExp(int itemReceived, int getXp) {
		switch (itemReceived) {
			// granite
			case 6979 :// 2kg
				return 50;
			case 6981 :// 5kg
				return 60;
			case 6983 : // 5kg
				return 75;
				// sandstone
			case 6971 :// 1kg
				return 30;
			case 6973 :// 2kg
				return 40;
			case 6975 :// 5kg
				return 50;
			case 6977 :// 10kg
				return 60;
		}
		return getXp;
	}

	public static int getItemRecieved(int object, int itemReceived) {
		switch (object) {
			case 0 : // random gem event
				return normalGems[Misc.randomMinusOne(normalGems.length)];
			case 2111 :
				while (true) {
					if (Misc.random(2) == 0) {
						return normalGems[Misc.randomMinusOne(normalGems.length)];
					} else {
						return specialGems[Misc.randomMinusOne(specialGems.length)];
					}
				}
			case 10947 :
				return granites[Misc.random(granites.length - 1)];
			case 10946 :
				return sandstone[Misc.random(sandstone.length - 1)];
		}
		return itemReceived;
	}

	public static boolean miningRocks(int object) {
		return MiningData.forId(object) != null;
	}

	private static int emptyOre(int object) {
		int[] ore1 = {9708, 9711, 9714, 9717, 9720};
		int[] ore2 = {9709, 9712, 9715, 9718, 9721};
		int[] ore3 = {9710, 9713, 9716, 9719, 9722};

		int[] ore4 = {11183, 11186, 11189, 11930, 11933, 11936, 11939, 11942, 11945, 11948, 11951, 11954, 11957, 11960, 11963};
		int[] ore5 = {11184, 11187, 11190, 11931, 11934, 11937, 11940, 11943, 11946, 11949, 11952, 11955, 11958, 11961, 11964};
		int[] ore6 = {11185, 11188, 11191, 11932, 11935, 11938, 11941, 11944, 11947, 11950, 11953, 11956, 11959, 11962, 11965};
		
		int[] ore7 = {14850, 14853, 14856, 14859, 14862};
		int[] ore8 = {14851, 14854, 14857, 14860, 14863};
		int[] ore9 = {14852, 14855, 14858, 14861, 14864};
		if (object == 10946 || object == 10948) {
			return 10944;
		}
		if (object == 10947 || object == 10949) {
			return 10945;
		}
		if (object == 2110) {
			return 10587;
		}
		if (object == 10583) {
			return 10585;
		}
		if (object == 10584) {
			return 10586;
		}
		for (int i : ore1) {
			if (object == i) {
				return 9723;
			}
		}
		for (int i : ore2) {
			if (object == i) {
				return 9724;
			}
		}
		for (int i : ore3) {
			if (object == i) {
				return 9725;
			}
		}
		for (int i : ore4) {
			if (object == i) {
				return object >= 11945 ? 11555 : 11552;
			}
		}
		for (int i : ore5) {
			if (object == i) {
				return object >= 11945 ? 11556 : 11553;
			}
		}
		for (int i : ore6) {
			if (object == i) {
				return object >= 11945 ? 11557 : 11554;
			}
		}
		for (int i : ore7) {
			if (object == i) {
				return 14832;
			}
		}
		for (int i : ore8) {
			if (object == i) {
				return 14833;
			}
		}
		for (int i : ore9) {
			if (object == i) {
				return 14834;
			}
		}
		if (object % 2 == 0 || object == 3043) {
			return 450;
		} else {
			return 451;
		}
	}

	private static int getLevelReq(int object) {
		if (MiningData.forId(object) != null)
			return MiningData.forId(object).levelRequired;
		return 0;
	}

	public boolean prospect(final int objectId) {
		int[] empty = {10587, 10585, 10586, 14832, 14833, 14834, 10944, 10945, 9723, 9724, 9725, 11555, 11552, 11553, 11554, 11557, 11556, 450, 451};
		for (int i : empty)
			if (objectId == i) {
				player.getActionSender().sendMessage("There is currently no ores remaining in this rock.");
				player.getActionSender().sendSound(429, 1, 0);
				return true;
			}
		MiningData miningData = MiningData.forId(objectId);
		if (miningData == null)
			return false;
		if (player.questStage[0] != 1)
			player.getDialogue().sendStartInfo("Please wait.", "", "Your character is now attempting to prospect the rock. This", "should only take a few seconds.", "", true);
		player.getActionSender().sendMessage("You examine the rock for ores...");
		player.setStopPacket(true);
		int oreId = getItemRecieved(objectId, miningData.oreReceived);
		final String oreName = ItemManager.getInstance().getItemName(oreId).toLowerCase().replaceAll("ore", "").trim();
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer b) {
				player.getActionSender().sendMessage("This rock contains " + (objectId == 2111 ? "gems" : objectId == 2491 ? "unbound Rune Stone essence" : oreName + "."));
				if (player.questStage[0] != 1) {
					if (player.questStage[0] == 30 && oreName.contains("tin")){
						player.setNextTutorialStage();
					}else if (player.questStage[0] == 31 && oreName.contains("copper")){
						player.setNextTutorialStage();
					}
					
					player.getDialogue().sendStatement("This rock contains " + (objectId == 2111 ? "gems" : objectId == 2491 ? "unbound Rune Stone essence" : oreName + "."));
					player.getQuestHandler().setTutorialStage();
					player.setClickId(0);
				}
				player.getActionSender().sendSound(431, 1, 0);
				player.setStopPacket(false);
				b.stop();
			}
			@Override
			public void stop() {

			}
		}, 5);
		return true;
	}

	public static int getSmokingRock(int object, Position objectClickingLocation) {
		if (objectClickingLocation == null) {
			return -1;
		}
		CacheObject g = ObjectLoader.object(object, objectClickingLocation.getX(), objectClickingLocation.getY(), objectClickingLocation.getZ());
		if (g.getDef().getId() >= 2090 && g.getDef().getId() <= 2111) {
			return g.getDef().getId() + 29;
		}
		if (g.getDef().getId() >= 10946 && g.getDef().getId() <= 10949) {
			return g.getDef().getId() + 246;
		}
		if (g.getDef().getId() >= 11183 && g.getDef().getId() <= 11191) {
			return g.getDef().getId() - 1456;
		}
		if (g.getDef().getId() >= 10583 && g.getDef().getId() <= 10584) {
			return g.getDef().getId() + 583;
		}
		if (g.getDef().getId() >= 14850 && g.getDef().getId() <= 14864) {
			return g.getDef().getId() - 15;
		}

		return -1;
	}
	
	public static int getSmokingRock(int object) {
		if (object >= 2090 && object <= 2111) {
			return object + 29;
		}
		if (object >= 9708 && object <= 9722) {
			return object + 1460;
		}
		if (object >= 10583 && object <= 10584) {
			return object + 583;
		}
		if (object >= 10946 && object <= 10949) {
			return object + 246;
		}
		if (object >= 11183 && object <= 11191) {
			return object - 1456;
		}
		if (object >= 11930 && object <= 11939) {
			return object - 506;
		}
		if (object >= 11940 && object <= 11944) {
			return object - 15;
		}
		if (object >= 11945 && object <= 11947) {
			return object - 24;
		}
		if (object >= 11948 && object <= 11953) {
			return object - 33;
		}
		if (object >= 11954 && object <= 11965) {
			return object - 521;
		}
		if (object >= 14850 && object <= 14864) {
			return object - 15;
		}
		return -1;
	}
	
	public static int getNormalFromSmokingRock(int object) {
		if (object >= 2119 && object <= 2140) {
			return object - 29;
		}
		if (object >= 9727 && object <= 9735) {
			return object + 1456;
		}
		if (object >= 11166 && object <= 11167) {
			return object - 583;
		}
		if (object >= 11168 && object <= 11182) {
			return object - 1460;
		}
		if (object >= 11192 && object <= 11195) {//
			return object - 246;
		}
		if (object >= 11424 && object <= 11432) {
			return object + 506;
		}
		if (object >= 11433 && object <= 11444) {
			return object + 521;
		}
		if (object >= 11915 && object <= 11920) {
			return object + 33;
		}
		if (object >= 11921 && object <= 11923) {
			return object + 24;
		}
		if (object >= 11925 && object <= 11929) {
			return object + 15;
		}
		if (object >= 14835 && object <= 14849) {
			return object + 15;
		}
		return -1;
	}

	public boolean handleRockSmokeClick(final Position loc) {
		final Tool pickaxe = Tools.getTool(player, Skill.MINING);
		if (pickaxe != null) {
			final int objectId = player.getClickId();
			player.getActionSender().sendMessage("You swing your pick at the rock.");
			final int task = player.getTask();
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer p) {
					if (!player.checkTask(task)) {
						p.stop();
						return;
					}
					player.getUpdateFlags().sendAnimation(pickaxe.getAnimation());
					p.stop();
				}
				@Override
				public void stop() {
				}
			}, 1);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer p) {
					GameObject o = ObjectHandler.getInstance().getObject(loc.getX(), loc.getY(), loc.getZ());
					if (o == null || o.getDef().getId() == objectId) {
						p.stop();
						return;
					}
					if (player.checkTask(task)) {
						Tools.breakTool(player, Skill.MINING);
						player.getActionSender().createStillGfx(157, loc.getX(), loc.getY(), 0, 1);
						//ObjectHandler.getInstance().removeObject(loc.getX(), loc.getY(), loc.getZ(), 10);
						player.getActionSender().sendMessage("Your pickaxe has been broken by the rock!");
						player.hit(Misc.random(5) + 5, HitType.NORMAL);
						player.getUpdateFlags().sendAnimation(-1);
					}
					p.stop();
				}
				@Override
				public void stop() {
				}
			}, 5);
			return true;
		} else {
			player.getActionSender().sendMessage("You do not have a pickaxe that you can use.");
		}
		return false;
	}

	public static int getRespawnTimerFormula(int timer) {
		return timer;
	}

}