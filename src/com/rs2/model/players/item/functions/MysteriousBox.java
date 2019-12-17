package com.rs2.model.players.item.functions;

import com.rs2.model.content.treasuretrails.ClueScroll;
import com.rs2.model.players.Player;
import com.rs2.model.players.item.Item;
import com.rs2.util.Misc;

public class MysteriousBox {

//	public static final Item[] COMMON = {new Item(1359, 1), new Item(3471, 11), new Item(2447, 500), new Item(1163, 1), new Item(1127, 1), new Item(1093, 1), new Item(1079, 1), new Item(1201, 1), new Item(1333, 1), new Item(989, 1), new Item(564, 500), new Item(454, 1000), new Item(1780, 1000)};
//	public static final Item[] UNCOMMON = {new Item(392, 1000), new Item(4708, 1), new Item(4710, 1), new Item(4712, 1), new Item(4714, 1), new Item(4716, 1), new Item(4718, 1), new Item(4720, 1), new Item(4722, 1), new Item(4724, 1), new Item(4726, 1), new Item(4728, 1), new Item(4730, 1), new Item(4732, 1), new Item(4734, 1), new Item(4736, 1), new Item(4738, 1), new Item(4740, 1000), new Item(4745, 1), new Item(4747, 1), new Item(4749, 1), new Item(4751, 1), new Item(4753, 1), new Item(4755, 1), new Item(4757, 1), new Item(4759, 1), new Item(2368, 1), new Item(4151, 1), new Item(892, 1000), new Item(537, 1000), new Item(2435, 500), new Item(7937, 5000), new Item(386, 1000), new Item(563, 1000), new Item(561, 1000), new Item(565, 1000)};
//	public static final Item[] RARE = {new Item(1037, 1), new Item(1050, 1), new Item(1053, 1), new Item(1055, 1), new Item(1057, 1), new Item(962, 1), new Item(1419, 1)};

	
	public static final Item[] BARROWS = {new Item(4708, 1), new Item(4710, 1), new Item(4712, 1), new Item(4714, 1), new Item(4716, 1), new Item(4718, 1), new Item(4720, 1), new Item(4722, 1), new Item(4724, 1), new Item(4726, 1), new Item(4728, 1), new Item(4730, 1), new Item(4732, 1), new Item(4734, 1), new Item(4736, 1), new Item(4738, 1), new Item(4740, 1000), new Item(4745, 1), new Item(4747, 1), new Item(4749, 1), new Item(4751, 1), new Item(4753, 1), new Item(4755, 1), new Item(4757, 1), new Item(4759, 1), new Item(4753, 1), new Item(4755, 1), new Item(4757, 1), new Item(4759, 1) };

	public static final Item[] COMMON = {new Item(607, 1), new Item(607, 1), new Item(607, 1), new Item(607, 1), new Item(607, 1), new Item(607, 1), new Item(607, 1), new Item(1359, 1), new Item(1163, 1), new Item(1127, 1), new Item(1093, 1), new Item(1079, 1), new Item(1201, 1), new Item(1333, 1), new Item(989, 1), new Item(454, 1000), new Item(1780, 1000), new Item(1754, 200), new Item(1712, 1), new Item(4131, 1), new Item(4153, 1), new Item(1275, 1), new Item(1514, 500), new Item(3755, 1), new Item(3749, 1), new Item(3751, 1), new Item(3753, 1), new Item(607, 1), new Item(607, 1), new Item(607, 1), new Item(607, 1), new Item(607, 1), new Item(607, 1), new Item(1754, 200), new Item(448, 300), new Item(890, 400),new Item(888, 600)};

	public static final Item[] UNCOMMON = {new Item(607, 1), new Item(607, 1), new Item(607, 1), new Item(607, 1), new Item(607, 1), new Item(2368, 1), new Item(892, 250), new Item(537, 250), new Item(2435, 25), new Item(7937, 2500), new Item(386, 250), new Item(563, 100), new Item(561, 2500), new Item(565, 500), new Item(1305, 1),  new Item(4587, 1), new Item(1750, 150),new Item(450, 200), new Item(892, 250), new Item(6568, 1)};

	public static final Item[] RARE = {new Item(607, 1), new Item(607, 1), new Item(607, 1), new Item(4566, 1), new Item(607, 1), new Item(6733, 1), new Item(6737, 1), new Item(6735, 1), new Item(4151, 1), new Item(7158, 1), new Item(2581, 1), new Item(2577, 1), new Item(6735, 1), new Item(6889, 1), new Item(6735, 1), new Item(6914, 1), new Item(6916, 1), new Item(6918, 1), new Item(6920, 1), new Item(6922, 1), new Item(6924, 1),new Item(6527, 1), new Item(6524, 1),new Item(1748, 100),new Item(452, 100)};

	public static final Item[] ELITE = {new Item(7927, 1), new Item(5607, 1), new Item(6583, 1), new Item(1038, 1), new Item(1040, 1), new Item(1042, 1), new Item(1044, 1), new Item(1046, 1), new Item(1048, 1), new Item(1053, 1), new Item(1055, 1), new Item(1057, 1), new Item(1419, 1)};
		
			public static void openMysteriousBox(Player player) {
		Item reward = null;
		if(Misc.random_(75) == 0){
			reward = ELITE[Misc.randomMinusOne(ELITE.length)];
		} else if(Misc.random_(35)== 0){
			reward = BARROWS[Misc.randomMinusOne(BARROWS.length)];
		}
		else if (Misc.random_(20) == 0) {
			reward = RARE[Misc.randomMinusOne(RARE.length)];
		} else if (Misc.random_(10) == 0) {
			reward = UNCOMMON[Misc.randomMinusOne(UNCOMMON.length)];
		} else {
			if (Misc.random_(3) == 0 && !player.hasClueScroll()) {
				reward = new Item(ClueScroll.getRandomClue((Misc.random_(3)+1)), 1);
			} else {
				reward = COMMON[Misc.randomMinusOne(COMMON.length)];
			}
		}
		player.getInventory().addItem(reward);
		player.getActionSender().sendMessage("You open the box and found: "+reward.getCount()+" x "+reward.getDefinition().getName()+".");
	}
}
