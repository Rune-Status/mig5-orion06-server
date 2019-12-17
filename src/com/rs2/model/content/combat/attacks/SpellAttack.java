package com.rs2.model.content.combat.attacks;

import java.util.ArrayList;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.World;
import com.rs2.model.content.combat.AttackType;
import com.rs2.model.content.combat.AttackUsableResponse;
import com.rs2.model.content.combat.CombatCycleEvent;
import com.rs2.model.content.combat.CombatCycleEvent.CanAttackResponse;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.combat.effect.Effect;
import com.rs2.model.content.combat.hit.Hit;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.minigames.duelarena.RulesData;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.util.Misc;
import com.rs2.util.requirement.EquipmentRequirement;
import com.rs2.util.requirement.Requirement;
import com.rs2.util.requirement.RuneRequirement;
import com.rs2.util.requirement.SkillLevelRequirement;

/**
 *
 */
public class SpellAttack extends BasicAttack {
	private Spell spell;
	public static final String FAILED_REQUIRED_RUNES = "You do not have the runes required!";
	public static final String FAILED_LEVEL_REQUIREMENT = "Your level in magic is not high enough!";

	public SpellAttack(Entity attacker, Entity victim, Spell spell) {
		super(attacker, victim);
		this.spell = spell;
	}

	@Override
	public final AttackUsableResponse.Type isUsable() {
		if (!spell.isCombatSpell() || spell.getHitDef() == null) {
			return AttackUsableResponse.Type.FAIL;
		}
		if (getAttacker().isPlayer()) {
			if (RulesData.NO_MAGIC.activated((Player) getAttacker())) {
				if (getAttacker().isPlayer()) {
					((Player) getAttacker()).getActionSender().sendMessage("Magic attacks have been disabled during this fight!");
					((Player) getAttacker()).setAutoSpell(null);
					CombatManager.resetCombat(getAttacker());
				}
				return AttackUsableResponse.Type.FAIL;
			}
		}
		if (spell.equals(Spell.CRUMBLE_UNDEAD)) {
			if (!Npc.isUndeadNpc(getVictim())) {
				((Player) getAttacker()).getActionSender().sendMessage("You can only cast this spell on undead npc.");
				CombatManager.resetCombat(getAttacker());
				return AttackUsableResponse.Type.FAIL;
			}
		}
		if (spell.getRequiredEffect() != null) {
			if (!getVictim().canAddEffect(spell.getRequiredEffect())) {
				if (getAttacker().isPlayer()) {
					((Player) getAttacker()).getActionSender().sendMessage("The target is immune to that spell right now!");
					CombatManager.resetCombat(getAttacker());
				}
				return AttackUsableResponse.Type.FAIL;
			}
		}
		return super.isUsable();
	}

	@Override
	public AttackType getAtkType() {
		if (!spell.isCombatSpell() || spell.getHitDef() == null)
			return null;
		return spell.getHitDef().getAttackStyle().getAttackType();
	}
	
	@Override
	public int distanceRequired() {
		return 10;
	}

	@Override
	public void initialize() {
		if (!spell.isCombatSpell() || spell.getHitDef() == null)
			return;
		
		Item[] runesRequired = spell.getRunesRequired();
		int staffRequired = -1;
		if (spell == Spell.FLAMES_OF_ZAMORAK)
			staffRequired = 2417;
		else if (spell == Spell.CLAWS_OF_GUTHIX)
			staffRequired = 2416;
		else if (spell == Spell.SARADOMIN_STRIKE)
			staffRequired = 2415;
		if (spell == Spell.IBAN_BLAST)
			staffRequired = 1409;
		if (spell == Spell.MAGIC_DART)
			staffRequired = 4170;
		int reqs = (staffRequired != -1 ? 1 : 0) + (runesRequired != null ? 1 : 0) + 1;
		Requirement[] requirements = new Requirement[reqs];
		int i = 0;
		if (runesRequired != null) {
			requirements[i] = new RuneRequirement(spell) {
				@Override
				public String getFailMessage() {
					return "You do not have the runes required!";
				}
			};
			i++;
		}
		requirements[i++] = new SkillLevelRequirement(Skill.MAGIC, spell.getLevelRequired()) {
			@Override
			public String getFailMessage() {
				return "Your level in magic is not high enough!";
			}
		};
		if (staffRequired != -1) {
			requirements[i] = new EquipmentRequirement(Constants.WEAPON, staffRequired, 1, false) {
				@Override
				public String getFailMessage() {
					return "You must equip the proper god staff to use this spell!";
				}
			};
		}
		if (spell.getAnimation() != -1) {
			setAnimation(spell.getAnimation());
				setSound(spell.getCastSound());
		}
		HitDef hitDef = spell.getHitDef().clone();
		hitDef.setSpell(spell);
		if (getAttacker().hasGodChargeEffect() && (spell == Spell.FLAMES_OF_ZAMORAK || spell == Spell.CLAWS_OF_GUTHIX || spell == Spell.SARADOMIN_STRIKE))
			hitDef.setDamage(hitDef.getDamage() + 10);
		if (getAttacker().isPlayer()) {
			Player player = (Player) getAttacker();
			if (player.getEquipment().getId(Constants.HANDS) == 777 && (spell == Spell.AIR_BOLT || spell == Spell.WATER_BOLT || spell == Spell.EARTH_BOLT || spell == Spell.FIRE_BOLT)) {
				hitDef.setDamage(hitDef.getDamage() + 3);
			}
			if (player.getEquipment().getId(Constants.WEAPON) == 4675 || player.getEquipment().getId(Constants.WEAPON) == 4710 || player.getEquipment().getId(Constants.WEAPON) == 6914) {
				hitDef.setDamage((int) (hitDef.getDamage() * 1.1));
			}
			if (spell == Spell.MAGIC_DART)
				hitDef.setDamage(hitDef.getDamage() + (player.getSkill().getPlayerLevel(Skill.MAGIC)/10));
		}
		boolean unblockable = spell == Spell.NECROMANCER || spell == Spell.CHAOSELE_DISARM || spell == Spell.CHAOSELE_TELEPORT;
        setHits(new HitDef[]{hitDef.randomizeDamage().applyAccuracy().addEffects(new Effect[]{spell.getRequiredEffect(), spell.getAdditionalEffect()}).setUnblockable(unblockable)});
		setGraphic(spell.getGraphic());
		setAttackDelay(5);
		setRequirements(requirements);
	}
	
	@Override
	public int execute(CycleEventContainer container) {
		if (getAttacker().isPlayer()) {
			Player player = (Player) getAttacker();
			if (!player.isAutoCasting()) {
				getAttacker().getMovementHandler().reset();
			}
			if (player.getCastedSpell() == spell) {
                player.setCastedSpell(null);
                container.stop();
            }
        }
		HitDef hitDef = spell.getHitDef().clone();
		hitDef.setSpell(spell);
        if (hitDef.isFirstHit() && spellIsMulti(spell)  && getAttacker().inMulti()) {
			int enemiesHit = 0;
			for (Player players : World.getPlayers()) {
				if (players != null && players != getAttacker() && players != getVictim() && Misc.goodDistance(getVictim().getPosition(), players.getPosition(), 1)) {
					CombatCycleEvent.CanAttackResponse canAttackResponse = CombatCycleEvent.canAttack(getAttacker(), players);
					if (canAttackResponse == CanAttackResponse.SUCCESS) {
						if (enemiesHit > 13) {
							break;
						}
						hitDef.setFirstHit(false);
						hitDef.setCheckAccuracy(true);
						hitDef.reduceDamagePercentage(0.75);
						hitDef.setStartingHitDelay(-1);
						new Hit(getAttacker(), players, hitDef).initialize();
						enemiesHit++;
					}
				}
			}
			for (Npc npcs : World.getNpcs()) {
				if (npcs != null && npcs != getVictim() && getVictim().goodDistanceEntity(npcs, 1)) {
					CombatCycleEvent.CanAttackResponse canAttackResponse = CombatCycleEvent.canAttack(getAttacker(), npcs);
					if (canAttackResponse == CanAttackResponse.SUCCESS) {
						if (enemiesHit > 13) {
							break;
						}
						hitDef.setFirstHit(false);
						hitDef.setCheckAccuracy(true);
						hitDef.reduceDamagePercentage(0.75);
						hitDef.setStartingHitDelay(-1);
						new Hit(getAttacker(), npcs, hitDef).initialize();
						enemiesHit++;
					}
				}
			}
        }
        if (getAttacker().isPlayer()) {
            Player player = (Player)getAttacker();
            player.getSkill().addExp(Skill.MAGIC, spell.getExpEarned());
        }
        int delay = super.execute(container);
		return delay;
	}

	@SuppressWarnings("unused")
	private boolean spellIsMulti(Spell spell) {
		return spell == Spell.SMOKE_BURST || spell == Spell.SHADOW_BURST || spell == Spell.BLOOD_BURST || spell == Spell.ICE_BURST || spell == Spell.SMOKE_BARRAGE || spell == Spell.SHADOW_BARRAGE || spell == Spell.BLOOD_BARRAGE || spell == Spell.ICE_BARRAGE;
	}

	@Override
	public void failedRequirement() {
		if (getAttacker().isPlayer()) {
			Player player = (Player) getAttacker();
			if (player.getCastedSpell() == spell)
				player.setCastedSpell(null);
			else if (player.getAutoSpell() == spell)
				player.setAutoSpell(null);
		}
	}

	public Spell getSpell() {
		return spell;
	}
}
