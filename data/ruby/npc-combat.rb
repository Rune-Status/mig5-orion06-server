require 'java'
java_import "com.rs2.model.npcs.NpcCombatDef"
java_import "com.rs2.model.content.combat.AttackScript"
java_import "com.rs2.model.content.combat.attacks.BasicAttack"
java_import "com.rs2.model.content.combat.HealersCombatScript"
java_import "com.rs2.model.content.combat.weapon.AttackStyle"
java_import "com.rs2.model.content.combat.AttackType"
java_import "com.rs2.model.content.combat.weapon.Weapon"
java_import "com.rs2.model.content.combat.projectile.ProjectileTrajectory"
java_import "com.rs2.model.Graphic"
java_import "com.rs2.model.content.skills.magic.Spell"

class SaradominWizard < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.magicAttack(attacker, victim, Spell::SARADOMIN_STRIKE),
                BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, 20, Weapon::DRAGON_DAGGER)
        ];
    end
end

class DagPrime < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.magicAttack(attacker, victim, Spell::PRIME),
        ];
    end
end

class DagSupreme < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::LONGRANGE, 30, 4, 2855, Graphic.new(-1, 0), Graphic.new(-1, 0), 294, ProjectileTrajectory.ARROW)
        ];
    end
end

class Wallasalki < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.magicAttack(attacker, victim, Spell::WALLASALKI),
        ];
    end
end

class Spinolyp < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.magicAttack(attacker, victim, Spell::SPINOLYP),
				BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::LONGRANGE, 11, 4, 2868, Graphic.new(-1, 0), Graphic.new(-1, 0), 294, ProjectileTrajectory.ARROW)
        ];
    end
end

class Dagannoth88 < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::LONGRANGE, 11, 4, 1343, Graphic.new(-1, 0), Graphic.new(-1, 0), 294, ProjectileTrajectory.ARROW)
        ];
    end
end

class Kolodion < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.magicAttack(attacker, victim, Spell::SARADOMIN_STRIKE),
				BasicAttack.magicAttack(attacker, victim, Spell::FLAMES_OF_ZAMORAK),
				BasicAttack.magicAttack(attacker, victim, Spell::CLAWS_OF_GUTHIX)
        ];
    end
end

class BattleMageZamorak < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.magicAttack(attacker, victim, Spell::FLAMES_OF_ZAMORAK),
        ];
    end
end

class BattleMageSaradomin < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.magicAttack(attacker, victim, Spell::SARADOMIN_STRIKE),
        ];
    end
end

class BattleMageGuthix < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.magicAttack(attacker, victim, Spell::CLAWS_OF_GUTHIX)
        ];
    end
end

class AberrantSpectre < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.magicAttack(attacker, victim, Spell::ABERRANT),
        ];
    end
end

class InfernalMage < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.magicAttack(attacker, victim, Spell::FIRE_BLAST_INFERNAL_MAGE),
        ];
    end
end

class ZamorakWizard < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.magicAttack(attacker, victim, Spell::FLAMES_OF_ZAMORAK)
        ];
    end
end

class DarkWizard < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.magicAttack(attacker, victim, Spell::WATER_STRIKE),
                BasicAttack.magicAttack(attacker, victim, Spell::CONFUSE)
        ];
    end
end

class DarkWizard20 < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.magicAttack(attacker, victim, Spell::EARTH_STRIKE),
                BasicAttack.magicAttack(attacker, victim, Spell::WEAKEN)
        ];
    end
end

class FireWizard < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.magicAttack(attacker, victim, Spell::FIRE_STRIKE),
                BasicAttack.magicAttack(attacker, victim, Spell::WEAKEN)
        ];
    end
end

class WaterWizard < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.magicAttack(attacker, victim, Spell::WATER_STRIKE),
                BasicAttack.magicAttack(attacker, victim, Spell::WEAKEN)
        ];
    end
end

class EarthWizard < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.magicAttack(attacker, victim, Spell::EARTH_STRIKE),
                BasicAttack.magicAttack(attacker, victim, Spell::WEAKEN)
        ];
    end
end

class AirWizard < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.magicAttack(attacker, victim, Spell::WIND_STRIKE),
                BasicAttack.magicAttack(attacker, victim, Spell::WEAKEN)
        ];
    end
end

class TokXil < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 14, 4, 2628),
                BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::LONGRANGE, 17, 4, 2633, Graphic.new(-1, 0), Graphic.new(-1, 0), 443, ProjectileTrajectory.ARROW)
        ];
    end
end

class MejKot < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 28, 5, 2637)
        ];
    end
end

class KetZek < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 54, 5, 2644),
                BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::MAGIC, 49, 6, 2647, Graphic.new(-1, 0), Graphic.new(446, 0), 445, ProjectileTrajectory.SPELL)
        ];
    end
end

class Jad < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 98, 8, 2655),
                BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::LONGRANGE, 97, 8, 2652, Graphic.new(-1, 0), Graphic.new(451, 0), 411, ProjectileTrajectory.JAD_RANGE),
                BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::MAGIC, 97, 8, 2656, Graphic.new(447, 250), Graphic.new(157, 0), 448, ProjectileTrajectory.JAD_SPELL)
        ];
    end
end


class YtHurkot < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::SLASH, 14, 8, 2637),

        ];
    end
end

class Ahrims < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.magicAttack(attacker, victim, Spell::FIRE_WAVE),
                BasicAttack.magicAttack(attacker, victim, Spell::CONFUSE),
                BasicAttack.magicAttack(attacker, victim, Spell::CURSE)
        ];
    end
end

class BlackDragon < NpcCombatDef
    def attackScripts attacker, victim
        return [
				BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE, 55, 5, 81, Graphic.new(1, 100), Graphic.new(-1, 0), -1, ProjectileTrajectory.JAD_RANGE),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::AGGRESSIVE, AttackStyle::Bonus::SLASH, 21, 5, 91),
        ];
    end
end

class BlueDragon < NpcCombatDef
    def attackScripts attacker, victim
        return [
				BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE, 50, 5, 81, Graphic.new(1, 100), Graphic.new(-1, 0), -1, ProjectileTrajectory.JAD_RANGE),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::AGGRESSIVE, AttackStyle::Bonus::SLASH, 10, 5, 91),
        ];
    end
end

class RedDragon < NpcCombatDef
    def attackScripts attacker, victim
        return [
				BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE, 50, 5, 81, Graphic.new(1, 100), Graphic.new(-1, 0), -1, ProjectileTrajectory.JAD_RANGE),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::AGGRESSIVE, AttackStyle::Bonus::SLASH, 16, 5, 91),
        ];
    end
end

class GreenDragon < NpcCombatDef
    def attackScripts attacker, victim
        return [
				BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE, 50, 5, 81, Graphic.new(1, 100), Graphic.new(-1, 0), -1, ProjectileTrajectory.JAD_RANGE),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::AGGRESSIVE, AttackStyle::Bonus::SLASH, 8, 5, 91),
        ];
    end
end

class BronzeDragon < NpcCombatDef
    def attackScripts attacker, victim
        return [
				BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE_FAR, 55, 5, 81, Graphic.new(-1, 0), Graphic.new(-1, 0), 438, ProjectileTrajectory.ARROW),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::AGGRESSIVE, AttackStyle::Bonus::SLASH, 12, 5, 80),
        ];
    end
end

class IronDragon < NpcCombatDef
    def attackScripts attacker, victim
        return [
				BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE_FAR, 55, 5, 81, Graphic.new(-1, 0), Graphic.new(-1, 0), 438, ProjectileTrajectory.ARROW),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::AGGRESSIVE, AttackStyle::Bonus::SLASH, 20, 5, 80),
        ];
    end
end

class SteelDragon < NpcCombatDef
    def attackScripts attacker, victim
        return [
				BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE_FAR, 55, 5, 81, Graphic.new(-1, 0), Graphic.new(-1, 0), 438, ProjectileTrajectory.ARROW),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::AGGRESSIVE, AttackStyle::Bonus::SLASH, 22, 5, 80),
        ];
    end
end

class KingBlackDragon < NpcCombatDef
    def attackScripts attacker, victim
        return [
				BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE_FAR, 65, 5, 81, Graphic.new(-1, 0), Graphic.new(-1, 0), 393, ProjectileTrajectory.ARROW),
				BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::MAGIC, 10, 5, 81, Graphic.new(-1, 0), Graphic.new(-1, 0), 394, ProjectileTrajectory.ARROW),
				BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::MAGIC, 10, 5, 81, Graphic.new(-1, 0), Graphic.new(-1, 0), 395, ProjectileTrajectory.ARROW),
				BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::MAGIC, 10, 5, 81, Graphic.new(-1, 0), Graphic.new(-1, 0), 396, ProjectileTrajectory.ARROW),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::AGGRESSIVE, AttackStyle::Bonus::SLASH, 26, 5, 80),
        ];
    end
end

class Elvarg < NpcCombatDef
    def attackScripts attacker, victim
        return [
				BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::DRAGONFIRE, 28, 5, 81, Graphic.new(1, 100), Graphic.new(-1, 0), -1, ProjectileTrajectory.JAD_RANGE),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::AGGRESSIVE, AttackStyle::Bonus::SLASH, 8, 5, 91),
        ];
    end
end

class Melzar < NpcCombatDef
    def attackScripts attacker, victim
        return [
			BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, 5, Weapon::FISTS),
			BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::AGGRESSIVE, 5, Weapon::FISTS),
			BasicAttack.magicAttack(attacker, victim, Spell::CURSE),
			BasicAttack.magicAttack(attacker, victim, Spell::FIRE_STRIKE),
			BasicAttack.magicAttack(attacker, victim, Spell::MELZAR),
			BasicAttack.magicAttack(attacker, victim, Spell::WEAKEN),
		];
    end
end

class KalphiteQueen1stForm < NpcCombatDef
    def attackScripts attacker, victim
        return [
				BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::KQ_RANGED, 31, 8, 1184, Graphic.new(-1, 0), Graphic.new(-1, 0), 289, ProjectileTrajectory.ARROW),
				BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::KQ_MAGIC, 31, 4, 1184, Graphic.new(278, 0), Graphic.new(281, 0), 280, ProjectileTrajectory.SPELL),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::STAB, 31, 5, 1184),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::CRUSH, 31, 5, 1185),
        ];
    end
end

class KalphiteQueen2ndForm < NpcCombatDef
    def attackScripts attacker, victim
        return [
				BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::KQ_RANGED, 31, 8, 1170, Graphic.new(-1, 0), Graphic.new(-1, 0), 289, ProjectileTrajectory.ARROW),
				BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::KQ_MAGIC, 31, 4, 1170, Graphic.new(279, 0), Graphic.new(281, 0), 280, ProjectileTrajectory.SPELL),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::MELEE_ACCURATE, AttackStyle::Bonus::STAB, 31, 5, 1177),
        ];
    end
end

class Karil < NpcCombatDef
    def attackScripts attacker, victim
        return [
				BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::LONGRANGE, 20, 4, 2075, Graphic.new(-1, 0), Graphic.new(-1, 0), 27, ProjectileTrajectory.ARROW),
        ];
    end
end

class ChaosElemental < NpcCombatDef
    def attackScripts attacker, victim
        return [
                BasicAttack.magicAttack(attacker, victim, Spell::CHAOSELE_DISARM),
				BasicAttack.magicAttack(attacker, victim, Spell::CHAOSELE_TELEPORT),
				BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::LONGRANGE, 28, 3, 3146, Graphic.new(556, 0), Graphic.new(558, 0), 557, ProjectileTrajectory.SPELL),
				BasicAttack.projectileAttack(attacker, victim, AttackType::MELEE, AttackStyle::Mode::MELEE_FAR, 28, 3, 3146, Graphic.new(556, 0), Graphic.new(558, 0), 557, ProjectileTrajectory.SPELL),
				BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::MAGIC, 28, 3, 3146, Graphic.new(556, 0), Graphic.new(558, 0), 557, ProjectileTrajectory.SPELL),
				BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::MAGIC, 28, 3, 3146, Graphic.new(556, 0), Graphic.new(558, 0), 557, ProjectileTrajectory.SPELL),
				BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::MAGIC, 28, 3, 3146, Graphic.new(556, 0), Graphic.new(558, 0), 557, ProjectileTrajectory.SPELL),
        ];
    end
end

class SkeletalWyvern < NpcCombatDef
    def attackScripts attacker, victim
        return [
				BasicAttack.projectileAttack(attacker, victim, AttackType::MAGIC, AttackStyle::Mode::ICY_BREATH, 55, 6, 2988, Graphic.new(1, 100), Graphic.new(-1, 0), -1, ProjectileTrajectory.JAD_RANGE),
				BasicAttack.projectileAttack(attacker, victim, AttackType::RANGED, AttackStyle::Mode::LONGRANGE, 14, 6, 2989, Graphic.new(-1, 0), Graphic.new(-1, 0), -1, ProjectileTrajectory.ARROW),
				BasicAttack.meleeAttack(attacker, victim, AttackStyle::Mode::AGGRESSIVE, AttackStyle::Bonus::SLASH, 14, 6, 2986),
        ];
    end
end

NpcCombatDef.add([2025], Ahrims.new)
NpcCombatDef.add([2028], Karil.new)
NpcCombatDef.add([2746], YtHurkot.new)
NpcCombatDef.add([2631], TokXil.new)
NpcCombatDef.add([2741], MejKot.new)
NpcCombatDef.add([2743], KetZek.new)
NpcCombatDef.add([2745], Jad.new)
NpcCombatDef.add([174], DarkWizard.new)
NpcCombatDef.add([172], DarkWizard20.new)
NpcCombatDef.add([2709,13], FireWizard.new)
NpcCombatDef.add([2710], WaterWizard.new)
NpcCombatDef.add([2711], EarthWizard.new)
NpcCombatDef.add([2712], AirWizard.new)
NpcCombatDef.add([1007], ZamorakWizard.new.bonusAtt(0, 0, 0, 350, 0))
NpcCombatDef.add([1264], SaradominWizard.new.bonusAtt(500, 500, 500, 500, 0))
NpcCombatDef.add([54], BlackDragon.new)
NpcCombatDef.add([55], BlueDragon.new)
NpcCombatDef.add([53], RedDragon.new)
NpcCombatDef.add([941], GreenDragon.new)
NpcCombatDef.add([50], KingBlackDragon.new)
NpcCombatDef.add([1590], BronzeDragon.new)
NpcCombatDef.add([1591], IronDragon.new)
NpcCombatDef.add([1592], SteelDragon.new)
NpcCombatDef.add([742], Elvarg.new)
NpcCombatDef.add([753], Melzar.new)
NpcCombatDef.add([1158], KalphiteQueen1stForm.new)
NpcCombatDef.add([1160], KalphiteQueen2ndForm.new)
NpcCombatDef.add([1604,1605,1606,1607], AberrantSpectre.new)
NpcCombatDef.add([1643], InfernalMage.new)
NpcCombatDef.add([3200], ChaosElemental.new)
NpcCombatDef.add([3068], SkeletalWyvern.new)
NpcCombatDef.add([907, 908, 909, 910, 911], Kolodion.new)
NpcCombatDef.add([912], BattleMageZamorak.new)
NpcCombatDef.add([913], BattleMageSaradomin.new)
NpcCombatDef.add([914], BattleMageGuthix.new)
NpcCombatDef.add([2456], Dagannoth88.new)
NpcCombatDef.add([2457], Wallasalki.new)
NpcCombatDef.add([2881], DagSupreme.new)
NpcCombatDef.add([2882], DagPrime.new)
NpcCombatDef.add([2892], Spinolyp.new)