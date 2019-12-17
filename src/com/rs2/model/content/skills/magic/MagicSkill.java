package com.rs2.model.content.skills.magic;

import java.util.ArrayList;

import com.rs2.Constants;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.combat.attacks.SpellAttack;
import com.rs2.model.content.combat.hit.HitDef;
import com.rs2.model.content.combat.projectile.Projectile;
import com.rs2.model.content.combat.projectile.ProjectileDef;
import com.rs2.model.content.skills.Menus;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.pf.StraightPathFinder;
import com.rs2.util.Misc;
import com.rs2.util.requirement.ExecutableRequirement;
import com.rs2.util.requirement.Requirement;
import com.rs2.util.requirement.RuneRequirement;
import com.rs2.util.requirement.SkillLevelRequirement;

/**
 *
 */
public abstract class MagicSkill extends CycleEvent {

	private Requirement[] requirements;
	private Player player;
	private Spell spell;
	private final int taskId;

	public static final int BANANA = 1963;
	public static final int PEACH = 6883;

	// ore1, ore1amount, ore2, ore2amount, item, xp, smith lvl req
	private static final int[][] SMELT = {{436, 1, 438, 1, 2349, 6, 1}, // TIN
			{438, 1, 436, 1, 2349, 6, 1}, // COPPER
			{440, 1, 453, 2, 2353, 18, 30}, // STEEL ORE
			{440, 1, -1, -1, 2351, 13, 15}, // IRON ORE
			{442, 1, -1, -1, 2355, 14, 20}, // SILVER ORE
			{444, 1, -1, -1, 2357, 23, 40}, // GOLD BAR
			{447, 1, 453, 4, 2359, 30, 50}, // MITHRIL ORE
			{449, 1, 453, 6, 2361, 38, 70}, // ADDY ORE
			{451, 1, 453, 8, 2363, 50, 85}, // RUNE ORE
	};

	// unenchanted ring, unenchanted amulet,
	// unenchanted necklace, enchanted ring,
	// enchanted amulet, enchanted necklace,
	// rune1, rune1 amount, rune2, rune2 amount,
	// level required, xp, anim, gfx
	private static final int[][] ENCHANT = {{1637, 1694, 1656, 2550, 1727, 3853, 555, 1, 0, 0, 7}, // sapphire
			{1639, 1696, 1658, 2552, 1729, 5521, 556, 3, 0, 0, 27}, // emerald
			{1641, 1698, -1, 2568, 1725, -1, 554, 5, 0, 0, 49, 59}, // ruby
			{1643, 1700, -1, 2570, 1731, -1, 557, 10, 0, 0, 57, 67}, // diamond
			{1645, 1702, -1, 2572, 1712, -1, 557, 15, 555, 15, 68}, // dragonstone
			{6575, 6581, -1, 6583, 6585, -1, 557, 20, 554, 20, 87} // onyx
	};

	//head, npc
	private static final int[][] REANIMATE = {
			{8080, 3867}, // goblin
			{8082, 3868}, // monkey
			{8084, 3869}, // imp
			{8086, 3870}, // scorpion
			{8088, 3871}, // bear
			{8090, 3872}, // unicorn
			{8092, 3873}, // dog
			{8094, 3874}, // chaos druid
			{8096, 3875}, // giant
			{8098, 3876}, // ogre
			{8100, 3877}, // elf
			{8102, 3878}, // troll
			{8104, 3879}, // kalphite
			{8106, 3880}, // dagannoth
			{8108, 3881}, // bloodveld
			{8110, 3882}, // tzhaar
			{8112, 3883}, // demon
			{8114, 3884}, // abyssal
			{8116, 3885}, // dragon
	};

	private MagicSkill(Player player, Spell spell) {
		this.player = player;
		this.spell = spell;
		//this.requirements = new Requirement[spell.getRunesRequired().length + 1];
		this.requirements = new Requirement[1 + 1];
		this.taskId = player.getTask();
		int i = 0;//
		
		if (!isTeleportSpell(spell)){//try it
		requirements[i++] = new RuneRequirement(spell) {
			@Override
			public String getFailMessage() {
				return SpellAttack.FAILED_REQUIRED_RUNES;
			}
		};
		}
		if (spell != Spell.LUMBRIDGE && spell != null) { //uhh I wish I could see that error again
		requirements[i] = new SkillLevelRequirement(Skill.MAGIC, spell.getLevelRequired()) {
			@Override
			public String getFailMessage() {
				return SpellAttack.FAILED_LEVEL_REQUIREMENT;
			}
		};
		}
	}

	private boolean isTeleportSpell(Spell spell) {
		boolean canCast = false;//test
		Spell[] teleportSpells = new Spell[] {Spell.LUMBRIDGE/*Spell.VARROCK, Spell.LUMBRIDGE, Spell.FALADOR, Spell.CAMELOT, Spell.ARDOUGNE, Spell.WATCHTOWER}*/}; //add more later
		for (Spell spells : teleportSpells) {
			if (canCast) {
				continue;
			}
			if (spell == spells) {
				canCast = true;
			}
		}
		return canCast;
	}
	
	@Override
	public void stop() {
		if (player.getSkilling() == this)
			player.setSkilling(null);
	}

	private boolean canUseSpell() {
		for (Requirement requirement : requirements) {
			if (requirement == null) 
				continue;
			
			if (!requirement.meets(player))
				return false;
		}
		return true;
	}

	void initialize() {
		player.setSkilling(this);
		execute(null);
	}

	boolean initialize_continuousSpell() {
		if (!player.checkTask(taskId) || !canUseSpell() || !onExecute())
			return false;
		player.setSkilling(this);
		execute(null);
		return true;
	}

	private void hit(Tick tick, HitDef hitDef) {
		tick.stop();
		onHit(hitDef);
	}

	@Override
	public final void execute(CycleEventContainer container) {
		if (!player.checkTask(taskId) || !canUseSpell() || !onExecute()) {
			//container.stop();
			stop();
			return;
		}

		
			for (Requirement requirement : requirements) {
				if (requirement instanceof ExecutableRequirement)
					((ExecutableRequirement) requirement).execute(player);
			}
		

		if (spell.getAnimation() != -1)
			player.getUpdateFlags().sendAnimation(spell.getAnimation());

		if (spell.getGraphic() != null)
			player.getUpdateFlags().sendGraphic(spell.getGraphic());

		if(spell.getCastSound() != -1)
			player.getActionSender().sendSound(spell.getCastSound(), 1, 0);

		player.getSkill().addExp(Skill.MAGIC, spell.getExpEarned());

		if(spell.getReward() != null){
			for (Item item : spell.getReward()) {
				player.getInventory().addItemOrDrop(item);
			}
		}
	}

	public void castProjectile(final Entity entity, final Position end) {
		final HitDef hitDef = spell.getHitDef();
		if (end != null && hitDef != null) {
			ProjectileDef projectileDef = hitDef.getProjectileDef();
			if (projectileDef != null || hitDef.getHitGraphic() != null) {
				final int hitDelay = hitDef.calculateHitDelay(player.getPosition(), end);
				if (projectileDef != null) {
					Projectile projectile;
					if (entity != null)
						projectile = new Projectile(player, entity, projectileDef);
					else
						projectile = new Projectile(player.getPosition(), player.getSize(), end, 0, projectileDef);
					projectile.show();
				}
				final Tick tick = new Tick(hitDelay) {
					@Override
					public void execute() {
						MagicSkill.this.hit(this, hitDef);
						if (hitDef.getHitGraphic() != null) {
							if (entity != null)
								entity.getUpdateFlags().sendGraphic(hitDef.getHitGraphic());
							else
								player.getActionSender().sendStillGraphic(hitDef.getHitGraphic().getId(), end, hitDef.getHitGraphic().getValue());
						}
					}
				};
				World.getTickManager().submit(tick);
			}
		}
	}

	public abstract boolean onExecute();
	public abstract void onHit(HitDef hitDef);

	public static void spellOnItem(final Player player, final Spell spell, final int itemId, final int slot) {
		MagicSkill magicSkill = new MagicSkill(player, spell) {
			@Override
			public boolean onExecute() {
				switch (spell) {
				case ENCHANT_LV_1 :
					return enchantJewelry(itemId, 0);
				case ENCHANT_LV_2 :
					return enchantJewelry(itemId, 1);
				case ENCHANT_LV_3 :
					return enchantJewelry(itemId, 2);
				case ENCHANT_LV_4 :
					return enchantJewelry(itemId, 3);
				case ENCHANT_LV_5 :
					return enchantJewelry(itemId, 4);
				case ENCHANT_LV_6 :
					return enchantJewelry(itemId, 5);
				case LOW_ALCH :
					int lowAlchPrice = ItemManager.getInstance().getItemValue(itemId, "lowalch", 995);
					return alchItem(player, itemId, slot, lowAlchPrice, 1200, spell);
				case HIGH_ALCH :
					int highAlchPrice = ItemManager.getInstance().getItemValue(itemId, "highalch", 995);
					return alchItem(player, itemId, slot, highAlchPrice, 3000, spell);
				case SUPERHEAT :
					return superHeatItem(itemId);
				case REANIMATE_GOBLIN :
					return reanimateNpc(itemId, 0);
				case REANIMATE_MONKEY :
					return reanimateNpc(itemId, 1);
				case REANIMATE_IMP :
					return reanimateNpc(itemId, 2);
				case REANIMATE_SCORPION :
					return reanimateNpc(itemId, 3);
				case REANIMATE_BEAR :
					return reanimateNpc(itemId, 4);
				case REANIMATE_UNICORN :
					return reanimateNpc(itemId, 5);
				case REANIMATE_DOG :
					return reanimateNpc(itemId, 6);
				case REANIMATE_CHAOS_DRUID :
					return reanimateNpc(itemId, 7);
				case REANIMATE_GIANT :
					return reanimateNpc(itemId, 8);
				case REANIMATE_OGRE :
					return reanimateNpc(itemId, 9);
				case REANIMATE_ELF :
					return reanimateNpc(itemId, 10);
				case REANIMATE_TROLL :
					return reanimateNpc(itemId, 11);
				case REANIMATE_KALPHITE :
					return reanimateNpc(itemId, 12);
				case REANIMATE_DAGANNOTH :
					return reanimateNpc(itemId, 13);
				case REANIMATE_BLOODVELD :
					return reanimateNpc(itemId, 14);
				case REANIMATE_TZHAAR :
					return reanimateNpc(itemId, 15);
				case REANIMATE_DEMON :
					return reanimateNpc(itemId, 16);
				case REANIMATE_ABYSSAL :
					return reanimateNpc(itemId, 17);
				case REANIMATE_DRAGON :
					return reanimateNpc(itemId, 18);
				}
				return false;
			}

			@Override
			public void onHit(HitDef hitDef) {
			}
		};
		magicSkill.initialize();
	}

	static int OBELISK_OF_EARTH = 2150;
	static int OBELISK_OF_WATER = 2151;
	static int OBELISK_OF_AIR = 2152;
	static int OBELISK_OF_FIRE = 2153;

	public static boolean spellOnObject(final Player player, final Spell spell, final int objectId, final int x, final int y, final int z) {
		player.tempBoolean = false;

		CacheObject object = ObjectLoader.object(objectId, x, y, z);
		if (object == null || object.getDef().getId() != objectId)
			return false;
		MagicSkill magicSkill = new MagicSkill(player, spell) {
			@Override
			public boolean onExecute() {
				CacheObject object = ObjectLoader.object(objectId, x, y, z);
				if (object == null || object.getDef().getId() != objectId)
					return false;
				switch (spell) {
				}
				if(spell == Spell.CHARGE_WATER || spell == Spell.CHARGE_EARTH || spell == Spell.CHARGE_FIRE || spell == Spell.CHARGE_AIR)
					return true;
				else
					return false;
			}

			@Override
			public void onHit(HitDef hitDef) {
			}
		};
		player.tempMagicSkill = magicSkill;
		switch (spell) {
		case CHARGE_WATER :
			if(objectId == OBELISK_OF_WATER){
				player.setStatedInterface("orb charge");
				Menus.display1Item(player, 571, "Water orb");
				return true;
			} else {
				player.getActionSender().sendMessage("Use this spell on Obelisk of Water!");
				return false;
			}
		case CHARGE_EARTH :
			if(objectId == OBELISK_OF_EARTH){
				player.setStatedInterface("orb charge");
				Menus.display1Item(player, 575, "Earth orb");
				return true;
			} else {
				player.getActionSender().sendMessage("Use this spell on Obelisk of Earth!");
				return false;
			}
		case CHARGE_FIRE :
			if(objectId == OBELISK_OF_FIRE){
				player.setStatedInterface("orb charge");
				Menus.display1Item(player, 569, "Fire orb");
				return true;
			} else {
				player.getActionSender().sendMessage("Use this spell on Obelisk of Fire!");
				return false;
			}
		case CHARGE_AIR :
			if(objectId == OBELISK_OF_AIR){
				player.setStatedInterface("orb charge");
				Menus.display1Item(player, 573, "Air orb");
				return true;
			} else {
				player.getActionSender().sendMessage("Use this spell on Obelisk of Air!");
				return false;
			}
		}
		magicSkill.initialize();
		return player.tempBoolean;
	}

	public static void spellOnPlayer(final Player player, final Player otherPlayer, final Spell spell) {
		if (otherPlayer == null) {
			return;
		}
		player.getUpdateFlags().sendFaceToDirection(otherPlayer.getPosition());
		player.getMovementHandler().reset();
		final MagicSkill magicSkill = new MagicSkill(player, spell) {
			@Override
			public boolean onExecute() {
				switch (spell) {
				case TELEOTHER_CAMELOT :
					return teleOther(player, otherPlayer, TeleotherLocation.CAMELOT);
				case TELEOTHER_LUMBRIDGE :
					return teleOther(player, otherPlayer, TeleotherLocation.LUMBRIDGE);
				case TELEOTHER_FALADOR :
					return teleOther(player, otherPlayer, TeleotherLocation.FALADOR);
				}
				return true;
			}

			@Override
			public void onHit(HitDef hitDef) {
			}
		};
		magicSkill.initialize();

	}

	public static void spellButtonClicked(final Player player, final Spell spell) {
		MagicSkill magicSkill = new MagicSkill(player, spell) {
			@Override
			public boolean onExecute() {
				switch (spell) {

				case BONES_TO_PEACH :
					return applyBonesToFruit(true);
				case BONES_TO_BANANA :
					return applyBonesToFruit(false);
				case CHARGE :
					if (player.getGodChargeDelayTimer().completed()) {
						player.refreshGodChargeEffect();
						player.getCombatDelayTick().setWaitDuration(player.getCombatDelayTick().getWaitDuration() + 2);
					} else {
						player.getActionSender().sendMessage("You cannot use this spell yet!");
						return false;
					}
					break;
                case VARROCK: //forgot to set the exit type
                    player.setExitType(Player.ExitType.TELEPORT);
                    player.getExitType().setTeleportSpell(spell);
                    return player.getTeleportation().attemptTeleport(new Position(Constants.VARROCK_X + Misc.random(1), Constants.VARROCK_Y + Misc.random(1), 0));
                case LUMBRIDGE:
                    player.setExitType(Player.ExitType.TELEPORT);
                    player.getExitType().setTeleportSpell(spell);
                    return player.getTeleportation().attemptTeleport(new Position(Constants.LUMBRIDGE_X + Misc.random(1), Constants.LUMBRIDGE_Y + Misc.random(1), 0));
                case FALADOR:
                    player.setExitType(Player.ExitType.TELEPORT);
                    player.getExitType().setTeleportSpell(spell);
                    return player.getTeleportation().attemptTeleport(new Position(Constants.FALADOR_X + Misc.random(1), Constants.FALADOR_Y + Misc.random(1), 0));
                case CAMELOT:
                    player.setExitType(Player.ExitType.TELEPORT);
                    player.getExitType().setTeleportSpell(spell);
                    return player.getTeleportation().attemptTeleport(new Position(Constants.CAMELOT_X + Misc.random(1), Constants.CAMELOT_Y + Misc.random(1), 0));
                case ARDOUGNE:
                    player.setExitType(Player.ExitType.TELEPORT);
                    player.getExitType().setTeleportSpell(spell);
                    return player.getTeleportation().attemptTeleport(new Position(Constants.ARDOUGNE_X + Misc.random(1), Constants.ARDOUGNE_Y + Misc.random(1), 0));
                case WATCHTOWER:
                    player.setExitType(Player.ExitType.TELEPORT);
                    player.getExitType().setTeleportSpell(spell);
                    return player.getTeleportation().attemptTeleport(new Position(Constants.WATCH_TOWER_X + Misc.random(1), Constants.WATCH_TOWER_Y + Misc.random(1), 0));
				case TROLLHEIM :
					if(player.getStaffRights() < 2){
						player.getActionSender().sendMessage("This spell has been temporarily disabled!");
						return false;
					}
					return player.getTeleportation().attemptTeleport(new Position(Constants.TROLLHEIM_X + Misc.random(1), Constants.TROLLHEIM_Y + Misc.random(1), 0));
				case APE_ATOLL :
					if(player.getStaffRights() < 2){
						player.getActionSender().sendMessage("This spell has been temporarily disabled!");
						return false;
					}
					return player.getTeleportation().attemptTeleport(new Position(Constants.APE_ATOLL_X + Misc.random(1), Constants.APE_ATOLL_Y + Misc.random(1), 0));
                case PADDEWWA:
                    player.setExitType(Player.ExitType.TELEPORT);
                    player.getExitType().setTeleportSpell(spell);
                    return player.getTeleportation().attemptTeleport(new Position(Constants.PADDEWWA_X + Misc.random(1), Constants.PADDEWWA_Y + Misc.random(1), 0));
                case SENNTISTEN:
                    player.setExitType(Player.ExitType.TELEPORT);
                    player.getExitType().setTeleportSpell(spell);
                    return player.getTeleportation().attemptTeleport(new Position(Constants.SENNTISTEN_X + Misc.random(1), Constants.SENNTISTEN_Y + Misc.random(1), 0));
                case CARRALLANGAR:
                    player.setExitType(Player.ExitType.TELEPORT);
                    player.getExitType().setTeleportSpell(spell);
                    return player.getTeleportation().attemptTeleport(new Position(Constants.CARRALLANGAR_X + Misc.random(1), Constants.CARRALLANGAR_Y + Misc.random(1), 0));
                case KHARYRLL:
                    player.setExitType(Player.ExitType.TELEPORT);
                    player.getExitType().setTeleportSpell(spell);
                    return player.getTeleportation().attemptTeleport(new Position(Constants.KHARYRLL_X + Misc.random(1), Constants.KHARYRLL_Y + Misc.random(1), 0));
                case LASSAR:
                    player.setExitType(Player.ExitType.TELEPORT);
                    player.getExitType().setTeleportSpell(spell);
                    return player.getTeleportation().attemptTeleport(new Position(Constants.LASSAR_X + Misc.random(1), Constants.LASSAR_Y + Misc.random(1), 0));
                case DAREEYAK:
                    player.setExitType(Player.ExitType.TELEPORT);
                    player.getExitType().setTeleportSpell(spell);
                    return player.getTeleportation().attemptTeleport(new Position(Constants.DAREEYAK_X + Misc.random(1), Constants.DAREEYAK_Y + Misc.random(1), 0));
                case ANNAKARL:
                    player.setExitType(Player.ExitType.TELEPORT);
                    player.getExitType().setTeleportSpell(spell);
                    return player.getTeleportation().attemptTeleport(new Position(Constants.ANNAKARL_X + Misc.random(1), Constants.ANNAKARL_Y + Misc.random(1), 0));
                case GHORROCK:
                    player.setExitType(Player.ExitType.TELEPORT);
                    player.getExitType().setTeleportSpell(spell);
                    return player.getTeleportation().attemptTeleport(new Position(Constants.GHORROCK_X + Misc.random(1), Constants.GHORROCK_Y + Misc.random(1), 0));}
				return true;
			}
			@Override
			public void onHit(HitDef hitDef) {
			}
		};
		magicSkill.initialize();
	}

	public static void spellOnGroundItem(final Player player, final Spell spell, final int itemId, final Position itemPos) {
		final int task = player.getTask();
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task)) {
					container.stop();
					return;
				}
				// Position check
				switch (spell) {
				case TELEGRAB :
					if(itemId == 1583){
						player.getActionSender().sendMessage("I can't use Telekinetic Grab on this object.");
						container.stop();
						return;
					}
					if(itemId >= 5509 && itemId <= 5515){//pouches
						if(player.hasItem(itemId)){
							player.getActionSender().sendMessage("I already have that pouch!");
							container.stop();
							return;
						}
					}
					if(!player.getTelekineticTheatre().isInTelekineticTheatre()){
						if (!Misc.checkClip(player.getPosition(), itemPos, false) || !Misc.goodDistance(player.getPosition(), itemPos, 10)) {
							return;
						}
					}
					if (player.getPosition().equals(itemPos)) {
						player.getFollowing().stepAway();
						return;
					}
					break;
				default://stops using other spells on ground items
					return;
				}
				doSpellOnGroundItem(player, spell, itemId, itemPos);
				container.stop();
			}
			@Override
			public void stop() {
			}
		}, 1);
	}

	public static void doSpellOnGroundItem(final Player player, final Spell spell, final int itemId, final Position itemPos) {
		final GroundItem groundItem = GroundItemManager.getManager().findItem(player,  itemId, itemPos);
		final MagicSkill magicSkill = new MagicSkill(player, spell) {
			@Override
			public boolean onExecute() {
				if (groundItem == null)
					return false;
				switch (spell) {
				case TELEGRAB :
					player.getUpdateFlags().sendFaceToDirection(itemPos);
					player.getMovementHandler().reset();
					castProjectile(null, itemPos);
					break;
				}
				return true;
			}

			@Override
			public void onHit(HitDef hitDef) {
				switch (spell) {
				case TELEGRAB :
					if (!GroundItemManager.getManager().itemExists(player, groundItem)) {
						hitDef.setHitGraphic(null);
						return;
					} else {
						if(!GroundItemManager.getManager().destroyItem(groundItem, player)){
							System.out.println("Bugged ground item!? "+groundItem.getItem().getId()+" "+player.getUsername());
							player.getActionSender().sendMessage("That item does not seem to exist anymore.");
							player.getActionSender().removeGroundItem(groundItem);
						}else{
							if(!player.getTelekineticTheatre().isInTelekineticTheatre()){
								player.getInventory().addItem(new Item(groundItem.getItem().getId(), groundItem.getItem().getCount()));
							}else{
								if(itemId == 6888){//statue
									String s = player.getTelekineticTheatre().getSidePlace(player.getTelekineticTheatre().mazeIndex);
									int xO = 0;
									int yO = 0;
									if(s == "right")
										xO += 20;
									else if(s == "left")
										xO -= 20;
									else if(s == "bottom")
										yO -= 20;
									else if(s == "upper")
										yO += 20;
									Position newPos = StraightPathFinder.findPath(itemPos, new Position(itemPos.getX()+xO, itemPos.getY()+yO, itemPos.getZ()), true);
									if(!player.getTelekineticTheatre().checkSolved(newPos)){
										//GroundItem item = new GroundItem(new Item(6888, 1), newPos, false);
										GroundItem item = new GroundItem(new Item(6888, 1), player, newPos);
										GroundItemManager.getManager().dropItem(item);
									} else {
										player.getTelekineticTheatre().solved();
									}
								}
							}
						}
					}
					break;
				}
			}
		};
		magicSkill.initialize();
	}
	/**
	 * The buttons associated with anything relating to this class.
	 */
	public static boolean clickingToAutoCast(Player player, int buttonId) {
		if(player.getMagicBookType() == SpellBook.NECROMANCY)
			return false;
		if (SpellBook.getAutoSpell(player, buttonId) != null && buttonId != 349) {
			player.setAutoSpell(SpellBook.getAutoSpell(player, buttonId));
			return true;
		}
		switch (buttonId) {
		case 349 :
			if (player.isAutoCasting()) {
				return true;
			}
			if (player.getAutoSpell() != null) {
				player.setAutoCasting(!player.isAutoCasting());
			} else {
				player.getActionSender().sendMessage("You haven't selected a spell to autocast!");
			}
			return true;
		case 353 :
			Item weapon = player.getEquipment().getItemContainer().get(Constants.WEAPON);
			if (player.getMagicBookType() == SpellBook.ANCIENT) {
				if (weapon.getId() == 4675) {
					player.getActionSender().sendSidebarInterface(0, 1689);
				} else {
					player.getActionSender().sendMessage("You can't autocast ancient magic without an ancient staff!");
				}
			} else {
				if (weapon.getId() == 4170) {
					player.getActionSender().sendSidebarInterface(0, 12050);
				} else {
					player.getActionSender().sendSidebarInterface(0, 1829);
				}
			}
			return true;
		case 2004 :
		case 6161 :
		case 12101 :
			player.getActionSender().sendSidebarInterface(0, 328);
			return true;
		}
		return false;
	}

	public boolean superHeatItem(int itemID) {
		if (!player.getSkill().canDoAction(1200)) {
			return false;
		}
		for (int smelt[] : SMELT) {
			if (itemID == smelt[0]) {
				if (!player.getInventory().playerHasItem(smelt[2], smelt[3])) {
					if (itemID == 440 && smelt[2] == 453) {
						continue;
					} else if (smelt[2] > 0) {
						player.getActionSender().sendMessage("You haven't got enough " + ItemManager.getInstance().getItemName(smelt[2]).toLowerCase() + " to cast this spell!");
						player.getActionSender().sendSound(218, 1, 0);
						return false;
					}
				}
				if (player.getSkill().getPlayerLevel(Skill.SMITHING) < smelt[6]) {
					player.getActionSender().sendMessage("You need a smithing level of " + smelt[6] + " to superheat this ore.");
					player.getActionSender().sendSound(218, 1, 0);
					return false;
				}
				player.getInventory().removeItem(new Item(itemID, 1));
				if (smelt[2] > 0) {
					player.getInventory().removeItem(new Item(smelt[2], smelt[3]));
				}
				player.getInventory().addItem(new Item(smelt[4], 1));
				if(smelt[4] == 2357 && player.getEquipment().getId(Constants.HANDS) == 776)
					player.getSkill().addExp(Skill.SMITHING, 56.2);
				else
					player.getSkill().addExp(Skill.SMITHING, smelt[5]);
				player.getActionSender().sendFrame106(6);
				return true;
			}
		}
		player.getActionSender().sendMessage("You can only cast superheat item on ores!");
		player.getActionSender().sendSound(218, 1, 0);
		return false;
	}

	public boolean reanimateNpc(int item, final int spellId) {
		if (!player.getSkill().canDoAction(1200)) {
			return false;
		}
		if(item != REANIMATE[spellId][0]){
			player.getActionSender().sendMessage("You cannot use this spell with this item.");
			return false;
		}
		if (player.getSpawnedNpc() != null){
			if(!player.getSpawnedNpc().isDead()){
				player.getActionSender().sendMessage("You cannot do that right now!");
				return false;
			}
		}

		/*if (player.canMove(-1, 0)) {
			player.getActionSender().walkTo(-1, 0, false);
		} else {
			player.getActionSender().walkTo(1, 0, false);
        }*/
		player.setStopPacket(true);

		final Tick timer = new Tick(2) {
			@Override
			public void execute() {
				player.getInventory().removeItem(new Item(REANIMATE[spellId][0], 1));
				Npc npc = new Npc(REANIMATE[spellId][1]);
				NpcLoader.spawnNpc(player, npc, true, false);
				npc.getUpdateFlags().sendHighGraphic(110);
				//player.getActionSender().sendFrame106(6);
				if(player.getEquipment().getId(Constants.SHIELD) != 8118){//book of the dead
					player.setMagicBookType(player.tempMagicBookType);
					if(player.getMagicBookType() == SpellBook.MODERN)
						player.getActionSender().sendSidebarInterface(6, 1151);
					if(player.getMagicBookType() == SpellBook.ANCIENT)
						player.getActionSender().sendSidebarInterface(6, 12855);
				}
				player.setStopPacket(false);
				stop();
			}
		};
		World.getTickManager().submit(timer);

		return true;
	}

	public boolean enchantJewelry(int item, int spellId) {
		if (!player.getSkill().canDoAction(1200)) {
			return false;
		}
		if (!player.getEnchantingChamber().isInEnchantingChamber()) {
			int index = -1;
			if (item == ENCHANT[spellId][0]) {
				index = 0;
			} else if (item == ENCHANT[spellId][1]) {
				index = 1;
			} else if (item == ENCHANT[spellId][2]) {
				index = 2;
			} else {
				player.getActionSender().sendMessage("You cannot enchant this item with this spell.");
				return false;
			}
			player.getInventory().removeItem(new Item(ENCHANT[spellId][index], 1));
			player.getInventory().addItem(new Item(ENCHANT[spellId][index + 3], 1));
			player.getActionSender().sendMessage("You enchant the " + ItemManager.getInstance().getItemName(ENCHANT[spellId][index]) + ".");
		} else {
			if(!player.getEnchantingChamber().isEnchantItem(spellId, item)){
				player.getActionSender().sendMessage("You cannot enchant this item with this spell.");
				return false;
			} else {
				player.getEnchantingChamber().enchantItem(spellId, item);
			}
		}
		player.getActionSender().sendFrame106(6);
		return true;
	}


	public boolean applyBonesToFruit(boolean peaches) {
		if (!player.getSkill().canDoAction(1200)) {
			return false;
		}
		if(peaches && !player.bonesToPeachUnlocked){
			player.getActionSender().sendMessage("You need to unlock this spell at Mage Training Arena!");
			return false;
		}
		if (!player.getCreatureGraveyard().isInCreatureGraveyard()) {
			int fruit = peaches ? PEACH : BANANA;
			boolean hasBones = false;
			for (Item item : player.getInventory().getItemContainer().getItems()) {
				if (item != null && (item.getId() == 526 || item.getId() == 528 || item.getId() == 530 || item.getId() == 532 || item.getId() == 2859 || item.getId() == 3179)) {
					player.getInventory().removeItem(item);
					player.getInventory().addItem(new Item(fruit));
					hasBones = true;
				}
			}
			if (!hasBones) {
				player.getActionSender().sendMessage("You don't have any bones to convert into fruits.");
				return false;
			}
		}else{
			if(!player.getCreatureGraveyard().applyBonesToFruit(peaches)){
				return false;
			}
		}
		return true;
	}

	public boolean teleOther(Player player, Player otherPlayer, TeleotherLocation location) {
		if (!player.getSkill().canDoAction(1200)) {
			return false;
		}
		if (otherPlayer.getInterface() > 0) {
			player.getActionSender().sendMessage("This player is busy.");
			return false;
		}
		if (!otherPlayer.isAcceptingAid()) {
			player.getActionSender().sendMessage("This player is not accepting aid.");
			return false;
		}
		if (otherPlayer.cantTeleport()) {
			player.getActionSender().sendMessage("You cannot use this spell here.");
			return false;
		}
		//		if (otherPlayer.inWild() && otherPlayer.getWildernessLevel() > 20) {
		//			player.getActionSender().sendMessage("You cannot use this spell here.");
		//			return false;
		//		}
		otherPlayer.setTeleotherPosition(location.getPosition());
		otherPlayer.getActionSender().sendString(player.getUsername(), 12558);
		otherPlayer.getActionSender().sendString(location.getName(), 12560);
		otherPlayer.getActionSender().sendInterface(12468);
		return true;
	}

	public enum TeleotherLocation {
		CAMELOT(new Position(Constants.CAMELOT_X, Constants.CAMELOT_Y), "Camelot"),
		FALADOR(new Position(Constants.FALADOR_X, Constants.FALADOR_Y), "Falador"),
		LUMBRIDGE(new Position(Constants.LUMBRIDGE_X, Constants.LUMBRIDGE_Y), "Lumbridge");

		Position position;
		String name;

		TeleotherLocation(Position position, String name) {
			this.position = position;
			this.name = name;
		}

		public Position getPosition() {
			return position;
		}

		public String getName() {
			return name;
		}
	}

	public static boolean alchItem(Player player, int itemId, int slot, int price, int timer, Spell spell) {
		if (!player.getSkill().canDoAction(timer)) {
			return false;
		}
		if (player.getAlchemistPlayground().isInAlchemistPlayGround() && !player.getAlchemistPlayground().isAlchemyItem(itemId)) {
			player.getActionSender().sendMessage("You can't alch this item here.");
			return false;
		}
		if ((new Item(itemId).getDefinition().isUntradable() && !player.getAlchemistPlayground().isAlchemyItem(itemId)) || itemId == 995) {
			player.getActionSender().sendMessage("You cannot alch this item.");
			return false;
		}
		if (player.getInventory().getItemContainer().get(slot).getCount() > 1 && !player.getInventory().getItemContainer().hasRoomFor(new Item(995))) {
			player.getActionSender().sendMessage("Not enough space in your inventory.");
			return false;
		}
		for(Item rune : spell.getRunesRequired()){
			if(rune.getId() == itemId){
				int invAmount = player.getInventory().getItemAmount(itemId);
				if(invAmount-1 < rune.getCount()){
					player.getActionSender().sendMessage("You don't have enough runes to do this.");
					return false;
				}
			}
		}
		player.getActionSender().stopPlayerPacket(2);
		player.getActionSender().sendFrame106(6);
		if(!player.getAlchemistPlayground().isInAlchemistPlayGround()){
			player.getInventory().removeItem(new Item(itemId, 1));
			if (price > 0) {
				player.getInventory().addItem(new Item(995, price));
			}
		}else{
			player.getAlchemistPlayground().alchemyItem(itemId);
		}
		return true;
	}
}
