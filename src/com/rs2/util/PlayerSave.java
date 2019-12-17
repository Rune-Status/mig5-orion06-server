package com.rs2.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

import com.rs2.Constants;
import com.rs2.Server;
import com.rs2.model.World;
import com.rs2.model.content.GeOffer;
import com.rs2.model.content.hiscore.Hplayer;
import com.rs2.model.content.minigames.barrows.Barrows;
import com.rs2.model.content.questing.QuestDefinition;
import com.rs2.model.content.skills.magic.SpellBook;
import com.rs2.model.content.treasuretrails.Puzzle;
import com.rs2.model.objects.functions.FlourMill;
import com.rs2.model.players.BankManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.net.packet.packets.AppearancePacketHandler;
import com.rs2.util.sql.SQLCompletionHandler;
import com.rs2.util.sql.SQLEngine;
import com.rs2.util.sql.SQLWorker;
import com.rs2.util.sql.impl.PlayerLoader;
import com.rs2.util.sql.impl.PlayerSaver;

/**
 * Static utility methods for saving and loading players.
 * 
 * @author blakeman8192
 */
public class PlayerSave {

	/** The directory where players are saved. */
	public static final String directory = "./data/characters/";
	public static final String backup_directory = "./data/backups/";
	
	public static final String directory_logs = "./data/logs/";

	static int[] musicCfg = {20, 21, 22, 23, 24, 25, 298, 311, 346, 414, 464, 598, 662, 721};
	
	final static int saveFileVersion = 15;//remember to change this everytime u change the data saved!
	
	/**
	 * Saves the player.
	 * 
	 * @param player
	 *            the player to save
	 * @return
	 */
	public static void save(final Player player) {
		try {
            if(Constants.MYSQL_ENABLED) {
                /* TODO: Add back up? */
                PlayerSaver saver = new PlayerSaver(player);
                SQLEngine.getGameDatabase().execute(saver.createPlayerInformationSaver(), new SQLCompletionHandler() {
                    @Override
                    public void onComplete(ResultSet results) throws SQLException {}

                    @Override
                    public void onException(Exception ex) {
                        ex.printStackTrace();
                    }
                });

                SQLEngine.getGameDatabase().execute(saver.createBankSaver(), new SQLCompletionHandler() {
                    @Override
                    public void onComplete(ResultSet results) throws SQLException {}

                    @Override
                    public void onException(Exception ex) {
                        ex.printStackTrace();
                    }
                });

                SQLEngine.getGameDatabase().execute(saver.createInventorySaver(), new SQLCompletionHandler() {
                    @Override
                    public void onComplete(ResultSet results) throws SQLException {}

                    @Override
                    public void onException(Exception ex) {
                        ex.printStackTrace();
                    }
                });

                SQLEngine.getGameDatabase().execute(saver.createEquipmentSaver(), new SQLCompletionHandler() {
                    @Override
                    public void onComplete(ResultSet results) throws SQLException {

                    }

                    @Override
                    public void onException(Exception ex) {
                        ex.printStackTrace();
                    }
                });

                SQLEngine.getGameDatabase().execute(saver.createContactSaver(), new SQLCompletionHandler() {
                    @Override
                    public void onComplete(ResultSet results) throws SQLException {}

                    @Override
                    public void onException(Exception ex) {
                        ex.printStackTrace();
                    }
                });

                SQLEngine.getGameDatabase().execute(saver.createSkillsSaver(), new SQLCompletionHandler() {
                    @Override
                    public void onComplete(ResultSet results) throws SQLException {}

                    @Override
                    public void onException(Exception ex) {
                        ex.printStackTrace();
                    }
                });
            } else {
                @SuppressWarnings("unused")
                Misc.Stopwatch stopwatch = new Misc.Stopwatch();
                if(System.currentTimeMillis() - player.lastSave < 50)
                	return;
                File file = new File(directory + player.getUsername() + ".dat");
                if (!file.exists()) {
                	file.createNewFile();
                }
                FileOutputStream outFile = new FileOutputStream(file);
                DataOutputStream write = new DataOutputStream(outFile);
                 write.writeShort(saveFileVersion);
                 write.writeUTF(player.getUsername());
                 write.writeUTF(player.getPassword());
                 write.writeUTF(player.getHost());
                 write.writeInt(player.getStaffRights());
                 write.writeUTF("");//email
                 write.writeUTF(player.getPaypal());//paypal
                 write.writeUTF(player.getLinkedAcc());//linked account
                 write.writeLong(System.currentTimeMillis());
                 if(Time.getHours(player.getPlayingTimeTotal()) >= 100000){//should no longer happen
                	 write.writeLong(player.prev_playingTime);
                	 player.appendToErrorList("Couldn't save playing time, prev: "+player.prev_playingTime+" login: "+player.loginTime+" current: "+System.currentTimeMillis());
                 }else{
                	 write.writeLong(player.getPlayingTimeTotal());
                 }
                 write.writeLong(player.accountCreated);
                 write.writeBoolean(player.testReward);//should be rewarded for playing on test week
                 write.writeBoolean(player.isDonator());
                 write.writeInt(player.getDonatorPoints());
                write.writeInt(player.getPosition().getX());
                write.writeInt(player.getPosition().getY());
                write.writeInt(player.getPosition().getZ());
                write.writeInt(player.getGender());
                //statistics below
                write.writeInt(player.npcKillCount);
                write.writeInt(player.playerKillCount);
                write.writeInt(player.numberOfDeaths);
                write.writeInt(player.easyCluesDone);
                write.writeInt(player.mediumCluesDone);
                write.writeInt(player.hardCluesDone);
                write.writeInt(player.soldItemsWorth);
                write.writeInt(player.boughtItemsWorth);
                write.writeInt(player.duelWins);
                write.writeInt(player.duelLosses);
                write.writeInt(player.totalDonationAmount);
                //statistics above
                write.writeBoolean(player.shouldAutoRetaliate());
                write.writeInt(player.getFightMode());
                write.writeInt(player.getScreenBrightness());
                write.writeInt(player.getMouseButtons());
                write.writeInt(player.getChatEffects());
                write.writeInt(player.getSplitPrivateChat());
                write.writeInt(player.getAcceptAid());
                write.writeInt(player.getMusicVolume());
                write.writeInt(player.getEffectVolume());
                write.writeDouble(player.getSpecialAmount());

                write.writeBoolean(player.getBankPin().isChangingBankPin());
                write.writeBoolean(player.getBankPin().isDeletingBankPin());
                write.writeInt(player.getBankPin().getPinAppendYear());
                write.writeInt(player.getBankPin().getPinAppendDay());
                write.writeInt(player.getBindingNeckCharge());
                write.writeInt(player.getRingOfForgingLife());
                write.writeInt(player.getRingOfRecoilLife());

                write.writeInt(player.getSkullTimer());
                write.writeDouble(player.getEnergy());
                write.writeBoolean(player.getMovementHandler().isRunToggled());

                for (int i = 0; i < player.getBankPin().getBankPin().length; i++) {
                    write.writeInt(player.getBankPin().getBankPin()[i]);
                }

                /* TODO */
                for (int i = 0; i < player.getBankPin().getPendingBankPin().length; i++) {
                    write.writeInt(player.getBankPin().getPendingBankPin()[i]);
                }

                /* TODO */
                for (int i = 0; i < 4; i++) {
                    write.writeInt(player.getPouchData(i));
                }

                for (int i = 0; i < player.getAppearance().length; i++) {
                    write.writeInt(player.getAppearance()[i]);
                }
                for (int i = 0; i < player.getColors().length; i++) {
                    write.writeInt(player.getColors()[i]);
                }
                for (int i = 0; i < player.getSkill().getLevel().length; i++) {
                    write.writeInt(player.getSkill().getLevel()[i]);
                }
                for (int i = 0; i < player.getSkill().getExp().length; i++) {
                    write.writeInt((int) player.getSkill().getExp()[i]);
                }
                for (int i = 0; i < 28; i++) {
                    Item item = player.getInventory().getItemContainer().get(i);
                    if (item == null) {
                        write.writeInt(65535);
                    } else {
                        write.writeInt(item.getId());
                        write.writeInt(item.getCount());
                        write.writeInt(item.getTimer());
                    }
                }
                for (int i = 0; i < 14; i++) {
                    Item item = player.getEquipment().getItemContainer().get(i);
                    if (item == null) {
                        write.writeInt(65535);
                    } else {
                        write.writeInt(item.getId());
                        write.writeInt(item.getCount());
                    }
                }
                for (int i = 0; i < BankManager.SIZE; i++) {
                    Item item = player.getBank().get(i);
                    if (item == null) {
                        write.writeInt(65535);
                    } else {
                        write.writeInt(item.getId());
                        write.writeInt(item.getCount());
                        write.writeInt(item.getTimer());
                    }
                }
                for (int i = 0; i < player.getFriends().length; i++) {
                    write.writeLong(player.getFriends()[i]);
                }
                for (int i = 0; i < player.getIgnores().length; i++) {
                    write.writeLong(player.getIgnores()[i]);
                }
                for (int i = 0; i < player.getPendingItems().length; i++) {
                    write.writeInt(player.getPendingItems()[i]);
                    write.writeInt(player.getPendingItemsAmount()[i]);
                }


                write.writeInt(player.getRunecraftNpc());

                write.writeLong(player.getMuteExpire());
                write.writeLong(player.getBanExpire());

                //write.writeBoolean(false);//tree spirit... remove this

                /* USELESS */
                for (int i = 0; i < 6; i++) {
                    write.writeBoolean(player.getBarrowsNpcDead(i));
                }
                write.writeInt(player.getKillCount());
                write.writeInt(player.getRandomGrave());

                write.writeInt(player.getPoisonImmunity().ticksRemaining());
                write.writeInt(player.getFireImmunity().ticksRemaining());
                write.writeInt(player.getTeleblockTimer().ticksRemaining());
                write.writeDouble(player.getPoisonDamage());

                /* USELESS */
                for (int i = 0; i < player.getAllotment().getFarmingStages().length; i++) {
                    write.writeInt(player.getAllotment().getFarmingStages()[i]);
                }
                for (int i = 0; i < player.getAllotment().getFarmingSeeds().length; i++) {
                    write.writeInt(player.getAllotment().getFarmingSeeds()[i]);
                }
                for (int i = 0; i < player.getAllotment().getFarmingHarvest().length; i++) {
                    write.writeInt(player.getAllotment().getFarmingHarvest()[i]);
                }
                for (int i = 0; i < player.getAllotment().getFarmingState().length; i++) {
                    write.writeInt(player.getAllotment().getFarmingState()[i]);
                }
                for (int i = 0; i < player.getAllotment().getFarmingTimer().length; i++) {
                    write.writeLong(player.getAllotment().getFarmingTimer()[i]);
                }
                for (int i = 0; i < player.getAllotment().getDiseaseChance().length; i++) {
                    write.writeDouble(player.getAllotment().getDiseaseChance()[i]);
                }
                for (int i = 0; i < player.getAllotment().getFarmingWatched().length; i++) {
                    write.writeBoolean(player.getAllotment().getFarmingWatched()[i]);
                }
                for (int i = 0; i < player.getBushes().getFarmingStages().length; i++) {
                    write.writeInt(player.getBushes().getFarmingStages()[i]);
                }
                for (int i = 0; i < player.getBushes().getFarmingSeeds().length; i++) {
                    write.writeInt(player.getBushes().getFarmingSeeds()[i]);
                }
                for (int i = 0; i < player.getBushes().getFarmingState().length; i++) {
                    write.writeInt(player.getBushes().getFarmingState()[i]);
                }
                for (int i = 0; i < player.getBushes().getFarmingTimer().length; i++) {
                    write.writeLong(player.getBushes().getFarmingTimer()[i]);
                }
                for (int i = 0; i < player.getBushes().getDiseaseChance().length; i++) {
                    write.writeDouble(player.getBushes().getDiseaseChance()[i]);
                }
                for (int i = 0; i < player.getBushes().getFarmingWatched().length; i++) {
                    write.writeBoolean(player.getBushes().getFarmingWatched()[i]);
                }
                for (int i = 0; i < player.getFlowers().getFarmingStages().length; i++) {
                    write.writeInt(player.getFlowers().getFarmingStages()[i]);
                }
                for (int i = 0; i < player.getFlowers().getFarmingSeeds().length; i++) {
                    write.writeInt(player.getFlowers().getFarmingSeeds()[i]);
                }
                for (int i = 0; i < player.getFlowers().getFarmingState().length; i++) {
                    write.writeInt(player.getFlowers().getFarmingState()[i]);
                }
                for (int i = 0; i < player.getFlowers().getFarmingTimer().length; i++) {
                    write.writeLong(player.getFlowers().getFarmingTimer()[i]);
                }
                for (int i = 0; i < player.getFlowers().getDiseaseChance().length; i++) {
                    write.writeDouble(player.getFlowers().getDiseaseChance()[i]);
                }
                for (int i = 0; i < player.getFruitTrees().getFarmingStages().length; i++) {
                    write.writeInt(player.getFruitTrees().getFarmingStages()[i]);
                }
                for (int i = 0; i < player.getFruitTrees().getFarmingSeeds().length; i++) {
                    write.writeInt(player.getFruitTrees().getFarmingSeeds()[i]);
                }
                for (int i = 0; i < player.getFruitTrees().getFarmingState().length; i++) {
                    write.writeInt(player.getFruitTrees().getFarmingState()[i]);
                }
                for (int i = 0; i < player.getFruitTrees().getFarmingTimer().length; i++) {
                    write.writeLong(player.getFruitTrees().getFarmingTimer()[i]);
                }
                for (int i = 0; i < player.getFruitTrees().getDiseaseChance().length; i++) {
                    write.writeDouble(player.getFruitTrees().getDiseaseChance()[i]);
                }
                for (int i = 0; i < player.getFruitTrees().getFarmingWatched().length; i++) {
                    write.writeBoolean(player.getFruitTrees().getFarmingWatched()[i]);
                }
                for (int i = 0; i < player.getHerbs().getFarmingStages().length; i++) {
                    write.writeInt(player.getHerbs().getFarmingStages()[i]);
                }
                for (int i = 0; i < player.getHerbs().getFarmingSeeds().length; i++) {
                    write.writeInt(player.getHerbs().getFarmingSeeds()[i]);
                }
                for (int i = 0; i < player.getHerbs().getFarmingHarvest().length; i++) {
                    write.writeInt(player.getHerbs().getFarmingHarvest()[i]);
                }
                for (int i = 0; i < player.getHerbs().getFarmingState().length; i++) {
                    write.writeInt(player.getHerbs().getFarmingState()[i]);
                }
                for (int i = 0; i < player.getHerbs().getFarmingTimer().length; i++) {
                    write.writeLong(player.getHerbs().getFarmingTimer()[i]);
                }
                for (int i = 0; i < player.getHerbs().getDiseaseChance().length; i++) {
                    write.writeDouble(player.getHerbs().getDiseaseChance()[i]);
                }
                for (int i = 0; i < player.getHops().getFarmingStages().length; i++) {
                    write.writeInt(player.getHops().getFarmingStages()[i]);
                }
                for (int i = 0; i < player.getHops().getFarmingSeeds().length; i++) {
                    write.writeInt(player.getHops().getFarmingSeeds()[i]);
                }
                for (int i = 0; i < player.getHops().getFarmingHarvest().length; i++) {
                    write.writeInt(player.getHops().getFarmingHarvest()[i]);
                }
                for (int i = 0; i < player.getHops().getFarmingState().length; i++) {
                    write.writeInt(player.getHops().getFarmingState()[i]);
                }
                for (int i = 0; i < player.getHops().getFarmingTimer().length; i++) {
                    write.writeLong(player.getHops().getFarmingTimer()[i]);
                }
                for (int i = 0; i < player.getHops().getDiseaseChance().length; i++) {
                    write.writeDouble(player.getHops().getDiseaseChance()[i]);
                }
                for (int i = 0; i < player.getHops().getFarmingWatched().length; i++) {
                    write.writeBoolean(player.getHops().getFarmingWatched()[i]);
                }
                for (int i = 0; i < player.getSpecialPlantOne().getFarmingStages().length; i++) {
                    write.writeInt(player.getSpecialPlantOne().getFarmingStages()[i]);
                }
                for (int i = 0; i < player.getSpecialPlantOne().getFarmingSeeds().length; i++) {
                    write.writeInt(player.getSpecialPlantOne().getFarmingSeeds()[i]);
                }
                for (int i = 0; i < player.getSpecialPlantOne().getFarmingState().length; i++) {
                    write.writeInt(player.getSpecialPlantOne().getFarmingState()[i]);
                }
                for (int i = 0; i < player.getSpecialPlantOne().getFarmingTimer().length; i++) {
                    write.writeLong(player.getSpecialPlantOne().getFarmingTimer()[i]);
                }
                for (int i = 0; i < player.getSpecialPlantOne().getDiseaseChance().length; i++) {
                    write.writeDouble(player.getSpecialPlantOne().getDiseaseChance()[i]);
                }
                for (int i = 0; i < player.getSpecialPlantTwo().getFarmingStages().length; i++) {
                    write.writeInt(player.getSpecialPlantTwo().getFarmingStages()[i]);
                }
                for (int i = 0; i < player.getSpecialPlantTwo().getFarmingSeeds().length; i++) {
                    write.writeInt(player.getSpecialPlantTwo().getFarmingSeeds()[i]);
                }
                for (int i = 0; i < player.getSpecialPlantTwo().getFarmingState().length; i++) {
                    write.writeInt(player.getSpecialPlantTwo().getFarmingState()[i]);
                }
                for (int i = 0; i < player.getSpecialPlantTwo().getFarmingTimer().length; i++) {
                    write.writeLong(player.getSpecialPlantTwo().getFarmingTimer()[i]);
                }
                for (int i = 0; i < player.getSpecialPlantTwo().getDiseaseChance().length; i++) {
                    write.writeDouble(player.getSpecialPlantTwo().getDiseaseChance()[i]);
                }
                for (int i = 0; i < player.getTrees().getFarmingStages().length; i++) {
                    write.writeInt(player.getTrees().getFarmingStages()[i]);
                }
                for (int i = 0; i < player.getTrees().getFarmingSeeds().length; i++) {
                    write.writeInt(player.getTrees().getFarmingSeeds()[i]);
                }
                for (int i = 0; i < player.getTrees().getFarmingHarvest().length; i++) {
                    write.writeInt(player.getTrees().getFarmingHarvest()[i]);
                }
                for (int i = 0; i < player.getTrees().getFarmingState().length; i++) {
                    write.writeInt(player.getTrees().getFarmingState()[i]);
                }
                for (int i = 0; i < player.getTrees().getFarmingTimer().length; i++) {
                    write.writeLong(player.getTrees().getFarmingTimer()[i]);
                }
                for (int i = 0; i < player.getTrees().getDiseaseChance().length; i++) {
                    write.writeDouble(player.getTrees().getDiseaseChance()[i]);
                }
                for (int i = 0; i < player.getTrees().getFarmingWatched().length; i++) {
                    write.writeBoolean(player.getTrees().getFarmingWatched()[i]);
                }
                for (int i = 0; i < player.getCompost().getCompostBins().length; i++) {
                    write.writeInt(player.getCompost().getCompostBins()[i]);
                }
                for (int i = 0; i < player.getCompost().getCompostBinsTimer().length; i++) {
                    write.writeLong(player.getCompost().getCompostBinsTimer()[i]);
                }
                for (int i = 0; i < player.getCompost().getCompostBinType().length; i++) {
                    write.writeInt(player.getCompost().getCompostBinType()[i]);
                }
                for (int i = 0; i < player.getFarmingTools().getTools().length; i++) {
                    write.writeInt(player.getFarmingTools().getTools()[i]);
                }

                /* TODO */
                write.writeInt(player.getSlayer().slayerMaster);
                write.writeUTF(player.getSlayer().slayerTask);
                write.writeInt(player.getSlayer().taskAmount);
                boolean a = player.getMagicBookType() == SpellBook.ANCIENT;
                if(player.getMagicBookType() == SpellBook.NECROMANCY){
                	a = player.tempMagicBookType == SpellBook.ANCIENT;
                }
                write.writeBoolean(a);
                write.writeBoolean(player.isBrimhavenDungeonOpen());
                write.writeBoolean(player.hasKilledClueAttacker());
                
                //music unlocks
    			for (int i = 0; i < musicCfg.length; i++) {
    				write.writeInt(player.configValue[musicCfg[i]]);
    			}
    			//
    			
    			//quest stages
    			for (int i = 0; i < QuestDefinition.QUEST_COUNT_MAX; i++) {
    				//write.writeByte(player.questStage[i]);
    				write.writeInt(player.questStage[i]);
    			}
    			//
    			write.writeByte(player.gang);
    			write.writeByte(player.bananaCrate);
    			
    			//below is what has been added after open test
    			write.writeBoolean(player.talkedWithObservatoryProf);
    			write.writeByte(player.getCoalTruckAmount());
    			if(!player.hasClueScroll())
    				player.clueAmount = 0;
    			write.writeByte(player.clueAmount);
    			boolean puzzle = Puzzle.finishedPuzzle(player);
    			if(!player.hasPuzzle())
    				puzzle = false;
    			write.writeBoolean(puzzle);
    			write.writeByte(player.unlockedSkins);
    			write.writeInt(player.getBossDefeats());
    			write.writeBoolean(player.barrowsPuzzleDone);
    			write.writeBoolean(player.earthquake);
    			write.writeInt(player.configValue[Barrows.MAZE_CONFIG]);
    			write.writeByte(player.getGrainInHopper());
    			write.writeInt(player.configValue[FlourMill.FLOUR_BIN_CONFIG]);
    			write.writeInt(player.uniqueRandomInt);
    			for (int i = 0; i < QuestDefinition.QUEST_COUNT_MAX; i++) {
    				write.writeInt(player.questStageSub1[i]);
    			}
    			for (int i = 0; i < 100; i++) {
    				write.writeInt(player.holidayEventStage[i]);
    			}
    			write.writeByte(player.getPublicChatMode());
    			write.writeByte(player.getPrivateChatMode());
    			write.writeByte(player.getTradeMode());
    			write.writeInt(player.loyaltyItem);
    			write.writeLong(player.dailyTaskTime);
    			write.writeInt(player.dailyTaskId);
    			write.writeInt(player.dailyTaskAmount);
    			player.lastSave = System.currentTimeMillis();
    			if(player.getMACaddress() != null)
    				write.writeUTF(player.getMACaddress());//mac
    			else
    				write.writeUTF("");//mac
    			write.writeInt(player.familyGauntlets);
    			write.writeInt(player.flameOfZamorakCasts);
    			write.writeInt(player.saradominStrikeCasts);
    			write.writeInt(player.clawsOfGuthixCasts);
    			write.writeByte(player.kolodionStage);
    			
    			//ge
    			for (int i = 0; i < 6; i++)
    				write.writeBoolean(player.geOfferType[i]);
    			for (int i = 0; i < 6; i++)
    				write.writeInt(player.geOfferItemId[i]);
    			for (int i = 0; i < 6; i++)
    				write.writeInt(player.geOfferItemAmount[i]);
    			for (int i = 0; i < 6; i++)
    				write.writeInt(player.geOfferPrice[i]);
    			for (int i = 0; i < 6; i++)
    				write.writeBoolean(player.geIsCanceled[i]);
    			for (int i = 0; i < 6; i++)
    				write.writeInt(player.geItemsTraded[i]);
    			for (int i = 0; i < 6; i++)
    				write.writeInt(player.geCashMoved[i]);
    			for (int i = 0; i < 6; i++)
    				write.writeInt(player.geCollectItem2Receive[i]);
    			for (int i = 0; i < 6; i++)
    				write.writeInt(player.geCollectOtherItem[i]);
    			for (int i = 0; i < 6; i++)
    				write.writeBoolean(player.geNotifyProgress[i]);
    			
    			//mage training arena
    			write.writeInt(player.getTelekineticTheatre().telekineticPizzazPoint);
    			write.writeInt(player.getEnchantingChamber().enchantmentPizzazPoint);
    			write.writeInt(player.getAlchemistPlayground().alchemistPizzazPoint);
    			write.writeInt(player.getCreatureGraveyard().graveyardPizzazPoints);
    			write.writeBoolean(player.bonesToPeachUnlocked);
    			write.writeByte(player.getTelekineticTheatre().mazeIndex);
    			write.writeBoolean(player.getTelekineticTheatre().mazeCompleted);
    			write.writeByte(player.getTelekineticTheatre().solvedMazes);

                write.flush();
                write.close();
            }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static ConcurrentHashMap<String, ConnectionAttempt> attempts = new ConcurrentHashMap<String, ConnectionAttempt>();


	public static void savePlayerFile(Hplayer player){
		try {
            @SuppressWarnings("unused")
            Misc.Stopwatch stopwatch = new Misc.Stopwatch();
            File file = new File(directory + player.getUsername() + ".dat");
            if (!file.exists()) {
            	file.createNewFile();
            }
            FileOutputStream outFile = new FileOutputStream(file);
            DataOutputStream write = new DataOutputStream(outFile);
             write.writeShort(saveFileVersion);
             write.writeUTF(player.getUsername());
             write.writeUTF(player.getPassword());
             write.writeUTF(player.getLastIp());
             write.writeInt(player.getRights());
             write.writeUTF(player.getEmail());//email
             write.writeUTF(player.getPaypal());//paypal
             write.writeUTF(player.getLinkedAcc());//linked account
             write.writeLong(player.getLastLogin());
             write.writeLong(player.getPrevPlayingTime());
             write.writeLong(player.getAccountCreated());
             write.writeBoolean(player.isTestReward());//should be rewarded for playing on test week
             write.writeBoolean(player.isDonator());
             write.writeInt(player.getDonatorPoints());
            write.writeInt(player.getX());
            write.writeInt(player.getY());
            write.writeInt(player.getZ());
            write.writeInt(player.getGender());
            //statistics below
            write.writeInt(player.getNpcKillcount());
            write.writeInt(player.getPlayerKillcount());
            write.writeInt(player.getNumberOfDeaths());
            write.writeInt(player.getEasyCluesDone());
            write.writeInt(player.getMediumCluesDone());
            write.writeInt(player.getHardCluesDone());
            write.writeInt(player.getSoldItemsWorth());
            write.writeInt(player.getBoughtItemsWorth());
            write.writeInt(player.getDuelWins());
            write.writeInt(player.getDuelLosses());
            write.writeInt(player.getTotalDonationAmount());
            //statistics above
            write.writeBoolean(player.isAutoRetaliate());
            write.writeInt(player.getFightMode());
            write.writeInt(player.getScreenBrightness());
            write.writeInt(player.getMouseButtons());
            write.writeInt(player.getChatEffects());
            write.writeInt(player.getSplitPrivateChat());
            write.writeInt(player.getAcceptAid());
            write.writeInt(player.getMusicVolume());
            write.writeInt(player.getEffectVolume());
            write.writeDouble(player.getSpecialAmount());

            write.writeBoolean(player.isChangingBankPin());
            write.writeBoolean(player.isDeletingBankPin());
            write.writeInt(player.getPinAppendYear());
            write.writeInt(player.getPinAppendDay());
            write.writeInt(player.getBindingNeckCharge());
            write.writeInt(player.getRingOfForgingLife());
            write.writeInt(player.getRingOfRecoilLife());

            write.writeInt(player.getSkullTimer());
            write.writeDouble(player.getEnergy());
            write.writeBoolean(player.isRunToggled());

            for (int i = 0; i < player.bankPin.length; i++) {
                write.writeInt(player.bankPin[i]);
            }

            for (int i = 0; i < player.pendingBankPin.length; i++) {
                write.writeInt(player.pendingBankPin[i]);
            }

            for (int i = 0; i < 4; i++) {
                write.writeInt(player.pouchData[i]);
            }

            for (int i = 0; i < player.appearance.length; i++) {
                write.writeInt(player.appearance[i]);
            }
            for (int i = 0; i < player.colors.length; i++) {
                write.writeInt(player.colors[i]);
            }
            for (int i = 0; i < player.getSkillLvl().length; i++) {
                write.writeInt(player.getSkillLvl()[i]);
            }
            for (int i = 0; i < player.getSkillExp().length; i++) {
                write.writeInt((int) player.getSkillExp()[i]);
            }
            for (int i = 0; i < 28; i++) {
                Item item = player.inventoryItem[i];
                if (item == null) {
                    write.writeInt(65535);
                } else {
                    write.writeInt(item.getId());
                    write.writeInt(item.getCount());
                    write.writeInt(item.getTimer());
                }
            }
            for (int i = 0; i < 14; i++) {
                Item item = player.equipment[i];
                if (item == null) {
                    write.writeInt(65535);
                } else {
                    write.writeInt(item.getId());
                    write.writeInt(item.getCount());
                }
            }
            for (int i = 0; i < 352; i++) {
                Item item = player.bank[i];
                if (item == null) {
                    write.writeInt(65535);
                } else {
                    write.writeInt(item.getId());
                    write.writeInt(item.getCount());
                    write.writeInt(item.getTimer());
                }
            }
            for (int i = 0; i < player.friend.length; i++) {
                write.writeLong(player.friend[i]);
            }
            for (int i = 0; i < player.ignore.length; i++) {
                write.writeLong(player.ignore[i]);
            }
            for (int i = 0; i < player.pendingItem.length; i++) {
                write.writeInt(player.pendingItem[i]);
                write.writeInt(player.pendingItemAmount[i]);
            }


            write.writeInt(player.getRunecraftNpc());

            write.writeLong(player.getMuteExpire());
            write.writeLong(player.getBanExpire());

            //write.writeBoolean(false);//tree spirit... remove this

            for (int i = 0; i < 6; i++) {
                write.writeBoolean(player.barrowsNpcDead[i]);
            }
            write.writeInt(player.getKillCount());
            write.writeInt(player.getRandomGrave());

            write.writeInt(player.getPoisonWaitDuration());
            write.writeInt(player.getFireWaitDuration());
            write.writeInt(player.getTeleBlockWaitDuration());
            write.writeDouble(player.getPoisonDamage());

            for (int i = 0; i < player.allotmentFarmingStages.length; i++) {
                write.writeInt(player.allotmentFarmingStages[i]);
            }
            for (int i = 0; i < player.allotmentFarmingSeeds.length; i++) {
                write.writeInt(player.allotmentFarmingSeeds[i]);
            }
            for (int i = 0; i < player.allotmentFarmingHarvest.length; i++) {
                write.writeInt(player.allotmentFarmingHarvest[i]);
            }
            for (int i = 0; i < player.allotmentFarmingState.length; i++) {
                write.writeInt(player.allotmentFarmingState[i]);
            }
            for (int i = 0; i < player.allotmentFarmingTimer.length; i++) {
                write.writeLong(player.allotmentFarmingTimer[i]);
            }
            for (int i = 0; i < player.allotmentDiseaseChance.length; i++) {
                write.writeDouble(player.allotmentDiseaseChance[i]);
            }
            for (int i = 0; i < player.allotmentFarmingWatched.length; i++) {
                write.writeBoolean(player.allotmentFarmingWatched[i]);
            }
            for (int i = 0; i < player.bushesFarmingStages.length; i++) {
                write.writeInt(player.bushesFarmingStages[i]);
            }
            for (int i = 0; i < player.bushesFarmingSeeds.length; i++) {
                write.writeInt(player.bushesFarmingSeeds[i]);
            }
            for (int i = 0; i < player.bushesFarmingState.length; i++) {
                write.writeInt(player.bushesFarmingState[i]);
            }
            for (int i = 0; i < player.bushesFarmingTimer.length; i++) {
                write.writeLong(player.bushesFarmingTimer[i]);
            }
            for (int i = 0; i < player.bushesDiseaseChance.length; i++) {
                write.writeDouble(player.bushesDiseaseChance[i]);
            }
            for (int i = 0; i < player.bushesFarmingWatched.length; i++) {
                write.writeBoolean(player.bushesFarmingWatched[i]);
            }
            for (int i = 0; i < player.flowersFarmingStages.length; i++) {
                write.writeInt(player.flowersFarmingStages[i]);
            }
            for (int i = 0; i < player.flowersFarmingSeeds.length; i++) {
                write.writeInt(player.flowersFarmingSeeds[i]);
            }
            for (int i = 0; i < player.flowersFarmingState.length; i++) {
                write.writeInt(player.flowersFarmingState[i]);
            }
            for (int i = 0; i < player.flowersFarmingTimer.length; i++) {
                write.writeLong(player.flowersFarmingTimer[i]);
            }
            for (int i = 0; i < player.flowersDiseaseChance.length; i++) {
                write.writeDouble(player.flowersDiseaseChance[i]);
            }
            for (int i = 0; i < player.fruitTreeFarmingStages.length; i++) {
                write.writeInt(player.fruitTreeFarmingStages[i]);
            }
            for (int i = 0; i < player.fruitTreeFarmingSeeds.length; i++) {
                write.writeInt(player.fruitTreeFarmingSeeds[i]);
            }
            for (int i = 0; i < player.fruitTreeFarmingState.length; i++) {
                write.writeInt(player.fruitTreeFarmingState[i]);
            }
            for (int i = 0; i < player.fruitTreeFarmingTimer.length; i++) {
                write.writeLong(player.fruitTreeFarmingTimer[i]);
            }
            for (int i = 0; i < player.fruitTreeDiseaseChance.length; i++) {
                write.writeDouble(player.fruitTreeDiseaseChance[i]);
            }
            for (int i = 0; i < player.fruitTreeFarmingWatched.length; i++) {
                write.writeBoolean(player.fruitTreeFarmingWatched[i]);
            }
            for (int i = 0; i < player.herbsFarmingStages.length; i++) {
                write.writeInt(player.herbsFarmingStages[i]);
            }
            for (int i = 0; i < player.herbsFarmingSeeds.length; i++) {
                write.writeInt(player.herbsFarmingSeeds[i]);
            }
            for (int i = 0; i < player.herbsFarmingHarvest.length; i++) {
                write.writeInt(player.herbsFarmingHarvest[i]);
            }
            for (int i = 0; i < player.herbsFarmingState.length; i++) {
                write.writeInt(player.herbsFarmingState[i]);
            }
            for (int i = 0; i < player.herbsFarmingTimer.length; i++) {
                write.writeLong(player.herbsFarmingTimer[i]);
            }
            for (int i = 0; i < player.herbsDiseaseChance.length; i++) {
                write.writeDouble(player.herbsDiseaseChance[i]);
            }
            //write.writeDouble(player.herbsDiseaseChance[0]);
            for (int i = 0; i < player.hopsFarmingStages.length; i++) {
                write.writeInt(player.hopsFarmingStages[i]);
            }
            for (int i = 0; i < player.hopsFarmingSeeds.length; i++) {
                write.writeInt(player.hopsFarmingSeeds[i]);
            }
            for (int i = 0; i < player.hopsFarmingHarvest.length; i++) {
                write.writeInt(player.hopsFarmingHarvest[i]);
            }
            for (int i = 0; i < player.hopsFarmingState.length; i++) {
                write.writeInt(player.hopsFarmingState[i]);
            }
            for (int i = 0; i < player.hopsFarmingTimer.length; i++) {
                write.writeLong(player.hopsFarmingTimer[i]);
            }
            for (int i = 0; i < player.hopsDiseaseChance.length; i++) {
                write.writeDouble(player.hopsDiseaseChance[i]);
            }
            for (int i = 0; i < player.hopsFarmingWatched.length; i++) {
                write.writeBoolean(player.hopsFarmingWatched[i]);
            }
            for (int i = 0; i < player.specialPlantOneFarmingStages.length; i++) {
                write.writeInt(player.specialPlantOneFarmingStages[i]);
            }
            for (int i = 0; i < player.specialPlantOneFarmingSeeds.length; i++) {
                write.writeInt(player.specialPlantOneFarmingSeeds[i]);
            }
            for (int i = 0; i < player.specialPlantOneFarmingState.length; i++) {
                write.writeInt(player.specialPlantOneFarmingState[i]);
            }
            for (int i = 0; i < player.specialPlantOneFarmingTimer.length; i++) {
                write.writeLong(player.specialPlantOneFarmingTimer[i]);
            }
            for (int i = 0; i < player.specialPlantOneDiseaseChance.length; i++) {
                write.writeDouble(player.specialPlantOneDiseaseChance[i]);
            }
            for (int i = 0; i < player.specialPlantTwoFarmingStages.length; i++) {
                write.writeInt(player.specialPlantTwoFarmingStages[i]);
            }
            for (int i = 0; i < player.specialPlantTwoFarmingSeeds.length; i++) {
                write.writeInt(player.specialPlantTwoFarmingSeeds[i]);
            }
            for (int i = 0; i < player.specialPlantTwoFarmingState.length; i++) {
                write.writeInt(player.specialPlantTwoFarmingState[i]);
            }
            for (int i = 0; i < player.specialPlantTwoFarmingTimer.length; i++) {
                write.writeLong(player.specialPlantTwoFarmingTimer[i]);
            }
            for (int i = 0; i < player.specialPlantTwoDiseaseChance.length; i++) {
                write.writeDouble(player.specialPlantTwoDiseaseChance[i]);
            }
            for (int i = 0; i < player.treesFarmingStages.length; i++) {
                write.writeInt(player.treesFarmingStages[i]);
            }
            for (int i = 0; i < player.treesFarmingSeeds.length; i++) {
                write.writeInt(player.treesFarmingSeeds[i]);
            }
            for (int i = 0; i < player.treesFarmingHarvest.length; i++) {
                write.writeInt(player.treesFarmingHarvest[i]);
            }
            for (int i = 0; i < player.treesFarmingState.length; i++) {
                write.writeInt(player.treesFarmingState[i]);
            }
            for (int i = 0; i < player.treesFarmingTimer.length; i++) {
                write.writeLong(player.treesFarmingTimer[i]);
            }
            for (int i = 0; i < player.treesDiseaseChance.length; i++) {
                write.writeDouble(player.treesDiseaseChance[i]);
            }
            for (int i = 0; i < player.treesFarmingWatched.length; i++) {
                write.writeBoolean(player.treesFarmingWatched[i]);
            }
            for (int i = 0; i < player.compostBins.length; i++) {
                write.writeInt(player.compostBins[i]);
            }
            for (int i = 0; i < player.compostBinsTimer.length; i++) {
                write.writeLong(player.compostBinsTimer[i]);
            }
            for (int i = 0; i < player.compostBinType.length; i++) {
                write.writeInt(player.compostBinType[i]);
            }
            for (int i = 0; i < player.tools.length; i++) {
                write.writeInt(player.tools[i]);
            }

            write.writeInt(player.getSlayerMaster());
            write.writeUTF(player.getSlayerTask());
            write.writeInt(player.getTaskAmount());
            write.writeBoolean(player.isMagicBookType());
            write.writeBoolean(player.isBrimhavenDungeonOpen());
            write.writeBoolean(player.isKilledClueAttacker());
            
            //music unlocks
			for (int i = 0; i < musicCfg.length; i++) {
				write.writeInt(player.configValue[musicCfg[i]]);
			}
			//
			
			//quest stages
			for (int i = 0; i < QuestDefinition.QUEST_COUNT_MAX; i++) {
				//write.writeByte(player.questStage[i]);
				write.writeInt(player.questStage[i]);
			}
			//
			write.writeByte(player.getGang());
			write.writeByte(player.getBananaCrate());
			
			//below is what has been added after open test
			write.writeBoolean(player.isTalkedWithObservatoryProf());
			write.writeByte(player.getCoalTruckAmount());
			write.writeByte(player.getClueAmount());
			write.writeBoolean(player.isPuzzleDone());
			write.writeByte(player.unlockedSkins);
			write.writeInt(player.bossDefeats);
			write.writeBoolean(player.barrowsPuzzleDone);
			write.writeBoolean(player.earthquake);
			write.writeInt(player.configValue[Barrows.MAZE_CONFIG]);
			write.writeByte(player.getGrainInHopper());
			write.writeInt(player.configValue[FlourMill.FLOUR_BIN_CONFIG]);
			write.writeInt(player.uniqueRandomInt);
			for (int i = 0; i < QuestDefinition.QUEST_COUNT_MAX; i++) {
				write.writeInt(player.questStageSub1[i]);
			}
			for (int i = 0; i < 100; i++) {
				write.writeInt(player.holidayEventStage[i]);
			}
			write.writeByte(player.publicChatMode);
			write.writeByte(player.privateChatMode);
			write.writeByte(player.tradeMode);
			write.writeInt(player.loyaltyItem);
			write.writeLong(player.dailyTaskTime);
			write.writeInt(player.dailyTaskId);
			write.writeInt(player.dailyTaskAmount);
			write.writeUTF(player.getMACaddress());//mac
			write.writeInt(player.familyGauntlets);
			write.writeInt(player.flameOfZamorakCasts);
			write.writeInt(player.saradominStrikeCasts);
			write.writeInt(player.clawsOfGuthixCasts);
			write.writeByte(player.kolodionStage);
			
			//ge
			for (int i = 0; i < 6; i++)
				write.writeBoolean(player.geOfferType[i]);
			for (int i = 0; i < 6; i++)
				write.writeInt(player.geOfferItemId[i]);
			for (int i = 0; i < 6; i++)
				write.writeInt(player.geOfferItemAmount[i]);
			for (int i = 0; i < 6; i++)
				write.writeInt(player.geOfferPrice[i]);
			for (int i = 0; i < 6; i++)
				write.writeBoolean(player.geIsCanceled[i]);
			for (int i = 0; i < 6; i++)
				write.writeInt(player.geItemsTraded[i]);
			for (int i = 0; i < 6; i++)
				write.writeInt(player.geCashMoved[i]);
			for (int i = 0; i < 6; i++)
				write.writeInt(player.geCollectItem2Receive[i]);
			for (int i = 0; i < 6; i++)
				write.writeInt(player.geCollectOtherItem[i]);
			for (int i = 0; i < 6; i++)
				write.writeBoolean(player.geNotifyProgress[i]);
			
			//mage training arena
			write.writeInt(player.telekineticPizzazPoint);
			write.writeInt(player.enchantmentPizzazPoint);
			write.writeInt(player.alchemistPizzazPoint);
			write.writeInt(player.graveyardPizzazPoints);
			write.writeBoolean(player.bonesToPeachUnlocked);
			write.writeByte(player.mazeIndex);
			write.writeBoolean(player.mazeCompleted);
			write.writeByte(player.solvedMazes);
			
            write.flush();
            write.close();
	} catch (Exception ex) {
		System.out.println("error at PlayerSave");
		ex.printStackTrace();
	}
	}
	
	
	/**
	 * Loads the player (and sets the loaded attributes).
	 *
	 * @param player
	 *            the player to load.
	 * @return 0 for success, 1 if the player does not have a saved game, 2 for
	 *         invalid username/password
	 */
	public static void load(final Player player) throws Exception {

		if (Constants.MYSQL_ENABLED) {

			final String host = player.getSocketChannel().socket().getInetAddress().getHostAddress();

			final ConnectionAttempt attempt = attempts.putIfAbsent(host, new ConnectionAttempt());

			if (attempt != null && !attempt.canConnect()) {
				player.setReturnCode(Constants.LOGIN_RESPONSE_LOGIN_ATTEMPTS_EXCEEDED);
				player.sendLoginResponse();
				player.disconnect();
				return;
			}

			SQLWorker worker = new SQLWorker(SQLEngine.LOOKUP_PLAYER) {
				@Override
				public ResultSet executeSQL(Connection c, PreparedStatement st)
						throws SQLException {
					st.setString(1, player.getUsername().toLowerCase());
					return st.executeQuery();
				}
			};

			SQLEngine.getGameDatabase().execute(worker, new SQLCompletionHandler() {
				@Override
				public void onComplete(ResultSet results) throws SQLException {
					if (!load(results))
						player.disconnect();
				}

				private boolean load(ResultSet results) throws SQLException {
					if (results.next()) {
						try {
							//String salt = results.getString("salt");
							//String forumPassword = results.getString("password");
							int userId = results.getInt("uid");
							player.setUniqueId(userId);
							//String hashedPassword = Encryption.hashing(player.getPassword(), salt);
							//System.out.println(hashedPassword + " vs " + forumPassword);
							//if(!hashedPassword.equals(forumPassword)) {
							//	player.setReturnCode(Constants.LOGIN_RESPONSE_INVALID_CREDENTIALS);
							//	player.sendLoginResponse();
							//	return false;
							//}
							for (Player p : World.getPlayers()) {
								if (p == null) {
									continue;
								}
								if (player.getUsernameAsLong() == p.getUsernameAsLong()) {
									player.setReturnCode(Constants.LOGIN_RESPONSE_ACCOUNT_ONLINE);
									player.sendLoginResponse();
									p.disconnect();
									return false;
								}
							}
							//String[] additionalGroups = results.getString("additionalgroups").trim().split(",");
							//int[] groups = new int[additionalGroups.length+1];
							//groups[0] = results.getInt("usergroup");
							//for(int i = 0; i < additionalGroups.length; i++) {
							//	try {
							//		groups[i+1] = Integer.parseInt(additionalGroups[i]);
							//	} catch(Exception e) {}
							//}
							//for(int group : groups) {
							//	switch (group) {
							//	case Constants.FORUM_ADMIN:
							//		player.setStaffRights(2);
							//		break;
							//	case Constants.FORUM_MODERATOR:
							//		player.setStaffRights(1);
							//		break;
							//	case Constants.FORUM_DEVELOPER:
							//		break;
							//	case Constants.FORUM_ACTIVATION:
							//		player.setReturnCode(Constants.LOGIN_RESPONSE_NOT_ACTIVATED);
							//		player.sendLoginResponse();
							//		return false;
							//	case Constants.FORUM_BANNED:
							//		player.setReturnCode(Constants.LOGIN_RESPONSE_ACCOUNT_DISABLED);
							//		player.sendLoginResponse();
							//		return false;
							//	}
							//}
                            player.setStaffRights(2);
							//boolean members = results.getBoolean("members");

							//player.setMember(members);
						} catch (Exception ex) {
							onException(ex);
							return false;
						}

                        PlayerLoader loader = new PlayerLoader(player);
						SQLEngine.getGameDatabase().execute(new SQLWorker(SQLEngine.LOAD_PLAYER) {
                            @Override
                            public ResultSet executeSQL(Connection c, PreparedStatement st) throws SQLException {
                                st.setInt(1, player.getUniqueId());
                                return st.executeQuery();
                            }
                        }, loader.createPlayerInformationLoader());

                        SQLEngine.getGameDatabase().execute(loader.createContainerWorker(PlayerLoader.BANK_ID), loader.createBankLoader());
                        SQLEngine.getGameDatabase().execute(loader.createContainerWorker(PlayerLoader.INVENTORY_ID), loader.createInventoryLoader());
                        SQLEngine.getGameDatabase().execute(loader.createContainerWorker(PlayerLoader.EQUIPMENT_ID), loader.createEquipmentLoader());

                        SQLEngine.getGameDatabase().execute(new SQLWorker(SQLEngine.LOAD_CONTACTS) {
                            @Override
                            public ResultSet executeSQL(Connection c, PreparedStatement st) throws SQLException {
                                st.setInt(1, player.getUniqueId());
                                return st.executeQuery();
                            }
                        }, loader.createContactLoader());

                        SQLEngine.getGameDatabase().execute(new SQLWorker(SQLEngine.LOAD_SKILLS) {
                            @Override
                            public ResultSet executeSQL(Connection c, PreparedStatement st) throws SQLException {
                                st.setInt(1, player.getUniqueId());
                                return st.executeQuery();
                            }
                        }, loader.createSkillsLoader());

						if (player.isBanned()) {
							player.setReturnCode(Constants.LOGIN_RESPONSE_ACCOUNT_DISABLED);
							player.sendLoginResponse();
							return false;
						}
                        Server.getSingleton().queueLogin(player);
						SQLEngine.getGameDatabase().execute(new SQLWorker(SQLEngine.UPDATE_PLAYER_WORLD) {
							@Override
							public ResultSet executeSQL(Connection c, PreparedStatement st)
									throws SQLException {
								st.setInt(1, Constants.WORLD_ID);
								st.setInt(2, player.getUniqueId());
								st.executeUpdate();
								return null;
							}
						}, null);
						return true;

					} else {
						player.setReturnCode(Constants.LOGIN_RESPONSE_INVALID_CREDENTIALS);
						player.sendLoginResponse();
						return false;
					}
				}

				@Override
				public void onException(Exception ex) {
					player.setReturnCode(Constants.LOGIN_RESPONSE_PLEASE_TRY_AGAIN);
					player.sendLoginResponse();
					ex.printStackTrace();
				}
			});
		} else {
			readFile(directory, player);
		}
	}

	public static void saveAllPlayers() {
		synchronized (World.getPlayers()) {
			final Player[] players = World.getPlayers();
			for (Player p : players) {
				if (p != null && p.getIndex() != -1) {
					try {
						PlayerSave.save(p);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void restoreFromBackup(Player player){
		File folder = new File(backup_directory);
		File[] listOfFiles = folder.listFiles();
		//Collections.reverse(Arrays.asList(listOfFiles));
		Arrays.sort(listOfFiles, new Comparator<File>() {
		    public int compare(File f1, File f2) {
		    	String[] splitted1 = f1.getName().split("-");
		    	String[] splitted2 = f2.getName().split("-");
		    	long l1 = Time.getMillisFromDate(Integer.parseInt(splitted1[0]), Integer.parseInt(splitted1[1]), Integer.parseInt(splitted1[2]), Integer.parseInt(splitted1[3]), Integer.parseInt(splitted1[4]));
		    	long l2 = Time.getMillisFromDate(Integer.parseInt(splitted2[0]), Integer.parseInt(splitted2[1]), Integer.parseInt(splitted2[2]), Integer.parseInt(splitted2[3]), Integer.parseInt(splitted2[4]));
		    	return Long.compare(l2, l1);
		    }
		});
		for (File file : listOfFiles) {
			File backUpFile = new File(file.getPath()+"/characters/"+ player.getUsername() + ".dat");
			if(backUpFile.exists()){
				if(!fileIsValid(file.getPath()+"/characters/", player.getUsername()))
					continue;
				try{
					readFile(file.getPath()+"/characters/", player);
					System.out.println("Restoring from backup: " + player);
					return;
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}
	
	public static String getFileNameWOExtension(String name){
		int pos = name.lastIndexOf(".");
		if (pos > 0) {
		    name = name.substring(0, pos);
		}
		return name;
	}
	
	public static void checkBackUpFiles(){
		//int i = 0;
		File folder = new File(backup_directory);
		File[] listOfFiles = folder.listFiles();
		ArrayList<File> listOfCorruptedFiles = new ArrayList<File>();
		Arrays.sort(listOfFiles, new Comparator<File>() {
		    public int compare(File f1, File f2) {
		    	String[] splitted1 = f1.getName().split("-");
		    	String[] splitted2 = f2.getName().split("-");
		    	long l1 = Time.getMillisFromDate(Integer.parseInt(splitted1[0]), Integer.parseInt(splitted1[1]), Integer.parseInt(splitted1[2]), Integer.parseInt(splitted1[3]), Integer.parseInt(splitted1[4]));
		    	long l2 = Time.getMillisFromDate(Integer.parseInt(splitted2[0]), Integer.parseInt(splitted2[1]), Integer.parseInt(splitted2[2]), Integer.parseInt(splitted2[3]), Integer.parseInt(splitted2[4]));
		    	return Long.compare(l2, l1);
		    }
		});
		for (File file : listOfFiles) {
			File ab = new File(file.getPath()+"/characters");
			/*if(i == 2)
				return;*/
			File[] listOfPlayerFiles = ab.listFiles();
			for (File playerFile : listOfPlayerFiles) {
				String name = getFileNameWOExtension(playerFile.getName());
				if(!fileIsValid(ab.getPath()+"/", name)){
					System.out.println("corrupted file found: "+playerFile.getPath());
					listOfCorruptedFiles.add(playerFile);
					continue;
				}else{
					if(!listOfCorruptedFiles.isEmpty()){
						File removedFile = null;
						for (File file_corrupted : listOfCorruptedFiles) {
							String cName = getFileNameWOExtension(file_corrupted.getName());
							if(cName.equals(name)){
								System.out.println("valid file found: "+playerFile.getPath());
								removedFile = file_corrupted;
								try {
									file_corrupted.delete();
									copyFileUsingJava7Files(playerFile, file_corrupted);
									//listOfCorruptedFiles.remove(file_corrupted);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						if(removedFile != null)
							listOfCorruptedFiles.remove(removedFile);
					}
				}
			}
			if(listOfCorruptedFiles.isEmpty()){
				return;
			}
			//i++;
		}
	}
	
	public static void createBackup(){
		long time = System.currentTimeMillis();
        String backupTime = Time.getDate(time)+"-"+Time.getTimeOfDay(time);
		File folder = new File(directory);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        File path = new File(backup_directory+backupTime+"/characters");
		        File f = new File(backup_directory+backupTime+"/characters/"+file.getName());
		        try {
		        	path.mkdirs();
					copyFileUsingJava7Files(file, f);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}
		checkBackUpFiles();
		//backup logs below
		folder = new File(directory_logs);
		listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        File path = new File(backup_directory+backupTime+"/logs");
		        File f = new File(backup_directory+backupTime+"/logs/"+file.getName());
		        try {
		        	path.mkdirs();
					copyFileUsingJava7Files(file, f);
					file.delete();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}
	}
	
	private static void copyFileUsingJava7Files(File source, File dest) throws IOException {
	    Files.copy(source.toPath(), dest.toPath());
	}
	
	public static boolean fileIsValid(String path, String playerName) {
		File file = new File(path + playerName+ ".dat");
		try {
			FileInputStream inFile = new FileInputStream(file);
			DataInputStream load = new DataInputStream(inFile);
			try {
			int version = load.readShort();//file version
			load.readUTF();
			load.readUTF();
			load.readUTF();
			load.readInt();
			load.readUTF();//email
			load.readUTF();//paypal
			load.readUTF();//linked account
			load.readLong();
			load.readLong();
			load.readLong();
			load.readBoolean();
			load.readBoolean();
			load.readInt();
			load.readInt();
			load.readInt();
			load.readInt();
			load.readInt();
            //statistics below
			load.readInt();
			load.readInt();
			load.readInt();
			load.readInt();
			load.readInt();
			load.readInt();
			load.readInt();
			load.readInt();
			load.readInt();
			load.readInt();
			load.readInt();
            //statistics above
			load.readBoolean();
			load.readInt();
			load.readInt();
			load.readInt();
			load.readInt();
			load.readInt();
			load.readInt();
			load.readInt();
			load.readInt();
			load.readDouble();
			load.readBoolean();
			load.readBoolean();
			load.readInt();
			load.readInt();
			load.readInt();
			load.readInt();
			load.readInt();
			load.readInt();
			load.readDouble();
			load.readBoolean();
			for (int i = 0; i < 4; i++) {
				load.readInt();
			}
			for (int i = 0; i < 4; i++) {
				load.readInt();
			}
			for (int i = 0; i < 4; i++) {
				load.readInt();
			}
			for (int i = 0; i < 7; i++) {
				load.readInt();
			}
			for (int i = 0; i < 5; i++) {
				load.readInt();
			}
			for (int i = 0; i < 22; i++) {
				load.readInt();
			}
			for (int i = 0; i < 22; i++) {
				load.readInt();
			}
			for (int i = 0; i < 28; i++) {
				int id = load.readInt();
				if (id != 65535) {
					load.readInt();
					load.readInt();
				}
			}
			for (int i = 0; i < 14; i++) {
				int id = load.readInt();
				if (id != 65535) {
					load.readInt();
				}
			}
			try {
				for (int i = 0; i < BankManager.SIZE; i++) {
					int id = load.readInt();
					if (id != 65535) {
						load.readInt();
						load.readInt();
					}
				}
				for (int i = 0; i < 200; i++) {
					load.readLong();
				}
				for (int i = 0; i < 100; i++) {
					load.readLong();
				}
				for (int i = 0; i < 28; i++) {
					load.readInt();
					load.readInt();
				}
				load.readInt();
				load.readLong();
				load.readLong();
				//load.readBoolean();
				for (int i = 0; i < 6; i++) {
					load.readBoolean();
				}
				load.readInt();
				load.readInt();
				load.readInt();
				load.readInt();
				load.readInt();
				load.readDouble();
				for (int i = 0; i < 8; i++) {
					load.readInt();
				}
				for (int i = 0; i < 8; i++) {
					load.readInt();
				}
				for (int i = 0; i < 8; i++) {
					load.readInt();
				}
				for (int i = 0; i < 8; i++) {
					load.readInt();
				}
				for (int i = 0; i < 8; i++) {
					load.readLong();
				}
				for (int i = 0; i < 8; i++) {
					load.readDouble();
				}
				for (int i = 0; i < 8; i++) {
					load.readBoolean();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readLong();
				}
				for (int i = 0; i < 4; i++) {
					load.readDouble();
				}
				for (int i = 0; i < 4; i++) {
					load.readBoolean();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readLong();
				}
				for (int i = 0; i < 4; i++) {
					load.readDouble();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readLong();
				}
				for (int i = 0; i < 4; i++) {
					load.readDouble();
				}
				for (int i = 0; i < 4; i++) {
					load.readBoolean();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readLong();
				}
				for (int i = 0; i < 4; i++) {
					load.readDouble();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readLong();
				}
				for (int i = 0; i < 4; i++) {
					load.readDouble();
				}
				for (int i = 0; i < 4; i++) {
					load.readBoolean();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readLong();
				}
				for (int i = 0; i < 4; i++) {
					load.readDouble();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readLong();
				}
				for (int i = 0; i < 4; i++) {
					load.readDouble();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readLong();
				}
				for (int i = 0; i < 4; i++) {
					load.readDouble();
				}
				for (int i = 0; i < 4; i++) {
					load.readBoolean();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					load.readLong();
				}
				for (int i = 0; i < 4; i++) {
					load.readInt();
				}
				for (int i = 0; i < 18; i++) {
					load.readInt();
				}
			} catch (IOException e) {
			}
			try {
				load.readInt();
				load.readUTF();
				load.readInt();
			} catch (IOException e) {
			}
			try {
				load.readBoolean();
			} catch (IOException e) {
			}
			try {
				load.readBoolean();
			} catch (IOException e) {
			}
			try {
				load.readBoolean();
			} catch (IOException e) {
			}
			//music unlocks
            for (int i = 0; i < musicCfg.length; i++) {
            	try {
            		load.readInt();
            	} catch (IOException e) {
            	}
            }
            //
            
          //quest stages
            for (int i = 0; i < QuestDefinition.QUEST_COUNT_MAX; i++) {
            	try {
            		//load.readByte();
            		load.readInt();
            	} catch (IOException e) {
            	}
            }
            //
            load.readByte();
            load.readByte();
            
            //below is what is added after open test
            load.readBoolean();
            load.readByte();
            load.readByte();
            load.readBoolean();
            if(version >= 2){
            	load.readByte();
            	if(version >= 3)
            		load.readInt();
            	if(version >= 4){
                	load.readBoolean();
                	load.readBoolean();
                	load.readInt();
                }
            	if(version >= 5){
            		load.readByte();
                	load.readInt();
                }
            	if(version >= 6){
                	load.readInt();
                }
            	if(version >= 7){
            		for (int i = 0; i < QuestDefinition.QUEST_COUNT_MAX; i++) {
            			try {
            				load.readInt();
            			} catch (IOException e) {
            			}
            		}
            		for (int i = 0; i < 100; i++) {
            			try {
            				load.readInt();
            			} catch (IOException e) {
            			}
            		}
            	}
            	if(version >= 8){
            		load.readByte();
            		load.readByte();
            		load.readByte();
                }
            	if(version >= 9){
            		load.readInt();
                }
            	if(version >= 10){
            		load.readLong();
            		load.readInt();
            		load.readInt();
            	}
            	if(version >= 11){
            		load.readUTF();//mac
            	}
            	if(version >= 12){
            		load.readInt();
            	}
            	if(version >= 13){
            		load.readInt();
            		load.readInt();
            		load.readInt();
            		load.readByte();
            	}
            	
            	//ge
            	if(version >= 14){
            		for (int i = 0; i < 6; i++)
            			load.readBoolean();
            		for (int i = 0; i < 6; i++)
            			load.readInt();
            		for (int i = 0; i < 6; i++)
            			load.readInt();
            		for (int i = 0; i < 6; i++)
            			load.readInt();
            		for (int i = 0; i < 6; i++)
            			load.readBoolean();
            		for (int i = 0; i < 6; i++)
            			load.readInt();
            		for (int i = 0; i < 6; i++)
            			load.readInt();
            		for (int i = 0; i < 6; i++)
            			load.readInt();
            		for (int i = 0; i < 6; i++)
            			load.readInt();
            		for (int i = 0; i < 6; i++)
            			load.readBoolean();
            	}
            	
            	//mage training arena
            	if(version >= 15){
            		load.readInt();
            		load.readInt();
            		load.readInt();
            		load.readInt();
            		load.readBoolean();
            		load.readByte();
            		load.readBoolean();
            		load.readByte();
            	}
            	
            }
			} catch (Exception ex) {
				load.close();
				inFile.close();
				return false;
			}
			load.close();
			inFile.close();
			return true;
		} catch (IOException e) {
			//e.printStackTrace(); //shouldnt get here...
			return false;
		}
	}
	
	public static void updatePlayerFile(final Player player) {
		int i = 0;
		for (Hplayer playerFile : listOfPlayersAll) {
			if(playerFile.getUsername().toLowerCase().equals(player.getUsername().toLowerCase())){
				Hplayer hPlayer = loadPlayerFile(directory, player.getUsername(), true);
				if(hPlayer != null){
					listOfPlayersAll.set(i, hPlayer);
				}
				return;
			}else{
				i++;
				continue;
			}
		}
		Hplayer hPlayer = loadPlayerFile(directory, player.getUsername(), true);
		if(hPlayer != null){
				listOfPlayersAll.add(hPlayer);
			Server.updateAllPlayers();
		}
	}
	
	public static ArrayList<Hplayer> listOfPlayersAll = new ArrayList<Hplayer>();
	
	public static void loadPlayerFiles(){
		listOfPlayersAll.clear();
		File folder = new File(directory);
		File[] listOfPlayerFiles = folder.listFiles();
		for (File playerFile : listOfPlayerFiles) {
			String name = getFileNameWOExtension(playerFile.getName());
			if(fileIsValid(folder.getPath()+"/", name)){
				Hplayer hPlayer = loadPlayerFile(folder.getPath()+"/", name, false);
				if(hPlayer != null){
					/*if(hPlayer.getRights() < 2 && !hPlayer.isDonator()){//check and remove inactive accounts
						if(removeInactiveAccount(hPlayer)){
							try{
								playerFile.delete();
							}catch(Exception ex){
								ex.printStackTrace();
							}
							continue;
						}
					}*/
					setGeOffers(hPlayer);
						listOfPlayersAll.add(hPlayer);
					Server.updateAllPlayers();
				}
				continue;
			}
		}
	}
	
	static boolean removeInactiveAccount(Hplayer hPlayer){
		String name = hPlayer.getUsername();
		//long creationDate = hPlayer.getAccountCreated();
		long playingTime = hPlayer.getPrevPlayingTime();
		long lastLogin = hPlayer.getLastLogin();
		int daysSinceLastLogin = Time.daysBetween(lastLogin, System.currentTimeMillis());
		if(daysSinceLastLogin >= 365){
			System.out.println("Inactive account: "+name+", Days since last login: "+daysSinceLastLogin);
			return true;
		}
		//int daysSinceCreation = Time.daysBetween(creationDate, System.currentTimeMillis());
		if(daysSinceLastLogin >= 180 && Time.getHours(playingTime) < 5){
			System.out.println("Inactive account: "+name+", Play time: "+Time.getHours(playingTime)+" hours.");
			return true;
		}
		return false;
	}
	
	public static Hplayer loadPlayerFile(String path, String playerName, boolean update) {
		File file = new File(path + playerName+ ".dat");
		Hplayer player = new Hplayer();
		/*if(!fileIsValid(path, player.getUsername()) && path.equals(directory)){
			return null;
		}*/
		try {
			FileInputStream inFile = new FileInputStream(file);
			DataInputStream load = new DataInputStream(inFile);
			try {
			int version = load.readShort();//file version
			player.setUsername(load.readUTF());//username
			player.setPassword(load.readUTF());
			player.setLastIp(load.readUTF());
			player.setRights(load.readInt());
			player.setEmail(load.readUTF());//email
			player.setPaypal(load.readUTF());//paypal
			player.setLinkedAcc(load.readUTF());//linked account
			player.setLastLogin(load.readLong());
			player.setPrevPlayingTime(load.readLong());
			player.setAccountCreated(load.readLong());
			player.setTestReward(load.readBoolean());
			player.setDonator(load.readBoolean());
			player.setDonatorPoints(load.readInt());
			player.setX(load.readInt());
			player.setY(load.readInt());
			player.setZ(load.readInt());
			player.setGender(load.readInt());
            //statistics below
			player.setNpcKillcount(load.readInt());
			player.setPlayerKillcount(load.readInt());
			player.setNumberOfDeaths(load.readInt());
			player.setEasyCluesDone(load.readInt());
			player.setMediumCluesDone(load.readInt());
			player.setHardCluesDone(load.readInt());
			player.setSoldItemsWorth(load.readInt());
			player.setBoughtItemsWorth(load.readInt());
			player.setDuelWins(load.readInt());
			player.setDuelLosses(load.readInt());
			player.setTotalDonationAmount(load.readInt());
            //statistics above
			player.setAutoRetaliate(load.readBoolean());
			player.setFightMode(load.readInt());
			player.setScreenBrightness(load.readInt());
			player.setMouseButtons(load.readInt());
			player.setChatEffects(load.readInt());
			player.setSplitPrivateChat(load.readInt());
			player.setAcceptAid(load.readInt());
			player.setMusicVolume(load.readInt());
			player.setEffectVolume(load.readInt());
			player.setSpecialAmount((int)load.readDouble());
			player.setChangingBankPin(load.readBoolean());
			player.setDeletingBankPin(load.readBoolean());
			player.setPinAppendYear(load.readInt());
			player.setPinAppendDay(load.readInt());
			player.setBindingNeckCharge(load.readInt());
			player.setRingOfForgingLife(load.readInt());
			player.setRingOfRecoilLife(load.readInt());
			player.setSkullTimer(load.readInt());
			player.setEnergy(load.readDouble());
			player.setRunToggled(load.readBoolean());
			for (int i = 0; i < 4; i++) {
				player.bankPin[i] = load.readInt();
			}
			for (int i = 0; i < 4; i++) {
				player.pendingBankPin[i] = load.readInt();
			}
			for (int i = 0; i < 4; i++) {
				player.pouchData[i] = load.readInt();
			}
			for (int i = 0; i < 7; i++) {
				player.appearance[i] = load.readInt();
			}
			for (int i = 0; i < 5; i++) {
				player.colors[i] = load.readInt();
			}
			for (int i = 0; i < 22; i++) {//lvl
				player.setSkillLvl(i, load.readInt());
			}
			for (int i = 0; i < 22; i++) {//exp
				player.setSkillExp(i, load.readInt());
			}
			for (int i = 0; i < 28; i++) {
				int id = load.readInt();
				if (id != 65535) {
					int amount = load.readInt();
					int timer = load.readInt();
					player.inventoryItem[i] = new Item(id, amount, timer);
					if(!update){
						int rid = id;
						if(player.inventoryItem[i].getDefinition().isNoted())
							rid = player.inventoryItem[i].getDefinition().getNormalId();
						Misc.addWatchedItem(new Item(rid, amount));
					}
				}
			}
			for (int i = 0; i < 14; i++) {
				int id = load.readInt();
				if (id != 65535) {
					int amount = load.readInt();
					player.equipment[i] = new Item(id, amount);
					if(!update){
						int rid = id;
						if(player.equipment[i].getDefinition().isNoted())
							rid = player.equipment[i].getDefinition().getNormalId();
						Misc.addWatchedItem(new Item(rid, amount));
					}
				}
			}
			try {
				for (int i = 0; i < 352; i++) {
					int id = load.readInt();
					if (id != 65535) {
						int amount = load.readInt();
						int timer = load.readInt();
						player.bank[i] = new Item(id, amount, timer);
						if(!update){
							int rid = id;
							if(player.bank[i].getDefinition().isNoted())
								rid = player.bank[i].getDefinition().getNormalId();
							Misc.addWatchedItem(new Item(rid, amount));
						}
					}
				}
				for (int i = 0; i < 200; i++) {
					player.friend[i] = load.readLong();
				}
				for (int i = 0; i < 100; i++) {
					player.ignore[i] = load.readLong();
				}
				for (int i = 0; i < 28; i++) {
					player.pendingItem[i] = load.readInt();
					player.pendingItemAmount[i] = load.readInt();
				}
				player.setRunecraftNpc(load.readInt());
				player.setMuteExpire(load.readLong());
				player.setBanExpire(load.readLong());
				//load.readBoolean();//tree spirit remove this!!
				for (int i = 0; i < 6; i++) {
					player.barrowsNpcDead[i] = load.readBoolean();
				}
				player.setKillCount(load.readInt());
				player.setRandomGrave(load.readInt());
				int i1 = load.readInt();
				player.setPoisonWaitDuration(i1);
				int i2 = load.readInt();
				player.setFireWaitDuration(i2);
				player.setTeleBlockWaitDuration(load.readInt());
				player.setPoisonDamage(load.readDouble());
				for (int i = 0; i < 8; i++) {
					player.allotmentFarmingStages[i] = load.readInt();
				}
				for (int i = 0; i < 8; i++) {
					player.allotmentFarmingSeeds[i] = load.readInt();
				}
				for (int i = 0; i < 8; i++) {
					player.allotmentFarmingHarvest[i] = load.readInt();
				}
				for (int i = 0; i < 8; i++) {
					player.allotmentFarmingState[i] = load.readInt();
				}
				for (int i = 0; i < 8; i++) {
					player.allotmentFarmingTimer[i] = load.readLong();
				}
				for (int i = 0; i < 8; i++) {
					player.allotmentDiseaseChance[i] = load.readDouble();
				}
				for (int i = 0; i < 8; i++) {
					player.allotmentFarmingWatched[i] = load.readBoolean();
				}
				for (int i = 0; i < 4; i++) {
					player.bushesFarmingStages[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.bushesFarmingSeeds[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.bushesFarmingState[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.bushesFarmingTimer[i] = load.readLong();
				}
				for (int i = 0; i < 4; i++) {
					player.bushesDiseaseChance[i] = load.readDouble();
				}
				for (int i = 0; i < 4; i++) {
					player.bushesFarmingWatched[i] = load.readBoolean();
				}
				for (int i = 0; i < 4; i++) {
					player.flowersFarmingStages[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.flowersFarmingSeeds[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.flowersFarmingState[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.flowersFarmingTimer[i] = load.readLong();
				}
				for (int i = 0; i < 4; i++) {
					player.flowersDiseaseChance[i] = load.readDouble();
				}
				for (int i = 0; i < 4; i++) {
					player.fruitTreeFarmingStages[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.fruitTreeFarmingSeeds[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.fruitTreeFarmingState[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.fruitTreeFarmingTimer[i] = load.readLong();
				}
				for (int i = 0; i < 4; i++) {
					player.fruitTreeDiseaseChance[i] = load.readDouble();
				}
				for (int i = 0; i < 4; i++) {
					player.fruitTreeFarmingWatched[i] = load.readBoolean();
				}
				for (int i = 0; i < 4; i++) {
					player.herbsFarmingStages[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.herbsFarmingSeeds[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.herbsFarmingHarvest[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.herbsFarmingState[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.herbsFarmingTimer[i] = load.readLong();
				}
				for (int i = 0; i < 4; i++) {
					player.herbsDiseaseChance[i] = load.readDouble();
				}
				//load.readDouble();
				for (int i = 0; i < 4; i++) {
					player.hopsFarmingStages[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.hopsFarmingSeeds[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.hopsFarmingHarvest[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.hopsFarmingState[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.hopsFarmingTimer[i] = load.readLong();
				}
				for (int i = 0; i < 4; i++) {
					player.hopsDiseaseChance[i] = load.readDouble();
				}
				for (int i = 0; i < 4; i++) {
					player.hopsFarmingWatched[i] = load.readBoolean();
				}
				for (int i = 0; i < 4; i++) {
					player.specialPlantOneFarmingStages[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.specialPlantOneFarmingSeeds[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.specialPlantOneFarmingState[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.specialPlantOneFarmingTimer[i] = load.readLong();
				}
				for (int i = 0; i < 4; i++) {
					player.specialPlantOneDiseaseChance[i] = load.readDouble();
				}
				for (int i = 0; i < 4; i++) {
					player.specialPlantTwoFarmingStages[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.specialPlantTwoFarmingSeeds[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.specialPlantTwoFarmingState[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.specialPlantTwoFarmingTimer[i] = load.readLong();
				}
				for (int i = 0; i < 4; i++) {
					player.specialPlantTwoDiseaseChance[i] = load.readDouble();
				}
				for (int i = 0; i < 4; i++) {
					player.treesFarmingStages[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.treesFarmingSeeds[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.treesFarmingHarvest[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.treesFarmingState[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.treesFarmingTimer[i] = load.readLong();
				}
				for (int i = 0; i < 4; i++) {
					player.treesDiseaseChance[i] = load.readDouble();
				}
				for (int i = 0; i < 4; i++) {
					player.treesFarmingWatched[i] = load.readBoolean();
				}
				for (int i = 0; i < 4; i++) {
					player.compostBins[i] = load.readInt();
				}
				for (int i = 0; i < 4; i++) {
					player.compostBinsTimer[i] = load.readLong();
				}
				for (int i = 0; i < 4; i++) {
					player.compostBinType[i] = load.readInt();
				}
				for (int i = 0; i < 18; i++) {
					player.tools[i] = load.readInt();
				}
			} catch (IOException e) {
			}
			try {
				player.setSlayerMaster(load.readInt());
				player.setSlayerTask(load.readUTF());
				player.setTaskAmount(load.readInt());
			} catch (IOException e) {
			}
			try {
				player.setMagicBookType(load.readBoolean());//true = ancient
			} catch (IOException e) {
			}
			try {
				player.setBrimhavenDungeonOpen(load.readBoolean());
			} catch (IOException e) {
			}
			try {
				player.setKilledClueAttacker(load.readBoolean());
			} catch (IOException e) {
			}
			//music unlocks
            for (int i = 0; i < musicCfg.length; i++) {
            	try {
            		player.configValue[musicCfg[i]] = load.readInt();
            	} catch (IOException e) {
            	}
            }
            //
            
          //quest stages
            for (int i = 0; i < QuestDefinition.QUEST_COUNT_MAX; i++) {
            	try {
            		//player.questStage[i] = load.readByte();
            		player.questStage[i] = load.readInt();
            	} catch (IOException e) {
            	}
            }
            //
            player.setGang(load.readByte());
            player.setBananaCrate(load.readByte());
            
            //below is what is added after open test
            player.setTalkedWithObservatoryProf(load.readBoolean());
            player.setCoalTruckAmount(load.readByte());
            player.setClueAmount(load.readByte());
            player.setPuzzleDone(load.readBoolean());
            if(version >= 2){
            	player.unlockedSkins = load.readByte();
            	if(version >= 3)
            		player.bossDefeats = load.readInt();
            	if(version >= 4){
                	player.barrowsPuzzleDone = load.readBoolean();
                	player.earthquake = load.readBoolean();
                	player.configValue[Barrows.MAZE_CONFIG] = load.readInt();
                }
            	if(version >= 5){
            		player.setGrainInHopper(load.readByte());
                	player.configValue[FlourMill.FLOUR_BIN_CONFIG] = load.readInt();
                }
            	if(version >= 6){
                	player.uniqueRandomInt = load.readInt();
                }
            	if(version >= 7){
            		for (int i = 0; i < QuestDefinition.QUEST_COUNT_MAX; i++) {
            			try {
            				player.questStageSub1[i] = load.readInt();
            			} catch (IOException e) {
            			}
            		}
            		for (int i = 0; i < 100; i++) {
            			try {
            				player.holidayEventStage[i] = load.readInt();
            			} catch (IOException e) {
            			}
            		}
            	}
            	if(version >= 8){
            		player.publicChatMode = load.readByte();
            		player.privateChatMode = load.readByte();
            		player.tradeMode = load.readByte();
                }
            	if(version >= 9){
                	player.loyaltyItem = load.readInt();
                }
            	if(version >= 10){
                	player.dailyTaskTime = load.readLong();
                	player.dailyTaskId = load.readInt();
                	player.dailyTaskAmount = load.readInt();
                }
            	if(version >= 11){
            		player.setMACaddress(load.readUTF());//mac
                }
            	if(version >= 12){
                	player.familyGauntlets = load.readInt();
                }
            	if(version >= 13){
                	player.flameOfZamorakCasts = load.readInt();
                	player.saradominStrikeCasts = load.readInt();
                	player.clawsOfGuthixCasts = load.readInt();
                	player.kolodionStage = load.readByte();
                }
            	
            	//ge
            	if(version >= 14){
            		for (int i = 0; i < 6; i++)
            			player.geOfferType[i] = load.readBoolean();
            		for (int i = 0; i < 6; i++)
            			player.geOfferItemId[i] = load.readInt();
            		for (int i = 0; i < 6; i++)
            			player.geOfferItemAmount[i] = load.readInt();
            		for (int i = 0; i < 6; i++)
            			player.geOfferPrice[i] = load.readInt();
            		for (int i = 0; i < 6; i++)
            			player.geIsCanceled[i] = load.readBoolean();
            		for (int i = 0; i < 6; i++)
            			player.geItemsTraded[i] = load.readInt();
            		for (int i = 0; i < 6; i++)
            			player.geCashMoved[i] = load.readInt();
            		for (int i = 0; i < 6; i++)
            			player.geCollectItem2Receive[i] = load.readInt();
            		for (int i = 0; i < 6; i++)
            			player.geCollectOtherItem[i] = load.readInt();
            		for (int i = 0; i < 6; i++)
            			player.geNotifyProgress[i] = load.readBoolean();
            	}
            	
            	//mage training arena
            	if(version >= 15){
            		player.telekineticPizzazPoint = load.readInt();
            		player.enchantmentPizzazPoint = load.readInt();
            		player.alchemistPizzazPoint = load.readInt();
            		player.graveyardPizzazPoints = load.readInt();
            		player.bonesToPeachUnlocked = load.readBoolean();
            		player.mazeIndex = load.readByte();
            		player.mazeCompleted = load.readBoolean();
            		player.solvedMazes = load.readByte();
            	}

            }
			} catch (Exception ex) {
				load.close();
				inFile.close();
				System.out.println("error at PlayerSave");
				return null;
			}
			load.close();
			inFile.close();
			player.getWealth();
			return player;
		} catch (IOException e) {
			//e.printStackTrace(); //shouldnt get here...
			System.out.println("error at PlayerSave");
			return null;
		}
	}
	
	public static void setGeOffers(Hplayer player){
		for (int i = 0; i < 6; i++){
			if(player.geOfferItemAmount[i] > 0 && !player.geIsCanceled[i])
				new GeOffer(player.getUsername(), i, player.geOfferType[i], player.geOfferItemId[i], player.geOfferItemAmount[i]-player.geItemsTraded[i], player.geOfferItemAmount[i], player.geOfferPrice[i]);
		}
	}
	
	public static void readFile(String path, Player player) {
		File file = new File(path + player.getUsername()
				+ ".dat");
		if (!file.exists()) {
			if (Server.getSingleton() != null)
				Server.getSingleton().queueLogin(player);
			return;
		}
		if(!fileIsValid(path, player.getUsername()) && path.equals(directory)){
			restoreFromBackup(player);
			return;
		}
		try {
			FileInputStream inFile = new FileInputStream(file);
			DataInputStream load = new DataInputStream(inFile);
			int version = load.readShort();//file version
			 player.setUsername(load.readUTF());
			 String password = load.readUTF();
			 player.setPassword(password);
			 player.lastIP = load.readUTF();
			 player.setStaffRights(load.readInt());
			 load.readUTF();//email
			 player.setPaypal(load.readUTF());//paypal
			 player.setLinkedAcc(load.readUTF());//linked account
			 player.lastLogin = load.readLong();
			 player.prev_playingTime = load.readLong();
			 player.accountCreated = load.readLong();
			 player.testReward = load.readBoolean();
			 player.setDonator(load.readBoolean());
			 player.setDonatorPoints(load.readInt());
			player.getPosition().setX(load.readInt());
			player.getPosition().setLastX(
					player.getPosition().getX());
			player.getPosition().setY(load.readInt());
			player.getPosition().setLastY(
					player.getPosition().getY() + 1);
			player.getPosition().setZ(load.readInt());
			player.setGender(load.readInt());
            //statistics below
			player.npcKillCount = load.readInt();
			player.playerKillCount = load.readInt();
			player.numberOfDeaths = load.readInt();
			player.easyCluesDone = load.readInt();
			player.mediumCluesDone = load.readInt();
			player.hardCluesDone = load.readInt();
			player.soldItemsWorth = load.readInt();
			player.boughtItemsWorth = load.readInt();
			player.duelWins = load.readInt();
			player.duelLosses = load.readInt();
			player.totalDonationAmount = load.readInt();
            //statistics above
			player.setAutoRetaliate(load.readBoolean());
			player.setFightMode(load.readInt());
			player.setScreenBrightness(load.readInt());
			player.setMouseButtons(load.readInt());
			player.setChatEffects(load.readInt());
			player.setSplitPrivateChat(load.readInt());
			player.setAcceptAid(load.readInt());
			player.setMusicVolume(load.readInt());
			player.setEffectVolume(load.readInt());
			player.setSpecialAmount((int) load.readDouble());

			player.getBankPin().setChangingBankPin(
					load.readBoolean());
			player.getBankPin().setDeletingBankPin(
					load.readBoolean());
			player.getBankPin()
			.setPinAppendYear(load.readInt());
			player.getBankPin().setPinAppendDay(load.readInt());

			player.setBindingNeckCharge(load.readInt());
			player.setRingOfForgingLife(load.readInt());
			player.setRingOfRecoilLife(load.readInt());
			int skullTimer = load.readInt();
			if (skullTimer > 0)
				player.addSkull(player, skullTimer);
			player.setEnergy(load.readDouble());
			player.getMovementHandler().setRunToggled(load.readBoolean());
			for (int i = 0; i < player.getBankPin().getBankPin().length; i++) {
				player.getBankPin().getBankPin()[i] = load.readInt();
			}
			for (int i = 0; i < player.getBankPin()
					.getPendingBankPin().length; i++) {
				player.getBankPin().getPendingBankPin()[i] = load
						.readInt();
			}
			for (int i = 0; i < 4; i++) {
				player.setPouchData(i, load.readInt());
			}
			for (int i = 0; i < player.getAppearance().length; i++) {
				player.getAppearance()[i] = load.readInt();
			}
			for (int i = 0; i < player.getColors().length; i++) {
				player.getColors()[i] = load.readInt();
			}
			for (int i = 0; i < player.getSkill().getLevel().length; i++) {
				player.getSkill().getLevel()[i] = load
						.readInt();
			}
			for (int i = 0; i < player.getSkill().getExp().length; i++) {
				player.getSkill().getExp()[i] = load.readInt();
			}
			for (int i = 0; i < 28; i++) {
				int id = load.readInt();
				if (id != 65535) {
					int amount = load.readInt();
					int timer = load.readInt();
					if (id < Constants.MAX_ITEM_ID && amount > 0)  {
						Item item = new Item(id, amount, timer);
						if (item.getId() == 2696 || item.getId() == 2699 || item.getId() == 3510) {
							item = new Item(id - 1, amount, timer);
						}
						player.getInventory().getItemContainer().set(i, item);
					}
				}
			}
			for (int i = 0; i < 14; i++) {
				int id = load.readInt();
				if (id != 65535) {
					int amount = load.readInt();
					if (id < Constants.MAX_ITEM_ID && amount > 0)  {
						Item item = new Item(id, amount);
						player.getEquipment().getItemContainer().set(i, item);
					}
				}
			}
			try {
				for (int i = 0; i < BankManager.SIZE; i++) {
					int id = load.readInt();
					if (id != 65535) {
						int amount = load.readInt();
						int timer = load.readInt();
						if (id < Constants.MAX_ITEM_ID && amount > 0)  {
							Item item = new Item(id, amount, timer);
							if (item.getId() == 2696 || item.getId() == 2699 || item.getId() == 3510) {
								item = new Item(id - 1, amount, timer);
							}
							player.getBank().set(i, item);
						}
					}
				}
				for (int i = 0; i < player.getFriends().length; i++) {
					player.getFriends()[i] = load.readLong();
				}
				for (int i = 0; i < player.getIgnores().length; i++) {
					player.getIgnores()[i] = load.readLong();
				}
				for (int i = 0; i < player.getPendingItems().length; i++) {
					player.getPendingItems()[i] = load.readInt();
					player.getPendingItemsAmount()[i] = load.readInt();
				}
				player.setRunecraftNpc(load.readInt());
				player.setMuteExpire(load.readLong());
				player.setBanExpire(load.readLong());
				//load.readBoolean();//tree spirit... remove this
				for (int i = 0; i < 6; i++) {
					player.setBarrowsNpcDead(i, load.readBoolean());
				}
				player.setKillCount(load.readInt());
				player.setRandomGrave(load.readInt());
				int i1 = load.readInt();
				player.getPoisonImmunity().setWaitDuration(i1);
				player.getPoisonImmunity().reset();
				int i2 = load.readInt();
				player.getFireImmunity().setWaitDuration(i2);
				player.getFireImmunity().reset();
				player.getTeleblockTimer().setWaitDuration(load.readInt());
				player.getTeleblockTimer().reset();
				double d1 = load.readDouble();
				double poison = d1;
				player.setPoisonDamage(poison);
				for (int i = 0; i < player.getAllotment().getFarmingStages().length; i++) {
					player.getAllotment().setFarmingStages(i, load.readInt());
				}
				for (int i = 0; i < player.getAllotment().getFarmingSeeds().length; i++) {
					player.getAllotment().setFarmingSeeds(i, load.readInt());
				}
				for (int i = 0; i < player.getAllotment().getFarmingHarvest().length; i++) {
					player.getAllotment().setFarmingHarvest(i, load.readInt());
				}
				for (int i = 0; i < player.getAllotment().getFarmingState().length; i++) {
					player.getAllotment().setFarmingState(i, load.readInt());
				}
				for (int i = 0; i < player.getAllotment().getFarmingTimer().length; i++) {
					player.getAllotment().setFarmingTimer(i, load.readLong());
				}
				for (int i = 0; i < player.getAllotment().getDiseaseChance().length; i++) {
					player.getAllotment().setDiseaseChance(i, load.readDouble());
				}
				for (int i = 0; i < player.getAllotment().getFarmingWatched().length; i++) {
					player.getAllotment().setFarmingWatched(i, load.readBoolean());
				}
				for (int i = 0; i < player.getBushes().getFarmingStages().length; i++) {
					player.getBushes().setFarmingStages(i, load.readInt());
				}
				for (int i = 0; i < player.getBushes().getFarmingSeeds().length; i++) {
					player.getBushes().setFarmingSeeds(i, load.readInt());
				}
				for (int i = 0; i < player.getBushes().getFarmingState().length; i++) {
					player.getBushes().setFarmingState(i, load.readInt());
				}
				for (int i = 0; i < player.getBushes().getFarmingTimer().length; i++) {
					player.getBushes().setFarmingTimer(i, load.readLong());
				}
				for (int i = 0; i < player.getBushes().getDiseaseChance().length; i++) {
					player.getBushes().setDiseaseChance(i, load.readDouble());
				}
				for (int i = 0; i < player.getBushes().getFarmingWatched().length; i++) {
					player.getBushes().setFarmingWatched(i, load.readBoolean());
				}
				for (int i = 0; i < player.getFlowers().getFarmingStages().length; i++) {
					player.getFlowers().setFarmingStages(i, load.readInt());
				}
				for (int i = 0; i < player.getFlowers().getFarmingSeeds().length; i++) {
					player.getFlowers().setFarmingSeeds(i, load.readInt());
				}
				for (int i = 0; i < player.getFlowers().getFarmingState().length; i++) {
					player.getFlowers().setFarmingState(i, load.readInt());
				}
				for (int i = 0; i < player.getFlowers().getFarmingTimer().length; i++) {
					player.getFlowers().setFarmingTimer(i, load.readLong());
				}
				for (int i = 0; i < player.getFlowers().getDiseaseChance().length; i++) {
					player.getFlowers().setDiseaseChance(i, load.readDouble());
				}
				for (int i = 0; i < player.getFruitTrees().getFarmingStages().length; i++) {
					player.getFruitTrees().setFarmingStages(i, load.readInt());
				}
				for (int i = 0; i < player.getFruitTrees().getFarmingSeeds().length; i++) {
					player.getFruitTrees().setFarmingSeeds(i, load.readInt());
				}
				for (int i = 0; i < player.getFruitTrees().getFarmingState().length; i++) {
					player.getFruitTrees().setFarmingState(i, load.readInt());
				}
				for (int i = 0; i < player.getFruitTrees().getFarmingTimer().length; i++) {
					player.getFruitTrees().setFarmingTimer(i, load.readLong());
				}
				for (int i = 0; i < player.getFruitTrees().getDiseaseChance().length; i++) {
					player.getFruitTrees().setDiseaseChance(i, load.readDouble());
				}
				for (int i = 0; i < player.getFruitTrees().getFarmingWatched().length; i++) {
					player.getFruitTrees().setFarmingWatched(i, load.readBoolean());
				}
				for (int i = 0; i < player.getHerbs().getFarmingStages().length; i++) {
					player.getHerbs().setFarmingStages(i, load.readInt());
				}
				for (int i = 0; i < player.getHerbs().getFarmingSeeds().length; i++) {
					player.getHerbs().setFarmingSeeds(i, load.readInt());
				}
				for (int i = 0; i < player.getHerbs().getFarmingHarvest().length; i++) {
					player.getHerbs().setFarmingHarvest(i, load.readInt());
				}
				for (int i = 0; i < player.getHerbs().getFarmingState().length; i++) {
					player.getHerbs().setFarmingState(i, load.readInt());
				}
				for (int i = 0; i < player.getHerbs().getFarmingTimer().length; i++) {
					player.getHerbs().setFarmingTimer(i, load.readLong());
				}
				for (int i = 0; i < player.getHerbs().getDiseaseChance().length; i++) {
					player.getHerbs().setDiseaseChance(i, load.readDouble());
				}
				for (int i = 0; i < player.getHops().getFarmingStages().length; i++) {
					player.getHops().setFarmingStages(i, load.readInt());
				}
				for (int i = 0; i < player.getHops().getFarmingSeeds().length; i++) {
					player.getHops().setFarmingSeeds(i, load.readInt());
				}
				for (int i = 0; i < player.getHops().getFarmingHarvest().length; i++) {
					player.getHops().setFarmingHarvest(i, load.readInt());
				}
				for (int i = 0; i < player.getHops().getFarmingState().length; i++) {
					player.getHops().setFarmingState(i, load.readInt());
				}
				for (int i = 0; i < player.getHops().getFarmingTimer().length; i++) {
					player.getHops().setFarmingTimer(i, load.readLong());
				}
				for (int i = 0; i < player.getHops().getDiseaseChance().length; i++) {
					player.getHops().setDiseaseChance(i, load.readDouble());
				}
				for (int i = 0; i < player.getHops().getFarmingWatched().length; i++) {
					player.getHops().setFarmingWatched(i, load.readBoolean());
				}
				for (int i = 0; i < player.getSpecialPlantOne().getFarmingStages().length; i++) {
					player.getSpecialPlantOne().setFarmingStages(i, load.readInt());
				}
				for (int i = 0; i < player.getSpecialPlantOne().getFarmingSeeds().length; i++) {
					player.getSpecialPlantOne().setFarmingSeeds(i, load.readInt());
				}
				for (int i = 0; i < player.getSpecialPlantOne().getFarmingState().length; i++) {
					player.getSpecialPlantOne().setFarmingState(i, load.readInt());
				}
				for (int i = 0; i < player.getSpecialPlantOne().getFarmingTimer().length; i++) {
					player.getSpecialPlantOne().setFarmingTimer(i, load.readLong());
				}
				for (int i = 0; i < player.getSpecialPlantOne().getDiseaseChance().length; i++) {
					player.getSpecialPlantOne().setDiseaseChance(i, load.readDouble());
				}
				for (int i = 0; i < player.getSpecialPlantTwo().getFarmingStages().length; i++) {
					player.getSpecialPlantTwo().setFarmingStages(i, load.readInt());
				}
				for (int i = 0; i < player.getSpecialPlantTwo().getFarmingSeeds().length; i++) {
					player.getSpecialPlantTwo().setFarmingSeeds(i, load.readInt());
				}
				for (int i = 0; i < player.getSpecialPlantTwo().getFarmingState().length; i++) {
					player.getSpecialPlantTwo().setFarmingState(i, load.readInt());
				}
				for (int i = 0; i < player.getSpecialPlantTwo().getFarmingTimer().length; i++) {
					player.getSpecialPlantTwo().setFarmingTimer(i, load.readLong());
				}
				for (int i = 0; i < player.getSpecialPlantTwo().getDiseaseChance().length; i++) {
					player.getSpecialPlantTwo().setDiseaseChance(i, load.readDouble());
				}
				for (int i = 0; i < player.getTrees().getFarmingStages().length; i++) {
					player.getTrees().setFarmingStages(i, load.readInt());
				}
				for (int i = 0; i < player.getTrees().getFarmingSeeds().length; i++) {
					player.getTrees().setFarmingSeeds(i, load.readInt());
				}
				for (int i = 0; i < player.getTrees().getFarmingHarvest().length; i++) {
					player.getTrees().setFarmingHarvest(i, load.readInt());
				}
				for (int i = 0; i < player.getTrees().getFarmingState().length; i++) {
					player.getTrees().setFarmingState(i, load.readInt());
				}
				for (int i = 0; i < player.getTrees().getFarmingTimer().length; i++) {
					player.getTrees().setFarmingTimer(i, load.readLong());
				}
				for (int i = 0; i < player.getTrees().getDiseaseChance().length; i++) {
					player.getTrees().setDiseaseChance(i, load.readDouble());
				}
				for (int i = 0; i < player.getTrees().getFarmingWatched().length; i++) {
					player.getTrees().setFarmingWatched(i, load.readBoolean());
				}
				for (int i = 0; i < player.getCompost().getCompostBins().length; i++) {
					player.getCompost().setCompostBins(i, load.readInt());
				}
				for (int i = 0; i < player.getCompost().getCompostBinsTimer().length; i++) {
					player.getCompost().setCompostBinsTimer(i, load.readLong());
				}
				/*for (int i = 0; i < player.getCompost().getOrganicItemAdded().length; i++) {
					player.getCompost().setOrganicItemAdded(i, load.readInt());
				}*/
				for (int i = 0; i < player.getCompost().getCompostBinType().length; i++) {
					//player.getCompost().setOrganicItemAdded(i, load.readInt());
					player.getCompost().setCompostBinType(i, load.readInt());
				}
				for (int i = 0; i < player.getFarmingTools().getTools().length; i++) {
					player.getFarmingTools().setTools(i, load.readInt());
				}
			} catch (IOException e) {
			}
			try {
				player.getSlayer().slayerMaster = load.readInt();
				player.getSlayer().slayerTask = load.readUTF();
				player.getSlayer().taskAmount = load.readInt();
			} catch (IOException e) {
			}
			try {
				boolean ancient = load.readBoolean();
				player.setMagicBookType(ancient ? SpellBook.ANCIENT : SpellBook.MODERN);
			} catch (IOException e) {
			}
			try {
				player.setBrimhavenDungeonOpen(load.readBoolean());
			} catch (IOException e) {
			}
			try {
				player.setKilledClueAttacker(load.readBoolean());
			} catch (IOException e) {
			}
			//music unlocks
            for (int i = 0; i < musicCfg.length; i++) {
            	try {
            		player.configValue[musicCfg[i]] = load.readInt();
            	} catch (IOException e) {
            		player.configValue[musicCfg[i]] = 0;
            	}
            }
            //
            
          //quest stages
            for (int i = 0; i < QuestDefinition.QUEST_COUNT_MAX; i++) {
            	try {
            		//player.questStage[i] = load.readByte();
            		player.questStage[i] = load.readInt();
            	} catch (IOException e) {
            		player.questStage[i] = 0;
            	}
            }
            //
            player.gang = load.readByte();
            player.bananaCrate = load.readByte();
            
            //below is what is added after open test
            player.talkedWithObservatoryProf = load.readBoolean();
            player.setCoalTruckAmount(load.readByte());
            player.clueAmount = load.readByte();
            player.puzzleDone = load.readBoolean();
            
            if(version >= 2){
            	player.unlockedSkins = load.readByte();
            	if(version >= 3)
            		player.setBossDefeats(load.readInt());
            	if(version >= 4){
                	player.barrowsPuzzleDone = load.readBoolean();
                	player.earthquake = load.readBoolean();
                	player.configValue[Barrows.MAZE_CONFIG] = load.readInt();
                }
            	if(version >= 5){
            		player.setGrainInHopper(load.readByte());
                	player.configValue[FlourMill.FLOUR_BIN_CONFIG] = load.readInt();
                }
            	if(version >= 6){
                	player.uniqueRandomInt = load.readInt();
                }
            	if(version >= 7){
            		for (int i = 0; i < QuestDefinition.QUEST_COUNT_MAX; i++) {
                		try {
                			player.questStageSub1[i] = load.readInt();
                		} catch (IOException e) {
                			player.questStageSub1[i] = 0;
                		}
            		}
            		for (int i = 0; i < 100; i++) {
                		try {
                			player.holidayEventStage[i] = load.readInt();
                		} catch (IOException e) {
                			player.holidayEventStage[i] = 0;
                		}
                	}
            	}
            	if(version >= 8){
            		player.setPublicChatMode(load.readByte());
            		player.setPrivateChatMode(load.readByte());
            		player.setTradeMode(load.readByte());
                }
            	if(version >= 9){
            		player.loyaltyItem = load.readInt();
                }
            	if(version >= 10){
            		player.dailyTaskTime = load.readLong();
            		player.dailyTaskId = load.readInt();
            		player.dailyTaskAmount = load.readInt();
                }
            	if(version >= 11){
            		player.lastMAC = load.readUTF();//mac
                }
            	if(version >= 12){
            		player.familyGauntlets = load.readInt();
                }
            	if(version >= 13){
            		player.flameOfZamorakCasts = load.readInt();
            		player.saradominStrikeCasts = load.readInt();
            		player.clawsOfGuthixCasts = load.readInt();
            		player.kolodionStage = load.readByte();
                }
            	//ge
            	if(version >= 14){
            		for (int i = 0; i < 6; i++)
            			player.geOfferType[i] = load.readBoolean();
            		for (int i = 0; i < 6; i++)
            			player.geOfferItemId[i] = load.readInt();
            		for (int i = 0; i < 6; i++)
            			player.geOfferItemAmount[i] = load.readInt();
            		for (int i = 0; i < 6; i++)
            			player.geOfferPrice[i] = load.readInt();
            		for (int i = 0; i < 6; i++)
            			player.geIsCanceled[i] = load.readBoolean();
            		for (int i = 0; i < 6; i++)
            			player.geItemsTraded[i] = load.readInt();
            		for (int i = 0; i < 6; i++)
            			player.geCashMoved[i] = load.readInt();
            		for (int i = 0; i < 6; i++)
            			player.geCollectItem2Receive[i] = load.readInt();
            		for (int i = 0; i < 6; i++)
            			player.geCollectOtherItem[i] = load.readInt();
            		for (int i = 0; i < 6; i++)
            			player.geNotifyProgress[i] = load.readBoolean();
            	}
            	
            	//mage training arena
            	if(version >= 15){
            		player.getTelekineticTheatre().telekineticPizzazPoint = load.readInt();
            		player.getEnchantingChamber().enchantmentPizzazPoint = load.readInt();
            		player.getAlchemistPlayground().alchemistPizzazPoint = load.readInt();
            		player.getCreatureGraveyard().graveyardPizzazPoints = load.readInt();
            		player.bonesToPeachUnlocked = load.readBoolean();
            		player.getTelekineticTheatre().mazeIndex = load.readByte();
            		player.getTelekineticTheatre().mazeCompleted = load.readBoolean();
            		player.getTelekineticTheatre().solvedMazes = load.readByte();
            	}
            }
            AppearancePacketHandler.checkOutfitRanges(player);
            
            if(!path.equals(directory)){
            	player.restoredFromBackup = true;
            	Logger.logEvent(System.currentTimeMillis()+"§"+player.getUsername()+"§"+player.lastLogin, "restored");
            }
            
			load.close();
			if (Server.getSingleton() != null)
				Server.getSingleton().queueLogin(player);
			return;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Account not loading: " + player);
			//corrupted save file
			//if (Server.getSingleton() != null)
			//	Server.getSingleton().queueLogin(player);
			return;
		}
	}

	public static class ConnectionAttempt {
		private static final long TIMEOUT = 5 * 60 * 1000;
		private static final int MAX_ATTEMPTS = 5;
		Misc.Stopwatch timer;
		int attempts;

		public ConnectionAttempt() {
			timer = new Misc.Stopwatch();
		}

		public void addAttempt() {
			if (attempts < MAX_ATTEMPTS)
				attempts += 1;
			else timer.reset();
		}

		public void reset() {
			attempts = 0;
		}

		public boolean canConnect() {
			return attempts != MAX_ATTEMPTS || timer.elapsed() >= TIMEOUT;
		}
	}
}
