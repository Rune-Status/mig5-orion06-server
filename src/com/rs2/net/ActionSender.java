package com.rs2.net;

import java.awt.Color;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.rs2.Constants;
import com.rs2.Server;
import com.rs2.cache.object.CacheObject;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.Graphic;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.DailyTask;
import com.rs2.model.content.MessageOfTheWeek;
import com.rs2.model.content.questing.Quest;
import com.rs2.model.content.questing.QuestDefinition;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.magic.Spell;
import com.rs2.model.content.skills.magic.SpellBook;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.Player.BankOptions;
import com.rs2.model.players.item.Item;
import com.rs2.model.region.music.Music;
import com.rs2.model.region.music.RegionMusic;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.net.StreamBuffer.AccessType;
import com.rs2.net.StreamBuffer.ValueType;
import com.rs2.util.Misc;
import com.rs2.util.NameUtil;
import com.rs2.util.Palette;
import com.rs2.util.Palette.PaletteTile;
import com.rs2.util.Time;

public class ActionSender {

	private Player player;

	public ActionSender(Player player) {
		this.player = player;
	}

	public ActionSender sendMapState(int state) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2);
		out.writeHeader(player.getEncryptor(), 99);
		out.writeByte(state);
		player.send(out.getBuffer());
		return this;
	}
	
	public void playMusic(Music songDef){
		sendString(songDef.getName(), 4439);
		sendSong(songDef.getId());
	}

	public void stopPlayerPacket(int ticks) {
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.setStopPacket(false);
				container.stop();
			}

			@Override
			public void stop() {
			}
		}, ticks);
	}

	public ActionSender sendUpdateServer(int timer) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		out.writeHeader(player.getEncryptor(), 114);
		out.writeShort(timer, StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public void sendSideBarInterfaces() {
		int[] sidebars = { 2423, 3917, 638, 3213, 1644, 5608, 0, -1, 5065,
				5715, 2449, 904, 147, 962 };
		for (int i = 0; i < sidebars.length; i++) {
			sendSidebarInterface(i, sidebars[i]);
			if (i == 6) {
				if(player.getMagicBookType() == SpellBook.MODERN)
					sendSidebarInterface(i, 1151);
				if(player.getMagicBookType() == SpellBook.ANCIENT)
					sendSidebarInterface(i, 12855);
				if(player.getMagicBookType() == SpellBook.NECROMANCY)
					sendSidebarInterface(i, 19104);
			}
		}
		player.getEquipment().sendWeaponInterface();
	}

	public void enableSideBarInterfaces(int[] listSideBar) {
		int[] sidebars = { 2423, 3917, 638, 3213, 1644, 5608, 1151, -1, 5065,
				5715, 2449, 904, 147, 962 };
		for (int i = 0; i < listSideBar.length; i++) {
			sendSidebarInterface(listSideBar[i], sidebars[listSideBar[i]]);

		}
	}
	
	public void updateQuestTab(){
		for (int i = 1; i < QuestDefinition.QUEST_COUNT; i++) {
			QuestDefinition questDef = QuestDefinition.forId(i);
			int questButton = questDef.getButton();
			if(player.questStage[i] == 0)
	    		player.getActionSender().recolorComponent(questButton, Color.RED);//red
	    	if(player.questStage[i] == 1)
	    		player.getActionSender().recolorComponent(questButton, Color.GREEN);//gre
	    	if(player.questStage[i] >= 2)
	    		player.getActionSender().recolorComponent(questButton, Color.YELLOW);//yel
		}
	}
	
	public ActionSender sendCustomMapRegion(Palette palette) {
		if(player.isBot)
			return this;
        sendMapRegion();
        StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(100);
        out.writeVariablePacketHeader(player.getEncryptor(), 241);        
        out.writeShort(player.getPosition().getRegionY() + 6, ValueType.A);
        out.setAccessType(AccessType.BIT_ACCESS);
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 13; x++) {
                for (int y = 0; y < 13; y++) {
                    PaletteTile tile = palette.getTile(x, y, z);
                    out.writeBits(1, tile != null ? 1 : 0);
                    if (tile != null) {
                        out.writeBits(
                            26,
                            tile.getX() << 14 | tile.getY() << 3 | tile.getZ() << 24 | tile.getRotation() << 1);
                    }
                }
            }
        }
        out.setAccessType(AccessType.BYTE_ACCESS);
		out.writeShort(player.getPosition().getRegionX() + 6);
		out.finishVariableShortPacketHeader();
		player.send(out.getBuffer());        
        return this;
    }

	public ActionSender sendLogin() {
		if(!player.getHost().equals("127.0.0.1"))
			Server.getSingleton().backUpPlayers = true;
		sendDetails();
		sendPacket107();
		sendMapRegion();
		sendEnergy();
		updateQuestTab();
		sendChatSettings();
		player.getEquipment().updateWeight();
		/* farming */
		player.getCompost().doCalculations();
		player.getAllotment().doCalculations();
		player.getFlowers().doCalculations();
		player.getHerbs().doCalculations();
		player.getHops().doCalculations();
		player.getBushes().doCalculations();
		player.getAllotment().doCalculations();
		player.getTrees().doCalculations();
		player.getFruitTrees().doCalculations();
		player.getSpecialPlantOne().doCalculations();
		player.getSpecialPlantTwo().doCalculations();
        for(int i = 0; i < player.getPendingItems().length; i++){
            player.getInventory().addItem(new Item(player.getPendingItems()[i], player.getPendingItemsAmount()[i]));
        }
		for (int i = 0; i < 4; i++)
			player.getTrees().respawnStumpTimer(i);
		player.getPrivateMessaging().sendPMOnLogin();
		if(player.questStage[0] == 1){
			openWelcomeScreen();
			sendMessage("Welcome to "+Constants.SERVER_NAME);
			sendMessage("Type ::help to see available commands.");
			sendMessage("Report bugs by typing ::bug DESCRIBE THE BUG HERE");
			sendMessage(DailyTask.getDailyTaskAsString(player));
			if(player.restoredFromBackup){
				sendMessage("Your account file was somehow corrupted and had to be loaded from backup.");
			}
			player.loggedIn = true;
				
		}
		return this;
	}
	
	int[] messageOfTheWeek = new int[]{17511, // Question Type
									15819, // Christmas Type
									15812, // Security Type
									15801, // Item Scam Type
									15791, // Password Safety
									15774, // Good/Bad Password
									15767, // Drama Type
				};
	
	void openWelcomeScreen(){
		if(player.isBot)
			return;
		int d1;
		d1 = Time.daysBetween(player.lastLogin, System.currentTimeMillis());
		MessageOfTheWeek motw = MessageOfTheWeek.getNewMessageOfTheWeek(Server.messageOfTheWeek);
		openWelcomeScreen(30000, player.isDonator(), 0, parseIp(player.lastIP), d1, player.getDonatorPoints(), motw.getStyle());//(127 << 24)+1
		sendString("Official Channels\\n\\n" +
				"Forums: @lre@http://OSRSPK.com/\\n" +
				"Youtube: @lre@OSRSPKRSPS", 15270);
		boolean b = false;
		if(motw.getStyle() == 5993)
			b = true;
		sendString(motw.getTitle(), (b ? 6002 : motw.getStyle()+4));
		sendString(motw.getMessage()[0], (b ? 15491 : motw.getStyle()+2));
		sendString(motw.getMessage()[1], (b ? 15492 : motw.getStyle()+3));
	}
	
	public static int parseIp(String address) {
	    int result = 0;
	    int i = 0;
	    String[] address_ = new String[4];
	    for(String part : address.split(Pattern.quote("."))) {
	    	address_[i] = part;
	    	i++;
	    }
	    String address_a = address_[1]+"."+address_[0]+"."+address_[3]+"."+address_[2];
	    // iterate over each octet
	    for(String part : address_a.split(Pattern.quote("."))) {
	        // shift the previously parsed bits over by 1 byte
	        result = result << 8;
	        // set the low order bits to the current octet
	        result |= Integer.parseInt(part);
	    }
	    return result;
	}
	
	public ActionSender sendConfigsOnLogin() {
		resetAutoCastInterface();
		sendConfig(166, player.getScreenBrightness());// screenBrightness
		sendConfig(168, player.getMusicVolume());// musicVolume
		sendConfig(169, player.getEffectVolume());// effectVolume
		sendConfig(170, player.getMouseButtons());// mouseButtons
		sendConfig(171, player.getChatEffects());// chatEffects
		sendConfig(172, player.shouldAutoRetaliate() ? 0 : 1); // auto retaliate
		sendConfig(173, player.getMovementHandler().isRunToggled() ? 1 : 0); // runOption
		sendConfig(287, player.getSplitPrivateChat());// splitPrivateChat
		sendConfig(427, player.isAcceptingAid() ? 1 : 0);// acceptAid
		sendConfig(115, player.isWithdrawAsNote() ? 1 : 0); // withdrawItemAsNote
		sendConfig(304,
				player.getBankOptions().equals(BankOptions.SWAP_ITEM) ? 0 : 1);// swapItem
		return this;
	}

	public ActionSender openXInterface(int XInterfaceId) {
		player.setEnterXInterfaceId(XInterfaceId);
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(1);
		out.writeHeader(player.getEncryptor(), 27);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender statEdit(int stat, int edit, boolean hasLimit) {
		int lvl = player.getSkill().getPlayerLevel(stat) + edit;
		if (player.getStaffRights() > 1 && Constants.SERVER_DEBUG)
			System.out.println("current: " + player.getSkill().getPlayerLevel(stat) + " max: " + lvl);
		if (!hasLimit) {
			player.getSkill().getLevel()[stat] += edit;
			if (player.getSkill().getLevel()[stat] < 0) {
				player.getSkill().getLevel()[stat] = 0;
			}
			player.getSkill().refresh(stat);
			return this;
		}
		if (edit < 0) {
			if (player.getSkill().getLevel()[stat] < lvl) {
				return this;
			}
			if (player.getSkill().getLevel()[stat] + edit < lvl) {
				player.getSkill().getLevel()[stat] = lvl;
			} else {
				player.getSkill().getLevel()[stat] += edit;
			}
		} else {
			if (player.getSkill().getLevel()[stat] > lvl) {
				return this;
			}
			if (player.getSkill().getLevel()[stat] + edit > lvl) {
				player.getSkill().getLevel()[stat] = lvl;
			} else {
				player.getSkill().getLevel()[stat] += edit;
			}
		}
		player.getSkill().refresh(stat);
		return this;
	}

	public ActionSender walkTo2(int x, int y, final boolean crossing, int walkAnim, final int endAnim, int time, final double xp, final boolean wasRunning, final String message) {
		if (player.isStunned() || player.isFrozen()) {
			return this;
		}
		if(wasRunning)
			player.getMovementHandler().setRunToggled(false);
		player.setStopPacket(true);
		if (crossing)
			player.isCrossingObstacle = true;
		if (walkAnim > 0) {
			player.setWalkAnim(walkAnim);
			player.setAppearanceUpdateRequired(true);
		}
		player.getMovementHandler().reset();
		player.getMovementHandler().addToPath(
				new Position(player.getPosition().getX() + x, player
						.getPosition().getY() + y));
		player.getMovementHandler().finish();
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			boolean walked = false;

			@Override
			public void execute(CycleEventContainer b) {
				if (walked) { // && !player.isMoving
					player.setStopPacket(false);
					b.stop();
				}
				walked = true;
			}

			@Override
			public void stop() {
				player.setWalkAnim(-1);
				player.setAppearanceUpdateRequired(true);
				player.getActionSender().sendMessage(message);
				player.getSkill().addExp(Skill.AGILITY, xp);
				if(wasRunning)
					player.getMovementHandler().setRunToggled(true);
				if (crossing)
					player.isCrossingObstacle = false;
			}
		}, (endAnim > 0 ? time-3 : time));
		if(endAnim > 0){
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					player.getUpdateFlags().sendAnimation(endAnim);
					container.stop();
				}
				@Override
				public void stop() {
				}
			}, time);
		}
		return this;
	}
	
	public ActionSender walkTo(int x, int y, final boolean crossing) {
		if (player.isStunned() || player.isFrozen()) {
			return this;
		}
		player.setStopPacket(true);
		if (crossing)
			player.isCrossingObstacle = true;
		player.getMovementHandler().reset();
		player.getMovementHandler().addToPath(
				new Position(player.getPosition().getX() + x, player
						.getPosition().getY() + y));
		player.getMovementHandler().finish();
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			boolean walked = false;

			@Override
			public void execute(CycleEventContainer b) {
				if (walked) { // && !player.isMoving
					player.setStopPacket(false);
					b.stop();
				}
				walked = true;
			}

			@Override
			public void stop() {
				if (crossing)
					player.isCrossingObstacle = false;
			}
		}, 1);
		return this;
	}
	
	public ActionSender walkToWithTime(int x, int y, int time, final boolean crossing) {
		if (player.isStunned() || player.isFrozen()) {
			return this;
		}
		player.setStopPacket(true);
		if (crossing)
			player.isCrossingObstacle = true;
		player.getMovementHandler().reset();
		player.getMovementHandler().addToPath(
				new Position(player.getPosition().getX() + x, player
						.getPosition().getY() + y));
		player.getMovementHandler().finish();
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {
			boolean walked = false;

			@Override
			public void execute(CycleEventContainer b) {
				if (walked) { // && !player.isMoving
					player.setStopPacket(false);
					b.stop();
				}
				walked = true;
			}

			@Override
			public void stop() {
				if (crossing)
					player.isCrossingObstacle = false;
			}
		}, time);
		return this;
	}

	public ActionSender walkToNoForce(int x, int y) {
		if (player.isStunned() || player.isFrozen()) {
			return this;
		}
		player.getMovementHandler().reset();
		player.getMovementHandler().addToPath(
				new Position(player.getPosition().getX() + x, player
						.getPosition().getY() + y));
		player.getMovementHandler().finish();
		return this;
	}
	
	public ActionSender walkToNoForceAbs(int x, int y) {
		if (player.isStunned() || player.isFrozen()) {
			return this;
		}
		player.getMovementHandler().reset();
		player.getMovementHandler().addToPath(new Position(x, y));
		player.getMovementHandler().finish();
		return this;
	}

	public ActionSender createStillGfx(int id, int x, int y, int height,
			int time) {
		for (Player players : World.getPlayers()) {
			if (players == null) {
				continue;
			}
            if (players.getPosition().getZ() != height)
                continue;
			if (Misc.goodDistance(x, y, players.getPosition().getX(), players
					.getPosition().getY(), 25)) {
				players.getActionSender().sendStillGraphic(id,
						new Position(x, y, height), time);
			}
		}
		return this;
	}

	public ActionSender sendSkill(int skillID, int level, double exp) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 134);
		out.writeByte(skillID);
		out.writeInt((int) exp, StreamBuffer.ByteOrder.MIDDLE);
		out.writeByte(level);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendFrame230(int interfaceId, int rotation1,
			int rotation2, int zoom) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(10);
		out.writeHeader(player.getEncryptor(), 230);
		out.writeShort(zoom, StreamBuffer.ValueType.A);
		out.writeShort(interfaceId);
		out.writeShort(rotation1);
		out.writeShort(rotation2, StreamBuffer.ValueType.A,
				StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;

	}

	public ActionSender sendFrame106(int id) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2);
		out.writeHeader(player.getEncryptor(), 106);
		out.writeByte(id, ValueType.C);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendFrame70(int id, int x, int y) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 70);
		out.writeShort(id);
		out.writeShort(x, StreamBuffer.ByteOrder.LITTLE);
		out.writeShort(y, StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender updateFlashingSideIcon(int tabId) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 152);
		out.writeByte(tabId);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender createPlayerHints(int type, int id) {
		player.hintNpc = id;
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 254);
		out.writeByte(type);
		out.writeShort(id);
		out.writeByte(0 >> 16);
		out.writeByte(0 >> 8);
		out.writeByte(0);
		player.send(out.getBuffer());
		return this;
	}

	/**
	 * Orient : 2 Middle ? 3 West ? 4 East ? 5 South ? 6 North.
	 */
	public ActionSender createObjectHints(int x, int y, int height, int pos) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 254);
		out.writeByte(pos);
		out.writeShort(x);
		out.writeShort(y);
		out.writeByte(height);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendUpdateItem(Item item, int slot, int column,
			int amount) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(32);
		out.writeVariableShortPacketHeader(player.getEncryptor(), 34);
		out.writeShort(column); // Column Across Smith Screen
		// out.writeByte(4); // Total Rows?
		out.writeByte(slot); // Row Down The Smith Screen
		out.writeShort(item.getId() + 1); // item
		out.writeByte(amount); // how many there are?
		out.finishVariableShortPacketHeader();
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendUpdateItem(int slot, int inventoryId, Item item) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(32);
		out.writeVariableShortPacketHeader(player.getEncryptor(), 34);
		out.writeShort(inventoryId);
		out.writeByte(slot);
        if(item.getId() == 0){
            out.writeShort(0);
            out.writeByte(0);
        } else {
            out.writeShort(item.getId() + 1);
            if (item.getCount() > 254) {
                out.writeByte(255);
                out.writeShort(item.getCount());
            } else {
                out.writeByte(item.getCount());
            }
        }
		out.finishVariableShortPacketHeader();
		player.send(out.getBuffer());
		return this;
	}
	
	public ActionSender sendUpdateItem(int childId, int itemId, int itemAmount) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2048);
		out.writeVariableShortPacketHeader(player.getEncryptor(), 53);
		out.writeShort(childId);
		out.writeShort(1);
		if(itemId > 0){
			if (itemAmount > 254) {
				out.writeByte(255);
				out.writeInt(itemAmount, StreamBuffer.ByteOrder.INVERSE_MIDDLE);
			} else {
				out.writeByte(itemAmount);
			}
			out.writeShort(itemId + 1, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		} else {
			out.writeByte(0);
			out.writeShort(0, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		}
		out.finishVariableShortPacketHeader();
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendUpdateItems(int inventoryId, Item[] items) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2048);
		out.writeVariableShortPacketHeader(player.getEncryptor(), 53);
		out.writeShort(inventoryId);
		out.writeShort(items.length);
		for (Item item : items) {
			if (item != null) {
				if (item.getCount() > 254) {
					out.writeByte(255);
					out.writeInt(item.getCount(),
							StreamBuffer.ByteOrder.INVERSE_MIDDLE);
				} else {
					out.writeByte(item.getCount());
				}
				out.writeShort(item.getId() + 1, StreamBuffer.ValueType.A,
						StreamBuffer.ByteOrder.LITTLE);
			} else {
				out.writeByte(0);
				out.writeShort(0, StreamBuffer.ValueType.A,
						StreamBuffer.ByteOrder.LITTLE);
			}
		}
		out.finishVariableShortPacketHeader();
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendObject(int id, int x, int y, int h, int face,
			int type) {
		if(player.isBot)
			return this;
		sendCoords(new Position(x, y, h));
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 151);
		out.writeByte(0, StreamBuffer.ValueType.S);
		out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
		out.writeByte(((type << 2) + (face & 3)), StreamBuffer.ValueType.S);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendChatboxOverlay(int interfaceId) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		out.writeHeader(player.getEncryptor(), 218);
		out.writeShort(interfaceId, StreamBuffer.ValueType.A,
				StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendMessage(String message) {
		if(player.isBot)
			return this;
		if(message == null)
			return this;
		if (player.questStage[0] != 1) {
			return this;
			/*player.getDialogue().sendStatement(message);
			player.setClickId(0);*/
		}
		StreamBuffer.OutBuffer out = StreamBuffer
				.newOutBuffer(message.length() + 3);
		out.writeVariablePacketHeader(player.getEncryptor(), 253);
		out.writeString(message);
		out.finishVariablePacketHeader();
		player.send(out.getBuffer());
		return this;
	}
	
	public ActionSender sendMessage(Player sender, String message) {//yell
		if(player.isBot)
			return this;
		if(message == null)
			return this;
		if (player.questStage[0] != 1) {
			return this;
			/*player.getDialogue().sendStatement(message);
			player.setClickId(0);*/
		}
		long name = sender.getUsernameAsLong();
		int rights = sender.getStaffRights();
		if(rights == 0 && sender.isDonator())
			rights += 10;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(message.length() + 14);
		out.writeVariablePacketHeader(player.getEncryptor(), 98);
		out.writeByte(0);//type, unused currently
		out.writeLong(name);
		out.writeByte(rights);
		out.writeString(message);
		out.finishVariablePacketHeader();
		player.send(out.getBuffer());
		return this;
	}

	public void hideAllSideBars() {
		for (int i = 0; i < 14; i++)
			sendSidebarInterface(i, -1);
	}
	
	public void hideSideBars(int[] tab) {
		for (int i = 0; i < tab.length; i++)
			sendSidebarInterface(tab[i], -1);
	}

	public ActionSender sendSidebarInterface(int menuId, int form) {
        player.setSideBarInterfaceId(menuId, form);
        if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		out.writeHeader(player.getEncryptor(), 71);
		out.writeShort(form);
		out.writeByte(menuId, StreamBuffer.ValueType.A);
		player.send(out.getBuffer());
		return this;
	}
	
	public ActionSender sendGeItemSprite(int childId, int itemId) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 16);
		out.writeShort(childId);
		out.writeShort(itemId);
		player.send(out.getBuffer());
		return this;
	}
	
	public ActionSender sendGeProgressBar(int id, int progress) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		if(progress < 0)
			progress = 0;
		out.writeHeader(player.getEncryptor(), 18);
		out.writeShort(id);
		out.writeByte(progress);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendProjectile(Position position, int offsetX,
			int offsetY, int id, int startHeight, int endHeight, int speed,
			int lockon, boolean mage) {
		if(player.isBot)
			return this;
		sendCoordinates2(position);
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(16);
		out.writeHeader(player.getEncryptor(), 117);
		out.writeByte(50);
		out.writeByte(offsetY);
		out.writeByte(offsetX);
		out.writeShort(lockon);
		out.writeShort(id);
		out.writeByte(startHeight);
		out.writeByte(endHeight);
		out.writeShort(mage ? 50 : 41);
		out.writeShort(speed);
		out.writeByte(16);
		out.writeByte(64);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendProjectile(Position start, int size, int lockOn,
			byte offsetX, byte offsetY, int projectileId, int delay,
			int duration, int startHeight, int endHeight, int curve) {
		if(player.isBot)
			return this;
		if (size > 1) {
			sendCoordinates2(new Position(start.getX() + (size / 2), start.getY() + (size / 2)));
		} else {
			sendCoordinates2(start);
		}
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(16);
		out.writeHeader(player.getEncryptor(), 117);
		out.writeByte(50);
		out.writeByte(offsetX);
		out.writeByte(offsetY);
		out.writeShort(lockOn);
		out.writeShort(projectileId);
		out.writeByte(startHeight);
		out.writeByte(endHeight);
		out.writeShort(delay);
		out.writeShort(duration);
		out.writeByte(curve);
		out.writeByte(64);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendCoordinates2(Position position) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 85);
		int y = position.getY() - player.getCurrentRegion().getRegionY() * 8
				- 2;
		int x = position.getX() - player.getCurrentRegion().getRegionX() * 8
				- 3;
		out.writeByte(y, StreamBuffer.ValueType.C);
		out.writeByte(x, StreamBuffer.ValueType.C);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendMapRegion() {
		player.getCurrentRegion().setAs(player.getPosition());
        player.calculateLoadedLandscape();
        int baseX = player.getCurrentRegion().getRegionX() * 8;
        int baseY = player.getCurrentRegion().getRegionY() * 8;
        player.testX = player.getPosition().getX()-baseX;
        player.testY = player.getPosition().getY()-baseY;
        if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 73);
		out.writeShort(player.getPosition().getRegionX() + 6,
				StreamBuffer.ValueType.A);
		out.writeShort(player.getPosition().getRegionY() + 6);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendLogout() {
		player.logoutSent = true;
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(1);
		out.writeHeader(player.getEncryptor(), 109);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendInterface(int id) {
		player.setInterface(id);
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 97);
		out.writeShort(id);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendWalkableInterface(int id) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 208);
		out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendScrollInterface(int interfaceId, int scrollPosition) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(6);
		out.writeHeader(player.getEncryptor(), 79);
		out.writeShort(interfaceId, StreamBuffer.ByteOrder.LITTLE);
		out.writeShort(scrollPosition, StreamBuffer.ValueType.A);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendMultiInterface(boolean inMulti) {
		if(inMulti == player.multiIcon)
			return this;
		player.multiIcon = inMulti;
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 61);
		out.writeByte(inMulti ? 1 : 0);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendInterface(int interfaceId, int inventoryId) {
		player.setInterface(interfaceId);
        player.setSideBarOpen(inventoryId);
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 248);
		out.writeShort(interfaceId, StreamBuffer.ValueType.A);
		out.writeShort(inventoryId);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender flashSideBarIcon(int barId) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 24);
		out.writeByte(-barId, StreamBuffer.ValueType.A);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender openWelcomeScreen(int recoveryChange, boolean isDonator, int messages, int lastLoginIP, int lastLogin, int donatorPoints, int messageOfTheWeek)
	{
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(16);
		out.writeHeader(player.getEncryptor(), 176);
		out.writeShort(recoveryChange);
		out.writeShort(messages, StreamBuffer.ValueType.A);
		out.writeByte(isDonator ? 1 : 0);
		out.writeInt(lastLoginIP);
		out.writeShort(lastLogin);
		out.writeShort(donatorPoints);
		out.writeShort(messageOfTheWeek);
		player.send(out.getBuffer());
		return this;
	}
	
	public ActionSender removeInterfaces() {
		player.setInterface(0);
        player.setSideBarOpen(0);
        player.readingBook = 0;
		player.readingBookPage = 0;
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2);
		out.writeHeader(player.getEncryptor(), 219);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendCoords(Position position) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 85);
		int y = position.getY() - 8 * player.getCurrentRegion().getRegionY();
		int x = position.getX() - 8 * player.getCurrentRegion().getRegionX();
		out.writeByte(y, StreamBuffer.ValueType.C);
		out.writeByte(x, StreamBuffer.ValueType.C);
		player.send(out.getBuffer());
		return this;
	}

    public ActionSender sendGroundItem(com.rs2.model.ground.GroundItem groundItem) {
    	if(player.isBot)
			return this;
        sendCoords(groundItem.getPosition());
        StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(6);
        out.writeHeader(player.getEncryptor(), 44);
        out.writeShort(groundItem.getItem().getId(), StreamBuffer.ValueType.A,
                StreamBuffer.ByteOrder.LITTLE);
        out.writeShort(groundItem.getItem().getCount());
        out.writeByte(0);
        player.send(out.getBuffer());
        return this;
    }

    public ActionSender sendChatSettings() {
    	if(player.isBot)
			return this;
        StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
        out.writeHeader(player.getEncryptor(), 206);
        out.writeByte(player.getPublicChatMode());
        out.writeByte(player.getPrivateChatMode());
        out.writeByte(player.getTradeMode());
        player.send(out.getBuffer());
        return this;
    }

    public ActionSender removeGroundItem(com.rs2.model.ground.GroundItem groundItem) {
    	if(player.isBot)
			return this;
        sendCoords(groundItem.getPosition());
        StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
        out.writeHeader(player.getEncryptor(), 156);
        out.writeByte(0, StreamBuffer.ValueType.S);
        out.writeShort(groundItem.getItem().getId());
        player.send(out.getBuffer());
        return this;

    }

	public ActionSender sendConfig(int id, int value) {
		if(player.isBot)
			return this;
		if (value < 128 && -128 <= value) {
			StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
			out.writeHeader(player.getEncryptor(), 36);
			out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
			out.writeByte(value);
			player.send(out.getBuffer());
		} else {
			StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
			out.writeHeader(player.getEncryptor(), 87);
			out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
			out.writeInt(value, StreamBuffer.ByteOrder.MIDDLE);
			player.send(out.getBuffer());
		}
		return this;
	}
	
	public ActionSender recolorComponent(int interfaceId, Color colour) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 122);
		out.writeShort(interfaceId, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		int r = (colour.getRed() >> 3) & 0x1F;
        int g = (colour.getGreen() >> 3) & 0x1F;
        int b = (colour.getBlue() >> 3) & 0x1F;
		out.writeShort(((r << 10) | (g << 5) | b), StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}
	
	public ActionSender recolorComponent(int widget, int red, int green, int blue) {
		if(player.isBot)
			return this;
		red >>= 3;
		green >>= 3;
		blue >>= 3;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 122);
		out.writeShort(widget, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		out.writeShort((red << 10) | (green << 5) | (blue), StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendString(String message, int interfaceId) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer
				.newOutBuffer(message.length() + 6);
		out.writeVariableShortPacketHeader(player.getEncryptor(), 126);
		out.writeString(message);
		out.writeShort(interfaceId, StreamBuffer.ValueType.A);
		out.finishVariableShortPacketHeader();
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendFriendList(long name, int world) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(10);
		out.writeHeader(player.getEncryptor(), 50);
		if (world != 0) {
			world += 9;
		}
		out.writeLong(name);
		out.writeByte(world);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendPMServer(int state) { // IMPROVED && CONVERTED
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2);
		out.writeHeader(player.getEncryptor(), 221);
		out.writeByte(state);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendPrivateMessage(long name, int rights,
			byte[] message, int messageSize) {
		if(player.isBot)
			return this;
		// TODO: FIXME
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2048);
		out.writeVariablePacketHeader(player.getEncryptor(), 196);
		out.writeLong(name);
		out.writeInt(player.getPrivateMessaging().getLastPrivateMessageId());
		out.writeByte(rights);
		out.writeBytes(message, messageSize);
		out.finishVariablePacketHeader();
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendItemOnInterface(int id, int zoom, int model) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 246);
		out.writeShort(id == 0 ? -1 : id, StreamBuffer.ByteOrder.LITTLE);
		out.writeShort(zoom);
		out.writeShort(model);
		player.send(out.getBuffer());
		return this;
	}
	
	public ActionSender sendModelOnInterface(int id, int model) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 8);
		out.writeShort(id, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		out.writeShort(model);
		player.send(out.getBuffer());
		return this;
	}
	
    public ActionSender sendStillGraphic(Graphic graphic, Position position) {
        return sendStillGraphic(graphic.getId(), position, graphic.getValue());
    }

	public ActionSender sendStillGraphic(int graphicId, Position pos, int delay) {
		if(player.isBot)
			return this;
		sendCoords(pos);
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 4);
		out.writeByte(0);
		out.writeShort(graphicId);
		out.writeByte(pos.getZ());
		out.writeShort(delay);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendChatInterface(int id) {
		player.setInterface(id);
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 164);
		out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendDialogueAnimation(int modelId, int animId) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 200);
		out.writeShort(modelId);
		out.writeShort(animId);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender animateObject(int objx, int objy, int objz,
			int animationID) {
		if(player.isBot)
			return this;
		CacheObject object = ObjectLoader.object(objx, objy, objz);
		if (object == null)
			return this;
		sendCoords(new Position(objx, objy, objz));
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 160);
		out.writeByte(0, StreamBuffer.ValueType.S);
		out.writeByte((object.getType() << 2) + (object.getRotation() & 3),
				StreamBuffer.ValueType.S);
		out.writeShort(animationID, StreamBuffer.ValueType.A);
		player.send(out.getBuffer());
		return this;

	}
	
	public ActionSender animateGameObject(int objx, int objy, int objz,
			int animationID) {
		if(player.isBot)
			return this;
		GameObject object = ObjectHandler.getInstance().getObject(objx, objy, objz);
		if (object == null)
			return this;
		sendCoords(new Position(objx, objy, objz));
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 160);
		out.writeByte(0, StreamBuffer.ValueType.S);
		out.writeByte((object.getDef().getType() << 2) + (object.getDef().getFace() & 3),
				StreamBuffer.ValueType.S);
		out.writeShort(animationID, StreamBuffer.ValueType.A);
		player.send(out.getBuffer());
		return this;

	}

	public ActionSender sendPlayerDialogueHead(int interfaceId) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 185);
		out.writeShort(interfaceId, StreamBuffer.ValueType.A,
				StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendNPCDialogueHead(int npcId, int interfaceId) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 75);
		out.writeShort(npcId, StreamBuffer.ValueType.A,
				StreamBuffer.ByteOrder.LITTLE);
		out.writeShort(interfaceId, StreamBuffer.ValueType.A,
				StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendSound(int id, int type, int delay) {
		if(player.isBot)
			return this;
		if(id < 0)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 174);
        out.writeShort(id);
		out.writeByte(type); out.writeShort(delay);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendSong(int id) {
		if(player.isBot)
			return this;
		if (player.currentSong == id)
			return this;
		player.currentSong = id;
		if (id != -1) {
			StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
			out.writeHeader(player.getEncryptor(), 74);
			out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
			player.send(out.getBuffer());
		}
		return this;
	}

	public ActionSender sendQuickSong(int id, int songDelay) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 121);
		out.writeShort(id, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		out.writeShort(songDelay, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.BIG);//256?
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendPlayerOption(String option, int slot, boolean top) {
		if(player.isBot)
			return this;
		if(slot >= 1 && slot <= 5){
			if(player.atPlayerActions[slot-1] != null)
			if(player.atPlayerActions[slot-1].equals(option))
				return this;
			player.atPlayerActions[slot-1] = option;
		}
		StreamBuffer.OutBuffer out = StreamBuffer
				.newOutBuffer(option.length() + 5);
		out.writeVariablePacketHeader(player.getEncryptor(), 104);
		out.writeByte(slot, StreamBuffer.ValueType.C);
		out.writeByte(top ? 1 : 0, StreamBuffer.ValueType.A);
		out.writeString(option);
		out.finishVariablePacketHeader();
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendDetails() {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		out.writeHeader(player.getEncryptor(), 249);
		out.writeByte(1, StreamBuffer.ValueType.A);
		out.writeShort(player.getIndex(), StreamBuffer.ValueType.A,
				StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendEnergy() {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2);
		out.writeHeader(player.getEncryptor(), 110);
		out.writeByte((int) player.getEnergy());
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendWeight() {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 240);
		out.writeShort((int) Math.floor(player.getWeight()));
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendPacket107() {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(1);
		out.writeHeader(player.getEncryptor(), 107);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendDuelEquipment(int itemId, int amount, int slot) {
		if(player.isBot)
			return this;
		if (itemId != 0) {
			StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(32);
			out.writeHeader(player.getEncryptor(), 34);
			out.writeShort(13824);
			out.writeByte(slot);
			out.writeShort(itemId + 1);
			if (amount > 254) {
				out.writeByte(255);
				out.writeInt(amount, StreamBuffer.ValueType.STANDARD,
						StreamBuffer.ByteOrder.BIG);
			} else {
				out.writeByte(amount);

			}
			out.finishVariableShortPacketHeader();
			player.send(out.getBuffer());
			return this;
		}
		return this;

	}

	public void updateAutoCastInterface(Spell spell) {
		String spellName = NameUtil.uppercaseFirstLetter(spell.name()
				.toLowerCase().replaceAll("_", " "));
		sendString(spellName, 352);
		sendConfig(108, 3);
		sendConfig(43, 3);
	}

	public void resetAutoCastInterface() {
		if (player.getAutoSpell() == null) {
			sendConfig(108, 0);
			sendConfig(43, player.getFightMode());
			sendString("", 352);
			return;
		}
		sendConfig(43, player.getFightMode());
		sendConfig(108, 2);
	}

	public ActionSender sendFrame171(int mainFrame, int subFrame) {//mainFrame: 1 hide/0 show, subFrame: subInterfaceId
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		out.writeHeader(player.getEncryptor(), 171);
		out.writeByte(mainFrame);
		out.writeShort(subFrame);
		player.send(out.getBuffer());
		return this;
	}
	
	/**
     * Shakes the player's screen.
     * Parameters 1, 0, 0, 0 to reset.
     * @param verticleAmount How far the up and down shaking goes (1-4).
     * @param verticleSpeed How fast the up and down shaking is.
     * @param horizontalAmount How far the left-right tilting goes.
     * @param horizontalSpeed How fast the right-left tiling goes..
     */
	public ActionSender sendScreenShake(int verticleAmount, int verticleSpeed, int horizontalAmount, int horizontalSpeed) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 35);
		out.writeByte(verticleAmount);
		out.writeByte(verticleSpeed);
		out.writeByte(horizontalAmount);
		out.writeByte(horizontalSpeed);
		player.send(out.getBuffer());
		return this;
	}
	
	public ActionSender setInterfaceChildVisibility(int subFrame, boolean visible) {//mainFrame: 1 hide/0 show, subFrame: subInterfaceId
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		out.writeHeader(player.getEncryptor(), 171);
		out.writeByte(visible ? 0 : 1);
		out.writeShort(subFrame);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendSpecialBar(int mainFrame, int subFrame) {
		return this;
	}

	public ActionSender sendComponentInterface(int interfaceId, int modelId) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(6);
		out.writeHeader(player.getEncryptor(), 8);
		out.writeShort(interfaceId, StreamBuffer.ValueType.A,
				StreamBuffer.ByteOrder.LITTLE);
		out.writeShort(modelId);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender updateSpecialBar(int amount, int barId) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 70);
		out.writeShort(amount);
		out.writeShort(0, StreamBuffer.ByteOrder.LITTLE);
		out.writeShort(barId, StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}
	
	public ActionSender moveInterfaceComponent(int componentId, int x, int y) {
		if(player.isBot)
			return this;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 70);
		out.writeShort(x);
		out.writeShort(y, StreamBuffer.ByteOrder.LITTLE);
		out.writeShort(componentId, StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public void updateSpecialBarText(int specialBarId) {
		if (player.isSpecialAttackActive()) {
			sendConfig(301, 1);
		} else {
			sendConfig(301, 0);
		}
	}

	public void updateSpecialAmount(int barId) {
		int specialCheck = 10;
		byte specialAmount = (byte) (player.getSpecialAmount() / 10);
		for (int i = 0; i < 10; i++) {
			updateSpecialBar(specialAmount >= specialCheck ? 500 : 0, --barId);
			specialCheck--;
		}
	}

	public void sendFullScreenInterface(int interfaceId, int secondVar) {
		if(player.isBot)
			return;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 69);
		out.writeShort(interfaceId);
		out.writeShort(secondVar);
		player.send(out.getBuffer());
	}

	public void stillCamera(int x, int y, int height, int speed, int angle) {
		if(player.isBot)
			return;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 177);
		out.writeByte(x / 64);
		out.writeByte(y / 64);
		out.writeShort(height);
		out.writeByte(speed);
		out.writeByte(angle);
		player.send(out.getBuffer());
	}

	public void spinCamera(int i1, int i2, int i3, int i4, int i5) {
		if(player.isBot)
			return;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 166);
		out.writeByte(i1 / 64);
		out.writeByte(i2 / 64);
		out.writeShort(i3);
		out.writeByte(i4);
		out.writeByte(i5);
		player.send(out.getBuffer());
	}

	public void resetCamera() {
		if(player.isBot)
			return;
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(1);
		out.writeHeader(player.getEncryptor(), 107);
		player.send(out.getBuffer());
		player.getUpdateFlags().setUpdateRequired(true);
	}

	public void sendWelcomeScreen() {
		sendFullScreenInterface(5993, 15244);
	}

	public void walkThroughTutIsGate(final int id1, final int id2,
			final int x1, final int y1, final int x2, final int y2, final int h) {
		final CacheObject g = ObjectLoader.object(id1, x1, y1, h);
		final CacheObject g2 = ObjectLoader.object(id2, x2, y2, h);
		final int z = player.getPosition().getZ();
		new GameObject(id1, x1 + 1, y1, z, g.getRotation() - 4, 0,
				Constants.EMPTY_OBJECT, 3, false);
		new GameObject(id2, x2 + 2, y2 + 1, z, g2.getRotation() - 1, 0,
				Constants.EMPTY_OBJECT, 3, false);
		new GameObject(Constants.EMPTY_OBJECT, x1, y1, z, g2.getRotation(), 0,
				id1, 3, false);
		new GameObject(Constants.EMPTY_OBJECT, x2, y2, z, g2.getRotation(), 0,
				id2, 3, false);
	}

	public void walkThroughDoor2(final int id1, final int id2, final int x1,
			final int y1, final int x2, final int y2, final int h) {
		final CacheObject g1 = ObjectLoader.object(id1, x1, y1, h);
		final CacheObject g2 = ObjectLoader.object(id2, x2, y2, h);
		final int z = player.getPosition().getZ();
		new GameObject(Constants.EMPTY_OBJECT, x1, y1, z, g1.getRotation(), 0,
				id1, 3, false);
		new GameObject(Constants.EMPTY_OBJECT, x2, y2, z, g2.getRotation() + 1,
				0, id2, 3, false);
		new GameObject(id1, x1 + 1, y1, z, g1.getRotation() + 1, 0,
				Constants.EMPTY_OBJECT, 3, false);
		new GameObject(id2, x2 + 1, y2, z, g2.getRotation(), 0,
				Constants.EMPTY_OBJECT, 3, false);
		sendSound(318, 1, 0);
	}

	public void walkThroughDoor3(final int id1, final int id2, final int x1,
			final int y1, final int x2, final int y2, final int h) {
		final CacheObject g1 = ObjectLoader.object(id1, x1, y1, h);
		final CacheObject g2 = ObjectLoader.object(id2, x2, y2, h);
		final int z = player.getPosition().getZ();
		new GameObject(Constants.EMPTY_OBJECT, x1, y1, z, g1.getRotation(), 0,
				id1, 3, false);
		new GameObject(Constants.EMPTY_OBJECT, x2, y2, z, g2.getRotation(), 0,
				id2, 3, false);
		new GameObject(id1, x1 - 1, y1, z, g1.getRotation() + 3, 0,
				Constants.EMPTY_OBJECT, 3, false);
		new GameObject(id2, x2 - 1, y2, z, g2.getRotation() + 1, 0,
				Constants.EMPTY_OBJECT, 3, false);
		sendSound(318, 1, 0);
	}

	public void walkThroughDoor2(final int id1, final int x1, final int y1,
			final int h) {
		final CacheObject g = ObjectLoader.object(id1, x1, y1, h);
		new GameObject(id1, x1, y1, h, g.getRotation() - 1, 0, id1, 2,
				g.getRotation() - 2, x1, y1, false);
		sendSound(318, 1, 0);
	}

	public void walkThroughDoor3(final int id1, final int x1, final int y1,
			final int h) {
		final CacheObject g = ObjectLoader.object(id1, x1, y1, h);
		new GameObject(id1, x1, y1, h, g.getRotation() - 2, 0, id1, 3,
				g.getRotation() - 3, x1, y1, false);
		sendSound(318, 1, 0);
	}

	public void walkThroughDoor(final int id1, final int x1, final int y1,
			final int h) {
		final CacheObject g = ObjectLoader.object(id1, x1, y1, h);
		new GameObject(id1, x1, y1, h, g.getRotation() - 1, 0, id1, 2,
				g.getRotation(), x1, y1, false);
		sendSound(318, 1, 0);
	}
	
	public void walkThroughDoorDiagonal(final int id1, final int x1, final int y1,
			final int h) {
		final CacheObject g = ObjectLoader.object(id1, x1, y1, h);
		new GameObject(Constants.EMPTY_OBJECT, x1, y1, h, g.getRotation() - 1, g.getType(), id1, 1,
				g.getRotation(), x1, y1, false);
		new GameObject(id1, x1, y1-1, h, g.getRotation() - 1, g.getType(), Constants.EMPTY_OBJECT, 1,
				g.getRotation(), x1, y1, false);
		/*new GameObject(id1, x1, y1, h, g.getRotation() - 1, g.getType(), id1, 2,
				g.getRotation(), x1, y1, false);*/
		sendSound(318, 1, 0);
	}

	public void walkThroughDoubleDoor(final int id1, final int id2,
			final int x1, final int y1, final int x2, final int y2, final int h) {
		final CacheObject g1 = ObjectLoader.object(id1, x1, y1, h);
		final CacheObject g2 = ObjectLoader.object(id2, x2, y2, h);
		new GameObject(id1, x1, y1, h, g1.getRotation() - 1, 0, id1, 1,
				g1.getRotation(), x1, y1, false);
		new GameObject(id2, x2, y2, h, g2.getRotation() + 1, 0, id2, 1,
				g2.getRotation(), x2, y2, false);
		sendSound(318, 1, 0);
	}
	
	public void walkThroughDoubleDoor2(final int id1, final int id2,
			final int x1, final int y1, final int x2, final int y2, final int h) {
		final CacheObject g1 = ObjectLoader.object(id1, x1, y1, h);
		final CacheObject g2 = ObjectLoader.object(id2, x2, y2, h);
		new GameObject(Constants.EMPTY_OBJECT, x1, y1, h, g1.getRotation() - 1, 0, id1, 1,
				g1.getRotation(), x1, y1, false);
		new GameObject(Constants.EMPTY_OBJECT, x2, y2, h, g2.getRotation() + 1, 0, id2, 1,
				g2.getRotation(), x2, y2, false);
		new GameObject(id1, x1, y1+1, h, g1.getRotation() - 1, 0, Constants.EMPTY_OBJECT, 1,
				g1.getRotation(), x1, y1, false);
		new GameObject(id2, x2, y2+1, h, g2.getRotation() + 1, 0, Constants.EMPTY_OBJECT, 1,
				g2.getRotation(), x2, y2, false);
		sendSound(318, 1, 0);
	}

}
