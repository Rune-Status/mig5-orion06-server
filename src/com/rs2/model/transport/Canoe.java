package com.rs2.model.transport;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.content.skills.Skill;
import com.rs2.model.content.skills.Tools;
import com.rs2.model.content.skills.Tools.Tool;
import com.rs2.model.objects.GameObject;
import com.rs2.model.players.ObjectHandler;
import com.rs2.model.players.Player;
import com.rs2.model.tick.CycleEvent;
import com.rs2.model.tick.CycleEventContainer;
import com.rs2.model.tick.CycleEventHandler;

public class Canoe {

	public enum CanoeTree {
		CANOE_TREE_LUMBRIDGE(new int[]{12163}, 12, 0),
		CANOE_TREE_CHAMPIONS_GUILD(new int[]{12164}, 12, 8),
		CANOE_TREE_BARBARIAN_VILLAGE(new int[]{12165}, 12, 16),
		CANOE_TREE_EDGE(new int[]{12166}, 12, 24);
		
		private int[] id;
		private int level;
		private int configShift;

		public static CanoeTree getTree(int id) {
            for(CanoeTree tree : CanoeTree.values()){
            	for (int ids : tree.getId()) {
	            	if (ids == id) {
	            		return tree;
	            	}
            	}
            }
            return null;
		}

		private CanoeTree(int[] id, int level, int configShift) {
			this.id = id;
			this.level = level;
			this.configShift = configShift;
		}

		public int[] getId() {
			return id;
		}

		public int getLevel() 	{
			return level;
		}
		
		public int getConfigShift() 	{
			return configShift;
		}
		
	}
	
	static int config = 674;
	
	static void handleDestinationMap(final Player player){
		int i = 0;
		for (Destinations destination : Destinations.values()) {
			if(i == player.canoeStation){
				player.getActionSender().setInterfaceChildVisibility(destination.getInterfaceChilds()[0], false);
				player.getActionSender().setInterfaceChildVisibility(destination.getInterfaceChilds()[1], true);
			}else{
				if(canTravelTo(player, destination, i)){
					player.getActionSender().setInterfaceChildVisibility(destination.getInterfaceChilds()[0], true);
					if(destination != Destinations.WILDERNESS)
						player.getActionSender().setInterfaceChildVisibility(destination.getInterfaceChilds()[1], false);
				}else{
					player.getActionSender().setInterfaceChildVisibility(destination.getInterfaceChilds()[0], false);
					if(destination != Destinations.WILDERNESS)
						player.getActionSender().setInterfaceChildVisibility(destination.getInterfaceChilds()[1], false);
				}
			}
			i++;
		}
	}
	
	static boolean canTravelTo(final Player player, Destinations destination, int i){
		if(destination == Destinations.WILDERNESS && !Canoes.values()[player.canoeType-1].canGoToWild())
			return false;
		if(Math.abs(player.canoeStation - i) <= Canoes.values()[player.canoeType-1].travelDistance)
			return true;
		return false;
	}
	
	public static void handle(final Player player, final int id, final int x, final int y) {
		final GameObject p = ObjectHandler.getInstance().getObject(x, y, player.getPosition().getZ());
		if (p != null && p.getDef().getId() != id) {
			return;
		}
		final CanoeTree tree = CanoeTree.getTree(id);
		if(tree == null) {
			return;
		}
		if(player.canoeType != 0){
			if(player.configValue[config] == player.canoeType << tree.getConfigShift()){
				player.configValue[config] = (player.canoeType+10) << tree.getConfigShift();
				player.getActionSender().sendConfig(config, player.configValue[config]);
				return;
			}
			if(player.configValue[config] == (player.canoeType+10) << tree.getConfigShift()){
				handleDestinationMap(player);
				player.getActionSender().sendInterface(18220);
				return;
			}
		}
		player.canoeType = 0;
		if(player.configValue[config] != 10 << tree.getConfigShift()){
			chopDown(player, p, tree);
			return;
		}
		if(player.configValue[config] == 10 << tree.getConfigShift()){
			for (Canoes canoeType : Canoes.values()) {
				if(canoeType == Canoes.LOG)
					continue;
				if(player.getSkill().getLevel()[Skill.WOODCUTTING] >= canoeType.getLevel()){
					player.getActionSender().setInterfaceChildVisibility(canoeType.getInterfaceChilds()[0], false);
					player.getActionSender().setInterfaceChildVisibility(canoeType.getInterfaceChilds()[1], true);
				}else{
					player.getActionSender().setInterfaceChildVisibility(canoeType.getInterfaceChilds()[0], true);
					player.getActionSender().setInterfaceChildVisibility(canoeType.getInterfaceChilds()[1], false);
				}
			}
			player.getActionSender().sendInterface(18178);
			player.canoeStation = tree.getConfigShift()/8;
			return;
		}
	}
	
	public static boolean shapeButtons(Player player, int button) {
		for (Canoes canoeType : Canoes.values()) {
			if (button == canoeType.buttonId) {
				handleShaping(player, canoeType);
				player.getActionSender().removeInterfaces();
				return true;
			}
		}
		return false;
	}
	
	public static boolean travelButtons(Player player, int button) {
		int i = 0;
		for (Destinations destination : Destinations.values()) {
			if (button == destination.buttonId) {
				if(canTravelTo(player, destination, i)){
					handleTravelling(player, destination);
				}else{
					player.getActionSender().sendMessage("You cannot travel there!");
				}
				return true;
			}
			i++;
		}
		return false;
	}
	
	public static void handleTravelling(final Player player, final Destinations destination) {
		player.teleport(destination.getLocation());
		player.getActionSender().sendMessage("You arrive at the "+destination.getPlaceName()+"."+(destination == Destinations.WILDERNESS ? " There are no trees suitable to make a canoe." : ""));
		player.getActionSender().sendMessage("Your canoe sinks into the water after the hard journey.");
		player.canoeStation = 0;
		player.canoeType = 0;
		player.configValue[config] = 0;
		player.getActionSender().sendConfig(config, player.configValue[config]);
		player.getActionSender().removeInterfaces();
	}
	
	public enum Canoes {
        LOG(18204, 12, 30, 1, new int[]{-1,-1}, 1, false),
        DUGOUT(18205, 27, 60, 2, new int[]{18212,18185}, 2, false),
        STABLE_DUGOUT(18206, 42, 90, 3, new int[]{18215,18182}, 3, false),
        WAKA(18207, 57, 150, 4, new int[]{18209,18193}, 4, true);

        int buttonId;
        int level;
        int xp;
        int configValue;
        int interfaceChilds[];
        int travelDistance;
        boolean canGoToWild;
        
        Canoes(int buttonId, int level, int xp, int configValue, int interfaceChilds[], int travelDistance, boolean canGoToWild) {
            this.buttonId = buttonId;
            this.configValue = configValue;
            this.level = level;
            this.xp = xp;
            this.interfaceChilds = interfaceChilds;
            this.travelDistance = travelDistance;
            this.canGoToWild = canGoToWild;
        }
        
		public int getLevel() {
			return level;
		}
		
		public int getXp() {
			return xp;
		}

		public int getConfigValue() {
			return configValue;
		}
		
		public int[] getInterfaceChilds() {
			return interfaceChilds;
		}
		
		public int getTravelDistance() {
			return travelDistance;
		}
		
		public boolean canGoToWild() {
			return canGoToWild;
		}
    }
	
	public enum Destinations {
        LUMBRDIGE("Lumbridge", new Position(3244,3237), 18255, new int[]{18234,18271}),
        CHAMPIONS_GUILD("Champions' Guild", new Position(3202,3344), 18256, new int[]{18236,18268}),
        BARBARIAN_VILLAGE("Barbarian Village", new Position(3113,3411), 18257, new int[]{18238,18265}),
        EDGEVILLE("Edgeville", new Position(3134,3510), 18262, new int[]{18261,18259}),
        WILDERNESS("Wilderness", new Position(3145,3798), 18258, new int[]{18254,-1});

        int buttonId;
        int interfaceChilds[];
        String placeName;
        Position location;
        
        Destinations(String placeName, Position location, int buttonId, int interfaceChilds[]) {
        	this.placeName = placeName;
        	this.location = location;
            this.buttonId = buttonId;
            this.interfaceChilds = interfaceChilds;
        }

        public Position getLocation() {
			return location;
		}

		public String getPlaceName() {
			return placeName;
		}

		public int[] getInterfaceChilds() {
			return interfaceChilds;
		}

    }
	
	public static void handleShaping(final Player player, final Canoes canoeType) {
		if (!Constants.WOODCUTTING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		final Tool axe = Tools.getTool(player, Skill.WOODCUTTING);
		if(axe == null) {
			player.getActionSender().sendMessage("You do not have an axe which you have the woodcutting level to use.");
			return;
		}
		if(player.getSkill().getLevel()[Skill.WOODCUTTING] < canoeType.getLevel()) {
			player.getActionSender().sendMessage("You need a Woodcutting level of " + canoeType.getLevel() + " to cut this tree.");
			return;
		}
		player.getUpdateFlags().sendAnimation(axe.getSpecialAnim(), 0);
		player.getActionSender().sendSound(471, 1, 0);
		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task)) {
					container.stop();
					return;
				}
				player.getSkill().addExp(Skill.WOODCUTTING, canoeType.getXp());
				player.configValue[config] = canoeType.getConfigValue() << (player.canoeStation*8);
				player.getActionSender().sendConfig(config, player.configValue[config]);
				player.canoeType = canoeType.getConfigValue();
				player.getActionSender().sendSound(473, 1, 0);
				player.getUpdateFlags().sendAnimation(axe.getSpecialAnim(), 0);
				container.stop();
				return;
			}
			@Override
			public void stop() {
				player.getMovementHandler().reset();
				player.getUpdateFlags().sendAnimation(-1, 0);
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 4);
	}
	
	public static void chopDown(final Player player, final GameObject p, final CanoeTree tree) {
		if (!Constants.WOODCUTTING_ENABLED) {
			player.getActionSender().sendMessage("This skill is currently disabled.");
			return;
		}
		final Tool axe = Tools.getTool(player, Skill.WOODCUTTING);
		if(axe == null) {
			player.getActionSender().sendMessage("You do not have an axe which you have the woodcutting level to use.");
			return;
		}
		if(player.getSkill().getLevel()[Skill.WOODCUTTING] < tree.getLevel()) {
			player.getActionSender().sendMessage("You need a Woodcutting level of " + tree.getLevel() + " to cut this tree.");
			return;
		}
		player.getUpdateFlags().sendAnimation(axe.getAnimation(), 0);
		player.getActionSender().sendSound(471, 1, 0);
		final int task = player.getTask();
		player.setSkilling(new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (!player.checkTask(task)) {
					container.stop();
					return;
				}
				player.configValue[config] = 10 << tree.getConfigShift();
				player.getActionSender().sendConfig(config, player.configValue[config]);
				player.getActionSender().sendSound(473, 1, 0);
				player.getActionSender().sendSound(1312, 1, 0);
				player.getUpdateFlags().sendAnimation(axe.getAnimation(), 0);
				container.stop();
				return;
			}
			@Override
			public void stop() {
				player.getMovementHandler().reset();
				player.getUpdateFlags().sendAnimation(-1, 0);
			}
		});
		CycleEventHandler.getInstance().addEvent(player, player.getSkilling(), 4);
	}

}
