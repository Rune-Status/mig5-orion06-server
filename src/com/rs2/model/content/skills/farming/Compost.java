package com.rs2.model.content.skills.farming;

import java.util.HashMap;
import java.util.Map;

import com.rs2.Constants;
import com.rs2.Server;
import com.rs2.model.Position;
import com.rs2.model.content.skills.GlobalToolConstants;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;
import com.rs2.util.Misc;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 22/02/12 Time: 15:43 To change
 * this template use File | Settings | File Templates.
 */
public class Compost {

	private Player player;

	public Compost(Player player) {
		this.player = player;
	}

	public int[] compostBins = new int[4];
	public int[] compostBinType = new int[4];
	public long[] compostBinsTimer = new long[4];

	/* setting up the experiences constants */

	public static final double COMPOST_EXP_RETRIEVE = 4.5;
	public static final double SUPER_COMPOST_EXP_RETRIEVE = 8.5;
	public static final double COMPOST_EXP_USE = 18;
	public static final double SUPER_COMPOST_EXP_USE = 26;

	public static final double ROTTEN_TOMATOES_EXP_RETRIEVE = 8.5;

	/* these are the constants related to compost making */

	public static final int COMPOST = 6032;

	public static final int SUPER_COMPOST = 6034;

	public static final int ROTTEN_TOMATO = 2518;

	public static final int TOMATO = 1982;

	public static final int[] COMPOST_ORGANIC = {239, 249, 251, 253, 255, 257, 259, 261, 263, 265, 267, 269, 753, 1942, 1951, 1957, 1965, 2126, 2481, 2998, 3000, 5504, 5986, 6018, 6055};

	public static final int[] SUPER_COMPOST_ORGANIC = {247, 2114, 5978, 5980, 5982, 6004, 6469};
	
	public static final int COMPOST_CONFIG = 511;
	/* this is the enum that stores the different locations of the compost bins */

	public enum CompostBinLocations {
		//NORTH_ARDOUGNE(0, new Position(2661, 3375, 0), FIRST_TYPE_COMPOST_BIN, 3), PHASMATYS(1, new Position(3610, 3522, 0), SECOND_TYPE_COMPOST_BIN, 1), FALADOR(2, new Position(3056, 3312, 0), FIRST_TYPE_COMPOST_BIN, 4), CATHERBY(3, new Position(2804, 3464, 0), FIRST_TYPE_COMPOST_BIN, 3);
		FALADOR(0, new Position(3056, 3312, 0)), CATHERBY(1, new Position(2804, 3464, 0)), PHASMATYS(2, new Position(3610, 3522, 0)), NORTH_ARDOUGNE(3, new Position(2661, 3375, 0));
		
		private int compostIndex;
		private Position binPosition;

		private static Map<Integer, CompostBinLocations> bins = new HashMap<Integer, CompostBinLocations>();

		static {
			for (CompostBinLocations data : CompostBinLocations.values()) {
				bins.put(data.compostIndex, data);
			}
		}

		CompostBinLocations(int compostIndex, Position binPosition) {
			this.compostIndex = compostIndex;
			this.binPosition = binPosition;
		}

		public static CompostBinLocations forId(int index) {
			return bins.get(index);
		}
		public static CompostBinLocations forPosition(Position position) {
			for (CompostBinLocations compostBinLocations : CompostBinLocations.values()) {
				if (compostBinLocations.binPosition.equals(position)) {
					return compostBinLocations;
				}
			}
			return null;
		}

		public int getCompostIndex() {
			return compostIndex;
		}

		public Position getBinPosition() {
			return binPosition;
		}

	}
	
	public void updateCompostStates() {
		int[] configValues = new int[compostBins.length];
		int configValue;
		for (int i = 0; i < compostBins.length; i++) {
			configValues[i] = getConfigValue(compostBins[i], compostBinType[i]);
		}
		//fala,camelot,canifis,ardy
		configValue = configValues[0] | configValues[1] << 8 | configValues[2] << 16 | configValues[3] << 24;
		player.getActionSender().sendConfig(COMPOST_CONFIG, configValue);
		
	}
	
	public int getConfigValue(int allotmentStage, int seedId) {
		int val = allotmentStage;
		if(allotmentStage >= 1 && allotmentStage <= 30){
			if(seedId == SUPER_COMPOST)
				val += 32;
			if(seedId == ROTTEN_TOMATO)
				val += 128;
		}
		return val;
	}
	
	/* handle what happens when the player close the compost bin */
	public boolean closeCompostBin(final int index) {
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return true;
		}
		if(compostBins[index] != 15)
			return true;
		if(compostBinType[index] == COMPOST)
			compostBins[index] = 64+35+Misc.random_(16);
		if(compostBinType[index] == SUPER_COMPOST)
			compostBins[index] = 32;
		if(compostBinType[index] == ROTTEN_TOMATO)
			compostBins[index] = 64+35+Misc.random_(16);
		compostBinsTimer[index] = Server.getMinutesCounter();
		player.getUpdateFlags().sendAnimation(835, 0);
		player.setStopPacket(true);
		CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.getActionSender().sendMessage("You close the compost bin, and its content start to rot.");
				updateCompostStates();
				container.stop();
			}

			@Override
			public void stop() {
				player.setStopPacket(false);
			}
		}, 2);
		return true;
	}

	/* handle what happens when the player opens the compost bin */
	public void openCompostBin(final int index) {
		boolean b = false;
		if(compostBinType[index] == COMPOST && compostBins[index] == 64)
			b = true;
		else if(compostBinType[index] == SUPER_COMPOST && compostBins[index] == 31)
			b = true;
		else if(compostBinType[index] == ROTTEN_TOMATO && compostBins[index] == 64)
			b = true;
		if (b) {
			/*if(compostBinType[index] == COMPOST)
				compostBins[index] = 30;
			else if(compostBinType[index] == SUPER_COMPOST)
				compostBins[index] = 30;
			else if(compostBinType[index] == ROTTEN_TOMATO)
				compostBins[index] = 30;*/
			compostBins[index] = 30;
			player.getUpdateFlags().sendAnimation(834, 0);
			CycleEventHandler.getInstance().addEvent(player, new CycleEvent() {

				@Override
				public void execute(CycleEventContainer container) {
					updateCompostStates();
					container.stop();
				}

				@Override
				public void stop() {
					player.setStopPacket(false);
				}
			}, 2);
		} else {
			player.getActionSender().sendMessage("The compost bin is still rotting. I should wait until it is complete.");
		}
	}

	/* handle compost bin filling */
	@SuppressWarnings("unused")
	public void fillCompostBin(final Position binPosition, final int organicItemUsed) {
		final CompostBinLocations compostBinLocations = CompostBinLocations.forPosition(binPosition);
		final int index = compostBinLocations.getCompostIndex();
		if (compostBinLocations == null) {
			return;
		}
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		if(compostBins[index] >= 15)
			return;
		int incrementFactor = 0;
		// setting up the different increments.
		for (int normalCompost : COMPOST_ORGANIC) {
			if (organicItemUsed == normalCompost) {
				compostBinType[index] = COMPOST;
				incrementFactor = 1;
			}
		}

		for (int superCompost : SUPER_COMPOST_ORGANIC) {
			if (organicItemUsed == superCompost) {
				if(compostBins[index] == 0)
					compostBinType[index] = SUPER_COMPOST;
				incrementFactor = 1;
			}
		}

		if (organicItemUsed == TOMATO) {
			if(compostBins[index] == 0)
				compostBinType[index] = ROTTEN_TOMATO;
			incrementFactor = 1;
		}

		// checking if the item used was an organic item.
		if (incrementFactor == 0) {
			player.getActionSender().sendMessage("You need to put organic items into the compost bin in order to make compost.");
			return;
		}
		final int factor = incrementFactor;
		// launching the main event for filling the compost bin.
		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task) || !player.getInventory().getItemContainer().contains(organicItemUsed) || compostBins[index] == 15) {
					container.stop();
					return;
				}
				player.getUpdateFlags().sendAnimation(832, 0);
				player.getInventory().removeItem(new Item(organicItemUsed));
				compostBins[index] += factor;
				updateCompostStates();

			}

			@Override
			public void stop() {
				player.resetAnimation();
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 2);
	}

	// handle what happens when the player retrieve the compost
	public void retrieveCompost(final int index) {
		if (!Constants.FARMING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		final int finalItem = compostBinType[index];

		final int task = player.getTask();
		player.getUpdateFlags().sendAnimation(832, 0);
		player.setSkilling(new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task) || (!player.getInventory().getItemContainer().contains(GlobalToolConstants.BUCKET) && compostBinType[index] != ROTTEN_TOMATO) || compostBins[index] < 16) {
					container.stop();
					//stop();
					return;
				}
				player.getSkill().addExp(Skill.FARMING, finalItem == COMPOST ? COMPOST_EXP_RETRIEVE : finalItem == SUPER_COMPOST ? SUPER_COMPOST_EXP_RETRIEVE : ROTTEN_TOMATOES_EXP_RETRIEVE);
				if (compostBinType[index] != ROTTEN_TOMATO) {
					player.getInventory().removeItem(new Item(GlobalToolConstants.BUCKET));
				}
				player.getInventory().addItem(new Item(finalItem));
				player.getUpdateFlags().sendAnimation(832, 0);
				compostBins[index]--;
				if (compostBins[index] < 16) {
					resetVariables(index);
				}
				updateCompostStates();
			}

			@Override
			public void stop() {
				player.resetAnimation();
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 2);
	}

	/* handling the item on object method */

	public boolean handleItemOnObject(int itemUsed, int objectId, int objectX, int objectY) {
		final CompostBinLocations allotmentFieldsData = CompostBinLocations.forPosition(new Position(objectX, objectY));
		if (allotmentFieldsData == null) {
			return false;
		}
		int index = allotmentFieldsData.getCompostIndex();
		if(compostBins[index] < 15){
			fillCompostBin(new Position(objectX, objectY), itemUsed);
			return true;
		}else if(compostBins[index] >= 16 && compostBins[index] <= 30){
			if (itemUsed == GlobalToolConstants.BUCKET) {
				retrieveCompost(index);
			} else {
				player.getActionSender().sendMessage("You might need some buckets to gather the compost.");
			}
			return true;
		}
		return false;
	}

	boolean hasOpenOption(int index){
		if(compostBinType[index] == COMPOST && compostBins[index] >= 64 && compostBins[index] <= 126)
			return true;
		if(compostBinType[index] == SUPER_COMPOST && (compostBins[index] == 31 || compostBins[index] == 32))
			return true;
		if(compostBinType[index] == ROTTEN_TOMATO && compostBins[index] >= 64 && compostBins[index] <= 126)
			return true;
		return false;
	}
	
	boolean notReady(int index){
		if(compostBinType[index] == COMPOST && compostBins[index] >= 65 && compostBins[index] <= 126)
			return true;
		if(compostBinType[index] == SUPER_COMPOST && compostBins[index] == 32)
			return true;
		if(compostBinType[index] == ROTTEN_TOMATO && compostBins[index] >= 65 && compostBins[index] <= 126)
			return true;
		return false;
	}
	
	public void doCalculations() {
		for (int i = 0; i < compostBins.length; i++) {
			if(!notReady(i))
				continue;
			long difference = Server.getMinutesCounter() - compostBinsTimer[i];
			if(difference <= 0)
				continue;
			if(compostBinType[i] == SUPER_COMPOST){
				if(difference >= 90)
					compostBins[i] = 31;
			}else{
				//compostBins[i]--;
				compostBins[i] -= difference;
				if(compostBins[i] <= 64)
					compostBins[i] = 64;
				compostBinsTimer[i] = Server.getMinutesCounter();
			}
		}
		updateCompostStates();
	}
	
	/* handling the object click method */

	public boolean handleObjectClick(int objectId, int objectX, int objectY) {
		final CompostBinLocations allotmentFieldsData = CompostBinLocations.forPosition(new Position(objectX, objectY));
		if (allotmentFieldsData == null) {
			return false;
		}
		int index = allotmentFieldsData.getCompostIndex();
		if(compostBins[index] == 15){
			closeCompostBin(index);
			return true;
		}else if(compostBins[index] >= 16 && compostBins[index] <= 30){
			retrieveCompost(index);
			return true;
		}else if(hasOpenOption(index)){
			openCompostBin(index);
			return true;
		}
		return false;
	}

	/* reseting the compost variables */

	public void resetVariables(int index) {
		compostBins[index] = 0;
		compostBinType[index] = 0;
		compostBinsTimer[index] = 0;
	}

	public int[] getCompostBins() {
		return compostBins;
	}

	public void setCompostBins(int i, int compostBins) {
		this.compostBins[i] = compostBins;
	}

	public int[] getCompostBinType() {
		return compostBinType;
	}

	public void setCompostBinType(int i, int compostBinType) {
		this.compostBinType[i] = compostBinType;
	}

	public long[] getCompostBinsTimer() {
		return compostBinsTimer;
	}

	public void setCompostBinsTimer(int i, long compostBinsTimer) {
		this.compostBinsTimer[i] = compostBinsTimer;
	}

}
