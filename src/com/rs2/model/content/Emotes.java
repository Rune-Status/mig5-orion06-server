package com.rs2.model.content;

import com.rs2.model.players.Player;

/**
 * By Mikey` of Rune-Server
 */
public class Emotes {

	private Player player;

	public Emotes(Player player) {
		this.player = player;
	}

	private static final int[][] EMOTES = {{168, 855}, {169, 856}, {162, 857}, {164, 858}, {165, 859}, {161, 860}, {170, 861}, {171, 862}, {163, 863}, {167, 864}, {172, 865}, {166, 866}, {13362, 2105}, {13363, 2106}, {13364, 2107}, {13365, 2108}, {13366, 2109}, {13367, 2110}, {13368, 2111}, {13369, 2112}, {13370, 2113}, {11100, 0x558}, {667, 1131}, {6503, 1130}, {6506, 1129}, {666, 1128}, {13383, 2127}, {13384, 2128}, {15166, 2836}, {18464, 3544}, {18465, 3543}};

	public void performEmote(int emoteId) {
		player.getUpdateFlags().sendAnimation(emoteId, 0);
	}

	public boolean activateEmoteButton(int buttonId) {
		for (int[] element : EMOTES) {
			if (buttonId == element[0]) {
				performEmote(element[1]);
				return true;
			}
		}
		return false;
	}

}
