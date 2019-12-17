package com.rs2.model.content.skills.fishing;

import java.util.Random;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.DailyTask;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.randomevents.SpawnEvent;
import com.rs2.model.content.randomevents.SpawnEvent.RandomNpc;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 07/04/12 Time: 13:34 To change
 * this template use File | Settings | File Templates.
 */
public class Fishing {

	private Player player;

	public Fishing(Player player) {
		this.player = player;
	}

	/**
	 * The java.util.Random instance used for this class.
	 */
	static final Random r = new Random();

	/**
	 * A simple resetting animation.
	 */
	private static final int RESET = -1;

	public boolean canFish(final FishingSpot spot) {
		/*
		 * Check if fishing skill is enabled
		 */
		if (!Constants.FISHING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return false;
		}
		/*
		 * We have sure the player have space for this fish..
		 */
		if (player.getInventory().getItemContainer().freeSlots() <= 0) {
			player.getUpdateFlags().sendAnimation(RESET);
			player.getDialogue().sendStatement("Not enough space in your inventory.");
			player.setClickId(0);
			return false;
		}
		/*
		 * This is the minimum level requirement dealing with the spot we're
		 * fishing at.
		 */
		final int levelReq = spot.getLevels()[0];

		/*
		 * We check if the players fishing level is high enough to fish at this
		 * spot.
		 */
		if (player.getSkill().getLevel()[Skill.FISHING] < levelReq) {
			player.getDialogue().sendStatement("You need a fishing level of at least " + levelReq + " in order to fish at this spot.");
			player.getUpdateFlags().sendAnimation(RESET);
			player.setClickId(0);
			return false;
		}

		/*
		 * If we somehow lost our fishing tool, we stop the action as well. is
		 * moving around, or he walks away.
		 */
		if (!player.getInventory().getItemContainer().contains(spot.getTool().getId())) {
			player.getDialogue().sendStatement("You need a " + spot.getTool().getDefinition().getName().toLowerCase() + " in order to fish at this spot.");
			player.getUpdateFlags().sendAnimation(RESET);
			player.setClickId(0);
			return false;
		}

		/*
		 * We make sure the players inventory contains the bait needed (In case
		 * we need some to fish at the spot)
		 */
		if (spot.getBait() != null) {
			if (!player.getInventory().getItemContainer().contains(spot.getBait().getId())) {
				player.getDialogue().sendStatement("you need more " + spot.getBait().getDefinition().getName().toLowerCase().toLowerCase() + " in order to fish at this spot.");
				player.getUpdateFlags().sendAnimation(RESET);
				player.setClickId(0);
				return false;
			}
		}
		return true;

	}

	public boolean fish(final Npc npc, final int action) {
		Whirlpool whirlpoolData = Whirlpool.spotIsWhirlpool(npc.getDefinition().getId());
		int realNpc = npc.getNpcId();
		if(whirlpoolData != null){
			realNpc = whirlpoolData.getFishspots()[0];
		}
		final FishingSpot spot = FishingSpot.getSpot(realNpc, action);
		final Position currentSpotPosition = npc.getPosition();

		/*
		 * A player will only stop fishing if the fishing spot is moving around,
		 * or he walks away (Which is done automaticly)
		 */
		if (!FishingSpots.spotExists(currentSpotPosition, spot) && whirlpoolData == null) {
			return false;
		}
		if (!canFish(spot))
			return true;
		if (player.questStage[0] != 1) {
			player.getDialogue().sendStartInfo("Please wait.", "This should only take a few seconds.", "As you gain Fishing experience you'll find that there are many", "types of fish and many ways to catch them.", "", true);
		} else {
			player.getActionSender().sendMessage("You attempt to catch a fish...");
		}

		/*
		 * If we're been in the cycle for a number of times remindable with 2
		 * (tick(600) * 4 ms = 2400 ms), we play the animation.
		 */
		player.getUpdateFlags().sendAnimation(spot.getAnimation());
		player.breakToolCounter = 0;
		if(spot.getTool().getId() == 307 || spot.getTool().getId() == 309)//rods
			player.getActionSender().sendSound(377, 1, 0);
		else
			player.getActionSender().sendSound(289, 1, 0);

		final int task = player.getTask();
		if(player.questStage[0] == 1)
			if(Misc.random_(800) == 0){
				int time = 20;
				Whirlpool whirlpool = Whirlpool.getWhirlpoolForSpot(npc.getDefinition().getId());
				if(whirlpool != null){
					npc.sendTransform(whirlpool.getWhirlpoolId(), time);
				}
			}
		player.setSkilling(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task)) {
					container.stop();
					return;
				}
				if(npc == null || npc.isDead() || !npc.isVisible()){
					player.getUpdateFlags().sendAnimation(-1);
					container.stop();
					return;
				}
				if (player.getInventory().getItemContainer().freeSlots() <= 0) {
					player.getActionSender().sendMessage("Not enough space in your inventory.");
					player.getActionSender().sendSound(1878, 1, 0);
					container.stop();
					return;
				}
				Whirlpool whirlpoolData = Whirlpool.spotIsWhirlpool(npc.getDefinition().getId());
				if(whirlpoolData != null){
					if(player.breakToolCounter >= 2){
						player.getActionSender().sendMessage("Your fishing equipment disappears into the whirlpool.");
						player.getInventory().removeItem(new Item(spot.getTool().getId(), 1));
						player.getUpdateFlags().sendAnimation(-1);
						container.stop();
						return;
					}else{
						player.getActionSender().sendSound(378, 1, 0);
						player.getUpdateFlags().sendAnimation(spot.getAnimation());
						player.breakToolCounter++;
						return;
					}
				} else {
				
				/*
				 * We calculate the max index of the fish we can fish with our
				 * current fishing level.
				 */
				int maxIndex = 0;
				for (int level : spot.getLevels()) {
					if (player.getSkill().getLevel()[Skill.FISHING] >= level) {
						maxIndex++;
					} else
						break;
				}
				
				/*
				 * We get a random index.
				 */
				if(spot == FishingSpot.BIG_NETTING){
					if(maxIndex > 3)
						maxIndex = 3;
				}
				
				int index = r.nextInt(maxIndex);
				
				if(spot == FishingSpot.BIG_NETTING){
					if (r.nextInt(59) == 0)//casket 405
						index = 3;	
					else if (r.nextInt(33) == 0)//gloves 1059
						index = 4;
					else if (r.nextInt(23) == 0)//oyster 407
						index = 5;
					else if (r.nextInt(14) == 0)//sea weed 401
						index = 6;
					else if (r.nextInt(14) == 0)//boots 1061
						index = 7;
				}

				/*
				 */
				Item fish = spot.getFish()[index];

				/*
				 * We check if the fisher receives this fish, based on his
				 * fishing level etc.
				 */
				if (SkillHandler.skillCheck(player.getSkill().getLevel()[Skill.FISHING], spot.getLevels()[index], 0)) {
					player.getInventory().addItem(fish);
					player.getActionSender().sendMessage("You catch " + (fish.getId() == 1061 || fish.getId() == 1059 || fish.getId() == 401 || fish.getId() == 321 || fish.getId() == 317 ? "some " : "a ") + fish.getDefinition().getName().toLowerCase().replace("raw ", "") + ".");
					player.getSkill().addExp(Skill.FISHING, spot.getExperience()[index]);
					DailyTask task = player.getDailyTask();
					if(task.getTaskTarget() == fish.getId() && player.dailyTaskAmount < task.getReqAmount()){
						player.dailyTaskAmount++;
						if(player.dailyTaskAmount == task.getReqAmount())
							task.completed(player);
					}
					if (player.questStage[0] != 1) {
						if (player.questStage[0] == 12){
							player.getActionSender().createPlayerHints(1, -1);
							player.setNextTutorialStage();
						}
						player.getQuestHandler().setTutorialStage();
						container.stop();
						player.getUpdateFlags().sendAnimation(-1);
						return;
					}
					if (spot.getBait() != null) {
						player.getInventory().removeItem(spot.getBait());
					}
				}
				
				/*if(!player.getInventory().canAddItem(fish)) {
					container.stop();
					player.getUpdateFlags().sendAnimation(-1);
				}*/
				
				if (player.getInventory().getItemContainer().freeSlots() <= 0) {
					player.getActionSender().sendMessage("Not enough space in your inventory.");
					player.getActionSender().sendSound(1878, 1, 0);
					player.getUpdateFlags().sendAnimation(-1);
					container.stop();
					return;
				}
				
				/*
				 * Checks if fisher still has any bait left
				 */
				if (spot.getBait() != null && !player.getInventory().playerHasItem(spot.getBait())) {
					player.getDialogue().sendStatement("you have run out of " + spot.getBait().getDefinition().getName().toLowerCase().toLowerCase() + ".");
					container.stop();
					return;
				}
				if(player.questStage[0] == 1)
				if(Misc.random_(200) == 0){
            		Npc fishy = FishingSpots.FISHING_SPOTS.get(npc.getPosition());
            		if(fishy != null){
            			FishingSpots.FISHING_SPOTS.remove(fishy.getPosition());
            		}
					CombatManager.endDeath(npc, player, false);
					player.getUpdateFlags().sendAnimation(-1);
					container.stop();
					return;
				}
				/*
				 * Checks if random should be spawned
				 */
				if (SkillHandler.doSpawnEvent(player)) {
					SpawnEvent.spawnNpc(player, RandomNpc.RIVER_TROLL);
				}
				/*
				 * The first delay is 0, so we start fishing instantly, but we
				 * want to delay it a bit.
				 */
				container.setTick(5);
				
				/*
				 * We increase the cycle.
				 */
				player.getActionSender().sendSound(378, 1, 0);
				player.getUpdateFlags().sendAnimation(spot.getAnimation());
			}
			}

			@Override
			public void stop() {
				player.resetAnimation();
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 5);

		return true;
	}

	/**
	 * Whirlpool flag.
	 */
	private boolean whirlpool = false;

	public enum FishingSpot {

		/*
		 * Contains Shrimps and Anchovies.
		 */
		NORMAL_NETTING(new int[] { 316 }, 303, -1, 621, new int[] { 1, 15 }, new int[] { 317/* Shrimp */, 321 /* Anchovies */}, new double[] { 10, 40 }),

		/*
		 * Contains Sardines and Herrings.
		 */
		SEA_BAITING(new int[] { 316 }, 307, 313, 622, new int[] { 5, 10 }, new int[] { 327 /* Sardine */, 345 /* Herring */}, new double[] { 20, 30 }),

		/*
		 * Contains Trouts, Salmons and Rainbow fish (only with stripy feathers)
		 * //TODO: Rainbow fish support? LOL.
		 */
		FLY_LURING(new int[] { 309 }, 309 /* Fly fishing rod */, 314/* Feather */, 622, new int[] { 20, 30 }, new int[] { 335 /* Trout */, 331 /* Salmon */}, new double[] { 50, 70 }),

		/*
		 * Contains Pikes.
		 */
		RIVER_BAITING(new int[] { 309 }, 307 /* Fishing rod */, 313 /*
																	 * Fishing
																	 * bait
																	 */, 622, new int[] { 25 }, new int[] { 349 /* Pike */}, new double[] { 60 }),

		/*
		 * Contains lobsters obviously.
		 */
		LOBSTER_CAGING(new int[] { 312 }, 301 /* Lobster pot */, -1/* No bait */, 619, new int[] { 40 }, new int[] { 377 /* Lobster */}, new double[] { 90 }),

		/*
		 * Contains Tuna and Swordfishes.
		 */
		HARPOONING(new int[] { 312 }, 311 /* Harpoon */, -1/* No bait */, 618, new int[] { 35, 50 }, new int[] { 359 /* Tuna */, 371 /* Swordfish */}, new double[] { 80, 100 }),

		// TODO: Casket, Oyster Seaweed, Leather boots/Gloves, (Maybe more?)
		// support.
		/*
		 * Contains Mackerel, Cod, Bass, Casket, Seaweed as well as Junk.
		 */
		//BIG_NETTING(new int[] { 313 }, 305 /* Big fishing net */, -1/* No bait */, 620, new int[] { 16, 23, 46 }, new int[] { 353 /* Mackerel */, 341 /* Cod */, 363 /* Bass */}, new double[] { 20, 45, 100 }),
		
		BIG_NETTING(new int[] { 313 }, 305 /* Big fishing net */, -1/* No bait */, 620, new int[] { 16, 23, 46, 16, 16, 16, 16, 16 }, new int[] { 353 /* Mackerel */, 341 /* Cod */, 363 /* Bass */, 405, 1059, 407, 401, 1061}, new double[] { 20, 45, 100, 0, 1, 10, 1, 1 }),
		
		/*
		 * Contains sharks obviously.
		 */
		SHARK_HARPOONING(new int[] { 313 }, 311 /* Harpoon */, -1/* No bait */, 618, new int[] { 76 }, new int[] { 383 /* Shark */}, new double[] { 110 }),

		
		LAVAEEL_BAITING(new int[] { 800 }, 1585, 313, 622, new int[] { 53 }, new int[] { 2148 }, new double[] { 0 }),
		/*lava eel 2148
		 * oily fishing rod 1585
		 * 
		 * raw shripm 317
		 * small fish net 303
		*/
		
		/*
		 * Contains monkfishes obviously.
		 */
		MONKFISH_NETTING(new int[] { 1174 }, 303 /* Small fishing net */, -1, 621, new int[] { 62 }, new int[] { 7944 }, new double[] { 120 });

		/**
		 * Constructs a fishing spot.
		 */
		private FishingSpot(int[] npcIds, int tool, int bait, int animation, int[] levels, int[] fish, double[] exp) {
			this.npcIds = npcIds;
			this.tool = new Item(tool);
			this.bait = bait == -1 ? null : new Item(bait);
			this.animation = animation;
			this.levels = levels;
			Item[] fishes = new Item[fish.length];
			for (int index = 0; index < fish.length; index++) {
				fishes[index] = new Item(fish[index]);
			}
			this.fish = fishes;
			this.exp = exp;
		}

		/**
		 * Gets the bait needed to fish at this spot.
		 * 
		 * @return <code>null</code> if none, else - the fishing bait.
		 */
		public Item getBait() {
			return bait;
		}

		/**
		 * Gets all the fishes for this spot.
		 * 
		 * @return the fishes the spot contains.
		 */
		public Item[] getFish() {
			return fish;
		}

		/**
		 * Gets the animation for this fishing spot.
		 * 
		 * @return The animation for this spot.
		 */
		public int getAnimation() {
			return animation;
		}

		/**
		 * Gets the level requirement array for this spot.
		 * 
		 * @return The level requirements.
		 */
		public int[] getLevels() {
			return levels;
		}

		/**
		 * Gets the experience array for this spot.
		 * 
		 * @return The spots experience rewards.
		 */
		public double[] getExperience() {
			return exp;
		}

		/**
		 * Gets the tool needed to fish at this spot.
		 * 
		 * @return The needed tool.
		 */
		public Item getTool() {
			return tool;
		}

		/**
		 * Gets the NPC id's that belongs to this spot.
		 * 
		 * @return The NPC id's.
		 */
		public int[] getNPCIds() {
			return npcIds;
		}

		/**
		 * The NPC ids of this spot.
		 */
		private final int[] npcIds;

		/**
		 * The tool needed in order to fish at this spot.
		 */
		private final Item tool;

		/**
		 * The level requirements for this spot - based on the fish's index.
		 * {smallest level, then increasing.}
		 */
		private final int[] levels;

		/**
		 * The fishes we can fish at this spot.
		 */
		private final Item[] fish;

		/**
		 * The experience reward for this spot - based on the fish's index.
		 */
		private final double[] exp;

		/**
		 * The animation for this spot.
		 */
		private final int animation;

		/**
		 * The fishing bait needed for this spot.
		 */
		private final Item bait;

		/**
		 * Gets a fishing by an NPC id + packet option.
		 * 
		 * @param id
		 *            The NPC id.
		 * @return The FishingSpot, or <code>null</code> if the NPC is not a
		 *         FishingSpot.
		 */
		public static FishingSpot getSpot(int id, int option) {
			/*
			 * NPC option 1.
			 */
			if (option == 1) {
				switch (id) {
				/*
				 * Lure/Bait spot.
				 */
				case 309:
					return FLY_LURING;
					/*
					 * Lobster/Swords and tuna spot.
					 */
				case 312:
					return LOBSTER_CAGING;
					/*
					 * Net /baiting spot.
					 */
				case 316:
					return NORMAL_NETTING;
					/*
					 * Net/harpooning spot.
					 */
				case 313:
					return BIG_NETTING;
					
				case 800:
					return LAVAEEL_BAITING;	

				case 1174:
					return MONKFISH_NETTING;
				}
				/*
				 * NPC option 2.
				 */
			} else {
				switch (id) {
				/*
				 * Lure/Bait spot.
				 */
				case 309:
					return RIVER_BAITING;
					/*
					 * Lobster/Swords and tuna spot.
					 */
				case 312:
					return HARPOONING;
					/*
					 * Net /baiting spot.
					 */
				case 316:
					return SEA_BAITING;
					/*
					 * Net/harpooning spot.
					 */
				case 313:
					return SHARK_HARPOONING;

				}
			}
			return null;
		}
	}
	
	public enum Whirlpool {
		LURE_BAIT(403, new int[]{309}),
		NET_BAIT(404, new int[]{316}),
		CAGE_HARPOON(405, new int[]{312}),
		NET_HARPOON(406, new int[]{313}),
		HARPOON_NET(3849, new int[]{3848});
	
		private int whirlpool;
		private int[] ids;
		
		private Whirlpool(int whirlpool, int[] ids) {
			this.whirlpool = whirlpool;
			this.ids = ids;
		}
		
		public int[] getFishspots() {
			return ids;
		}
		
		public int getWhirlpoolId() {
			return whirlpool;
		}
	
		public static Whirlpool getWhirlpoolForSpot(int id) 	{
			for(Whirlpool whirlpool : Whirlpool.values()){
            	for (int ids : whirlpool.getFishspots()) {
	            	if (ids == id) {
	            		return whirlpool;
	            	}
            	}
            }
            return null;
		}
		
		public static Whirlpool spotIsWhirlpool(int id){
			for(Whirlpool whirlpool : Whirlpool.values()){	
	            if (whirlpool.getWhirlpoolId() == id) {
	            	return whirlpool;
            	}
            }
			return null;
		}
		
	}

}
