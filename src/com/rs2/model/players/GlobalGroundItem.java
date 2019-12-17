package com.rs2.model.players;

import java.io.BufferedReader;
import java.io.FileReader;

import com.rs2.Constants;
import com.rs2.model.Position;
import com.rs2.model.ground.GroundItem;
import com.rs2.model.ground.GroundItemManager;
import com.rs2.model.players.item.Item;
import com.rs2.util.FileOperations;
import com.rs2.util.Stream;

public class GlobalGroundItem {
	
	public static void initialize() {
        int drops = 0;
		try {
			byte abyte2[] = FileOperations.ReadFile("./data/world/Item-spawn.dat");
			Stream stream2 = new Stream(abyte2);
			drops = stream2.readUnsignedWord();
			for (int i2 = 0; i2 < drops; i2++) {
				int id = stream2.readUnsignedWord();
				int amount = stream2.readUnsignedWord();
				int type = stream2.readUnsignedByte();
				int x = stream2.readUnsignedWord();
				int y = stream2.readUnsignedWord();
				int z = stream2.readUnsignedByte();
				if(Constants.F2P_CONTENT_ONLY && type == 1)
					continue;
				GroundItem item = new GroundItem(new Item(id, amount), new Position(x, y, z), true);
                GroundItemManager.getManager().dropItem(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Loaded " + drops + " global drops.");
	}

}
