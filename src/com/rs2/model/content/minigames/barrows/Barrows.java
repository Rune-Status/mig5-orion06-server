package com.rs2.model.content.minigames.barrows;

import java.util.ArrayList;

import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.dialogue.Dialogues;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcLoader;
import com.rs2.model.objects.functions.Ladders;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.model.tick.Tick;
import com.rs2.util.Area;
import com.rs2.util.Misc;

public class Barrows {

	public static final int[][] barrowsBrothers = {{6823, 2030}, {6772, 2029}, {6822, 2028}, {6773, 2027}, {6771, 2026}, {6821, 2025}};
													//verac		torag			karil			guthan		dharok			ahrim
	
	public static final Position[] gravePosition = { new Position(3578,9706,3), //verac
														new Position(3568,9683,3), //torag
														new Position(3546,9684,3), //karil
														new Position(3534,9704,3), //guthan
														new Position(3556,9718,3), //dharok
														new Position(3557,9703,3), //ahrim
	};
	
	static int[][] barrowsItems = {
			{4753, 4755, 4757, 4759}, // verac
			{4745, 4747, 4749, 4751}, // torag
			{4732, 4734, 4736, 4738}, // karil
			{4724, 4726, 4728, 4730}, // guthan
			{4716, 4718, 4720, 4722}, // dharok
			{4708, 4710, 4712, 4714}, // ahrim
	};
	
	/*public static final int[][] veracItems = {4753, 4755, 4757, 4759};
	public static final int[] toracItems = {4745, 4747, 4749, 4751};
	public static final int[] karilItems = {4732, 4734, 4736, 4738};
	public static final int[] guthanItems = {4724, 4726, 4728, 4730};
	public static final int[] dharokItems = {4716, 4718, 4720, 4722};
	public static final int[] ahrimItems = {4708, 4710, 4712, 4714};*/
	
	//public static final int[] Items = {4708, 4710, 4712, 4714, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759, 1149, 165, 117, 141, 129, 385};
	public static final int[] Items = {1149, 165, 117, 141, 129, 385};
	public static final int[][] Stackables = {{4740, 5}, {558, 25}, {562, 11}, {560, 5}, {565, 2}, {995, 55}};
	
	public static final int[] startingRooms = {1,3,7,9};
	public static final Position[] startingPosition = { new Position(3535,9710), //room1
														new Position(3568,9710), //room3
														new Position(3535,9676), //room7
														new Position(3568,9676), //room9
													};
	
	public static final int[] puzzleSeq = {4545,4546,4547};
	public static final int[] puzzleOpt = {4550,4551,4552};
	
	public static final int[] headItems = {4761,4762,4763,4764,4765,4766,4767,4768,4769,4770,4771,4772};
	public static final int[] headPos = {4537,4538,4539,4540,4541,4542};
	
	public static final int[] barrowsNpc = {2031,2032,2033,2034,2035,2036,2037};
	
	public static final int MAZE_CONFIG = 452;
	public static final int CHEST_CONFIG = 453;

	public static boolean barrowsObject2(Player player, int objectId, int objectX, int objectY) {
		//room 1
		if(objectId == 6709 && objectX == 3534 && objectY == 9712){
			int val = (int) Math.pow(2, 6);
			if((player.configValue[MAZE_CONFIG] & val) == 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			Ladders.climbLadder(player, gravePosition[player.getRandomGrave()]);
			return true;
		}
		if((objectId == 6717 && objectX == 3528 && objectY == 9711) || (objectId == 6736 && objectX == 3528 && objectY == 9712)){
			int val = (int) Math.pow(2, 11);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int x = player.getPosition().getX() < 3529 ? 1 : -1;
			player.getActionSender().walkTo(x, 0, true);
			player.getActionSender().walkThroughDoubleDoor(6736, 6717, 3528, 9712, 3528, 9711, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX()+x,player.getPosition().getY()));
			return true;
		}
		if((objectId == 6719 && objectX == 3541 && objectY == 9712) || (objectId == 6738 && objectX == 3541 && objectY == 9711)){
			int val = (int) Math.pow(2, 13);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int x = player.getPosition().getX() < 3541 ? 1 : -1;
			player.getActionSender().walkTo(x, 0, true);
			player.getActionSender().walkThroughDoubleDoor(6738, 6719, 3541, 9711, 3541, 9712, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX()+x,player.getPosition().getY()));
			return true;
		}
		if((objectId == 6716 && objectX == 3534 && objectY == 9718) || (objectId == 6735 && objectX == 3535 && objectY == 9718)){
			int val = (int) Math.pow(2, 10);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int y = player.getPosition().getY() < 9718 ? 1 : -1;
			player.getActionSender().walkTo(0, y, true);
			player.getActionSender().walkThroughDoubleDoor(6735, 6716, 3535, 9718, 3534, 9718, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX(),player.getPosition().getY()+y));
			return true;
		}
		if((objectId == 6718 && objectX == 3535 && objectY == 9705) || (objectId == 6737 && objectX == 3534 && objectY == 9705)){
			int val = (int) Math.pow(2, 12);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int y = player.getPosition().getY() < 9706 ? 1 : -1;
			player.getActionSender().walkTo(0, y, true);
			player.getActionSender().walkThroughDoubleDoor(6737, 6718, 3534, 9705, 3535, 9705, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX(),player.getPosition().getY()+y));
			return true;
		}
		//room2
		if((objectId == 6719 && objectX == 3545 && objectY == 9711) || (objectId == 6738 && objectX == 3545 && objectY == 9712)){
			int val = (int) Math.pow(2, 13);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int x = player.getPosition().getX() < 3546 ? 1 : -1;
			player.getActionSender().walkTo(x, 0, true);
			player.getActionSender().walkThroughDoubleDoor(6738, 6719, 3545, 9712, 3545, 9711, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX()+x,player.getPosition().getY()));
			return true;
		}
		if((objectId == 6721 && objectX == 3558 && objectY == 9712) || (objectId == 6740 && objectX == 3558 && objectY == 9711)){
			int val = (int) Math.pow(2, 15);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int x = player.getPosition().getX() < 3558 ? 1 : -1;
			player.getActionSender().walkTo(x, 0, true);
			player.getActionSender().walkThroughDoubleDoor(6740, 6721, 3558, 9711, 3558, 9712, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX()+x,player.getPosition().getY()));
			return true;
		}
		//center door N
		if((objectId == 6720 && objectX == 3552 && objectY == 9705) || (objectId == 6739 && objectX == 3551 && objectY == 9705)){
			int val = (int) Math.pow(2, 14);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			if(player.barrowsPuzzleDone){
				int y = player.getPosition().getY() < 9706 ? 1 : -1;
				player.getActionSender().walkTo(0, y, true);
				player.getActionSender().walkThroughDoubleDoor(6739, 6720, 3551, 9705, 3552, 9705, 0);
				spawnDoorNpc(player, new Position(player.getPosition().getX(),player.getPosition().getY()+y));
			}else
				openPuzzle(player);
			return true;
		}
		//room3
		if(objectId == 6710 && objectX == 3568 && objectY == 9712){
			int val = (int) Math.pow(2, 7);
			if((player.configValue[MAZE_CONFIG] & val) == 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			Ladders.climbLadder(player, gravePosition[player.getRandomGrave()]);
			return true;
		}
		if((objectId == 6721 && objectX == 3562 && objectY == 9711) || (objectId == 6740 && objectX == 3562 && objectY == 9712)){
			int val = (int) Math.pow(2, 15);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int x = player.getPosition().getX() < 3563 ? 1 : -1;
			player.getActionSender().walkTo(x, 0, true);
			player.getActionSender().walkThroughDoubleDoor(6740, 6721, 3562, 9712, 3562, 9711, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX()+x,player.getPosition().getY()));
			return true;
		}
		if((objectId == 6723 && objectX == 3575 && objectY == 9712) || (objectId == 6742 && objectX == 3575 && objectY == 9711)){
			int val = (int) Math.pow(2, 17);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int x = player.getPosition().getX() < 3575 ? 1 : -1;
			player.getActionSender().walkTo(x, 0, true);
			player.getActionSender().walkThroughDoubleDoor(6742, 6723, 3575, 9711, 3575, 9712, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX()+x,player.getPosition().getY()));
			return true;
		}
		if((objectId == 6716 && objectX == 3568 && objectY == 9718) || (objectId == 6735 && objectX == 3569 && objectY == 9718)){
			int val = (int) Math.pow(2, 10);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int y = player.getPosition().getY() < 9718 ? 1 : -1;
			player.getActionSender().walkTo(0, y, true);
			player.getActionSender().walkThroughDoubleDoor(6735, 6716, 3569, 9718, 3568, 9718, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX(),player.getPosition().getY()+y));
			return true;
		}
		if((objectId == 6722 && objectX == 3569 && objectY == 9705) || (objectId == 6741 && objectX == 3568 && objectY == 9705)){
			int val = (int) Math.pow(2, 16);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int y = player.getPosition().getY() < 9706 ? 1 : -1;
			player.getActionSender().walkTo(0, y, true);
			player.getActionSender().walkThroughDoubleDoor(6741, 6722, 3568, 9705, 3569, 9705, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX(),player.getPosition().getY()+y));
			return true;
		}
		//room4
		if((objectId == 6718 && objectX == 3534 && objectY == 9701) || (objectId == 6737 && objectX == 3535 && objectY == 9701)){
			int val = (int) Math.pow(2, 12);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int y = player.getPosition().getY() < 9701 ? 1 : -1;
			player.getActionSender().walkTo(0, y, true);
			player.getActionSender().walkThroughDoubleDoor(6737, 6718, 3535, 9701, 3534, 9701, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX(),player.getPosition().getY()+y));
			return true;
		}
		if((objectId == 6726 && objectX == 3535 && objectY == 9688) || (objectId == 6745 && objectX == 3534 && objectY == 9688)){
			int val = (int) Math.pow(2, 20);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int y = player.getPosition().getY() < 9689 ? 1 : -1;
			player.getActionSender().walkTo(0, y, true);
			player.getActionSender().walkThroughDoubleDoor(6745, 6726, 3534, 9688, 3535, 9688, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX(),player.getPosition().getY()+y));
			return true;
		}
		//center door W
		if((objectId == 6724 && objectX == 3541 && objectY == 9695) || (objectId == 6743 && objectX == 3541 && objectY == 9694)){
			int val = (int) Math.pow(2, 18);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			if(player.barrowsPuzzleDone){
				int x = player.getPosition().getX() < 3541 ? 1 : -1;
				player.getActionSender().walkTo(x, 0, true);
				player.getActionSender().walkThroughDoubleDoor(6743, 6724, 3541, 9694, 3541, 9695, 0);
				spawnDoorNpc(player, new Position(player.getPosition().getX()+x,player.getPosition().getY()));
			}else
				openPuzzle(player);
			return true;
		}
		//room5 (center)
		//center door W
		if((objectId == 6724 && objectX == 3545 && objectY == 9694) || (objectId == 6743 && objectX == 3545 && objectY == 9695)){
			int val = (int) Math.pow(2, 18);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int x = player.getPosition().getX() < 3546 ? 1 : -1;
			player.getActionSender().walkTo(x, 0, true);
			player.getActionSender().walkThroughDoubleDoor(6743, 6724, 3545, 9695, 3545, 9694, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX()+x,player.getPosition().getY()));
			return true;
		}
		//center door N
		if((objectId == 6720 && objectX == 3551 && objectY == 9701) || (objectId == 6739 && objectX == 3552 && objectY == 9701)){
			int val = (int) Math.pow(2, 14);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int y = player.getPosition().getY() < 9701 ? 1 : -1;
			player.getActionSender().walkTo(0, y, true);
			player.getActionSender().walkThroughDoubleDoor(6739, 6720, 3552, 9701, 3551, 9701, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX(),player.getPosition().getY()+y));
			return true;
		}
		//center door E
		if((objectId == 6725 && objectX == 3558 && objectY == 9695) || (objectId == 6744 && objectX == 3558 && objectY == 9694)){
			int val = (int) Math.pow(2, 19);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int x = player.getPosition().getX() < 3558 ? 1 : -1;
			player.getActionSender().walkTo(x, 0, true);
			player.getActionSender().walkThroughDoubleDoor(6744, 6725, 3558, 9694, 3558, 9695, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX()+x,player.getPosition().getY()));
			return true;
		}
		//center door S
		if((objectId == 6727 && objectX == 3552 && objectY == 9688) || (objectId == 6746 && objectX == 3551 && objectY == 9688)){
			int val = (int) Math.pow(2, 21);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int y = player.getPosition().getY() < 9689 ? 1 : -1;
			player.getActionSender().walkTo(0, y, true);
			player.getActionSender().walkThroughDoubleDoor(6746, 6727, 3551, 9688, 3552, 9688, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX(),player.getPosition().getY()+y));
			return true;
		}
		//room6
		//center door E
		if((objectId == 6725 && objectX == 3562 && objectY == 9694) || (objectId == 6744 && objectX == 3562 && objectY == 9695)){
			int val = (int) Math.pow(2, 19);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			if(player.barrowsPuzzleDone){
				int x = player.getPosition().getX() < 3563 ? 1 : -1;
				player.getActionSender().walkTo(x, 0, true);
				player.getActionSender().walkThroughDoubleDoor(6744, 6725, 3562, 9695, 3562, 9694, 0);
				spawnDoorNpc(player, new Position(player.getPosition().getX()+x,player.getPosition().getY()));
			}else
				openPuzzle(player);
			return true;
		}
		if((objectId == 6722 && objectX == 3568 && objectY == 9701) || (objectId == 6741 && objectX == 3569 && objectY == 9701)){
			int val = (int) Math.pow(2, 16);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int y = player.getPosition().getY() < 9701 ? 1 : -1;
			player.getActionSender().walkTo(0, y, true);
			player.getActionSender().walkThroughDoubleDoor(6741, 6722, 3569, 9701, 3568, 9701, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX(),player.getPosition().getY()+y));
			return true;
		}
		if((objectId == 6728 && objectX == 3569 && objectY == 9688) || (objectId == 6747 && objectX == 3568 && objectY == 9688)){
			int val = (int) Math.pow(2, 22);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int y = player.getPosition().getY() < 9689 ? 1 : -1;
			player.getActionSender().walkTo(0, y, true);
			player.getActionSender().walkThroughDoubleDoor(6747, 6728, 3568, 9688, 3569, 9688, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX(),player.getPosition().getY()+y));
			return true;
		}
		//room7
		if(objectId == 6711 && objectX == 3534 && objectY == 9678){
			int val = (int) Math.pow(2, 8);
			if((player.configValue[MAZE_CONFIG] & val) == 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			Ladders.climbLadder(player, gravePosition[player.getRandomGrave()]);
			return true;
		}
		if((objectId == 6717 && objectX == 3528 && objectY == 9677) || (objectId == 6736 && objectX == 3528 && objectY == 9678)){
			int val = (int) Math.pow(2, 11);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int x = player.getPosition().getX() < 3529 ? 1 : -1;
			player.getActionSender().walkTo(x, 0, true);
			player.getActionSender().walkThroughDoubleDoor(6736, 6717, 3528, 9678, 3528, 9677, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX()+x,player.getPosition().getY()));
			return true;
		}
		if((objectId == 6729 && objectX == 3541 && objectY == 9678) || (objectId == 6748 && objectX == 3541 && objectY == 9677)){
			int val = (int) Math.pow(2, 23);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int x = player.getPosition().getX() < 3541 ? 1 : -1;
			player.getActionSender().walkTo(x, 0, true);
			player.getActionSender().walkThroughDoubleDoor(6748, 6729, 3541, 9677, 3541, 9678, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX()+x,player.getPosition().getY()));
			return true;
		}
		if((objectId == 6726 && objectX == 3534 && objectY == 9684) || (objectId == 6745 && objectX == 3535 && objectY == 9684)){
			int val = (int) Math.pow(2, 20);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int y = player.getPosition().getY() < 9684 ? 1 : -1;
			player.getActionSender().walkTo(0, y, true);
			player.getActionSender().walkThroughDoubleDoor(6745, 6726, 3535, 9684, 3534, 9684, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX(),player.getPosition().getY()+y));
			return true;
		}
		if((objectId == 6731 && objectX == 3535 && objectY == 9671) || (objectId == 6750 && objectX == 3534 && objectY == 9671)){
			int val = (int) Math.pow(2, 25);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int y = player.getPosition().getY() < 9672 ? 1 : -1;
			player.getActionSender().walkTo(0, y, true);
			player.getActionSender().walkThroughDoubleDoor(6750, 6731, 3534, 9671, 3535, 9671, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX(),player.getPosition().getY()+y));
			return true;
		}
		//room8
		if((objectId == 6729 && objectX == 3545 && objectY == 9677) || (objectId == 6748 && objectX == 3545 && objectY == 9678)){
			int val = (int) Math.pow(2, 23);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int x = player.getPosition().getX() < 3546 ? 1 : -1;
			player.getActionSender().walkTo(x, 0, true);
			player.getActionSender().walkThroughDoubleDoor(6748, 6729, 3545, 9678, 3545, 9677, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX()+x,player.getPosition().getY()));
			return true;
		}
		if((objectId == 6730 && objectX == 3558 && objectY == 9678) || (objectId == 6749 && objectX == 3558 && objectY == 9677)){
			int val = (int) Math.pow(2, 24);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int x = player.getPosition().getX() < 3558 ? 1 : -1;
			player.getActionSender().walkTo(x, 0, true);
			player.getActionSender().walkThroughDoubleDoor(6749, 6730, 3558, 9677, 3558, 9678, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX()+x,player.getPosition().getY()));
			return true;
		}
		//center door S
		if((objectId == 6727 && objectX == 3551 && objectY == 9684) || (objectId == 6746 && objectX == 3552 && objectY == 9684)){
			int val = (int) Math.pow(2, 21);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			if(player.barrowsPuzzleDone){
				int y = player.getPosition().getY() < 9684 ? 1 : -1;
				player.getActionSender().walkTo(0, y, true);
				player.getActionSender().walkThroughDoubleDoor(6746, 6727, 3552, 9684, 3551, 9684, 0);
				spawnDoorNpc(player, new Position(player.getPosition().getX(),player.getPosition().getY()+y));
			}else
				openPuzzle(player);
			return true;
		}
		//room9
		if(objectId == 6712 && objectX == 3568 && objectY == 9678){
			int val = (int) Math.pow(2, 9);
			if((player.configValue[MAZE_CONFIG] & val) == 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			Ladders.climbLadder(player, gravePosition[player.getRandomGrave()]);
			return true;
		}
		if((objectId == 6730 && objectX == 3562 && objectY == 9677) || (objectId == 6749 && objectX == 3562 && objectY == 9678)){
			int val = (int) Math.pow(2, 24);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int x = player.getPosition().getX() < 3563 ? 1 : -1;
			player.getActionSender().walkTo(x, 0, true);
			player.getActionSender().walkThroughDoubleDoor(6749, 6730, 3562, 9678, 3562, 9677, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX()+x,player.getPosition().getY()));
			return true;
		}
		if((objectId == 6723 && objectX == 3575 && objectY == 9678) || (objectId == 6742 && objectX == 3575 && objectY == 9677)){
			int val = (int) Math.pow(2, 17);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int x = player.getPosition().getX() < 3575 ? 1 : -1;
			player.getActionSender().walkTo(x, 0, true);
			player.getActionSender().walkThroughDoubleDoor(6742, 6723, 3575, 9677, 3575, 9678, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX()+x,player.getPosition().getY()));
			return true;
		}
		if((objectId == 6728 && objectX == 3568 && objectY == 9684) || (objectId == 6747 && objectX == 3569 && objectY == 9684)){
			int val = (int) Math.pow(2, 22);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int y = player.getPosition().getY() < 9684 ? 1 : -1;
			player.getActionSender().walkTo(0, y, true);
			player.getActionSender().walkThroughDoubleDoor(6747, 6728, 3569, 9684, 3568, 9684, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX(),player.getPosition().getY()+y));
			return true;
		}
		if((objectId == 6731 && objectX == 3569 && objectY == 9671) || (objectId == 6750 && objectX == 3568 && objectY == 9671)){
			int val = (int) Math.pow(2, 25);
			if((player.configValue[MAZE_CONFIG] & val) != 0 || player.configValue[MAZE_CONFIG] == 0)
				return false;
			int y = player.getPosition().getY() < 9672 ? 1 : -1;
			player.getActionSender().walkTo(0, y, true);
			player.getActionSender().walkThroughDoubleDoor(6750, 6731, 3568, 9671, 3569, 9671, 0);
			spawnDoorNpc(player, new Position(player.getPosition().getX(),player.getPosition().getY()+y));
			return true;
		}
		return false;
	}
	
	static void spawnDoorNpc(Player player, Position pos){
		if(Misc.random_(8) == 0){
			if (!player.getBarrowsNpcDead()[player.getRandomGrave()] && !NpcLoader.checkSpawn(player, barrowsBrothers[player.getRandomGrave()][1])) {
				NpcLoader.spawnNpcPos(player, pos, new Npc(barrowsBrothers[player.getRandomGrave()][1]), true, true);
			}
		}else{
			final int npc = barrowsNpc[Misc.random_(barrowsNpc.length)];
			NpcLoader.spawnNpcPos(player, pos, new Npc(npc), true, false);
		}
	}
	
	public static boolean barrowsObject(Player player, int objectId) {
		switch (objectId) {
			case 10284 : // chest
				if(player.earthquake)
					return false;
				int val = (int) Math.pow(2, 16);
				if((player.configValue[CHEST_CONFIG] & val) == 0 || player.configValue[CHEST_CONFIG] == 0){
					player.getActionSender().sendConfig(CHEST_CONFIG, val);
					player.configValue[CHEST_CONFIG] = val;
					if (!player.getBarrowsNpcDead()[player.getRandomGrave()])
						NpcLoader.spawnNpc(player, new Npc(barrowsBrothers[player.getRandomGrave()][1]), true, true);
					return true;
				} else if ((player.configValue[CHEST_CONFIG] & val) != 0){
					if (player.getKillCount() < 1) {
						player.getActionSender().sendMessage("You search the chest but don't find anything.");
						return true;
					}
					getReward(player);
					return true;
				}
			case 6823 :
			case 6772 :
			case 6821 :
			case 6771 :
			case 6773 :
			case 6822 :
				for (int x = 0; x < barrowsBrothers.length; x++) {
					if (objectId == barrowsBrothers[x][0]) {
						if (x == player.getRandomGrave()) {
							Dialogues.startDialogue(player, 10001);
							return true;
						}
						if (NpcLoader.checkSpawn(player, barrowsBrothers[x][1])) {
							player.getActionSender().sendMessage("You must kill the the brother before searching this.");
							return true;
						}
						if (player.getBarrowsNpcDead()[x]) {
							player.getActionSender().sendMessage("You have already searched this sarcophagus.");
							return true;
						}
						NpcLoader.spawnNpc(player, new Npc(barrowsBrothers[x][1]), true, true);
						if (x != player.getRandomGrave()) {
							player.getActionSender().sendMessage("You don't find anything.");
						}
						return true;
					}
				}
				return true;
			case 6707 : // verac stairs
				player.teleport(new Position(3556, 3298, 0));
				return true;
			case 6706 : // torag stairs
				player.teleport(new Position(3553, 3283, 0));
				break;
			case 6705 : // karil stairs
				player.teleport(new Position(3565, 3276, 0));
				return true;
			case 6704 : // guthan stairs
				player.teleport(new Position(3578, 3284, 0));
				return true;
			case 6703 : // dharok stairs
				player.teleport(new Position(3574, 3298, 0));
				return true;
			case 6702 : // ahrim stairs
				player.teleport(new Position(3565, 3290, 0));
				return true;
		}
		return false;
	}

	public static boolean digCrypt(Player player) {
		if (player.Area(3553, 3561, 3294, 3301)) {
			player.teleport(new Position(3578, 9706, 3));
			player.getActionSender().sendMessage("You've broken into a crypt!");
			return true;
		} else if (player.Area(3550, 3557, 3278, 3287)) {
			player.teleport(new Position(3568, 9683, 3));
			player.getActionSender().sendMessage("You've broken into a crypt!");
			return true;
		} else if (player.Area(3561, 3568, 3285, 3292)) {
			player.teleport(new Position(3557, 9703, 3));
			player.getActionSender().sendMessage("You've broken into a crypt!");
			return true;
		} else if (player.Area(3570, 3579, 3293, 3302)) {
			player.teleport(new Position(3556, 9718, 3));
			player.getActionSender().sendMessage("You've broken into a crypt!");
			return true;
		} else if (player.Area(3571, 3582, 3278, 3285)) {
			player.teleport(new Position(3534, 9704, 3));
			player.getActionSender().sendMessage("You've broken into a crypt!");
			return true;
		} else if (player.Area(3562, 3569, 3273, 3279)) {
			player.teleport(new Position(3546, 9684, 3));
			player.getActionSender().sendMessage("You've broken into a crypt!");
			return true;
		}
		return false;
	}

	public static void getReward(Player player) {
		ArrayList<Integer> rewards = new ArrayList<Integer>();
		for (int x = 0; x < barrowsBrothers.length; x++) {
			if (player.getBarrowsNpcDead()[x]) {
				for (int i = 0; i < barrowsItems[x].length; i++) {
					rewards.add(barrowsItems[x][i]);
					//System.out.println("Barrows reward added: "+barrowsItems[x][i]);
				}
			}
		}
		for (int i = 0; i < Items.length; i++) {
			rewards.add(Items[i]);
		}
		final int number = Misc.randomMinusOne(Stackables.length);
		final int rune = Stackables[number][0];
		final int amount = Stackables[number][1];
		//final int reward = Items[Misc.randomMinusOne(Items.length)];
		final int reward = rewards.get(Misc.random_(rewards.size()));
		int brothersKilled = brotherKillCount(player);
		if (brothersKilled  < 1) {
			player.getActionSender().sendMessage("You can only loot the chest after killing at least 1 brother.");
			return;
		}
		int kills = player.getKillCount();
		//System.out.println("[Barrows] brothers: "+brothersKilled+", total: "+kills);
		if(kills >= 14)
			kills = 14;
		double mod = 1.0;
		if(player.ringOfWealthEffect()){
			mod = 0.667;
		}
		//int chance = (int) ((648 - (kills * 10))*mod);//old formula, new one below!
		if(kills <= 0){
			System.out.println("Something went wrong @barrows reward: kills 0!");
			return;
		}
		double killsDouble = kills;
		double d1 = killsDouble/200;
		double d2 = 1/d1;
		int chance = (int) (d2*mod);
		//System.out.println("[Barrows] kills: "+kills+", chance: "+chance);
		boolean getBarrows = Misc.random(chance) == 0;
		if (getBarrows) {
			if (player.getInventory().getItemContainer().freeSlots() == 1) {
				if (!player.getInventory().playerHasItem(rune)) {
					player.getActionSender().sendMessage("You must have three empty spaces in order to take this loot.");
					return;
				}
			} else if (player.getInventory().getItemContainer().freeSlots() < 1) {
				player.getActionSender().sendMessage("You must have three empty spaces in order to take this loot.");
				return;
			}
			player.getInventory().addItemOrDrop(new Item(rune, Misc.random(amount * kills) + 1));
			player.getInventory().addItemOrDrop(new Item(reward, 1));
		} else {
			final int number2 = Misc.randomMinusOne(Stackables.length);
			final int rune2 = Stackables[number2][0];
			final int amount2 = Stackables[number2][1];
			if (player.getInventory().getItemContainer().freeSlots() < 1) {
				if (!player.getInventory().playerHasItem(rune) || !player.getInventory().playerHasItem(rune2)) {
					player.getActionSender().sendMessage("You must have three empty spaces in order to take this loot.");
					return;
				}
			}
			if (player.getInventory().getItemContainer().freeSlots() == 1) {
				if (!player.getInventory().playerHasItem(rune) && !player.getInventory().playerHasItem(rune2)) {
					player.getActionSender().sendMessage("You must have three empty spaces in order to take this loot.");
					return;
				}
			}
			player.getInventory().addItemOrDrop(new Item(rune, Misc.random(amount * kills) + 1));
			player.getInventory().addItemOrDrop(new Item(rune2, Misc.random(amount2 * kills) + 1));
		}
		player.startEarthquake();
		player.save();
		/*player.getUpdateFlags().sendAnimation(714);
		player.getUpdateFlags().sendHighGraphic(301);
		player.getTeleportation().teleport(3565, 3298, 0, true);
		player.getActionSender().sendMessage("You grab the loot and teleport away.");
		resetBarrows(player);*/
		return;
	}

	private static int brotherKillCount(Player player) {
		int brotherKillCount = 0;
		for (boolean kill : player.getBarrowsNpcDead()) {
			if (kill) {
				brotherKillCount++;
			}
		}
		return brotherKillCount;
	}
	
	/*public static void showHead(final Player player){
		if(Misc.random_(70) == 0 && player.tempAreaEffectInt == 0){
			player.tempAreaEffectInt = 4535;
			final int head = headItems[Misc.random_(headItems.length)];
			final int pos = headPos[Misc.random_(headPos.length)];
			player.getActionSender().sendItemOnInterface(pos, 100, head);
			//player.getActionSender().sendDialogueAnimation(4883, 591);
			final Tick timer = new Tick(3) {
				@Override
	            public void execute() {
					player.getActionSender().sendItemOnInterface(pos, 200, -1);
					player.tempAreaEffectInt = 0;
					stop();
				}
			};
			World.getTickManager().submit(timer);
		}
	}*/
	
	public static boolean showHead(final Player player){
		if(player.tempAreaEffectInt == 4535)
			return true;
		if(player.inBarrows()){
			final Tick timer1 = new Tick(6) {
	            @Override
	            public void execute() {
	            	if(!player.isLoggedIn()){
	            		stop();
	            		return;
	            	}
	            	if(player.inBarrows()){
	            		if(Misc.random_(70) == 0){
	            			final int head = headItems[Misc.random_(headItems.length)];
	            			final int pos = headPos[Misc.random_(headPos.length)];
	        				player.getActionSender().sendItemOnInterface(pos, 100, head);
	        				player.getActionSender().sendDialogueAnimation(pos, 2085);
	        				final Tick timer2 = new Tick(3) {
	        					@Override
	        					public void execute() {
	        						player.getActionSender().sendItemOnInterface(pos, 200, -1);
	        						stop();
	        					}
	        				};
	        				World.getTickManager().submit(timer2);
	            		}
	            	}else{
	            		player.tempAreaEffectInt = -1;
	            		stop();
	            	}
	            }
			};
			player.tempAreaEffectInt = 4535;
	        World.getTickManager().submit(timer1);
			return true;
		}
		return false;
	}
	
	public static boolean solvePuzzle(Player player, int buttonId){
		if(player.barrowsPuzzle < 0)
			return false;
		Puzzle puzzle = puzzles[player.barrowsPuzzle];
		switch(buttonId){
			case 4550:
				if(player.barrowsPuzzlePieces[0] == puzzle.getCorrectAnswer()){
					player.barrowsPuzzleDone = true;
					player.getActionSender().sendMessage("You hear the doors' locking mechanism grind open.");
				}else{
					Barrows.shuffleMaze(player, false);
					player.getActionSender().sendMessage("You got the puzzle wrong! You can hear the catacombs moving around you.");
				}
				player.getActionSender().removeInterfaces();
				return true;
			case 4551:
				if(player.barrowsPuzzlePieces[1] == puzzle.getCorrectAnswer()){
					player.barrowsPuzzleDone = true;
					player.getActionSender().sendMessage("You hear the doors' locking mechanism grind open.");
				}else{
					Barrows.shuffleMaze(player, false);
					player.getActionSender().sendMessage("You got the puzzle wrong! You can hear the catacombs moving around you.");
				}
				player.getActionSender().removeInterfaces();
				return true;
			case 4552:
				if(player.barrowsPuzzlePieces[2] == puzzle.getCorrectAnswer()){
					player.barrowsPuzzleDone = true;
					player.getActionSender().sendMessage("You hear the doors' locking mechanism grind open.");
				}else{
					Barrows.shuffleMaze(player, false);
					player.getActionSender().sendMessage("You got the puzzle wrong! You can hear the catacombs moving around you.");
				}
				player.getActionSender().removeInterfaces();
				return true;
		}
		return false;
	}
	
	static void openPuzzle(Player player){
		int puzzle_index = Misc.random_(puzzles.length);
		player.barrowsPuzzle = puzzle_index;
		Puzzle puzzle = puzzles[puzzle_index];
		int temp[] = puzzle.getOptions().clone();
		player.barrowsPuzzlePieces = Misc.ShuffleArray(temp);
		for(int i = 0; i < puzzleSeq.length; i++){
			player.getActionSender().sendModelOnInterface(puzzleSeq[i], puzzle.getSequence(i));
			player.getActionSender().sendModelOnInterface(puzzleOpt[i], player.barrowsPuzzlePieces[i]);
		}
		player.getActionSender().sendMessage("The door is locked with a strange puzzle.");
		player.getActionSender().sendInterface(4543);
	}
	
	public static void shuffleMaze(Player player, boolean enteredTunnel){
		int val = 0;
		int playerRoom = 0;
		int barrowsLadder = 0;
		player.barrowsPuzzleDone = false;
		ArrayList<Room> unlockedRooms = new ArrayList<Room>();
		for (int i = 10; i <= 25; i++) {
			val += (int) Math.pow(2, i);
		}
		int ladder = Misc.random_(startingRooms.length);
		val += (int) Math.pow(2, (6+ladder));
		barrowsLadder = startingRooms[ladder];
		if(enteredTunnel){
			player.teleport(startingPosition[ladder]);
		}
		for (int i = 0; i < rooms.length; i++) {
			Room room = rooms[i];
			if(room.getArea().containsBorderIncluded(player.getPosition())){
				playerRoom = room.getId();
				break;
			}
		}
		unlockedRooms.add(getRoom(5));
		boolean routeToLadder = false;
		boolean routeToPlayer = false;
		while(!routeToLadder || !routeToPlayer){
			Room previousRoom = null;
			if(unlockedRooms.size()-2 >= 0)
				previousRoom = unlockedRooms.get(unlockedRooms.size()-2);
			Room room = unlockedRooms.get(unlockedRooms.size()-1);
			ArrayList<Integer> possibleRooms = new ArrayList<Integer>();
			for(int i = 0; i < room.getNextRooms().length; i++){
				if(previousRoom != null){
					if(i != previousRoom.getId())
						possibleRooms.add(i);
				}else
					possibleRooms.add(i);
			}
			int index = Misc.random_(possibleRooms.size());
			int id = room.getNextRoom(index);
			//int id = room.getNextRoom(possibleRooms.get(index));
			boolean c = false;
			for(Room rooms : unlockedRooms){
				if(rooms.getId() == id)
					c = true;
			}
			if(!c)
				val -= (int) Math.pow(2, (room.getDoor(index)));
			unlockedRooms.add(getRoom(id));
			if(id == barrowsLadder)
				routeToLadder = true;
			if(id == playerRoom)
				routeToPlayer = true;
		}
		player.getActionSender().sendConfig(MAZE_CONFIG, val);
		player.configValue[MAZE_CONFIG] = val;
	}

	public static void resetBarrows(Player player) {
		for (int x = 0; x < Barrows.barrowsBrothers.length; x++) {
			player.setBarrowsNpcDead(x, false);
		}
		player.setKillCount(0);
		player.setRandomGrave(Misc.random(5));
		player.getActionSender().sendConfig(CHEST_CONFIG, 0);
		player.configValue[CHEST_CONFIG] = 0;
	}

	public static void handleDeath(Player player, Npc npc) {
		for (int x = 0; x < barrowsBrothers.length; x++) {
			if (npc.getNpcId() == barrowsBrothers[x][1]) {
				player.setKillCount(player.getKillCount() + 1);
				player.getActionSender().sendString("Kill count: " + player.getKillCount(), 4536);
				player.setBarrowsNpcDead(x, true);
				break;
			}
		}
		for (int x = 0; x < barrowsNpc.length; x++) {
			if (npc.getNpcId() == barrowsNpc[x]) {
				player.setKillCount(player.getKillCount() + 1);
				player.getActionSender().sendString("Kill count: " + player.getKillCount(), 4536);
				break;
			}
		}
	}
	
	public static Room getRoom(int i){
		return rooms[i-1];
	}
	
	public static Room rooms[] = {
		new Room(1, new int[]{2,3,4,7}, new int[]{13,10,12,11}, new Area(3529, 9706, 3540, 9717, (byte)0)),
		new Room(2, new int[]{1,3}, new int[]{13,15}, new Area(3546, 9706, 3557, 9717, (byte)0)),
		new Room(3, new int[]{1,2,6,9}, new int[]{10,15,16,17}, new Area(3563, 9706, 3574, 9717, (byte)0)),
		new Room(4, new int[]{1,7}, new int[]{12,20}, new Area(3529, 9689, 3540, 9700, (byte)0)),
		new Room(5, new int[]{2,4,6,8}, new int[]{14,18,19,21}, new Area(3546, 9689, 3557, 9700, (byte)0)),
		new Room(6, new int[]{3,9}, new int[]{16,22}, new Area(3563, 9689, 3574, 9700, (byte)0)),
		new Room(7, new int[]{1,4,8,9}, new int[]{11,20,23,25}, new Area(3529, 9672, 3540, 9683, (byte)0)),
		new Room(8, new int[]{7,9}, new int[]{23,24}, new Area(3546, 9672, 3557, 9683, (byte)0)),
		new Room(9, new int[]{3,6,7,8}, new int[]{17,22,25,24}, new Area(3563, 9672, 3574, 9683, (byte)0)),
	};
	
	public static Puzzle puzzles[] = {
		new Puzzle(new int[]{6716,6717,6718}, new int[]{6713,6714,6715}),
		new Puzzle(new int[]{6722,6723,6724}, new int[]{6719,6720,6721}),
		new Puzzle(new int[]{6728,6729,6730}, new int[]{6725,6726,6727}),
		new Puzzle(new int[]{6734,6735,6736}, new int[]{6731,6732,6733}),
	};
	
	public static class Puzzle{
		
		int[] sequence;
		int[] options;
		
		Puzzle(int[] sequence, int[] options){
			this.sequence = sequence;
			this.options = options;
		}

		public int[] getSequence() {
			return sequence;
		}

		public int[] getOptions() {
			return options;
		}
		
		public int getSequence(int i) {
			return sequence[i];
		}
		
		public int getCorrectAnswer(){
			return options[0];
		}
		
	}
	
	public static class Room{
		
		int id;
		int[] nextRooms;
		int[] doors;
		Area area;
		
		Room(int id, int[] nextRooms, int[] doors, Area area){
			this.id = id;
			this.nextRooms = nextRooms;
			this.doors = doors;
			this.area = area;
		}

		public int getId() {
			return id;
		}

		public int[] getNextRooms() {
			return nextRooms;
		}

		public int getNextRoom(int i) {
			return nextRooms[i];
		}
		
		public int[] getDoors() {
			return doors;
		}
		
		public int getDoor(int i) {
			return doors[i];
		}

		public Area getArea() {
			return area;
		}
		
	}
	
}
