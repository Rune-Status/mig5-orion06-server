package com.rs2.model.players;

import com.rs2.Constants;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.GameObjectData;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.BarrowsItem;
import com.rs2.model.content.Following;
import com.rs2.model.content.Pets;
import com.rs2.model.content.Shops;
import com.rs2.model.content.combat.hit.HitType;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.content.dungeons.Abyss;
import com.rs2.model.content.minigames.barrows.Barrows;
import com.rs2.model.content.minigames.duelarena.GlobalDuelRecorder;
import com.rs2.model.content.minigames.magetrainingarena.RewardShop;
import com.rs2.model.content.questing.QuestDefinition;
import com.rs2.model.content.skills.Menus;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.SkillHandler;
import com.rs2.model.content.skills.crafting.GemCrafting;
import com.rs2.model.content.skills.crafting.GlassMaking;
import com.rs2.model.content.skills.crafting.PotteryMaking;
import com.rs2.model.content.skills.crafting.SilverCrafting;
import com.rs2.model.content.skills.crafting.Tanning;
import com.rs2.model.content.skills.agility.Agility;
import com.rs2.model.content.skills.agility.CrossObstacle;
import com.rs2.model.content.skills.farming.Farming;
import com.rs2.model.content.skills.fishing.Fishing;
import com.rs2.model.content.skills.magic.MagicSkill;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.content.skills.magic.SpellBook;
import com.rs2.model.content.skills.mining.MineEssence;
import com.rs2.model.content.skills.mining.MineOre;
import com.rs2.model.content.skills.prayer.Prayer;
import com.rs2.model.content.skills.runecrafting.MixingRunes;
import com.rs2.model.content.skills.runecrafting.RunecraftAltars;
import com.rs2.model.content.skills.runecrafting.Runecrafting;
import com.rs2.model.content.skills.runecrafting.Tiaras;
import com.rs2.model.content.skills.smithing.Smelting;
import com.rs2.model.content.skills.smithing.SmithBars;
import com.rs2.model.content.skills.thieving.ThieveNpcs;
import com.rs2.model.content.skills.thieving.ThieveOther;
import com.rs2.model.content.skills.thieving.ThieveStalls;
import com.rs2.model.content.skills.woodcutting.ChopTree;
import com.rs2.model.content.skills.woodcutting.ChopTree.Tree;
import com.rs2.model.transport.Canoe;
import com.rs2.model.transport.Canoe.CanoeTree;
import com.rs2.model.content.treasuretrails.AnagramsScrolls;
import com.rs2.model.content.treasuretrails.MapScrolls;
import com.rs2.model.content.treasuretrails.SearchScrolls;
import com.rs2.model.content.treasuretrails.SpeakToScrolls;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcActions;
import com.rs2.model.objects.GameObject;
import com.rs2.model.objects.GameObjectDef;
import com.rs2.model.objects.functions.AxeInLog;
import com.rs2.model.objects.functions.CoalTruck;
import com.rs2.model.objects.functions.CrystalChest;
import com.rs2.model.objects.functions.Doors;
import com.rs2.model.objects.functions.DoubleDoors;
import com.rs2.model.objects.functions.FlourMill;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.objects.functions.MilkCow;
import com.rs2.model.objects.functions.MuddyChest;
import com.rs2.model.objects.functions.ObeliskTick;
import com.rs2.model.objects.functions.PickableObjects;
import com.rs2.model.objects.functions.TrapDoor;
import com.rs2.model.objects.functions.Webs;
import com.rs2.model.players.container.inventory.Inventory;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.model.tick.Tick;
import com.rs2.util.Misc;
import com.rs2.util.clip.Rangable;

public class WalkToActionHandler {

	private static Actions actions = Actions.OBJECT_FIRST_CLICK;

	public static void doActions(Player player) {
		switch (actions) {
		case OBJECT_FIRST_CLICK:
			doObjectFirstClick(player);
			break;
		case OBJECT_SECOND_CLICK:
			doObjectSecondClick(player);
			break;
		case OBJECT_THIRD_CLICK:
			doObjectThirdClick(player);
			break;
		case OBJECT_FOURTH_CLICK:
			doObjectFourthClick(player);
			break;
		case NPC_FIRST_CLICK:
			doNpcFirstClick(player);
			break;
		case NPC_SECOND_CLICK:
			doNpcSecondClick(player);
			break;
		case NPC_THIRD_CLICK:
			doNpcThirdClick(player);
			break;
		case NPC_FOURTH_CLICK:
			doNpcFourthClick(player);
			break;
		case ITEM_ON_OBJECT:
			doItemOnObject(player);
			break;
		case ITEM_ON_NPC:
			doItemOnNpc(player);
			break;
		case SPELL_ON_OBJECT:
			doSpellOnObject(player);
			break;
		}
	}

	public static void doObjectFirstClick(final Player player) {
		final int id = player.getClickId();
		final int x = player.getClickX();
		final int y = player.getClickY();
		final int z = player.getClickZ()%4;
		final String objectName = GameObjectData.forId(id) != null ? GameObjectData.forId(id).getName().toLowerCase() : "";
		final int task = player.getTask();
		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task)) {
					this.stop();
					return;
				}
				if (player.isMoving() || player.isStunned()) {
					return;
				}
				GameObjectDef def = SkillHandler.getObject(id, x, y, z);
				if (def == null) { // Server.npcHandler.getNpcByLoc(Location.create(x,
					/*if ((id == 3203 || id == 4616 || id == 4615) || (id == 2213 && x == 3513) || (id == 356 && y == 3507) || (id == 2593 && x == 3047 && y == 3205) || (id == 2295 && x == 2474 && y == 3435) || (id == 2294 && x == 2550 && y == 3546) || (id == 356 && x == 3509 && y == 3497) || (id == 356 && x == 2905 && y == 3189) || (id == 2296 && x == 2599 && y == 3477) || (id == 2296 && x == 2602 && y == 3477)) { //exceptions
						def = new GameObjectDef(id, 10, 0, new Position(x, y, z));
					} else {*/
						return;
					//}
				}
				GameObjectData object = GameObjectData.forId(player.getClickId());
				Position objectPosition;
				objectPosition = Misc.goodDistanceObject(def.getPosition().getX(), def.getPosition().getY(), player.getPosition().getX(), player.getPosition().getY(), object.getSizeX(def.getFace()), object.getSizeY(def.getFace()), z);
				if (objectPosition == null)
					return;
				if (!canInteractWithObject(player, objectPosition, def)) {
					stop();
					return;
				}
				if (id == 4031){
					if(player.getPosition().getY() > y){
						if(player.getInventory().playerHasItem(1854)){
							player.getActionSender().sendMessage("You go through the gate.");
							player.getInventory().removeItem(new Item(1854, 1));
							player.getActionSender().walkTo(0, -2, true);
						} else {
							player.getActionSender().sendMessage("You need a shantay pass to go through.");
						}
					} else
						player.getActionSender().walkTo(0, 2, true);
					this.stop();
					return;
				}
				Position loc = new Position(player.getClickX(), player.getClickY(), z);
				if (object != null)
					player.getUpdateFlags().sendFaceToDirection(loc.getActualLocation(object.getBiggestSize()));
				if (Barrows.barrowsObject(player, id)) {
					this.stop();
					return;
				}
				if (!Misc.goodDistance(player.getPosition().getX(), player.getPosition().getY(), x, y, 7))
					return;
				if (player.getQuestHandler().handleFirstClickObject(id, x, y)) {
					this.stop();
					return;
				}
				if (Barrows.barrowsObject2(player, id, x, y)) {
					this.stop();
					return;
				}
				if (Dialogues.startDialogueObject(1, player, player.getClickId(), x, y)) {
					this.stop();
					return;
				}
				if (player.getSlayer().handleObjects(id, x, y)) {
					this.stop();
					return;
				}
				if (ObeliskTick.clickObelisk(id)) {
					this.stop();
					return;
				}
				if(Agility.handleObstacle(player, id, x, y)){
					this.stop();
					return;
				}
				if (Abyss.handleObstacle(player, id, x, y)) {
					this.stop();
					return;
				}
				if (RunecraftAltars.runecraftAltar(player, id) || RunecraftAltars.clickRuin(player, id)) {
					this.stop();
					return;
				}
				if ((object.getName().toLowerCase().contains("altar") || (id >= 10638 && id <= 10640)) && id != 2640 && id != 6552) {
					Prayer.rechargePrayer(player);
					this.stop();
					return;
				}
				if (player.getAlchemistPlayground().handleObjectClicking(id, x, y)) {
					this.stop();
					return;
				}
				if (player.getEnchantingChamber().handleObjectClicking(id)) {
					this.stop();
					return;
				}
				if (player.getCreatureGraveyard().handleObjectClicking(id, x, y, z)) {
					this.stop();
					return;
				}
				if (player.getTelekineticTheatre().handleObjectClicking(id, x, y)) {
					this.stop();
					return;
				}
				if (id == 10093) {
					Menus.sendSkillMenu(player, "dairyChurn");
					this.stop();
					return;
				}
				if(id == 190){//gnome gate
					final CacheObject g = ObjectLoader.object(id, x, y, z);
					ObjectHandler.getInstance().addObject(new GameObject(191, x, y, z, g.getRotation(), g.getType(), id, 4), true);
					ObjectHandler.getInstance().addObject(new GameObject(192, x+3, y, z, g.getRotation()-2, g.getType(), Constants.EMPTY_OBJECT, 4), true);
					player.getActionSender().sendSound(318, 1, 0);
					player.getActionSender().walkToWithTime(0, (player.getPosition().getY() < 3385 ? 3 : -3), 2, true);
					this.stop();
					return;
				}
				if(id == 4577 && x == 2509 && y == 3636){//lighthouse door
					player.getActionSender().walkTo(0, (player.getPosition().getY() < 3636 ? 1 : -1), true);
					player.getActionSender().sendSound(318, 1, 0);
					this.stop();
					return;
				}

				if((id == 2624 && x == 2902 && y == 3510) || (id == 2625 && x == 2902 && y == 3511)){//heroes guild door
					if (player.questStage[50] == 1) {
						player.getActionSender().walkTo((player.getPosition().getX() < 2902 ? 1 : -1), 0, true);
						player.getActionSender().walkThroughDoubleDoor(2624, 2625, 2902, 3510, 2902, 3511, 0);
					}else{
						QuestDefinition questDef = QuestDefinition.forId(50);
						String questName = questDef.getName();
						player.getDialogue().sendStatement("You need to complete "+questName+" to enter the guild.");
					}
					this.stop();
					return;
				}
				
				if((id == 2391 && x == 2728 && y == 3349) || (id == 2392 && x == 2729 && y == 3349)){//legends guild gate
					if (player.questStage[57] >= 1) {
						player.getActionSender().walkTo(0, (player.getPosition().getY() < 3350 ? 1 : -1), true);
						player.getActionSender().walkThroughDoubleDoor(2391, 2392, 2728, 3349, 2729, 3349, 0);
					}else{
						QuestDefinition questDef = QuestDefinition.forId(57);
						String questName = questDef.getName();
						player.getDialogue().sendStatement("You need to start "+questName+" to go through.");
					}
					this.stop();
					return;
				}
			
			if (id == 2514){ // ranged guild
				if (SkillHandler.hasRequiredLevel(player, Skill.RANGED, 40, "enter the Ranging Guild")) {
					boolean down = false;
					if(player.getPosition().getX() <= 2658 && player.getPosition().getY() >= 3438)
						down = true;
					player.getActionSender().walkTo(down ? 1 : -1, down ? -1 : 1, true);
					player.getActionSender().walkThroughDoorDiagonal(id, x, y, z);
				}
				this.stop();
				return;
			}
			
			if (id == 2025){ // fishing guild
				if (SkillHandler.hasRequiredLevel(player, Skill.FISHING, 68, "enter the Fishing Guild")) {
					player.getActionSender().walkTo(0, player.getPosition().getY() > 3393 ? -1 : 1, true);
					player.getActionSender().walkThroughDoor(id, x, y, z);
				}
				this.stop();
				return;
			}
			
			if((id == 1600 && x == 2597 && y == 3087) || (id == 1601 && x == 2597 && y == 3088)){//mage guild doors E
				if (SkillHandler.hasRequiredLevel(player, Skill.MAGIC, 66, "enter the Magic Guild")) {
					player.getActionSender().walkTo((player.getPosition().getX() < 2597 ? 1 : -1), 0, true);
					player.getActionSender().walkThroughDoubleDoor(1600, 1601, 2597, 3087, 2597, 3088, 0);
				}
				this.stop();
				return;
			}
			
			if((id == 1601 && x == 2584 && y == 3087) || (id == 1600 && x == 2584 && y == 3088)){//mage guild doors W
				if (SkillHandler.hasRequiredLevel(player, Skill.MAGIC, 66, "enter the Magic Guild")) {
					player.getActionSender().walkTo((player.getPosition().getX() < 2585 ? 1 : -1), 0, true);
					player.getActionSender().walkThroughDoubleDoor(1600, 1601, 2584, 3088, 2584, 3087, 0);
				}
				this.stop();
				return;
			}
				
			if (id == 2712){ // Cooking guild door
				if (SkillHandler.hasRequiredLevel(player, Skill.COOKING, 32, "enter the Cooks' Guild")) {
					if(player.getEquipment().getItemContainer().get(Constants.HAT) == null){
						if (player.getPosition().getY() > 3443) {
							player.getActionSender().walkThroughDoor(id, x, y, z);
							player.getActionSender().walkTo(0, -1, true);
						}else{
							player.getDialogue().sendStatement("You need to wear chef's hat to", "enter the guild.");
							player.setClickId(0);
						}
					}else{
						if (player.getPosition().getY() > 3443) {
							player.getActionSender().walkThroughDoor(id, x, y, z);
							player.getActionSender().walkTo(0, -1, true);
						}else{
							if(player.getEquipment().getItemContainer().get(Constants.HAT).getId() == 1949){
								player.getActionSender().walkThroughDoor(id, x, y, z);
								player.getActionSender().walkTo(0, 1, true);
							}else{
								player.getDialogue().sendStatement("You need to wear chef's hat to", "enter the guild.");
								player.setClickId(0);
							}
						}
					}
				}
				this.stop();
				return;
			}
			if (id == 2647){ // Crafting guild door
				if (SkillHandler.hasRequiredLevel(player, Skill.CRAFTING, 40, "enter the Crafting Guild")) {
					if(player.getEquipment().getItemContainer().get(Constants.CHEST) == null){
						if (player.getPosition().getY() < 3289) {
							player.getActionSender().walkThroughDoor(id, x, y, z);
							player.getActionSender().walkTo(0, 1, true);
						}else{
							player.getDialogue().sendStatement("You need to wear brown apron to", "enter the guild.");
							player.setClickId(0);
						}
					}else{
						if (player.getPosition().getY() < 3289) {
							player.getActionSender().walkThroughDoor(id, x, y, z);
							player.getActionSender().walkTo(0, 1, true);
						}else{
							if(player.getEquipment().getItemContainer().get(Constants.CHEST).getId() == 1757){
								player.getActionSender().walkThroughDoor(id, x, y, z);
								player.getActionSender().walkTo(0, -1, true);
							}else{
								player.getDialogue().sendStatement("You need to wear brown apron to", "enter the guild.");
								player.setClickId(0);
							}
						}
					}
				}
				this.stop();
				return;
			}
				if (id == 2551 || id == 2550 || id == 2555 || id == 2556 || id == 2558 || id == 2557) {
					player.getActionSender().sendMessage("This door is locked.");
					this.stop();
					return;
				}
				if (id == 2566) {
					player.getActionSender().sendMessage("This chest is locked.");
					this.stop();
					return;
				}
				if (MapScrolls.handleCrate(player, x, y)) {
					this.stop();
					return;
				}
				if (SearchScrolls.handleObject(player, def)) {
					this.stop();
					return;
				}
				if (Farming.harvest(player, x, y)) {
					this.stop();
					return;
				}
				if (CanoeTree.getTree(id) != null) {
					Canoe.handle(player, id, x, y);
					this.stop();
					return;
				}
				if (Tree.getTree(id) != null) {
					ChopTree.handle(player, id, x, y, false);
					this.stop();
					return;
				}
				if (MineOre.miningRocks(id)) {
					if (player.getMining().canMine(id)) {
						player.getMining().startMining(id, x, y);
					}
					this.stop();
					return;
				}

				if (player.getCompost().handleObjectClick(id, x, y)) {
					this.stop();
					return;
				}
				
				if(id == 10721){ //mage training area doorway
					player.getActionSender().walkTo(0, player.getPosition().getY() < 3300 ? 2 : -2, true);
					this.stop();
					return;
				}

				if (DoubleDoors.handleDoubleDoor(id, x, y, z)) {
					player.getActionSender().sendSound(318, 1, 0);
					this.stop();
					return;
				}
				if (player.questStage[0] == 1) {
					if (Doors.handleDoor(player, id, x, y, z)) {
						this.stop();
						return;
					}
				}
				if (objectName.contains("hay")) {
					player.getActionSender().sendMessage("You search the hay bales...");
					player.getUpdateFlags().sendAnimation(832);
					player.setStopPacket(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							if (Misc.random(99) == 0) {
								if (Misc.random(1) == 0) {
									player.getDialogue().sendPlayerChat("Wow! A needle!", "Now what are the chances of finding that?", Dialogues.HAPPY);
									player.getDialogue().endDialogue();
									player.getInventory().addItem(new Item(1733));
								} else {
									player.hit(1, HitType.NORMAL);
									player.getDialogue().sendPlayerChat("Ow! There's something sharp in there!", Dialogues.SAD);
									player.getDialogue().endDialogue();
								}
							} else {
								player.getActionSender().sendMessage("You find nothing of interest.");
							}
							b.stop();
						}
						@Override
						public void stop() {
							player.setStopPacket(false);
						}
					}, 2);
					this.stop();
					return;
				}
				if (objectName.contains("crate")) {
					player.getActionSender().sendMessage("You search the crate...");
					player.getUpdateFlags().sendAnimation(832);
					player.setStopPacket(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							if (Misc.random(99) == 0) {
								Item[] rewards = {new Item(995, 10), new Item(686), new Item(687), new Item(689), new Item(690), new Item(697), new Item(1059), new Item(1061)};
								Item reward = rewards[Misc.randomMinusOne(rewards.length)];
								player.getInventory().addItem(reward);
								player.getActionSender().sendMessage("You find some "+reward.getDefinition().getName().toLowerCase()+"!");
							} else {
								player.getActionSender().sendMessage("You find nothing of interest.");
							}
							b.stop();
						}
						@Override
						public void stop() {
							player.setStopPacket(false);
						}
					}, 2);
					this.stop();
					return;
				}
				if (objectName.contains("bookcase")) {
					player.getActionSender().sendMessage("You search the books...");
					player.setStopPacket(true);
					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer b) {
							player.getActionSender().sendMessage("None of them look very interesting.");
							b.stop();
						}
						@Override
						public void stop() {
							player.setStopPacket(false);
						}
					}, 2);
					this.stop();
					return;
				}
				switch (id) {
                case 6:
                    player.getDwarfMultiCannon().addAmmo(x, y);
                    this.stop();
                    return;
				case 2114 : // coal truck
					CoalTruck.withdrawCoal(player);
					break;
				case 2693 : // shantay bank chest
				case 2995 : // etceteria bank chest
				case 4483 : // castle wars bank chest
					BankManager.openBank(player);
					break;
				case 3045: //tutorial bank	
					Dialogues.startDialogue(player, 953);
					break;
				//banana trees
				case 2073:
				case 2074:
				case 2075:
				case 2076:
				case 2077:
					player.getActionSender().sendMessage("You pick a banana.");
					player.getInventory().addItemOrDrop(new Item(1963, 1));
					ObjectHandler.getInstance().removeObject(x, y, z, 0);
					new GameObject(def.getId()+1, x, y, z, def.getFace(), def.getType(), def.getId(), 10);
					//ObjectHandler.getInstance().addObject(new GameObject(2145, 3249, 3192, 0, 0, 10, 2145, 999999999), true);
					break;
				//kharidian cactus
				case 2670:
					if(player.getInventory().playerHasItem(946)){
						int item = 0;
						if(player.getInventory().playerHasItem(1825))
							item = 1825;
						else if(player.getInventory().playerHasItem(1827))
							item = 1827;
						else if(player.getInventory().playerHasItem(1829))
							item = 1829;
						else if(player.getInventory().playerHasItem(1831))
							item = 1831;
						if(item != 0){
							player.getActionSender().sendMessage("You fill your waterskin with a dose of water.");
							player.getSkill().addExp(Skill.WOODCUTTING, 10);
							player.getInventory().removeItem(new Item(item, 1));
							player.getInventory().addItemOrDrop(new Item(item-2, 1));
							ObjectHandler.getInstance().removeObject(x, y, z, 0);
							new GameObject(def.getId()+1, x, y, z, def.getFace(), def.getType(), def.getId(), 100);
						} else {
							player.getActionSender().sendMessage("You don't have any waterskins to fill.");
						}
					} else {
						player.getActionSender().sendMessage("You need a knife to do this.");
					}
					break;	
				case 2078:
					player.getActionSender().sendMessage("There are no bananas left on the tree.");
					break;
				//pineapple plant
				case 1408:
				case 1409:
				case 1410:
				case 1411:
				case 1412:
					player.getActionSender().sendMessage("You pick a pineapple.");
					player.getInventory().addItemOrDrop(new Item(2114, 1));
					ObjectHandler.getInstance().removeObject(x, y, z, 0);
					new GameObject(def.getId()+1, x, y, z, def.getFace(), def.getType(), def.getId(), 10);
					break;
				case 1413:
					player.getActionSender().sendMessage("There are no pineapples left on this plant.");
					break;
				
				case 3193 : // closed bank chest
					player.getUpdateFlags().sendAnimation(832);
					new GameObject(3194, x, y, z, def.getFace(), def.getType(), id, 500);
					break;
				case 1293 : // spirit tree
				case 1294 :
				case 1317 :
					if(player.questStage[95] != 1){
						QuestDefinition questDef = QuestDefinition.forId(95);
						String questName = questDef.getName();
						player.getActionSender().sendMessage("You need to complete "+questName+" to do this.");
					} else {
						Dialogues.startDialogue(player, 10011);
					}
					break;
				case 1805 : // champions guild
					if(player.getQuestPoints() >= 32){
						int i = player.getPosition().getY() > 3362 ? -1 : 1;
						player.getActionSender().walkTo(0, i, true);
						player.getActionSender().walkThroughDoor(id, x, y, z);
						if(i == -1){
							player.getDialogue().setLastNpcTalk(198);
							player.getDialogue().sendNpcChat("Greetings bold adventurer. Welcome to the guild of",
														"Champions.",Dialogues.CONTENT);
							player.getDialogue().endDialogue();
						}
					}else{
						player.getDialogue().sendStatement("You need atleast 32 Quest points to enter the Champions' Guild.");
					}
					break;
					
				case 12094://zanaris, fairy ring back to swamp
					player.teleport(new Position(3201, 3169));
					break;
				case 12003://zanaris, fairy ring to al kharid
					player.teleport(new Position(3262, 3171));
					break;
				case 4624 : // burthorpe staircase
					player.teleport(new Position(2208, 4938));
					break;
				case 4622 : // games room staircase
					player.teleport(new Position(2899, 3565));
					break;
					
				case 5098 : // brimhaven dungeon stairs
					player.teleport(new Position(2637, 9517, 0));
					break;
					
				case 5097 : // brimhaven dungeon stairs
					player.teleport(new Position(2637, 9510, 2));
					break;
					
				case 5096 : // brimhaven dungeon stairs
					player.teleport(new Position(2650, 9591, 0));
					break;
				
				case 5094 : // brimhaven dungeon stairs
					player.teleport(new Position(2643, 9595, 2));
					break;
					
				case 10776 : // mage training area stairs
					player.teleport(new Position(3360, 3306, 0));
					break;
					
				case 10773 : // mage training area stairs
					player.teleport(new Position(3366, 3306, 0));
					break;	
					
				case 10775 : // mage training area stairs
					player.teleport(new Position(3357, 3307, 1));
					break;	
					
				case 10771 : // mage training area stairs
					player.teleport(new Position(3369, 3307, 1));
					break;	
					
				case 2834 : // battlements
					if (player.getPosition().getX() > 2567) {
						player.getUpdateFlags().sendAnimation(839);
						player.getActionSender().walkTo(-2, 0, true);
					} else {
						//player.movePlayer(player.getPosition());
						player.getUpdateFlags().sendAnimation(2750);
						CrossObstacle.setForceMovement(player, 2, 0, 1, 80, 2, true, 0, 0);
					}
					break;
				case 1968 :
				case 1967 :
					player.getActionSender().walkTo(0, player.getPosition().getY() < 3492 ? 2 : -2, true);
					break;
				case 4616 : //lighthouse bridge
				case 4615 :
					//player.movePlayer(player.getPosition());
					CrossObstacle.setForceMovement(player, player.getPosition().getX() < 2597 ? 4 : -4, 0, 1, 150, 4, true, 0, player.getPosition().getX() < 2597 ? 756 : 754);
					break;
				case 51 : // McGrubors
					//player.movePlayer(player.getPosition());
					player.getUpdateFlags().sendAnimation(754);
					CrossObstacle.setForceMovement(player, player.getPosition().getX() < 2662 ? 1 : -1, 0, 1, 80, 2, true, 0, 0);
					break;
				case 2186 :
					//player.movePlayer(player.getPosition());
					player.getUpdateFlags().sendAnimation(754);
					CrossObstacle.setForceMovement(player, 0, player.getPosition().getY() < 3161 ? 1 : -1, 1, 80, 2, true, 0, 0);
					break;
				case 5259 : // port phays entrance
					player.getActionSender().walkTo(0, player.getPosition().getY() < 3508 ? 1 : -1, true);
					break;
				case 2618 : // lumberyard fence
					//player.movePlayer(player.getPosition());
					player.getUpdateFlags().sendAnimation(839);
					CrossObstacle.setForceMovement(player, 0, player.getPosition().getY() < 3493 ? 1 : -1, 1, 80, 2, true, 0, 0);
					break;
				case 2266 :
					if (player.getPosition().getY() > 2963) {
						player.getActionSender().walkTo(0, -1, true);
						player.getActionSender().walkThroughDoor(id, x, y, z);
					} else {
						Dialogues.startDialogue(player, 513);
					}
					break;
				case 10177 :
					Dialogues.startDialogue(player, 10005);
					break;
				case 8960 :
				case 8959 :
				case 8958 :
					Player p1 = null;
					Player p2 = null;
					for (Player players : World.getPlayers()) {
						if (players == null) {
							continue;
						}
						if (players.getPosition().getX() == 2490 && players.getPosition().getY() == y) {
							p1 = players;
						}
						if (players.getPosition().getX() == 2490 && players.getPosition().getY() == y + 2) {
							p2 = players;
						}
					}
					if (p1 != null && p2 != null) {
						new GameObject(Constants.EMPTY_OBJECT, x, y, z, def.getFace(), def.getType(), id, 50);
					} else {
						player.getActionSender().sendMessage("The door is locked.");
					}
					break;
				case 8966 : // daggnoth cave exit
					player.teleport(new Position(2523, 3739, 0));
					break;
				case 8929 : // daggnoth cave entrance
					player.teleport(new Position(2442, 10146, 0));
					break;
				case 8930 :
					player.teleport(new Position(2545, 10143, 0));
					break;
				case 10193:
					Ladders.climbLadder(player, new Position(2545, 10143, 0));
					break;
				case 10195:
					Ladders.climbLadder(player, new Position(1809, 4405, 2));
					break;
				case 10196:
					Ladders.climbLadder(player, new Position(1807, 4405, 3));
					break;
				case 10197:
					Ladders.climbLadder(player, new Position(1823, 4404, 2));
					break;
				case 10198:
					Ladders.climbLadder(player, new Position(1825, 4404, 3));
					break;
				case 10199:
					Ladders.climbLadder(player, new Position(1834, 4388, 2));
					break;
				case 10200:
					Ladders.climbLadder(player, new Position(1834, 4390, 3));
					break;
				case 10201:
					Ladders.climbLadder(player, new Position(1811, 4394, 1));
					break;
				case 10202:
					Ladders.climbLadder(player, new Position(1812, 4394, 2));
					break;
				case 10203:
					Ladders.climbLadder(player, new Position(1799, 4386, 2));
					break;
				case 10204:
					Ladders.climbLadder(player, new Position(1799, 4388, 1));
					break;
				case 10205:
					Ladders.climbLadder(player, new Position(1796, 4382, 1));
					break;
				case 10206:
					Ladders.climbLadder(player, new Position(1796, 4382, 2));
					break;
				case 10207:
					Ladders.climbLadder(player, new Position(1800, 4369, 2));
					break;
				case 10208:
					Ladders.climbLadder(player, new Position(1802, 4370, 1));
					break;
				case 10209:
					Ladders.climbLadder(player, new Position(1827, 4362, 1));
					break;
				case 10210:
					Ladders.climbLadder(player, new Position(1825, 4362, 2));
					break;
				case 10211:
					Ladders.climbLadder(player, new Position(1863, 4373, 2));
					break;
				case 10212:
					Ladders.climbLadder(player, new Position(1863, 4371, 1));
					break;
				case 10213:
					Ladders.climbLadder(player, new Position(1864, 4389, 1));
					break;
				case 10214:
					Ladders.climbLadder(player, new Position(1864, 4387, 2));
					break;
				case 10215:
					Ladders.climbLadder(player, new Position(1890, 4407, 0));
					break;
				case 10216:
					Ladders.climbLadder(player, new Position(1890, 4406, 1));
					break;
				case 10217:
					Ladders.climbLadder(player, new Position(1957, 4373, 1));
					break;
				case 10218:
					Ladders.climbLadder(player, new Position(1957, 4371, 0));
					break;
				case 10219:
					Ladders.climbLadder(player, new Position(1824, 4379, 3));
					break;
				case 10220:
					Ladders.climbLadder(player, new Position(1824, 4381, 2));
					break;
				case 10221:
					Ladders.climbLadder(player, new Position(1838, 4375, 2));
					break;
				case 10222:
					Ladders.climbLadder(player, new Position(1838, 4377, 3));
					break;
				case 10223:
					Ladders.climbLadder(player, new Position(1850, 4386, 1));
					break;
				case 10224:
					Ladders.climbLadder(player, new Position(1850, 4387, 2));
					break;
				case 10225:
					Ladders.climbLadder(player, new Position(1932, 4378, 1));
					break;
				case 10226:
					Ladders.climbLadder(player, new Position(1932, 4380, 2));
					break;
				case 10227:
					if (x == 1961 && y == 4392)
						Ladders.climbLadder(player, new Position(1961, 4392, 2));
					else
						Ladders.climbLadder(player, new Position(1932, 4377, 1));
					break;
				case 10228:
					Ladders.climbLadder(player, new Position(1961, 4393, 3));
					break;
				case 195://brimtail cave out
					Ladders.climbLadder(player, new Position(2410, 3421, 0));
					break;	
				case 10229:
					Ladders.climbLadder(player, new Position(1912, 4367, 0));
					break;
				case 10230:
					Ladders.climbLadder(player, new Position(2899, 4449, 0));
					break;
				case 9398: // deposit box
					BankManager.openDepositBox(player);
					break;
				case 10596: // enter icy cavern
					player.teleport(new Position(3056, 9555));
					break;
				case 10595: // exit icy cavern
					player.teleport(new Position(3056, 9562));
					break;
				case 5949: // lumby jump
					if (player.getPosition().getY() > y) {
						player.teleport(new Position(3221, 9552));
					} else {
						player.teleport(new Position(3221, 9556));
					}
					break;
				case 2623: // taverly dungeon door
					if (!player.getInventory().playerHasItem(1590) && player.getPosition().getX() > 2923) {
						player.getActionSender().sendMessage("The door is locked, you need a dusty key to open it.");
					} else {
						player.getActionSender().walkThroughDoor(id, x, y, 0);
						player.getActionSender().walkTo(player.getPosition().getX() > 2923 ? -1 : 1, 0, true);
					}
					break;
				case 2631: // jailer door
					if(player.getPosition().getY() < 9692){
						if (!player.getInventory().playerHasItem(1591) && player.getPosition().getY() > 9689) {
							player.getActionSender().sendMessage("The door is locked, you need a jail key to open it.");
						} else {
							player.getActionSender().walkThroughDoor(id, x, y, 0);
							player.getActionSender().walkTo(0, player.getPosition().getY() > 9689 ? -1 : 1, true);
						}
					}else{
						if (!player.getInventory().playerHasItem(1591) && player.getPosition().getY() < 9695) {
							player.getActionSender().sendMessage("The door is locked, you need a jail key to open it.");
						} else {
							player.getActionSender().walkThroughDoor(id, x, y, 0);
							player.getActionSender().walkTo(0, player.getPosition().getY() < 9695 ? 1 : -1, true);
						}
					}
					break;
				case 2320:
					if (y < player.getPosition().getY())
						player.teleport(new Position(player.getPosition().getX(), 9964));
					else
						player.teleport(new Position(player.getPosition().getX(), 9969));
					break;
				case 5083:
					if (player.isBrimhavenDungeonOpen()) {
						player.fadeTeleport(new Position(2713, 9564));
						player.setBrimhavenDungeonOpen(false);
					} else {
						Dialogues.startDialogue(player, 1595);
					}
					break;
				case 5084:
					player.teleport(new Position(2744, 3152));
					break;
				case 4499:
					player.fadeTeleport(new Position(2808, 10002));
					player.getActionSender().sendMessage("You enter the cave.");
					break;
				case 4500:
					player.teleport(new Position(2796, 3615));
					break;
				case 5100:
					if (player.getPosition().getY() < y)
						player.teleport(new Position(2655, 9573));
					// player.getActionSender().walkTo(0, 7, true);
					else
						player.teleport(new Position(2655, 9566));
					// player.getActionSender().walkTo(0, -7, true);
					break;
				case 5111:
					player.teleport(new Position(2649, 9562));
					break;
				case 5110:
					player.teleport(new Position(2647, 9557));
					break;
				case 5088://brimhaven dung. red dragon log
					CrossObstacle.walkAcross3(player, 0, 5, 0, -1, 762, -1, 2, null, null);
					break;	
				case 5090://brimhaven dung. red dragon log
					CrossObstacle.walkAcross3(player, 0, -5, 0, -1, 762, -1, 2, null, null);
					break;
				case 5099://pipe @brimhaven dung.
					if(x == 2698 && y == 9498){
						CrossObstacle.walkAcross3(player, 0, 0, -8, 746, 844, 748, 7, null, null);
					}
					else if(x == 2698 && y == 9493){
						CrossObstacle.walkAcross3(player, 0, 0, 8, 746, 844, 748, 7, null, null);
					}
				break;
				case 6552:
					player.getUpdateFlags().sendAnimation(645);
					boolean a = player.getMagicBookType() == SpellBook.MODERN;
					if(player.getMagicBookType() == SpellBook.NECROMANCY){
						a = player.tempMagicBookType == SpellBook.MODERN;
					}
					if (a) {
						player.getActionSender().sendMessage(" You feel a strange wisdom fill your mid...");
						player.getActionSender().sendSidebarInterface(6, 12855);
						player.setMagicBookType(SpellBook.ANCIENT);
					} else {
						player.getActionSender().sendMessage("You feel a strange drain upon your memory...");
						player.getActionSender().sendSidebarInterface(6, 1151);
						player.setMagicBookType(SpellBook.MODERN);
					}
					break;
				case 6481:
					player.getActionSender().sendMessage("You sneak into the back of the pyramid...");
					player.fadeTeleport(new Position(3229, 9310));
					break;
				case 6515:
					player.getActionSender().sendMessage("You search the sarcophagus, and sneak into it...");
					player.getActionSender().sendInterface(8677);

					CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {
							player.teleport(new Position(3233, 2887));
							container.stop();
						}

						@Override
						public void stop() {

						}
					}, 4);

					break;
				case 1742: // gnome tree stairs
					if (x == 2445 && y == 3434) {
						player.teleport(new Position(2445, 3433, 1));
					}
					if (x == 2444 && y == 3414) {
						player.teleport(new Position(2445, 3416, 1));
					}
					if (x == 2475 && y == 3400) {
						player.teleport(new Position(2475, 3399, 1));
					}
					if (x == 2479 && y == 3408) {
						player.teleport(new Position(2479, 3407, 1));
					}
					if (x == 2485 && y == 3402) {
						player.teleport(new Position(2485, 3401, 1));
					}
					if (x == 2488 && y == 3407) {
						player.teleport(new Position(2489, 3409, 1));
					}
					if (x == 2461 && y == 3416) {
						player.teleport(new Position(2460, 3417, 1));
					}
					if (x == 2455 && y == 3417) {
						player.teleport(new Position(2457, 3417, 1));
					}
					if (x == 2440 && y == 3404) {
						player.teleport(new Position(2440, 3403, 1));
					}
					if (x == 2418 && y == 3417) {
						player.teleport(new Position(2418, 3416, 1));
					}
					if (x == 2401 && y == 3449) {
						player.teleport(new Position(2400, 3450, 1));
					}
					if (x == 2395 && y == 3474) {
						player.teleport(new Position(2396, 3476, 1));
					}
					if (x == 2376 && y == 3489) {
						player.teleport(new Position(2378, 3489, 1));
					}
					if (x == 2384 && y == 3506) {
						player.teleport(new Position(2384, 3505, 1));
					}
					if (x == 2389 && y == 3512) {
						player.teleport(new Position(2388, 3513, 1));
					}
					if (x == 2396 && y == 3501) {
						player.teleport(new Position(2396, 3500, 1));
					}
					if (x == 2398 && y == 3512) {
						player.teleport(new Position(2397, 3513, 1));
					}
					if (x == 2413 && y == 3488) {
						player.teleport(new Position(2412, 3489, 1));
					}
					if (x == 2417 && y == 3490) {
						player.teleport(new Position(2418, 3492, 1));
					}
					if (x == 2420 && y == 3471) {
						player.teleport(new Position(2421, 3473, 1));
					}
					if (x == 2416 && y == 3445) {
						player.teleport(new Position(2415, 3446, 1));
					}
					break;
				case 1744: // gnome tree stairs
					if (x == 2445 && y == 3434) {
						player.teleport(new Position(2445, 3436, 0));
					}
					if (x == 2445 && y == 3415) {
						player.teleport(new Position(2445, 3413, 0));
					}
					if (x == 2475 && y == 3400) {
						player.teleport(new Position(2475, 3399, 0));
					}
					if (x == 2479 && y == 3408) {
						player.teleport(new Position(2479, 3407, 0));
					}
					if (x == 2485 && y == 3402) {
						player.teleport(new Position(2485, 3401, 0));
					}
					if (x == 2489 && y == 3408) {
						player.teleport(new Position(2488, 3409, 0));
					}
					if (x == 2461 && y == 3417) {
						player.teleport(new Position(2460, 3417, 0));
					}
					if (x == 2456 && y == 3417) {
						player.teleport(new Position(2457, 3417, 0));
					}
					if (x == 2440 && y == 3404) {
						player.teleport(new Position(2440, 3403, 0));
					}
					if (x == 2418 && y == 3417) {
						player.teleport(new Position(2418, 3416, 0));
					}
					if (x == 2401 && y == 3450) {
						player.teleport(new Position(2400, 3450, 0));
					}
					if (x == 2396 && y == 3475) {
						player.teleport(new Position(2396, 3476, 0));
					}
					if (x == 2377 && y == 3489) {
						player.teleport(new Position(2378, 3489, 0));
					}
					if (x == 2384 && y == 3506) {
						player.teleport(new Position(2384, 3505, 0));
					}
					if (x == 2389 && y == 3513) {
						player.teleport(new Position(2388, 3513, 0));
					}
					if (x == 2396 && y == 3501) {
						player.teleport(new Position(2395, 3501, 0));
					}
					if (x == 2398 && y == 3513) {
						player.teleport(new Position(2397, 3513, 0));
					}
					if (x == 2413 && y == 3489) {
						player.teleport(new Position(2412, 3489, 0));
					}
					if (x == 2418 && y == 3491) {
						player.teleport(new Position(2419, 3491, 0));
					}
					if (x == 2421 && y == 3472) {
						player.teleport(new Position(2421, 3473, 0));
					}
					if (x == 2416 && y == 3446) {
						player.teleport(new Position(2417, 3447, 0));
					}
					break;
				case 2407: // entrana dungeon door
					if (x == 2874 && y == 9750) {
						player.teleport(new Position(3250, 3772));
						player.getActionSender().sendMessage("You feel the world around you dissolve...");
					}
					break;
				case 2408: // entrana dungeon
					Dialogues.startDialogue(player, 656);
					break;
				case 2640: // prayer guild altar
					Prayer.rechargePrayerGuild(player);
					break;
				case 2641: // prayer guild ladder
					if (player.getSkill().getPlayerLevel(Skill.PRAYER) < 31) {
						player.getDialogue().sendStatement("You need a Prayer level of 31 to enter the Prayer guild.");
					} else {
						Ladders.checkClimbLadder(player, "up");
					}
					break;
				case 1804: // brass key door
					if (player.getPosition().getY() < 3450 && !player.getInventory().playerHasItem(983)) {
						player.getActionSender().sendMessage("You need a brass key to enter here.");
					} else {
						player.getActionSender().walkThroughDoor(id, x, y, 0);
						player.getActionSender().walkTo(0, player.getPosition().getY() < 3450 ? 1 : -1, true);
					}
					break;
				case 733: // slash web
					Webs.slashWeb(player, x, y, player.getEquipment().getId(Constants.WEAPON));
					break;
				case 9334: // canifis gate
					player.getActionSender().sendMessage("Please use the trapdoor located a bit north of here.");
					break;
				case 6434: // open trapdoor
					TrapDoor.handleTrapdoor(player, id, 6435, def);
					break;
				case 1568: // open trapdoor
					TrapDoor.handleTrapdoor(player, id, 1570, def);
					break;
				case 1570: // climb down trapdoor
				case 5947: // climb into lumby swamp
				case 6435: // climb down trapdoor
				case 882: // climb down manhole
				case 1754: // climb down ladder
				case 9472: // climb down port sarim dungeon
				case 1759: // taverly dungeon entrance
				case 11867: // dwarven mine entrance
					Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY() + 6400));
					break;
				case 1755: // exit stairs
				case 2405:
				case 6436: // climb up ladder
				case 5946: // climb out of lumby swamp
				case 1757: // climb up ladder
					if (x == 3097 && y == 9867) { // up to edgeville
						Ladders.climbLadder(player, new Position(3096, 3468, 0));
					} else {
						Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY() - 6400));
					}
					break;
				case 3432: // open canifis trapdoor
					TrapDoor.handleTrapdoor(player, id, 3433, def);
					break;
				case 3433: // climb down canifis trapdoor
					Ladders.climbLadder(player, new Position(3440, 9887, 0));
					break;
				case 2156: // wizard guild portal e
					player.teleport(new Position(3113, 3169, 0));
					break;
				case 2157: // wizard guild portal s
					player.teleport(new Position(2907, 3332, 0));
					break;	
				case 2158: // wizard guild portal w
					player.teleport(new Position(2703, 3404, 0));
					break;	
				case 2492: // portal out of rune mines
					switch (player.getRunecraftNpc()) {
					case 171:
						player.teleport(new Position(2388, 9812));
						break;
					case 462:
						player.teleport(new Position(2591, 3086));
						break;
					case 553:
						player.teleport(new Position(3253, 3401));
						break;
					case 300:
						player.teleport(new Position(3101, 9571));
						break;
					case 844:
						player.teleport(new Position(2684, 3322));
						break;
					}
					break;
				case 3044: // tutorial furnace
					if (player.questStage[0] != 1) {
						player.getDialogue().sendStatement("This is a furnace for smelting metal. To use it simply click on the", "ore you wish to smelt then click on the furnace you would like to", "use.");
						player.setClickId(0);
					}
					break;
				case 3039: // range
					player.getDialogue().sendStatement("To cook something you need to use the item you would like to cook", "on the cooking range. Do this by clicking the item in your inventory", "and then clicking the range.");
					player.setClickId(0);
					break;
				case 3566: // brimhaven swing
					int walkX = player.getPosition().getX() > 2768 ? -5 : 5;
					CrossObstacle.walkAcross(player, 50, walkX, 0, 2, 30, 751);
					break;
				case 5492: // H.A.M. trapdoor
					/*
					 * if (player.isHamTrapDoor()) { new GameObject(5491, x, y,
					 * z, 0, 10, 5492, 999999); }
					 */
					break;
				case 5581: // take axe from log
					AxeInLog.pullAxeFromLog(player, x, y);
					break;
				case 8689: // Milk cow
					MilkCow.milkCow(player);
					break;
				case 2718: // operate hopper
				case 2719:
				case 2720:
				case 2721:
					FlourMill.operateHopper(player);
					break;
				case 1781: // grain bin
					FlourMill.takeFromBin(player);
					break;
				case 2609: // crandor tunnel
					Ladders.climbLadder(player, new Position(2834, 9657, 0));
					break;
				case 2610: // karamja rope
					Ladders.climbLadder(player, new Position(2833, 3257, 0));
					break;
				case 2147:// wizard tower ladder to sedridor
					Ladders.climbLadder(player, new Position(3104, 9576, 0));
					break;
				case 4568 : // lighthouse stairs to 1st floor	
				case 9582 : //crafting guild down
				case 11511 : // draynor manor middle to top
				case 12536:// wizard tower ladder to upstairs up to height 1
				case 11732:// fally staircase
				case 11729:// fally staircase
					Ladders.checkClimbTallStaircase(player, "up");
					break;
				case 4570 : // lighthouse stairs to 1st floor	
				case 11733:// fally staircase to height 0
				case 11731:// fally staircase
				case 9584 : // draynor manor top floor, crafting guild
				case 12538:// wizard tower staircase down to height 1
					Ladders.checkClimbTallStaircase(player, "down");
					break;
				case 11736:// fally staircase
				case 11734:// fally staircase
				case 1722: // staircase up
				case 1725 : // varrock in bottom to middle
				case 9470: // rimmington staircase
					if(x == 2590 && y == 3089)//mage guild stairs
						player.teleport(new Position(player.getPosition().getX(), 3092, 1));
					else
						Ladders.checkClimbStaircase(player, 4, 4, "up");
					break;
				case 4493: // slayer tower
				case 4495: // slayer tower
				case 11498 : // draynor manor bottom to middle
					Ladders.checkClimbStaircase(player, 5, 5, "up");
					break;
				case 1723: // staircase down
				case 1726 : // varrock inn middle to bottom
				case 11737:// fally staircase
				case 11735:// fally staircase
				case 9471: // rimmington staircase
					Ladders.checkClimbStaircase(player, 4, 4, "down");
					break;
				case 4494: // slayer tower
				case 4496: // slayer tower
				case 11499 : // draynor manor middle to bottom
					Ladders.checkClimbStaircase(player, 5, 5, "down");
					break;
				case 7057 : // single staircase
					Ladders.checkClimbStaircaseBackwards(player, 4, 4, "up");
					break;
				case 7056 : // single staircase
					Ladders.checkClimbStaircaseBackwards(player, 4, 4, "down");
					break;
				case 9319: // climb up ladder
				case 8744:
				case 1747:
				case 1750:
				case 9558:
				case 11739:
				case 11727:	
				case 12964:// flour mill ladder to upstairs
				case 12112:
					Ladders.checkClimbLadder(player, "up");
					break;
				case 4163:
				case 4164:
					Ladders.checkClimbLadder(player, "up", 2);
					break;
				case 4173:
				case 4174:
					Ladders.checkClimbLadder(player, "down", 2);
					break;	
				case 9320: // climb down ladder
				case 8746:
				case 3205:
				case 9559:
				case 11741:
				case 9560:
				case 12966:// flour mill staircase down
				case 1746: // climb down a height ladder
				case 1749:
				case 11728:
				case 11742:
				case 12113:
				case 4189:
					Ladders.checkClimbLadder(player, "down");
					break;
				case 2148:// wizard tower ladder to sedridor
					Ladders.climbLadder(player, new Position(3105, 3162, 0));
					break;
				case 881: // open manhold
					TrapDoor.handleTrapdoor(player, id, 882, def);
					break;
				case 883: // close manhold
					TrapDoor.handleTrapdoor(player, id, 881, def);
					break;
				case 2112: // Mining guild door entrance
					if (player.getPosition().getY() > 9756) {
						if (SkillHandler.hasRequiredLevel(player, Skill.MINING, 60, "enter the Mining Guild")) {
							player.getActionSender().walkThroughDoor(id, x, y, z);
							player.getActionSender().walkTo(0, -1, true);
						}
					} else {
						player.getActionSender().walkThroughDoor(id, x, y, z);
						player.getActionSender().walkTo(0, 1, true);
					}
					break;
				case 2113: // Mining guild ladder entrance
					if (SkillHandler.hasRequiredLevel(player, Skill.MINING, 60, "enter the Mining Guild")) {
						Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY() + 6400, 0));
					}
					break;
				case 98: //camelot dung. boots of lightness stairs (down)
					if(x == 2650 && y == 9804){
						if(player.hasLitCandleOrLamp())
							player.teleport(new Position(player.getPosition().getX() - 8, player.getPosition().getY() - 41, 0));//light
						else{
							player.teleport(new Position(player.getPosition().getX() - 8, player.getPosition().getY() - 64, 0));//dark
							player.getDialogue().sendStatement("Hmm...bit dark down here! I'm not going to venture far!");
							player.getDialogue().endDialogue();
						}
					}
					break;
				case 96: //camelot dung. boots of lightness stairs (up)
					if(x == 2638 && y == 9763)//light
						player.teleport(new Position(2649, player.getPosition().getY() + 41, 0));
					if(x == 2638 && y == 9740)//dark
						player.teleport(new Position(2649, player.getPosition().getY() + 64, 0));
					break;	
				case 2616: //draynor manor vampire stairs(down)
					player.teleport(new Position(player.getPosition().getX() - 38, player.getPosition().getY() + 6415, 0));
					player.getActionSender().sendMessage("You walk down the stairs...");
					break;
				case 2617: //draynor manor vampire stairs(up)
					player.teleport(new Position(player.getPosition().getX() + 38, player.getPosition().getY() - 6415, 0));
					player.getActionSender().sendMessage("You walk up the stairs...");
					break;	
				case 1733:
					Ladders.checkClimbStaircaseDungeon(player, 4, 4, "down");
					break;
				case 1734:
					Ladders.checkClimbStaircaseDungeon(player, 4, 4, "up");
					break;
				case 492: // climb down karamja volcano
					Ladders.climbLadder(player, new Position(2857, 9569, 0));
					break;
				case 1764: // climb up karamja volcano
					Ladders.climbLadder(player, new Position(2856, 3167, 0));
					break;
				case 9358: // enter tzhaar cave
					player.sendTeleport(2480, 5175, 0);
					break;
				case 9356: // enter fight caves minigame
					player.getFightCavesMainData().startFightCaveMinigame();
					break;
				case 9357: // leave fight caves minigame
					player.getFightCavesMainData().endFightCaveMinigame();
					break;
				case 9706: // lever to mage arena
					if(player.kolodionStage >= 5){
						player.getActionSender().sendSound(319, 1, 0);
						player.getUpdateFlags().sendAnimation(835);
						player.getActionSender().sendMessage("You pull the lever...");
						player.getTeleportation().forcedObjectTeleport(3105, 3951, 0, "... and get teleported into the arena!");
					}
					break;
				case 9707: // lever out of mage arena
					player.getActionSender().sendSound(319, 1, 0);
					player.getUpdateFlags().sendAnimation(835);
					player.getActionSender().sendMessage("You pull the lever...");
					player.getTeleportation().forcedObjectTeleport(3105, 3956, 0, "... and get teleported out of the arena!");
					break;	
				case 1816: // lever to kbd
					player.getActionSender().sendSound(319, 1, 0);
					player.getUpdateFlags().sendAnimation(835);
					player.getActionSender().sendMessage("You pull the lever...");
					player.getTeleportation().forcedObjectTeleport(2272, 4681, 0, "...And teleport into the Dragon's Lair.");
					break;
				case 1817: // lever out of kbd
					player.getActionSender().sendSound(319, 1, 0);
					player.getUpdateFlags().sendAnimation(835);
					player.getActionSender().sendMessage("You pull the lever...");
					player.getTeleportation().forcedObjectTeleport(3067, 10254, 0, "...And teleport out of the Dragon's Lair.");
					break;
				case 5959: // lever to magebank
					player.getActionSender().sendSound(319, 1, 0);
					player.getUpdateFlags().sendAnimation(835);
					player.getActionSender().sendMessage("You pull the lever...");
					player.getTeleportation().forcedObjectTeleport(2539, 4712, 0, "... and teleport into the mage's cave.");
					break;
				case 5960: // lever out of magebank
					player.getActionSender().sendSound(319, 1, 0);
					player.getUpdateFlags().sendAnimation(835);
					player.getActionSender().sendMessage("You pull the lever...");
					player.getTeleportation().forcedObjectTeleport(3090, 3956, 0, "... and teleport out of the mage's cave.");
					break;	
				case 1814: // lever to lvl 51 wild
					player.getActionSender().sendSound(319, 1, 0);
					player.getUpdateFlags().sendAnimation(835);
					player.getActionSender().sendMessage("You pull the lever...");
					player.getTeleportation().forcedObjectTeleport(3154, 3923, 0, "...And teleport into the wilderness.");
					break;
				case 1815: // lever to ardy
					player.getActionSender().sendSound(319, 1, 0);
					player.getUpdateFlags().sendAnimation(835);
					player.getActionSender().sendMessage("You pull the lever...");
					player.getTeleportation().forcedObjectTeleport(2562, 3311, 0, "...And teleport out of the wilderness.");
					break;	
				case 194: // enter brimtails cave
					player.sendTeleport(2409, 9819, 0);
					break;	
				case 3735: // enter saba cave
					player.sendTeleport(2269, 4752, 0);
					break;	
				case 3736: // exit saba cave
					player.sendTeleport(2858, 3577, 0);
					break;	
				case 3379: // enter rantz cave
					player.sendTeleport(2647, 9378, 0);
					break;	
				case 3381: // exit rantz cave
					player.sendTeleport(2630, 2997, 0);
					break;	
				case 9359: // exit tzhaar cave
					player.sendTeleport(2862, 9571, 0);
					break;
				case 6279: // climb into smoke dungeon
					Ladders.climbLadder(player, new Position(3206, 9379, 0));
					break;
				case 6439: // climb out of smoke dungeon
					Ladders.climbLadder(player, new Position(3311, 2963, 0));
					break;	
				case 3828: // climb into kalphite tunnel
					Ladders.climbLadder(player, new Position(3484, 9509, 2));
					break;
				case 3829: // climb out of kalphite tunnel
					Ladders.climbLadder(player, new Position(3229, 3108, 0));
					break;
				case 3831: // climb into kalphite boss
					Ladders.climbLadder(player, new Position(3507, 9494, 0));
					break;
				case 3832: // climb out of kalphite boss
					Ladders.climbLadder(player, new Position(3509, 9499, 2));
					break;
				case 10168:
					int height = z > 1 ? 1 : 0;
					Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY(), height));
					break;
				case 10167:
					int height2 = z < 1 ? 1 : 2;
					Ladders.climbLadder(player, new Position(player.getPosition().getX(), player.getPosition().getY(), height2));
					break;
				case 1765: // climb down KBD cave
					Ladders.climbLadder(player, new Position(3069, 10255, 0));
					break;
				case 1766: // climb up KBD cave
					Ladders.climbLadder(player, new Position(3017, 3850, 0));
					break;	
				case 1738: // staircase up
					Ladders.checkClimbTallStaircase(player, "up");
					break;
				case 1740: // staircase down
					Ladders.checkClimbTallStaircase(player, "down");
					break;
				case 1739: // staircase mid
				case 4569:	
				case 12537:
					Dialogues.startDialogue(player, 10007);
					break;
				case 1748: // stairs mid
				case 8745:
				case 12965:// flour mill
				case 2884:
					Dialogues.startDialogue(player, 10006);
					break;
				case 2882: // alkharid gate
				case 2883:
					Dialogues.startDialogue(player, 9999);
					break;
				case 3203:
					Dialogues.startDialogue(player, 10010);
					break;
				case 3192:
					GlobalDuelRecorder.displayScoreBoardInterface(player);
					break;
				case 2213: //bank booth
				case 5276: //bank booth (port phasmatys)
				case 6084: //bank booth (keldagrim)
				case 11338: //bank booth (mos le'harmless)
				case 12759: //bank booth (burgh de rott)
				case 10517: //bank booth (nardah)
				case 14367: //bank booth (pest control)
				case 11758:
					Dialogues.startDialogue(player, 494);
					break;
				case 2491: // mine rune/pure ess
					MineEssence.startMiningEss(player);
					break;
				case 2634 : // white wolf mountain rock
					player.getActionSender().sendMessage("These rocks contain nothing interesting.");
					player.getActionSender().sendMessage("They are just in the way.");
					break;	
				default:
					player.getActionSender().sendMessage("Nothing interesting happens.");
					break;
				}
				this.stop();
			}
		});
	}

	public static void doObjectSecondClick(final Player player) {
		final int id = player.getClickId();
		final int x = player.getClickX();
		final int y = player.getClickY();
		final int z = player.getClickZ();
		final int task = player.getTask();
		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task)) {
					this.stop();
					return;
				}
				if (player.isMoving() || player.isStunned()) {
					return;
				}
				GameObjectDef def = SkillHandler.getObject(id, x, y, z);
				if (def == null) { // Server.npcHandler.getNpcByLoc(Location.create(x,
					/*if (id == 2213 && x == 3513) { //exception
						def = new GameObjectDef(id, 10, 0, new Position(x, y, z));
					} else {*/
						return;
					//}
				}
				GameObjectData object = GameObjectData.forId(player.getClickId());
				Position objectPosition;
				objectPosition = Misc.goodDistanceObject(def.getPosition().getX(), def.getPosition().getY(), player.getPosition().getX(), player.getPosition().getY(), object.getSizeX(def.getFace()), object.getSizeY(def.getFace()), z);
				if (objectPosition == null)
					return;
				if (!canInteractWithObject(player, objectPosition, def)) {
					stop();
					return;
				}
				Position loc = new Position(player.getClickX(), player.getClickY(), z);
				if (object != null)
					player.getUpdateFlags().sendFaceToDirection(loc.getActualLocation(object.getBiggestSize()));
				
				if (player.getQuestHandler().handleSecondClickObject(id, x, y)) {
					this.stop();
					return;
				}
				if (Dialogues.startDialogueObject(2, player, player.getClickId(), x, y)) {
					this.stop();
					return;
				}
				if (ThieveOther.handleObjectClick2(player, id, x, y)) {
					this.stop();
					return;
				}
				/* Thieving */
				if (ThieveStalls.handleThievingStall(player, id, x, y)) {
					this.stop();
					return;
				}
				if (Farming.inspectObject(player, x, y)) {
					this.stop();
					return;
				}
				if (player.getMining().prospect(id)) {
					this.stop();
					return;
				}
				if (PickableObjects.pickObject(player, id, x, y)) {
					this.stop();
					return;
				}
				switch (player.getClickId()) {
                case 6:
                    player.getDwarfMultiCannon().removeCannon(x, y, false);
                    this.stop();
                    return;
				case 2634 : // white wolf mountain rock (6 ticks to mine)
					MiscActions.mineWolfMountain(player, id, x, y);
					break;
				case 2114 : // coal truck
					CoalTruck.checkCoal(player);
					break;
				case 3194 : // opened bank chest
					BankManager.openBank(player);
					break;
				case 8930: //waterbirth snow cave to daggnoth
					Ladders.climbLadder(player, new Position(2545, 10143, 0));
					break;
				case 10177 : //daggnoth to waterbirth snow cave
					Ladders.climbLadder(player, new Position(2544, 3741, 0));
					break;
				case 3433: // close canifis trapdoor
					TrapDoor.handleTrapdoor(player, id, 3432, def);
					break;
				case 1570: // close trapdoor
					TrapDoor.handleTrapdoor(player, id, 1568, def);
					break;
				case 1739: // staircase mid
				case 4569:
				case 12537:
					Ladders.checkClimbTallStaircase(player, "up");
					break;
				case 1748: // stairs mid
				case 8745:
				case 12965:// flour mill
				case 2884: // gnome tree
					Ladders.checkClimbLadder(player, "up");
					break;
				case 14921: // furnace
				case 11666:
				case 9390:
				case 2781:
				case 3044:
					Smelting.smeltInterface(player);
					// player.getSmithing().setUpSmelting();
					break;
				case 2644:
					Menus.sendSkillMenu(player, "spinning");
					break;
				case 12121: // entrana bank
				case 2213:
				case 6084:
				case 5276:
				case 11338:
				case 14367: //bank booth (pest control)
				case 10517:
				case 11758:
					BankManager.openBank(player);
					break;
				case 8717:
					Menus.sendSkillMenu(player, "weaving");
					break;
				default:
					player.getActionSender().sendMessage("Nothing interesting happens.");
					break;	
				}
				this.stop();
			}
		});
	}

	public static void doObjectThirdClick(final Player player) {
		final int id = player.getClickId();
		final int x = player.getClickX();
		final int y = player.getClickY();
		final int z = player.getClickZ();
		final int task = player.getTask();

		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task)) {
					this.stop();
					return;
				}
				if (player.isMoving() || player.isStunned()) {
					return;
				}
				GameObjectDef def = SkillHandler.getObject(id, x, y, z);
				if (def == null) { // Server.npcHandler.getNpcByLoc(Location.create(x,
					return;
				}
				GameObjectData object = GameObjectData.forId(player.getClickId());
				Position objectPosition;
				objectPosition = Misc.goodDistanceObject(def.getPosition().getX(), def.getPosition().getY(), player.getPosition().getX(), player.getPosition().getY(), object.getSizeX(def.getFace()), object.getSizeY(def.getFace()), z);
				if (objectPosition == null)
					return;
				if (!canInteractWithObject(player, objectPosition, def)) {
					stop();
					return;
				}
				Position loc = new Position(player.getClickX(), player.getClickY(), z);
				if (object != null)
					player.getUpdateFlags().sendFaceToDirection(loc.getActualLocation(object.getBiggestSize()));

				switch (player.getClickId()) {
				case 3194 : // opened bank chest
					final GameObject p = ObjectHandler.getInstance().getObject(id, x, y, z);
					if (p != null) {
						player.getUpdateFlags().sendAnimation(832);
						ObjectHandler.getInstance().removeObject(x, y, z, def.getType());
					}
					break;
				case 10177: // Dagganoth ladder 1st level
					Ladders.climbLadder(player, new Position(1798, 4407, 3));
					break;
				case 1739: //staircase
				case 4569:
				case 12537:
					Ladders.checkClimbTallStaircase(player, "down");
					break;
				case 1748: // stairs mid
				case 8745:
				case 12965:// flour mill
				case 2884: // gnome tree
					Ladders.checkClimbLadder(player, "down");
					break;
				case 4187: // rellekka fight ladder
					Ladders.checkClimbLadder(player, "up");
					break;	
				default:
					player.getActionSender().sendMessage("Nothing interesting happens.");
					break;
				}
				this.stop();
			}
		});
	}

	public static void doObjectFourthClick(final Player player) {
		final int id = player.getClickId();
		final int x = player.getClickX();
		final int y = player.getClickY();
		final int z = player.getClickZ();
		final int task = player.getTask();
		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task)) {
					this.stop();
					return;
				}
				if (player.isMoving() || player.isStunned()) {
					return;
				}
				GameObjectDef def = SkillHandler.getObject(id, x, y, z);
				if (def == null) { // Server.npcHandler.getNpcByLoc(Location.create(x,
					return;
				}
				GameObjectData object = GameObjectData.forId(player.getClickId());
				Position objectPosition;
				objectPosition = Misc.goodDistanceObject(def.getPosition().getX(), def.getPosition().getY(), player.getPosition().getX(), player.getPosition().getY(), object.getSizeX(def.getFace()), object.getSizeY(def.getFace()), z);
				if (objectPosition == null)
					return;
				if (!canInteractWithObject(player, objectPosition, def)) {
					stop();
					return;
				}
				Position loc = new Position(player.getClickX(), player.getClickY(), z);
				if (object != null)
					player.getUpdateFlags().sendFaceToDirection(loc.getActualLocation(object.getBiggestSize()));

				if (Farming.guide(player, x, y)) {
					this.stop();
					return;
				}
				switch (player.getClickId()) {

				}
				player.getActionSender().sendMessage("Nothing interesting happens.");
				this.stop();
			}
		});
	}

	private static void doNpcFirstClick(final Player player) {
		final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
		final int task = player.getTask();
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		for (int[] element : Pets.PET_IDS) {
			if (player.getClickId() == element[1]) {
				player.getPets().unregisterPet();
				return;
			}
		}
		if (npc.getPlayerOwner() != null && (npc.getPlayerOwner() != player || npc.getCombatingEntity() != null)) {
			player.getActionSender().sendMessage("This npc is not interested in talking with you right now.");
			return;
		}
		npc.setInteractingEntity(player);
		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task) || npc.isDead()) {
					this.stop();
					return;
				}
				if (npc.isBoothBanker() || npc.getNpcId() == 736 || npc.getNpcId() == 745 || npc.getNpcId() == 3859 || npc.getNpcId() == 482) {
					if (npc.getCorrectStandPosition(player.getPosition(), 2)) {
						npc.getUpdateFlags().faceEntity(player.getFaceIndex());
						player.setInteractingEntity(npc);
						player.getUpdateFlags().faceEntity(npc.getFaceIndex());
						Dialogues.startDialogue(player, player.getClickId());
						Following.resetFollow(player);
						this.stop();
					}
					return;
				}
				if (!player.goodDistanceEntity(npc, 1) || player.inEntity(npc)) {
					return;
				}
				if(npc.getNpcId() == 3103){//mage training area reward guardian
					RewardShop.openShop(player);
					this.stop();
					return;
				}
				if (player.getFishing().fish(npc, 1)) {
					Following.resetFollow(player);
					player.setInteractingEntity(npc);
					player.getUpdateFlags().faceEntity(npc.getFaceIndex());
					this.stop();
					return;
				}
				if(npc.getNpcId() != 767 && npc.getNpcId() != 249 && npc.getNpcId() != 221)
				if (!Misc.checkClip(player.getPosition(), npc.getPosition(), true)) {
					return;
				}
				Following.resetFollow(player);
				player.setInteractingEntity(npc);
				player.getUpdateFlags().faceEntity(npc.getFaceIndex());
				if (npc.getPlayerOwner() != null && npc.getPlayerOwner() != player) {
					player.getActionSender().sendMessage(npc.getDefinition().getName() + " is not interested in interacting with you right now.");
					this.stop();
					return;
				}
				if (player.getSlayer().doNpcSpecialEffect(npc)) {
					this.stop();
					return;
				}
				if (Tree.getTreeEnt(npc.getNpcId()) != null) {
					ChopTree.handle(player, npc.getNpcId(), npc.getPosition().getX(), npc.getPosition().getY(), true);
					this.stop();
					return;
				}
				if (player.getQuestHandler().handleFirstClickNpc(player.getClickId())) {
					this.stop();
					return;
				}
				if (AnagramsScrolls.handleNpc(player, player.getClickId())) {
					this.stop();
					return;
				}
				if (SpeakToScrolls.handleNpc(player, player.getClickId())) {
					this.stop();
					return;
				}

				if (Dialogues.startDialogue(player, player.getClickId())) {
					this.stop();
					return;
				}
				if (Shops.findShop(player, player.getClickId()) > -1) {
					Dialogues.sendDialogue(player, 10008, 1, 0, player.getClickId());
					this.stop();
					return;
				}
				switch (player.getClickId()) {
					case 166 :
					case 494 :
					case 495 :
					case 496 :
					case 499 :
					case 2619:
						npc.getUpdateFlags().faceEntity(player.getFaceIndex());
						player.setInteractingEntity(npc);
						player.getUpdateFlags().faceEntity(npc.getFaceIndex());
						Dialogues.startDialogue(player, player.getClickId());
						Following.resetFollow(player);
						break;
				}
				if(npc.getNpcId() != 767)//do not send message
					player.getActionSender().sendMessage("This npc is not interested in talking with you right now.");
				this.stop();
			}
		});
	}

	private static void doNpcSecondClick(final Player player) {
		final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
		final int task = player.getTask();
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task) || npc.isDead()) {
					this.stop();
					return;
				}
				if (npc.isBoothBanker()) {
					if (npc.getCorrectStandPosition(player.getPosition(), 2)) {
						npc.getUpdateFlags().faceEntity(player.getFaceIndex());
						player.setInteractingEntity(npc);
						player.getUpdateFlags().faceEntity(npc.getFaceIndex());
						BankManager.openBank(player);
						Following.resetFollow(player);
						this.stop();
					}
					return;
				}
				if (!player.goodDistanceEntity(npc, 1) || player.inEntity(npc)) {
					return;
				}
				if (player.getFishing().fish(npc, 2)) {
					Following.resetFollow(player);
					player.getUpdateFlags().faceEntity(npc.getFaceIndex());
					this.stop();
					return;
				}
				if (!Misc.checkClip(player.getPosition(), npc.getPosition(), true)) {
					return;
				}
				Following.resetFollow(player);
				player.getUpdateFlags().faceEntity(npc.getFaceIndex());
				if (ThieveNpcs.handleThieveNpc(player, npc)) {
					this.stop();
					return;
				}
				npc.getUpdateFlags().faceEntity(player.getFaceIndex());
				player.setInteractingEntity(npc);
				if (Shops.openShop(player, npc.getNpcId())) {
					this.stop();
					return;
				}
				switch (player.getClickId()) {
				case 166 :
				case 494 :
				case 495 :
				case 496 :
				case 499 :
				case 2619:
					npc.getUpdateFlags().faceEntity(player.getFaceIndex());
					player.setInteractingEntity(npc);
					player.getUpdateFlags().faceEntity(npc.getFaceIndex());
					BankManager.openBank(player);
					Following.resetFollow(player);
					break;		
				case 3863: //ge clerk
					GeManager.openGe(player);
					break;	
				case 2437:
					Dialogues.sendDialogue(player, 2437, 4, 0);
					break;
				case 1595:
					Dialogues.sendDialogue(player, 1595, 3, 1);
					break;
				case 2824:
				case 804:
				case 1041:
					Tanning.tanningInterface(player);
					break;
				case 300:
				case 844:
				case 462:
				case 171:
					Runecrafting.teleportRunecraft(player, npc);
					break;
				case 960:
				case 961:
				case 962:
					player.getDuelMainData().healPlayer();
					break;
				case 3021:
					player.getFarmingTools().loadInterfaces();
					break;
				case 958:
					BankManager.openBank(player);
					break;

				}
				this.stop();
			}
		});
	}

	private static void doNpcThirdClick(final Player player) {
		final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
		final int task = player.getTask();
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task) || npc.isDead()) {
					this.stop();
					return;
				}
				if (!player.goodDistanceEntity(npc, 1) || player.inEntity(npc)) {
					return;
				}
				if(npc.getNpcId() == 3103){//mage training area reward guardian
					RewardShop.openShop(player);
				}
				if (!Misc.checkClip(player.getPosition(), npc.getPosition(), true)) {
					return;
				}

				
				Following.resetFollow(player);
				Npc npc = World.getNpcs()[player.getNpcClickIndex()];
				player.getUpdateFlags().faceEntity(npc.getFaceIndex());
				npc.getUpdateFlags().faceEntity(player.getFaceIndex());
				switch (player.getClickId()) {
				case 553:
					Runecrafting.teleportRunecraft(player, npc);
					break;
				case 70:
				case 1596:
				case 1597:
				case 1598:
				case 1599:
					ShopManager.openShop(player, 162);
					break;
				// case 958 :
				// ShopManager.openShop(player, 164);
				// break;
				case 2258:
					if(player.questStage[14] != 1){
						QuestDefinition questDef = QuestDefinition.forId(14);
						String questName = questDef.getName();
						player.getDialogue().sendStatement("You need to complete "+questName+" to do this.");
						player.getDialogue().endDialogue();
						break;
					}
					Abyss.teleportToAbyss(player, npc);
					break;
				}
				this.stop();
			}
		});
	}

	private static void doNpcFourthClick(final Player player) {
		final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
		final int task = player.getTask();
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task) || npc.isDead()) {
					this.stop();
					return;
				}
				if (!player.goodDistanceEntity(npc, 1) || player.inEntity(npc)) {
					return;
				}
				if (!Misc.checkClip(player.getPosition(), npc.getPosition(), true)) {
					return;
				}
				Following.resetFollow(player);
				player.getUpdateFlags().faceEntity(npc.getFaceIndex());
				npc.getUpdateFlags().faceEntity(player.getFaceIndex());
				switch (player.getClickId()) {
				case 494:
					BankManager.openBank(player);
					break;
				}
				this.stop();
			}
		});
	}
	
	private static void doSpellOnObject(final Player player) {
		final int x = player.getClickX();
		final int y = player.getClickY();
		final int z = player.getClickZ();
		final int id = player.getClickId();
		final int task = player.getTask();
		final int spell_ = player.getSpellId();
		final CacheObject obj = ObjectLoader.object(id, x, y, z);
		if (obj == null && ObjectHandler.getInstance().getObject(x, y, z) == null)
			return;
		final Spell spell = player.getMagicBookType().getSpells().get(spell_);
		if (spell == null)
			return;
		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task)) {
					this.stop();
					return;
				}
				if (player.isMoving() || player.isStunned()) {
					return;
				}
				GameObjectDef def = SkillHandler.getObject(id, x, y, z);
				if (def == null) { // Server.npcHandler.getNpcByLoc(Location.create(x,
					return;
				}
				GameObjectData object = GameObjectData.forId(player.getClickId());
				Position objectPosition;
				objectPosition = Misc.goodDistanceObject(def.getPosition().getX(), def.getPosition().getY(), player.getPosition().getX(), player.getPosition().getY(), object.getSizeX(def.getFace()), object.getSizeY(def.getFace()), z);
				if (id != 2638) {
					if (objectPosition == null)
						return;
					if (!canInteractWithObject(player, objectPosition, def)) {
						stop();
						return;
					}
				}

				Position loc = new Position(player.getClickX(), player.getClickY(), z);
				if (object != null)
					player.getUpdateFlags().sendFaceToDirection(loc.getActualLocation(object.getBiggestSize()));
				if (MagicSkill.spellOnObject(player, spell, id, x, y, player.getPosition().getZ())) {
					this.stop();
					return;
				}
				this.stop();
			}
		});
	}

	private static void doItemOnObject(final Player player) {
		final int x = player.getClickX();
		final int y = player.getClickY();
		final int z = player.getClickZ();
		final int id = player.getClickId();
		final int item = player.getClickItem();
		final int task = player.getTask();
		final CacheObject obj = ObjectLoader.object(id, x, y, z);
		if (obj == null && ObjectHandler.getInstance().getObject(x, y, z) == null)
			return;
		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task)) {
					this.stop();
					return;
				}
				if (player.isMoving() || player.isStunned()) {
					return;
				}
				GameObjectDef def = SkillHandler.getObject(id, x, y, z);
				if (def == null) { // Server.npcHandler.getNpcByLoc(Location.create(x,
					return;
				}
				GameObjectData object = GameObjectData.forId(player.getClickId());
				Position objectPosition;
				objectPosition = Misc.goodDistanceObject(def.getPosition().getX(), def.getPosition().getY(), player.getPosition().getX(), player.getPosition().getY(), object.getSizeX(def.getFace()), object.getSizeY(def.getFace()), z);
				if (id != 2638) {
					if (objectPosition == null)
						return;
					if (!canInteractWithObject(player, objectPosition, def)) {
						stop();
						return;
					}
				}

				Position loc = new Position(player.getClickX(), player.getClickY(), z);
				if (object != null)
					player.getUpdateFlags().sendFaceToDirection(loc.getActualLocation(object.getBiggestSize()));

				if (player.getQuestHandler().useQuestItemOnObject(item, id)) {
					this.stop();
					return;
				}
				/* cooking */
				if (player.getCooking().handleInterface(item, id, x, y)) {
					this.stop();
					return;
				}
				if (player.getCooking().handleFillingObjectWater(item, id)) {
					player.getInventory().refresh();
					this.stop();
					return;
				}
				
				if (id == 1781 && item == 1931) {
					FlourMill.takeFromBin(player);
					this.stop();
					return;
				}
				
				// smithing
				if(id == 14921 || id == 11666 || id == 9390 || id == 2781 || id == 3044){// furnace
					if(item == 446){
						Smelting.oreOnFurnace(player, item);
						this.stop();
						return;
					}
				}
				
				if (id == 3044 && player.questStage[0] != 1 && (item == 438 || item == 436)) {// furnace, tutorial
					Smelting.oreOnFurnace(player, item);
					// player.getSmithing().startSmelting(1, 0);
					this.stop();
					return;
				}
				
				if (id == 2783 && player.questStage[0] == 37 && item == 2349) {
					SmithBars.smithInterface(player, 2349);
					player.getActionSender().createPlayerHints(1, -1);
					player.setNextTutorialStage();
					this.stop();
					return;
				}

				// farming
				if (Farming.prepareCrop(player, item, id, x, y)) {
					this.stop();
					return;
				}

				if (RunecraftAltars.useTaliOnRuin(player, item, id)) {
					this.stop();
					return;
				}
				if (Tiaras.bindTiara(player, item, id)) {
					this.stop();
					return;
				}
				if (MixingRunes.combineRunes(player, item, id)) {
					this.stop();
					return;
				}
				if (item >= 3422 && item <= 3428 && id == 4090) {
					player.getInventory().removeItem(new Item(item));
					player.getInventory().addItem(new Item(item + 8));
					player.getUpdateFlags().sendAnimation(832);
					player.getActionSender().sendMessage("You put the olive oil on the fire, and turn it into sacred oil.");
					this.stop();
					return;
				}

				switch (id) {
				case 2114 : // coal truck
					if (item == 453) {
						CoalTruck.depositCoal(player);
					}
					break;
				case 172: // crystal chest
					if (item == 989) {
						CrystalChest.openCrystalChest(player);
					}
					break;
				case 3827: // kalphite lair entrance
					if (item == 954) {
						player.getInventory().removeItem(new Item(954, 1));
						ObjectHandler.getInstance().removeObject(x, y, z, 0);
						new GameObject(def.getId()+1, x, y, z, def.getFace(), def.getType(), def.getId(), 30);
					}
					break;
				case 3830: // kalphite queen entrance
					if (item == 954) {
						player.getInventory().removeItem(new Item(954, 1));
						ObjectHandler.getInstance().removeObject(x, y, z, 0);
						new GameObject(def.getId()+1, x, y, z, def.getFace(), def.getType(), def.getId(), 30);
					}
					break;	
				case 170: // muddy chest
					if (item == 991) {
						MuddyChest.openMuddyChest(player);
					}
					break;	
				case 733: // slash web
					Webs.slashWeb(player, x, y, item);
					break;
				case 2782: // anvil
				case 2783:
					SmithBars.smithInterface(player, item);
					// player.getSmithing().setUpSmithing(item);
					break;
				case 2714: // grain hopper
				case 2715:
				case 2716:
				case 2717:
					FlourMill.putFlourInHopper(player);
					break;
				case 2638: // glory fountain
					if (item == 1704 || item == 1706 || item == 1708 || item == 1710) {
						player.getActionSender().sendMessage("You dip your amulet into the fountain...");
						player.getUpdateFlags().sendAnimation(827, 0);
						for (int i = 0; i < Inventory.SIZE; i++) {
							int[] glorys = { 1704, 1706, 1708, 1710 };
							for (int glory : glorys) {
								if (player.getInventory().getItemContainer().contains(glory)) {
									player.getInventory().addItemToSlot(new Item(1712, 1), player.getInventory().getItemContainer().getSlotById(glory));
								}
							}
						}
						player.getDialogue().sendItem3Lines("You feel a power emanating from the fountain as it",
															"recharges your amulet. You can now rub the amulet to",
															"teleport and wear it to get more gems whilst mining.", new Item(item, 1));
						player.getDialogue().endDialogue();
					}
					break;
				case 2645:// pile of sand
					if (item == GlassMaking.BUCKET) {
						GlassMaking.fillWithSand(player);
					}
					break;
				/*
				 * Furnaces.
				 */
				case 14921:
				case 9390:
				case 2781:
				case 2785:
				case 2966:
				case 3044:
				case 3294:
				//case 3413:
				case 4304:
				case 4305:
				case 6189:
				case 6190:
				case 11009:
				case 11010:
				case 11666:
				case 12100:
				case 12809:
					if (item == GlassMaking.BUCKET_OF_SAND) {
						GlassMaking.makeMoltenGlass(player);
					} else if (item == GemCrafting.GOLD_BAR || item == GemCrafting.PERFECT_GOLD_BAR) {
						GemCrafting.openInterface(player, item);
					} else if (item == SilverCrafting.SILVER_BAR) {
						Menus.sendSkillMenu(player, "silverCrafting");
					} else if (ItemManager.getInstance().getItemName(item).toLowerCase().endsWith("ore")) {
						Smelting.smeltInterface(player);
						// player.getSmithing().setUpSmelting();
					}
					break;
				case 2642:// pottery unfire
					if (item == PotteryMaking.SOFT_CLAY) {
						Menus.sendSkillMenu(player, "potteryUnfired");
					}
					break;
				case 11601: // fire pottery
					Menus.sendSkillMenu(player, "potteryFired");
					break;
				default:
					player.getActionSender().sendMessage("Nothing interesting happens.");
					break;
				}
				this.stop();
			}
		});
	}

	private static void doItemOnNpc(final Player player) {
		// final int x = player.getClickX();
		// final int y = player.getClickY();
		final int item = player.getClickItem();
		final Npc npc = World.getNpcs()[player.getNpcClickIndex()];
		final int task = player.getTask();
		final int slot = player.getSlot();
		if (npc == null || !npc.isRealNpc()) {
			return;
		}
		World.submit(new Tick(1, true) {
			@Override
			public void execute() {
				if (player == null || !player.checkTask(task) || npc.isDead()) {
					this.stop();
					return;
				}
				if (!player.goodDistanceEntity(npc, 1) || player.inEntity(npc)) {
					return;
				}
				if (!Misc.checkClip(player.getPosition(), npc.getPosition(), true) && npc.getNpcId() != 901) {
					return;
				}
				player.getSlayer().finishOffMonster(npc, item);
				Following.resetFollow(player);
				if (player.getQuestHandler().handleItemOnNpc(player.getClickId(), item)) {
					this.stop();
					return;
				}
				if (player.getItemRepairing().handleItemOnNpc(player.getClickId(), item)) {
					this.stop();
					return;
				}
				if (BarrowsItem.handleItemOnNpc(player, player.getClickId(), player.getInventory().getItemContainer().get(slot))) {
					this.stop();
					return;
				}
				switch (player.getClickId()) { // npc ids
				case 3021:
					if (player.getFarmingTools().noteItem(npc, item)) {
						this.stop();
						return;
					}
					break;
				}
				switch (item) { // item ids
				case 1735:
					if (player.getClickId() == 43 || player.getClickId() == 1765) {
						NpcActions.shearSheep(player);
						this.stop();
						return;
					}
					break;
				}
				this.stop();
			}
		});
	}

	private static boolean canInteractWithObject(Player player, Position objectPos, GameObjectDef def) {
		if (def.getId() == 2638) {
			return true;
		}
		Rangable.removeObjectAndClip(def.getId(), def.getPosition().getX(), def.getPosition().getY(), def.getPosition().getZ(), def.getFace(), def.getType());
		boolean canInteract = Misc.checkClip(player.getPosition(), objectPos, false);
		Rangable.addObject(def.getId(), def.getPosition().getX(), def.getPosition().getY(), def.getPosition().getZ(), def.getFace(), def.getType(), true);
		return canInteract;
	}

	public static void setActions(Actions actions) {
		WalkToActionHandler.actions = actions;
	}

	public static Actions getActions() {
		return actions;
	}

	public static enum Actions {

		OBJECT_FIRST_CLICK, OBJECT_SECOND_CLICK, OBJECT_THIRD_CLICK, OBJECT_FOURTH_CLICK, ITEM_ON_OBJECT, SPELL_ON_OBJECT,

		NPC_FIRST_CLICK, ITEM_ON_NPC, NPC_SECOND_CLICK, NPC_THIRD_CLICK, NPC_FOURTH_CLICK
	}

}
