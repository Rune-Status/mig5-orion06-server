package com.rs2;

import com.rs2.model.content.combat.AttackType;
import com.rs2.model.content.combat.weapon.AttackStyle;

import java.math.BigInteger;

public class Constants {
	
	//
	public static boolean EASY_AEON = false;
	
	//logging
	public static boolean LOG_PUBLIC_CHAT = true;
	public static boolean LOG_PRIVATE_CHAT = true;
	public static boolean LOG_TRADES = true;
	public static boolean LOG_COMMANDS = true;
	public static boolean LOG_YELL = true;
	
	public static boolean GE_ENABLED = true;
	
	public static boolean ALLOW_TUTORIAL_SKIP = true;

	public static String SERVER_NAME = "OSRSPK";
	public static String EMULATOR = "EasyRS06";
	public static boolean SHOW_EMU = false;

	public static String DB_DRIVER = "com.mysql.jdbc.Driver";
    public static String GAME_DB_URL = "jdbc:mysql://localhost/prs06_main";
	public static String GAME_DB_USER = "root";
	public static String GAME_DB_PASS = "";
	public static final int WORLD_ID = 1;
	
	public static final int FORUM_ADMIN = 4;
	public static final int FORUM_MODERATOR = 3;
	public static final int FORUM_ACTIVATION = 5;
	public static final int FORUM_BANNED = 7;
	public static final int FORUM_DEVELOPER = 8;
	
	public static boolean MYSQL_ENABLED = false;
	public static boolean SERVER_DEBUG = false;
    public static boolean DEVELOPER_MODE = false;
    public static boolean HIGHSCORES_ENABLED = false;

	public static boolean ADMINS_CAN_INTERACT = false;
	
	public static int PORT_NUMBER = 43594;
	
	public static final int SOUTH = 0;
	public static final int NORTH = 2;
	public static final int EAST = 4;
	public static final int WEST = 5;
	
	//not implemented yet!
	public static boolean F2P_CONTENT_ONLY = false;
	public static boolean ITEM_SPAWNING = false;//allow every1 to spawn items
	public static boolean FUN_PK = false;//dont lose items after death
	public static boolean PK_WORLD = false;//pk everywhere
	
	public static int RESTRICT_LOGIN = 0;//0 - none, 1 - p2p+mods+admins, 2 - mods+admins, 3 - admins
	//

	public static final int MAGIC_ID = -1;
    public static boolean RSA_CHECK = false;
    public static boolean SYSTEM_UPDATE = false;
    
	public static int CLIENT_VERSION = 15;//controlled from control panel!
	
	public static final String TEST_VERSION = "1.0";

	public static String key1 = "126281243334509621910786157961571648139029640444686961553514423188662257084412132099345405190869140255510782101811050242941632064063371431334829733868500675557132805905863594614007916107891529024023784039950030199813062171961096886168210646453313260607895180672957089197913358703168034604511968295223858703073";
	public static String key2 = "30652639256685216982113709825788207861142309232862050206651318249008028834075838037084192223878820867367525545502695992564741792805897626221439361069016103707546910916876758973004891298232729104728327473982300624040057756427674392115989911877923828003372350341670381425233062849579939381994254110724498182865";

	public static BigInteger RSA_MODULUS = new BigInteger(key1);
	public static BigInteger RSA_EXPONENT = new BigInteger(key2);

   	public static final double COMBAT_XP = 20.0;//
   	public static final double SKILLING_XP = 15.0;
   	public static double QUEST_EXP_RATE = 15.0;
   	
	public static int START_X = 3093;
	public static int START_Y = 3104;
	public static int START_Z = 0;
	
	public static int RESPAWN_X = 3222;
	public static int RESPAWN_Y = 3218;
	public static int RESPAWN_Z = 0;
	
	public static final int MAX_NPCS = 8192;
	public static final int MAX_NPC_ID = 3886;//3852
	public static final int NPC_WALK_DISTANCE = 2;
	public static final int NPC_FOLLOW_DISTANCE = 10;
	public static final int MAX_ITEM_ID = 8119;//7956
	public static final int MAX_ITEM_COUNT = Integer.MAX_VALUE;
	public static int MAX_PLAYERS_AMOUNT = 3000;
	public static final int LOGIN_RESPONSE_OK = 2;
	public static final int LOGIN_RESPONSE_INVALID_CREDENTIALS = 3;
	public static final int LOGIN_RESPONSE_ACCOUNT_DISABLED = 4;
	public static final int LOGIN_RESPONSE_ACCOUNT_ONLINE = 5;
	public static final int LOGIN_RESPONSE_UPDATED = 6;
	public static final int LOGIN_RESPONSE_WORLD_FULL = 7;
	public static final int LOGIN_RESPONSE_LOGIN_SERVER_OFFLINE = 8;
	public static final int LOGIN_RESPONSE_LOGIN_LIMIT_EXCEEDED = 9;
	public static final int LOGIN_RESPONSE_BAD_SESSION_ID = 10;
	public static final int LOGIN_RESPONSE_PLEASE_TRY_AGAIN = 11;
	public static final int LOGIN_RESPONSE_NEED_MEMBERS = 12;
	public static final int LOGIN_RESPONSE_ACCOUNT_CREATED = 29;
	public static final int LOGIN_RESPONSE_NEED_STAFF = 30;
	public static final int LOGIN_RESPONSE_NOT_ACTIVATED = 13;
	public static final int LOGIN_RESPONSE_SERVER_BEING_UPDATED = 14;
	public static final int LOGIN_RESPONSE_LOGIN_ATTEMPTS_EXCEEDED = 16;
	public static final int LOGIN_RESPONSE_MEMBERS_ONLY_AREA = 17;
	public static final int HAT = 0;
	public static final int CAPE = 1;
	public static final int AMULET = 2;
	public static final int WEAPON = 3;
	public static final int CHEST = 4;
	public static final int SHIELD = 5;
	public static final int LEGS = 7;
	public static final int HANDS = 9;
	public static final int FEET = 10;
	public static final int RING = 12;
	public static final int ARROWS = 13;
	public static final int APPEARANCE_SLOT_CHEST = 0;
	public static final int APPEARANCE_SLOT_ARMS = 1;
	public static final int APPEARANCE_SLOT_LEGS = 2;
	public static final int APPEARANCE_SLOT_HEAD = 3;
	public static final int APPEARANCE_SLOT_HANDS = 4;
	public static final int APPEARANCE_SLOT_FEET = 5;
	public static final int APPEARANCE_SLOT_BEARD = 6;
	public static final int GENDER_MALE = 0;
	public static final int GENDER_FEMALE = 1;
	
	public static boolean DISEASING_ENABLED = true;//temporary farming fix!

	public static boolean AGILITY_ENABLED = false;
	public static boolean COOKING_ENABLED = true;
	public static boolean CRAFTING_ENABLED = true;
	public static boolean FARMING_ENABLED = true;//reguires testing!
	public static boolean FIREMAKING_ENABLED = true;
	public static boolean FISHING_ENABLED = true;
	public static boolean FLETCHING_ENABLED = true;
	public static boolean HERBLORE_ENABLED = true;
	public static boolean MINING_ENABLED = true;
	public static boolean PRAYER_ENABLED = true;
	public static boolean RUNECRAFTING_ENABLED = true;
	public static boolean SLAYER_ENABLED = true;
	public static boolean SMITHING_ENABLED = true;
	public static boolean THIEVING_ENABLED = true;
	public static boolean WOODCUTTING_ENABLED = true;

    public static boolean DUELING_DISABLED = false;

	// modern teleport coords
	public static final int VARROCK_X = 3213, VARROCK_Y = 3423;
	public static final int LUMBRIDGE_X = 3222, LUMBRIDGE_Y = 3218;
	public static final int FALADOR_X = 2964, FALADOR_Y = 3378;
	public static final int CAMELOT_X = 2757, CAMELOT_Y = 3479;
	public static final int ARDOUGNE_X = 2662, ARDOUGNE_Y = 3305;
	public static final int WATCH_TOWER_X = 2547, WATCH_TOWER_Y = 3112;
	public static final int TROLLHEIM_X = 2910, TROLLHEIM_Y = 3612;
	public static final int APE_ATOLL_X = 2754, APE_ATOLL_Y = 2784;

	// ancient teleport coords
	public static final int PADDEWWA_X = 3098, PADDEWWA_Y = 9884;
	public static final int SENNTISTEN_X = 3321, SENNTISTEN_Y = 3335;
	public static final int KHARYRLL_X = 3492, KHARYRLL_Y = 3471;
	public static final int LASSAR_X = 3001, LASSAR_Y = 3470;
	public static final int DAREEYAK_X = 2970, DAREEYAK_Y = 3697;
	public static final int CARRALLANGAR_X = 3156, CARRALLANGAR_Y = 3666;
	public static final int ANNAKARL_X = 3287, ANNAKARL_Y = 3886;
	public static final int GHORROCK_X = 2977, GHORROCK_Y = 3873;

	public static final int[] MOVEMENT_ANIMS = new int[] { 0x328, 0x333, 0x338 };

	public static AttackStyle MAGIC_STYLE = new AttackStyle(AttackType.MAGIC, AttackStyle.Mode.MAGIC, AttackStyle.Bonus.MAGIC);

	public static final String BONUS_NAME[] = { "Stab", "Slash", "Crush", "Magic", "Range", "Stab", "Slash", "Crush", "Magic", "Range", "Strength", "Prayer" };

	public static final int[] UNTRADEABLE_ITEMS = {6529};
	public static final int[] FUN_WEAPONS = { 4566, 1419, 2460, 2462, 2464, 2466, 2468, 2470, 2472, 2474, 2476, 8936, 8938 };

	public static final int EMPTY_OBJECT = 6951;

	public static final int PACKET_LENGTHS[] = { 0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
			0, 1, 0, 0, 4, 0, 6, 2, 2, 2, // 10
			0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
			0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
			2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
			0, 0, 0, 12, 0, 0, 0, 8, 8, 12, // 50
			8, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
			6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
			0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
			0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
			0, 13, 0, -1, 0, 0, 0, 0, 0, 0,// 100
			0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
			1, 0, 6, 0, 0, 0, -1, 0, 2, 6, // 120
			0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
			0, 0, 0, 0, 0, 6, 0, 0, 0, 0, // 140
			0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
			0, 0, 0, 0, -1, -1, 0, 0, 0, 0,// 160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
			0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
			0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
			2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
			4, 0, 0, 0, 7, 8, 0, 0, 10, 0, // 210
			0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
			1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
			0, 4, 0, 0, 0, 0, -1, 0, -1, 4,// 240
			0, 0, 6, 6, 0, 0, 0 // 250
	};

    public static final int[][][] COLOR_RANGES = new int[][][] { { { 0, 0, 0, 0, 0 },  { 11, 15, 15, 5, 7} }, { { 0, 0, 0, 0, 0 }, { 11, 15, 15, 5, 7} } };
    //public static final int[][][] APPEARANCE_RANGES = new int[][][] { { { 18, 26, 36, 0, 33, 42, 10 }, { 25, 31, 40, 8, 34, 43, 17 } }, { { 56, 61, 70, 45, 67, 79, -1 }, { 60, 65, 77, 54, 68, 80, -1 } } };
    public static final int[][][] APPEARANCE_RANGES = new int[][][] { { { 18, 26, 36, 0, 33, 42, 10 }, { 25, 32, 41, 9, 35, 44, 17 } }, { { 56, 61, 70, 45, 67, 79, -1 }, { 60, 66, 78, 55, 69, 81, -1 } } };
    																	//2,3,5,0,4,6,1					2,3,5,0,4,6,1						9,10,12,7,11,13,-1					9,10,12,7,11,13,-1
    public static final int SKILL_ATTACK = 0, SKILL_DEFENCE = 1,
            SKILL_STRENGTH = 2, SKILL_HP = 3, SKILL_RANGE = 4, SKILL_PRAY = 5,
            SKILL_MAGE = 6, SKILL_COOK = 7, SKILL_WC = 8, SKILL_FLETCH = 9,
            SKILL_FISH = 10, SKILL_FM = 11, SKILL_CRAFT = 12, SKILL_SMITH = 13,
            SKILL_MINE = 14, SKILL_HERB = 15, SKILL_AGILITY = 16,
            SKILL_THIEVE = 17, SKILL_SLAY = 18, SKILL_FARM = 19, SKILL_RC = 20;
}
