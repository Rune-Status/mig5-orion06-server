package com.rs2.model.content.combat.hit;

import java.util.ArrayList;
import java.util.List;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.Graphic;
import com.rs2.model.Position;
import com.rs2.model.UpdateFlags;
import com.rs2.model.content.combat.AttackType;
import com.rs2.model.content.combat.CombatCycleEvent;
import com.rs2.model.content.combat.CombatCycleEvent.CanAttackResponse;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.effect.Effect;
import com.rs2.model.content.combat.effect.impl.BindingEffect;
import com.rs2.model.content.combat.effect.impl.StatEffect;
import com.rs2.model.content.combat.effect.impl.StunEffect;
import com.rs2.model.content.combat.effect.impl.TeleportEffect;
import com.rs2.model.content.combat.effect.impl.UnequipEffect;
import com.rs2.model.content.combat.projectile.Projectile;
import com.rs2.model.content.combat.special.SpecialType;
import com.rs2.model.content.combat.util.RingEffect;
import com.rs2.model.content.combat.util.WeaponDegrading;
import com.rs2.model.content.combat.weapon.AttackStyle;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.content.skills.prayer.Prayer;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Area;
import com.rs2.util.Misc;

/**
 *
 */
public class Hit {

	private HitDef hitDef;
	private int hitDelay, damage;
	private Entity attacker, victim;
	private boolean isDelayedDamaged, hit;
	private Entity targetAttacker;
	private Entity previousVictim;
	
	private ArrayList<Entity> targets = new ArrayList<Entity>();
	
	public Hit(Entity attacker, Entity victim, HitDef hitDef) {
		this.attacker = attacker;
		this.victim = victim;
		this.hitDef = hitDef;
		this.damage = hitDef.getDamage();
	}
	
	public Hit(Entity attacker, Entity victim, HitDef hitDef, Entity previousVictim) {
		this.attacker = attacker;
		this.victim = victim;
		this.hitDef = hitDef;
		this.damage = hitDef.getDamage();
		this.previousVictim = previousVictim;
	}

	public void addTarget(Entity target) {
		targets.add(target);
	}
	
	public void setTargetAttacker(Entity attacker){
		this.targetAttacker = attacker;
	}
	
	public Entity getTargetAttacker(){
		return targetAttacker;
	}
	
	public void setTargets(ArrayList<Entity> targets) {
		this.targets = targets;
	}

	public void tick() {
		hitDelay--;
	}

    public void initialize() {
        initialize(true);
    }
    
    //Area mageArena = new Area(3087, 3919, 3122, 3947, (byte)0);

	public void initialize(boolean queue) {
		if (hitDef.shouldRandomizeDamage())
			this.damage = Misc.random(damage);
		if (attacker != null && !hitDef.isUnblockable() && hitDef.shouldCheckAccuracy()) {
			executeAccuracy();
		}
		if (hitDef.getProjectileDef() != null){
			if(previousVictim != null)
				new Projectile(previousVictim, victim, hitDef.getProjectileDef()).show();
			else
				new Projectile(attacker, victim, hitDef.getProjectileDef()).show();
		}
		hitDelay = hitDef.calculateHitDelay(attacker != null ? attacker.getPosition() : null, victim.getPosition());
        if (queue)
            CombatManager.getManager().submit(this);
        
        /*if(attacker.isNpc())
			System.out.println("now2 "+this.damage+" "+hit);*/
        if(victim != null && attacker != null)
        if((victim.isNpc() && attacker.isPlayer()) || (victim.isPlayer() && attacker.isNpc())){
        	int d = -1;
        	if(attacker.isPlayer())
        		d = ((Player) attacker).getQuestHandler().controlCombatDamage(attacker, victim);
        	else
        		d = ((Player) victim).getQuestHandler().controlCombatDamage(attacker, victim);
        	if(attacker.isInMageArena())
        		if(hitDef.getAttackStyle().getAttackType() != AttackType.MAGIC)
        			d = 0;
        	if(d != -1)
        		this.damage = d;
        }
        if(hitDef.getSpell() != null){
        	if(hitDef.getSpell() == Spell.FLAMES_OF_ZAMORAK || hitDef.getSpell() == Spell.SARADOMIN_STRIKE || hitDef.getSpell() == Spell.CLAWS_OF_GUTHIX){
        		int sound = 0;
        		if(hitDef.getSpell() == Spell.FLAMES_OF_ZAMORAK)
        			sound = hit ? 290 : 293;
        		if(hitDef.getSpell() == Spell.SARADOMIN_STRIKE)
        			sound = hit ? 297 : 299;
        		if(hitDef.getSpell() == Spell.CLAWS_OF_GUTHIX)
        			sound = hit ? 291 : 296;
        		if(attacker.isPlayer()){
        			Player player = (Player) getAttacker();
        			if(hitDef.getSpell() == Spell.SARADOMIN_STRIKE){
						if(player.saradominStrikeCasts > 0){
							player.saradominStrikeCasts--;
							if(player.saradominStrikeCasts == 0)
								player.getActionSender().sendMessage("You can now cast Saradomin Strike outside the Arena.");
						}
					}
					if(hitDef.getSpell() == Spell.FLAMES_OF_ZAMORAK){
						if(player.flameOfZamorakCasts > 0){
							player.flameOfZamorakCasts--;
							if(player.flameOfZamorakCasts == 0)
								player.getActionSender().sendMessage("You can now cast Flames of Zamorak outside the Arena.");
						}
					}
					if(hitDef.getSpell() == Spell.CLAWS_OF_GUTHIX){
						if(player.clawsOfGuthixCasts > 0){
							player.clawsOfGuthixCasts--;
							if(player.clawsOfGuthixCasts == 0)
								player.getActionSender().sendMessage("You can now cast Claws of Guthix outside the Arena.");
						}
					}
        			player.getActionSender().sendSound(sound, 1, 0);
        		}
        		if(victim.isPlayer()){
        			Player player2 = (Player) getVictim();
        			player2.getActionSender().sendSound(sound, 1, 0);
        		}
        	}
        }
        
		if (hit && victim.isPlayer()) {
			RingEffect.ringOfRecoil(attacker, (Player) victim, damage);
		}
		
		if(attacker != null)
		if(attacker.isPlayer()){
			WeaponDegrading.degradeEquipment((Player) attacker); 
		}
	}

	public void display() {
		if (!canDamageEnemy() || damage < 0)
			return;
		UpdateFlags flags = victim.getUpdateFlags();
		HitType hitType = damage == 0 ? HitType.MISS : hitDef.getHitType();
		if (!flags.isDamage1Set()) {
			flags.setHitUpdate(true);
			flags.setDamage(damage);
			flags.setHitType(hitType.toInteger());
			
			if(victim.isPlayer()){
				Player player = (Player) getVictim();
				if(damage > 0)
					player.getActionSender().sendSound(69, 1, 0);
				else
					player.getActionSender().sendSound(player.getBlockSound(), 1, 0);
				if(hitDef.getAttackStyle() != null){
					if(hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC){
						player.getActionSender().sendSound(hitDef.getHitSound(), 1, 0);
					}
				}
				if(attacker != null){
					if(attacker.isPlayer()){
						Player player2 = (Player) getAttacker();
						player2.getActionSender().sendSound(player.getBlockSound(), 1, 0);
						if(hitDef.getAttackStyle() != null){
							if(hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC){
								player2.getActionSender().sendSound(hitDef.getHitSound(), 1, 0);
							}
						}
					}
				}
			}
			if(victim != null && attacker != null)
			if(victim.isNpc() && attacker.isPlayer()){
				Player player = (Player) getAttacker();
				Npc npc = (Npc) getVictim();
				player.getActionSender().sendSound(npc.getBlockSound(), 1, 0);
				if(hitDef.getAttackStyle() != null){
					if(hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC){
						player.getActionSender().sendSound(hitDef.getHitSound(), 1, 0);
						if(npc.getNpcId() == 667){//chronozon
							if(hitDef.getSpell() != null && damage > 0){
								if(hitDef.getSpell() == Spell.AIR_BLAST){
									npc.hitByAirBlast = true;
									player.getActionSender().sendMessage("Chronozon weakens...");
								}
								if(hitDef.getSpell() == Spell.WATER_BLAST){
									npc.hitByWaterBlast = true;
									player.getActionSender().sendMessage("Chronozon weakens...");
								}
								if(hitDef.getSpell() == Spell.EARTH_BLAST){
									npc.hitByEarthBlast = true;
									player.getActionSender().sendMessage("Chronozon weakens...");
								}
								if(hitDef.getSpell() == Spell.FIRE_BLAST){
									npc.hitByFireBlast = true;
									player.getActionSender().sendMessage("Chronozon weakens...");
								}
							}
						}
						if(damage > 0 && player.questStage[0] == 66){
							player.setNextTutorialStage();
						}
					}
				}
			}
			
		} else if (!flags.isDamage2Set()) {
			flags.setHitUpdate2(true);
			flags.setDamage2(damage);
			flags.setHitType2(hitType.toInteger());
		} else flags.queueDamage(damage, hitType.toInteger());
    }

    @SuppressWarnings("rawtypes")
    public void execute(List<Hit> recoilList) {
        if (hitDef.getDropItem() != null && attacker != null && attacker.isPlayer()) {
            Player player = (Player) attacker;
            if (player.isDropArrow() && Misc.getRandom().nextInt(10) < 6) {
            	/*if (player.inDuelArena()) {
            		player.getActionSender().sendMessage(""+hitDef.getDropItem().getId());
            		player.getDuelMainData().getAmmoUsed().add(new Item(hitDef.getDropItem().getId(), hitDef.getDropItem().getCount()));
            	} else {*/
                    GroundItem dropItem = new GroundItem(new Item(hitDef.getDropItem().getId(), hitDef.getDropItem().getCount()), player, victim.getPosition().clone());
                    GroundItemManager.getManager().dropItem(dropItem);
            	//}
            }
        }

        if (!canDamageEnemy())
            return;

        if(!this.targets.isEmpty()){
			Entity target = targets.get(0);
			if(target.isPlayer()){
				Player newPlayer = (Player) target;
				Player player = (Player) getVictim();
				//System.out.println("Next target: "+newPlayer.getUsername());
				CombatCycleEvent.CanAttackResponse canAttackResponse = CombatCycleEvent.canAttack(getAttacker(), newPlayer);
				Hit hit = new Hit(getTargetAttacker(), newPlayer, hitDef, player);
				targets.remove(0);
				hit.setTargetAttacker(getAttacker());
				hit.setTargets(targets);
				if (canAttackResponse == CanAttackResponse.SUCCESS)
					hit.initialize();
			}
		}
        /*if (hitDef.getEffects().size() > 0) {
              if (damage > 0 || (hitDef.getAttackStyle() != null && hitDef.getAttackStyle().getAttackType() == AttackType.MELEE)) {
                  for (Effect effect : hitDef.getEffects())
                      effect.execute(this);
              }
          } */
        // retaliate
        if (attacker != null && !isDelayedDamaged && !hitDef.isUnblockable() && hitDef.getHitType() != HitType.POISON && hitDef.getHitType() != HitType.BURN) {
            if (victim.isNpc() && !victim.isDontAttack()) {
            	CombatManager.attack(victim, attacker);
            } else if (victim.isPlayer() && !victim.isMoving()) {
                Player player = (Player) victim;
                boolean alreadyFighting = false;
                if(player.getCombatingEntity() != null)
            		if(!player.getCombatingEntity().isDead())
            			alreadyFighting = true;
                if (player.shouldAutoRetaliate() && !alreadyFighting) {
                    CombatManager.attack(victim, attacker);
                }
            }
        }
        if(hitDef.getSpell() != null)
    		if(hitDef.getSpell() == Spell.MELZAR){
    			int x = 2927+Misc.random_(3);
    			int y = 9648+Misc.random_(3);
    			GroundItemManager.getManager().dropItem(new GroundItem(new Item(1965, 1), new Position(x,y,0), false));
    			if(victim.isPlayer()){
    				Player player = (Player) victim;
    				player.getActionSender().createStillGfx(86, x, y, 0, 0);
    			}
    			return;
    		}
        if (hitDef.doBlockAnim() && !victim.getUpdateFlags().isAnimationUpdateRequired()) {
        	if (hitDef.getVictimAnimation() > 0) {
        		 victim.getUpdateFlags().sendAnimation(hitDef.getVictimAnimation());
        	} else if (victim.isPlayer()) {
    			Player player = (Player) victim;
    			if (player.transformNpc > 1) {
    	            victim.getUpdateFlags().sendAnimation(new Npc(player.transformNpc).getDefinition().getBlockAnim());
    			} else {
    	            victim.getUpdateFlags().sendAnimation(victim.getBlockAnimation());
    			}
    		} else {
                victim.getUpdateFlags().sendAnimation(victim.getBlockAnimation());
    		}
        }
        if (victim.isPlayer()) {
            Player player = (Player) victim;
            player.getActionSender().removeInterfaces();
        }
        if (hitDef.getHitGraphic() != null)
            victim.getUpdateFlags().sendGraphic(hitDef.getHitGraphic());
        if (attacker != null && attacker.isPlayer() && victim.isNpc()){
            Player player = (Player) attacker;
            Npc npc = (Npc) victim;
            if(npc.getNpcId() >= 1024 && npc.getNpcId() <= 1029){
           	 if(!(hitDef.getAttackStyle().getAttackType() == AttackType.MELEE && player.getEquipment().getId(Constants.WEAPON) == 2952)){//check wolfbane
           		 npc.getUpdateFlags().sendGraphic(86, 25);
           		 npc.sendTransform(npc.getNpcId()+6, 100000);
           		 player.getActionSender().sendSound(267, 1, 0);
           	 }
            }
       }
        if(hitDef.getSpell() != null){
    		if(hitDef.getSpell() == Spell.CHAOSELE_TELEPORT || hitDef.getSpell() == Spell.CHAOSELE_DISARM){
    			/*if(hitDef.getSpell() == Spell.CHAOSELE_TELEPORT){
    				Effect effect = new TeleportEffect();
    				if(effect != null)
    					hitDef.addEffect(effect);
    			}
    			if(hitDef.getSpell() == Spell.CHAOSELE_DISARM){
    				Effect effect = new UnequipEffect();
    				if(effect != null)
    					hitDef.addEffect(effect);
    			}*/
    			if(hitDef.getSpell().getOnHitEffect() != null){
    				hitDef.addEffect(hitDef.getSpell().getOnHitEffect());
    			}
    			if (hitDef.getEffects() != null && hitDef.getEffects().size() > 0) {
                    for (Effect effect : hitDef.getEffects()) {
                        if (victim.canAddEffect(effect)) {
                            effect.initialize(this);
                        }
                    }
                }
    			if (hitDef.getEffects() != null && hitDef.getEffects().size() > 0) {
    	            for (Effect effect : hitDef.getEffects()) {
    	            	if (effect != null) {
    	                    effect.execute(this);
    	            	}
    	            }
    	        }
    			return;
    		}
        }
        if (!hit && !hitDef.isUnblockable()) {
        	if (damage > 0) {
            	damage = 0;
        	}
        	if (hitDef.getAttackStyle() != null && hitDef.getAttackStyle().getAttackType() != AttackType.MAGIC) {
                display();
        	} else if(hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC){//splash sound
        		if(hitDef.getSpell() != null)
        		if(hitDef.getSpell() == Spell.FLAMES_OF_ZAMORAK || hitDef.getSpell() == Spell.SARADOMIN_STRIKE || hitDef.getSpell() == Spell.CLAWS_OF_GUTHIX)
        			return;
        		if(attacker.isPlayer()){
        			Player player = (Player) getAttacker();
        			player.getActionSender().sendSound(940, 1, 0);
        		}
        		if(victim.isPlayer()){
    				Player player2 = (Player) getVictim();
    				player2.getActionSender().sendSound(940, 1, 0);
    			}
        	}
            return;
        }
        if (attacker != null && attacker.isPlayer() && victim.isNpc()){
             Player player = (Player) attacker;
             Npc npc = (Npc) victim;
             if (player.getSlayer().needToFinishOffMonster(((Npc)victim), true)) {
            	 if (damage >= victim.getCurrentHp()) {
            		 damage = victim.getCurrentHp() - 1;
            	 }
             }
        }
        if (!isDelayedDamaged) {
            if (hitDef.getAttackStyle() != null) {
            	if (hitDef.getAttackStyle().getMode() == AttackStyle.Mode.ICY_BREATH && victim.isPlayer()) {
            		Player player = (Player) victim;
            		if(player.antiIcyBreath()){
            			player.getActionSender().sendMessage("Your shield absorbs most of the wyvern's icy breath!");
						damage = Misc.random(10);
            		} else {
            			Effect effect = new BindingEffect(8);
            			if (victim.canAddEffect(effect)) {
            				player.getActionSender().sendMessage("You've been frozen!");
            				effect.initialize(this);
                        }
            		}
            	}
            	if (hitDef.getAttackStyle().getMode() == AttackStyle.Mode.DRAGONFIRE || hitDef.getAttackStyle().getMode() == AttackStyle.Mode.DRAGONFIRE_FAR && victim.isPlayer()) {
            		Player player = (Player) victim;
					switch (player.antiFire()) {
						case 2 :
							damage = 0;
							break;
						case 1 :
							player.getActionSender().sendMessage("You manage to resist some of the dragonfire.");
							damage = Misc.random(hitDef.getAttackStyle().getMode() == AttackStyle.Mode.DRAGONFIRE_FAR ? 8 : 4);
							break;
						default :
							player.getActionSender().sendMessage("You are horribly burned by the dragonfire!");
							damage = 30 + Misc.random(30);
							break;
					}
					if (damage > 10 && hitDef.getAttackStyle().getMode() == AttackStyle.Mode.DRAGONFIRE && victim.isProtectingFromCombat(hitDef.getAttackStyle().getAttackType(), attacker)) {
            			player.getActionSender().sendMessage("Your prayers manage to resist some of the dragonfire.");
            			damage = Misc.random(10);
            		}
            	} else if (victim.isProtectingFromCombat(hitDef.getAttackStyle().getAttackType(), attacker) && hitDef.getSpecialEffect() != 11) {
                    if (attacker != null && attacker.isPlayer() && victim.isPlayer())
                        damage = (int) Math.ceil(damage * .6);
                    else
                        damage = 0;
                }
            }
        }
        SpecialType.doSpecEffect(attacker, victim, hitDef, damage);

        int currentHp = victim.getCurrentHp();

        if (damage > currentHp)
            damage = currentHp;

        if (hitDef.getEffects() != null && hitDef.getEffects().size() > 0) {
            for (Effect effect : hitDef.getEffects()) {
            	if (effect != null) {
                    effect.execute(this);
            	}
            }
        }

        //if (hitDef.getDamageDelay() == 0) {
        if (attacker != null && hitDef != null && attacker.isPlayer()) {
            addCombatExp((Player) attacker, damage, hitDef.getAttackStyle());
        }
        currentHp -= damage;
        if (hitDef.getHitAnimation() != -1)
            victim.getUpdateFlags().sendAnimation(hitDef.getHitAnimation());
        if (attacker != null) {
            HitRecord hitRecord = victim.getHitRecord(attacker);
            if (hitRecord == null)
                hitRecord = new HitRecord(attacker);
            else
                victim.getHitRecordQueue().remove(hitRecord);
            hitRecord.addDamage(damage);
            victim.getHitRecordQueue().add(hitRecord);
            if (attacker.isPlayer()) {
                Player player = (Player) attacker;
                if (player.getIsUsingPrayer()[Prayer.SMITE] && victim.isPlayer()) {
                    Prayer.applySmite(player, ((Player) victim), damage);
                }
            }
        }
        victim.setCurrentHp(currentHp);
        if (victim.isPlayer()) {
            Player player = (Player) victim;
            if (currentHp > 0) {
                int saveHp = (int) Math.ceil(victim.getMaxHp() * .1);
                if (currentHp < saveHp) {
                    if (player.getIsUsingPrayer()[Prayer.REDEMPTION]) {
                        Prayer.applyRedemption(player, victim, currentHp);
                    }
                    RingEffect.ringOfLife(player);
                }
            }
        }
        if (hitDef.getSpecialEffect() != 5) { // d spear
            display();
        }
        /*} else {
              Hit hit = new Hit(attacker, victim, new HitDef(hitDef.getAttackStyle(), hitDef.getHitType(), damage).setStartingHitDelay(hitDef.getDamageDelay() - 1));
              hit.isDelayedDamaged = true;
              hit.initialize();
          } */
    }

    public void addCombatExp(Player player, int damage, AttackStyle attackStyle) {
        if (attackStyle == null) {
            return;
        }
        final double totalExp = damage * 4d;
        final double expPerSkill = totalExp / attackStyle.getMode().getSkillsTrained().length;
        for (int i : attackStyle.getMode().getSkillsTrained())
            player.getSkill().addExp(i, expPerSkill);
        player.getSkill().addExp(Skill.HITPOINTS, (totalExp / 3d));
    }

    public boolean shouldExecute() {
        return hitDelay <= 0;
    }

    public boolean canDamageEnemy() {
        return (Boolean) victim.getAttributes().get("canTakeDamage");
    }

    public Entity getVictim() {
        return victim;
    }

    public Entity getAttacker() {
        return attacker;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @SuppressWarnings("rawtypes")
    private void executeAccuracy() {
    	hit = true;
        if (attacker != null && Misc.random(3) == 0) { //barrows effect, was at 4
        	if (((attacker.isPlayer() && ((Player) attacker).hasFullGuthan()) || (attacker.isNpc() && ((Npc) attacker).getDefinition().getId() == 2027))) {
	    		hitDef.setSpecialEffect(7);
        	} else if (((attacker.isPlayer() && ((Player) attacker).hasFullTorag()) || (attacker.isNpc() && ((Npc) attacker).getDefinition().getId() == 2029))) {
        		hitDef.setSpecialEffect(8);
        	} else if (((attacker.isPlayer() && ((Player) attacker).hasFullAhrim()) || (attacker.isNpc() && ((Npc) attacker).getDefinition().getId() == 2025))) {
        		hitDef.setSpecialEffect(9);
        	} else if (((attacker.isPlayer() && ((Player) attacker).hasFullKaril()) || (attacker.isNpc() && ((Npc) attacker).getDefinition().getId() == 2028))) {
        		hitDef.setSpecialEffect(10);
        	} else if (((attacker.isPlayer() && ((Player) attacker).hasFullVerac()) || (attacker.isNpc() && ((Npc) attacker).getDefinition().getId() == 2030))) {
        		hitDef.setSpecialEffect(11);
        	} else if (((attacker.isPlayer() && ((Player) attacker).hasFullDharok()) || (attacker.isNpc() && ((Npc) attacker).getDefinition().getId() == 2026))) {
        		double hpLost = attacker.getMaxHp() - attacker.getCurrentHp();
        		this.damage += damage * hpLost * 0.01;
        		if(attacker.isNpc()){
        			if(this.damage >= 60)
        				this.damage = 60;
        		}
        	}
        }
        if (getAttacker().isNpc() && getVictim().isPlayer()) { // slayer npc effects
            if (!((Player) getVictim()).getSlayer().hasSlayerRequirement((Npc) getAttacker())) {
                String name = ((Npc) getAttacker()).getDefinition().getName().toLowerCase();
                if (name.equalsIgnoreCase("banshee")) {
                	damage = 8;
                	hitDef.addEffects(new Effect[]{new StatEffect(Skill.ATTACK, 1), new StatEffect(Skill.STRENGTH, 1), new StatEffect(Skill.DEFENCE, 1), new StatEffect(Skill.RANGED, 1), new StatEffect(Skill.MAGIC, 1)});
                } else if (name.equalsIgnoreCase("cockatrice")) {
                	damage = 11;
                	hitDef.addEffects(new Effect[]{new StatEffect(Skill.ATTACK, 3), new StatEffect(Skill.STRENGTH, 3), new StatEffect(Skill.DEFENCE, 3), new StatEffect(Skill.RANGED, 3), new StatEffect(Skill.MAGIC, 3), new StatEffect(Skill.AGILITY, 3)});
                } else if (name.equalsIgnoreCase("basilisk")) {
                	damage = 12;
                	hitDef.addEffects(new Effect[]{new StatEffect(Skill.ATTACK, 3), new StatEffect(Skill.STRENGTH, 3), new StatEffect(Skill.DEFENCE, 3), new StatEffect(Skill.RANGED, 3), new StatEffect(Skill.MAGIC, 3)});
                } else if (name.equalsIgnoreCase("wall beast")) {
                	damage = 18;
                	hitDef.addEffect(new StunEffect(5)).setVictimAnimation(734);
                } else if (name.equalsIgnoreCase("aberrant specter")) {
                	damage = 14;
                	hitDef.addEffects(new Effect[]{new StatEffect(Skill.ATTACK, 5), new StatEffect(Skill.STRENGTH, 5), new StatEffect(Skill.DEFENCE, 5), new StatEffect(Skill.RANGED, 5), new StatEffect(Skill.MAGIC, 5)});
                } else if (name.equalsIgnoreCase("dust devil")) {
                	damage = 14;
                }
            }
        }
        if (getVictim().isPlayer() && ((Player) getVictim()).questStage[0] != 1) {
            if (((Player) getVictim()).getSkill().getLevel()[Skill.HITPOINTS] == 1)
            	hit = false;
        }
        if (getAttacker().isPlayer() && getVictim().isNpc()) {
            if (!((Player) getAttacker()).getSlayer().hasSlayerRequirement((Npc) getVictim())) {
            	hit = false;
            }
        }
        if ((attacker.isPlayer() && ((Player) attacker).questStage[0] == 65)) {
        	((Player) attacker).setNextTutorialStage();
            hit = false;
        }
        if (hit) {
            double defence = CombatManager.getDefenceRoll(victim, hitDef);
			double accuracy = CombatManager.getAttackRoll(attacker, hitDef);
            double chance = CombatManager.getChance(accuracy, defence);
            boolean accurate = CombatManager.isAccurateHit(chance);
            if (getAttacker().isPlayer() && ((Player) getAttacker()).isDebugCombat()) {
            	((Player) getAttacker()).getActionSender().sendMessage("Chance to hit: "+(int) (chance * 100)+"% (Rounded) damage: "+damage);
            }
            if (getVictim().isPlayer() && ((Player) getVictim()).isDebugCombat() && getAttacker().isNpc()) {
            	((Player) getVictim()).getActionSender().sendMessage("Chance of npc hitting u: "+(int) (chance * 100)+"% (Rounded) damage: "+damage);
            }
            hit = accurate;
        }
        if (!hit && !hitDef.isUnblockable()) {
            if (hitDef.getAttackStyle() != null && hitDef.getAttackStyle().getAttackType() == AttackType.MAGIC) {
                hitDef.setHitGraphic(new Graphic(85, 100));
            }
            hitDef.removeEffects();
        } else {
            if (hitDef.getEffects() != null && hitDef.getEffects().size() > 0) {
                for (Effect effect : hitDef.getEffects()) {
                    if (victim.canAddEffect(effect)) {
                        effect.initialize(this);
                    }
                }
            }
        }
    }

}
