package com.rs2.model.content.consumables;

import com.rs2.util.FileOperations;
import com.rs2.util.Stream;

public class PotionLoader {
	
	public static void loadPotions() {
		int potionCount = 0;
        try {
			byte abyte2[] = FileOperations.ReadFile("./data/content/combat/potiondef.dat");
			Stream stream2 = new Stream(abyte2);
			potionCount = stream2.readUnsignedWord();
			for (int i2 = 0; i2 < potionCount; i2++) {
				PotionDefinition def = new PotionDefinition();
				String name = stream2.readString();
				int type = stream2.readUnsignedByte();
				def.potionName = name;
				def.potionType = PotionDefinition.PotionTypes.values()[type-1];
				int ri_1 = stream2.readUnsignedByte();
				def.potionIds = new int[ri_1];
				for (int i234 = 0; i234 < ri_1; i234++) {
					int item_ = stream2.readUnsignedWord();
					def.potionIds[i234] = item_;
				}
				int stats = stream2.readUnsignedByte();
				def.affectedStats = new int[stats];
				def.statAddons = new int[stats];
				def.statModifiers = new double[stats];
				for (int i235 = 0; i235 < stats; i235++) {
					int stat = stream2.readUnsignedByte();
					int addon = stream2.readUnsignedByte();
					double mod = stream2.readUnsignedWord();
					def.affectedStats[i235] = stat;
					def.statAddons[i235] = addon;
					def.statModifiers[i235] = mod/100;
				}
				Potion.getPotionDefinitions()[i2] = def;
				Potion.potionCount++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        System.out.println("Loaded " + Potion.potionCount + " potion definitions.");
	}

	public static class PotionDefinition {

		private String potionName;
		private PotionTypes potionType;
		private int[] potionIds;
		private int[] affectedStats;
		private int[] statAddons;
		private double[] statModifiers;

		public String getPotionName() {
			return potionName;
		}

		public int[] getPotionIds() {
			return potionIds;
		}

		public int[] getAffectedStats() {
			return affectedStats;
		}

		public int[] getStatAddons() {
			return statAddons;
		}

		public double[] getStatModifiers() {
			return statModifiers;
		}

		public PotionTypes getPotionType() {
			return potionType;
		}

		enum PotionTypes {
			BOOST, RESTORE
		}

	}
}
