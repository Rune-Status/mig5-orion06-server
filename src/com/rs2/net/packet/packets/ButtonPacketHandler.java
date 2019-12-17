package com.rs2.net.packet.packets;

import com.rs2.Constants;
import com.rs2.cache.interfaces.RSInterface;
import com.rs2.model.content.BarrowsItem;
import com.rs2.model.content.EnchantStaff;
import com.rs2.model.content.minigames.barrows.Barrows;
import com.rs2.model.content.randomevents.TalkToEvent;
import com.rs2.model.content.skills.SkillsX;
import com.rs2.model.content.skills.crafting.DramenBranch;
import com.rs2.model.content.skills.crafting.GlassMaking;
import com.rs2.model.content.skills.crafting.LeatherMakingHandler;
import com.rs2.model.content.skills.crafting.PotteryMaking;
import com.rs2.model.content.skills.crafting.SilverCrafting;
import com.rs2.model.content.skills.crafting.Spinning;
import com.rs2.model.content.skills.crafting.Tanning;
import com.rs2.model.content.skills.crafting.Weaving;
import com.rs2.model.content.skills.fletching.HandleLogCutting;
import com.rs2.model.content.skills.cooking.Cooking;
import com.rs2.model.content.skills.cooking.DairyChurn;
import com.rs2.model.content.skills.cooking.FlourRelated;
import com.rs2.model.content.skills.magic.MagicSkill;
import com.rs2.model.content.skills.magic.OrbCharging;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.content.skills.smithing.Smelting;
import com.rs2.model.content.treasuretrails.Sextant;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.players.BankManager;
import com.rs2.model.players.GeManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.Player.BankOptions;
import com.rs2.model.players.Player.ExitType;
import com.rs2.model.players.TradeManager;
import com.rs2.model.players.item.Item;
import com.rs2.model.region.music.Music;
import com.rs2.model.region.music.RegionMusic;
import com.rs2.model.transport.Canoe;
import com.rs2.model.transport.GnomeGlider;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;

public class ButtonPacketHandler implements PacketHandler {

	public static final int BUTTON = 185;

	@Override
	public void handlePacket(Player player, Packet packet) {
		int id = packet.getIn().readShort();
		RSInterface inter = RSInterface.forId(id);
        if (!player.hasInterfaceOpen(inter)) {
            return;
        }
		handleButton(player, id);
	}
	
	private void handleButton(Player player, int buttonId) {
		if (Constants.SERVER_DEBUG) {
			System.out.println("button id: "+buttonId);
		}
		if (player.getDuelMainData().handleButton(buttonId))
			return;
		if (player.getQuestHandler().handleButton(buttonId)) {
			return;
		}
		//hiscores
		if(buttonId >= 18792 && buttonId <= 18812){
			int skill = buttonId-18792;
			player.currentHiscorePage = 0;
			player.currentHiscoreSkill = skill;
			player.setHiscoreTableInfo();
		}
		if(buttonId == 18791){
			player.currentHiscorePage = 0;
			player.currentHiscoreSkill = 21;
			player.setHiscoreTableInfo();
		}
		if(buttonId == 18813){
			player.currentHiscorePage = 0;
			player.currentHiscoreSkill = 22;
			player.setHiscoreTableInfo();
		}
		if(buttonId == 18884){
			if(player.currentHiscorePage >= 1){
				player.currentHiscorePage--;
				player.setHiscoreTableInfo();
			}
		}
		if(buttonId == 18883){
			player.currentHiscorePage++;
			player.setHiscoreTableInfo();
		}
		//music tab below
        for(int id = 0; id < Music.SONG_COUNT; id++){
        	Music songDef = Music.forId(id);
        	if(songDef.getButton() == buttonId){
        		if(RegionMusic.songUnlocked(player, id)){
        			player.getActionSender().playMusic(songDef);
        			player.currentMusic = id;
      				player.autoplayMusic = false;
      			}else{
      				player.getActionSender().sendMessage("You haven't unlocked that song yet.");
      			}
        		return;
      		}
      	}
      	if(buttonId == 6269){
      		player.autoplayMusic = true;
      		return;
      	}
      	if(buttonId == 6270){
      		player.autoplayMusic = false;
      		return;
      	}
      	//music tab^^
		// Buttons that can be used while packets stopped
		switch (buttonId) {
            // smithing
			case 10162://close book
				player.getActionSender().removeInterfaces();
				player.readingBook = 0;
				player.readingBookPage = 0;
			break;
			case 6020 : // unmorph
				player.getActionSender().removeInterfaces();
				player.transformNpc = -1;
				player.getActionSender().sendSideBarInterfaces();
				player.setAppearanceUpdateRequired(true);
				//player.getEquipment().unequip(Constants.RING);
				break;
            case 2422:
                player.getActionSender().removeInterfaces();
                break;
			/* Skill menus */
			case 8654 : // attack
				player.getSkillGuide().attackComplex(1);
				player.getSkillGuide().selected = 0;
				return;
			case 8657 : // strength
				player.getSkillGuide().strengthComplex(1);
				player.getSkillGuide().selected = 1;
				return;
			case 8660 : // Defence
				player.getSkillGuide().defenceComplex(1);
				player.getSkillGuide().selected = 2;
				return;
			case 8663 : // range
				player.getSkillGuide().rangedComplex(1);
				player.getSkillGuide().selected = 3;
				return;
			case 8666 : // prayer
				player.getSkillGuide().prayerComplex(1);
				player.getSkillGuide().selected = 4;
				return;
			case 8669 : // mage
				player.getSkillGuide().magicComplex(1);
				player.getSkillGuide().selected = 5;
				return;
			case 8672 : // runecrafting
				player.getSkillGuide().runecraftingComplex(1);
				player.getSkillGuide().selected = 6;
				return;
			case 8655 : // hp
				player.getSkillGuide().hitpointsComplex(1);
				player.getSkillGuide().selected = 7;
				return;
			case 8658 : // agility
				player.getSkillGuide().agilityComplex(1);
				player.getSkillGuide().selected = 8;
				return;
			case 8661 : // herblore
				player.getSkillGuide().herbloreComplex(1);
				player.getSkillGuide().selected = 9;
				return;
			case 8664 : // theiving
				player.getSkillGuide().thievingComplex(1);
				player.getSkillGuide().selected = 10;
				return;
			case 8667 : // crafting
				player.getSkillGuide().craftingComplex(1);
				player.getSkillGuide().selected = 11;
				return;
			case 8670 : // fletching
				player.getSkillGuide().fletchingComplex(1);
				player.getSkillGuide().selected = 12;
				return;
			case 12162 :// slayer
				player.getSkillGuide().slayerComplex(1);
				player.getSkillGuide().selected = 13;
				return;
			case 8656 :// mining
				player.getSkillGuide().miningComplex(1);
				player.getSkillGuide().selected = 14;
				return;
			case 8659 : // smithing
				player.getSkillGuide().smithingComplex(1);
				player.getSkillGuide().selected = 15;
				return;
			case 8662 : // fishing
				player.getSkillGuide().fishingComplex(1);
				player.getSkillGuide().selected = 16;
				return;
			case 8665 : // cooking
				player.getSkillGuide().cookingComplex(1);
				player.getSkillGuide().selected = 17;
				return;
			case 8668 : // firemaking
				player.getSkillGuide().firemakingComplex(1);
				player.getSkillGuide().selected = 18;
				return;
			case 8671 : // woodcut
				player.getSkillGuide().woodcuttingComplex(1);
				player.getSkillGuide().selected = 19;
				return;
			case 13928 : // farming
				player.getSkillGuide().farmingComplex(1);
				player.getSkillGuide().selected = 20;
				return;

			case 8846 : // tab 1
				player.getSkillGuide().menuCompilation(1);
				return;

			case 8823 : // tab 2
				player.getSkillGuide().menuCompilation(2);
				return;

			case 8824 : // tab 3
				player.getSkillGuide().menuCompilation(3);
				return;

			case 8827 : // tab 4
				player.getSkillGuide().menuCompilation(4);
				return;

			case 8837 : // tab 5
				player.getSkillGuide().menuCompilation(5);
				return;

			case 8840 : // tab 6
				player.getSkillGuide().menuCompilation(6);
				return;

			case 8843 : // tab 7
				player.getSkillGuide().menuCompilation(7);
				return;

			case 8859 : // tab 8
				player.getSkillGuide().menuCompilation(8);
				return;

			case 8862 : // tab 9
				player.getSkillGuide().menuCompilation(9);
				return;

			case 8865 : // tab 10
				player.getSkillGuide().menuCompilation(10);
				return;

			case 15303 : // tab 11
				player.getSkillGuide().menuCompilation(11);
				return;

			case 15306 : // tab 12
				player.getSkillGuide().menuCompilation(12);
				return;

			case 15309 : // tab 13
				player.getSkillGuide().menuCompilation(13);
				return;
			case 152 :
				player.getMovementHandler().setRunToggled(false);
				return;
			case 153 :
				if (player.questStage[0] == 23){
					player.setNextTutorialStage();
				}
				player.getMovementHandler().setRunToggled(true);
				return;
			case 150 :
				player.setAutoRetaliate(true);
				return;
			case 151 :
				player.setAutoRetaliate(false);
				return;
			case 906 :
				player.setScreenBrightness(1);
				return;
			case 908 :
				player.setScreenBrightness(2);
				return;
			case 910 :
				player.setScreenBrightness(3);
				return;
			case 912 :
				player.setScreenBrightness(4);
				return;
			case 914 :
				player.setMouseButtons(1);
				return;
			case 913 :
				player.setMouseButtons(0);
				return;
			case 915 :
				player.setChatEffects(0);
				return;
			case 916 :
				player.setChatEffects(1);
				return;
			case 957 :
				player.setSplitPrivateChat(1);
				return;
			case 958 :
				player.setSplitPrivateChat(0);
				return;
			case 12464 :
				player.setAcceptAid(0);
				return;
			case 12465 :
				player.setAcceptAid(1);
				return;
			case 930 :// setMusicVolume (0/4)
				player.setMusicVolume(4);
				return;
			case 931 :// setMusicVolume (1/4)
				player.setMusicVolume(3);
				return;
			case 932 :// setMusicVolume (2/4)
				player.setMusicVolume(2);
				return;
			case 933 :// setMusicVolume (3/4)
				player.setMusicVolume(1);
				return;
			case 934 :// setMusicVolume (4/4)
				player.setMusicVolume(0);
				return;
			case 941 :// setEffectVolume (0/4)
				player.setEffectVolume(4);
				return;
			case 942 :// setEffectVolume (1/4)
				player.setEffectVolume(3);
				return;
			case 943 :// setEffectVolume (2/4)
				player.setEffectVolume(2);
				return;
			case 944 :// setEffectVolume (3/4)
				player.setEffectVolume(1);
				return;
			case 945 :// setEffectVolume (4/4)
				player.setEffectVolume(0);
				return;
			case 5386 :
				player.setWithdrawAsNote(true);
				return;
			case 5387 :
				player.setWithdrawAsNote(false);
				return;
			case 18885 :
				BankManager.bankInventory(player);
				return;	
			case 18886 :
				BankManager.bankEquipment(player);
				return;	
			case 8130 :
				player.setBankOptions(BankOptions.SWAP_ITEM);
				return;
			case 8131 :
				player.setBankOptions(BankOptions.INSERT_ITEM);
				return;
		}
		if (Barrows.solvePuzzle(player, buttonId))
			return;
		if (GeManager.geButtons(player, buttonId))
			return;
		if (MagicSkill.clickingToAutoCast(player, buttonId))
			return;
		if (player.getEquipment().setFightMode(buttonId)) {
			player.getEquipment().sendWeaponInterface();
			return;
		}
		if (player.getPrayer().setPrayers(buttonId)) {
			return;
		}
		/**
		 * All buttons after this part cannot be used while player's packets are
		 * disabled
		 */
		if (player.stopPlayerPacket()) {
			return;
		}
		switch (buttonId) {
			/** Destroy item **/
		
			case 14175 :
				if (player.getDestroyItem() != null) {
					BarrowsItem bi = BarrowsItem.getBarrowsItem(player.getDestroyItem());
					if(bi != null){
						if (player.getInventory().getItemContainer().contains(player.getDestroyItem().getId())) {
							player.getActionSender().sendSound(376, 1, 0);
					        if (!Constants.ADMINS_CAN_INTERACT && player.getStaffRights() >= 2) {
					            player.getActionSender().sendMessage("Your item disappears because you're an administrator.");
					        } else {
				                GroundItemManager.getManager().dropItem(new GroundItem(new Item(bi.getWear0(), 1), player));
					        }
							if (!player.getInventory().removeItemSlot(player.getDestroyItem(), player.getSlot())) {
								player.getInventory().removeItem(player.getDestroyItem());
							}
						}
						player.getEquipment().updateWeight();
					} else {
						if(player.getDestroyItem().getDefinition().getName().toLowerCase().contains("progress hat")){
							player.resetPizazzPoints();
						}
						player.getInventory().removeItem(player.getDestroyItem());
					}
				}
				player.setDestroyItem(null);
			case 14176 :
				player.setDestroyItem(null);
				player.getActionSender().removeInterfaces();
				break;
			/** Teleother **/
			case 12566 :
				//player.getTeleportation().teleportObelisk(player.getTeleotherPosition());
				player.getTeleportation().attemptTeleport(player.getTeleotherPosition());
			case 12568 :
				player.getActionSender().removeInterfaces();
				return;
			/** Emotes **/
			case 168 :
				player.getUpdateFlags().sendAnimation(855);
				return;
			case 169 :
				player.getUpdateFlags().sendAnimation(856);
				return;
			case 162 :
				player.getUpdateFlags().sendAnimation(857);
				return;
			case 164 :
				player.getUpdateFlags().sendAnimation(858);
				return;
			case 165 :
				player.getUpdateFlags().sendAnimation(859);
				return;
			case 161 :
				player.getUpdateFlags().sendAnimation(860);
				return;
			case 170 :
				player.getUpdateFlags().sendAnimation(861);
				return;
			case 171 :
				player.getUpdateFlags().sendAnimation(862);
				return;
			case 163 :
				player.getUpdateFlags().sendAnimation(863);
				return;
			case 167 :
				player.getUpdateFlags().sendAnimation(864);
				return;
			case 172 :
				player.getUpdateFlags().sendAnimation(865);
				return;
			case 166 :
				player.getUpdateFlags().sendAnimation(866);
				return;
			case 13362 :
				player.getUpdateFlags().sendAnimation(2105);
				return;
			case 13363 :
				player.getUpdateFlags().sendAnimation(2106);
				return;
			case 13364 :
				player.getUpdateFlags().sendAnimation(2107);
				return;
			case 13365 :
				player.getUpdateFlags().sendAnimation(2108);
				return;
			case 13366 :
				player.getUpdateFlags().sendAnimation(2109);
				return;
			case 13367 :
				player.getUpdateFlags().sendAnimation(2110);
				return;
			case 13368 :
				player.getUpdateFlags().sendAnimation(2111);
				return;
			case 13369 :
				player.getUpdateFlags().sendAnimation(2112);
				return;
			case 13370 :
				player.getUpdateFlags().sendAnimation(2113);
				return;
			case 11100 :
				player.getActionSender().stopPlayerPacket(4);
				player.getUpdateFlags().sendAnimation(0x558);
				player.getUpdateFlags().sendGraphic(574);
				return;
			case 667 :
				player.getUpdateFlags().sendAnimation(0x46B);
				return;
			case 6503 :
				player.getUpdateFlags().sendAnimation(0x46A);
				return;
			case 6506 :
				player.getUpdateFlags().sendAnimation(0x469);
				return;
			case 666 :
				player.getUpdateFlags().sendAnimation(0x468);
				return;
			case 13383 :
				player.getUpdateFlags().sendAnimation(0x84F);
				return;
			case 13384 :
				player.getUpdateFlags().sendAnimation(0x850);
				return;
			case 18691 :
				player.getUpdateFlags().sendAnimation(2836);
				return;
			case 18689 :
				player.getUpdateFlags().sendAnimation(3544);
				return;
			case 18688 :
				player.getUpdateFlags().sendAnimation(3543);
				return;
			case 18464 :
				player.getUpdateFlags().sendAnimation(3544);
				return;
			case 18465 :
				player.getUpdateFlags().sendAnimation(3543);
				return;
			case 15166 :
				player.getUpdateFlags().sendAnimation(2836);
				return;
			case 6020 :
				player.getActionSender().removeInterfaces();
				player.getEquipment().unequip(Constants.RING);
				return;
			case 3546 :
				TradeManager.acceptStageTwo(player);
				return;
			case 3420 :
				TradeManager.acceptStageOne(player);
				return;
			case 3651 :
				player.setAppearanceUpdateRequired(true);
				player.getUpdateFlags().setUpdateRequired(true);
				player.getActionSender().removeInterfaces();
				if(player.questStage[0] == 0){
					player.questStage[0] = 2;
					player.getQuestHandler().setTutorialStage();
				}
				return;
			case 2458 :
				if (!player.getInCombatTick().completed()) {
					player.getActionSender().sendMessage("You have to wait 10 seconds after combat in order to logout.");
					return;
				}
                if (player.inDuelArena()) {
                    player.getActionSender().sendMessage("You can't logout during a duel fight!");
                    return;
                }
                if (player.inWild(player.getPosition().getX(), player.getPosition().getY())){
                	if (player.getExitType() != Player.ExitType.LOGOUT || (player.getExitType() == null || player.getExitType() != ExitType.LOGOUT)) {
                		player.setExitType(Player.ExitType.LOGOUT);
                		player.setCountDownEscape(true);
                		player.escapeTimer = 17;
                		return;
                	}
                if (player.escapeTimer > 0) {
                	return;
                }
                }
                if (player.getPjTimer().completed()){
    				player.getActionSender().sendLogout();
    				player.disconnect();
    			}
				return;
		}
		if (TalkToEvent.isGenieLampButton(player, buttonId)) {
			return;
		}
		if (player.clickSpecialBar(buttonId)) {
			return;
		}
		if (GnomeGlider.flightButtons(player, buttonId)) {
			return;
		}
		if (Canoe.shapeButtons(player, buttonId)) {
			return;
		}
		if (EnchantStaff.staffButtons(player, buttonId)) {
			return;
		}
		if (Canoe.travelButtons(player, buttonId)) {
			return;
		}
		if (player.getSkillGuide().skillGuidesButton(buttonId)) {
			return;
		}
		if (player.getRandomInterfaceClick().handleButtonClicking(buttonId)) {
			return;
		}
		if (Sextant.handleSextantButtons(player, buttonId)) {
			return;
		}
		Spell spell = player.getMagicBookType().getSpells().get(buttonId);
		if (spell != null) {
			MagicSkill.spellButtonClicked(player, spell);
			return;
		}
		if (player.getDialogue().optionButtons(buttonId)) {
			return;
		}
		if (player.getBankPin().clickPinButton(buttonId)) {
			return;
		}
		if (player.getEmotes().activateEmoteButton(buttonId)) {
			return;
		}
		if (SkillsX.handleXButtons(player, buttonId)) {
			return;
		}
		if (DairyChurn.churnItem(player, buttonId)) {
			return;
		}
		if (PotteryMaking.makePottery(player, buttonId, 0)) {//few button ids not changed!
			return;
		}
		if (SilverCrafting.makeSilver(player, buttonId, 0)) {
			return;
		}
		if (Spinning.spin(player, buttonId, 0)) {
			return;
		}
		if (GlassMaking.makeSilver(player, buttonId, 0)) {
			return;
		}
		if (LeatherMakingHandler.handleButtons(player, buttonId, 0)) {
			return;
		}
		if (Tanning.handleButtons(player, buttonId)) {
			return;
		}
		if (Weaving.weave(player, buttonId, 0)) {
			return;
		}
		if (DramenBranch.cutDramen(player, buttonId, 0)) {
			return;
		}
		if (OrbCharging.chargeOrb(player, buttonId, 0)) {
			return;
		}
		if (HandleLogCutting.handleButtons(player, buttonId, 0)) {
			return;
		}
		if (Cooking.handleButtons(player, buttonId)) {
			return;
		}
		if (FlourRelated.handleButton(player, buttonId)) {
			return;
		}
		if (Smelting.handleSmelting(player, buttonId, 0)) {
			return;
		}
        if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG)
			System.out.println("button "+buttonId+" doesn't do anything");
	}
}
