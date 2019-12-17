package com.rs2.model.content.skills.magic;

import java.util.HashMap;
import java.util.Map;

import com.rs2.model.players.Player;

/**
 *
 */
public enum SpellBook {

	MODERN(modernSpells(), modernAutoSpells()), ANCIENT(ancientSpells(), ancientAutoSpells()), NECROMANCY(necromancySpells(), null);

	private Map<Integer, Spell> spells, autoSpells;

	SpellBook(Map<Integer, Spell> spells, Map<Integer, Spell> autoSpells) {
		this.spells = spells;
		this.autoSpells = autoSpells;
	}

	public static Spell getSpell(Player player, int id) {
		return player.getMagicBookType().spells.get(id);
	}

	public static Spell getAutoSpell(Player player, int id) {
		return player.getMagicBookType().autoSpells.get(id);
	}

	private static Map<Integer, Spell> modernAutoSpells() {
		Map<Integer, Spell> spells = new HashMap<Integer, Spell>();
		spells.put(1830, Spell.WIND_STRIKE);
		spells.put(1831, Spell.WATER_STRIKE);
		spells.put(1832, Spell.EARTH_STRIKE);
		spells.put(1833, Spell.FIRE_STRIKE);
		spells.put(1834, Spell.AIR_BOLT);
		spells.put(1835, Spell.WATER_BOLT);
		spells.put(1836, Spell.EARTH_BOLT);
		spells.put(1837, Spell.FIRE_BOLT);
		spells.put(1838, Spell.AIR_BLAST);
		spells.put(1839, Spell.WATER_BLAST);
		spells.put(1840, Spell.EARTH_BLAST);
		spells.put(1841, Spell.FIRE_BLAST);
		spells.put(1842, Spell.AIR_WAVE);
		spells.put(1843, Spell.WATER_WAVE);
		spells.put(1844, Spell.EARTH_WAVE);
		spells.put(1845, Spell.FIRE_WAVE);
		//slayer staff
		spells.put(12051, Spell.MAGIC_DART);
		spells.put(12052, Spell.CRUMBLE_UNDEAD);
		spells.put(12053, Spell.AIR_WAVE);
		spells.put(12054, Spell.WATER_WAVE);
		spells.put(12055, Spell.EARTH_WAVE);
		spells.put(12056, Spell.FIRE_WAVE);
		return spells;
	}
	
	private static Map<Integer, Spell> ancientAutoSpells() {
		Map<Integer, Spell> spells = new HashMap<Integer, Spell>();
		spells.put(13189, Spell.SMOKE_RUSH);
		spells.put(13241, Spell.SHADOW_RUSH);
		spells.put(13147, Spell.BLOOD_RUSH);
		spells.put(6162, Spell.ICE_RUSH);
		spells.put(13215, Spell.SMOKE_BURST);
		spells.put(13267, Spell.SHADOW_BURST);
		spells.put(13167, Spell.BLOOD_BURST);
		spells.put(13125, Spell.ICE_BURST);
		spells.put(13202, Spell.SMOKE_BLITZ);
		spells.put(13254, Spell.SHADOW_BLITZ);
		spells.put(13158, Spell.BLOOD_BLITZ);
		spells.put(13114, Spell.ICE_BLITZ);
		spells.put(13228, Spell.SMOKE_BARRAGE);
		spells.put(13280, Spell.SHADOW_BARRAGE);
		spells.put(13178, Spell.BLOOD_BARRAGE);
		spells.put(13136, Spell.ICE_BARRAGE);
		return spells;
	}
	
	private static Map<Integer, Spell> modernSpells() {
		Map<Integer, Spell> spells = new HashMap<Integer, Spell>();
		spells.put(1168, Spell.TELEGRAB);
		spells.put(1179, Spell.CHARGE_WATER);
		spells.put(1182, Spell.CHARGE_EARTH);
		spells.put(1184, Spell.CHARGE_FIRE);
		spells.put(1186, Spell.CHARGE_AIR);
		spells.put(1152, Spell.WIND_STRIKE);
		spells.put(1154, Spell.WATER_STRIKE);
		spells.put(1156, Spell.EARTH_STRIKE);
		spells.put(1158, Spell.FIRE_STRIKE);
		spells.put(1160, Spell.AIR_BOLT);
		spells.put(1163, Spell.WATER_BOLT);
		spells.put(1166, Spell.EARTH_BOLT);
		spells.put(1169, Spell.FIRE_BOLT);
		spells.put(1172, Spell.AIR_BLAST);
		spells.put(1175, Spell.WATER_BLAST);
		spells.put(1177, Spell.EARTH_BLAST);
		spells.put(1181, Spell.FIRE_BLAST);
		spells.put(1183, Spell.AIR_WAVE);
		spells.put(1185, Spell.WATER_WAVE);
		spells.put(1188, Spell.EARTH_WAVE);
		spells.put(1189, Spell.FIRE_WAVE);
		spells.put(1153, Spell.CONFUSE);
		spells.put(1157, Spell.WEAKEN);
		spells.put(1161, Spell.CURSE);
		spells.put(1542, Spell.VULNERABILITY);
		spells.put(1543, Spell.ENFEEBLE);
		spells.put(1562, Spell.STUN);
		spells.put(1572, Spell.BIND);
		spells.put(1582, Spell.SNARE);
		spells.put(1592, Spell.ENTANGLE);
		spells.put(1171, Spell.CRUMBLE_UNDEAD);
		spells.put(1539, Spell.IBAN_BLAST);
		spells.put(12037, Spell.MAGIC_DART);
		spells.put(1190, Spell.SARADOMIN_STRIKE);
		spells.put(1191, Spell.CLAWS_OF_GUTHIX);
		spells.put(1192, Spell.FLAMES_OF_ZAMORAK);
		spells.put(12445, Spell.TELEBLOCK);
		spells.put(1159, Spell.BONES_TO_BANANA);
		spells.put(15877, Spell.BONES_TO_PEACH);
		spells.put(1162, Spell.LOW_ALCH);
		spells.put(1178, Spell.HIGH_ALCH);
		spells.put(1173, Spell.SUPERHEAT);
		spells.put(1155, Spell.ENCHANT_LV_1);
		spells.put(1165, Spell.ENCHANT_LV_2);
		spells.put(1176, Spell.ENCHANT_LV_3);
		spells.put(1180, Spell.ENCHANT_LV_4);
		spells.put(1187, Spell.ENCHANT_LV_5);
		spells.put(6003, Spell.ENCHANT_LV_6);
		spells.put(1193, Spell.CHARGE);
		spells.put(12425, Spell.TELEOTHER_LUMBRIDGE);
		spells.put(12435, Spell.TELEOTHER_FALADOR);
		spells.put(12455, Spell.TELEOTHER_CAMELOT);
		spells.put(1164, Spell.VARROCK);
		spells.put(1167, Spell.LUMBRIDGE);
		spells.put(1170, Spell.FALADOR);
		spells.put(1174, Spell.CAMELOT);
		spells.put(1540, Spell.ARDOUGNE);
		spells.put(1541, Spell.WATCHTOWER);
		spells.put(7455, Spell.TROLLHEIM);
		spells.put(18470, Spell.APE_ATOLL);
		return spells;
	}
	
	private static Map<Integer, Spell> ancientSpells() {
		Map<Integer, Spell> spells = new HashMap<Integer, Spell>();
		spells.put(12939, Spell.SMOKE_RUSH);
		spells.put(12987, Spell.SHADOW_RUSH);
		spells.put(12901, Spell.BLOOD_RUSH);
		spells.put(12861, Spell.ICE_RUSH);
		spells.put(12963, Spell.SMOKE_BURST);
		spells.put(13011, Spell.SHADOW_BURST);
		spells.put(12919, Spell.BLOOD_BURST);
		spells.put(12881, Spell.ICE_BURST);
		spells.put(12951, Spell.SMOKE_BLITZ);
		spells.put(12999, Spell.SHADOW_BLITZ);
		spells.put(12911, Spell.BLOOD_BLITZ);
		spells.put(12871, Spell.ICE_BLITZ);
		spells.put(12975, Spell.SMOKE_BARRAGE);
		spells.put(13023, Spell.SHADOW_BARRAGE);
		spells.put(12929, Spell.BLOOD_BARRAGE);
		spells.put(12891, Spell.ICE_BARRAGE);
		spells.put(13035, Spell.PADDEWWA);
		spells.put(13045, Spell.SENNTISTEN);
		spells.put(13053, Spell.KHARYRLL);
		spells.put(13061, Spell.LASSAR);
		spells.put(13069, Spell.DAREEYAK);
		spells.put(13079, Spell.CARRALLANGAR);
		spells.put(13087, Spell.ANNAKARL);
		spells.put(13095, Spell.GHORROCK);
		return spells;
	}
	
	private static Map<Integer, Spell> necromancySpells() {
		Map<Integer, Spell> spells = new HashMap<Integer, Spell>();
		spells.put(19117, Spell.REANIMATE_GOBLIN);
		spells.put(19125, Spell.REANIMATE_MONKEY);
		spells.put(19133, Spell.REANIMATE_IMP);
		spells.put(19141, Spell.REANIMATE_SCORPION);
		spells.put(19151, Spell.REANIMATE_BEAR);
		spells.put(19161, Spell.REANIMATE_UNICORN);
		spells.put(19171, Spell.REANIMATE_DOG);
		spells.put(19181, Spell.REANIMATE_CHAOS_DRUID);
		spells.put(19191, Spell.REANIMATE_GIANT);
		spells.put(19201, Spell.REANIMATE_OGRE);
		spells.put(19211, Spell.REANIMATE_ELF);
		spells.put(19221, Spell.REANIMATE_TROLL);
		spells.put(19231, Spell.REANIMATE_KALPHITE);
		spells.put(19241, Spell.REANIMATE_DAGANNOTH);
		spells.put(19251, Spell.REANIMATE_BLOODVELD);
		spells.put(19261, Spell.REANIMATE_TZHAAR);
		spells.put(19271, Spell.REANIMATE_DEMON);
		spells.put(19281, Spell.REANIMATE_ABYSSAL);
		spells.put(19291, Spell.REANIMATE_DRAGON);
		return spells;
	}
	
	public Map<Integer, Spell> getSpells() {
		return spells;
	}
}
