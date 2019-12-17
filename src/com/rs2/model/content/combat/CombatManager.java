package com.rs2.model.content.combat;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.World;
import com.rs2.model.content.Following;
import com.rs2.model.content.combat.attacks.WeaponAttack;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.weapon.AttackStyle;
import com.rs2.model.content.cutscene.Cutscene;
import com.rs2.model.content.cutscene.cutscenes.PiratesOfTheGielinorScene;
import com.rs2.model.content.minigames.barrows.Barrows;
import com.rs2.model.content.questing.QuestConstants;
import com.rs2.model.content.randomevents.TalkToEvent;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.fishing.FishingSpots;
import com.rs2.model.content.skills.magic.Teleportation;
import com.rs2.model.content.skills.prayer.Prayer;
import com.rs2.model.content.treasuretrails.ClueScroll;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;

/**
 *  TODO: on death remove all victims hits from hitsStory
 *         log out during death Spell delays for ancients
 *         http://www.2006scape.com/services
 *         /forums/thread.ws?3,4,13136,38613,goto,3
 * 
 * 
 */
public class CombatManager extends Tick {

	private static CombatManager combatManager;

	public static final String NO_AMMO_MESSAGE = "You have no ammo left!";
	public static final String WRONG_AMMO_MESSAGE = "You can not use that kind of ammo!";
	public static final String AMMO_COMPATIBILITY_MESSAGE = "You cannot use that ammo with that weapon!";
	public static final String NO_SPECIAL_ENERGY_MESSAGE = "You have no special energy left.";

	private final List<Hit> hitsStory;

	public CombatManager() {
		super(1);
		hitsStory = new LinkedList<Hit>();
		World.getTickManager().submit(this);
	}

	public static void attack(Entity attacker, Entity victim) {
        if((victim.isNpc() && !((Npc)victim).isVisible()) || victim.isDead()) {
			return;
        }
        if (victim.getMaxHp() < 1 || (victim.isNpc() && (((Npc) victim).getNpcId() == 411 || TalkToEvent.isTalkToNpc(((Npc) victim).getNpcId())))) {
        	if (attacker.isPlayer()) {
        		((Player) attacker).getActionSender().sendMessage("You cannot attack this npc.");
        	}
        	CombatManager.resetCombat(attacker);
        	return;
        }
        if (attacker.isPlayer() && victim.isNpc()) {
    		if(!((Player) attacker).getQuestHandler().allowedToAttackNpc(((Npc) victim).getNpcId())){
    			((Player) attacker).getActionSender().sendMessage("You cannot attack this npc.");
    			CombatManager.resetCombat(attacker);
    			return;
    		}
    	}
		if (attacker.isPlayer() && attacker.inDuelArena()) {
			if (!((Player) attacker).getDuelMainData().canStartDuel()) {
	        	CombatManager.resetCombat(attacker);
				return;
			}
		}
        List<AttackUsableResponse> attacks = new LinkedList<AttackUsableResponse>();
        int distance = Misc.getDistance(attacker.getPosition(), victim.getPosition());
        attacker.fillUsableAttacks(attacks, victim, distance);
        AttackUsableResponse foundResponse = null;
        for (AttackUsableResponse response : attacks) {
            if (response.getType() != AttackUsableResponse.Type.FAIL) {
                if (foundResponse == null || response.getScript().distanceRequired() > foundResponse.getScript().distanceRequired())
                    foundResponse = response;
            }
        }
        distance = foundResponse == null ? 1 : foundResponse.getScript().distanceRequired();
        attacker.setFollowDistance(distance);
		CombatCycleEvent.startCombat(attacker, victim);
		if (attacker.isPlayer() && ((Player) attacker).questStage[0] == 47) {
			((Player) attacker).getDialogue().sendStartInfo("Sit back and watch.", "While you are fighting you will see a bar over your head. The", "bar shows how much health you have left. Your opponent will", "have one too. You will continue to attack the rat until it's dead", "or you do something else.", true);
		}
	}

	static boolean mageArenaNpcs(final Entity attacker, final Entity victim){
		if(victim.isNpc()){
        	Player player = (Player) attacker;
        	Npc npc = (Npc) victim;
        	if(npc.getNpcId() == 907){
        		npc.setDead(true);
				Npc npc1 = new Npc(908);
				NpcLoader.spawnNpcPos(player, npc.getPosition(), npc1, true, false);
				npc1.getUpdateFlags().setForceChatMessage("This is only the beginning; you can't beat me!");
				player.kolodionStage = 1;
        		return true;
        	}
        	if(npc.getNpcId() == 908){
        		npc.setDead(true);
				Npc npc1 = new Npc(909);
				NpcLoader.spawnNpcPos(player, npc.getPosition(), npc1, true, false);
				npc1.getUpdateFlags().setForceChatMessage("Foolish mortal; I am unstoppable.");
				player.kolodionStage = 2;
        		return true;
        	}
        	if(npc.getNpcId() == 909){
        		npc.setDead(true);
				Npc npc1 = new Npc(910);
				NpcLoader.spawnNpcPos(player, npc.getPosition(), npc1, true, false);
				npc1.getUpdateFlags().setForceChatMessage("Now you feel it... The dark energy.");
				player.kolodionStage = 3;
        		return true;
        	}
        	if(npc.getNpcId() == 910){
        		npc.setDead(true);
				Npc npc1 = new Npc(911);
				NpcLoader.spawnNpcPos(player, npc.getPosition(), npc1, true, false);
				npc1.getUpdateFlags().setForceChatMessage("Aaaaaaaarrgghhhh! The power!");
				player.kolodionStage = 4;
        		return true;
        	}
		}
		return false;
	}
	
	static boolean mageArenaNpcs2(final Entity attacker, final Entity victim){
		if(victim.isNpc()){
        	Player player = (Player) attacker;
        	Npc npc = (Npc) victim;
        	if(npc.getNpcId() == 911){
        		npc.setDead(true);
				CombatManager.startDeath(npc);
        		player.getTeleportation().forcedObjectTeleport(2541, 4717, 0, null);
        		player.kolodionStage = 5;
        		return true;
        	}
		}
		return false;
	}
	
	@Override
	public void execute() {
		try {
			// execute current hits
			List<Hit> hitList = new LinkedList<Hit>();
			hitList.addAll(hitsStory);
			hitsStory.clear();
			List<Hit> recoilHits = new LinkedList<Hit>();

			for (Hit hit : hitList) {
				hit.tick();
				if (hit.shouldExecute()) {
					if (!hit.getVictim().isDead())
						hit.execute(recoilHits);
					if (hit.getVictim().getCurrentHp() <= 0 && !hit.getVictim().isDead()) {
						if(hit.getAttacker() != null && hit.getVictim() != null)
						if((hit.getAttacker().isPlayer() && hit.getVictim().isNpc()) || (hit.getAttacker().isNpc() && hit.getVictim().isPlayer())){
							boolean b = false;
							if(hit.getAttacker().isPlayer())
								b = ((Player) hit.getAttacker()).getQuestHandler().controlDying(hit.getAttacker(), hit.getVictim());
							else
								b = ((Player) hit.getVictim()).getQuestHandler().controlDying(hit.getAttacker(), hit.getVictim());
							if(!b)
								b = mageArenaNpcs2(hit.getAttacker(), hit.getVictim());
			    			if(b)
			    				return;
			    		}
						if(hit.getAttacker() != null)
						if(hit.getAttacker().isNpc()){
							final Npc npc = (Npc) hit.getAttacker();
							if(npc.linkedPlayer != null){
								final Player player = npc.linkedPlayer;
								boolean b = false;
								//b = player.getQuestHandler().controlDying(player, hit.getVictim());
								b = player.getQuestHandler().controlDying(hit.getAttacker(), hit.getVictim());
								if(b)
									return;
							}
						}
						if(hit.getVictim() != null)
						if(hit.getVictim().isNpc()){
							final Npc npc = (Npc) hit.getVictim();
							if(npc.linkedPlayer != null){
								final Player player = npc.linkedPlayer;
								boolean b = false;
								b = player.getQuestHandler().controlDying(hit.getAttacker(), hit.getVictim());
								//b = player.getQuestHandler().controlDying(hit.getAttacker(), player);
								if(b)
									return;
							}
						}
						if(hit.getVictim() != null)
						if(hit.getVictim().isPlayer()){
							final Player player = (Player) hit.getVictim();
							if(player.godMode){
								player.setCurrentHp(player.getMaxHp());
								return;
							}
						}
						hit.getVictim().setDead(true);
						startDeath(hit.getVictim());
                        if (hit.getVictim().isPlayer() && hit.getVictim().inDuelArena()) {
                            ((Player) hit.getVictim()).getDuelMainData().getOpponent().getAttributes().put("canTakeDamage", false);
                            return;
                        }
                    }
				} else if (!hit.getVictim().isDead()) {
					hitsStory.add(hit);
				}
			}
			for (Hit hit : recoilHits) {
				hit.execute(null);
			}
			recoilHits.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void startDeath(final Entity died) {
		/*if (died.isDoorSupport()) {
			((Npc) died).sendTransform(((Npc) died).getNpcId() + 1, 10);
			new GameObject(Constants.EMPTY_OBJECT, died.getPosition().getX(), died.getPosition().getY(), died.getPosition().getZ(), 0, 10, 3, 35);
		}*/
		final Entity killer = died.findKiller();// == null ? possibleKiller : died.findKiller();
		if(died != null && killer != null)
		if(died.isNpc() && killer.isPlayer()){
			Npc npc = (Npc) died;
			for (Player players : World.getPlayers()) {
				if(players == null)
					continue;
				if(players.hintNpc == npc.getIndex() && npc.getPlayerOwner() == null){
					players.getActionSender().createPlayerHints(1, Npc.getNpcById(npc.getNpcId()).getIndex());
				}
				if(players.hintNpc == npc.getIndex() && npc.getPlayerOwner() == players){
					players.getActionSender().createPlayerHints(1, -1);
				}
			}
		}
		died.getTask();
        died.setDeathPosition(died.getPosition().clone());
		if (killer != null && killer.isPlayer()) {
			Player player = (Player) killer;
			player.getPjTimer().setWaitDuration(0);
			player.getPjTimer().reset();
			if (player.questStage[0] == 47 || player.questStage[0] == 49){
				player.setNextTutorialStage();
			}
		}
		if (died.isPlayer()) {
			Player player = (Player) died;
			player.setStopPacket(true);
		}
		final int deathAnimation = died.getDeathAnimation();
		if (deathAnimation != -1) {
			Tick tick = new Tick(2) {
				@Override
				public void execute() {
					int death = deathAnimation;
					if (died.isPlayer()) {
						Player player = (Player) died;
						player.setHideWeapons(true);
						player.setAppearanceUpdateRequired(true);
						if (player.transformNpc > 0) {
							death = new Npc(player.transformNpc).getDefinition().getDeathAnim();
						}
					}
					died.getUpdateFlags().sendAnimation(death);
					if(died != null && killer != null)
					if(died.isNpc() && killer.isPlayer()){
						Player player = (Player) killer;
						Npc npc = (Npc) died;
						player.getActionSender().sendSound(npc.getDeathSound(), 1, 20);
						
					}
					stop();
				}
			};
			World.getTickManager().submit(tick);
		}
		/*if (died != null && died.isPlayer()) {
			Player player = (Player) died;
			player.dropItems2(killer);
		}*/
		Tick deathTimer = new Tick(died.getDeathAnimationLength()) {
			@Override
			public void execute() {
				endDeath(died, killer, true);
				this.stop();
			}
		};
		World.getTickManager().submit(deathTimer);
		died.getTask();
		died.removeAllEffects();
		//died.getInCombatTick().setWaitDuration(0);
		//died.getInCombatTick().reset();
		//died.getPjTimer().setWaitDuration(0);
		//died.getPjTimer().reset();
		died.getInCombatTick().setWaitDuration(died.getDeathAnimationLength()+2);
		died.getInCombatTick().reset();
		died.getPjTimer().setWaitDuration(died.getDeathAnimationLength()+2);
		died.getPjTimer().reset();
		if (killer != null && died.isPlayer() && ((Player) died).getIsUsingPrayer()[Prayer.RETRIBUTION]) {
			Prayer.applyRetribution(died, killer);
		}
	}

	static Npc kalphite;
	
	public static void endDeath(final Entity died, final Entity killer, final boolean firstTime) {
		if(killer != null){
		if (died.isPlayer() && killer.isPlayer()) {	
			if(!((Player) died).isSkulled){
			((Player) killer).tickDifference = ((Player) killer).getTimerForKilling(((Player) killer), ((Player) died));
			}
		}
		}
		if (firstTime) {
			/*if(died != null && died.isPlayer()){
				died.dropItems3(killer);
			} else {
				died.dropItems(killer);
			}*/
			died.dropItems(killer);
        	if(died.getPoisonDamage() > 0)//test
        		died.setPoisonDamage(0);
            died.setDeathPosition(null);
    		if (killer != null && killer.isPlayer() && died.isNpc()) {
	    		final Npc npc = (Npc) died;
	    		final Player player = ((Player) killer);
	    		int value = player.getBossDefeats();
	    		((Player) killer).npcKillCount++;
	    		if(npc.getNpcId() == 50 && (value & 1) == 0){
	    			int newVal = value+1;
	    			player.setBossDefeats(newVal);
	    		}
	    		if(npc.getNpcId() == 1160 && (value & 2) == 0){
	    			int newVal = value+2;
	    			player.setBossDefeats(newVal);
	    		}
	    		if(npc.getNpcId() == 3200 && (value & 4) == 0){
	    			int newVal = value+4;
	    			player.setBossDefeats(newVal);
	    		}
	    		if(npc.getNpcId() == 2745 && (value & 8) == 0){
	    			int newVal = value+8;
	    			player.setBossDefeats(newVal);
	    		}
                ClueScroll.handleAttackerDeath((Player)killer, npc);
                ((Player) killer).getSlayer().handleNpcDeath(npc);
                ((Player) killer).getPrayer().handleNpcDeath(npc);
            	Barrows.handleDeath(((Player)killer), npc);
			}
		}
        if (died.isNpc()) {
    		final Npc npc = (Npc) died;
    		if(killer != null && npc != null)
    		if(killer.isPlayer() && died.isNpc()){
    			
    			if (died.isDoorSupport()) {
    				//ObjectHandler.getInstance().removeClip(died.getPosition().getX(), died.getPosition().getY(), died.getPosition().getZ(), 10, 0);
    				//((Npc) died).sendTransform(((Npc) died).getNpcId() + 1, 10);
    				new GameObject(Constants.EMPTY_OBJECT, died.getPosition().getX(), died.getPosition().getY(), died.getPosition().getZ(), 0, 10, 2620, 60);
    			}
    			
    			((Player) killer).getQuestHandler().handleNpcDeath(((Npc) died).getNpcId());
    			final Player player = (Player) killer;
    			//sailing ship check
    			/*if(player.inSailingShip() && npc.inSailingShip()){
    				boolean wasShipNpc = false;
    				for (Npc playerPirateShipNpcs : player.getPirateShipNpcs()) {
    					if(npc == playerPirateShipNpcs){
    						player.removePirateShipNpcs(npc);
    						wasShipNpc = true;
    						break;
    					}
    				}
    				if(wasShipNpc){
    					boolean clearOfSailors = true;
    					for (Npc playerPirateShipNpcs : player.getPirateShipNpcs()) {
        					if(playerPirateShipNpcs.getNpcId() == QuestConstants.SAILOR_CUSTOM){
        						clearOfSailors = false;
        						break;
        					}
        				}
    					if(clearOfSailors){
    						Cutscene cs = new PiratesOfTheGielinorScene(player, null);
    						cs.start();
    						for (Npc playerPirateShipNpcs : player.getPirateShipNpcs()) {
    							playerPirateShipNpcs.setVisible(false);
    				            World.unregister(playerPirateShipNpcs);
    						}
    					}
    				}
    				for (Npc playerPirateShipNpcs : player.getPirateShipNpcs()) {
    					System.out.println(playerPirateShipNpcs.getNpcId()+" "+playerPirateShipNpcs.isDead());
    				}
    			}*/
    			//fight cave check
    			if(npc.inCaves() && player.inCaves()){
    				boolean wasCaveMonster = false;
    				boolean lastWave = false;
    				if(npc.getNpcId() == 2745)
    					lastWave = true;
    				for (Npc playerCaveMonsters : player.getFightCaveMonsters()) {
    					if(npc == playerCaveMonsters){
    						player.removeFightCaveMonster(npc);
    						wasCaveMonster = true;
    						break;
    					}
    				}
    				if(wasCaveMonster){
    					if(player.getFightCaveMonsters().isEmpty()){
    						if(!lastWave)
    							player.getFightCavesMainData().startNextWave();
    						else
    							player.getFightCavesMainData().finishMinigame();
    					}
    				}
    			} else {
    				for (Npc playerCaveMonsters : player.getFightCaveMonsters()) {
    					if(npc == playerCaveMonsters){
    						System.out.println("FIGHT CAVE! Not in area?");
    						break;
    					}
    				}
    			}
    			//fight cave check
    			
    			//mage arena check
    			mageArenaNpcs(player, npc);
    		}
            if (!npc.needsRespawn()) {
            	if (npc.getNpcId() == 1160 && kalphite != null){
            		CycleEventHandler.getInstance().addEvent(kalphite, new CycleEvent() {
            			@Override
            			public void execute(CycleEventContainer container) {
            				endDeath(kalphite, killer, false);
            				container.stop();
            			}
            			@Override
            			public void stop() {
            			}
            		}, kalphite.getRespawnTimer());
            	}
                npc.setVisible(false);
                World.unregister(npc);
            } else {
                if (npc.isVisible()) {
                    npc.setVisible(false);
                	npc.setPosition(npc.getSpawnPosition().clone());
            		npc.getMovementHandler().reset();
            		npc.sendTransform(npc.getOriginalNpcId(), 0);
            		CombatManager.resetCombat(npc);
                    // Set respawn
            		if(npc.getNpcId() != 1158){
            			CycleEventHandler.getInstance().addEvent(npc, new CycleEvent() {
    						@Override
    						public void execute(CycleEventContainer container) {
            					endDeath(npc, killer, false);
            					container.stop();
    						}
    						@Override
    						public void stop() {
    						}
                    	}, npc.getRespawnTimer());
            		} else {
            			kalphite = npc;
            			NpcLoader.spawnNewNPC(1160, kalphite, 1181);
            		}
                    //died.setDeathTimer(npc.getRespawnTimer());
                    return;
                } else {
    				npc.setVisible(true);
    				if(npc.getDefinition().getName().toLowerCase().equals("fishing spot")){
            			Npc fish = FishingSpots.FISHING_SPOTS.get(npc.getPosition());
            			if(fish == null){
            				FishingSpots.FISHING_SPOTS.put(npc.getPosition(), npc);
            			}
            		}
    			}
            }
        }
        died.getUpdateFlags().faceEntity(-1);
        died.setDead(false);
        if(died.isNpc()){
        	if(((Npc) died).getPlayerOwner() != null)
        		died.setDead(true);
        }
		died.setCurrentHp(died.getMaxHp());
		died.getUpdateFlags().sendAnimation(65535, 0);
		died.removeAllEffects();
		if (killer != null && killer.isPlayer() && died.isPlayer()) {
			Player attacker = (Player) killer;
			Player victim = (Player) died;
			if(!died.inDuelArena())
				attacker.playerKillCount++;
			attacker.getActionSender().sendMessage("You have defeated " + Misc.formatPlayerName(victim.getUsername()) + ".");
		}
		if (died.isPlayer()) {
			Player player = (Player) died;
			player.setStopPacket(false);
			player.setHideWeapons(false);
			player.setAutoSpell(null);
			player.resetEffects();
			player.getSkill().refresh();
		}
        died.getHitRecordQueue().clear();
		if (died.isPlayer() && died.inDuelArena()) {
			((Player) died).getDuelMainData().handleDeath(false);
			return;
		}
		if (died.isPlayer() && died.inCaves()) {
			((Player) died).getFightCavesMainData().handleDeath(false);
			return;
		}
		if (died.isPlayer() && ((Player) died).getCreatureGraveyard().isInCreatureGraveyard()) {
			((Player) died).getCreatureGraveyard().handleDeath();
			return;
		}
		if (died.isPlayer()) {
			((Player) died).teleport(Teleportation.HOME);
            ((Player)died).getActionSender().sendMessage("Oh dear, you are dead!");
            ((Player)died).numberOfDeaths++;
            ((Player)died).getActionSender().sendQuickSong(203, 256);
            ((Player)died).getInCombatTick().setWaitDuration(0);
            ((Player)died).getInCombatTick().reset();
            ((Player)died).getPjTimer().setWaitDuration(0);
            ((Player)died).getPjTimer().reset();
        }

	}

	public static void init() {
		combatManager = new CombatManager();
	}

	public static CombatManager getManager() {
		return combatManager;
	}

	public void submit(Hit hit) {
		hitsStory.add(hit);
	}

	public static double calculateMaxHit(Player player, WeaponAttack weaponAttack) {
		AttackStyle attackStyle = weaponAttack.getAttackStyle();
        double damage = 0;
		if (attackStyle.getAttackType() == AttackType.MELEE)
			damage = calculateMaxMeleeHit(player, weaponAttack);
		else if (attackStyle.getAttackType() == AttackType.RANGED && weaponAttack.getRangedAmmo() != null)
			damage = calculateMaxRangedHit(player, weaponAttack);
		return damage;
	}

	public static double calculateMaxRangedHit(Player player, WeaponAttack weaponAttack) {
		int rangedLevel = player.getSkill().getLevel()[Skill.RANGED];
		double styleBonus = 0;
		AttackStyle attackStyle = weaponAttack.getAttackStyle();
		if (attackStyle.getMode() == AttackStyle.Mode.RANGED_ACCURATE)
			styleBonus = 3;
		else if (attackStyle.getMode() == AttackStyle.Mode.LONGRANGE)
			styleBonus = 1;
		rangedLevel += styleBonus;
		double rangedStrength = weaponAttack.getRangedAmmo().getRangeStrength();
		double maxHit = (rangedLevel + rangedStrength / 8 + rangedLevel * rangedStrength * Math.pow(64, -1) + 14) / 10;
		return (int) Math.floor(maxHit);
	}

	public static double calculateMaxMeleeHit(Player player, WeaponAttack weaponAttack) {
		double strengthLevel = player.getSkill().getLevel()[Skill.STRENGTH];
		if (player.getIsUsingPrayer()[Prayer.BURST_OF_STRENGTH])
			strengthLevel *= 1.05;
		else if (player.getIsUsingPrayer()[Prayer.SUPERHUMAN_STRENGTH])
			strengthLevel *= 1.1;
		else if (player.getIsUsingPrayer()[Prayer.ULTIMATE_STRENGTH])
			strengthLevel *= 1.15;
		AttackStyle attackStyle = weaponAttack.getAttackStyle();
		int styleBonus = 0;
		if (attackStyle.getMode() == AttackStyle.Mode.AGGRESSIVE)
			styleBonus = 3;
		else if (attackStyle.getMode() == AttackStyle.Mode.CONTROLLED)
			styleBonus = 1;
		int effectiveStrengthDamage = (int) (strengthLevel + styleBonus);
		double baseDamage = 5 + (effectiveStrengthDamage + 8) * (player.getBonus(10) + 64) / 64; //10 = str bonus
		int maxHit = (int) Math.floor(baseDamage);
		return (int) Math.floor(maxHit / 10);
	}

	public static double getChance(double attack, double defence) {
		double A = Math.floor(attack);
		double D = Math.floor(defence);
		double chance = A < D ? (A - 1.0) / (2.0 * D) : 1.0 - (D + 1.0) / (2.0 * A);
		chance = chance > 0.9999 ? 0.9999 : chance < 0.0001 ? 0.0001 : chance;
		return chance;
	}

	public static final Random r = new Random(System.currentTimeMillis());

	public static boolean isAccurateHit(double chance) {
		return r.nextDouble() <= chance;
	}

	private static double getEffectiveAccuracy(Entity attacker, AttackStyle attackStyle) {
		double attackBonus = attacker.getBonus(attackStyle.getBonus().toInteger());
		double baseAttack = attacker.getBaseAttackLevel(attackStyle.getAttackType());
		if(attackBonus < 0){//increase the effect of negative bonus
			baseAttack /= 2.0;
			attackBonus *= 3.0;
		}
		/*if(attacker.isPlayer()){
			Player player = (Player) attacker;
			if(player.getHost().equals("127.0.0.1"))
				System.out.println("Atk start: "+attackBonus+" "+baseAttack);
		}*/
		if (attackStyle.getAttackType() == AttackType.MELEE && attacker.isPlayer()) {
			Player player = (Player) attacker;
			if (player.getIsUsingPrayer()[Prayer.CLARITY_OF_THOUGHT])
				baseAttack *= 1.05;
			else if (player.getIsUsingPrayer()[Prayer.IMPROVED_REFLEXES])
				baseAttack *= 1.1;
			else if (player.getIsUsingPrayer()[Prayer.INCREDIBLE_REFLEXES])
				baseAttack *= 1.15;
		}
		/*if(attacker.isPlayer()){
			Player player = (Player) attacker;
			if(player.getHost().equals("127.0.0.1"))
				System.out.println("Atk end: "+(Math.floor(baseAttack + attackBonus) + 8));
		}*/
		return Math.floor(baseAttack + attackBonus) + 8;
	}

	private static double getEffectiveDefence(Entity victim, AttackStyle attackStyle) {
		double defenceBonus = victim.getBonus(attackStyle.getBonus().toInteger() + AttackStyle.Bonus.values().length);
		double baseDefence = victim.getBaseDefenceLevel(attackStyle.getAttackType());
		if(defenceBonus < 0){//increase the effect of negative bonus
			baseDefence /= 2.0;
			defenceBonus *= 3.0;
		}
		/*if(victim.isPlayer()){
			Player player = (Player) victim;
			if(player.getHost().equals("127.0.0.1")){
				System.out.println("Def start: "+baseDefence+" "+defenceBonus);
			}
		}*/
		if (attackStyle.getAttackType() == AttackType.MELEE && victim.isPlayer()) {
			Player player = (Player) victim;
			if (player.getIsUsingPrayer()[Prayer.THICK_SKIN])
				baseDefence *= 1.05;
			else if (player.getIsUsingPrayer()[Prayer.ROCK_SKIN])
				baseDefence *= 1.1;
			else if (player.getIsUsingPrayer()[Prayer.STEEL_SKIN])
				baseDefence *= 1.15;
		}
		/*if(victim.isPlayer()){
			Player player = (Player) victim;
			if(player.getHost().equals("127.0.0.1")){
				System.out.println("Def end: "+(Math.floor(baseDefence + defenceBonus) + 8));
			}
		}*/
		return Math.floor(baseDefence + defenceBonus) + 8;
	}

	public static double getDefenceRoll(Entity victim, HitDef hitDef) {
		AttackStyle attackStyle = hitDef.getAttackStyle();
        //if (victim.isNpc())
        //    return victim.getBonus(attackStyle.getBonus().toInteger() + AttackStyle.Bonus.values().length);
		double effectiveDefence = getEffectiveDefence(victim, attackStyle);
		//effectiveDefence += victim.getBonus(attackStyle.getBonus().toInteger() + AttackStyle.Bonus.values().length);
		int styleBonusDefence = 0;
		if (victim.isPlayer()) {
			Player pVictim = ((Player) victim);
			if (attackStyle.getAttackType() == AttackType.MAGIC) {
				int level = pVictim.getSkill().getLevel()[Skill.MAGIC];
				effectiveDefence = (int) (Math.floor(level * 0.7) + Math.floor(effectiveDefence * 0.3));
			} else {
				AttackStyle defenceStyle = pVictim.getEquippedWeapon().getWeaponInterface().getAttackStyles()[pVictim.getFightMode()];
				if (defenceStyle.getMode() == AttackStyle.Mode.DEFENSIVE || defenceStyle.getMode() == AttackStyle.Mode.LONGRANGE)
					styleBonusDefence += 3;
				else if (defenceStyle.getMode() == AttackStyle.Mode.CONTROLLED)
					styleBonusDefence += 1;
			}
		}
		effectiveDefence *= (1 + (styleBonusDefence) / 64);
		if (hitDef.getSpecialEffect() == 11) { //verac effect
			effectiveDefence *= 0.75;
		}
		if(victim.isNpc()){
			if (attackStyle.getAttackType() == AttackType.MAGIC) {//reducing magic defence, so spells wouldn't splash so often...
				effectiveDefence *= 0.7;
			}
			if (attackStyle.getAttackType() == AttackType.MELEE) {//reducing melee def, so u can actually hit high lvl monsters...
				effectiveDefence *= 0.6;
			}
		}
		return effectiveDefence;
	}

	public static double getAttackRoll(Entity attacker, HitDef hitDef) {
		AttackStyle attackStyle = hitDef.getAttackStyle();
        //if (attacker.isNpc())
        //    return attacker.getBonus(attackStyle.getBonus().toInteger());*/

		double specAccuracy = hitDef.getSpecAccuracy();
		double effectiveAccuracy = getEffectiveAccuracy(attacker, attackStyle);

		int styleBonusAttack = 0;
		if (attackStyle.getMode() == AttackStyle.Mode.MELEE_ACCURATE || attackStyle.getMode() == AttackStyle.Mode.RANGED_ACCURATE)
			styleBonusAttack = 3;
		else if (attackStyle.getMode() == AttackStyle.Mode.CONTROLLED)
			styleBonusAttack = 1;
		effectiveAccuracy *= (1 + (styleBonusAttack) / 64);
		/*if(attacker != null)
		if(attacker.isPlayer()){
			Player player = (Player) attacker;
		}*/
		return (int) (effectiveAccuracy * specAccuracy);
	}

	/**
	 * Resets anything needed after the end of combat.
	 */
	public static void resetCombat(Entity entity) {
		if (entity.isPlayer()) {
			((Player) entity).setCastedSpell(null);
		}
		entity.setCombatingEntity(null);
		entity.setInstigatingAttack(false);
		entity.setSkilling(null);
		entity.getUpdateFlags().faceEntity(-1);
		Following.resetFollow(entity);
	}

}