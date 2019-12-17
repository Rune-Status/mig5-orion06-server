package com.rs2.model.content.questing;

import com.rs2.model.content.questing.quests.*;
import com.rs2.util.FileOperations;
import com.rs2.util.Stream;

public class QuestDefinition {

	private int id;
	private String name;
	private int button;
	private boolean members;
	
	public static int QUEST_COUNT = 0;
	
	public static int QUEST_COUNT_MAX = 200;
	
	private static QuestDefinition[] definitions = new QuestDefinition[0];
	
	static Quest questlist[] = {
		new MissingQuest(-1),
		new Tutorial(0),
		new BlackKnightFortress(1),
		new CooksAssistant(2),
		new DemonSlayer(3),
		new DoricsQuest(4),
		new DragonSlayer(5),
		new ErnestTheChicken(6),
		new GoblinDiplomacy(7),
		new ImpCatcher(8),
		new TheKnightsSword(9),
		new PiratesTreasure(10),
		new PrinceAliRescue(11),
		new TheRestlessGhost(12),
		new RomeoAndJuliet(13),
		new RuneMysteriesQuest(14),
		new SheepShearer(15),
		new ShieldOfArrav(16),
		new VampireSlayer(17),
		new WitchsPotion(18),
		//p2p quests
		new DruidicRitual(29),
		new ElementalWorkshop(32),
		new FamilyCrest(35),
		new TheFremennikTrials(40),
		new GertrudesCat(42),
		new HeroesQuest(50),
		new HolyGrail(51),
		new JunglePotion(56),
		new LostCity(58),
		new MerlinsCrystal(61),
		new PriestInPeril(72),
		new ScorpionCatcher(80),
		new TreeGnomeVillage(95),
		new WitchHouse(103),
		new PiratesOfTheGielinor(105),
		new WayOfTheNecromancer(106),
	};
	
	public static int getMaxQuestPoints() {
		int qp = 0;
		for(int id = 1; id < QUEST_COUNT; id++){
			Quest quest = getQuest(id);
			qp += quest.getRewardQP();
		}
		return qp;
	}
	
	public static Quest getQuest(int id){
		for (int i2 = 0; i2 < questlist.length; i2++) {
			int questId = questlist[i2].getId();
			if(id == questId)
				return questlist[i2];
		}
		return questlist[0];
	}
	
	public static QuestDefinition forId(int id) {
		if (id < 0) {
			id = 1;
		}
		QuestDefinition def = definitions[id];
		if (def == null) {
			def = new QuestDefinition(id, "UNKNOWN", -1, false);
		}
		return def;
	}
	
	public static void loadQuestDef() {
		try {
			byte abyte2[] = FileOperations.ReadFile("./Data/content/questing/Quests.dat");
			Stream stream2 = new Stream(abyte2);
			QUEST_COUNT = stream2.readUnsignedByte();
			definitions = new QuestDefinition[QUEST_COUNT];
			for (int i2 = 0; i2 < QUEST_COUNT; i2++) {
				String name = stream2.readString();
				int button = stream2.readUnsignedWord()-1;
				boolean members = (stream2.readUnsignedByte() == 0 ? false : true);
				definitions[i2] = new QuestDefinition(i2, name, button, members);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private QuestDefinition(int id, String name, int button, boolean members){
		this.id = id;
		this.name = name;
		this.button = button;
		this.members = members;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getButton() {
		return button;
	}
	
	public boolean isMembers() {
		return members;
	}
	
}
