package com.rs2.model.content.skills.woodcutting;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.DailyTask;
import com.rs2.model.content.randomevents.SpawnEvent;
import com.rs2.model.content.randomevents.SpawnEvent.RandomNpc;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.content.skills.Tools.Tool;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.functions.ChopVines;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.util.Misc;

public class ChopTree {

	public enum Tree {
		ACHEY_TREE(new int[]{2023}, new int[]{1746}, 1, 25, 2862, 3371, 75, 100),//
		NORMAL_TREE(new int[]{1276, 1277, 1278, 1279, 1280, 1282, 1283, 1284, 1285, 1286, 1289, 1290, 1291, 1315, 1316, 1318, 1319, 1330, 1331, 1332, 1333, 1365, 1383, 1384, 2409, 3033, 3034, 3035, 3036, 3881, 3882, 3883, 5902, 5903, 5904}, 
					new int[]{1719, 1720, 1721, 1721, 1721, 1722, 1722, 1722, 1723, 1727, 1727, 1728, 1729, 1741, 1742, 1743, 1743, 1744, 1744, 1744, 1732, 1725, 1727, 1745, 1721, 1719, 1721, 1722, 1726, 1747, 1747, 1747, 1722, 1727, 1727}, 1, 25, 1511, 1342, 75, 100),
		OAK_TREE(new int[]{1281, 2037}, new int[]{1739}, 15, 37.5, 1521, 1356, 17, 20),
		WILLOW_TREE(new int[]{1308, 5551, 5552, 5553}, new int[]{1736, 1737, 1737, 1738}, 30, 67.5, 1519, 7399, 17, 15),
		TEAK_TREE(new int[]{9036}, new int[]{2535}, 35, 85, 6333, 9037, 14, 20),//
		MAPLE_TREE(new int[]{1307, 4677}, new int[]{1735}, 45, 100, 1517, 1343, 75, 15),
		HOLLOW_TREE(new int[]{2289, 4060}, new int[]{1749, 1750}, 45, 83, 3239, 2310, 59, 15),//
		MAHOGANY_TREE(new int[]{9034}, new int[]{2534}, 50, 125, 6332, 9035, 80, 10),//
		YEW_TREE(new int[]{1309}, new int[]{1740}, 60, 175, 1515, 7402, 138, 5),
		MAGIC_TREE(new int[]{1306}, new int[]{1734}, 75, 250, 1513, 7401, 270, 5),
		DRAMEN_TREE(new int[]{1292}, null, 36, 0, 771, 1513, 59, 100),//
		SWAYING_TREE(new int[]{4142}, null, 40, 1, 3692, 1513, 59, 100),//
		VINES(new int[]{5103, 5104, 5105, 5106, 5107}, null, 34, 0, -1, 1513, 2, 100);
		
		private int[] id;
		private int[] ent;
		private int level;
		private double xp;
		private int log;
		private int stump;
		private int respawnTime;
        private int decayChance;

		public static Tree getTree(int id) {
            for(Tree tree : Tree.values()){
            	for (int ids : tree.getId()) {
	            	if (ids == id) {
	            		return tree;
	            	}
            	}
            }
            return null;
		}
		
		public static int getTreeIndex(int id, Tree tree){
			int i = 0;
            for (int ids : tree.getId()) {
	            if (ids == id) {
	            	return i;
	            }
	            i++;
            }
            return -1;
		}
		
		public static Tree getTreeEnt(int id) {
            for(Tree tree : Tree.values()){
            	if(tree.getEnt() == null)
            		continue;
            	for (int ids : tree.getEnt()) {
	            	if (ids == id) {
	            		return tree;
	            	}
            	}
            }
            return null;
		}
		
		public static Tree getActualTree(int id, boolean ent){
			if(!ent)
				return getTree(id);
			else
				return getTreeEnt(id);
		}

		private Tree(int[] id, int[] ent, int level, double xp, int log, int stump, int respawnTime, int decayChance) {
			this.id = id;
			this.ent = ent;
			this.level = level;
			this.xp = xp;
			this.log = log;
			this.stump = stump;
			this.respawnTime = respawnTime;
            this.decayChance = decayChance;
		}

		public int[] getId() {
			return id;
		}
		
		public int[] getEnt() {
			return ent;
		}

		public int getLevel() 	{
			return level;
		}

		public double getXP() {
			return xp;
		}

		public int getLog() {
			return log;
		}

		public int getStump() {
			return stump;
		}

		public int getRespawnTime() {
			return respawnTime;
		}

        public int getDecayChance() {
            return decayChance;
        }
	}
	
	static boolean entSpawned(final Player player, Tree tree, Position pos){
        if(tree.getEnt() == null)
        	return false;
        for (int ids : tree.getEnt()) {
        	Npc npc = Npc.getNpcByIdAndPos(ids, pos);
        	if(npc != null)
        		return true;
        }
		return false;
	}
	
	public static void handle(final Player player, final int id, final int x, final int y, final boolean ent) {
		if(!ent){
			final GameObject p = ObjectHandler.getInstance().getObject(x, y, player.getPosition().getZ());
			if (p != null && p.getDef().getId() != id) {
				return;
			}
		}else{
			Npc npc = Npc.getNpcByIdAndPos(id, new Position(x, y, player.getPosition().getZ()));
			if(npc == null){
				return;
			}
		}
		final Tree tree = Tree.getActualTree(id, ent);
		if(tree == null) {
			return;
		}
		if (!Constants.WOODCUTTING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		final Tool axe = Tools.getTool(player, Skill.WOODCUTTING);
		if(axe == null) {
			player.getActionSender().sendMessage("You do not have an axe which you have the woodcutting level to use.");
			return;
		}
		if(player.getSkill().getLevel()[Skill.WOODCUTTING] < tree.getLevel()) {
			player.getActionSender().sendMessage("You need a Woodcutting level of " + tree.getLevel() + " to cut this tree.");
			return;
		}
		final Item log = new Item(tree.getLog(), 1);
		if(player.getInventory().getItemContainer().freeSlot() == -1) {
			player.getActionSender().sendMessage("Your inventory is too full to hold any more " + log.getDefinition().getName().toLowerCase() + ".");
			player.getActionSender().sendSound(1878, 1, 0);
			return;
		}
		if (player.questStage[0] != 1) {
			player.getDialogue().sendStartInfo("Please wait.", "", "Your character is now attempting to cut down the tree. Sit back", "for a moment while he does all the hard work.", "", true);
		} else {
			player.getActionSender().sendMessage("You swing your axe at the "+(tree == Tree.VINES ? "vines" : "tree")+".");
		}
		player.getUpdateFlags().sendAnimation(axe.getAnimation(), 0);
		player.getActionSender().sendSound(471, 1, 0);
		player.breakToolCounter = 0;
		if(player.questStage[0] == 1)
			if(Misc.random_(800) == 0 && tree.getEnt() != null){
				int time = 15;
				int index = (Tree.getTreeIndex(id, tree) >= tree.getEnt().length ? 0 : Tree.getTreeIndex(id, tree));
				Npc npc = new Npc(tree.getEnt()[index]);
				GameObjectDef def = SkillHandler.getObject(id, x, y, player.getPosition().getZ());
				ObjectHandler.getInstance().addObject(new GameObject(Constants.EMPTY_OBJECT, x, y, player.getPosition().getZ(), def.getFace(), def.getType(), id, time), true);
				NpcLoader.spawnTimedStillNpc(npc, x, y, player.getPosition().getZ(), time);
			}
		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task)) {
					container.stop();
					return;
				}
				boolean entSpawned = entSpawned(player, tree, new Position(x, y, player.getPosition().getZ()));
				if(entSpawned){
					if(player.breakToolCounter >= 2){
						Tools.breakTool(player, Skill.WOODCUTTING);
						player.getActionSender().sendMessage("Your axe has been broken by the tree ent!");
						player.getActionSender().sendSound(343, 1, 0);
						player.getUpdateFlags().sendAnimation(-1);
						container.stop();
						return;
					}else{
						player.getActionSender().sendSound(472, 1, 0);
						player.getUpdateFlags().sendAnimation(axe.getAnimation(), 0);
						player.breakToolCounter++;
						return;
					}
				} else {
				final GameObject p = ObjectHandler.getInstance().getObject(x, y, player.getPosition().getZ());
				if (p != null && p.getDef().getId() != id) {
					player.getActionSender().sendMessage("The tree has run out of logs.");
					container.stop();
					return;
				}
				final Item log = new Item(tree.getLog(), 1);
				if(player.getInventory().getItemContainer().freeSlot() == -1) {
					player.getActionSender().sendMessage("Your inventory is too full to hold any more " + log.getDefinition().getName().toLowerCase() + ".");
					player.getActionSender().sendSound(1878, 1, 0);
					container.stop();
					return;
				}
				if (SkillHandler.doSpawnEvent(player)) {
					SpawnEvent.spawnNpc(player, RandomNpc.TREE_SPIRIT);
				}
				//if (Misc.random(900) == 0) { // Birds nest (chance = 281)
				if(Misc.random_(282) == 0){
                    GroundItem item = new GroundItem(new Item(5070 + Misc.random(4)), player);
                    GroundItemManager.getManager().dropItem(item);
					//World.getGroundItems().register(new GroundItem(new Item(5070 + Misc.random(4)), player));
				}
				if (SkillHandler.skillCheck(player.getSkill().getLevel()[Skill.WOODCUTTING], tree.getLevel(), axe.getBonus())) {
					player.getSkill().addExp(Skill.WOODCUTTING, tree.getXP());
					if (log.getId() > 0) {
						player.getInventory().addItem(log);
						DailyTask task = player.getDailyTask();
						if(task.getTaskTarget() == log.getId() && player.dailyTaskAmount < task.getReqAmount()){
							player.dailyTaskAmount++;
							if(player.dailyTaskAmount == task.getReqAmount())
								task.completed(player);
						}
						if (player.questStage[0] != 1) {
							player.getDialogue().sendItem1Line("You get some logs.", new Item(1511));
							player.getDialogue().endDialogue();
							if (player.questStage[0] == 8){
								player.getActionSender().createPlayerHints(1, -1);
								player.setNextTutorialStage();
							}
							player.getQuestHandler().setTutorialStage();
							player.setClickId(0);
						} else {
							if(tree != Tree.DRAMEN_TREE && tree != Tree.SWAYING_TREE)
								player.getActionSender().sendMessage("You get some " + log.getDefinition().getName().toLowerCase() + ".");
							else if(tree == Tree.DRAMEN_TREE)
								player.getActionSender().sendMessage("You cut a branch from the Dramen tree.");
							else if(tree == Tree.SWAYING_TREE)
								player.getActionSender().sendMessage("You cut a branch from the strangely musical tree.");
						}
					}
					if (tree != Tree.DRAMEN_TREE && tree != Tree.SWAYING_TREE && Misc.random(100) <= tree.decayChance) {
						if (tree != Tree.VINES) {
							player.getActionSender().sendMessage("The tree has run out of logs.");
							player.getActionSender().sendSound(473, 1, 0);
							player.getActionSender().sendSound(1312, 1, 0);
						}
						int face = SkillHandler.getFace(id, x, y, player.getPosition().getZ());
						new GameObject(tree.getStump(), x, y, player.getPosition().getZ(), face, 10, id, tree.getRespawnTime(), tree != Tree.VINES);
						container.stop();
						if (tree == Tree.VINES) {
							ChopVines.walkThru(player, x, y);
						}
						return;
					}
				}
				if (player.getInventory().getItemContainer().freeSlots() <= 0) {
					player.getActionSender().sendMessage("Not enough space in your inventory.");
					player.getActionSender().sendSound(1878, 1, 0);
					player.getUpdateFlags().sendAnimation(-1);
					container.stop();
					return;
				}
				}
				player.getActionSender().sendSound(472, 1, 0);
				player.getUpdateFlags().sendAnimation(axe.getAnimation(), 0);
			}
			@Override
			public void stop() {
				player.getMovementHandler().reset();
				player.getUpdateFlags().sendAnimation(-1, 0);
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 4);
	}

}
