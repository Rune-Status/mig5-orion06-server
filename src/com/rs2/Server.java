package com.rs2;

import com.rs2.cache.Cache;
import com.rs2.launcher.Cpanel;
import com.rs2.cache.interfaces.RSInterface;
import com.rs2.cache.object.GameObjectData;
import com.rs2.cache.object.ObjectLoader;
import com.rs2.model.World;
import com.rs2.model.content.GeOffer;
import com.rs2.model.content.MessageOfTheWeek;
import com.rs2.model.content.combat.CombatManager;
import com.rs2.model.content.consumables.PotionLoader;
import com.rs2.model.content.minigames.GroupMiniGame;
import com.rs2.model.content.minigames.groupminigames.CastleWarsCounter;
import com.rs2.model.content.minigames.magetrainingarena.AlchemistPlayground;
import com.rs2.model.content.minigames.magetrainingarena.CreatureGraveyard;
import com.rs2.model.content.minigames.magetrainingarena.EnchantingChamber;
import com.rs2.model.content.minigames.magetrainingarena.TelekineticTheatre;
import com.rs2.model.content.questing.QuestDefinition;
import com.rs2.model.content.questing.SpecialEventDefinition;
import com.rs2.model.content.skills.fishing.FishingSpots;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcDefinition;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.npcs.drop.NPCDropTable;
import com.rs2.model.players.GlobalGroundItem;
import com.rs2.model.players.HighscoresManager;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.Player.LoginStages;
import com.rs2.model.players.ShopManager;
import com.rs2.model.players.item.Item;
import com.rs2.model.players.item.ItemDefinition;
import com.rs2.model.players.item.ItemManager;
import com.rs2.model.region.MultiCombatArea;
import com.rs2.model.region.music.Music;
import com.rs2.model.region.music.MusicArea;
import com.rs2.model.region.music.RegionMusic;
import com.rs2.model.tick.Tick;
import com.rs2.net.DedicatedReactor;
import com.rs2.net.packet.PacketManager;
import com.rs2.util.*;
import com.rs2.util.clip.Rangable;
import com.rs2.util.clip.Region;
import com.rs2.util.plugin.PluginManager;
import com.rs2.util.sql.SQLEngine;
import com.rs2.util.sql.SQLWorker;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The main core of RuneSource.
 *
 * @author blakeman8192
 */
public class Server implements Runnable {

    /**
     * The singleton instance of the server.
     */
	private static Server singleton;

    /**
     * The host address to bind the servers socket to.
     */
	private final String host;

    /**
     * The port to bind the servers socket to.
     */
	private final int port;

	private final int cycleRate;

	private static long minutesCounter;

	public static GroupMiniGame castleWarsGroup = new GroupMiniGame(new CastleWarsCounter());

	
	public static ArrayList<Item> watchedItems = new ArrayList<Item>() {{
		add(new Item(962, 0));
		add(new Item(1037, 0));
		add(new Item(1038, 0));
		add(new Item(1040, 0));
		add(new Item(1042, 0));
		add(new Item(1044, 0));
		add(new Item(1046, 0));
		add(new Item(1048, 0));
		add(new Item(1050, 0));
		add(new Item(1053, 0));
		add(new Item(1055, 0));
		add(new Item(1057, 0));
		add(new Item(1419, 0));
	    add(new Item(7956, 0));
	    add(new Item(7958, 0));
	    add(new Item(7960, 0));
	    add(new Item(7962, 0));
	    add(new Item(7964, 0));
	    add(new Item(7966, 0));
	    add(new Item(7968, 0));
	    add(new Item(7970, 0));
	    add(new Item(7972, 0));
	    add(new Item(7974, 0));
	    add(new Item(7976, 0));
	    add(new Item(7978, 0));
	    add(new Item(7980, 0));
	    add(new Item(7982, 0));
	    add(new Item(7984, 0));
	    add(new Item(7986, 0));
	    add(new Item(7988, 0));
	    add(new Item(7990, 0));
	    add(new Item(7999, 0));
	    add(new Item(8000, 0));
	    add(new Item(8066, 0));
	    add(new Item(8068, 0));
	    add(new Item(8074, 0));
	}};
	
	public static ArrayList<Item> watchedItems2 = new ArrayList<Item>();
	
	private Selector selector;
	private InetSocketAddress address;
	private ServerSocketChannel serverChannel;
	private Misc.Stopwatch cycleTimer;
	private final Queue<Player> loginQueue = new ConcurrentLinkedQueue<Player>();

	private static final Queue<Player> disconnectedPlayers = new LinkedList<Player>();
	//private static final TaskScheduler scheduler = new TaskScheduler();

	/**
	 * Creates a new Server.
	 *
	 * @param host
	 *            the host
	 * @param port
	 *            the port
	 * @param cycleRate
	 *            the cycle rate
	 */
	private Server(String host, int port, int cycleRate) {
		this.host = host;
		this.port = port;
		this.cycleRate = cycleRate;
	}
	
	public static Thread runner;
	public static Thread runner2;
	
	public static int serverStatus = 0;//0 - offline, 1 - starting, 2 - online, 3 - shutting down, 4 - restarting
	public static boolean stop = false;
	
	public static int runTimeMin = 0;
	
	/**
	 * The main method.
	 *
	 * @param args  The command line arguments.
	 */
	public static void main(String[] args) {

		String host = "0.0.0.0";
		runTimeMin = 0;
		int port = Constants.PORT_NUMBER;
		if(Constants.EASY_AEON)
			port = 5555;
		int cycleRate = 600;
		boolean live = false;
        /*Constants.MYSQL_ENABLED = false;
		if(live) {
			System.out.println("Starting live server!");
			Constants.DEVELOPER_MODE = false;
			Constants.MYSQL_ENABLED = false;
			Constants.SERVER_DEBUG = false;
			Constants.HIGHSCORES_ENABLED = true;
			Constants.ADMINS_CAN_INTERACT = true;
		}*/
		//Constants.HIGHSCORES_ENABLED = true;
		//Constants.ADMINS_CAN_INTERACT = true;

		setSingleton(new Server(host, port, cycleRate));

		runner = new Thread(getSingleton());
		runner.start();
	}

	public static Queue<Player> getDisconnectedPlayers() {
		return disconnectedPlayers;
	}

	public void queueLogin(Player player) {
		loginQueue.add(player);
	}

	public boolean checkLoginQueueContains(Long user) {
		for(Iterator<Player> players = loginQueue.iterator(); players.hasNext();) {
			Player p = players.next();
			if(p.getUsernameAsLong() == user) {
				return true;
			}
		}
		return false;
	}
	
	public boolean backUpPlayers = false;
	boolean backUpTime = false;
	
	public static boolean isHween = false;
	public static boolean isXmass = false;
	public static boolean isEaster = false;
	
	public static int messageOfTheWeek = 0;
	
	@Override
	public void run() {
		try {
			runner2 = Thread.currentThread();
			runner2.setName("ServerEngine");
			System.setOut(new Misc.TimestampLogger(System.out));
			System.setErr(new Misc.TimestampLogger(System.err, "./data/err.log"));

			address = new InetSocketAddress(host, port);
			System.out.println("Starting " + Constants.SERVER_NAME + " on " + address + "...");
			System.out.println("Days to - hween: "+Time.getDaysToHween()+" xmas: "+Time.getDaysToXmas()+" easter: "+Time.getDaysToEaster());
			if(Time.getDaysToHween() <= 7){
				isHween = true;
				System.out.println("Hween is here!");
			}
			if(Time.getDaysToXmas() <= 7){
				isXmass = true;
				System.out.println("Xmas is here!");
			}
			if(Time.getDaysToEaster() <= 7){
				isEaster = true;
				System.out.println("Easter is here!");
			}
			serverStatus = 1;
			updateCpanel();
			MessageOfTheWeek.loadMessageOfTheWeek();

			// load shutdown hook
			Thread shutdownhook = new ShutDownHook();
			Runtime.getRuntime().addShutdownHook(shutdownhook);

			PacketManager.loadPackets();

			Cache.load();
			System.out.println("cache");

			// load scripts
			Misc.loadScripts(new File("./data/ruby/"));
			System.out.println("scripts");
			
			PotionLoader.loadPotions();
			QuestDefinition.loadQuestDef();
			ShopManager.loadShops();
			NpcDefinition.loadNpcDef();
			ItemDefinition.loadItemDef();

			//interfaces
			RSInterface.load();
			System.out.println("interface");

			// Load plugins
			PluginManager.loadPlugins();
			System.out.println("plugin");

			// Load objects
			ObjectLoader objectLoader = new ObjectLoader();
			objectLoader.load();
			
			// Load regions
			Region.load();
			Rangable.load();
			System.out.println("region");

			// load combat manager
			CombatManager.init();

			// Load minute timer
			startMinutesCounter();

			// global drops
			GlobalGroundItem.initialize();

			// spawning world fishing spots
			FishingSpots.spawnFishingSpots();
			
			Music.loadSongs();
			MusicArea.loadMusicAreas();
			MultiCombatArea.loadMultiCombatAreas();
			
			//Npc.unpackDrops();
			NPCDropTable.unpackDrops();
			
			NpcLoader.loadSpawns();
			
			//load completed ge offers
			GeOffer.CompletedOffer.loadCompletedOfferData();

			HighscoresManager.load();
			
			Cache cache = Cache.getSingleton();
			cache.close();

			//init holiday events and stuff
			SpecialEventDefinition.initSpecialEvents();
			
			AlchemistPlayground.loadAlchemistPlayGround();
			EnchantingChamber.loadEnchantingChamber();
			CreatureGraveyard.loadCreatureGraveyard();
			//TelekineticTheatre.loadTelekineticTheatre();
			
			// Start up and get a'rollin!
			startup();
			serverStatus = 2;
			long startTime = System.currentTimeMillis();
			stop = false;
			for (Item items : watchedItems) {
				watchedItems2.add(items);
			}
			System.out.println("Load Playerfiles");
			PlayerSave.loadPlayerFiles();
			Misc.readSettings();
			/*for (Item items : watchedItems2) {
				if(items.getId() == 7956)
				System.out.println("p "+items.getId()+" "+items.getCount());
			}
			for (Item items : watchedItems) {
				if(items.getId() == 7956)
				System.out.println("s "+items.getId()+" "+items.getCount());
			}*/
			Misc.checkWatchedItems();
			
			System.out.println("Online!");
			/*for (Item items : watchedItems) {
				System.out.println(items.getId()+" "+items.getCount());
			}*/
			while (!Thread.interrupted() && !stop) {
				try {
					long runTime = System.currentTimeMillis()-startTime;
					runTimeMin = (int) (runTime/1000/60);
					if(runTimeMin%60 == 0 && backUpTime){
						if(!backUpPlayers){
							if(World.playerAmount() > 0)
								backUpPlayers = true;
						}
						if(backUpPlayers){
							PlayerSave.saveAllPlayers();
							PlayerSave.createBackup();
							System.out.println("Creating hourly backups of character files.");
						}else
							System.out.println("No backup files created, reason: No players have been online");
						backUpPlayers = false;
						backUpTime = false;
					}
					if(runTimeMin%60 != 0){
						backUpTime = true;
					}
					updateCpanel();
					cycle();
					sleep();
				} catch (Exception ex) {
					PlayerSave.saveAllPlayers();
					ex.printStackTrace();
				}
			}
			PlayerSave.saveAllPlayers();
			serverStatus = 0;
			updateCpanel();
			
			
			Server.singleton = null;
			runner.stop();
			runner = null;
			runner2 = null;
			runner2.stop();
			/*scheduler.schedule(new Task() {
				@Override
				protected void execute() {
					if (Thread.interrupted()) {
						PlayerSave.saveAllPlayers();
						stop();
						return;
					}
					try {
						cycle();
					} catch (Exception ex) {
						PlayerSave.saveAllPlayers();
						ex.printStackTrace();
						stop();
					}
				}
			});*/
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		PluginManager.close();
	}
	
	public static void updateAllPlayers(){
		Cpanel.loadAllPlayers();
	}

	static void updateCpanel(){
		cPAmount = World.playerAmount();
		cAAmount = World.adminAmount();
		cMAmount = World.modAmount();
		Cpanel.refreshCpanel();
	}
	
	public static int cPAmount = 0;
	public static int cAAmount = 0;
	public static int cMAmount = 0;
	
	public static void sendCPanelMessage(String s) {
		for (Player player : World.getPlayers()) {
			if (player == null || player.getIndex() == -1)
				continue;
			player.getActionSender().sendMessage("[SERVER]: "+s);
		}
	}
	
	/**
	 * Starts the server up.
	 *
	 * @throws java.io.IOException
	 */
	private void startup() throws Exception {
		// Initialize the SQL system.
		SQLEngine.setGameDatabase(new SQLEngine(8, Constants.DB_DRIVER, Constants.GAME_DB_URL, Constants.GAME_DB_USER, Constants.GAME_DB_PASS));

		// Initialize the networking objects.
		selector = Selector.open();
		serverChannel = ServerSocketChannel.open();
		DedicatedReactor.setInstance(new DedicatedReactor(Selector.open()));
		DedicatedReactor.getInstance().start();

		// ... and configure them!
		serverChannel.configureBlocking(false);
		serverChannel.socket().bind(address);

		synchronized (DedicatedReactor.getInstance()) {
			DedicatedReactor.getInstance().getSelector().wakeup();
			serverChannel.register(DedicatedReactor.getInstance().getSelector(), SelectionKey.OP_ACCEPT);
		}
		/*SQLEngine.getGameDatabase().execute(new SQLWorker(SQLEngine.UPDATE_WORLD_STATUS) {
			@Override
			public ResultSet executeSQL(Connection c, PreparedStatement st)
					throws SQLException {
				st.setInt(1, 1);
				st.setInt(2, Constants.WORLD_ID);
				st.executeUpdate();
				return null;
			}
		}, null);   */
		// Finally, initialize whatever else we need.
		cycleTimer = new Misc.Stopwatch();
	}

	/**
	 * Accepts any incoming connections.
	 *
	 * @throws java.io.IOException
	 */
	public static void accept(SelectionKey key) throws IOException {
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();

		// Accept the socket channel.
		SocketChannel channel = serverChannel.accept();
		if (channel == null) {
			return;
		}

		// Make sure we can allow this connection.
		if (!HostGateway.enter(channel.socket().getInetAddress().getHostAddress())) {
			channel.close();
			return;
		}

		// Set up the new connection.
		channel.configureBlocking(false);
		SelectionKey newKey = channel.register(key.selector(), SelectionKey.OP_READ);
		Player player = new Player(newKey);
		newKey.attach(player);
	}

	/**
	 * Performs a server cycle.
	 */
	private void cycle() {

        /* Benchmark and finish the login of the queued players to login */
		Benchmark benchmark = Benchmarks.getBenchmark("loginQueue");
		benchmark.start();
        for(int i = 0; i < 100; i++) {

            /* Check if the login queue is empty */
            if(loginQueue.isEmpty()) {
                break;
            }

            Player player = loginQueue.poll();
            try {
                player.finishLogin();
                player.setLoginStage(LoginStages.LOGGED_IN);
            } catch (Exception ex) {
                ex.printStackTrace();
                player.disconnect();
            }
        }
		benchmark.stop();

        /* Benchmark and handle each of the network packets */
		benchmark = Benchmarks.getBenchmark("handleNetworkPackets");
		benchmark.start();
		try {
			selector.selectNow();
			for (SelectionKey selectionKey : selector.selectedKeys()) {
				if (selectionKey.isValid()) {
					if (selectionKey.isReadable()) {
						// Tell the client to handle the packet.
						PacketManager.handleIncomingData((Player) selectionKey.attachment());
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		benchmark.stop();

		// Next, perform game processing.
		try {
			PluginManager.tick();
			World.process();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		benchmark = Benchmarks.getBenchmark("disconnectingPlayers");
		benchmark.start();
		synchronized (disconnectedPlayers) {
			for (Iterator<Player> players = disconnectedPlayers.iterator(); players.hasNext();) {
				Player player = players.next();
				if (player.logoutDisabled())
					continue;
				player.logout();
				players.remove();
			}
		}
        benchmark.stop();
	}

	/**
	 * Sleeps for the cycle delay.
	 *
	 * @throws InterruptedException
	 */
	private void sleep() {
		try {
			long sleepTime = cycleRate - cycleTimer.elapsed();
			boolean sleep = sleepTime > 0 && sleepTime < 600;
			for (int i = 0; i < PacketManager.SIZE; i++) {
				Benchmark b = PacketManager.packetBenchmarks[i];
				if (!sleep && b.getTime() > 0)
					System.out.println("Packet "+i+"["+PacketManager.packets[i].getClass().getSimpleName()+"] took "+b.getTime()+" ms.");
				b.reset();
			}
			if (sleep) {
				Benchmarks.resetAll();
				//System.out.println("[ENGINE]: Sleeping for " + sleepTime + "ms");
				Thread.sleep(sleepTime);
			} else {
				// The server has reached maximum load, players may now lag.
				long cycle = 100 + ((Math.abs(sleepTime)-cycleRate)/6);
				/*if (cycle > 999) {
					initiateRestart();
				}*/
				System.out.println("[WARNING]: Server load: " + cycle + "%!");
				Benchmarks.printAll();
				Benchmarks.resetAll();
				System.out.println("");
			}
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			cycleTimer.reset();
		}
	}

	@SuppressWarnings("unused")
	public static void initiateRestart() {
		int time = 300;//time in secs (300 = 5 mins)
		if(World.playerAmount() == 0)
			time = 1;
		for (Player player : World.getPlayers()) {
			if (player == null || player.getIndex() == -1)
				continue;
			player.getActionSender().sendUpdateServer(time/30*50+1);
		}
		new ShutdownWorldProcess(time+3).start();
	}

	/**
	 * Starts the minute counter
	 */
	private void startMinutesCounter() {
		try {
			BufferedReader minuteFile = new BufferedReader(new FileReader("./data/minutes.log"));
			Server.minutesCounter = Integer.parseInt(minuteFile.readLine());
		} catch (Exception e) {
			e.printStackTrace();
		}

		//World.submit(new Tick(25) {//old one, this is actually just 15secs
		World.submit(new Tick(100) {
			@Override public void execute() {
				setMinutesCounter(getMinutesCounter() + 1);
				//ObjectHandler.getInstance().updateBananaTrees();
				for (Player player : World.getPlayers()) {
					if (player == null) {
						continue;
					}
					player.getCompost().doCalculations();
					player.getAllotment().doCalculations();
					player.getFlowers().doCalculations();
					player.getHerbs().doCalculations();
					player.getHops().doCalculations();
					player.getBushes().doCalculations();
					player.getTrees().doCalculations();
					player.getFruitTrees().doCalculations();
					player.getSpecialPlantOne().doCalculations();
					player.getSpecialPlantTwo().doCalculations(); //lowering all player items timer
					ItemManager.getInstance().lowerAllItemsTimers(player);
				}
			}
		});

	}

	public static void setMinutesCounter(long minutesCounter) {
		Server.minutesCounter = minutesCounter;
		try {
			BufferedWriter minuteCounter = new BufferedWriter(new FileWriter("./data/minutes.log"));
			minuteCounter.write(Long.toString(getMinutesCounter()));
			minuteCounter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static long getMinutesCounter() {
		return minutesCounter;
	}

	/**
	 * Sets the server singleton object.
	 * 
	 * @param singleton
	 *            the singleton
	 */
	public static void setSingleton(Server singleton) {
		if (Server.singleton != null) {
			throw new IllegalStateException("Singleton already set!");
		}
		Server.singleton = singleton;
	}

	/**
	 * Gets the server singleton object.
	 * 
	 * @return the singleton
	 */
	public static Server getSingleton() {
		return singleton;
	}

	/**
	 * Gets the selector.
	 * 
	 * @return The selector
	 */
	public Selector getSelector() {
		return selector;
	}

    /**
     * Gets the port.
     *
     * @return  The port
     */
	public int getPort() {
		return port;
	}
}
