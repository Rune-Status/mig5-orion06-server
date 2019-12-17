package com.rs2.util.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SQLEngine {

	/** The lookup player query. */
	public static final String LOOKUP_PLAYER = "SELECT uid FROM `prs06_users` WHERE username = ?";
    public static final String UPDATE_HIGH_SCORES = "REPLACE INTO `prs06_hiscores` (`uid`, `totallevel`, `totalxp`, `xp_0`, `xp_1`, `xp_2`, `xp_3`, `xp_4`, `xp_5`, `xp_6`, `xp_7`, `xp_8`, `xp_9`, `xp_10`, `xp_11`, `xp_12`, `xp_13`, `xp_14`, `xp_15`, `xp_16`, `xp_17`, `xp_18`, `xp_19`, `xp_20`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    public static final String UPDATE_PLAYER_WORLD = "UPDATE `prs06_users` SET `world`=? WHERE `uid` = ?";
    public static final String UPDATE_WORLD_STATUS = "UPDATE `prs06_gameworlds` SET `online`=? WHERE `worldid` = ?";
    
    /** The queries to load the player */
    public static final String LOAD_PLAYER = "SELECT x, y, z, is_male, is_auto_retaliate, fight_mode, brightness, " +
                                             "mouse_buttons, chat_effects, split_private_chat, accept_aid, music_volume, " +
                                             "effect_volume, quest_points, special, energy, is_running, skull_timer, pin, " +
                                             "appearance_0, appearance_1, appearance_2, appearance_3, appearance_4, appearance_5, " +
                                             "appearance_6, color_0, color_1, color_2, color_3, color_4, tutorial_stage, tutorial_progress, " +
                                             "ban_expires, mute_expires, changing_bankpin, deleting_bankpin, pin_append_year, pin_append_date, " +
                                             "binding_neck_charge, ring_of_forging_life, ring_of_recoil_life, slayer_master, slayer_task, " +
                                             "task_amount, using_ancients, brimhaven_open, killed_clue_attacker " +
                                             "FROM `prs06_players` WHERE id = ?";
    public static final String LOAD_CONTAINER = "SELECT item_id, amount, timer, slot FROM prs06_containers WHERE user_id = ? AND container_id = ?";
    public static final String LOAD_CONTACTS = "SELECT `contact`, `ignore`, `slot` FROM prs06_contacts WHERE `player_id` = ?";
    public static final String LOAD_SKILLS = "SELECT  `id`,  `player_id`,  `cur_attack`,  `cur_defence`,  `cur_strength`,  `cur_hitpoints`,  `cur_ranged`,  `cur_prayer`,  `cur_magic`,  `cur_cooking`,  `cur_woodcutting`,  `cur_fletching`,  `cur_fishing`,  `cur_firemaking`,  `cur_crafting`,  `cur_smithing`,  `cur_mining`,  `cur_herblore`,  `cur_agility`,  `cur_thieving`,  `cur_slayer`,  `cur_farming`,  `cur_runecrafting`,  `exp_attack`,  `exp_defence`,  `exp_strength`,  `exp_hitpoints`,  `exp_ranged`,  `exp_prayer`,  `exp_magic`,  `exp_cooking`,  `exp_woodcutting`,  `exp_fletching`,  `exp_fishing`,  `exp_firemaking`,  `exp_crafting`,  `exp_smithing`,  `exp_mining`,  `exp_herblore`,  `exp_agility`,  `exp_thieving`,  `exp_slayer`,  `exp_farming`,  `exp_runecrafting` FROM `prs06_main`.`prs06_skills` WHERE player_id = ? LIMIT 1";

    /** The instance. */
	private static SQLEngine gameDatabase;

	/** The SQL thread pool. */
	private ExecutorService threadPool;

	/** The thread local. */
	private ThreadLocal<SQLWorker.ThreadLocalFields> threadLocal;

	/** The database URL. */
	private final String url;

	/** The database username. */
	private final String username;

	/** The database password. */
	private final String password;

	public SQLEngine(final int connections, final String driver, final String url, final String username, final String password) throws ClassNotFoundException {
		Class.forName(driver);
		threadPool = Executors.newFixedThreadPool(connections);
		threadLocal = new ThreadLocal<SQLWorker.ThreadLocalFields>();
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public void execute(SQLWorker worker, SQLCompletionHandler completionHandler) {
		worker.setLocalFields(threadLocal);
		worker.setCompletionHandler(completionHandler);
        worker.setSQLEngine(this);
		threadPool.execute(worker);
	}

	protected Connection openConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}

    public static SQLEngine getGameDatabase() {
        return gameDatabase;
    }

    public static void setGameDatabase(SQLEngine gameDatabase) {
        SQLEngine.gameDatabase = gameDatabase;
    }


}
