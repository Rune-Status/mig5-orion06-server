package com.rs2.model.content.combat.weapon;

/**
 *
 */
import static com.rs2.model.content.combat.weapon.AttackStyle.Bonus.CRUSH;
import static com.rs2.model.content.combat.weapon.AttackStyle.Bonus.RANGED;
import static com.rs2.model.content.combat.weapon.AttackStyle.Bonus.SLASH;
import static com.rs2.model.content.combat.weapon.AttackStyle.Bonus.STAB;
import static com.rs2.model.content.combat.weapon.AttackStyle.Mode.AGGRESSIVE;
import static com.rs2.model.content.combat.weapon.AttackStyle.Mode.CONTROLLED;
import static com.rs2.model.content.combat.weapon.AttackStyle.Mode.DEFENSIVE;
import static com.rs2.model.content.combat.weapon.AttackStyle.Mode.LONGRANGE;
import static com.rs2.model.content.combat.weapon.AttackStyle.Mode.MELEE_ACCURATE;
import static com.rs2.model.content.combat.weapon.AttackStyle.Mode.RANGED_ACCURATE;
import static com.rs2.model.content.combat.weapon.AttackStyle.Mode.RAPID;

import com.rs2.model.content.combat.AttackType;

public enum WeaponInterface {

	FISTS(5855, 417, new int[]{5857, -1}, -1, -1, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 5860, MELEE_ACCURATE, CRUSH), new AttackStyle(AttackType.MELEE, 5862, AGGRESSIVE, CRUSH), new AttackStyle(AttackType.MELEE, 5861, DEFENSIVE, CRUSH)}),

	WHIP(12290, 1080, new int[]{12293, 12291}, 12323, 12335, 12311, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 12298, MELEE_ACCURATE, SLASH), new AttackStyle(AttackType.MELEE, 12297, CONTROLLED, SLASH), new AttackStyle(AttackType.MELEE, 12296, DEFENSIVE, SLASH)}),

	MAUL(425, 1079, new int[]{428, 426}, 7474, 7486, 7462, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 433, MELEE_ACCURATE, CRUSH), new AttackStyle(AttackType.MELEE, 432, AGGRESSIVE, CRUSH), new AttackStyle(AttackType.MELEE, 431, DEFENSIVE, CRUSH)}),

	TWO_HANDED(4705, 396, new int[]{4708, 4706}, 7699, 7711, 7687, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 4711, MELEE_ACCURATE, SLASH), new AttackStyle(AttackType.MELEE, 4714, AGGRESSIVE, SLASH), new AttackStyle(AttackType.MELEE, 4713, AGGRESSIVE, CRUSH), new AttackStyle(AttackType.MELEE, 4712, DEFENSIVE, SLASH)}),

	DAGGER(2276, 401, new int[]{2279, 2277}, 7574, 7586, 7562, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 2282, MELEE_ACCURATE, STAB), new AttackStyle(AttackType.MELEE, 2285, AGGRESSIVE, STAB), new AttackStyle(AttackType.MELEE, 2284, AGGRESSIVE, SLASH), new AttackStyle(AttackType.MELEE, 2283, DEFENSIVE, STAB)}),

	STAFF(328, 1784, new int[]{331, 329}, -1, -1, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 336, MELEE_ACCURATE, CRUSH), new AttackStyle(AttackType.MELEE, 335, AGGRESSIVE, CRUSH), new AttackStyle(AttackType.MELEE, 334, DEFENSIVE, CRUSH)}),

	PICKAXE(5570, 399, new int[]{5573, 5571}, -1, -1, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 5576, MELEE_ACCURATE, STAB), new AttackStyle(AttackType.MELEE, 5579, AGGRESSIVE, STAB), new AttackStyle(AttackType.MELEE, 5578, AGGRESSIVE, STAB), new AttackStyle(AttackType.MELEE, 5577, DEFENSIVE, STAB)}),
	
	AXE(1698, 399, new int[]{1701, 1699}, 7499, 7511, 7487, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 1704, MELEE_ACCURATE, SLASH), new AttackStyle(AttackType.MELEE, 1707, AGGRESSIVE, SLASH), new AttackStyle(AttackType.MELEE, 1706, AGGRESSIVE, CRUSH), new AttackStyle(AttackType.MELEE, 1705, DEFENSIVE, SLASH)}),

	HALBERD(8460, 420, new int[]{8463, 8461}, 8493, 8505, 8481, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 8466, CONTROLLED, STAB), new AttackStyle(AttackType.MELEE, 8468, AGGRESSIVE, SLASH), new AttackStyle(AttackType.MELEE, 8467, DEFENSIVE, STAB)}),

	CLAWS(7762, 396, new int[]{7765, 7763}, 7800, 7812, 7788, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 7768, MELEE_ACCURATE, SLASH), new AttackStyle(AttackType.MELEE, 7771, AGGRESSIVE, SLASH), new AttackStyle(AttackType.MELEE, 7770, CONTROLLED, STAB), new AttackStyle(AttackType.MELEE, 7769, DEFENSIVE, SLASH)}),

	SPEAR(4679, 395, new int[]{4682, 4680}, 7674, 7686, 7662, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 4685, CONTROLLED, STAB), new AttackStyle(AttackType.MELEE, 4688, CONTROLLED, SLASH), new AttackStyle(AttackType.MELEE, 4687, CONTROLLED, CRUSH), new AttackStyle(AttackType.MELEE, 4686, DEFENSIVE, STAB)}),

	MACE(3796, 400, new int[]{3799, 3797}, 7624, 7636, 7612, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 3802, MELEE_ACCURATE, CRUSH), new AttackStyle(AttackType.MELEE, 3805, AGGRESSIVE, CRUSH), new AttackStyle(AttackType.MELEE, 3804, CONTROLLED, STAB), new AttackStyle(AttackType.MELEE, 3803, DEFENSIVE, CRUSH)}),

	SCYTHE(776, 396, new int[]{779, 777}, -1, -1, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 782, MELEE_ACCURATE, SLASH), new AttackStyle(AttackType.MELEE, 785, AGGRESSIVE, STAB), new AttackStyle(AttackType.MELEE, 784, AGGRESSIVE, CRUSH), new AttackStyle(AttackType.MELEE, 783, DEFENSIVE, SLASH)}),

	SHORT_BOW(1749, 370, new int[]{1752, 1750}, 7524, 7536, 7512, new AttackStyle[]{new AttackStyle(AttackType.RANGED, 1757, RANGED_ACCURATE, RANGED), new AttackStyle(AttackType.RANGED, 1756, RAPID, RANGED), new AttackStyle(AttackType.RANGED, 1755, LONGRANGE, RANGED)}),

	LONG_BOW(1764, 367, new int[]{1767, 1765}, 7549, 7561, 7537, new AttackStyle[]{new AttackStyle(AttackType.RANGED, 1772, RANGED_ACCURATE, RANGED), new AttackStyle(AttackType.RANGED, 1771, RAPID, RANGED), new AttackStyle(AttackType.RANGED, 1770, LONGRANGE, RANGED)}),

	THROWING(4446, 364, new int[]{4449, 4447}, 7649, 7661, new AttackStyle[]{new AttackStyle(AttackType.RANGED, 4454, RANGED_ACCURATE, RANGED), new AttackStyle(AttackType.RANGED, 4453, RAPID, RANGED), new AttackStyle(AttackType.RANGED, 4452, LONGRANGE, RANGED)}),

	LONGSWORD(2423, 396, new int[]{2426, 2424}, 7599, 7611, 7587, new AttackStyle[]{new AttackStyle(AttackType.MELEE, 2429, MELEE_ACCURATE, SLASH), new AttackStyle(AttackType.MELEE, 2432, AGGRESSIVE, SLASH), new AttackStyle(AttackType.MELEE, 2431, CONTROLLED, STAB), new AttackStyle(AttackType.MELEE, 2430, DEFENSIVE, SLASH)});

	private int interfaceId;
	public int atkSound;
	private int specialBarId;
	private int specialTextId;
	private int specialBarButtonId;
	private int[] interfaceData;
	private AttackStyle[] attackStyles;

	WeaponInterface(int interfaceId, int atkSound, int[] interfaceData, int specialBarId, int specialTextId, int specialBarButtonId, AttackStyle[] attackStyles) {
		this.interfaceId = interfaceId;
		this.atkSound = atkSound;
		this.interfaceData = interfaceData;
		this.specialBarId = specialBarId;
		this.specialBarButtonId = specialBarButtonId;
		this.specialTextId = specialTextId;
		this.attackStyles = attackStyles;
	}

	WeaponInterface(int interfaceId, int atkSound, int[] interfaceData, int specialBarId, int specialTextId, AttackStyle[] attackStyles) {
		this(interfaceId, atkSound, interfaceData, specialBarId, specialTextId, -1, attackStyles);
	}

	public int getSpecialBarButtonId() {
		return specialBarButtonId;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

	public int getSpecialBarId() {
		return specialBarId;
	}

	public int weaponNameChild() {
		return interfaceData[0];
	}

	public int weaponDisplayChild() {
		return interfaceData[1];
	}

	public int[] getInterfaceData() {
		return interfaceData;
	}

	public int getSpecialTextId() {
		return specialTextId;
	}

	public AttackStyle[] getAttackStyles() {
		return attackStyles;
	}
}
