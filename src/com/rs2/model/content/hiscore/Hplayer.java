package com.rs2.model.content.hiscore;

import com.rs2.model.content.questing.QuestDefinition;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;

public class Hplayer{
	
	public String username;
	public String password;
	public String lastIp = "0.0.0.0";
	public String MACaddress = "";
	public int rights;
	public String email = "";
	public String paypal = "";
	public String linkedAcc = "";
	public long lastLogin;
	public long prevPlayingTime;
	public long accountCreated;
	public boolean testReward;
	public boolean donator;
	public int donatorPoints;
	public int donatorRank;
	public int bossDefeats;
	public int x = 3093;
	public int y = 3104;
	public int z = 0;
	public int unlockedSkins = 0;
	public int gender;
	public int npcKillcount;
	public int playerKillcount;
	public int numberOfDeaths;
	public int easyCluesDone;
	public int mediumCluesDone;
	public int hardCluesDone;
	public int soldItemsWorth;
	public int boughtItemsWorth;
	public int duelWins;
	public int duelLosses;
	public int totalDonationAmount;
	public boolean autoRetaliate;
	public int fightMode;
	public int screenBrightness = 2;
	public int mouseButtons;
	public int chatEffects = 1;
	public int splitPrivateChat;
	public int acceptAid;
	public int musicVolume;
	public int effectVolume;
	public int specialAmount = 100;
	public boolean changingBankPin;
	public boolean deletingBankPin;
	public int pinAppendYear = -1;
	public int pinAppendDay = -1;
	public int bindingNeckCharge = 15;
	public int ringOfForgingLife = 140;
	public int ringOfRecoilLife = 40;
	public int skullTimer;
	public double energy = 100;
	public boolean runToggled;
	public int runecraftNpc;
	public long muteExpire;
	public long banExpire;
	public int killCount;
	public int randomGrave;
	public int poisonWaitDuration;
	public int fireWaitDuration;
	public int teleBlockWaitDuration;
	public double poisonDamage;
	public int slayerMaster;
	public String slayerTask = "";
	public int taskAmount;
	public boolean magicBookType;
	public boolean brimhavenDungeonOpen;
	public boolean killedClueAttacker;
	public int gang;
	public int bananaCrate;
	public boolean talkedWithObservatoryProf;
	public int coalTruckAmount;
	public int clueAmount;
	public boolean puzzleDone;
	public int bankPin[] = {-1, -1, -1, -1};
	public int pendingBankPin[] = {-1, -1, -1, -1};
	public int pouchData[] = new int[4];
	public int appearance[] = new int[7];
	public int colors[] = new int[5];
	public Item inventoryItem[] = new Item[28];
	public Item equipment[] = new Item[14];
	public Item bank[] = new Item[352];
	public long friend[] = new long[200];
	public long ignore[] = new long[100];
	public int pendingItem[] = new int[28];
	public int pendingItemAmount[] = new int[28];
	public boolean barrowsNpcDead[] = new boolean[6];
	public int allotmentFarmingStages[] = new int[8];
	public int allotmentFarmingSeeds[] = new int[8];
	public int allotmentFarmingHarvest[] = new int[8];
	public int allotmentFarmingState[] = new int[8];
	public long allotmentFarmingTimer[] = new long[8];
	public double allotmentDiseaseChance[] = new double[8];
	public boolean allotmentFarmingWatched[] = new boolean[8];
	public int bushesFarmingStages[] = new int[4];
	public int bushesFarmingSeeds[] = new int[4];
	public int bushesFarmingState[] = new int[4];
	public long bushesFarmingTimer[] = new long[4];
	public double bushesDiseaseChance[] = new double[4];
	public boolean bushesFarmingWatched[] = new boolean[4];
	public int flowersFarmingStages[] = new int[4];
	public int flowersFarmingSeeds[] = new int[4];
	public int flowersFarmingState[] = new int[4];
	public long flowersFarmingTimer[] = new long[4];
	public double flowersDiseaseChance[] = new double[4];
	public int fruitTreeFarmingStages[] = new int[4];
	public int fruitTreeFarmingSeeds[] = new int[4];
	public int fruitTreeFarmingState[] = new int[4];
	public long fruitTreeFarmingTimer[] = new long[4];
	public double fruitTreeDiseaseChance[] = new double[4];
	public boolean fruitTreeFarmingWatched[] = new boolean[4];
	public int herbsFarmingStages[] = new int[4];
	public int herbsFarmingSeeds[] = new int[4];
	public int herbsFarmingHarvest[] = new int[4];
	public int herbsFarmingState[] = new int[4];
	public long herbsFarmingTimer[] = new long[4];
	public double herbsDiseaseChance[] = new double[4];
	public int hopsFarmingStages[] = new int[4];
	public int hopsFarmingSeeds[] = new int[4];
	public int hopsFarmingHarvest[] = new int[4];
	public int hopsFarmingState[] = new int[4];
	public long hopsFarmingTimer[] = new long[4];
	public double hopsDiseaseChance[] = new double[4];
	public boolean hopsFarmingWatched[] = new boolean[4];
	public int specialPlantOneFarmingStages[] = new int[4];
	public int specialPlantOneFarmingSeeds[] = new int[4];
	public int specialPlantOneFarmingState[] = new int[4];
	public long specialPlantOneFarmingTimer[] = new long[4];
	public double specialPlantOneDiseaseChance[] = new double[4];
	public int specialPlantTwoFarmingStages[] = new int[4];
	public int specialPlantTwoFarmingSeeds[] = new int[4];
	public int specialPlantTwoFarmingState[] = new int[4];
	public long specialPlantTwoFarmingTimer[] = new long[4];
	public double specialPlantTwoDiseaseChance[] = new double[4];
	public int treesFarmingStages[] = new int[4];
	public int treesFarmingSeeds[] = new int[4];
	public int treesFarmingHarvest[] = new int[4];
	public int treesFarmingState[] = new int[4];
	public long treesFarmingTimer[] = new long[4];
	public double treesDiseaseChance[] = new double[4];
	public boolean treesFarmingWatched[] = new boolean[4];
	public int compostBins[] = new int[4];
	public long compostBinsTimer[] = new long[4];
	public int compostBinType[] = new int[4];
	public int tools[] = new int[18];
	public int configValue[] = new int[1000];
	public int questStage[] = new int[QuestDefinition.QUEST_COUNT_MAX];
	public int questStageSub1[] = new int[QuestDefinition.QUEST_COUNT_MAX];
	public int holidayEventStage[] = new int[100];
	public int skillLvl[] = new int[22];
	public long skillExp[] = new long[22];
	public boolean barrowsPuzzleDone;
	public boolean earthquake;
	int wealth = 0;
	//ge
	public boolean geOfferType[] = new boolean[6];//true = sell, false = buy
	public int geOfferItemId[] = new int[6];
	public int geOfferItemAmount[] = new int[6];
	public int geOfferPrice[] = new int[6];
	public boolean geIsCanceled[] = new boolean[6];
	public int geItemsTraded[] = new int[6];
	public int geCashMoved[] = new int[6];
	public int geCollectItem2Receive[] = new int[6];
	public int geCollectOtherItem[] = new int[6];
	public boolean geNotifyProgress[] = new boolean[6];
	
	public static final int[][][] COLOR_RANGES = new int[][][] { { { 0, 0, 0, 0, 0 },  { 11, 15, 15, 5, 7} }, { { 0, 0, 0, 0, 0 }, { 11, 15, 15, 5, 7} } };
    public static final int[][][] APPEARANCE_RANGES = new int[][][] { { { 18, 26, 36, 0, 33, 42, 10 }, { 25, 31, 40, 8, 34, 43, 17 } }, { { 56, 61, 70, 45, 67, 79, -1 }, { 60, 65, 77, 54, 68, 80, -1 } } };
	
	public Hplayer(){
	}
	
	public int grainInHopper = 0;
	public int uniqueRandomInt;
	public int publicChatMode = 0;
	public int privateChatMode = 0;
	public int tradeMode = 0;
	public int loyaltyItem = 0;
	public long dailyTaskTime = 0;
	public int dailyTaskId = 0;
	public int dailyTaskAmount = 0;
	public int familyGauntlets = 778;
	public int flameOfZamorakCasts = 100;
	public int saradominStrikeCasts = 100;
	public int clawsOfGuthixCasts = 100;
	public int kolodionStage = 0;
	public int telekineticPizzazPoint = 0;
	public int enchantmentPizzazPoint = 0;
	public int alchemistPizzazPoint = 0;
	public int graveyardPizzazPoints = 0;
	public boolean bonesToPeachUnlocked = false;
	public int mazeIndex = 0;
	public boolean mazeCompleted = false;
	public int solvedMazes = 0;
	
	public String getMACaddress() {
		return MACaddress;
	}

	public void setMACaddress(String mACaddress) {
		MACaddress = mACaddress;
	}

	public int getGrainInHopper() {
		return grainInHopper;
	}

	public int getWealth(){
		if(wealth == 0){
			for(Item item : bank){
				if(item == null)
					continue;
				if(item.getCount() < 1)
					continue;
				ItemDefinition def = item.getDefinition();
				if(def.isUntradable())
					continue;
				wealth += def.getPrice()*item.getCount();
			}
			for(Item item : inventoryItem){
				if(item == null)
					continue;
				if(item.getCount() < 1)
					continue;
				ItemDefinition def = item.getDefinition();
				if(def.isUntradable())
					continue;
				wealth += def.getPrice()*item.getCount();
			}
			for(Item item : equipment){
				if(item == null)
					continue;
				if(item.getCount() < 1)
					continue;
				ItemDefinition def = item.getDefinition();
				if(def.isUntradable())
					continue;
				wealth += def.getPrice()*item.getCount();
			}
		}
		return wealth;
	}

	public void setGrainInHopper(int grainInHopper) {
		this.grainInHopper = grainInHopper;
	}
	
	public void dump(){
		System.out.println(username+" "+testReward);
		//System.out.println(runecraftNpc);
		/*for (int i = 0; i < 105; i++) {
        	System.out.println(i+" "+questStage[i]);
        }*/
	}
	
	public void setDefaultValues(){
		setDefaultAppearance();
		setDefaultLevels();
	}
	
	public void setDefaultAppearance() {
		setGender(0);
		int[] defaultAppearances = APPEARANCE_RANGES[gender][0];
		System.arraycopy(defaultAppearances, 0, appearance, 0, appearance.length);
		int[] defaultColors = COLOR_RANGES[gender][0];
		System.arraycopy(defaultColors, 0, colors, 0, colors.length);
		colors[0] = 7;
		colors[1] = 0;
		colors[2] = 9;
		colors[3] = 5;
		colors[4] = 0;
	}
	
	public void setDefaultLevels(){
		for (int i = 0; i < skillLvl.length; i++) {
			if (i == 3) {
				skillLvl[i] = 10;
				skillExp[i] = 1154;
			} else {
				skillLvl[i] = 1;
				skillExp[i] = 0;
			}
		}
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastIp() {
		return lastIp;
	}

	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}

	public int getRights() {
		return rights;
	}

	public void setRights(int rights) {
		this.rights = rights;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPaypal() {
		return paypal;
	}

	public void setPaypal(String paypal) {
		this.paypal = paypal;
	}

	public String getLinkedAcc() {
		return linkedAcc;
	}

	public void setLinkedAcc(String linkedAcc) {
		this.linkedAcc = linkedAcc;
	}

	public long getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(long lastLogin) {
		this.lastLogin = lastLogin;
	}

	public long getPrevPlayingTime() {
		return prevPlayingTime;
	}

	public void setPrevPlayingTime(long prevPlayingTime) {
		this.prevPlayingTime = prevPlayingTime;
	}

	public long getAccountCreated() {
		return accountCreated;
	}

	public void setAccountCreated(long accountCreated) {
		this.accountCreated = accountCreated;
	}

	public boolean isTestReward() {
		return testReward;
	}

	public void setTestReward(boolean testReward) {
		this.testReward = testReward;
	}

	public boolean isDonator() {
		return donator;
	}

	public void setDonator(boolean donator) {
		this.donator = donator;
	}

	public int getDonatorPoints() {
		return donatorPoints;
	}
	
	public int getDonatorRank() {
		return donatorRank;
	}

	public void setDonatorPoints(int donatorPoints) {
		this.donatorPoints = donatorPoints;
	}
	
	public void setDonatorRank(int donatorRank) {
		this.donatorRank = donatorRank;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getNpcKillcount() {
		return npcKillcount;
	}

	public void setNpcKillcount(int npcKillcount) {
		this.npcKillcount = npcKillcount;
	}

	public int getPlayerKillcount() {
		return playerKillcount;
	}

	public void setPlayerKillcount(int playerKillcount) {
		this.playerKillcount = playerKillcount;
	}

	public int getNumberOfDeaths() {
		return numberOfDeaths;
	}

	public void setNumberOfDeaths(int numberOfDeaths) {
		this.numberOfDeaths = numberOfDeaths;
	}

	public int getEasyCluesDone() {
		return easyCluesDone;
	}

	public void setEasyCluesDone(int easyCluesDone) {
		this.easyCluesDone = easyCluesDone;
	}

	public int getMediumCluesDone() {
		return mediumCluesDone;
	}

	public void setMediumCluesDone(int mediumCluesDone) {
		this.mediumCluesDone = mediumCluesDone;
	}

	public int getHardCluesDone() {
		return hardCluesDone;
	}

	public void setHardCluesDone(int hardCluesDone) {
		this.hardCluesDone = hardCluesDone;
	}

	public int getSoldItemsWorth() {
		return soldItemsWorth;
	}

	public void setSoldItemsWorth(int soldItemsWorth) {
		this.soldItemsWorth = soldItemsWorth;
	}

	public int getBoughtItemsWorth() {
		return boughtItemsWorth;
	}

	public void setBoughtItemsWorth(int boughtItemsWorth) {
		this.boughtItemsWorth = boughtItemsWorth;
	}

	public int getDuelWins() {
		return duelWins;
	}

	public void setDuelWins(int duelWins) {
		this.duelWins = duelWins;
	}

	public int getDuelLosses() {
		return duelLosses;
	}

	public void setDuelLosses(int duelLosses) {
		this.duelLosses = duelLosses;
	}

	public int getTotalDonationAmount() {
		return totalDonationAmount;
	}

	public void setTotalDonationAmount(int totalDonationAmount) {
		this.totalDonationAmount = totalDonationAmount;
	}
	
	public void addDonatorPoints(int amount){
		this.donatorPoints += amount;
		addTotalDonationAmount(amount);
	}
	
	public void addTotalDonationAmount(int amount){
		this.totalDonationAmount += amount;
		/*if(this.totalDonationAmount >= 200)
			this.setDonator(true);*/
	}

	public boolean isAutoRetaliate() {
		return autoRetaliate;
	}

	public void setAutoRetaliate(boolean autoRetaliate) {
		this.autoRetaliate = autoRetaliate;
	}

	public int getFightMode() {
		return fightMode;
	}

	public void setFightMode(int fightMode) {
		this.fightMode = fightMode;
	}

	public int getScreenBrightness() {
		return screenBrightness;
	}

	public void setScreenBrightness(int screenBrightness) {
		this.screenBrightness = screenBrightness;
	}

	public int getMouseButtons() {
		return mouseButtons;
	}

	public void setMouseButtons(int mouseButtons) {
		this.mouseButtons = mouseButtons;
	}

	public int getChatEffects() {
		return chatEffects;
	}

	public void setChatEffects(int chatEffects) {
		this.chatEffects = chatEffects;
	}

	public int getSplitPrivateChat() {
		return splitPrivateChat;
	}

	public void setSplitPrivateChat(int splitPrivateChat) {
		this.splitPrivateChat = splitPrivateChat;
	}

	public int getAcceptAid() {
		return acceptAid;
	}

	public void setAcceptAid(int acceptAid) {
		this.acceptAid = acceptAid;
	}

	public int getMusicVolume() {
		return musicVolume;
	}

	public void setMusicVolume(int musicVolume) {
		this.musicVolume = musicVolume;
	}

	public int getEffectVolume() {
		return effectVolume;
	}

	public void setEffectVolume(int effectVolume) {
		this.effectVolume = effectVolume;
	}

	public int getSpecialAmount() {
		return specialAmount;
	}

	public void setSpecialAmount(int specialAmount) {
		this.specialAmount = specialAmount;
	}

	public boolean isChangingBankPin() {
		return changingBankPin;
	}

	public void setChangingBankPin(boolean changingBankPin) {
		this.changingBankPin = changingBankPin;
	}

	public boolean isDeletingBankPin() {
		return deletingBankPin;
	}

	public void setDeletingBankPin(boolean deletingBankPin) {
		this.deletingBankPin = deletingBankPin;
	}

	public int getPinAppendYear() {
		return pinAppendYear;
	}

	public void setPinAppendYear(int pinAppendYear) {
		this.pinAppendYear = pinAppendYear;
	}

	public int getPinAppendDay() {
		return pinAppendDay;
	}

	public void setPinAppendDay(int pinAppendDay) {
		this.pinAppendDay = pinAppendDay;
	}

	public int getBindingNeckCharge() {
		return bindingNeckCharge;
	}

	public void setBindingNeckCharge(int bindingNeckCharge) {
		this.bindingNeckCharge = bindingNeckCharge;
	}

	public int getRingOfForgingLife() {
		return ringOfForgingLife;
	}

	public void setRingOfForgingLife(int ringOfForgingLife) {
		this.ringOfForgingLife = ringOfForgingLife;
	}

	public int getRingOfRecoilLife() {
		return ringOfRecoilLife;
	}

	public void setRingOfRecoilLife(int ringOfRecoilLife) {
		this.ringOfRecoilLife = ringOfRecoilLife;
	}

	public int getSkullTimer() {
		return skullTimer;
	}

	public void setSkullTimer(int skullTimer) {
		this.skullTimer = skullTimer;
	}

	public double getEnergy() {
		return energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}

	public boolean isRunToggled() {
		return runToggled;
	}

	public void setRunToggled(boolean runToggled) {
		this.runToggled = runToggled;
	}

	public int getRunecraftNpc() {
		return runecraftNpc;
	}

	public void setRunecraftNpc(int runecraftNpc) {
		this.runecraftNpc = runecraftNpc;
	}

	public long getMuteExpire() {
		return muteExpire;
	}

	public void setMuteExpire(long muteExpire) {
		this.muteExpire = muteExpire;
	}

	public long getBanExpire() {
		return banExpire;
	}

	public void setBanExpire(long banExpire) {
		this.banExpire = banExpire;
	}

	public int getKillCount() {
		return killCount;
	}

	public void setKillCount(int killCount) {
		this.killCount = killCount;
	}

	public int getRandomGrave() {
		return randomGrave;
	}

	public void setRandomGrave(int randomGrave) {
		this.randomGrave = randomGrave;
	}

	public int getPoisonWaitDuration() {
		return poisonWaitDuration;
	}

	public void setPoisonWaitDuration(int poisonWaitDuration) {
		this.poisonWaitDuration = poisonWaitDuration;
	}

	public int getFireWaitDuration() {
		return fireWaitDuration;
	}

	public void setFireWaitDuration(int fireWaitDuration) {
		this.fireWaitDuration = fireWaitDuration;
	}

	public int getTeleBlockWaitDuration() {
		return teleBlockWaitDuration;
	}

	public void setTeleBlockWaitDuration(int teleBlockWaitDuration) {
		this.teleBlockWaitDuration = teleBlockWaitDuration;
	}

	public double getPoisonDamage() {
		return poisonDamage;
	}

	public void setPoisonDamage(double poisonDamage) {
		this.poisonDamage = poisonDamage;
	}

	public int getSlayerMaster() {
		return slayerMaster;
	}

	public void setSlayerMaster(int slayerMaster) {
		this.slayerMaster = slayerMaster;
	}

	public String getSlayerTask() {
		return slayerTask;
	}

	public void setSlayerTask(String slayerTask) {
		this.slayerTask = slayerTask;
	}

	public int getTaskAmount() {
		return taskAmount;
	}

	public void setTaskAmount(int taskAmount) {
		this.taskAmount = taskAmount;
	}

	public boolean isMagicBookType() {
		return magicBookType;
	}

	public void setMagicBookType(boolean magicBookType) {
		this.magicBookType = magicBookType;
	}

	public boolean isBrimhavenDungeonOpen() {
		return brimhavenDungeonOpen;
	}

	public void setBrimhavenDungeonOpen(boolean brimhavenDungeonOpen) {
		this.brimhavenDungeonOpen = brimhavenDungeonOpen;
	}

	public boolean isKilledClueAttacker() {
		return killedClueAttacker;
	}

	public void setKilledClueAttacker(boolean killedClueAttacker) {
		this.killedClueAttacker = killedClueAttacker;
	}

	public int getGang() {
		return gang;
	}

	public void setGang(int gang) {
		this.gang = gang;
	}

	public int getBananaCrate() {
		return bananaCrate;
	}

	public void setBananaCrate(int bananaCrate) {
		this.bananaCrate = bananaCrate;
	}

	public boolean isTalkedWithObservatoryProf() {
		return talkedWithObservatoryProf;
	}

	public void setTalkedWithObservatoryProf(boolean talkedWithObservatoryProf) {
		this.talkedWithObservatoryProf = talkedWithObservatoryProf;
	}

	public int getCoalTruckAmount() {
		return coalTruckAmount;
	}

	public void setCoalTruckAmount(int coalTruckAmount) {
		this.coalTruckAmount = coalTruckAmount;
	}

	public int getClueAmount() {
		return clueAmount;
	}

	public void setClueAmount(int clueAmount) {
		this.clueAmount = clueAmount;
	}

	public boolean isPuzzleDone() {
		return puzzleDone;
	}

	public void setPuzzleDone(boolean puzzleDone) {
		this.puzzleDone = puzzleDone;
	}

	public int[] getSkillLvl() {
		return skillLvl;
	}

	public void setSkillLvl(int[] skillLvl) {
		this.skillLvl = skillLvl;
	}

	public long[] getSkillExp() {
		return skillExp;
	}

	public void setSkillExp(long[] skillExp) {
		this.skillExp = skillExp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getSkillLvl(int i) {
		return skillLvl[i];
	}

	public void setSkillLvl(int i, int skillLvl) {
		this.skillLvl[i] = skillLvl;
	}

	public void setSkillExp(int i, int skillExp) {
		this.skillExp[i] = skillExp;
	}
	
	public long getSkillExp(int i) {
		if(i < 21)
			return skillExp[i];
		else{
			long val = 0;
			for(int j = 0; j < 21; j++){
				val += skillExp[j];
			}
			return val;
		}
	}
	
	public int getTotalLvl() {
		int val = 0;
		for(int j = 0; j < 21; j++){
			val += getLevelForXP(getSkillExp(j));
		}
		return val;
	}
	
	private static final int[] EXPERIENCE_BY_LEVEL;
	
	static {
        EXPERIENCE_BY_LEVEL = new int[100];
        int points = 0, output = 0;
        for (int lvl = 1; lvl <= 99; lvl++) {
            points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            output = (int) Math.floor(points / 4);
            EXPERIENCE_BY_LEVEL[lvl] = output;
        }
    }
	
	public int getLevelForXP(double exp) {
		for (int lvl = 1; lvl <= 99; lvl++) {
			if (EXPERIENCE_BY_LEVEL[lvl] > exp) {
				return lvl;
			}
		}
		return 99;
	}
	
}
